package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurComplexModel;
import ch.globaz.vulpecula.business.models.taxationoffice.TaxationOfficeSimpleModel;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;

public class DecompteComplexModelAJAX extends JadeComplexModel {
    private static final long serialVersionUID = -1025546784075067435L;

    private String etatDecompte = EtatDecompte.TAXATION_DOFFICE.TAXATION_DOFFICE.getValue();

    private DecompteSimpleModel decompteSimpleModel;
    private EmployeurComplexModel employeurComplexModel;
    private TaxationOfficeSimpleModel taxationOfficeSimpleModel;

    public DecompteComplexModelAJAX() {
        decompteSimpleModel = new DecompteSimpleModel();
        employeurComplexModel = new EmployeurComplexModel();
        taxationOfficeSimpleModel = new TaxationOfficeSimpleModel();
    }

    public DecompteSimpleModel getDecompteSimpleModel() {
        return decompteSimpleModel;
    }

    public void setDecompteSimpleModel(DecompteSimpleModel decompteSimpleModel) {
        this.decompteSimpleModel = decompteSimpleModel;
    }

    public EmployeurComplexModel getEmployeurComplexModel() {
        return employeurComplexModel;
    }

    public void setEmployeurComplexModel(EmployeurComplexModel employeurComplexModel) {
        this.employeurComplexModel = employeurComplexModel;
    }

    public TaxationOfficeSimpleModel getTaxationOfficeSimpleModel() {
        return taxationOfficeSimpleModel;
    }

    public void setTaxationOfficeSimpleModel(TaxationOfficeSimpleModel taxationOfficeSimpleModel) {
        this.taxationOfficeSimpleModel = taxationOfficeSimpleModel;
    }

    @Override
    public String getId() {
        return decompteSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return decompteSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        decompteSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        decompteSimpleModel.setSpy(spy);
    }

    public String getEtatDecompte() {
        return etatDecompte;
    }
}
