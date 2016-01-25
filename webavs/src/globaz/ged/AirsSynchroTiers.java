package globaz.ged;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.Properties;

/**
 * Effectue la synchronisation des données de base d'un tiers vers AIRS.
 * 
 * @author EFL
 * 
 */
public class AirsSynchroTiers extends BAbstractEntityExternalService {

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

    private boolean changementNumAvs(TITiersViewBean tiers) {
        if (!JadeStringUtil.isNull(tiers.getNumAvsActuel()) && !JadeStringUtil.isNull(tiers.getOldNumAvs())) {
            if (tiers.getNumAvsActuel().equals(tiers.getOldNumAvs())) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void init(BEntity entity) throws Throwable {
    }

    private void synchronize(BEntity entity) throws JadeServiceLocatorException, JadeServiceActivatorException {
        try {
            TITiersViewBean tiers = null;
            if (TITiersViewBean.class.isAssignableFrom(entity.getClass())) {
                tiers = (TITiersViewBean) entity;
            } else if (TIAvoirAdresse.class.isAssignableFrom(entity.getClass())) {
                TIAvoirAdresse adresse = (TIAvoirAdresse) entity;
                tiers = new TITiersViewBean();
                tiers.setSession(adresse.getSession());
                tiers.setIdTiers(adresse.getIdTiers());
                tiers.retrieve();
            }
            if (tiers != null) {
                Properties properties = new Properties();
                properties.setProperty(AirsConstants.NIP, tiers.getIdTiers());
                properties.setProperty(AirsConstants.NSS, JadeStringUtil.removeChar(tiers.getNumAvsActuel(), '.'));
                properties.setProperty(AirsConstants.NOM, tiers.getDesignation1());
                properties.setProperty(AirsConstants.PRENOM, tiers.getDesignation2());
                properties.setProperty(AirsConstants.NPA, tiers.getLocaliteLong());
                properties.setProperty(AirsConstants.NNSS, JadeStringUtil.removeChar(tiers.getNumAvsActuel(), '.'));
                if (changementNumAvs(tiers)) {
                    properties.setProperty(AirsConstants.PREVIOUS_NSS,
                            JadeStringUtil.removeChar(tiers.getOldNumAvs(), '.'));
                }
                JadeGedFacade.propagate(properties);
            }
        } catch (Exception e) {
            JadeLogger.warn(this,
                    "Could not propagate data for " + entity + "(" + entity.getId() + "): " + e.toString());
        }
    }

    @Override
    public void validate(BEntity entity) throws Throwable {
    }

}
