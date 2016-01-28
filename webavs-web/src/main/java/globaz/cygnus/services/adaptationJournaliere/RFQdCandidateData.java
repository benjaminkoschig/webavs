package globaz.cygnus.services.adaptationJournaliere;

import java.util.LinkedList;
import java.util.List;

public class RFQdCandidateData {

    String idPeriode = "";
    String idQdPrincipale = "";
    boolean isPeriodesComprisesDansPeriodePc = false;
    List<RFMutationPeriodeValiditeQd> mutations = new LinkedList<RFMutationPeriodeValiditeQd>();

    public RFQdCandidateData(String idPeriode, String idQdPrincipale, boolean isPeriodesComprisesDansPeriodePc,
            List<RFMutationPeriodeValiditeQd> mutations) {
        super();
        this.idPeriode = idPeriode;
        this.idQdPrincipale = idQdPrincipale;
        this.isPeriodesComprisesDansPeriodePc = isPeriodesComprisesDansPeriodePc;
        this.mutations = mutations;
    }

    public String getIdPeriode() {
        return idPeriode;
    }

    public String getIdQdPrincipale() {
        return idQdPrincipale;
    }

    public List<RFMutationPeriodeValiditeQd> getMutations() {
        return mutations;
    }

    public boolean isPeriodesComprisesDansPeriodePc() {
        return isPeriodesComprisesDansPeriodePc;
    }

    public void setIdPeriode(String idPeriode) {
        this.idPeriode = idPeriode;
    }

    public void setIdQdPrincipale(String idQdPrincipale) {
        this.idQdPrincipale = idQdPrincipale;
    }

    public void setMutations(List<RFMutationPeriodeValiditeQd> mutations) {
        this.mutations = mutations;
    }

    public void setPeriodesComprisesDansPeriodePc(boolean isPeriodesComprisesDansPeriodePc) {
        this.isPeriodesComprisesDansPeriodePc = isPeriodesComprisesDansPeriodePc;
    }

}
