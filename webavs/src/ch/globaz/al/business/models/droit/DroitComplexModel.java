package ch.globaz.al.business.models.droit;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

/**
 * modèle liste des droits (modèle complexe)
 * 
 * @author PTA
 * 
 */
public class DroitComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Modèle simple du droit
     */
    private DroitModel droitModel = null;// entité maître
    /**
     * Modèle complex de l'enfnat
     */
    private EnfantComplexModel enfantComplexModel = null;
    /**
     * Modèle du tiers bénéficiaire du droit
     */
    private TiersSimpleModel tiersBeneficiaireModel = null;

    /**
     * Constructeur du modèle du droit complet
     */
    public DroitComplexModel() {
        super();
        droitModel = new DroitModel();
        enfantComplexModel = new EnfantComplexModel();
        tiersBeneficiaireModel = new TiersSimpleModel();
    }

    /**
     * @return the droitModel
     */
    public DroitModel getDroitModel() {
        return droitModel;
    }

    /**
     * @return the enfantModel
     */
    public EnfantComplexModel getEnfantComplexModel() {
        return enfantComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {

        return droitModel.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {

        return droitModel.getSpy();
    }

    /**
     * Retourne le tiers bénéficiaire lié au droit
     * 
     * @return Le tiers bénéficiaire
     */
    public TiersSimpleModel getTiersBeneficiaireModel() {
        return tiersBeneficiaireModel;
    }

    /**
     * @param droitModel
     *            the droitModel to set
     */
    public void setDroitModel(DroitModel droitModel) {
        this.droitModel = droitModel;
    }

    /**
     * @param enfantComplexModel
     *            the enfantModel to set
     */
    public void setEnfantComplexModel(EnfantComplexModel enfantComplexModel) {
        this.enfantComplexModel = enfantComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        droitModel.setId(id);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        droitModel.setSpy(spy);

    }

    /**
     * Définit le tiers bénéficiaire
     * 
     * @param tiersBeneficiaireModel
     *            le tiers bénéficiaire à lier au droit
     */
    public void setTiersBeneficiaireModel(TiersSimpleModel tiersBeneficiaireModel) {
        this.tiersBeneficiaireModel = tiersBeneficiaireModel;
    }
}
