package ch.globaz.pegasus.businessimpl.services.models.calcul.donneeInterne;

import ch.globaz.common.domaine.Periode;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;

import java.util.ArrayList;
import java.util.List;

public class DonneeInterneHomeVersement {
    public final static String TYPE_CREANCIER = "TYPE_CREANCIER";
    public final static String TYPE_RETENUS = "TYPE_RETENUS";
    private String csTypeVersement = "";



    private boolean isVersementDirect = false;
    //RETENUS
    private String idPca = "";
    private String idRenteAccordee ="";
    private String idAdressePaiement ="";
    private String idTiersHome ="";
    private String csRoleBeneficiaire= "";
    private String dateDebut ="";
    private String dateFin ="";
    private String dateDebutPCA ="";
    private String dateFinPCA ="";

    //CREANCIER
    private String idDemande ="";
    private String idTiersAdressePaiement ="";
    private String idTiersRegroupement ="";

    //MONTANT
    private String montantPCMensuel = "";
    private String montantHomes = "";
    private String montantDepenses = "";
    private String montantDejaVerser = "";
    private String montantDette = "";
    private String montantAVerser = "";
    List<Periode> periodeListCreanceAccorde = new ArrayList<>();


    public String getIdPca() {
        return idPca;
    }

    public void setIdPca(String idPca) {
        this.idPca = idPca;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public String getCsRoleBeneficiaire() {
        return csRoleBeneficiaire;
    }

    public void setCsRoleBeneficiaire(String csRoleBeneficiaire) {
        this.csRoleBeneficiaire = csRoleBeneficiaire;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }
    public String getIdTiersHome() {
        return idTiersHome;
    }

    public void setIdTiersHome(String idTiersHome) {
        this.idTiersHome = idTiersHome;
    }

    public String getCsTypeVersement() {
        return csTypeVersement;
    }

    public void setCsTypeVersement(String csTypeVersement) {
        this.csTypeVersement = csTypeVersement;
    }
    public String getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }


    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public String getIdTiersRegroupement() {
        return idTiersRegroupement;
    }

    public void setIdTiersRegroupement(String idTiersRegroupement) {
        this.idTiersRegroupement = idTiersRegroupement;
    }

    public String getMontantHomes() {
        return montantHomes;
    }

    public void setMontantHomes(String montantHomes) {
        this.montantHomes = montantHomes;
    }

    public String getMontantPCMensuel() {
        return montantPCMensuel;
    }

    public void setMontantPCMensuel(String montantPCMensuel) {
        this.montantPCMensuel = montantPCMensuel;
    }

    public String getMontantDepenses() {
        return montantDepenses;
    }

    public void setMontantDepenses(String montantDepenses) {
        this.montantDepenses = montantDepenses;
    }
    public List<Periode> getPeriodeListCreanceAccorde() {
        return periodeListCreanceAccorde;
    }

    public void setPeriodeListCreanceAccorde(List<Periode> periodeListCreanceAccorde) {
        this.periodeListCreanceAccorde = periodeListCreanceAccorde;
    }

    public String getMontantDejaVerser() {
        return montantDejaVerser;
    }

    public void setMontantDejaVerser(String montantDejaVerser) {
        this.montantDejaVerser = montantDejaVerser;
    }

    public String getDateDebutPCA() {
        return dateDebutPCA;
    }

    public void setDateDebutPCA(String dateDebutPCA) {
        this.dateDebutPCA = dateDebutPCA;
    }

    public String getDateFinPCA() {
        return dateFinPCA;
    }

    public void setDateFinPCA(String dateFinPCA) {
        this.dateFinPCA = dateFinPCA;
    }

    public String getMontantDette() {
        return montantDette;
    }

    public void setMontantDette(String montantDette) {
        this.montantDette = montantDette;
    }
    public void setVersementDirect(boolean versementDirect) {
        isVersementDirect = versementDirect;
    }
    public boolean isVersementDirect() {
        return isVersementDirect;
    }


    public String getMontantAVerser() {
        return montantAVerser;
    }

    public void setMontantAVerser(String montantAVerser) {
        this.montantAVerser = montantAVerser;
    }
}
