package globaz.hercule.vb.controleEmployeur;

import globaz.hercule.db.CEAbstractViewBean;

/**
 * @author JPA
 * @revision JPA juin 2010
 */
public class CEControles5PourCentViewBean extends CEAbstractViewBean {
    private String annee = "";
    private String eMailAddress = "";
    private String typeAdresse = "";

    /**
     * Constructeur de CEControles5PourCentViewBean
     */
    public CEControles5PourCentViewBean() {
        super();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getAnnee() {
        return annee;
    }

    public String geteMailAddress() {
        return eMailAddress;
    }

    public String getTypeAdresse() {
        return typeAdresse;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    // *******************************************************
    // Setter
    // *******************************************************
    public void setTypeAdresse(String typeAdresse) {
        this.typeAdresse = typeAdresse;
    }

}
