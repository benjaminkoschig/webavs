package ch.globaz.al.business.services;

import static ch.globaz.al.business.services.ALRepositoryLocator.*;
import ch.globaz.al.business.exceptions.decision.ALDecisionException;
import ch.globaz.al.business.services.adi.CalculAdiBusinessService;
import ch.globaz.al.business.services.adiDecomptes.AdiDecomptesService;
import ch.globaz.al.business.services.adiDecomptes.DecompteAdiBusinessService;
import ch.globaz.al.business.services.affiliation.AffiliationBusinessService;
import ch.globaz.al.business.services.affiliation.RadiationAffilieProtocoleService;
import ch.globaz.al.business.services.affiliation.RadiationAffilieService;
import ch.globaz.al.business.services.attestationVersement.AttestationsVersementBusinessService;
import ch.globaz.al.business.services.attestationVersement.AttestationsVersementLoadCsvDetailleService;
import ch.globaz.al.business.services.calcul.CalculBusinessService;
import ch.globaz.al.business.services.compensation.CompensationFactureBusinessService;
import ch.globaz.al.business.services.compensation.CompensationFactureProtocolesService;
import ch.globaz.al.business.services.copies.CopiesBusinessService;
import ch.globaz.al.business.services.courrier.LettreAccompagnementCopieService;
import ch.globaz.al.business.services.decision.DecisionBuilderService;
import ch.globaz.al.business.services.decision.DecisionEditingService;
import ch.globaz.al.business.services.decision.DecisionListService;
import ch.globaz.al.business.services.decision.DecisionProviderService;
import ch.globaz.al.business.services.decision.DecisionService;
import ch.globaz.al.business.services.decision.ProtocoleDecisionsMasseService;
import ch.globaz.al.business.services.declarationVersement.DeclarationsVersementService;
import ch.globaz.al.business.services.dossiers.RadiationAutomatiqueDossiersProtocoleService;
import ch.globaz.al.business.services.dossiers.RadiationAutomatiqueService;
import ch.globaz.al.business.services.echeances.DatesEcheanceService;
import ch.globaz.al.business.services.echeances.DroitEcheanceService;
import ch.globaz.al.business.services.echeances.ImpressionEcheancesService;
import ch.globaz.al.business.services.echeances.ProtocoleDroitEcheancesService;
import ch.globaz.al.business.services.envoi.EnvoiItemService;
import ch.globaz.al.business.services.ged.GedBusinessService;
import ch.globaz.al.business.services.generation.factures.NumeroFactureService;
import ch.globaz.al.business.services.generation.prestations.GenerationAffilieService;
import ch.globaz.al.business.services.generation.prestations.GenerationDossierService;
import ch.globaz.al.business.services.languesAllocAffilies.LangueAllocAffilieService;
import ch.globaz.al.business.services.models.adi.AdiDecompteComplexModelService;
import ch.globaz.al.business.services.models.adi.AdiEnfantMoisComplexModelService;
import ch.globaz.al.business.services.models.adi.AdiSaisieComplexModelService;
import ch.globaz.al.business.services.models.adi.AdiSaisieModelService;
import ch.globaz.al.business.services.models.adi.DecompteAdiModelService;
import ch.globaz.al.business.services.models.allocataire.AllocataireAgricoleComplexModelService;
import ch.globaz.al.business.services.models.allocataire.AllocataireBusinessService;
import ch.globaz.al.business.services.models.allocataire.AllocataireComplexModelService;
import ch.globaz.al.business.services.models.allocataire.RevenuModelService;
import ch.globaz.al.business.services.models.attribut.AttributEntiteBusinessService;
import ch.globaz.al.business.services.models.attribut.AttributEntiteModelService;
import ch.globaz.al.business.services.models.dossier.CommentaireModelService;
import ch.globaz.al.business.services.models.dossier.CopieComplexModelService;
import ch.globaz.al.business.services.models.dossier.CopieModelService;
import ch.globaz.al.business.services.models.dossier.DossierAgricoleComplexModelService;
import ch.globaz.al.business.services.models.dossier.DossierBusinessService;
import ch.globaz.al.business.services.models.dossier.DossierComplexModelService;
import ch.globaz.al.business.services.models.dossier.DossierDecisionComplexModelService;
import ch.globaz.al.business.services.models.dossier.DossierLieComplexModelService;
import ch.globaz.al.business.services.models.dossier.DossierListComplexModelService;
import ch.globaz.al.business.services.models.dossier.DossierModelService;
import ch.globaz.al.business.services.models.droit.CalculDroitEditingModelService;
import ch.globaz.al.business.services.models.droit.DroitBusinessService;
import ch.globaz.al.business.services.models.droit.DroitComplexModelService;
import ch.globaz.al.business.services.models.droit.DroitEcheanceComplexModelService;
import ch.globaz.al.business.services.models.droit.EnfantBusinessService;
import ch.globaz.al.business.services.models.droit.EnfantComplexModelService;
import ch.globaz.al.business.services.models.envoi.EnvoiComplexModelService;
import ch.globaz.al.business.services.models.envoi.EnvoiTemplateComplexModelService;
import ch.globaz.al.business.services.models.periodeAF.PeriodeAFBusinessService;
import ch.globaz.al.business.services.models.periodeAF.PeriodeAFModelService;
import ch.globaz.al.business.services.models.personne.PersonneAFComplexModelService;
import ch.globaz.al.business.services.models.prestation.DetailPrestationComplexModelService;
import ch.globaz.al.business.services.models.prestation.EntetePrestationListRecapComplexModelService;
import ch.globaz.al.business.services.models.prestation.EntetePrestationModelService;
import ch.globaz.al.business.services.models.prestation.PrestationBusinessService;
import ch.globaz.al.business.services.models.prestation.RecapitulatifEntrepriseBusinessService;
import ch.globaz.al.business.services.models.prestation.RecapitulatifEntrepriseListComplexModelService;
import ch.globaz.al.business.services.models.prestation.RecapitulatifEntrepriseModelService;
import ch.globaz.al.business.services.models.processus.ConfigProcessusModelService;
import ch.globaz.al.business.services.models.processus.ProcessusPeriodiqueModelService;
import ch.globaz.al.business.services.models.processus.TemplateTraitementListComplexModelService;
import ch.globaz.al.business.services.models.processus.TraitementPeriodiqueModelService;
import ch.globaz.al.business.services.models.rafam.AnnonceRafamComplexModelService;
import ch.globaz.al.business.services.models.rafam.AnnonceRafamDelegueComplexModelService;
import ch.globaz.al.business.services.models.rafam.AnnonceRafamModelService;
import ch.globaz.al.business.services.models.rafam.ComplementDelegueModelService;
import ch.globaz.al.business.services.models.rafam.ErreurAnnonceRafamModelService;
import ch.globaz.al.business.services.models.rafam.ErrorPeriodModelService;
import ch.globaz.al.business.services.models.rafam.OverlapInformationModelService;
import ch.globaz.al.business.services.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereModelService;
import ch.globaz.al.business.services.parameters.ParametersServices;
import ch.globaz.al.business.services.processus.BusinessProcessusService;
import ch.globaz.al.business.services.rafam.AnnonceRafamCreationService;
import ch.globaz.al.business.services.rafam.AnnonceRafamDelegueBusinessService;
import ch.globaz.al.business.services.rafam.AnnonceRafamImportProtocoleService;
import ch.globaz.al.business.services.rafam.AnnoncesRafamDelegueProtocoleService;
import ch.globaz.al.business.services.rafam.AnnoncesRafamErrorBusinessService;
import ch.globaz.al.business.services.rafam.AnnoncesRafamNewXSDVersionErrorBusinessService;
import ch.globaz.al.business.services.rafam.AnnoncesRafamProtocoleService;
import ch.globaz.al.business.services.rafam.sedex.ExportAnnoncesNewXSDVersionRafamService;
import ch.globaz.al.business.services.rafam.sedex.ExportAnnoncesRafamService;
import ch.globaz.al.business.services.recapitulatifs.RecapitulatifEntrepriseImpressionService;
import ch.globaz.al.business.services.tarif.TarifBusinessService;
import ch.globaz.al.business.services.tucana.TucanaBusinessService;
import ch.globaz.al.business.services.impotsource.TauxImpositionService;
import ch.globaz.al.business.services.impotsource.TauxImpositionServiceCRUD;
import ch.globaz.al.businessimpl.services.impotsource.TauxImpositionServiceImpl;
import ch.globaz.vulpecula.business.services.is.ImpotSourceService;
import ch.globaz.vulpecula.businessimpl.services.is.ImpotSourceServiceImpl;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

