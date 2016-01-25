package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.vulpecula.external.models.CotisationComplexModel;

/**
 * @author Arnaud Geiser (AGE) | Créé le 20 févr. 2014
 * 
 */
public class CotisationDecompteComplexModel extends JadeComplexModel {
    private CotisationDecompteSimpleModel cotisationDecompteSimpleModel;
    private CotisationComplexModel cotisationComplexModel;

    public CotisationDecompteComplexModel() {
        cotisationDecompteSimpleModel = new CotisationDecompteSimpleModel();
        cotisationComplexModel = new CotisationComplexModel();
    }

    public CotisationDecompteSimpleModel getCotisationDecompteSimpleModel() {
        return cotisationDecompteSimpleModel;
    }

    public void setCotisationDecompteSimpleModel(CotisationDecompteSimpleModel cotisationDecompteSimpleModel) {
        this.cotisationDecompteSimpleModel = cotisationDecompteSimpleModel;
    }

    public CotisationComplexModel getCotisationComplexModel() {
        return cotisationComplexModel;
    }

    public void setCotisationComplexModel(CotisationComplexModel cotisationComplexModel) {
        this.cotisationComplexModel = cotisationComplexModel;
    }

    @Override
    public String getId() {
        return cotisationDecompteSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return cotisationDecompteSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        cotisationDecompteSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        cotisationDecompteSimpleModel.setSpy(spy);
    }
}
