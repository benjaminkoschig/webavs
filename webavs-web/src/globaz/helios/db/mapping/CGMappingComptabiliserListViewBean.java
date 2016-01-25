package globaz.helios.db.mapping;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author dda
 * 
 */
public class CGMappingComptabiliserListViewBean extends CGMappingComptabiliserManager implements
        FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdExterneCompteDestinationLike;
    private String forIdExterneCompteSourceLike;
    private String forIdExterneContreEcritureDestinationLike;

    /**
     * @see globaz.helios.db.mapping.CGMappingComptabiliserManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CGMappingComptabiliserViewBean();
    }

    public String getForIdExterneCompteDestinationLike() {
        return forIdExterneCompteDestinationLike;
    }

    public String getForIdExterneCompteSourceLike() {
        return forIdExterneCompteSourceLike;
    }

    public String getForIdExterneContreEcritureDestinationLike() {
        return forIdExterneContreEcritureDestinationLike;
    }

    public void setForIdExterneCompteDestinationLike(String forIdExterneCompteDestinationLike) {
        this.forIdExterneCompteDestinationLike = forIdExterneCompteDestinationLike;
    }

    public void setForIdExterneCompteSourceLike(String forIdExterneCompteSourceLike) {
        this.forIdExterneCompteSourceLike = forIdExterneCompteSourceLike;
    }

    public void setForIdExterneContreEcritureDestinationLike(String forIdExterneContreEcritureDestinationLike) {
        this.forIdExterneContreEcritureDestinationLike = forIdExterneContreEcritureDestinationLike;
    }

    public boolean shouldDiplay(CGMappingComptabiliserViewBean mapping) {
        if ((!JadeStringUtil.isBlank(getForIdExterneCompteSourceLike()))
                && (!JadeStringUtil.isBlank(mapping.getIdExterneCompteSource()))
                && (!mapping.getIdExterneCompteSource().startsWith(getForIdExterneCompteSourceLike()))) {
            return false;
        }

        if ((!JadeStringUtil.isBlank(getForIdExterneCompteDestinationLike()))
                && (!JadeStringUtil.isBlank(mapping.getIdExterneCompteDestination()))
                && (!mapping.getIdExterneCompteDestination().startsWith(getForIdExterneCompteDestinationLike()))) {
            return false;
        }

        if ((!JadeStringUtil.isBlank(getForIdExterneContreEcritureDestinationLike()))
                && (!JadeStringUtil.isBlank(mapping.getIdExterneContreEcritureDestination()))
                && (!mapping.getIdExterneContreEcritureDestination().startsWith(
                        getForIdExterneContreEcritureDestinationLike()))) {
            return false;
        }

        return true;
    }

}
