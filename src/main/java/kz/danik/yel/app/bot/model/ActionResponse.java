package kz.danik.yel.app.bot.model;

import lombok.Data;

@Data
public class ActionResponse<T> {
    private String status;
    private T payload;
}
