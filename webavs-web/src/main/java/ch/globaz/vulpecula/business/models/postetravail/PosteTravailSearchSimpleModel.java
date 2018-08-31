/**
 *
 */
package ch.globaz.vulpecula.business.models.postetravail;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author jpa
 * 
 */
public class PosteTravailSearchSimpleModel extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = -3787344990541366423L;

    private String forIdPosteTravail;
    private String forIdTravailleur;
    private String forIdEmployeur;
    private String forPosteCorrelationId;

    public String getForIdEmployeur() {
        return forIdEmployeur;
    }

    public void setForIdEmployeur(String forIdEmployeur) {
        this.forIdEmployeur = forIdEmployeur;
    }

    public String getForIdPosteTravail() {
        return forIdPosteTravail;
    }

    public String getForIdTravailleur() {
        return forIdTravailleur;
    }

    public void setForIdPosteTravail(String forIdPosteTravail) {
        this.forIdPosteTravail = forIdPosteTravail;
    }

    public void setForIdTravailleur(String forIdTravailleur) {
        this.forIdTravailleur = forIdTravailleur;
    }

    public String getForPosteCorrelationId() {
        return forPosteCorrelationId;
    }

    public void setForPosteCorrelationId(String forPosteCorrelationId) {
        this.forPosteCorrelationId = forPosteCorrelationId;
    }

    @Override
    public Class<PosteTravailSimpleModel> whichModelClass() {
        return PosteTravailSimpleModel.class;
    }
}
