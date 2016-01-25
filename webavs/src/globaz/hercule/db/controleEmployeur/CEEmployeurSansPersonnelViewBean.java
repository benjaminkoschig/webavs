package globaz.hercule.db.controleEmployeur;

import globaz.hercule.db.CEAbstractViewBean;

/**
 * @author MMO
 * @since 30 juillet 2010
 */
public class CEEmployeurSansPersonnelViewBean extends CEAbstractViewBean {

    private String forAnnee = "";
    private String typeAdresse = "";

    /**
     * Constructeur de CEEmployeurSansPersonnelViewBean
     */
    public CEEmployeurSansPersonnelViewBean() throws Exception {
    }

    /**
     * getter
     */

    public String getForAnnee() {
        return forAnnee;
    }

    public String getTypeAdresse() {
        return typeAdresse;
    }

    /**
     * setter
     */

    public void setForAnnee(String newForAnnee) {
        forAnnee = newForAnnee;
    }

    public void setTypeAdresse(String newTypeAdresse) {
        typeAdresse = newTypeAdresse;
    }

}
