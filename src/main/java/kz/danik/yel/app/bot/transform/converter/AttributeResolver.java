package kz.danik.yel.app.bot.transform.converter;

@FunctionalInterface
public interface AttributeResolver {
    boolean isAttribute(String name);
}
