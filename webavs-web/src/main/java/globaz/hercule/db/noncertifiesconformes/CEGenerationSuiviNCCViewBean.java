/*
 * Globaz SA.
 */
package globaz.hercule.db.noncertifiesconformes;

import globaz.hercule.db.CEAbstractViewBean;

public class CEGenerationSuiviNCCViewBean extends CEAbstractViewBean {

    private String forNumAffilie;
    private String annee;

    public CEGenerationSuiviNCCViewBean() {
        super();
    }

    public String getForNumAffilie() {
        return forNumAffilie;
    }

    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

}
