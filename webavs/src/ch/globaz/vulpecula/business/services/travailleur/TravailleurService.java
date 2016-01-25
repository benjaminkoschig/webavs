package ch.globaz.vulpecula.business.services.travailleur;

import globaz.globall.util.JAException;
import java.util.List;
import ch.globaz.exceptions.GlobazBusinessException;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
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

    /**
     * Retourne le 1er jour du mois suivant l'âge de retraite du travailleur
     * 
     * @param dateNaissance
     * @return Date 1er jour de retraite du travailleur
     * @throws JAException
     */
    Date giveDateRentier(String dateNaissance, String sexe) throws JAException;
}
