package globaz.osiris.db.irrecouvrable;

import java.math.BigDecimal;

/**
 * Représente une base d'amortissement qui contient le cumul des compteurs d'amortissement pour une année
 * 
 * @author sch
 * 
 */

public class CARecouvrementBaseAmortissement {
    private Integer anneeAmortissement;
    private BigDecimal cumulCotisationForAnnee;

    /**
     * Constructeur
     * 
     * @param anneeAmortissement
     * @param cumulCotisationForAnnee
     */
    public CARecouvrementBaseAmortissement(Integer anneeAmortissement, BigDecimal cumulCotisationForAnnee) {
        this.anneeAmortissement = anneeAmortissement;
        this.cumulCotisationForAnnee = cumulCotisationForAnnee;
    }

    /**
     * @return the anneeAmortissement
     */
    public Integer getAnneeAmortissement() {
        return anneeAmortissement;
    }

    /**
     * @return the cumulCotisationForAnnee
     */
    public BigDecimal getCumulCotisationForAnnee() {
        return cumulCotisationForAnnee;
    }

    @Override
    public String toString() {
        String result = "";
        result += "           anneeAmortissement      : " + anneeAmortissement + "\n";
        result += "           cumulCotisationForAnnee : " + cumulCotisationForAnnee + "\n";
        return result;
    }

}
