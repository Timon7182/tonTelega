package kz.danik.yel.view.telegramusertask;


import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.view.*;
import kz.danik.yel.entity.TelegramUserTask;
import kz.danik.yel.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;



@Route(value = "telegramUserTasks", layout = MainView.class)
@ViewController("yel_TelegramUserTask.list")
@ViewDescriptor("telegram-user-task-list-view.xml")
@LookupComponent("telegramUserTasksDataGrid")
@DialogMode(width = "64em")
public class TelegramUserTaskListView extends StandardListView<TelegramUserTask> {

    @ViewComponent
    private DataGrid<TelegramUserTask> telegramUserTasksDataGrid;
    @Autowired
    private UiComponents uiComponents;
    private int counter;

    @Subscribe(id = "telegramUserTasksDc", target = Target.DATA_CONTAINER)
    public void onTelegramUserTasksDcCollectionChange(final CollectionContainer.CollectionChangeEvent<TelegramUserTask> event) {
        counter=0;
    }


    @Supply(to = "telegramUserTasksDataGrid.numeration", subject = "renderer")
    private Renderer<TelegramUserTask> telegramUserTasksDataGridNumerationRenderer() {
        return new ComponentRenderer<>(
                () -> {
                    TextField textField = uiComponents.create(TextField.class);
                    textField.setEnabled(false);
                    return textField;
                },
                (textField, task) -> textField.setValue(String.valueOf(counter+=1))
        );    }




}