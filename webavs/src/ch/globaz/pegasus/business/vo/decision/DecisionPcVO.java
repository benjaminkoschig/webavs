package ch.globaz.pegasus.business.vo.decision;

/**
 * Classe value object pour le retour de données particulières liées aux décisions
 * 
 * @author SCE
 * 
 */
public class DecisionPcVO {
    private String csMotif = null;
    private String csSousMotif = null;
    private String csTypeDecision = null;
    private String dateDebut = null;
    private String dateFin = null;
    private String idDecision = null;
    private String idDecisionConjoint = null;
    private String idTiersBeneficiaire = null;
    private String idVersionDroitApc = null;
    private String idVersionDroitSup = null;

    private String noDecision = null;

    private String nss = null;

    public String getCsMotif() {
        return csMotif;
    }

    public String getCsSousMotif() {
        return csSousMotif;
    }

    public String getCsTypeDecision() {
        return csTypeDecision;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDecisionConjoint() {
        return idDecisionConjoint;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public String getIdVersionDroitApc() {
        return idVersionDroitApc;
    }

    public String getIdVersionDroitSup() {
        return idVersionDroitSup;
    }

    public String getNoDecision() {
        return noDecision;
    }

    public String getNss() {
        return nss;
    }

    public void setCsMotif(String csMotif) {
        this.csMotif = csMotif;
    }

    public void setCsSousMotif(String csSousMotif) {
        this.csSousMotif = csSousMotif;
    }

    public void setCsTypeDecision(String csTypeDecision) {
        this.csTypeDecision = csTypeDecision;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDecisionConjoint(String idDecisionConjoint) {
        this.idDecisionConjoint = idDecisionConjoint;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setIdVersionDroitApc(String idVersionDroitApc) {
        this.idVersionDroitApc = idVersionDroitApc;
    }

    public void setIdVersionDroitSup(String idVersionDroitSup) {
        this.idVersionDroitSup = idVersionDroitSup;
    }

    public void setNoDecision(String noDecision) {
        this.noDecision = noDecision;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

}
