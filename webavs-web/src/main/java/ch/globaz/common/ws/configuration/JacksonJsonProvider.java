package ch.globaz.common.ws.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class JacksonJsonProvider {

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = JsonMapper.builder()
                           .addModule(new ParameterNamesModule())
                           .addModule(new Jdk8Module())
                           .addModule(new JavaTimeModule())
                .build();

        MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        MAPPER.setTimeZone(TimeZone.getDefault());
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JacksonJsonProvider() {}

    public static ObjectMapper getInstance() {
        return MAPPER;
    }

}
