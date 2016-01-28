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
    private String forIdPosteTravail;
    private String forIdTravailleur;
    private String forIdEmployeur;

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

    @Override
    public Class<PosteTravailSimpleModel> whichModelClass() {
        return PosteTravailSimpleModel.class;
    }
}
