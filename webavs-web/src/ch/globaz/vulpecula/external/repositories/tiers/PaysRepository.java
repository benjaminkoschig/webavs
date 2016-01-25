/**
 * 
 */
package ch.globaz.vulpecula.external.repositories.tiers;

import ch.globaz.vulpecula.external.models.pyxis.Pays;

/**
 * D�finition des m�thodes pour le repository relatif � la classe {@link Pays}
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 23 d�c. 2013
 * 
 */
public interface PaysRepository {
    /**
     * Recherche d'un pays gr�ce � son identifiant
     * 
     * @param id String repr�sentant l'id du pays
     * @return {@link Pays} correspondant � l'id saisi en param�tre
     */
    Pays findById(String id);
}
