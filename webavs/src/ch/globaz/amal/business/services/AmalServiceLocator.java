package ch.globaz.amal.business.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.services.annonce.AnnonceService;
import ch.globaz.amal.business.services.models.annonce.SimpleAnnonceService;
import ch.globaz.amal.business.services.models.annoncesedex.ComplexAnnonceSedexService;
import ch.globaz.amal.business.services.models.annoncesedex.SimpleAnnonceSedexService;
import ch.globaz.amal.business.services.models.caissemaladie.CaisseMaladieService;
import ch.globaz.amal.business.services.models.caissemaladie.SimpleDetailCaisseMaladieService;
import ch.globaz.amal.business.services.models.contribuable.ContribuableService;
import ch.globaz.amal.business.services.models.controleurEnvoi.ControleurEnvoiService;
import ch.globaz.amal.business.services.models.controleurRappel.ControleurRappelService;
import ch.globaz.amal.business.services.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfantsService;
import ch.globaz.amal.business.services.models.detailfamille.DetailFamilleService;
import ch.globaz.amal.business.services.models.famille.FamilleContribuableService;
import ch.globaz.amal.business.services.models.formule.HistoriqueImportationService;
import ch.globaz.amal.business.services.models.parametreannuel.SimpleParametreAnnuelService;
import ch.globaz.amal.business.services.models.parametreapplication.SimpleParametreApplicationService;
import ch.globaz.amal.business.services.models.parametremodel.ParametreModelService;
import ch.globaz.amal.business.services.models.primeavantageuse.SimplePrimeAvantageuseService;
import ch.globaz.amal.business.services.models.primemoyenne.SimplePrimeMoyenneService;
import ch.globaz.amal.business.services.models.primesassurance.SimplePrimesAssuranceService;
import ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService;
import ch.globaz.amal.business.services.models.revenu.RevenuCalculService;
import ch.globaz.amal.business.services.models.revenu.RevenuService;
import ch.globaz.amal.business.services.models.subsideannee.SimpleSubsideAnneeService;
import ch.globaz.amal.business.services.models.uploadfichierreprise.SimpleUploadFichierRepriseService;

public class AmalServiceLocator {
    public static AnnonceService getAnnonceService() throws JadeApplicationServiceNotAvailableException {
        return (AnnonceService) JadeApplicationServiceLocator.getInstance().getServiceImpl(AnnonceService.class);
    }

