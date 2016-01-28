/**
 * 
 */
package ch.globaz.perseus.businessimpl.checkers.creancier;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.constantes.CSTypeCreance;
import ch.globaz.perseus.business.exceptions.PerseusException;
import ch.globaz.perseus.business.exceptions.models.creancier.CreancierException;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.models.creancier.Creancier;
import ch.globaz.perseus.business.models.creancier.CreancierSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

/**
 * @author MBO
 * 
 */
public class CreancierChecker extends PerseusAbstractChecker {

    /**
     * @param simpleCreancier
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws CreancierException
     * @throws PerseusException
     */
    public static void checkForCreate(Creancier creancier) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, PerseusException {
        CreancierChecker.checkMandatory(creancier);
    }

    /**
     * @param simpleCreancier
     */
    public static void checkForDelete(Creancier creancier) {
    }

    /**
     * @param simpleCreancier
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws CreancierException
     * @throws PerseusException
     */
    public static void checkForUpdate(Creancier creancier) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, PerseusException {
        CreancierChecker.checkMandatory(creancier);
    }

    /**
     * @param simpleCreancier
     * @throws JadeNoBusinessLogSessionError
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PerseusException
     */
    private static void checkMandatory(Creancier creancier) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, PerseusException {
        // Voir si le tiers est déjà dans les créanciers si ce n'est pas un impôt à la source
        if (!CSTypeCreance.TYPE_CREANCE_IMPOT_SOURCE.getCodeSystem().equals(
                creancier.getSimpleCreancier().getCsTypeCreance())) {
            CreancierSearchModel csm = new CreancierSearchModel();
            csm.setForIdDemande(creancier.getSimpleCreancier().getIdDemande());
            csm.setForIdTiers(creancier.getSimpleCreancier().getIdTiers());
            csm.setForNotIdCreancier(creancier.getId());
            if (PerseusServiceLocator.getCreancierService().count(csm) > 0) {
                JadeThread.logError(CreancierChecker.class.getName(), "perseus.creancier.creancier.tiers.existdeja");
            }
        }

        // if (!JadeStringUtil.isEmpty(creancier.getSimpleCreancier().getReferencePaiement())
        // && !PerseusServiceLocator.getBVRService().validationNumeroBVR(
        // creancier.getSimpleCreancier().getReferencePaiement())) {
        // JadeThread.logError(CreancierChecker.class.getName(),
        // "perseus.creancier.creanceAccordee.numero.reference.incorrect");
        // }
        // VOir si le montant attribué n'est pas plus grand que le montant déjà attribué
        try {
            Demande demande = PerseusServiceLocator.getDemandeService().read(
                    creancier.getSimpleCreancier().getIdDemande());
            Float montantAttribuable = PerseusServiceLocator.getDemandeService().calculerRetroPourCreanciers(demande);
            Float montantAttribue = new Float(0);
            CreancierSearchModel searchModel = new CreancierSearchModel();
            searchModel.setForIdDemande(creancier.getSimpleCreancier().getIdDemande());
            searchModel.setForNotIdCreancier(creancier.getId());
            searchModel = PerseusServiceLocator.getCreancierService().search(searchModel);
            for (JadeAbstractModel model : searchModel.getSearchResults()) {
                Creancier c = (Creancier) model;
                montantAttribue += Float.parseFloat(c.getSimpleCreancier().getMontantAccorde());
            }
            Float montantAttribuePourCreancier = new Float(0);
            if (!JadeStringUtil.isEmpty(creancier.getSimpleCreancier().getMontantAccorde())) {
                montantAttribuePourCreancier = Float.parseFloat(creancier.getSimpleCreancier().getMontantAccorde()
                        .replace("'", ""));
            }
            if (montantAttribue + montantAttribuePourCreancier > montantAttribuable) {
                JadeThread.logError(CreancierChecker.class.getName(), "perseus.creancier.creancier.repartition.tomuch");
            }

        } catch (DemandeException e) {
            throw new CreancierException("DemandeException during check Creancier repartition " + e.toString(), e);
        }

    }
}
