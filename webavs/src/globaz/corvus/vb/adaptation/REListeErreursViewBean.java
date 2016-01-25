package globaz.corvus.vb.adaptation;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * 
 * @author HPE
 * 
 */
public class REListeErreursViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String eMailAddress = "";
    private String moisAnnee = "";
    private String pourcentA = "";
    private String pourcentDe = "";

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

    public String getMoisAnnee() {
        return moisAnnee;
    }

    public String getPourcentA() {
        return pourcentA;
    }

    public String getPourcentDe() {
        return pourcentDe;
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

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

    public void setPourcentA(String pourcentA) {
        this.pourcentA = pourcentA;
    }

    public void setPourcentDe(String pourcentDe) {
        this.pourcentDe = pourcentDe;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
