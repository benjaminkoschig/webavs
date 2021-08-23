package ch.globaz.common.acor;

public interface Acor2020TokenService<T extends Acor2020Token> {
    T convertToken(String token);

    // T creatToken(Supplier<T> supplier);
}
