package ch.globaz.pegasus.businessimpl.services;

import ch.globaz.pegasus.business.models.assurancemaladie.SimplePrimeAssuranceMaladie;
import ch.globaz.pegasus.business.services.models.assurancemaladie.*;
import ch.globaz.pegasus.business.services.models.calcul.*;
import ch.globaz.pegasus.business.services.models.creancier.SimpleCreancierHystoriqueService;
import ch.globaz.pegasus.business.services.models.habitat.*;
import ch.globaz.pegasus.business.services.models.revenusdepenses.*;
import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.chrysaor.ChrysaorService;
import ch.globaz.pegasus.business.services.decompte.DecompteService;
import ch.globaz.pegasus.business.services.models.annonce.annoncelaprams.AnnonceLapramsService;
import ch.globaz.pegasus.business.services.models.annonce.annoncelaprams.PrepareAnnonceLapramsService;
import ch.globaz.pegasus.business.services.models.annonce.annoncelaprams.SimpleAnnonceLapramsDoFinHService;
import ch.globaz.pegasus.business.services.models.annonce.annoncelaprams.SimpleAnnonceLapramsService;
import ch.globaz.pegasus.business.services.models.annonce.annoncelaprams.SimpleSequenceService;
import ch.globaz.pegasus.business.services.models.annonce.communicationocc.GenerationCommunicationOCCService;
import ch.globaz.pegasus.business.services.models.annonce.communicationocc.SimpleCommunicationOCCService;
import ch.globaz.pegasus.business.services.models.avance.AvanceService;
import ch.globaz.pegasus.business.services.models.blocage.DeblocageDetteService;
import ch.globaz.pegasus.business.services.models.blocage.DeblocageService;
import ch.globaz.pegasus.business.services.models.blocage.PcaBloqueService;
import ch.globaz.pegasus.business.services.models.blocage.SimpleLigneDeblocageService;
import ch.globaz.pegasus.business.services.models.creancier.SimpleCreanceAccordeeService;
import ch.globaz.pegasus.business.services.models.creancier.SimpleCreancierService;
import ch.globaz.pegasus.business.services.models.decision.CleanDecisionsService;
import ch.globaz.pegasus.business.services.models.decision.CopiesDecisionService;
import ch.globaz.pegasus.business.services.models.decision.DecisionApresCalculService;
import ch.globaz.pegasus.business.services.models.decision.DecisionBusinessService;
import ch.globaz.pegasus.business.services.models.decision.DecisionHeaderService;
import ch.globaz.pegasus.business.services.models.decision.DecisionRefusService;
import ch.globaz.pegasus.business.services.models.decision.DecisionService;
import ch.globaz.pegasus.business.services.models.decision.DecisionSuppressionService;
import ch.globaz.pegasus.business.services.models.decision.SimpleAnnexesDecisionService;
import ch.globaz.pegasus.business.services.models.decision.SimpleCopiesDecisionService;
import ch.globaz.pegasus.business.services.models.decision.SimpleDecisionApresCalculService;
import ch.globaz.pegasus.business.services.models.decision.SimpleDecisionHeaderService;
import ch.globaz.pegasus.business.services.models.decision.SimpleDecisionRefusService;
import ch.globaz.pegasus.business.services.models.decision.SimpleDecisionSuppressionService;
import ch.globaz.pegasus.business.services.models.decision.SimpleValidationService;
import ch.globaz.pegasus.business.services.models.decision.ValidationDecisionService;
import ch.globaz.pegasus.business.services.models.demande.SimpleDemandeService;
import ch.globaz.pegasus.business.services.models.dessaisissement.DessaisissementFortuneAutoService;
import ch.globaz.pegasus.business.services.models.dessaisissement.DessaisissementFortuneService;
import ch.globaz.pegasus.business.services.models.dessaisissement.DessaisissementRevenuAutoService;
import ch.globaz.pegasus.business.services.models.dessaisissement.DessaisissementRevenuService;
import ch.globaz.pegasus.business.services.models.dessaisissement.DessaisissementService;
import ch.globaz.pegasus.business.services.models.dessaisissement.SimpleDessaisissementFortuneService;
import ch.globaz.pegasus.business.services.models.dessaisissement.SimpleDessaisissementRevenuService;
import ch.globaz.pegasus.business.services.models.dettecomptatcompense.SimpleDetteComptatCompenseService;
import ch.globaz.pegasus.business.services.models.dossier.SimpleDossierService;
import ch.globaz.pegasus.business.services.models.droit.DonneeFinanciereHeaderService;
import ch.globaz.pegasus.business.services.models.droit.DonneesPersonnellesService;
import ch.globaz.pegasus.business.services.models.droit.DroitBusinessService;
import ch.globaz.pegasus.business.services.models.droit.DroitMembreFamilleEtenduService;
import ch.globaz.pegasus.business.services.models.droit.DroitMembreFamilleService;
import ch.globaz.pegasus.business.services.models.droit.SimpleDonneeFinanciereHeaderService;
import ch.globaz.pegasus.business.services.models.droit.SimpleDonneesPersonnellesService;
import ch.globaz.pegasus.business.services.models.droit.SimpleDroitMembreFamilleService;
import ch.globaz.pegasus.business.services.models.droit.SimpleDroitService;
import ch.globaz.pegasus.business.services.models.droit.SimpleVersionDroitService;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.AssuranceRenteViagereService;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.AutreFortuneMobiliereService;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.BetailService;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.FortuneParticuliereService;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.MarchandisesStockService;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.NumeraireService;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.PretEnversTiersService;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.SimpleAssuranceRenteViagereService;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.SimpleAutreFortuneMobiliereService;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.SimpleBetailService;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.SimpleMarchandisesStockService;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.SimpleNumeraireService;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.SimplePretEnversTiersService;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.SimpleVehiculeService;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.VehiculeService;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.AssuranceVieService;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.AutresDettesProuveesService;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.BienImmobilierHabitationNonPrincipaleService;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.BienImmobilierNonHabitableService;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.BienImmobilierServantHabitationPrincipaleService;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.CapitalLPPService;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.CompteBancaireCCPService;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.FortuneUsuelleService;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.SimpleAssuranceVieService;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.SimpleAutresDettesProuveesService;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.SimpleBienImmobilierHabitationNonPrincipaleService;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.SimpleBienImmobilierNonHabitableService;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.SimpleBienImmobilierServantHabitationPrincipaleService;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.SimpleCapitalLPPService;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.SimpleCompteBancaireCCPService;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.SimpleTitreService;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.TitreService;
import ch.globaz.pegasus.business.services.models.home.ChambreMedicaliseeService;
import ch.globaz.pegasus.business.services.models.home.PeriodeServiceEtatService;
import ch.globaz.pegasus.business.services.models.home.PrixChambreService;
import ch.globaz.pegasus.business.services.models.home.SimpleHomeService;
import ch.globaz.pegasus.business.services.models.home.SimplePeriodeServiceEtatService;
import ch.globaz.pegasus.business.services.models.home.SimplePrixChambreService;
import ch.globaz.pegasus.business.services.models.home.SimpleTypeChambreService;
import ch.globaz.pegasus.business.services.models.home.TypeChambreService;
import ch.globaz.pegasus.business.services.models.lot.SimpleOrdreVersementService;
import ch.globaz.pegasus.business.services.models.lot.SimplePrestationService;
import ch.globaz.pegasus.business.services.models.monnaieetrangere.SimpleMonnaieEtrangereService;
import ch.globaz.pegasus.business.services.models.mutation.MutationPcaService;
import ch.globaz.pegasus.business.services.models.mutation.MutationService;
import ch.globaz.pegasus.business.services.models.parametre.ConversionRenteService;
import ch.globaz.pegasus.business.services.models.parametre.SimpleConversionRenteService;
import ch.globaz.pegasus.business.services.models.parametre.SimpleForfaitPrimesAssuranceMaladieService;
import ch.globaz.pegasus.business.services.models.parametre.SimpleLienZoneLocaliteService;
import ch.globaz.pegasus.business.services.models.parametre.SimpleZoneForfaitsService;
import ch.globaz.pegasus.business.services.models.pcaccordee.AllocationDeNoelService;
import ch.globaz.pegasus.business.services.models.pcaccordee.PCAccordeeService;
import ch.globaz.pegasus.business.services.models.pcaccordee.SimpleAllocationDeNoelService;
import ch.globaz.pegasus.business.services.models.pcaccordee.SimpleJoursAppointService;
import ch.globaz.pegasus.business.services.models.pcaccordee.SimplePCAccordeeService;
import ch.globaz.pegasus.business.services.models.pcaccordee.SimplePersonneDansPlanCalculService;
import ch.globaz.pegasus.business.services.models.pcaccordee.SimplePlanDeCalculService;
import ch.globaz.pegasus.business.services.models.pcaccordee.SimplePrestationAccordeeService;
import ch.globaz.pegasus.business.services.models.renteijapi.AllocationImpotentService;
import ch.globaz.pegasus.business.services.models.renteijapi.AutreApiService;
import ch.globaz.pegasus.business.services.models.renteijapi.AutreRenteService;
import ch.globaz.pegasus.business.services.models.renteijapi.IjApgService;
import ch.globaz.pegasus.business.services.models.renteijapi.IndemniteJournaliereAiService;
import ch.globaz.pegasus.business.services.models.renteijapi.RenteAvsAiService;
import ch.globaz.pegasus.business.services.models.renteijapi.RenteIjApiService;
import ch.globaz.pegasus.business.services.models.renteijapi.SimpleAllocationImpotentService;
import ch.globaz.pegasus.business.services.models.renteijapi.SimpleAutreApiService;
import ch.globaz.pegasus.business.services.models.renteijapi.SimpleAutreRenteService;
import ch.globaz.pegasus.business.services.models.renteijapi.SimpleIjApgService;
import ch.globaz.pegasus.business.services.models.renteijapi.SimpleIndemniteJournaliereAiService;
import ch.globaz.pegasus.business.services.models.renteijapi.SimpleRenteAvsAiService;
import ch.globaz.pegasus.business.services.models.transfert.SimpleTransfertDossierSuppressionService;
import ch.globaz.pegasus.business.services.models.variablemetier.SimpleVariableMetierService;
import ch.globaz.pegasus.business.services.process.adaptation.AdaptationService;
import ch.globaz.pegasus.business.services.process.adaptation.RenteAdapationDemandeService;
import ch.globaz.pegasus.business.services.process.adaptation.SimpleDemandeCentraleService;
import ch.globaz.pegasus.business.services.process.adaptation.SimpleRenteAdaptationService;
import ch.globaz.pegasus.business.services.revisionquadriennale.RevisionQuadriennaleService;

