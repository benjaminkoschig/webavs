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
    private SimpleAnnonceSedexCOPersonne simpleAnnonceSedexCOPersonne;
    private AdministrationComplexModel caisseMaladie;

    /**
     * Default constructor
     * Initialize class objects
     */
    public ComplexAnnonceSedexCO2() {
        super();
        simpleAnnonceSedexCO = new SimpleAnnonceSedexCO();
        caisseMaladie = new AdministrationComplexModel();
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
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleAnnonceSedexCO.getSpy();
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
     * @return the simpleAnnonceSedexCOPersonne
     */
    public SimpleAnnonceSedexCOPersonne getSimpleAnnonceSedexCOPersonne() {
        return simpleAnnonceSedexCOPersonne;
    }

    /**
     * @param simpleAnnonceSedexCOPersonne the simpleAnnonceSedexCOPersonne to set
     */
    public void setSimpleAnnonceSedexCOPersonne(SimpleAnnonceSedexCOPersonne simpleAnnonceSedexCOPersonne) {
        this.simpleAnnonceSedexCOPersonne = simpleAnnonceSedexCOPersonne;
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
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleAnnonceSedexCO.setSpy(spy);
    }
}
