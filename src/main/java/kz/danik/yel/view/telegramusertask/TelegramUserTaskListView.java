package kz.danik.yel.view.telegramusertask;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;
import kz.danik.yel.entity.TelegramUserTask;
import kz.danik.yel.view.main.MainView;

@Route(value = "telegramUserTasks", layout = MainView.class)
@ViewController("yel_TelegramUserTask.list")
@ViewDescriptor("telegram-user-task-list-view.xml")
@LookupComponent("telegramUserTasksDataGrid")
@DialogMode(width = "64em")
public class TelegramUserTaskListView extends StandardListView<TelegramUserTask> {
}