package ch.globaz.vulpecula.external.repositories.tiers;

import ch.globaz.vulpecula.external.models.pyxis.PersonneSimple;

/**
 * Définition des méthodes pour le repository relatif à la classe {@link PersonneSimple}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 23 déc. 2013
 * 
 */
public interface PersonneSimpleRepository {
    /**
     * Recherche d'une personneSimple grâce à son identifiant
     * 
     * @param id String représentant l'id du tiers
     * @return {@link PersonneSimple} correspondant à l'id saisi en paramètre
     */
    PersonneSimple findById(String id);
}
