package ch.globaz.vulpecula.business.services;

import static ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator.*;
import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.vulpecula.business.models.registres.ParametreCotisationAssociationComplexModel;
import ch.globaz.vulpecula.business.models.registres.ParametreCotisationAssociationSearchComplexModel;
import ch.globaz.vulpecula.business.services.absencesjustifiees.AbsenceJustifieeService;
import ch.globaz.vulpecula.business.services.administration.AdministrationServiceCRUD;
import ch.globaz.vulpecula.business.services.association.AssociationCotisationService;
import ch.globaz.vulpecula.business.services.association.CotisationAssociationProfessionnelleCRUDService;
import ch.globaz.vulpecula.business.services.caissemaladie.AffiliationCaisseMaladieService;
import ch.globaz.vulpecula.business.services.caissemaladie.SuiviCaisseMaladieService;
import ch.globaz.vulpecula.business.services.comptabilite.ReferenceRubriqueServiceCRUD;
import ch.globaz.vulpecula.business.services.compteur.CompteurService;
import ch.globaz.vulpecula.business.services.congepaye.CongePayeService;
import ch.globaz.vulpecula.business.services.decompte.CotisationDecompteService;
import ch.globaz.vulpecula.business.services.decompte.DecompteSalaireService;
import ch.globaz.vulpecula.business.services.decompte.DecompteService;
import ch.globaz.vulpecula.business.services.decompte.DecompteServiceCRUD;
import ch.globaz.vulpecula.business.services.employeur.EmployeurService;
import ch.globaz.vulpecula.business.services.is.ImpotSourceService;
import ch.globaz.vulpecula.business.services.is.ProcessusAFService;
import ch.globaz.vulpecula.business.services.is.TauxImpositionService;
import ch.globaz.vulpecula.business.services.is.TauxImpositionServiceCRUD;
import ch.globaz.vulpecula.business.services.postetravail.PosteTravailService;
import ch.globaz.vulpecula.business.services.postetravail.PosteTravailServiceCRUD;
import ch.globaz.vulpecula.business.services.properties.PropertiesService;
import ch.globaz.vulpecula.business.services.registre.CotisationAssociationProfessionnelleService;
import ch.globaz.vulpecula.business.services.registre.ParametreCotisationAssociationServiceCRUD;
import ch.globaz.vulpecula.business.services.registre.ParametreSyndicatService;
import ch.globaz.vulpecula.business.services.servicemilitaire.ServiceMilitaireService;
import ch.globaz.vulpecula.business.services.syndicat.AffiliationSyndicatService;
import ch.globaz.vulpecula.business.services.taxationoffice.TaxationOfficeService;
import ch.globaz.vulpecula.business.services.travailleur.TravailleurService;
import ch.globaz.vulpecula.business.services.travailleur.TravailleurServiceCRUD;
import ch.globaz.vulpecula.business.services.users.UsersService;
import ch.globaz.vulpecula.businessimpl.services.absencesjustifiees.AbsenceJustifieeServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.association.AssociationCotisationServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.caissemaladie.AffiliationCaisseMaladieServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.caissemaladie.SuiviCaisseMaladieServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.compteur.CompteurServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.congepaye.CongePayeServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.decompte.CotisationDecompteServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.decompte.DecompteSalaireServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.decompte.DecompteServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.employeur.EmployeurServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.is.ImpotSourceServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.is.ProcessusAFServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.is.TauxImpositionServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.postetravail.PosteTravailServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.properties.PropertiesServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.registre.CotisationAssociationProfessionnelleServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.registre.ParametreSyndicatServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.servicemilitaire.ServiceMilitaireServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.syndicat.AffiliationSyndicatServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.taxationoffice.TaxationOfficeServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.travailleur.TravailleurServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.users.UsersServiceImpl;
import ch.globaz.vulpecula.external.services.CotisationService;
import ch.globaz.vulpecula.external.services.CotisationServiceImpl;
import ch.globaz.vulpecula.external.services.musca.PassageService;
import ch.globaz.vulpecula.external.services.musca.PassageServiceImpl;

/**
 * @author jpa
 */
public class VulpeculaServiceLocator {

    public static TaxationOfficeService getTaxationOfficeService() {
        return TaxationOfficeServiceHolder.INSTANCE;
    }

    public static DecompteService getDecompteService() {
        return DecompteServiceHolder.INSTANCE;
    }

    public static DecompteSalaireService getDecompteSalaireService() {
        return DecompteSalaireServiceHolder.INSTANCE;
    }

    public static CotisationDecompteService getCotisationDecompteService() {
        return CotisationDecompteServiceHolder.INSTANCE;
    }

    public static PropertiesService getPropertiesService() {
        return PropertiesServiceHolder.INSTANCE;
    }

