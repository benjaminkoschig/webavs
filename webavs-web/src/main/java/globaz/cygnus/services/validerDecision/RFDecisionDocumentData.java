/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.services.validerDecision;

import globaz.cygnus.vb.decisions.RFCopieDecisionsValidationData;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * author fha
 */
public class RFDecisionDocumentData implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresse = "";
    private String anneeQD = "";
    private ArrayList<RFCopieDecisionsValidationData> copieDecision = null;
    private String dateDebutRetro = "";
    private String dateDebutTraitementFormate = "";
    private String dateDecision_JourMoisAnnee = "";
    private String dateDecision_MoisAnnee = "";
    private String dateFinRetro = "";
    private ArrayList<RFDemandeValidationData> decisionDemande = null;
    private String depassementQD = "";
    private String excedentRevenu = "";
    private String genrePrestation = "";
    private String gestionnaire = "";
    private String idDecision = "";
    private String idQdPrincipale = "";
    private String idTiers = "";
    private String idTiersAdressePaiement = "";
    private String idTypeSoin = "";
    private Boolean isBordereauAccompagnement = Boolean.FALSE;
    private Boolean isBulletinVersement = Boolean.FALSE;
    private Boolean isDecompteRetour = Boolean.FALSE;
    private Boolean isPhraseIncitationDepot = Boolean.FALSE;
    private Boolean isPhraseRetourBV = Boolean.FALSE;
    private boolean isRestitution = false;
    private String langueTiers = "";
    private String montantARembourserParLeDsas = "";
    private String montantCourantPartieFuture = "";
    private String montantCourantPartieRetroactive = "";
    private String montantDette = "";
    private String montantSoldeExcedantRevenu = "";
    private String montantTotal = "";
    private String nom = "";
    private String nss = "";
    private String numeroDecision = "";
    private String prenom = "";
    private String referencePaiement = "";
    private String texteAnnexe = "";
    private String texteRemarque = "";
    private String titre = "";
    private String typePaiement = "";

    /**************** Constructor *****************/
    public RFDecisionDocumentData(String idDecision, String idTiers, String gestionnaire, String dateDecision,
            String anneeQD, ArrayList<RFDemandeValidationData> decisionDemande, String texteRemarque,
            String texteAnnexe, ArrayList<RFCopieDecisionsValidationData> copieDecision, Boolean isDecompteRetour,
            Boolean isBulletinVersement, Boolean isBordereauAccompagnement, String excedentRevenu,
            String depassementQD, String montantTotal, String typePaiement, String dateDebutRetro, String dateFinRetro,
            String montantCourantPartieRetroactive, String montantCourantPartieFuture, String numeroDecision,
            String genrePrestation, String idTypeSoin, String referencePaiement, String montantARembourserParLeDsas,
            String idTiersAdressePaiement, String idQdPrincipale, boolean isPhraseIncitationDepot,
            boolean isPhraseRetourBV, String dateSurDocument) {
        this.idDecision = idDecision;
        this.idTiers = idTiers;
        this.gestionnaire = gestionnaire;
        dateDecision_JourMoisAnnee = dateSurDocument;
        this.anneeQD = anneeQD;
        this.decisionDemande = decisionDemande;
        this.texteRemarque = texteRemarque;
        this.texteAnnexe = texteAnnexe;
        this.copieDecision = copieDecision;
        this.isBordereauAccompagnement = isBordereauAccompagnement;
        this.isBulletinVersement = isBulletinVersement;
        this.isDecompteRetour = isDecompteRetour;
        this.excedentRevenu = excedentRevenu;
        this.depassementQD = depassementQD;
        this.montantTotal = montantTotal;
        this.typePaiement = typePaiement;
        this.dateDebutRetro = dateDebutRetro;
        this.dateFinRetro = dateFinRetro;
        this.montantCourantPartieRetroactive = montantCourantPartieRetroactive;
        this.montantCourantPartieFuture = montantCourantPartieFuture;
        this.numeroDecision = numeroDecision;
        this.genrePrestation = genrePrestation;
        this.idTypeSoin = idTypeSoin;
        this.referencePaiement = referencePaiement;
        this.montantARembourserParLeDsas = montantARembourserParLeDsas;
        this.idTiersAdressePaiement = idTiersAdressePaiement;
        this.idQdPrincipale = idQdPrincipale;
        this.isPhraseIncitationDepot = isPhraseIncitationDepot;
        this.isPhraseRetourBV = isPhraseRetourBV;

    }

    public String getAdresse() {
        return adresse;
    }

    public String getAnneeQD() {
        return anneeQD;
    }

    public ArrayList<RFCopieDecisionsValidationData> getCopieDecision() {
        return copieDecision;
    }

    public String getDateDebutRetro() {
        return dateDebutRetro;
    }

    public String getDateDebutTraitementFormate() {
        return dateDebutTraitementFormate;
    }

    public String getDateDecision_JourMoisAnnee() {
        return dateDecision_JourMoisAnnee;
    }

    /**
     * @return the dateDecision_MoisAnnee
     */
    public String getDateDecision_MoisAnnee() {
        return dateDecision_MoisAnnee;
    }

    public String getDateFinRetro() {
        return dateFinRetro;
    }

    public ArrayList<RFDemandeValidationData> getDecisionDemande() {
        return decisionDemande;
    }

    public String getDepassementQD() {
        return depassementQD;
    }

    public String getExcedentRevenu() {
        return excedentRevenu;
    }

    public String getGenrePrestation() {
        return genrePrestation;
    }

    /****************** getters / setters *************/

    public String getGestionnaire() {
        return gestionnaire;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdQdPrincipale() {
        return idQdPrincipale;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public String getIdTypeSoin() {
        return idTypeSoin;
    }

    public Boolean getIsBordereauAccompagnement() {
        return isBordereauAccompagnement;
    }

    public Boolean getIsBulletinVersement() {
        return isBulletinVersement;
    }

    public Boolean getIsDecompteRetour() {
        return isDecompteRetour;
    }

    public Boolean getIsPhraseIncitationDepot() {
        return isPhraseIncitationDepot;
    }

    public Boolean getIsPhraseRetourBV() {
        return isPhraseRetourBV;
    }

    public boolean getIsRestitution() {
        return isRestitution;
    }

    public String getLangueTiers() {
        return langueTiers;
    }

    public String getMontantARembourserParLeDsas() {
        return montantARembourserParLeDsas;
    }

    public String getMontantCourantPartieFuture() {
        return montantCourantPartieFuture;
    }

    public String getMontantCourantPartieRetroactive() {
        return montantCourantPartieRetroactive;
    }

    public String getMontantDette() {
        return montantDette;
    }

    public String getMontantSoldeExcedantRevenu() {
        return montantSoldeExcedantRevenu;
    }

    // il serait bien d'avoir aussi les infos pour le genre de prestation : récupérer le champ genrePCAccordee les QDs?

    public String getMontantTotal() {
        return montantTotal;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public String getNumeroDecision() {
        return numeroDecision;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getReferencePaiement() {
        return referencePaiement;
    }

    public String getTexteAnnexe() {
        return texteAnnexe;
    }

    public String getTexteRemarque() {
        return texteRemarque;
    }

    public String getTitre() {
        return titre;
    }

    public String getTypePaiement() {
        return typePaiement;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setAnneeQD(String anneeQD) {
        this.anneeQD = anneeQD;
    }

    public void setCopieDecision(ArrayList<RFCopieDecisionsValidationData> copieDecision) {
        this.copieDecision = copieDecision;
    }

    public void setDateDebutRetro(String dateDebutRetro) {
        this.dateDebutRetro = dateDebutRetro;
    }

    public void setDateDebutTraitementFormate(String dateDebutTraitementFormate) {
        this.dateDebutTraitementFormate = dateDebutTraitementFormate;
    }

    public void setDateDecision_JourMoisAnnee(String dateDecision) {
        dateDecision_JourMoisAnnee = dateDecision;
    }

    /**
     * @param dateDecision_MoisAnnee
     *            the dateDecision_MoisAnnee to set
     */
    public void setDateDecision_MoisAnnee(String dateDecision_MoisAnnee) {
        this.dateDecision_MoisAnnee = dateDecision_MoisAnnee;
    }

    public void setDateFinRetro(String dateFinRetro) {
        this.dateFinRetro = dateFinRetro;
    }

    public void setDecisionDemande(ArrayList<RFDemandeValidationData> decisionDemande) {
        this.decisionDemande = decisionDemande;
    }

    public void setDepassementQD(String depassementQD) {
        this.depassementQD = depassementQD;
    }

    public void setExcedentRevenu(String excedentRevenu) {
        this.excedentRevenu = excedentRevenu;
    }

    public void setGenrePrestation(String genrePrestation) {
        this.genrePrestation = genrePrestation;
    }

    public void setGestionnaire(String gestionnaire) {
        this.gestionnaire = gestionnaire;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdQdPrincipale(String idQdPrincipale) {
        this.idQdPrincipale = idQdPrincipale;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public void setIdTypeSoin(String idTypeSoin) {
        this.idTypeSoin = idTypeSoin;
    }

    public void setIsBordereauAccompagnement(Boolean isBordereauAccompagnement) {
        this.isBordereauAccompagnement = isBordereauAccompagnement;
    }

    public void setIsBulletinVersement(Boolean isBulletinVersement) {
        this.isBulletinVersement = isBulletinVersement;
    }

    public void setIsDecompteRetour(Boolean isDecompteRetour) {
        this.isDecompteRetour = isDecompteRetour;
    }

    public void setIsPhraseIncitationDepot(Boolean isPhraseIncitationDepot) {
        this.isPhraseIncitationDepot = isPhraseIncitationDepot;
    }

    public void setIsPhraseRetourBV(Boolean isPhraseRetourBV) {
        this.isPhraseRetourBV = isPhraseRetourBV;
    }

    public void setIsRestitution(boolean isRestitution) {
        this.isRestitution = isRestitution;
    }

    public void setLangueTiers(String langueTiers) {
        this.langueTiers = langueTiers;
    }

    public void setMontantARembourserParLeDsas(String montantARembourserParLeDsas) {
        this.montantARembourserParLeDsas = montantARembourserParLeDsas;
    }

    public void setMontantCourantPartieFuture(String montantCourantPartieFuture) {
        this.montantCourantPartieFuture = montantCourantPartieFuture;
    }

    public void setMontantCourantPartieRetroactive(String montantCourantPartieRetroactive) {
        this.montantCourantPartieRetroactive = montantCourantPartieRetroactive;
    }

    public void setMontantDette(String montantDette) {
        this.montantDette = montantDette;
    }

    public void setMontantSoldeExcedantRevenu(String montantSoldeExcedantRevenu) {
        this.montantSoldeExcedantRevenu = montantSoldeExcedantRevenu;
    }

    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setNumeroDecision(String numeroDecision) {
        this.numeroDecision = numeroDecision;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setReferencePaiement(String referencePaiement) {
        this.referencePaiement = referencePaiement;
    }

    public void setTexteAnnexe(String texteAnnexe) {
        this.texteAnnexe = texteAnnexe;
    }

    public void setTexteRemarque(String texteRemarque) {
        this.texteRemarque = texteRemarque;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setTypePaiement(String typePaiement) {
        this.typePaiement = typePaiement;
    }

}
