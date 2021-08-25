package ch.globaz.common.properties;

import ch.globaz.common.exceptions.CommonTechnicalException;
import globaz.globall.api.BIApplication;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSystem;
import globaz.jade.client.util.JadeStringUtil;
import lombok.extern.slf4j.Slf4j;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CommonPropertiesUtils {

    private static CommonPropertiesUtils instance;

    private static BIApplication getApplication(final String nameApplication) throws PropertiesException {
        try {
            return GlobazSystem.getApplication(nameApplication);
        } catch (final Exception e) {
            throw new PropertiesException("Unable to obtain the " + nameApplication + " application", e);
        }
    }

    /**
     * Permet de transformer un valeur boolean d'une properties. Si on arrive pas à convertir la properties une
     * exception est lancé.
     * 
     * @param properties
     *            La propriété voulue
     * @return La valeur de la property sous forme booléenne. Si la valeur de la propriété est null ou vide une
     *         Exception sera lancée
     * @throws PropertiesException
     *             Si la valeur de la propriété est null ou vide
     */
    public static Boolean getBoolean(final IProperties properties) throws PropertiesException {
        CommonPropertiesUtils.getInstance();
        final String value = CommonPropertiesUtils.getValue(properties);

        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            return Boolean.parseBoolean(value);
        } else {
            throw new PropertiesException("The value (" + value + ") for the properties" + properties.toString()
                    + "is not a boolean value");
        }
    }

    /**
     * Permet de transformer un valeur en integer d'une properties. Si on arrive pas à convertir la properties une
     * exception est lancé.
     *
     * @param properties
     *            La propriété voulue
     * @return La valeur de la property sous forme booléenne. Si la valeur de la propriété est null ou vide une
     *         Exception sera lancée
     * @throws PropertiesException
     *             Si la valeur de la propriété est null ou vide
     */
    public static Integer getInteger(final IProperties properties) throws PropertiesException {
        final String value = CommonPropertiesUtils.getValue(properties);
        return toInteger(properties, value);
    }

    /**
     * Permet de transformer un valeur en integer d'une properties. Si on arrive pas à convertir la properties une
     * exception est lancé.
     *
     * @param properties
     *            La propriété voulue
     * @param defaultValue
     *            La valeur par défaut si la properties n'existe pas.
     * @return La valeur de la property sous forme booléenne. Si la valeur de la propriété est null ou vide une
     *         Exception sera lancée
     */
    public static Integer getIntegerWithDefaultValue(final IProperties properties, String defaultValue)  {
        final String value = CommonPropertiesUtils.getValueWithDefault(properties,defaultValue);
        return toInteger(properties, value);
    }

    private static Integer toInteger(final IProperties properties, final String value) {
        try {
            if(value == null){
                return null;
            }
            return Integer.valueOf(value);
        }catch (NumberFormatException e){
            throw new CommonTechnicalException("The value (" + value + ") for the properties" + properties.toString()
                                                  + "is not a integer value");
        }
    }

    /**
     * Ne doit pas être appelé directement. Cette class est directement utilisé par CommonProperties pour résoudre la
     * valeur des propriétés
     * 
     * @return L'unique instance de CommonPropertiesUtils
     */
    public static CommonPropertiesUtils getInstance() {
        if (CommonPropertiesUtils.instance == null) {
            synchronized (CommonPropertiesUtils.class) {
                if (CommonPropertiesUtils.instance == null) {
                    CommonPropertiesUtils.instance = new CommonPropertiesUtils();
                }
            }
        }
        return CommonPropertiesUtils.instance;
    }

    /**
     * Permet de retrouver la valeur de la properties si cette properties n'existe pas on lance une exception.
     * 
     * @param properties
     *            La propriété voulue
     * @return La valeur de la propriété en String
     * @throws PropertiesException
     *             Si la propriété n'est pas trouvé
     */
    public static String getValue(final IProperties properties) throws PropertiesException {
        try {
            final String value = CommonPropertiesUtils.getInstance().getPropertyValue(properties);

            if (value == null) {
                // Ceci n'est pas trés beau mais cette class et faite pour être utilser dans une application autre que
                // le FW.
                // Comme il faut une application pour trouvé une properties on utilise l'application du FW afin de
                // pouvoir trouver les common.
                String idApplication = properties.getApplicationName().toLowerCase();
                if (GlobazSystem.APPLICATION_FRAMEWORK.equalsIgnoreCase(idApplication)) {
                    idApplication = BSystem.COMMON_PREFIX;
                }
                throw new PropertiesException("The properties [" + idApplication + "." + properties.getPropertyName()
                        + "] doesn't exist. Desc: " + properties.getDescription());
            }
            return value;
        } catch (final RemoteException e) {
            throw new PropertiesException("Remote Exeption", e);
        }
    }

    /**
     * Permet de retrouver la valeur de la properties si cette properties n'existe pas on lance une exception.
     *
     * @param properties
     *            La propriété voulue
     * @return La valeur de la propriété en String

     */
    public static String getValueWithDefault(final IProperties properties, String defaultValue)  {
        try {
            final String value = CommonPropertiesUtils.getInstance().getPropertyValue(properties);
            if (value == null) {
                LOG.warn("The Default value is used for this property : [{}], Desc: {}", properties.getPropertyName(), properties.getDescription());
                return defaultValue;
            }
            return value;
        } catch (final RemoteException | PropertiesException e ) {
            LOG.warn("Exception happened, the Default value is used for this property : [{}], Desc: {}", properties.getPropertyName(),
                     properties.getDescription(), e);
            return defaultValue;
        }
    }

    public static String getValueWithoutException(final IProperties properties) throws PropertiesException {
        try {
            return CommonPropertiesUtils.getInstance().getPropertyValue(properties);
        } catch (final RemoteException e) {
            throw new PropertiesException("Remote Exception", e);
        }
    }

    protected CommonPropertiesUtils() {

    }

    protected String getPropertyValue(final IProperties properties) throws RemoteException, PropertiesException {
        return CommonPropertiesUtils.getApplication(properties.getApplicationName()).getProperty(
                properties.getPropertyName());
    }

    /**
     * Valide que la valeur passé en argument correspond à la chaîne 'true' ou 'false'
     * 
     * @param value
     *            La valeur à tester
     * @return <code>true</code> si la valeur passé en argument correspond à la chaîne 'true' ou 'false'
     */
    public static boolean isValidBooleanPropertyValue(final String value) {
        if (Boolean.TRUE.toString().toLowerCase().equals(value) || Boolean.FALSE.toString().toLowerCase().equals(value)) {
            return true;
        }
        return false;
    }

    /**
     * Contrôle que la valeur fourie en paramètre soit un nombre entier
     * 
     * @param value
     * @return
     */
    public static boolean isValidIntegerPropertyValue(final String value) {
        try {
            Integer.valueOf(value);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    /**
     * Contrôle que la valeur passée en argument soit égale à une chaîne contenue dans la liste values.
     * 
     * @param value
     *            La valeur à évaluer. Ne doit pas être null ou une chaîne vide
     * @return <code>true</code> si la chaîne value n'est pas null ou vide et correspond à une chaîne de caractère
     *         trouvée dans la liste
     */
    public static boolean isValidPropertyValue(final String propertyValue, final List<String> values) {
        final boolean result = !JadeStringUtil.isEmpty(propertyValue);
        if (result) {
            for (final String s : values) {
                if (JadeStringUtil.isEmpty(s) & s.equals(propertyValue)) {
                    return true;
                }
            }
        }
        return result;
    }

    /**
     * Valide la valeur d'une propriété en fonction de la liste de valeurs 'values' passées en arguments
     * 
     * @param property
     *            La propriété en question. Ne doit pas être null ou vide
     * @param propertyValue
     *            La valeur de propriété. Ne doit pas être null ou une chaîne vide
     * @param values
     *            La liste des valeurs possible pour la propriété
     * @throws PropertiesException
     *             Lance une exception si : - la propertyValue est null</br> - propertyValue est vide </br> -
     *             propertyValue ne correspond à aucune des valeurs définie dans la liste passée en argument</br>
     */
    public static void validatePropertyValue(final IProperties property, final String propertyValue,
            final List<String> values) throws PropertiesException {
        final boolean found = CommonPropertiesUtils.isValidPropertyValue(propertyValue, values);
        if (!found) {
            final StringBuilder message = new StringBuilder();
            message.append("The property value [" + propertyValue + "] is invalid for property with name ["
                    + property.getApplicationName() + "." + property.getPropertyName() + "]");
            if (values != null) {
                message.append(" Possible values for property are [" + values.toString() + "]");
            }
            throw new PropertiesException(message.toString());
        }
    }

    /**
     * Retourne true si la property est vide ou null
     * 
     * @param property
     * @return
     * @throws PropertiesException
     */
    public static boolean isEmpty(final IProperties property) throws PropertiesException {
        return isEmptyValue(property.getValue());
    }

    static boolean isEmptyValue(String value) {
        if (null == value || value.trim().length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * /**
     * Permet de retrouver la valeur de la properties si cette properties n'existe pas on lance une exception.
     * La propriété définit une liste de valeur séparée par une virgule.
     * Le résultat est retourné sous forme de liste de valeur
     * 
     * @param properties La propriété voulue
     * @return Une liste de valeur
     * @throws PropertiesException
     */
    public static List<String> getListValue(final IProperties properties) throws PropertiesException {
        return decoupeStringValue(CommonPropertiesUtils.getValue(properties));
    }

    static List<String> decoupeStringValue(String values) throws PropertiesException {

        if (CommonPropertiesUtils.isEmptyValue(values)) {
            return new ArrayList<String>();
        }

        String[] decoupe = values.trim().split("\\s*,\\s*");
        return Arrays.asList(decoupe);
    }
}
