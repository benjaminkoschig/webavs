package globaz.prestation.acor.acor2020.ws;

public interface Acor2020TokenService<T extends Acor2020Token> {
    T convertToken(String token);

    // T creatToken(Supplier<T> supplier);
}
