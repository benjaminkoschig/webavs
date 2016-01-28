package ch.globaz.pegasus.business.models.dettecomptatcompense;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class DetteCompenseCompteAnnexeSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDroit = null;
    private String forIdVersionDroit = null;

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    @Override
    public Class<DetteCompenseCompteAnnexe> whichModelClass() {
        return DetteCompenseCompteAnnexe.class;
    }
}
