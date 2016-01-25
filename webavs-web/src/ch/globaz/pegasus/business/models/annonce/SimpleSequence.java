package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleSequence extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String businessKey;
    public String domaine;
    public String id;
    public String sequence;

    public String getBusinessKey() {
        return businessKey;
    }

    public String getDomaine() {
        return domaine;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getSequence() {
        return sequence;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
}
