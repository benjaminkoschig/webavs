package ch.globaz.vulpecula.external.repositories.affiliation;

import ch.globaz.vulpecula.external.models.affiliation.Affilie;

/**
 * D�finition des m�thodes pour le repository relatif � la classe {@link Affilie}
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 23 d�c. 2013
 * 
 */
public interface AffilieRepository {
    /**
     * Recherche d'un affili� gr�ce � son identifiant
     * 
     * @param id String repr�sentant l'id du tiers
     * @return {@link Affilie} correspondant � l'id saisi en param�tre
     */
    Affilie findById(String id);
}
