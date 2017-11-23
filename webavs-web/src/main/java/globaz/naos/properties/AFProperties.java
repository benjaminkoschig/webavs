package globaz.naos.properties;

import globaz.naos.application.AFApplication;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

public enum AFProperties implements IProperties {

    IDE_EXTENSION_CONTEXTS("ide.extension.contexts", "extensions du service IDE"),
    SERVICE_USER_PROPERTY("ide.webservice.user", "login de connection WebService"),
    SERVICE_PASSWORD_PROPERTY("ide.webservice.password", "passwd de connection webService"),
    ANNONCE_GENERER("ide.annonce.generation", "determine si les annonces doivent êtres générées"),
    SERVICE_URI_WSDL("ide.webservice.uri.wsdl",
            "uri de wsdl pour l'init du service ( ex. https://www.uid-wse-a.admin.ch/V3.0/PartnerServices.svc?wsdl)"),
    SERVICE_URI_NAMESPACE("ide.webservice.uri.namespace", "uri du namespace pour l'init du service "),
    SERVICE_LOCALPART("ide.webservice.localpart", "nom du service context pour l'init du service"),
    MAIL_NOTIFICATION_PROPERTY("ide.webservice.mailnotification.group",
            "Adresse mail auquel les rapports de traitement IDE sont envoyés"),
    GENRE_AFFILIE_ACTIF("ide.genreAffiliation.actif",
            "codeSysteme des genres d'affiliation pouvant être Actif & Passif pour l'ide"),
    GENRE_AFFILIE_PASSIF("ide.genreAffiliation.passif",
            "codeSysteme des genres d'affiliation pouvant être Passif pour l'ide"),
    MOTIFS_FIN_CESSATION("ide.motifsFinAffiliation.cessation",
            "codeSysteme des motifs de fin d'affiliation étant de l'ordre d'une cessation d'activité (radiation)"),
    CATEGORIE_DECISION_CAP_TO_PRINT_DURING_RENOUVELLEMENT(
            "renouvellement.cap.impression.decisions.pour.type.assurance",
            "Catégories de décisions CAP à imprimer durant le renouvellement"),
    REE_NOM_REFERENCE("ree.header.name", "Nom de référence de la personne pour les annonces REE"),
    REE_EMAIL_REFERENCE("ree.header.email", "Mail de référence de la personne pour les annonces REE"),
    REE_TELEPHONE_REFERENCE("ree.header.phone", "Numéro de téléphone de référence de la personne pour les annonces REE"),
    REE_DEPARTEMENT_REFERENCE("ree.header.departement", "Département de référence de la personne pour les annonces REE"),
    REE_INFO_COMPLEMENTAIRE_REFERENCE("ree.header.other",
            "Informations complémentaire de la personne pour les annonces REE"),
    REE_NB_PACKAGE("ree.process.paquet", "Nombre d'annonces par message"),
    REE_VALIDATION_UNITAIRE("ree.process.validation", "Validation unitaire à réaliser"),
    REE_DESTINATAIRE("ree.message.header.recipient.id", "Destinataire"),
    IDE_PLAN_AFFILIATION_VERIFICATION_INACTIF("ide.planAffiliation.verificationInactif",
            "Prise en compte ou non des plans d'affiliation inactif lors d'annonce IDE"),
    IDE_COTISATION_VERIFICATION_TAXE_SOUS("ide.cotisation.verificationTaxeSous",
            "Prise en compte ou non des plans d'affiliation de l'affiliation qui détient une autre affiliation"),
    IDE_URL_ENDPOINT("ide.webservice.url.endpoint", "Adresse Endpoint IDE"),
    NOGA_SYNCHRO_REGISTRE("exploitation.codeNOGA.IDE.SynchroRegistre",
            "Mettre à jour le code NOGA depuis la synchro IDE"),
    NOGA_UPDATE_ANNONCE_IDE("exploitation.codeNOGA.IDE.InfoABO",
            "Mettre à jour le code NOGA depuis lors d'une annonce IDE"),
    IDE_CASCADE_ADRESSE_MORALE("ide.cascade.adresse.morale", "Cascade d'adresse IDE pour personnes morales"),
    IDE_CASCADE_ADRESSE_PHYSIQUE("ide.cascade.adresse.physique", "Cascade d'adresse IDE pour personnes physiques"),
    CONTROLE_ANNUEL_LPP_GENERATION_EXTRAIT_DS("lpp.annuel.generation.extrait",
            "Generation de l'extrait de décompte lors de l'envoi du questionnaire LPP pour le suivi annuel");
    ;

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
