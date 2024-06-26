package kz.danik.yel.app;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component("yel_TelegramService")
public class TelegramService {

    public void isGroupMember(TelegramClient telegramClient, User user,long chatId) throws TelegramApiException {
        GetChatMember getChatMember = new GetChatMember(String.valueOf(chatId),user.getId());
        telegramClient.execute(getChatMember);
    }
}