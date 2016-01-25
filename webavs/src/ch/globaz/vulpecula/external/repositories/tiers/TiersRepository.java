package ch.globaz.vulpecula.external.repositories.tiers;

import ch.globaz.vulpecula.external.models.pyxis.Tiers;

/**
 * Définition des méthodes pour le repository relatif à la classe {@link Tiers}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 23 déc. 2013
 * 
 */
public interface TiersRepository {
    /**
     * Recherche d'un tiers grâce à son identifiant
     * 
     * @param id String représentant l'id du tiers
     * @return {@link Tiers} correspondant à l'id saisi en paramètre
     */
    Tiers findById(String id);
}
