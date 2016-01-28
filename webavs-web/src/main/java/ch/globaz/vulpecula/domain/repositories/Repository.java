package ch.globaz.vulpecula.domain.repositories;

import ch.globaz.exceptions.GlobazTechnicalException;

/**
 * Interface que chaque interface sp�cialis�e de Repository devra impl�menter.
 * Elle d�finit les m�thodes de bases qui seront d�pendantes � chaque
 * repository.
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 22 janv. 2014
 * 
 * @param <T>
 *            Classe � persister, mettre � jour, supprimer
 */
public interface Repository<T> {
    /**
     * Persistance de l'entit�.
     * 
     * @param entity
     *            Entit� � persister
     * @return objet pass� en param�tre en mettant � jour son id et son spy
     * @throws GlobazTechnicalException
     */
    T create(T entity);

    /**
     * Recherche de l'entit� gr�ce � l'id pass� en param�tre.
     * 
     * @param id
     *            � rechercher
     * @return Objet correspondant � l'id
     * @throws GlobazTechnicalException
     */
    T findById(String id);

    /**
     * Mise � jour de l'entit�.
     * 
     * @param entity
     *            Entit� � mettre � jour
     * @return Objet pass� en param�tre en mettant � jour son id et son spy
     * @throws GlobazTechnicalException
     */
    T update(T entity);

    /**
     * Suppression de l'entit�.
     * 
     * @param entity
     *            Entit� � supprimer
     * @throws GlobazTechnicalException
     */
    void delete(T entity);

    /**
     * Suppression de l'entit� par son id.
     */
    void deleteById(String idEntity);
}
