package globaz.ged;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.splitting.CIDossierSplitting;
import globaz.pyxis.db.tiers.TITiersViewBean;

import java.util.Properties;

/**
 * Effectue la synchronisation des données de base d'un tiers au niveau de la création
 * d'un splitting dans les CI
 */
public class AirsFPVSynchroCISplitting extends BAbstractEntityExternalService {

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        synchronize(entity);
    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
        synchronize(entity);
    }

    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeDelete(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
    }

    @Override
    public void init(BEntity entity) throws Throwable {
    }

    @Override
    public void validate(BEntity entity) throws Throwable {
    }

    /**
     * Cette méthode permet de propager un tiers rentier
     * 
     * @param entity
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     */
    private void synchronize(BEntity entity) throws JadeServiceLocatorException, JadeServiceActivatorException {
        try {
            // l'entité est un dossier de splitting
            CIDossierSplitting splitting = null;

            if (entity instanceof CIDossierSplitting) {
                splitting = (CIDossierSplitting) entity;
                BSession session = splitting.getSession();
                CIApplication application = (CIApplication) session.getApplication();
                if(application.isSplittingWantLienGed()) {
                    propagateTiers(splitting.getIdTiersInterneAssure(), session);
                    propagateTiers(splitting.getIdTiersInterneConjoint(), session);
                }
            }

        } catch (Exception e) {
            JadeLogger.warn(this,
                    "Could not propagate data for " + entity + "(" + entity.getId() + "): " + e.toString());
        }
    }

    private void propagateTiers(String idTiers, BSession session) throws Exception {
        // on créer un objet de type tiers
        TITiersViewBean tiers = new TITiersViewBean();

        // on récupère les informations du tiers
        tiers.setIdTiers(idTiers);
        tiers.setSession(session);
        tiers.retrieve();

        Properties properties = new Properties();

        if (tiers != null && !tiers.isNew()) {
            properties.setProperty(AirsConstants.NSS, NSUtil.unFormatAVS(tiers.getNumAvsActuel()));
            properties.setProperty(AirsConstants.NOM, tiers.getDesignation1());
            properties.setProperty(AirsConstants.PRENOM, tiers.getDesignation2());
        }
        properties.setProperty(AirsConstants.DOMAINE, "RCP");
        properties.setProperty(AirsConstants.DATE_CREATION, JACalendar.todayJJsMMsAAAA());
        properties.setProperty(AirsConstants.USER_ID, tiers.getSession().getUserId());
        JadeGedFacade.propagate(properties);
    }

}