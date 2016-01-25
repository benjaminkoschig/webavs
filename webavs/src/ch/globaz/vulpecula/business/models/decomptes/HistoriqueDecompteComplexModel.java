package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeComplexModel;

public class HistoriqueDecompteComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = 4811960871607345438L;

    private HistoriqueDecompteSimpleModel historiqueDecompteSimpleModel;
    private DecompteComplexModel decompteComplexModel;

    public HistoriqueDecompteComplexModel() {
        historiqueDecompteSimpleModel = new HistoriqueDecompteSimpleModel();
        decompteComplexModel = new DecompteComplexModel();
    }

    public HistoriqueDecompteSimpleModel getHistoriqueDecompteSimpleModel() {
        return historiqueDecompteSimpleModel;
    }

    public void setHistoriqueDecompteSimpleModel(HistoriqueDecompteSimpleModel historiqueDecompteSimpleModel) {
        this.historiqueDecompteSimpleModel = historiqueDecompteSimpleModel;
    }

    public DecompteComplexModel getDecompteComplexModel() {
        return decompteComplexModel;
    }

    public void setDecompteComplexModel(DecompteComplexModel decompteComplexModel) {
        this.decompteComplexModel = decompteComplexModel;
    }

    @Override
    public String getId() {
        return historiqueDecompteSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return historiqueDecompteSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        historiqueDecompteSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        historiqueDecompteSimpleModel.setSpy(spy);
    }
}
