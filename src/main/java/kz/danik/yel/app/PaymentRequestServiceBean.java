package kz.danik.yel.app;

import io.jmix.core.DataManager;
import kz.danik.yel.entity.PaymentRequest;
import kz.danik.yel.entity.PaymentStatus;
import kz.danik.yel.entity.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("yel_PaymentRequestServiceBean")
public class PaymentRequestServiceBean {

    @Autowired
    protected DataManager dataManager;
    public void createPaymentRequest(TelegramUser telegramUser, Double sum){
        PaymentRequest paymentRequest =dataManager.create(PaymentRequest.class);
        paymentRequest.setSum(sum);
        paymentRequest.setUser(telegramUser);
        paymentRequest.setStatus(PaymentStatus.TO_PAY);
        dataManager.save(paymentRequest);
    }
}