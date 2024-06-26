package kz.danik.yel.app.bot.transform.converter;

public class NopTypeConverter implements TypeConverter {
    @Override
    public Object transform(Object srcData) {
        return srcData;
    }
}
