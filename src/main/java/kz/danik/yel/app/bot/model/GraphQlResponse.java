package kz.danik.yel.app.bot.model;

import lombok.Data;

@Data
public class GraphQlResponse<T> {
    private T payload;
}
