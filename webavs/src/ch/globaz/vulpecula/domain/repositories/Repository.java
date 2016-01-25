package ch.globaz.vulpecula.domain.repositories;

import ch.globaz.exceptions.GlobazTechnicalException;

/**
 * Interface que chaque interface spécialisée de Repository devra implémenter.
 * Elle définit les méthodes de bases qui seront dépendantes à chaque
 * repository.
 * 
 * @author Arnaud Geiser (AGE) | Créé le 22 janv. 2014
 * 
 * @param <T>
 *            Classe à persister, mettre à jour, supprimer
 */
public interface Repository<T> {
    /**
     * Persistance de l'entité.
     * 
     * @param entity
     *            Entité à persister
     * @return objet passé en paramètre en mettant à jour son id et son spy
     * @throws GlobazTechnicalException
     */
    T create(T entity);

    /**
     * Recherche de l'entité grâce à l'id passé en paramètre.
     * 
     * @param id
     *            à rechercher
     * @return Objet correspondant à l'id
     * @throws GlobazTechnicalException
     */
    T findById(String id);

    /**
     * Mise à jour de l'entité.
     * 
     * @param entity
     *            Entité à mettre à jour
     * @return Objet passé en paramètre en mettant à jour son id et son spy
     * @throws GlobazTechnicalException
     */
    T update(T entity);

    /**
     * Suppression de l'entité.
     * 
     * @param entity
     *            Entité à supprimer
     * @throws GlobazTechnicalException
     */
    void delete(T entity);

    /**
     * Suppression de l'entité par son id.
     */
    void deleteById(String idEntity);
}
