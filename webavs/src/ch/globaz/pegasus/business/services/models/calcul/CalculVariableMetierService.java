/**
 * 
 */
package ch.globaz.pegasus.business.services.models.calcul;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculVariableMetier;
import ch.globaz.pegasus.business.models.calcul.CalculVariableMetierSearch;

/**
 * @author ECO
 * 
 */
public interface CalculVariableMetierService extends JadeApplicationService {

    public int count(CalculVariableMetierSearch search) throws CalculException, JadePersistenceException;

    public CalculVariableMetier read(String idCalculDonneesDroit) throws JadePersistenceException, CalculException;

    public CalculVariableMetierSearch search(CalculVariableMetierSearch calculDonneesDroitSearch)
            throws JadePersistenceException, CalculException;

}
