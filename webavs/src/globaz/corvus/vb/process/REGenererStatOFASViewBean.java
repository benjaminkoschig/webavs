package globaz.corvus.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

public class REGenererStatOFASViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String AnneeStatistique = "";
    private String eMailAddress = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getAnneeStatistique() {
        return AnneeStatistique;
    }

    /**
     * getter pour l'attribut EMail address
     * 
     * @return la valeur courante de l'attribut EMail address
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    public void setAnneeStatistique(String anneeStatistique) {
        AnneeStatistique = anneeStatistique;
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
