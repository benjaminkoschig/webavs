package ch.globaz.perseus.business.models.variablemetier;

import globaz.jade.persistence.model.JadeComplexModel;

public class VariableMetier extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleVariableMetier simpleVariableMetier = null;

    public VariableMetier() {
        super();
        simpleVariableMetier = new SimpleVariableMetier();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleVariableMetier.getId();
    }

    public Float getMontant() {
        return Float.valueOf(simpleVariableMetier.getMontant());
    }

    /**
     * @return the simpleVariableMetier
     */
    public SimpleVariableMetier getSimpleVariableMetier() {
        return simpleVariableMetier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleVariableMetier.getSpy();
    }

    public Float getTaux() {
        return Float.valueOf(simpleVariableMetier.getTaux());
    }

    @Override
    public void setId(String id) {
        simpleVariableMetier.setId(id);

    }

    /**
     * @param simpleVariableMetier
     *            the simpleVariableMetier to set
     */
    public void setSimpleVariableMetier(SimpleVariableMetier simpleVariableMetier) {
        this.simpleVariableMetier = simpleVariableMetier;
    }

    @Override
    public void setSpy(String spy) {
        setSpy(spy);

    }

}
