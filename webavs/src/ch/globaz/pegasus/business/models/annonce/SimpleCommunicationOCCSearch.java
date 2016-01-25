package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleCommunicationOCCSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdVersionDroit;

    @Override
    public Class<SimpleCommunicationOCC> whichModelClass() {
        return SimpleCommunicationOCC.class;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

}
