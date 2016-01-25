package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author dma
 * 
 */
public class ComptabilisationUtil {

    /**
     * Permet de split un montant en deux Le premier résultat du tableau aura un franc de plus si le montant et impaire.
     * 
     * @param montant
     * @return BigDecimal[]
     */
    public static BigDecimal[] splitMontant(BigDecimal montant) {
        BigDecimal[] montants = new BigDecimal[2];
        BigDecimal montantRequerant = null;
        BigDecimal montantConjoint = new BigDecimal(0);
        montantConjoint = montant.divide(new BigDecimal(2), 0, RoundingMode.FLOOR);
        montantRequerant = montant.subtract(montantConjoint);
        montants[0] = montantRequerant;
        montants[1] = montantConjoint;
        return montants;
    }

}
