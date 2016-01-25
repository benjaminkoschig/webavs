package globaz.ij.vb.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJGenererFormulaireViewBean extends PRAbstractViewBeanSupport implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csTypeIJ = "";
    private String date = JACalendar.todayJJsMMsAAAA();
    private String dateRetour = "";
    private String displaySendToGed = "0";
    private String email = "";
    private String idFormulaire = "";

    private String idPrononce = "";
    private Boolean isSendToGed = Boolean.FALSE;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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
    public String getEmail() {
        if (JadeStringUtil.isEmpty(email) && (getSession() != null)) {
            email = getSession().getUserEMail();
        }

        return email;
    }

    /**
     * getter pour l'attribut id formulaire.
     * 
     * @return la valeur courante de l'attribut id formulaire
     */
    public String getIdFormulaire() {
        return idFormulaire;
    }

    /**
     * getter pour l'attribut id prononce.
     * 
     * @return la valeur courante de l'attribut id prononce
     */
    public String getIdPrononce() {
        return idPrononce;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
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
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * setter pour l'attribut id formulaire.
     * 
     * @param idFormulaire
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdFormulaire(String idFormulaire) {
        this.idFormulaire = idFormulaire;
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
