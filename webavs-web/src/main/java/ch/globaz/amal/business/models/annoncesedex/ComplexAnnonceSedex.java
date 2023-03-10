/**
 * 
 */
package ch.globaz.amal.business.models.annoncesedex;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.CompositionTiersSimpleModel;

/**
 * @author dhi
 * 
 */
public class ComplexAnnonceSedex extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AdministrationComplexModel administrationComplexModelGroupes = null;
    private AdministrationComplexModel caisseMaladie = null;
    private CompositionTiersSimpleModel compositionTiersSimpleModel = null;
    private Contribuable contribuable = null;
    private SimpleAnnonceSedex simpleAnnonceSedex = null;
    private SimpleDetailFamille simpleDetailFamille = null;
    private SimpleFamille simpleFamille = null;

    /**
     * Default constructor
     */
    public ComplexAnnonceSedex() {
        super();
        simpleAnnonceSedex = new SimpleAnnonceSedex();
        simpleFamille = new SimpleFamille();
        simpleDetailFamille = new SimpleDetailFamille();
        caisseMaladie = new AdministrationComplexModel();
        compositionTiersSimpleModel = new CompositionTiersSimpleModel();
        administrationComplexModelGroupes = new AdministrationComplexModel();
        contribuable = new Contribuable();
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

    /**
     * @return the contribuable
     */
    public Contribuable getContribuable() {
        return contribuable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleAnnonceSedex.getId();
    }

    /**
     * @return the simpleAnnonceSedex
     */
    public SimpleAnnonceSedex getSimpleAnnonceSedex() {
        return simpleAnnonceSedex;
    }

    /**
     * @return the simpleDetailFamille
     */
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
        return simpleAnnonceSedex.getSpy();
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

    /**
     * @param contribuable
     *            the contribuable to set
     */
    public void setContribuable(Contribuable contribuable) {
        this.contribuable = contribuable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleAnnonceSedex.setId(id);
    }

    /**
     * @param simpleAnnonceSedex
     *            the simpleAnnonceSedex to set
     */
    public void setSimpleAnnonceSedex(SimpleAnnonceSedex simpleAnnonceSedex) {
        this.simpleAnnonceSedex = simpleAnnonceSedex;
    }

    /**
     * @param simpleDetailFamille
     *            the simpleDetailFamille to set
     */
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
        simpleAnnonceSedex.setSpy(spy);
    }

}
