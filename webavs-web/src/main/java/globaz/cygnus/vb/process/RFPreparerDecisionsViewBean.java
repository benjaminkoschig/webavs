/*
 * Créé le 03 sept. 07
 */
package globaz.cygnus.vb.process;

import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * <H1>Description</H1>
 * 
 * @author JJE
 */
public class RFPreparerDecisionsViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateSurDocument = "";
    private String eMailAddress = "";
    private String idGestionnaire = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getDateSurDocument() {
        return dateSurDocument;
    }

    /**
     * getter pour l'attribut EMail address
     * 
     * @return la valeur courante de l'attribut EMail address
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    // Methode controlant la propriété utilisé
    public boolean preparerDecisionGestionnaire() throws Exception {
        return RFPropertiesUtils.utiliserGestionnaireViewBean();
    }

    @Override
    public void retrieve() throws Exception {
    }

    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
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

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
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
