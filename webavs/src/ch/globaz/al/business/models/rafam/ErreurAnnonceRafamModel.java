package ch.globaz.al.business.models.rafam;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle d'une erreur liée à une annonce RAFam
 * 
 * @author jts
 * 
 */
public class ErreurAnnonceRafamModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Code d'erreur généré par la centrale */
    private String code = null;
    /** ID de l'annonce à laquelle est liée l'erreur */
    private String idAnnonce = null;
    /** identifiant */
    private String idErreurAnnonce = null;

    public String getCode() {
        return code;
    }

    @Override
    public String getId() {
        return idErreurAnnonce;
    }

    public String getIdAnnonce() {
        return idAnnonce;
    }

    public String getIdErreurAnnonce() {
        return idErreurAnnonce;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public void setId(String id) {
        idErreurAnnonce = id;
    }

    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

    public void setIdErreurAnnonce(String idErreurAnnonce) {
        this.idErreurAnnonce = idErreurAnnonce;
    }
}