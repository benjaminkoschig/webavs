package ch.globaz.vulpecula.business.models.controleemployeur;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class ControleEmployeurSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = 6881059627747699385L;

    public static final String ORDER_BY_DATE_AU_DESC = "dateAuDesc";

    private String forId;
    private String forIdEmployeur;
    private String forControleLess;
    private String forDateDebutGOE;
    private String forDateDebutLOE;

    public String getForControleLess() {
        return forControleLess;
    }

    public void setForControleLess(String forControleLess) {
        this.forControleLess = forControleLess;
    }

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

    public String getForDateDebutGOE() {
        return forDateDebutGOE;
    }

    public void setForDateDebutGOE(String forDateDebutGOE) {
        this.forDateDebutGOE = forDateDebutGOE;
    }

    public void setForDateDebutLOE(String forDateDebutLOE) {
        this.forDateDebutLOE = forDateDebutLOE;
    }

    public String getForDateDebutLOE() {
        return forDateDebutLOE;
    }

    @Override
    public Class<ControleEmployeurComplexModel> whichModelClass() {
        return ControleEmployeurComplexModel.class;
    }

}
