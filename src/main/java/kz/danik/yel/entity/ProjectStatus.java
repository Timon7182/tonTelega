package kz.danik.yel.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum ProjectStatus implements EnumClass<String> {

    ACTIVE("ACTIVE"),
    NEW_REQUEST("NEW_REQUEST"),
    IN_ACTIVE("IN_ACTIVE");

    private final String id;

    ProjectStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ProjectStatus fromId(String id) {
        for (ProjectStatus at : ProjectStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}