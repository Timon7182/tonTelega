package kz.danik.yel.app.bot.request.parameters;

import lombok.Value;

@Value
public class MediaCode implements RequestParameter {
    private String shortcode;
}
