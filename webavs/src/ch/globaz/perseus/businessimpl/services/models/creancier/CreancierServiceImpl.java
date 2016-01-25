/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.creancier;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.PerseusException;
import ch.globaz.perseus.business.exceptions.models.creancier.CreancierException;
import ch.globaz.perseus.business.models.creancier.Creancier;
import ch.globaz.perseus.business.models.creancier.CreancierSearchModel;
import ch.globaz.perseus.business.models.creancier.SimpleCreancier;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.creancier.CreancierService;
import ch.globaz.perseus.businessimpl.checkers.creancier.CreancierChecker;
import ch.globaz.perseus.businessimpl.checkers.qd.FactureChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * @author MBO
 * 
 */
public class CreancierServiceImpl extends PerseusAbstractServiceImpl implements CreancierService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.creancier.CreancierService#count(ch.globaz.perseus.business.models
     * .creancier.CreancierSearchModel)
     */
    @Override
    public int count(CreancierSearchModel search) throws CreancierException, JadePersistenceException {
        if (search == null) {
            throw new CreancierException("Unable to count creancier, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.creancier.CreancierService#create(ch.globaz.perseus.business.models
     * .creancier.Creancier)
     */
    @Override
    public Creancier create(Creancier creancier) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, PerseusException {
        if (creancier == null) {
            throw new CreancierException("Unable to create creancier, the given model is null!");
        }

        try {
            CreancierChecker.checkForCreate(creancier);

            checkBVRetCCP(creancier);
            // Création du creancier
            SimpleCreancier simpleCreancier = creancier.getSimpleCreancier();
            simpleCreancier = PerseusImplServiceLocator.getSimpleCreancierService().create(simpleCreancier);
            creancier.setSimpleCreancier(simpleCreancier);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CreancierException("Service not available - " + e.getMessage());
        }

        return creancier;
    }

    private void checkBVRetCCP(Creancier creancier) throws JadePersistenceException, CreancierException,
            JadeNoBusinessLogSessionError {

        boolean bvrOk = true;
        boolean ccpOk = true;

        try {
            if (!JadeStringUtil.isEmpty(creancier.getSimpleCreancier().getReferencePaiement())) {

                String bvrReturn = PerseusServiceLocator.getBVRService().validationNumeroBVR(
                        creancier.getSimpleCreancier().getReferencePaiement());

                if (bvrReturn.equals("")) {
                    bvrOk = false;
                } else {
                    creancier.getSimpleCreancier().setReferencePaiement(bvrReturn);
                }

                if (JadeStringUtil.isEmpty(creancier.getSimpleCreancier().getIdTiers())
                        || JadeStringUtil.isEmpty(creancier.getSimpleCreancier().getIdDomaineApplicatif())) {
                    ccpOk = false;
                } else if (!PerseusServiceLocator.getBVRService().validationCCP(
                        creancier.getSimpleCreancier().getIdTiers(),
                        creancier.getSimpleCreancier().getIdDomaineApplicatif())) {
                    ccpOk = false;
                }
            }
        } catch (JadeApplicationException e) {
            throw new CreancierException("Service not available - " + e.getMessage());
        } finally {
            if (!bvrOk && !ccpOk) {
                JadeThread
                        .logError(FactureChecker.class.getName(), "perseus.facture.numero.reference.et.ccp.incorrect");
            } else if (!bvrOk) {
                JadeThread.logError(FactureChecker.class.getName(), "perseus.facture.numero.reference.incorrect");
            } else if (!ccpOk) {
                JadeThread.logError(FactureChecker.class.getName(), "perseus.facture.ccp.incorrect");
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.creancier.CreancierService#delete(ch.globaz.perseus.business.models
     * .creancier.Creancier)
     */
    @Override
    public Creancier delete(Creancier creancier) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        if (creancier == null) {
            throw new CreancierException("Unable to delete a creancier, the model passed is null!");
        }

        try {
            CreancierChecker.checkForDelete(creancier);

            // Suppression de la decision
            creancier.setSimpleCreancier(PerseusImplServiceLocator.getSimpleCreancierService().delete(
                    creancier.getSimpleCreancier()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CreancierException("Service not available - " + e.getMessage());
        }

        return creancier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.creancier.CreancierService#read(java.lang.String)
     */
    @Override
    public Creancier read(String idCreancier) throws CreancierException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idCreancier)) {
            throw new CreancierException("Unable to read a creancier, the id passed is null!");
        }
        // Lecture du loyer
        Creancier creancier = new Creancier();
        creancier.setId(idCreancier);

        return (Creancier) JadePersistenceManager.read(creancier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.creancier.CreancierService#search(ch.globaz.perseus.business.models
     * .creancier.CreancierSearchModel)
     */
    @Override
    public CreancierSearchModel search(CreancierSearchModel creancierSearch) throws CreancierException,
            JadePersistenceException {
        if (creancierSearch == null) {
            throw new CreancierException("Unable to search a creancier, the search model passed is null!");
        }

        return (CreancierSearchModel) JadePersistenceManager.search(creancierSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.creancier.CreancierService#update(ch.globaz.perseus.business.models
     * .creancier.Creancier)
     */
    @Override
    public Creancier update(Creancier creancier) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, PerseusException {
        if (creancier == null) {
            throw new CreancierException("Unable to update creancier, the given model is null!");
        }

        try {
            CreancierChecker.checkForUpdate(creancier);

            checkBVRetCCP(creancier);

            // Mise à jour de la creance
            SimpleCreancier simpleCreancier = creancier.getSimpleCreancier();
            simpleCreancier = PerseusImplServiceLocator.getSimpleCreancierService().update(simpleCreancier);
            creancier.setSimpleCreancier(simpleCreancier);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CreancierException("Service not available - " + e.getMessage());
        }

        return creancier;
    }

}
