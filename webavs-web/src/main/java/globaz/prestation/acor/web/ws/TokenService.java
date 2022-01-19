package globaz.prestation.acor.web.ws;

public interface TokenService<T extends Token> {
    T convertToken(String token);
}
