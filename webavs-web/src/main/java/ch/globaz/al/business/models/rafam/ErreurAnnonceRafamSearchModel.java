package ch.globaz.al.business.models.rafam;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Modèle de recherche pour le modèle {@link ErreurAnnonceRafamModel}
 * 
 * @author jts
 */
public class ErreurAnnonceRafamSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Recherche sur le code d'erreur */
    private String forCode = null;
    /** Recherche sur l'id de l'annonce */
    private String forIdAnnonce = null;
    /** Rechrche sur l'id de l'erreur */
    private String forIdErreurAnnonce = null;

    public String getForCode() {
        return forCode;
    }

    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    public String getForIdErreurAnnonce() {
        return forIdErreurAnnonce;
    }

    public void setForCode(String forCode) {
        this.forCode = forCode;
    }

    public void setForIdAnnonce(String forIdAnnonce) {
        this.forIdAnnonce = forIdAnnonce;
    }

    public void setForIdErreurAnnonce(String forIdErreurAnnonce) {
        this.forIdErreurAnnonce = forIdErreurAnnonce;
    }

    @Override
    public Class<ErreurAnnonceRafamModel> whichModelClass() {
        return ErreurAnnonceRafamModel.class;
    }
}
