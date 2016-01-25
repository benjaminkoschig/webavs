/*
 * Créé le 22 août 07
 */
package globaz.corvus.vb.paiementMensuel;

import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * <H1>Description</H1>
 * 
 * @author BSC
 */
public class REGenererListesVerificationViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String eMailAddress = "";

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

    /**
     * 
     * @return Date du prochain pmt mensuel au format : MM.AAAA
     * 
     */
    public String getMoisProchainPmt() {

        JADate dateProchainPmt;
        try {
            dateProchainPmt = new JADate(REPmtMensuel.getDateDernierPmt(getSession()));
            JACalendar cal = new JACalendarGregorian();
            dateProchainPmt = cal.addMonths(dateProchainPmt, 1);
            return PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dateProchainPmt.toStrAMJ());
        } catch (JAException e) {
            return "";
        }

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
