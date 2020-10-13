package globaz.corvus.vb.adaptation;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * ViewBean pour la recherche des remarques de la centrale
 *
 * @author ESVE | Créé le 26 août 2020
 *
 */
public class REListeRemarquesCentraleViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String eMailAddress = "";
    private String moisAnnee = "";

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

    /**
     * setter pour l'attribut EMail address
     *
     * @param string une nouvelle valeur pour cet attribut
     */
    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
