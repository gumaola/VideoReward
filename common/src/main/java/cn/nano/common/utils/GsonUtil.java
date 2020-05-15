package cn.nano.common.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class GsonUtil {
    private static Gson mGson = null;

    private static class UtilDateSerialization implements JsonSerializer<Date> {
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {

            return new JsonPrimitive(src.getTime());
        }
    }

    private static class UtilDateDeserialization implements JsonDeserializer<Date> {

        public Date deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
            return new Date(json.getAsLong());
        }

    }


    public static Gson createGson() {
        if (mGson == null) {
            synchronized (GsonUtil.class) {
                if (mGson == null) {
                    GsonBuilder builder = new GsonBuilder()
                            .disableHtmlEscaping()
                            .registerTypeAdapter(String.class, new JsonStringReader())
                            .registerTypeAdapter(Date.class, new UtilDateSerialization())
                            .registerTypeAdapter(Date.class, new UtilDateDeserialization());
                    mGson = builder.create();
                }
            }
        }
        return mGson;
    }

    public static <T> ArrayList<T> parseArrayListByStr(Class<T> classT, String srcStr) {

        ArrayList<T> list = null;

        if (!TextUtils.isEmpty(srcStr)) {
            try {
                JsonElement srcJsonElement = new JsonParser().parse(srcStr);
                if (srcJsonElement != null) {
                    JsonArray array = srcJsonElement.getAsJsonArray();
                    list = new ArrayList<>();
                    for (JsonElement jsonElement : array) {
                        if (jsonElement != null && jsonElement.isJsonObject()) {
                            list.add(GsonUtil.createGson().fromJson(jsonElement, classT));
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
