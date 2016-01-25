package globaz.cygnus.services.validerDecision;

import java.io.Serializable;
import java.util.Set;

/*
 * une demande a : - un id - une liste de motifs de refus - une date de facture - une période de traitement - un assuré
 * concerné - un type
 */
public class RFDemandeValidationData implements Serializable {

    private static final long serialVersionUID = 1L;
    private String dateDebutTraitement = "";
    private String dateFacture = "";
    private String dateFinTraitement = "";
    private String dateReception = "";
    private String idDemande = "";
    private String idDemandeParent = "";
    private String idFournisseur = "";
    private String idQdPrincipale = "";
    private String idRubriqueAI = "";
    private String idRubriqueAVS = "";
    private String idRubriqueInvalidite = "";
    private String idSousTypeSoin = "";
    private String idTiers = "";
    private String idTiersAssureConcerne = "";
    private String idTypeSoin = "";
    private Boolean isForcerPayement = false;
    private Boolean isPP = false;
    private Boolean isTexteRedirection = Boolean.FALSE;
    // montantAdmis = montant demande - somme des montants des motifs de refus
    private String montantAccepte = "";
    // private String montantAcceptePeriodeDeTraitement = "";
    // private String typeDemande = "";
    private String montantFacture = "";
    private String montantMensuel = "";
    private String montantOAI = "";
    // private String montantPeriodeDeTraitement = "";
    // private String montantPayement = "";
    private Set<RFMotifRefusDemandeValidationData> motifsRefus = null;
    private String remarqueFournisseur = "";

    public RFDemandeValidationData(String idDemande, Set<RFMotifRefusDemandeValidationData> motifsRefus,
            String dateFacture, String dateDebutTraitement, String dateFinTraitement, String dateReception,
            String idTiers, String idFournisseur, String idSousTypeSoin, String idRubriqueAI, String idRubriqueAVS,
            String idRubriqueInvalidite, Boolean isPP, Boolean isTexteRedirection, String idTypeSoin,
            String montantFacture, String montantAccepte, Boolean isForcerPayement, String montantOAI,
            String montantMensuel, String idTiersAssureConcerne, String idDemandeParent, String idQdPrincipale,
            String remarqueFournisseur) {
        super();
        this.idDemande = idDemande;
        this.motifsRefus = motifsRefus;
        this.dateFacture = dateFacture;
        this.dateDebutTraitement = dateDebutTraitement;
        this.dateFinTraitement = dateFinTraitement;
        this.dateReception = dateReception;
        this.idTiers = idTiers;
        // this.typeDemande = typeDemande;
        this.idFournisseur = idFournisseur;
        // this.montantPayement = montantPayement;
        this.idSousTypeSoin = idSousTypeSoin;
        this.idTypeSoin = idTypeSoin;
        this.idRubriqueAI = idRubriqueAI;
        this.idRubriqueAVS = idRubriqueAVS;
        this.idRubriqueInvalidite = idRubriqueInvalidite;
        this.isPP = isPP;
        this.isTexteRedirection = isTexteRedirection;
        this.isForcerPayement = isForcerPayement;
        this.montantAccepte = montantAccepte;
        this.montantFacture = montantFacture;
        this.montantOAI = montantOAI;
        this.montantMensuel = montantMensuel;
        this.idTiersAssureConcerne = idTiersAssureConcerne;
        this.idDemandeParent = idDemandeParent;
        this.idQdPrincipale = idQdPrincipale;
        this.remarqueFournisseur = remarqueFournisseur;

        // this.montantPeriodeDeTraitement = montantPeriodeDeTraitement;
        // this.montantAcceptePeriodeDeTraitement = montantAcceptePeriodeDeTraitement;
    }

    public String getDateDebutTraitement() {
        return dateDebutTraitement;
    }

    public String getDateFacture() {
        return dateFacture;
    }

    public String getDateFinTraitement() {
        return dateFinTraitement;
    }

    public String getDateReception() {
        return dateReception;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdDemandeParent() {
        return idDemandeParent;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public String getIdQdPrincipale() {
        return idQdPrincipale;
    }

    public String getIdRubriqueAI() {
        return idRubriqueAI;
    }

    public String getIdRubriqueAVS() {
        return idRubriqueAVS;
    }

    public String getIdRubriqueInvalidite() {
        return idRubriqueInvalidite;
    }

    public String getIdSousTypeSoin() {
        return idSousTypeSoin;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersAssureConcerne() {
        return idTiersAssureConcerne;
    }

    public String getIdTypeSoin() {
        return idTypeSoin;
    }

    public Boolean getIsForcerPayement() {
        return isForcerPayement;
    }

    public Boolean getIsPP() {
        return isPP;
    }

    public String getMontantAccepte() {
        return montantAccepte;
    }

    public String getMontantFacture() {
        return montantFacture;
    }

    public String getMontantMensuel() {
        return montantMensuel;
    }

    public String getMontantOAI() {
        return montantOAI;
    }

    public Set<RFMotifRefusDemandeValidationData> getMotifsRefus() {
        return motifsRefus;
    }

    public String getRemarqueFournisseur() {
        return remarqueFournisseur;
    }

    public void setDateDebutTraitement(String dateDebutTraitement) {
        this.dateDebutTraitement = dateDebutTraitement;
    }

    public void setDateFacture(String dateFacture) {
        this.dateFacture = dateFacture;
    }

    public void setDateFinTraitement(String dateFinTraitement) {
        this.dateFinTraitement = dateFinTraitement;
    }

    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdDemandeParent(String idDemandeParent) {
        this.idDemandeParent = idDemandeParent;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public void setIdQdPrincipale(String idQdPrincipale) {
        this.idQdPrincipale = idQdPrincipale;
    }

    public void setIdRubriqueAI(String idRubriqueAI) {
        this.idRubriqueAI = idRubriqueAI;
    }

    public void setIdRubriqueAVS(String idRubriqueAVS) {
        this.idRubriqueAVS = idRubriqueAVS;
    }

    public void setIdRubriqueInvalidite(String idRubriqueInvalidite) {
        this.idRubriqueInvalidite = idRubriqueInvalidite;
    }

    public void setIdSousTypeSoin(String idSousTypeSoin) {
        this.idSousTypeSoin = idSousTypeSoin;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersAssureConcerne(String idTiersAssureConcerne) {
        this.idTiersAssureConcerne = idTiersAssureConcerne;
    }

    public void setIdTypeSoin(String idTypeSoin) {
        this.idTypeSoin = idTypeSoin;
    }

    public void setIsForcerPayement(Boolean isForcerPayement) {
        this.isForcerPayement = isForcerPayement;
    }

    public void setIsPP(Boolean isPP) {
        this.isPP = isPP;
    }

    public void setMontantAccepte(String montantAccepte) {
        this.montantAccepte = montantAccepte;
    }

    public void setMontantFacture(String montantFacture) {
        this.montantFacture = montantFacture;
    }

    public void setMontantMensuel(String montantMensuel) {
        this.montantMensuel = montantMensuel;
    }

    public void setMontantOAI(String montantOAI) {
        this.montantOAI = montantOAI;
    }

    public void setMotifsRefus(Set<RFMotifRefusDemandeValidationData> motifsRefus) {
        this.motifsRefus = motifsRefus;
    }

    public void setRemarqueFournisseur(String remarqueFournisseur) {
        this.remarqueFournisseur = remarqueFournisseur;
    }

    public Boolean getIsTexteRedirection() {
        return isTexteRedirection;
    }

    public void setIsTexteRedirection(Boolean isTexteRedirection) {
        this.isTexteRedirection = isTexteRedirection;
    }

}
