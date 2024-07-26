package kz.danik.yel.app;

import io.jmix.core.DataManager;
import io.jmix.core.FetchPlan;
import io.jmix.core.SaveContext;
import io.jmix.core.security.Authenticated;
import kz.danik.yel.entity.TaskStatus;
import kz.danik.yel.entity.TelegramTask;
import kz.danik.yel.entity.TelegramUser;
import kz.danik.yel.entity.TelegramUserTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Component("yel_TelegramUserService")
public class TelegramUserService {


    private static volatile TelegramUserService instance;


    @Autowired
    private DataManager dataManager;

    @Autowired
    private TelegramTaskService telegramTaskService;

    @Authenticated
    public List<TelegramUserTask> getOrCreateTelegramTasksByUserId(User user,long chatid) {
        TelegramUser telegramUser = getOrCreateTelegramUserById(user,chatid);
        return telegramUser.getTasks();
    }
    @Authenticated
    public TelegramUser getOrCreateTelegramUserById(User user,long chatid) {


        TelegramUser telegramUser = dataManager.load(TelegramUser.class)
                .query("select e from yel_TelegramUser e where e.userid =:userId")
                .parameter("userId", user.getId())
                .fetchPlan("telegramUser-full-fetch-plan")
                .optional().orElse(null);
        if(telegramUser == null){

            SaveContext saveContext = new SaveContext();

            telegramUser = dataManager.create(TelegramUser.class);
            telegramUser.setFirstName(user.getFirstName());
            telegramUser.setLastName(user.getLastName());
            telegramUser.setUserid(BigInteger.valueOf(user.getId()));
            telegramUser.setUsername(user.getUserName());
            telegramUser.setChatId(BigInteger.valueOf(chatid));
            telegramUser.setState(0);

            List<TelegramTask> tasks = telegramTaskService.getActiveTelegramTasks();
            List<TelegramUserTask> telegramUserTasks = new ArrayList<>();
            for (TelegramTask task : tasks) {
                if((task.getIsToEveryone() != null &&  task.getIsToEveryone().equals(Boolean.TRUE))
                        || (task.getIsToSendToNew() != null && task.getIsToSendToNew().equals(Boolean.TRUE))){
                    TelegramUserTask telegramUserTask = dataManager.create(TelegramUserTask.class);
                    telegramUserTask.setUser(telegramUser);
                    telegramUserTask.setTask(task);
                    telegramUserTask.setStatus(TaskStatus.IN_PROGRESS);
                    telegramUserTask.setToNotify(false);
                    saveContext.saving(telegramUserTask);
                    telegramUserTasks.add(telegramUserTask);
                }
            }

            telegramUser.setTasks(telegramUserTasks);
            saveContext.saving(telegramUser);


            dataManager.save(saveContext);
            return telegramUser;
        }
        return telegramUser;
    }



    public TelegramUser getTelegramUserInfo(String userid,
                                            String firstname,
                                            String lastName,
                                            String username,
                                            long chatid) {

        TelegramUser telegramUser = dataManager.load(TelegramUser.class)
                .query("select e from yel_TelegramUser e where e.userid =:userId")
                .parameter("userId", Long.parseLong(userid))
                .fetchPlan("telegramUser-full-fetch-plan")
                .optional().orElse(null);
        if(telegramUser == null){

            SaveContext saveContext = new SaveContext();

            telegramUser = dataManager.create(TelegramUser.class);
            telegramUser.setFirstName(firstname);
            telegramUser.setLastName(lastName);
            telegramUser.setUserid(BigInteger.valueOf(Long.parseLong(userid)));
            telegramUser.setUsername(username);
            telegramUser.setChatId(BigInteger.valueOf(Long.parseLong(userid)));
            telegramUser.setState(0);

            List<TelegramTask> tasks = telegramTaskService.getActiveTelegramTasks();
            List<TelegramUserTask> telegramUserTasks = new ArrayList<>();
            for (TelegramTask task : tasks) {
                if((task.getIsToEveryone() != null &&  task.getIsToEveryone().equals(Boolean.TRUE))
                        || (task.getIsToSendToNew() != null && task.getIsToSendToNew().equals(Boolean.TRUE))){
                    TelegramUserTask telegramUserTask = dataManager.create(TelegramUserTask.class);
                    telegramUserTask.setUser(telegramUser);
                    telegramUserTask.setTask(task);
                    telegramUserTask.setStatus(TaskStatus.IN_PROGRESS);
                    telegramUserTask.setToNotify(false);
                    saveContext.saving(telegramUserTask);
                    telegramUserTasks.add(telegramUserTask);
                }
            }

            telegramUser.setTasks(telegramUserTasks);
            saveContext.saving(telegramUser);


            dataManager.save(saveContext);
            return telegramUser;
        }
        return telegramUser;
    }

    @Authenticated
    public void saveInstagramNickForUser(long userid,String username) {

        TelegramUser telegramUser = dataManager.load(TelegramUser.class)
                .query("select e from yel_TelegramUser e where e.userid =:userId")
                .parameter("userId", userid)
                .fetchPlan(FetchPlan.LOCAL)
                .optional().orElse(null);
        telegramUser.setInstagram(username);
        dataManager.save(telegramUser);
    }
}