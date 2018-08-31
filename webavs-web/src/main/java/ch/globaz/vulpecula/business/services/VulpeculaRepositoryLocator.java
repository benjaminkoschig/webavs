package ch.globaz.vulpecula.business.services;

import ch.globaz.vulpecula.domain.repositories.absencejustifiee.AbsenceJustifieeRepository;
import ch.globaz.vulpecula.domain.repositories.association.AssociationCotisationRepository;
import ch.globaz.vulpecula.domain.repositories.association.AssociationEmployeurRepository;
import ch.globaz.vulpecula.domain.repositories.association.CotisationAssociationProfessionnelleRepository;
import ch.globaz.vulpecula.domain.repositories.association.EnteteFactureAssociationProfessionnelleRepository;
import ch.globaz.vulpecula.domain.repositories.association.FactureAssociationRepository;
import ch.globaz.vulpecula.domain.repositories.association.LigneFactureAssociationProfessionnelleRepository;
import ch.globaz.vulpecula.domain.repositories.caissemaladie.AffiliationCaisseMaladieRepository;
import ch.globaz.vulpecula.domain.repositories.caissemaladie.SuiviCaisseMaladieRepository;
import ch.globaz.vulpecula.domain.repositories.congepaye.CompteurRepository;
import ch.globaz.vulpecula.domain.repositories.congepaye.CongePayeRepository;
import ch.globaz.vulpecula.domain.repositories.controleemployeur.ControleEmployeurRepository;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteRepository;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteSalaireRepository;
import ch.globaz.vulpecula.domain.repositories.decompte.HistoriqueDecompteRepository;
import ch.globaz.vulpecula.domain.repositories.ebusiness.NouveauTravailleurRepository;
import ch.globaz.vulpecula.domain.repositories.ebusiness.SynchronisationRepository;
import ch.globaz.vulpecula.domain.repositories.ebusiness.SynchronisationTravailleurEbuRepository;
import ch.globaz.vulpecula.domain.repositories.is.HistoriqueProcessusAfRepository;
import ch.globaz.vulpecula.domain.repositories.is.TauxImpositionRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.AdhesionCotisationPosteTravailRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.EmployeurRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.OccupationRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.PosteTravailRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.QualificationRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.TravailleurRepository;
import ch.globaz.vulpecula.domain.repositories.registre.ConventionQualificationRepository;
import ch.globaz.vulpecula.domain.repositories.registre.ConventionRepository;
import ch.globaz.vulpecula.domain.repositories.registre.ParametreCotisationAssociationRepository;
import ch.globaz.vulpecula.domain.repositories.servicemilitaire.ServiceMilitaireRepository;
import ch.globaz.vulpecula.domain.repositories.syndicat.AffiliationSyndicatRepository;
import ch.globaz.vulpecula.domain.repositories.syndicat.ParametreSyndicatRepository;
import ch.globaz.vulpecula.domain.repositories.taxationoffice.LigneTaxationRepository;
import ch.globaz.vulpecula.domain.repositories.taxationoffice.TaxationOfficeRepository;
import ch.globaz.vulpecula.external.repositories.affiliation.AdhesionRepository;
import ch.globaz.vulpecula.external.repositories.affiliation.CotisationRepository;
import ch.globaz.vulpecula.external.repositories.tiers.AdministrationRepository;
import ch.globaz.vulpecula.external.repositories.tiers.AdresseRepository;
import ch.globaz.vulpecula.external.repositories.tiers.ContactRepository;
import ch.globaz.vulpecula.external.repositories.tiers.DetailGroupeLocaliteRepository;
import ch.globaz.vulpecula.external.repositories.tiers.PaysRepository;
import ch.globaz.vulpecula.external.repositories.tiers.PersonneEtendueRepository;
import ch.globaz.vulpecula.external.repositories.tiers.TiersRepository;
import ch.globaz.vulpecula.external.repositoriesjade.naos.AdhesionRepositoryJade;
import ch.globaz.vulpecula.external.repositoriesjade.naos.CotisationRepositoryJade;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.AdministrationRepositoryJade;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.AdresseRepositoryJade;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.ContactRepositoryJade;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.DetailGroupeLocaliteRepositoryJade;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.PaysRepositoryJade;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.PersonneEtendueRepositoryJade;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.TiersRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.absencejustifiee.AbsenceJustifieeRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.association.AssociationCotisationRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.association.AssociationEmployeurRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.association.CotisationAssociationProfessionnelleRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.association.EnteteFactureAssociationProfessionnelleRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.association.FactureAssociationRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.association.LigneFactureAssociationProfessionnelleRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.caissemaladie.AffiliationCaisseMaladieRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.caissemaladie.SuiviCaisseMaladieRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.congepaye.CompteurRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.congepaye.CongePayeRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.controleemployeur.ControleEmployeurRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DecompteRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DecompteSalaireRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.HistoriqueDecompteRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.ebusiness.NouveauTravailleurRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.ebusiness.SynchronisationRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.ebusiness.SynchronisationTravailleurEbuRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.is.HistoriqueProcessusAfRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.is.TauxImpositionRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.AdhesionCotisationPosteTravailRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.EmployeurRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.OccupationRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.PosteTravailRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.QualificationRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.TravailleurRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.registre.ConventionQualificationRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.registre.ConventionRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.registre.ParametreCotisationAssociationRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.servicemilitaire.ServiceMilitaireRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.syndicat.AffiliationSyndicatRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.syndicat.ParametreSyndicatRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.taxationoffice.LigneTaxationRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.taxationoffice.TaxationOfficeRepositoryJade;

