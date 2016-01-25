package ch.globaz.perseus.business.services;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.corvus.business.services.models.rentesaccordees.SimpleInformationsComptabiliteService;
import ch.globaz.perseus.business.exceptions.PerseusException;
import ch.globaz.perseus.business.services.bvr.BvrService;
import ch.globaz.perseus.business.services.calcul.CalculPCFService;
import ch.globaz.perseus.business.services.document.DecisionBuilderProviderService;
import ch.globaz.perseus.business.services.impotsource.ImpotSourceService;
import ch.globaz.perseus.business.services.models.creancier.CreancierService;
import ch.globaz.perseus.business.services.models.decision.AnnexeDecisionService;
import ch.globaz.perseus.business.services.models.decision.CopieDecisionService;
import ch.globaz.perseus.business.services.models.decision.DecisionForTypeDocumentService;
import ch.globaz.perseus.business.services.models.decision.DecisionOOService;
import ch.globaz.perseus.business.services.models.decision.DecisionService;
import ch.globaz.perseus.business.services.models.demande.DemandeService;
import ch.globaz.perseus.business.services.models.donneesfinancieres.DepenseReconnueService;
import ch.globaz.perseus.business.services.models.donneesfinancieres.DetteService;
import ch.globaz.perseus.business.services.models.donneesfinancieres.DonneeFinanciereService;
import ch.globaz.perseus.business.services.models.donneesfinancieres.FortuneService;
import ch.globaz.perseus.business.services.models.donneesfinancieres.RevenuService;
import ch.globaz.perseus.business.services.models.dossier.DossierService;
import ch.globaz.perseus.business.services.models.echeance.EcheanceLibreService;
import ch.globaz.perseus.business.services.models.impotsource.PeriodeImpotSourceService;
import ch.globaz.perseus.business.services.models.impotsource.TauxService;
import ch.globaz.perseus.business.services.models.informationfacture.InformationFactureService;
import ch.globaz.perseus.business.services.models.lot.LotService;
import ch.globaz.perseus.business.services.models.lot.OrdreVersementService;
import ch.globaz.perseus.business.services.models.lot.PrestationRentePontService;
import ch.globaz.perseus.business.services.models.lot.PrestationService;
import ch.globaz.perseus.business.services.models.parametres.LienLocaliteService;
import ch.globaz.perseus.business.services.models.parametres.LoyerService;
import ch.globaz.perseus.business.services.models.parametres.SimpleZoneService;
import ch.globaz.perseus.business.services.models.pcfaccordee.PCFAccordeeService;
import ch.globaz.perseus.business.services.models.qd.FactureService;
import ch.globaz.perseus.business.services.models.qd.QDAnnuelleService;
import ch.globaz.perseus.business.services.models.qd.QDService;
import ch.globaz.perseus.business.services.models.rentepont.CreancierRentePontService;
import ch.globaz.perseus.business.services.models.rentepont.FactureRentePontService;
import ch.globaz.perseus.business.services.models.rentepont.QDRentePontService;
import ch.globaz.perseus.business.services.models.rentepont.RentePontService;
import ch.globaz.perseus.business.services.models.retenue.RetenueDemandePCFAccordeeDecisionService;
import ch.globaz.perseus.business.services.models.retenue.RetenueService;
import ch.globaz.perseus.business.services.models.situationfamille.ConjointService;
import ch.globaz.perseus.business.services.models.situationfamille.EnfantFamilleService;
import ch.globaz.perseus.business.services.models.situationfamille.EnfantService;
import ch.globaz.perseus.business.services.models.situationfamille.MembreFamilleService;
import ch.globaz.perseus.business.services.models.situationfamille.RequerantService;
import ch.globaz.perseus.business.services.models.situationfamille.SituationFamilialeService;
import ch.globaz.perseus.business.services.models.statistique.StatistiquesMensuellesDemPcfDecService;
import ch.globaz.perseus.business.services.models.statistique.StatistiquesMensuellesEnfantDemandeService;
import ch.globaz.perseus.business.services.models.variablemetier.VariableMetierService;
import ch.globaz.perseus.business.services.paiement.PmtDecisionRentePontService;
import ch.globaz.perseus.business.services.paiement.PmtDecisionService;
import ch.globaz.perseus.business.services.paiement.PmtFactureRentePontService;
import ch.globaz.perseus.business.services.paiement.PmtFactureService;
import ch.globaz.perseus.business.services.paiement.PmtMensuelRentePontService;
import ch.globaz.perseus.business.services.paiement.PmtMensuelService;
import ch.globaz.perseus.business.services.rentepont.TypesSoinsRentePontService;
import ch.globaz.perseus.business.services.statsOFS.StatsOFSService;
import ch.globaz.perseus.business.services.traitements.TraitementAdaptationService;
import ch.globaz.perseus.business.services.traitements.TraitementAnnuelService;
import ch.globaz.perseus.business.services.traitements.TraitementPonctuelService;

