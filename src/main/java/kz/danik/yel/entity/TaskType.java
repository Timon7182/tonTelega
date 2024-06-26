package kz.danik.yel.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum TaskType implements EnumClass<String> {

    FOLLOW("FOLLOW"),
    LINK("LINK");

    private final String id;

    TaskType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static TaskType fromId(String id) {
        for (TaskType at : TaskType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}