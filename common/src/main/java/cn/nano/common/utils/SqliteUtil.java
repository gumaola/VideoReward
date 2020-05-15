package cn.nano.common.utils;

public class SqliteUtil {

    public static String sqliteEscape(String keyWord) {
        keyWord = keyWord.replace("'", "''");
        keyWord = keyWord.replace("[", "/[");
        keyWord = keyWord.replace("]", "/]");
        keyWord = keyWord.replace("%", "/%");
        keyWord = keyWord.replace("&", "/&");
        keyWord = keyWord.replace("(", "/(");
        keyWord = keyWord.replace(")", "/)");
        return keyWord;
    }

    public static String sqliteUnEscape(String keyWord) {
        keyWord = keyWord.replace("''", "'");
        keyWord = keyWord.replace("/[", "[");
        keyWord = keyWord.replace("/]", "]");
        keyWord = keyWord.replace("/%", "%");
        keyWord = keyWord.replace("/&", "&");
        keyWord = keyWord.replace("/(", "(");
        keyWord = keyWord.replace("/)", ")");
        return keyWord;
    }
}
