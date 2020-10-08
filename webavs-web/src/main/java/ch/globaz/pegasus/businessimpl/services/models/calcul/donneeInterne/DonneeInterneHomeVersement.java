package ch.globaz.pegasus.businessimpl.services.models.calcul.donneeInterne;

import ch.globaz.common.domaine.Periode;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;

import java.util.ArrayList;
import java.util.List;

public class DonneeInterneHomeVersement {
    public static String TYPE_CREANCIER = "TYPE_CREANCIER";
    public static String TYPE_RETENUS = "TYPE_RETENUS";
    private String csTypeVersement = "";
    //RETENUS
    private String idPca = "";
    private String idRenteAccordee ="";
    private String idAdressePaiement ="";
    private String idTiersHome ="";
    private String csRoleBeneficiaire= "";
    private String dateDebut ="";
    private String dateFin ="";

    //CREANCIER
    private String idDemande ="";
    private String idTiersAdressePaiement ="";
    private String idTiersRegroupement ="";

    //MONTANT
    private String montantPCMensuel = "";
    private String montantHomes = "";
    private String montantDepenses = "";
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

}