/**
 * @author ECO
 */
public abstract class PegasusImplServiceLocator extends PegasusServiceLocator {
    /**
     * Retourne le service de gestion du process de l'adaptation
     * 
     * @return Le service pour l'adaptation
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static AdaptationService getAdaptationService() throws JadeApplicationServiceNotAvailableException {
        return (AdaptationService) JadeApplicationServiceLocator.getInstance().getServiceImpl(AdaptationService.class);
    }

    public static ChrysaorService getChrysaorService() throws JadeApplicationServiceNotAvailableException {
        return (ChrysaorService) JadeApplicationServiceLocator.getInstance().getServiceImpl(ChrysaorService.class);
    }

    public static AllocationDeNoelService getAllocationDeNoelService()
            throws JadeApplicationServiceNotAvailableException {
        return (AllocationDeNoelService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                AllocationDeNoelService.class);
    }

    public static RevisionQuadriennaleService getRevisionQuadriennaleService()
            throws JadeApplicationServiceNotAvailableException {
        return (RevisionQuadriennaleService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                RevisionQuadriennaleService.class);
    }

    /**
     * 
     * @return Implémentation du srevice d'allocation impotent
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static AllocationImpotentService getAllocationImpotentService()
            throws JadeApplicationServiceNotAvailableException {
        return (AllocationImpotentService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                AllocationImpotentService.class);

    }

    public static AllocationsFamilialesEtenduService getAllocationsFamilialesEtenduService()
            throws JadeApplicationServiceNotAvailableException {
        return (AllocationsFamilialesEtenduService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                AllocationsFamilialesEtenduService.class);
    }

    /**
     * @return Implémentation du service de gestion des revenus/depenses allocations familiales
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static AllocationsFamilialesService getAllocationsFamilialesService()
            throws JadeApplicationServiceNotAvailableException {
        return (AllocationsFamilialesService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                AllocationsFamilialesService.class);
    }

    /**
     * @return Implémentation du service de gestion des annonces LAPRAMS
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static AnnonceLapramsService getAnnonceLapramsService() throws JadeApplicationServiceNotAvailableException {
        return (AnnonceLapramsService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                AnnonceLapramsService.class);
    }

    /**
     * @return Implémentation du service de gestion de la fortune particuliere assurance rente viegere
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static AssuranceRenteViagereService getAssuranceRenteViagereService()
            throws JadeApplicationServiceNotAvailableException {
        return (AssuranceRenteViagereService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                AssuranceRenteViagereService.class);
    }

    public static AssuranceVieService getAssuranceVieService() throws JadeApplicationServiceNotAvailableException {
        return (AssuranceVieService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                AssuranceVieService.class);
    }

    /**
     * @return Implémentation du service apiService
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static AutreApiService getAutreApiService() throws JadeApplicationServiceNotAvailableException {
        return (AutreApiService) JadeApplicationServiceLocator.getInstance().getServiceImpl(AutreApiService.class);
    }

    /**
     * @return Implémentation du service de gestion de la fortune particuliere autre fortune mobiliere
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static AutreFortuneMobiliereService getAutreFortuneMobiliereService()
            throws JadeApplicationServiceNotAvailableException {
        return (AutreFortuneMobiliereService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                AutreFortuneMobiliereService.class);
    }

    public static AutreRenteCalculService getAutreRenteCalculService()
            throws JadeApplicationServiceNotAvailableException {
        return (AutreRenteCalculService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                AutreRenteCalculService.class);
    }

    /**
     * @return Implémentation du service AutreRenteService
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static AutreRenteService getAutreRenteService() throws JadeApplicationServiceNotAvailableException {
        return (AutreRenteService) JadeApplicationServiceLocator.getInstance().getServiceImpl(AutreRenteService.class);
    }

    public static AutresDettesProuveesService getAutresDettesProuveesService()
            throws JadeApplicationServiceNotAvailableException {
        return (AutresDettesProuveesService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                AutresDettesProuveesService.class);
    }

    /**
     * @return Implémentation du service de gestion des revenus/depenses AutresRevenus
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static AutresRevenusService getAutresRevenusService() throws JadeApplicationServiceNotAvailableException {
        return (AutresRevenusService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                AutresRevenusService.class);
    }

    public static AvanceService getAvanceService() throws JadeApplicationServiceNotAvailableException {
        return (AvanceService) JadeApplicationServiceLocator.getInstance().getServiceImpl(AvanceService.class);
    }

    /**
     * @return Implémentation du service de gestion de la fortune particuliere betail
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static BetailService getBetailService() throws JadeApplicationServiceNotAvailableException {
        return (BetailService) JadeApplicationServiceLocator.getInstance().getServiceImpl(BetailService.class);
    }

    public static BienImmobilierHabitationNonPrincipaleService getBienImmobilierHabitationNonPrincipaleService()
            throws JadeApplicationServiceNotAvailableException {
        return (BienImmobilierHabitationNonPrincipaleService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(BienImmobilierHabitationNonPrincipaleService.class);
    }

    public static BienImmobilierNonHabitableService getBienImmobilierNonHabitableService()
            throws JadeApplicationServiceNotAvailableException {
        return (BienImmobilierNonHabitableService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                BienImmobilierNonHabitableService.class);
    }

    public static BienImmobilierServantHabitationPrincipaleService getBienImmobilierServantHabitationPrincipaleService()
            throws JadeApplicationServiceNotAvailableException {
        return (BienImmobilierServantHabitationPrincipaleService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(BienImmobilierServantHabitationPrincipaleService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static CalculComparatifService getCalculComparatifService()
            throws JadeApplicationServiceNotAvailableException {
        return (CalculComparatifService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CalculComparatifService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static CalculDonneesDroitService getCalculDonneesDroitService()
            throws JadeApplicationServiceNotAvailableException {
        return (CalculDonneesDroitService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CalculDonneesDroitService.class);
    }
    public static CalculDonneesFraisGardeService getCalculDonneesFraisGardeService()
            throws JadeApplicationServiceNotAvailableException {
        return (CalculDonneesFraisGardeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CalculDonneesFraisGardeService.class);
    }

    public static CalculDonneesCCService getCalculDonneesEnfantsService()
            throws JadeApplicationServiceNotAvailableException {
        return (CalculDonneesCCService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CalculDonneesCCService.class);
    }

    public static CalculDonneesHomeService getCalculDonneesHomeService()
            throws JadeApplicationServiceNotAvailableException {
        return (CalculDonneesHomeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CalculDonneesHomeService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static CalculDroitService getCalculDroitService() throws JadeApplicationServiceNotAvailableException {
        return (CalculDroitService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(CalculDroitService.class);
    }

    /**
     * 
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static CalculMembreFamilleService getCalculMembreFamilleService()
            throws JadeApplicationServiceNotAvailableException {
        return (CalculMembreFamilleService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CalculMembreFamilleService.class);
    }

    public static CalculMoisSuivantService getCalculMoisSuivantService()
            throws JadeApplicationServiceNotAvailableException {
        return (CalculMoisSuivantService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CalculMoisSuivantService.class);
    }

    public static CalculPersistanceService getCalculPersistanceService()
            throws JadeApplicationServiceNotAvailableException {
        return (CalculPersistanceService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CalculPersistanceService.class);
    }

    public static CalculVariableMetierService getCalculVariableMetierService()
            throws JadeApplicationServiceNotAvailableException {
        return (CalculVariableMetierService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CalculVariableMetierService.class);
    }

    public static CapitalLPPService getCapitalLPPService() throws JadeApplicationServiceNotAvailableException {
        return (CapitalLPPService) JadeApplicationServiceLocator.getInstance().getServiceImpl(CapitalLPPService.class);
    }

    /**
     * @return Implémentation du service de recherche des chambres médicalisées d'un home
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static ChambreMedicaliseeService getChambreMedicaliseeService()
            throws JadeApplicationServiceNotAvailableException {
        return (ChambreMedicaliseeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                ChambreMedicaliseeService.class);
    }

    /**
     * 
     * @return Implémentation du service de nettoyge des decisionApresCalcul
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static CleanDecisionsService getCleanDecisionService() throws JadeApplicationServiceNotAvailableException {
        return (CleanDecisionsService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CleanDecisionsService.class);
    }

    public static CompteBancaireCCPService getCompteBancaireCCPService()
            throws JadeApplicationServiceNotAvailableException {
        return (CompteBancaireCCPService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CompteBancaireCCPService.class);
    }

    /**
     * @return Implémentation du service de gestion des revenus/dépenses : contrat entretien viager
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static ContratEntretienViagerService getContratEntretienViagerService()
            throws JadeApplicationServiceNotAvailableException {
        return (ContratEntretienViagerService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                ContratEntretienViagerService.class);
    }

    /**
     * @return Implémentation du service ConversionRente
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static ConversionRenteService getConversionRente() throws JadeApplicationServiceNotAvailableException {
        return (ConversionRenteService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                ConversionRenteService.class);
    }

    public static CopiesDecisionService getCopiesDecisionsService() throws JadeApplicationServiceNotAvailableException {
        return (CopiesDecisionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CopiesDecisionService.class);
    }

    /**
     * @return Implémentation du service de gestion des revenus/depenses CotisationsPsal
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static CotisationsPsalService getCotisationsPsalService() throws JadeApplicationServiceNotAvailableException {
        return (CotisationsPsalService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CotisationsPsalService.class);
    }




    public static DeblocageDetteService getDeblocageDetteService() throws JadeApplicationServiceNotAvailableException {
        return (DeblocageDetteService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DeblocageDetteService.class);

    }

    public static DeblocageService getDeblocageService() throws JadeApplicationServiceNotAvailableException {
        return (DeblocageService) JadeApplicationServiceLocator.getInstance().getServiceImpl(DeblocageService.class);
    }

    public static DecisionApresCalculService getDecisionApresCalculService()
            throws JadeApplicationServiceNotAvailableException {
        return (DecisionApresCalculService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DecisionApresCalculService.class);
    }

    /**
     * Retourne le service de gestion des décisions pour le rcListe
     * 
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static DecisionBusinessService getDecisionBusinessService()
            throws JadeApplicationServiceNotAvailableException {
        return (DecisionBusinessService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DecisionBusinessService.class);
    }

    /**
     * Retourne l'implémentation pour le service de gestion des header des décisions
     * 
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static DecisionHeaderService getDecisionHeaderService() throws JadeApplicationServiceNotAvailableException {
        return (DecisionHeaderService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DecisionHeaderService.class);
    }

    public static DecisionRefusService getDecisionRefusService() throws JadeApplicationServiceNotAvailableException {
        return (DecisionRefusService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DecisionRefusService.class);
    }

    /**
     * Reourne l'implémentation du servce des décisions
     * 
     * @return implémentation du servce des décisions
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static DecisionService getDecisionService() throws JadeApplicationServiceNotAvailableException {
        return (DecisionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(DecisionService.class);
    }

    public static DecisionSuppressionService getDecisionSuppressionService()
            throws JadeApplicationServiceNotAvailableException {
        return (DecisionSuppressionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DecisionSuppressionService.class);
    }

    public static DecompteService getDecompteService() throws JadeApplicationServiceNotAvailableException {
        return (DecompteService) JadeApplicationServiceLocator.getInstance().getServiceImpl(DecompteService.class);
    }

    public static DessaisissementFortuneAutoService getDessaisissementFortuneAutoService()
            throws JadeApplicationServiceNotAvailableException {
        return (DessaisissementFortuneAutoService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DessaisissementFortuneAutoService.class);
    }

    public static DessaisissementFortuneService getDessaisissementFortuneService()
            throws JadeApplicationServiceNotAvailableException {
        return (DessaisissementFortuneService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DessaisissementFortuneService.class);
    }

    public static DessaisissementRevenuAutoService getDessaisissementRevenuAutoService()
            throws JadeApplicationServiceNotAvailableException {
        return (DessaisissementRevenuAutoService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DessaisissementRevenuAutoService.class);
    }

    public static DessaisissementRevenuService getDessaisissementRevenuService()
            throws JadeApplicationServiceNotAvailableException {
        return (DessaisissementRevenuService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DessaisissementRevenuService.class);
    }

    /**
     * @return Implémentation du service de gestion de la fortune particuliere vehicule
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static DessaisissementService getDessaisissementService() throws JadeApplicationServiceNotAvailableException {
        return (DessaisissementService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DessaisissementService.class);
    }

    public static DonneeFinanciereHeaderService getDonneeFinanciereHeaderService()
            throws JadeApplicationServiceNotAvailableException {
        return (DonneeFinanciereHeaderService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DonneeFinanciereHeaderService.class);
    }

    /**
     * @return Implémentation du service de gestion des données personnelles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static DonneesPersonnellesService getDonneesPersonnellesService()
            throws JadeApplicationServiceNotAvailableException {
        return (DonneesPersonnellesService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DonneesPersonnellesService.class);
    }

    /**
     * @return Implémentation du service de gestion des enfantDansCalcul
     * @throws JadeApplicationServiceNotAvailableException
     *             /**
     * @return Implémentation du service de gestion de droit
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static DroitBusinessService getDroitBusinessService() throws JadeApplicationServiceNotAvailableException {
        return (DroitBusinessService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DroitBusinessService.class);
    }

    /**
     * @return Implémentation du service DroitMembreFamilleEtendu
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static DroitMembreFamilleEtenduService getDroitMembreFamilleEtenduService()
            throws JadeApplicationServiceNotAvailableException {
        return (DroitMembreFamilleEtenduService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DroitMembreFamilleEtenduService.class);
    }

    /*
     * public static EnfantDansCalculService getEnfantDansCalculService() throws
     * JadeApplicationServiceNotAvailableException { return (EnfantDansCalculService) JadeApplicationServiceLocator
     * .getInstance().getServiceImpl(EnfantDansCalculService.class); }
     */

