/**
 * 
 */
package ch.globaz.pegasus.business.models.dessaisissement;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

/**
 * @author ECO
 * 
 */
public class DessaisissementRevenuAuto extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String autreGenreAutreIjApg = null;
    private String csGenreAllocationImpotent = null;
    private String csGenreAutreApi = null;
    private String csGenreAutreIjApg = null;
    private String csGenreAutreRente = null;
    private String csMotifPensionAlimentaire = null;
    private String csTypeAutreApi = null;
    private String csTypeAutreRente = null;
    private String csTypeRenteAllocationImpotent = null;
    private String excedentRenteViagere = null;
    private String idContratEntretienViager = null;
    private String idHomeTaxeJournaliereHome = null;
    private String idTiersPensionAlimentaire = null;
    private String libelleAutresRevenus = null;
    private MembreFamilleEtendu membreFamilleEtendu = null;
    private String MontantActiviteLucrativeDependante = null;
    private String MontantActiviteLucrativeIndependante = null;
    private String montantAllocationImpotent = null;
    private String montantAllocationsFamiliales = null;
    private String montantAutreApi = null;
    private String montantAutreIjApg = null;
    private String montantAutreRente = null;
    private String montantAutresRevenus = null;
    private String montantBetail = null;
    private String montantBienImmobilierNonHabitable = null;
    private String montantBienImmobilierNonPrincipal = null;
    private String montantBienImmobilierPrincipal = null;
    private String montantCapitalLPP = null;
    private String montantCompteBancaireCCP = null;
    private String montantContratEntretienViager = null;
    private String montantJournaliereHome = null;
    private String montantMarchandisesStock = null;
    private String montantNumeraire = null;
    private String montantPensionAlimentaire = null;
    private String montantPretEnversTiers = null;
    private String montantRenteViagere = null;
    private String montantTitre = null;
    private String montantValeurRachatAssuranceVie = null;
    private String montantVehicule = null;
    private String NomEmployeurActiviteLucrativeDependante = null;

    private SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader = null;

    /**
	 * 
	 */
    public DessaisissementRevenuAuto() {
        super();
        simpleDonneeFinanciereHeader = new SimpleDonneeFinanciereHeader();
        membreFamilleEtendu = new MembreFamilleEtendu();
    }

    public String getAutreGenreAutreIjApg() {
        return autreGenreAutreIjApg;
    }

    public String getCsGenreAllocationImpotent() {
        return csGenreAllocationImpotent;
    }

    public String getCsGenreAutreApi() {
        return csGenreAutreApi;
    }

    public String getCsGenreAutreIjApg() {
        return csGenreAutreIjApg;
    }

    public String getCsGenreAutreRente() {
        return csGenreAutreRente;
    }

    /**
     * @return the csMotifPensionAlimentaire
     */
    public String getCsMotifPensionAlimentaire() {
        return csMotifPensionAlimentaire;
    }

    public String getCsTypeAutreApi() {
        return csTypeAutreApi;
    }

    public String getCsTypeAutreRente() {
        return csTypeAutreRente;
    }

    public String getCsTypeRenteAllocationImpotent() {
        return csTypeRenteAllocationImpotent;
    }

    /**
     * @return the excedentRenteViagere
     */
    public String getExcedentRenteViagere() {
        return excedentRenteViagere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleDonneeFinanciereHeader.getId();
    }

    public String getIdContratEntretienViager() {
        return idContratEntretienViager;
    }

    public String getIdHomeTaxeJournaliereHome() {
        return idHomeTaxeJournaliereHome;
    }

    /**
     * @return the idTiersPensionAlimentaire
     */
    public String getIdTiersPensionAlimentaire() {
        return idTiersPensionAlimentaire;
    }

    public String getLibelleAutresRevenus() {
        return libelleAutresRevenus;
    }

    /**
     * @return the membreFamilleEtendu
     */
    public MembreFamilleEtendu getMembreFamilleEtendu() {
        return membreFamilleEtendu;
    }

    /**
     * @return the montantActiviteLucrativeDependante
     */
    public String getMontantActiviteLucrativeDependante() {
        return MontantActiviteLucrativeDependante;
    }

    /**
     * @return the montantActiviteLucrativeIndependante
     */
    public String getMontantActiviteLucrativeIndependante() {
        return MontantActiviteLucrativeIndependante;
    }

    public String getMontantAllocationImpotent() {
        return montantAllocationImpotent;
    }

    /**
     * @return the montantAllocationsFamiliales
     */
    public String getMontantAllocationsFamiliales() {
        return montantAllocationsFamiliales;
    }

    public String getMontantAutreApi() {
        return montantAutreApi;
    }

    public String getMontantAutreIjApg() {
        return montantAutreIjApg;
    }

    public String getMontantAutreRente() {
        return montantAutreRente;
    }

    /**
     * @return the montantAutresRevenus
     */
    public String getMontantAutresRevenus() {
        return montantAutresRevenus;
    }

    /**
     * @return the montantBetail
     */
    public String getMontantBetail() {
        return montantBetail;
    }

    /**
     * @return the montantBienImmobilierNonHabitable
     */
    public String getMontantBienImmobilierNonHabitable() {
        return montantBienImmobilierNonHabitable;
    }

    /**
     * @return the montantBienImmobilierNonPrincipal
     */
    public String getMontantBienImmobilierNonPrincipal() {
        return montantBienImmobilierNonPrincipal;
    }

    /**
     * @return the montantBienImmobilierPrincipal
     */
    public String getMontantBienImmobilierPrincipal() {
        return montantBienImmobilierPrincipal;
    }

    /**
     * @return the montantCapitalLPP
     */
    public String getMontantCapitalLPP() {
        return montantCapitalLPP;
    }

    /**
     * @return the montantCompteBancaireCCP
     */
    public String getMontantCompteBancaireCCP() {
        return montantCompteBancaireCCP;
    }

    /**
     * @return the montantContratEntretienViager
     */
    public String getMontantContratEntretienViager() {
        return montantContratEntretienViager;
    }

    public String getMontantJournaliereHome() {
        return montantJournaliereHome;
    }

    /**
     * @return the montantMarchandisesStock
     */
    public String getMontantMarchandisesStock() {
        return montantMarchandisesStock;
    }

    /**
     * @return the montantNumeraire
     */
    public String getMontantNumeraire() {
        return montantNumeraire;
    }

    /**
     * @return the montantPensionAlimentaire
     */
    public String getMontantPensionAlimentaire() {
        return montantPensionAlimentaire;
    }

    /**
     * @return the montantPretEnversTiers
     */
    public String getMontantPretEnversTiers() {
        return montantPretEnversTiers;
    }

    /**
     * @return the montantRenteViagere
     */
    public String getMontantRenteViagere() {
        return montantRenteViagere;
    }

    /**
     * @return the montantTitre
     */
    public String getMontantTitre() {
        return montantTitre;
    }

    /**
     * @return the montantValeurRachatAssuranceVie
     */
    public String getMontantValeurRachatAssuranceVie() {
        return montantValeurRachatAssuranceVie;
    }

    /**
     * @return the montantVehicule
     */
    public String getMontantVehicule() {
        return montantVehicule;
    }

    /**
     * @return the nomEmployeurActiviteLucrativeDependante
     */
    public String getNomEmployeurActiviteLucrativeDependante() {
        return NomEmployeurActiviteLucrativeDependante;
    }

    /**
     * @return the simpleDessaisissementFortune
     */
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return simpleDonneeFinanciereHeader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleDonneeFinanciereHeader.getSpy();
    }

    public void setAutreGenreAutreIjApg(String autreGenreAutreIjApg) {
        this.autreGenreAutreIjApg = autreGenreAutreIjApg;
    }

    public void setCsGenreAllocationImpotent(String csGenreAllocationImpotent) {
        this.csGenreAllocationImpotent = csGenreAllocationImpotent;
    }

    public void setCsGenreAutreApi(String csGenreAutreApi) {
        this.csGenreAutreApi = csGenreAutreApi;
    }

    public void setCsGenreAutreIjApg(String csGenreAutreIjApg) {
        this.csGenreAutreIjApg = csGenreAutreIjApg;
    }

    public void setCsGenreAutreRente(String csGenreAutreRente) {
        this.csGenreAutreRente = csGenreAutreRente;
    }

    /**
     * @param csMotifPensionAlimentaire
     *            the csMotifPensionAlimentaire to set
     */
    public void setCsMotifPensionAlimentaire(String csMotifPensionAlimentaire) {
        this.csMotifPensionAlimentaire = csMotifPensionAlimentaire;
    }

    public void setCsTypeAutreApi(String csTypeAutreApi) {
        this.csTypeAutreApi = csTypeAutreApi;
    }

    public void setCsTypeAutreRente(String csTypeAutreRente) {
        this.csTypeAutreRente = csTypeAutreRente;
    }

    public void setCsTypeRenteAllocationImpotent(String csTypeRenteAllocationImpotent) {
        this.csTypeRenteAllocationImpotent = csTypeRenteAllocationImpotent;
    }

    /**
     * @param excedentRenteViagere
     *            the excedentRenteViagere to set
     */
    public void setExcedentRenteViagere(String excedentRenteViagere) {
        this.excedentRenteViagere = excedentRenteViagere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleDonneeFinanciereHeader.setId(id);
    }

    public void setIdContratEntretienViager(String idContratEntretienViager) {
        this.idContratEntretienViager = idContratEntretienViager;
    }

    public void setIdHomeTaxeJournaliereHome(String idHomeTaxeJournaliereHome) {
        this.idHomeTaxeJournaliereHome = idHomeTaxeJournaliereHome;
    }

    /**
     * @param idTiersPensionAlimentaire
     *            the idTiersPensionAlimentaire to set
     */
    public void setIdTiersPensionAlimentaire(String idTiersPensionAlimentaire) {
        this.idTiersPensionAlimentaire = idTiersPensionAlimentaire;
    }

    public void setLibelleAutresRevenus(String libelleAutresRevenus) {
        this.libelleAutresRevenus = libelleAutresRevenus;
    }

    /**
     * @param membreFamilleEtendu
     *            the membreFamilleEtendu to set
     */
    public void setMembreFamilleEtendu(MembreFamilleEtendu membreFamilleEtendu) {
        this.membreFamilleEtendu = membreFamilleEtendu;
    }

    /**
     * @param montantActiviteLucrativeDependante
     *            the montantActiviteLucrativeDependante to set
     */
    public void setMontantActiviteLucrativeDependante(String montantActiviteLucrativeDependante) {
        MontantActiviteLucrativeDependante = montantActiviteLucrativeDependante;
    }

    /**
     * @param montantActiviteLucrativeIndependante
     *            the montantActiviteLucrativeIndependante to set
     */
    public void setMontantActiviteLucrativeIndependante(String montantActiviteLucrativeIndependante) {
        MontantActiviteLucrativeIndependante = montantActiviteLucrativeIndependante;
    }

    public void setMontantAllocationImpotent(String montantAllocationImpotent) {
        this.montantAllocationImpotent = montantAllocationImpotent;
    }

    /**
     * @param montantAllocationsFamiliales
     *            the montantAllocationsFamiliales to set
     */
    public void setMontantAllocationsFamiliales(String montantAllocationsFamiliales) {
        this.montantAllocationsFamiliales = montantAllocationsFamiliales;
    }

    public void setMontantAutreApi(String montantAutreApi) {
        this.montantAutreApi = montantAutreApi;
    }

    public void setMontantAutreIjApg(String montantAutreIjApg) {
        this.montantAutreIjApg = montantAutreIjApg;
    }

    public void setMontantAutreRente(String montantAutreRente) {
        this.montantAutreRente = montantAutreRente;
    }

    /**
     * @param montantAutresRevenus
     *            the montantAutresRevenus to set
     */
    public void setMontantAutresRevenus(String montantAutresRevenus) {
        this.montantAutresRevenus = montantAutresRevenus;
    }

    /**
     * @param montantBetail
     *            the montantBetail to set
     */
    public void setMontantBetail(String montantBetail) {
        this.montantBetail = montantBetail;
    }

    /**
     * @param montantBienImmobilierNonHabitable
     *            the montantBienImmobilierNonHabitable to set
     */
    public void setMontantBienImmobilierNonHabitable(String montantBienImmobilierNonHabitable) {
        this.montantBienImmobilierNonHabitable = montantBienImmobilierNonHabitable;
    }

    /**
     * @param montantBienImmobilierNonPrincipal
     *            the montantBienImmobilierNonPrincipal to set
     */
    public void setMontantBienImmobilierNonPrincipal(String montantBienImmobilierNonPrincipal) {
        this.montantBienImmobilierNonPrincipal = montantBienImmobilierNonPrincipal;
    }

    /**
     * @param montantBienImmobilierPrincipal
     *            the montantBienImmobilierPrincipal to set
     */
    public void setMontantBienImmobilierPrincipal(String montantBienImmobilierPrincipal) {
        this.montantBienImmobilierPrincipal = montantBienImmobilierPrincipal;
    }

    /**
     * @param montantCapitalLPP
     *            the montantCapitalLPP to set
     */
    public void setMontantCapitalLPP(String montantCapitalLPP) {
        this.montantCapitalLPP = montantCapitalLPP;
    }

    /**
     * @param montantCompteBancaireCCP
     *            the montantCompteBancaireCCP to set
     */
    public void setMontantCompteBancaireCCP(String montantCompteBancaireCCP) {
        this.montantCompteBancaireCCP = montantCompteBancaireCCP;
    }

    /**
     * @param montantContratEntretienViager
     *            the montantContratEntretienViager to set
     */
    public void setMontantContratEntretienViager(String montantContratEntretienViager) {
        this.montantContratEntretienViager = montantContratEntretienViager;
    }

    public void setMontantJournaliereHome(String montantJournaliereHome) {
        this.montantJournaliereHome = montantJournaliereHome;
    }

    /**
     * @param montantMarchandisesStock
     *            the montantMarchandisesStock to set
     */
    public void setMontantMarchandisesStock(String montantMarchandisesStock) {
        this.montantMarchandisesStock = montantMarchandisesStock;
    }

    /**
     * @param montantNumeraire
     *            the montantNumeraire to set
     */
    public void setMontantNumeraire(String montantNumeraire) {
        this.montantNumeraire = montantNumeraire;
    }

    /**
     * @param montantPensionAlimentaire
     *            the montantPensionAlimentaire to set
     */
    public void setMontantPensionAlimentaire(String montantPensionAlimentaire) {
        this.montantPensionAlimentaire = montantPensionAlimentaire;
    }

    /**
     * @param montantPretEnversTiers
     *            the montantPretEnversTiers to set
     */
    public void setMontantPretEnversTiers(String montantPretEnversTiers) {
        this.montantPretEnversTiers = montantPretEnversTiers;
    }

    /**
     * @param montantRenteViagere
     *            the montantRenteViagere to set
     */
    public void setMontantRenteViagere(String montantRenteViagere) {
        this.montantRenteViagere = montantRenteViagere;
    }

    /**
     * @param montantTitre
     *            the montantTitre to set
     */
    public void setMontantTitre(String montantTitre) {
        this.montantTitre = montantTitre;
    }

    /**
     * @param montantValeurRachatAssuranceVie
     *            the montantValeurRachatAssuranceVie to set
     */
    public void setMontantValeurRachatAssuranceVie(String montantValeurRachatAssuranceVie) {
        this.montantValeurRachatAssuranceVie = montantValeurRachatAssuranceVie;
    }

    /**
     * @param montantVehicule
     *            the montantVehicule to set
     */
    public void setMontantVehicule(String montantVehicule) {
        this.montantVehicule = montantVehicule;
    }

    /**
     * @param nomEmployeurActiviteLucrativeDependante
     *            the nomEmployeurActiviteLucrativeDependante to set
     */
    public void setNomEmployeurActiviteLucrativeDependante(String nomEmployeurActiviteLucrativeDependante) {
        NomEmployeurActiviteLucrativeDependante = nomEmployeurActiviteLucrativeDependante;
    }

    /**
     * @param simpleDessaisissementFortune
     *            the simpleDessaisissementFortune to set
     */
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDessaisissementFortune) {
        simpleDonneeFinanciereHeader = simpleDessaisissementFortune;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleDonneeFinanciereHeader.setSpy(spy);
    }

}
