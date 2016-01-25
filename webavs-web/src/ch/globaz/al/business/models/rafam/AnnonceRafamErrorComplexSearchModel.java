package ch.globaz.al.business.models.rafam;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author jts
 * 
 */
public class AnnonceRafamErrorComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateReception = null;
    private String forEtat = null;
    private String forIdAnnonce = null;
    private Boolean forInsignificance = null;

    public String getForDateReception() {
        return forDateReception;
    }

    public String getForEtat() {
        return forEtat;
    }

    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    public Boolean getForInsignificance() {
        return forInsignificance;
    }

    public void setForDateReception(String forDateReception) {
        this.forDateReception = forDateReception;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public void setForIdAnnonce(String forIdAnnonce) {
        this.forIdAnnonce = forIdAnnonce;
    }

    public void setForInsignificance(Boolean forInsignificance) {
        this.forInsignificance = forInsignificance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<AnnonceRafamErrorComplexModel> whichModelClass() {
        return AnnonceRafamErrorComplexModel.class;
    }
}
