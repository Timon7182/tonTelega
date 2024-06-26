package kz.danik.yel.app.bot;

import kz.danik.yel.app.bot.model.*;

import java.io.IOException;

public interface AnonymousInsta extends StatelessInsta {

    Location getLocationMediasById(String locationId, int pageCount) throws IOException;

    Tag getMediasByTag(String tag, int pageCount) throws IOException;

    PageObject<Comment> getCommentsByMediaCode(String code, int pageCount) throws IOException;

    Account getAccountById(long id) throws IOException;

}
