package globaz.ij.vb.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.Calendar;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJGenererFormulairesViewBean extends PRAbstractViewBeanSupport implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csTypeIJ = "";
    private String date = JACalendar.todayJJsMMsAAAA();
    private String dateRetour = "";
    private String displaySendToGed = "0";
    private String eMailAddress = "";
    private String forAnneeDebut = "";
    private String forAnneeFin = "";
    private String forIdPrononce = "";
    private String forMoisDebut = "";
    private String forMoisFin = "";
    private Boolean genererFormulaires = null;
    private String idBaseIndemnisation = "";
    private String idPrononce = "";
    private String impressionFomulairesForEtat = "";

    private Boolean imprimerFormulaires = null;
    private Boolean isSendToGed = Boolean.FALSE;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     * @return
     */
    public String[] getAnneesList() {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        String[] annees = { Integer.toString(year - 1), Integer.toString(year - 1), Integer.toString(year),
                Integer.toString(year), Integer.toString(year + 1), Integer.toString(year + 1) };
        return annees;
    }

    /**
     * getter pour l'attribut cs type IJ.
     * 
     * @return la valeur courante de l'attribut cs type IJ
     */
    public String getCsTypeIJ() {
        return csTypeIJ;
    }

    /**
     * getter pour l'attribut date.
     * 
     * @return la valeur courante de l'attribut date
     */
    public String getDate() {

        return date;
    }

    /**
     * @return
     */
    public String getDateRetour() {
        return dateRetour;
    }

    public String getDisplaySendToGed() {
        return displaySendToGed;
    }

    /**
     * getter pour l'attribut email.
     * 
     * @return la valeur courante de l'attribut email
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
    public String getForAnneeDebut() {
        return forAnneeDebut;
    }

    /**
     * @return
     */
    public String getForAnneeFin() {
        return forAnneeFin;
    }

    /**
     * @return
     */
    public String getForIdPrononce() {
        return forIdPrononce;
    }

    /**
     * @return
     */
    public String getForMoisDebut() {
        return forMoisDebut;
    }

    /**
     * @return
     */
    public String getForMoisFin() {
        return forMoisFin;
    }

    /**
     * @return
     */
    public Boolean getGenererFormulaires() {
        return genererFormulaires;
    }

    /**
     * @return
     */
    public String getIdBaseIndemnisation() {
        return idBaseIndemnisation;
    }

    /**
     * getter pour l'attribut id prononce.
     * 
     * @return la valeur courante de l'attribut id prononce
     */
    public String getIdPrononce() {
        return idPrononce;
    }

    /**
     * @return
     */
    public String getImpressionFomulairesForEtat() {
        return impressionFomulairesForEtat;
    }

    /**
     * @return
     */
    public Boolean getImprimerFormulaires() {
        return imprimerFormulaires;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    /**
     * 
     * @return
     */
    public String getMoisCourant() {
        Calendar cal = Calendar.getInstance();
        return String.valueOf(cal.get(Calendar.MONTH) + 1);
    }

    /**
     * 
     * @return
     */
    public String[] getMoisList() {
        BSession bsession = getSession();
        String langue = bsession.getIdLangueISO();

        String[] mois = { "1", JACalendar.getMonthName(1, langue), "2", JACalendar.getMonthName(2, langue), "3",
                JACalendar.getMonthName(3, langue), "4", JACalendar.getMonthName(4, langue), "5",
                JACalendar.getMonthName(5, langue), "6", JACalendar.getMonthName(6, langue), "7",
                JACalendar.getMonthName(7, langue), "8", JACalendar.getMonthName(8, langue), "9",
                JACalendar.getMonthName(9, langue), "10", JACalendar.getMonthName(10, langue), "11",
                JACalendar.getMonthName(11, langue), "12", JACalendar.getMonthName(12, langue) };
        return mois;
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public void retrieve() throws Exception {
        // jamais de retrieve sur ce bean
    }

    /**
     * setter pour l'attribut cs type IJ.
     * 
     * @param csTypeIJ
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeIJ(String csTypeIJ) {
        this.csTypeIJ = csTypeIJ;
    }

    /**
     * setter pour l'attribut date.
     * 
     * @param date
     *            une nouvelle valeur pour cet attribut
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @param string
     */
    public void setDateRetour(String string) {
        dateRetour = string;
    }

    public void setDisplaySendToGed(String displaySendToGed) {
        this.displaySendToGed = displaySendToGed;
    }

    /**
     * setter pour l'attribut email.
     * 
     * @param email
     *            une nouvelle valeur pour cet attribut
     */
    public void setEMailAddress(String email) {
        eMailAddress = email;
    }

    /**
     * @param string
     */
    public void setForAnneeDebut(String string) {
        forAnneeDebut = string;
    }

    /**
     * @param string
     */
    public void setForAnneeFin(String string) {
        forAnneeFin = string;
    }

    /**
     * @param string
     */
    public void setForIdPrononce(String string) {
        forIdPrononce = string;
    }

    /**
     * @param string
     */
    public void setForMoisDebut(String string) {
        forMoisDebut = string;
    }

    /**
     * @param string
     */
    public void setForMoisFin(String string) {
        forMoisFin = string;
    }

    /**
     * @param boolean1
     */
    public void setGenererFormulaires(Boolean boolean1) {
        genererFormulaires = boolean1;
    }

    /**
     * @param string
     */
    public void setIdBaseIndemnisation(String string) {
        idBaseIndemnisation = string;
    }

    /**
     * setter pour l'attribut id prononce.
     * 
     * @param idPrononce
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPrononce(String idPrononce) {
        this.idPrononce = idPrononce;
    }

    /**
     * @param string
     */
    public void setImpressionFomulairesForEtat(String string) {
        impressionFomulairesForEtat = string;
    }

    /**
     * @param boolean1
     */
    public void setImprimerFormulaires(Boolean boolean1) {
        imprimerFormulaires = boolean1;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    public boolean validate() {
        return false;
    }

}
