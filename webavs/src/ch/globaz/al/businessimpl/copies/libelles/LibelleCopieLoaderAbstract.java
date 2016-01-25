package ch.globaz.al.businessimpl.copies.libelles;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

/**
 * Classe contenant les méthodes communes à toutes les classes de récupération de libellé de copie
 * 
 * @author jts
 * 
 */
public abstract class LibelleCopieLoaderAbstract {

    /**
     * Contexte contenant les informations permettant de récupérer le libellé à utiliser
     */
    protected ContextLibellesCopiesLoader context = null;

    /**
     * Détermine le libellé à utiliser en fonction des informations contenues dans le contexte
     * 
     * @return id tiers à utiliser
     * @throws JadePersistenceException
     *             Exception levée si l'id tiers de l'affilié n'a pas pu être récupéré
     * @throws JadeApplicationException
     *             Exception levée si l'id tiers de l'affilié n'a pas pu être récupéré
     */
    public abstract String getLibelle() throws JadePersistenceException, JadeApplicationException;
}
