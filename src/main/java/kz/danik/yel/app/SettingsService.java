package kz.danik.yel.app;

import io.jmix.core.FetchPlan;
import io.jmix.core.impl.DataManagerImpl;
import io.jmix.core.security.Authenticated;
import kz.danik.yel.entity.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("yel_SettingsService")
public class SettingsService {

    @Autowired
    private DataManagerImpl dataManager;

    @Authenticated
    Settings getSettingsByCode(String code){
        return dataManager.load(Settings.class)
                .query("select e from yel_Settings e where e.code =:code")
                .parameter("code",code)
                .fetchPlan(FetchPlan.LOCAL)
                .optional().orElse(null);
    }
}