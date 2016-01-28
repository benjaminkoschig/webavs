/*
 * Créé le 04 avril 2010
 */
package globaz.cygnus.services.preparerDecision;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author JJE
 * 
 */
public class RFDecisionData {

    private String anneeQD = "";
    private String codeSousTypeDeSoin = "";
    private String codeTypeDeSoin = "";
    private String dateDebutRetro = "";
    private String dateDernierPaiementRente = "";
    private String dateFinRetro = "";
    private String dateValidation = "";
    private String depassementQd = "0.00"; // Dépassement de la quotité disponible
    private String excedentDeRevenus = "0.00"; // Excedent de revenus selon calcul PC
    private boolean hasDemandeForcerPaiement = false;
    private boolean hasTraiterDecision = false;
    private boolean hasTraiterRestitutions = false;
    private String idAdresseDomicile = "";
    private String idAdressePaiement = "";
    private String idDecision = "";
    private Set<String> idDemandes = new LinkedHashSet<String>();
    private String idExecutionProcess = "";
    // public String datePreparation = "";
    // public String etatDecision = "";
    private String idGestionnaire = "";
    private String idPreparePar = ""; // id gestionnaire
    private String idPrestation = "";
    private String idQdPrincipale = "";
    private String idRequerant = "";
    // Set<String[idDossier,idTiers]
    private Map<String, String[]> idsDossier = new HashMap<String, String[]>();
    private String idValidePar = ""; // id gestionnaire
    private boolean isPaiementMensuel = false; // le paiement mensuel uniquement rétroactif est considéré comme ponctuel
    private String montantARembourserDsas = "";
    private String montantCourantPartieRetro = "";
    private BigDecimal montantCourantPartieRetroDette = null;
    // private BigDecimal montantMensuelPaimentSuivantCourantPartieFuture;
    // private BigDecimal montantMensuelPremierPaimentCourantPartieFuture;
    private String montantMensuel = "";
    private String montantRestitution = "";
    private String montantTotalAPayer = "";
    private BigDecimal montantTotalBigDec = null;
    private String numeroDecision = "";
    private String typeDePaiment = "";
    private Boolean bordereauAccompagnement = Boolean.TRUE;

    public String getAnneeQD() {
        return anneeQD;
    }

    public String getCodeSousTypeDeSoin() {
        return codeSousTypeDeSoin;
    }

    public String getCodeTypeDeSoin() {
        return codeTypeDeSoin;
    }

    public String getDateDebutRetro() {
        return dateDebutRetro;
    }

    public String getDateDernierPaiementRente() {
        return dateDernierPaiementRente;
    }

    public String getDateFinRetro() {
        return dateFinRetro;
    }

    public String getDateValidation() {
        return dateValidation;
    }

    public String getDepassementQd() {
        return depassementQd;
    }

    public String getExcedentDeRevenus() {
        return excedentDeRevenus;
    }

