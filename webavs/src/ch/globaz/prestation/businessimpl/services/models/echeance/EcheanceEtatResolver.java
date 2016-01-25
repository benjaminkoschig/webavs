package ch.globaz.prestation.businessimpl.services.models.echeance;

import java.util.Date;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Echeance.Echeance;
import ch.globaz.common.domaine.Echeance.EcheanceEtat;

/**
 * Permet de résoudre l’état de l’échéance
 * 
 * @author DMA
 */
class EcheanceEtatResolver {

    /**
     * Résous l’état de l’échéance en fonction de la date de l’échéance. Si la date de maintenant est strictement plus
     * petite que la date de l’écheance l’état sera ECHU dans tous les autre cas l’état ne change pas
     * 
     * @throws RuntimeException
     *             si l'échéance passée en paramétre est null
     * @param echeance
     * @return EcheanceEtat
     */
    public static EcheanceEtat resolve(Echeance echeance) {
        Checkers.checkNotNull(echeance, "Echeance domaine");
        if (echeance.getDateEcheance().before(new Date()) && !EcheanceEtat.ANNULEE.equals(echeance.getEtat())
                && !EcheanceEtat.TRAITEE.equals(echeance.getEtat())) {
            return EcheanceEtat.ECHUE;
        } else {
            return echeance.getEtat();
        }
    }
}
