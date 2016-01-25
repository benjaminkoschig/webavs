/*
 * Créé le 03 sept. 07
 */
package globaz.cygnus.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * <H1>Description</H1>
 * 
 * @author JJE
 */
public class RFAdaptationsJournaliereViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String eMailAddress = "";
    private String idGestionnaire = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    @Override
    public void retrieve() throws Exception {

    }

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
