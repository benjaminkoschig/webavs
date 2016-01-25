package ch.globaz.hera.business.models.famille;

import globaz.jade.persistence.model.JadeComplexModel;

public class RelationConjoint extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleConjoint simpleConjoint = null;
    private SimpleRelationConjoint simpleRelationConjoint = null;

    public RelationConjoint() {
        super();
        simpleRelationConjoint = new SimpleRelationConjoint();
        simpleConjoint = new SimpleConjoint();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleRelationConjoint.getId();
    }

    /**
     * getter pour l'attribut simpleConjoint
     * 
     * @return
     */
    public SimpleConjoint getSimpleConjoint() {
        return simpleConjoint;
    }

    /**
     * getter pour l'attribut simpleRelationConjoint
     * 
     * @return
     */
    public SimpleRelationConjoint getSimpleRelationConjoint() {
        return simpleRelationConjoint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleRelationConjoint.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleRelationConjoint.setId(id);
    }

    /**
     * setter pour l'attribut simpleConjoint
     * 
     * @param simpleConjoint
     */
    public void setSimpleConjoint(SimpleConjoint simpleConjoint) {
        this.simpleConjoint = simpleConjoint;
    }

    /**
     * setter pour l'attribut simpleRelationConjoint
     * 
     * @param simpleRelationConjoint
     */
    public void setSimpleRelationConjoint(SimpleRelationConjoint simpleRelationConjoint) {
        this.simpleRelationConjoint = simpleRelationConjoint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleRelationConjoint.setSpy(spy);
    }
}
