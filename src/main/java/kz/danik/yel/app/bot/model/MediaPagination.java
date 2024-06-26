package kz.danik.yel.app.bot.model;

import lombok.Data;

@Data
public class MediaPagination extends PageObject<Media> {
    private Integer count;
}
