package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author Arnaud Geiser (AGE) | Cr�� le 20 f�vr. 2014
 * 
 */
public class HistoriqueDecompteSearchSimpleModel extends JadeSearchSimpleModel {
    private String forId;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    @Override
    public Class<HistoriqueDecompteSimpleModel> whichModelClass() {
        return HistoriqueDecompteSimpleModel.class;
    }

}
