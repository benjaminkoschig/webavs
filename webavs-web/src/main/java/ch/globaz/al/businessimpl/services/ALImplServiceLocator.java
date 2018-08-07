package ch.globaz.al.businessimpl.services;

import ch.globaz.al.business.exceptions.enteteDocument.ALEnteteDocumentException;
import ch.globaz.al.business.exceptions.rubriques.ALRubriquesException;
import ch.globaz.al.business.exceptions.signatureService.ALSignatureServiceException;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.adiDecomptes.AdiDecompteDetailleService;
import ch.globaz.al.business.services.adiDecomptes.AdiDecompteGlobalService;
import ch.globaz.al.business.services.affiliation.AffiliationService;
import ch.globaz.al.business.services.calcul.CalculMontantsService;
import ch.globaz.al.business.services.calcul.CalculService;
import ch.globaz.al.business.services.compensation.CompensationFactureService;
import ch.globaz.al.business.services.compensation.ProtocoleCSVCompensation;
import ch.globaz.al.business.services.compensation.ProtocoleDetailleCompensationService;
import ch.globaz.al.business.services.compensation.ProtocoleErreursCompensationService;
import ch.globaz.al.business.services.compensation.ProtocoleRecapitulatifCompensationService;
import ch.globaz.al.business.services.copies.CopiesService;
import ch.globaz.al.business.services.declarationVersement.DeclarationVersementDetailleDemandeService;
import ch.globaz.al.business.services.declarationVersement.DeclarationVersementDetailleDirectService;
import ch.globaz.al.business.services.declarationVersement.DeclarationVersementDetailleFrontaliersNonActifService;
import ch.globaz.al.business.services.declarationVersement.DeclarationVersementDetailleImposeSourceService;
import ch.globaz.al.business.services.declarationVersement.DeclarationVersementGlobalDirectService;
import ch.globaz.al.business.services.declarationVersement.DeclarationVersementGlobalFrontaliersNonActifService;
import ch.globaz.al.business.services.declarationVersement.DeclarationVersementGlobalImposeSourceService;
import ch.globaz.al.business.services.declarationVersement.ProtocoleInfoDeclarationVersementService;
import ch.globaz.al.business.services.echeances.DatesEcheancePrivateService;
import ch.globaz.al.business.services.echeances.ProtocoleErreursImpressionEcheancesService;
import ch.globaz.al.business.services.entetesDocument.EnteteDocumentUserInfoService;
import ch.globaz.al.business.services.entetesDocument.EnteteService;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentService;
import ch.globaz.al.business.services.generation.prestations.GenerationService;
import ch.globaz.al.business.services.generation.prestations.ProtocoleErreursGenerationService;
import ch.globaz.al.business.services.horloger.HorlogerBusinessService;
import ch.globaz.al.business.services.importation.ImportationADIService;
import ch.globaz.al.business.services.importation.ImportationAllocataireService;
import ch.globaz.al.business.services.importation.ImportationDossierService;
import ch.globaz.al.business.services.importation.ImportationDroitService;
import ch.globaz.al.business.services.importation.ImportationPrestationService;
import ch.globaz.al.business.services.models.adi.AdiDecompteComplexModelService;
import ch.globaz.al.business.services.models.adi.AdiEnfantMoisModelService;
import ch.globaz.al.business.services.models.allocataire.AgricoleModelService;
import ch.globaz.al.business.services.models.allocataire.AllocataireAgricoleComplexModelService;
import ch.globaz.al.business.services.models.allocataire.AllocataireComplexModelService;
import ch.globaz.al.business.services.models.allocataire.AllocataireModelService;
import ch.globaz.al.business.services.models.allocataire.RevenuModelService;
import ch.globaz.al.business.services.models.dossier.AffilieListComplexModelService;
import ch.globaz.al.business.services.models.dossier.CommentaireModelService;
import ch.globaz.al.business.services.models.dossier.DossierBusinessService;
import ch.globaz.al.business.services.models.dossier.DossierDeclarationVersementComplexModelService;
import ch.globaz.al.business.services.models.dossier.DossierFkModelService;
import ch.globaz.al.business.services.models.dossier.LienDossierModelService;
import ch.globaz.al.business.services.models.droit.DroitModelService;
import ch.globaz.al.business.services.models.droit.EnfantComplexModelService;
import ch.globaz.al.business.services.models.droit.EnfantModelService;
import ch.globaz.al.business.services.models.envoi.EnvoiItemSimpleModelService;
import ch.globaz.al.business.services.models.envoi.EnvoiJobSimpleModelService;
import ch.globaz.al.business.services.models.envoi.EnvoiParametresSimpleModelService;
import ch.globaz.al.business.services.models.envoi.EnvoiTemplateSimpleModelService;
import ch.globaz.al.business.services.models.importation.CorrespondanceIdentifiantsService;
import ch.globaz.al.business.services.models.prestation.DeclarationVersementDetailleComplexModelService;
import ch.globaz.al.business.services.models.prestation.DetailPrestationGenComplexModelService;
import ch.globaz.al.business.services.models.prestation.DetailPrestationModelService;
import ch.globaz.al.business.services.models.prestation.EnteteAndDetailPrestationComplexModelService;
import ch.globaz.al.business.services.models.prestation.EntetePrestationModelService;
import ch.globaz.al.business.services.models.prestation.RecapitulatifEntrepriseImpressionComplexModelService;
import ch.globaz.al.business.services.models.prestation.TransfertTucanaModelService;
import ch.globaz.al.business.services.models.processus.TraitementHistoriqueModelService;
import ch.globaz.al.business.services.models.processus.TraitementPeriodiqueModelService;
import ch.globaz.al.business.services.models.rafam.AnnonceRafamDelegueProtocoleComplexModelService;
import ch.globaz.al.business.services.models.rafam.AnnonceRafamErrorComplexModelService;
import ch.globaz.al.business.services.models.rafam.AnnonceRafamProtocoleComplexModelService;
import ch.globaz.al.business.services.models.tarif.CategorieTarifComplexModelService;
import ch.globaz.al.business.services.models.tarif.CategorieTarifModelService;
import ch.globaz.al.business.services.models.tarif.CritereTarifModelService;
import ch.globaz.al.business.services.models.tarif.EcheanceComplexModelService;
import ch.globaz.al.business.services.models.tarif.LegislationTarifModelService;
import ch.globaz.al.business.services.models.tarif.PrestationTarifModelService;
import ch.globaz.al.business.services.models.tarif.TarifComplexModelService;
import ch.globaz.al.business.services.paiement.PaiementDirectProtocolesService;
import ch.globaz.al.business.services.paiement.PaiementDirectService;
import ch.globaz.al.business.services.paiement.ProtocoleCSVPaiementDirect;
import ch.globaz.al.business.services.paiement.ProtocoleDetaillePaiementDirectService;
import ch.globaz.al.business.services.paiement.ProtocoleErreursPaiementDirectService;
import ch.globaz.al.business.services.paiement.ProtocoleRecapitulatifPaiementDirectService;
import ch.globaz.al.business.services.rafam.AnnonceRafamBusinessService;
import ch.globaz.al.business.services.rafam.AnnonceRafamEtatRegistreService;
import ch.globaz.al.business.services.rafam.AnnoncesRafamNewXSDVersionSearchService;
import ch.globaz.al.business.services.rafam.AnnoncesRafamSearchService;
import ch.globaz.al.business.services.rafam.InitAnnoncesRafamService;
import ch.globaz.al.business.services.rafam.sedex.AnnonceRafamSedexService;
import ch.globaz.al.business.services.recapitulatifs.RecapitulatifsListeAffilieService;
import ch.globaz.al.business.services.rubriques.RubriqueService;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesService;
import ch.globaz.al.business.services.signatures.SignatureService;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseService;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