    /**
     * @return Implémentation du service de gestion de droitMembreFamille
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static DroitMembreFamilleService getDroitMembreFamilleService()
            throws JadeApplicationServiceNotAvailableException {
        return (DroitMembreFamilleService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DroitMembreFamilleService.class);
    }

    /**
     * @return Implémentation du service de gestion fortuneParticuliereService
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static FortuneParticuliereService getFortuneParticuliereService()
            throws JadeApplicationServiceNotAvailableException {
        return (FortuneParticuliereService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                FortuneParticuliereService.class);
    }

    /**
     * Retourne l'implémentation du service de fortuneUsuelle
     * 
     * @return L'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static FortuneUsuelleService getFortuneUsuelleService() throws JadeApplicationServiceNotAvailableException {
        return (FortuneUsuelleService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                FortuneUsuelleService.class);
    }

    /*
     * public static EnfantDansCalculService getEnfantDansCalculService() throws
     * JadeApplicationServiceNotAvailableException { return (EnfantDansCalculService) JadeApplicationServiceLocator
     * .getInstance().getServiceImpl(EnfantDansCalculService.class); }
     */

    /**
     * Retourn l'implémentation du service de Communication OCC
     * 
     * @return L'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static GenerationCommunicationOCCService getGenerationCommunicationOCCService()
            throws JadeApplicationServiceNotAvailableException {
        return (GenerationCommunicationOCCService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                GenerationCommunicationOCCService.class);
    }

    /**
     * @return Implémentation du service de gestion de l' habitat
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static HabitatService getHabitatService() throws JadeApplicationServiceNotAvailableException {
        return (HabitatService) JadeApplicationServiceLocator.getInstance().getServiceImpl(HabitatService.class);
    }

    /**
     * @return Implémentation du service IjApg
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static IjApgService getIjApgService() throws JadeApplicationServiceNotAvailableException {
        return (IjApgService) JadeApplicationServiceLocator.getInstance().getServiceImpl(IjApgService.class);
    }

    public static IndemniteJournaliereAiService getIndemniteJournaliereAiService()
            throws JadeApplicationServiceNotAvailableException {
        return (IndemniteJournaliereAiService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                IndemniteJournaliereAiService.class);
    }

    /**
     * @return Implémentation du service de gestion habitat pour le loyer
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static LoyerService getLoyerService() throws JadeApplicationServiceNotAvailableException {
        return (LoyerService) JadeApplicationServiceLocator.getInstance().getServiceImpl(LoyerService.class);
    }

    /**
     * @return Implémentation du service de gestion assurance maladie
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static AssuranceMaladieService getAssuranceMaladieService() throws JadeApplicationServiceNotAvailableException {
        return (AssuranceMaladieService) JadeApplicationServiceLocator.getInstance().getServiceImpl(AssuranceMaladieService.class);
    }

    /**
     * @return Implémentation du service de gestion de la fortune particuliere marchandisesStock
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static MarchandisesStockService getMarchandisesStockService()
            throws JadeApplicationServiceNotAvailableException {
        return (MarchandisesStockService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                MarchandisesStockService.class);
    }

    /**
     * 
     * @return Implémentation du srevice des mutation pca
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static MutationPcaService getMutationPcaService() throws JadeApplicationServiceNotAvailableException {
        return (MutationPcaService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(MutationPcaService.class);

    }

    /**
     * Retourne le service des mutations
     * 
     * @return Le service pour les mutations
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static MutationService getMutationService() throws JadeApplicationServiceNotAvailableException {
        return (MutationService) JadeApplicationServiceLocator.getInstance().getServiceImpl(MutationService.class);

    }

    /**
     * @return Implémentation du service de gestion de la fortune particuliere numeraire
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static NumeraireService getNumeraireService() throws JadeApplicationServiceNotAvailableException {
        return (NumeraireService) JadeApplicationServiceLocator.getInstance().getServiceImpl(NumeraireService.class);
    }

    public static PcaBloqueService getPcaBloqueService() throws JadeApplicationServiceNotAvailableException {
        return (PcaBloqueService) JadeApplicationServiceLocator.getInstance().getServiceImpl(PcaBloqueService.class);
    }

    /**
     * @return Implémentation du service de gestion des PCAccordees
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static PCAccordeeService getPCAccordeeService() throws JadeApplicationServiceNotAvailableException {
        return (PCAccordeeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(PCAccordeeService.class);
    }

    /**
     * @return Implémentation du service de gestion des revenus/depenses PensionAlimentaire
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static PensionAlimentaireService getPensionAlimentaireService()
            throws JadeApplicationServiceNotAvailableException {
        return (PensionAlimentaireService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                PensionAlimentaireService.class);
    }

    /**
     * @return Implémentation du service de gestion des periodeServiceEtat d'un home
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static PeriodeServiceEtatService getPeriodeServiceEtatService()
            throws JadeApplicationServiceNotAvailableException {
        return (PeriodeServiceEtatService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                PeriodeServiceEtatService.class);
    }

    public static PeriodesService getPeriodesService() throws JadeApplicationServiceNotAvailableException {
        return (PeriodesService) JadeApplicationServiceLocator.getInstance().getServiceImpl(PeriodesService.class);
    }

    /**
     * Retourn l'implémentation du service de Annonce LAPRAMS
     * 
     * @return L'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static PrepareAnnonceLapramsService getPrepareAnnonceLapramsService()
            throws JadeApplicationServiceNotAvailableException {
        return (PrepareAnnonceLapramsService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                PrepareAnnonceLapramsService.class);
    }

    /**
     * @return Implémentation du service de gestion de la fortune particuliere pretEnversTiers
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static PretEnversTiersService getPretEnversTiersService() throws JadeApplicationServiceNotAvailableException {
        return (PretEnversTiersService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                PretEnversTiersService.class);
    }

    /**
     * @return Implémentation du service de gestion des prixChambre d'un home
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static PrixChambreService getPrixChambreService() throws JadeApplicationServiceNotAvailableException {
        return (PrixChambreService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(PrixChambreService.class);
    }

    /**
     * @return Implémentation du service RenteAdapationDemande
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static RenteAdapationDemandeService getRenteAdapationDemandeService()
            throws JadeApplicationServiceNotAvailableException {
        return (RenteAdapationDemandeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                RenteAdapationDemandeService.class);
    }

    public static RenteAvsAiService getRenteAvsAiService() throws JadeApplicationServiceNotAvailableException {
        return (RenteAvsAiService) JadeApplicationServiceLocator.getInstance().getServiceImpl(RenteAvsAiService.class);
    }

    public static RenteIjApiService getRenteIjApiService() throws JadeApplicationServiceNotAvailableException {
        return (RenteIjApiService) JadeApplicationServiceLocator.getInstance().getServiceImpl(RenteIjApiService.class);
    }

    /**
     * @return Implémentation du service de gestion des revenus/depenses revenuActiviteLucrativeDependante
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static RevenuActiviteLucrativeDependanteService getRevenuActiviteLucrativeDependanteService()
            throws JadeApplicationServiceNotAvailableException {
        return (RevenuActiviteLucrativeDependanteService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                RevenuActiviteLucrativeDependanteService.class);
    }

    public static RevenuActiviteLucrativeIndependanteEtenduService getRevenuActiviteLucrativeIndependanteEtenduService()
            throws JadeApplicationServiceNotAvailableException {
        return (RevenuActiviteLucrativeIndependanteEtenduService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(RevenuActiviteLucrativeIndependanteEtenduService.class);
    }

    /**
     * @return Implémentation du service de gestion des revenus/depenses revenuActiviteLucrativeDependante
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static RevenuActiviteLucrativeIndependanteService getRevenuActiviteLucrativeIndependanteService()
            throws JadeApplicationServiceNotAvailableException {
        return (RevenuActiviteLucrativeIndependanteService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                RevenuActiviteLucrativeIndependanteService.class);
    }

    /**
     * @return Implémentation du service de gestion des revenus/dépenses : revenu hypothetique
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static RevenuHypothetiqueService getRevenuHypothetiqueService()
            throws JadeApplicationServiceNotAvailableException {
        return (RevenuHypothetiqueService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                RevenuHypothetiqueService.class);
    }

    /**
     * Retourne l'implémentation du service de revenuDepense
     * 
     * @return L'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static RevenusDepenseService getRevenusDepensesService() throws JadeApplicationServiceNotAvailableException {
        return (RevenusDepenseService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                RevenusDepenseService.class);
    }

    public static SimpleAllocationDeNoelService getSimpleAllocationDeNoelService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleAllocationDeNoelService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleAllocationDeNoelService.class);
    }

    /**
     * Retourne l'implémentation du service simple des allocations impotents
     * 
     * @return l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleAllocationImpotentService getSimpleAllocationImpotentService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleAllocationImpotentService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleAllocationImpotentService.class);
    }

    public static SimpleAllocationsFamilialesService getSimpleAllocationsFamilialesService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleAllocationsFamilialesService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleAllocationsFamilialesService.class);
    }

    public static SimpleAnnexesDecisionService getSimpleAnnexesDecisionService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleAnnexesDecisionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleAnnexesDecisionService.class);
    }

    /**
     * @return Implémentation du service de gestion des annonces LAPRAMS-Donnée financière Header
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleAnnonceLapramsDoFinHService getSimpleAnnonceLapramsDoFinHService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleAnnonceLapramsDoFinHService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleAnnonceLapramsDoFinHService.class);
    }

    /**
     * @return Implémentation du service de gestion des annonces LAPRAMS
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleAnnonceLapramsService getSimpleAnnonceLapramsService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleAnnonceLapramsService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleAnnonceLapramsService.class);
    }

    /**
     * @return Implémentation du service de gestion des simpleAssuranceRenteViagere
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleAssuranceRenteViagereService getSimpleAssuranceRenteViagereService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleAssuranceRenteViagereService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleAssuranceRenteViagereService.class);
    }

    /**
     * @return Implémentation du service de gestion des revenus/dépenses : allocations familiales
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleAssuranceVieService getSimpleAssuranceVieService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleAssuranceVieService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleAssuranceVieService.class);
    }

    /**
     * @return Implémentation du service SimpleAutreApi
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleAutreApiService getSimpleAutreApiService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleAutreApiService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleAutreApiService.class);
    }

    /**
     * @return Implémentation du service de gestion des simpleAutreFortuneMobiliere
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleAutreFortuneMobiliereService getSimpleAutreFortuneMobiliereService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleAutreFortuneMobiliereService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleAutreFortuneMobiliereService.class);
    }

    /**
     * @return Implémentation du service pour les type simpleAutreRente
     * 
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleAutreRenteService getSimpleAutreRenteService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleAutreRenteService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleAutreRenteService.class);
    }

    public static SimpleAutresDettesProuveesService getSimpleAutresDettesProuveesService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleAutresDettesProuveesService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleAutresDettesProuveesService.class);
    }

    /**
     * @return Implémentation du service de gestion des revenus/dépenses : autres revenus
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleAutresRevenusService getSimpleAutresRevenusService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleAutresRevenusService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleAutresRevenusService.class);
    }

    /**
     * @return Implémentation du service de gestion des simpleBetail
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleBetailService getSimpleBetailService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleBetailService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleBetailService.class);
    }

    public static SimpleBienImmobilierHabitationNonPrincipaleService getSimpleBienImmobilierHabitationNonPrincipaleService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleBienImmobilierHabitationNonPrincipaleService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(SimpleBienImmobilierHabitationNonPrincipaleService.class);
    }

    public static SimpleBienImmobilierNonHabitableService getSimpleBienImmobilierNonHabitableService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleBienImmobilierNonHabitableService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleBienImmobilierNonHabitableService.class);
    }

    public static SimpleBienImmobilierServantHabitationPrincipaleService getSimpleBienImmobilierServantHabitationPrincipaleService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleBienImmobilierServantHabitationPrincipaleService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(SimpleBienImmobilierServantHabitationPrincipaleService.class);
    }

    public static SimpleCapitalLPPService getSimpleCapitalLPPService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleCapitalLPPService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleCapitalLPPService.class);
    }

    /**
     * @return Implémentation du service de gestion des communications OCC
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleCommunicationOCCService getSimpleCommunicationOCCService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleCommunicationOCCService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleCommunicationOCCService.class);
    }

    public static SimpleCompteBancaireCCPService getSimpleCompteBancaireCCPService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleCompteBancaireCCPService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleCompteBancaireCCPService.class);
    }

    /**
     * @return Implémentation du service de gestion des revenus/dépenses : contrat entretien viager
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleContratEntretienViagerService getSimpleContratEntretienViagerService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleContratEntretienViagerService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleContratEntretienViagerService.class);
    }

    /**
     * @return Implémentation du service SimpleConversionRente
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleConversionRenteService getSimpleConversionRenteService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleConversionRenteService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleConversionRenteService.class);
    }

    public static SimpleCopiesDecisionService getSimpleCopiesDecisionsService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleCopiesDecisionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleCopiesDecisionService.class);
    }

    /**
     * @return Implémentation du service de gestion des revenus/dépenses : cotisations psal
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleCotisationsPsalService getSimpleCotisationsPsalService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleCotisationsPsalService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleCotisationsPsalService.class);
    }



    /**
     * @return Implémentation du service SimpleCreanceAccordee
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleCreanceAccordeeService getSimpleCreanceAccordeeService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleCreanceAccordeeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleCreanceAccordeeService.class);
    }

    /**
     * @return Implémentation du service SimpleCreancier
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleCreancierService getSimpleCreancierService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleCreancierService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleCreancierService.class);
    }

    public static SimpleCreancierHystoriqueService getSimpleCreancierHystoriqueService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleCreancierHystoriqueService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleCreancierHystoriqueService.class);
    }

    public static SimpleDecisionApresCalculService getSimpleDecisionApresCalculService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleDecisionApresCalculService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDecisionApresCalculService.class);
    }

    public static SimpleDecisionHeaderService getSimpleDecisionHeaderService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleDecisionHeaderService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDecisionHeaderService.class);
    }

    public static SimpleDecisionRefusService getSimpleDecisionRefusService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleDecisionRefusService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDecisionRefusService.class);
    }

    public static SimpleDecisionSuppressionService getSimpleDecisionSuppressionService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleDecisionSuppressionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDecisionSuppressionService.class);
    }

    /**
     * @return Implémentation du service SimpleDemandeCentrale
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleDemandeCentraleService getSimpleDemandeCentraleService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleDemandeCentraleService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDemandeCentraleService.class);
    }

    /**
     * @return Implémentation du service de gestion de la table des types de chambres
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleDemandeService getSimpleDemandeService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleDemandeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDemandeService.class);
    }

    public static SimpleDessaisissementFortuneService getSimpleDessaisissementFortuneService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleDessaisissementFortuneService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDessaisissementFortuneService.class);
    }

    public static SimpleDessaisissementRevenuService getSimpleDessaisissementRevenuService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleDessaisissementRevenuService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDessaisissementRevenuService.class);
    }

    public static SimpleDetteComptatCompenseService getSimpleDetteComptatCompenseService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleDetteComptatCompenseService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDetteComptatCompenseService.class);

    }

    /**
     * @return Implémentation du service de gestion des headers de donnees financieres
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleDonneeFinanciereHeaderService getSimpleDonneeFinanciereHeaderService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleDonneeFinanciereHeaderService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDonneeFinanciereHeaderService.class);
    }

    /**
     * @return Implémentation du service de gestion des données personnelles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleDonneesPersonnellesService getSimpleDonneesPersonnellesService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleDonneesPersonnellesService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDonneesPersonnellesService.class);
    }

    /**
     * @return Implémentation du service de gestion de la table des dossiers
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleDossierService getSimpleDossierService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleDossierService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDossierService.class);
    }

    /**
     * @return Implémentation du service de gestion des simplePCAccordees
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    /*
     * public static SimplePrestationsAccordeesService getSimplePrestationsAccordeesService() throws
     * JadeApplicationServiceNotAvailableException { return (SimplePrestationsAccordeesService)
     * JadeApplicationServiceLocator .getInstance().getServiceImpl( SimplePrestationsAccordeesService.class); }
     */

