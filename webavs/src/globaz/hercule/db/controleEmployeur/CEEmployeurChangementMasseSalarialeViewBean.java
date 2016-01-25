package globaz.hercule.db.controleEmployeur;

import globaz.hercule.db.CEAbstractViewBean;

/**
 * @author MMO
 * @since 27 juillet 2010
 */
public class CEEmployeurChangementMasseSalarialeViewBean extends CEAbstractViewBean {

    private String forAnnee = "";
    private String fromNumAffilie = "";
    private String toNumAffilie = "";
    private String typeAdresse = "";

    /**
     * Constructeur de CEEmployeurChangementMasseSalarialeViewBean
     */
    public CEEmployeurChangementMasseSalarialeViewBean() throws Exception {
    }

    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * getter
     */
    public String getFromNumAffilie() {
        return fromNumAffilie;
    }

    public String getToNumAffilie() {
        return toNumAffilie;
    }

    public String getTypeAdresse() {
        return typeAdresse;
    }

    public void setForAnnee(String newForAnnee) {
        forAnnee = newForAnnee;
    }

    /**
     * setter
     */

    public void setFromNumAffilie(String newFromNumAffilie) {
        fromNumAffilie = newFromNumAffilie;
    }

    public void setToNumAffilie(String newToNumAffilie) {
        toNumAffilie = newToNumAffilie;
    }

    public void setTypeAdresse(String newTypeAdresse) {
        typeAdresse = newTypeAdresse;
    }

}
