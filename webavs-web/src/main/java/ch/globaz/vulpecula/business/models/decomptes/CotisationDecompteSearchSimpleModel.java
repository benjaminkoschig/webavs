package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author Arnaud Geiser (AGE) | Créé le 20 févr. 2014
 * 
 */
public class CotisationDecompteSearchSimpleModel extends JadeSearchSimpleModel {
    private String forId;
    private String forIdLigneDecompte;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdLigneDecompte() {
        return forIdLigneDecompte;
    }

    public void setForIdLigneDecompte(String forIdLigneDecompte) {
        this.forIdLigneDecompte = forIdLigneDecompte;
    }

    @Override
    public Class<CotisationDecompteSimpleModel> whichModelClass() {
        return CotisationDecompteSimpleModel.class;
    }

}
