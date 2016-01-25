/*
 * Créé le 6 juin 05
 */
package globaz.apg.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APGenererLotViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String description = "";
    private String eMailAddress = "";
    private String prestationDateFin = "";
    private String typePrestation = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut description
     * 
     * @return la valeur courante de l'attribut description
     */
    public String getDescription() {
        return description;
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
     * @return
     */
    public String getPrestationDateFin() {
        return prestationDateFin;
    }

    /**
     * getter pour l'attribut type prestation
     * 
     * @return la valeur courante de l'attribut type prestation
     */
    public String getTypePrestation() {
        return typePrestation;
    }

    /**
     * setter pour l'attribut description
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDescription(String string) {
        description = string;
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
     * @param string
     */
    public void setPrestationDateFin(String string) {
        prestationDateFin = string;
    }

    /**
     * setter pour l'attribut type prestation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setTypePrestation(String string) {
        typePrestation = string;
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
        // TODO Raccord de méthode auto-généré
        return true;
    }

}
