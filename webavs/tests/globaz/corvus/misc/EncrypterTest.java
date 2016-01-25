package globaz.corvus.misc;

import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.crypto.JadeEncrypter;
import globaz.jade.crypto.JadeEncrypterLocator;
import org.junit.Ignore;
import org.junit.Test;

public class EncrypterTest /* extends AbstractTestCaseWithContext */{

    @Ignore
    @Test
    public void decrypt() {
        try {
            System.out.println("Valeur décryptée : ");
            JadeEncrypter encrypter = JadeEncrypterLocator.getInstance().getAdapter(
                    JadeDefaultEncrypters.JADE_DEFAULT_ENCRYPTER);
            String user = encrypter.decrypt("hKgeaEfOc16IBHYnnkxGvA==");
            String password = encrypter.decrypt("/PEyKODVOEE=");
            System.out.println("INT  user = " + user);
            System.out.println("INT  password = " + password);

            user = encrypter.decrypt("t561uzhfm+U=");
            password = encrypter.decrypt("P4OJAJRCLu8=");
            System.out.println("PROD user = " + user);
            System.out.println("PROD password = " + password);
            System.out.println();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // @Override
    // protected String getApplicationName() {
    // return TestConfig.getDefaultConfig().getApplicationName();
    // }
    //
    // @Override
    // protected String getUserName() {
    // return TestConfig.getDefaultConfig().getUserName();
    // }
    //
    // @Override
    // protected String getUserPassword() {
    // return TestConfig.getDefaultConfig().getUserPassword();
    // }

}
