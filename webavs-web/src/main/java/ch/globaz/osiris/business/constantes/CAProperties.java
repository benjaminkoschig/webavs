package ch.globaz.osiris.business.constantes;

import globaz.osiris.application.CAApplication;
import java.util.List;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

public enum CAProperties implements IProperties {
    RUBRIQUE_SANS_EXTOURNE("rubrique.sansExtourne", "Liste des rubriques pour lesquelles on extourne par les �critures"),
    ISO_SEPA_MAX_MULTIOG("iso.sepa.nbmax.multiog", "nombre max d'OG cr�� � la pr�paration"),
    ISO_SEPA_MAX_OVPAROG("iso.sepa.nbmax.ovparog", "nombre max d'OV par OG � la pr�paration"),
    ISO_SEPA_FTP_HOST("iso.sepa.ftp.host", "host sftp pour les transmission ISO20022"),
    ISO_SEPA_FTP_PORT("iso.sepa.ftp.port", "port sftp pour les transmission ISO20022"),
    ISO_SEPA_FTP_USER("iso.sepa.ftp.user", "user sftp pour les transmission ISO20022"),
    ISO_SEPA_FTP_PASS("iso.sepa.ftp.pass", "pass sftp pour les transmission ISO20022 (pour la plateforme de test)"),
    ISO_SEPA_FTP_002_FOLDER("iso.sepa.ftp.ack.folder", "nom du folder de quittance pour les transmission ISO20022"),
    ISO_SEPA_FTP_001_FOLDER("iso.sepa.ftp.post.folder", "nom du folder de post pain001 pour les transmission ISO20022"),
    ISO_SEPA_RESPONSABLE_OG_EMAIL(
            "iso.sepa.responsable.og.email",
            "email du responsable OG � qui la liste des transactions non execut�es est envoy�e pas le process de traitement des quittances");

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