/***
 * Classe gérant les implémentations en singleton pour les repositories Jade.
 * 
 * @author Arnaud Geiser (AGE) | Créé le 17 déc. 2013
 */
public final class VulpeculaRepositoryLocator {
    /**
     * La classe ne peut pas être instanciée.
     */
    private VulpeculaRepositoryLocator() {
        throw new UnsupportedOperationException();
    }

    /**
     * Retourne une implémentation de la classe {@link TravailleurRepository}
     * 
     * @return Une instance de {@link TravailleurRepositoryJade}
     */
    public static TravailleurRepository getTravailleurRepository() {
        return TravailleurRepositoryHolder.INSTANCE;
    }

    public static NouveauTravailleurRepository getNouveauTravailleurRepository() {
        return NouveauTravailleurRepositoryHolder.INSTANCE;
    }

    /**
     * Retourne une implémentation de la classe {@link DecompteRepository}
     * 
     * @return Une instance de {@link DecompteRepositoryJade}
     */
    public static DecompteRepository getDecompteRepository() {
        return DecompteRepositoryHolder.INSTANCE;
    }

    /**
     * Retourne une implémentation de la classe {@link DecompteSalaireRepository}
     * 
     * @return Une instance de {@link DecompteSalaireRepositoryJade}
     */
    public static DecompteSalaireRepository getDecompteSalaireRepository() {
        return DecompteSalaireRepositoryHolder.INSTANCE;
    }

    /**
     * Retourne une implémentation de la classe {@link EmployeurRepository}
     * 
     * @return Une instance de {@link EmployeurRepositoryJade}
     */
    public static EmployeurRepository getEmployeurRepository() {
        return EmployeurRepositoryHolder.INSTANCE;
    }

    /**
     * Retourne une implémentation de la classe {@link PosteTravailRepository}
     * 
     * @return Une instance de {@link PosteTravailRepositoryJade}
     */
    public static PosteTravailRepository getPosteTravailRepository() {
        return PosteTravailRepositoryHolder.INSTANCE;
    }

    /**
     * Retourne une implémentation de la classe {@link ConventionRepository}
     * 
     * @return Une instance de {@link ConventionRepositoryJade}
     */
    public static ConventionRepository getConventionRepository() {
        return ConventionRepositoryHolder.INSTANCE;
    }

