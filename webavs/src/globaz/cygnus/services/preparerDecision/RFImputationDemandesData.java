/*
 * Créé le 25 novembre 2010
 */
package globaz.cygnus.services.preparerDecision;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author JJE
 * 
 */
public class RFImputationDemandesData {

    private String codeSousTypeDeSoin = "";
    private String codeTypeDeSoin = "";
    private String csEtat = "";
    private String csSource = "";
    private String csSousTypeDeSoin = "";
    private String dateDebutTraitement = "";
    private String dateDeces = "";
    private String dateDemande = "";
    private String dateDernierPaiement_mm_yyyy = "";
    private String dateFacture = "";
    private String dateFinTraitement = "";
    private String dateReception = "";
    private boolean hasSoldeExcedent = false;
    private String idAdressePaiement = "";
    private String idDemande = "";
    private String idDemandeParent = "";
    private String idDossier = "";
    private String idFournisseur = "";
    private String idGestionnaire = "";
    private String idQdAssure = "";
    private String idQdPrincipale = "";
    private String idQdPrincipaleParent = "";
    private String idQdVirtuelle = "";
    private String idTiers = "";
    private boolean isConventionne = false;
    private boolean isEnfant = false;
    private boolean isEnfantExclu = false;
    private boolean isForcerImputation = false;
    private boolean isForcerPaiement = false;
    // si la demande concerne un paiement mensuel uniquemement rétroactif, ce champ sera setté à false -> ne pas
    // utiliser ce champ lors du paiement pour déterminer la gestion des dettes
    private boolean isPaiementMensuel = false;
    // peut aussi concerner un paiement mensuel uniquement rétroactif donc ponctuel (voir champ, isPaiementMensuel)
    private boolean isPP = false;
    private String montantAccepte = "";
    // public String montantAccepteSiSoldeExcedent = "";
    private String montantAPayerInitial = "";
    // private String montantAPayerInitialPaiementMensuel = "";
    // public String montantFacture = "";
    private String montantFacture44 = "";
    private String montantMensuel = "";
    private String montantVerseOAI = "";
    // id motif refus système, montant refusé
    private Set<String[]> motifsDeRefus = new HashSet<String[]>();
    private String nss = "";
    private String restitutionCsSource = "";
    private String restitutionIdDecisionRFMAccordee = "";
    private String restitutionIdTiersRequerant = "";
    private boolean restitutionIsForcerPaiementParent = false;
    private String restitutionMontantAPayeParent = "";
    private String restitutionMontantDepassementQdParent = "";
    private String statutDemande = "";
    private String typeDePaiment = "";
    private String typePCParent = "";

    private String montantAccepteAvantAdaptation = "";
    private String montantMensuelAccepteAvantAdaptation = "";

    public String getCodeSousTypeDeSoin() {
        return codeSousTypeDeSoin;
    }

    public String getCodeTypeDeSoin() {
        return codeTypeDeSoin;
    }

    public String getCsEtat() {
        return csEtat;
    }

    public String getCsSource() {
        return csSource;
    }

    public String getCsSousTypeDeSoin() {
        return csSousTypeDeSoin;
    }

