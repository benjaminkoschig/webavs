package globaz.corvus.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

public class REGenererListeImpotSourceViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String canton = "";
    private String eMailAddress = "";
    private String moisDebut = "";
    private String moisFin = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getCanton() {
        return canton;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getMoisDebut() {
        return moisDebut;
    }

    public String getMoisFin() {
        return moisFin;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public void setEMailAddress(String mailAddress) {
        eMailAddress = mailAddress;
    }

    public void setMoisDebut(String moisDebut) {
        this.moisDebut = moisDebut;
    }

    public void setMoisFin(String moisFin) {
        this.moisFin = moisFin;
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
