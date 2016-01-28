package globaz.ged;

import globaz.commons.nss.NSUtil;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.demandes.REDemandeRenteJointDemandeManager;
import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.globall.util.JACalendar;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.Properties;

/**
 * Effectue la synchronisation des données de base d'un tiers au niveau des Rentes
 * 
 * @author JWE
 * 
 */
public class AirsFPVSynchroRentes extends BAbstractEntityExternalService {

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
     * Cette méthode permet de propager un tiers rentier
     * 
     * @param entity
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     */
    private void synchronize(BEntity entity) throws JadeServiceLocatorException, JadeServiceActivatorException {
        try {
            // l'entité est une demande de rentes
            REDemandeRente demandeRente = null;

            if (entity instanceof REDemandeRente) {
                demandeRente = (REDemandeRente) entity;
            }
            // récupérer la prestation pour avoir les informations du tiers
            REDemandeRenteJointDemandeManager renteJointDemandeRenteManager = new REDemandeRenteJointDemandeManager();
            renteJointDemandeRenteManager.setForIdDemandeRente(demandeRente.getIdDemandeRente());
            renteJointDemandeRenteManager.setSession(demandeRente.getSession());
            renteJointDemandeRenteManager.find();

            // on récupère la première entité
            REDemandeRenteJointDemande demandeRenteJointDemande = (REDemandeRenteJointDemande) renteJointDemandeRenteManager
                    .getFirstEntity();

            // on créer un objet de type tiers
            TITiersViewBean tiers = new TITiersViewBean();

            // on récupère les informations du tiers
            if (demandeRenteJointDemande != null) {
                tiers.setIdTiers(demandeRenteJointDemande.getIdTiersRequerant());
                tiers.setSession(demandeRente.getSession());
                tiers.retrieve();
            }

            Properties properties = new Properties();

            if (tiers != null && !tiers.isNew()) {
                properties.setProperty(AirsConstants.NSS, NSUtil.unFormatAVS(tiers.getNumAvsActuel()));
                properties.setProperty(AirsConstants.NOM, tiers.getDesignation1());
                properties.setProperty(AirsConstants.PRENOM, tiers.getDesignation2());
            }
            properties.setProperty(AirsConstants.DOMAINE, "RCP");
            properties.setProperty(AirsConstants.DATE_CREATION, JACalendar.todayJJsMMsAAAA());
            properties.setProperty(AirsConstants.USER_ID, tiers.getSession().getUserId());
            // Si on une demande de rente qui est de type survivant, il faut vérifier si le tiers a bien une date de
            // décès.
            if (demandeRente.getCsTypeDemandeRente().equalsIgnoreCase(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT)) {
                if (!tiers.getDateDeces().equals("")) {
                    JadeGedFacade.propagate(properties);
                }

            } else {
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