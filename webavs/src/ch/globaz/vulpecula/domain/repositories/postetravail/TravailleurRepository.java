package ch.globaz.vulpecula.domain.repositories.postetravail;

import java.util.List;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.repositories.Repository;

/**
 * D�finition des m�thodes pour le repository relatives � la classe {@link Travailleur}
 * 
 * @author sel
 */
public interface TravailleurRepository extends Repository<Travailleur> {
    /**
     * Retourne l'enesemble des travailleurs existant en base de donn�es
     * 
     * @return List<{@link Travailleur}> contenant tous les travailleurs
     */
    List<Travailleur> findAll();

    /**
     * Retourne un travailleur par rapport � son id tiers.
     * 
     * @param idTiers
     *            idTiers � rechercher
     * @return Un travailleur ou null si non pr�sent en base de donn�es.
     */
    Travailleur findByIdTiers(String idTiers);
}
