/**
 *
 */
package ch.globaz.vulpecula.business.models.codesystem;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author sel
 * 
 */
public class CodeSystemComplexModel extends JadeComplexModel {
    private CodeSystemSimpleModel simpleCodeSystem = null;
    private CodeSystemLibelleSimpleModel simpleCodeSystemLibelle = null;

    public CodeSystemComplexModel() {
        super();
        simpleCodeSystem = new CodeSystemSimpleModel();
        simpleCodeSystemLibelle = new CodeSystemLibelleSimpleModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleCodeSystem.getIdCodeSystem();
    }

    public CodeSystemSimpleModel getSimpleCodeSystem() {
        return simpleCodeSystem;
    }

    public CodeSystemLibelleSimpleModel getSimpleCodeSystemLibelle() {
        return simpleCodeSystemLibelle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleCodeSystem.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleCodeSystem.setIdCodeSystem(id);
    }

    public void setSimpleCodeSystem(CodeSystemSimpleModel simpleCodeSystem) {
        this.simpleCodeSystem = simpleCodeSystem;
    }

    public void setSimpleCodeSystemLibelle(CodeSystemLibelleSimpleModel simpleCodeSystemLibelle) {
        this.simpleCodeSystemLibelle = simpleCodeSystemLibelle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleCodeSystem.setSpy(spy);
    }

}
