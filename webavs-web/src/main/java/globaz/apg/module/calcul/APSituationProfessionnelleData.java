package globaz.apg.module.calcul;

import globaz.apg.enums.APAssuranceTypeAssociation;
import java.math.BigDecimal;

public class APSituationProfessionnelleData {

    private APAssuranceTypeAssociation association = null;
    private String dateDebut = "";
    private String dateFin = "";
    private String id = "";
    private String idAffilie = "";
    private String idTiersEmployeur = "";
    private BigDecimal montantJournalierAcmNe = null;
    private String nom = "";
    private String idTiersPaiementEmployeur = "";
    private String idDomainePaiementEmployeur = "";

    public APSituationProfessionnelleData(APAssuranceTypeAssociation association, String dateDebut, String dateFin,
            BigDecimal montantJournalierAcmNe, String id, String idTiersEmployeur, String idAffilie, String nom,
            String idTiersPaiementEmployeur, String idDomainePaiementEmployeur) {
        super();
        this.association = association;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.montantJournalierAcmNe = montantJournalierAcmNe;
        this.id = id;
        this.idTiersEmployeur = idTiersEmployeur;
        this.idAffilie = idAffilie;
        this.nom = nom;
        this.idTiersPaiementEmployeur = idTiersPaiementEmployeur;
        this.idDomainePaiementEmployeur = idDomainePaiementEmployeur;
    }

    public APAssuranceTypeAssociation getAssociation() {
        return association;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getId() {
        return id;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getIdTiersEmployeur() {
        return idTiersEmployeur;
    }

    public BigDecimal getMontantJournalierAcmNe() {
        return montantJournalierAcmNe;
    }

    public String getNom() {
        return nom;
    }

    public void setAssociation(APAssuranceTypeAssociation association) {
        this.association = association;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setIdTiersEmployeur(String idTiersEmployeur) {
        this.idTiersEmployeur = idTiersEmployeur;
    }

    public void setMontantJournalierAcmNe(BigDecimal montantJournalierAcmNe) {
        this.montantJournalierAcmNe = montantJournalierAcmNe;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setIdDomainePaiementEmployeur(String idDomainePaiementEmployeur) {
        this.idDomainePaiementEmployeur = idDomainePaiementEmployeur;
    }

    public void setIdTiersPaiementEmployeur(String idTiersPaiementEmployeur) {
        this.idTiersPaiementEmployeur = idTiersPaiementEmployeur;
    }

    public String getIdDomainePaiementEmployeur() {
        return idDomainePaiementEmployeur;
    }

    public String getIdTiersPaiementEmployeur() {
        return idTiersPaiementEmployeur;
    }
}
