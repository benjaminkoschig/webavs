package globaz.phenix.util;

import globaz.phenix.application.CPApplication;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

/**
 * Permet la r�cup�ration des propri�t�s les cotisations personnelles
 * 
 * @author sco
 */
public enum CPProperties implements IProperties {

    LISTE_DECISIONS_COMPTABILISEES_TYPE_SUIVI_AF("liste.decisions.comptabilisees.type.suivi.af"),
    LISTE_TAXATION_DEFINITIVE_GROUP_MAIL("liste.taxation.definitive.group.mail"),
    ETAT_CIVIL_SIMUL_CONJOINT("etatCivilCodeSimulConjoint"),
    // Indique si les d�cisions de cotisations personnelles doivent �tre cr��es dans l'EBusiness
    ADI_MANAGE_DECISION_IN_EBUSINESS("adi.manage.decision.in.ebusiness"),
    ACTIVER_NUM_AFF_LIGNE_TECHNIQUE("activerNumAffLigneTechnique");

    private String property;

    CPProperties(String prop) {
        property = prop;
    }

    @Override
    public String getApplicationName() {
        return CPApplication.DEFAULT_APPLICATION_PHENIX;
    }

    @Override
    public Boolean getBooleanValue() throws PropertiesException {
        return CommonPropertiesUtils.getBoolean(this);
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getPropertyName() {
        return property;
    }

    @Override
    public String getValue() throws PropertiesException {
        return CommonPropertiesUtils.getValue(this);
    }

}
