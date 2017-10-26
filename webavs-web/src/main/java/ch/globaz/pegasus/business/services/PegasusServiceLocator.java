package ch.globaz.pegasus.business.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.services.annonce.annoncelaprams.AnnonceLapramsBuilderProviderService;
import ch.globaz.pegasus.business.services.annonce.communicationocc.CommunicationOCCBuilderProviderService;
import ch.globaz.pegasus.business.services.chrysaor.ChrysaorService;
import ch.globaz.pegasus.business.services.decision.DecisionBuilderProviderService;
import ch.globaz.pegasus.business.services.decompte.DecompteService;
import ch.globaz.pegasus.business.services.demande.DemandeBuilderProviderService;
import ch.globaz.pegasus.business.services.demanderenseignement.DemandeRenseignementProviderService;
import ch.globaz.pegasus.business.services.doc.excel.ListeDeControleService;
import ch.globaz.pegasus.business.services.models.annonce.annoncelaprams.AnnonceLapramsService;
import ch.globaz.pegasus.business.services.models.annonce.communicationocc.CommunicationOCCService;
import ch.globaz.pegasus.business.services.models.avance.AvanceService;
import ch.globaz.pegasus.business.services.models.blocage.BlocageService;
import ch.globaz.pegasus.business.services.models.blocage.PcaBloqueService;
import ch.globaz.pegasus.business.services.models.blocage.SimpleLigneDeblocageService;
import ch.globaz.pegasus.business.services.models.calcul.CalculMoisSuivantService;
import ch.globaz.pegasus.business.services.models.creancier.CreanceAccordeeService;
import ch.globaz.pegasus.business.services.models.creancier.CreancierService;
import ch.globaz.pegasus.business.services.models.decision.CopiesDecisionService;
import ch.globaz.pegasus.business.services.models.decision.DecisionAdaptationService;
import ch.globaz.pegasus.business.services.models.decision.DecisionApresCalculService;
import ch.globaz.pegasus.business.services.models.decision.DecisionBusinessService;
import ch.globaz.pegasus.business.services.models.decision.DecisionHeaderService;
import ch.globaz.pegasus.business.services.models.decision.DecisionRefusService;
import ch.globaz.pegasus.business.services.models.decision.DecisionService;
import ch.globaz.pegasus.business.services.models.decision.DecisionSuppressionService;
import ch.globaz.pegasus.business.services.models.decision.FortuneService;
import ch.globaz.pegasus.business.services.models.decision.ListDecisionsValideesService;
import ch.globaz.pegasus.business.services.models.decision.SimpleAnnexesDecisionService;
import ch.globaz.pegasus.business.services.models.decision.ValidationDecisionService;
import ch.globaz.pegasus.business.services.models.demande.DemandeService;
import ch.globaz.pegasus.business.services.models.dessaisissement.DessaisissementFortuneService;
import ch.globaz.pegasus.business.services.models.dettecomptatcompense.DetteComptatCompenseService;
import ch.globaz.pegasus.business.services.models.dossier.DossierService;
import ch.globaz.pegasus.business.services.models.droit.DroitService;
import ch.globaz.pegasus.business.services.models.habitat.TaxeJournaliereHomeService;
import ch.globaz.pegasus.business.services.models.home.HomeService;
import ch.globaz.pegasus.business.services.models.lot.LotService;
import ch.globaz.pegasus.business.services.models.lot.OrdreVersementService;
import ch.globaz.pegasus.business.services.models.lot.PrestationService;
import ch.globaz.pegasus.business.services.models.monnaieetrangere.MonnaieEtrangereService;
import ch.globaz.pegasus.business.services.models.parametre.ConversionRenteService;
import ch.globaz.pegasus.business.services.models.pcaccordee.AllocationDeNoelService;
import ch.globaz.pegasus.business.services.models.pcaccordee.PCAccordeeAdaptationImpressionService;
import ch.globaz.pegasus.business.services.models.pcaccordee.PCAccordeeService;
import ch.globaz.pegasus.business.services.models.pcaccordee.PersonneDansPlanCalculService;
import ch.globaz.pegasus.business.services.models.pcaccordee.RetenueService;
import ch.globaz.pegasus.business.services.models.pcaccordee.SimpleAllocationDeNoelService;
import ch.globaz.pegasus.business.services.models.pcaccordee.SimplePCAccordeeService;
import ch.globaz.pegasus.business.services.models.pcaccordee.SimplePlanDeCalculService;
import ch.globaz.pegasus.business.services.models.pmtmensuel.PmtMensuelService;
import ch.globaz.pegasus.business.services.models.renteijapi.RenteIjApiService;
import ch.globaz.pegasus.business.services.models.transfert.TransfertDossierSuppressionService;
import ch.globaz.pegasus.business.services.models.variablemetier.VariableMetierService;
import ch.globaz.pegasus.business.services.process.adaptation.AdaptationService;
import ch.globaz.pegasus.business.services.process.adaptation.RenteAdapationDemandeService;
import ch.globaz.pegasus.business.services.process.adaptation.SimpleRenteAdaptationService;
import ch.globaz.pegasus.business.services.process.statistiquesofas.StatistiquesOFASService;
import ch.globaz.pegasus.business.services.recap.RecapService;
import ch.globaz.pegasus.business.services.revisionquadriennale.RevisionQuadriennaleService;
import ch.globaz.pegasus.business.services.rpc.RpcService;
import ch.globaz.pegasus.business.services.transfertDossier.TransfertDossierBuilderProviderService;
import ch.globaz.pegasus.business.services.transfertDossier.TransfertDossierPCProviderService;
import ch.globaz.pegasus.business.services.transfertDossier.TransfertRentePCProviderService;

