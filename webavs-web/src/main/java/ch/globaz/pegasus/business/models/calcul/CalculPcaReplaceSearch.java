package ch.globaz.pegasus.business.models.calcul;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class CalculPcaReplaceSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ORDER_BY_DATE_DEBUT = "byDateDebut";
    public static final String WITH_NO_VERSION_DROIT_LESS = "withNoVersionDroitLess";

    private String forDateFin;
    private String forIdDemande;
    private String forIdDroit;
    private String forNoVersion;

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForIdDemande() {
        return forIdDemande;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForNoVersion() {
        return forNoVersion;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForNoVersion(String forNoVersion) {
        this.forNoVersion = forNoVersion;
    }

    @Override
    public Class<CalculPcaReplace> whichModelClass() {
        return CalculPcaReplace.class;
    }

}
