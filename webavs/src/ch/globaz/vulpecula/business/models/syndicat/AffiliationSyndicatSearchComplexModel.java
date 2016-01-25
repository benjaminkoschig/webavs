package ch.globaz.vulpecula.business.models.syndicat;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import ch.globaz.vulpecula.domain.models.common.Date;

public class AffiliationSyndicatSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = -5373858117105257667L;

    private String forId;
    private String forIdTravailleur;
    private String forIdSyndicat;
    private String forDateDebutBeforeOrEquals;
    private String forDateDebutAfterOrEquals;
    private String forDateFinAfterOrEquals;
    private String forDateFinIsNull;

    public static final String ORDER_DATE_DEBUT_DESC = "dateDebutDesc";

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdTravailleur() {
        return forIdTravailleur;
    }

    public void setForIdTravailleur(String forIdTravailleur) {
        this.forIdTravailleur = forIdTravailleur;
    }

    public String getForIdSyndicat() {
        return forIdSyndicat;
    }

    public void setForIdSyndicat(String forIdSyndicat) {
        this.forIdSyndicat = forIdSyndicat;
    }

    public String getForDateDebutBeforeOrEquals() {
        return forDateDebutBeforeOrEquals;
    }

    public void setForDateDebutBeforeOrEquals(String forDateDebutBeforeOrEquals) {
        this.forDateDebutBeforeOrEquals = forDateDebutBeforeOrEquals;
    }

    public String getForDateDebutAfterOrEquals() {
        return forDateDebutAfterOrEquals;
    }

    public void setForDateDebutAfterOrEquals(String forDateDebutAfterOrEquals) {
        this.forDateDebutAfterOrEquals = forDateDebutAfterOrEquals;
    }

    public void setForDateDebutBeforeOrEquals(Date lastDayOfYear) {
        forDateDebutBeforeOrEquals = lastDayOfYear.getSwissValue();
    }

    public void setForDateDebutAfterOrEquals(Date firstDayOfYear) {
        forDateDebutAfterOrEquals = firstDayOfYear.getSwissValue();
    }

    public String getForDateFinAfterOrEquals() {
        return forDateFinAfterOrEquals;
    }

    public void setForDateFinAfterOrEquals(String forDateFinAfterOrEquals) {
        this.forDateFinAfterOrEquals = forDateFinAfterOrEquals;
    }

    public void setForDateFinAfterOrEquals(Date forDateFinAfterOrEquals) {
        this.forDateFinAfterOrEquals = forDateFinAfterOrEquals.getSwissValue();
    }

    public String getForDateFinIsNull() {
        return forDateFinIsNull;
    }

    public void setForDateFinIsNull() {
        forDateFinIsNull = "";
    }

    @Override
    public Class<AffiliationSyndicatComplexModel> whichModelClass() {
        return AffiliationSyndicatComplexModel.class;
    }
}
