package kz.danik.yel.app;

import io.jmix.core.impl.DataManagerImpl;
import kz.danik.yel.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.description.SetMyDescription;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.*;
import java.util.stream.Collectors;


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

//        telegramClient = new OkHttpTelegramClient("7390627968:AAHhrjWDt2Itr7af6JegVfZF2gtxdVFUILE");
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
        if (!isUUID(callBackValue[0])) {
            return;
        }

        String lang = callbackQuery.getFrom().getLanguageCode();
        sentWaitText(callBackValue, lang);

        UUID taskId = UUID.fromString(callBackValue[0]);
        TelegramUserTask telegramUserTask = telegramTaskService.getUserTaskById(taskId);

        if (telegramUserTask == null) {
            return;
        }

        if (!Boolean.TRUE.equals(telegramUserTask.getTask().getIsActive())) {
            sendMessage(getMessage("taskIsNotActive", lang), callbackQuery.getMessage().getChatId());
            return;
        }

        Platform platform = telegramUserTask.getTask().getPlatform();
        String text = "TO_PAY";
        boolean isToPay = true;

        if (platform != null && platform.equals(Platform.INSTAGRAM)) {
            if (toCheck(Platform.INSTAGRAM.getId())) {
                text = instagramService.checkTaskForComplete(telegramUserTask);
            }
            isToPay = text.contains("TO_PAY");
        }

        sentToPay(callbackQuery, callBackValue, text, lang, isToPay, telegramUserTask);
    }


    private void sentToPay(CallbackQuery callbackQuery, String[] callBackValue,
                           String text,
                           String lang, Boolean isToPay,
                           TelegramUserTask telegramUserTask) throws TelegramApiException {
        SendMessage request = new SendMessage(String.valueOf(callBackValue[1]), getMessage(text, lang));
        telegramClient.execute(request);

        if(isToPay){
            telegramTaskService.changeStatus(telegramUserTask,TaskStatus.DONE,PaymentStatus.TO_PAY);


            int msgId = ((Message) callbackQuery.getMessage()).getMessageId();

            DeleteMessage deleteMessage = new DeleteMessage(
                    String.valueOf(callbackQuery.getMessage().getChatId())
                    ,msgId);
            telegramClient.execute(deleteMessage);
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
                showStart(chatId,message.getFrom());
                break;
            case "/help":
                showHelpMessage(chatId,message.getFrom().getLanguageCode());
                break;
            case "/tasks":
                showTasks(chatId,message.getFrom());
                break;
            case "/done":
                showDoneTasks(chatId,message.getFrom());
                break;
            case "/admin":
                showAdminCmdList(chatId,message.getFrom());
                break;
            case "/admtasks":
                showAdminTasks(chatId,message.getFrom());
                break;
            case "/needpay":
                needPayShow(chatId,message.getFrom());
                break;
            case "/refresh":
                refreshValues(chatId,message.getFrom());
                break;
            default:
                handleInstagramUsername(chatId,message.getFrom().getId(), text,message.getFrom().getLanguageCode());
                break;
        }
    }

    private void refreshValues(long chatId, User from) throws TelegramApiException {
        if (isAdmin(from,chatId).equals(Boolean.FALSE))
            return;

        SetMyDescription setMyDescriptionEn = new SetMyDescription();
        setMyDescriptionEn.setDescription(getMessage("description","en"));
        setMyDescriptionEn.setLanguageCode("en");

        SetMyDescription setMyDescriptionRu = new SetMyDescription();
        setMyDescriptionRu.setDescription(getMessage("description","ru"));
        setMyDescriptionRu.setLanguageCode("ru");

        telegramClient.execute(setMyDescriptionEn);
        telegramClient.execute(setMyDescriptionRu);
    }

    private void showStart(long chatId, User from) throws TelegramApiException {
        TelegramUser telegramUser = telegramUserService.getOrCreateTelegramUserById(from,chatId);
        String text = String.format(getMessage("/start",from.getLanguageCode()),telegramUser.getUsername());
        sendMessage(text,chatId);
    }

    protected void needPayShow(long chatId, User from) throws TelegramApiException {

        if(isAdmin(from,chatId).equals(Boolean.FALSE))
            return;

        List<TelegramUserTask> telegramUserTasks = telegramTaskService
                .getActiveTelegramUserTasksByPaymentStatus(PaymentStatus.TO_PAY);

        if(telegramUserTasks.isEmpty()){
            sendMessage("Нет выплат",chatId);
            return;
        }

        StringBuilder result = new StringBuilder();

        Map<String, Double> currencyTotals = new HashMap<>();
        int userCount = 0;

        for (TelegramUserTask task : telegramUserTasks) {
            String username = task.getUser().getUsername();
            double prizeAmount = task.getTask().getPrize();
            String currency = task.getTask().getCurrency();

            result.append(String.format("@%s %.2f %s\n", username, prizeAmount, currency));

            currencyTotals.put(currency, currencyTotals.getOrDefault(currency, 0.0) + prizeAmount);
        }

        result.append("\nОбщие данные:\n");
        for (Map.Entry<String, Double> entry : currencyTotals.entrySet()) {
            result.append(String.format("В общем %s: %.2f\n", entry.getKey(), entry.getValue()));
        }


        String finalResult = result.toString();

        sendMessage(finalResult,chatId);
    }

    private void showAdminCmdList(long chatId, User from) throws TelegramApiException {
        if(isAdmin(from,chatId).equals(Boolean.FALSE))
            return;
        String cmds ="/admtasks - Статистика задач\n" +
                " /needpay - Список кому нужно оплатить";

        sendMessage(cmds,chatId);
    }

    private Boolean isAdmin(User user,long chatId){
        TelegramUser telegramUser = telegramUserService.getOrCreateTelegramUserById(user,chatId);
        return telegramUser.getIsAdmin();
    }

    private void showAdminTasks(long chatId, User from) throws TelegramApiException {

        if(isAdmin(from,chatId).equals(Boolean.FALSE))
            return;

        List<TelegramUserTask> telegramUserTasks = telegramTaskService.getActiveTelegramUserTasks();
        String lang = from.getLanguageCode();

        if(telegramUserTasks.isEmpty()){
            sendMessage(getMessage("noTasksAdmin",lang),chatId);
            return;
        }

        List<TelegramUserTask> doneTasks = telegramUserTasks.stream()
                .filter(e->e.getStatus().equals(TaskStatus.DONE))
                .collect(Collectors.toList());

        int all = telegramUserTasks.size();
        int done = doneTasks.size();

        String needToPay = getCountFromTaskList(doneTasks,PaymentStatus.TO_PAY);
        String paidCount = getCountFromTaskList(doneTasks,PaymentStatus.PAID);

        String doneMsgTemplate = getMessage("doneAdm",lang);
        String doneMsg = doneMsgTemplate
                .replace("${needToPay}", needToPay)
                .replace("${paidCount}", paidCount)
                .replace("${done}", String.valueOf(done))
                .replace("${all}", String.valueOf(all));

        sendMessage(doneMsg,chatId);


    }

    protected void showHelpMessage(long chatId,String lang) throws TelegramApiException {
        sendMessage(getMessage("help",lang),chatId);
    }

    protected void sendMessage(String message,long chatId) throws TelegramApiException {
        SendMessage request = new SendMessage(String.valueOf(chatId),message);
        telegramClient.execute(request);
    }

    protected String getMessage(String code, String lang){

        Settings settings = settingsService.getSettingsByCode(code);
        lang = lang.toLowerCase();

        if(settings == null)
            return code;

        if(lang.equals("ru")){
            return settings.getRuValue();
        }else{
            return settings.getEnValue();
        }
    }



    protected void showDoneTasks(long chatId, User from) throws TelegramApiException {

        List<TelegramUserTask> telegramTasks = telegramUserService
                .getOrCreateTelegramTasksByUserId(from,chatId);
        String lang = from.getLanguageCode();

        if(telegramTasks.isEmpty()){
            sendMessage(getMessage("noTasks",lang),chatId);
            return;
        }
        List<TelegramUserTask> doneTasks = telegramTasks.stream()
                .filter(e->e.getStatus().equals(TaskStatus.DONE))
                .collect(Collectors.toList());

        int all = telegramTasks.size();
        int done = doneTasks.size();

        String needToPay = getCountFromTaskList(doneTasks,PaymentStatus.TO_PAY);
        String paidCount = getCountFromTaskList(doneTasks,PaymentStatus.PAID);

        String doneMsgTemplate = getMessage("done",lang);
        String doneMsg = doneMsgTemplate
                .replace("${needToPay}", needToPay)
                .replace("${paidCount}", paidCount)
                .replace("${done}", String.valueOf(done))
                .replace("${all}", String.valueOf(all));

        sendMessage(doneMsg,chatId);

    }

    private String getCountFromTaskList(List<TelegramUserTask> doneTasks, PaymentStatus paymentStatus) {
        if(doneTasks.isEmpty())
            return " 0";

        Map<String, Double> prizeSummary = doneTasks.stream()
                .filter(e->e.getPaymentStatus() != null  && e.getPaymentStatus().equals(paymentStatus))
                .collect(Collectors.groupingBy(e -> e.getTask().getCurrency(),
                        Collectors.summingDouble(e -> e.getTask().getPrize())));

        String result = prizeSummary.entrySet().stream()
                .map(entry -> entry.getValue() + " " + entry.getKey())
                .collect(Collectors.joining(", "));

        return result;
    }

    protected void showTasks(long chatId,User user) throws TelegramApiException {
        telegramUserService.getOrCreateTelegramUserById(user,chatId);

        List<TelegramUserTask> telegramTasks = telegramUserService
                .getOrCreateTelegramTasksByUserId(user,chatId)
                .stream()
                .filter(e->!e.getStatus().equals(TaskStatus.DONE)
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

            if(telegramTask.getType().equals(TaskType.FOLLOW) || telegramTask.getType().equals(TaskType.LINK)){
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