package globaz.apg.vb.process;

import globaz.globall.util.JACalendar;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APGenererDecisionAMATViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private boolean calcule;
    private String date = JACalendar.todayJJsMMsAAAA();
    private boolean decision = true;
    private String displaySendToGed = "0";
    private String email = "";

    private String idDroit = "";
    private Boolean isSendToGed = Boolean.FALSE;
    // Permet de filter les MATCIAB2 des décisions pour les faire ressortir sur un document séparé
    private Boolean isMatciab2ViewBean = Boolean.FALSE;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut date
     * 
     * @return la valeur courante de l'attribut date
     */
    public String getDate() {
        return date;
    }

    public String getDisplaySendToGed() {
        return displaySendToGed;
    }

    /**
     * getter pour l'attribut email
     * 
     * @return la valeur courante de l'attribut email
     */
    public String getEmail() {
        return email;
    }

    /**
     * getter pour l'attribut id droit
     * 
     * @return la valeur courante de l'attribut id droit
     */
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * getter pour l'attribut isMatciab2ViewBean
     *
     * @return la valeur courante de l'attribut isMatciab2ViewBean
     */
    public Boolean getIsMatciab2ViewBean() {
        return isMatciab2ViewBean;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    /**
     * getter pour l'attribut calcule
     *
     * @return la valeur courante de l'attribut calcule
     */
    public boolean isCalcule() {
        return calcule;
    }

    /**
     * getter pour l'attribut decision
     * 
     * @return la valeur courante de l'attribut decision
     */
    public boolean isDecision() {
        return decision;
    }

    /**
     * setter pour l'attribut calcule
     * 
     * @param calcule
     *            une nouvelle valeur pour cet attribut
     */
    public void setCalcule(boolean calcule) {
        this.calcule = calcule;
    }

    /**
     * setter pour l'attribut date
     * 
     * @param date
     *            une nouvelle valeur pour cet attribut
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * setter pour l'attribut decision
     * 
     * @param decision
     *            une nouvelle valeur pour cet attribut
     */
    public void setDecision(boolean decision) {
        this.decision = decision;
    }

    /**
     * setter pour l'attribut isMatciab2ViewBean
     *
     * @param isMatciab2ViewBean
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsMatciab2ViewBean(Boolean isMatciab2ViewBean) {
        this.isMatciab2ViewBean = isMatciab2ViewBean;
    }

    public void setDisplaySendToGed(String displaySendToGed) {
        this.displaySendToGed = displaySendToGed;
    }

    /**
     * setter pour l'attribut email
     * 
     * @param email
     *            une nouvelle valeur pour cet attribut
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * setter pour l'attribut id droit
     * 
     * @param idDroit
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean validate() {
        return true;
    }

}
