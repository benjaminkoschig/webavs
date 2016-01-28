package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class PcaRetenueSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPca;

    public String getForIdPca() {
        return forIdPca;
    }

    public void setForIdPca(String forIdPca) {
        this.forIdPca = forIdPca;
    }

    @Override
    public Class<PcaRetenue> whichModelClass() {
        return PcaRetenue.class;
    }

}
