package ch.globaz.vulpecula.businessimpl.services.travailleur;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.sun.star.uno.RuntimeException;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazBusinessException;
import ch.globaz.vulpecula.business.models.ebusiness.SynchronisationTravailleurEbuSearchSimpleModel;
import ch.globaz.vulpecula.business.models.ebusiness.SynchronisationTravailleurEbuSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.postetravail.PosteTravailService;
import ch.globaz.vulpecula.business.services.travailleur.TravailleurService;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.ebusiness.SynchronisationTravailleur;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.repositories.ebusiness.SynchronisationTravailleurEbuRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.PosteTravailRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.TravailleurRepository;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.prestation.interfaces.tiers.PRTiersHelper;

/**
 * Implémentation Jade du service <code>TravailleurService</code>.
 *
 */
public class TravailleurServiceImpl implements TravailleurService {

    private TravailleurRepository travailleurRepository;
    private PosteTravailRepository posteTravailRepository;
    private PosteTravailService posteTravailService;
    private SynchronisationTravailleurEbuRepository synchronisationTravailleurEbuRepository;

    public TravailleurServiceImpl(final TravailleurRepository travailleurRepository,
            final PosteTravailRepository posteTravailRepository, PosteTravailService posteTravailService,
            SynchronisationTravailleurEbuRepository synchroEbu) {
        this.travailleurRepository = travailleurRepository;
        this.posteTravailRepository = posteTravailRepository;
        this.posteTravailService = posteTravailService;
        synchronisationTravailleurEbuRepository = synchroEbu;
    }

    @Override
    public Travailleur create(final Travailleur travailleur) throws GlobazBusinessException {
        if (JadeStringUtil.isEmpty(travailleur.getIdTiers())) {
            throw new GlobazBusinessException(ExceptionMessage.TRAVAILLEUR_CREATION_IMPOSSIBLE_SANS_TIERS);
        } else if (travailleurAlreadyExists(travailleur)) {
            throw new GlobazBusinessException(ExceptionMessage.TRAVAILLEUR_DEJA_EXISTANT);
        }
        return travailleurRepository.create(travailleur);
    }

    @Override
    public List<Travailleur> findActifs(Annee annee) {
        Set<Travailleur> travailleurs = new HashSet<Travailleur>();
        List<PosteTravail> postes = posteTravailService.findPostesActifs(annee);
        for (PosteTravail poste : postes) {
            travailleurs.add(poste.getTravailleur());
        }
        return new ArrayList<Travailleur>(travailleurs);
    }

    @Override
    public void delete(final Travailleur travailleur) throws GlobazBusinessException {
        if (hasPosteTravail(travailleur)) {
            throw new GlobazBusinessException(
                    ExceptionMessage.TRAVAILLEUR_SUPPRESSION_IMPOSSIBLE_CAR_LIE_A_POSTE_TRAVAIL);
        }
        travailleurRepository.delete(travailleur);
    }

    @Override
    public boolean hasPosteTravail(final Travailleur travailleur) {
        return posteTravailRepository.findByIdTravailleur(travailleur.getId()).size() > 0;
    }

    private boolean travailleurAlreadyExists(final Travailleur travailleur) {
        Travailleur retrievedTravailleur = travailleurRepository.findByIdTiers(travailleur.getIdTiers());
        return retrievedTravailleur != null;
    }

    @Override
    public boolean isRentier(String idTravailleur, Date date) throws Exception {
        Travailleur travailleur = travailleurRepository.findById(idTravailleur);
        return isRentier(travailleur, date);
    }

    @Override
    public boolean isRentier(Travailleur travailleur, Date date) throws Exception {
        if (JAUtil.isDateEmpty(travailleur.getDateNaissance())) {
            return false; // on part du principe que si la date n'est pas renseignée, ce n'est pas un retraité.
        }
        if (JadeStringUtil.isEmpty(travailleur.getSexe())) {
            throw new IllegalArgumentException("Erreur : Le sexe du tiers n'est pas renseigné. idTiers = "
                    + travailleur.getIdTiers() + " nss = " + travailleur.getNumAvsActuel());
        }

        Date dateRetraite = giveDateRentier(travailleur.getDateNaissance(), travailleur.getSexe());
        return (date.afterOrEquals(dateRetraite));

    }

    @Override
    public Date giveDateRentier(String idTravailleur) throws Exception {
        Travailleur travailleur = travailleurRepository.findById(idTravailleur);
        return giveDateRentier(travailleur.getDateNaissance(), travailleur.getSexe());
    }

    int getAgeAVS(String sexe, String dateNaissance) {
        return PRTiersHelper.getAgeAvs(sexe, dateNaissance);
    }

