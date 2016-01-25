/**
 * 
 */
package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

/**
 * @author eco
 */
public class AnnonceLapramsMediatorDonneeFinanciere extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String activiteLucrativeDependanteRevenuMontant = null;
    private String activiteLucrativeIndependanteRevenuMontant = null;
    private String allocationImpotentRevenu = null;
    private String allocationsFamilialesMontantMensuel = null;
    private String assuranceRenteViagereExcedent = null;
    private String assuranceRenteViagereMontant = null;
    private String assuranceRenteViagereMontantValeurRachat = null;
    private String assuranceVieMontant = null;
    private String autreApiRevenu = null;
    private String autreFortuneMobiliereCsTypePropriete = null;
    private String autreFortuneMobiliereDenominateur = null;
    private String autreFortuneMobiliereMontant = null;
    private String autreFortuneMobiliereNumerateur = null;
    private String autreRenteRevenu = null;
    private String autresDettesProuveesMontantDette = null;
    private String autresRevenusMontant = null;
    private String betailCsTypePropriete = null;
    private String betailDenominateur = null;
    private String betailMontant = null;
    private String betailNumerateur = null;
    private String bienImmobilierHabitationNonPrincipaleMontantDetteHypothecaire = null;
    private String bienImmobilierHabitationNonPrincipaleMontantInteretHypothecaire = null;
    private String bienImmobilierHabitationNonPrincipaleMontantLoyersEncaisses = null;
    private String bienImmobilierHabitationNonPrincipaleMontantSousLocation = null;
    private String bienImmobilierHabitationNonPrincipaleMontantValeurLocative = null;
    private String bienImmobilierHabitationNonPrincipaleValeurVenale = null;
    private String bienImmobilierNonHabitableMontantDetteHypothecaire = null;
    private String bienImmobilierNonHabitableMontantInteretHypothecaire = null;
    private String bienImmobilierNonHabitableMontantRendement = null;
    private String bienImmobilierNonHabitableValeurVenale = null;
    private String bienImmobilierServantHabitationPrincipaleMontantDetteHypothecaire = null;
    private String bienImmobilierServantHabitationPrincipaleMontantInteretHypothecaire = null;
    private String bienImmobilierServantHabitationPrincipaleMontantLoyersEncaisses = null;
    private String bienImmobilierServantHabitationPrincipaleMontantSousLocation = null;
    private String bienImmobilierServantHabitationPrincipaleMontantValeurFiscale = null;
    private String bienImmobilierServantHabitationPrincipaleMontantValeurLocative = null;
    private String capitalLPPCsTypePropriete = null;
    private String capitalLPPDenominateur = null;
    private String capitalLPPMontant = null;
    private String capitalLPPMontantInteret = null;
    private String capitalLPPNumerateur = null;
    private String compteBancaireCCPCsTypePropriete = null;
    private String compteBancaireCCPDenominateur = null;
    private String compteBancaireCCPMontant = null;
    private String compteBancaireCCPMontantInteret = null;
    private String compteBancaireCCPNumerateur = null;
    private String contratEntretienViagerMontant = null;
    private String cotisationsPsalMontantCotisationsAnnuelles = null;
    private String csRoleMembreFamille = null;
    private String csTypeDonneeFinanciere = null;
    private String dateDebutDonneeFinanciere = null;
    private String dessaisissementFortuneDeductions = null;
    private String dessaisissementFortuneMontantBrut = null;
    private String dessaisissementRevenuDeductions = null;
    private String dessaisissementRevenuMontantBrut = null;
    private String idTaxeJournaliereHome = null;
    private String ijApgCotisationLPPMensC = null;
    private String ijApgGainIntAnnuel = null;
    private String ijApgMontant = null;
    private String ijApgMontantBrutAC = null;
    private String ijApgNbJours = null;
    private String ijApgTauxAA = null;
    private String ijApgTauxAVS = null;
    private String indemniteJournaliereAiNbjours = null;
    private String indemniteJournaliereAiRevenu = null;
    private String loyerCharges = null;
    private String loyerCsTypeLoyer = null;
    private String loyerMontantNet = null;
    private String marchandisesStockCsTypePropriete = null;
    private String marchandisesStockDenominateur = null;
    private String marchandisesStockMontant = null;
    private String marchandisesStockNumerateur = null;
    private MembreFamilleEtendu membreFamilleEtendu = null;
    private String numeraireCsTypePropriete = null;
    private String numeraireDenominateur = null;
    private String numeraireMontant = null;
    private String numeraireMontantInteret = null;
    private String numeraireNumerateur = null;
    private String pensionAlimentaireCsTypePension = null;
    private String pensionAlimentaireMontant = null;
    private String pretEnversTiersCsTypePropriete = null;
    private String pretEnversTiersDenominateur = null;
    private String pretEnversTiersMontant = null;
    private String pretEnversTiersMontantInteret = null;
    private String pretEnversTiersNumerateur = null;
    private String renteAvsAiMontant = null;
    private String revenuHypothetiqueMontantBrut = null;
    private String revenuHypothetiqueMontantNet = null;
    private SimpleAnnonceLapramsDonneeFinanciereHeader simpleAnnonceLapramsDonneeFinanciereHeader = null;
    private SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader = null;
    private String taxeJournaliereHomeCsDestinationSortie = null;
    private String taxeJournaliereHomeCsTypeAdressePmt = null;
    private String taxeJournaliereHomeDateEntreeHome = null;
    private String taxeJournaliereHomeIdTypeChambre = null;
    private Boolean taxeJournaliereHomeIsPartLCA = null;
    private String taxeJournaliereHomePartLCA = null;
    private String taxeJournaliereHomePimeAPayerLCA = null;
    private String titreCsTypePropriete = null;
    private String titreDenominateur = null;
    private String titreMontant = null;
    private String titreNumerateur = null;
    private String titreRendement = null;
    private String vehiculeCsTypePropriete = null;
    private String vehiculeDenominateur = null;
    private String vehiculeMontant = null;
    private String vehiculeNumerateur = null;

    public AnnonceLapramsMediatorDonneeFinanciere() {
        super();
        membreFamilleEtendu = new MembreFamilleEtendu();
        simpleAnnonceLapramsDonneeFinanciereHeader = new SimpleAnnonceLapramsDonneeFinanciereHeader();
        simpleDonneeFinanciereHeader = new SimpleDonneeFinanciereHeader();
    }

    public String getActiviteLucrativeDependanteRevenuMontant() {
        return activiteLucrativeDependanteRevenuMontant;
    }

    public String getActiviteLucrativeIndependanteRevenuMontant() {
        return activiteLucrativeIndependanteRevenuMontant;
    }

    public String getAllocationImpotentRevenu() {
        return allocationImpotentRevenu;
    }

    public String getAllocationsFamilialesMontantMensuel() {
        return allocationsFamilialesMontantMensuel;
    }

    public String getAssuranceRenteViagereExcedent() {
        return assuranceRenteViagereExcedent;
    }

    public String getAssuranceRenteViagereMontant() {
        return assuranceRenteViagereMontant;
    }

    public String getAssuranceRenteViagereMontantValeurRachat() {
        return assuranceRenteViagereMontantValeurRachat;
    }

    public String getAssuranceVieMontant() {
        return assuranceVieMontant;
    }

    public String getAutreApiRevenu() {
        return autreApiRevenu;
    }

    public String getAutreFortuneMobiliereCsTypePropriete() {
        return autreFortuneMobiliereCsTypePropriete;
    }

    public String getAutreFortuneMobiliereDenominateur() {
        return autreFortuneMobiliereDenominateur;
    }

    public String getAutreFortuneMobiliereMontant() {
        return autreFortuneMobiliereMontant;
    }

    public String getAutreFortuneMobiliereNumerateur() {
        return autreFortuneMobiliereNumerateur;
    }

    public String getAutreRenteRevenu() {
        return autreRenteRevenu;
    }

    public String getAutresDettesProuveesMontantDette() {
        return autresDettesProuveesMontantDette;
    }

    public String getAutresRevenusMontant() {
        return autresRevenusMontant;
    }

    public String getBetailCsTypePropriete() {
        return betailCsTypePropriete;
    }

    public String getBetailDenominateur() {
        return betailDenominateur;
    }

    public String getBetailMontant() {
        return betailMontant;
    }

    public String getBetailNumerateur() {
        return betailNumerateur;
    }

    public String getBienImmobilierHabitationNonPrincipaleMontantDetteHypothecaire() {
        return bienImmobilierHabitationNonPrincipaleMontantDetteHypothecaire;
    }

    public String getBienImmobilierHabitationNonPrincipaleMontantInteretHypothecaire() {
        return bienImmobilierHabitationNonPrincipaleMontantInteretHypothecaire;
    }

    public String getBienImmobilierHabitationNonPrincipaleMontantLoyersEncaisses() {
        return bienImmobilierHabitationNonPrincipaleMontantLoyersEncaisses;
    }

    public String getBienImmobilierHabitationNonPrincipaleMontantSousLocation() {
        return bienImmobilierHabitationNonPrincipaleMontantSousLocation;
    }

    public String getBienImmobilierHabitationNonPrincipaleMontantValeurLocative() {
        return bienImmobilierHabitationNonPrincipaleMontantValeurLocative;
    }

    public String getBienImmobilierHabitationNonPrincipaleValeurVenale() {
        return bienImmobilierHabitationNonPrincipaleValeurVenale;
    }

    public String getBienImmobilierNonHabitableMontantDetteHypothecaire() {
        return bienImmobilierNonHabitableMontantDetteHypothecaire;
    }

    public String getBienImmobilierNonHabitableMontantInteretHypothecaire() {
        return bienImmobilierNonHabitableMontantInteretHypothecaire;
    }

    public String getBienImmobilierNonHabitableMontantRendement() {
        return bienImmobilierNonHabitableMontantRendement;
    }

    public String getBienImmobilierNonHabitableValeurVenale() {
        return bienImmobilierNonHabitableValeurVenale;
    }

    public String getBienImmobilierServantHabitationPrincipaleMontantDetteHypothecaire() {
        return bienImmobilierServantHabitationPrincipaleMontantDetteHypothecaire;
    }

    public String getBienImmobilierServantHabitationPrincipaleMontantInteretHypothecaire() {
        return bienImmobilierServantHabitationPrincipaleMontantInteretHypothecaire;
    }

    public String getBienImmobilierServantHabitationPrincipaleMontantLoyersEncaisses() {
        return bienImmobilierServantHabitationPrincipaleMontantLoyersEncaisses;
    }

    public String getBienImmobilierServantHabitationPrincipaleMontantSousLocation() {
        return bienImmobilierServantHabitationPrincipaleMontantSousLocation;
    }

    public String getBienImmobilierServantHabitationPrincipaleMontantValeurFiscale() {
        return bienImmobilierServantHabitationPrincipaleMontantValeurFiscale;
    }

    public String getBienImmobilierServantHabitationPrincipaleMontantValeurLocative() {
        return bienImmobilierServantHabitationPrincipaleMontantValeurLocative;
    }

    public String getCapitalLPPCsTypePropriete() {
        return capitalLPPCsTypePropriete;
    }

    public String getCapitalLPPDenominateur() {
        return capitalLPPDenominateur;
    }

    public String getCapitalLPPMontant() {
        return capitalLPPMontant;
    }

    public String getCapitalLPPMontantInteret() {
        return capitalLPPMontantInteret;
    }

    public String getCapitalLPPNumerateur() {
        return capitalLPPNumerateur;
    }

    public String getCompteBancaireCCPCsTypePropriete() {
        return compteBancaireCCPCsTypePropriete;
    }

    public String getCompteBancaireCCPDenominateur() {
        return compteBancaireCCPDenominateur;
    }

    public String getCompteBancaireCCPMontant() {
        return compteBancaireCCPMontant;
    }

    public String getCompteBancaireCCPMontantInteret() {
        return compteBancaireCCPMontantInteret;
    }

    public String getCompteBancaireCCPNumerateur() {
        return compteBancaireCCPNumerateur;
    }

    public String getContratEntretienViagerMontant() {
        return contratEntretienViagerMontant;
    }

    public String getCotisationsPsalMontantCotisationsAnnuelles() {
        return cotisationsPsalMontantCotisationsAnnuelles;
    }

    public String getCsRoleMembreFamille() {
        return csRoleMembreFamille;
    }

    public String getCsTypeDonneeFinanciere() {
        return csTypeDonneeFinanciere;
    }

    public String getDateDebutDonneeFinanciere() {
        return dateDebutDonneeFinanciere;
    }

    public String getDessaisissementFortuneDeductions() {
        return dessaisissementFortuneDeductions;
    }

    public String getDessaisissementFortuneMontantBrut() {
        return dessaisissementFortuneMontantBrut;
    }

    public String getDessaisissementRevenuDeductions() {
        return dessaisissementRevenuDeductions;
    }

    public String getDessaisissementRevenuMontantBrut() {
        return dessaisissementRevenuMontantBrut;
    }

    @Override
    public String getId() {
        return simpleAnnonceLapramsDonneeFinanciereHeader.getId();
    }

    public String getIdTaxeJournaliereHome() {
        return idTaxeJournaliereHome;
    }

    public String getIjApgCotisationLPPMensC() {
        return ijApgCotisationLPPMensC;
    }

    public String getIjApgGainIntAnnuel() {
        return ijApgGainIntAnnuel;
    }

    public String getIjApgMontant() {
        return ijApgMontant;
    }

    public String getIjApgMontantBrutAC() {
        return ijApgMontantBrutAC;
    }

    public String getIjApgNbJours() {
        return ijApgNbJours;
    }

    public String getIjApgTauxAA() {
        return ijApgTauxAA;
    }

    public String getIjApgTauxAVS() {
        return ijApgTauxAVS;
    }

    public String getIndemniteJournaliereAiNbjours() {
        return indemniteJournaliereAiNbjours;
    }

    public String getIndemniteJournaliereAiRevenu() {
        return indemniteJournaliereAiRevenu;
    }

    public String getLoyerCharges() {
        return loyerCharges;
    }

    public String getLoyerCsTypeLoyer() {
        return loyerCsTypeLoyer;
    }

    public String getLoyerMontantNet() {
        return loyerMontantNet;
    }

    public String getMarchandisesStockCsTypePropriete() {
        return marchandisesStockCsTypePropriete;
    }

    public String getMarchandisesStockDenominateur() {
        return marchandisesStockDenominateur;
    }

    public String getMarchandisesStockMontant() {
        return marchandisesStockMontant;
    }

    public String getMarchandisesStockNumerateur() {
        return marchandisesStockNumerateur;
    }

    public MembreFamilleEtendu getMembreFamilleEtendu() {
        return membreFamilleEtendu;
    }

    public String getNumeraireCsTypePropriete() {
        return numeraireCsTypePropriete;
    }

    public String getNumeraireDenominateur() {
        return numeraireDenominateur;
    }

    public String getNumeraireMontant() {
        return numeraireMontant;
    }

    public String getNumeraireMontantInteret() {
        return numeraireMontantInteret;
    }

    public String getNumeraireNumerateur() {
        return numeraireNumerateur;
    }

    public String getPensionAlimentaireCsTypePension() {
        return pensionAlimentaireCsTypePension;
    }

    public String getPensionAlimentaireMontant() {
        return pensionAlimentaireMontant;
    }

    public String getPretEnversTiersCsTypePropriete() {
        return pretEnversTiersCsTypePropriete;
    }

    public String getPretEnversTiersDenominateur() {
        return pretEnversTiersDenominateur;
    }

    public String getPretEnversTiersMontant() {
        return pretEnversTiersMontant;
    }

    public String getPretEnversTiersMontantInteret() {
        return pretEnversTiersMontantInteret;
    }

    public String getPretEnversTiersNumerateur() {
        return pretEnversTiersNumerateur;
    }

    public String getRenteAvsAiMontant() {
        return renteAvsAiMontant;
    }

    public String getRevenuHypothetiqueMontantBrut() {
        return revenuHypothetiqueMontantBrut;
    }

    public String getRevenuHypothetiqueMontantNet() {
        return revenuHypothetiqueMontantNet;
    }

    public SimpleAnnonceLapramsDonneeFinanciereHeader getSimpleAnnonceLapramsDonneeFinanciereHeader() {
        return simpleAnnonceLapramsDonneeFinanciereHeader;
    }

    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return simpleDonneeFinanciereHeader;
    }

    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getTaxeJournaliereHomeCsDestinationSortie() {
        return taxeJournaliereHomeCsDestinationSortie;
    }

    public String getTaxeJournaliereHomeCsTypeAdressePmt() {
        return taxeJournaliereHomeCsTypeAdressePmt;
    }

    public String getTaxeJournaliereHomeDateEntreeHome() {
        return taxeJournaliereHomeDateEntreeHome;
    }

    public String getTaxeJournaliereHomeIdTypeChambre() {
        return taxeJournaliereHomeIdTypeChambre;
    }

    public Boolean getTaxeJournaliereHomeIsPartLCA() {
        return taxeJournaliereHomeIsPartLCA;
    }

    public String getTaxeJournaliereHomePartLCA() {
        return taxeJournaliereHomePartLCA;
    }

    public String getTaxeJournaliereHomePimeAPayerLCA() {
        return taxeJournaliereHomePimeAPayerLCA;
    }

    public String getTitreCsTypePropriete() {
        return titreCsTypePropriete;
    }

    public String getTitreDenominateur() {
        return titreDenominateur;
    }

    public String getTitreMontant() {
        return titreMontant;
    }

    public String getTitreNumerateur() {
        return titreNumerateur;
    }

    public String getTitreRendement() {
        return titreRendement;
    }

    public String getVehiculeCsTypePropriete() {
        return vehiculeCsTypePropriete;
    }

    public String getVehiculeDenominateur() {
        return vehiculeDenominateur;
    }

    public String getVehiculeMontant() {
        return vehiculeMontant;
    }

    public String getVehiculeNumerateur() {
        return vehiculeNumerateur;
    }

    public void setActiviteLucrativeDependanteRevenuMontant(String activiteLucrativeDependanteRevenuMontant) {
        this.activiteLucrativeDependanteRevenuMontant = activiteLucrativeDependanteRevenuMontant;
    }

    public void setActiviteLucrativeIndependanteRevenuMontant(String activiteLucrativeIndependanteRevenuMontant) {
        this.activiteLucrativeIndependanteRevenuMontant = activiteLucrativeIndependanteRevenuMontant;
    }

    public void setAllocationImpotentRevenu(String allocationImpotentRevenu) {
        this.allocationImpotentRevenu = allocationImpotentRevenu;
    }

    public void setAllocationsFamilialesMontantMensuel(String allocationsFamilialesMontantMensuel) {
        this.allocationsFamilialesMontantMensuel = allocationsFamilialesMontantMensuel;
    }

    public void setAssuranceRenteViagereExcedent(String assuranceRenteViagereExcedent) {
        this.assuranceRenteViagereExcedent = assuranceRenteViagereExcedent;
    }

    public void setAssuranceRenteViagereMontant(String assuranceRenteViagereMontant) {
        this.assuranceRenteViagereMontant = assuranceRenteViagereMontant;
    }

    public void setAssuranceRenteViagereMontantValeurRachat(String assuranceRenteViagereMontantValeurRachat) {
        this.assuranceRenteViagereMontantValeurRachat = assuranceRenteViagereMontantValeurRachat;
    }

    public void setAssuranceVieMontant(String assuranceVieMontant) {
        this.assuranceVieMontant = assuranceVieMontant;
    }

    public void setAutreApiRevenu(String autreApiRevenu) {
        this.autreApiRevenu = autreApiRevenu;
    }

    public void setAutreFortuneMobiliereCsTypePropriete(String autreFortuneMobiliereCsTypePropriete) {
        this.autreFortuneMobiliereCsTypePropriete = autreFortuneMobiliereCsTypePropriete;
    }

    public void setAutreFortuneMobiliereDenominateur(String autreFortuneMobiliereDenominateur) {
        this.autreFortuneMobiliereDenominateur = autreFortuneMobiliereDenominateur;
    }

    public void setAutreFortuneMobiliereMontant(String autreFortuneMobiliereMontant) {
        this.autreFortuneMobiliereMontant = autreFortuneMobiliereMontant;
    }

    public void setAutreFortuneMobiliereNumerateur(String autreFortuneMobiliereNumerateur) {
        this.autreFortuneMobiliereNumerateur = autreFortuneMobiliereNumerateur;
    }

    public void setAutreRenteRevenu(String autreRenteRevenu) {
        this.autreRenteRevenu = autreRenteRevenu;
    }

    public void setAutresDettesProuveesMontantDette(String autresDettesProuveesMontantDette) {
        this.autresDettesProuveesMontantDette = autresDettesProuveesMontantDette;
    }

    public void setAutresRevenusMontant(String autresRevenusMontant) {
        this.autresRevenusMontant = autresRevenusMontant;
    }

    public void setBetailCsTypePropriete(String betailCsTypePropriete) {
        this.betailCsTypePropriete = betailCsTypePropriete;
    }

    public void setBetailDenominateur(String betailDenominateur) {
        this.betailDenominateur = betailDenominateur;
    }

    public void setBetailMontant(String betailMontant) {
        this.betailMontant = betailMontant;
    }

    public void setBetailNumerateur(String betailNumerateur) {
        this.betailNumerateur = betailNumerateur;
    }

    public void setBienImmobilierHabitationNonPrincipaleMontantDetteHypothecaire(
            String bienImmobilierHabitationNonPrincipaleMontantDetteHypothecaire) {
        this.bienImmobilierHabitationNonPrincipaleMontantDetteHypothecaire = bienImmobilierHabitationNonPrincipaleMontantDetteHypothecaire;
    }

    public void setBienImmobilierHabitationNonPrincipaleMontantInteretHypothecaire(
            String bienImmobilierHabitationNonPrincipaleMontantInteretHypothecaire) {
        this.bienImmobilierHabitationNonPrincipaleMontantInteretHypothecaire = bienImmobilierHabitationNonPrincipaleMontantInteretHypothecaire;
    }

    public void setBienImmobilierHabitationNonPrincipaleMontantLoyersEncaisses(
            String bienImmobilierHabitationNonPrincipaleMontantLoyersEncaisses) {
        this.bienImmobilierHabitationNonPrincipaleMontantLoyersEncaisses = bienImmobilierHabitationNonPrincipaleMontantLoyersEncaisses;
    }

    public void setBienImmobilierHabitationNonPrincipaleMontantSousLocation(
            String bienImmobilierHabitationNonPrincipaleMontantSousLocation) {
        this.bienImmobilierHabitationNonPrincipaleMontantSousLocation = bienImmobilierHabitationNonPrincipaleMontantSousLocation;
    }

    public void setBienImmobilierHabitationNonPrincipaleMontantValeurLocative(
            String bienImmobilierHabitationNonPrincipaleMontantValeurLocative) {
        this.bienImmobilierHabitationNonPrincipaleMontantValeurLocative = bienImmobilierHabitationNonPrincipaleMontantValeurLocative;
    }

    public void setBienImmobilierHabitationNonPrincipaleValeurVenale(
            String bienImmobilierHabitationNonPrincipaleValeurVenale) {
        this.bienImmobilierHabitationNonPrincipaleValeurVenale = bienImmobilierHabitationNonPrincipaleValeurVenale;
    }

    public void setBienImmobilierNonHabitableMontantDetteHypothecaire(
            String bienImmobilierNonHabitableMontantDetteHypothecaire) {
        this.bienImmobilierNonHabitableMontantDetteHypothecaire = bienImmobilierNonHabitableMontantDetteHypothecaire;
    }

    public void setBienImmobilierNonHabitableMontantInteretHypothecaire(
            String bienImmobilierNonHabitableMontantInteretHypothecaire) {
        this.bienImmobilierNonHabitableMontantInteretHypothecaire = bienImmobilierNonHabitableMontantInteretHypothecaire;
    }

    public void setBienImmobilierNonHabitableMontantRendement(String bienImmobilierNonHabitableMontantRendement) {
        this.bienImmobilierNonHabitableMontantRendement = bienImmobilierNonHabitableMontantRendement;
    }

    public void setBienImmobilierNonHabitableValeurVenale(String bienImmobilierNonHabitableValeurVenale) {
        this.bienImmobilierNonHabitableValeurVenale = bienImmobilierNonHabitableValeurVenale;
    }

    public void setBienImmobilierServantHabitationPrincipaleMontantDetteHypothecaire(
            String bienImmobilierServantHabitationPrincipaleMontantDetteHypothecaire) {
        this.bienImmobilierServantHabitationPrincipaleMontantDetteHypothecaire = bienImmobilierServantHabitationPrincipaleMontantDetteHypothecaire;
    }

    public void setBienImmobilierServantHabitationPrincipaleMontantInteretHypothecaire(
            String bienImmobilierServantHabitationPrincipaleMontantInteretHypothecaire) {
        this.bienImmobilierServantHabitationPrincipaleMontantInteretHypothecaire = bienImmobilierServantHabitationPrincipaleMontantInteretHypothecaire;
    }

    public void setBienImmobilierServantHabitationPrincipaleMontantLoyersEncaisses(
            String bienImmobilierServantHabitationPrincipaleMontantLoyersEncaisses) {
        this.bienImmobilierServantHabitationPrincipaleMontantLoyersEncaisses = bienImmobilierServantHabitationPrincipaleMontantLoyersEncaisses;
    }

    public void setBienImmobilierServantHabitationPrincipaleMontantSousLocation(
            String bienImmobilierServantHabitationPrincipaleMontantSousLocation) {
        this.bienImmobilierServantHabitationPrincipaleMontantSousLocation = bienImmobilierServantHabitationPrincipaleMontantSousLocation;
    }

    public void setBienImmobilierServantHabitationPrincipaleMontantValeurFiscale(
            String bienImmobilierServantHabitationPrincipaleMontantValeurFiscale) {
        this.bienImmobilierServantHabitationPrincipaleMontantValeurFiscale = bienImmobilierServantHabitationPrincipaleMontantValeurFiscale;
    }

    public void setBienImmobilierServantHabitationPrincipaleMontantValeurLocative(
            String bienImmobilierServantHabitationPrincipaleMontantValeurLocative) {
        this.bienImmobilierServantHabitationPrincipaleMontantValeurLocative = bienImmobilierServantHabitationPrincipaleMontantValeurLocative;
    }

    public void setCapitalLPPCsTypePropriete(String capitalLPPCsTypePropriete) {
        this.capitalLPPCsTypePropriete = capitalLPPCsTypePropriete;
    }

    public void setCapitalLPPDenominateur(String capitalLPPDenominateur) {
        this.capitalLPPDenominateur = capitalLPPDenominateur;
    }

    public void setCapitalLPPMontant(String capitalLPPMontant) {
        this.capitalLPPMontant = capitalLPPMontant;
    }

    public void setCapitalLPPMontantInteret(String capitalLPPMontantInteret) {
        this.capitalLPPMontantInteret = capitalLPPMontantInteret;
    }

    public void setCapitalLPPNumerateur(String capitalLPPNumerateur) {
        this.capitalLPPNumerateur = capitalLPPNumerateur;
    }

    public void setCompteBancaireCCPCsTypePropriete(String compteBancaireCCPCsTypePropriete) {
        this.compteBancaireCCPCsTypePropriete = compteBancaireCCPCsTypePropriete;
    }

    public void setCompteBancaireCCPDenominateur(String compteBancaireCCPDenominateur) {
        this.compteBancaireCCPDenominateur = compteBancaireCCPDenominateur;
    }

    public void setCompteBancaireCCPMontant(String compteBancaireCCPMontant) {
        this.compteBancaireCCPMontant = compteBancaireCCPMontant;
    }

    public void setCompteBancaireCCPMontantInteret(String compteBancaireCCPMontantInteret) {
        this.compteBancaireCCPMontantInteret = compteBancaireCCPMontantInteret;
    }

    public void setCompteBancaireCCPNumerateur(String compteBancaireCCPNumerateur) {
        this.compteBancaireCCPNumerateur = compteBancaireCCPNumerateur;
    }

    public void setContratEntretienViagerMontant(String contratEntretienViagerMontant) {
        this.contratEntretienViagerMontant = contratEntretienViagerMontant;
    }

    public void setCotisationsPsalMontantCotisationsAnnuelles(String cotisationsPsalMontantCotisationsAnnuelles) {
        this.cotisationsPsalMontantCotisationsAnnuelles = cotisationsPsalMontantCotisationsAnnuelles;
    }

    public void setCsRoleMembreFamille(String csRoleMembreFamille) {
        this.csRoleMembreFamille = csRoleMembreFamille;
    }

    public void setCsTypeDonneeFinanciere(String csTypeDonneeFinanciere) {
        this.csTypeDonneeFinanciere = csTypeDonneeFinanciere;
    }

    public void setDateDebutDonneeFinanciere(String dateDebutDonneeFinanciere) {
        this.dateDebutDonneeFinanciere = dateDebutDonneeFinanciere;
    }

    public void setDessaisissementFortuneDeductions(String dessaisissementFortuneDeductions) {
        this.dessaisissementFortuneDeductions = dessaisissementFortuneDeductions;
    }

    public void setDessaisissementFortuneMontantBrut(String dessaisissementFortuneMontantBrut) {
        this.dessaisissementFortuneMontantBrut = dessaisissementFortuneMontantBrut;
    }

    public void setDessaisissementRevenuDeductions(String dessaisissementRevenuDeductions) {
        this.dessaisissementRevenuDeductions = dessaisissementRevenuDeductions;
    }

    public void setDessaisissementRevenuMontantBrut(String dessaisissementRevenuMontantBrut) {
        this.dessaisissementRevenuMontantBrut = dessaisissementRevenuMontantBrut;
    }

    @Override
    public void setId(String id) {
        simpleAnnonceLapramsDonneeFinanciereHeader.setId(id);
    }

    public void setIdTaxeJournaliereHome(String idTaxeJournaliereHome) {
        this.idTaxeJournaliereHome = idTaxeJournaliereHome;
    }

    public void setIjApgCotisationLPPMensC(String ijApgCotisationLPPMensC) {
        this.ijApgCotisationLPPMensC = ijApgCotisationLPPMensC;
    }

    public void setIjApgGainIntAnnuel(String ijApgGainIntAnnuel) {
        this.ijApgGainIntAnnuel = ijApgGainIntAnnuel;
    }

    public void setIjApgMontant(String ijApgMontant) {
        this.ijApgMontant = ijApgMontant;
    }

    public void setIjApgMontantBrutAC(String ijApgMontantBrutAC) {
        this.ijApgMontantBrutAC = ijApgMontantBrutAC;
    }

    public void setIjApgNbJours(String ijApgNbJours) {
        this.ijApgNbJours = ijApgNbJours;
    }

    public void setIjApgTauxAA(String ijApgTauxAA) {
        this.ijApgTauxAA = ijApgTauxAA;
    }

    public void setIjApgTauxAVS(String ijApgTauxAVS) {
        this.ijApgTauxAVS = ijApgTauxAVS;
    }

    public void setIndemniteJournaliereAiNbjours(String indemniteJournaliereAiNbjours) {
        this.indemniteJournaliereAiNbjours = indemniteJournaliereAiNbjours;
    }

    public void setIndemniteJournaliereAiRevenu(String indemniteJournaliereAiRevenu) {
        this.indemniteJournaliereAiRevenu = indemniteJournaliereAiRevenu;
    }

    public void setLoyerCharges(String loyerCharges) {
        this.loyerCharges = loyerCharges;
    }

    public void setLoyerCsTypeLoyer(String loyerCsTypeLoyer) {
        this.loyerCsTypeLoyer = loyerCsTypeLoyer;
    }

    public void setLoyerMontantNet(String loyerMontantNet) {
        this.loyerMontantNet = loyerMontantNet;
    }

    public void setMarchandisesStockCsTypePropriete(String marchandisesStockCsTypePropriete) {
        this.marchandisesStockCsTypePropriete = marchandisesStockCsTypePropriete;
    }

    public void setMarchandisesStockDenominateur(String marchandisesStockDenominateur) {
        this.marchandisesStockDenominateur = marchandisesStockDenominateur;
    }

    public void setMarchandisesStockMontant(String marchandisesStockMontant) {
        this.marchandisesStockMontant = marchandisesStockMontant;
    }

    public void setMarchandisesStockNumerateur(String marchandisesStockNumerateur) {
        this.marchandisesStockNumerateur = marchandisesStockNumerateur;
    }

    public void setMembreFamilleEtendu(MembreFamilleEtendu membreFamilleEtendu) {
        this.membreFamilleEtendu = membreFamilleEtendu;
    }

    public void setNumeraireCsTypePropriete(String numeraireCsTypePropriete) {
        this.numeraireCsTypePropriete = numeraireCsTypePropriete;
    }

    public void setNumeraireDenominateur(String numeraireDenominateur) {
        this.numeraireDenominateur = numeraireDenominateur;
    }

    public void setNumeraireMontant(String numeraireMontant) {
        this.numeraireMontant = numeraireMontant;
    }

    public void setNumeraireMontantInteret(String numeraireMontantInteret) {
        this.numeraireMontantInteret = numeraireMontantInteret;
    }

    public void setNumeraireNumerateur(String numeraireNumerateur) {
        this.numeraireNumerateur = numeraireNumerateur;
    }

    public void setPensionAlimentaireCsTypePension(String pensionAlimentaireCsTypePension) {
        this.pensionAlimentaireCsTypePension = pensionAlimentaireCsTypePension;
    }

    public void setPensionAlimentaireMontant(String pensionAlimentaireMontant) {
        this.pensionAlimentaireMontant = pensionAlimentaireMontant;
    }

    public void setPretEnversTiersCsTypePropriete(String pretEnversTiersCsTypePropriete) {
        this.pretEnversTiersCsTypePropriete = pretEnversTiersCsTypePropriete;
    }

    public void setPretEnversTiersDenominateur(String pretEnversTiersDenominateur) {
        this.pretEnversTiersDenominateur = pretEnversTiersDenominateur;
    }

    public void setPretEnversTiersMontant(String pretEnversTiersMontant) {
        this.pretEnversTiersMontant = pretEnversTiersMontant;
    }

    public void setPretEnversTiersMontantInteret(String pretEnversTiersMontantInteret) {
        this.pretEnversTiersMontantInteret = pretEnversTiersMontantInteret;
    }

    public void setPretEnversTiersNumerateur(String pretEnversTiersNumerateur) {
        this.pretEnversTiersNumerateur = pretEnversTiersNumerateur;
    }

    public void setRenteAvsAiMontant(String renteAvsAiMontant) {
        this.renteAvsAiMontant = renteAvsAiMontant;
    }

    public void setRevenuHypothetiqueMontantBrut(String revenuHypothetiqueMontantBrut) {
        this.revenuHypothetiqueMontantBrut = revenuHypothetiqueMontantBrut;
    }

    public void setRevenuHypothetiqueMontantNet(String revenuHypothetiqueMontantNet) {
        this.revenuHypothetiqueMontantNet = revenuHypothetiqueMontantNet;
    }

    public void setSimpleAnnonceLapramsDonneeFinanciereHeader(
            SimpleAnnonceLapramsDonneeFinanciereHeader simpleAnnonceLapramsDonneeFinanciereHeader) {
        this.simpleAnnonceLapramsDonneeFinanciereHeader = simpleAnnonceLapramsDonneeFinanciereHeader;
    }

    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        this.simpleDonneeFinanciereHeader = simpleDonneeFinanciereHeader;
    }

    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub

    }

    public void setTaxeJournaliereHomeCsDestinationSortie(String taxeJournaliereHomeCsDestinationSortie) {
        this.taxeJournaliereHomeCsDestinationSortie = taxeJournaliereHomeCsDestinationSortie;
    }

    public void setTaxeJournaliereHomeCsTypeAdressePmt(String taxeJournaliereHomeCsTypeAdressePmt) {
        this.taxeJournaliereHomeCsTypeAdressePmt = taxeJournaliereHomeCsTypeAdressePmt;
    }

    public void setTaxeJournaliereHomeDateEntreeHome(String taxeJournaliereHomeDateEntreeHome) {
        this.taxeJournaliereHomeDateEntreeHome = taxeJournaliereHomeDateEntreeHome;
    }

    public void setTaxeJournaliereHomeIdTypeChambre(String taxeJournaliereHomeIdTypeChambre) {
        this.taxeJournaliereHomeIdTypeChambre = taxeJournaliereHomeIdTypeChambre;
    }

    public void setTaxeJournaliereHomeIsPartLCA(Boolean taxeJournaliereHomeIsPartLCA) {
        this.taxeJournaliereHomeIsPartLCA = taxeJournaliereHomeIsPartLCA;
    }

    public void setTaxeJournaliereHomePartLCA(String taxeJournaliereHomePartLCA) {
        this.taxeJournaliereHomePartLCA = taxeJournaliereHomePartLCA;
    }

    public void setTaxeJournaliereHomePimeAPayerLCA(String taxeJournaliereHomePimeAPayerLCA) {
        this.taxeJournaliereHomePimeAPayerLCA = taxeJournaliereHomePimeAPayerLCA;
    }

    public void setTitreCsTypePropriete(String titreCsTypePropriete) {
        this.titreCsTypePropriete = titreCsTypePropriete;
    }

    public void setTitreDenominateur(String titreDenominateur) {
        this.titreDenominateur = titreDenominateur;
    }

    public void setTitreMontant(String titreMontant) {
        this.titreMontant = titreMontant;
    }

    public void setTitreNumerateur(String titreNumerateur) {
        this.titreNumerateur = titreNumerateur;
    }

    public void setTitreRendement(String titreRendement) {
        this.titreRendement = titreRendement;
    }

    public void setVehiculeCsTypePropriete(String vehiculeCsTypePropriete) {
        this.vehiculeCsTypePropriete = vehiculeCsTypePropriete;
    }

    public void setVehiculeDenominateur(String vehiculeDenominateur) {
        this.vehiculeDenominateur = vehiculeDenominateur;
    }

    public void setVehiculeMontant(String vehiculeMontant) {
        this.vehiculeMontant = vehiculeMontant;
    }

    public void setVehiculeNumerateur(String vehiculeNumerateur) {
        this.vehiculeNumerateur = vehiculeNumerateur;
    }

    @Override
    public String toString() {
        return "AnnonceLapramsMediatorDonneeFinanciere [csRoleMembreFamille=" + csRoleMembreFamille
                + ", csTypeDonneeFinanciere=" + csTypeDonneeFinanciere + ", idDonneeFinanciereHeader = "
                + getSimpleDonneeFinanciereHeader().getIdDonneeFinanciereHeader() + ", idDroitMembreFamille = "
                + getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille() + " ]";
    }

}
