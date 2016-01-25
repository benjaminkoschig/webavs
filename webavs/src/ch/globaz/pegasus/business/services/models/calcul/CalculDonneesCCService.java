/**
 * 
 */
package ch.globaz.pegasus.business.services.models.calcul;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCCSearch;

/**
 * @author ECO
 * 
 */
public interface CalculDonneesCCService extends JadeApplicationService {

    public int count(CalculDonneesCCSearch search) throws CalculException, JadePersistenceException;

    public CalculDonneesCC read(String idCalculDonneesDroit) throws JadePersistenceException, CalculException;

    public CalculDonneesCCSearch search(CalculDonneesCCSearch calculDonneesEnfantsSearch)
            throws JadePersistenceException, CalculException;

}
