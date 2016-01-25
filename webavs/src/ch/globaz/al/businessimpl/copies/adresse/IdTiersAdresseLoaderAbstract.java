package ch.globaz.al.businessimpl.copies.adresse;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

/**
 * Classe contenant les m�thodes commune � toutes les classes de r�cup�ration d'id de tiers adresse
 * 
 * @author jts
 * 
 */
public abstract class IdTiersAdresseLoaderAbstract {

    /**
     * Contexte contenant les informations permettant de r�cup�rer l'id tiers � utiliser pour l'adresse
     */
    protected ContextIdTiersAdresseCopiesLoader context = null;

    /**
     * D�termine l'id tiers pour lequel l'adresse doit �tre r�cup�r� en fonction des informations contenues dans le
     * contexte
     * 
     * @return id tiers � utiliser
     * @throws JadePersistenceException
     *             Exception lev�e si l'id tiers de l'affili� n'a pas pu �tre r�cup�r�
     * @throws JadeApplicationException
     *             Exception lev�e si l'id tiers de l'affili� n'a pas pu �tre r�cup�r�
     */
    public abstract String getIdTiersAdresse() throws JadePersistenceException, JadeApplicationException;
}
