package ch.globaz.al.business.models.rafam;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle d'une erreur liée à une annonce RAFAM
 * 
 * @author jts
 * 
 */
public class ErrorPeriodModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Date indiquant la fin d'une période d'erreur */
    private String errorPeriodEnd = null;
    /** Date indiquant le début d'une période d'erreur */
    private String errorPeriodStart = null;
    /** ID de l'annonce à laquelle est liée l'erreur */
    private String idErreurAnnonce = null;
    /** identifiant */
    private String idErrorPeriod = null;

    public String getErrorPeriodEnd() {
        return errorPeriodEnd;
    }

    public String getErrorPeriodStart() {
        return errorPeriodStart;
    }

    @Override
    public String getId() {
        return idErrorPeriod;
    }

    public String getIdErreurAnnonce() {
        return idErreurAnnonce;
    }

    public String getIdErrorPeriod() {
        return idErrorPeriod;
    }

    public void setErrorPeriodEnd(String errorPeriodEnd) {
        this.errorPeriodEnd = errorPeriodEnd;
    }

    public void setErrorPeriodStart(String errorPeriodStart) {
        this.errorPeriodStart = errorPeriodStart;
    }

    @Override
    public void setId(String id) {
        idErrorPeriod = id;
    }

    public void setIdErreurAnnonce(String idErreurAnnonce) {
        this.idErreurAnnonce = idErreurAnnonce;
    }

    public void setIdErrorPeriod(String idErrorPeriod) {
        this.idErrorPeriod = idErrorPeriod;
    }
}