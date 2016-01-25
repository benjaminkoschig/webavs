package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class AnnonceLapramsSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDebutRapport = null;
    private String forDateFinRapport = null;
    private String forIdAnnonceLaprams = null;
    private List<String> inIdsDecision = null;

    public String getForDateDebutRapport() {
        return forDateDebutRapport;
    }

    public String getForDateFinRapport() {
        return forDateFinRapport;
    }

    public String getForIdAnnonceLaprams() {
        return forIdAnnonceLaprams;
    }

    public List<String> getInIdsDecision() {
        return inIdsDecision;
    }

    public void setForDateDebutRapport(String forDateDebutRapport) {
        this.forDateDebutRapport = forDateDebutRapport;
    }

    public void setForDateFinRapport(String forDateFinRapport) {
        this.forDateFinRapport = forDateFinRapport;
    }

    public void setForIdAnnonceLaprams(String forIdAnnonceLaprams) {
        this.forIdAnnonceLaprams = forIdAnnonceLaprams;
    }

    public void setInIdsDecision(List<String> inIdsDecision) {
        this.inIdsDecision = inIdsDecision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<AnnonceLaprams> whichModelClass() {
        return AnnonceLaprams.class;
    }

}
