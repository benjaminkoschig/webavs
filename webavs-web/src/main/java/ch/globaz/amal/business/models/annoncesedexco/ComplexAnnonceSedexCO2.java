/**
 * 
 */
package ch.globaz.amal.business.models.annoncesedexco;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;

/**
 * @author LBE
 * 
 */
public class ComplexAnnonceSedexCO2 extends JadeComplexModel {

    private static final long serialVersionUID = 1L;
    private SimpleAnnonceSedexCO simpleAnnonceSedexCO;
    private AdministrationComplexModel caisseMaladie;
    private SimpleAnnonceSedexCOPersonne simplePersonne;

    /**
     * Default constructor
     * Initialize class objects
     */
    public ComplexAnnonceSedexCO2() {
        super();
        simpleAnnonceSedexCO = new SimpleAnnonceSedexCO();
        caisseMaladie = new AdministrationComplexModel();
        simplePersonne = new SimpleAnnonceSedexCOPersonne();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleAnnonceSedexCO.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleAnnonceSedexCO.setId(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleAnnonceSedexCO.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleAnnonceSedexCO.setSpy(spy);
    }

    /**
     * @return the simpleAnnonceSedexCO
     */
    public SimpleAnnonceSedexCO getSimpleAnnonceSedexCO() {
        return simpleAnnonceSedexCO;
    }

    /**
     * @param simpleAnnonceSedexCO the simpleAnnonceSedexCO to set
     */
    public void setSimpleAnnonceSedexCO(SimpleAnnonceSedexCO simpleAnnonceSedexCO) {
        this.simpleAnnonceSedexCO = simpleAnnonceSedexCO;
    }

    /**
     * @return the caisseMaladie
     */
    public AdministrationComplexModel getCaisseMaladie() {
        return caisseMaladie;
    }

    /**
     * @param caisseMaladie the caisseMaladie to set
     */
    public void setCaisseMaladie(AdministrationComplexModel caisseMaladie) {
        this.caisseMaladie = caisseMaladie;
    }

    /**
     * @return the simplePersonne
     */
    public SimpleAnnonceSedexCOPersonne getSimplePersonne() {
        return simplePersonne;
    }

    /**
     * @param simplePersonne the simplePersonne to set
     */
    public void setSimplePersonne(SimpleAnnonceSedexCOPersonne simplePersonne) {
        this.simplePersonne = simplePersonne;
    }

}