    @Override
    public Date giveDateRentier(Date dateNaissance, String sexe) {
        return giveDateRentier(dateNaissance.getSwissValue(), sexe);
    }

    @Override
    public Date giveDateRentier(String dateNaissance, String sexe) {
        try {
            if (JAUtil.isDateEmpty(dateNaissance)) {
                return null; // on part du principe que si la date n'est pas renseignée, ce n'est pas un retraité.
            }

            int anneeNaissance = JACalendar.getYear(dateNaissance);

            if (JadeStringUtil.isEmpty(sexe)) {
                throw new IllegalArgumentException("Erreur : Le sexe du tiers n'est pas renseigné !");
            }

            int ageAvs = anneeNaissance + getAgeAVS(sexe, dateNaissance);

            // on construit la date d'age avs
            Date date = new Date("01." + JACalendar.getMonth(dateNaissance) + "." + ageAvs);
            date = date.addMonth(1);
            return date;
        } catch (JAException ex) {
            throw new RuntimeException("Exception inconnu", ex);
        }
    }

    @Override
    public void notifierSynchronisationEbu(String idTravailleur, String correlationId) throws JadePersistenceException {
        boolean isEbu = false;
        List<PosteTravail> listePostes = VulpeculaRepositoryLocator.getPosteTravailRepository()
                .findByIdTravailleurWithDependencies(idTravailleur);
        for (PosteTravail posteTravail : listePostes) {
            Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository()
                    .findById(posteTravail.getEmployeur().getId());
            if (employeur.isEBusiness()) {
                isEbu = true;
                continue;
            }
        }
        if (isEbu) {
            addSynchroTravailleur(idTravailleur, Date.now().getSwissValue(), correlationId, "", "");
        }
    }

    @Override
    public void notifierSynchroPosteTravailEbu(PosteTravail posteTravail) throws JadePersistenceException {

        Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository()
                .findById(posteTravail.getEmployeur().getId());

        if (employeur.isEBusiness()) {
            String correlationId = posteTravail.getCorrelationId().isEmpty()
                    ? posteTravail.getTravailleur().getCorrelationId()
                    : posteTravail.getCorrelationId();
            addSynchroTravailleur(posteTravail.getIdTravailleur(), Date.now().getSwissValue(), correlationId,
                    posteTravail.getPosteCorrelationId(), posteTravail.getIdPortail());
        }
    }

    @Override
    public void notifierSynchroPosteTravailEbu(String idPosteTravail, String posteTravail_correlationId,
            Travailleur travailleur) throws JadePersistenceException {
        PosteTravail poste = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(idPosteTravail);

        Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository()
                .findById(poste.getEmployeur().getId());

        if (employeur.isEBusiness()) {
            addSynchroTravailleur(travailleur.getId(), Date.now().getSwissValue(), travailleur.getCorrelationId(),
                    poste.getPosteCorrelationId(), "");
        }
    }

    @Override
    public void ackSyncTravailleurs(List<String> idsTableSynchro) {
        for (String id : idsTableSynchro) {
            SynchronisationTravailleur synchro = synchronisationTravailleurEbuRepository.findById(id);
            synchro.setDateSynchronisation(Date.now());
            synchronisationTravailleurEbuRepository.update(synchro);

            if (synchro.getTravailleur() != null) {
                List<SynchronisationTravailleur> listeSync = VulpeculaRepositoryLocator
                        .getSynchronisationTravailleurEbuRepository()
                        .findByIdTravailleur(synchro.getTravailleur().getId());
                for (SynchronisationTravailleur sync : listeSync) {
                    if (sync.getDateSynchronisation() == null) {
                        sync.setDateSynchronisation(new Date());
                        VulpeculaRepositoryLocator.getSynchronisationTravailleurEbuRepository().update(sync);
                    }
                }
            }

            if (!JadeStringUtil.isBlankOrZero(synchro.getIdAnnonce())) {
                List<SynchronisationTravailleur> listeSyncRefused = VulpeculaRepositoryLocator
                        .getSynchronisationTravailleurEbuRepository().findByIdAnnnonce(synchro.getIdAnnonce());
                for (SynchronisationTravailleur sync : listeSyncRefused) {
                    if (sync.getDateSynchronisation() == null) {
                        sync.setDateSynchronisation(new Date());
                        VulpeculaRepositoryLocator.getSynchronisationTravailleurEbuRepository().update(sync);
                    }
                }
            }
        }
    }

