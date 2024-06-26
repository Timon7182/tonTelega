package kz.danik.yel.view.telegramuser;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;
import kz.danik.yel.entity.TelegramUser;
import kz.danik.yel.view.main.MainView;

@Route(value = "telegramUsers", layout = MainView.class)
@ViewController("yel_TelegramUser.list")
@ViewDescriptor("telegram-user-list-view.xml")
@LookupComponent("telegramUsersDataGrid")
@DialogMode(width = "64em")
public class TelegramUserListView extends StandardListView<TelegramUser> {
}