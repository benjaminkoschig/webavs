package ch.globaz.vulpecula.business.models.controleemployeur;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class ControleEmployeurSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = -7170477093707364745L;

    private String forId;
    private String forIdEmployeur;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdEmployeur() {
        return forIdEmployeur;
    }

    public void setForIdEmployeur(String forIdEmployeur) {
        this.forIdEmployeur = forIdEmployeur;
    }

    @Override
    public Class<ControleEmployeurSimpleModel> whichModelClass() {
        return ControleEmployeurSimpleModel.class;
    }
}
