package kz.danik.yel.view.telegramuser;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import kz.danik.yel.entity.TelegramUser;
import kz.danik.yel.view.main.MainView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Route(value = "telegramUsers", layout = MainView.class)
@ViewController("yel_TelegramUser.list")
@ViewDescriptor("telegram-user-list-view.xml")
@LookupComponent("telegramUsersDataGrid")
@DialogMode(width = "64em")
public class TelegramUserListView extends StandardListView<TelegramUser> {


    List<UUID> idsToExclude = new ArrayList<>();
    @ViewComponent
    private CollectionLoader<TelegramUser> telegramUsersDl;

    public void setIdsToHide(List<UUID> ids){
        idsToExclude=ids;
    }

    @Subscribe(id = "telegramUsersDl", target = Target.DATA_LOADER)
    public void onTelegramUsersDlPreLoad(final CollectionLoader.PreLoadEvent<TelegramUser> event) {
        telegramUsersDl.setParameter("idsToExclude", idsToExclude);
    }



}