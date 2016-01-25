package ch.globaz.perseus.business.models.decision;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author MBO
 * 
 */

public class SimpleDecisionMotif extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String commentaire = null;
    private String idDecision = null;
    private String idDecisionMotif = null;
    private String idMotif = null;

    public String getCommentaire() {
        return commentaire;
    }

    @Override
    public String getId() {
        return idDecisionMotif;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDecisionMotif() {
        return idDecisionMotif;
    }

    public String getIdMotif() {
        return idMotif;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    @Override
    public void setId(String id) {
        idDecisionMotif = id;

    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDecisionMotif(String idDecisionMotif) {
        this.idDecisionMotif = idDecisionMotif;
    }

    public void setIdMotif(String idMotif) {
        this.idMotif = idMotif;
    }

}
