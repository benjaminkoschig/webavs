package globaz.corvus.vb.process;

import globaz.globall.util.JACalendar;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * 
 * @author SCR
 * 
 */

public class REConcordanceCentraleViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String eMailAddress = "";
    private String moisAnnee = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut EMail address
     * 
     * @return la valeur courante de l'attribut EMail address
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    @Override
    public void retrieve() throws Exception {
        // Dummy function, to avoid Unsupported method execpetion from super
        // class.
        // Do not load anything from DB.
        setEMailAddress(getSession().getUserEMail());
        setMoisAnnee(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(JACalendar.todayJJsMMsAAAA()));
        ;
    }

    /**
     * setter pour l'attribut EMail address
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.vb.PRAbstractViewBeanSupport#validate()
     */
    @Override
    public boolean validate() {
        return true;
    }
}
