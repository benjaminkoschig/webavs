package ch.globaz.common.business.services;

import ch.globaz.common.domaine.EntiteDeDomaine;

/**
 * Contrat comprenant les rudiments d'un DAO (load, add, update, remove) pour un objet de domaine m�tier donn�
 */
public interface DaoService<T extends EntiteDeDomaine> {

    /**
     * <p>
     * Ajoute l'objet de domaine pass� en param�tre � la persistance, et retourne l'objet (qui vient d'�tre enregistr�
     * en base) avec son identifiant � jour
     * </p>
     * <p>
     * Lancera une {@link IllegalArgumentException} si l'objet poss�de d�j� un identifiant ou s'il est null
     * </p>
     * 
     * @param objetDeDomaine
     *            l'objet que l'on veut sauver en base de donn�es
     * @return l'objet sauv� en base, avec son nouvel identifiant � jour
     */
    public T add(T objetDeDomaine);

    /**
     * <p>
     * Charge l'objet pass� en param�tre (selon son identifiant unique) depuis la base de donn�e et retourne cet objet
     * avec ses valeurs � jour selon les donn�es de la base
     * </p>
     * <p>
     * Lancera une {@link IllegalArgumentException} si l'objet ne poss�de pas d'ID ou s'il est null
     * </p>
     * 
     * @param objetDeDomaine
     *            l'objet dont on veut le chargement depuis la base de donn�e
     * @return l'objet charg�, avec ses champs � jour
     */
    public T load(T objetDeDomaine);

    /**
     * <p>
     * Supprime l'entit� et ses d�pendances de la base de donn�e
     * </p>
     * <p>
     * Lancera une {@link IllegalArgumentException} si l'objet ne poss�de pas d'ID ou s'il est null
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
     */
    public boolean remove(T objetDeDomaine);

    /**
     * <p>
     * Met � jour l'objet de domaine pass� en param�tre, puis le retourne
     * </p>
     * <p>
     * Lancera une {@link IllegalArgumentException} si l'objet pass� en param�tre ne poss�de pas d'ID ou s'il est null
     * </p>
     * 
     * @param objetDeDomaine
     *            l'objet que l'on veut modifier
     * @return l'objet apr�s modification et sauvegarde en base de donn�es
     */
    public T update(T objetDeDomaine);
}
