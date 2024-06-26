package kz.danik.yel.app.bot.request.parameters;

import lombok.Value;

@Value
public class LocationParameter implements RequestParameter {
    private String locationName;
}
