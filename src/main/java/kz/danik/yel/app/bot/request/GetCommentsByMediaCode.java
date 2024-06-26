package kz.danik.yel.app.bot.request;


import kz.danik.yel.app.bot.Endpoint;
import kz.danik.yel.app.bot.mapper.Mapper;
import kz.danik.yel.app.bot.model.Comment;
import kz.danik.yel.app.bot.model.PageInfo;
import kz.danik.yel.app.bot.model.PageObject;

import kz.danik.yel.app.bot.request.parameters.MediaCode;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.InputStream;
import java.util.List;

public class GetCommentsByMediaCode extends PaginatedRequest<PageObject<Comment>, MediaCode> {

    public GetCommentsByMediaCode(OkHttpClient httpClient, Mapper mapper, DelayHandler delayHandler) {
        super(httpClient, mapper, delayHandler);
    }

    @Override
    protected Request requestInstagram(MediaCode requestParameters, PageInfo pageInfo) {
        return new Request.Builder()
                .url(Endpoint.getCommentsBeforeCommentIdByCode(
                        requestParameters.getShortcode(), 20, pageInfo.getEndCursor()))
                .header(Endpoint.REFERER, Endpoint.BASE_URL + "/")
                .build();
    }

    @Override
    protected void updateResult(PageObject<Comment> result, PageObject<Comment> current) {
        result.getNodes().addAll(current.getNodes());
        result.setPageInfo(current.getPageInfo());
    }

    @Override
    protected PageInfo getPageInfo(PageObject<Comment> current) {
        List<Comment> comments = current.getNodes();
        return new PageInfo(current.getPageInfo().isHasNextPage(),Long.toString(comments.get(0).getId()));
    }

    @Override
    protected PageObject<Comment> mapResponse(InputStream jsonStream) {
        return getMapper().mapComments(jsonStream);
    }
}
