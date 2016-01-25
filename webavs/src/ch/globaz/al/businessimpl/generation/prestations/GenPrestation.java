package ch.globaz.al.businessimpl.generation.prestations;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

/**
 * Interface devant �tre impl�ment� par les classes de g�n�ration de prestations
 * 
 * @author jts
 * 
 */
public interface GenPrestation {

    /**
     * Ex�cute la g�n�ration des prestations
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public void execute() throws JadeApplicationException, JadePersistenceException;
}
