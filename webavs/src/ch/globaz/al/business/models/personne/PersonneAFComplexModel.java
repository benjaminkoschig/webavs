package ch.globaz.al.business.models.personne;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.al.business.models.allocataire.AllocataireModel;
import ch.globaz.al.business.models.droit.EnfantModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

/**
 * Modèle représentant une personne AF (enfant ou allocataire)
 * 
 * @author GMO
 * 
 */
public class PersonneAFComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Le modèle allocataire
     */
    AllocataireModel allocataireModel = null;
    /**
     * Le modèle enfant
     */
    EnfantModel enfantModel = null;
    /**
     * Le modèle tiers
     */
    PersonneEtendueComplexModel personneEtendueComplexModel = null;

    /**
     * Le constructeur du modèle
     */
    public PersonneAFComplexModel() {
        super();
        allocataireModel = new AllocataireModel();
        enfantModel = new EnfantModel();
        personneEtendueComplexModel = new PersonneEtendueComplexModel();
    }

    /**
     * @return allocataireModel Le modèle allocataire
     */
    public AllocataireModel getAllocataireModel() {
        return allocataireModel;
    }

    /**
     * @return enfantModel Le modèle enfant
     */
    public EnfantModel getEnfantModel() {
        return enfantModel;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return personneEtendueComplexModel.getId();
    }

    /**
     * @return personneEtendueComplexModel Le modèle PersonneEtendueComplexModel
     */
    public PersonneEtendueComplexModel getPersonneEtendueComplexModel() {
        return personneEtendueComplexModel;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return personneEtendueComplexModel.getSpy();
    }

    /**
     * @param allocataireModel
     *            Le modèle allocataire
     */
    public void setAllocataireModel(AllocataireModel allocataireModel) {
        this.allocataireModel = allocataireModel;
    }

    /**
     * @param enfantModel
     *            Le modèle enfant
     */
    public void setEnfantModel(EnfantModel enfantModel) {
        this.enfantModel = enfantModel;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        personneEtendueComplexModel.setId(id);

    }

    /**
     * @param personneEtendueComplexModel
     *            Le modèle PersonneEtendueComplexModel
     */
    public void setPersonneEtendueComplexModel(PersonneEtendueComplexModel personneEtendueComplexModel) {
        this.personneEtendueComplexModel = personneEtendueComplexModel;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        personneEtendueComplexModel.setSpy(spy);

    }

}
