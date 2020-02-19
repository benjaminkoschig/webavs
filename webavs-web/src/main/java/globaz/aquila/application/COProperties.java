package globaz.aquila.application;

import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

public enum COProperties implements IProperties {

    AJOUT_INFOS_LISTE_PROVISIONNELLE_POURSUITES("ajout.infos.liste.provisionnelle.poursuites",
            "Permet d'ajouter des informations supplémentaires sur la liste provisionnelle des poursuites",
            Boolean.class),
    RECEPTION_MESSAGES_ELP("aquila.receptionMessagesELP",
            "Permet d’activer et de désactiver le traitement automatiquement des messages eLP", Boolean.class),
    REPERTOIRE_GESTION_MESSAGES_ELP("aquila.repertoireGestionMessagesELP",
            "Indique l’emplacement du répertoire pour la gestion des messages eLP à traiter", String.class),
    DESTINATAIRE_PROTOCOLE_MESSAGES_ELP("aquila.destinataireProtocoleMessagesELP",
                                               "Destinataire du courriel de la génération du protocol des messages eLP", String.class);

    private String property;
    private String description;
    private Class<?> type;

    COProperties(String prop, String description, Class<?> type) {
        property = prop;
        this.type = type;
        this.description = description;
    }

    @Override
    public String getApplicationName() {
        return "AQUILA";
    }

    @Override
    public Boolean getBooleanValue() throws PropertiesException {
        return CommonPropertiesUtils.getBoolean(this);
    }

    @Override
    public String getDescription() {
        return description;
    }

    public String getProperty() {
        return property;
    }

    @Override
    public String getPropertyName() {
        return property;
    }

    @Override
    public String getValue() throws PropertiesException {
        return CommonPropertiesUtils.getValue(this);
    }

    public Class<?> getValueType() {
        return type;
    }
}
