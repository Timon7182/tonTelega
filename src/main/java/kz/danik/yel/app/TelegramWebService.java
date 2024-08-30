package kz.danik.yel.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlan;
import io.jmix.core.security.Authenticated;
import kz.danik.yel.entity.*;
import okhttp3.internal.concurrent.Task;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("yel_TelegramWebService")
public class TelegramWebService {

    private final TelegramAuth telegramAuth;
    private final TelegramUserService telegramUserService;
    private final SettingsService settingsService;
    private final ProjectServiceBean projectServiceBean;
    private final PaymentRequestServiceBean paymentRequestServiceBean;
    @Autowired
    private DataManager dataManager;

    public TelegramWebService(TelegramAuth telegramAuth, TelegramUserService telegramUserService, SettingsService settingsService, ProjectServiceBean projectServiceBean, PaymentRequestServiceBean paymentRequestServiceBean) {
        this.telegramAuth = telegramAuth;
        this.telegramUserService = telegramUserService;
        this.settingsService = settingsService;
        this.projectServiceBean = projectServiceBean;
        this.paymentRequestServiceBean = paymentRequestServiceBean;
    }

    public List<TelegramTask> getActiveTelegramTasks(String hash) {
        return dataManager.load(TelegramTask.class)
                .query("select e from yel_TelegramTask e")
                .fetchPlan(FetchPlan.BASE)
                .list();
    }

    public TelegramDto getUserInfo(String user,
                                   String chat_instance,
                                   String chat_type,
                                   String auth_date,
                                   String hash,
                                   String query_id) {
        try {
            telegramAuth.authenticate(user, chat_instance, chat_type, auth_date, hash, query_id);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode userNode = objectMapper.readTree(user);
            String userId = userNode.get("id").asText();
            String firstName = userNode.get("first_name").asText();
            String lastName = userNode.has("last_name") ? userNode.get("last_name").asText() : "";
            String username = userNode.get("username").asText();
            Long chatId = null;

            try {
                if (chat_instance != null && !chat_instance.isEmpty() && !chat_instance.equals("null"))
                    chatId = Long.valueOf(chat_instance);
            } catch (Exception ez) {

            }

            TelegramUser telegramUser = telegramUserService.getTelegramUserInfo(userId,
                    firstName,
                    lastName,
                    username,
                    chatId);

            TelegramDto telegramDto = dataManager.create(TelegramDto.class);
            telegramDto.setId(telegramUser.getId());
            telegramDto.setBalance(telegramUser.getBalance() != null ? telegramUser.getBalance() : 0);

            List<TelegramUserTask> telegramUserTasks = telegramUser.getTasks().stream()
                    .filter(e -> e.getTask().getIsActive().equals(Boolean.TRUE))
                    .filter(e -> {
                        Date now = new Date();
                        return now.after(e.getDateTimeFrom()) && now.before(e.getDateTimeTo());
                    })
                    .collect(Collectors.toList());

            telegramDto.setTaskCount((long) telegramUserTasks.size());
            telegramDto.setLevel(telegramUser.getLevel().getId());
            return telegramDto;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void accomplishTask(String user,
                               String chat_instance,
                               String chat_type,
                               String auth_date,
                               String hash,
                               UUID taskId,
                               String query_id) {
        try {
            telegramAuth.authenticate(user, chat_instance, chat_type, auth_date, hash, query_id);

            TelegramUserTask telegramUserTask = dataManager.load(TelegramUserTask.class)
                    .id(taskId)
                    .fetchPlan(FetchPlan.BASE)
                    .optional().orElse(null);

            if (telegramUserTask == null
                    || telegramUserTask.getStatus().equals(TaskStatus.DONE)
                    || telegramUserTask.getTask().getIsActive().equals(Boolean.FALSE)
                    || new Date().before(telegramUserTask.getDateTimeFrom())
                    || new Date().after(telegramUserTask.getDateTimeTo())) {
                throw new Exception("Task not found");
            }

            TelegramUser telegramUser = telegramUserTask.getUser();
            telegramUser.setBalance(telegramUser.getBalance() != null
                    ? telegramUser.getBalance() + telegramUserTask.getTask().getPrize()
                    : telegramUserTask.getTask().getPrize());
            telegramUserTask.setStatus(TaskStatus.DONE);

            dataManager.save(telegramUser);
            dataManager.save(telegramUserTask);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createProject(String user,
                              String chat_instance,
                              String chat_type,
                              String auth_date,
                              String hash,
                              String query_id,
                              String text,
                              String link) {
        try {
            telegramAuth.authenticate(user, chat_instance, chat_type, auth_date, hash, query_id);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode userNode = objectMapper.readTree(user);
            String userId = userNode.get("id").asText();

            TelegramUser telegramUser = dataManager.load(TelegramUser.class)
                    .query("select e from yel_TelegramUser e where e.userid =:userId")
                    .parameter("userId", Long.parseLong(userId))
                    .optional().orElse(null);

            projectServiceBean.createProject(telegramUser, link, text);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProjectDto> getProjects(String user,
                                        String chat_instance,
                                        String chat_type,
                                        String auth_date,
                                        String hash,
                                        String query_id) {

        try {
            telegramAuth.authenticate(user, chat_instance, chat_type, auth_date, hash, query_id);
            return projectServiceBean.getProjects();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createPaymentRequest(String user,
                                     String chat_instance,
                                     String chat_type,
                                     String auth_date,
                                     String hash,
                                     String query_id,
                                     Double sum){
        try {
            telegramAuth.authenticate(user, chat_instance, chat_type, auth_date, hash, query_id);

            TelegramUser telegramUser = getTelegramUser(user);

            paymentRequestServiceBean.createPaymentRequest(telegramUser, sum);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private @Nullable TelegramUser getTelegramUser(String user) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode userNode = objectMapper.readTree(user);
        String userId = userNode.get("id").asText();

        TelegramUser telegramUser = dataManager.load(TelegramUser.class)
                .query("select e from yel_TelegramUser e where e.userid =:userId")
                .parameter("userId", Long.parseLong(userId))
                        .fetchPlan("telegramUser-full-fetch-plan")
                .optional().orElse(null);
        return telegramUser;
    }

    public List<TelegramUserTask> getTasks(String user,
                                       String chat_instance,
                                       String chat_type,
                                       String auth_date,
                                       String hash,
                                       String query_id){
        try {
            telegramAuth.authenticate(user, chat_instance, chat_type, auth_date, hash, query_id);
            return telegramUserService.getUserTasks(getTelegramUser(user));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}