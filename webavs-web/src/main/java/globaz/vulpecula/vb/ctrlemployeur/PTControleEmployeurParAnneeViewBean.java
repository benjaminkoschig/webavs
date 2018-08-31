package globaz.vulpecula.vb.ctrlemployeur;

import globaz.vulpecula.vb.listes.PTListeProcessViewBean;
import ch.globaz.vulpecula.domain.models.common.Date;

public class PTControleEmployeurParAnneeViewBean extends PTListeProcessViewBean {
    private String annee;

    public String getAnnee() {
        if (annee == null) {
            return new Date().getAnnee();
        }
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

}
