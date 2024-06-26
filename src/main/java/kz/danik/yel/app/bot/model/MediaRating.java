package kz.danik.yel.app.bot.model;

import lombok.Data;

import java.util.List;

@Data
public class MediaRating {
    private PageObject<Media> media;
    private List<Media> topPosts;
}
