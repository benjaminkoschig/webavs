/**
 * 
 */
package ch.globaz.vulpecula.external.repositories.tiers;

import ch.globaz.vulpecula.external.models.pyxis.Pays;

/**
 * Définition des méthodes pour le repository relatif à la classe {@link Pays}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 23 déc. 2013
 * 
 */
public interface PaysRepository {
    /**
     * Recherche d'un pays grâce à son identifiant
     * 
     * @param id String représentant l'id du pays
     * @return {@link Pays} correspondant à l'id saisi en paramètre
     */
    Pays findById(String id);
}
