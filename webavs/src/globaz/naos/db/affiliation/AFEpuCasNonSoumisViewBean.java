package globaz.naos.db.affiliation;

import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;

/**
 * @author BJO
 * 
 */
public class AFEpuCasNonSoumisViewBean extends AFAbstractViewBean {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String email;
    private String fromDate = JACalendar.todayJJsMMsAAAA();
    private String simulation;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public AFEpuCasNonSoumisViewBean() throws Exception {

    }

    /**
     * @param session
     */
    public AFEpuCasNonSoumisViewBean(BSession session) {

    }

    /**
     * getter pour l'attribut email
     * 
     * @return la valeur courante de l'attribut email
     */
    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }

        return email;
    }

    /**
     * getter pour l'attribut from date.
     * 
     * @return la valeur courante de l'attribut from date
     */
    public String getFromDate() {
        return fromDate;

    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     * @return
     */
    public String getSimulation() {
        return simulation;
    }

    /**
     * setter pour l'attribut email
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEmail(String string) {
        email = string;
    }

    /**
     * setter pour l'attribut from date.
     * 
     * @param fromDate
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @param simulation
     */
    public void setSimulation(String simulation) {
        this.simulation = simulation;
    }

}
