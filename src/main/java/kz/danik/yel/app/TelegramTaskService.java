package kz.danik.yel.app;

import io.jmix.core.DataManager;
import io.jmix.core.FetchPlan;
import io.jmix.core.security.Authenticated;
import kz.danik.yel.entity.TelegramTask;
import kz.danik.yel.entity.TelegramUserTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component("yel_TelegramTaskService")
public class TelegramTaskService {


    @Autowired
    private DataManager dataManager;

    @Authenticated
    List<TelegramTask> getActiveTelegramTasks(){
       return dataManager.load(TelegramTask.class)
                .query("select e from yel_TelegramTask e")
               .fetchPlan(FetchPlan.BASE)
               .list();
    }

    @Authenticated
    TelegramUserTask getUserTaskById(UUID id){
        return dataManager.load(TelegramUserTask.class)
                .id(id)
                .fetchPlan("telegramUserTask-withTask-fetch-plan")
                .optional().orElse(null);
    }
}