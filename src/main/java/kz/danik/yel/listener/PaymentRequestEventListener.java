package kz.danik.yel.listener;

import io.jmix.core.FetchPlan;
import io.jmix.core.SaveContext;
import io.jmix.core.impl.DataManagerImpl;
import kz.danik.yel.app.TonBot;
import kz.danik.yel.entity.PaymentRequest;
import io.jmix.core.event.EntityChangedEvent;
import kz.danik.yel.entity.PaymentStatus;
import kz.danik.yel.entity.TelegramUser;
import kz.danik.yel.entity.TelegramUserTask;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component("yel_PaymentRequestEventListener")
public class PaymentRequestEventListener {

    private static final Logger log = LoggerFactory.getLogger(PaymentRequestEventListener.class);

    @Autowired
    private DataManagerImpl dataManager;

    @TransactionalEventListener
    public void onPaymentRequestChangedAfterCommit(final EntityChangedEvent<PaymentRequest> event) throws TelegramApiException {
        PaymentRequest paymentRequest = dataManager.load(PaymentRequest.class)
                .id(event.getEntityId().getValue())
                .joinTransaction(false)
                .one();

        if (event.getType().equals(EntityChangedEvent.Type.CREATED)) {
            sendToAdmin(paymentRequest);
            return;
        }

        if (event.getType().equals(EntityChangedEvent.Type.UPDATED)) {
            double balance = paymentRequest.getUser().getBalance() - paymentRequest.getSum();

            if (!paymentRequest.getStatus().equals(PaymentStatus.PAID)) {
                return;
            }

            try {

                TelegramUser telegramUser = paymentRequest.getUser();
                telegramUser.setBalance(balance);
                dataManager.save(new SaveContext().saving(telegramUser).setJoinTransaction(false));

                TonBot.sendMessageToChat(String.valueOf(paymentRequest.getUser().getChatId().longValue()),
                        "Ваша заявка на выплату прошло и скоро упадёт на ваш кошелёк. Сумма запроса: "
                                + paymentRequest.getSum());
            } catch (TelegramApiException e) {
                log.error(String.format("%s, %s", ExceptionUtils.getMessage(e),
                        ExceptionUtils.getStackTrace(e)));
                throw new RuntimeException(e);
            }
        }

    }

    protected void sendToAdmin(PaymentRequest paymentRequest) throws TelegramApiException {
        List<TelegramUser> telegramUserList = dataManager.load(TelegramUser.class)
                .query("select e from yel_TelegramUser e where e.isAdmin = true")
                .joinTransaction(false)
                .fetchPlan(FetchPlan.LOCAL)
                .list();

        for (TelegramUser telegramUser : telegramUserList) {
            String username = paymentRequest.getUser().getUsername();
            TonBot.sendMessageToChat(String.valueOf(telegramUser.getChatId().longValue()),
                    String.format("Новая заявка на выплату: %s, \n Ссылка: %s",
                            "@" + username,
                            "https://tontelega-410f0443bf5d.herokuapp.com/paymentRequests/"+paymentRequest.getId()));
        }

    }
}