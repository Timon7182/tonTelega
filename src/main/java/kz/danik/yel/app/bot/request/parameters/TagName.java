package kz.danik.yel.app.bot.request.parameters;

import lombok.Value;

@Value
public class TagName implements RequestParameter {
    private String tag;
}
