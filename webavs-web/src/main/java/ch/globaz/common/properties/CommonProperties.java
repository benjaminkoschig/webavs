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
    WIRRWEBSERVICE_NAMESPACE("wirr.webservice.namespace", "name space du web service wirr"),
    WIRRWEBSERVICE_NAME("wirr.webservice.name", "name du web service wirr"),
    WIRRWEBSERVICE_SEDEX_SENDER_ID("wirr.webservice.sedex.sender.id", "sedex sender id pour le web service wirr"),
    WIRR_KEYSTORE_PATH("wirr.keystore.path", "Chemin absolu du keystore"),
    WIRR_KEYSTORE_TYPE("wirr.keystore.type", "Type du keystore"),
    WIRR_KEYSTORE_PASSWORD("wirr.keystore.password", "Password du keystore"),
    WIRR_SSL_CONTEXT_TYPE("wirr.ssl.context.type", "type du context ssl"),
    WIRR_ENDPOINT_ADDRESS("wirr.endpoint.address", "URL de surcharge de la wsdl"),
    WIRR_WSDL_RESOURCE_PATH("wirr.wsdl.path", "Chemin de la wsdl"),
    EBUSINESS_CONNECTED("ebusiness.connected", "Indique si un EBusiness est connecté avec WebAVS"),

    UPI_WEBSERVICE_NAMESPACE("upi.webservice.namespace", "name space du web service upi"),
    UPI_WEBSERVICE_NAME("upi.webservice.name", "name du web service upi"),
    UPI_WEBSERVICE_SEDEX_SENDER_ID("upi.webservice.sedex.sender.id", "sedex sender id pour le web service wirr"),
    UPI_KEYSTORE_PATH("upi.keystore.path", "Chemin absolu du keystore"),
    UPI_KEYSTORE_TYPE("upi.keystore.type", "Type du keystore"),
    UPI_KEYSTORE_PASSWORD("upi.keystore.password", "Password du keystore"),
    UPI_SSL_CONTEXT_TYPE("upi.ssl.context.type", "type du context ssl"),
    UPI_WSDL_RESOURCE_PATH("upi.wsdl.path", "Chemin de la wsdl"),
    UPI_ENDPOINT_ADDRESS("upi.endpoint.address", "URL de surcharge de la wsdl"),

    RAPG_ENDPOINT_ADDRESS("rapg.endpoint.address","adresse endpoint"),
    RAPG_KEYSTORE_PASSWORD("rapg.keystore.password","adresse endpoint"),
    RAPG_KEYSTORE_PATH("rapg.keystore.path","adresse endpoint"),
    RAPG_KEYSTORE_TYPE("rapg.keystore.type","adresse endpoint"),
    RAPG_SEODOR_WSDL_PATH("rapg.seodor.wsdl.path","adresse endpoint"),
    RAPG_WEBSERVICE_WSDL_PATH("rapg.webservice.wsdl.path","adresse endpoint"),
    RAPG_SSL_CONTEXT_TYPE("rapg.ssi.context.type","adresse endpoint"),
    RAPG_WEBSERVICE_NAME("rapg.webservice.name","adresse endpoint"),
    RAPG_WEBSERVICE_NAMESPACE("rapg.webservice.namespace","adresse endpoint"),
    RAPG_WEBSERVICE_SEDEX_SENDER_ID("rapg.webservice.sedex.sender.id","adresse endpoint"),

    SEODOR_ENDPOINT_ADDRESS("seodor.endpoint.address","adresse endpoint"),
    SEODOR_KEYSTORE_PASSWORD("seodor.keystore.password","Password KeyStore"),
    SEODOR_KEYSTORE_PATH("seodor.keystore.path","Path keystore"),
    SEODOR_KEYSTORE_TYPE("seodor.keystore.type","Type keystore"),
    SEODOR_SEODOR_WSDL_PATH("seodor.wsdl.path","Path Wsdl Seodor"),
    SEODOR_SSL_CONTEXT_TYPE("seodor.ssi.context.type","Type Context Seodor"),
    SEODOR_WEBSERVICE_NAME("seodor.webservice.name","Webservice Name Seodor"),
    SEODOR_WEBSERVICE_NAMESPACE("seodor.webservice.namespace","WebService NameSpace"),
    SEODOR_WEBSERVICE_SEDEX_SENDER_ID("seodor.webservice.sedex.sender.id","Id Sedex Seodor"),

    QR_FACTURE("qrFacture","Permet de changer le mode de facturation QR-Facture/BVR"),
    ACTIVATION_TRAITILLE_QR_FACTURE("qrFacture.traitille","Permet d'activer/desactiver les traitillés sur QrFacture'"),

    TAUX_INTERET_PANDEMIE("tauxInteret.pandemie.isActivated","Propriete qui permet de changer le calcul des intérêts moratoires"),

    ACOR_ADRESSE_WEB("acor.adresse.web","Adresse web à utiliser pour ACOR Web"),
    ACOR_BACKEND_PATH("acor.backend.path","Path de l'api utilisé pour ACOR Web"),
    ACOR_NAVIGATEUR("acor.navigateur", "chemin de l'éxécutable du navigateur à utiliser avec ACOR web"),
    ACOR_TOKEN_DURATION("acor.token.duration", "Indique la duré en heure de validité du token");

    private String description;
    private String propertyName;

    CommonProperties(String propertyName, String description) {
        this.propertyName = propertyName;
        this.description = description;
    }

    @Override
    public String getApplicationName() {
        return GlobazSystem.APPLICATION_FRAMEWORK;
    }

    @Override
    public Boolean getBooleanValue() throws PropertiesException {
        return CommonPropertiesUtils.getBoolean(this);
    }

    public Integer getIntegerValue() throws PropertiesException {
        return CommonPropertiesUtils.getInteger(this);
    }

    public Integer getIntegerWithDefaultValue(Integer defaultValue)  {
        return CommonPropertiesUtils.getIntegerWithDefaultValue(this, String.valueOf(defaultValue));
    }

    public boolean exist() {
        return CommonPropertiesUtils.exist(this);
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

    public String getValueWithDefault(String defaultValue)  {
        return CommonPropertiesUtils.getValueWithDefault(this,defaultValue);
    }

}
