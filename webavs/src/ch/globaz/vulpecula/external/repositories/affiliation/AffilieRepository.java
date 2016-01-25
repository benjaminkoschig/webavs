package ch.globaz.vulpecula.external.repositories.affiliation;

import ch.globaz.vulpecula.external.models.affiliation.Affilie;

/**
 * Définition des méthodes pour le repository relatif à la classe {@link Affilie}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 23 déc. 2013
 * 
 */
public interface AffilieRepository {
    /**
     * Recherche d'un affilié grâce à son identifiant
     * 
     * @param id String représentant l'id du tiers
     * @return {@link Affilie} correspondant à l'id saisi en paramètre
     */
    Affilie findById(String id);
}
