package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * Permet d'effectuer des recherches dans les en-t�tes de prestations pour afficher la liste des prestations par r�caps
 * Crit�res support�s :
 * <ul>
 * <li>forIdRecap</li>
 * </ul>
 * 
 * @author GMO
 * 
 */
public class EntetePrestationListRecapComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * S�lection sur l'id r�cap des ent�tes prestation
     */
    private String forIdRecap = null;

    /**
     * @return forIdRecap
     */
    public String getForIdRecap() {
        return forIdRecap;
    }

    /**
     * D�finit le crit�re id r�cap
     * 
     * @param forIdRecap
     *            id de la r�cap
     */
    public void setForIdRecap(String forIdRecap) {
        this.forIdRecap = forIdRecap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<EntetePrestationListRecapComplexModel> whichModelClass() {
        return EntetePrestationListRecapComplexModel.class;
    }

}