    /**
     * Retourne une implémentation de la classe {@link PaysRepository}
     * 
     * @return Une instance de {@link PaysRepositoryJade}
     */
    public static PaysRepository getPaysRepository() {
        return PaysRepositoryHolder.INSTANCE;
    }

    /**
     * Retourne une implémentation de la classe {@link AdresseRepository}
     * 
     * @return Une instance de {@link AdresseRepositoryJade}
     */
    public static AdresseRepository getAdresseRepository() {
        return AdresseRepositoryHolder.INSTANCE;
    }

    /**
     * Retourne une implémentation de la classe {@link OccupationRepository}
     * 
     * @return Une instance de {@link OccupationRepositoryJade}
     */
    public static OccupationRepository getOccupationRepository() {
        return OccupationRepositoryHolder.INSTANCE;
    }

    /**
     * Retourne une implémentation de la classe {@link TiersRepository}
     * 
     * @return Une instance de {@link TiersRepositoryJade}
     */
    public static TiersRepository getTiersRepository() {
        return TiersRepositoryHolder.INSTANCE;
    }

    /**
     * Retourne une implémentation de la classe {@link QualificationRepository}
     * 
     * @return Une instance de {@link QualificationRepositoryJade}
     */
    public static QualificationRepository getQualificationRepository() {
        return QualificationRepositoryHolder.INSTANCE;
    }

    /**
     * Retourne une implémentation de la classe {@link AdministrationRepository}
     * 
     * @return Une instance de {@link AdministrationRepositoryJade}
     */
    public static AdministrationRepository getAdministrationRepository() {
        return AdministrationRepositoryHolder.INSTANCE;
    }

    /**
     * Retourne une implémentation de la classe {@link HistoriqueDecompteRepository}
     * 
     * @return Une instance de {@link HistoriqueDecompteRepositoryJade}
     */
    public static HistoriqueDecompteRepository getHistoriqueDecompteRepository() {
        return HistoriqueDecompteRepositoryHolder.INSTANCE;
    }

    /**
     * Retourne une implémentation de la classe {@link AdhesionCotisationPosteTravailRepository}
     * 
     * @return Une instance de {@link AdhesionCotisationPosteTravailRepositoryJade}
     */
    public static AdhesionCotisationPosteTravailRepository getAdhesionCotisationPosteRepository() {
        return AdhesionCotisationPosteTravailRepositoryHolder.INSTANCE;
    }

    /**
     * Retourne une implémentation de la classe {@link TaxationOfficeRepository}
     * 
     * @return Une instance de {@link TaxationOfficeRepositoryJade}
     */
    public static TaxationOfficeRepository getTaxationOfficeRepository() {
        return TaxationOfficeRepositoryHolder.INSTANCE;
    }

    /**
     * Retourne une implémentation de la classe {@link LigneTaxationRepository}
     * 
     * @return Une instance de {@link LigneTaxationRepositoryJade}
     */
    public static LigneTaxationRepository getLigneTaxationRepository() {
        return LigneTaxationRepositoryHolder.INSTANCE;
    }

    /**
     * Retourne une implémentation de la classe {@link AdhesionRepository}
     * 
     * @return Une instance de {@link AdhesionRepositoryJade}
     */
    public static AdhesionRepository getAdhesionRepository() {
        return AdhesionRepositoryHolder.INSTANCE;
    }

    /**
     * Retourne une implémentation de la classe {@link CotisationRepository}
     * 
     * @return Une instance de {@link CotisationRepositoryJade}
     */
    public static CotisationRepository getCotisationsRepository() {
        return CotisationRepositoryHolder.INSTANCE;
    }

    public static ConventionQualificationRepository getConventionQualificationRepository() {
        return ConventionQualificationRepositoryHolder.INSTANCE;
    }

