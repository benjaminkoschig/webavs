package ch.globaz.pegasus.business.models.creancier;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleCreancierHystoriqueSearch extends JadeSearchSimpleModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String forIdCreaanceAccordee = null;
    private String forIdCreancier = null;
    private String forIdPcAccordee = null;

    public String getForIdCreaanceAccordee() {
        return forIdCreaanceAccordee;
    }

    public String getForIdCreancier() {
        return forIdCreancier;
    }

    public String getForIdPcAccordee() {
        return forIdPcAccordee;
    }

    public void setForIdCreaanceAccordee(String forIdCreaanceAccordee) {
        this.forIdCreaanceAccordee = forIdCreaanceAccordee;
    }

    public void setForIdCreancier(String forIdCreancier) {
        this.forIdCreancier = forIdCreancier;
    }

    public void setForIdPcAccordee(String forIdPcAccordee) {
        this.forIdPcAccordee = forIdPcAccordee;
    }

    @Override
    public Class<SimpleCreancierHystorique> whichModelClass() {
        return SimpleCreancierHystorique.class;
    }
}
