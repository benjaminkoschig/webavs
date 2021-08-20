package ch.globaz.common.ws;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class JacksonJsonProvider {

    private static ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

    static {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        MAPPER.setDateFormat(df);
    }

    private JacksonJsonProvider() {
    }

    public static ObjectMapper getInstance() {
        return MAPPER;
    }

}
