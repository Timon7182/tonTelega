package kz.danik.yel.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum Level implements EnumClass<String> {

    BRONZE("BRONZE"),
    SILVER("SILVER"),
    GOLD("GOLD"),
    PLATINUM("PLATINUM");

    private final String id;

    Level(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static Level fromId(String id) {
        for (Level at : Level.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}