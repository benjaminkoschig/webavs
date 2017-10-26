package ch.globaz.pegasus.rpc.business.models;

import ch.globaz.common.persistence.DomaineJadeAbstractSearchModel;

public class LotDossierDecisionSearch extends DomaineJadeAbstractSearchModel {
    private String forIdDecision;
    private String forIdDossier;
    private String forMonth;

    @Override
    public Class<LotDossierDecision> whichModelClass() {
        return LotDossierDecision.class;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForMonth() {
        return forMonth;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForMonth(String forMonth) {
        this.forMonth = forMonth;
    }

}
