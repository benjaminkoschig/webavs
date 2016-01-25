package ch.globaz.vulpecula.domain.repositories.postetravail;

import java.util.List;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.repositories.Repository;

/**
 * Définition des méthodes pour le repository relatives à la classe {@link Travailleur}
 * 
 * @author sel
 */
public interface TravailleurRepository extends Repository<Travailleur> {
    /**
     * Retourne l'enesemble des travailleurs existant en base de données
     * 
     * @return List<{@link Travailleur}> contenant tous les travailleurs
     */
    List<Travailleur> findAll();

    /**
     * Retourne un travailleur par rapport à son id tiers.
     * 
     * @param idTiers
     *            idTiers à rechercher
     * @return Un travailleur ou null si non présent en base de données.
     */
    Travailleur findByIdTiers(String idTiers);
}
