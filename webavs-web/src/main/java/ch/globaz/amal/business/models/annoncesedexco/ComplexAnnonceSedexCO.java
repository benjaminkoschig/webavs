/**
 * 
 */
package ch.globaz.amal.business.models.annoncesedexco;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.CompositionTiersSimpleModel;

/**
 * @author cbu
 * 
 */
public class ComplexAnnonceSedexCO extends JadeComplexModel {

    private static final long serialVersionUID = 1L;
    private AdministrationComplexModel administrationComplexModelGroupes = null;
    private AdministrationComplexModel caisseMaladie = null;
    private CompositionTiersSimpleModel compositionTiersSimpleModel = null;
    private SimpleAnnonceSedexCO simpleAnnonceSedexCO = null;

    // private SimpleAnnonceSedexCOPersonne simpleAnnonceSedexCOPersonne = null;
    // private SimpleContribuable simpleContribuable = null;
    // private SimpleFamille simpleFamille = null;

    /**
     * Default constructor
     */
    public ComplexAnnonceSedexCO() {
        super();
        simpleAnnonceSedexCO = new SimpleAnnonceSedexCO();
        // simpleAnnonceSedexCOPersonne = new SimpleAnnonceSedexCOPersonne();
        // simpleFamille = new SimpleFamille();
        caisseMaladie = new AdministrationComplexModel();
        compositionTiersSimpleModel = new CompositionTiersSimpleModel();
        administrationComplexModelGroupes = new AdministrationComplexModel();
    }

    /**
     * @return the administrationComplexModelGroupes
     */
    public AdministrationComplexModel getAdministrationComplexModelGroupes() {
        return administrationComplexModelGroupes;
    }

    /**
     * @return the caisseMaladie
     */
    public AdministrationComplexModel getCaisseMaladie() {
        return caisseMaladie;
    }

    /**
     * @return the compositionTiersSimpleModel
     */
    public CompositionTiersSimpleModel getCompositionTiersSimpleModel() {
        return compositionTiersSimpleModel;
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

    /**
     * @return the simpleAnnonceSedex
     */
    public SimpleAnnonceSedexCO getSimpleAnnonceSedexCO() {
        return simpleAnnonceSedexCO;
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
     * @param administrationComplexModelGroupes
     *            the administrationComplexModelGroupes to set
     */
    public void setAdministrationComplexModelGroupes(AdministrationComplexModel administrationComplexModelGroupes) {
        this.administrationComplexModelGroupes = administrationComplexModelGroupes;
    }

    /**
     * @param caisseMaladie
     *            the caisseMaladie to set
     */
    public void setCaisseMaladie(AdministrationComplexModel caisseMaladie) {
        this.caisseMaladie = caisseMaladie;
    }

    /**
     * @param compositionTiersSimpleModel
     *            the compositionTiersSimpleModel to set
     */
    public void setCompositionTiersSimpleModel(CompositionTiersSimpleModel compositionTiersSimpleModel) {
        this.compositionTiersSimpleModel = compositionTiersSimpleModel;
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

    /**
     * @param simpleAnnonceSedex
     *            the simpleAnnonceSedex to set
     */
    public void setSimpleAnnonceSedexCO(SimpleAnnonceSedexCO simpleAnnonceSedexCO) {
        this.simpleAnnonceSedexCO = simpleAnnonceSedexCO;
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
