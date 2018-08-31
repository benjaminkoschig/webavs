/**
 *
 */
package ch.globaz.vulpecula.business.models.travailleur;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class TravailleurSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = -7353723917486081635L;

    private String forId;
    private String forIdTiers;
    private String forIdTravailleur;
    private String forCorrelationId;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForIdTravailleur() {
        return forIdTravailleur;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForIdTravailleur(String forIdTravailleur) {
        this.forIdTravailleur = forIdTravailleur;
    }

    public String getForCorrelationId() {
        return forCorrelationId;
    }

    public void setForCorrelationId(String forCorrelationId) {
        this.forCorrelationId = forCorrelationId;
    }

    @Override
    public Class<TravailleurSimpleModel> whichModelClass() {
        return TravailleurSimpleModel.class;
    }
}
