package kz.danik.yel.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;
import java.util.UUID;

@JmixEntity
@Table(name = "YEL_TELEGRAM_USER_TASK", indexes = {
        @Index(name = "IDX_YEL_TELEGRAM_USER_TASK_USER", columnList = "USER_ID"),
        @Index(name = "IDX_YEL_TELEGRAM_USER_TASK_TASK", columnList = "TASK_ID")
})
@Entity(name = "yel_TelegramUserTask")
public class TelegramUserTask {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "TO_NOTIFY")
    private Boolean toNotify = true;

    @JoinColumn(name = "USER_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TelegramUser user;

    @Column(name = "STATUS", nullable = false)
    @NotNull
    private String status = TaskStatus.IN_PROGRESS.getId();

    @JoinColumn(name = "TASK_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TelegramTask task;

    @Column(name = "PAYMENT_STATUS")
    private String paymentStatus;

    @CreatedBy
    @Column(name = "CREATED_BY")
    private String createdBy;

    @CreatedDate
    @Column(name = "CREATED_DATE")
    private OffsetDateTime createdDate;

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    private OffsetDateTime lastModifiedDate;

    public OffsetDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(OffsetDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(OffsetDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus == null ? null : PaymentStatus.fromId(paymentStatus);
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus == null ? null : paymentStatus.getId();
    }

    public Boolean getToNotify() {
        return toNotify;
    }

    public void setToNotify(Boolean toNotify) {
        this.toNotify = toNotify;
    }

    public TelegramTask getTask() {
        return task;
    }

    public void setTask(TelegramTask task) {
        this.task = task;
    }

    public TaskStatus getStatus() {
        return status == null ? null : TaskStatus.fromId(status);
    }

    public void setStatus(TaskStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public TelegramUser getUser() {
        return user;
    }

    public void setUser(TelegramUser user) {
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}