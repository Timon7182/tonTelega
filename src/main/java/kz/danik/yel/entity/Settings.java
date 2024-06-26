package kz.danik.yel.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@JmixEntity
@Table(name = "YEL_SETTINGS")
@Entity(name = "yel_Settings")
public class Settings {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "VERSION", nullable = false)
    @Version
    private Integer version;

    @InstanceName
    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE", nullable = false)
    @NotNull
    private String code;

    @Lob
    @Column(name = "RU_VALUE")
    private String ruValue;

    @Column(name = "TO_CHECK", nullable = false)
    @NotNull
    private Boolean toCheck = false;

    @Lob
    @Column(name = "EN_VALUE")
    private String enValue;

    public Boolean getToCheck() {
        return toCheck;
    }

    public void setToCheck(Boolean toCheck) {
        this.toCheck = toCheck;
    }

    public String getEnValue() {
        return enValue;
    }

    public void setEnValue(String enValue) {
        this.enValue = enValue;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRuValue() {
        return ruValue;
    }

    public void setRuValue(String ruValue) {
        this.ruValue = ruValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}