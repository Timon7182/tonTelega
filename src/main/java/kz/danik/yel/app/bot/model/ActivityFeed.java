package kz.danik.yel.app.bot.model;

import lombok.Data;

import java.util.List;

@Data
public class ActivityFeed {
    private String timestamp;
    private Integer count;
    private List<Activity> activities;
}
