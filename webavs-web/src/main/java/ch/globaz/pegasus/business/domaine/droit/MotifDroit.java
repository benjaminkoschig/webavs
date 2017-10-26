package ch.globaz.pegasus.business.domaine.droit;

import java.util.HashSet;
import java.util.Set;
import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;
import ch.globaz.pegasus.business.constantes.IPCDroits;

public enum MotifDroit implements CodeSystemEnum<MotifDroit> {

    ADAPTATION(IPCDroits.CS_MOTIF_DROIT_ADAPTATION),
    ADAPTATION_HOME(IPCDroits.CS_MOTIF_DROIT_ADAPTATION_HOME),
    ARRIVEE_ETRANGER(IPCDroits.CS_MOTIF_DROIT_ARRIVEE_ETRANGER),
    DIVORCE(IPCDroits.CS_MOTIF_DROIT_DIVORCE), // N'est pas utilisé en production
    MARRIAGE(IPCDroits.CS_MOTIF_DROIT_MARRIAGE),
    MODIFICATIONS_DIVERSES(IPCDroits.CS_MOTIF_DROIT_MODIFICATIONS_DIVERSES),
    MODIFICATION_RETOUR_REGISTRE(IPCDroits.CS_MOTIF_DROIT_MODIFICATION_RETOUR_REGISTRE),
    NOUVEAU_DROIT(IPCDroits.CS_MOTIF_DROIT_NOUVEAU_DROIT),
    REPRISE(IPCDroits.CS_MOTIF_DROIT_REPRISE),
    REPRISE_ADAPTATION_ERRONE(IPCDroits.CS_MOTIF_DROIT_REPRISE_ADAPTATION_ERRONE),
    CORRECTION_REPRISE("64063015"),
    REVISION_QUADRIENNALE(IPCDroits.CS_MOTIF_DROIT_REVISION_QUADRIENNALE),
    TRANSFERT_CLARENS(IPCDroits.CS_MOTIF_DROIT_TRANSFERT_CLARENS),
    TRANSFERT_HORS_CANTON(IPCDroits.CS_MOTIF_DROIT_TRANSFERT_HORS_CANTON),
    VEUVAGE(IPCDroits.CS_MOTIF_DROIT_VEUVAGE),
    DEPART_ETRANGER("64063016"),
    DECES(IPCDroits.CS_MOTIF_DROIT_DECES);

    private String value;

    MotifDroit(String motifDroit) {
        value = motifDroit;
    }

    public boolean isDeces() {
        return MotifDroit.DECES.equals(this);
    }

    public boolean isRetourRegistre() {
        return MotifDroit.MODIFICATION_RETOUR_REGISTRE.equals(this);
    }

    public boolean isSuppression() {
        Set<MotifDroit> motifsDroitSuppression = new HashSet<MotifDroit>();
        motifsDroitSuppression.add(MotifDroit.DEPART_ETRANGER);
        motifsDroitSuppression.add(MotifDroit.TRANSFERT_HORS_CANTON);
        motifsDroitSuppression.add(MotifDroit.TRANSFERT_CLARENS);
        motifsDroitSuppression.add(MotifDroit.DECES);
        motifsDroitSuppression.add(MotifDroit.VEUVAGE);
        return motifsDroitSuppression.contains(this);
    }

    public static MotifDroit fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, MotifDroit.class);
    }

    @Override
    public String getValue() {
        return value;
    }
}
