/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.calcul;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreRenteException;
import ch.globaz.pegasus.business.models.calcul.AutreRenteCalcul;
import ch.globaz.pegasus.business.models.calcul.AutreRenteCalculSearch;
import ch.globaz.pegasus.business.services.models.calcul.AutreRenteCalculService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * @author ECO
 * 
 */
public class AutreRenteCalculServiceImpl extends PegasusAbstractServiceImpl implements AutreRenteCalculService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.calcul.AutreRenteCalculService
     * #count(ch.globaz.pegasus.business.models.calcul.AutreRenteCalculSearch)
     */
    @Override
    public int count(AutreRenteCalculSearch search) throws JadePersistenceException, AutreRenteException {
        if (search == null) {
            throw new AutreRenteException("Unable to count autreRente, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.calcul.AutreRenteCalculService #read(java.lang.String)
     */
    @Override
    public AutreRenteCalcul read(String idAutreRenteCalcul) throws JadePersistenceException, AutreRenteException {
        if (JadeStringUtil.isEmpty(idAutreRenteCalcul)) {
            throw new AutreRenteException("Unable to read autreRente, the id passed is null!");
        }
        AutreRenteCalcul autreRenteCalcul = new AutreRenteCalcul();
        autreRenteCalcul.setId(idAutreRenteCalcul);
        return (AutreRenteCalcul) JadePersistenceManager.read(autreRenteCalcul);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.calcul.AutreRenteCalculService
     * #search(ch.globaz.pegasus.business.models.calcul.AutreRenteCalculSearch)
     */
    @Override
    public AutreRenteCalculSearch search(AutreRenteCalculSearch search) throws JadePersistenceException,
            AutreRenteException {
        if (search == null) {
            throw new AutreRenteException("Unable to search autreRente, the search model passed is null!");
        }
        return (AutreRenteCalculSearch) JadePersistenceManager.search(search);
    }

}
