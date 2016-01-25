/**
 * 
 */
package ch.globaz.perseus.business.models.situationfamille;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

/**
 * @author DDE
 * 
 */
public class MembreFamille extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private PersonneEtendueComplexModel personneEtendue = null;
    private SimpleMembreFamille simpleMembreFamille = null;

    public MembreFamille() {
        super();
        personneEtendue = new PersonneEtendueComplexModel();
        simpleMembreFamille = new SimpleMembreFamille();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleMembreFamille.getId();
    }

    /**
     * @return the personneEtendue
     */
    public PersonneEtendueComplexModel getPersonneEtendue() {
        return personneEtendue;
    }

    /**
     * @return the simpleMembreFamille
     */
    public SimpleMembreFamille getSimpleMembreFamille() {
        return simpleMembreFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleMembreFamille.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleMembreFamille.setId(id);
    }

    /**
     * @param personneEtendue
     *            the personneEtendue to set
     */
    public void setPersonneEtendue(PersonneEtendueComplexModel personneEtendue) {
        this.personneEtendue = personneEtendue;
    }

    /**
     * @param simpleMembreFamille
     *            the simpleMembreFamille to set
     */
    public void setSimpleMembreFamille(SimpleMembreFamille simpleMembreFamille) {
        this.simpleMembreFamille = simpleMembreFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleMembreFamille.setSpy(spy);
    }

}
