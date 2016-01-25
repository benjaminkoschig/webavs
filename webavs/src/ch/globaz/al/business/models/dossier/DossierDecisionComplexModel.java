package ch.globaz.al.business.models.dossier;

import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.pyxis.business.model.TiersLiaisonComplexModel;

/**
 * Modèle complexe ses dossiers pour l'impression de décision
 * 
 * @author PTA
 * 
 */
public class DossierDecisionComplexModel extends DossierComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Le modèle simple de l'affiliation
     */
    private AffiliationSimpleModel affiliationSimpleModel = null;

    /**
     * Le modèle complexe du tiers de liaison
     */
    private TiersLiaisonComplexModel tiersLiaisonComplexModel = null;

    /**
     * Constructeur
     */
    public DossierDecisionComplexModel() {
        super();
        affiliationSimpleModel = new AffiliationSimpleModel();
        tiersLiaisonComplexModel = new TiersLiaisonComplexModel();

    }

    /**
     * @return the affiliationSimpleModel
     */
    public AffiliationSimpleModel getAffiliationSimpleModel() {
        return affiliationSimpleModel;
    }

    /**
     * @return the dossierModel
     */
    @Override
    public DossierModel getDossierModel() {
        return dossierModel;
    }

    /**
     * @return the tiersLiaisonComplexModel
     */
    public TiersLiaisonComplexModel getTiersLiaisonComplexModel() {
        return tiersLiaisonComplexModel;
    }

    /**
     * @param affiliationSimpleModel
     *            the affiliationSimpleModel to set
     */
    public void setAffiliationSimpleModel(AffiliationSimpleModel affiliationSimpleModel) {
        this.affiliationSimpleModel = affiliationSimpleModel;
    }

    /**
     * @param tiersLiaisonComplexModel
     *            the tiersLiaisonComplexModel to set
     */
    public void setTiersLiaisonComplexModel(TiersLiaisonComplexModel tiersLiaisonComplexModel) {
        this.tiersLiaisonComplexModel = tiersLiaisonComplexModel;
    }

}
