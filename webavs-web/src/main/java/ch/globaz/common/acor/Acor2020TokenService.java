package ch.globaz.common.acor;

public interface Acor2020TokenService<T extends Acor2020Token > {

    T getToken(String token);

    String createToken();

}
