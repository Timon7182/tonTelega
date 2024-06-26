package kz.danik.yel.app.bot.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class TaggedUser {
    private String username;
    private Float x;
    private Float y;
}
