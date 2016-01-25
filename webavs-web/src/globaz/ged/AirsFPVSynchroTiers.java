package globaz.ged;

import globaz.commons.nss.NSUtil;
import globaz.globall.context.BJadeThreadActivator;
import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.services.AFAffiliationServices;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.corvus.business.services.models.demande.DemandeRenteService;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.pyxis.business.services.PyxisCrudServiceLocator;
import ch.globaz.pyxis.domaine.PersonneAVS;

/**
 * Effectue la synchronisation des données de base d'un tiers vers AIRS.
 * 
 * @author JWE
 * 
 */
public class AirsFPVSynchroTiers extends BAbstractEntityExternalService {

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
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
        boolean newContextCreated = false;
        try {

            TITiersViewBean tiers = (TITiersViewBean) entity;

            Properties properties = new Properties();
            properties.setProperty(AirsConstants.NSS, NSUtil.unFormatAVS(tiers.getNumAvsActuel()));
            properties.setProperty(AirsConstants.PRENOM, tiers.getDesignation2());
            properties.setProperty(AirsConstants.NOM, tiers.getDesignation1());
            if (changementNumAvs(tiers)) {
                properties.setProperty(AirsConstants.PREVIOUS_NSS, NSUtil.unFormatAVS(tiers.getOldNumAvs()));
            }
            properties.setProperty(AirsConstants.USER_ID, tiers.getSession().getUserId());

            if (!tiers.isNew()) {
                properties.setProperty(AirsConstants.DATE_MODIFICATION, JACalendar.todayJJsMMsAAAA());
            }
            propagateAffiliation(tiers, properties);

            if (JadeThread.currentContext() == null) {
                BJadeThreadActivator.startUsingContext(entity.getSession().getCurrentThreadTransaction());
                newContextCreated = true;
            }
            propagateRentes(tiers, properties);
            propagateAF(tiers, properties);

        } catch (Exception e) {
            JadeLogger.warn(this,
                    "Could not propagate data for " + entity + "(" + entity.getId() + "): " + e.toString());
        } finally {
            if (newContextCreated) {
                BJadeThreadActivator.stopUsingContext(entity.getSession().getCurrentThreadTransaction());
            }
        }
    }

    /**
     * Cette méthode permet de vérifier si le tiers possède un dossier AF ou des droits AF, si c'est le cas on pourra
     * propager les
     * données avec le domaine : "AYANT_DROIT_CAF"
     * 
     * @param tiers
     * @param properties
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     * @throws InterruptedException
     */
    private void propagateAF(TITiersViewBean tiers, Properties properties) throws JadeApplicationException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException, JadeServiceLocatorException,
            JadeServiceActivatorException, InterruptedException {
        try {

            // On contrôle si l'enfant a des droits de formation actifs
            if (ALServiceLocator.getEnfantBusinessService().getNombreDroitsFormationActifs(tiers.getIdTiers()) > 0) {
                Thread.sleep(2000);
                // si le tiers a des droits de formation actifs, on le propage
                properties.setProperty(AirsConstants.DOMAINE, "BENEFICIAIRE_CAF");
                JadeGedFacade.propagate(properties);

            } else {
                // On propage si le tiers est allocataire CAF
                DossierComplexSearchModel dossierSearchModel = new DossierComplexSearchModel();
                dossierSearchModel.setForIdTiersAllocataire(tiers.getIdTiers());
                dossierSearchModel.setWhereKey("dossiersForIdTiers");
                dossierSearchModel = ALServiceLocator.getDossierComplexModelService().search(dossierSearchModel);

                if (dossierSearchModel.getSize() > 0) {

                    Thread.sleep(2000);
                    properties.setProperty(AirsConstants.DOMAINE, "ALLOC");
                    JadeGedFacade.propagate(properties);

                }
            }
        } catch (Exception e) {
            // on catch l'exception a cette hauteur pour ne pas impacter les autres traitements de propagations
            JadeLogger.warn(this, "Could not propagate data for " + tiers + "(" + tiers.getId() + "): " + e.toString());
        }
    }

    /**
     * Cette méthode permet de propager les affiliations potentielles d'un tiers
     * 
     * @param tiers : le tiers passé en paramètre
     * @param properties : on passe les propriétés déjà settés
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     * @throws InterruptedException
     */
    private void propagateAffiliation(TITiersViewBean tiers, Properties properties) throws JadeServiceLocatorException,
            JadeServiceActivatorException, InterruptedException {
        try {
            List<AFAffiliation> affiliationsList = AFAffiliationServices.getAffiliationsByIDTiers(tiers.getIdTiers(),
                    tiers.getSession());
            if (affiliationsList != null) {
                for (AFAffiliation affiliation : affiliationsList) {
                    properties.setProperty(AirsConstants.NAFF, affiliation.getAffilieNumero());
                    // on propage dans tous les cas le domaine par défaut AFFILIE_AVS
                    properties.setProperty(AirsConstants.DOMAINE, "AVS");
                    JadeGedFacade.propagate(properties);
                    Thread.sleep(2000);
                    // CAF : On redéfini le domaine et on propage à nouveau
                    properties.setProperty(AirsConstants.DOMAINE, "AFFIL");
                    JadeGedFacade.propagate(properties);

                }
                // Supprimer la propriété numéro d'affilié dès qu'on a fini la propagation des affiliations.
                properties.remove(AirsConstants.NAFF);
            }
        } catch (Exception e) {
            // on catch l'exception a cette hauteur pour ne pas impacter les autres traitements de propagations
            JadeLogger.warn(this, "Could not propagate data for " + tiers + "(" + tiers.getId() + "): " + e.toString());
        }

    }

    /**
     * Cette méthode permet de propager un tiers dans le domaine des rentes s'il en a au moins une.
     * 
     * @param tiers
     * @param properties
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     * @throws InterruptedException
     */
    private void propagateRentes(TITiersViewBean tiers, Properties properties) throws JadeServiceLocatorException,
            JadeServiceActivatorException, InterruptedException {
        try {
            DemandeRenteService serviceRente = CorvusServiceLocator.getDemandeRenteService();
            PersonneAVS requerant = PyxisCrudServiceLocator.getPersonneAvsCrudService().read(
                    Long.parseLong(tiers.getIdTiers()));
            Set<DemandeRente> listeDemandes = serviceRente.demandesDuRequerantEtDeSaFamille(requerant);
            for (DemandeRente demande : listeDemandes) {
                if (requerant.equals(demande.getRequerant())) {
                    // on ne propage que si le tiers a des rentes et une seule fois
                    properties.setProperty(AirsConstants.DOMAINE, "RCP");
                    Thread.sleep(2000);
                    JadeGedFacade.propagate(properties);
                    break;
                }
            }
        } catch (Exception e) {
            // on catch l'exception et on la log a cette hauteur pour ne pas impacter les autres traitements de
            // propagations
            JadeLogger.warn(this, "Could not propagate data for " + tiers + "(" + tiers.getId() + "): " + e.toString());
        }
    }

    @Override
    public void validate(BEntity entity) throws Throwable {
    }

}
