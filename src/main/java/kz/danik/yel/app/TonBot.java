package kz.danik.yel.app;

import io.jmix.core.impl.DataManagerImpl;
import kz.danik.yel.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component("yel_TonBot")
public class TonBot implements LongPollingSingleThreadUpdateConsumer {


    protected static TelegramClient telegramClient;


    @Autowired
    TelegramUserService telegramUserService;
    @Autowired
    protected TelegramTaskService telegramTaskService;
    @Autowired
    protected InstagramService instagramService;
    @Autowired
    private TelegramService telegramService;
    @Autowired
    private SettingsService settingsService;
    @Autowired
    private DataManagerImpl core_DataManager;


    public  void initBot(){
        telegramClient = new OkHttpTelegramClient("7062923943:AAFwL-88vIo3Us_HK-64MGsMYmQl--Nbgr8");
    }
    @Override
    public void consume(Update update) {
        try {
            if (update.hasMessage()) {
                Message message = update.getMessage();
                if (message.hasText()) {
                    handleIncomingMessage(message);
                }
            }else if(update.hasCallbackQuery()){
                if(update.getCallbackQuery().getData().equals("NONE"))
                    return;
                handleCallback(update.getCallbackQuery());
            }
        } catch (Exception e) {
            log.error("TonTelegram Handler error", e);
        }
    }

    protected void handleCallback(CallbackQuery callbackQuery) throws Exception {
        String[] callBackValue = callbackQuery.getData().split(":");
        boolean  isUUID = isUUID(callBackValue[0]);
        if(isUUID){

            String lang = callbackQuery.getFrom().getLanguageCode();
            sentWaitText(callBackValue,lang);

            TelegramUserTask telegramUserTask = telegramTaskService
                    .getUserTaskById(UUID.fromString(callBackValue[0]));

            if(telegramUserTask == null){
                return;
            }
            Platform platform = telegramUserTask.getTask().getPlatform();

            if(platform == null){

                String text = "TO_PAY";
                sentToPay(callbackQuery, callBackValue, text, lang, true, telegramUserTask);

            } else if(platform.equals(Platform.INSTAGRAM)){
                Boolean isToPay= false;

                String text = "TO_PAY";

                if(toCheck(Platform.INSTAGRAM.getId())){
                    text = instagramService.checkTaskForComplete(telegramUserTask);
                }
                if(text.contains("TO_PAY"))
                    isToPay=true;

                sentToPay(callbackQuery, callBackValue, text, lang, isToPay, telegramUserTask);
            }


        }
    }

    private void sentToPay(CallbackQuery callbackQuery, String[] callBackValue, String text, String lang, Boolean isToPay, TelegramUserTask telegramUserTask) throws TelegramApiException {
        SendMessage request = new SendMessage(String.valueOf(callBackValue[1]), getMessage(text, lang));
        telegramClient.execute(request);

        if(isToPay){
            telegramUserTask.setStatus(TaskStatus.TO_PAY);
            core_DataManager.save(telegramUserTask);

            EditMessageText newMessage = new EditMessageText(((Message) callbackQuery.getMessage()).getText()
                    + String.format(" (%s) ",getMessage("DONE", lang)));
            newMessage.setChatId(callbackQuery.getMessage().getChatId());
            newMessage.setMessageId(callbackQuery.getMessage().getMessageId());
            telegramClient.execute(newMessage);
        }
    }

    protected void sentWaitText(String[] callBackValue, String languageCode) throws TelegramApiException {
        String waitText = getMessage("WAIT",languageCode);
        SendMessage request = new SendMessage(String.valueOf(callBackValue[1]), waitText);
        telegramClient.execute(request);
    }