    public static TravailleurService getTravailleurService() {
        return TravailleurServiceHolder.INSTANCE;
    }

    public static PosteTravailService getPosteTravailService() {
        return PosteTravailServiceHolder.INSTANCE;
    }

    public static EmployeurService getEmployeurService() {
        return EmployeurServiceHolder.INSTANCE;
    }

    public static AbsenceJustifieeService getAbsenceJustifieeService() {
        return AbsenceJustifieeServiceHolder.INSTANCE;
    }

    public static CongePayeService getCongePayeService() {
        return CongePayeServiceHolder.INSTANCE;
    }

    public static CompteurService getCompteurService() {
        return CompteurServiceHolder.INSTANCE;
    }

    public static ServiceMilitaireService getServiceMilitaireService() {
        return ServiceMilitaireServiceHolder.INSTANCE;
    }

    public static CotisationAssociationProfessionnelleService getCotisationAssociationProfessionnelleService() {
        return CotisationAssociationProfessionnelleServiceHolder.INSTANCE;
    }

    public static AssociationCotisationService getAssociationCotisationService() {
        return AssociationCotisationServiceHolder.INSTANCE;
    }

    public static AffiliationCaisseMaladieService getAffiliationCaisseMaladieService() {
        return AffiliationCaisseMaladieServiceHolder.INSTANCE;
    }

    public static AffiliationSyndicatService getAffiliationSyndicatService() {
        return AffiliationSyndicatServiceHolder.INSTANCE;
    }

    public static ParametreSyndicatService getParametreSyndicatService() {
        return ParametreSyndicatServiceHolder.INSTANCE;
    }

    public static ImpotSourceService getImpotSourceService() {
        return ImpotSourceServiceHolder.INSTANCE;
    }

    public static ProcessusAFService getProcessusAFService() {
        return ProcessusAFServiceHolder.INSTANCE;
    }

    public static TauxImpositionService getTauxImpositionService() {
        return TauxImpositionServiceHolder.INSTANCE;
    }

    public static SuiviCaisseMaladieService getSuiviCaisseMaladieService() {
        return SuiviCaisseMaladieHolder.INSTANCE;
    }

    public static UsersService getUsersService() {
        return UsersHolder.INSTANCE;
    }

    // FIXME: Devrait être placé dans naos
    public static CotisationService getCotisationService() {
        return new CotisationServiceImpl();
    }

    // FIXME: Devrait être placé dans musca
    public static PassageService getPassageService() {
        return PassageServiceHolder.INSTANCE;
    }

