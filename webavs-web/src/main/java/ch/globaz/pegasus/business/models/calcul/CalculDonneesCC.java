package ch.globaz.pegasus.business.models.calcul;

import globaz.jade.persistence.model.JadeComplexModel;

public class CalculDonneesCC extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String allocationFamilialeMontantMensuel = null;
    private String APIAVSAIMontant = null;
    private String APIAVSCsType = null;
    private String assuranceRenteViagereExcedant = null;
    private String assuranceRenteViagereMontant = null;
    private String assuranceRenteViagereMontantValeurRachat = null;
    private String assuranceVieMontantValeurRachat = null;
    private String autreFortuneMobiliereCsTypeFortune = null;
    private String autreFortuneMobiliereCsTypePropriete = null;
    private String autreFortuneMobiliereFractionDenominateur = null;
    private String autreFortuneMobiliereFractionNumerateur = null;
    private String autreFortuneMobiliereMontant = null;
    private String autreRentesEtrangeresCSTypeDevise = null;
    private String autresApiAutre = null;
    private String autresAPICsTypeMontant = null;
    private String autresAPIMontant = null;
    private String autresDettesProuveesMontant = null;
    private String autresRentesAutreGenre = null;
    private String autresRentesCsGenre = null;
    private String autresRentesMontant = null;
    private String autresRevenusLibelle = null;
    private String autresRevenusMontant = null;
    private String betailCsTypePropriete = null;
    private String betailFractionDenominateur = null;
    private String betailFractionNumerateur = null;
    private String betailMontant = null;
    private String bienImmoAnnexeCsTypePropriete = null;
    private String bienImmoAnnexeMontantDetteHypothecaire = null;
    private String bienImmoAnnexeMontantInteretHypothecaire = null;
    private String bienImmoAnnexeMontantLoyersEncaisses = null;

    private String bienImmoAnnexeMontantSousLocation = null;

    private String bienImmoAnnexeMontantValeurLocative = null;

    private String bienImmoAnnexeMontantValeurVenale = null;

    private String bienImmoAnnexePartDenominateur = null;

    private String bienImmoAnnexePartNumerateur = null;

    private String bienImmoNonHabitableCsTypePropriete = null;

    private String bienImmoNonHabitableMontantDetteHypothecaire = null;

    private String bienImmoNonHabitableMontantInteretHypothecaire = null;

    private String bienImmoNonHabitableMontantRendement = null;

    private String bienImmoNonHabitableMontantValeurVenale = null;

    private String bienImmoNonHabitablePartDenominateur = null;

    private String bienImmoNonHabitablePartNumerateur = null;

    private String bienImmoPrincipalCSPropriete = null;

    private String bienImmoPrincipalMontantDetteHypothecaire = null;

    private String bienImmoPrincipalMontantInteretHypothecaire = null;

    private String bienImmoPrincipalMontantLoyersEncaisses = null;

    private String bienImmoPrincipalMontantSousLocation = null;

    private String bienImmoPrincipalMontantValeurFiscale = null;

    private String bienImmoPrincipalMontantValeurLocative = null;
    private String bienImmoPrincipalNombrePersonnes = null;
    private String bienImmoPrincipalPartDenominateur = null;
    private String bienImmoPrincipalPartNumerateur = null;

    private Boolean isBienImmoPrincipalDeMoinsDe10Ans = Boolean.FALSE;
    private Boolean isBienImmoAnnexeDeMoinsDe10Ans = Boolean.FALSE;

    private String capitalLPPCsTypePropriete = null;
    private String capitalLPPFractionDenominateur = null;
    private String capitalLPPFractionNumerateur = null;

    public Boolean getIsBienImmoAnnexeDeMoinsDe10Ans() {
        return isBienImmoAnnexeDeMoinsDe10Ans;
    }

    public void setIsBienImmoAnnexeDeMoinsDe10Ans(Boolean isBienImmoAnnexeDeMoinsDe10Ans) {
        this.isBienImmoAnnexeDeMoinsDe10Ans = isBienImmoAnnexeDeMoinsDe10Ans;
    }

    private Boolean capitalLPPIsSansInteret = null;
    private String capitalLPPMontant = null;

    private String capitalLPPMontantFrais = null;

    private String capitalLPPMontantInterets = null;
    private String compteBancaireCPPCsTypePropriete = null;
    private String compteBancaireCPPFractionDenominateur = null;
    private String compteBancaireCPPFractionNumerateur = null;
    private String compteBancaireCPPMontant = null;
    private String compteBancaireCPPMontantFrais = null;
    private String compteBancaireCPPMontantInterets = null;
    private Boolean compteBancaireIsSansInteret = null;
    private String contratEntretienViagerMontant = null;
    private String cotisationPSALMontantAnnuel = null;
    private String csRoleFamille = null;
    private String csTypeDonneeFinanciere = null;
    private String dateDebutDonneeFinanciere = null;
    private String dateFinDonneeFinanciere = null;
    private String dessaisissementFortuneDeductions = null;
    private String dessaisissementFortuneMontant = null;
    private String dessaisissementFortuneType = null;
    private String dessaisissementRevenuDeductions = null;
    private String dessaisissementRevenuMontant = null;
    private String idDonneeFinanciereHeader = null;
    private String idDroitMembreFamille = null;
    private String idMembreFamilleSF = null;
    private String IJAIJours = null;
    private String IJAIMontant = null;
    private String IJAPGAutreGenre = null;

    private String IJAPGcotisationLPPMens = null;

    private String IJAPGgainIntAnnuel = null;

    private String IJAPGGenre = null;

    private String IJAPGLPP = null;

    private String IJAPGMontant = null;

    private String IJAPGMontantChomage = null;

    private String IJAPGnbJours = null;

    private String IJAPGtauxAA = null;
    private String IJAPGtauxAVS = null;
    private Boolean isDessaisissementFortune = Boolean.FALSE;
    private Boolean isDessaisissementRevenu = Boolean.FALSE;
    private String typeDessaisissementFortune = null;
    private String loyerCsTypeLoyer = null;
    private Boolean loyerIsFauteuilRoulant = null;
    private Boolean loyerIsTenueMenage = null;
    private String loyerMontant = null;

    private String taxeJournaliereMontantFraisLongueDuree = null;

    public String getTaxeJournaliereMontantFraisLongueDuree() {
        return taxeJournaliereMontantFraisLongueDuree;
    }

    public void setTaxeJournaliereMontantFraisLongueDuree(String taxeJournaliereMontantFraisLongueDuree) {
        this.taxeJournaliereMontantFraisLongueDuree = taxeJournaliereMontantFraisLongueDuree;
    }

    private String loyerMontantCharges = null;

    private String loyerMontantSousLocations = null;

    private String loyerNbPersonnes = null;

    private String loyerTaxeJournalierePensionNonReconnue = null;

    private String loyerCsDeplafonnementAppartementPartage = null;

    private String marchandiseStockCsTypePropriete = null;

    private String marchandiseStockFractionDenominateur = null;

    private String marchandiseStockFractionNumerateur = null;

    private String marchandiseStockMontant = null;

    private String numeraireCsTypePropriete = null;

    private String numeraireFractionDenominateur = null;

    private String numeraireFractionNumerateur = null;

    private Boolean numeraireIsSansInteret = null;

    private String numeraireMontant = null;

    private String numeraireMontantInterets = null;

    private String pensionAlimentaireCsTypePension = null;

    private Boolean pensionAlimentaireIsDeductionsRenteEnfant = null;

    private String pensionAlimentaireLienParente = null; // ATTENTION le libelle de la variable prete à confusion, c'est
                                                         // bine le lien de parenté avec qui vers éla pension, et pas
                                                         // le requerant

    private String pensionAlimentaireMontant = null;

    private String pensionAlimentaireMontantRenteEnfant = null;

    private String pretEnversTiersCsTypePropriete = null;

    private Boolean pretEnversTiersIsSansInteret = null;

    private String pretEnversTiersMontant = null;

    private String pretEnversTiersMontantInterets = null;

    private String pretEnversTiersPartProprieteDenominateur = null;

    private String pretEnversTiersPartProprieteNumerateur = null;

    private String renteAVSAICsType = null;

    private String renteAVSAICsTypeSansRente = null;

    private String renteAVSAIMontant = null;

    private String revenuActiviteLucrativeDependanteDeductionsLPP = null;

    private String revenuActiviteLucrativeDependanteDeductionsSociales = null;

    private String revenuActiviteLucrativeDependanteMontant = null;

    private String revenuActiviteLucrativeDependanteMontantFraisEffectifs = null;

    private String revenuActiviteLucrativeDependanteMontantFraisDeGarde = null;

    private String revenuActiviteLucrativeIndependanteCSGenreRevenu = null;

    private String revenuActiviteLucrativeIndependanteMontant = null;

    private String revenuActiviteLucrativeIndependanteMontantFraisDeGarde = null;

    private String revenuHypothetiqueMontantDeductionsLPP = null;

    private String revenuHypothetiqueMontantDeductionsSociales = null;

    private String revenuHypothetiqueMontantFraisGarde = null;

    private String revenuHypothetiqueMontantRevenuBrut = null;

    private String revenuHypothetiqueMontantRevenuNet = null;

    private String taxeJournaliereDateEntreeHome = null;

    private String taxeJournaliereIdTypeChambre = null;

    private Boolean taxeJournaliereIsParticipationLCA = null;

    private String taxeJournaliereMontantJournalierLCA = null;

    private String taxeJournalierePrimeAPayer = null;

    private String titreCsTypePropriete = null;

    private Boolean taxeJournaliereIsDeplafonner = null;

    public Boolean getTaxeJournaliereIsDeplafonner() {
        return taxeJournaliereIsDeplafonner;
    }

    public void setTaxeJournaliereIsDeplafonner(Boolean taxeJournaliereIsDeplafonner) {
        this.taxeJournaliereIsDeplafonner = taxeJournaliereIsDeplafonner;
    }

    private String titreDroitGarde = null;

    private String titreFractionDenominateur = null;

    private String titreFractionNumerateur = null;

    private Boolean titreIsSansRendement = null;

    private String titreMontant = null;

    private String titreRendement = null;

    private String vehiculeCsTypePropriete = null;

    private String vehiculeFractionDenominateur = null;

    private String vehiculeFractionNumerateur = null;

    private String vehiculeMontant = null;
    
    private String imputationFortune = null;

    private String idLocalite = null;

    private String bienImmoPrincipalIdLocalite = null;

    private String fraisGardeLibelle = null;

    private String fraisGardeMontant = null;

    private String nbTotalFamille = null;

    private String primeAssuranceMaladieMontant = null;

    private String subsideAssuranceMaladieMontant = null;

    private String sejourMoisPartielPrixJournalier = null;

    private String sejourMoisPartielFraisNourriture = null;

    private String sejourMoisPartielNombreJour = null;

    private Boolean sejourMoisPartielVersementDirect = Boolean.FALSE;

    private String sejourMoisPartielHome = null;

    /**
     * @return the allocationFamilialeMontantMensuel
     */
    public String getAllocationFamilialeMontantMensuel() {
        return allocationFamilialeMontantMensuel;
    }

    /**
     * @return the aPIAVSAIMontant
     */
    public String getAPIAVSAIMontant() {
        return APIAVSAIMontant;
    }

    /**
     * @return the aPIAVSCsType
     */
    public String getAPIAVSCsType() {
        return APIAVSCsType;
    }

    /**
     * @return the assuranceRenteViagereExcedant
     */
    public String getAssuranceRenteViagereExcedant() {
        return assuranceRenteViagereExcedant;
    }

    /**
     * @return the assuranceRenteViagereMontant
     */
    public String getAssuranceRenteViagereMontant() {
        return assuranceRenteViagereMontant;
    }

    /**
     * @return the assuranceRenteViagereMontantValeurRachat
     */
    public String getAssuranceRenteViagereMontantValeurRachat() {
        return assuranceRenteViagereMontantValeurRachat;
    }

    /**
     * @return the assuranceVieMontantValeurRachat
     */
    public String getAssuranceVieMontantValeurRachat() {
        return assuranceVieMontantValeurRachat;
    }

    /**
     * @return the autreFortuneMobiliereCsTypeFortune
     */
    public String getAutreFortuneMobiliereCsTypeFortune() {
        return autreFortuneMobiliereCsTypeFortune;
    }

    /**
     * @return the autreFortuneMobiliereCsTypePropriete
     */
    public String getAutreFortuneMobiliereCsTypePropriete() {
        return autreFortuneMobiliereCsTypePropriete;
    }

    public String getAutreFortuneMobiliereFractionDenominateur() {
        return autreFortuneMobiliereFractionDenominateur;
    }

    public String getAutreFortuneMobiliereFractionNumerateur() {
        return autreFortuneMobiliereFractionNumerateur;
    }

    /**
     * @return the autreFortuneMobiliereMontant
     */
    public String getAutreFortuneMobiliereMontant() {
        return autreFortuneMobiliereMontant;
    }

    public String getAutreRentesEtrangeresCSTypeDevise() {
        return autreRentesEtrangeresCSTypeDevise;
    }

    /**
     * @return the autresApiAutre
     */
    public String getAutresApiAutre() {
        return autresApiAutre;
    }

    /**
     * @return the autresAPICsTypeMontant
     */
    public String getAutresAPICsTypeMontant() {
        return autresAPICsTypeMontant;
    }

    /**
     * @return the autresAPIMontant
     */
    public String getAutresAPIMontant() {
        return autresAPIMontant;
    }

    /**
     * @return the autresDettesProuveesMontant
     */
    public String getAutresDettesProuveesMontant() {
        return autresDettesProuveesMontant;
    }

    /**
     * @return the autresRentesAutreGenre
     */
    public String getAutresRentesAutreGenre() {
        return autresRentesAutreGenre;
    }

    /**
     * @return the autresRentesCsGenre
     */
    public String getAutresRentesCsGenre() {
        return autresRentesCsGenre;
    }

    /**
     * @return the autresRentesMontant
     */
    public String getAutresRentesMontant() {
        return autresRentesMontant;
    }

    /**
     * @return the autresRevenusLibelle
     */
    public String getAutresRevenusLibelle() {
        return autresRevenusLibelle;
    }

    /**
     * @return the autresRevenusMontant
     */
    public String getAutresRevenusMontant() {
        return autresRevenusMontant;
    }

    public String getBetailCsTypePropriete() {
        return betailCsTypePropriete;
    }

    public String getBetailFractionDenominateur() {
        return betailFractionDenominateur;
    }

    public String getBetailFractionNumerateur() {
        return betailFractionNumerateur;
    }

    /**
     * @return the betailMontant
     */
    public String getBetailMontant() {
        return betailMontant;
    }

    public String getBienImmoAnnexeCsTypePropriete() {
        return bienImmoAnnexeCsTypePropriete;
    }

    /**
     * @return the bienImmoAnnexeMontantDetteHypothecaire
     */
    public String getBienImmoAnnexeMontantDetteHypothecaire() {
        return bienImmoAnnexeMontantDetteHypothecaire;
    }

    /**
     * @return the bienImmoAnnexeMontantInteretHypothecaire
     */
    public String getBienImmoAnnexeMontantInteretHypothecaire() {
        return bienImmoAnnexeMontantInteretHypothecaire;
    }

    /**
     * @return the bienImmoAnnexeMontantLoyersEncaisses
     */
    public String getBienImmoAnnexeMontantLoyersEncaisses() {
        return bienImmoAnnexeMontantLoyersEncaisses;
    }

    public String getBienImmoAnnexeMontantSousLocation() {
        return bienImmoAnnexeMontantSousLocation;
    }

    /**
     * @return the bienImmoAnnexeMontantValeurLocative
     */
    public String getBienImmoAnnexeMontantValeurLocative() {
        return bienImmoAnnexeMontantValeurLocative;
    }

    /**
     * @return the bienImmoAnnexeMontantValeurVenale
     */
    public String getBienImmoAnnexeMontantValeurVenale() {
        return bienImmoAnnexeMontantValeurVenale;
    }

    public String getBienImmoAnnexePartDenominateur() {
        return bienImmoAnnexePartDenominateur;
    }

    public String getBienImmoAnnexePartNumerateur() {
        return bienImmoAnnexePartNumerateur;
    }

    public String getBienImmoNonHabitableCsTypePropriete() {
        return bienImmoNonHabitableCsTypePropriete;
    }

    /**
     * @return the bienImmoNonHabitableMontantDetteHypothecaire
     */
    public String getBienImmoNonHabitableMontantDetteHypothecaire() {
        return bienImmoNonHabitableMontantDetteHypothecaire;
    }

    /**
     * @return the bienImmoNonHabitableMontantInteretHypothecaire
     */
    public String getBienImmoNonHabitableMontantInteretHypothecaire() {
        return bienImmoNonHabitableMontantInteretHypothecaire;
    }

    /**
     * @return the bienImmoNonHabitableMontantRendement
     */
    public String getBienImmoNonHabitableMontantRendement() {
        return bienImmoNonHabitableMontantRendement;
    }

    /**
     * @return the bienImmoNonHabitableMontantValeurVenale
     */
    public String getBienImmoNonHabitableMontantValeurVenale() {
        return bienImmoNonHabitableMontantValeurVenale;
    }

    public String getBienImmoNonHabitablePartDenominateur() {
        return bienImmoNonHabitablePartDenominateur;
    }

    public String getBienImmoNonHabitablePartNumerateur() {
        return bienImmoNonHabitablePartNumerateur;
    }

    public String getBienImmoPrincipalCSPropriete() {
        return bienImmoPrincipalCSPropriete;
    }

    /**
     * @return the bienImmoPrincipalMontantDetteHypothecaire
     */
    public String getBienImmoPrincipalMontantDetteHypothecaire() {
        return bienImmoPrincipalMontantDetteHypothecaire;
    }

    /**
     * @return the bienImmoPrincipalMontantInteretHypothecaire
     */
    public String getBienImmoPrincipalMontantInteretHypothecaire() {
        return bienImmoPrincipalMontantInteretHypothecaire;
    }

    /**
     * @return the bienImmoPrincipalMontantLoyersEncaisses
     */
    public String getBienImmoPrincipalMontantLoyersEncaisses() {
        return bienImmoPrincipalMontantLoyersEncaisses;
    }

    /**
     * @return the bienImmoPrincipalMontantSousLocation
     */
    public String getBienImmoPrincipalMontantSousLocation() {
        return bienImmoPrincipalMontantSousLocation;
    }

    /**
     * @return the bienImmoPrincipalMontantValeurFiscale
     */
    public String getBienImmoPrincipalMontantValeurFiscale() {
        return bienImmoPrincipalMontantValeurFiscale;
    }

    /**
     * @return the bienImmoPrincipalMontantValeurLocative
     */
    public String getBienImmoPrincipalMontantValeurLocative() {
        return bienImmoPrincipalMontantValeurLocative;
    }

    public String getBienImmoPrincipalNombrePersonnes() {
        return bienImmoPrincipalNombrePersonnes;
    }

    public String getBienImmoPrincipalPartDenominateur() {
        return bienImmoPrincipalPartDenominateur;
    }

    public String getBienImmoPrincipalPartNumerateur() {
        return bienImmoPrincipalPartNumerateur;
    }

    public String getCapitalLPPCsTypePropriete() {
        return capitalLPPCsTypePropriete;
    }

    public String getCapitalLPPFractionDenominateur() {
        return capitalLPPFractionDenominateur;
    }

    public String getCapitalLPPFractionNumerateur() {
        return capitalLPPFractionNumerateur;
    }

    /**
     * @return the capitalLPPIsSansInteret
     */
    public Boolean getCapitalLPPIsSansInteret() {
        return capitalLPPIsSansInteret;
    }

    /**
     * @return the capitalLPPMontant
     */
    public String getCapitalLPPMontant() {
        return capitalLPPMontant;
    }

    /**
     * @return the capitalLPPMontantFrais
     */
    public String getCapitalLPPMontantFrais() {
        return capitalLPPMontantFrais;
    }

    /**
     * @return the capitalLPPMontantInterets
     */
    public String getCapitalLPPMontantInterets() {
        return capitalLPPMontantInterets;
    }

    /**
     * @return the compteBancaireCPPCsTypePropriete
     */
    public String getCompteBancaireCPPCsTypePropriete() {
        return compteBancaireCPPCsTypePropriete;
    }

    public String getCompteBancaireCPPFractionDenominateur() {
        return compteBancaireCPPFractionDenominateur;
    }

    public String getCompteBancaireCPPFractionNumerateur() {
        return compteBancaireCPPFractionNumerateur;
    }

    /**
     * @return the compteBancaireCPPMontant
     */
    public String getCompteBancaireCPPMontant() {
        return compteBancaireCPPMontant;
    }

    /**
     * @return the compteBancaireCPPMontantFrais
     */
    public String getCompteBancaireCPPMontantFrais() {
        return compteBancaireCPPMontantFrais;
    }

    /**
     * @return the compteBancaireCPPMontantInterets
     */
    public String getCompteBancaireCPPMontantInterets() {
        return compteBancaireCPPMontantInterets;
    }

    /**
     * @return the compteBancaireIsSansInteret
     */
    public Boolean getCompteBancaireIsSansInteret() {
        return compteBancaireIsSansInteret;
    }

    /**
     * @return the contratEntretienViagerMontant
     */
    public String getContratEntretienViagerMontant() {
        return contratEntretienViagerMontant;
    }

    /**
     * @return the cotisationPSALMontantAnnuel
     */
    public String getCotisationPSALMontantAnnuel() {
        return cotisationPSALMontantAnnuel;
    }

    /**
     * @return the csRoleFamille
     */
    public String getCsRoleFamille() {
        return csRoleFamille;
    }

    /**
     * @return the csTypeDonneeFinanciere
     */
    public String getCsTypeDonneeFinanciere() {
        return csTypeDonneeFinanciere;
    }

    /**
     * @return the dateDebutDonneeFinanciere
     */
    public String getDateDebutDonneeFinanciere() {
        return dateDebutDonneeFinanciere;
    }

    /**
     * @return the dateFinDonneeFinanciere
     */
    public String getDateFinDonneeFinanciere() {
        return dateFinDonneeFinanciere;
    }

    /**
     * @return the dessaisissementFortuneDeductions
     */
    public String getDessaisissementFortuneDeductions() {
        return dessaisissementFortuneDeductions;
    }

    /**
     * @return the dessaisissementFortuneType
     */
    public String getDessaisissementFortuneType() {
        return dessaisissementFortuneType;
    }

    /**
     * @return the dessaisissementFortuneMontant
     */
    public String getDessaisissementFortuneMontant() {
        return dessaisissementFortuneMontant;
    }

    /**
     * @return the dessaisissementRevenuDeductions
     */
    public String getDessaisissementRevenuDeductions() {
        return dessaisissementRevenuDeductions;
    }

    /**
     * @return the dessaisissementRevenuMontant
     */
    public String getDessaisissementRevenuMontant() {
        return dessaisissementRevenuMontant;
    }

    @Override
    public String getId() {
        return idMembreFamilleSF;
    }

    /**
     * @return the idDonneeFinanciereHeader
     */
    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    public String getIdDroitMembreFamille() {
        return idDroitMembreFamille;
    }

    /**
     * @return the idMembreFamilleSF
     */
    public String getIdMembreFamilleSF() {
        return idMembreFamilleSF;
    }

    public String getIJAIJours() {
        return IJAIJours;
    }

    /**
     * @return the iJAIMontant
     */
    public String getIJAIMontant() {
        return IJAIMontant;
    }

    /**
     * @return the iJAPGAutreGenre
     */
    public String getIJAPGAutreGenre() {
        return IJAPGAutreGenre;
    }

    public String getIJAPGcotisationLPPMens() {
        return IJAPGcotisationLPPMens;
    }

    public String getIJAPGgainIntAnnuel() {
        return IJAPGgainIntAnnuel;
    }

    /**
     * @return the iJAPGGenre
     */
    public String getIJAPGGenre() {
        return IJAPGGenre;
    }

    public String getIJAPGLPP() {
        return IJAPGLPP;
    }

    /**
     * @return the iJAPGMontant
     */
    public String getIJAPGMontant() {
        return IJAPGMontant;
    }

    public String getIJAPGMontantChomage() {
        return IJAPGMontantChomage;
    }

    public String getIJAPGnbJours() {
        return IJAPGnbJours;
    }

    public String getIJAPGtauxAA() {
        return IJAPGtauxAA;
    }

    public String getIJAPGtauxAVS() {
        return IJAPGtauxAVS;
    }

    public Boolean getIsDessaisissementFortune() {
        return isDessaisissementFortune;
    }

    public Boolean getIsDessaisissementRevenu() {
        return isDessaisissementRevenu;
    }

    public String getTypeDessaisissementFortune() {
        return typeDessaisissementFortune;
    }

    /**
     * @return the loyerCsTypeLoyer
     */
    public String getLoyerCsTypeLoyer() {
        return loyerCsTypeLoyer;
    }

    /**
     * @return the loyerIsFauteuilRoulant
     */
    public Boolean getLoyerIsFauteuilRoulant() {
        return loyerIsFauteuilRoulant;
    }

    /**
     * @return the loyerIsTenueMenage
     */
    public Boolean getLoyerIsTenueMenage() {
        return loyerIsTenueMenage;
    }

    /**
     * @return the loyerMontant
     */
    public String getLoyerMontant() {
        return loyerMontant;
    }

    /**
     * @return the loyerMontantCharges
     */
    public String getLoyerMontantCharges() {
        return loyerMontantCharges;
    }

    public String getLoyerMontantSousLocations() {
        return loyerMontantSousLocations;
    }

    /**
     * @return the loyerNbPersonnes
     */
    public String getLoyerNbPersonnes() {
        return loyerNbPersonnes;
    }

    public String getLoyerTaxeJournalierePensionNonReconnue() {
        return loyerTaxeJournalierePensionNonReconnue;
    }

    public String getLoyerCsDeplafonnementAppartementPartage() {
        return loyerCsDeplafonnementAppartementPartage;
    }

    public String getMarchandiseStockCsTypePropriete() {
        return marchandiseStockCsTypePropriete;
    }

    public String getMarchandiseStockFractionDenominateur() {
        return marchandiseStockFractionDenominateur;
    }

    public String getMarchandiseStockFractionNumerateur() {
        return marchandiseStockFractionNumerateur;
    }

    /**
     * @return the marchandiseStockMontant
     */
    public String getMarchandiseStockMontant() {
        return marchandiseStockMontant;
    }

    /**
     * @return the numeraireCsTypePropriete
     */
    public String getNumeraireCsTypePropriete() {
        return numeraireCsTypePropriete;
    }

    public String getNumeraireFractionDenominateur() {
        return numeraireFractionDenominateur;
    }

    public String getNumeraireFractionNumerateur() {
        return numeraireFractionNumerateur;
    }

    /**
     * @return the numeraireIsSansInteret
     */
    public Boolean getNumeraireIsSansInteret() {
        return numeraireIsSansInteret;
    }

    /**
     * @return the numeraireMontant
     */
    public String getNumeraireMontant() {
        return numeraireMontant;
    }

    /**
     * @return the numeraireMontantInterets
     */
    public String getNumeraireMontantInterets() {
        return numeraireMontantInterets;
    }

    /**
     * @return the pensionAlimentaireCsTypePension
     */
    public String getPensionAlimentaireCsTypePension() {
        return pensionAlimentaireCsTypePension;
    }

    /**
     * @return the pensionAlimentaireIsDeductionsRenteEnfant
     */
    public Boolean getPensionAlimentaireIsDeductionsRenteEnfant() {
        return pensionAlimentaireIsDeductionsRenteEnfant;
    }

    public String getPensionAlimentaireLienParente() {
        return pensionAlimentaireLienParente;
    }

    /**
     * @return the pensionAlimentaireMontant
     */
    public String getPensionAlimentaireMontant() {
        return pensionAlimentaireMontant;
    }

    public String getPensionAlimentaireMontantRenteEnfant() {
        return pensionAlimentaireMontantRenteEnfant;
    }

    public String getPretEnversTiersCsTypePropriete() {
        return pretEnversTiersCsTypePropriete;
    }

    /**
     * @return the pretEnversTiersIsSansInteret
     */
    public Boolean getPretEnversTiersIsSansInteret() {
        return pretEnversTiersIsSansInteret;
    }

    /**
     * @return the pretEnversTiersMontant
     */
    public String getPretEnversTiersMontant() {
        return pretEnversTiersMontant;
    }

    /**
     * @return the pretEnversTiersMontantInterets
     */
    public String getPretEnversTiersMontantInterets() {
        return pretEnversTiersMontantInterets;
    }

    /**
     * @return the pretEnversTiersPartProprieteDenominateur
     */
    public String getPretEnversTiersPartProprieteDenominateur() {
        return pretEnversTiersPartProprieteDenominateur;
    }

    /**
     * @return the pretEnversTiersPartProprieteNumerateur
     */
    public String getPretEnversTiersPartProprieteNumerateur() {
        return pretEnversTiersPartProprieteNumerateur;
    }

    /**
     * @return the renteAVSAICsType
     */
    public String getRenteAVSAICsType() {
        return renteAVSAICsType;
    }

    public String getRenteAVSAICsTypeSansRente() {
        return renteAVSAICsTypeSansRente;
    }

    /**
     * @return the renteAVSAIMontant
     */
    public String getRenteAVSAIMontant() {
        return renteAVSAIMontant;
    }

    /**
     * @return the revenuActiviteLucrativeDependanteDeductionsLPP
     */
    public String getRevenuActiviteLucrativeDependanteDeductionsLPP() {
        return revenuActiviteLucrativeDependanteDeductionsLPP;
    }

    /**
     * @return the revenuActiviteLucrativeDependanteDeductionsSociales
     */
    public String getRevenuActiviteLucrativeDependanteDeductionsSociales() {
        return revenuActiviteLucrativeDependanteDeductionsSociales;
    }

    /**
     * @return the revenuActiviteLucrativeDependanteMontant
     */
    public String getRevenuActiviteLucrativeDependanteMontant() {
        return revenuActiviteLucrativeDependanteMontant;
    }

    /**
     * @return the revenuActiviteLucrativeDependanteMontantFraisEffectifs
     */
    public String getRevenuActiviteLucrativeDependanteMontantFraisEffectifs() {
        return revenuActiviteLucrativeDependanteMontantFraisEffectifs;
    }

    public String getRevenuActiviteLucrativeIndependanteCSGenreRevenu() {
        return revenuActiviteLucrativeIndependanteCSGenreRevenu;
    }

    /**
     * @return the revenuActiviteLucrativeIndependanteMontant
     */
    public String getRevenuActiviteLucrativeIndependanteMontant() {
        return revenuActiviteLucrativeIndependanteMontant;
    }

    /**
     * @return the revenuHypothetiqueMontantDeductionsLPP
     */
    public String getRevenuHypothetiqueMontantDeductionsLPP() {
        return revenuHypothetiqueMontantDeductionsLPP;
    }

    /**
     * @return the revenuHypothetiqueMontantDeductionsSociales
     */
    public String getRevenuHypothetiqueMontantDeductionsSociales() {
        return revenuHypothetiqueMontantDeductionsSociales;
    }

    /**
     * @return the revenuHypothetiqueMontantFraisGarde
     */
    public String getRevenuHypothetiqueMontantFraisGarde() {
        return revenuHypothetiqueMontantFraisGarde;
    }

    /**
     * @return the revenuHypothetiqueMontantRevenuBrut
     */
    public String getRevenuHypothetiqueMontantRevenuBrut() {
        return revenuHypothetiqueMontantRevenuBrut;
    }

    /**
     * @return the revenuHypothetiqueMontantRevenuNet
     */
    public String getRevenuHypothetiqueMontantRevenuNet() {
        return revenuHypothetiqueMontantRevenuNet;
    }

    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getTaxeJournaliereDateEntreeHome() {
        return taxeJournaliereDateEntreeHome;
    }

    public String getTaxeJournaliereIdTypeChambre() {
        return taxeJournaliereIdTypeChambre;
    }

    public Boolean getTaxeJournaliereIsParticipationLCA() {
        return taxeJournaliereIsParticipationLCA;
    }

    public String getTaxeJournaliereMontantJournalierLCA() {
        return taxeJournaliereMontantJournalierLCA;
    }

    public String getTaxeJournalierePrimeAPayer() {
        return taxeJournalierePrimeAPayer;
    }

    /**
     * @return the titreCsTypePropriete
     */
    public String getTitreCsTypePropriete() {
        return titreCsTypePropriete;
    }

    /**
     * @return the titreDroitGarde
     */
    public String getTitreDroitGarde() {
        return titreDroitGarde;
    }

    public String getTitreFractionDenominateur() {
        return titreFractionDenominateur;
    }

    public String getTitreFractionNumerateur() {
        return titreFractionNumerateur;
    }

    /**
     * @return the titreIsSansRendement
     */
    public Boolean getTitreIsSansRendement() {
        return titreIsSansRendement;
    }

    /**
     * @return the titreMontant
     */
    public String getTitreMontant() {
        return titreMontant;
    }

    /**
     * @return the titreRendement
     */
    public String getTitreRendement() {
        return titreRendement;
    }

    public String getVehiculeCsTypePropriete() {
        return vehiculeCsTypePropriete;
    }

    public String getVehiculeFractionDenominateur() {
        return vehiculeFractionDenominateur;
    }

    public String getVehiculeFractionNumerateur() {
        return vehiculeFractionNumerateur;
    }

    /**
     * @return the vehiculeMontant
     */
    public String getVehiculeMontant() {
        return vehiculeMontant;
    }

    /**
     * @param allocationFamilialeMontantMensuel
     *            the allocationFamilialeMontantMensuel to set
     */
    public void setAllocationFamilialeMontantMensuel(String allocationFamilialeMontantMensuel) {
        this.allocationFamilialeMontantMensuel = allocationFamilialeMontantMensuel;
    }

    /**
     * @param aPIAVSAIMontant
     *            the aPIAVSAIMontant to set
     */
    public void setAPIAVSAIMontant(String aPIAVSAIMontant) {
        APIAVSAIMontant = aPIAVSAIMontant;
    }

    /**
     * @param aPIAVSCsType
     *            the aPIAVSCsType to set
     */
    public void setAPIAVSCsType(String aPIAVSCsType) {
        APIAVSCsType = aPIAVSCsType;
    }

    /**
     * @param assuranceRenteViagereExcedant
     *            the assuranceRenteViagereExcedant to set
     */
    public void setAssuranceRenteViagereExcedant(String assuranceRenteViagereExcedant) {
        this.assuranceRenteViagereExcedant = assuranceRenteViagereExcedant;
    }

    /**
     * @param assuranceRenteViagereMontant
     *            the assuranceRenteViagereMontant to set
     */
    public void setAssuranceRenteViagereMontant(String assuranceRenteViagereMontant) {
        this.assuranceRenteViagereMontant = assuranceRenteViagereMontant;
    }

    /**
     * @param assuranceRenteViagereMontantValeurRachat
     *            the assuranceRenteViagereMontantValeurRachat to set
     */
    public void setAssuranceRenteViagereMontantValeurRachat(String assuranceRenteViagereMontantValeurRachat) {
        this.assuranceRenteViagereMontantValeurRachat = assuranceRenteViagereMontantValeurRachat;
    }

    /**
     * @param assuranceVieMontantValeurRachat
     *            the assuranceVieMontantValeurRachat to set
     */
    public void setAssuranceVieMontantValeurRachat(String assuranceVieMontantValeurRachat) {
        this.assuranceVieMontantValeurRachat = assuranceVieMontantValeurRachat;
    }

    /**
     * @param autreFortuneMobiliereCsTypeFortune
     *            the autreFortuneMobiliereCsTypeFortune to set
     */
    public void setAutreFortuneMobiliereCsTypeFortune(String autreFortuneMobiliereCsTypeFortune) {
        this.autreFortuneMobiliereCsTypeFortune = autreFortuneMobiliereCsTypeFortune;
    }

    /**
     * @param autreFortuneMobiliereCsTypePropriete
     *            the autreFortuneMobiliereCsTypePropriete to set
     */
    public void setAutreFortuneMobiliereCsTypePropriete(String autreFortuneMobiliereCsTypePropriete) {
        this.autreFortuneMobiliereCsTypePropriete = autreFortuneMobiliereCsTypePropriete;
    }

    public void setAutreFortuneMobiliereFractionDenominateur(String autreFortuneMobiliereFractionDenominateur) {
        this.autreFortuneMobiliereFractionDenominateur = autreFortuneMobiliereFractionDenominateur;
    }

    public void setAutreFortuneMobiliereFractionNumerateur(String autreFortuneMobiliereFractionNumerateur) {
        this.autreFortuneMobiliereFractionNumerateur = autreFortuneMobiliereFractionNumerateur;
    }

    /**
     * @param autreFortuneMobiliereMontant
     *            the autreFortuneMobiliereMontant to set
     */
    public void setAutreFortuneMobiliereMontant(String autreFortuneMobiliereMontant) {
        this.autreFortuneMobiliereMontant = autreFortuneMobiliereMontant;
    }

    public void setAutreRentesEtrangeresCSTypeDevise(String autreRentesEtrangeresCSTypeDevise) {
        this.autreRentesEtrangeresCSTypeDevise = autreRentesEtrangeresCSTypeDevise;
    }

    /**
     * @param autresApiAutre
     *            the autresApiAutre to set
     */
    public void setAutresApiAutre(String autresApiAutre) {
        this.autresApiAutre = autresApiAutre;
    }

    /**
     * @param autresAPICsTypeMontant
     *            the autresAPICsTypeMontant to set
     */
    public void setAutresAPICsTypeMontant(String autresAPICsTypeMontant) {
        this.autresAPICsTypeMontant = autresAPICsTypeMontant;
    }

    /**
     * @param autresAPIMontant
     *            the autresAPIMontant to set
     */
    public void setAutresAPIMontant(String autresAPIMontant) {
        this.autresAPIMontant = autresAPIMontant;
    }

    /**
     * @param autresDettesProuveesMontant
     *            the autresDettesProuveesMontant to set
     */
    public void setAutresDettesProuveesMontant(String autresDettesProuveesMontant) {
        this.autresDettesProuveesMontant = autresDettesProuveesMontant;
    }

    /**
     * @param autresRentesAutreGenre
     *            the autresRentesAutreGenre to set
     */
    public void setAutresRentesAutreGenre(String autresRentesAutreGenre) {
        this.autresRentesAutreGenre = autresRentesAutreGenre;
    }

    /**
     * @param autresRentesCsGenre
     *            the autresRentesCsGenre to set
     */
    public void setAutresRentesCsGenre(String autresRentesCsGenre) {
        this.autresRentesCsGenre = autresRentesCsGenre;
    }

    /**
     * @param autresRentesMontant
     *            the autresRentesMontant to set
     */
    public void setAutresRentesMontant(String autresRentesMontant) {
        this.autresRentesMontant = autresRentesMontant;
    }

    /**
     * @param autresRevenusLibelle
     *            the autresRevenusLibelle to set
     */
    public void setAutresRevenusLibelle(String autresRevenusLibelle) {
        this.autresRevenusLibelle = autresRevenusLibelle;
    }

    /**
     * @param autresRevenusMontant
     *            the autresRevenusMontant to set
     */
    public void setAutresRevenusMontant(String autresRevenusMontant) {
        this.autresRevenusMontant = autresRevenusMontant;
    }

    public void setBetailCsTypePropriete(String betailCsTypePropriete) {
        this.betailCsTypePropriete = betailCsTypePropriete;
    }

    public void setBetailFractionDenominateur(String betailFractionDenominateur) {
        this.betailFractionDenominateur = betailFractionDenominateur;
    }

    public void setBetailFractionNumerateur(String betailFractionNumerateur) {
        this.betailFractionNumerateur = betailFractionNumerateur;
    }

    /**
     * @param betailMontant
     *            the betailMontant to set
     */
    public void setBetailMontant(String betailMontant) {
        this.betailMontant = betailMontant;
    }

    public void setBienImmoAnnexeCsTypePropriete(String bienImmoAnnexeCsTypePropriete) {
        this.bienImmoAnnexeCsTypePropriete = bienImmoAnnexeCsTypePropriete;
    }

    /**
     * @param bienImmoAnnexeMontantDetteHypothecaire
     *            the bienImmoAnnexeMontantDetteHypothecaire to set
     */
    public void setBienImmoAnnexeMontantDetteHypothecaire(String bienImmoAnnexeMontantDetteHypothecaire) {
        this.bienImmoAnnexeMontantDetteHypothecaire = bienImmoAnnexeMontantDetteHypothecaire;
    }

    /**
     * @param bienImmoAnnexeMontantInteretHypothecaire
     *            the bienImmoAnnexeMontantInteretHypothecaire to set
     */
    public void setBienImmoAnnexeMontantInteretHypothecaire(String bienImmoAnnexeMontantInteretHypothecaire) {
        this.bienImmoAnnexeMontantInteretHypothecaire = bienImmoAnnexeMontantInteretHypothecaire;
    }

    /**
     * @param bienImmoAnnexeMontantLoyersEncaisses
     *            the bienImmoAnnexeMontantLoyersEncaisses to set
     */
    public void setBienImmoAnnexeMontantLoyersEncaisses(String bienImmoAnnexeMontantLoyersEncaisses) {
        this.bienImmoAnnexeMontantLoyersEncaisses = bienImmoAnnexeMontantLoyersEncaisses;
    }

    public void setBienImmoAnnexeMontantSousLocation(String bienImmoAnnexeMontantSousLocation) {
        this.bienImmoAnnexeMontantSousLocation = bienImmoAnnexeMontantSousLocation;
    }

    /**
     * @param bienImmoAnnexeMontantValeurLocative
     *            the bienImmoAnnexeMontantValeurLocative to set
     */
    public void setBienImmoAnnexeMontantValeurLocative(String bienImmoAnnexeMontantValeurLocative) {
        this.bienImmoAnnexeMontantValeurLocative = bienImmoAnnexeMontantValeurLocative;
    }

    /**
     * @param bienImmoAnnexeMontantValeurVenale
     *            the bienImmoAnnexeMontantValeurVenale to set
     */
    public void setBienImmoAnnexeMontantValeurVenale(String bienImmoAnnexeMontantValeurVenale) {
        this.bienImmoAnnexeMontantValeurVenale = bienImmoAnnexeMontantValeurVenale;
    }

    public void setBienImmoAnnexePartDenominateur(String bienImmoAnnexePartDenominateur) {
        this.bienImmoAnnexePartDenominateur = bienImmoAnnexePartDenominateur;
    }

    public void setBienImmoAnnexePartNumerateur(String bienImmoAnnexePartNumerateur) {
        this.bienImmoAnnexePartNumerateur = bienImmoAnnexePartNumerateur;
    }

    public void setBienImmoNonHabitableCsTypePropriete(String bienImmoNonHabitableCsTypePropriete) {
        this.bienImmoNonHabitableCsTypePropriete = bienImmoNonHabitableCsTypePropriete;
    }

    /**
     * @param bienImmoNonHabitableMontantDetteHypothecaire
     *            the bienImmoNonHabitableMontantDetteHypothecaire to set
     */
    public void setBienImmoNonHabitableMontantDetteHypothecaire(String bienImmoNonHabitableMontantDetteHypothecaire) {
        this.bienImmoNonHabitableMontantDetteHypothecaire = bienImmoNonHabitableMontantDetteHypothecaire;
    }

    /**
     * @param bienImmoNonHabitableMontantInteretHypothecaire
     *            the bienImmoNonHabitableMontantInteretHypothecaire to set
     */
    public void setBienImmoNonHabitableMontantInteretHypothecaire(String bienImmoNonHabitableMontantInteretHypothecaire) {
        this.bienImmoNonHabitableMontantInteretHypothecaire = bienImmoNonHabitableMontantInteretHypothecaire;
    }

    /**
     * @param bienImmoNonHabitableMontantRendement
     *            the bienImmoNonHabitableMontantRendement to set
     */
    public void setBienImmoNonHabitableMontantRendement(String bienImmoNonHabitableMontantRendement) {
        this.bienImmoNonHabitableMontantRendement = bienImmoNonHabitableMontantRendement;
    }

    /**
     * @param bienImmoNonHabitableMontantValeurVenale
     *            the bienImmoNonHabitableMontantValeurVenale to set
     */
    public void setBienImmoNonHabitableMontantValeurVenale(String bienImmoNonHabitableMontantValeurVenale) {
        this.bienImmoNonHabitableMontantValeurVenale = bienImmoNonHabitableMontantValeurVenale;
    }

    public void setBienImmoNonHabitablePartDenominateur(String bienImmoNonHabitablePartDenominateur) {
        this.bienImmoNonHabitablePartDenominateur = bienImmoNonHabitablePartDenominateur;
    }

    public void setBienImmoNonHabitablePartNumerateur(String bienImmoNonHabitablePartNumerateur) {
        this.bienImmoNonHabitablePartNumerateur = bienImmoNonHabitablePartNumerateur;
    }

    public void setBienImmoPrincipalCSPropriete(String bienImmoPrincipalCSPropriete) {
        this.bienImmoPrincipalCSPropriete = bienImmoPrincipalCSPropriete;
    }

    /**
     * @param bienImmoPrincipalMontantDetteHypothecaire
     *            the bienImmoPrincipalMontantDetteHypothecaire to set
     */
    public void setBienImmoPrincipalMontantDetteHypothecaire(String bienImmoPrincipalMontantDetteHypothecaire) {
        this.bienImmoPrincipalMontantDetteHypothecaire = bienImmoPrincipalMontantDetteHypothecaire;
    }

    /**
     * @param bienImmoPrincipalMontantInteretHypothecaire
     *            the bienImmoPrincipalMontantInteretHypothecaire to set
     */
    public void setBienImmoPrincipalMontantInteretHypothecaire(String bienImmoPrincipalMontantInteretHypothecaire) {
        this.bienImmoPrincipalMontantInteretHypothecaire = bienImmoPrincipalMontantInteretHypothecaire;
    }

    /**
     * @param bienImmoPrincipalMontantLoyersEncaisses
     *            the bienImmoPrincipalMontantLoyersEncaisses to set
     */
    public void setBienImmoPrincipalMontantLoyersEncaisses(String bienImmoPrincipalMontantLoyersEncaisses) {
        this.bienImmoPrincipalMontantLoyersEncaisses = bienImmoPrincipalMontantLoyersEncaisses;
    }

    /**
     * @param bienImmoPrincipalMontantSousLocation
     *            the bienImmoPrincipalMontantSousLocation to set
     */
    public void setBienImmoPrincipalMontantSousLocation(String bienImmoPrincipalMontantSousLocation) {
        this.bienImmoPrincipalMontantSousLocation = bienImmoPrincipalMontantSousLocation;
    }

    /**
     * @param bienImmoPrincipalMontantValeurFiscale
     *            the bienImmoPrincipalMontantValeurFiscale to set
     */
    public void setBienImmoPrincipalMontantValeurFiscale(String bienImmoPrincipalMontantValeurFiscale) {
        this.bienImmoPrincipalMontantValeurFiscale = bienImmoPrincipalMontantValeurFiscale;
    }

    /**
     * @param bienImmoPrincipalMontantValeurLocative
     *            the bienImmoPrincipalMontantValeurLocative to set
     */
    public void setBienImmoPrincipalMontantValeurLocative(String bienImmoPrincipalMontantValeurLocative) {
        this.bienImmoPrincipalMontantValeurLocative = bienImmoPrincipalMontantValeurLocative;
    }

    public void setBienImmoPrincipalNombrePersonnes(String bienImmoPrincipalNombrePersonnes) {
        this.bienImmoPrincipalNombrePersonnes = bienImmoPrincipalNombrePersonnes;
    }

    public void setBienImmoPrincipalPartDenominateur(String bienImmoPrincipalPartDenominateur) {
        this.bienImmoPrincipalPartDenominateur = bienImmoPrincipalPartDenominateur;
    }

    public void setBienImmoPrincipalPartNumerateur(String bienImmoPrincipalPartNumerateur) {
        this.bienImmoPrincipalPartNumerateur = bienImmoPrincipalPartNumerateur;
    }

    public void setCapitalLPPCsTypePropriete(String capitalLPPCsTypePropriete) {
        this.capitalLPPCsTypePropriete = capitalLPPCsTypePropriete;
    }

    public void setCapitalLPPFractionDenominateur(String capitalLPPFractionDenominateur) {
        this.capitalLPPFractionDenominateur = capitalLPPFractionDenominateur;
    }

    public void setCapitalLPPFractionNumerateur(String capitalLPPFractionNumerateur) {
        this.capitalLPPFractionNumerateur = capitalLPPFractionNumerateur;
    }

    /**
     * @param capitalLPPIsSansInteret
     *            the capitalLPPIsSansInteret to set
     */
    public void setCapitalLPPIsSansInteret(Boolean capitalLPPIsSansInteret) {
        this.capitalLPPIsSansInteret = capitalLPPIsSansInteret;
    }

    /**
     * @param capitalLPPMontant
     *            the capitalLPPMontant to set
     */
    public void setCapitalLPPMontant(String capitalLPPMontant) {
        this.capitalLPPMontant = capitalLPPMontant;
    }

    /**
     * @param capitalLPPMontantFrais
     *            the capitalLPPMontantFrais to set
     */
    public void setCapitalLPPMontantFrais(String capitalLPPMontantFrais) {
        this.capitalLPPMontantFrais = capitalLPPMontantFrais;
    }

    /**
     * @param capitalLPPMontantInterets
     *            the capitalLPPMontantInterets to set
     */
    public void setCapitalLPPMontantInterets(String capitalLPPMontantInterets) {
        this.capitalLPPMontantInterets = capitalLPPMontantInterets;
    }

    /**
     * @param compteBancaireCPPCsTypePropriete
     *            the compteBancaireCPPCsTypePropriete to set
     */
    public void setCompteBancaireCPPCsTypePropriete(String compteBancaireCPPCsTypePropriete) {
        this.compteBancaireCPPCsTypePropriete = compteBancaireCPPCsTypePropriete;
    }

    public void setCompteBancaireCPPFractionDenominateur(String compteBancaireCPPFractionDenominateur) {
        this.compteBancaireCPPFractionDenominateur = compteBancaireCPPFractionDenominateur;
    }

    public void setCompteBancaireCPPFractionNumerateur(String compteBancaireCPPFractionNumerateur) {
        this.compteBancaireCPPFractionNumerateur = compteBancaireCPPFractionNumerateur;
    }

    /**
     * @param compteBancaireCPPMontant
     *            the compteBancaireCPPMontant to set
     */
    public void setCompteBancaireCPPMontant(String compteBancaireCPPMontant) {
        this.compteBancaireCPPMontant = compteBancaireCPPMontant;
    }

    /**
     * @param compteBancaireCPPMontantFrais
     *            the compteBancaireCPPMontantFrais to set
     */
    public void setCompteBancaireCPPMontantFrais(String compteBancaireCPPMontantFrais) {
        this.compteBancaireCPPMontantFrais = compteBancaireCPPMontantFrais;
    }

    /**
     * @param compteBancaireCPPMontantInterets
     *            the compteBancaireCPPMontantInterets to set
     */
    public void setCompteBancaireCPPMontantInterets(String compteBancaireCPPMontantInterets) {
        this.compteBancaireCPPMontantInterets = compteBancaireCPPMontantInterets;
    }

    /**
     * @param compteBancaireIsSansInteret
     *            the compteBancaireIsSansInteret to set
     */
    public void setCompteBancaireIsSansInteret(Boolean compteBancaireIsSansInteret) {
        this.compteBancaireIsSansInteret = compteBancaireIsSansInteret;
    }

    /**
     * @param contratEntretienViagerMontant
     *            the contratEntretienViagerMontant to set
     */
    public void setContratEntretienViagerMontant(String contratEntretienViagerMontant) {
        this.contratEntretienViagerMontant = contratEntretienViagerMontant;
    }

    /**
     * @param cotisationPSALMontantAnnuel
     *            the cotisationPSALMontantAnnuel to set
     */
    public void setCotisationPSALMontantAnnuel(String cotisationPSALMontantAnnuel) {
        this.cotisationPSALMontantAnnuel = cotisationPSALMontantAnnuel;
    }

    /**
     * @param csRoleFamille
     *            the csRoleFamille to set
     */
    public void setCsRoleFamille(String csRoleFamille) {
        this.csRoleFamille = csRoleFamille;
    }

    /**
     * @param csTypeDonneeFinanciere
     *            the csTypeDonneeFinanciere to set
     */
    public void setCsTypeDonneeFinanciere(String csTypeDonneeFinanciere) {
        this.csTypeDonneeFinanciere = csTypeDonneeFinanciere;
    }

    /**
     * @param dateDebutDonneeFinanciere
     *            the dateDebutDonneeFinanciere to set
     */
    public void setDateDebutDonneeFinanciere(String dateDebutDonneeFinanciere) {
        this.dateDebutDonneeFinanciere = dateDebutDonneeFinanciere;
    }

    /**
     * @param dateFinDonneeFinanciere
     *            the dateFinDonneeFinanciere to set
     */
    public void setDateFinDonneeFinanciere(String dateFinDonneeFinanciere) {
        this.dateFinDonneeFinanciere = dateFinDonneeFinanciere;
    }

    /**
     * @param dessaisissementFortuneDeductions
     *            the dessaisissementFortuneDeductions to set
     */
    public void setDessaisissementFortuneDeductions(String dessaisissementFortuneDeductions) {
        this.dessaisissementFortuneDeductions = dessaisissementFortuneDeductions;
    }

    /**
     * @param dessaisissementFortuneMontant
     *            the dessaisissementFortuneMontant to set
     */
    public void setDessaisissementFortuneMontant(String dessaisissementFortuneMontant) {
        this.dessaisissementFortuneMontant = dessaisissementFortuneMontant;
    }


    /**
     * @param dessaisissementFortuneType
     *            the dessaisissementFortuneType to set
     */
    public void setDessaisissementFortuneType(String dessaisissementFortuneType) {
        this.dessaisissementFortuneType = dessaisissementFortuneType;
    }

    /**
     * @param dessaisissementRevenuDeductions
     *            the dessaisissementRevenuDeductions to set
     */
    public void setDessaisissementRevenuDeductions(String dessaisissementRevenuDeductions) {
        this.dessaisissementRevenuDeductions = dessaisissementRevenuDeductions;
    }

    /**
     * @param dessaisissementRevenuMontant
     *            the dessaisissementRevenuMontant to set
     */
    public void setDessaisissementRevenuMontant(String dessaisissementRevenuMontant) {
        this.dessaisissementRevenuMontant = dessaisissementRevenuMontant;
    }

    @Override
    public void setId(String id) {
        idMembreFamilleSF = id;
    }

    /**
     * @param idDonneeFinanciereHeader
     *            the idDonneeFinanciereHeader to set
     */
    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    public void setIdDroitMembreFamille(String idDroitMembreFamille) {
        this.idDroitMembreFamille = idDroitMembreFamille;
    }

    /**
     * @param idMembreFamilleSF
     *            the idMembreFamilleSF to set
     */
    public void setIdMembreFamilleSF(String idMembreFamilleSF) {
        this.idMembreFamilleSF = idMembreFamilleSF;
    }

    public void setIJAIJours(String iJAIJours) {
        IJAIJours = iJAIJours;
    }

    /**
     * @param iJAIMontant
     *            the iJAIMontant to set
     */
    public void setIJAIMontant(String iJAIMontant) {
        IJAIMontant = iJAIMontant;
    }

    /**
     * @param iJAPGAutreGenre
     *            the iJAPGAutreGenre to set
     */
    public void setIJAPGAutreGenre(String iJAPGAutreGenre) {
        IJAPGAutreGenre = iJAPGAutreGenre;
    }

    public void setIJAPGcotisationLPPMens(String iJAPGcotisationLPPMens) {
        IJAPGcotisationLPPMens = iJAPGcotisationLPPMens;
    }

    public void setIJAPGgainIntAnnuel(String iJAPGgainIntAnnuel) {
        IJAPGgainIntAnnuel = iJAPGgainIntAnnuel;
    }

    /**
     * @param iJAPGGenre
     *            the iJAPGGenre to set
     */
    public void setIJAPGGenre(String iJAPGGenre) {
        IJAPGGenre = iJAPGGenre;
    }

    public void setIJAPGLPP(String iJAPGLPP) {
        IJAPGLPP = iJAPGLPP;
    }

    /**
     * @param iJAPGMontant
     *            the iJAPGMontant to set
     */
    public void setIJAPGMontant(String iJAPGMontant) {
        IJAPGMontant = iJAPGMontant;
    }

    public void setIJAPGMontantChomage(String iJAPGMontantChomage) {
        IJAPGMontantChomage = iJAPGMontantChomage;
    }

    public void setIJAPGnbJours(String iJAPGnbJours) {
        IJAPGnbJours = iJAPGnbJours;
    }

    public void setIJAPGtauxAA(String iJAPGtauxAA) {
        IJAPGtauxAA = iJAPGtauxAA;
    }

    public void setIJAPGtauxAVS(String iJAPGtauxAVS) {
        IJAPGtauxAVS = iJAPGtauxAVS;
    }

    public void setIsDessaisissementFortune(Boolean isDessaisissementFortune) {
        this.isDessaisissementFortune = isDessaisissementFortune;
    }

    public void setIsDessaisissementRevenu(Boolean isDessaisissementRevenu) {
        this.isDessaisissementRevenu = isDessaisissementRevenu;
    }

    public void setTypeDessaisissementFortune(String typeDessaisissementFortune){
        this.typeDessaisissementFortune = typeDessaisissementFortune;
    }

    /**
     * @param loyerCsTypeLoyer
     *            the loyerCsTypeLoyer to set
     */
    public void setLoyerCsTypeLoyer(String loyerCsTypeLoyer) {
        this.loyerCsTypeLoyer = loyerCsTypeLoyer;
    }

    /**
     * @param loyerIsFauteuilRoulant
     *            the loyerIsFauteuilRoulant to set
     */
    public void setLoyerIsFauteuilRoulant(Boolean loyerIsFauteuilRoulant) {
        this.loyerIsFauteuilRoulant = loyerIsFauteuilRoulant;
    }

    /**
     * @param loyerIsTenueMenage
     *            the loyerIsTenueMenage to set
     */
    public void setLoyerIsTenueMenage(Boolean loyerIsTenueMenage) {
        this.loyerIsTenueMenage = loyerIsTenueMenage;
    }

    /**
     * @param loyerMontant
     *            the loyerMontant to set
     */
    public void setLoyerMontant(String loyerMontant) {
        this.loyerMontant = loyerMontant;
    }

    /**
     * @param loyerMontantCharges
     *            the loyerMontantCharges to set
     */
    public void setLoyerMontantCharges(String loyerMontantCharges) {
        this.loyerMontantCharges = loyerMontantCharges;
    }

    public void setLoyerMontantSousLocations(String loyerMontantSousLocations) {
        this.loyerMontantSousLocations = loyerMontantSousLocations;
    }

    /**
     * @param loyerNbPersonnes
     *            the loyerNbPersonnes to set
     */
    public void setLoyerNbPersonnes(String loyerNbPersonnes) {
        this.loyerNbPersonnes = loyerNbPersonnes;
    }

    public void setLoyerTaxeJournalierePensionNonReconnue(String loyerTaxeJournalierePensionNonReconnue) {
        this.loyerTaxeJournalierePensionNonReconnue = loyerTaxeJournalierePensionNonReconnue;
    }

    public void setLoyerCsDeplafonnementAppartementPartage(String loyerCsDeplafonnementAppartementPartage) {
        this.loyerCsDeplafonnementAppartementPartage = loyerCsDeplafonnementAppartementPartage;
    }

    public void setMarchandiseStockCsTypePropriete(String marchandiseStockCsTypePropriete) {
        this.marchandiseStockCsTypePropriete = marchandiseStockCsTypePropriete;
    }

    public void setMarchandiseStockFractionDenominateur(String marchandiseStockFractionDenominateur) {
        this.marchandiseStockFractionDenominateur = marchandiseStockFractionDenominateur;
    }

    public void setMarchandiseStockFractionNumerateur(String marchandiseStockFractionNumerateur) {
        this.marchandiseStockFractionNumerateur = marchandiseStockFractionNumerateur;
    }

    /**
     * @param marchandiseStockMontant
     *            the marchandiseStockMontant to set
     */
    public void setMarchandiseStockMontant(String marchandiseStockMontant) {
        this.marchandiseStockMontant = marchandiseStockMontant;
    }

    /**
     * @param numeraireCsTypePropriete
     *            the numeraireCsTypePropriete to set
     */
    public void setNumeraireCsTypePropriete(String numeraireCsTypePropriete) {
        this.numeraireCsTypePropriete = numeraireCsTypePropriete;
    }

    public void setNumeraireFractionDenominateur(String numeraireFractionDenominateur) {
        this.numeraireFractionDenominateur = numeraireFractionDenominateur;
    }

    public void setNumeraireFractionNumerateur(String numeraireFractionNumerateur) {
        this.numeraireFractionNumerateur = numeraireFractionNumerateur;
    }

    /**
     * @param numeraireIsSansInteret
     *            the numeraireIsSansInteret to set
     */
    public void setNumeraireIsSansInteret(Boolean numeraireIsSansInteret) {
        this.numeraireIsSansInteret = numeraireIsSansInteret;
    }

    /**
     * @param numeraireMontant
     *            the numeraireMontant to set
     */
    public void setNumeraireMontant(String numeraireMontant) {
        this.numeraireMontant = numeraireMontant;
    }

    /**
     * @param numeraireMontantInterets
     *            the numeraireMontantInterets to set
     */
    public void setNumeraireMontantInterets(String numeraireMontantInterets) {
        this.numeraireMontantInterets = numeraireMontantInterets;
    }

    /**
     * @param pensionAlimentaireCsTypePension
     *            the pensionAlimentaireCsTypePension to set
     */
    public void setPensionAlimentaireCsTypePension(String pensionAlimentaireCsTypePension) {
        this.pensionAlimentaireCsTypePension = pensionAlimentaireCsTypePension;
    }

    /**
     * @param pensionAlimentaireIsDeductionsRenteEnfant
     *            the pensionAlimentaireIsDeductionsRenteEnfant to set
     */
    public void setPensionAlimentaireIsDeductionsRenteEnfant(Boolean pensionAlimentaireIsDeductionsRenteEnfant) {
        this.pensionAlimentaireIsDeductionsRenteEnfant = pensionAlimentaireIsDeductionsRenteEnfant;
    }

    public void setPensionAlimentaireLienParente(String pensionAlimentaireLienParente) {
        this.pensionAlimentaireLienParente = pensionAlimentaireLienParente;
    }

    /**
     * @param pensionAlimentaireMontant
     *            the pensionAlimentaireMontant to set
     */
    public void setPensionAlimentaireMontant(String pensionAlimentaireMontant) {
        this.pensionAlimentaireMontant = pensionAlimentaireMontant;
    }

    public void setPensionAlimentaireMontantRenteEnfant(String pensionAlimentaireMontantRenteEnfant) {
        this.pensionAlimentaireMontantRenteEnfant = pensionAlimentaireMontantRenteEnfant;
    }

    public void setPretEnversTiersCsTypePropriete(String pretEnversTiersCsTypePropriete) {
        this.pretEnversTiersCsTypePropriete = pretEnversTiersCsTypePropriete;
    }

    /**
     * @param pretEnversTiersIsSansInteret
     *            the pretEnversTiersIsSansInteret to set
     */
    public void setPretEnversTiersIsSansInteret(Boolean pretEnversTiersIsSansInteret) {
        this.pretEnversTiersIsSansInteret = pretEnversTiersIsSansInteret;
    }

    /**
     * @param pretEnversTiersMontant
     *            the pretEnversTiersMontant to set
     */
    public void setPretEnversTiersMontant(String pretEnversTiersMontant) {
        this.pretEnversTiersMontant = pretEnversTiersMontant;
    }

    /**
     * @param pretEnversTiersMontantInterets
     *            the pretEnversTiersMontantInterets to set
     */
    public void setPretEnversTiersMontantInterets(String pretEnversTiersMontantInterets) {
        this.pretEnversTiersMontantInterets = pretEnversTiersMontantInterets;
    }

    /**
     * @param pretEnversTiersPartProprieteDenominateur
     *            the pretEnversTiersPartProprieteDenominateur to set
     */
    public void setPretEnversTiersPartProprieteDenominateur(String pretEnversTiersPartProprieteDenominateur) {
        this.pretEnversTiersPartProprieteDenominateur = pretEnversTiersPartProprieteDenominateur;
    }

    /**
     * @param pretEnversTiersPartProprieteNumerateur
     *            the pretEnversTiersPartProprieteNumerateur to set
     */
    public void setPretEnversTiersPartProprieteNumerateur(String pretEnversTiersPartProprieteNumerateur) {
        this.pretEnversTiersPartProprieteNumerateur = pretEnversTiersPartProprieteNumerateur;
    }

    /**
     * @param renteAVSAICsType
     *            the renteAVSAICsType to set
     */
    public void setRenteAVSAICsType(String renteAVSAICsType) {
        this.renteAVSAICsType = renteAVSAICsType;
    }

    public void setRenteAVSAICsTypeSansRente(String renteAVSAICsTypeSansRente) {
        this.renteAVSAICsTypeSansRente = renteAVSAICsTypeSansRente;
    }

    /**
     * @param renteAVSAIMontant
     *            the renteAVSAIMontant to set
     */
    public void setRenteAVSAIMontant(String renteAVSAIMontant) {
        this.renteAVSAIMontant = renteAVSAIMontant;
    }

    /**
     * @param revenuActiviteLucrativeDependanteDeductionsLPP
     *            the revenuActiviteLucrativeDependanteDeductionsLPP to set
     */
    public void setRevenuActiviteLucrativeDependanteDeductionsLPP(String revenuActiviteLucrativeDependanteDeductionsLPP) {
        this.revenuActiviteLucrativeDependanteDeductionsLPP = revenuActiviteLucrativeDependanteDeductionsLPP;
    }

    /**
     * @param revenuActiviteLucrativeDependanteDeductionsSociales
     *            the revenuActiviteLucrativeDependanteDeductionsSociales to set
     */
    public void setRevenuActiviteLucrativeDependanteDeductionsSociales(
            String revenuActiviteLucrativeDependanteDeductionsSociales) {
        this.revenuActiviteLucrativeDependanteDeductionsSociales = revenuActiviteLucrativeDependanteDeductionsSociales;
    }

    /**
     * @param revenuActiviteLucrativeDependanteMontant
     *            the revenuActiviteLucrativeDependanteMontant to set
     */
    public void setRevenuActiviteLucrativeDependanteMontant(String revenuActiviteLucrativeDependanteMontant) {
        this.revenuActiviteLucrativeDependanteMontant = revenuActiviteLucrativeDependanteMontant;
    }

    /**
     * @param revenuActiviteLucrativeDependanteMontantFraisEffectifs
     *            the revenuActiviteLucrativeDependanteMontantFraisEffectifs to set
     */
    public void setRevenuActiviteLucrativeDependanteMontantFraisEffectifs(
            String revenuActiviteLucrativeDependanteMontantFraisEffectifs) {
        this.revenuActiviteLucrativeDependanteMontantFraisEffectifs = revenuActiviteLucrativeDependanteMontantFraisEffectifs;
    }

    public void setRevenuActiviteLucrativeIndependanteCSGenreRevenu(
            String revenuActiviteLucrativeIndependanteCSGenreRevenu) {
        this.revenuActiviteLucrativeIndependanteCSGenreRevenu = revenuActiviteLucrativeIndependanteCSGenreRevenu;
    }

    /**
     * @param revenuActiviteLucrativeIndependanteMontant
     *            the revenuActiviteLucrativeIndependanteMontant to set
     */
    public void setRevenuActiviteLucrativeIndependanteMontant(String revenuActiviteLucrativeIndependanteMontant) {
        this.revenuActiviteLucrativeIndependanteMontant = revenuActiviteLucrativeIndependanteMontant;
    }

    /**
     * @param revenuHypothetiqueMontantDeductionsLPP
     *            the revenuHypothetiqueMontantDeductionsLPP to set
     */
    public void setRevenuHypothetiqueMontantDeductionsLPP(String revenuHypothetiqueMontantDeductionsLPP) {
        this.revenuHypothetiqueMontantDeductionsLPP = revenuHypothetiqueMontantDeductionsLPP;
    }

    /**
     * @param revenuHypothetiqueMontantDeductionsSociales
     *            the revenuHypothetiqueMontantDeductionsSociales to set
     */
    public void setRevenuHypothetiqueMontantDeductionsSociales(String revenuHypothetiqueMontantDeductionsSociales) {
        this.revenuHypothetiqueMontantDeductionsSociales = revenuHypothetiqueMontantDeductionsSociales;
    }

    /**
     * @param revenuHypothetiqueMontantFraisGarde
     *            the revenuHypothetiqueMontantFraisGarde to set
     */
    public void setRevenuHypothetiqueMontantFraisGarde(String revenuHypothetiqueMontantFraisGarde) {
        this.revenuHypothetiqueMontantFraisGarde = revenuHypothetiqueMontantFraisGarde;
    }

    /**
     * @param revenuHypothetiqueMontantRevenuBrut
     *            the revenuHypothetiqueMontantRevenuBrut to set
     */
    public void setRevenuHypothetiqueMontantRevenuBrut(String revenuHypothetiqueMontantRevenuBrut) {
        this.revenuHypothetiqueMontantRevenuBrut = revenuHypothetiqueMontantRevenuBrut;
    }

    /**
     * @param revenuHypothetiqueMontantRevenuNet
     *            the revenuHypothetiqueMontantRevenuNet to set
     */
    public void setRevenuHypothetiqueMontantRevenuNet(String revenuHypothetiqueMontantRevenuNet) {
        this.revenuHypothetiqueMontantRevenuNet = revenuHypothetiqueMontantRevenuNet;
    }

    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub
    }

    public void setTaxeJournaliereDateEntreeHome(String taxeJournaliereDateEntreeHome) {
        this.taxeJournaliereDateEntreeHome = taxeJournaliereDateEntreeHome;
    }

    public void setTaxeJournaliereIdTypeChambre(String taxeJournaliereIdTypeChambre) {
        this.taxeJournaliereIdTypeChambre = taxeJournaliereIdTypeChambre;
    }

    public void setTaxeJournaliereIsParticipationLCA(Boolean taxeJournaliereIsParticipationLCA) {
        this.taxeJournaliereIsParticipationLCA = taxeJournaliereIsParticipationLCA;
    }

    public void setTaxeJournaliereMontantJournalierLCA(String taxeJournaliereMontantJournalierLCA) {
        this.taxeJournaliereMontantJournalierLCA = taxeJournaliereMontantJournalierLCA;
    }

    public void setTaxeJournalierePrimeAPayer(String taxeJournalierePrimeAPayer) {
        this.taxeJournalierePrimeAPayer = taxeJournalierePrimeAPayer;
    }

    /**
     * @param titreCsTypePropriete
     *            the titreCsTypePropriete to set
     */
    public void setTitreCsTypePropriete(String titreCsTypePropriete) {
        this.titreCsTypePropriete = titreCsTypePropriete;
    }

    /**
     * @param titreDroitGarde
     *            the titreDroitGarde to set
     */
    public void setTitreDroitGarde(String titreDroitGarde) {
        this.titreDroitGarde = titreDroitGarde;
    }

    public void setTitreFractionDenominateur(String titreFractionDenominateur) {
        this.titreFractionDenominateur = titreFractionDenominateur;
    }

    public void setTitreFractionNumerateur(String titreFractionNumerateur) {
        this.titreFractionNumerateur = titreFractionNumerateur;
    }

    /**
     * @param titreIsSansRendement
     *            the titreIsSansRendement to set
     */
    public void setTitreIsSansRendement(Boolean titreIsSansRendement) {
        this.titreIsSansRendement = titreIsSansRendement;
    }

    /**
     * @param titreMontant
     *            the titreMontant to set
     */
    public void setTitreMontant(String titreMontant) {
        this.titreMontant = titreMontant;
    }

    /**
     * @param titreRendement
     *            the titreRendement to set
     */
    public void setTitreRendement(String titreRendement) {
        this.titreRendement = titreRendement;
    }

    public void setVehiculeCsTypePropriete(String vehiculeCsTypePropriete) {
        this.vehiculeCsTypePropriete = vehiculeCsTypePropriete;
    }

    public void setVehiculeFractionDenominateur(String vehiculeFractionDenominateur) {
        this.vehiculeFractionDenominateur = vehiculeFractionDenominateur;
    }

    public void setVehiculeFractionNumerateur(String vehiculeFractionNumerateur) {
        this.vehiculeFractionNumerateur = vehiculeFractionNumerateur;
    }

    /**
     * @param vehiculeMontant
     *            the vehiculeMontant to set
     */
    public void setVehiculeMontant(String vehiculeMontant) {
        this.vehiculeMontant = vehiculeMontant;
    }

    public Boolean getIsBienImmoPrincipalDeMoinsDe10Ans() {
        return isBienImmoPrincipalDeMoinsDe10Ans;
    }

    public void setIsBienImmoPrincipalDeMoinsDe10Ans(Boolean bienImmoPrincipalDeMoinsDe10Ans) {
        isBienImmoPrincipalDeMoinsDe10Ans = bienImmoPrincipalDeMoinsDe10Ans;
    }

    public String getIdLocalite() {
        return idLocalite;
    }

    public void setIdLocalite(String idLocalite) {
        this.idLocalite = idLocalite;
    }

    public String getBienImmoPrincipalIdLocalite() {
        return bienImmoPrincipalIdLocalite;
    }

    public void setBienImmoPrincipalIdLocalite(String bienImmoPrincipalIdLocalite) {
        this.bienImmoPrincipalIdLocalite = bienImmoPrincipalIdLocalite;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return CalculDonneesCC.class.getSimpleName() + "(" + csTypeDonneeFinanciere + ")@" + hashCode();
    }

    public String getImputationFortune() {
        return imputationFortune;
    }

    public void setImputationFortune(String imputationFortune) {
        this.imputationFortune = imputationFortune;
    }
    public String getFraisGardeLibelle() {
        return fraisGardeLibelle;
    }

    public void setFraisGardeLibelle(String fraisGardeLibelle) {
        this.fraisGardeLibelle = fraisGardeLibelle;
    }

    public String getFraisGardeMontant() {
        return fraisGardeMontant;
    }

    public void setFraisGardeMontant(String fraisGardeMontant) {
        this.fraisGardeMontant = fraisGardeMontant;
    }

    public String getNbTotalFamille() {
        return nbTotalFamille;
    }

    public void setNbTotalFamille(String nbTotalFamille) {
        this.nbTotalFamille = nbTotalFamille;
    }

    public String getPrimeAssuranceMaladieMontant() {
        return primeAssuranceMaladieMontant;
    }

    public void setPrimeAssuranceMaladieMontant(String primeAssuranceMaladieMontant) {
        this.primeAssuranceMaladieMontant = primeAssuranceMaladieMontant;
    }

    public String getSubsideAssuranceMaladieMontant() {
        return subsideAssuranceMaladieMontant;
    }

    public void setSubsideAssuranceMaladieMontant(String subsideAssuranceMaladieMontant) {
        this.subsideAssuranceMaladieMontant = subsideAssuranceMaladieMontant;
    }

    public String getRevenuActiviteLucrativeDependanteMontantFraisDeGarde() {
        return revenuActiviteLucrativeDependanteMontantFraisDeGarde;
    }

    public void setRevenuActiviteLucrativeDependanteMontantFraisDeGarde(String revenuActiviteLucrativeDependanteMontantFraisDeGarde) {
        this.revenuActiviteLucrativeDependanteMontantFraisDeGarde = revenuActiviteLucrativeDependanteMontantFraisDeGarde;
    }
    public String getRevenuActiviteLucrativeIndependanteMontantFraisDeGarde() {
        return revenuActiviteLucrativeIndependanteMontantFraisDeGarde;
    }

    public void setRevenuActiviteLucrativeIndependanteMontantFraisDeGarde(String revenuActiviteLucrativeIndependanteMontantFraisDeGarde) {
        this.revenuActiviteLucrativeIndependanteMontantFraisDeGarde = revenuActiviteLucrativeIndependanteMontantFraisDeGarde;
    }

    public String getSejourMoisPartielPrixJournalier() {
        return sejourMoisPartielPrixJournalier;
    }

    public void setSejourMoisPartielPrixJournalier(String sejourMoisPartielPrixJournalier) {
        this.sejourMoisPartielPrixJournalier = sejourMoisPartielPrixJournalier;
    }

    public String getSejourMoisPartielFraisNourriture() {
        return sejourMoisPartielFraisNourriture;
    }

    public void setSejourMoisPartielFraisNourriture(String sejourMoisPartielFraisNourriture) {
        this.sejourMoisPartielFraisNourriture = sejourMoisPartielFraisNourriture;
    }

    public String getSejourMoisPartielNombreJour() {
        return sejourMoisPartielNombreJour;
    }

    public void setSejourMoisPartielNombreJour(String sejourMoisPartielNombreJour) {
        this.sejourMoisPartielNombreJour = sejourMoisPartielNombreJour;
    }

    public Boolean getSejourMoisPartielVersementDirect() {
        return sejourMoisPartielVersementDirect;
    }

    public void setSejourMoisPartielVersementDirect(Boolean sejourMoisPartielVersementDirect) {
        this.sejourMoisPartielVersementDirect = sejourMoisPartielVersementDirect;
    }

    public String getSejourMoisPartielHome() {
        return sejourMoisPartielHome;
    }

    public void setSejourMoisPartielHome(String sejourMoisPartielHome) {
        this.sejourMoisPartielHome = sejourMoisPartielHome;
    }
}
