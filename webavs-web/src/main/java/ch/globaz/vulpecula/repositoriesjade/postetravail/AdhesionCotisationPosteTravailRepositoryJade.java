package ch.globaz.vulpecula.repositoriesjade.postetravail;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.NotImplementedException;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.naos.business.model.TauxAssuranceSimpleModel;
import ch.globaz.vulpecula.business.models.postetravail.AdhesionCotisationPosteTravailComplexModel;
import ch.globaz.vulpecula.business.models.postetravail.AdhesionCotisationPosteTravailSearchComplexModel;
import ch.globaz.vulpecula.business.models.postetravail.AdhesionCotisationPosteTravailSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.repositories.postetravail.AdhesionCotisationPosteTravailRepository;
import ch.globaz.vulpecula.external.services.CotisationService;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.converters.AdhesionCotisationPosteTravailConverter;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;

/***
 * Implémentation Jade de {@link AdhesionCotisationPosteTravailRepository}
 *
 * @author Jonas Paratte (JPA) | Créé le 06.02.2014
 *
 */
public class AdhesionCotisationPosteTravailRepositoryJade implements AdhesionCotisationPosteTravailRepository {
    private CotisationService cotisationService = VulpeculaServiceLocator.getCotisationService();

    @Override
    public List<AdhesionCotisationPosteTravail> findByIdPosteTravail(String id) {
        AdhesionCotisationPosteTravailSearchComplexModel searchModel = new AdhesionCotisationPosteTravailSearchComplexModel();
        searchModel.setForIdPosteTravail(id);
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        try {
            JadePersistenceManager.search(searchModel);
        } catch (JadePersistenceException ex) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, ex);
        }
        return fetchSearchModel(searchModel);
    }

    private List<AdhesionCotisationPosteTravail> fetchSearchModel(JadeAbstractSearchModel searchModel) {
        List<AdhesionCotisationPosteTravail> adhesions = new ArrayList<AdhesionCotisationPosteTravail>();
        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            AdhesionCotisationPosteTravailComplexModel adhesionCotisationPosteTravailComplexModel = (AdhesionCotisationPosteTravailComplexModel) model;
            AdhesionCotisationPosteTravail adhesionCotisationPosteTravail = AdhesionCotisationPosteTravailConverter
                    .convertToDomain(adhesionCotisationPosteTravailComplexModel);
            adhesions.add(adhesionCotisationPosteTravail);
        }
        return adhesions;
    }

    @Override
    public List<AdhesionCotisationPosteTravail> findByIdPosteTravail(String id, Date date) {
        List<AdhesionCotisationPosteTravail> adhesionsCotisations = findByIdPosteTravail(id);
        List<AdhesionCotisationPosteTravail> adhesionsActives = new ArrayList<AdhesionCotisationPosteTravail>();

        for (AdhesionCotisationPosteTravail adhesionCotisation : adhesionsCotisations) {
            if (adhesionCotisation.isActifPourLeMois(date)) {
                adhesionsActives.add(adhesionCotisation);
            }
        }

        return adhesionsActives;
    }

    @Override
    public List<AdhesionCotisationPosteTravail> findByIdPosteTravailAndPeriode(String id, Periode periode) {
        List<AdhesionCotisationPosteTravail> adhesionsCotisations = findByIdPosteTravail(id);
        List<AdhesionCotisationPosteTravail> adhesionsActives = new ArrayList<AdhesionCotisationPosteTravail>();

        for (AdhesionCotisationPosteTravail adhesionCotisation : adhesionsCotisations) {
            if (adhesionCotisation.isActifPourLeMoisParPeriode(periode)) {
                adhesionsActives.add(adhesionCotisation);
            }
        }

        return adhesionsActives;
    }

    @Override
    public List<AdhesionCotisationPosteTravail> findByIdPosteTravailForCP(DecompteSalaire decompteSalaire) {
        List<AdhesionCotisationPosteTravail> adhesionsCotisations = findByIdPosteTravail(
                decompteSalaire.getIdPosteTravail());
        List<AdhesionCotisationPosteTravail> adhesionsActives = new ArrayList<AdhesionCotisationPosteTravail>();

        Date dateFinEmployeur = null;
        if (!JadeNumericUtil.isEmptyOrZero(decompteSalaire.getEmployeur().getDateFin())) {
            dateFinEmployeur = new Date(decompteSalaire.getEmployeur().getDateFin());
        }

        for (AdhesionCotisationPosteTravail adhesionCotisation : adhesionsCotisations) {
            if (adhesionCotisation.isActifForCP(dateFinEmployeur, decompteSalaire.getDecompte().getPeriodeFin())) {
                adhesionsActives.add(adhesionCotisation);
            }
        }

        return adhesionsActives;
    }

    @Override
    public List<AdhesionCotisationPosteTravail> findByIdPosteTravail(String id, Annee annee) {
        List<AdhesionCotisationPosteTravail> adhesionsCotisations = findByIdPosteTravail(id);
        List<AdhesionCotisationPosteTravail> adhesionsActives = new ArrayList<AdhesionCotisationPosteTravail>();
        for (AdhesionCotisationPosteTravail adhesionCotisation : adhesionsCotisations) {
            if (adhesionCotisation.isActifIn(annee)) {
                adhesionsActives.add(adhesionCotisation);
            }
        }
        return adhesionsActives;
    }

    @Override
    public List<AdhesionCotisationPosteTravail> findAll() {
        AdhesionCotisationPosteTravailSearchComplexModel searchModel = new AdhesionCotisationPosteTravailSearchComplexModel();
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        try {
            JadePersistenceManager.search(searchModel);
        } catch (JadePersistenceException ex) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, ex);
        }
        return fetchSearchModel(searchModel);
    }

    @Override
    public Taux findTauxCPRTravailleur(PosteTravail posteTravail, Date date) {
        Taux tauxApplicable = Taux.ZERO();
        AdhesionCotisationPosteTravail cotisation = findAdhesionCPRTravailleur(posteTravail, date);
        if (cotisation == null) {
            return tauxApplicable;
        }

        TauxAssuranceSimpleModel tauxAssurance = cotisationService.findTauxForAssurance(cotisation.getIdAssurance(),
                date);
        if (tauxAssurance != null) {
            return new Taux(tauxAssurance.getValeurEmploye());
        } else {
            return tauxApplicable;
        }
    }

    private AdhesionCotisationPosteTravail findAdhesionCPRTravailleur(PosteTravail posteTravail, Date date) {
        List<AdhesionCotisationPosteTravail> cotisations = findByIdPosteTravail(posteTravail.getId(), date);
        for (AdhesionCotisationPosteTravail cotisation : cotisations) {
            if (TypeAssurance.CPR_TRAVAILLEUR.equals(cotisation.getTypeAssurance())) {
                return cotisation;
            }
        }
        return null;
    }

    @Override
    public Taux findTauxCPRTravailleurByAnnee(List<PosteTravail> postesTravail, Annee annee) {
        Taux tauxApplicable = null;
        for (PosteTravail posteTravail : postesTravail) {
            AdhesionCotisationPosteTravail cotisation = findAdhesionCPRTravailleurByAnnee(posteTravail, annee);
            if (cotisation != null) {
                TauxAssuranceSimpleModel tauxAssurance = cotisationService
                        .findTauxForAssurance(cotisation.getIdAssurance(), annee.getLastDayOfYear());
                if (tauxAssurance != null) {
                    return new Taux(tauxAssurance.getValeurEmploye());
                }
            }
        }
        return tauxApplicable;
    }

    @Override
    public AdhesionCotisationPosteTravail findAdhesionCPRTravailleurByAnnee(PosteTravail posteTravail, Annee annee) {
        List<AdhesionCotisationPosteTravail> cotisations = findByIdPosteTravail(posteTravail.getId(), annee);
        for (AdhesionCotisationPosteTravail cotisation : cotisations) {
            if (TypeAssurance.CPR_TRAVAILLEUR.equals(cotisation.getTypeAssurance())) {
                return cotisation;
            }
        }
        return null;
    }

    @Override
    public AdhesionCotisationPosteTravail create(String idPoste,
            AdhesionCotisationPosteTravail adhesionCotisationPosteTravail) {
        try {
            AdhesionCotisationPosteTravailSimpleModel adhesionCotisationPosteTravailSimpleModel = AdhesionCotisationPosteTravailConverter
                    .convertToPersistence(idPoste, adhesionCotisationPosteTravail);
            adhesionCotisationPosteTravailSimpleModel = (AdhesionCotisationPosteTravailSimpleModel) JadePersistenceManager
                    .add(adhesionCotisationPosteTravailSimpleModel);
            adhesionCotisationPosteTravail.setId(adhesionCotisationPosteTravailSimpleModel.getId());
            adhesionCotisationPosteTravail.setSpy(adhesionCotisationPosteTravailSimpleModel.getSpy());
            return adhesionCotisationPosteTravail;
        } catch (JadePersistenceException ex) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, ex);
        }
    }

    @Override
    public AdhesionCotisationPosteTravail findById(String id) {
        AdhesionCotisationPosteTravailSearchComplexModel searchModel = new AdhesionCotisationPosteTravailSearchComplexModel();
        searchModel.setForId(id);
        List<AdhesionCotisationPosteTravailComplexModel> adhesions = RepositoryJade.searchForAndFetch(searchModel);
        if (adhesions.size() > 0) {
            return AdhesionCotisationPosteTravailConverter.convertToDomain(adhesions.get(0));
        }
        return null;
    }

    @Override
    public AdhesionCotisationPosteTravail update(String idPoste,
            AdhesionCotisationPosteTravail adhesionCotisationPosteTravail) {
        try {
            AdhesionCotisationPosteTravailSimpleModel adhesionCotisationPosteTravailSimpleModel = AdhesionCotisationPosteTravailConverter
                    .convertToPersistence(idPoste, adhesionCotisationPosteTravail);
            adhesionCotisationPosteTravailSimpleModel = (AdhesionCotisationPosteTravailSimpleModel) JadePersistenceManager
                    .update(adhesionCotisationPosteTravailSimpleModel);
            adhesionCotisationPosteTravail.setId(adhesionCotisationPosteTravailSimpleModel.getId());
            adhesionCotisationPosteTravail.setSpy(adhesionCotisationPosteTravailSimpleModel.getSpy());
            return adhesionCotisationPosteTravail;
        } catch (JadePersistenceException ex) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, ex);
        }
    }

    @Override
    public void delete(String idPoste, AdhesionCotisationPosteTravail adhesionCotisationPosteTravail) {
        try {
            AdhesionCotisationPosteTravailSimpleModel simpleModel = AdhesionCotisationPosteTravailConverter
                    .convertToPersistence(idPoste, adhesionCotisationPosteTravail);
            JadePersistenceManager.delete(simpleModel);
        } catch (JadePersistenceException ex) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, ex);
        }
    }

    @Override
    public void deleteById(String id) {
        throw new NotImplementedException();
    }

    @Override
    public List<AdhesionCotisationPosteTravail> findByIdEmployeur(String idEmployeur, String idCotisation) {
        AdhesionCotisationPosteTravailSearchComplexModel searchModel = new AdhesionCotisationPosteTravailSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForIdCotisation(idCotisation);
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        try {
            JadePersistenceManager.search(searchModel);
        } catch (JadePersistenceException ex) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, ex);
        }
        return fetchSearchModel(searchModel);
    }

}