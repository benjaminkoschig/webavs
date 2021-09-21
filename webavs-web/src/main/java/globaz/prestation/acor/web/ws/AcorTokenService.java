package globaz.prestation.acor.web.ws;

public interface AcorTokenService<T extends AcorToken> {
    T convertToken(String token);

    // T creatToken(Supplier<T> supplier);
}
