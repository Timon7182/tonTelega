package kz.danik.yel.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@JmixEntity
@Table(name = "YEL_PAYMENT_REQUEST", indexes = {
        @Index(name = "IDX_YEL_PAYMENT_REQUEST_USER", columnList = "USER_ID")
})
@Entity(name = "yel_PaymentRequest")
public class PaymentRequest {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @NotNull
    @Column(name = "STATUS", nullable = false)
    private String status;

    @JoinColumn(name = "USER_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TelegramUser user;

    @Column(name = "SUM_", nullable = false)
    @NotNull
    private Double sum;

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public TelegramUser getUser() {
        return user;
    }

    public void setUser(TelegramUser user) {
        this.user = user;
    }

    public PaymentStatus getStatus() {
        return status == null ? null : PaymentStatus.fromId(status);
    }

    public void setStatus(PaymentStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}