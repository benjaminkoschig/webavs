package ch.globaz.al.business.models.droit;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.PaysSimpleModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

/**
 * Modèle complexe d'un enfant regroupant les données enfant AF, données tiers et données pays (uniquement pour afficher
 * le code ISO dans la liste des droits...)
 * 
 * @author GMO
 * 
 */
public class EnfantComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Modèle de l'enfant
     */
    private EnfantModel enfantModel = null;
    /**
     * Modèle simple du pays
     */
    private PaysSimpleModel paysModel = null;
    /**
     * Modèle complexe de la personne
     */
    private PersonneEtendueComplexModel personneEtendueComplexModel = null;

    /**
     * Constructeur du modèle
     */
    public EnfantComplexModel() {
        super();
        enfantModel = new EnfantModel();
        paysModel = new PaysSimpleModel();
        personneEtendueComplexModel = new PersonneEtendueComplexModel();
    }

    /**
     * Constructeur du modèle initialisant les "sous-modèles"
     * 
     * @param enfantModel
     *            données enfant AF
     * @param paysModel
     *            pays de résidence
     * @param personneEtendue
     *            données enfant tiers
     */
    public EnfantComplexModel(EnfantModel enfantModel, PaysSimpleModel paysModel,
            PersonneEtendueComplexModel personneEtendue) {
        super();
        this.enfantModel = enfantModel;
        this.paysModel = paysModel;
        personneEtendueComplexModel = personneEtendue;
    }

    /**
     * @return the enfantModel
     */
    public EnfantModel getEnfantModel() {
        return enfantModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return enfantModel.getId();
    }

    /**
     * 
     * @return the paysModel (pays de résidence de l'enfant)
     */
    public PaysSimpleModel getPaysModel() {
        return paysModel;
    }

    /**
     * @return the tiersModel
     */
    public PersonneEtendueComplexModel getPersonneEtendueComplexModel() {
        return personneEtendueComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return enfantModel.getSpy();
    }

    /**
     * @param enfantModel
     *            the enfantModel to set
     */
    public void setEnfantModel(EnfantModel enfantModel) {
        this.enfantModel = enfantModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        enfantModel.setId(id);
    }

    /**
     * définit le paysModel (pays de résidence de l'enfant)
     * 
     * @param paysModel
     *            : the paysModel to set
     */
    public void setPaysModel(PaysSimpleModel paysModel) {
        this.paysModel = paysModel;
    }

    /**
     * @param personneEtendue
     *            the tiersModel to set
     */
    public void setPersonneEtendueComplexModel(PersonneEtendueComplexModel personneEtendue) {
        personneEtendueComplexModel = personneEtendue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        enfantModel.setSpy(spy);
    }
}
