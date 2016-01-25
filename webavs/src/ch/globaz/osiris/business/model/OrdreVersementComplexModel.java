package ch.globaz.osiris.business.model;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author SCO
 */
public class OrdreVersementComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private OperationSimpleModel operation = new OperationSimpleModel();
    private OrdreSimpleModel ordre = new OrdreSimpleModel();

    /**
     * Constructeur de OrdreVersementComplexModel
     */
    public OrdreVersementComplexModel() {
        super();
        operation.setIdTypeOperation("V");
    }

    @Override
    public String getId() {
        return operation.getIdOperation();
    }

    public OperationSimpleModel getOperation() {
        return operation;
    }

    public OrdreSimpleModel getOrdre() {
        return ordre;
    }

    @Override
    public String getSpy() {
        return operation.getSpy();
    }

    @Override
    public void setId(String id) {
        operation.setId(id);
        ordre.setId(id);
    }

    public void setOperation(OperationSimpleModel operation) {
        this.operation = operation;
    }

    public void setOrdre(OrdreSimpleModel ordre) {
        this.ordre = ordre;
    }

    @Override
    public void setSpy(String spy) {
        operation.setSpy(spy);
        ordre.setSpy(spy);
    }

}
