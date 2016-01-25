package ch.globaz.vulpecula.external.repositories.tiers;

import ch.globaz.vulpecula.external.models.pyxis.Tiers;

/**
 * D�finition des m�thodes pour le repository relatif � la classe {@link Tiers}
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 23 d�c. 2013
 * 
 */
public interface TiersRepository {
    /**
     * Recherche d'un tiers gr�ce � son identifiant
     * 
     * @param id String repr�sentant l'id du tiers
     * @return {@link Tiers} correspondant � l'id saisi en param�tre
     */
    Tiers findById(String id);
}
