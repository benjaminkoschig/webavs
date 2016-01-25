package ch.globaz.al.external;

import globaz.globall.context.BJadeThreadActivator;
import globaz.globall.context.exception.BJadeMultipleJdbcConnectionInSameThreadException;
import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.naos.db.adhesion.AFAdhesionViewBean;
import java.sql.SQLException;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Service externe permettant la création d'annonce RAFam si un tiers utilisé comme allocataire ou enfant dans les AF
 * est modifié
 * 
 * @author jts
 * 
 */
public class ExternalServiceRAFamAffiliation extends BAbstractEntityExternalService {

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        callRafamService(entity);
    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
        // DO NOTHING
    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
        // DO NOTHING
    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
        callRafamService(entity);
    }

    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
        // DO NOTHING
    }

    @Override
    public void beforeDelete(BEntity entity) throws Throwable {
        // DO NOTHING
    }

    @Override
    public void beforeRetrieve(BEntity entity) throws Throwable {
        // DO NOTHING
    }

    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
        // DO NOTHING
    }

    @Override
    public void init(BEntity entity) throws Throwable {
        // DO NOTHING
    }

    @Override
    public void validate(BEntity entity) throws Throwable {
        // DO NOTHING
    }

    /**
     * Appel le service de création des annonces RAFam pour l'affilié qui a été modifié
     * 
     * @param entity
     * @throws SQLException
     * @throws BJadeMultipleJdbcConnectionInSameThreadException
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private void callRafamService(BEntity entity) throws SQLException,
            BJadeMultipleJdbcConnectionInSameThreadException, JadeApplicationException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {

        boolean newContextCreated = false;

        try {
            if (JadeThread.currentContext() == null) {
                BJadeThreadActivator.startUsingContext(entity.getSession().getCurrentThreadTransaction());
                newContextCreated = true;
            }

            AFAdhesionViewBean adhesion = (AFAdhesionViewBean) entity;
            ALServiceLocator.getAnnonceRafamCreationService().creerAnnonceModificationAffilie(
                    adhesion.getAffiliation().getAffilieNumero());
        } finally {
            if (newContextCreated) {
                BJadeThreadActivator.stopUsingContext(entity.getSession().getCurrentThreadTransaction());
            }
        }
    }

}
