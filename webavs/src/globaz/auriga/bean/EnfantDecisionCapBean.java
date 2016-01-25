package globaz.auriga.bean;

import java.io.Serializable;

/**
 * Bean représentant un enfant dans l'écran des décisions CAP
 * 
 * @author bjo
 * 
 */
public class EnfantDecisionCapBean implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateNaissance;
    private String dateRadiation;
    private String idDecision;
    private String idEnfantDecision;
    private String idTiers;
    private String montant;
    private String nomTiers;
    private String prenomTiers;

    public EnfantDecisionCapBean(String idEnfantDecision, String idDecision, String idTiers, String dateNaissance,
            String dateRadiation, String montant, String nomTiers, String prenomTiers) {
        this.idEnfantDecision = idEnfantDecision;
        this.idDecision = idDecision;
        this.idTiers = idTiers;
        this.dateRadiation = dateRadiation;
        this.dateNaissance = dateNaissance;
        this.montant = montant;
        this.nomTiers = nomTiers;
        this.prenomTiers = prenomTiers;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDateRadiation() {
        return dateRadiation;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdEnfantDecision() {
        return idEnfantDecision;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMontant() {
        return montant;
    }

    public String getNomTiers() {
        return nomTiers;
    }

    public String getPrenomTiers() {
        return prenomTiers;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDateRadiation(String dateRadiation) {
        this.dateRadiation = dateRadiation;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdEnfantDecision(String idEnfantDecision) {
        this.idEnfantDecision = idEnfantDecision;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setNomTiers(String nomTiers) {
        this.nomTiers = nomTiers;
    }

    public void setPrenomTiers(String prenomTiers) {
        this.prenomTiers = prenomTiers;
    }
}
