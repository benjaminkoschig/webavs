package globaz.corvus.vb.adaptation;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * 
 * @author HPE
 * 
 */
public class REListeAnnoncesSubsequentesViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String eMailAddress = "";
    private String moisAnnee = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

    @Override
    public boolean validate() {
        return true;
    }

}
