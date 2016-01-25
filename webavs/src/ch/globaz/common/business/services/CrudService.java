package ch.globaz.common.business.services;

import ch.globaz.common.domaine.EntiteDeDomaine;

/**
 * Contrat pour un CRUD (create, read, update, delete) pour une entit� de domaine m�tier donn�
 */
public interface CrudService<T extends EntiteDeDomaine> {

    /**
     * <p>
     * Ajoute l'objet de domaine pass� en param�tre � la persistance, et retourne l'objet (qui vient d'�tre enregistr�
     * en base) avec son identifiant � jour
     * </p>
     * 
     * @param objetDeDomaine
     *            l'objet que l'on veut sauver en base de donn�es
     * @return l'objet sauv� en base, avec son nouvel identifiant � jour
     * @throws {@link IllegalArgumentException} si l'objet poss�de d�j� un identifiant ou s'il est null
     */
    public T create(T objetDeDomaine);

    /**
     * <p>
     * Supprime l'entit� et ses d�pendances de la base de donn�e
     * </p>
     * <p>
     * Retournera <code>true</code> si le traitement s'est pass� sans encombre, ou <code>false</code> s'il y a eu un
     * probl�me avec la persistance (entit� non trouv�, probl�me technique, etc...)
     * </p>
     * 
     * @param id
     *            l'id de l'entit� qu'on aimerait supprimer
     * @return <code>true</code> si le traitement s'est pass� sans encombre, ou <code>false</code> s'il y a eu un
     *         probl�me avec la persistance (entit� non trouv�, probl�me technique, etc...)
     * @throws {@link IllegalArgumentException} si aucune entit� avec cet ID n'a �t� trouv�, ou si l'ID est null
     */
    public boolean delete(Long id);

    /**
     * <p>
     * Supprime l'entit� et ses d�pendances de la base de donn�e
     * </p>
     * <p>
     * Retournera <code>true</code> si le traitement s'est pass� sans encombre, ou <code>false</code> s'il y a eu un
     * probl�me avec la persistance (entit� non trouv�, probl�me technique, etc...)
     * </p>
     * 
     * @param objetDeDomaine
     *            l'objet qu'on aimerait supprimer
     * @return <code>true</code> si le traitement s'est pass� sans encombre, ou <code>false</code> s'il y a eu un
     *         probl�me avec la persistance (entit� non trouv�, probl�me technique, etc...)
     * @throws {@link IllegalArgumentException} si l'objet ne poss�de pas d'ID ou s'il est null
     */
    public boolean delete(T objetDeDomaine);

    /**
     * <p>
     * Charge l'objet pass� en param�tre (selon son identifiant unique) depuis la base de donn�e et retourne cet objet
     * avec ses valeurs � jour selon les donn�es de la base
     * </p>
     * 
     * @param id
     *            l'ID de l'objet dont on veut le chargement depuis la base de donn�e
     * @return l'objet charg�, avec ses champs � jour
     * @throws {@link IllegalArgumentException} si l'ID est null ou si aucune entit� n'a �t� trouv�e avec cet ID
     */
    public T read(Long id);

    /**
     * <p>
     * Charge l'objet pass� en param�tre (selon son identifiant unique) depuis la base de donn�e et retourne cet objet
     * avec ses valeurs � jour selon les donn�es de la base
     * </p>
     * 
     * @param objetDeDomaine
     *            l'objet dont on veut le chargement depuis la base de donn�e
     * @return l'objet charg�, avec ses champs � jour
     * @throws {@link IllegalArgumentException} si l'objet ne poss�de pas d'ID ou s'il est null
     */
    public T read(T objetDeDomaine);

    /**
     * <p>
     * Met � jour l'objet de domaine pass� en param�tre, puis le retourne
     * </p>
     * 
     * @param objetDeDomaine
     *            l'objet que l'on veut modifier
     * @return l'objet apr�s modification et sauvegarde en base de donn�es
     * @throws {@link IllegalArgumentException} si l'objet pass� en param�tre ne poss�de pas d'ID ou s'il est null
     */
    public T update(T objetDeDomaine);
}
