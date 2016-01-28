package ch.globaz.amal.businessimpl.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.business.services.models.annonce.SimpleAnnonceService;
import ch.globaz.amal.business.services.models.annoncesedex.SimpleAnnonceSedexService;
import ch.globaz.amal.business.services.models.caissemaladie.SimpleDetailCaisseMaladieService;
import ch.globaz.amal.business.services.models.contribuable.ContribuableService;
import ch.globaz.amal.business.services.models.contribuable.SimpleContribuableService;
import ch.globaz.amal.business.services.models.controleurEnvoi.SimpleControleurEnvoiStatusService;
import ch.globaz.amal.business.services.models.controleurEnvoi.SimpleControleurJobService;
import ch.globaz.amal.business.services.models.detailfamille.SimpleDetailFamilleService;
import ch.globaz.amal.business.services.models.document.SimpleDocumentService;
import ch.globaz.amal.business.services.models.famille.SimpleFamilleService;
import ch.globaz.amal.business.services.models.revenu.SimpleRevenuContribuableService;
import ch.globaz.amal.business.services.models.revenu.SimpleRevenuDeterminantService;
import ch.globaz.amal.business.services.models.revenu.SimpleRevenuHistoriqueService;
import ch.globaz.amal.business.services.models.revenu.SimpleRevenuService;
import ch.globaz.amal.business.services.models.revenu.SimpleRevenuSourcierService;
import ch.globaz.amal.business.services.sedexRP.AnnoncesRPService;

public class AmalImplServiceLocator extends AmalServiceLocator {

    public static AnnoncesRPService getAnnoncesRPService() throws JadeApplicationServiceNotAvailableException {
        return (AnnoncesRPService) JadeApplicationServiceLocator.getInstance().getServiceImpl(AnnoncesRPService.class);
    }

    /**
     * Récupération d'une instance du service Contribuable - db
     * 
     * @return Le service Contribuable, utilisable
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static ContribuableService getContribuableService() throws JadeApplicationServiceNotAvailableException {
        return (ContribuableService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                ContribuableService.class);
    }

    /**
     * Récupération d'une instance du service des annonces sedex - db
     * 
     * @return Le service des annonces, utilisable
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleAnnonceSedexService getSimpleAnnonceSedexService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleAnnonceSedexService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleAnnonceSedexService.class);
    }

    /**
     * Récupération d'une instance du service des annonces - db
     * 
     * @return Le service des annonces, utilisable
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleAnnonceService getSimpleAnnonceService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleAnnonceService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleAnnonceService.class);
    }

    /**
     * Récupération d'une instance du service simplecontribuable - db
     * 
     * @return le service simplecontribuable, utilisable
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleContribuableService getSimpleContribuableService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleContribuableService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleContribuableService.class);
    }

    /**
     * Récupération d'une instance du service du controleur des envois - db
     * 
     * @return le service controleur envoi utilisable
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleControleurEnvoiStatusService getSimpleControleurEnvoiStatusService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleControleurEnvoiStatusService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleControleurEnvoiStatusService.class);
    }

    /**
     * Récupération d'une instance du service du controleur des jobs d'impression - db
     * 
     * @return le service controleur job utilisable
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleControleurJobService getSimpleControleurJobService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleControleurJobService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleControleurJobService.class);
    }

    /**
     * Récupération d'une instance du service simpleDetailCaisseMaladie - db
     * 
     * @return le service simpleDetailCaisseMaladie
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleDetailCaisseMaladieService getSimpleDetailCaisseMaladieService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleDetailCaisseMaladieService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDetailCaisseMaladieService.class);
    }

    /**
     * Récupération d'une instance du service des subsides (simpledetailfamille) - db
     * 
     * @return le service simple detail famille utilisable
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleDetailFamilleService getSimpleDetailFamilleService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleDetailFamilleService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDetailFamilleService.class);
    }

    /**
     * Récupération d'une instance du service des documents - db
     * 
     * @return le service simpledocument, utilisable
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleDocumentService getSimpleDocumentService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleDocumentService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDocumentService.class);
    }

    /**
     * Récupération d'une instance du service des simple familles - db
     * 
     * @return le service des simplefamille utilisable
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleFamilleService getSimpleFamilleService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleFamilleService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleFamilleService.class);
    }

    /**
     * Récupération du service des simplerevenucontribuable - db
     * 
     * @return le service simple revenu contribuable, utilisable
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleRevenuContribuableService getSimpleRevenuContribuableService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleRevenuContribuableService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleRevenuContribuableService.class);
    }

    /**
     * Récupération d'une instance du service des simples revenus déterminant - db
     * 
     * @return le service simplerevenudeterminant, utilisable
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleRevenuDeterminantService getSimpleRevenuDeterminantService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleRevenuDeterminantService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleRevenuDeterminantService.class);
    }

    /**
     * Récupération d'une instance du service Simple Revenu Historique - db
     * 
     * @return le service simple revenu historique, utilisable
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleRevenuHistoriqueService getSimpleRevenuHistoriqueService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleRevenuHistoriqueService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleRevenuHistoriqueService.class);
    }

    /**
     * Récupératio d'une instance du service simple revenu - db
     * 
     * @return une instance du service simple revenu, utilisable
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleRevenuService getSimpleRevenuService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleRevenuService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleRevenuService.class);
    }

    /**
     * Récupération d'une instance du service SimpleRevenuSourcier - db
     * 
     * @return le service simple revenu sourcier, utilisable
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleRevenuSourcierService getSimpleRevenuSourcierService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleRevenuSourcierService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleRevenuSourcierService.class);
    }

}
