package ch.globaz.pegasus.business.models.revenusdepenses;

import globaz.jade.persistence.model.JadeComplexModel;

public class TypeFraisObtentionRevenu extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleRevenuActiviteLucrativeDependante simpleRevenuActiviteLucrativeDependante = null;
    private SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu = null;

    /**
	 * 
	 */
    public TypeFraisObtentionRevenu() {
        super();
        simpleTypeFraisObtentionRevenu = new SimpleTypeFraisObtentionRevenu();
        simpleRevenuActiviteLucrativeDependante = new SimpleRevenuActiviteLucrativeDependante();
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

    /**
     * @return the simpleRevenuActiviteLucrativeDependante
     */
    public SimpleRevenuActiviteLucrativeDependante getSimpleRevenuActiviteLucrativeDependante() {
        return simpleRevenuActiviteLucrativeDependante;
    }

    public SimpleTypeFraisObtentionRevenu getSimpleTypeFraisObtentionRevenu() {
        return simpleTypeFraisObtentionRevenu;
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleRevenuActiviteLucrativeDependante.setId(id);
        // this.simpleTypeFraisObtentionRevenu.setId(id);
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    public void setSimpleRevenuActiviteLucrativeDependante(
            SimpleRevenuActiviteLucrativeDependante simpleRevenuActiviteLucrativeDependante) {
        this.simpleRevenuActiviteLucrativeDependante = simpleRevenuActiviteLucrativeDependante;
    }

    public void setSimpleTypeFraisObtentionRevenu(SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu) {
        this.simpleTypeFraisObtentionRevenu = simpleTypeFraisObtentionRevenu;
        this.simpleTypeFraisObtentionRevenu.setIdFraisObtentionRevenu(this.simpleTypeFraisObtentionRevenu.getId());
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
