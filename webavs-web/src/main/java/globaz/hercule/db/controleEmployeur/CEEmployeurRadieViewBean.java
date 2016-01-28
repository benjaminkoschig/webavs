package globaz.hercule.db.controleEmployeur;

import globaz.hercule.db.CEAbstractViewBean;

/**
 * @author SCO
 * @since 4 aout 2010
 */
public class CEEmployeurRadieViewBean extends CEAbstractViewBean {

    private String forMotifRadiation = "";
    private String fromDateRadiation = "";
    private String fromMasseSalariale = "";
    private String toDateRadiation = "";
    private String toMasseSalariale = "";
    private String typeAdresse = "";

    /**
     * Constructeur de CEEmployeurRadieViewBean
     */
    public CEEmployeurRadieViewBean() throws Exception {
    }

    public String getForMotifRadiation() {
        return forMotifRadiation;
    }

    /**
     * getter
     */

    public String getFromDateRadiation() {
        return fromDateRadiation;
    }

    public String getFromMasseSalariale() {
        return fromMasseSalariale;
    }

    public String getToDateRadiation() {
        return toDateRadiation;
    }

    public String getToMasseSalariale() {
        return toMasseSalariale;
    }

    public String getTypeAdresse() {
        return typeAdresse;
    }

    public void setForMotifRadiation(String forMotifRadiation) {
        this.forMotifRadiation = forMotifRadiation;
    }

    /**
     * setter
     */

    public void setFromDateRadiation(String newFromDateRadiation) {
        fromDateRadiation = newFromDateRadiation;
    }

    public void setFromMasseSalariale(String newFromMasseSalariale) {
        fromMasseSalariale = newFromMasseSalariale;
    }

    public void setToDateRadiation(String newToDateRadiation) {
        toDateRadiation = newToDateRadiation;
    }

    public void setToMasseSalariale(String newToMasseSalariale) {
        toMasseSalariale = newToMasseSalariale;
    }

    public void setTypeAdresse(String newTypeAdresse) {
        typeAdresse = newTypeAdresse;
    }

}
