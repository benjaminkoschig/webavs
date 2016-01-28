package globaz.utils;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

/**
 * Cette class doit �tre utilis� uniquement dans le cas de test. Elle permet de red�finir les valeurs des propri�t�s
 * lors du test sans affecter les valeurs stock�es en base de donn�es
 * 
 * @author lga
 */
public class CommonPropertiesUtilsTest extends CommonPropertiesUtils {

    private static final Map<IProperties, String> overridedProperties = new HashMap<IProperties, String>();

    /**
     * Red�finit la value d'une propri�t�. <strong>ATTENTION : Aucun test n'est effectu� pour savoir si la propri�t�
     * existe en base de donn�es</strong>
     * 
     * @param property
     *            La propri�t� dont la valeur doit �tre red�finie
     * @param value
     *            La valeur de la propri�t�
     */
    public static void setPropertyValue(IProperties property, String value) {
        CommonPropertiesUtils.getInstance();
        CommonPropertiesUtilsTest.overridedProperties.put(property, value);
    }

    /**
     * Permet de red�finir les valeur d'un ensemble de propri�t�s <strong>ATTENTION : Aucun test n'est effectu� pour
     * savoir si les propri�t�s existent en base de donn�es</strong>
     * 
     * @param overridedProperties
     *            Un map avec l'ensemble des propri�t�s � red�finir
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
