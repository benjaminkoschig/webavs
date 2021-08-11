package ch.globaz.common.acor;

public interface Acor2020TokenService {

    Acor2020Token getToken(String token);

    String createToken();

}
