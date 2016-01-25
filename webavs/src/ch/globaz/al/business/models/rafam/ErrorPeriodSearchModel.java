package ch.globaz.al.business.models.rafam;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Modèle de recherche pour le modèle {@link ErrorPeriodModel}
 * 
 * @author jts
 */
public class ErrorPeriodSearchModel extends JadeSearchSimpleModel {

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
    public Class<ErrorPeriodModel> whichModelClass() {
        return ErrorPeriodModel.class;
    }
}
