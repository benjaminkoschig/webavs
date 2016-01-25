package ch.globaz.al.business.models.droit;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

/**
 * mod�le liste des droits (mod�le complexe)
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
     * Mod�le simple du droit
     */
    private DroitModel droitModel = null;// entit� ma�tre
    /**
     * Mod�le complex de l'enfnat
     */
    private EnfantComplexModel enfantComplexModel = null;
    /**
     * Mod�le du tiers b�n�ficiaire du droit
     */
    private TiersSimpleModel tiersBeneficiaireModel = null;

    /**
     * Constructeur du mod�le du droit complet
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
     * Retourne le tiers b�n�ficiaire li� au droit
     * 
     * @return Le tiers b�n�ficiaire
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
     * D�finit le tiers b�n�ficiaire
     * 
     * @param tiersBeneficiaireModel
     *            le tiers b�n�ficiaire � lier au droit
     */
    public void setTiersBeneficiaireModel(TiersSimpleModel tiersBeneficiaireModel) {
        this.tiersBeneficiaireModel = tiersBeneficiaireModel;
    }
}