public abstract class PegasusServiceLocator {

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

    public static RevisionQuadriennaleService getRevisionQuadriennaleService()
            throws JadeApplicationServiceNotAvailableException {
        return (RevisionQuadriennaleService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                RevisionQuadriennaleService.class);
    }

    public static AllocationDeNoelService getAllocationDeNoelService()
            throws JadeApplicationServiceNotAvailableException {
        return (AllocationDeNoelService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                AllocationDeNoelService.class);
    }

    public static AnnonceLapramsBuilderProviderService getAnnonceLapramsBuilderProviderService()
            throws JadeApplicationServiceNotAvailableException {
        return (AnnonceLapramsBuilderProviderService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                AnnonceLapramsBuilderProviderService.class);
    }

    public static AnnonceLapramsService getAnnonceLapramsService() throws JadeApplicationServiceNotAvailableException {
        return (AnnonceLapramsService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                AnnonceLapramsService.class);
    }

    public static AvanceService getAvanceService() throws JadeApplicationServiceNotAvailableException {
        return (AvanceService) JadeApplicationServiceLocator.getInstance().getServiceImpl(AvanceService.class);
    }

    public static BlocageService getBlocageService() throws JadeApplicationServiceNotAvailableException {
        return (BlocageService) JadeApplicationServiceLocator.getInstance().getServiceImpl(BlocageService.class);
    }

    public static CalculMoisSuivantService getCalculMoisSuivantService()
            throws JadeApplicationServiceNotAvailableException {
        return (CalculMoisSuivantService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CalculMoisSuivantService.class);
    }

    public static CommunicationOCCBuilderProviderService getCommunicationOCCBuilderProviderService()
            throws JadeApplicationServiceNotAvailableException {
        return (CommunicationOCCBuilderProviderService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CommunicationOCCBuilderProviderService.class);
    }

    public static CommunicationOCCService getCommunicationOCCService()
            throws JadeApplicationServiceNotAvailableException {
        return (CommunicationOCCService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CommunicationOCCService.class);
    }