public class PerseusServiceLocator {

    /**
     * @return Implémentation du service de gestion des AnnexeDecision
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static AnnexeDecisionService getAnnexeDecisionService() throws JadeApplicationServiceNotAvailableException {
        return (AnnexeDecisionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                AnnexeDecisionService.class);
    }

    /**
     * @return Implémentation du service de gestion des BVR
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static BvrService getBVRService() throws PerseusException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        return (BvrService) JadeApplicationServiceLocator.getInstance().getServiceImpl(BvrService.class);

    }

    /**
     * @return Implémentation du service de gestion du calcul PC Famille
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static CalculPCFService getCalculPCFService() throws JadeApplicationServiceNotAvailableException {
        return (CalculPCFService) JadeApplicationServiceLocator.getInstance().getServiceImpl(CalculPCFService.class);
    }

    /**
     * @return Implémentation du service de gestion des Conjoints
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static ConjointService getConjointService() throws JadeApplicationServiceNotAvailableException {
        return (ConjointService) JadeApplicationServiceLocator.getInstance().getServiceImpl(ConjointService.class);
    }

    /**
     * @return Implémentation du service de gestion des copie de décision
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static CopieDecisionService getCopieDecisionService() throws JadeApplicationServiceNotAvailableException {
        return (CopieDecisionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CopieDecisionService.class);
    }

    /**
     * @return Implémentation du service de gestion des créanciers rente-pont
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static CreancierRentePontService getCreancierRentePontService()
            throws JadeApplicationServiceNotAvailableException {
        return (CreancierRentePontService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CreancierRentePontService.class);
    }

    /**
     * @return Implémentation du service de gestion des créanciers PC Famille
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static CreancierService getCreancierService() throws JadeApplicationServiceNotAvailableException {
        return (CreancierService) JadeApplicationServiceLocator.getInstance().getServiceImpl(CreancierService.class);
    }

    /**
     * @return Implémentation du service de gestion des décisions builder
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static DecisionBuilderProviderService getDecisionBuilderProviderService()
            throws JadeApplicationServiceNotAvailableException {
        return (DecisionBuilderProviderService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DecisionBuilderProviderService.class);
    }

    /**
     * @return Implémentation du service de gestion de la détermination du type de décision PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static DecisionForTypeDocumentService getDecisionForTypeDocumentService()
            throws JadeApplicationServiceNotAvailableException {
        return (DecisionForTypeDocumentService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DecisionForTypeDocumentService.class);
    }

    public static DecisionOOService getDecisionOOService() throws JadeApplicationServiceNotAvailableException {
        return (DecisionOOService) JadeApplicationServiceLocator.getInstance().getServiceImpl(DecisionOOService.class);
    }

    /**
     * @return Implémentation du service de gestion des décisions
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static DecisionService getDecisionService() throws JadeApplicationServiceNotAvailableException {
        return (DecisionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(DecisionService.class);

    }

    /**
     * @return Implémentation du service de gestion des demande
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static DemandeService getDemandeService() throws JadeApplicationServiceNotAvailableException {
        return (DemandeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(DemandeService.class);
    }

    /**
     * @return Implémentation du service de gestion des dépenses reconnues
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static DepenseReconnueService getDepenseReconnueService() throws JadeApplicationServiceNotAvailableException {
        return (DepenseReconnueService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DepenseReconnueService.class);
    }

    /**
     * @return Implémentation du service de gestion des dettes
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static DetteService getDetteService() throws JadeApplicationServiceNotAvailableException {
        return (DetteService) JadeApplicationServiceLocator.getInstance().getServiceImpl(DetteService.class);
    }

    /**
     * @return Implémentation du service de gestion des données financières
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static DonneeFinanciereService getDonneeFinanciereService()
            throws JadeApplicationServiceNotAvailableException {
        return (DonneeFinanciereService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DonneeFinanciereService.class);
    }

    /**
     * @return Implémentation du service de gestion des dossiers
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static DossierService getDossierService() throws JadeApplicationServiceNotAvailableException {
        return (DossierService) JadeApplicationServiceLocator.getInstance().getServiceImpl(DossierService.class);
    }

    /**
     * @return Implémentation du service de gestion des échéances libres
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static EcheanceLibreService getEcheanceLibreService() throws JadeApplicationServiceNotAvailableException {
        return (EcheanceLibreService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                EcheanceLibreService.class);
    }

    /**
     * @return Implementation du service de gestion des informations pour les factures
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static InformationFactureService getInformationFactureService()
            throws JadeApplicationServiceNotAvailableException {
        return (InformationFactureService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                InformationFactureService.class);
    }

    /**
     * @return Implémentation du service de gestion des enfants famille
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static EnfantFamilleService getEnfantFamilleService() throws JadeApplicationServiceNotAvailableException {
        return (EnfantFamilleService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                EnfantFamilleService.class);
    }

    /**
     * @return Implémentation du service de gestion des enfants
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static EnfantService getEnfantService() throws JadeApplicationServiceNotAvailableException {
        return (EnfantService) JadeApplicationServiceLocator.getInstance().getServiceImpl(EnfantService.class);
    }

    /**
     * @return Implémentation du service de gestion des factures rente-pont
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static FactureRentePontService getFactureRentePontService()
            throws JadeApplicationServiceNotAvailableException {
        return (FactureRentePontService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                FactureRentePontService.class);
    }

    /**
     * @return Implémentation du service de gestion des factures PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static FactureService getFactureService() throws JadeApplicationServiceNotAvailableException {
        return (FactureService) JadeApplicationServiceLocator.getInstance().getServiceImpl(FactureService.class);
    }

    /**
     * @return Implémentation du service de gestion de la fortune
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static FortuneService getFortuneService() throws JadeApplicationServiceNotAvailableException {
        return (FortuneService) JadeApplicationServiceLocator.getInstance().getServiceImpl(FortuneService.class);
    }

    /**
     * @return Implémentation du service de gestion de l'impôt à la source
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static ImpotSourceService getImpotSourceService() throws JadeApplicationServiceNotAvailableException {
        return (ImpotSourceService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ImpotSourceService.class);
    }

    /**
     * @return Implémentation du service de gestion des liens entre localité
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static LienLocaliteService getLienLocaliteService() throws JadeApplicationServiceNotAvailableException {
        return (LienLocaliteService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                LienLocaliteService.class);
    }

    /**
     * @return Implémentation du service de gestion des Lot
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static LotService getLotService() throws JadeApplicationServiceNotAvailableException {
        return (LotService) JadeApplicationServiceLocator.getInstance().getServiceImpl(LotService.class);
    }

    /**
     * @return Implémentation du service de gestion des loyers
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static LoyerService getLoyerService() throws JadeApplicationServiceNotAvailableException {
        return (LoyerService) JadeApplicationServiceLocator.getInstance().getServiceImpl(LoyerService.class);
    }

    /**
     * @return Implémentation du service de gestion des membres de familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static MembreFamilleService getMembreFamilleService() throws JadeApplicationServiceNotAvailableException {
        return (MembreFamilleService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                MembreFamilleService.class);
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
     * @return Implémentation du service de gestion des PCF Accordées
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static PCFAccordeeService getPCFAccordeeService() throws JadeApplicationServiceNotAvailableException {
        return (PCFAccordeeService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(PCFAccordeeService.class);
    }

    /**
     * @return Implémentation du service de gestion des périodes d'impôt à la source
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static PeriodeImpotSourceService getPeriodeImpotSourceService()
            throws JadeApplicationServiceNotAvailableException {
        return (PeriodeImpotSourceService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                PeriodeImpotSourceService.class);
    }

    /**
     * @return Implémentation du service de gestion des pmt décisions rente-pont
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static PmtDecisionRentePontService getPmtDecisionRentePontService()
            throws JadeApplicationServiceNotAvailableException {
        return (PmtDecisionRentePontService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                PmtDecisionRentePontService.class);
    }

    /**
     * @return Implémentation du service de gestion des pmt décisions PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static PmtDecisionService getPmtDecisionService() throws JadeApplicationServiceNotAvailableException {
        return (PmtDecisionService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(PmtDecisionService.class);
    }

    /**
     * @return Implémentation du service de gestion des pmt factures rente-pont
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static PmtFactureRentePontService getPmtFactureRentePontService()
            throws JadeApplicationServiceNotAvailableException {
        return (PmtFactureRentePontService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                PmtFactureRentePontService.class);
    }

    /**
     * @return Implémentation du service de gestion des pmt factures PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static PmtFactureService getPmtFactureService() throws JadeApplicationServiceNotAvailableException {
        return (PmtFactureService) JadeApplicationServiceLocator.getInstance().getServiceImpl(PmtFactureService.class);
    }

    /**
     * @return Implémentation du service de gestion du pmt mensuel rente-pont
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static PmtMensuelRentePontService getPmtMensuelRentePontService()
            throws JadeApplicationServiceNotAvailableException {
        return (PmtMensuelRentePontService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                PmtMensuelRentePontService.class);
    }

    /**
     * @return Implémentation du service de gestion du pmt mensuel PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static PmtMensuelService getPmtMensuelService() throws JadeApplicationServiceNotAvailableException {
        return (PmtMensuelService) JadeApplicationServiceLocator.getInstance().getServiceImpl(PmtMensuelService.class);
    }

    /**
     * @return Implémentation du service de gestion des prestations rente-pont
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static PrestationRentePontService getPrestationRentePontService()
            throws JadeApplicationServiceNotAvailableException {
        return (PrestationRentePontService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                PrestationRentePontService.class);
    }

    /**
     * @return Implémentation du service de gestion des prestations PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static PrestationService getPrestationService() throws JadeApplicationServiceNotAvailableException {
        return (PrestationService) JadeApplicationServiceLocator.getInstance().getServiceImpl(PrestationService.class);
    }

    /**
     * @return Implémentation du service de gestion des QD annuelle PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static QDAnnuelleService getQDAnnuelleService() throws JadeApplicationServiceNotAvailableException {
        return (QDAnnuelleService) JadeApplicationServiceLocator.getInstance().getServiceImpl(QDAnnuelleService.class);
    }

    /**
     * @return Implémentation du service de gestion des QD Rente-pont
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static QDRentePontService getQDRentePontService() throws JadeApplicationServiceNotAvailableException {
        return (QDRentePontService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(QDRentePontService.class);
    }

    /**
     * @return Implémentation du service de gestion des QD PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static QDService getQDService() throws JadeApplicationServiceNotAvailableException {
        return (QDService) JadeApplicationServiceLocator.getInstance().getServiceImpl(QDService.class);
    }

    /**
     * @return Implémentation du service de gestion des Rente-pont
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static RentePontService getRentePontService() throws JadeApplicationServiceNotAvailableException {
        return (RentePontService) JadeApplicationServiceLocator.getInstance().getServiceImpl(RentePontService.class);
    }

    /**
     * @return Implémentation du service de gestion des requérants PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static RequerantService getRequerantService() throws JadeApplicationServiceNotAvailableException {
        return (RequerantService) JadeApplicationServiceLocator.getInstance().getServiceImpl(RequerantService.class);
    }

    /**
     * @return Implémentation du service de gestion des RetenueDemandePCFAccordeeDecision
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static RetenueDemandePCFAccordeeDecisionService getRetenueDemandePCFAccordeeDecisionService()
            throws JadeApplicationServiceNotAvailableException {
        return (RetenueDemandePCFAccordeeDecisionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                RetenueDemandePCFAccordeeDecisionService.class);
    }

    /**
     * @return Implémentation du service de gestion des revenues PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static RetenueService getRetenueService() throws JadeApplicationServiceNotAvailableException {
        return (RetenueService) JadeApplicationServiceLocator.getInstance().getServiceImpl(RetenueService.class);
    }

    /**
     * @return Implémentation du service de gestion des revenues PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static RevenuService getRevenuService() throws JadeApplicationServiceNotAvailableException {
        return (RevenuService) JadeApplicationServiceLocator.getInstance().getServiceImpl(RevenuService.class);
    }

    /**
     * @return Implémentation du service de gestion des Lot
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleInformationsComptabiliteService getSimpleInformationsComptabiliteService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleInformationsComptabiliteService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleInformationsComptabiliteService.class);
    }

    /**
     * @return Implémentation du service de gestion des zones de loyer
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleZoneService getSimpleZoneService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleZoneService) JadeApplicationServiceLocator.getInstance().getServiceImpl(SimpleZoneService.class);
    }

    /**
     * @return Implémentation du service de gestion de la situation familiale PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SituationFamilialeService getSituationFamilialeService()
            throws JadeApplicationServiceNotAvailableException {
        return (SituationFamilialeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SituationFamilialeService.class);
    }

    /**
     * @return Implémentation du service de gestion des StatistiquesMensuellesDemPcfDecService
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static StatistiquesMensuellesDemPcfDecService getStatistiquesMensuellesDemPcfDecService()
            throws JadeApplicationServiceNotAvailableException {
        return (StatistiquesMensuellesDemPcfDecService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                StatistiquesMensuellesDemPcfDecService.class);
    }

    /**
     * @return Implémentation du service de gestion des StatistiquesMensuellesEnfantDemandeService
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static StatistiquesMensuellesEnfantDemandeService getStatistiquesMensuellesEnfantDemandeService()
            throws JadeApplicationServiceNotAvailableException {
        return (StatistiquesMensuellesEnfantDemandeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                StatistiquesMensuellesEnfantDemandeService.class);
    }

    /**
     * @return Implémentation du service de gestion des statistiques OFS PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static StatsOFSService getStatsOFSServie() throws JadeApplicationServiceNotAvailableException {
        return (StatsOFSService) JadeApplicationServiceLocator.getInstance().getServiceImpl(StatsOFSService.class);
    }

    /**
     * @return Implémentation du service de gestion des taux pour l'impôt à la source PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static TauxService getTauxService() throws JadeApplicationServiceNotAvailableException {
        return (TauxService) JadeApplicationServiceLocator.getInstance().getServiceImpl(TauxService.class);
    }

    /**
     * @return Implémentation du service de gestion du traitement d'adaptation PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static TraitementAdaptationService getTraitementAdaptationService()
            throws JadeApplicationServiceNotAvailableException {
        return (TraitementAdaptationService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                TraitementAdaptationService.class);
    }

    /**
     * @return Implémentation du service de gestion du traitement annuel PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static TraitementAnnuelService getTraitementAnnuelService()
            throws JadeApplicationServiceNotAvailableException {
        return (TraitementAnnuelService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                TraitementAnnuelService.class);
    }

    /**
     * @return Implémentation du service de gestion du traitement ponctuel PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static TraitementPonctuelService getTraitementPonctuelService()
            throws JadeApplicationServiceNotAvailableException {
        return (TraitementPonctuelService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                TraitementPonctuelService.class);
    }

    /**
     * @return Implémentation du service de gestion des sous-types de soins Rente-pont
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static TypesSoinsRentePontService getTypesSoinsRentePontService()
            throws JadeApplicationServiceNotAvailableException {
        return (TypesSoinsRentePontService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                TypesSoinsRentePontService.class);
    }

    /**
     * @return Implémentation du service de gestion des variables métiers
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static VariableMetierService getVariableMetierService() throws JadeApplicationServiceNotAvailableException {
        return (VariableMetierService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                VariableMetierService.class);
    }

}
