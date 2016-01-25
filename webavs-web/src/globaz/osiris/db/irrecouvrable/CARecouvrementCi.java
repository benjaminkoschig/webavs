package globaz.osiris.db.irrecouvrable;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Représente un amortissementCi pour le calcul de la mise à charge des irrécouvrable.
 * 
 * @author bjo
 * 
 */
public class CARecouvrementCi implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Integer annee;
    private String genreDecision;
    private BigDecimal montantEtatCi;
    private BigDecimal montantRecouvrement;

    public CARecouvrementCi(BigDecimal montantRecouvrement, BigDecimal montantEtatCi, Integer annee,
            String genreDecision) {
        this.montantRecouvrement = montantRecouvrement;
        this.montantEtatCi = montantEtatCi;
        this.annee = annee;
        this.genreDecision = genreDecision;
    }

    public Integer getAnnee() {
        return annee;
    }

    public String getGenreDecision() {
        return genreDecision;
    }

    public BigDecimal getMontantEtatCi() {
        return montantEtatCi;
    }

    public BigDecimal getMontantRecouvrement() {
        return montantRecouvrement;
    }

    public void setMontantRecouvrement(BigDecimal montantRecouvrement) {
        this.montantRecouvrement = montantRecouvrement;
    }

    @Override
    public String toString() {
        String result = "";
        result += "           annee                : " + annee + "\n";
        result += "           genreDecision        : " + genreDecision + "\n";
        result += "           montantRecouvrement  : " + montantRecouvrement + "\n";
        result += "           montantEtatCi        : " + montantEtatCi + "\n";
        return result;
    }

}