    /**
     * @return Implémentation du service ConversionRente
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static ConversionRenteService getConversionRenteService() throws JadeApplicationServiceNotAvailableException {
        return (ConversionRenteService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                ConversionRenteService.class);
    }

    public static CopiesDecisionService getCopiesDecisionsService() throws JadeApplicationServiceNotAvailableException {
        return (CopiesDecisionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CopiesDecisionService.class);
    }

    /**
     * @return Implémentation du service CreanceAccordee
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static CreanceAccordeeService getCreanceAccordeeService() throws JadeApplicationServiceNotAvailableException {
        return (CreanceAccordeeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CreanceAccordeeService.class);
    }

    /**
     * @return Implémentation du service Creancier
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static CreancierService getCreancierService() throws JadeApplicationServiceNotAvailableException {
        return (CreancierService) JadeApplicationServiceLocator.getInstance().getServiceImpl(CreancierService.class);
    }

    /**
     * @return Implémentation du service DecisionAdaptation
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static DecisionAdaptationService getDecisionAdaptationService()
            throws JadeApplicationServiceNotAvailableException {
        return (DecisionAdaptationService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DecisionAdaptationService.class);
    }

    /**
     * Retourne le service pour les décision apresClacul
     * 
     * @return le service, DecisionApresCalculService
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static DecisionApresCalculService getDecisionApresCalculService()
            throws JadeApplicationServiceNotAvailableException {
        return (DecisionApresCalculService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DecisionApresCalculService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static DecisionBuilderProviderService getDecisionBuilderProvderService()
            throws JadeApplicationServiceNotAvailableException {
        return (DecisionBuilderProviderService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DecisionBuilderProviderService.class);
    }

    /**
     * Retourne le service de gestion des décisions principalement pouzr le rcListe
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
     * Retourne le service pour les headers des décision
     * 
     * @return le service, DecisionHeaderService
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static DecisionHeaderService getDecisionHeaderService() throws JadeApplicationServiceNotAvailableException {
        return (DecisionHeaderService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DecisionHeaderService.class);
    }

    /**
     * Retourne le service pour les décisions de refus
     * 
     * @return le service, DecisionRefusService
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static DecisionRefusService getDecisionRefusService() throws JadeApplicationServiceNotAvailableException {
        return (DecisionRefusService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DecisionRefusService.class);
    }

    /**
     * Retourne l'imlémentation du service des décisions
     * 
     * @return imlémentation du service des décisions
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static DecisionService getDecisionService() throws JadeApplicationServiceNotAvailableException {
        return (DecisionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(DecisionService.class);

    }

    /**
     * Retourne le service pour les décisions de suppression
     * 
     * @return le service, DecisionSuppressionService
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static DecisionSuppressionService getDecisionSuppressionService()
            throws JadeApplicationServiceNotAvailableException {
        return (DecisionSuppressionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DecisionSuppressionService.class);
    }

    /**
     * Implémentation du service de décompte des pca
     * 
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static DecompteService getDecompteService() throws JadeApplicationServiceNotAvailableException {
        return (DecompteService) JadeApplicationServiceLocator.getInstance().getServiceImpl(DecompteService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static DemandeBuilderProviderService getDemandeBuilderProvderService()
            throws JadeApplicationServiceNotAvailableException {
        return (DemandeBuilderProviderService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DemandeBuilderProviderService.class);
    }

    public static DemandeRenseignementProviderService getDemandeRenseignementProviderService()
            throws JadeApplicationServiceNotAvailableException {
        return (DemandeRenseignementProviderService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DemandeRenseignementProviderService.class);
    }

    /**
     * @return Implémentation du service de gestion de demande pc
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static DemandeService getDemandeService() throws JadeApplicationServiceNotAvailableException {
        return (DemandeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(DemandeService.class);
    }

    /**
     * @return Implémentation du service DessaisissementFortune
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static DessaisissementFortuneService getDessaisissementFortuneService()
            throws JadeApplicationServiceNotAvailableException {
        return (DessaisissementFortuneService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DessaisissementFortuneService.class);
    }

    /**
     * 
     * @return Implémentation du service de gestion de la compensation des dettes en comptats
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static DetteComptatCompenseService getDetteComptatCompenseService()
            throws JadeApplicationServiceNotAvailableException {
        return (DetteComptatCompenseService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DetteComptatCompenseService.class);
    }

    /**
     * @return Implémentation du service de gestion de dossier
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static DossierService getDossierService() throws JadeApplicationServiceNotAvailableException {
        return (DossierService) JadeApplicationServiceLocator.getInstance().getServiceImpl(DossierService.class);
    }

    /**
     * @return Implémentation du service de gestion de droit
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static DroitService getDroitService() throws JadeApplicationServiceNotAvailableException {
        return (DroitService) JadeApplicationServiceLocator.getInstance().getServiceImpl(DroitService.class);
    }

    /**
     * @return Implémentation du service de gestion de EnfantDansCalcul
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static PersonneDansPlanCalculService getEnfantDansCalculService()
            throws JadeApplicationServiceNotAvailableException {
        return (PersonneDansPlanCalculService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                PersonneDansPlanCalculService.class);
    }

    /**
     * Retourne le service pour les fortunes
     * 
     * @return le service, DecisionApresCalculService
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static FortuneService getFortuneService() throws JadeApplicationServiceNotAvailableException {
        return (FortuneService) JadeApplicationServiceLocator.getInstance().getServiceImpl(FortuneService.class);
    }

    /**
     * @return Implémentation du service de gestion de home
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static HomeService getHomeService() throws JadeApplicationServiceNotAvailableException {
        return (HomeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(HomeService.class);
    }

    /**
     * Retourne l'implémentation du service de liste des décisions validées (pour la génération de la liste excel du
     * même nom)
     * 
     * @return imlémentation du service des décisions
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static ListDecisionsValideesService getListDecisionsValideesService()
            throws JadeApplicationServiceNotAvailableException {
        return (ListDecisionsValideesService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                ListDecisionsValideesService.class);

    }

    /**
     * @return Implémentation du service des listes de controles
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static ListeDeControleService getListeDeControleService() throws JadeApplicationServiceNotAvailableException {
        return (ListeDeControleService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                ListeDeControleService.class);
    }

    /**
     * @return Implémentation du service des lots (comptabilisation)
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static LotService getLotService() throws JadeApplicationServiceNotAvailableException {
        return (LotService) JadeApplicationServiceLocator.getInstance().getServiceImpl(LotService.class);
    }

    /**
     * @return Implémentation du service de gestion des monnaies etrangeres
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static MonnaieEtrangereService getMonnaieEtrangereService()
            throws JadeApplicationServiceNotAvailableException {
        return (MonnaieEtrangereService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                MonnaieEtrangereService.class);
    }

    /**
     * @return Implémentation du service de gestion des ordres de versement
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static OrdreVersementService getOrdreVersementService() throws JadeApplicationServiceNotAvailableException {
        return (OrdreVersementService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                OrdreVersementService.class);
    }

    /**
     * Implémentation du service de gestion des header de decision
     * 
     * @return l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     */
    // public static SimpleDecisionHeaderService getSimpleDecisionHeaderService()
    // throws JadeApplicationServiceNotAvailableException {
    // return (SimpleDecisionHeaderService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
    // SimpleDecisionHeaderService.class);
    // }

