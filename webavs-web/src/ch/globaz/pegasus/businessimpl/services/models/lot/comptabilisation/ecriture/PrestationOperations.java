package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import java.math.BigDecimal;
import java.util.List;

public class PrestationOperations {
    // key: idRoleComtabilite
    // Map<String, CompteAnnexeSimpleModel> comptesAnnexes;
    private BigDecimal controlAmount = new BigDecimal(0);
    private List<Ecriture> ecritures;
    private String idPrestation;
    private BigDecimal montantPresation;
    private String nom;
    private String numAvsRequerant;
    private List<OrdreVersementCompta> ordresVersements;
    private String prenom;

    public BigDecimal getControlAmount() {
        return controlAmount;
    }

    public List<Ecriture> getEcritures() {
        return ecritures;
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    public BigDecimal getMontantPresation() {
        return montantPresation;
    }

    public String getNom() {
        return nom;
    }

    public String getNumAvsRequerant() {
        return numAvsRequerant;
    }

    public List<OrdreVersementCompta> getOrdresVersements() {
        return ordresVersements;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setControlAmount(BigDecimal controlAmount) {
        this.controlAmount = controlAmount;
    }

    public void setEcritures(List<Ecriture> ecritures) {
        this.ecritures = ecritures;
    }

    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setMontantPresation(BigDecimal montantPresation) {
        this.montantPresation = montantPresation;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNumAvsRequerant(String numAvsRequerant) {
        this.numAvsRequerant = numAvsRequerant;
    }

    public void setOrdresVersements(List<OrdreVersementCompta> ordresVersements) {
        this.ordresVersements = ordresVersements;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

}
