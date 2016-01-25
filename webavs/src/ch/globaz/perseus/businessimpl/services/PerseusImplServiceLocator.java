package ch.globaz.perseus.businessimpl.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.calcul.CalculDepensesReconnuesService;
import ch.globaz.perseus.business.services.calcul.CalculFortuneService;
import ch.globaz.perseus.business.services.calcul.CalculRevenusService;
import ch.globaz.perseus.business.services.document.DecisionBuilderProviderService;
import ch.globaz.perseus.business.services.models.creancier.SimpleCreancierService;
import ch.globaz.perseus.business.services.models.decision.AnnexeDecisionService;
import ch.globaz.perseus.business.services.models.decision.CopieDecisionService;
import ch.globaz.perseus.business.services.models.decision.SimpleAnnexeDecisionService;
import ch.globaz.perseus.business.services.models.decision.SimpleCopieDecisionService;
import ch.globaz.perseus.business.services.models.decision.SimpleDecisionService;
import ch.globaz.perseus.business.services.models.demande.SimpleDemandeService;
import ch.globaz.perseus.business.services.models.donneesfinancieres.SimpleDonneeFinanciereService;
import ch.globaz.perseus.business.services.models.donneesfinancieres.SimpleDonneeFinanciereSpecialisationService;
import ch.globaz.perseus.business.services.models.dossier.SimpleDossierService;
import ch.globaz.perseus.business.services.models.echeance.SimpleEcheanceLibreService;
import ch.globaz.perseus.business.services.models.impotsource.SimpleBaremeService;
import ch.globaz.perseus.business.services.models.impotsource.SimplePeriodeImpotSourceService;
import ch.globaz.perseus.business.services.models.impotsource.SimpleTauxService;
import ch.globaz.perseus.business.services.models.impotsource.SimpleTrancheSalaireService;
import ch.globaz.perseus.business.services.models.impotsource.TrancheSalaireService;
import ch.globaz.perseus.business.services.models.informationfacture.SimpleInformationFactureService;
import ch.globaz.perseus.business.services.models.lot.SimpleLotService;
import ch.globaz.perseus.business.services.models.lot.SimpleOrdreVersementService;
import ch.globaz.perseus.business.services.models.lot.SimplePrestationService;
import ch.globaz.perseus.business.services.models.parametres.SimpleLienLocaliteService;
import ch.globaz.perseus.business.services.models.parametres.SimpleLoyerService;
import ch.globaz.perseus.business.services.models.pcfaccordee.SimpleDetailsCalculService;
import ch.globaz.perseus.business.services.models.pcfaccordee.SimplePCFAccordeeService;
import ch.globaz.perseus.business.services.models.qd.SimpleFactureService;
import ch.globaz.perseus.business.services.models.qd.SimpleQDAnnuelleService;
import ch.globaz.perseus.business.services.models.qd.SimpleQDService;
import ch.globaz.perseus.business.services.models.rentepont.SimpleCreancierRentePontService;
import ch.globaz.perseus.business.services.models.rentepont.SimpleFactureRentePontService;
import ch.globaz.perseus.business.services.models.rentepont.SimpleQDRentePontService;
import ch.globaz.perseus.business.services.models.rentepont.SimpleRentePontService;
import ch.globaz.perseus.business.services.models.retenue.SimpleRetenueService;
import ch.globaz.perseus.business.services.models.situationfamille.EnfantFamilleService;
import ch.globaz.perseus.business.services.models.situationfamille.SimpleConjointService;
import ch.globaz.perseus.business.services.models.situationfamille.SimpleEnfantFamilleService;
import ch.globaz.perseus.business.services.models.situationfamille.SimpleEnfantService;
import ch.globaz.perseus.business.services.models.situationfamille.SimpleMembreFamilleService;
import ch.globaz.perseus.business.services.models.situationfamille.SimpleRequerantService;
import ch.globaz.perseus.business.services.models.situationfamille.SimpleSituationFamilialeService;
import ch.globaz.perseus.business.services.models.variablemetier.SimpleVariableMetierService;

public abstract class PerseusImplServiceLocator extends PerseusServiceLocator {

