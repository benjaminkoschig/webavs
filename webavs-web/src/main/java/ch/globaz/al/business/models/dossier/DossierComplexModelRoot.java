package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

/**
 * Modèle racine d'un dossier. Déclare les méthodes devant être définie pour chaque modèle complexe de dossier
 * 
 * @author jts
 * 
 * @see ch.globaz.al.business.models.dossier.DossierComplexModel
 * @see ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel
 */
public class DossierComplexModelRoot extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Le modèle de la caisse AF du conjoint
     */
    AdministrationComplexModel caisseAFComplexModel = null;
    /**
     * Le modèle du dossier
     */
    DossierModel dossierModel = null;// root-model
    /**
     * Le modèle du tiers bénéficiaire
     */
    TiersSimpleModel tiersBeneficiaireModel = null;

    /**
     * Constructeur du modèle
     */
    public DossierComplexModelRoot() {
        super();
        dossierModel = new DossierModel();
        tiersBeneficiaireModel = new TiersSimpleModel();
        caisseAFComplexModel = new AdministrationComplexModel();
    }

    /**
     * @return caisseAFComplexModel Le modèle caisse AF du conjoint
     */
    public AdministrationComplexModel getCaisseAFComplexModel() {
        return caisseAFComplexModel;
    }

    /**
     * @return the dossierModel Le modèle dossier
     */
    public DossierModel getDossierModel() {
        return dossierModel;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return dossierModel.getId();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return dossierModel.getSpy();
    }

    /**
     * @return tiersBeneficiaireModel Le modèle du tiers bénéficiaire
     */
    public TiersSimpleModel getTiersBeneficiaireModel() {
        return tiersBeneficiaireModel;
    }

    /**
     * @param caisseAFModel
     *            Le modèle caisse AF du conjoint
     */
    public void setCaisseAFComplexModel(AdministrationComplexModel caisseAFModel) {
        caisseAFComplexModel = caisseAFModel;
    }

    /**
     * @param dossierModel
     *            Le modèle dossier
     */
    public void setDossierModel(DossierModel dossierModel) {
        this.dossierModel = dossierModel;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        dossierModel.setId(id);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        dossierModel.setSpy(spy);
    }

    /**
     * @param tiersBeneficiaireModel
     *            Le modèle du tiers bénéficiaire
     */
    public void setTiersBeneficiaireModel(TiersSimpleModel tiersBeneficiaireModel) {
        this.tiersBeneficiaireModel = tiersBeneficiaireModel;
    }
}
