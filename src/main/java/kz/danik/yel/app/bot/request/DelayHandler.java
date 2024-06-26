package kz.danik.yel.app.bot.request;


import kz.danik.yel.app.bot.model.PageInfo;

public interface DelayHandler {
    void onEachRequest();
    void onNextPage(int currentPage, int pageCount, Class<? extends PaginatedRequest> pageOperation, PageInfo pageCursor);
}
