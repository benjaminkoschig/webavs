package globaz.ij.vb.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorianStandard;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.Calendar;

/**
 * @author hpe
 * 
 *         ViewBean reprenant de la JSP l'adresse email, la date d'envoi et les périodes à prendre en compte pour la
 *         création de la liste des formulaires non reçus
 */
public class IJListeFormulairesNonRecusViewBean extends PRAbstractViewBeanSupport implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String date = JACalendar.todayJJsMMsAAAA();
    private String eMailAddress = "";
    private String forAnnee = "";
    private String forMois = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String[] getAnneesList() {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        String[] annees = { Integer.toString(year - 1), Integer.toString(year - 1), Integer.toString(year),
                Integer.toString(year), Integer.toString(year + 1), Integer.toString(year + 1) };
        return annees;
    }

    /**
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     * @return
     */
    public String getEMailAddress() {
        if (JadeStringUtil.isEmpty(eMailAddress) && (getSession() != null)) {
            eMailAddress = getSession().getUserEMail();
        }
        return eMailAddress;
    }

    /**
     * @return
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * @return
     */
    public String getForMois() {
        return forMois;
    }

    public String getMoisCourant() {
        Calendar cal = Calendar.getInstance();
        return String.valueOf(cal.get(Calendar.MONTH) + 1);
    }

    public String[] getMoisList() {
        JACalendarGregorianStandard cal = new JACalendarGregorianStandard();
        BSession bsession = getSession();
        String langue = bsession.getLibelleLangue();

        String[] mois = { "1", JACalendar.getMonthName(1, langue), "2", JACalendar.getMonthName(2, langue), "3",
                JACalendar.getMonthName(3, langue), "4", JACalendar.getMonthName(4, langue), "5",
                JACalendar.getMonthName(5, langue), "6", JACalendar.getMonthName(6, langue), "7",
                JACalendar.getMonthName(7, langue), "8", JACalendar.getMonthName(8, langue), "9",
                JACalendar.getMonthName(9, langue), "10", JACalendar.getMonthName(10, langue), "11",
                JACalendar.getMonthName(11, langue), "12", JACalendar.getMonthName(12, langue) };

        return mois;
    }

    /**
     * @param string
     */
    public void setDate(String string) {
        date = string;
    }

    /**
     * @param string
     */
    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

    /**
     * @param string
     */
    public void setForAnnee(String string) {
        forAnnee = string;
    }

    /**
     * @param string
     */
    public void setForMois(String string) {
        forMois = string;
    }

    @Override
    public boolean validate() {
        return false;
    }

}
