/*
 * Créé le 6 juin 05
 */
package globaz.ij.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJGenererLotViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String description = "";
    private String eMailAddress = "";

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
     * getter pour l'attribut EMail addess
     * 
     * @return la valeur courante de l'attribut EMail addess
     */
    public String getEMailAddress() {
        return eMailAddress;
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
     * setter pour l'attribut EMail addess
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEMailAddress(String string) {
        eMailAddress = string;
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
