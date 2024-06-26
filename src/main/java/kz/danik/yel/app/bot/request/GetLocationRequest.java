package kz.danik.yel.app.bot.request;

import kz.danik.yel.app.bot.Endpoint;
import kz.danik.yel.app.bot.mapper.Mapper;
import kz.danik.yel.app.bot.model.Location;
import kz.danik.yel.app.bot.model.PageInfo;
import kz.danik.yel.app.bot.request.parameters.LocationParameter;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.InputStream;

public class GetLocationRequest extends PaginatedRequest<Location, LocationParameter> {

    public GetLocationRequest(OkHttpClient httpClient, Mapper mapper, DelayHandler delayHandler) {
        super(httpClient, mapper, delayHandler);
    }

    @Override
    protected Request requestInstagram(LocationParameter requestParameters, PageInfo pageInfo) {
        return new Request.Builder()
                .url(Endpoint.getMediasJsonByLocationIdLink(requestParameters.getLocationName(), pageInfo.getEndCursor()))
                .header(Endpoint.REFERER, Endpoint.BASE_URL + "/")
                .build();
    }

    @Override
    protected void updateResult(Location result, Location current) {
        result.getMediaRating().getMedia().getNodes().addAll(current.getMediaRating().getMedia().getNodes());
        result.getMediaRating().getMedia().setPageInfo(current.getMediaRating().getMedia().getPageInfo());

    }

    @Override
    protected PageInfo getPageInfo(Location current) {
        return current.getMediaRating().getMedia().getPageInfo();
    }

    @Override
    protected Location mapResponse(InputStream jsonStream) {
        return getMapper().mapLocation(jsonStream);
    }
}
