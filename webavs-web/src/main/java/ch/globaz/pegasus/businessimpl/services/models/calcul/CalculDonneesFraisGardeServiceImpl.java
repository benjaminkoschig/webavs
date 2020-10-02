/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.calcul;

import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCCSearch;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesFraisGarde;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesFraisGardeSearch;
import ch.globaz.pegasus.business.services.models.calcul.CalculDonneesCCService;
import ch.globaz.pegasus.business.services.models.calcul.CalculDonneesFraisGardeService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;

/**
 * @author ECO
 * 
 */
public class CalculDonneesFraisGardeServiceImpl extends PegasusAbstractServiceImpl implements CalculDonneesFraisGardeService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.calcul.CalculDonneesEnfantsService #
     * count(ch.globaz.pegasus.business.models.calcul.CalculDonneesEnfantsSearch )
     */
    @Override
    public int count(CalculDonneesFraisGardeSearch search) throws CalculException, JadePersistenceException {
        if (search == null) {
            throw new CalculException("Unable to count calculDonneesFraisGarde, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.calcul.CalculDonneesEnfantsService #read(java.lang.String)
     */
    @Override
    public CalculDonneesFraisGarde read(String idCalculDonneesDroit) throws JadePersistenceException, CalculException {
        if (JadeStringUtil.isEmpty(idCalculDonneesDroit)) {
            throw new CalculException("Unable to read calculDonneesFraisGarde, the id passed is null!");
        }
        CalculDonneesFraisGarde calculDonneesFraisGarde = new CalculDonneesFraisGarde();
        calculDonneesFraisGarde.setId(idCalculDonneesDroit);
        return (CalculDonneesFraisGarde) JadePersistenceManager.read(calculDonneesFraisGarde);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.calcul.CalculDonneesEnfantsService #
     * search(ch.globaz.pegasus.business.models.calcul.CalculDonneesEnfantsSearch )
     */
    @Override
    public CalculDonneesFraisGardeSearch search(CalculDonneesFraisGardeSearch calculDonneesFraisGarde)
            throws JadePersistenceException, CalculException {
        if (calculDonneesFraisGarde == null) {
            throw new CalculException("Unable to read calculDonneesFraisGarde, the id passed is null!");
        }
        return (CalculDonneesFraisGardeSearch) JadePersistenceManager.search(calculDonneesFraisGarde);
    }

}
