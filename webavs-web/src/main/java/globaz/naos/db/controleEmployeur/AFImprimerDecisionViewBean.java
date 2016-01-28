package globaz.naos.db.controleEmployeur;

import globaz.globall.db.BManager;
import globaz.naos.db.AFAbstractViewBean;

// public class AFListeRadieTaxeCo2ViewBean extends AFListeRadieTaxeCo2_Doc implements FWViewBeanInterface {
// /**
// * Insérez la description de la méthode ici. Date de création : (10.03.2003 10:55:33)
// */
// public AFListeRadieTaxeCo2ViewBean() throws java.lang.Exception {
// }
// }
public class AFImprimerDecisionViewBean extends AFAbstractViewBean {

    // DB
    // Fields

    private String Email = new String();
    private String idPassage = new String();

    /**
     * Constructeur de AFAffiliation
     */
    public AFImprimerDecisionViewBean() {
        super();
    }

    public String getEmail() {
        return Email;
    }

    public String getIdPassage() {
        return idPassage;
    }

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    protected BManager getManager() {
        return new AFImprimerDecisionManager();
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

}
