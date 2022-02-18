package ch.globaz.common.ws.token;

public interface Token {

    Token setUserId(String userId);

    String getUserId();

    Token setLangue(String langue);

    String getLangue();

    Token setEmail(String email);

    String getEmail();

}
