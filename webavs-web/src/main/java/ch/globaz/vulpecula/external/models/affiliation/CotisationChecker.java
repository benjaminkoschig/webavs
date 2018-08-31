package ch.globaz.vulpecula.external.models.affiliation;

import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;

/**
 * Classe permettant d'effectuer des contrôles relatifs à la classe {@link Cotisation}
 * 
 */
public class CotisationChecker {
    /**
     * Retourne si dans la liste des cotisations on retrouve une cotisations soumise à l'AVS.
     * 
     * @param cotisations Liste de cotisations
     * @return true si on retrouve le type d'assurance {@link Assurance#TYPE_ASSURANCE_AVS}
     */
    public static boolean isSoumisAVS(final List<Cotisation> cotisations) {
        checkListsCotisationAndThrowExceptionIfNull(cotisations);
        for (Cotisation cotisation : cotisations) {
            if (cotisation.isAssuranceAVS()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSoumisAVS(final List<Cotisation> cotisations, Date dateRef) {
        checkListsCotisationAndThrowExceptionIfNull(cotisations);
        for (Cotisation cotisation : cotisations) {
            if (dateRef != null && dateRef.afterOrEquals(cotisation.getDateDebut())
                    && (cotisation.getDateFin() == null || dateRef.beforeOrEquals(cotisation.getDateFin()))) {
                if (cotisation.isAssuranceAVS()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isSoumisAVSPlus1JourPourPeriode(final List<Cotisation> cotisations, Periode periode) {
        checkListsCotisationAndThrowExceptionIfNull(cotisations);
        for (Cotisation cotisation : cotisations) {
            if (!cotisation.getDateDebut().equals(cotisation.getDateFin())) {
                Periode periodeCotisation = new Periode(cotisation.getDateDebut(), cotisation.getDateFin());
                if (periodeCotisation.contains(periode) || periodeCotisation.chevauche(periode)) {
                    if (cotisation.isAssuranceAVS()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Retourne si dans la liste des cotisations on retrouve une cotisations soumise à l'AC.
     * 
     * @param cotisations Liste de cotisations
     * @return true si on retrouve le type d'assurance {@link Assurance#TYPE_ASSURANCE_AC}
     */
    public static boolean isSoumisAC(final List<Cotisation> cotisations) {
        checkListsCotisationAndThrowExceptionIfNull(cotisations);
        for (Cotisation cotisation : cotisations) {
            if (cotisation.isAssuranceAC()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retourne si dans la liste des cotisations on retrouve une cotisations soumise à l'AC2.
     * 
     * @param cotisations Liste de cotisations
     * @return true si on retrouve le type d'assurance {@link Assurance#TYPE_ASSURANCE_AC2}
     */
    public static boolean isSoumisAC2(final List<Cotisation> cotisations) {
        checkListsCotisationAndThrowExceptionIfNull(cotisations);
        for (Cotisation cotisation : cotisations) {
            if (cotisation.isAssuranceAC2()) {
                return true;
            }
        }
        return false;
    }

    private static void checkListsCotisationAndThrowExceptionIfNull(List<Cotisation> cotisations) {
        if (cotisations == null) {
            throw new IllegalArgumentException(
                    "L'employeur est dans un état inconsistant car il ne possède pas de cotisations");
        }
    }
}
