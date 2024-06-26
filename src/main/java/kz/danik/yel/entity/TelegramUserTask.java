package kz.danik.yel.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

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