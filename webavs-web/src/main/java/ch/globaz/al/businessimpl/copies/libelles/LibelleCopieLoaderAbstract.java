package ch.globaz.al.businessimpl.copies.libelles;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

/**
 * Classe contenant les m�thodes communes � toutes les classes de r�cup�ration de libell� de copie
 * 
 * @author jts
 * 
 */
public abstract class LibelleCopieLoaderAbstract {

    /**
     * Contexte contenant les informations permettant de r�cup�rer le libell� � utiliser
     */
    protected ContextLibellesCopiesLoader context = null;

    /**
     * D�termine le libell� � utiliser en fonction des informations contenues dans le contexte
     * 
     * @return id tiers � utiliser
     * @throws JadePersistenceException
     *             Exception lev�e si l'id tiers de l'affili� n'a pas pu �tre r�cup�r�
     * @throws JadeApplicationException
     *             Exception lev�e si l'id tiers de l'affili� n'a pas pu �tre r�cup�r�
     */
    public abstract String getLibelle() throws JadePersistenceException, JadeApplicationException;
}
