package kz.danik.yel.app.bot.request;

import kz.danik.yel.app.bot.Endpoint;
import kz.danik.yel.app.bot.mapper.Mapper;
import kz.danik.yel.app.bot.model.Media;
import kz.danik.yel.app.bot.model.PageInfo;
import kz.danik.yel.app.bot.model.PageObject;
import kz.danik.yel.app.bot.request.parameters.UserParameter;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.InputStream;

public class GetMediasRequest extends PaginatedRequest<PageObject<Media>, UserParameter> {

    public GetMediasRequest(OkHttpClient httpClient, Mapper mapper, DelayHandler delayHandler) {
        super(httpClient, mapper, delayHandler);
    }

    @Override
    protected Request requestInstagram(UserParameter requestParameters, PageInfo pageCursor) {
        return  new Request.Builder()
                .url(Endpoint.getAccountMediasJsonLink(requestParameters.getUserId(), pageCursor.getEndCursor()))
                .build();
    }

    @Override
    protected void updateResult(PageObject<Media> result, PageObject<Media> current) {
        result.getNodes().addAll(current.getNodes());
        result.setPageInfo(current.getPageInfo());
    }

    @Override
    protected PageInfo getPageInfo(PageObject<Media> current) {
        return current.getPageInfo();
    }

    @Override
    protected PageObject<Media> mapResponse(InputStream jsonStream) {
        return getMapper().mapMedias(jsonStream);
    }
}
