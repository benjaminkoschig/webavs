package globaz.apg.vb.process;

import globaz.babel.api.doc.impl.CTScalableDocumentAbstractViewBeanDefaultImpl;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;

/**
 * @author JJE
 */
public class APGenererDecisionRefusViewBean extends CTScalableDocumentAbstractViewBeanDefaultImpl {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateSurDocument = JACalendar.todayJJsMMsAAAA();
    private String eMailAddress = "";
    private String idDroit = "";
    private Boolean isSendToGED = null;
    private String noAVS = "";

    public APGenererDecisionRefusViewBean() {
        super();
    }

    public String getDateSurDocument() {
        return dateSurDocument;
    }

    @Override
    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public Boolean getIsSendToGED() {
        return isSendToGED;
    }

    public String getNoAVS() {
        return noAVS;
    }

    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
    }

    @Override
    public void setEMailAddress(String mailAddress) {
        eMailAddress = mailAddress;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setIsSendToGED(Boolean isSendToGED) {
        this.isSendToGED = isSendToGED;
    }

    public void setNoAVS(String noAVS) {
        this.noAVS = noAVS;
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

        if (JAUtil.isDateEmpty(dateSurDocument)) {
            _addError(getSession().getLabel("JSP_ERREUR_DATE_SUR_DOCUMENT"));
            retValue = false;
        }

        return retValue;
    }

}
