package ch.globaz.vulpecula.businessimpl.services.travailleur;

import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazBusinessException;
import ch.globaz.vulpecula.business.services.postetravail.PosteTravailService;
import ch.globaz.vulpecula.business.services.travailleur.TravailleurService;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.repositories.postetravail.PosteTravailRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.TravailleurRepository;

/**
 * Implémentation Jade du service <code>TravailleurService</code>.
 * 
 */
public class TravailleurServiceImpl implements TravailleurService {

    private TravailleurRepository travailleurRepository;
    private PosteTravailRepository posteTravailRepository;
    private PosteTravailService posteTravailService;

    public TravailleurServiceImpl(final TravailleurRepository travailleurRepository,
            final PosteTravailRepository posteTravailRepository, PosteTravailService posteTravailService) {
        this.travailleurRepository = travailleurRepository;
        this.posteTravailRepository = posteTravailRepository;
        this.posteTravailService = posteTravailService;
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

        int anneeNaissance = JACalendar.getYear(travailleur.getDateNaissance());

        if (JadeStringUtil.isEmpty(travailleur.getSexe())) {
            throw new IllegalArgumentException("Erreur : Le sexe du tiers n'est pas renseigné. idTiers = "
                    + travailleur.getIdTiers() + " nss = " + travailleur.getNumAvsActuel());
        }

        int ageAvs = anneeNaissance + PRTiersHelper.getAgeAvs(travailleur.getSexe(), travailleur.getDateNaissance());

        // on construit la date d'age avs
        JADate dateAVS = new JADate(JACalendar.getDay(travailleur.getDateNaissance()), JACalendar.getMonth(travailleur
                .getDateNaissance()), ageAvs);

        return (BSessionUtil.getSessionFromThreadContext().getApplication().getCalendar()
                .compare(new JADate(date.getSwissValue()), dateAVS) != JACalendar.COMPARE_FIRSTLOWER);

    }

    @Override
    public Date giveDateRentier(String dateNaissance, String sexe) throws JAException {
        if (JAUtil.isDateEmpty(dateNaissance)) {
            return null; // on part du principe que si la date n'est pas renseignée, ce n'est pas un retraité.
        }

        int anneeNaissance = JACalendar.getYear(dateNaissance);

        if (JadeStringUtil.isEmpty(sexe)) {
            throw new IllegalArgumentException("Erreur : Le sexe du tiers n'est pas renseigné !");
        }

        int ageAvs = anneeNaissance + PRTiersHelper.getAgeAvs(sexe, dateNaissance);

        // on construit la date d'age avs
        Date date = new Date("01." + JACalendar.getMonth(dateNaissance) + "." + ageAvs);
        date = date.addMonth(1);

        return date;
    }
}
