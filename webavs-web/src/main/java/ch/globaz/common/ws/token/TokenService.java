package ch.globaz.common.ws.token;

public interface TokenService<T extends Token> {
    T convertToken(String token);
}
