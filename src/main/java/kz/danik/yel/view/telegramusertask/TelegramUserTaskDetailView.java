package kz.danik.yel.view.telegramusertask;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import kz.danik.yel.entity.TelegramUserTask;
import kz.danik.yel.view.main.MainView;

@Route(value = "telegramUserTasks/:id", layout = MainView.class)
@ViewController("yel_TelegramUserTask.detail")
@ViewDescriptor("telegram-user-task-detail-view.xml")
@EditedEntityContainer("telegramUserTaskDc")
public class TelegramUserTaskDetailView extends StandardDetailView<TelegramUserTask> {
}