    public String getDateDebutTraitement() {
        return dateDebutTraitement;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateDemande() {
        return dateDemande;
    }

    public String getDateDernierPaiement_mm_yyyy() {
        return dateDernierPaiement_mm_yyyy;
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

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdDemandeParent() {
        return idDemandeParent;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdQdAssure() {
        return idQdAssure;
    }

    public String getIdQdPrincipale() {
        return idQdPrincipale;
    }

    public String getIdQdPrincipaleParent() {
        return idQdPrincipaleParent;
    }

    public String getIdQdVirtuelle() {
        return idQdVirtuelle;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public boolean getIsForcerImputation() {
        return isForcerImputation;
    }

    public String getMontantAccepte() {
        return montantAccepte;
    }

    public String getMontantAPayerInitial() {
        return montantAPayerInitial;
    }

    public String getMontantFacture44() {
        return montantFacture44;
    }

    public String getMontantMensuel() {
        return montantMensuel;
    }

    public String getMontantVerseOAI() {
        return montantVerseOAI;
    }

    public Set<String[]> getMotifsDeRefus() {
        return motifsDeRefus;
    }

    public String getNss() {
        return nss;
    }

    public String getRestitutionCsSource() {
        return restitutionCsSource;
    }

    public String getRestitutionIdDecisionRFMAccordee() {
        return restitutionIdDecisionRFMAccordee;
    }

    public String getRestitutionIdTiersRequerant() {
        return restitutionIdTiersRequerant;
    }

    public boolean getRestitutionIsForcerPaiementParent() {
        return restitutionIsForcerPaiementParent;
    }

    public String getRestitutionMontantAPayeParent() {
        return restitutionMontantAPayeParent;
    }

    public String getRestitutionMontantDepassementQdParent() {
        return restitutionMontantDepassementQdParent;
    }

    public String getStatutDemande() {
        return statutDemande;
    }

    public String getTypeDePaiment() {
        return typeDePaiment;
    }

    public String getTypePCParent() {
        return typePCParent;
    }

    public boolean isConventionne() {
        return isConventionne;
    }

    public boolean isEnfant() {
        return isEnfant;
    }

    public boolean isEnfantExclu() {
        return isEnfantExclu;
    }

    public boolean isForcerPaiement() {
        return isForcerPaiement;
    }

    public boolean isHasSoldeExcedent() {
        return hasSoldeExcedent;
    }

    public boolean isPaiementMensuel() {
        return isPaiementMensuel;
    }

    public boolean isPP() {
        return isPP;
    }

    public void setCodeSousTypeDeSoin(String codeSousTypeDeSoin) {
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
    }

    public void setCodeTypeDeSoin(String codeTypeDeSoin) {
        this.codeTypeDeSoin = codeTypeDeSoin;
    }

    public void setConventionne(boolean isConventionne) {
        this.isConventionne = isConventionne;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setCsSource(String csSource) {
        this.csSource = csSource;
    }

    public void setCsSousTypeDeSoin(String csSousTypeDeSoin) {
        this.csSousTypeDeSoin = csSousTypeDeSoin;
    }

    public void setDateDebutTraitement(String dateDebutTraitement) {
        this.dateDebutTraitement = dateDebutTraitement;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateDemande(String dateDemande) {
        this.dateDemande = dateDemande;
    }

    public void setDateDernierPaiement_mm_yyyy(String dateDernierPaiement_mm_yyyy) {
        this.dateDernierPaiement_mm_yyyy = dateDernierPaiement_mm_yyyy;
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

    public void setEnfant(boolean isEnfant) {
        this.isEnfant = isEnfant;
    }

    public void setEnfantExclu(boolean isEnfantExclu) {
        this.isEnfantExclu = isEnfantExclu;
    }

    public void setForcerPaiement(boolean isForcerPaiement) {
        this.isForcerPaiement = isForcerPaiement;
    }

    public void setHasSoldeExcedent(boolean hasSoldeExcedent) {
        this.hasSoldeExcedent = hasSoldeExcedent;
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdDemandeParent(String idDemandeParent) {
        this.idDemandeParent = idDemandeParent;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdQdAssure(String idQdAssure) {
        this.idQdAssure = idQdAssure;
    }

    public void setIdQdPrincipale(String idQdPrincipale) {
        this.idQdPrincipale = idQdPrincipale;
    }

    public void setIdQdPrincipaleParent(String idQdPrincipaleParent) {
        this.idQdPrincipaleParent = idQdPrincipaleParent;
    }

    public void setIdQdVirtuelle(String idQdVirtuelle) {
        this.idQdVirtuelle = idQdVirtuelle;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsForcerImputation(boolean isForcerImputation) {
        this.isForcerImputation = isForcerImputation;
    }

    public void setMontantAccepte(String montantAccepte) {
        this.montantAccepte = montantAccepte;
    }

    public void setMontantAPayerInitial(String montantAPayerInitial) {
        this.montantAPayerInitial = montantAPayerInitial;
    }

    public void setMontantFacture44(String montantFacture44) {
        this.montantFacture44 = montantFacture44;
    }

    public void setMontantMensuel(String montantMensuel) {
        this.montantMensuel = montantMensuel;
    }

    public void setMontantVerseOAI(String montantVerseOAI) {
        this.montantVerseOAI = montantVerseOAI;
    }

    public void setMotifsDeRefus(Set<String[]> motifsDeRefus) {
        this.motifsDeRefus = motifsDeRefus;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPaiementMensuel(boolean isPaiementMensuel) {
        this.isPaiementMensuel = isPaiementMensuel;
    }

    public void setPP(boolean isPP) {
        this.isPP = isPP;
    }

    public void setRestitutionCsSource(String restitutionCsSource) {
        this.restitutionCsSource = restitutionCsSource;
    }

    public void setRestitutionIdDecisionRFMAccordee(String restitutionIdDecisionRFMAccordee) {
        this.restitutionIdDecisionRFMAccordee = restitutionIdDecisionRFMAccordee;
    }

    public void setRestitutionIdTiersRequerant(String restitutionIdTiersRequerant) {
        this.restitutionIdTiersRequerant = restitutionIdTiersRequerant;
    }

    public void setRestitutionIsForcerPaiementParent(boolean restitutionIsForcerPaiementParent) {
        this.restitutionIsForcerPaiementParent = restitutionIsForcerPaiementParent;
    }

    public void setRestitutionMontantAPayeParent(String restitutionMontantAPayeParent) {
        this.restitutionMontantAPayeParent = restitutionMontantAPayeParent;
    }

    public void setRestitutionMontantDepassementQdParent(String restitutionMontantDepassementQdParent) {
        this.restitutionMontantDepassementQdParent = restitutionMontantDepassementQdParent;
    }

    public void setStatutDemande(String statutDemande) {
        this.statutDemande = statutDemande;
    }

    public void setTypeDePaiment(String typeDePaiment) {
        this.typeDePaiment = typeDePaiment;
    }

    public void setTypePCParent(String typePCParent) {
        this.typePCParent = typePCParent;
    }

    public String getMontantAccepteAvantAdaptation() {
        return montantAccepteAvantAdaptation;
    }

    public void setMontantAccepteAvantAdaptation(String montantAccepteAvantAdaptation) {
        this.montantAccepteAvantAdaptation = montantAccepteAvantAdaptation;
    }

    public String getMontantMensuelAccepteAvantAdaptation() {
        return montantMensuelAccepteAvantAdaptation;
    }

    public void setMontantMensuelAccepteAvantAdaptation(String montantMensuelAccepteAvantAdaptation) {
        this.montantMensuelAccepteAvantAdaptation = montantMensuelAccepteAvantAdaptation;
    }

}