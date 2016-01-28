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
 * Checker du mod�le <code>DossierPlusComplexModel</code>
 * 
 * @author jts
 * 
 * @see ch.globaz.al.business.models.dossier.DossierComplexModel
 */
public class DossierComplexModelChecker extends ALAbstractChecker {

    /**
     * V�rifie l'int�grit�e "business" des donn�es
     * 
     * @param dossierComplexModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkBusinessIntegrity(DossierComplexModel dossierComplexModel)
            throws JadePersistenceException, JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // Si le dossier est agricole et que l'allocataire li� n'est pas
        // agricole, il faut le signaler
        if (ALServiceLocator.getDossierBusinessService().isAgricole(
                dossierComplexModel.getDossierModel().getActiviteAllocataire())
                && !ALServiceLocator.getAllocataireBusinessService().isAgricole(
                        dossierComplexModel.getDossierModel().getIdAllocataire())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.allocataire.agricoleModel.domaineMontagne.mandatory");

        }

        // R�cup�ration du param�tre paiement direct par d�faut selon date du jour
        String dateJour = JadeDateUtil.getGlobazFormattedDate(new Date());
        String paiementDirectDefault = (ParamServiceLocator.getParameterModelService().getParameterByName(
                ALConstParametres.APPNAME, ALConstParametres.MODE_PAIEMENT_DIRECT, dateJour)).getValeurAlphaParametre();

        // si la CAF travaille en paiement direct par d�faut, on met un avertissement si le nouveau dossier
        // est encod� en direct alors que l'affili� est en indirect
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
     * Validation des donn�es
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validate(DossierComplexModel model) throws JadeApplicationException, JadePersistenceException {
        DossierComplexModelChecker.checkBusinessIntegrity(model);
    }
}
