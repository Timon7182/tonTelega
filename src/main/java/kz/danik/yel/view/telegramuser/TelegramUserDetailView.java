package kz.danik.yel.view.telegramuser;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import kz.danik.yel.entity.TelegramUser;
import kz.danik.yel.view.main.MainView;

@Route(value = "telegramUsers/:id", layout = MainView.class)
@ViewController("yel_TelegramUser.detail")
@ViewDescriptor("telegram-user-detail-view.xml")
@EditedEntityContainer("telegramUserDc")
public class TelegramUserDetailView extends StandardDetailView<TelegramUser> {
}