package ch.globaz.common.properties;

public interface IProperties {

    /**
     * Doit retourner le nom de l'application qui permet de trouver un BIApplication
     * 
     * @return l'identifiant de l'application
     */
    public String getApplicationName();

    /**
     * @return la valeur boolean de la properties
     * @throws Exception
     */
    public Boolean getBooleanValue() throws PropertiesException;

    /**
     * @return Texte décrivant la properties
     */
    public String getDescription();

    /**
     * @return le nom de la properties
     */
    public String getPropertyName();

    /**
     * @return La valeur de la properties
     * @throws Exception
     */
    public String getValue() throws PropertiesException;

}
