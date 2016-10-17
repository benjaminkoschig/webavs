package ch.globaz.common.properties;

import globaz.globall.api.GlobazSystem;

/**
 * Permet de d�clarer les propri�t� communes aux diff�rents modules de l'application. ATTENTION : ne pas indiquer le
 * prefix common. dans le nom de la propri�t� !!!
 */
public enum CommonProperties implements IProperties {
    KEY_NO_CAISSE("noCaisse", "Num caisse"),

    KEY_NO_CAISSE_FORMATE("noCaisseFormate", "noCaisseFormate"),

    NUMERO_AGENCE("noAgence", "Le num�ro d'agence"),
    /**
     * Propri�t� boolean [true, false]</br> Renseigne si la caisse utilise des sous-types dans les genre de prestation
     * PC ET RFM
     */
    SOUS_TYPE_GENRE_PRESTATION_ACTIF(
            "prestation.sousTypesGenrePrestationActif",
            "Cette propri�t� permet de savoir si les prestations accord�es poss�dent un sous type prestations. "
                    + "Ce sous type genre prestation est principalement utilis� pour la compta afin de pouvoir repartir plus "
                    + "pr�cis�ment les versements des prestations dans les rubriques comptables. Pour l'instant cette propri�t� est utilis� par CORVUS, CYGNUS, PEGASUS"),

    /**
     * Propri�t� boolean [true, false]</br> Permet d'activer l'affichage des communes politiques sur un ensemble de
     * listes des prestations (PC, RFM, IJ, RENTES)
     */
    ADD_COMMUNE_POLITIQUE("liste.ajouter.commune.politique",
            "Enrichit une partie des listes PC, RFM, IJ et rentes avec le commune politiques"),
    WIRRWEBSERVICE_NAMESPACE("wirr.webservice.namespace", "name space du web service wirr"),
    WIRRWEBSERVICE_NAME("wirr.webservice.name", "name du web service wirr"),
    WIRRWEBSERVICE_SEDEX_SENDER_ID("wirr.webservice.sedex.sender.id", "sedex sender id pour le web service wirr"),

    WIRR_KEYSTORE_PATH("wirr.keystore.path", "Chemin absolu du keystore"),
    WIRR_KEYSTORE_TYPE("wirr.keystore.type", "Type du keystore"),
    WIRR_KEYSTORE_PASSWORD("wirr.keystore.password", "Password du keystore"),
    WIRR_SSL_CONTEXT_TYPE("wirr.ssl.context.type", "type du context ssl");

    private String description;
    private String propertyName;

    CommonProperties(String propertyName, String description) {
        this.propertyName = propertyName;
        this.description = description;
    }

    /**
     * Retourne le nom de l'application Lyra.
     */
    @Override
    public String getApplicationName() {
        return GlobazSystem.APPLICATION_FRAMEWORK;
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
