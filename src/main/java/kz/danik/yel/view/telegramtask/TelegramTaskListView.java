package kz.danik.yel.view.telegramtask;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;
import kz.danik.yel.entity.TelegramTask;
import kz.danik.yel.view.main.MainView;

@Route(value = "telegramTasks", layout = MainView.class)
@ViewController("yel_TelegramTask.list")
@ViewDescriptor("telegram-task-list-view.xml")
@LookupComponent("telegramTasksDataGrid")
@DialogMode(width = "64em")
public class TelegramTaskListView extends StandardListView<TelegramTask> {
}