/**
 * Service locator pour les services privés de l'application AF
 *
 * @author jts, gmo, jer, pta
 */
public abstract class ALImplServiceLocator extends ALServiceLocator {

    /**
     *
     * @return instance du service de gestion de la persistance des adi décomptes complexes
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static AdiDecompteComplexModelService getAdiDecompteComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (AdiDecompteComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AdiDecompteComplexModelService.class);
    }

    /**
     *
     * @return instance du service de gestion de la persistance des décomptes détaillés des adi
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static AdiDecompteDetailleService getAdiDecompteDetailleService()
            throws JadeApplicationServiceNotAvailableException {
        return (AdiDecompteDetailleService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AdiDecompteDetailleService.class);
    }

    /**
     *
     * @return instance du service de gestion de la persistance des décomptes globaux des adi
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static AdiDecompteGlobalService getAdiDecompteGlobalService()
            throws JadeApplicationServiceNotAvailableException {
        return (AdiDecompteGlobalService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AdiDecompteGlobalService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance des informations enfant pour les ADI
     *
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static AdiEnfantMoisModelService getAdiEnfantMoisModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (AdiEnfantMoisModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AdiEnfantMoisModelService.class);
    }

    /**
     * retourne une instance du service Affiliation
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible Exception levée si le service n'est pas accessible
     * @see ch.globaz.al.business.services.affiliation.AffiliationService
     */
    public static AffiliationService getAffiliationService() throws JadeApplicationServiceNotAvailableException {
        return (AffiliationService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AffiliationService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance des informations des affiliés des dossiers
     *
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static AffilieListComplexModelService getAffilieListComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (AffilieListComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AffilieListComplexModelService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance des agricoles
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static AgricoleModelService getAgricoleModelService() throws JadeApplicationServiceNotAvailableException {
        return (AgricoleModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AgricoleModelService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance des allocataires agricoles
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static AllocataireAgricoleComplexModelService getAllocataireAgricoleComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (AllocataireAgricoleComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AllocataireAgricoleComplexModelService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance des allocataires
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static AllocataireComplexModelService getAllocataireComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (AllocataireComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AllocataireComplexModelService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance des allocataires
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static AllocataireModelService getAllocataireModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (AllocataireModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AllocataireModelService.class);

    }

    /**
     *
     * @return Instance du service métier lié aux annonces RAFAM
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static AnnonceRafamBusinessService getAnnonceRafamBusinessService()
            throws JadeApplicationServiceNotAvailableException {

        return (AnnonceRafamBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AnnonceRafamBusinessService.class);

    }

    public static AnnonceRafamDelegueProtocoleComplexModelService getAnnonceRafamDelegueProtocoleComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (AnnonceRafamDelegueProtocoleComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AnnonceRafamDelegueProtocoleComplexModelService.class);
    }

    public static AnnonceRafamErrorComplexModelService getAnnonceRafamErrorComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (AnnonceRafamErrorComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AnnonceRafamErrorComplexModelService.class);
    }

    /**
     *
     * @return Instance du service de gestion de l'état du registre du RAFam
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static AnnonceRafamEtatRegistreService getAnnonceRafamEtatRegistreService()
            throws JadeApplicationServiceNotAvailableException {
        return (AnnonceRafamEtatRegistreService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AnnonceRafamEtatRegistreService.class);
    }

    public static AnnonceRafamProtocoleComplexModelService getAnnonceRafamProtocoleComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (AnnonceRafamProtocoleComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AnnonceRafamProtocoleComplexModelService.class);
    }

    public static AnnonceRafamSedexService getAnnonceRafamSedexService()
            throws JadeApplicationServiceNotAvailableException {
        return (AnnonceRafamSedexService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AnnonceRafamSedexService.class);
    }

    /**
     *
     * @return instance du service RAFam utils
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static AnnoncesRafamSearchService getAnnoncesRafamSearchService()
            throws JadeApplicationServiceNotAvailableException {
        return (AnnoncesRafamSearchService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AnnoncesRafamSearchService.class);
    }

    /**
     *
     * @return instance du service RAFam utils avec les nouveaux schéma xsd (4.1)
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static AnnoncesRafamNewXSDVersionSearchService getAnnoncesRafamNewXSDVersionSearchService()
            throws JadeApplicationServiceNotAvailableException {
        return (AnnoncesRafamNewXSDVersionSearchService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AnnoncesRafamNewXSDVersionSearchService.class);
    }

    /**
     * Retourne une instance du service de calcul des montants
     *
     * @return Une instance du service de calcul des montants
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     * @see ch.globaz.al.business.services.calcul.CalculBusinessService
     */
    public static CalculMontantsService getCalculMontantsService() throws JadeApplicationServiceNotAvailableException {
        return (CalculMontantsService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(CalculMontantsService.class);
    }

    /**
     * Retourne une instance du service de calcul
     *
     * @return Une instance de l'implémentation du service CalculService
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     * @see ch.globaz.al.business.services.calcul.CalculBusinessService
     */
    public static CalculService getCalculService() throws JadeApplicationServiceNotAvailableException {
        return (CalculService) JadeApplicationServiceLocator.getInstance().getServiceImpl(CalculService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance des catégories (modèle complexe) de tarif
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static CategorieTarifComplexModelService getCategorieTarifComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (CategorieTarifComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(CategorieTarifComplexModelService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance des catégories de tarif
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static CategorieTarifModelService getCategorieTarifModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (CategorieTarifModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(CategorieTarifModelService.class);
    }

    /**
     * @return Instance du service de gestion de la persistance des commentaires
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static CommentaireModelService getCommentaireModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (CommentaireModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(CommentaireModelService.class);
    }

    /**
     * Retourne une instance du service de compensation sur facture
     *
     * @return Instance du service de compensations sur facture
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static CompensationFactureService getCompensationFactureService()
            throws JadeApplicationServiceNotAvailableException {
        return (CompensationFactureService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(CompensationFactureService.class);
    }

    /**
     * retourne une instance du service de gestion des copies
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible Exception levée si le service n'est pas accessible
     * @see ch.globaz.al.business.services.copies.CopiesService
     */
    public static CopiesService getCopiesService() throws JadeApplicationServiceNotAvailableException {
        return (CopiesService) JadeApplicationServiceLocator.getInstance().getServiceImpl(CopiesService.class);
    }

    /**
     * @return instance du service de gestion de la persistance de la correspondance des id Alfa-Gest et WebAF
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static CorrespondanceIdentifiantsService getCorrespondanceIdentifiantsService()
            throws JadeApplicationServiceNotAvailableException {
        return (CorrespondanceIdentifiantsService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(CorrespondanceIdentifiantsService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance des critères de tarif
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */

    public static CritereTarifModelService getCritereTarifModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (CritereTarifModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(CritereTarifModelService.class);
    }

    /**
     * Retourne un instance du service métier de gestion des échéances
     *
     * @return une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible * @throws
     *             JadeApplicationServiceNotAvailableException Exception levée si le service n'est pas accessible
     */
    public static DatesEcheancePrivateService getDatesEcheancePrivateService()
            throws JadeApplicationServiceNotAvailableException {
        return (DatesEcheancePrivateService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DatesEcheancePrivateService.class);
    }

    /**
     * @return une instaance du service de la persistance des déclarations de versement détaillées
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible * @throws
     *             JadeApplicationServiceNotAvailableException Exception levée si le service n'est pas accessible
     */
    public static DeclarationVersementDetailleComplexModelService getDeclarationVersementDetailleComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (DeclarationVersementDetailleComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DeclarationVersementDetailleComplexModelService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance de déclarations de versement détaillées pour les
     *         déclarations sur demande paiements directs
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static DeclarationVersementDetailleDemandeService getDeclarationVersementDetailleDemandeService()
            throws JadeApplicationServiceNotAvailableException {
        return (DeclarationVersementDetailleDemandeService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DeclarationVersementDetailleDemandeService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance de déclarations de versement détaillées pour les
     *         paiements directs
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static DeclarationVersementDetailleDirectService getDeclarationVersementDetailleDirectService()
            throws JadeApplicationServiceNotAvailableException {
        return (DeclarationVersementDetailleDirectService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DeclarationVersementDetailleDirectService.class);
    }

    /**
     * Retourne une instance du service de gestion de la persistance de déclarations d'attestation de prestations
     * détaillées pour les frontaliers
     *
     * @return une instance du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */

    public static DeclarationVersementDetailleFrontaliersNonActifService getDeclarationVersementDetailleFrontaliersNonActifService()
            throws JadeApplicationServiceNotAvailableException {
        return (DeclarationVersementDetailleFrontaliersNonActifService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DeclarationVersementDetailleFrontaliersNonActifService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance de déclarations de versement détaillées pour les
     *         paiements directs imposés à la source
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static DeclarationVersementDetailleImposeSourceService getDeclarationVersementDetailleImposeSourceService()
            throws JadeApplicationServiceNotAvailableException {
        return (DeclarationVersementDetailleImposeSourceService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DeclarationVersementDetailleImposeSourceService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance de la liste des déclarations de services
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static DeclarationVersementGlobalDirectService getDeclarationVersementGlobalDirectService()
            throws JadeApplicationServiceNotAvailableException {
        return (DeclarationVersementGlobalDirectService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DeclarationVersementGlobalDirectService.class);
    }

    /**
     * Retourne une instance du service de gestion de la persistance de déclarations d'attestation de prestations pour
     * les frontaliers
     *
     * @return une instance du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static DeclarationVersementGlobalFrontaliersNonActifService getDeclarationVersementGlobalFrontaliersNonActifService()
            throws JadeApplicationServiceNotAvailableException {
        return (DeclarationVersementGlobalFrontaliersNonActifService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DeclarationVersementGlobalFrontaliersNonActifService.class);
    }

    /**
     *
     * @return une instance sdu service métier pour l'établissement d'une attestation d'un dossier imposé à la source
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static DeclarationVersementGlobalImposeSourceService getDeclarationVersementGlobalImposeSourceService()
            throws JadeApplicationServiceNotAvailableException {
        return (DeclarationVersementGlobalImposeSourceService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DeclarationVersementGlobalImposeSourceService.class);
    }

    /**
     * @return Instance du service de gestion de la persistance des détails de prestations (modèle complexe)
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static DetailPrestationGenComplexModelService getDetailPrestationGenComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (DetailPrestationGenComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DetailPrestationGenComplexModelService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance des détails de prestations
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static DetailPrestationModelService getDetailPrestationModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (DetailPrestationModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DetailPrestationModelService.class);
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
     *
     * @return Instance du service de gestion de la persistance des déclaration de versements
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static DossierDeclarationVersementComplexModelService getDossierDeclarationVersementComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (DossierDeclarationVersementComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DossierDeclarationVersementComplexModelService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance des dossiers
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static DossierFkModelService getDossierFkModelService() throws JadeApplicationServiceNotAvailableException {
        return (DossierFkModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DossierFkModelService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance des droits
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static DroitModelService getDroitModelService() throws JadeApplicationServiceNotAvailableException {
        return (DroitModelService) JadeApplicationServiceLocator.getInstance().getServiceImpl(DroitModelService.class);
    }

    /**
     *
     * @return Instance du service de gestion
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */

    public static EcheanceComplexModelService getEcheanceComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (EcheanceComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(EcheanceComplexModelService.class);
    }

    /**
     * @return Instance du service de gestion de la persistance des enfants
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static EnfantComplexModelService getEnfantComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (EnfantComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(EnfantComplexModelService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance des enfants
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static EnfantModelService getEnfantModelService() throws JadeApplicationServiceNotAvailableException {
        return (EnfantModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(EnfantModelService.class);
    }

    public static EnteteAndDetailPrestationComplexModelService getEnteteAndDetailPrestationComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (EnteteAndDetailPrestationComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(EnteteAndDetailPrestationComplexModelService.class);
    }

    /**
     * Retourne le service d'entete de document correspondnant à <code>serviceClassImpl</code>
     *
     * @param serviceClassImpl
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */

    public static EnteteDocumentService getEnteteDocumentService(Class serviceClassImpl)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException {
        if (serviceClassImpl == null) {
            throw new ALEnteteDocumentException(
                    "ALImplServiceLocator, unable initialize EnteteDocumentService, interface class not defined!");
        } else if (!EnteteDocumentService.class.isAssignableFrom(serviceClassImpl)) {
            throw new ALEnteteDocumentException(
                    "ALImplServiceLocator, unable initialize EnteteDocumentService, interface is not a EnteteDocumentService sub-interface!");

        }
        return (EnteteDocumentService) JadeApplicationServiceLocator.getInstance().getServiceImpl(serviceClassImpl);
    }

    // FIXME : javadoc
    public static EnteteDocumentUserInfoService getEnteteDocumentUserInfoService()
            throws JadeApplicationServiceNotAvailableException {
        return (EnteteDocumentUserInfoService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(EnteteDocumentUserInfoService.class);

    }

    /**
     *
     * @return Instance du service de gestion de la persistance des en-tête de prestation
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static EntetePrestationModelService getEntetePrestationModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (EntetePrestationModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(EntetePrestationModelService.class);

    }

    /**
     *
     * @return une une instance de l'implémentatind du service d'entete
     * @throws JadeApplicationServiceNotAvailableException
     *             xception levée si le service n'est pas accessible
     */
    public static EnteteService getEnteteService() throws JadeApplicationServiceNotAvailableException {
        return (EnteteService) JadeApplicationServiceLocator.getInstance().getServiceImpl(EnteteService.class);
    }

    /**
     * Récupération du service de gestion des envois simples AF
     *
     * @return Le service instancié
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static EnvoiItemSimpleModelService getEnvoiItemSimpleModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (EnvoiItemSimpleModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(EnvoiItemSimpleModelService.class);

    }

    /**
     * Récupération du service de gestion des jobs d'envoi AF
     *
     * @return Le service instancié
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static EnvoiJobSimpleModelService getEnvoiJobSimpleModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (EnvoiJobSimpleModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(EnvoiJobSimpleModelService.class);

    }

    /**
     * Récupération du service de gestion des paramètres envois AF
     *
     * @return Le service instancié
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static EnvoiParametresSimpleModelService getEnvoiParametresSimpleModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (EnvoiParametresSimpleModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(EnvoiParametresSimpleModelService.class);

    }

    /**
     * Récupération du service de gestion de la persistence des templates d'envoi AF
     *
     * @return Le service instancié
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static EnvoiTemplateSimpleModelService getEnvoiTemplateSimpleModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (EnvoiTemplateSimpleModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(EnvoiTemplateSimpleModelService.class);

    }

    /**
     * Retourne une instance du service de génération des prestations
     *
     * @return Une instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static GenerationService getGenerationService() throws JadeApplicationServiceNotAvailableException {
        return (GenerationService) JadeApplicationServiceLocator.getInstance().getServiceImpl(GenerationService.class);
    }

    /**
     *
     * @return instance du service utile pour les caisses horlogères
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static HorlogerBusinessService getHorlogerBusinessService()
            throws JadeApplicationServiceNotAvailableException {
        return (HorlogerBusinessService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(HorlogerBusinessService.class);
    }

    /**
     *
     * @return Instance du service d'importation des données de prestations ADI
     *
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static ImportationADIService getImportationADIService() throws JadeApplicationServiceNotAvailableException {
        return (ImportationADIService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ImportationADIService.class);
    }

    /**
     *
     * @return Instance du service d'importation des données d'allocataire
     *
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static ImportationAllocataireService getImportationAllocataireService()
            throws JadeApplicationServiceNotAvailableException {
        return (ImportationAllocataireService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ImportationAllocataireService.class);
    }

    /**
     *
     * @return Instance du service d'importation des données de dossier
     *
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static ImportationDossierService getImportationDossierService()
            throws JadeApplicationServiceNotAvailableException {
        return (ImportationDossierService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ImportationDossierService.class);
    }

    /**
     *
     * @return Instance du service d'importation des droits
     *
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static ImportationDroitService getImportationDroitService()
            throws JadeApplicationServiceNotAvailableException {
        return (ImportationDroitService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ImportationDroitService.class);
    }

    /**
     *
     * @return Instance du service d'importation des données de dossier
     *
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static ImportationPrestationService getImportationPrestationService()
            throws JadeApplicationServiceNotAvailableException {
        return (ImportationPrestationService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ImportationPrestationService.class);
    }

    /**
     *
     * @return Instance du service d'initialisation du RAFam
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static InitAnnoncesRafamService getInitAnnoncesRafamService()
            throws JadeApplicationServiceNotAvailableException {
        return (InitAnnoncesRafamService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(InitAnnoncesRafamService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance des législations de tarifs
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static LegislationTarifModelService getLegislationTarifModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (LegislationTarifModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(LegislationTarifModelService.class);
    }

    /**
     *
     * @return instance du service de gestion de la persistance des liens de dossiers
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static LienDossierModelService getLienDossierModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (LienDossierModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(LienDossierModelService.class);
    }

    /**
     *
     * @return Instance du service d'impression des protocoles de paiement direct
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static PaiementDirectProtocolesService getPaiementDirectProtocolesService()
            throws JadeApplicationServiceNotAvailableException {
        return (PaiementDirectProtocolesService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(PaiementDirectProtocolesService.class);
    }

    /**
     * Retourne une instance du service de paiement direct
     *
     * @return Instance du service de compensations sur facture
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static PaiementDirectService getPaiementDirectService() throws JadeApplicationServiceNotAvailableException {
        return (PaiementDirectService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(PaiementDirectService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance des tarif (prestations)
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static PrestationTarifModelService getPrestationTarifModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (PrestationTarifModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(PrestationTarifModelService.class);
    }

    /**
     *
     * @return instance du service de génération de protocole CSV pour les compensations
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static ProtocoleCSVCompensation getProtocoleCSVCompensation()
            throws JadeApplicationServiceNotAvailableException {
        return (ProtocoleCSVCompensation) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ProtocoleCSVCompensation.class);
    }

    /**
     *
     * @return instance du service de génération de protocole CSV pour les paiements direct
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static ProtocoleCSVPaiementDirect getProtocoleCSVPaiementDirect()
            throws JadeApplicationServiceNotAvailableException {
        return (ProtocoleCSVPaiementDirect) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ProtocoleCSVPaiementDirect.class);
    }

    /**
     *
     * @return Instance du service de génération de protocoles de compensation sur facture
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static ProtocoleDetailleCompensationService getProtocoleDetailleCompensationService()
            throws JadeApplicationServiceNotAvailableException {
        return (ProtocoleDetailleCompensationService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ProtocoleDetailleCompensationService.class);
    }

    /**
     *
     * @return Instance du service de génération de protocoles de paiement direct
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static ProtocoleDetaillePaiementDirectService getProtocoleDetaillePaiementDirectService()
            throws JadeApplicationServiceNotAvailableException {
        return (ProtocoleDetaillePaiementDirectService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ProtocoleDetaillePaiementDirectService.class);
    }

    /**
     *
     * @return Instance du service de génération de protocoles d'erreurs de compensation sur facture
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static ProtocoleErreursCompensationService getProtocoleErreursCompensationService()
            throws JadeApplicationServiceNotAvailableException {
        return (ProtocoleErreursCompensationService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ProtocoleErreursCompensationService.class);
    }

    /**
     * Retourne une instance du service de génération de protocole pour la génération de prestations
     *
     * @return instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static ProtocoleErreursGenerationService getProtocoleErreursGenerationService()
            throws JadeApplicationServiceNotAvailableException {
        return (ProtocoleErreursGenerationService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ProtocoleErreursGenerationService.class);

    }

    /**
     * @return Instance du service de génération du protocole d'erreurs de l'impression des avis d'échéances
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static ProtocoleErreursImpressionEcheancesService getProtocoleErreursImpressionEcheancesService()
            throws JadeApplicationServiceNotAvailableException {
        return (ProtocoleErreursImpressionEcheancesService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ProtocoleErreursImpressionEcheancesService.class);
    }

    /**
     *
     * @return Instance du service de génération de protocoles d'erreurs de paiement direct
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static ProtocoleErreursPaiementDirectService getProtocoleErreursPaiementDirectService()
            throws JadeApplicationServiceNotAvailableException {
        return (ProtocoleErreursPaiementDirectService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ProtocoleErreursPaiementDirectService.class);
    }

    /**
     * Retourne une instance du service de génération de protocole pour le protocole d'info de déclaration de versement
     *
     * @return instance de l'implémentation du service
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */

    public static ProtocoleInfoDeclarationVersementService getProtocoleInfoDeclarationVersementService()
            throws JadeApplicationServiceNotAvailableException {
        return (ProtocoleInfoDeclarationVersementService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ProtocoleInfoDeclarationVersementService.class);
    }

    /**
     *
     * @return Instance du service de génération de protocoles de paiementdirect
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static ProtocoleRecapitulatifPaiementDirectService getProtocoleRecapitulatifPaiementDirectService()
            throws JadeApplicationServiceNotAvailableException {
        return (ProtocoleRecapitulatifPaiementDirectService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ProtocoleRecapitulatifPaiementDirectService.class);
    }

    /**
     *
     * @return Instance du service de génération de protocoles de compensation sur facture
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static ProtocoleRecapitulatifCompensationService getProtocolesRecapitulatifCompensationService()
            throws JadeApplicationServiceNotAvailableException {
        return (ProtocoleRecapitulatifCompensationService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ProtocoleRecapitulatifCompensationService.class);
    }

    /**
     *
     * @return une instance du service de la persistance des récapitulatifs d'entreprise destiné à l'impression
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */

    public static RecapitulatifEntrepriseImpressionComplexModelService getRecapitulatifEntrepriseImpressionComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (RecapitulatifEntrepriseImpressionComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(RecapitulatifEntrepriseImpressionComplexModelService.class);
    }

    /**
     * Retourne une instance du service de gestion de la liste des récapitulatifs
     *
     * @return Instance du service de gestion de la persistence des listes des récap à imprimer
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static RecapitulatifsListeAffilieService getRecapitulatifsListeAffilieService()
            throws JadeApplicationServiceNotAvailableException {
        return (RecapitulatifsListeAffilieService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(RecapitulatifsListeAffilieService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance des revenus
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static RevenuModelService getRevenuModelService() throws JadeApplicationServiceNotAvailableException {
        return (RevenuModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(RevenuModelService.class);

    }

    /**
     * Retourne le service de rubrique comptable correspondant à <code>serviceClassImpl</code>
     *
     * @param serviceClassImpl
     *            Service à rechercher
     * @return Une instance de l'implémentation du service DecisionService
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static RubriquesComptablesService getRubriqueComptableService(Class serviceClassImpl)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException {
        if (serviceClassImpl == null) {
            throw new ALRubriquesException(
                    "ALImplServiceLocator, unable to initialize RubriquesComptablesService service, interface class not defined!");
        } else if (!RubriquesComptablesService.class.isAssignableFrom(serviceClassImpl)) {
            throw new ALRubriquesException(
                    "ALImplServiceLocator, unable to initialize RubriquesComptablesService service, interface class is not a RubriquesComptablesService sub-interface!");
        }
        return (RubriquesComptablesService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(serviceClassImpl);
    }

    /**
     *
     * @return Instance du service de gestion des rubriques comptables
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static RubriqueService getRubriqueService() throws JadeApplicationServiceNotAvailableException {
        return (RubriqueService) JadeApplicationServiceLocator.getInstance().getServiceImpl(RubriqueService.class);
    }

    /**
     * Retourne le service de signature correspondant à <code>serviceClassImpl</code>
     *
     * @param serviceClassImpl
     *            service à rechercher
     * @return instance
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static SignatureCaisseService getSignatureCaisseService(Class serviceClassImpl)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException {

        if (serviceClassImpl == null) {
            throw new ALSignatureServiceException(
                    "ALImplServiceLocator, unable initialize SignatureCaisseService service, interface class not defined!");

        } else if (!SignatureCaisseService.class.isAssignableFrom(serviceClassImpl)) {
            throw new ALSignatureServiceException(
                    "ALImplServiceLocator, unable initialize SignatureCaisseService service, interface is not a SignatureCaisseService sub-interface!");

        }

        return (SignatureCaisseService) JadeApplicationServiceLocator.getInstance().getServiceImpl(serviceClassImpl);
    }

    /**
     *
     * @return instances service de gestion des signatures des caisses
     * @throws JadeApplicationServiceNotAvailableException
     */

    public static SignatureService getSignatureService() throws JadeApplicationServiceNotAvailableException {
        return (SignatureService) JadeApplicationServiceLocator.getInstance().getServiceImpl(SignatureService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance des tarifs
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static TarifComplexModelService getTarifComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (TarifComplexModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(TarifComplexModelService.class);
    }

    /**
     *
     * @return Instance du service de gestion de la persistance de l'historique des traitements
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static TraitementHistoriqueModelService getTraitementHistoriqueModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (TraitementHistoriqueModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(TraitementHistoriqueModelService.class);
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
     * @return Instance du service de gestion de la persistance des transfert Tucana
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service n'est pas accessible
     */
    public static TransfertTucanaModelService getTransfertTucanaModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (TransfertTucanaModelService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(TransfertTucanaModelService.class);

    }

}