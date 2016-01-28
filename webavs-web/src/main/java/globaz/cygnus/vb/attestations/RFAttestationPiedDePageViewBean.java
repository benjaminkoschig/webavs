/*
 * Créé le 18 octobre 2010
 */
package globaz.cygnus.vb.attestations;

import globaz.cygnus.db.attestations.RFAttestation;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSpy;

/**
 * author : fha
 */
public class RFAttestationPiedDePageViewBean extends RFAttestationViewBean implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean aAccident = Boolean.FALSE;
    private String aideDureeDeterminee = "";
    private String aideDureeIndeterminee = "";
    private Boolean aideRemunereNecessaire = Boolean.FALSE;
    private Boolean aInvalidite = Boolean.FALSE;

    private Boolean aMaladie = Boolean.FALSE;
    // instance fields for "Maintien à domicile"
    private Boolean aTroubleAge = Boolean.FALSE;
    private String attestationLivraison = "";
    private Boolean autrePersonneAAccident = Boolean.FALSE;
    private Boolean autrePersonneAInvalidite = Boolean.FALSE;
    private Boolean autrePersonneAMaladie = Boolean.FALSE;
    private Boolean autrePersonneATroubleAge = Boolean.FALSE;
    private String autrePersonneDescriptionMotif = "";
    private String autresFraisPourRegime = "";

    // instance fields for "Frais livraison"
    private String centreLocation = "";
    private String commentaire = "";

    private String csGenreTravauxAVS = "";
    private String dateAideDureeDetermineeDesLe = "";
    private String dateAideDureeDetermineeJusqua = "";
    private String dateAideDureeIndetermineeDesLe = "";
    private String dateAideDureeIndetermineeReevaluation = "";

    // instance fields for "Moyens auxiliaires certificat"
    private String dateCertificat = "";
    private String dateDecision = "";
    private String dateDecisionOAI = "";
    private String dateDecisionRefus = "";

    private String dateEntretienTel = "";
    private String dateEnvoiEvaluationCMS = "";
    private String dateEnvoiInfosMedecin = "";
    private String dateFraisLiv = "";
    private String dateMoyAuxB = "";
    // instance fields for "Regime alimentaire"
    private String dateReception = "";

    private String dateRetourEvaluationCMS = "";
    private String dateRetourInfosMedecin = "";
    private String descriptionMotif = "";
    private String dureeAideRemunere = "";
    private String dureeAideRemunereTenueMenage = "";

    private String echeanceRevision = "";
    private String heuresMoisRemunere = "";
    private String idTiers = "";

    // instance fields for "Attestation AVS"
    private String idTiersAVS = "";
    private Boolean isRegimeAccepte = Boolean.FALSE;
    private String lessive = "";
    // instance fields for "Moyens auxiliaires decision"
    private String libelleMoyensAuxDecision = "";
    private String lits = "";
    private String montantMensuelAccepte = "";
    // instance fields for "Moyens auxiliaires bon"
    private String moyenAuxiliaire = "";
    private String nettoyageRangement = "";
    private String nombrePersonneLogement = "";
    private String nombrePieceUtilise = "";
    private String nombreTotalPiece = "";
    private String officePC = "";
    private String raisonPasRepas = "";
    private Boolean recoitRepasCMS = Boolean.FALSE;
    private String remarque = "";
    private Boolean repasDomicileCMS = Boolean.FALSE;
    private String repassage = "";
    private String tauxHoraireAVS = "";

    private String typeRegime = "";

    private String vaisselle = "";
    private String veillesPresence = "";

    public Boolean getaAccident() {
        return aAccident;
    }

    public String getAideDureeDeterminee() {
        return aideDureeDeterminee;
    }

    public String getAideDureeIndeterminee() {
        return aideDureeIndeterminee;
    }

    public Boolean getAideRemunereNecessaire() {
        return aideRemunereNecessaire;
    }

    public Boolean getaInvalidite() {
        return aInvalidite;
    }

    public Boolean getaMaladie() {
        return aMaladie;
    }

    public Boolean getaTroubleAge() {
        return aTroubleAge;
    }

    public String getAttestationLivraison() {
        return attestationLivraison;
    }

    public Boolean getAutrePersonneAAccident() {
        return autrePersonneAAccident;
    }

    public Boolean getAutrePersonneAInvalidite() {
        return autrePersonneAInvalidite;
    }

    public Boolean getAutrePersonneAMaladie() {
        return autrePersonneAMaladie;
    }

    public Boolean getAutrePersonneATroubleAge() {
        return autrePersonneATroubleAge;
    }

    public String getAutrePersonneDescriptionMotif() {
        return autrePersonneDescriptionMotif;
    }

    public String getAutresFraisPourRegime() {
        return autresFraisPourRegime;
    }

    public String getCentreLocation() {
        return centreLocation;
    }

    public String getCommentaire() {
        return commentaire;
    }

    @Override
    public BSpy getCreationSpy() {

        RFAttestation attestation = new RFAttestation();

        try {
            attestation = RFAttestation.loadAttestation(getSession(), getSession().getCurrentThreadTransaction(),
                    getIdAttestation());
        } catch (Exception e) {
        }
        return attestation.getCreationSpy();
    }

    public String getCsGenreTravauxAVS() {
        return csGenreTravauxAVS;
    }

    public String getDateAideDureeDetermineeDesLe() {
        return dateAideDureeDetermineeDesLe;
    }

    public String getDateAideDureeDetermineeJusqua() {
        return dateAideDureeDetermineeJusqua;
    }

    public String getDateAideDureeIndetermineeDesLe() {
        return dateAideDureeIndetermineeDesLe;
    }

    public String getDateAideDureeIndetermineeReevaluation() {
        return dateAideDureeIndetermineeReevaluation;
    }

    public String getDateCertificat() {
        return dateCertificat;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public String getDateDecisionOAI() {
        return dateDecisionOAI;
    }

    public String getDateDecisionRefus() {
        return dateDecisionRefus;
    }

    public String getDateEntretienTel() {
        return dateEntretienTel;
    }

    public String getDateEnvoiEvaluationCMS() {
        return dateEnvoiEvaluationCMS;
    }

    public String getDateEnvoiInfosMedecin() {
        return dateEnvoiInfosMedecin;
    }

    public String getDateFraisLiv() {
        return dateFraisLiv;
    }

    public String getDateMoyAuxB() {
        return dateMoyAuxB;
    }

    // methods
    public String getDateReception() {
        return dateReception;
    }

    public String getDateRetourEvaluationCMS() {
        return dateRetourEvaluationCMS;
    }

    public String getDateRetourInfosMedecin() {
        return dateRetourInfosMedecin;
    }

    public String getDescriptionMotif() {
        return descriptionMotif;
    }

    public String getDureeAideRemunere() {
        return dureeAideRemunere;
    }

    public String getDureeAideRemunereTenueMenage() {
        return dureeAideRemunereTenueMenage;
    }

    public String getEcheanceRevision() {
        return echeanceRevision;
    }

    public String getHeuresMoisRemunere() {
        return heuresMoisRemunere;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersAVS() {
        return idTiersAVS;
    }

    public Boolean getIsRegimeAccepte() {
        return isRegimeAccepte;
    }

    public String getLessive() {
        return lessive;
    }

    public String getLibelleMoyensAuxDecision() {
        return libelleMoyensAuxDecision;
    }

    public String getLits() {
        return lits;
    }

    public String getMontantMensuelAccepte() {
        return montantMensuelAccepte;
    }

    public String getMoyenAuxiliaire() {
        return moyenAuxiliaire;
    }

    public String getNettoyageRangement() {
        return nettoyageRangement;
    }

    public String getNombrePersonneLogement() {
        return nombrePersonneLogement;
    }

    public String getNombrePieceUtilise() {
        return nombrePieceUtilise;
    }

    public String getNombreTotalPiece() {
        return nombreTotalPiece;
    }

    public String getOfficePC() {
        return officePC;
    }

    public String getRaisonPasRepas() {
        return raisonPasRepas;
    }

    public Boolean getRecoitRepasCMS() {
        return recoitRepasCMS;
    }

    public String getRemarque() {
        return remarque;
    }

    public Boolean getRepasDomicileCMS() {
        return repasDomicileCMS;
    }

    public String getRepassage() {
        return repassage;
    }

    @Override
    public BSpy getSpy() {

        RFAttestation attestation = new RFAttestation();

        try {
            attestation = RFAttestation.loadAttestation(getSession(), getSession().getCurrentThreadTransaction(),
                    getIdAttestation());
        } catch (Exception e) {
        }
        return attestation.getSpy();
    }

    public String getTauxHoraireAVS() {
        return tauxHoraireAVS;
    }

    public String getTypeRegime() {
        return typeRegime;
    }

    public String getVaisselle() {
        return vaisselle;
    }

    public String getVeillesPresence() {
        return veillesPresence;
    }

    public void setaAccident(Boolean aAccident) {
        this.aAccident = aAccident;
    }

    public void setAideDureeDeterminee(String aideDureeDeterminee) {
        this.aideDureeDeterminee = aideDureeDeterminee;
    }

    public void setAideDureeIndeterminee(String aideDureeIndeterminee) {
        this.aideDureeIndeterminee = aideDureeIndeterminee;
    }

    public void setAideRemunereNecessaire(Boolean aideRemunereNecessaire) {
        this.aideRemunereNecessaire = aideRemunereNecessaire;
    }

    public void setaInvalidite(Boolean aInvalidite) {
        this.aInvalidite = aInvalidite;
    }

    public void setaMaladie(Boolean aMaladie) {
        this.aMaladie = aMaladie;
    }

    public void setaTroubleAge(Boolean aTroubleAge) {
        this.aTroubleAge = aTroubleAge;
    }

    public void setAttestationLivraison(String attestationLivraison) {
        this.attestationLivraison = attestationLivraison;
    }

    public void setAutrePersonneAAccident(Boolean autrePersonneAAccident) {
        this.autrePersonneAAccident = autrePersonneAAccident;
    }

    public void setAutrePersonneAInvalidite(Boolean autrePersonneAInvalidite) {
        this.autrePersonneAInvalidite = autrePersonneAInvalidite;
    }

    public void setAutrePersonneAMaladie(Boolean autrePersonneAMaladie) {
        this.autrePersonneAMaladie = autrePersonneAMaladie;
    }

    public void setAutrePersonneATroubleAge(Boolean autrePersonneATroubleAge) {
        this.autrePersonneATroubleAge = autrePersonneATroubleAge;
    }

    public void setAutrePersonneDescriptionMotif(String autrePersonneDescriptionMotif) {
        this.autrePersonneDescriptionMotif = autrePersonneDescriptionMotif;
    }

    public void setAutresFraisPourRegime(String autresFraisPourRegime) {
        this.autresFraisPourRegime = autresFraisPourRegime;
    }

    public void setCentreLocation(String centreLocation) {
        this.centreLocation = centreLocation;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public void setCsGenreTravauxAVS(String csGenreTravauxAVS) {
        this.csGenreTravauxAVS = csGenreTravauxAVS;
    }

    public void setDateAideDureeDetermineeDesLe(String dateAideDureeDetermineeDesLe) {
        this.dateAideDureeDetermineeDesLe = dateAideDureeDetermineeDesLe;
    }

    public void setDateAideDureeDetermineeJusqua(String dateAideDureeDetermineeJusqua) {
        this.dateAideDureeDetermineeJusqua = dateAideDureeDetermineeJusqua;
    }

    public void setDateAideDureeIndetermineeDesLe(String dateAideDureeIndetermineeDesLe) {
        this.dateAideDureeIndetermineeDesLe = dateAideDureeIndetermineeDesLe;
    }

    public void setDateAideDureeIndetermineeReevaluation(String dateAideDureeIndetermineeReevaluation) {
        this.dateAideDureeIndetermineeReevaluation = dateAideDureeIndetermineeReevaluation;
    }

    public void setDateCertificat(String dateCertificat) {
        this.dateCertificat = dateCertificat;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public void setDateDecisionOAI(String dateDecisionOAI) {
        this.dateDecisionOAI = dateDecisionOAI;
    }

    public void setDateDecisionRefus(String dateDecisionRefus) {
        this.dateDecisionRefus = dateDecisionRefus;
    }

    public void setDateEntretienTel(String dateEntretienTel) {
        this.dateEntretienTel = dateEntretienTel;
    }

    public void setDateEnvoiEvaluationCMS(String dateEnvoiEvaluationCMS) {
        this.dateEnvoiEvaluationCMS = dateEnvoiEvaluationCMS;
    }

    public void setDateEnvoiInfosMedecin(String dateEnvoiInfosMedecin) {
        this.dateEnvoiInfosMedecin = dateEnvoiInfosMedecin;
    }

    public void setDateFraisLiv(String dateFraisLiv) {
        this.dateFraisLiv = dateFraisLiv;
    }

    public void setDateMoyAuxB(String dateMoyAuxB) {
        this.dateMoyAuxB = dateMoyAuxB;
    }

    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    public void setDateRetourEvaluationCMS(String dateRetourEvaluationCMS) {
        this.dateRetourEvaluationCMS = dateRetourEvaluationCMS;
    }

    public void setDateRetourInfosMedecin(String dateRetourInfosMedecin) {
        this.dateRetourInfosMedecin = dateRetourInfosMedecin;
    }

    public void setDescriptionMotif(String descriptionMotif) {
        this.descriptionMotif = descriptionMotif;
    }

    public void setDureeAideRemunere(String dureeAideRemunere) {
        this.dureeAideRemunere = dureeAideRemunere;
    }

    public void setDureeAideRemunereTenueMenage(String dureeAideRemunereTenueMenage) {
        this.dureeAideRemunereTenueMenage = dureeAideRemunereTenueMenage;
    }

    public void setEcheanceRevision(String echeanceRevision) {
        this.echeanceRevision = echeanceRevision;
    }

    public void setHeuresMoisRemunere(String heuresMoisRemunere) {
        this.heuresMoisRemunere = heuresMoisRemunere;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersAVS(String idTiersAVS) {
        this.idTiersAVS = idTiersAVS;
    }

    public void setIsRegimeAccepte(Boolean isRegimeAccepte) {
        this.isRegimeAccepte = isRegimeAccepte;
    }

    public void setLessive(String lessive) {
        this.lessive = lessive;
    }

    public void setLibelleMoyensAuxDecision(String libelleMoyensAuxDecision) {
        this.libelleMoyensAuxDecision = libelleMoyensAuxDecision;
    }

    public void setLits(String lits) {
        this.lits = lits;
    }

    public void setMontantMensuelAccepte(String montantMensuelAccepte) {
        this.montantMensuelAccepte = montantMensuelAccepte;
    }

    public void setMoyenAuxiliaire(String moyenAuxiliaire) {
        this.moyenAuxiliaire = moyenAuxiliaire;
    }

    public void setNettoyageRangement(String nettoyageRangement) {
        this.nettoyageRangement = nettoyageRangement;
    }

    public void setNombrePersonneLogement(String nombrePersonneLogement) {
        this.nombrePersonneLogement = nombrePersonneLogement;
    }

    public void setNombrePieceUtilise(String nombrePieceUtilise) {
        this.nombrePieceUtilise = nombrePieceUtilise;
    }

    public void setNombreTotalPiece(String nombreTotalPiece) {
        this.nombreTotalPiece = nombreTotalPiece;
    }

    public void setOfficePC(String officePC) {
        this.officePC = officePC;
    }

    public void setRaisonPasRepas(String raisonPasRepas) {
        this.raisonPasRepas = raisonPasRepas;
    }

    public void setRecoitRepasCMS(Boolean recoitRepasCMS) {
        this.recoitRepasCMS = recoitRepasCMS;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public void setRepasDomicileCMS(Boolean repasDomicileCMS) {
        this.repasDomicileCMS = repasDomicileCMS;
    }

    public void setRepassage(String repassage) {
        this.repassage = repassage;
    }

    public void setTauxHoraireAVS(String tauxHoraireAVS) {
        this.tauxHoraireAVS = tauxHoraireAVS;
    }

    public void setTypeRegime(String typeRegime) {
        this.typeRegime = typeRegime;
    }

    public void setVaisselle(String vaisselle) {
        this.vaisselle = vaisselle;
    }

    public void setVeillesPresence(String veillesPresence) {
        this.veillesPresence = veillesPresence;
    }

}