    /**
     * @return l'implémentation Jade de la classe {@link AbsenceJustifieeRepository}
     */
    public static AbsenceJustifieeRepository getAbsenceJustifieeRepository() {
        return AbsenceJustifieeRepositoryHolder.INSTANCE;
    }

    /**
     * @return l'implémentation Jade de la classe {@link CongePayeRepository}
     */
    public static CongePayeRepository getCongePayeRepository() {
        return CongePayeRepositoryHolder.INSTANCE;
    }

    /**
     * @return l'implémentation Jade de la classe {@link CompteurRepository}
     */
    public static CompteurRepository getCompteurRepository() {
        return CompteurRepositoryHolder.INSTANCE;
    }

    /**
     * Retourne une implémentation de la classe {@link ServiceMilitaireRepository}
     * 
     * @return Une instance de {@link ServiceMilitaireRepositoryJade}
     */
    public static ServiceMilitaireRepository getServiceMilitaireRepository() {
        return ServiceMilitaireRepositoryHolder.INSTANCE;
    }

    public static EnteteFactureAssociationProfessionnelleRepository getEnteteFactureRepository() {
        return EnteteFactureAssociationProfessionnelleRepositoryHolder.INSTANCE;
    }

    public static LigneFactureAssociationProfessionnelleRepository getLigneFactureRepository() {
        return LigneFactureAssociationProfessionnelleRepositoryHolder.INSTANCE;
    }

    public static AssociationEmployeurRepository getAssociationEmployeurRepository() {
        return AssociationEmployeurRepositoryHolder.INSTANCE;
    }

    public static ContactRepository getContactRepository() {
        return ContactRepositoryHolder.INSTANCE;
    }

    /**
     * @return l'implémentation Jade de la classe {@link DetailGroupeLocaliteRepository}
     */
    public static DetailGroupeLocaliteRepository getDetailGroupeLocaliteRepository() {
        return DetailGroupeLocaliteRepositoryHolder.INSTANCE;
    }

    public static HistoriqueProcessusAfRepository getHistoriqueProcessusAfRepository() {
        return HistoriqueProcessusAfRepositoryHolder.INSTANCE;
    }

    public static ParametreCotisationAssociationRepository getParametreCotisationAssociationRepository() {
        return ParametreCotisationAssociationRepositoryHolder.INSTANCE;
    }

    public static AffiliationCaisseMaladieRepository getAffiliationCaisseMaladieRepository() {
        return AffiliationCaisseMaladieRepositoryHolder.INSTANCE;
    }

    public static PersonneEtendueRepository getPersonneEtendueRepository() {
        return PersonneRepositoryHolder.INSTANCE;
    }

    public static AssociationCotisationRepository getAssociationCotisationRepository() {
        return AssociationCotisationRepositoryHolder.INSTANCE;
    }

    public static AffiliationSyndicatRepository getAffiliationSyndicatRepository() {
        return AffiliationSyndicatRepositoryHolder.INSTANCE;
    }

    public static ParametreSyndicatRepository getParametreSyndicatRepository() {
        return ParametreSyndicatRepositoryHolder.INSTANCE;
    }

    public static CotisationAssociationProfessionnelleRepository getCotisationAssociationProfessionnelleRepository() {
        return CotisationAssociationProfessionnelleRepositoryHolder.INSTANCE;
    }

    public static TauxImpositionRepository getTauxImpositionRepository() {
        return TauxImpositionRepositoryHolder.INSTANCE;
    }

    public static SuiviCaisseMaladieRepository getSuiviCaisseMaladieRepository() {
        return SuiviCaisseMaladieRepositoryHolder.INSTANCE;
    }

    public static ControleEmployeurRepository getControleEmployeurRepository() {
        return ControleEmployeurRepositoryHolder.INSTANCE;
    }

    public static FactureAssociationRepository getFactureAssociationRepository() {
        return FactureAssociationRepositoryHolder.INSTANCE;
    }

