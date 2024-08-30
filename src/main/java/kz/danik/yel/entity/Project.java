package kz.danik.yel.entity;

import io.jmix.core.FileRef;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

import java.util.UUID;

@JmixEntity
@Table(name = "YEL_PROJECT", indexes = {
        @Index(name = "IDX_YEL_PROJECT_USER", columnList = "USER_ID")
})
@Entity(name = "yel_Project")
public class Project {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "LINK")
    @Lob
    private String link;

    @Column(name = "ICON", length = 1024)
    private FileRef icon;

    @Column(name = "TEXT_FROM_REQUEST")
    @Lob
    private String textFromRequest;

    @Column(name = "TEXT_RU")
    @Lob
    private String textRu;

    @Column(name = "TEXT_EN")
    @Lob
    private String textEn;

    @JoinColumn(name = "USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TelegramUser user;

    public FileRef getIcon() {
        return icon;
    }

    public void setIcon(FileRef icon) {
        this.icon = icon;
    }

    public String getTextEn() {
        return textEn;
    }

    public void setTextEn(String textEn) {
        this.textEn = textEn;
    }

    public String getTextRu() {
        return textRu;
    }

    public void setTextRu(String textRu) {
        this.textRu = textRu;
    }

    public TelegramUser getUser() {
        return user;
    }

    public void setUser(TelegramUser user) {
        this.user = user;
    }

    public String getTextFromRequest() {
        return textFromRequest;
    }

    public void setTextFromRequest(String textFromRequest) {
        this.textFromRequest = textFromRequest;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ProjectStatus getStatus() {
        return status == null ? null : ProjectStatus.fromId(status);
    }

    public void setStatus(ProjectStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}