package globaz.osiris.db.irrecouvrable;

import java.math.BigDecimal;

/**
 * Permet de stocker des informations concernant un montant d'amortissement (sert exclusivement de container pour
 * transporter des données)
 * 
 * @author bjo
 * 
 */
public class CAInfoMontantAmortissementCi {
    private String genreDecision;
    private BigDecimal montantAmortissementCi;

    /**
     * @param genreDecision
     * @param montantAmortissementCi
     */
    public CAInfoMontantAmortissementCi(String genreDecision, BigDecimal montantAmortissementCi) {
        this.genreDecision = genreDecision;
        this.montantAmortissementCi = montantAmortissementCi;
    }

    public String getGenreDecision() {
        return genreDecision;
    }

    public BigDecimal getMontantAmortissementCi() {
        return montantAmortissementCi;
    }

    public void setGenreDecision(String genreDecision) {
        this.genreDecision = genreDecision;
    }

    public void setMontantAmortissementCi(BigDecimal montantAmortissementCi) {
        this.montantAmortissementCi = montantAmortissementCi;
    }
}
