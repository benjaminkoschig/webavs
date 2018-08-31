package globaz.vulpecula.vb.is;

import globaz.vulpecula.vb.listes.PTListeProcessViewBean;
import ch.globaz.vulpecula.domain.models.common.Date;

/**
 * 
 * @author jwe
 * 
 */
public class PTListeAFVerseesViewBean extends PTListeProcessViewBean {
    private String dateDebut = initDate();
    private String dateFin = initDate();

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    private String initDate() {

        Date dateToInit = new Date();

        return dateToInit.getMoisAnneeFormatte();

    }
}
