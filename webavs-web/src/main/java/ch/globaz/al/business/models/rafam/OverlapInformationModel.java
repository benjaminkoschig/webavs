package ch.globaz.al.business.models.rafam;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle d'une erreur liée à une annonce RAFAM
 * 
 * @author jts
 * 
 */
public class OverlapInformationModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Numéro de la branche de l'office en conflit */
    private String branch = null;
    /** ID de l'annonce à laquelle est liée l'erreur */
    private String idErreurAnnonce = null;
    /** identifiant */
    private String idOverlapInformation = null;
    /** Indique un conflit de moindre importance */
    private Boolean insignificance = null;
    private String minimalStartFlag = null;
    /** Identifiant de l'office en conflit */
    private String officeIdentifier = null;
    /** Date indiquant la fin de chevauchement d'une période */
    private String overlapPeriodeEnd = null;
    /** Date indiquant le début de chevauchement d'une période */
    private String overlapPeriodeStart = null;

    public String getBranch() {
        return branch;
    }

    @Override
    public String getId() {
        return idOverlapInformation;
    }

    public String getIdErreurAnnonce() {
        return idErreurAnnonce;
    }

    public String getIdOverlapInformation() {
        return idOverlapInformation;
    }

    public Boolean getInsignificance() {
        return insignificance;
    }

    public String getMinimalStartFlag() {
        return minimalStartFlag;
    }

    public String getOfficeIdentifier() {
        return officeIdentifier;
    }

    public String getOverlapPeriodeEnd() {
        return overlapPeriodeEnd;
    }

    public String getOverlapPeriodeStart() {
        return overlapPeriodeStart;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Override
    public void setId(String id) {
        idOverlapInformation = id;
    }

    public void setIdErreurAnnonce(String idErreurAnnonce) {
        this.idErreurAnnonce = idErreurAnnonce;
    }

    public void setIdOverlapInformation(String idOverlapInformation) {
        this.idOverlapInformation = idOverlapInformation;
    }

    public void setInsignificance(Boolean insignificance) {
        this.insignificance = insignificance;
    }

    public void setMinimalStartFlag(String minimalStartFlag) {
        this.minimalStartFlag = minimalStartFlag;
    }

    public void setOfficeIdentifier(String officeIdentifier) {
        this.officeIdentifier = officeIdentifier;
    }

    public void setOverlapPeriodeEnd(String overlapPeriodeEnd) {
        this.overlapPeriodeEnd = overlapPeriodeEnd;
    }

    public void setOverlapPeriodeStart(String overlapPeriodeStart) {
        this.overlapPeriodeStart = overlapPeriodeStart;
    }

}