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
public class ComplexAnnonceSedexCO4 extends JadeComplexModel {

    private static final long serialVersionUID = 1L;
    private SimpleAnnonceSedexCO simpleAnnonceSedexCO;
    private AdministrationComplexModel caisseMaladie;
    private SimpleAnnonceSedexCODebiteur simpleAnnonceSedexCODebiteur;
    private SimpleAnnonceSedexCOAssure simpleAnnonceSedexCOAssure;

    public ComplexAnnonceSedexCO4() {
        super();
        simpleAnnonceSedexCO = new SimpleAnnonceSedexCO();
        caisseMaladie = new AdministrationComplexModel();
        simpleAnnonceSedexCODebiteur = new SimpleAnnonceSedexCODebiteur();
        simpleAnnonceSedexCOAssure = new SimpleAnnonceSedexCOAssure();
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
     * @return the simpleAnnonceSedexCODebiteur
     */
    public SimpleAnnonceSedexCODebiteur getSimpleAnnonceSedexCODebiteur() {
        return simpleAnnonceSedexCODebiteur;
    }

    /**
     * @param simpleAnnonceSedexCODebiteur the simpleAnnonceSedexCODebiteur to set
     */
    public void setSimpleAnnonceSedexCODebiteur(SimpleAnnonceSedexCODebiteur simpleAnnonceSedexCODebiteur) {
        this.simpleAnnonceSedexCODebiteur = simpleAnnonceSedexCODebiteur;
    }

    /**
     * @return the simpleAnnonceSedexCOAssure
     */
    public SimpleAnnonceSedexCOAssure getSimpleAnnonceSedexCOAssure() {
        return simpleAnnonceSedexCOAssure;
    }

    /**
     * @param simpleAnnonceSedexCOAssure the simpleAnnonceSedexCOAssure to set
     */
    public void setSimpleAnnonceSedexCOAssure(SimpleAnnonceSedexCOAssure simpleAnnonceSedexCOAssure) {
        this.simpleAnnonceSedexCOAssure = simpleAnnonceSedexCOAssure;
    }
}
