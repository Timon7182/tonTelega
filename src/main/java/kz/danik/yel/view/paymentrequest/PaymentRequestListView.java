package kz.danik.yel.view.paymentrequest;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;
import kz.danik.yel.entity.PaymentRequest;
import kz.danik.yel.view.main.MainView;

@Route(value = "paymentRequests", layout = MainView.class)
@ViewController("yel_PaymentRequest.list")
@ViewDescriptor("payment-request-list-view.xml")
@LookupComponent("paymentRequestsDataGrid")
@DialogMode(width = "64em")
public class PaymentRequestListView extends StandardListView<PaymentRequest> {
}