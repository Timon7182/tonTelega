package kz.danik.yel.app;

import io.jmix.core.DataManager;
import io.jmix.core.FetchPlan;
import io.jmix.core.security.Authenticated;
import kz.danik.yel.entity.TelegramTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("yel_TelegramWebService")
public class TelegramWebService {

    private final TelegramAuth telegramAuth;
    @Autowired
    private DataManager dataManager;

    public TelegramWebService(TelegramAuth telegramAuth) {
        this.telegramAuth = telegramAuth;
    }

    public List<TelegramTask> getActiveTelegramTasks(String hash){
        return dataManager.load(TelegramTask.class)
                .query("select e from yel_TelegramTask e")
                .fetchPlan(FetchPlan.BASE)
                .list();
    }

    public BigDecimal getBalance(String user,
                                 String chat_instance,
                                 String chat_type,
                                 String auth_date,
                                 String hash){
        try{
            telegramAuth.authenticate(user,chat_instance,chat_type, auth_date, hash);
            return new BigDecimal(30000);
        }catch (Exception exception){
            return new BigDecimal(0);
        }

    }

}