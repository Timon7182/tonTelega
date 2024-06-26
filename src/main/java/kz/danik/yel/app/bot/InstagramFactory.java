package kz.danik.yel.app.bot;

import lombok.experimental.UtilityClass;
import kz.danik.yel.app.bot.cookie.CookieHashSet;
import kz.danik.yel.app.bot.cookie.DefaultCookieJar;
import kz.danik.yel.app.bot.interceptor.ErrorInterceptor;
import kz.danik.yel.app.bot.interceptor.FakeBrowserInterceptor;
import okhttp3.OkHttpClient;

import java.io.IOException;

@UtilityClass
public class InstagramFactory {

    public static Instagram getAuthenticatedInstagramClient(String login, String password, String userAgent)
            throws IOException{

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new FakeBrowserInterceptor(userAgent))
                .addInterceptor(new ErrorInterceptor())
                .cookieJar(new DefaultCookieJar(new CookieHashSet()))
                .build();
        Instagram client = new Instagram(httpClient);
        client.basePage();
        client.login(login, password);
        client.basePage();
        return client;
    }
}
