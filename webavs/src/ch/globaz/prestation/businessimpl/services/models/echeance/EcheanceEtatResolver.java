package ch.globaz.prestation.businessimpl.services.models.echeance;

import java.util.Date;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Echeance.Echeance;
import ch.globaz.common.domaine.Echeance.EcheanceEtat;

/**
 * Permet de r�soudre l��tat de l��ch�ance
 * 
 * @author DMA
 */
class EcheanceEtatResolver {

    /**
     * R�sous l��tat de l��ch�ance en fonction de la date de l��ch�ance. Si la date de maintenant est strictement plus
     * petite que la date de l��cheance l��tat sera ECHU dans tous les autre cas l��tat ne change pas
     * 
     * @throws RuntimeException
     *             si l'�ch�ance pass�e en param�tre est null
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
