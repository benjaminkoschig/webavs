package ch.globaz.vulpecula.business.services.travailleur;

import globaz.jade.exception.JadePersistenceException;
import java.util.List;
import ch.globaz.exceptions.GlobazBusinessException;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;

/**
 * Permet différentes opérations métier liées à un travailleur.
 * 
 */
public interface TravailleurService {
    Travailleur create(Travailleur travailleur) throws GlobazBusinessException;

    List<Travailleur> findActifs(Annee annee);

    void delete(Travailleur travailleur) throws GlobazBusinessException;

    boolean hasPosteTravail(Travailleur travailleur);

    boolean isRentier(Travailleur travailleur, Date date) throws Exception;

    boolean isRentier(String idTravailleur, Date date) throws Exception;

    public Date giveDateRentier(String idTravailleur) throws Exception;

    /**
     * Retourne le 1er jour du mois suivant l'âge de retraite du travailleur
     * 
     * @param dateNaissance
     * @return Date 1er jour de retraite du travailleur
     */
    Date giveDateRentier(String dateNaissance, String sexe);

    Date giveDateRentier(Date dateNaissance, String sexe);

    void notifierSynchronisationEbu(String idTravailleur, String correlationId) throws JadePersistenceException;

    void notifierSynchronisationEbu(String idTravailleur, String correlationId, String posteCorrelationId,
            String annonceId) throws JadePersistenceException;

    void notifierSynchroPosteTravailEbu(PosteTravail posteTravail) throws JadePersistenceException;

    void notifierSynchroPosteTravailEbu(String idPosteTravail, String correlationId, Travailleur travailleur)
            throws JadePersistenceException;

    void notifierSynchroAnnonce(String idAnnonce, String correlationId, String posteCorrelationId)
            throws JadePersistenceException;

    void ackSyncTravailleurs(List<String> idsTableSynchro);

    Travailleur findByNomPrenomDateNaissanceEmployeur(String nom, String prenom, String dateNaissance,
            String idEmployeur);

    Travailleur findByNomPrenomDateNaissance(String nom, String prenom, String dateNaissance);

    Travailleur findById(String idTravailleur);
}
