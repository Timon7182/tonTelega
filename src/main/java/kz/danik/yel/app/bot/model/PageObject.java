package kz.danik.yel.app.bot.model;

import lombok.Data;

import java.util.List;

@Data
public class PageObject<T> {

    private List<T> nodes;
    private Integer count;
    private PageInfo pageInfo;
}
