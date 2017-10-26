package ch.globaz.common.domaine.repository;

import java.util.Collection;
import java.util.List;
import ch.globaz.exceptions.GlobazTechnicalException;

/**
 * Interface que chaque interface sp�cialis�e de Repository devra impl�menter.
 * Elle d�finit les m�thodes de bases qui seront d�pendantes � chaque
 * repository.
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
     * Recherche le model par son id
     * 
     * @param id
     * @return le simpleModel
     */
    <S> S findModelById(final String id);

    /**
     * Recherche tout
     * 
     */
    <S> S findAll();

    /**
     * Recherche de l'entit� gr�ce � l'id pass� en param�tre.
     * 
     * @param ids
     *            � rechercher
     * @return Objets correspondant aux id
     * @throws GlobazTechnicalException
     */
    List<T> findByIds(Collection<String> ids);

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
