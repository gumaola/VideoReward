package cn.nano.common.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class JsonStringReader extends TypeAdapter<String> {

    @Override
    public void write(JsonWriter out, String value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value);
        }
    }

    @Override
    public String read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        switch (peek) {
            case BOOLEAN:
                return String.valueOf(in.nextBoolean());
            case NULL:
                in.nextNull();
                return null;
            case NUMBER:
                double db = in.nextDouble();
                if (db % 1 == 0) {
                    long l = (long) db;
                    return String.valueOf(l);
                } else {
                    return String.valueOf(db);
                }
            case STRING:
                return in.nextString();
        }
        return null;
    }
}
