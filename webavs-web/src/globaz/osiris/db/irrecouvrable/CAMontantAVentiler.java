package globaz.osiris.db.irrecouvrable;

import java.math.BigDecimal;

/**
 * Représente un montant disponible pour la ventilation
 * 
 * @author bjo
 * 
 */
public class CAMontantAVentiler {
    public static Integer ANNEE_INDETERMINEE = 0;

    private Integer annee;
    private BigDecimal montant;

    /**
     * @param annee
     * @param montant
     */
    public CAMontantAVentiler(Integer annee, BigDecimal montant) {
        this.annee = annee;
        this.montant = montant;
    }

    /**
     * Additionne le montant passé en paramètre au montant
     * 
     * @param montantToAdd
     */
    public void additionnerToMontant(BigDecimal montantToAdd) {
        montant = montant.add(montantToAdd);
    }

    public BigDecimal getMontant() {
        return montant;
    }

    @Override
    public String toString() {
        String result = "";
        result += "annee : " + annee + "\n";
        result += "montant : " + montant + "\n";
        return result;
    }
}
