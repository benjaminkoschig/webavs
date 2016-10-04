package ch.globaz.common.properties;

import globaz.globall.api.GlobazSystem;

/**
 * Permet de déclarer les propriété communes aux différents modules de l'application. ATTENTION : ne pas indiquer le
 * prefix common. dans le nom de la propriété !!!
 */
public enum CommonProperties implements IProperties {
    KEY_NO_CAISSE("noCaisse", "Num caisse"),

    KEY_NO_CAISSE_FORMATE("noCaisseFormate", "noCaisseFormate"),

    NUMERO_AGENCE("noAgence", "Le numéro d'agence"),
    /**
     * Propriété boolean [true, false]</br> Renseigne si la caisse utilise des sous-types dans les genre de prestation
     * PC ET RFM
     */
    SOUS_TYPE_GENRE_PRESTATION_ACTIF(
            "prestation.sousTypesGenrePrestationActif",
            "Cette propriété permet de savoir si les prestations accordées possèdent un sous type prestations. "
                    + "Ce sous type genre prestation est principalement utilisé pour la compta afin de pouvoir repartir plus "
                    + "précisément les versements des prestations dans les rubriques comptables. Pour l'instant cette propriété est utilisé par CORVUS, CYGNUS, PEGASUS"),

    /**
     * Propriété boolean [true, false]</br> Permet d'activer l'affichage des communes politiques sur un ensemble de
     * listes des prestations (PC, RFM, IJ, RENTES)
     */
    ADD_COMMUNE_POLITIQUE("liste.ajouter.commune.politique",
            "Enrichit une partie des listes PC, RFM, IJ et rentes avec le commune politiques"),

    KEYSTORE_PATH("keystore.path", "Chemin absolu du keystore"),
    KEYSTORE_TYPE("keystore.type", "Type du keystore"),
    KEYSTORE_PASSWORD("keystore.password", "Password du keystore");

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
