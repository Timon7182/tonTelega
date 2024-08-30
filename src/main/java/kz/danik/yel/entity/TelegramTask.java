package kz.danik.yel.entity;

import io.jmix.core.MetadataTools;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "YEL_TELEGRAM_TASK")
@Entity(name = "yel_TelegramTask")
public class TelegramTask {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "LEVEL_")
    private String level;

    @Column(name = "TASK_URL")
    @Lob
    private String taskUrl;

    @Column(name = "TASK_NAME", nullable = false)
    @Lob
    @NotNull
    private String taskName;

    @Column(name = "TASK_NAME_EN")
    @Lob
    private String taskNameEn;

    @Column(name = "TYPE_", nullable = false)
    @NotNull
    private String type;

    @Column(name = "PLATFORM")
    private String platform;

    @Column(name = "PRIZE", nullable = false)
    @NotNull
    private Double prize;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "IS_ACTIVE", nullable = false)
    @NotNull
    private Boolean isActive = true;

    @JoinTable(name = "YEL_TELEGRAM_TASK_TELEGRAM_USER_TASK_LINK",
            joinColumns = @JoinColumn(name = "TELEGRAM_TASK_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "TELEGRAM_USER_TASK_ID", referencedColumnName = "ID"))
    @ManyToMany
    private List<TelegramUserTask> userTasks;

    @Column(name = "IS_TO_EVERYONE")
    private Boolean isToEveryone = false;

    @Column(name = "IS_TO_SEND_TO_NEW")
    private Boolean isToSendToNew = false;

    @Column(name = "DATE_TIME_FROM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeFrom;

    @Column(name = "DATE_TIME_TO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeTo;

    public Date getDateTimeTo() {
        return dateTimeTo;
    }

    public void setDateTimeTo(Date dateTimeTo) {
        this.dateTimeTo = dateTimeTo;
    }

    public Date getDateTimeFrom() {
        return dateTimeFrom;
    }

    public void setDateTimeFrom(Date dateTimeFrom) {
        this.dateTimeFrom = dateTimeFrom;
    }

    public Level getLevel() {
        return level == null ? null : Level.fromId(level);
    }

    public void setLevel(Level level) {
        this.level = level == null ? null : level.getId();
    }

    public Boolean getIsToSendToNew() {
        return isToSendToNew;
    }

    public void setIsToSendToNew(Boolean isToSendToNew) {
        this.isToSendToNew = isToSendToNew;
    }

    public Boolean getIsToEveryone() {
        return isToEveryone;
    }

    public void setIsToEveryone(Boolean isToEveryone) {
        this.isToEveryone = isToEveryone;
    }

    public List<TelegramUserTask> getUserTasks() {
        return userTasks;
    }

    public void setUserTasks(List<TelegramUserTask> userTasks) {
        this.userTasks = userTasks;
    }

    public String getTaskNameEn() {
        return taskNameEn;
    }

    public void setTaskNameEn(String taskNameEn) {
        this.taskNameEn = taskNameEn;
    }

    public String getTaskUrl() {
        return taskUrl;
    }

    public void setTaskUrl(String taskUrl) {
        this.taskUrl = taskUrl;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getPrize() {
        return prize;
    }

    public void setPrize(Double prize) {
        this.prize = prize;
    }

    public Platform getPlatform() {
        return platform == null ? null : Platform.fromId(platform);
    }

    public void setPlatform(Platform platform) {
        this.platform = platform == null ? null : platform.getId();
    }

    public TaskType getType() {
        return type == null ? null : TaskType.fromId(type);
    }

    public void setType(TaskType type) {
        this.type = type == null ? null : type.getId();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @InstanceName
    @DependsOnProperties({"taskName"})
    public String getInstanceName(MetadataTools metadataTools) {
        return metadataTools.format(taskName);
    }
}