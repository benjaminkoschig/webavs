package ch.globaz.orion.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import ch.globaz.orion.ws.affiliation.WebAvsAffiliationService;
import ch.globaz.orion.ws.comptabilite.WebAvsComptabiliteService;
import ch.globaz.orion.ws.cotisation.WebAvsCotisationsService;

public final class AVSEBProvider {
    private static final String FILE_PROPERTIES_WEBAVS_API = "webavs-api.properties";
    private static final String PROP_BACK_WS_URL = "webavs.ws.url";

    private static final String WEBAVS_COTISATION_WSDL = "/webAvsCotisationsService?wsdl";
    private static final String WEBAVS_COTISATION_NS_URI = "http://cotisation.ws.orion.globaz.ch/";
    private static final String WEBAVS_COTISATION_LOCAL_PART = "WebAvsCotisationsServiceImplService";

    private static final String WEBAVS_COMPTABILITE_WSDL = "/webAvsComptabiliteService?wsdl";
    private static final String WEBAVS_COMPTABILITE_NS_URI = "http://comptabilite.ws.orion.globaz.ch/";
    private static final String WEBAVS_COMPTABILITE_LOCAL_PART = "WebAvsComptabiliteServiceImplService";

    private static final String WEBAVS_AFFILIATION_WSDL = "/webAvsAffiliationService?wsdl";
    private static final String WEBAVS_AFFILIATION_NS_URI = "http://affiliation.ws.orion.globaz.ch/";
    private static final String WEBAVS_AFFILIATION_LOCAL_PART = "WebAvsAffiliationServiceImplService";

    private static Logger log = Logger.getLogger(AVSEBProvider.class.getName());
    private static Properties prop = null;
    private static Service webAvsCotisationService = null;
    private static Service webAvsComptabiliteService = null;
    private static Service webAvsAffiliationService = null;

    private AVSEBProvider() {
    };

    private static void init() throws IOException {
        if (prop == null) {
            prop = new Properties();
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(FILE_PROPERTIES_WEBAVS_API));
        }
    }

    private static String getAVSUrl() throws MalformedURLException, IOException {
        String wsBackUrl = prop.getProperty(PROP_BACK_WS_URL);
        return wsBackUrl;
    }

    /**
     * Retourne une instance du service WebavsCotisation
     * 
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public static WebAvsCotisationsService getWebavsCotisationService() throws MalformedURLException, IOException {
        if (webAvsCotisationService == null) {
            init();
            String wsBackUrl = getAVSUrl();
            if (wsBackUrl == null) {
                log.severe("Missing property : " + PROP_BACK_WS_URL);
            }
            webAvsCotisationService = Service.create(new URL(wsBackUrl + WEBAVS_COTISATION_WSDL), new QName(
                    WEBAVS_COTISATION_NS_URI, WEBAVS_COTISATION_LOCAL_PART));
        }
        WebAvsCotisationsService theWebAvsCotisationService = webAvsCotisationService
                .getPort(WebAvsCotisationsService.class);
        return theWebAvsCotisationService;
    }

    /**
     * Retourne une instance du service WebavsComptabilite
     * 
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public static WebAvsComptabiliteService getWebavsComptabiliteService() throws MalformedURLException, IOException {
        if (webAvsComptabiliteService == null) {
            init();
            String wsBackUrl = getAVSUrl();
            if (wsBackUrl == null) {
                log.severe("Missing property : " + PROP_BACK_WS_URL);
            }
            webAvsComptabiliteService = Service.create(new URL(wsBackUrl + WEBAVS_COMPTABILITE_WSDL), new QName(
                    WEBAVS_COMPTABILITE_NS_URI, WEBAVS_COMPTABILITE_LOCAL_PART));
        }
        WebAvsComptabiliteService theWebAvsComptabiliteService = webAvsComptabiliteService
                .getPort(WebAvsComptabiliteService.class);
        return theWebAvsComptabiliteService;
    }

    /**
     * Retourne une instance du service WebavsAffiliation
     * 
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public static WebAvsAffiliationService getWebavsAffiliationService() throws MalformedURLException, IOException {
        if (webAvsAffiliationService == null) {
            init();
            String wsBackUrl = getAVSUrl();
            if (wsBackUrl == null) {
                log.severe("Missing property : " + PROP_BACK_WS_URL);
            }
            webAvsAffiliationService = Service.create(new URL(wsBackUrl + WEBAVS_AFFILIATION_WSDL), new QName(
                    WEBAVS_AFFILIATION_NS_URI, WEBAVS_AFFILIATION_LOCAL_PART));
        }

        return webAvsAffiliationService.getPort(WebAvsAffiliationService.class);

    }

}
