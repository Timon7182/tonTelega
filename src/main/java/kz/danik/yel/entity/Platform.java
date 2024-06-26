package kz.danik.yel.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum Platform implements EnumClass<String> {

    INSTAGRAM("INSTAGRAM"),
    YOUTUBE("YOUTUBE");

    private final String id;

    Platform(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static Platform fromId(String id) {
        for (Platform at : Platform.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}