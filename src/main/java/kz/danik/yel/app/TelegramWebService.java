package kz.danik.yel.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlan;
import io.jmix.core.security.Authenticated;
import kz.danik.yel.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("yel_TelegramWebService")
public class TelegramWebService {

    private final TelegramAuth telegramAuth;
    private final TelegramUserService telegramUserService;
    private final SettingsService settingsService;
    @Autowired
    private DataManager dataManager;

    public TelegramWebService(TelegramAuth telegramAuth, TelegramUserService telegramUserService, SettingsService settingsService) {
        this.telegramAuth = telegramAuth;
        this.telegramUserService = telegramUserService;
        this.settingsService = settingsService;
    }

    public List<TelegramTask> getActiveTelegramTasks(String hash){
        return dataManager.load(TelegramTask.class)
                .query("select e from yel_TelegramTask e")
                .fetchPlan(FetchPlan.BASE)
                .list();
    }

    public TelegramDto getUserInfo(String user,
                                  String chat_instance,
                                  String chat_type,
                                  String auth_date,
                                  String hash){
        try {
            telegramAuth.authenticate(user,chat_instance,chat_type, auth_date, hash);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode userNode = objectMapper.readTree(user);
            String userId = userNode.get("id").asText();
            String firstName = userNode.get("first_name").asText();
            String lastName = userNode.has("last_name") ? userNode.get("last_name").asText() : "";
            String username = userNode.get("username").asText();

            TelegramUser telegramUser = telegramUserService.getTelegramUserInfo(userId,
                    firstName,
                    lastName,
                    username,
                    Long.parseLong(chat_instance));

            TelegramDto telegramDto = dataManager.create(TelegramDto.class);
            telegramDto.setId(telegramUser.getId());
            telegramDto.setBalance(telegramUser.getBalance() != null ? telegramDto.getBalance() : 0);
            telegramDto.setTasks(telegramUser.getTasks().stream()
                    .filter(e->e.getStatus().equals(TaskStatus.IN_PROGRESS)
                            || e.getPaymentStatus().equals(PaymentStatus.TO_PAY))
                    .toList());

            return telegramDto;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}