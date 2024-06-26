package kz.danik.yel.app.bot.request.parameters;

import lombok.Value;

@Value
public class UsernameParameter implements RequestParameter{
    private String username;
}
