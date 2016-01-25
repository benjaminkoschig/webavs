package ch.globaz.perseus.business.models.decision;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author MBO
 * 
 */

public class SimpleCopieDecision extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idCopieDecision = null;
    private String idDecision = null;
    private String idTiers = null;
    private String remarqueCopieDecision = null;

    @Override
    public String getId() {
        return idCopieDecision;
    }

    public String getIdCopieDecision() {
        return idCopieDecision;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getRemarqueCopieDecision() {
        return remarqueCopieDecision;
    }

    @Override
    public void setId(String id) {
        idCopieDecision = id;

    }

    public void setIdCopieDecision(String idCopieDecision) {
        this.idCopieDecision = idCopieDecision;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setRemarqueCopieDecision(String remarqueCopieDecision) {
        this.remarqueCopieDecision = remarqueCopieDecision;
    }

}
