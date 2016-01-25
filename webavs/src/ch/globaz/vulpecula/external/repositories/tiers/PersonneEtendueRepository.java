package ch.globaz.vulpecula.external.repositories.tiers;

import ch.globaz.vulpecula.external.models.pyxis.PersonneEtendue;

/**
 * D�finition des m�thodes pour le repository relatif � la classe {@link PersonneEtendue}
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 23 d�c. 2013
 * 
 */
public interface PersonneEtendueRepository {
    /**
     * Recherche d'un pays gr�ce � son identifiant
     * 
     * @param id String repr�sentant l'id du tiers
     * @return {@link PersonneEtendue} correspondant � l'id saisi en param�tre
     */
    PersonneEtendue findById(String id);
}
