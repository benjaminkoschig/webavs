package globaz.itucana.properties;

import globaz.itucana.exception.TUInitPropertiesException;
import globaz.jade.client.util.JadeUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe permettant de charger les properties
 * 
 * @author fgo date de création : 7 juin 2006
 * @version : version 1.0
 * 
 */
public class TUPropertiesProvider {
    private static String PROPERTIES_NAME = "itucana.properties";

    private static TUPropertiesProvider provider = null;

    /**
     * @return
     * @throws TUInitPropertiesException
     */
    public static TUPropertiesProvider getInstance() throws TUInitPropertiesException {
        if (provider == null) {
            provider = new TUPropertiesProvider();
            provider.loadProperties();
        }
        return provider;
    }

    private Properties properties = null;

    /**
     * Constructeur
     */
    private TUPropertiesProvider() {
        super();
        properties = new Properties();
    }

    /**
     * @param key
     * @return
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * @throws TUInitPropertiesException
     * 
     */
    private void loadProperties() throws TUInitPropertiesException {
        try {
            InputStream is = JadeUtil.getGlobazInputStream(PROPERTIES_NAME);
            properties.load(is);
            is.close();
        } catch (IOException e) {
            throw new TUInitPropertiesException(e);
        }
    }
}
