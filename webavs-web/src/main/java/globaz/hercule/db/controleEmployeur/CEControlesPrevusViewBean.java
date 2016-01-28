package globaz.hercule.db.controleEmployeur;

import globaz.hercule.db.CEAbstractViewBean;

/**
 * @author SCO
 * @since 11 oct. 2010
 */
public class CEControlesPrevusViewBean extends CEAbstractViewBean {

    private String annee;
    private String typeAdresse;

    /**
     * Constructeur de CEControlesPrevusViewBean
     */
    public CEControlesPrevusViewBean() {
        super();
    }

    // *******************************************************
    // Getter
    // ***************************************************

    public String getAnnee() {
        return annee;
    }

    public String getTypeAdresse() {
        return typeAdresse;
    }

    // *******************************************************
    // Setter
    // ***************************************************

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setTypeAdresse(String typeAdresse) {
        this.typeAdresse = typeAdresse;
    }

}
