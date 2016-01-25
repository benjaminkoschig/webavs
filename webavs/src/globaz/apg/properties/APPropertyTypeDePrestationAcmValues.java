package globaz.apg.properties;

import java.util.ArrayList;
import java.util.List;

/**
 * Ce type énuméré définit le domain de valeur de la property @see APProperties.TYPE_DE_PRESTATION_ACM
 * 
 * @author lga
 */

public enum APPropertyTypeDePrestationAcmValues {

    ACM_ALFA("ACM_ALFA"),
    ACM_NE("ACM_NE"),
    NONE("NONE");

    private String propertyValue;

    private APPropertyTypeDePrestationAcmValues(final String value) {
        propertyValue = value;
    }

    /**
     * @return the value
     */
    public final String getPropertyValue() {
        return propertyValue;
    }

    /**
     * Retourne la liste des valeurs possible pour la propriété @see APProperties.TYPE_DE_PRESTATION_ACM
     * 
     * @return la liste des valeurs possible pour la propriété @see APProperties.TYPE_DE_PRESTATION_ACM
     */
    public static List<String> propertyValues() {
        final List<String> values = new ArrayList<String>();
        for (final APPropertyTypeDePrestationAcmValues prop : APPropertyTypeDePrestationAcmValues.values()) {
            values.add(prop.getPropertyValue());
        }
        return values;
    }
}
