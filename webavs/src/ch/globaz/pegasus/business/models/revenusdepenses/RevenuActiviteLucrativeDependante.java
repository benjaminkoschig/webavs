package ch.globaz.pegasus.business.models.revenusdepenses;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class RevenuActiviteLucrativeDependante extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private TiersSimpleModel employeur = null;
    private List<SimpleTypeFraisObtentionRevenu> listTypeDeFrais = null;
    private AffiliationSimpleModel simpleAffiliation = null;
    private SimpleRevenuActiviteLucrativeDependante simpleRevenuActiviteLucrativeDependante = null;

    /**
	 * 
	 */
    public RevenuActiviteLucrativeDependante() {
        super();
        employeur = new TiersSimpleModel();
        simpleRevenuActiviteLucrativeDependante = new SimpleRevenuActiviteLucrativeDependante();
        simpleAffiliation = new AffiliationSimpleModel();
        listTypeDeFrais = new ArrayList<SimpleTypeFraisObtentionRevenu>();
    }

    public TiersSimpleModel getEmployeur() {
        return employeur;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleRevenuActiviteLucrativeDependante.getId();
    }

    public List<SimpleTypeFraisObtentionRevenu> getListTypeDeFrais() {
        return listTypeDeFrais;
    }

    /**
     * @return the simpleAffiliation
     */
    public AffiliationSimpleModel getSimpleAffiliation() {
        return simpleAffiliation;
    }

    /**
     * @return the simpleRevenuActiviteLucrativeDependante
     */
    public SimpleRevenuActiviteLucrativeDependante getSimpleRevenuActiviteLucrativeDependante() {
        return simpleRevenuActiviteLucrativeDependante;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleRevenuActiviteLucrativeDependante.getSpy();
    }

    public void setEmployeur(TiersSimpleModel employeur) {
        this.employeur = employeur;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleRevenuActiviteLucrativeDependante.setId(id);
    }

    @Override
    public void setIsNew() {
        super.setIsNew();
        simpleRevenuActiviteLucrativeDependante.setId(null);
        simpleRevenuActiviteLucrativeDependante.setSpy(null);
    }

    public void setListTypeDeFrais(List<SimpleTypeFraisObtentionRevenu> listTypeDeFrais) {
        this.listTypeDeFrais = listTypeDeFrais;
    }

    /**
     * @param simpleAffiliation
     *            the simpleAffiliation to set
     */
    public void setSimpleAffiliation(AffiliationSimpleModel simpleAffiliation) {
        this.simpleAffiliation = simpleAffiliation;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleRevenuActiviteLucrativeDependante.setIdDonneeFinanciereHeader(this.simpleDonneeFinanciereHeader.getId());
    }

    /**
     * @param simpleRevenuActiviteLucrativeDependante
     *            the simpleRevenuActiviteLucrativeDependante to set
     */
    public void setSimpleRevenuActiviteLucrativeDependante(
            SimpleRevenuActiviteLucrativeDependante simpleRevenuActiviteLucrativeDependante) {
        this.simpleRevenuActiviteLucrativeDependante = simpleRevenuActiviteLucrativeDependante;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleRevenuActiviteLucrativeDependante.setSpy(spy);
    }

}
