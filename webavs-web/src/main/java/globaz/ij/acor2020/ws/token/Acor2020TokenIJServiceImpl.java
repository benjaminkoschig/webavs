package globaz.ij.acor2020.ws.token;

import ch.globaz.common.acor.Acor2020Token;
import ch.globaz.common.acor.Acor2020TokenService;

public class Acor2020TokenIJServiceImpl implements Acor2020TokenService {

    private static Acor2020TokenIJServiceImpl instance;

    // static method to create instance of Singleton class
    public static Acor2020TokenIJServiceImpl getInstance()
    {
        if (instance == null)
            instance = new Acor2020TokenIJServiceImpl();

        return instance;
    }

    private Acor2020TokenIJServiceImpl() {
    }

    @Override
    public Acor2020Token getToken(String token) {
        Acor2020TokenIJ acor2020TokenIJ = new Acor2020TokenIJ();
        return acor2020TokenIJ;
    }

    @Override
    public String createToken() {
        return null;
    }
}
