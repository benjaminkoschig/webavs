package globaz.osiris.db.irrecouvrable;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Représente un amortissementCi pour le calcul de la mise à charge des irrécouvrable.
 * 
 * @author bjo
 * 
 */
public class CAAmortissementCi implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Integer annee;
    private String genreDecision;
    private BigDecimal montantAmortissement;
    private BigDecimal montantEtatCi;

    public CAAmortissementCi(BigDecimal montantAmortissement, BigDecimal montantEtatCi, Integer annee,
            String genreDecision) {
        this.montantAmortissement = montantAmortissement;
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

    public BigDecimal getMontantAmortissement() {
        return montantAmortissement;
    }

    public BigDecimal getMontantEtatCi() {
        return montantEtatCi;
    }

    public void setMontantAmortissement(BigDecimal montantAmortissement) {
        this.montantAmortissement = montantAmortissement;
    }

    @Override
    public String toString() {
        String result = "";
        result += "           annee                : " + annee + "\n";
        result += "           genreDecision        : " + genreDecision + "\n";
        result += "           montantAmortissement : " + montantAmortissement + "\n";
        result += "           montantEtatCi        : " + montantEtatCi + "\n";
        return result;
    }

}
