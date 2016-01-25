package ch.globaz.al.businessimpl.generation.prestations;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

/**
 * Interface devant être implémenté par les classes de génération de prestations
 * 
 * @author jts
 * 
 */
public interface GenPrestation {

    /**
     * Exécute la génération des prestations
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public void execute() throws JadeApplicationException, JadePersistenceException;
}
