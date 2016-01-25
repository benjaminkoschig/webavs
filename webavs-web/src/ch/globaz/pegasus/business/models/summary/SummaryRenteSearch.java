package ch.globaz.pegasus.business.models.summary;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class SummaryRenteSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateValable = null;
    private String forIdDroit = null;
    private String forIdDroitMembreFamille = null;
    private String forIdTiers = null;
    private String forNumeroVersion = null;

    private List<String> inCsTypeDonneeFinancierer = null;

    public String getForDateValable() {
        return forDateValable;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForIdDroitMembreFamille() {
        return forIdDroitMembreFamille;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForNumeroVersion() {
        return forNumeroVersion;
    }

    public List<String> getInCsTypeDonneeFinancierer() {
        return inCsTypeDonneeFinancierer;
    }

    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForIdDroitMembreFamille(String forIdDroitMembreFamille) {
        this.forIdDroitMembreFamille = forIdDroitMembreFamille;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForNumeroVersion(String forNumeroVersion) {
        this.forNumeroVersion = forNumeroVersion;
    }

    public void setInCsTypeDonneeFinancierer(List<String> inCsTypeDonneeFinancierer) {
        this.inCsTypeDonneeFinancierer = inCsTypeDonneeFinancierer;
    }

    @Override
    public Class<SummaryRente> whichModelClass() {
        return SummaryRente.class;
    }

}
