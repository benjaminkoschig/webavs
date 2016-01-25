package ch.globaz.vulpecula.business.models.taxationoffice;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.vulpecula.external.models.CotisationComplexModel;

public class LigneTaxationComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = 1L;

    private LigneTaxationSimpleModel ligneTaxationSimpleModel;
    private TaxationOfficeComplexModel taxationOfficeComplexModel;
    private CotisationComplexModel cotisationComplexModel;

    public LigneTaxationComplexModel() {
        ligneTaxationSimpleModel = new LigneTaxationSimpleModel();
        taxationOfficeComplexModel = new TaxationOfficeComplexModel();
        cotisationComplexModel = new CotisationComplexModel();
    }

    public LigneTaxationSimpleModel getLigneTaxationSimpleModel() {
        return ligneTaxationSimpleModel;
    }

    public void setLigneTaxationSimpleModel(LigneTaxationSimpleModel ligneTaxationSimpleModel) {
        this.ligneTaxationSimpleModel = ligneTaxationSimpleModel;
    }

    public TaxationOfficeComplexModel getTaxationOfficeComplexModel() {
        return taxationOfficeComplexModel;
    }

    public void setTaxationOfficeComplexModel(TaxationOfficeComplexModel taxationOfficeComplexModel) {
        this.taxationOfficeComplexModel = taxationOfficeComplexModel;
    }

    public CotisationComplexModel getCotisationComplexModel() {
        return cotisationComplexModel;
    }

    public void setCotisationComplexModel(CotisationComplexModel cotisationComplexModel) {
        this.cotisationComplexModel = cotisationComplexModel;
    }

    @Override
    public String getId() {
        return ligneTaxationSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return ligneTaxationSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        ligneTaxationSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        ligneTaxationSimpleModel.setSpy(spy);
    }

}
