package ch.globaz.pegasus.business.domaine.pca;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pyxis.domaine.PersonneAVS;

public class Pca {
    private static final long serialVersionUID = 1L;
    private PcaEtat etat;
    private PcaEtatCalcul etatCalcul;
    private PcaGenre genre;
    private RoleMembreFamille roleBeneficiaire;
    private PcaType type;
    private Date dateDebut;
    private Date dateFin;
    private Boolean hasCalculComparatif;
    private Boolean hasJoursAppoint;
    private String id;
    private PersonneAVS beneficiaire = new PersonneAVS();
    private PersonneAVS beneficiaireConjointDom2R = new PersonneAVS();
    private String idVersionDroit;
    private Boolean isCalculManuel;
    private Boolean isCalculRetro;
    private Boolean isSupprime;
    private String sousCode;
    private Calcul calcul;
    private Date dateDernierPaiement;

    private Montant montant = Montant.ZERO;

    public PcaEtat getEtat() {
        return etat;
    }

    public void setEtat(PcaEtat etat) {
        this.etat = etat;
    }

    public PcaGenre getGenre() {
        return genre;
    }

    public void setGenre(PcaGenre genre) {
        this.genre = genre;
    }

    public RoleMembreFamille getRoleBeneficiaire() {
        return roleBeneficiaire;
    }

    public void setRoleBeneficiaire(RoleMembreFamille roleBeneficiaire) {
        this.roleBeneficiaire = roleBeneficiaire;
    }

    public PcaType getType() {
        return type;
    }

    public void setType(PcaType type) {
        this.type = type;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getHasCalculComparatif() {
        return hasCalculComparatif;
    }

    public void setHasCalculComparatif(Boolean hasCalculComparatif) {
        this.hasCalculComparatif = hasCalculComparatif;
    }

    public Boolean getHasJoursAppoint() {
        return hasJoursAppoint;
    }

    public void setHasJoursAppoint(Boolean hasJoursAppoint) {
        this.hasJoursAppoint = hasJoursAppoint;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PersonneAVS getBeneficiaire() {
        return beneficiaire;
    }

    public void setBeneficiaire(PersonneAVS beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public PersonneAVS getBeneficiaireConjointDom2R() {
        return beneficiaireConjointDom2R;
    }

    public void setBeneficiaireConjointDom2R(PersonneAVS beneficiaireConjointDom2R) {
        this.beneficiaireConjointDom2R = beneficiaireConjointDom2R;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public Boolean getIsCalculManuel() {
        return isCalculManuel;
    }

    public void setIsCalculManuel(Boolean isCalculManuel) {
        this.isCalculManuel = isCalculManuel;
    }

    public Boolean getIsCalculRetro() {
        return isCalculRetro;
    }

    public void setIsCalculRetro(Boolean isCalculRetro) {
        this.isCalculRetro = isCalculRetro;
    }

    public Boolean getIsSupprime() {
        return isSupprime;
    }

    public void setIsSupprime(Boolean isSupprime) {
        this.isSupprime = isSupprime;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Montant getMontant() {
        return montant;
    }

    public void setMontant(Montant montant) {
        this.montant = montant;
    }

    public String getSousCode() {
        return sousCode;
    }

    public void setSousCode(String sousCode) {
        this.sousCode = sousCode;
    }

    public PcaEtatCalcul getEtatCalcul() {
        return etatCalcul;
    }

    public void setEtatCalcul(PcaEtatCalcul etatCalcul) {
        this.etatCalcul = etatCalcul;
    }

    public Calcul getCalcul() {
        return calcul;
    }

    public void setCalcul(Calcul calcul) {
        this.calcul = calcul;
    }

    public Date getDateDernierPaiement() {
        return dateDernierPaiement;
    }

    public void setDateDernierPaiement(Date dateDernierPaiement) {
        this.dateDernierPaiement = dateDernierPaiement;
    }

    public boolean hasCurrent() {
        return dateFin == null;
    }

    @Override
    public String toString() {
        return "Pca [etat=" + etat + ", genre=" + genre + ", roleBeneficiaire=" + roleBeneficiaire + ", type=" + type
                + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin + ", hasCalculComparatif=" + hasCalculComparatif
                + ", hasJoursAppoint=" + hasJoursAppoint + ", id=" + id + ", beneficiaire=" + beneficiaire
                + ", beneficiaireConjointDom2R=" + beneficiaireConjointDom2R + ", idVersionDroit=" + idVersionDroit
                + ", isCalculManuel=" + isCalculManuel + ", isCalculRetro=" + isCalculRetro + ", isSupprime="
                + isSupprime + ", sousCode=" + sousCode + ", Calcul=" + calcul + ", montant=" + montant + "]";
    }

}
