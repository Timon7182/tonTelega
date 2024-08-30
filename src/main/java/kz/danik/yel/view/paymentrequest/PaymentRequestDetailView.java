package kz.danik.yel.view.paymentrequest;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import kz.danik.yel.entity.PaymentRequest;
import kz.danik.yel.view.main.MainView;

@Route(value = "paymentRequests/:id", layout = MainView.class)
@ViewController("yel_PaymentRequest.detail")
@ViewDescriptor("payment-request-detail-view.xml")
@EditedEntityContainer("paymentRequestDc")
public class PaymentRequestDetailView extends StandardDetailView<PaymentRequest> {
}