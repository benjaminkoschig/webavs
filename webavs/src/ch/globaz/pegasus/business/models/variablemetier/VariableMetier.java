package ch.globaz.pegasus.business.models.variablemetier;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.jade.business.models.codesysteme.ComplexCodeSysteme;

public class VariableMetier extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleVariableMetier simpleVariableMetier = null;
    private ComplexCodeSysteme complexCodeSysteme = null;

    public VariableMetier() {
        super();
        simpleVariableMetier = new SimpleVariableMetier();
        complexCodeSysteme = new ComplexCodeSysteme();
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
        simpleVariableMetier.setSpy(spy);

    }

    public ComplexCodeSysteme getComplexCodeSysteme() {
        return complexCodeSysteme;
    }

    public void setComplexCodeSysteme(ComplexCodeSysteme complexCodeSysteme) {
        this.complexCodeSysteme = complexCodeSysteme;
    }

}
