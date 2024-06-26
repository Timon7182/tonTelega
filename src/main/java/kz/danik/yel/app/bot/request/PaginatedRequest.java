package kz.danik.yel.app.bot.request;

import kz.danik.yel.app.bot.mapper.Mapper;
import kz.danik.yel.app.bot.model.PageInfo;
import kz.danik.yel.app.bot.request.parameters.RequestParameter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public abstract class PaginatedRequest<R, P extends RequestParameter> {

    protected abstract Request requestInstagram(P requestParameters, PageInfo pageInfo);
    protected abstract void updateResult(R result, R current);
    protected abstract PageInfo getPageInfo(R current);
    protected abstract R mapResponse(InputStream jsonStream);
    private final OkHttpClient httpClient;
    @Getter(AccessLevel.PROTECTED)
    private final Mapper mapper;
    private final DelayHandler delayHandler;

    public R requestInstagramResult(P requestParameters, int pageCount, PageInfo pageCursor) throws IOException {
        R result = null;
        int currentPage = 0;
        while (currentPage++ < pageCount && pageCursor.isHasNextPage()) {

            Request request = requestInstagram(requestParameters, pageCursor);

            Response response = httpClient.newCall(request).execute();
            R current;
            try(InputStream jsonStream = response.body().byteStream()) {
                current = mapResponse(jsonStream);
            }
            if(delayHandler!=null) {
                delayHandler.onEachRequest();
            }

            if(result==null){
                result = current;
            } else {
                updateResult(result, current);
            }

            if(currentPage<pageCount && pageCursor.isHasNextPage() && delayHandler!=null){
                delayHandler.onNextPage(currentPage, pageCount, getClass(), pageCursor);
            }

            pageCursor = getPageInfo(current);
        }
        return result;
    }
}
