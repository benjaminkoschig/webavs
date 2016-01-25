package ch.globaz.al.external;

import globaz.globall.context.BJadeThreadActivator;
import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import globaz.pyxis.db.tiers.TIHistoriqueAvsManager;
import java.util.Date;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Service externe permettant la création d'annonce RAFam si un tiers utilisé comme allocataire ou enfant dans les AF
 * est modifié
 * 
 * @author jts
 * 
 */
public class ExternalServiceTiersViewBean extends BAbstractEntityExternalService {

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        // DO NOTHING

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

        TIHistoriqueAvsManager avsMgr = new TIHistoriqueAvsManager();
        avsMgr.setISession(entity.getSession());
        avsMgr.setFromDateDebut(JadeDateUtil.getGlobazFormattedDate(new Date()));
        avsMgr.setForIdTiers(entity.getId());
        avsMgr.changeManagerSize(0);
        avsMgr.find();

        if (avsMgr.getSize() > 0) {

            boolean newContextCreated = false;
            try {
                if (JadeThread.currentContext() == null) {
                    BJadeThreadActivator.startUsingContext(entity.getSession().getCurrentThreadTransaction());
                    newContextCreated = true;
                }

                ALServiceLocator.getAnnonceRafamCreationService().creerAnnonceModificationsTiers(entity.getId());
            } finally {
                if (newContextCreated) {
                    BJadeThreadActivator.stopUsingContext(entity.getSession().getCurrentThreadTransaction());
                }
            }
        }
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

}
