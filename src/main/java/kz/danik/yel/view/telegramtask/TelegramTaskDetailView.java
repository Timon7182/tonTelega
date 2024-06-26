package kz.danik.yel.view.telegramtask;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import kz.danik.yel.entity.TelegramTask;
import kz.danik.yel.view.main.MainView;

@Route(value = "telegramTasks/:id", layout = MainView.class)
@ViewController("yel_TelegramTask.detail")
@ViewDescriptor("telegram-task-detail-view.xml")
@EditedEntityContainer("telegramTaskDc")
public class TelegramTaskDetailView extends StandardDetailView<TelegramTask> {
}