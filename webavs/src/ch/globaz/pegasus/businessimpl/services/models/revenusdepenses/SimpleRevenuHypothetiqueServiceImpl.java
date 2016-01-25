package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuHypothetiqueException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuHypothetique;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuHypothetiqueSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.SimpleRevenuHypothetiqueService;
import ch.globaz.pegasus.businessimpl.checkers.revenusdepenses.SimpleRevenuHypothetiqueChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleRevenuHypothetiqueServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleRevenuHypothetiqueService {
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleRevenuHypothetiqueService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleRevenuHypothetique)
     */
    @Override
    public SimpleRevenuHypothetique create(SimpleRevenuHypothetique simpleRevenuHypothetique)
            throws JadePersistenceException, RevenuHypothetiqueException {
        if (simpleRevenuHypothetique == null) {
            throw new RevenuHypothetiqueException(
                    "Unable to create simpleRevenuHypothetique, the model passed is null!");
        }
        SimpleRevenuHypothetiqueChecker.checkForCreate(simpleRevenuHypothetique);
        return (SimpleRevenuHypothetique) JadePersistenceManager.add(simpleRevenuHypothetique);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleRevenuHypothetiqueService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleRevenuHypothetique)
     */
    @Override
    public SimpleRevenuHypothetique delete(SimpleRevenuHypothetique simpleRevenuHypothetique)
            throws RevenuHypothetiqueException, JadePersistenceException {
        if (simpleRevenuHypothetique == null) {
            throw new RevenuHypothetiqueException(
                    "Unable to delete simpleRevenuHypothetique, the model passed is null!");
        }
        if (simpleRevenuHypothetique.isNew()) {
            throw new RevenuHypothetiqueException("Unable to delete simpleRevenuHypothetique, the model passed is new!");
        }
        SimpleRevenuHypothetiqueChecker.checkForDelete(simpleRevenuHypothetique);
        return (SimpleRevenuHypothetique) JadePersistenceManager.delete(simpleRevenuHypothetique);
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleRevenuHypothetiqueSearch search = new SimpleRevenuHypothetiqueSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleRevenuHypothetiqueService#read(java.lang.String)
     */
    @Override
    public SimpleRevenuHypothetique read(String idRevenuHypothetique) throws JadePersistenceException,
            RevenuHypothetiqueException {
        if (JadeStringUtil.isEmpty(idRevenuHypothetique)) {
            throw new RevenuHypothetiqueException(
                    "Unable to read simpleRevenuHypothetique, the id passed is not defined!");
        }
        SimpleRevenuHypothetique simpleRevenuHypothetique = new SimpleRevenuHypothetique();
        simpleRevenuHypothetique.setId(idRevenuHypothetique);
        return (SimpleRevenuHypothetique) JadePersistenceManager.read(simpleRevenuHypothetique);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleRevenuHypothetiqueService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleRevenuHypothetique)
     */
    @Override
    public SimpleRevenuHypothetique update(SimpleRevenuHypothetique simpleRevenuHypothetique)
            throws JadePersistenceException, RevenuHypothetiqueException {
        if (simpleRevenuHypothetique == null) {
            throw new RevenuHypothetiqueException(
                    "Unable to update simpleRevenuHypothetique, the model passed is null!");
        }
        if (simpleRevenuHypothetique.isNew()) {
            throw new RevenuHypothetiqueException("Unable to update simpleRevenuHypothetique, the model passed is new!");
        }
        SimpleRevenuHypothetiqueChecker.checkForUpdate(simpleRevenuHypothetique);
        return (SimpleRevenuHypothetique) JadePersistenceManager.update(simpleRevenuHypothetique);
    }

}