    /**
     * @return Impl�mentation du service de gestion des caisse maladie
     * @throws JadeApplicationServiceNotAvailableException
     *             Lev�e lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static CaisseMaladieService getCaisseMaladieService() throws JadeApplicationServiceNotAvailableException {
        return (CaisseMaladieService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CaisseMaladieService.class);
    }

    /**
     * Impl�mentation du service d'annonce sedex complex mod�le
     * 
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static ComplexAnnonceSedexService getComplexAnnonceSedexService()
            throws JadeApplicationServiceNotAvailableException {
        return (ComplexAnnonceSedexService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                ComplexAnnonceSedexService.class);
    }

    /**
     * @return Impl�mentation du service de gestion de contribuable de reprise
     * @throws JadeApplicationServiceNotAvailableException
     *             Lev�e lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static ContribuableRepriseService getContribuableRepriseService()
            throws JadeApplicationServiceNotAvailableException {
        return (ContribuableRepriseService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                ContribuableRepriseService.class);
    }

    /**
     * @return Impl�mentation du service de gestion de contribuable
     * @throws JadeApplicationServiceNotAvailableException
     *             Lev�e lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static ContribuableService getContribuableService() throws JadeApplicationServiceNotAvailableException {
        return (ContribuableService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                ContribuableService.class);
    }

    /**
     * 
     * @return ControleurEnvoiService, service de gestion des travaux envois
     * @throws JadeApplicationServiceNotAvailableException
     *             Lev�e lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static ControleurEnvoiService getControleurEnvoiService() throws JadeApplicationServiceNotAvailableException {
        return (ControleurEnvoiService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                ControleurEnvoiService.class);
    }

    /**
     * 
     * @return ControleurRappelService, service de gestion des travaux rappels
     * @throws JadeApplicationServiceNotAvailableException
     *             Lev�e lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static ControleurRappelService getControleurRappelService()
            throws JadeApplicationServiceNotAvailableException {
        return (ControleurRappelService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                ControleurRappelService.class);
    }

    /**
     * @return Impl�mentation du service de gestion des d�ductions fiscales enfants
     * @throws JadeApplicationServiceNotAvailableException
     *             Lev�e lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleDeductionsFiscalesEnfantsService getDeductionsFiscalesEnfantsService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleDeductionsFiscalesEnfantsService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDeductionsFiscalesEnfantsService.class);
    }

    /**
     * @return Impl�mentation du service de gestion des details de caisse maladie
     * @throws JadeApplicationServiceNotAvailableException
     *             Lev�e lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleDetailCaisseMaladieService getDetailCaisseMaladieService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleDetailCaisseMaladieService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDetailCaisseMaladieService.class);
    }

    /**
     * 
     * @return Impl�mentation du service de gestion du d�tail d'un membre de la famille du contribuable
     * @throws JadeApplicationServiceNotAvailableException
     *             Lev�e lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static DetailFamilleService getDetailFamilleService() throws JadeApplicationServiceNotAvailableException {
        return (DetailFamilleService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DetailFamilleService.class);
    }

    /**
     * 
     * @return Impl�mentation du service de gestion de la famille du contribuable
     * @throws JadeApplicationServiceNotAvailableException
     *             Lev�e lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static FamilleContribuableService getFamilleContribuableService()
            throws JadeApplicationServiceNotAvailableException {
        return (FamilleContribuableService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                FamilleContribuableService.class);
    }

    /**
     * @return Impl�mentation du service d historique des importation
     * @throws JadeApplicationServiceNotAvailableException
     *             Lev�e lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static HistoriqueImportationService getHistoriqueImportationService()
            throws JadeApplicationServiceNotAvailableException {
        return (HistoriqueImportationService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                HistoriqueImportationService.class);
    }

    /**
     * @return Impl�mentation du service de gestion des param�tres annuels
     * @throws JadeApplicationServiceNotAvailableException
     *             Lev�e lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleParametreAnnuelService getParametreAnnuelService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleParametreAnnuelService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleParametreAnnuelService.class);
    }

    /**
     * @return Impl�mentation du service de gestion des param�tres models
     * @throws JadeApplicationServiceNotAvailableException
     *             Lev�e lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static ParametreModelService getParametreModelService() throws JadeApplicationServiceNotAvailableException {
        return (ParametreModelService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                ParametreModelService.class);
    }

    /**
     * @return Impl�mentation du service de gestion des primes d'assurances moyennes
     * @throws JadeApplicationServiceNotAvailableException
     *             Lev�e lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimplePrimesAssuranceService getPrimesAssuranceService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimplePrimesAssuranceService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimplePrimesAssuranceService.class);
    }

    /**
     * @return Impl�mentation du service de gestion de revenus
     * @throws JadeApplicationServiceNotAvailableException
     *             Lev�e lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static RevenuCalculService getRevenuCalculService() throws JadeApplicationServiceNotAvailableException {
        return (RevenuCalculService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                RevenuCalculService.class);
    }

    /**
     * @return Impl�mentation du service de gestion de revenus
     * @throws JadeApplicationServiceNotAvailableException
     *             Lev�e lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static RevenuService getRevenuService() throws JadeApplicationServiceNotAvailableException {
        return (RevenuService) JadeApplicationServiceLocator.getInstance().getServiceImpl(RevenuService.class);
    }

    /**
     * @return Impl�mentation du service de gestion des caisse maladie
     * @throws JadeApplicationServiceNotAvailableException
     *             Lev�e lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleAnnonceService getSimpleAnnonceService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleAnnonceService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleAnnonceService.class);
    }

    /**
     * @return Impl�mentation du service de gestion des annonces SEDEX
     * @throws JadeApplicationServiceNotAvailableException
     *             Lev�e lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleAnnonceSedexService getSimpleAnnonceSedexService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleAnnonceSedexService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleAnnonceSedexService.class);
    }

    /**
     * R�cup�ration d'une instance du service Simple Parametre Application - db
     * 
     * @return le service simple param�tre application, utilisable
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleParametreApplicationService getSimpleParametreApplicationService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleParametreApplicationService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleParametreApplicationService.class);
    }

    /**
     * @return Impl�mentation du service de gestion des primes moyennes
     * @throws JadeApplicationServiceNotAvailableException
     *             Lev�e lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimplePrimeMoyenneService getSimplePrimeMoyenneService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimplePrimeMoyenneService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimplePrimeMoyenneService.class);
    }

    /**
     * @return Impl�mentation du service de gestion des primes avantageuses
     * @throws JadeApplicationServiceNotAvailableException
     *             Lev�e lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimplePrimeAvantageuseService getSimplePrimeAvantageuseService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimplePrimeAvantageuseService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimplePrimeAvantageuseService.class);
    }

    /**
     * @return Impl�mentation du service de gestion de subsides par ann�ee
     * @throws JadeApplicationServiceNotAvailableException
     *             Lev�e lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleSubsideAnneeService getSimpleSubsideAnneeService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleSubsideAnneeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleSubsideAnneeService.class);
    }

    /**
     * @return Impl�mentation du service de gestion de l'upload de fichier
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static SimpleUploadFichierRepriseService getSimpleUploadFichierService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleUploadFichierRepriseService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleUploadFichierRepriseService.class);
    }
}