package ch.globaz.al.business.models.allocataire;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.PaysSimpleModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

/**
 * Modèle complexe de l'allocataire
 * 
 * @author PTA
 * 
 */
public class AllocataireComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Le modèle de l'allocataire
     */
    private AllocataireModel allocataireModel = null; // entité maître

    /**
     * Le modèle simple du pays
     */
    private PaysSimpleModel paysModel = null;

    /**
     * Le modèle simple du pays de résidence
     */
    private PaysSimpleModel paysResidenceModel = null;

    /**
     * Le modèle complexe d'une personne étendue
     */
    private PersonneEtendueComplexModel personneEtendueComplexModel = null;

    /**
     * Constructeur du modèle
     */
    public AllocataireComplexModel() {
        super();
        allocataireModel = new AllocataireModel();
        paysModel = new PaysSimpleModel();
        personneEtendueComplexModel = new PersonneEtendueComplexModel();
        paysResidenceModel = new PaysSimpleModel();
    }

    /**
     * @return the allocataireModel
     */
    public AllocataireModel getAllocataireModel() {
        return allocataireModel;
    }

    /**
     * Retourne l'id de l'allocataire AF
     * 
     * @return the allocataire's id
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return allocataireModel.getId();
    }

    /**
     * Retourne la nationalité de l'allocataire
     * 
     * @return the paysModel
     */
    public PaysSimpleModel getPaysModel() {
        return paysModel;
    }

    /**
     * Retourne le pays de résidence de l'allocataire
     * 
     * @return the paysResidenceModel
     */
    public PaysSimpleModel getPaysResidenceModel() {
        return paysResidenceModel;
    }

    /**
     * @return the tiersModel
     */
    public PersonneEtendueComplexModel getPersonneEtendueComplexModel() {
        return personneEtendueComplexModel;
    }

    /**
     * Retourne le spy de l'allocataire AF
     * 
     * @return the allocataire's spy
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return allocataireModel.getSpy();
    }

    /**
     * @param allocataireModel
     *            the allocataireModel to set
     */
    public void setAllocataireModel(AllocataireModel allocataireModel) {
        this.allocataireModel = allocataireModel;
    }

    /**
     * Définit l'id allocataire
     * 
     * @param id
     *            id de l'allocataire allocataire id to set
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        allocataireModel.setId(id);

    }

    /**
     * Définit la nationalité de l'allocataire
     * 
     * @param paysModel
     *            : the paysModel to set
     */
    public void setPaysModel(PaysSimpleModel paysModel) {
        this.paysModel = paysModel;
    }

    /**
     * Définit le pays de résidence de l'allocataire
     * 
     * @param paysResidenceModel
     *            : the paysResidenceModel to set
     */
    public void setPaysResidenceModel(PaysSimpleModel paysResidenceModel) {
        this.paysResidenceModel = paysResidenceModel;
    }

    /**
     * Définit le tiers de l'allocataire
     * 
     * @param personneEtendue
     *            : the tiersModel to set
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
        allocataireModel.setSpy(spy);

    }
}
