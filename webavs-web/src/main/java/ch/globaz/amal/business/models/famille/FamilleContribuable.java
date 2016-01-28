/**
 * 
 */
package ch.globaz.amal.business.models.famille;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.amal.business.models.contribuable.SimpleContribuable;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

/**
 * @author CBU
 * 
 */
public class FamilleContribuable extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private PersonneEtendueComplexModel personneEtendue = null;
    private SimpleContribuable simpleContribuable = null;
    private SimpleDetailFamille simpleDetailFamille = null;
    private SimpleFamille simpleFamille = null;
    private String supplExtra = null;

    public FamilleContribuable() {
        super();
        simpleFamille = new SimpleFamille();
        simpleDetailFamille = new SimpleDetailFamille();
        personneEtendue = new PersonneEtendueComplexModel();
        simpleContribuable = new SimpleContribuable();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleFamille.getId();
    }

    /**
     * @return the personneEtendue
     */
    public PersonneEtendueComplexModel getPersonneEtendue() {
        return personneEtendue;
    }

    public SimpleContribuable getSimpleContribuable() {
        return simpleContribuable;
    }

    public SimpleDetailFamille getSimpleDetailFamille() {
        return simpleDetailFamille;
    }

    /**
     * @return the simpleFamille
     */
    public SimpleFamille getSimpleFamille() {
        return simpleFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleFamille.getSpy();
    }

    /**
     * @return the supplExtra
     */
    public String getSupplExtra() {
        return supplExtra;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleFamille.setId(id);
    }

    /**
     * @param personneEtendue
     *            the personneEtendue to set
     */
    public void setPersonneEtendue(PersonneEtendueComplexModel personneEtendue) {
        this.personneEtendue = personneEtendue;
    }

    public void setSimpleContribuable(SimpleContribuable simpleContribuable) {
        this.simpleContribuable = simpleContribuable;
    }

    public void setSimpleDetailFamille(SimpleDetailFamille simpleDetailFamille) {
        this.simpleDetailFamille = simpleDetailFamille;
    }

    /**
     * @param simpleFamille
     *            the simpleFamille to set
     */
    public void setSimpleFamille(SimpleFamille simpleFamille) {
        this.simpleFamille = simpleFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleFamille.setSpy(spy);
    }

    /**
     * @param supplExtra
     *            the supplExtra to set
     */
    public void setSupplExtra(String supplExtra) {
        this.supplExtra = supplExtra;
    }

}
