/**
 * 
 */
package ch.globaz.pegasus.business.services.models.calcul;

import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCCSearch;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesFraisGarde;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesFraisGardeSearch;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * @author ECO
 * 
 */
public interface CalculDonneesFraisGardeService extends JadeApplicationService {

    public int count(CalculDonneesFraisGardeSearch search) throws CalculException, JadePersistenceException;

    public CalculDonneesFraisGarde read(String idCalculDonneesDroit) throws JadePersistenceException, CalculException;

    public CalculDonneesFraisGardeSearch search(CalculDonneesFraisGardeSearch calculDonneesEnfantsSearch)
            throws JadePersistenceException, CalculException;

}
