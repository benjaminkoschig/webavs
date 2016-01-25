package globaz.phenix.vb.suivis;

import globaz.globall.db.BSpy;
import globaz.naos.db.AFAbstractViewBean;

public class CPRevBilanViewBean extends AFAbstractViewBean {

    private String affiliationId = "";
    private String dateDebut = "";
    private String dateFin = "";
    private String eMailAddress = "";

    public String getAffiliationId() {
        return affiliationId;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public BSpy getSpy() {
        return null;
    }

    public void setAffiliationId(String affiliationId) {
        this.affiliationId = affiliationId;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setEMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

}
