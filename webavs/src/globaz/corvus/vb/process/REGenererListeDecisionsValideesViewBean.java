package globaz.corvus.vb.process;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

public class REGenererListeDecisionsValideesViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateDepuis = "";
    private String dateJusqua = "";
    private String eMailAddress = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getDateDepuis() {
        return dateDepuis;
    }

    public String getDateDepuisDefaut() throws JAException {

        JADate date = new JADate(JACalendar.todayJJsMMsAAAA());

        String mois = String.valueOf(date.getMonth());
        if (mois.length() < 2) {
            mois = "0" + mois;
        }

        return "01" + "." + mois + "." + String.valueOf(date.getYear());

    }

    public String getDateJusqua() {
        return dateJusqua;
    }

    public String getDateJusquaDefaut() throws JAException {

        JADate date = new JADate(JACalendar.todayJJsMMsAAAA());

        JACalendar cal = new JACalendarGregorian();
        int last = cal.daysInMonth(date.getMonth(), date.getYear());

        String mois = String.valueOf(date.getMonth());
        if (mois.length() < 2) {
            mois = "0" + mois;
        }

        return String.valueOf(last) + "." + mois + "." + date.getYear();
    }

    /**
     * getter pour l'attribut EMail address
     * 
     * @return la valeur courante de l'attribut EMail address
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    public void setDateDepuis(String dateDepuis) {
        this.dateDepuis = dateDepuis;
    }

    public void setDateJusqua(String dateJusqua) {
        this.dateJusqua = dateJusqua;
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
