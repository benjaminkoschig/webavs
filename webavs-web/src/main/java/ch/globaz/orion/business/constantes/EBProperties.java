package ch.globaz.orion.business.constantes;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

public enum EBProperties implements IProperties {

    VALIDATION_SWISSDEC("pucs.validation.swissdec",
            "Permet d'activer les �crans de validation des fichiers en provenance de SwissDec", String.class),
    MISE_EN_GED_DEFAULT("pucs.validation.declaration.miseEnGed",
            "Indique s'y il faut cocher par d�faut la mise en ged", Boolean.class),
    VALIDATION_DEFAULT("pucs.validation.declaration.validationDs",
            "Indique s'y il faut cocher par d�faut la validation des DS", Boolean.class),
    PUCS_SWISS_DEC_DIRECTORY("pucs.remotePucsFileDirectory",
            "le r�pertoire des fichiers � traiter (par exemple ftp:/swissdec/aTraite/", String.class),
    PUCS_SWISS_DEC_DIRECTORY_OK("pucs.remotePucsFileDirectoryOk",
            "le r�pertoire des fichiers � traiter (par exemple ftp:/swissdec/ok/", String.class),
    PUCS_SWISS_DEC_DIRECTORY_KO("pucs.remotePucsFileDirectoryKo",
            "le r�pertoire des fichiers � traiter (par exemple  ftp:/swissdec/ko/", String.class),
    VALIDATION_MONTANT_POURCENTAGE_CONTROLE("validationAutomatique.verification.pourcentage",
            "Pourcentage � ne pas d�paser sur le montant factur� ou rembours�", BigDecimal.class),
    VALIDATION_MONTANT_LIMIT_CONTROLE("validationAutomatique.verification.limit",
            "Pourcentage � ne pas d�paser sur le montant factur� ou rembours�", BigDecimal.class),
    VALIDATION_MONTANT_NEGATIF("validationAutomatique.verification.valeurNegative",
            "V�rifie si le montant factur� est n�gatif", Boolean.class),
    ECL_EXCLURE_CA_AFFILIE_PERSONNEL("ecl.exclure.ca.affilie.personnel",
            "permet d'exclure dans l'affichage ECL les comptes annexes avec le r�le affilie personnel", Boolean.class),
    EMAIL_MUTATION_ADRESSE_CAF("email.mutation.adresse.caf",
            "Adresse email � laquelle envoyer les mutations d'adresses caf", String.class),
    EMAIL_MUTATION_ADRESSE_AVS("email.mutation.adresse.avs",
            "Adresse email � laquelle envoyer les mutations d'adresses avs", String.class),
    GENERIC_USER("generic.user", "Nom de l'utilisateur g�n�rique EBusiness", String.class);

    private String property;
    private String description;
    private Class<?> type;

    EBProperties(String prop, String description, Class<?> type) {
        property = prop;
        this.type = type;
        this.description = description;
    }

    @Override
    public String getApplicationName() {
        return "ORION";
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

    public BigDecimal getValueAsBigDecimal() throws PropertiesException {
        return new BigDecimal(CommonPropertiesUtils.getValue(this), new MathContext(16, RoundingMode.HALF_UP));
    }

    public boolean isEmpty() throws PropertiesException {
        return CommonPropertiesUtils.isEmpty(this);
    }

    public Class<?> getValueType() {
        return type;
    }

}
