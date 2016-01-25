/*
 * Créé le 28 juin 05
 */
package globaz.apg.vb.process;

import globaz.apg.db.lots.APLot;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APGenererDecomptesViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateSurDocument = JACalendar.todayJJsMMsAAAA();
    private String dateValeurComptable = JACalendar.todayJJsMMsAAAA();
    private String descriptionLot = null;
    private String displaySendToGed = "0";
    private String eMailAddress = "";
    private String etatLot = "";
    private String idLot = "";
    private Boolean isDefinitif = null;
    private Boolean isSendToGed = Boolean.FALSE;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut date sur document
     * 
     * @return la valeur courante de l'attribut date sur document
     */
    public String getDateSurDocument() {
        return dateSurDocument;
    }

    /**
     * getter pour l'attribut date valeur comptable
     * 
     * @return la valeur courante de l'attribut date valeur comptable
     */
    public String getDateValeurComptable() {
        return dateValeurComptable;
    }

    /**
     * getter pour l'attribut description lot
     * 
     * @return la valeur courante de l'attribut description lot
     */
    public String getDescriptionLot() {
        if (descriptionLot == null) {
            BSession session = (BSession) getISession();

            try {
                APLot lot = new APLot();

                lot.setSession(session);
                lot.setIdLot(idLot);
                lot.retrieve();
                descriptionLot = lot.getDescription();
            } catch (Exception e) {
                descriptionLot = session.getLabel("ECHEC_CHARGER_DESCRIPTION_LOT");
            }
        }

        return descriptionLot;
    }

    public String getDisplaySendToGed() {
        return displaySendToGed;
    }

    /**
     * getter pour l'attribut EMail address
     * 
     * @return la valeur courante de l'attribut EMail address
     */
    public String getEMailAddress() {
        if (JadeStringUtil.isEmpty(eMailAddress)) {
            eMailAddress = getSession().getUserEMail();
        }

        return eMailAddress;
    }

    /**
     * getter pour l'attribut etat lot
     * 
     * @return la valeur courante de l'attribut etat lot
     */
    public String getEtatLot() {
        return etatLot;
    }

    /**
     * getter pour l'attribut no lot
     * 
     * @return la valeur courante de l'attribut no lot
     */
    public String getIdLot() {
        return idLot;
    }

    /**
     * getter pour l'attribut is definitif
     * 
     * @return la valeur courante de l'attribut is definitif
     */
    public Boolean getIsDefinitif() {
        return isDefinitif;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    /**
     * setter pour l'attribut date sur document
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateSurDocument(String string) {
        dateSurDocument = string;
    }

    /**
     * setter pour l'attribut date valeur comptable
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateValeurComptable(String string) {
        dateValeurComptable = string;
    }

    /**
     * setter pour l'attribut description lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDescriptionLot(String string) {
        descriptionLot = string;
    }

    public void setDisplaySendToGed(String displaySendToGed) {
        this.displaySendToGed = displaySendToGed;
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

    /**
     * setter pour l'attribut etat lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEtatLot(String string) {
        etatLot = string;
    }

    /**
     * setter pour l'attribut no lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdLot(String string) {
        idLot = string;
    }

    /**
     * setter pour l'attribut is definitif
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsDefinitif(Boolean boolean1) {
        isDefinitif = boolean1;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    /**
     * (non-Javadoc)
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.apg.vb.PRAbstractViewBeanSupport#validate()
     */
    @Override
    public boolean validate() {
        boolean retValue = true;

        if (JAUtil.isDateEmpty(dateSurDocument) || JAUtil.isDateEmpty(dateValeurComptable)) {
            _addError("DATES_OBLIGATOIRES");
            retValue = false;
        }

        return retValue;
    }

}
