package kz.danik.yel.listener;

import io.jmix.core.FetchPlan;
import io.jmix.core.SaveContext;
import io.jmix.core.impl.DataManagerImpl;
import kz.danik.yel.entity.TelegramTask;
import io.jmix.core.event.EntityChangedEvent;
import kz.danik.yel.entity.TelegramUser;
import kz.danik.yel.entity.TelegramUserTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component("yel_TelegramTaskEventListener")
public class TelegramTaskEventListener {

    @Autowired
    private DataManagerImpl dataManager;

    @TransactionalEventListener
    public void onTelegramTaskChangedAfterCommit(final EntityChangedEvent<TelegramTask> event) {
        if (!event.getType().equals(EntityChangedEvent.Type.CREATED))
            return;

        TelegramTask task = dataManager.load(TelegramTask.class)
                .id(event.getEntityId().getValue())
                .joinTransaction(false)
                .fetchPlan(FetchPlan.LOCAL)
                .one();

        List<TelegramUser> telegramUserList =  dataManager.load(TelegramUser.class)
                .query("select e from yel_TelegramUser e where e.level = :level")
                .parameter("level",task.getLevel().getId())
                .joinTransaction(false)
                .list();

        for (TelegramUser telegramUser : telegramUserList) {
            TelegramUserTask telegramUserTask = dataManager.create(TelegramUserTask.class);
            telegramUserTask.setUser(telegramUser);
            telegramUserTask.setTask(task);
            telegramUserTask.setDateTimeFrom(task.getDateTimeFrom());
            telegramUserTask.setDateTimeTo(task.getDateTimeTo());
            dataManager.save(new SaveContext()
                            .saving(telegramUserTask)
                            .setJoinTransaction(false));
        }

    }
}