/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.rentepont;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.PerseusException;
import ch.globaz.perseus.business.exceptions.models.creancier.CreancierException;
import ch.globaz.perseus.business.exceptions.models.rentepont.RentePontException;
import ch.globaz.perseus.business.models.rentepont.CreancierRentePont;
import ch.globaz.perseus.business.models.rentepont.CreancierRentePontSearchModel;
import ch.globaz.perseus.business.models.rentepont.SimpleCreancierRentePont;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.rentepont.CreancierRentePontService;
import ch.globaz.perseus.businessimpl.checkers.qd.FactureChecker;
import ch.globaz.perseus.businessimpl.checkers.rentepont.CreancierRentePontChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * @author DDE
 * 
 */
public class CreancierRentePontServiceImpl extends PerseusAbstractServiceImpl implements CreancierRentePontService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.creancierRentePont.CreancierRentePontService#count(ch.globaz.perseus
     * .business.models .creancierRentePont.CreancierRentePontSearchModel)
     */
    @Override
    public int count(CreancierRentePontSearchModel search) throws RentePontException, JadePersistenceException {
        if (search == null) {
            throw new RentePontException("Unable to count creancierRentePont, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.creancierRentePont.CreancierRentePontService#create(ch.globaz.perseus
     * .business.models .creancierRentePont.CreancierRentePont)
     */
    @Override
    public CreancierRentePont create(CreancierRentePont creancierRentePont) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, PerseusException {
        if (creancierRentePont == null) {
            throw new RentePontException("Unable to create creancierRentePont, the given model is null!");
        }

        try {
            CreancierRentePontChecker.checkForCreate(creancierRentePont);

            checkBVRetCCP(creancierRentePont);

            // Création du creancierRentePont
            SimpleCreancierRentePont simpleCreancierRentePont = creancierRentePont.getSimpleCreancierRentePont();
            simpleCreancierRentePont = PerseusImplServiceLocator.getSimpleCreancierRentePontService().create(
                    simpleCreancierRentePont);
            creancierRentePont.setSimpleCreancierRentePont(simpleCreancierRentePont);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RentePontException("Service not available - " + e.getMessage());
        }

        return creancierRentePont;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.creancierRentePont.CreancierRentePontService#delete(ch.globaz.perseus
     * .business.models .creancierRentePont.CreancierRentePont)
     */
    @Override
    public CreancierRentePont delete(CreancierRentePont creancierRentePont) throws RentePontException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        if (creancierRentePont == null) {
            throw new RentePontException("Unable to delete a creancierRentePont, the model passed is null!");
        }

        try {
            CreancierRentePontChecker.checkForDelete(creancierRentePont);

            // Suppression de la decision
            creancierRentePont.setSimpleCreancierRentePont(PerseusImplServiceLocator
                    .getSimpleCreancierRentePontService().delete(creancierRentePont.getSimpleCreancierRentePont()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RentePontException("Service not available - " + e.getMessage());
        }

        return creancierRentePont;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.creancierRentePont.CreancierRentePontService#read(java.lang.String)
     */
    @Override
    public CreancierRentePont read(String idCreancierRentePont) throws RentePontException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idCreancierRentePont)) {
            throw new RentePontException("Unable to read a creancierRentePont, the id passed is null!");
        }
        // Lecture du loyer
        CreancierRentePont creancierRentePont = new CreancierRentePont();
        creancierRentePont.setId(idCreancierRentePont);

        return (CreancierRentePont) JadePersistenceManager.read(creancierRentePont);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.creancierRentePont.CreancierRentePontService#search(ch.globaz.perseus
     * .business.models .creancierRentePont.CreancierRentePontSearchModel)
     */
    @Override
    public CreancierRentePontSearchModel search(CreancierRentePontSearchModel creancierRentePontSearch)
            throws RentePontException, JadePersistenceException {
        if (creancierRentePontSearch == null) {
            throw new RentePontException("Unable to search a creancierRentePont, the search model passed is null!");
        }

        return (CreancierRentePontSearchModel) JadePersistenceManager.search(creancierRentePontSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.creancierRentePont.CreancierRentePontService#update(ch.globaz.perseus
     * .business.models .creancierRentePont.CreancierRentePont)
     */
    @Override
    public CreancierRentePont update(CreancierRentePont creancierRentePont) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, PerseusException {
        if (creancierRentePont == null) {
            throw new RentePontException("Unable to update creancierRentePont, the given model is null!");
        }

        try {
            CreancierRentePontChecker.checkForUpdate(creancierRentePont);

            checkBVRetCCP(creancierRentePont);

            // Mise à jour de la creance
            SimpleCreancierRentePont simpleCreancierRentePont = creancierRentePont.getSimpleCreancierRentePont();
            simpleCreancierRentePont = PerseusImplServiceLocator.getSimpleCreancierRentePontService().update(
                    simpleCreancierRentePont);
            creancierRentePont.setSimpleCreancierRentePont(simpleCreancierRentePont);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RentePontException("Service not available - " + e.getMessage());
        }

        return creancierRentePont;
    }

    private void checkBVRetCCP(CreancierRentePont creancier) throws JadePersistenceException, CreancierException,
            JadeNoBusinessLogSessionError {

        boolean bvrOk = true;
        boolean ccpOk = true;

        try {
            if (!JadeStringUtil.isEmpty(creancier.getSimpleCreancierRentePont().getReferencePaiement())) {

                String bvrReturn = PerseusServiceLocator.getBVRService().validationNumeroBVR(
                        creancier.getSimpleCreancierRentePont().getReferencePaiement());

                if (bvrReturn.equals("")) {
                    bvrOk = false;
                } else {
                    creancier.getSimpleCreancierRentePont().setReferencePaiement(bvrReturn);
                }

                if (JadeStringUtil.isEmpty(creancier.getSimpleCreancierRentePont().getIdTiers())
                        || JadeStringUtil.isEmpty(creancier.getSimpleCreancierRentePont().getIdDomaineApplicatif())) {
                    ccpOk = false;
                } else if (!PerseusServiceLocator.getBVRService().validationCCP(
                        creancier.getSimpleCreancierRentePont().getIdTiers(),
                        creancier.getSimpleCreancierRentePont().getIdDomaineApplicatif())) {
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

}
