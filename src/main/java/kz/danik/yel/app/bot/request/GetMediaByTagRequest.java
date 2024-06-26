package kz.danik.yel.app.bot.request;

import kz.danik.yel.app.bot.Endpoint;
import kz.danik.yel.app.bot.mapper.Mapper;
import kz.danik.yel.app.bot.model.PageInfo;
import kz.danik.yel.app.bot.model.Tag;
import kz.danik.yel.app.bot.request.parameters.TagName;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.InputStream;

public class GetMediaByTagRequest extends PaginatedRequest<Tag, TagName> {

    public GetMediaByTagRequest(OkHttpClient httpClient, Mapper mapper, DelayHandler delayHandler) {
        super(httpClient, mapper, delayHandler);
    }

    @Override
    protected Request requestInstagram(TagName requestParameters, PageInfo pageInfo) {
        return new Request.Builder()
                .url(Endpoint.getMediasJsonByTagLink(requestParameters.getTag(), pageInfo.getEndCursor()))
                .header(Endpoint.REFERER, Endpoint.BASE_URL + "/")
                .build();
    }

    @Override
    protected void updateResult(Tag result, Tag current) {
        if(isResultMediaEmpty(result) || isCurrentMediaEmpty(current)){
            return;
        }
        result.getMediaRating().getMedia().getNodes().addAll(current.getMediaRating().getMedia().getNodes());
        if(result.getMediaRating().getMedia().getPageInfo()==null){
            return;
        }
        result.getMediaRating().getMedia().setPageInfo(current.getMediaRating().getMedia().getPageInfo());
    }

    private boolean isResultMediaEmpty(Tag result) {
        return result == null || result.getMediaRating() == null || result.getMediaRating().getMedia() == null
                || result.getMediaRating().getMedia().getNodes() == null;
    }

    private boolean isCurrentMediaEmpty(Tag current) {
        return isResultMediaEmpty(current);
    }

    @Override
    protected PageInfo getPageInfo(Tag current) {
        return current.getMediaRating().getMedia().getPageInfo();
    }

    @Override
    protected Tag mapResponse(InputStream jsonStream) {
        return getMapper().mapTag(jsonStream);
    }
}
