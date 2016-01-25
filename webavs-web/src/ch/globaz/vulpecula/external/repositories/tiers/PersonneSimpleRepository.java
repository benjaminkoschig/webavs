package ch.globaz.vulpecula.external.repositories.tiers;

import ch.globaz.vulpecula.external.models.pyxis.PersonneSimple;

/**
 * D�finition des m�thodes pour le repository relatif � la classe {@link PersonneSimple}
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 23 d�c. 2013
 * 
 */
public interface PersonneSimpleRepository {
    /**
     * Recherche d'une personneSimple gr�ce � son identifiant
     * 
     * @param id String repr�sentant l'id du tiers
     * @return {@link PersonneSimple} correspondant � l'id saisi en param�tre
     */
    PersonneSimple findById(String id);
}
