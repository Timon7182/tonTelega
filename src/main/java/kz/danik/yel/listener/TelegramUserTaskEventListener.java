package kz.danik.yel.listener;

import io.jmix.core.FetchPlan;
import io.jmix.core.impl.DataManagerImpl;
import kz.danik.yel.app.TonBot;
import kz.danik.yel.entity.PaymentStatus;
import kz.danik.yel.entity.TaskStatus;
import kz.danik.yel.entity.TelegramUser;
import kz.danik.yel.entity.TelegramUserTask;
import io.jmix.core.event.EntityChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Component("yel_TelegramUserTaskEventListener")
public class TelegramUserTaskEventListener {

    @Autowired
    private DataManagerImpl dataManager;

    @TransactionalEventListener
    public void onTelegramUserTaskChangedAfterCommit(final EntityChangedEvent<TelegramUserTask> event) throws TelegramApiException {
//        if(event.getType().equals(EntityChangedEvent.Type.CREATED)){
//
//            TelegramUserTask telegramUserTask = dataManager.load(TelegramUserTask.class)
//                    .id(event.getEntityId().getValue())
//                    .joinTransaction(false)
//                    .fetchPlan("telegramUserTask-withTask-fetch-plan")
//                    .one();
//            try {
//                if(telegramUserTask.getTask().getIsActive().equals(Boolean.FALSE)
//                        || telegramUserTask.getToNotify().equals(Boolean.FALSE))
//                    return;
//
//                TonBot.sendMessageToChat(String.valueOf(telegramUserTask.getUser().getChatId().longValue()),
//                        "У вас появилась новая задача :D");
//            } catch (TelegramApiException e) {
//                log.error(String.format("%s, %s", ExceptionUtils.getMessage(e),
//                        ExceptionUtils.getStackTrace(e)));
//                throw new RuntimeException(e);
//            }
//        }
    }

    protected void sendNotificationToAdminsToPay(TelegramUserTask telegramUserTask) throws TelegramApiException {
        List<TelegramUser> telegramUserList = dataManager.load(TelegramUser.class)
                .query("select e from yel_TelegramUser e where e.isAdmin = true")
                .joinTransaction(false)
                .fetchPlan(FetchPlan.LOCAL)
                .list();

        for (TelegramUser telegramUser : telegramUserList) {
            TonBot.sendMessageToChat(String.valueOf(telegramUser.getChatId().longValue()),
                    String.format("Нужно выплатить приз: %s, %s. Задание: %s","@"+telegramUserTask.getUser().getUsername(),
                            telegramUserTask.getTask().getPrize(),
                            telegramUserTask.getTask().getTaskName()));
        }
    }
}