package ch.globaz.al.businessimpl.copies.adresse;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

/**
 * Classe contenant les méthodes commune à toutes les classes de récupération d'id de tiers adresse
 * 
 * @author jts
 * 
 */
public abstract class IdTiersAdresseLoaderAbstract {

    /**
     * Contexte contenant les informations permettant de récupérer l'id tiers à utiliser pour l'adresse
     */
    protected ContextIdTiersAdresseCopiesLoader context = null;

    /**
     * Détermine l'id tiers pour lequel l'adresse doit être récupéré en fonction des informations contenues dans le
     * contexte
     * 
     * @return id tiers à utiliser
     * @throws JadePersistenceException
     *             Exception levée si l'id tiers de l'affilié n'a pas pu être récupéré
     * @throws JadeApplicationException
     *             Exception levée si l'id tiers de l'affilié n'a pas pu être récupéré
     */
    public abstract String getIdTiersAdresse() throws JadePersistenceException, JadeApplicationException;
}
