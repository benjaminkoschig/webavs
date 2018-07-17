package globaz.helios.application;

import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;
import globaz.helios.application.CGApplication;
import globaz.ij.application.IJApplication;

public enum CGProperties  implements IProperties {
    
    ACTIVER_ANNONCES_XML("isAnnoncesXML", "détermine si les annonces sont générées au format xml"),
    RACINE_NOM_FICHIER_OUTPUT_ZAS("racine.nom.fichier.centrale",
            "donne la raçine nom du fichier à envoyer à la centrale"),
    FTP_CENTRALE_PATH("centrale.url", "donne l'url de la centrale"),
    CENTRALE_TEST("centrale.test",
            "définit si nous sommes en mode test pour mettre la balise test dans le fichier output de la centrale");
    
    private String description;
    private String propertyName;

    CGProperties(String propertyName, String description) {
        this.propertyName = propertyName;
        this.description = description;
    }

    @Override
    public String getApplicationName() {
        return CGApplication.DEFAULT_APPLICATION_HELIOS;
    }

    @Override
    public Boolean getBooleanValue() throws PropertiesException {
        return CommonPropertiesUtils.getBoolean(this);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public String getValue() throws PropertiesException {
        return CommonPropertiesUtils.getValue(this);
    }

}