    public static SynchronisationRepository getSynchronisationRepository() {
        return SynchronisationEbuRepositoryHolder.INSTANCE;
    }

    public static SynchronisationTravailleurEbuRepository getSynchronisationTravailleurEbuRepository() {
        return SynchronisationTravailleurEbuRepositoryHolder.INSTANCE;
    }

    /*
     * Repository holders - to avoid loading all repositories at once -
     * following the "initialization on demand holder idiom" see
     * http://en.wikipedia.org/wiki/Singleton_pattern#The_solution_of_Bill_Pugh
     */
    private static class LigneFactureAssociationProfessionnelleRepositoryHolder {
        private static final LigneFactureAssociationProfessionnelleRepository INSTANCE = new LigneFactureAssociationProfessionnelleRepositoryJade();
    }

    private static class EnteteFactureAssociationProfessionnelleRepositoryHolder {
        private static final EnteteFactureAssociationProfessionnelleRepository INSTANCE = new EnteteFactureAssociationProfessionnelleRepositoryJade(
                getLigneFactureRepository(), getAdministrationRepository());
    }

    private static class TravailleurRepositoryHolder {
        private static final TravailleurRepository INSTANCE = new TravailleurRepositoryJade();
    }

    private static class NouveauTravailleurRepositoryHolder {
        private static final NouveauTravailleurRepository INSTANCE = new NouveauTravailleurRepositoryJade();
    }

    private static class DecompteRepositoryHolder {
        private static final DecompteRepository INSTANCE = new DecompteRepositoryJade();
    }

    private static class DecompteSalaireRepositoryHolder {
        private static final DecompteSalaireRepository INSTANCE = new DecompteSalaireRepositoryJade();
    }

    private static class EmployeurRepositoryHolder {
        private static final EmployeurRepository INSTANCE = new EmployeurRepositoryJade();
    }

    private static class PosteTravailRepositoryHolder {
        private static final PosteTravailRepository INSTANCE = new PosteTravailRepositoryJade();
    }

    private static class ConventionRepositoryHolder {
        private static final ConventionRepository INSTANCE = new ConventionRepositoryJade();
    }

    private static class PaysRepositoryHolder {
        private static final PaysRepository INSTANCE = new PaysRepositoryJade();
    }

    private static class AdresseRepositoryHolder {
        private static final AdresseRepository INSTANCE = new AdresseRepositoryJade();
    }

    private static class OccupationRepositoryHolder {
        private static final OccupationRepository INSTANCE = new OccupationRepositoryJade();
    }

    private static class TiersRepositoryHolder {
        private static final TiersRepository INSTANCE = new TiersRepositoryJade();
    }

    private static class QualificationRepositoryHolder {
        private static final QualificationRepository INSTANCE = new QualificationRepositoryJade();
    }

    private static class AdministrationRepositoryHolder {
        private static final AdministrationRepository INSTANCE = new AdministrationRepositoryJade();
    }

    private static class HistoriqueDecompteRepositoryHolder {
        private static final HistoriqueDecompteRepository INSTANCE = new HistoriqueDecompteRepositoryJade();
    }

    private static class AdhesionCotisationPosteTravailRepositoryHolder {
        private static final AdhesionCotisationPosteTravailRepository INSTANCE = new AdhesionCotisationPosteTravailRepositoryJade();
    }

    private static class ConventionQualificationRepositoryHolder {
        private static final ConventionQualificationRepository INSTANCE = new ConventionQualificationRepositoryJade();
    }

    private static class TaxationOfficeRepositoryHolder {
        private static final TaxationOfficeRepository INSTANCE = new TaxationOfficeRepositoryJade();
    }

    private static class LigneTaxationRepositoryHolder {
        private static final LigneTaxationRepository INSTANCE = new LigneTaxationRepositoryJade();
    }

    private static class AbsenceJustifieeRepositoryHolder {
        private static final AbsenceJustifieeRepository INSTANCE = new AbsenceJustifieeRepositoryJade();
    }

