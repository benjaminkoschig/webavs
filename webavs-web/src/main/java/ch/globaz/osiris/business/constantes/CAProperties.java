package ch.globaz.osiris.business.constantes;

import java.util.List;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;
import globaz.osiris.application.CAApplication;

public enum CAProperties implements IProperties {
    RUBRIQUE_SANS_EXTOURNE("rubrique.sansExtourne",
            "Liste des rubriques pour lesquelles on extourne par les �critures"),
    MODE_AUTO_REPORT("modeReportAuto", "Activation du mode report auto"),
    ISO_SEPA_MAX_MULTIOG("iso.sepa.nbmax.multiog", "nombre max d'OG cr�� � la pr�paration"),
    ISO_SEPA_MAX_OVPAROG("iso.sepa.nbmax.ovparog", "nombre max d'OV par OG � la pr�paration"),

    ISO_SEPA_FTP_HOST("iso.sepa.ftp.host", "host sftp pour les transmission ISO20022"),
    ISO_SEPA_FTP_PORT("iso.sepa.ftp.port", "port sftp pour les transmission ISO20022"),
    ISO_SEPA_FTP_USER("iso.sepa.ftp.user", "user sftp pour les transmission ISO20022"),
    ISO_SEPA_FTP_PASS("iso.sepa.ftp.pass", "pass sftp pour les transmission ISO20022 (pour la plateforme de test)"),

    EBILL_FTP_HOST("eBill.ftp.host", "host sftp pour les factures eBill"),
    EBILL_FTP_PORT("eBill.ftp.port", "port sftp pour les factures eBill"),
    EBILL_FTP_USER("eBill.ftp.login", "user sftp pour les factures eBill"),
    EBILL_FTP_KEY_PASSPHRASE("eBill.key.passphrase", "key passphrase pour eBill"),
    EBILL_FTP_KNOWN_HOSTS("eBill.known.hosts", "known hosts file pour eBill"),
    EBILL_FTP_IN("eBill.dossier.Inbox","nom du dossier pour les inscriptions et les traitements eBill"),
    EBILL_FTP_OUT("eBill.dossier.Outbox","nom du dossier pour les factures eBill"),
    EBILL_EMAIL_CONFIRMATION("eBill.email.confirmation","L�adresse email d�exp�dition pour les emails de confirmation"),

    EBILL_BILLER_ID("eBill.numero.BillerID", "num�ro ebill de fournisseur"),
    EBILL_BASCULE("eBill.formulaire.bascule", "activation de la prise en charge des csv V2"),

    ISO_SEPA_FTP_CAMT054_HOST("iso.sepa.ftp.camt054.host", "host sftp pour les receptions camt054 ISO20022"),
    ISO_SEPA_FTP_CAMT054_PORT("iso.sepa.ftp.camt054.port", "port sftp pour les receptions camt054 ISO20022"),
    ISO_SEPA_FTP_CAMT054_USER("iso.sepa.ftp.camt054.user", "user sftp pour les receptions camt054 ISO20022"),
    ISO_SEPA_FTP_CAMT054_PASS("iso.sepa.ftp.camt054.pass",
            "pass sftp pour les receptions camt054 ISO20022 (pour la plateforme de test)"),

    ISO_SEPA_FTP_002_FROM_PAIN001_FOLDER("iso.sepa.ftp.ack.folder",
            "nom du folder de quittance pour les transmission ISO20022 de pain 001"),
    ISO_SEPA_FTP_002_FROM_PAIN008_FOLDER("iso.sepa.ftp.ack.folder2",
            "nom du folder de quittance pour les transmission ISO20022 de pain 008"),
    ISO_SEPA_FTP_001_FOLDER("iso.sepa.ftp.post.folder", "nom du folder de post pain001 pour les transmission ISO20022"),
    ISO_SEPA_FTP_008_FOLDER("iso.sepa.ftp.lsvpost.folder",
            "nom du folder de post pain008 pour les transmission ISO20022"),
    ISO_SEPA_FTP_CAMT054_FOLDER("iso.sepa.ftp.camt054.folder", "nom du folder pour les receptions camt054 ISO20022"),

    ISO_SEPA_RESPONSABLE_OG_EMAIL("iso.sepa.responsable.og.email",
            "email du responsable OG � qui la liste des transactions non execut�es est envoy�e pas le process de traitement des quittances"),

    ISO_SEPA_ENABLE_IBAN_POSTAL("iso.sepa.enableIbanPostal",
            "permet de savoir si un iban peut �tre de type postal ou uniquement banquaire"),
    ISO_SEPA_CODE_TYPE_PRELEVEMENT_CHTA("iso.sepa.typeprelevement.code.chta",
            "permet de d�finir le code du type de pr�l�vement pour CHTA"),
    ISO_SEPA_CODE_TYPE_PRELEVEMENT_CHDD("iso.sepa.typeprelevement.code.chdd",
            "permet de d�finir le code du type de pr�l�vement pour CHDD"),
    IMPORTATION_NEW_SEARCH_AFFILIATION("importationOperation.enableNewSearchAffiliation",
            "Nouvelle mani�re de rechercher l'affiliation");

    private String description;
    private String propertyName;

    CAProperties(String propertyName, String description) {
        this.propertyName = propertyName;
        this.description = description;
    }

    @Override
    public String getApplicationName() {
        return CAApplication.DEFAULT_APPLICATION_OSIRIS;
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

    public List<String> getListValue() throws PropertiesException {
        return CommonPropertiesUtils.getListValue(this);
    }
}
