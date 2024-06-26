package kz.danik.yel.app.bot.request;


import kz.danik.yel.app.bot.Endpoint;
import kz.danik.yel.app.bot.mapper.Mapper;
import kz.danik.yel.app.bot.model.Account;
import kz.danik.yel.app.bot.model.PageInfo;
import kz.danik.yel.app.bot.model.PageObject;
import kz.danik.yel.app.bot.request.parameters.UserParameter;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.InputStream;

public class GetFollowersRequest extends PaginatedRequest<PageObject<Account>, UserParameter> {

    public GetFollowersRequest(OkHttpClient httpClient, Mapper mapper, DelayHandler delayHandler) {
        super(httpClient, mapper, delayHandler);
    }

    @Override
    protected Request requestInstagram(UserParameter requestParameters, PageInfo pageInfo) {
        return new Request.Builder()
                .url(Endpoint.getFollowersLinkVariables(requestParameters.getUserId(), 200, pageInfo.getEndCursor()))
                .header(Endpoint.REFERER, Endpoint.BASE_URL + "/")
                .build();
    }

    @Override
    protected void updateResult(PageObject<Account> result, PageObject<Account> current) {
        result.getNodes().addAll(current.getNodes());
        result.setPageInfo(current.getPageInfo());
    }

    @Override
    protected PageInfo getPageInfo(PageObject<Account> current) {
        return current.getPageInfo();
    }

    @Override
    protected PageObject<Account> mapResponse(InputStream jsonStream) {
        return getMapper().mapFollowers(jsonStream);
    }
}
