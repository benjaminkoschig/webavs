/**
 * 
 */
package ch.globaz.amal.business.models.contribuable;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

/**
 * @author CBU
 * 
 */
public class ContribuableOnly extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleContribuable contribuable = null;
    private Boolean isContribuableHistorique = false;
    private PersonneEtendueComplexModel personneEtendue = null;

    public ContribuableOnly() {
        super();
        contribuable = new SimpleContribuable();
        personneEtendue = new PersonneEtendueComplexModel();
    }

    public ContribuableOnly(PersonneEtendueComplexModel personneEtendue) {
        super();
        this.personneEtendue = personneEtendue;
        contribuable = new SimpleContribuable();
    }

    public ContribuableOnly(SimpleContribuable contribuable) {
        super();
        this.contribuable = contribuable;
        personneEtendue = new PersonneEtendueComplexModel();
    }

    public SimpleContribuable getContribuable() {
        return contribuable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return contribuable.getId();
    }

    public Boolean getIsContribuableHistorique() {
        return isContribuableHistorique;
    }

    public PersonneEtendueComplexModel getPersonneEtendue() {
        return personneEtendue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return contribuable.getSpy();
    }

    public Boolean isContribuableHistorique() {
        return getIsContribuableHistorique();
    }

    public void setContribuable(SimpleContribuable contribuable) {
        this.contribuable = contribuable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        contribuable.setId(id);
    }

    public void setIsContribuableHistorique(Boolean isContribuableHistorique) {
        this.isContribuableHistorique = isContribuableHistorique;
    }

    public void setPersonneEtendueComplexModel(PersonneEtendueComplexModel personneEtendue) {
        this.personneEtendue = personneEtendue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        contribuable.setSpy(spy);
    }

}
