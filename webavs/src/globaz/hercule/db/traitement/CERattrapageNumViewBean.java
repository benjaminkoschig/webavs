package globaz.hercule.db.traitement;

import globaz.hercule.db.CEAbstractViewBean;

public class CERattrapageNumViewBean extends CEAbstractViewBean {

    private String fromAnnee = "";
    private String untilAnnee = "";

    /**
     * @return the fromAnnee
     */
    public String getFromAnnee() {
        return fromAnnee;
    }

    /**
     * @return the untilAnnee
     */
    public String getUntilAnnee() {
        return untilAnnee;
    }

    /**
     * @param fromAnnee
     *            the fromAnnee to set
     */
    public void setFromAnnee(String fromAnnee) {
        this.fromAnnee = fromAnnee;
    }

    /**
     * @param untilAnnee
     *            the untilAnnee to set
     */
    public void setUntilAnnee(String untilAnnee) {
        this.untilAnnee = untilAnnee;
    }

}
