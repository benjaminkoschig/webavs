package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class AffiliationTiersComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AffiliationSimpleModel affiliation = new AffiliationSimpleModel();
    private TiersSimpleModel tiersAffiliation = new TiersSimpleModel();

    public AffiliationSimpleModel getAffiliation() {
        return affiliation;
    }

    @Override
    public String getId() {
        return affiliation.getId();
    }

    @Override
    public String getSpy() {
        return affiliation.getSpy();
    }

    public TiersSimpleModel getTiersAffiliation() {
        return tiersAffiliation;
    }

    public void setAffiliation(AffiliationSimpleModel affiliation) {
        this.affiliation = affiliation;
    }

    @Override
    public void setId(String id) {
        affiliation.setId(id);

    }

    @Override
    public void setSpy(String spy) {
        affiliation.setSpy(spy);

    }

    public void setTiersAffiliation(TiersSimpleModel tiersAffiliation) {
        this.tiersAffiliation = tiersAffiliation;
    }

}