    @Override
    public Travailleur findByNomPrenomDateNaissanceEmployeur(String nom, String prenom, String dateNaissance,
            String idEmployeur) {
        List<Travailleur> listeTravailleur = travailleurRepository.findByNomPrenomDateNaissanceV2(nom, prenom,
                dateNaissance);
        if (listeTravailleur != null && !listeTravailleur.isEmpty()) {
            for (Travailleur travailleur : listeTravailleur) {
                if (posteTravailRepository.findByTravailleurEtEmployeur(travailleur.getId(), idEmployeur) != null) {
                    return travailleur;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public Travailleur findByNomPrenomDateNaissance(String nom, String prenom, String dateNaissance) {
        List<Travailleur> listeTravailleur = travailleurRepository.findByNomPrenomDateNaissanceV2(nom, prenom,
                dateNaissance);
        if (listeTravailleur != null && !listeTravailleur.isEmpty()) {
            return listeTravailleur.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Travailleur findById(String idTravailleur) {
        return travailleurRepository.findById(idTravailleur);
    }

    @Override
    public void notifierSynchroAnnonce(String idAnnonce, String correlationId, String posteCorrelationId)
            throws JadePersistenceException {

        addSynchroTravailleur("", Date.now().getSwissValue(), correlationId, posteCorrelationId, idAnnonce);
    }

    @Override
    public void notifierSynchronisationEbu(String idTravailleur, String correlationId, String posteCorrelationId,
            String annonceId) throws JadePersistenceException {
        addSynchroTravailleur(idTravailleur, Date.now().getSwissValue(), correlationId, posteCorrelationId, annonceId);

    }

    private void addSynchroTravailleur(String idTravailleur, String dateAjout, String correlationId,
            String posteCorrelationId, String idAnnonceTravailleurEbu) throws JadePersistenceException {
        if (checkIsValid(idTravailleur, dateAjout, correlationId, posteCorrelationId, idAnnonceTravailleurEbu)) {
            SynchronisationTravailleurEbuSimpleModel synchronisation = new SynchronisationTravailleurEbuSimpleModel();
            synchronisation.setIdTravailleur(idTravailleur);
            synchronisation.setDateAjout(dateAjout);
            synchronisation.setCorrelationId(correlationId);
            synchronisation.setPosteCorrelationId(posteCorrelationId);
            synchronisation.setIdAnnonce(idAnnonceTravailleurEbu);
            JadePersistenceManager.add(synchronisation);
        }
    }

    private boolean checkIsValid(String idTravailleur, String dateAjout, String correlationId,
            String posteCorrelationId, String idAnnonceTravailleurEbu) {
        SynchronisationTravailleurEbuSearchSimpleModel search = new SynchronisationTravailleurEbuSearchSimpleModel();
        search.setForDateSynchronisationIsEmpty(true);
        search.setForIdTravailleur(idTravailleur);
        try {
            JadePersistenceManager.search(search);
            // En cas de refus le idTravailleur est = 0
            if ((JadeStringUtil.isBlankOrZero(idTravailleur) || search.getSize() == 0)
                    && !JadeStringUtil.isEmpty(correlationId) && !JadeStringUtil.isEmpty(posteCorrelationId)) {
                search.setForIdTravailleur("");
                search.setForCorrelationId(correlationId);
                search.setForPosteCorrelationId(posteCorrelationId);
                search.setForDateSynchronisationIsEmpty(true);
                JadePersistenceManager.search(search);
            }

        } catch (JadePersistenceException e) {
            JadeLogger.debug(this, e.toString());
        }
        if (search.getSize() == 0) {
            return true;
        } else {
            for (JadeAbstractModel abstractModel : search.getSearchResults()) {
                SynchronisationTravailleurEbuSimpleModel simpleModel = (SynchronisationTravailleurEbuSimpleModel) abstractModel;
                if (!JadeStringUtil.isBlankOrZero(idTravailleur)
                        && JadeStringUtil.isBlankOrZero(simpleModel.getIdTravailleur())) {
                    simpleModel.setIdTravailleur(idTravailleur);
                }
                if (!JadeStringUtil.isBlankOrZero(correlationId)
                        && JadeStringUtil.isBlankOrZero(simpleModel.getCorrelationId())) {
                    simpleModel.setCorrelationId(correlationId);
                }
                if (!JadeStringUtil.isBlankOrZero(posteCorrelationId)
                        && JadeStringUtil.isBlankOrZero(simpleModel.getPosteCorrelationId())) {
                    simpleModel.setPosteCorrelationId(posteCorrelationId);
                }
                try {
                    JadePersistenceManager.update(simpleModel);
                } catch (JadePersistenceException e) {
                    JadeLogger.debug(this, e.toString());
                }
            }
            return false;
        }
    }
}