    private static class CongePayeRepositoryHolder {
        private static final CongePayeRepository INSTANCE = new CongePayeRepositoryJade();
    }

    private static class CompteurRepositoryHolder {
        private static final CompteurRepository INSTANCE = new CompteurRepositoryJade();
    }

    private static class ServiceMilitaireRepositoryHolder {
        private static final ServiceMilitaireRepository INSTANCE = new ServiceMilitaireRepositoryJade();
    }

    private static class ParametreCotisationAssociationRepositoryHolder {
        private static final ParametreCotisationAssociationRepository INSTANCE = new ParametreCotisationAssociationRepositoryJade();
    }

    private static class AssociationCotisationRepositoryHolder {
        private static final AssociationCotisationRepository INSTANCE = new AssociationCotisationRepositoryJade();
    }

    private static class AffiliationCaisseMaladieRepositoryHolder {
        private static final AffiliationCaisseMaladieRepository INSTANCE = new AffiliationCaisseMaladieRepositoryJade();
    }

    private static class AffiliationSyndicatRepositoryHolder {
        private static final AffiliationSyndicatRepository INSTANCE = new AffiliationSyndicatRepositoryJade();
    }

    private static class ParametreSyndicatRepositoryHolder {
        private static final ParametreSyndicatRepository INSTANCE = new ParametreSyndicatRepositoryJade();
    }

    private static class CotisationAssociationProfessionnelleRepositoryHolder {
        private static final CotisationAssociationProfessionnelleRepository INSTANCE = new CotisationAssociationProfessionnelleRepositoryJade();
    }

    private static class TauxImpositionRepositoryHolder {
        private static final TauxImpositionRepository INSTANCE = new TauxImpositionRepositoryJade();
    }

    private static class HistoriqueProcessusAfRepositoryHolder {
        private static final HistoriqueProcessusAfRepository INSTANCE = new HistoriqueProcessusAfRepositoryJade();
    }

    private static class ControleEmployeurRepositoryHolder {
        private static final ControleEmployeurRepository INSTANCE = new ControleEmployeurRepositoryJade();
    }

    private static class FactureAssociationRepositoryHolder {
        private static final FactureAssociationRepository INSTANCE = new FactureAssociationRepositoryJade(
                getLigneFactureRepository(), getEnteteFactureRepository());
    }

    /* TODO: Repositories externes devant être à terme, externalisés */
    private static class AdhesionRepositoryHolder {
        private static final AdhesionRepository INSTANCE = new AdhesionRepositoryJade();
    }

    private static class CotisationRepositoryHolder {
        private static final CotisationRepository INSTANCE = new CotisationRepositoryJade();
    }

    private static class PersonneRepositoryHolder {
        private static final PersonneEtendueRepository INSTANCE = new PersonneEtendueRepositoryJade();
    }

    private static class SuiviCaisseMaladieRepositoryHolder {
        private static final SuiviCaisseMaladieRepository INSTANCE = new SuiviCaisseMaladieRepositoryJade();
    }

    private static class DetailGroupeLocaliteRepositoryHolder {
        private static final DetailGroupeLocaliteRepository INSTANCE = new DetailGroupeLocaliteRepositoryJade();
    }

    private static class AssociationEmployeurRepositoryHolder {
        private static final AssociationEmployeurRepository INSTANCE = new AssociationEmployeurRepositoryJade();
    }

    private static class ContactRepositoryHolder {
        private static final ContactRepository INSTANCE = new ContactRepositoryJade();
    }

    private static class SynchronisationEbuRepositoryHolder {
        private static final SynchronisationRepositoryJade INSTANCE = new SynchronisationRepositoryJade();
    }

    private static class SynchronisationTravailleurEbuRepositoryHolder {
        private static final SynchronisationTravailleurEbuRepositoryJade INSTANCE = new SynchronisationTravailleurEbuRepositoryJade();
    }
}
