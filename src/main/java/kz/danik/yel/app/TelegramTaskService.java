package kz.danik.yel.app;

import io.jmix.core.DataManager;
import io.jmix.core.FetchPlan;
import io.jmix.core.security.Authenticated;
import kz.danik.yel.entity.PaymentStatus;
import kz.danik.yel.entity.TaskStatus;
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
    List<TelegramUserTask> getActiveTelegramUserTasks(){
        return dataManager.load(TelegramUserTask.class)
                .query("select e from yel_TelegramUserTask e")
                .fetchPlan("telegramUserTask-withTask-fetch-plan")
                .list();
    }

    @Authenticated
    List<TelegramUserTask> getActiveTelegramUserTasksByPaymentStatus(PaymentStatus paymentStatus){
        return dataManager.load(TelegramUserTask.class)
                .query("select e from yel_TelegramUserTask e where e.paymentStatus =:paymentStatus")
                .parameter("paymentStatus",paymentStatus.getId())
                .fetchPlan("telegramUserTask-withTask-fetch-plan")
                .list();
    }

    @Authenticated
    void changeStatus(TelegramUserTask userTask,TaskStatus status,PaymentStatus paymentStatus){
        userTask.setStatus(status);
        userTask.setPaymentStatus(paymentStatus);
        dataManager.save(userTask);
    }

    @Authenticated
    TelegramUserTask getUserTaskById(UUID id){
        return dataManager.load(TelegramUserTask.class)
                .id(id)
                .fetchPlan("telegramUserTask-withTask-fetch-plan")
                .optional().orElse(null);
    }
}