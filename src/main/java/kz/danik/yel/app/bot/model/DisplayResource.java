package kz.danik.yel.app.bot.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class DisplayResource {
    private String src;
    private Integer width;
    private Integer height;
}