/**
 * Permet d'obtenir une instance de l'implémentation des services de l'application des Allocations Familiales
 *
 * @author jts
 */
public abstract class ALServiceLocator {
    /**
     * Méthode qui retourne une instance du service des décomptes Adi complexe
     *
     * @return une instance du service de l'implémantation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible Exception levée si le service n'est pas accessible
     */
    public static AdiDecompteComplexModelService getAdiDecompteComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (AdiDecompteComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AdiDecompteComplexModelService.class);
    }

    /**
     *
     * Méthode qui retourne une instance du service des décomptes Adi
     *
     * @return une instance du service de l'implémantation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible Exception levée si le service n'est pas accessible
     */
    public static AdiDecomptesService getAdiDecomptesService() throws JadeApplicationServiceNotAvailableException {
        return (AdiDecomptesService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AdiDecomptesService.class);
    }

    /**
     * Méthode qui retourne une instance du services des adi par enfant par mois complexe
     *
     * @return une instance du service de l'implémantation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible Exception levée si le service n'est pas accessible
     */
    public static AdiEnfantMoisComplexModelService getAdiEnfantMoisComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (AdiEnfantMoisComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AdiEnfantMoisComplexModelService.class);
    }

    /**
     *
     * Méthode qui retourne une instance du service des saisies Adi complexe
     *
     * @return une instance du service de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible Exception levée si le service n'est pas accessible
     */
    public static AdiSaisieComplexModelService getAdiSaisieComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (AdiSaisieComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AdiSaisieComplexModelService.class);
    }

    /**
     *
     * Méthode qui retourne une instance du service des saisies Adi
     *
     * @return une instance du service de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible Exception levée si le service n'est pas accessible
     */
    public static AdiSaisieModelService getAdiSaisieModelService() throws JadeApplicationServiceNotAvailableException {
        return (AdiSaisieModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AdiSaisieModelService.class);
    }

    /**
     * retourne une instance du service Affiliation
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible Exception levée si le service n'est pas accessible
     * @see ch.globaz.al.business.services.affiliation.AffiliationBusinessService
     */
    public static AffiliationBusinessService getAffiliationBusinessService()
            throws JadeApplicationServiceNotAvailableException {
        return (AffiliationBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AffiliationBusinessService.class);
    }

    /**
     * Retourne une instance du service de gestion de la persistance du modèle AllocataireAgricoleComplexModel
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     * @see ch.globaz.al.business.services.models.allocataire.AllocataireAgricoleComplexModelService
     */
    public static AllocataireAgricoleComplexModelService getAllocataireAgricoleComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (AllocataireAgricoleComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AllocataireAgricoleComplexModelService.class);
    }

    /**
     * retourne une instance du service métier des allocataires
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     * @see ch.globaz.al.business.services.models.allocataire.AllocataireBusinessService
     */
    public static AllocataireBusinessService getAllocataireBusinessService()
            throws JadeApplicationServiceNotAvailableException {
        return (AllocataireBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AllocataireBusinessService.class);
    }

    /**
     * retourne une instance du service de gestion de la persistance des allocataire (modèle complexe)
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     * @see ch.globaz.al.business.services.models.allocataire.AllocataireComplexModelService
     */
    public static AllocataireComplexModelService getAllocataireComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (AllocataireComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AllocataireComplexModelService.class);
    }

    /**
     *
     * @return Instance du service qui gère la persistence des Annonces RAFam
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static AnnonceRafamComplexModelService getAnnonceRafamComplexModelService()
            throws JadeApplicationServiceNotAvailableException {

        return (AnnonceRafamComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AnnonceRafamComplexModelService.class);
    }

    /**
     *
     * @return Instance du service de création d'annonces RAFam
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static AnnonceRafamCreationService getAnnonceRafamCreationService()
            throws JadeApplicationServiceNotAvailableException {

        return (AnnonceRafamCreationService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AnnonceRafamCreationService.class);

    }

    /**
     *
     * @return Instance du service qui gère la logique métier des Annonces RAFam délégué
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static AnnonceRafamDelegueBusinessService getAnnonceRafamDelegueBusinessService()
            throws JadeApplicationServiceNotAvailableException {
        return (AnnonceRafamDelegueBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AnnonceRafamDelegueBusinessService.class);
    }

    public static AnnonceRafamDelegueComplexModelService getAnnonceRafamDelegueComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (AnnonceRafamDelegueComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AnnonceRafamDelegueComplexModelService.class);
    }

    /**
     * Retourne une instance du service AnnonceRafamImportProtocoleService
     *
     * @return instance du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static AnnonceRafamImportProtocoleService getAnnonceRafamImportProtocoleService()
            throws JadeApplicationServiceNotAvailableException {
        return (AnnonceRafamImportProtocoleService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AnnonceRafamImportProtocoleService.class);
    }

    /**
     *
     * @return Instance du service qui gère la persistence des Annonces RAFam
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static AnnonceRafamModelService getAnnonceRafamModelService()
            throws JadeApplicationServiceNotAvailableException {

        return (AnnonceRafamModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AnnonceRafamModelService.class);

    }

    /**
     *
     * @return une instance du service de génération de protocole RAFam pour les annonces déléguées
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static AnnoncesRafamDelegueProtocoleService getAnnoncesRafamDelegueProtocoleService()
            throws JadeApplicationServiceNotAvailableException {
        return (AnnoncesRafamDelegueProtocoleService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AnnoncesRafamDelegueProtocoleService.class);
    }

    public static AnnoncesRafamErrorBusinessService getAnnoncesRafamErrorBusinessService()
            throws JadeApplicationServiceNotAvailableException {
        return (AnnoncesRafamErrorBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AnnoncesRafamErrorBusinessService.class);
    }

    public static AnnoncesRafamNewXSDVersionErrorBusinessService getAnnoncesRafamNewXSDVersionErrorBusinessService()
            throws JadeApplicationServiceNotAvailableException {
        return (AnnoncesRafamNewXSDVersionErrorBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AnnoncesRafamNewXSDVersionErrorBusinessService.class);
    }

    /**
     *
     * @return une instance du service de génération de protocole RAFam
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static AnnoncesRafamProtocoleService getAnnoncesRafamProtocoleService()
            throws JadeApplicationServiceNotAvailableException {
        return (AnnoncesRafamProtocoleService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AnnoncesRafamProtocoleService.class);
    }

    /**
     *
     * @return retourne une onstaance métier du service AttestatsionVersementsService
     * @throws JadeApplicationServiceNotAvailableException
     */

    public static AttestationsVersementBusinessService getAttestationsVersementBusinessService()
            throws JadeApplicationServiceNotAvailableException {
        return (AttestationsVersementBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AttestationsVersementBusinessService.class);
    }

    /**
     *
     * @return retourne une instance du service AttestationsVersementLoadCsvDetaille
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static AttestationsVersementLoadCsvDetailleService getAttestationsVersementLoadCsvDetaille()
            throws JadeApplicationServiceNotAvailableException {
        return (AttestationsVersementLoadCsvDetailleService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AttestationsVersementLoadCsvDetailleService.class);
    }

    /**
     * retourne une instance du service métier attributs entité
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible Exception levée si le service n'est pas accessible
     */
    public static AttributEntiteBusinessService getAttributEntiteBusinessService()
            throws JadeApplicationServiceNotAvailableException {
        return (AttributEntiteBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AttributEntiteBusinessService.class);
    }

    /**
     * Retourne une instance du service des attributs entité
     *
     * @return Une instance de l'implémentation du service AttributEntiteModelService
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     * @see ch.globaz.al.business.services.models.attribut.AttributEntiteModelService
     */
    public static AttributEntiteModelService getAttributEntiteModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (AttributEntiteModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AttributEntiteModelService.class);
    }

    /**
     * retourne une instance des services métier des processus métier
     *
     * @return Une instance de l'implémentation du service BusinessProcessusService
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static BusinessProcessusService getBusinessProcessusService()
            throws JadeApplicationServiceNotAvailableException {
        return (BusinessProcessusService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(BusinessProcessusService.class);
    }

    /**
     * Retourne une instance du service des calcul adi métier
     *
     * @return Une instance de l'implémentation du service CalculAdiBusinessService
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static CalculAdiBusinessService getCalculAdiBusinessService()
            throws JadeApplicationServiceNotAvailableException {
        return (CalculAdiBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(CalculAdiBusinessService.class);
    }

    /**
     * Retourne une instance du service de calcul
     *
     * @return Une instance de l'implémentation du service CalculService
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     * @see ch.globaz.al.business.services.calcul.CalculBusinessService
     */
    public static CalculBusinessService getCalculBusinessService() throws JadeApplicationServiceNotAvailableException {
        return (CalculBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(CalculBusinessService.class);
    }

    /**
     * @return Une instance de l'implémentation du service CalculDroitEdintModelService
     */
    public static CalculDroitEditingModelService getCalculDroitEditingModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (CalculDroitEditingModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(CalculDroitEditingModelService.class);
    }

    /**
     * Retourne une instance du service de gestion de la persistance
     *
     * @return Une instance de l'implémentation du service CommentaireModelService
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static CommentaireModelService getCommentaireModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (CommentaireModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(CommentaireModelService.class);
    }

    /**
     * retourne une instance des services liés à la compensation sur facture de prestations AF
     *
     * @return une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static CompensationFactureBusinessService getCompensationFactureBusinessService()
            throws JadeApplicationServiceNotAvailableException {
        return (CompensationFactureBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(CompensationFactureBusinessService.class);
    }

    /**
     * Retourne une instance du service de génération de protocole de liste d'échéances des droits
     *
     * @return instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static CompensationFactureProtocolesService getCompensationFactureProtocolesService()
            throws JadeApplicationServiceNotAvailableException {
        return (CompensationFactureProtocolesService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(CompensationFactureProtocolesService.class);

    }

    /**
     *
     * Méthode qui retourne une instance du service des complements aux annonces délégués
     *
     * @return une instance du service de l'implémantation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible Exception levée si le service n'est pas accessible
     */
    public static ComplementDelegueModelService getComplementDelegueModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (ComplementDelegueModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ComplementDelegueModelService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance des configurations processus
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static ConfigProcessusModelService getConfigProcessusModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (ConfigProcessusModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ConfigProcessusModelService.class);
    }

    /**
     * retourne une instance du service de gestion de la persistance des copies (modèle complexe)
     *
     * @return Une instance de l'implémentation du service CopieComplexModelService
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     *
     * @see ch.globaz.al.business.services.models.dossier .CopieComplexModelService
     */
    public static CopieComplexModelService getCopieComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (CopieComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(CopieComplexModelService.class);
    }

    /**
     * @return Une instance de l'implémentation du service CopieModelService
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static CopieModelService getCopieModelService() throws JadeApplicationServiceNotAvailableException {
        return (CopieModelService) JadeApplicationServiceLocator.getInstance().getServiceImpl(CopieModelService.class);
    }

    /**
     * retourne une instance du service de gestion des copies
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible Exception levée si le service n'est pas accessible
     * @see ch.globaz.al.business.services.copies.CopiesBusinessService
     */
    public static CopiesBusinessService getCopiesBusinessService() throws JadeApplicationServiceNotAvailableException {
        return (CopiesBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(CopiesBusinessService.class);
    }

    /**
     * Retourne un instance du service métier de gestion des échéances
     *
     * @return une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static DatesEcheanceService getDatesEcheanceService() throws JadeApplicationServiceNotAvailableException {
        return (DatesEcheanceService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DatesEcheanceService.class);
    }

    /**
     * Retourne une instance du service de construction des décisions
     *
     * @return instance du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static DecisionBuilderService getDecisionBuilderService()
            throws JadeApplicationServiceNotAvailableException {
        return (DecisionBuilderService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DecisionBuilderService.class);
    }

    /**
     * Retourne une instance du service edition des décisions
     *
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static DecisionEditingService getDecisionEditingService()
            throws JadeApplicationServiceNotAvailableException {
        return (DecisionEditingService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DecisionEditingService.class);
    }

    /**
     * retourne une instance du service de gestion de la liste des décisions
     *
     * @return une instance de l'implémentartion du service DecisionListService
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static DecisionListService getDecisionListService() throws JadeApplicationServiceNotAvailableException {
        return (DecisionListService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DecisionListService.class);
    }

    /**
     * @return Une instance de l'implémentation du service DecisionProviderService
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static DecisionProviderService getDecisionProviderService()
            throws JadeApplicationServiceNotAvailableException {
        return (DecisionProviderService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DecisionProviderService.class);
    }

    /**
     * Retourne le service de décision correspondant à <code>serviceClassImpl</code>
     *
     * @param serviceClassImpl
     *            Service à rechercher
     * @return Une instance de l'implémentation du service DecisionService
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static DecisionService getDecisionService(Class serviceClassImpl)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException {
        if (serviceClassImpl == null) {
            throw new ALDecisionException(
                    "ALServiceLocator, unable to initialize decision service, interface class not defined!");
        } else if (!DecisionService.class.isAssignableFrom(serviceClassImpl)) {
            throw new ALDecisionException(
                    "ALServiceLocator, unable to initialize decision service, interface class is not a DecisionService sub-interface!");
        }
        return (DecisionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(serviceClassImpl);
    }

    /**
     * retourne une instance du service de gestion des déclarations de versement
     *
     * @return Une instance de l'implémentattion du service DeclarationVersmentService
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static DeclarationsVersementService getDeclarationsVersementService()
            throws JadeApplicationServiceNotAvailableException {
        return (DeclarationsVersementService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DeclarationsVersementService.class);
    }

    /**
     *
     * @return Instance du service métier des décomptes ADI
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static DecompteAdiBusinessService getDecompteAdiBusinessService()
            throws JadeApplicationServiceNotAvailableException {
        return (DecompteAdiBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DecompteAdiBusinessService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance des décomptes ADI
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static DecompteAdiModelService getDecompteAdiModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (DecompteAdiModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DecompteAdiModelService.class);
    }

    /**
     * retourne une instance du service de gestion de la persistance des modèles complexe de détail de prestation
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     * @see ch.globaz.al.business.services.models.droit.DroitComplexModelService
     */
    public static DetailPrestationComplexModelService getDetailPrestationComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (DetailPrestationComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DetailPrestationComplexModelService.class);
    }

    /**
     * Retourne une instance du service de gestion de la persistance des dossiers complexes pour le cas d'un agriculteur
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     * @see ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel
     */
    public static DossierAgricoleComplexModelService getDossierAgricoleComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (DossierAgricoleComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DossierAgricoleComplexModelService.class);
    }

    /**
     * retourne une instance du service de gestion de la persistance métier d'un dossier
     *
     * @return Une instance de l'implémentatiion du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static DossierBusinessService getDossierBusinessService()
            throws JadeApplicationServiceNotAvailableException {
        return (DossierBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DossierBusinessService.class);
    }

    /**
     * Retourne une instance du service de gestion de la persistance des dossiers complexes
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     * @see ch.globaz.al.business.models.dossier.DossierComplexModel
     */
    public static DossierComplexModelService getDossierComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (DossierComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DossierComplexModelService.class);
    }

    /**
     * Retourne une instance du service de gestion de la persistance des dossiers décisions complexes
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */

    public static DossierDecisionComplexModelService getDossierDecisionComplexeModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (DossierDecisionComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DossierDecisionComplexModelService.class);
    }

    /**
     * Méthode qui retourne une instance du service des dossiers liés
     *
     * @return une instance du service de l'implémantation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible Exception levée si le service n'est pas accessible
     */
    public static DossierLieComplexModelService getDossierLieComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (DossierLieComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DossierLieComplexModelService.class);
    }

    /**
     * retourne une instance du service dossierListComplexModel
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     * @see ch.globaz.al.business.services.models.dossier.DossierListComplexModelService
     */
    public static DossierListComplexModelService getDossierListComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (DossierListComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DossierListComplexModelService.class);
    }

    /**
     * retourne une instance du service de gestion de la persistance des dossiers
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     * @see ch.globaz.al.business.services.models.dossier.DossierModelService
     */
    public static DossierModelService getDossierModelService() throws JadeApplicationServiceNotAvailableException {
        return (DossierModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DossierModelService.class);
    }

    /**
     * Retourne une instance du service de gestion métier des droits
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     * @see ch.globaz.al.business.services.models.droit.DroitBusinessService
     */
    public static DroitBusinessService getDroitBusinessService() throws JadeApplicationServiceNotAvailableException {
        return (DroitBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DroitBusinessService.class);
    }

    /**
     * retourne une instance du service de gestion de la persistance des modèle complexe de droit
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     * @see ch.globaz.al.business.services.models.droit.DroitComplexModelService
     */
    public static DroitComplexModelService getDroitComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (DroitComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DroitComplexModelService.class);
    }

    /**
     * Retourne une instance du service de gestion de la persitence du modéle DroitEcheanceComplexModel
     *
     * @return DroitEcheanceComplexModelService Instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static DroitEcheanceComplexModelService getDroitEcheanceComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (DroitEcheanceComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DroitEcheanceComplexModelService.class);
    }

    /**
     * retourne une instance du service de gestion des échéances liées aux droits
     *
     * @return une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */

    public static DroitEcheanceService getDroitEcheanceService() throws JadeApplicationServiceNotAvailableException {
        return (DroitEcheanceService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DroitEcheanceService.class);
    }

    /**
     * retourne une instance du service métier de gestion des enfants
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     * @see ch.globaz.al.business.services.models.droit.EnfantBusinessService
     */
    public static EnfantBusinessService getEnfantBusinessService() throws JadeApplicationServiceNotAvailableException {
        return (EnfantBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(EnfantBusinessService.class);
    }

    /**
     * retourne une instance du service de gestion de la persistance des modèles EnfantComplexModel
     *
     * @return Une instance de l'implémentation du service EnfantComplexModelService
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static EnfantComplexModelService getEnfantComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (EnfantComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(EnfantComplexModelService.class);
    }

    /**
     * retourne une instance du service de gestion de la persistance des modèles EntetePrestationListRecapComplexModel
     *
     * @return Une instance de l'implémentation du service EntetePrestationListRecapComplexModel
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static EntetePrestationListRecapComplexModelService getEntetePrestationListRecapComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (EntetePrestationListRecapComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(EntetePrestationListRecapComplexModelService.class);
    }

    /**
     * Retourne une instance du service de gestion de la persistance des modèles prestationModel
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static EntetePrestationModelService getEntetePrestationModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (EntetePrestationModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(EntetePrestationModelService.class);
    }

    /**
     * Récupération d'une instance du service de gestion des modèles complexes envoi
     *
     * @return Instance du service de gestion des modèles complexes envoi
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static EnvoiComplexModelService getEnvoiComplexService() throws JadeApplicationServiceNotAvailableException {
        return (EnvoiComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(EnvoiComplexModelService.class);
    }

    /**
     * Récupération d'une instance du service de gestion des envois (items)
     *
     * @return Instance du service de gestion des envois (items)
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static EnvoiItemService getEnvoiItemService() throws JadeApplicationServiceNotAvailableException {
        return (EnvoiItemService) JadeApplicationServiceLocator.getInstance().getServiceImpl(EnvoiItemService.class);
    }

    /**
     * Récupération d'une instance du service de gestion des modèles complexes template envoi
     *
     * @return Instance du service de gestion des modèles complexes template envoi
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static EnvoiTemplateComplexModelService getEnvoiTemplateComplexService()
            throws JadeApplicationServiceNotAvailableException {
        return (EnvoiTemplateComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(EnvoiTemplateComplexModelService.class);
    }

    public static ErreurAnnonceRafamModelService getErreurAnnonceRafamModelService()
            throws JadeApplicationServiceNotAvailableException {

        return (ErreurAnnonceRafamModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ErreurAnnonceRafamModelService.class);

    }

    public static ErrorPeriodModelService getErrorPeriodModelService()
            throws JadeApplicationServiceNotAvailableException {

        return (ErrorPeriodModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ErrorPeriodModelService.class);

    }

    public static ExportAnnoncesRafamService getExportAnnoncesRafamService()
            throws JadeApplicationServiceNotAvailableException {

        return (ExportAnnoncesRafamService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ExportAnnoncesRafamService.class);

    }

    /**
     *
     * @return Une instance permettant l'export des annonces Rafam avec la nouvelle version des schémas XSD (4.1)
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static ExportAnnoncesNewXSDVersionRafamService getExportAnnoncesNewXSDVersionRafamService()
            throws JadeApplicationServiceNotAvailableException {

        return (ExportAnnoncesNewXSDVersionRafamService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ExportAnnoncesNewXSDVersionRafamService.class);

    }

    /**
     *
     * @return Une instance du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible Exception levée si le service n'est pas accessible
     */
    public static GedBusinessService getGedBusinessService() throws JadeApplicationServiceNotAvailableException {
        return (GedBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(GedBusinessService.class);
    }

    /**
     * Retourne une instance du service de génération des prestations des dossiers d'un affilié
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static GenerationAffilieService getGenerationAffilieService()
            throws JadeApplicationServiceNotAvailableException {
        return (GenerationAffilieService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(GenerationAffilieService.class);
    }

    /**
     * Retourne une instance du service de génération des prestations d'un dossier
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static GenerationDossierService getGenerationDossierService()
            throws JadeApplicationServiceNotAvailableException {
        return (GenerationDossierService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(GenerationDossierService.class);
    }

    /**
     * Retourne une instance de service d'impression des droits échéancés
     *
     * @return une instance de l'implémnetation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static ImpressionEcheancesService getImpressionEcheancesServices()
            throws JadeApplicationServiceNotAvailableException {
        return (ImpressionEcheancesService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ImpressionEcheancesService.class);
    }

    /**
     *
     * @return Instance du service de gestion des langues des affiliés et allocataires
     * @throws ApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessibel
     */
    public static LangueAllocAffilieService getLangueAllocAffilieService()
            throws JadeApplicationServiceNotAvailableException {
        return (LangueAllocAffilieService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(LangueAllocAffilieService.class);
    }

    /**
     * Retourne une instance du service de lettre d'accompagnement
     *
     * @return une instance d'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static LettreAccompagnementCopieService getLettreAccompagnementCopieService()
            throws JadeApplicationServiceNotAvailableException {
        return (LettreAccompagnementCopieService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(LettreAccompagnementCopieService.class);
    }

    /**
     * Méthode qui retourne une instance du service des décomptes Adi complexe
     *
     * @return une instance du service de l'implémantation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible Exception levée si le service n'est pas accessible
     */
    public static NumeroFactureService getNumeroFactureService() throws JadeApplicationServiceNotAvailableException {
        return (NumeroFactureService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(NumeroFactureService.class);
    }

    public static OverlapInformationModelService getOverlapInformationModelService()
            throws JadeApplicationServiceNotAvailableException {

        return (OverlapInformationModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(OverlapInformationModelService.class);

    }

    /**
     *
     * @return Instance du service de gestion de paramètre AF
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static ParametersServices getParametersServices() throws JadeApplicationServiceNotAvailableException {
        return (ParametersServices) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ParametersServices.class);
    }

    /**
     * retourne une instance du service periodeAFBusiness
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible Exception levée si le service n'est pas accessible
     * @see ch.globaz.al.business.services.models.periodeAF.PeriodeAFBusinessService
     */
    public static PeriodeAFBusinessService getPeriodeAFBusinessService()
            throws JadeApplicationServiceNotAvailableException {
        return (PeriodeAFBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(PeriodeAFBusinessService.class);
    }

    /**
     * @return Instance du service de gestion de la persistance des périodes AF
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static PeriodeAFModelService getPeriodeAFModelService() throws JadeApplicationServiceNotAvailableException {
        return (PeriodeAFModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(PeriodeAFModelService.class);
    }

    /**
     * retourne une instance des services sur des personnes AF
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     * @see ch.globaz.al.business.services.models.personne.PersonneAFComplexModelService
     */
    public static PersonneAFComplexModelService getPersonneAFComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (PersonneAFComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(PersonneAFComplexModelService.class);

    }

    /**
     * retourne une instance des services particuliers sur les prestations (import...)
     *
     * @return une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static PrestationBusinessService getPrestationBusinessService()
            throws JadeApplicationServiceNotAvailableException {
        return (PrestationBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(PrestationBusinessService.class);
    }

    /**
     * Méthode qui retourne une instance du service des processus périodieque
     *
     * @return une instance du service de l'implémantation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible Exception levée si le service n'est pas accessible
     */
    public static ProcessusPeriodiqueModelService getProcessusPeriodiqueModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (ProcessusPeriodiqueModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ProcessusPeriodiqueModelService.class);
    }

    /**
     * Retourne une instance du service de génération de protocole d'erreurs pour l'impression en masse de décisions
     *
     * @return instance du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static ProtocoleDecisionsMasseService getProtocoleDecisionsMasseService()
            throws JadeApplicationServiceNotAvailableException {
        return (ProtocoleDecisionsMasseService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ProtocoleDecisionsMasseService.class);
    }

    /**
     * Retourne une instance du service de génération de protocole de liste d'échéances des droits
     *
     * @return instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static ProtocoleDroitEcheancesService getProtocoleDroitEcheancesService()
            throws JadeApplicationServiceNotAvailableException {
        return (ProtocoleDroitEcheancesService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ProtocoleDroitEcheancesService.class);

    }

    /**
     * Retourne une instance du service de génération de protocole pour la radiation d'un affilié
     *
     * @return instance du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static RadiationAffilieProtocoleService getRadiationAffilieProtocoleService()
            throws JadeApplicationServiceNotAvailableException {
        return (RadiationAffilieProtocoleService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(RadiationAffilieProtocoleService.class);
    }

    /**
     * Retourne une instance du service de radiation des dossiers d'un affilié
     *
     * @return instance du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static RadiationAffilieService getRadiationAffilieService()
            throws JadeApplicationServiceNotAvailableException {
        return (RadiationAffilieService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(RadiationAffilieService.class);
    }

    /**
     * Retourne une instance du service permettant la génération d'un protocole lors de la radiation automatique de
     * dossier
     *
     * @return instance du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static RadiationAutomatiqueDossiersProtocoleService getRadiationAutomatiqueDossiersProtocoleService()
            throws JadeApplicationServiceNotAvailableException {
        return (RadiationAutomatiqueDossiersProtocoleService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(RadiationAutomatiqueDossiersProtocoleService.class);
    }

    /**
     * Retourne une instance du service permettant la gestion de la radiation automatique de dossiers
     *
     * @return instance du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static RadiationAutomatiqueService getRadiationAutomatiqueService()
            throws JadeApplicationServiceNotAvailableException {
        return (RadiationAutomatiqueService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(RadiationAutomatiqueService.class);
    }

    /**
     * retourne une instance des services particuliers sur les récaps
     *
     * @return une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static RecapitulatifEntrepriseBusinessService getRecapitulatifEntrepriseBusinessService()
            throws JadeApplicationServiceNotAvailableException {
        return (RecapitulatifEntrepriseBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(RecapitulatifEntrepriseBusinessService.class);
    }

    /**
     *
     * @return une instance du service de la gestion des récapitulatifs des entreprises à imprimer
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */

    public static RecapitulatifEntrepriseImpressionService getRecapitulatifEntrepriseImpressionService()
            throws JadeApplicationServiceNotAvailableException {
        return (RecapitulatifEntrepriseImpressionService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(RecapitulatifEntrepriseImpressionService.class);
    }

    /**
     *
     * @return une instance du service de la gestion des récapitulatifs des entreprises à afficher dans les résultats de
     *         recherche récaps
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static RecapitulatifEntrepriseListComplexModelService getRecapitulatifEntrepriseListComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (RecapitulatifEntrepriseListComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(RecapitulatifEntrepriseListComplexModelService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance des récap
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static RecapitulatifEntrepriseModelService getRecapitulatifEntrepriseModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (RecapitulatifEntrepriseModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(RecapitulatifEntrepriseModelService.class);
    }

    /**
     * Retourne une instance du service de gestion de la persistence du modèle RevenuModel
     *
     * @return RevenuModelService Instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static RevenuModelService getRevenuModelService() throws JadeApplicationServiceNotAvailableException {
        return (RevenuModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(RevenuModelService.class);

    }

    /**
     * Retourne une instance du service métier des tarifs
     *
     * @return TarifBusinessService Instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static TarifBusinessService getTarifBusinessService() throws JadeApplicationServiceNotAvailableException {
        return (TarifBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(TarifBusinessService.class);
    }

    /**
     *
     * @return Instance du service qui gère la persistence du taux des monnaies étrangères
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static TauxMonnaieEtrangereModelService getTauxMonnaieEtrangereModelService()
            throws JadeApplicationServiceNotAvailableException {

        return (TauxMonnaieEtrangereModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(TauxMonnaieEtrangereModelService.class);

    }

    /**
     * Retourne une instance du service de gestion de la persistence du modèle TemplateTraitementListeComplexModel
     *
     * @return TemplateTraitementListComplexModelService Instance du l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */

    public static TemplateTraitementListComplexModelService getTemplateTraitementListComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (TemplateTraitementListComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(TemplateTraitementListComplexModelService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance des traitements périodiques
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static TraitementPeriodiqueModelService getTraitementPeriodiqueModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (TraitementPeriodiqueModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(TraitementPeriodiqueModelService.class);
    }

    /**
     *
     * @return Instance du service métier Tucana
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static TucanaBusinessService getTucanaBusinessService() throws JadeApplicationServiceNotAvailableException {
        return (TucanaBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(TucanaBusinessService.class);
    }

    public static TauxImpositionService getTauxImpositionService() {
        return TauxImpositionServiceHolder.INSTANCE;
    }

    public static TauxImpositionServiceCRUD getTauxImpositionServiceCRUD()
            throws JadeApplicationServiceNotAvailableException {
        return (TauxImpositionServiceCRUD) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                TauxImpositionServiceCRUD.class);
    }

    private static class TauxImpositionServiceHolder {
        public static final TauxImpositionService INSTANCE = new TauxImpositionServiceImpl(
                getTauxImpositionRepository());
    }

    public static ImpotSourceService getImpotSourceService() {
        return ImpotSourceServiceHolder.INSTANCE;
    }

    private static class ImpotSourceServiceHolder {
        public static final ImpotSourceService INSTANCE = new ImpotSourceServiceImpl();
    }

}
