package globaz.ged;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.globall.util.JACalendar;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.Properties;

/**
 * Effectue la synchronisation des données de base d'un tiers Affilié vers AIRS.
 * 
 * @author JWE
 * 
 */
public class AirsFPVSynchroAffiliation extends BAbstractEntityExternalService {

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

    /**
     * Cette méthode permet de propager un tiers affilié
     * 
     * @param entity : l'entité de type BEntity
     * @param tiers : un objet TITIers
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     */
    public static void synchronize(BEntity entity) throws JadeServiceLocatorException, JadeServiceActivatorException {

        try {
            // récupérer affiliation
            AFAffiliation affiliation = (AFAffiliation) entity;
            TITiersViewBean tiers = null;

            if (affiliation != null) {
                // récupérer tiers
                tiers = affiliation.getTiers();
            }
            if (tiers != null) {
                // il faut propager 1 fois pour les affiliation simples
                propagateAffiliation(affiliation, tiers, "AVS");
                Thread.sleep(2000);
                propagateAffiliation(affiliation, tiers, "AFFIL");
            }
        } catch (Exception e) {
            JadeLogger.warn(AirsFPVSynchroAffiliation.class,
                    "Could not propagate data for " + entity + "(" + entity.getId() + "): " + e.toString());
        }
    }

    private static void propagateAffiliation(AFAffiliation affiliation, TITiersViewBean tiers, String domaine)
            throws JadeServiceLocatorException, JadeServiceActivatorException {

        Properties properties = new Properties();
        properties.setProperty(AirsConstants.NAFF, affiliation.getAffilieNumero());
        properties.setProperty(AirsConstants.NSS, NSUtil.unFormatAVS(tiers.getNumAvsActuel()));
        properties.setProperty(AirsConstants.NOM, tiers.getDesignation1());
        properties.setProperty(AirsConstants.PRENOM, tiers.getDesignation2());
        properties.setProperty(AirsConstants.DOMAINE, domaine);
        properties.setProperty(AirsConstants.DATE_CREATION, JACalendar.todayJJsMMsAAAA());
        properties.setProperty(AirsConstants.USER_ID, affiliation.getSession().getUserId());

        JadeGedFacade.propagate(properties);
    }

    @Override
    public void validate(BEntity entity) throws Throwable {
    }

}