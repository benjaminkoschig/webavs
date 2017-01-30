package ch.globaz.amal.business.models.simplepersonneanepaspoursuivre;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimplePersonneANePasPoursuivreSearch extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 463601327635170370L;

    private String forNSS = null;
    private String forAnnee = null;
    private String forIdTiersCM = null;

    @Override
    public Class whichModelClass() {
        return SimplePersonneANePasPoursuivre.class;
    }

    public String getForNSS() {
        return forNSS;
    }

    public void setForNSS(String forNSS) {
        this.forNSS = forNSS;
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public String getForIdTiersCM() {
        return forIdTiersCM;
    }

    public void setForIdTiersCM(String forIdTiersCM) {
        this.forIdTiersCM = forIdTiersCM;
    }

}
