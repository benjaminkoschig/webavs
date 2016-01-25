package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

/**
 * @author DMA
 * @date 22 sept. 2010
 */
public class AffiliationComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AffiliationSimpleModel affiliationSimpleModel = null;
    private TiersSimpleModel tiersSimpleModel = null;

    public AffiliationComplexModel() {
        super();
        affiliationSimpleModel = new AffiliationSimpleModel();
        tiersSimpleModel = new TiersSimpleModel();
    }

    /**
     * @return the affiliationSimpleModel
     */
    public AffiliationSimpleModel getAffiliationSimpleModel() {
        return affiliationSimpleModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return affiliationSimpleModel.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return affiliationSimpleModel.getSpy();
    }

    /**
     * @return the tiersSimpleModel
     */
    public TiersSimpleModel getTiersSimpleModel() {
        return tiersSimpleModel;
    }

    /**
     * @param affiliationSimpleModel
     *            the affiliationSimpleModel to set
     */
    public void setAffiliationSimpleModel(AffiliationSimpleModel affiliationSimpleModel) {
        this.affiliationSimpleModel = affiliationSimpleModel;
    }

    @Override
    public void setId(String id) {
        affiliationSimpleModel.setId(id);

    }

    @Override
    public void setSpy(String spy) {
        affiliationSimpleModel.setSpy(spy);

    }

    /**
     * @param tiersSimpleModel
     *            the tiersSimpleModel to set
     */
    public void setTiersSimpleModel(TiersSimpleModel tiersSimpleModel) {
        this.tiersSimpleModel = tiersSimpleModel;
    }

}
