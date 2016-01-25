/**
 * 
 */
package ch.globaz.pegasus.business.services.models.calcul;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreRenteException;
import ch.globaz.pegasus.business.models.calcul.AutreRenteCalcul;
import ch.globaz.pegasus.business.models.calcul.AutreRenteCalculSearch;

/**
 * @author ECO
 * 
 */
public interface AutreRenteCalculService extends JadeApplicationService {
    public int count(AutreRenteCalculSearch search) throws JadePersistenceException, AutreRenteException;

    public AutreRenteCalcul read(String idAutreRenteCalcul) throws JadePersistenceException, AutreRenteException;

    public AutreRenteCalculSearch search(AutreRenteCalculSearch search) throws JadePersistenceException,
            AutreRenteException;

}
