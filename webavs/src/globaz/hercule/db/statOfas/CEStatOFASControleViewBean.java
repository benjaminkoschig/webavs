package globaz.hercule.db.statOfas;

import globaz.hercule.db.CEAbstractViewBean;

/**
 * Permet de controler les valeurs entr�es par l'utilisateur
 * 
 * @author sda
 * @since Cr�� le 25 avr. 05
 */
public class CEStatOFASControleViewBean extends CEAbstractViewBean {

    private String annee;

    public CEStatOFASControleViewBean() throws java.lang.Exception {
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

}
