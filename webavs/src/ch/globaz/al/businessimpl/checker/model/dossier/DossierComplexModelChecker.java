package ch.globaz.al.businessimpl.checker.model.dossier;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Date;
import ch.globaz.al.business.constantes.ALConstAttributsEntite;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.param.business.service.ParamServiceLocator;

/**
 * Checker du modèle <code>DossierPlusComplexModel</code>
 * 
 * @author jts
 * 
 * @see ch.globaz.al.business.models.dossier.DossierComplexModel
 */
public class DossierComplexModelChecker extends ALAbstractChecker {

    /**
     * Vérifie l'intégritée "business" des données
     * 
     * @param dossierComplexModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkBusinessIntegrity(DossierComplexModel dossierComplexModel)
            throws JadePersistenceException, JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // Si le dossier est agricole et que l'allocataire lié n'est pas
        // agricole, il faut le signaler
        if (ALServiceLocator.getDossierBusinessService().isAgricole(
                dossierComplexModel.getDossierModel().getActiviteAllocataire())
                && !ALServiceLocator.getAllocataireBusinessService().isAgricole(
                        dossierComplexModel.getDossierModel().getIdAllocataire())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.allocataire.agricoleModel.domaineMontagne.mandatory");

        }

        // Récupération du paramètre paiement direct par défaut selon date du jour
        String dateJour = JadeDateUtil.getGlobazFormattedDate(new Date());
        String paiementDirectDefault = (ParamServiceLocator.getParameterModelService().getParameterByName(
                ALConstParametres.APPNAME, ALConstParametres.MODE_PAIEMENT_DIRECT, dateJour)).getValeurAlphaParametre();

        // si la CAF travaille en paiement direct par défaut, on met un avertissement si le nouveau dossier
        // est encodé en direct alors que l'affilié est en indirect
        if ("true".equals(paiementDirectDefault)) {
            if ("false".equals(ALServiceLocator
                    .getAttributEntiteModelService()
                    .getAttributAffilieByNumAffilie(ALConstAttributsEntite.PAIEMENT_DIRECT,
                            dossierComplexModel.getDossierModel().getNumeroAffilie()).getValeurAlpha())
                    && !"0".equals(dossierComplexModel.getDossierModel().getIdTiersBeneficiaire())) {

                JadeThread.logWarn(DossierModelChecker.class.getName(), "al.dossier.modePaiementIndirect.warning");
            }
        }

    }

    /**
     * Validation des données
     * 
     * @param model
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validate(DossierComplexModel model) throws JadeApplicationException, JadePersistenceException {
        DossierComplexModelChecker.checkBusinessIntegrity(model);
    }
}
