package ch.globaz.utils;

import java.util.Arrays;
import java.util.List;

public class BasicSerializer {
    private static final String SERIALIZER_CHARACTER = ";";

    public static String serialize(List<String> listToSerialize) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < listToSerialize.size(); i++) {
            String string = listToSerialize.get(i);
            if (i != 0) {
                sb.append(SERIALIZER_CHARACTER);
            }
            sb.append(string);
        }
        return sb.toString();
    }

    public static List<String> deserialize(String stringToDeserialize) {
        String[] strings = stringToDeserialize.split(SERIALIZER_CHARACTER);
        return Arrays.asList(strings);
    }
}
