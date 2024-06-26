package kz.danik.yel.app.bot.request.parameters;

import lombok.Value;

@Value
public class UserParameter implements RequestParameter {
    private long userId;
}