    public static DecompteServiceCRUD getDecompteServiceCRUD() throws JadeApplicationServiceNotAvailableException {
        return (DecompteServiceCRUD) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DecompteServiceCRUD.class);
    }

    public static ch.globaz.vulpecula.business.services.employeur.EmployeurServiceCRUD getEmployeurServiceCRUD()
            throws JadeApplicationServiceNotAvailableException {
        return (ch.globaz.vulpecula.business.services.employeur.EmployeurServiceCRUD) JadeApplicationServiceLocator
                .getInstance().getServiceImpl(
                        ch.globaz.vulpecula.business.services.employeur.EmployeurServiceCRUD.class);
    }

    public static PosteTravailServiceCRUD getSimplePosteTravailService()
            throws JadeApplicationServiceNotAvailableException {
        return (PosteTravailServiceCRUD) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                PosteTravailServiceCRUD.class);
    }

    public static TravailleurServiceCRUD getTravailleurServiceCRUD() throws JadeApplicationServiceNotAvailableException {
        return (TravailleurServiceCRUD) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                TravailleurServiceCRUD.class);
    }

    public static JadeCrudService<AdministrationComplexModel, AdministrationSearchComplexModel> getAdministrationService()
            throws JadeApplicationServiceNotAvailableException {
        return (AdministrationServiceCRUD) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                AdministrationServiceCRUD.class);
    }

    public static JadeCrudService<ParametreCotisationAssociationComplexModel, ParametreCotisationAssociationSearchComplexModel> getParametreCotisationServiceCRUD()
            throws JadeApplicationServiceNotAvailableException {
        return (ParametreCotisationAssociationServiceCRUD) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                ParametreCotisationAssociationServiceCRUD.class);
    }

    public static CotisationAssociationProfessionnelleCRUDService getCotisationAssociationProfessionnelleCrudService()
            throws JadeApplicationServiceNotAvailableException {
        return (CotisationAssociationProfessionnelleCRUDService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(CotisationAssociationProfessionnelleCRUDService.class);
    }

    public static TauxImpositionServiceCRUD getTauxImpositionServiceCRUD()
            throws JadeApplicationServiceNotAvailableException {
        return (TauxImpositionServiceCRUD) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                TauxImpositionServiceCRUD.class);
    }

    public static ReferenceRubriqueServiceCRUD getReferenceRubriqueServiceCRUD()
            throws JadeApplicationServiceNotAvailableException {
        return (ReferenceRubriqueServiceCRUD) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                ReferenceRubriqueServiceCRUD.class);
    }

    private static class PropertiesServiceHolder {
        public static final PropertiesService INSTANCE = new PropertiesServiceImpl();
    }

    private static class TravailleurServiceHolder {
        public static final TravailleurService INSTANCE = new TravailleurServiceImpl(getTravailleurRepository(),
                getPosteTravailRepository(), getPosteTravailService());
    }

    private static class DecompteSalaireServiceHolder {
        public static final DecompteSalaireService INSTANCE = new DecompteSalaireServiceImpl(
                getDecompteSalaireRepository(), getDecompteRepository(), getPosteTravailRepository(),
                getPropertiesService());
    }

    private static class CotisationDecompteServiceHolder {
        public static final CotisationDecompteService INSTANCE = new CotisationDecompteServiceImpl(
                getDecompteSalaireRepository());
    }

    private static class TaxationOfficeServiceHolder {
        public static final TaxationOfficeService INSTANCE = new TaxationOfficeServiceImpl(
                getTaxationOfficeRepository(), getLigneTaxationRepository(), getPassageService());
    }

    private static class PosteTravailServiceHolder {
        public static final PosteTravailService INSTANCE = new PosteTravailServiceImpl(
                VulpeculaRepositoryLocator.getPosteTravailRepository(), getAdhesionCotisationPosteRepository(),
                getCotisationService(), getAffiliationCaisseMaladieRepository());
    }

    private static class EmployeurServiceHolder {
        public static final EmployeurService INSTANCE = new EmployeurServiceImpl(getEmployeurRepository(),
                getPosteTravailService());
    }

    private static class DecompteServiceHolder {
        public static final DecompteService INSTANCE = new DecompteServiceImpl(getDecompteRepository(),
                getHistoriqueDecompteRepository(), getDecompteSalaireRepository(),
                getAdhesionCotisationPosteRepository(), getEmployeurRepository(), getPosteTravailService(),
                getPropertiesService(), getTaxationOfficeService());
    }

    private static class AbsenceJustifieeServiceHolder {
        public static final AbsenceJustifieeService INSTANCE = new AbsenceJustifieeServiceImpl(
                getAbsenceJustifieeRepository(), getPosteTravailService());
    }

    private static class CongePayeServiceHolder {
        public static final CongePayeService INSTANCE = new CongePayeServiceImpl(getCongePayeRepository());
    }

    private static class CompteurServiceHolder {
        public static final CompteurService INSTANCE = new CompteurServiceImpl(getCompteurRepository());
    }

    private static class PassageServiceHolder {
        public static final PassageService INSTANCE = new PassageServiceImpl();
    }

    private static class ServiceMilitaireServiceHolder {
        public static final ServiceMilitaireService INSTANCE = new ServiceMilitaireServiceImpl(
                getServiceMilitaireRepository(), getPosteTravailService());
    }

    private static class CotisationAssociationProfessionnelleServiceHolder {
        public static final CotisationAssociationProfessionnelleService INSTANCE = new CotisationAssociationProfessionnelleServiceImpl(
                getCotisationAssociationProfessionnelleRepository());
    }

    private static class AssociationCotisationServiceHolder {
        public static final AssociationCotisationService INSTANCE = new AssociationCotisationServiceImpl(
                getAssociationCotisationRepository());
    }

    private static class AffiliationCaisseMaladieServiceHolder {
        public static final AffiliationCaisseMaladieService INSTANCE = new AffiliationCaisseMaladieServiceImpl(
                getAffiliationCaisseMaladieRepository());
    }

    private static class AffiliationSyndicatServiceHolder {
        public static final AffiliationSyndicatService INSTANCE = new AffiliationSyndicatServiceImpl();
    }

    private static class ParametreSyndicatServiceHolder {
        public static final ParametreSyndicatService INSTANCE = new ParametreSyndicatServiceImpl(
                getParametreSyndicatRepository());
    }

    private static class ImpotSourceServiceHolder {
        public static final ImpotSourceService INSTANCE = new ImpotSourceServiceImpl();
    }

    private static class ProcessusAFServiceHolder {
        public static final ProcessusAFService INSTANCE = new ProcessusAFServiceImpl();
    }

    private static class TauxImpositionServiceHolder {
        public static final TauxImpositionService INSTANCE = new TauxImpositionServiceImpl(
                getTauxImpositionRepository());
    }

    private static class SuiviCaisseMaladieHolder {
        public static final SuiviCaisseMaladieService INSTANCE = new SuiviCaisseMaladieServiceImpl(
                getSuiviCaisseMaladieRepository());
    }

    private static class UsersHolder {
        public static final UsersService INSTANCE = new UsersServiceImpl();
    }

}