    protected boolean isUUID(String data) {
        try{
            UUID id = UUID.fromString(data);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    protected void handleIncomingMessage(Message message) throws TelegramApiException {
        long chatId = message.getChatId();
        String text = message.getText();


        switch (text) {
            case "/start":
                showStartMsg(chatId,message.getFrom());
                break;
            case "/help":
                showBalance(chatId);
                break;
            case "/tasks":
                showTasks(chatId,message.getFrom());
                break;
            default:
                handleInstagramUsername(chatId,message.getFrom().getId(), text,message.getFrom().getLanguageCode());
                break;
        }
    }

    private void showStartMsg(long chatId, User user) throws TelegramApiException {
        telegramUserService.getOrCreateTelegramUserById(user,chatId);

        String text = getMessage("start",user.getLanguageCode());

        SendMessage request = new SendMessage(String.valueOf(chatId),text);
        telegramClient.execute(request);
    }

    protected String getMessage(String code, String lang){

        Settings settings = settingsService.getSettingsByCode(code);
        lang = lang.toLowerCase();

        if(lang.equals("ru")){
            return settings.getRuValue();
        }else{
            return settings.getEnValue();
        }
    }

    protected void showBalance(long chatId) throws TelegramApiException {
        SendMessage request = new SendMessage(String.valueOf(chatId), "Your balance is: [balance]");
        telegramClient.execute(request);
    }

    protected void showTasks(long chatId,User user) throws TelegramApiException {
        List<TelegramUserTask> telegramTasks = telegramUserService
                .getOrCreateTelegramTasksByUserId(user,chatId)
                .stream()
                .filter(e->e.getStatus().equals(TaskStatus.IN_PROGRESS)
                        && e.getTask().getIsActive().equals(Boolean.TRUE))
                .toList();

        if(telegramTasks.isEmpty()){
            SendMessage message = new SendMessage(String.valueOf(chatId),
                    getMessage("noTasks",user.getLanguageCode()));
            telegramClient.execute(message);
            return;
        }else{
            SendMessage message = new SendMessage(String.valueOf(chatId),
                    String.format(getMessage("yesTasks",user.getLanguageCode()),telegramTasks.size()));
            telegramClient.execute(message);
        }

        int i =1;

        for (TelegramUserTask telegramUserTask : telegramTasks) {

            TelegramTask telegramTask = telegramUserTask.getTask();

            //"%s. %s. Приз: %s %s"
            String text = String.format(getMessage("taskName",user.getLanguageCode()), i, telegramTask.getTaskName(),
                    telegramTask.getPrize(),telegramTask.getCurrency());
            SendMessage request = new SendMessage(String.valueOf(chatId),text);

            if(telegramTask.getType().equals(TaskType.FOLLOW)){
                InlineKeyboardButton followButton = new InlineKeyboardButton(getMessage("ACCOMPLISH", user.getLanguageCode()));
                followButton.setUrl(telegramTask.getTaskUrl());

                List<InlineKeyboardRow> keyboardRows = new ArrayList<>();
                InlineKeyboardRow inlineKeyboardRow = new InlineKeyboardRow();
                inlineKeyboardRow.add(followButton);

                InlineKeyboardButton checkFollowButton = new InlineKeyboardButton(getMessage("CHECK", user.getLanguageCode()));
                checkFollowButton.setCallbackData(telegramUserTask.getId().toString() +":"+chatId);
                inlineKeyboardRow.add(checkFollowButton);


                keyboardRows.add(inlineKeyboardRow);
                InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(keyboardRows);
                request.setReplyMarkup(keyboard);
            }

            telegramClient.execute(request);
            i++;
        }


    }

    protected boolean toCheck(String id) {
        Settings settings = settingsService.getSettingsByCode(id);
        return settings.getToCheck();
    }


    protected void handleInstagramUsername(long chatId, long userId, String username, String languageCode) throws TelegramApiException {
        String text ="";
        try{
            if(username.contains("@")){
                String usernameFinal = instagramService.getUsernameAfterAtSymbol(username);
                telegramUserService.saveInstagramNickForUser(userId,usernameFinal);
                text = getMessage("INSTAGRAM_ADDED",languageCode);
                //"Инстаграм аккаунт привязан. Можете выполнить задание :D"
            }else{
                text=getMessage("UNKNOWN",languageCode);
                //Неверная команда, попробуй выбрать из Списка Меню или через /
            }
        }catch (Exception exception){
            log.error(String.format("%s, %s", ExceptionUtils.getMessage(exception),
                    ExceptionUtils.getStackTrace(exception)));
            text=getMessage("ERROR",languageCode);
        }

        SendMessage request = new SendMessage(String.valueOf(chatId), text);
        telegramClient.execute(request);
    }

    public  static void  sendMessageToChat(String chatId,String text) throws TelegramApiException {
        SendMessage request = new SendMessage(String.valueOf(chatId), text);
        telegramClient.execute(request);
    }

}