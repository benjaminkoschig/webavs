package globaz.utils;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

/**
 * Cette class doit être utilisé uniquement dans le cas de test. Elle permet de redéfinir les valeurs des propriétés
 * lors du test sans affecter les valeurs stockées en base de données
 * 
 * @author lga
 */
public class CommonPropertiesUtilsTest extends CommonPropertiesUtils {

    private static final Map<IProperties, String> overridedProperties = new HashMap<IProperties, String>();

    /**
     * Redéfinit la value d'une propriété. <strong>ATTENTION : Aucun test n'est effectué pour savoir si la propriété
     * existe en base de données</strong>
     * 
     * @param property
     *            La propriété dont la valeur doit être redéfinie
     * @param value
     *            La valeur de la propriété
     */
    public static void setPropertyValue(IProperties property, String value) {
        CommonPropertiesUtils.getInstance();
        CommonPropertiesUtilsTest.overridedProperties.put(property, value);
    }

    /**
     * Permet de redéfinir les valeur d'un ensemble de propriétés <strong>ATTENTION : Aucun test n'est effectué pour
     * savoir si les propriétés existent en base de données</strong>
     * 
     * @param overridedProperties
     *            Un map avec l'ensemble des propriétés à redéfinir
     */
    public static void setPropertyValues(Map<IProperties, String> overridedProperties) {
        CommonPropertiesUtils.getInstance();
        for (Object prop : overridedProperties.keySet().toArray()) {
            CommonPropertiesUtilsTest.overridedProperties.put((IProperties) prop, overridedProperties.get(prop));
        }
    }

    private CommonPropertiesUtilsTest() {

    }

    @Override
    protected String getPropertyValue(IProperties properties) throws RemoteException, PropertiesException {
        if (CommonPropertiesUtilsTest.overridedProperties.containsKey(properties)) {
            return CommonPropertiesUtilsTest.overridedProperties.get(properties);
        } else {
            return super.getPropertyValue(properties);
        }
    }

}
