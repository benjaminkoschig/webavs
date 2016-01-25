package globaz.hercule.db.traitement;

import globaz.hercule.db.CEAbstractViewBean;

/**
 * Bean représentant les écrans de rattrapage des notes
 * 
 * @author SCO
 * @revision SCO 7 juin 2010
 */
public class CETraitementViewBean extends CEAbstractViewBean {

    private String annee;

    /**
     * Constructeur de CETraitementViewBean
     */
    public CETraitementViewBean() {
        super();
    }

    // *******************************************************
    // Getter
    // ***************************************************

    public String getAnnee() {
        return annee;
    }

    // *******************************************************
    // Getter
    // ***************************************************
    public void setAnnee(String annee) {
        this.annee = annee;
    }

}
