package kz.danik.yel.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum PaymentStatus implements EnumClass<String> {

    FREE("FREE"),
    PAID("PAID"),
    TO_PAY("TO_PAY");

    private final String id;

    PaymentStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static PaymentStatus fromId(String id) {
        for (PaymentStatus at : PaymentStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}