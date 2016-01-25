/*
 * Créé le 17 janvier 2012
 */

package globaz.cygnus.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * @author MBO
 */

public class RFStatistiquesParNbCasViewBean extends PRAbstractViewBeanSupport {

    private String dateDebutStat = null;
    private String dateFinStat = null;
    private String eMailAdr = null;
    private String gestionnaire = null;

    public String getDateDebutStat() {
        if (dateDebutStat == null) {
            dateDebutStat = "";
        }
        return dateDebutStat;
    }

    public String getDateFinStat() {
        if (dateFinStat == null) {
            dateFinStat = "";
        }
        return dateFinStat;
    }

    public String geteMailAdr() {
        return eMailAdr;
    }

    public String getGestionnaire() {
        return gestionnaire;
    }

    @Override
    public void retrieve() throws Exception {
    }

    public void setDateDebutStat(String dateDebutStat) {
        this.dateDebutStat = dateDebutStat;
    }

    public void setDateFinStat(String dateFinStat) {
        this.dateFinStat = dateFinStat;
    }

    public void seteMailAdr(String eMailAdr) {
        this.eMailAdr = eMailAdr;
    }

    public void setGestionnaire(String gestionnaire) {
        this.gestionnaire = gestionnaire;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
