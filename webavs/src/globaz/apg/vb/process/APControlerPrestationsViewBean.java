/*
 * Créé le 2 juin 05
 */
package globaz.apg.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APControlerPrestationsViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Boolean controlerPrestations = Boolean.FALSE;
    private String eMailAddress = "";
    private Boolean imprimerListeDroits = Boolean.FALSE;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut controler prestations
     * 
     * @return la valeur courante de l'attribut controler prestations
     */
    public Boolean getControlerPrestations() {
        return controlerPrestations;
    }

    /**
     * getter pour l'attribut EMail address
     * 
     * @return la valeur courante de l'attribut EMail address
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    /**
     * getter pour l'attribut imprimer liste droits
     * 
     * @return la valeur courante de l'attribut imprimer liste droits
     */
    public Boolean getImprimerListeDroits() {
        return imprimerListeDroits;
    }

    /**
     * setter pour l'attribut controler prestations
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setControlerPrestations(Boolean boolean1) {
        controlerPrestations = boolean1;
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
     * setter pour l'attribut imprimer liste droits
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setImprimerListeDroits(Boolean boolean1) {
        imprimerListeDroits = boolean1;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean validate() {
        return true;
    }
}
