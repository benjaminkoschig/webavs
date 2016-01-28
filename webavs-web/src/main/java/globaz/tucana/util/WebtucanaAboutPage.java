package globaz.tucana.util;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WebtucanaAboutPage {

    private static Properties prop = null;

    private static final String PROP_DATE = "date";

    private static final String PROP_NAME = "name";
    private static final String PROP_VERSION = "version";
    private static final String PROP_VERSION_SERVICEPACK = "version.servicepack";
    private static final String VERSION_PROP_FILE = "/webtucana.version";

    private static final String WEBCONTENTS_DOCUMENTS_PATH = "documents/";

    /**
     * Retourne la date de version actuelle.
     * 
     * @return
     */
    public static String getDate() {
        try {
            loadProperties();

            return prop.getProperty(PROP_DATE);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Retourne l'emplacement de la documentation. Si non spécifiée dans Jade.xml => documents/ sous répertoire de
     * webavs/Web Content.
     * 
     * @return
     */
    public static String getDocumentationLocation() {
        String documentUrl = "";
        if (JadeStringUtil.isBlank(Jade.getInstance().getDocumentationUrl())) {
            documentUrl = WEBCONTENTS_DOCUMENTS_PATH;
        } else {
            if (!Jade.getInstance().getDocumentationUrl().endsWith("/")) {
                documentUrl = Jade.getInstance().getDocumentationUrl() + "/";
            } else {
                documentUrl = Jade.getInstance().getDocumentationUrl();
            }
        }

        return documentUrl + getName() + "/" + getVersion() + "/";
    }

    /**
     * Retourne le nom du projet (webavs).
     * 
     * @return
     */
    public static String getName() {
        try {
            loadProperties();

            return prop.getProperty(PROP_NAME);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Retourne le numéro de version du service pack actuelle.
     * 
     * @return
     */
    public static String getServicePackVersion() {
        try {
            loadProperties();

            return prop.getProperty(PROP_VERSION_SERVICEPACK);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Retourne le numéro de version actuelle.
     * 
     * @return
     */
    public static String getVersion() {
        try {
            loadProperties();

            return prop.getProperty(PROP_VERSION);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Charge les propriétés webavs.version.
     * 
     */
    private static void loadProperties() {
        if (prop == null) {
            try {
                prop = new Properties();
                InputStream is = WebtucanaAboutPage.class.getResourceAsStream(VERSION_PROP_FILE);
                prop.load(is);
            } catch (IOException e) {
                prop = null;
            }
        }
    }
}
