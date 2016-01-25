package ch.globaz.vulpecula.business.models.taxationoffice;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteComplexModel;

public class TaxationOfficeComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = 1L;

    private DecompteComplexModel decompteComplexModel;
    private TaxationOfficeSimpleModel taxationOfficeSimpleModel;

    public TaxationOfficeComplexModel() {
        decompteComplexModel = new DecompteComplexModel();
        taxationOfficeSimpleModel = new TaxationOfficeSimpleModel();
    }

    public DecompteComplexModel getDecompteComplexModel() {
        return decompteComplexModel;
    }

    public void setDecompteComplexModel(DecompteComplexModel decompteComplexModel) {
        this.decompteComplexModel = decompteComplexModel;
    }

    public TaxationOfficeSimpleModel getTaxationOfficeSimpleModel() {
        return taxationOfficeSimpleModel;
    }

    public void setTaxationOfficeSimpleModel(TaxationOfficeSimpleModel taxationOfficeSimpleModel) {
        this.taxationOfficeSimpleModel = taxationOfficeSimpleModel;
    }

    @Override
    public String getId() {
        return taxationOfficeSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return taxationOfficeSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        taxationOfficeSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        taxationOfficeSimpleModel.setSpy(spy);
    }

}
