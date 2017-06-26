/*
 * Globaz SA.
 */
package globaz.hercule.vb.controleEmployeur;

import globaz.hercule.db.CEAbstractViewBean;

public class CEListeNccViewBean extends CEAbstractViewBean {
    private String annee = "";
    private String eMailAddress = "";

    public CEListeNccViewBean() {
        super();
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String geteMailAddress() {
        return eMailAddress;
    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }
}
