package ch.globaz.pegasus.business.domaine.droit;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;
import ch.globaz.pegasus.business.constantes.IPCDroits;

public enum EtatDroit implements CodeSystemEnum<EtatDroit> {

    VALIDE(IPCDroits.CS_VALIDE),
    ENREGISTRE(IPCDroits.CS_ENREGISTRE),
    AU_CALCUL(IPCDroits.CS_AU_CALCUL),
    HISTORISE(IPCDroits.CS_HISTORISE),
    COURANT_VALIDE(IPCDroits.CS_COURANT_VALIDE), // N'est pas utilisé en production
    CALCULE(IPCDroits.CS_CALCULE);

    private String value;

    EtatDroit(String csEtatDroit) {
        value = csEtatDroit;
    }

    public static EtatDroit fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, EtatDroit.class);
    }

    @Override
    public String getValue() {
        return value;
    }

    public boolean isValide() {
        return VALIDE.equals(this);
    }

    public boolean isEnregistre() {
        return ENREGISTRE.equals(this);
    }

    public boolean isAuCalcul() {
        return AU_CALCUL.equals(this);
    }

    public boolean isCalcule() {
        return CALCULE.equals(this);
    }

    public boolean isHistorise() {
        return HISTORISE.equals(this);
    }

}
