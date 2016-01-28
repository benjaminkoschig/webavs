package globaz.osiris.db.irrecouvrable;

import java.math.BigDecimal;

/**
 * Permet de stocker des informations concernant un montant d'amortissement (sert exclusivement de container pour
 * transporter des données)
 * 
 * @author bjo
 * 
 */
public class CAInfoMontantRecouvrementCi {
    private String genreDecision;
    private BigDecimal montantRecouvrementCi;

    /**
     * @param genreDecision
     * @param montantRecouvrementCi
     */
    public CAInfoMontantRecouvrementCi(String genreDecision, BigDecimal montantRecouvrementCi) {
        this.genreDecision = genreDecision;
        this.montantRecouvrementCi = montantRecouvrementCi;
    }

    public String getGenreDecision() {
        return genreDecision;
    }

    public BigDecimal getMontantRecouvrementCi() {
        return montantRecouvrementCi;
    }

    public void setGenreDecision(String genreDecision) {
        this.genreDecision = genreDecision;
    }

    public void setMontantRecouvremenetCi(BigDecimal montantRecouvrementCi) {
        this.montantRecouvrementCi = montantRecouvrementCi;
    }
}
