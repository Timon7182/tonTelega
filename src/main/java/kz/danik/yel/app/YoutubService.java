package kz.danik.yel.app;

import com.google.api.services.youtube.model.SubscriptionListResponse;
import org.springframework.stereotype.Component;


import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;

import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Component("yel_YoutubService")
public class YoutubService {
    private static final String CLIENT_SECRETS= "\"C:\\Users\\UCO - T14\\Downloads\\client_secret.json\"";
    private static final List<String> SCOPES = Collections.singletonList("https://www.googleapis.com/auth/youtube.readonly");
    private static final String CREDENTIALS_FOLDER = "credentials"; // Directory to store user credentials.
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();



    private static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new FileReader(CLIENT_SECRETS));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(CREDENTIALS_FOLDER)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        return new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName("youtube-recent-subscribers")
                .build();
    }

    public void doSmth() throws GeneralSecurityException, IOException {
        YouTube youtubeService = getService();

        YouTube.Subscriptions.List request = youtubeService.subscriptions()
                .list("snippet,contentDetails");

        SubscriptionListResponse response = request.setMine(true).execute();

    }
}