    public static AnnexeDecisionService getAnnexeDecisionService() throws JadeApplicationServiceNotAvailableException {
        return (AnnexeDecisionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                AnnexeDecisionService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static CalculDepensesReconnuesService getCalculDepensesReconnuesService()
            throws JadeApplicationServiceNotAvailableException {
        return (CalculDepensesReconnuesService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CalculDepensesReconnuesService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static CalculFortuneService getCalculFortuneService() throws JadeApplicationServiceNotAvailableException {
        return (CalculFortuneService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CalculFortuneService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static CalculRevenusService getCalculRevenuService() throws JadeApplicationServiceNotAvailableException {
        return (CalculRevenusService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CalculRevenusService.class);
    }

    /**
     * @return Implémentation du service de gestion des copies de décisions PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static CopieDecisionService getCopieDecisionService() throws JadeApplicationServiceNotAvailableException {
        return (CopieDecisionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CopieDecisionService.class);
    }

    /**
     * @return Implémentation du service de gestion du builder des décisions PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static DecisionBuilderProviderService getDocumentBuilderProviderService()
            throws JadeApplicationServiceNotAvailableException {
        return (DecisionBuilderProviderService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DecisionBuilderProviderService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static EnfantFamilleService getEnfantFamilleService() throws JadeApplicationServiceNotAvailableException {
        return (EnfantFamilleService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                EnfantFamilleService.class);
    }

    /**
     * @return Implémentation du service de gestion des annexes des décisions PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleAnnexeDecisionService getSimpleAnnexeDecisionService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleAnnexeDecisionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleAnnexeDecisionService.class);
    }

    /**
     * @return Implémentation du service de gestion des barèmes PC Familles pour le taux d'imposition à la source
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleBaremeService getSimpleBaremeService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleBaremeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleBaremeService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static SimpleConjointService getSimpleConjointService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleConjointService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleConjointService.class);
    }

    /**
     * @return Implémentation du service de gestion des copies de décisions PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleCopieDecisionService getSimpleCopieDecisionService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleCopieDecisionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleCopieDecisionService.class);
    }

    /**
     * @return Implémentation du service de gestion des créanciers rente-pont
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleCreancierRentePontService getSimpleCreancierRentePontService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleCreancierRentePontService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleCreancierRentePontService.class);
    }

    /**
     * @return Implémentation du service de gestion des créanciers PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleCreancierService getSimpleCreancierService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleCreancierService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleCreancierService.class);
    }

    /**
     * @return Implémentation du service de gestion des simple décision
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleDecisionService getSimpleDecisionService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleDecisionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDecisionService.class);

    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static SimpleDemandeService getSimpleDemandeService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleDemandeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDemandeService.class);
    }

    /**
     * @return Implémentation du service de gestion du simple détails calcul PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleDetailsCalculService getSimpleDetailsCalculService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleDetailsCalculService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDetailsCalculService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static SimpleDonneeFinanciereService getSimpleDonneeFinanciereService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleDonneeFinanciereService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDonneeFinanciereService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static SimpleDonneeFinanciereSpecialisationService getSimpleDonneeFinanciereSpecialisationService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleDonneeFinanciereSpecialisationService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(SimpleDonneeFinanciereSpecialisationService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static SimpleDossierService getSimpleDossierService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleDossierService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDossierService.class);
    }

    /**
     * @return Implémentation du service de gestion des échéances libres PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleEcheanceLibreService getSimpleEcheanceLibreService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleEcheanceLibreService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleEcheanceLibreService.class);

    }

    /**
     * @return Implémentation du service de gestion des informations sur les factures
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleInformationFactureService getSimpleInformationFactureService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleInformationFactureService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleInformationFactureService.class);

    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static SimpleEnfantFamilleService getSimpleEnfantFamilleService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleEnfantFamilleService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleEnfantFamilleService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static SimpleEnfantService getSimpleEnfantService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleEnfantService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleEnfantService.class);
    }

    /**
     * @return Implémentation du service de gestion des factures rente-pont PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleFactureRentePontService getSimpleFactureRentePontService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleFactureRentePontService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleFactureRentePontService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static SimpleFactureService getSimpleFactureService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleFactureService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleFactureService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static SimpleLienLocaliteService getSimpleLienLocaliteService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleLienLocaliteService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleLienLocaliteService.class);
    }

    /**
     * @return Implémentation du service de gestion des simples lots PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleLotService getSimpleLotService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleLotService) JadeApplicationServiceLocator.getInstance().getServiceImpl(SimpleLotService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static SimpleLoyerService getSimpleLoyerService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleLoyerService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(SimpleLoyerService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static SimpleMembreFamilleService getSimpleMembreFamilleService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleMembreFamilleService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleMembreFamilleService.class);
    }

    /**
     * @return Implémentation du service de gestion des simples ordre de versement PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleOrdreVersementService getSimpleOrdreVersementService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleOrdreVersementService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleOrdreVersementService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static SimplePCFAccordeeService getSimplePCFAccordeeService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimplePCFAccordeeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimplePCFAccordeeService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static SimplePeriodeImpotSourceService getSimplePeriodeImpotSourceService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimplePeriodeImpotSourceService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimplePeriodeImpotSourceService.class);
    }

    /**
     * @return Implémentation du service de gestion des simples prestations
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimplePrestationService getSimplePrestationService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimplePrestationService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimplePrestationService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static SimpleQDAnnuelleService getSimpleQDAnnuelleService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleQDAnnuelleService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleQDAnnuelleService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static SimpleQDRentePontService getSimpleQDRentePontService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleQDRentePontService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleQDRentePontService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static SimpleQDService getSimpleQDService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleQDService) JadeApplicationServiceLocator.getInstance().getServiceImpl(SimpleQDService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static SimpleRentePontService getSimpleRentePontService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleRentePontService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleRentePontService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static SimpleRequerantService getSimpleRequerantService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleRequerantService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleRequerantService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static SimpleRetenueService getSimpleRetenueService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleRetenueService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleRetenueService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static SimpleSituationFamilialeService getSimpleSituationFamilialeService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleSituationFamilialeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleSituationFamilialeService.class);
    }

    /**
     * @return Implémentation du service de gestion des simples taux PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleTauxService getSimpleTauxService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleTauxService) JadeApplicationServiceLocator.getInstance().getServiceImpl(SimpleTauxService.class);
    }

    /**
     * @return Implémentation du service de gestion des simple tranche salaire PC Familles
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleTrancheSalaireService getSimpleTrancheSalaireService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleTrancheSalaireService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleTrancheSalaireService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static SimpleVariableMetierService getSimpleVariableMetierService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleVariableMetierService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleVariableMetierService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static TrancheSalaireService getTrancheSalaireService() throws JadeApplicationServiceNotAvailableException {
        return (TrancheSalaireService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                TrancheSalaireService.class);
    }

}
