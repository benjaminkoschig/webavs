package globaz.corvus.vb.adaptation;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * 
 * @author HPE
 * 
 */
public class REListePrstAugmenteesViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String eMailAddress = "";
    private Boolean isLstPrestAugManuellement = false;
    private Boolean isLstPrestNonAdapte = false;
    private Boolean isLstPrestProgrammeCentrale = false;
    private Boolean isLstPrestTraitementAutomatique = false;
    private Boolean isLstRecapAdaptation = false;
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

    public String getMoisAnnee() {
        return moisAnnee;
    }

    public Boolean isLstPrestAugManuellement() {
        return isLstPrestAugManuellement;
    }

    public Boolean isLstPrestNonAdapte() {
        return isLstPrestNonAdapte;
    }

    public Boolean isLstPrestProgrammeCentrale() {
        return isLstPrestProgrammeCentrale;
    }

    public Boolean isLstPrestTraitementAutomatique() {
        return isLstPrestTraitementAutomatique;
    }

    public Boolean isLstRecapAdaptation() {
        return isLstRecapAdaptation;
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

    public void setIsLstPrestAugManuellement(Boolean isLstPrestAugManuellement) {
        this.isLstPrestAugManuellement = isLstPrestAugManuellement;
    }

    public void setIsLstPrestNonAdapte(Boolean isLstPrestNonAdapte) {
        this.isLstPrestNonAdapte = isLstPrestNonAdapte;
    }

    public void setIsLstPrestProgrammeCentrale(Boolean isLstPrestProgrammeCentrale) {
        this.isLstPrestProgrammeCentrale = isLstPrestProgrammeCentrale;
    }

    public void setIsLstPrestTraitementAutomatique(Boolean isLstPrestTraitementAutomatique) {
        this.isLstPrestTraitementAutomatique = isLstPrestTraitementAutomatique;
    }

    public void setIsLstRecapAdaptation(Boolean isLstRecapAdaptation) {
        this.isLstRecapAdaptation = isLstRecapAdaptation;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
