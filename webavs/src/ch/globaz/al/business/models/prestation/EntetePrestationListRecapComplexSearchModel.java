package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * Permet d'effectuer des recherches dans les en-têtes de prestations pour afficher la liste des prestations par récaps
 * Critères supportés :
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
     * Sélection sur l'id récap des entêtes prestation
     */
    private String forIdRecap = null;

    /**
     * @return forIdRecap
     */
    public String getForIdRecap() {
        return forIdRecap;
    }

    /**
     * Définit le critère id récap
     * 
     * @param forIdRecap
     *            id de la récap
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
