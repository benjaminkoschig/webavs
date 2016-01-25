package ch.globaz.al.business.models.rafam;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Mod�le de recherche pour le mod�le {@link OverlapInformationModel}
 * 
 * @author jts
 */
public class OverlapInformationSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdErreurAnnonce = null;

    public String getForIdErreurAnnonce() {
        return forIdErreurAnnonce;
    }

    public void setForIdErreurAnnonce(String forIdErreurAnnonce) {
        this.forIdErreurAnnonce = forIdErreurAnnonce;
    }

    @Override
    public Class<OverlapInformationModel> whichModelClass() {
        return OverlapInformationModel.class;
    }
}
