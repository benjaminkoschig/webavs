package ch.globaz.ci.businessimpl.service;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.ci.business.models.CompteIndividuelSearchModel;
import ch.globaz.ci.business.service.CompteIndividuelService;
import ch.globaz.ci.exception.CIException;

/**
 * 
 * classe d'implémentation des services de CompteIndividuelModel
 * 
 * @author GMO
 * 
 */
public class CompteIndividuelServiceImpl implements CompteIndividuelService {

    /**
     * (non-Javadoc)
     * 
     * @see ch.globaz.ci.business.service.CompteIndividuelService#search(ch.globaz.ci.business.models.CompteIndividuelSearchModel)
     */
    @Override
    public CompteIndividuelSearchModel search(CompteIndividuelSearchModel ciSearchModel)
            throws JadeApplicationException, JadePersistenceException {
        if (ciSearchModel == null) {
            throw new CIException("unable to search compteIndividuelModel - the model passed is null");
        }
        return (CompteIndividuelSearchModel) JadePersistenceManager.search(ciSearchModel);
    }

}
