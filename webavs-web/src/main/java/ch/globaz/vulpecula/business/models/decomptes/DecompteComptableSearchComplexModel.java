package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import ch.globaz.vulpecula.domain.models.common.AnneeComptable;

public class DecompteComptableSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = -1898188553816660272L;

    private String forId;
    private String forIdEmployeur;
    private String forEtat;
    private String forDatePassageGreaterOfEquals;
    private String forDatePassageLessOrEquals;

    @Override
    public Class<DecompteComptableComplexModel> whichModelClass() {
        return DecompteComptableComplexModel.class;
    }

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public void setForAnneeComptable(AnneeComptable anneeComptable) {
        setForDatePassageGreaterOfEquals(anneeComptable.getDateDebutAsSwissValue());
        setForDatePassageLessOrEquals(anneeComptable.getDateFinAsSwissValue());
    }

    public String getForIdEmployeur() {
        return forIdEmployeur;
    }

    public void setForIdEmployeur(String forIdEmployeur) {
        this.forIdEmployeur = forIdEmployeur;
    }

    public String getForEtat() {
        return forEtat;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public String getForDatePassageGreaterOfEquals() {
        return forDatePassageGreaterOfEquals;
    }

    public void setForDatePassageGreaterOfEquals(String forDatePassageGreaterOfEquals) {
        this.forDatePassageGreaterOfEquals = forDatePassageGreaterOfEquals;
    }

    public String getForDatePassageLessOrEquals() {
        return forDatePassageLessOrEquals;
    }

    public void setForDatePassageLessOrEquals(String forDatePassageLessOrEquals) {
        this.forDatePassageLessOrEquals = forDatePassageLessOrEquals;
    }
}
