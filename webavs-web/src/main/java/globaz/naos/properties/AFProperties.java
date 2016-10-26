package globaz.naos.properties;

import globaz.naos.application.AFApplication;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

public enum AFProperties implements IProperties {

    SERVICE_USER_PROPERTY("ide.webservice.user", "login de connection WebService"),
    SERVICE_PASSWORD_PROPERTY("ide.webservice.password", "passwd de connection webService"),
    ANNONCE_GENERER("ide.annonce.generation", "determine si les annonces doivent �tres g�n�r�es"),
    SERVICE_URI_WSDL("ide.webservice.uri.wsdl",
            "uri de wsdl pour l'init du service ( ex. https://www.uid-wse-a.admin.ch/V3.0/PartnerServices.svc?wsdl)"),
    SERVICE_URI_NAMESPACE("ide.webservice.uri.namespace", "uri du namespace pour l'init du service "),
    SERVICE_LOCALPART("ide.webservice.localpart", "nom du service context pour l'init du service"),
    MAIL_NOTIFICATION_PROPERTY("ide.webservice.mailnotification.group",
            "Adresse mail auquel les rapports de traitement IDE sont envoy�s"),
    GENRE_AFFILIE_ACTIF("ide.genreAffiliation.actif",
            "codeSysteme des genres d'affiliation pouvant �tre Actif & Passif pour l'ide"),
    GENRE_AFFILIE_PASSIF("ide.genreAffiliation.passif",
            "codeSysteme des genres d'affiliation pouvant �tre Passif pour l'ide"),
    MOTIFS_FIN_CESSATION("ide.motifsFinAffiliation.cessation",
            "codeSysteme des motifs de fin d'affiliation �tant de l'ordre d'une cessation d'activit� (radiation)"),
    CATEGORIE_DECISION_CAP_TO_PRINT_DURING_RENOUVELLEMENT(
            "renouvellement.cap.impression.decisions.pour.type.assurance",
            "Cat�gories de d�cisions CAP � imprimer durant le renouvellement");

    private String description;
    private String propertyName;

    AFProperties(String propertyName, String description) {
        this.propertyName = propertyName;
        this.description = description;
    }

    @Override
    public String getApplicationName() {
        return AFApplication.DEFAULT_APPLICATION_NAOS;
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