    public static ParametreServicesLocator getParametreServicesLocator()
            throws JadeApplicationServiceNotAvailableException {
        return new ParametreServicesLocator();
    }

    public static PcaBloqueService getPcaBloqueService() throws JadeApplicationServiceNotAvailableException {
        return (PcaBloqueService) JadeApplicationServiceLocator.getInstance().getServiceImpl(PcaBloqueService.class);
    }

    public static PCAccordeeAdaptationImpressionService getPCAccordeeAdaptationImpressionService()
            throws JadeApplicationServiceNotAvailableException {
        return (PCAccordeeAdaptationImpressionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                PCAccordeeAdaptationImpressionService.class);
    }

    /**
     * Implémentation du service de gestion des decisions de refus
     * 
     * @return l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     */
    // public static SimpleDecisionRefusService getSimpleDecisionRefusService()
    // throws JadeApplicationServiceNotAvailableException {
    // return (SimpleDecisionRefusService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
    // SimpleDecisionRefusService.class);
    // }

    /**
     * @return Implémentation du service de gestion de pc accordees
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static PCAccordeeService getPCAccordeeService() throws JadeApplicationServiceNotAvailableException {
        return (PCAccordeeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(PCAccordeeService.class);
    }

    // public static SimpleCopiesDecisionService getSimpleCopiesDecisionsService()
    // throws JadeApplicationServiceNotAvailableException {
    // return (SimpleCopiesDecisionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
    // CopiesDecisionService.class);
    // }

    /**
     * @return Implementation du service lies au paiement mensuel
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static PmtMensuelService getPmtMensuelService() throws JadeApplicationServiceNotAvailableException {
        return (PmtMensuelService) JadeApplicationServiceLocator.getInstance().getServiceImpl(PmtMensuelService.class);
    }

    /**
     * @return Implémentation du service de gestion des Prestations
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static PrestationService getPrestationService() throws JadeApplicationServiceNotAvailableException {
        return (PrestationService) JadeApplicationServiceLocator.getInstance().getServiceImpl(PrestationService.class);
    }

    public static RecapService getRecapService() throws JadeApplicationServiceNotAvailableException {
        return (RecapService) JadeApplicationServiceLocator.getInstance().getServiceImpl(RecapService.class);
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

    /**
     * Implémentation du service de gestion des rentes ijApi
     * 
     * @return l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static RenteIjApiService getRenteIjApiService() throws JadeApplicationServiceNotAvailableException {
        return (RenteIjApiService) JadeApplicationServiceLocator.getInstance().getServiceImpl(RenteIjApiService.class);
    }

    public static RetenueService getRetenueService() throws JadeApplicationServiceNotAvailableException {
        return (RetenueService) JadeApplicationServiceLocator.getInstance().getServiceImpl(RetenueService.class);
    }

    public static SimpleAllocationDeNoelService getSimpleAllocationDeNoelService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleAllocationDeNoelService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleAllocationDeNoelService.class);
    }

    /**
     * Implémentation du service de gestion des annexes de décision
     * 
     * @return l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleAnnexesDecisionService getSimpleAnnexesDecisionsService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleAnnexesDecisionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleAnnexesDecisionService.class);
    }

    public static SimpleLigneDeblocageService getSimpleDeblocageService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleLigneDeblocageService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleLigneDeblocageService.class);
    }

    public static SimplePCAccordeeService getSimplePcaccordeeService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimplePCAccordeeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimplePCAccordeeService.class);
    }

    public static SimplePlanDeCalculService getSimplePlanDeCalculService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimplePlanDeCalculService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimplePlanDeCalculService.class);
    }

    /**
     * Ce service a été créer pour le processus d'adaptation de pc
     * 
     * @return Implémentation du service SimpleRenteAdaptationService
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleRenteAdaptationService getSimpleRenteAdaptationService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleRenteAdaptationService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleRenteAdaptationService.class);
    }

    /**
     * Implémentation du service de gestion des statistiques OFAS
     * 
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static StatistiquesOFASService getStatistiquesOFASService()
            throws JadeApplicationServiceNotAvailableException {
        return (StatistiquesOFASService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                StatistiquesOFASService.class);
    }

    /**
     * @return Implémentation du service TaxeJournaliereHomeService
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static TaxeJournaliereHomeService getTaxeJournaliereHomeService()
            throws JadeApplicationServiceNotAvailableException {
        return (TaxeJournaliereHomeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                TaxeJournaliereHomeService.class);
    }

    public static TransfertDossierBuilderProviderService getTransfertDossierPCBuilderProviderService()
            throws JadeApplicationServiceNotAvailableException {
        return (TransfertDossierBuilderProviderService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                TransfertDossierBuilderProviderService.class);
    }

    public static TransfertDossierPCProviderService getTransfertDossierPCProviderService()
            throws JadeApplicationServiceNotAvailableException {
        return (TransfertDossierPCProviderService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                TransfertDossierPCProviderService.class);
    }

    public static TransfertDossierSuppressionService getTransfertDossierSuppressionService()
            throws JadeApplicationServiceNotAvailableException {
        return (TransfertDossierSuppressionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                TransfertDossierSuppressionService.class);
    }

    public static TransfertRentePCProviderService getTransfertRentePCProviderService()
            throws JadeApplicationServiceNotAvailableException {
        return (TransfertRentePCProviderService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                TransfertRentePCProviderService.class);
    }

    /**
     * Retourne le service de gestion des validations de décisions
     * 
     * @return Le service de validation
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static ValidationDecisionService getValidationDecisionService()
            throws JadeApplicationServiceNotAvailableException {
        return (ValidationDecisionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                ValidationDecisionService.class);

    }

    /**
     * @return Implémentation du service de gestion de variableMetier
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static VariableMetierService getVariableMetierService() throws JadeApplicationServiceNotAvailableException {
        return (VariableMetierService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                VariableMetierService.class);
    }

    /**
     * @return Implémentation du service de gestion des annonces rpc
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static RpcService getRpcService() throws JadeApplicationServiceNotAvailableException {
        return (RpcService) JadeApplicationServiceLocator.getInstance().getServiceImpl(RpcService.class);
    }
}
