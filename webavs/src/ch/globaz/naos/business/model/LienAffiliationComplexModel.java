package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeComplexModel;

public class LienAffiliationComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AffiliationSimpleModel child = new AffiliationSimpleModel();
    private LienAffiliationSimpleModel lien = new LienAffiliationSimpleModel();
    private AffiliationSimpleModel parent = new AffiliationSimpleModel();

    public AffiliationSimpleModel getChild() {
        return child;
    }

    @Override
    public String getId() {
        return lien.getLienAffiliationId();
    }

    public LienAffiliationSimpleModel getLien() {
        return lien;
    }

    public AffiliationSimpleModel getParent() {
        return parent;
    }

    @Override
    public String getSpy() {
        return lien.getSpy();
    }

    public void setChild(AffiliationSimpleModel child) {
        this.child = child;
    }

    @Override
    public void setId(String id) {
        lien.setLienAffiliationId(id);
    }

    public void setLien(LienAffiliationSimpleModel lien) {
        this.lien = lien;
    }

    public void setParent(AffiliationSimpleModel parent) {
        this.parent = parent;
    }

    @Override
    public void setSpy(String spy) {
        lien.setSpy(spy);
    }

}
