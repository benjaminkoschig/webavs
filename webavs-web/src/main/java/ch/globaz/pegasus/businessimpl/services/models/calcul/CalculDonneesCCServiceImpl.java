/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.calcul;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCCSearch;
import ch.globaz.pegasus.business.services.models.calcul.CalculDonneesCCService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * @author ECO
 * 
 */
public class CalculDonneesCCServiceImpl extends PegasusAbstractServiceImpl implements CalculDonneesCCService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.calcul.CalculDonneesEnfantsService #
     * count(ch.globaz.pegasus.business.models.calcul.CalculDonneesEnfantsSearch )
     */
    @Override
    public int count(CalculDonneesCCSearch search) throws CalculException, JadePersistenceException {
        if (search == null) {
            throw new CalculException("Unable to count calculDonneesEnfants, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.calcul.CalculDonneesEnfantsService #read(java.lang.String)
     */
    @Override
    public CalculDonneesCC read(String idCalculDonneesDroit) throws JadePersistenceException, CalculException {
        if (JadeStringUtil.isEmpty(idCalculDonneesDroit)) {
            throw new CalculException("Unable to read calculDonneesEnfants, the id passed is null!");
        }
        CalculDonneesCC calculDonneesEnfants = new CalculDonneesCC();
        calculDonneesEnfants.setId(idCalculDonneesDroit);
        return (CalculDonneesCC) JadePersistenceManager.read(calculDonneesEnfants);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.calcul.CalculDonneesEnfantsService #
     * search(ch.globaz.pegasus.business.models.calcul.CalculDonneesEnfantsSearch )
     */
    @Override
    public CalculDonneesCCSearch search(CalculDonneesCCSearch calculDonneesEnfantsSearch)
            throws JadePersistenceException, CalculException {
        if (calculDonneesEnfantsSearch == null) {
            throw new CalculException("Unable to read calculDonneesEnfants, the id passed is null!");
        }
        return (CalculDonneesCCSearch) JadePersistenceManager.search(calculDonneesEnfantsSearch);
    }

}
