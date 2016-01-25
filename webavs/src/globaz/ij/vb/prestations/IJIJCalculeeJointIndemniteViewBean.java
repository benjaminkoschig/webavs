package globaz.ij.vb.prestations;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.ij.db.prestations.IJIJCalculeeJointIndemnite;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJIJCalculeeJointIndemniteViewBean extends IJIJCalculeeJointIndemnite implements FWViewBeanInterface {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * getter pour l'attribut image garantie r3
     * 
     * @return la valeur courante de l'attribut image garantie r3
     */
    public String getLibelleGarantieR3() {
        return getSession().getCodeLibelle(getCsGarantieR3());
    }

    /**
     * getter pour l'attribut libelle type indemnisation.
     * 
     * @return la valeur courante de l'attribut libelle type indemnisation
     */
    public String getLibelleTypeIndemnisation() {
        return getSession().getCodeLibelle(getCsTypeIndemnisation());
    }

}