    public String getIdAdresseDomicile() {
        return idAdresseDomicile;
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public Set<String> getIdDemandes() {
        return idDemandes;
    }

    public String getIdExecutionProcess() {
        return idExecutionProcess;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdPreparePar() {
        return idPreparePar;
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    public String getIdQdPrincipale() {
        return idQdPrincipale;
    }

    public String getIdRequerant() {
        return idRequerant;
    }

    public Map<String, String[]> getIdsDossier() {
        return idsDossier;
    }

    public String getIdValidePar() {
        return idValidePar;
    }

    public String getMontantARembourserDsas() {
        return montantARembourserDsas;
    }

    public String getMontantCourantPartieRetro() {
        return montantCourantPartieRetro;
    }

    public BigDecimal getMontantCourantPartieRetroDette() {
        return montantCourantPartieRetroDette;
    }

    public String getMontantMensuel() {
        return montantMensuel;
    }

    public String getMontantRestitution() {
        return montantRestitution;
    }

    public String getMontantTotalAPayer() {
        return montantTotalAPayer;
    }

    public BigDecimal getMontantTotalBigDec() {
        return montantTotalBigDec;
    }

    public String getNumeroDecision() {
        return numeroDecision;
    }

    public String getTypeDePaiment() {
        return typeDePaiment;
    }

    public boolean isHasDemandeForcerPaiement() {
        return hasDemandeForcerPaiement;
    }

    public boolean isHasTraiterDecision() {
        return hasTraiterDecision;
    }

    public boolean isHasTraiterRestitutions() {
        return hasTraiterRestitutions;
    }

    public boolean isPaiementMensuel() {
        return isPaiementMensuel;
    }

    public void setAnneeQD(String anneeQD) {
        this.anneeQD = anneeQD;
    }

    public void setCodeSousTypeDeSoin(String codeSousTypeDeSoin) {
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
    }

    public void setCodeTypeDeSoin(String codeTypeDeSoin) {
        this.codeTypeDeSoin = codeTypeDeSoin;
    }

    public void setDateDebutRetro(String dateDebutRetro) {
        this.dateDebutRetro = dateDebutRetro;
    }

    public void setDateDernierPaiementRente(String dateDernierPaiementRente) {
        this.dateDernierPaiementRente = dateDernierPaiementRente;
    }

    public void setDateFinRetro(String dateFinRetro) {
        this.dateFinRetro = dateFinRetro;
    }

    public void setDateValidation(String dateValidation) {
        this.dateValidation = dateValidation;
    }

    public void setDepassementQd(String depassementQd) {
        this.depassementQd = depassementQd;
    }

    public void setExcedentDeRevenus(String excedentDeRevenus) {
        this.excedentDeRevenus = excedentDeRevenus;
    }

    public void setHasDemandeForcerPaiement(boolean hasDemandeForcerPaiement) {
        this.hasDemandeForcerPaiement = hasDemandeForcerPaiement;
    }

    public void setHasTraiterDecision(boolean hasTraiterDecision) {
        this.hasTraiterDecision = hasTraiterDecision;
    }

    public void setHasTraiterRestitutions(boolean hasTraiterRestitutions) {
        this.hasTraiterRestitutions = hasTraiterRestitutions;
    }

    public void setIdAdresseDomicile(String idAdresseDomicile) {
        this.idAdresseDomicile = idAdresseDomicile;
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDemandes(Set<String> idDemandes) {
        this.idDemandes = idDemandes;
    }

    public void setIdExecutionProcess(String idExecutionProcess) {
        this.idExecutionProcess = idExecutionProcess;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdPreparePar(String idPreparePar) {
        this.idPreparePar = idPreparePar;
    }

    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setIdQdPrincipale(String idQdPrincipale) {
        this.idQdPrincipale = idQdPrincipale;
    }

    public void setIdRequerant(String idRequerant) {
        this.idRequerant = idRequerant;
    }

    public void setIdsDossier(Map<String, String[]> idsDossier) {
        this.idsDossier = idsDossier;
    }

    public void setIdValidePar(String idValidePar) {
        this.idValidePar = idValidePar;
    }

    public void setMontantARembourserDsas(String montantARembourserDsas) {
        this.montantARembourserDsas = montantARembourserDsas;
    }

    public void setMontantCourantPartieRetro(String montantCourantPartieRetro) {
        this.montantCourantPartieRetro = montantCourantPartieRetro;
    }

    public void setMontantCourantPartieRetroDette(BigDecimal montantCourantPartieRetroDette) {
        this.montantCourantPartieRetroDette = montantCourantPartieRetroDette;
    }

    public void setMontantMensuel(String montantMensuelCourantPartieFuture) {
        montantMensuel = montantMensuelCourantPartieFuture;
    }

    public void setMontantRestitution(String montantRestitution) {
        this.montantRestitution = montantRestitution;
    }

    public void setMontantTotalAPayer(String montantTotalAPayer) {
        this.montantTotalAPayer = montantTotalAPayer;
    }

    public void setMontantTotalBigDec(BigDecimal montantTotalBigDec) {
        this.montantTotalBigDec = montantTotalBigDec;
    }

    public void setNumeroDecision(String numeroDecision) {
        this.numeroDecision = numeroDecision;
    }

    public void setPaiementMensuel(boolean isPaiementMensuel) {
        this.isPaiementMensuel = isPaiementMensuel;
    }

    public void setTypeDePaiment(String typeDePaiment) {
        this.typeDePaiment = typeDePaiment;
    }

    public Boolean getBordereauAccompagnement() {
        return bordereauAccompagnement;
    }

    public void setBordereauAccompagnement(Boolean bordereauAccompagnement) {
        this.bordereauAccompagnement = bordereauAccompagnement;
    }

}