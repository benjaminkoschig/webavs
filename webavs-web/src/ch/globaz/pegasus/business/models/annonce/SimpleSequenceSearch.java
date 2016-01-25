package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleSequenceSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forBusinessKey;
    private String forDomaine;

    public String getForBusinessKey() {
        return forBusinessKey;
    }

    public String getForDomaine() {
        return forDomaine;
    }

    public void setForBusinessKey(String forBusinessKey) {
        this.forBusinessKey = forBusinessKey;
    }

    public void setForDomaine(String forDomaine) {
        this.forDomaine = forDomaine;
    }

    @Override
    public Class<SimpleSequence> whichModelClass() {
        return SimpleSequence.class;
    }

}
