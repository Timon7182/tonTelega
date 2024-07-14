package kz.danik.yel.view.telegramtask;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.component.checkbox.JmixCheckbox;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import kz.danik.yel.entity.TelegramTask;
import kz.danik.yel.entity.TelegramUser;
import kz.danik.yel.entity.TelegramUserTask;
import kz.danik.yel.view.main.MainView;
import kz.danik.yel.view.telegramuser.TelegramUserListView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Route(value = "telegramTasks/:id", layout = MainView.class)
@ViewController("yel_TelegramTask.detail")
@ViewDescriptor("telegram-task-detail-view.xml")
@EditedEntityContainer("telegramTaskDc")
public class TelegramTaskDetailView extends StandardDetailView<TelegramTask> {

    @Autowired
    private DialogWindows dialogWindows;
    @ViewComponent
    private InstanceContainer<TelegramTask> telegramTaskDc;
    @Autowired
    private DataManager dataManager;
    @ViewComponent
    private DataContext dataContext;
    @ViewComponent
    private CollectionPropertyContainer<TelegramUserTask> userTasksDc;
    @ViewComponent
    private JmixCheckbox isToEveryoneField;

    @Subscribe("userTasksDataGrid.create")
    public void onUserTasksDataGridCreate(final ActionPerformedEvent event) {

        DialogWindow telegramUserView = dialogWindows.lookup(this, TelegramUser.class)
                .withViewClass(TelegramUserListView.class)
                .withSelectHandler(e->{
                    e.forEach(l->{
                        TelegramUserTask telegramUserTask = dataManager.create(TelegramUserTask.class);
                        telegramUserTask.setUser(l);
                        telegramUserTask.setTask(getEditedEntity());

                        userTasksDc.getMutableItems().add(telegramUserTask);

                        dataContext.merge(telegramUserTask);
                    });
                }).build();

        ((TelegramUserListView)telegramUserView.getView()).setIdsToHide(getUserIds());
        telegramUserView.open();
    }

    protected List<UUID> getUserIds(){
        if(telegramTaskDc.getItem().getUserTasks() == null)
            return  new ArrayList<>();

        return telegramTaskDc.getItem().getUserTasks().stream().map(e->e.getUser().getId()).toList();
    }

    @Subscribe(id = "isToEveryoneField", subject = "clickListener")
    public void onIsToEveryoneFieldClick(final ClickEvent<JmixCheckbox> event) {
        Boolean isToEveyrone = isToEveryoneField.getValue();

        if(isToEveyrone.equals(Boolean.FALSE))
            return;
        List<TelegramUser> allUsers = getAllUsers();

        for (TelegramUser user : allUsers) {
            TelegramUserTask telegramUserTask = dataManager.create(TelegramUserTask.class);
            telegramUserTask.setUser(user);
            telegramUserTask.setTask(getEditedEntity());

            userTasksDc.getMutableItems().add(telegramUserTask);

            dataContext.merge(telegramUserTask);

        }

    }

    private List<TelegramUser> getAllUsers() {
        List<UUID> idsToExclude =getUserIds();

        return dataManager.load(TelegramUser.class)
                .query("select e from yel_TelegramUser e where e.id not in :idsToExclude")
                .parameter("idsToExclude",idsToExclude)
                .list();
    }


}