package ch.globaz.common.util;

import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.PropertiesException;

/**
 * Pojo regroupant les informations du numero de caisse
 * Sous forme de Singleton
 *
 * @author sce
 */
public class CaisseInfoPropertiesWrapper {

    private String noCaisse = null;
    private String noAgence = null;
    private String noCaisseFormatee = null;

    private static final CaisseInfoPropertiesWrapper instance = new CaisseInfoPropertiesWrapper();

    private CaisseInfoPropertiesWrapper() {
        loadCaisseProperties();
    }

    private void loadCaisseProperties() {
        try {
            this.noCaisse = CommonPropertiesUtils.getValue(CommonProperties.KEY_NO_CAISSE);
            this.noAgence = CommonPropertiesUtils.getValue(CommonProperties.NUMERO_AGENCE);
            this.noCaisseFormatee = CommonPropertiesUtils.getValue(CommonProperties.KEY_NO_CAISSE_FORMATE);
        } catch (PropertiesException e) {
            throw new IllegalArgumentException("A problem occured with a properties needed to instantiate the wrapper", e);
        }
    }

    public String getNoCaisse() {
        return noCaisse;
    }

    public String getNoAgence() {
        return noAgence;
    }

    public String getNoCaisseFormatee() {
        return noCaisseFormatee;
    }

    public static String noAgence() {
        return getInstance().noAgence;
    }

    public static String noCaisse() {
        return getInstance().noCaisse;
    }

    public static String noCaisseNoAgence() {
        return noCaisse() + noAgence();
    }

    public static CaisseInfoPropertiesWrapper getInstance() {
        return instance;
    }
}
