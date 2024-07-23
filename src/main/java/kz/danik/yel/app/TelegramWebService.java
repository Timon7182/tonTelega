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

    @Autowired
    private DataManager dataManager;

    public List<TelegramTask> getActiveTelegramTasks(String hash){
        return dataManager.load(TelegramTask.class)
                .query("select e from yel_TelegramTask e")
                .fetchPlan(FetchPlan.BASE)
                .list();
    }

    public BigDecimal getBalance(String hash){
        return new BigDecimal(30000);
    }

}