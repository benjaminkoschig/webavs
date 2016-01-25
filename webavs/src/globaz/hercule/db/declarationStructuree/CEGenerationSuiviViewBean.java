package globaz.hercule.db.declarationStructuree;

import globaz.hercule.db.CEAbstractViewBean;

/**
 * @author SCO
 * @since 15 juil. 2010
 */
public class CEGenerationSuiviViewBean extends CEAbstractViewBean {

    private String annee;
    private String fromNumAffilie;
    private String toNumAffilie;

    /**
     * Constructeur de CEGenerationSuiviViewBean
     */
    public CEGenerationSuiviViewBean() {
        super();
    }

    // *******************************************************
    // Getter
    // ***************************************************

    public String getAnnee() {
        return annee;
    }

    public String getFromNumAffilie() {
        return fromNumAffilie;
    }

    public String getToNumAffilie() {
        return toNumAffilie;
    }

    // *******************************************************
    // Getter
    // ***************************************************
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setFromNumAffilie(String fromNumAffilie) {
        this.fromNumAffilie = fromNumAffilie;
    }

    public void setToNumAffilie(String toNumAffilie) {
        this.toNumAffilie = toNumAffilie;
    }

}