    /**
     * @return Implémentation du service de gestion de droitMembreFamille
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleDroitMembreFamilleService getSimpleDroitMembreFamilleService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleDroitMembreFamilleService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDroitMembreFamilleService.class);
    }

    /**
     * <<<<<<< PegasusImplServiceLocator.java
     * 
     * @return Implémentation du service de gestion des simplePCAccordees
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    /*
     * public static SimplePrestationsAccordeesService getSimplePrestationsAccordeesService() throws
     * JadeApplicationServiceNotAvailableException { return (SimplePrestationsAccordeesService)
     * JadeApplicationServiceLocator .getInstance().getServiceImpl( SimplePrestationsAccordeesService.class); }
     */

    /**
     * @return Implémentation du service de gestion des simple droits
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleDroitService getSimpleDroitService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleDroitService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(SimpleDroitService.class);
    }

    /**
     * @return Implémentation du service SimpleForfaitPrimesAssuranceMaladie
     * 
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleForfaitPrimesAssuranceMaladieService getSimpleForfaitPrimesAssuranceMaladieService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleForfaitPrimesAssuranceMaladieService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleForfaitPrimesAssuranceMaladieService.class);
    }

    /**
     * @return Implémentation du service de gestion de la table des homes
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleHomeService getSimpleHomeService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleHomeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(SimpleHomeService.class);
    }

    /**
     * @return Implémentation du service SimpleIjApg
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleIjApgService getSimpleIjApgService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleIjApgService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(SimpleIjApgService.class);
    }

    /**
     * @return Implémentation du service de gestion des indemnitesJournalieresAi
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleIndemniteJournaliereAiService getSimpleIndemniteJournaliereAiService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleIndemniteJournaliereAiService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleIndemniteJournaliereAiService.class);
    }

    public static SimpleJoursAppointService getSimpleJoursAppointService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleJoursAppointService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleJoursAppointService.class);
    }

    /**
     * @return Implémentation du service pour les type de contrat entretien viager
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleLibelleContratEntretienViagerService getSimpleLibelleContratEntretienViagerService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleLibelleContratEntretienViagerService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleLibelleContratEntretienViagerService.class);
    }

    /**
     * @return Implémentation du service SimpleLienZoneLocalite
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleLienZoneLocaliteService getSimpleLienZoneLocaliteService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleLienZoneLocaliteService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleLienZoneLocaliteService.class);
    }

    /**
     * Service qui permet de gérer les déblocages
     * 
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleLigneDeblocageService getSimpleLigneDeblocageService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleLigneDeblocageService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleLigneDeblocageService.class);

    }

    /**
     * @return Implémentation du service de gestion des loyers
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleLoyerService getSimpleLoyerService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleLoyerService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(SimpleLoyerService.class);
    }

    /**
     * @return Implémentation du service de gestion des simpleMarchandisesStock
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleMarchandisesStockService getSimpleMarchandisesStockService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleMarchandisesStockService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleMarchandisesStockService.class);
    }

    /**
     * @return Implémentation du service de gestion des monnaies etrangeres
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleMonnaieEtrangereService getSimpleMonnaieEtrangereService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleMonnaieEtrangereService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleMonnaieEtrangereService.class);
    }

    /**
     * @return Implémentation du service de gestion des simpleNumeraire
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleNumeraireService getSimpleNumeraireService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleNumeraireService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleNumeraireService.class);
    }

    /**
     * @return Implémentation du service SimpleOrdreVersement
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleOrdreVersementService getSimpleOrdreVersementService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleOrdreVersementService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleOrdreVersementService.class);
    }

    /**
     * @return Implémentation du service de gestion des simplePCAccordees
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimplePCAccordeeService getSimplePCAccordeeService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimplePCAccordeeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimplePCAccordeeService.class);
    }

    /**
     * @return Implémentation du service de gestion des revenus/dépenses : pension alimentaire
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimplePensionAlimentaireService getSimplePensionAlimentaireService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimplePensionAlimentaireService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimplePensionAlimentaireService.class);
    }

    /**
     * =======
     * 
     * @return Implémentation du service de gestion des simplePeriodeServiceEtat d'un home
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimplePeriodeServiceEtatService getSimplePeriodeServiceEtatService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimplePeriodeServiceEtatService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimplePeriodeServiceEtatService.class);
    }

    /**
     * @return Implémentation du service de gestion des monnaies etrangeres
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimplePersonneDansPlanCalculService getSimplePersonneDansPlanCalculService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimplePersonneDansPlanCalculService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimplePersonneDansPlanCalculService.class);
    }

    /**
     * >>>>>>> 1.7.2.25
     * 
     * @return Implémentation du service de gestion des simplePlanDeCalcul
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimplePlanDeCalculService getSimplePlanDeCalculService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimplePlanDeCalculService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimplePlanDeCalculService.class);
    }

    public static SimplePrestationAccordeeService getSimplePrestatioAccordeeService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimplePrestationAccordeeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimplePrestationAccordeeService.class);
    }

    /**
     * @return Implémentation du service SimplePrestation
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimplePrestationService getSimplePrestationService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimplePrestationService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimplePrestationService.class);
    }

    /**
     * @return Implémentation du service de gestion des simplePretEnversTiers
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimplePretEnversTiersService getSimplePretEnversTiersService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimplePretEnversTiersService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimplePretEnversTiersService.class);
    }

    /**
     * @return Implémentation du service de gestion de la table des prix des chambres
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimplePrixChambreService getSimplePrixChambreService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimplePrixChambreService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimplePrixChambreService.class);
    }

    /**
     * @return Implémentation du service SimpleRenteAdaptation
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleRenteAdaptationService getSimpleRenteAdaptationService1()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleRenteAdaptationService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleRenteAdaptationService.class);
    }

    /**
     * Retourne l'implémentation du service de gestion des rentes avs ai
     * 
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleRenteAvsAiService getSimpleRenteAvsAiService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleRenteAvsAiService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleRenteAvsAiService.class);
    }

    /**
     * @return Implémentation du service de gestion des revenus/dépenses :
     *         SimpleRevenuActiviteLucrativeDependanteService
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleRevenuActiviteLucrativeDependanteService getSimpleRevenuActiviteLucrativeDependanteService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleRevenuActiviteLucrativeDependanteService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(SimpleRevenuActiviteLucrativeDependanteService.class);
    }

    /**
     * @return Implémentation du service de gestion des revenus/dépenses :
     *         SimpleRevenuActiviteLucrativeIndependanteService
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleRevenuActiviteLucrativeIndependanteService getSimpleRevenuActiviteLucrativeIndependanteService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleRevenuActiviteLucrativeIndependanteService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(SimpleRevenuActiviteLucrativeIndependanteService.class);
    }

    /**
     * @return Implémentation du service de gestion des revenus/dépenses : revenu hypothetique
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleRevenuHypothetiqueService getSimpleRevenuHypothetiqueService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleRevenuHypothetiqueService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleRevenuHypothetiqueService.class);
    }

    public static SimpleSequenceService getSimpleSequenceService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleSequenceService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleSequenceService.class);
    }

    /**
     * 
     * @return Implémentation des taxe journlières (home)
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleTaxeJournaliereHomeService getSimpleTaxeJournaliereHomeService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleTaxeJournaliereHomeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleTaxeJournaliereHomeService.class);

    }

    /**
     *
     * @return Implémentation des séjours mois partiel (home)
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleSejourMoisPartielHomeService getSimpleSejourMoisPartielHomeService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleSejourMoisPartielHomeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleSejourMoisPartielHomeService.class);

    }

    public static SimpleTitreService getSimpleTitreService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleTitreService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(SimpleTitreService.class);
    }

    public static SimpleTransfertDossierSuppressionService getSimpleTransfertDossierSuppression()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleTransfertDossierSuppressionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleTransfertDossierSuppressionService.class);
    }

    /**
     * @return Implémentation du service de gestion des types de chambres de home
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleTypeChambreService getSimpleTypeChambreService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleTypeChambreService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleTypeChambreService.class);
    }

    /**
     * @return Implémentation du service pour les type de frais
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleTypeFraisObtentionRevenuService getSimpleTypeFraisObtentionRevenuService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleTypeFraisObtentionRevenuService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleTypeFraisObtentionRevenuService.class);
    }

    public static SimpleValidationService getSimpleValidationService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleValidationService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleValidationService.class);

    }

    /**
     * @return Implémentation du service de gestion des simpleVariableMetier
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleVariableMetierService getSimpleVariableMetierService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleVariableMetierService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleVariableMetierService.class);
    }

    /**
     * @return Implémentation du service de gestion des simpleVehicule
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleVehiculeService getSimpleVehiculeService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleVehiculeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleVehiculeService.class);
    }

    /**
     * @return Implémentation du service de gestion des versions de droit
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleVersionDroitService getSimpleVersionDroitService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleVersionDroitService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleVersionDroitService.class);
    }

    /**
     * @return Implémentation du service SimpleZoneForfaits
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleZoneForfaitsService getSimpleZoneForfaitsService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleZoneForfaitsService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleZoneForfaitsService.class);
    }

    /**
     * 
     * @return Implémentation des taxe journlières (home) étendu
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static TaxeJournaliereHomeEtenduService getTaxeJournaliereHomeEtenduService()
            throws JadeApplicationServiceNotAvailableException {
        return (TaxeJournaliereHomeEtenduService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                TaxeJournaliereHomeEtenduService.class);

    }

    /**
     * 
     * @return Implémentation des taxe journlières (home)
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static TaxeJournaliereHomeService getTaxeJournaliereHomeService()
            throws JadeApplicationServiceNotAvailableException {
        return (TaxeJournaliereHomeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                TaxeJournaliereHomeService.class);

    }

    /**
     *
     * @return Implémentation des séjours mois partiel (home)
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SejourMoisPartielHomeService getSejourMoisPartielHomeService()
            throws JadeApplicationServiceNotAvailableException {
        return (SejourMoisPartielHomeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SejourMoisPartielHomeService.class);

    }

    public static TitreService getTitreService() throws JadeApplicationServiceNotAvailableException {
        return (TitreService) JadeApplicationServiceLocator.getInstance().getServiceImpl(TitreService.class);
    }

    /**
     * @return Implémentation du service de gestion des typeChambre d'un home
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static TypeChambreService getTypeChambreService() throws JadeApplicationServiceNotAvailableException {
        return (TypeChambreService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(TypeChambreService.class);
    }

    /**
     * @return Implémentation du service de gestion des revenus/depenses typeFraisObtentionRevenu
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static TypeFraisObtentionRevenuService getTypeFraisObtentionRevenuService()
            throws JadeApplicationServiceNotAvailableException {
        return (TypeFraisObtentionRevenuService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                TypeFraisObtentionRevenuService.class);
    }

    /**
     * Retourne l'implémentation du service de gestion des validations
     * 
     * @return L'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static ValidationDecisionService getValidationDecisionService()
            throws JadeApplicationServiceNotAvailableException {
        return (ValidationDecisionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                ValidationDecisionService.class);
    }

    /**
     * @return Implémentation du service de gestion de la fortune particuliere vehicule
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static VehiculeService getVehiculeService() throws JadeApplicationServiceNotAvailableException {
        return (VehiculeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(VehiculeService.class);
    }


    //REFORME PC

    public static FraisGardeService getFraisGardeService() throws JadeApplicationServiceNotAvailableException {
        return (FraisGardeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                FraisGardeService.class);
    }
    public static SimpleFraisGardeService getSimpleFraisGardeService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleFraisGardeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleFraisGardeService.class);
    }

    public static PrimeAssuranceMaladieService getPrimeAssuranceMaladieService() throws JadeApplicationServiceNotAvailableException {
        return (PrimeAssuranceMaladieService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                PrimeAssuranceMaladieService.class);
    }
    public static SimplePrimeAssuranceMaladieService getSimplePrimeAssuranceMaladieService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimplePrimeAssuranceMaladieService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimplePrimeAssuranceMaladieService.class);
    }

    public static SubsideAssuranceMaladieService getSubsideAssuranceMaladieService() throws JadeApplicationServiceNotAvailableException {
        return (SubsideAssuranceMaladieService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SubsideAssuranceMaladieService.class);
    }
    public static SimpleSubsideAssuranceMaladieService getSimpleSubsideAssuranceMaladieService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleSubsideAssuranceMaladieService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleSubsideAssuranceMaladieService.class);
    }
}
