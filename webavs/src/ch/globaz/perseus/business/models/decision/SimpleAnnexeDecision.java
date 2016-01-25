package ch.globaz.perseus.business.models.decision;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author MBO
 * 
 */

public class SimpleAnnexeDecision extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String descriptionAnnexe = null;
    private String idAnnexeDecision = null;
    private String idDecision = null;

    public String getDescriptionAnnexe() {
        return descriptionAnnexe;
    }

    @Override
    public String getId() {
        return idAnnexeDecision;
    }

    public String getIdAnnexeDecision() {
        return idAnnexeDecision;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public void setDescriptionAnnexe(String descriptionAnnexe) {
        this.descriptionAnnexe = descriptionAnnexe;
    }

    @Override
    public void setId(String id) {
        idAnnexeDecision = id;

    }

    public void setIdAnnexeDecision(String idAnnexe) {
        idAnnexeDecision = idAnnexe;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

}
