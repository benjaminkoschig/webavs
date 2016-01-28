package ch.globaz.common.business.services;

import ch.globaz.common.domaine.EntiteDeDomaine;

/**
 * Contrat comprenant les rudiments d'un DAO (load, add, update, remove) pour un objet de domaine métier donné
 */
public interface DaoService<T extends EntiteDeDomaine> {

    /**
     * <p>
     * Ajoute l'objet de domaine passé en paramètre à la persistance, et retourne l'objet (qui vient d'être enregistré
     * en base) avec son identifiant à jour
     * </p>
     * <p>
     * Lancera une {@link IllegalArgumentException} si l'objet possède déjà un identifiant ou s'il est null
     * </p>
     * 
     * @param objetDeDomaine
     *            l'objet que l'on veut sauver en base de données
     * @return l'objet sauvé en base, avec son nouvel identifiant à jour
     */
    public T add(T objetDeDomaine);

    /**
     * <p>
     * Charge l'objet passé en paramètre (selon son identifiant unique) depuis la base de donnée et retourne cet objet
     * avec ses valeurs à jour selon les données de la base
     * </p>
     * <p>
     * Lancera une {@link IllegalArgumentException} si l'objet ne possède pas d'ID ou s'il est null
     * </p>
     * 
     * @param objetDeDomaine
     *            l'objet dont on veut le chargement depuis la base de donnée
     * @return l'objet chargé, avec ses champs à jour
     */
    public T load(T objetDeDomaine);

    /**
     * <p>
     * Supprime l'entité et ses dépendances de la base de donnée
     * </p>
     * <p>
     * Lancera une {@link IllegalArgumentException} si l'objet ne possède pas d'ID ou s'il est null
     * </p>
     * <p>
     * Retournera <code>true</code> si le traitement s'est passé sans encombre, ou <code>false</code> s'il y a eu un
     * problème avec la persistance (entité non trouvé, problème technique, etc...)
     * </p>
     * 
     * @param objetDeDomaine
     *            l'objet qu'on aimerait supprimer
     * @return <code>true</code> si le traitement s'est passé sans encombre, ou <code>false</code> s'il y a eu un
     *         problème avec la persistance (entité non trouvé, problème technique, etc...)
     */
    public boolean remove(T objetDeDomaine);

    /**
     * <p>
     * Met à jour l'objet de domaine passé en paramètre, puis le retourne
     * </p>
     * <p>
     * Lancera une {@link IllegalArgumentException} si l'objet passé en paramètre ne possède pas d'ID ou s'il est null
     * </p>
     * 
     * @param objetDeDomaine
     *            l'objet que l'on veut modifier
     * @return l'objet après modification et sauvegarde en base de données
     */
    public T update(T objetDeDomaine);
}
