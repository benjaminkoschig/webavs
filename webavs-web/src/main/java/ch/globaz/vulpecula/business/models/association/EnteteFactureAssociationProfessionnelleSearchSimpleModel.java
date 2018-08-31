/**
 *
 */
package ch.globaz.vulpecula.business.models.association;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import ch.globaz.vulpecula.domain.models.association.EtatFactureAP;

public class EnteteFactureAssociationProfessionnelleSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 3553507742090707047L;
    private String forId;
    private String forIdEmployeur;
    private String forEtat;
    private String forIdAssociation;
    private String forAnnee;
    private String forIdPassageFacturation;

    public String getForIdEmployeur() {
        return forIdEmployeur;
    }

    public void setForIdEmployeur(String forIdEmployeur) {
        this.forIdEmployeur = forIdEmployeur;
    }

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    /**
     * @return the forEtat
     */
    public String getForEtat() {
        return forEtat;
    }

    /**
     * @param forEtat the forEtat to set
     */
    public void setForEtat(EtatFactureAP forEtat) {
        this.forEtat = forEtat.getValue();
    }

    public String getForIdAssociation() {
        return forIdAssociation;
    }

    public void setForIdAssociation(String forIdAssociation) {
        this.forIdAssociation = forIdAssociation;
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    /**
     * @return the forIdPassageFacturation
     */
    public String getForIdPassageFacturation() {
        return forIdPassageFacturation;
    }

    /**
     * @param forIdPassageFacturation the forIdPassageFacturation to set
     */
    public void setForIdPassageFacturation(String forIdPassageFacturation) {
        this.forIdPassageFacturation = forIdPassageFacturation;
    }

    @Override
    public Class<EnteteFactureAssociationProfessionnelleSimpleModel> whichModelClass() {
        return EnteteFactureAssociationProfessionnelleSimpleModel.class;
    }
}
