package ch.globaz.vulpecula.external.repositories.tiers;

import ch.globaz.vulpecula.external.models.pyxis.PersonneEtendue;

/**
 * Définition des méthodes pour le repository relatif à la classe {@link PersonneEtendue}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 23 déc. 2013
 * 
 */
public interface PersonneEtendueRepository {
    /**
     * Recherche d'un pays grâce à son identifiant
     * 
     * @param id String représentant l'id du tiers
     * @return {@link PersonneEtendue} correspondant à l'id saisi en paramètre
     */
    PersonneEtendue findById(String id);
}
