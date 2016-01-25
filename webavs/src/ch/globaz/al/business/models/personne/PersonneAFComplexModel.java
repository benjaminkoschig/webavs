package ch.globaz.al.business.models.personne;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.al.business.models.allocataire.AllocataireModel;
import ch.globaz.al.business.models.droit.EnfantModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

/**
 * Mod�le repr�sentant une personne AF (enfant ou allocataire)
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
     * Le mod�le allocataire
     */
    AllocataireModel allocataireModel = null;
    /**
     * Le mod�le enfant
     */
    EnfantModel enfantModel = null;
    /**
     * Le mod�le tiers
     */
    PersonneEtendueComplexModel personneEtendueComplexModel = null;

    /**
     * Le constructeur du mod�le
     */
    public PersonneAFComplexModel() {
        super();
        allocataireModel = new AllocataireModel();
        enfantModel = new EnfantModel();
        personneEtendueComplexModel = new PersonneEtendueComplexModel();
    }

    /**
     * @return allocataireModel Le mod�le allocataire
     */
    public AllocataireModel getAllocataireModel() {
        return allocataireModel;
    }

    /**
     * @return enfantModel Le mod�le enfant
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
     * @return personneEtendueComplexModel Le mod�le PersonneEtendueComplexModel
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
     *            Le mod�le allocataire
     */
    public void setAllocataireModel(AllocataireModel allocataireModel) {
        this.allocataireModel = allocataireModel;
    }

    /**
     * @param enfantModel
     *            Le mod�le enfant
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
     *            Le mod�le PersonneEtendueComplexModel
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
