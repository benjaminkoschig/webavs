package ch.globaz.al.businessimpl.checker.model.rafam;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;

/**
 * classe de validation des données de {@link ch.globaz.al.business.models.rafam.AnnonceRafamModel}
 * 
 * @author jts
 * 
 */
public abstract class AnnonceRafam68cModelChecker extends ALAbstractChecker {

    /**
     * Vérifie l'intégritée "business" des données
     * 
     * @param model
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected static void checkBusinessIntegrity(AnnonceRafamModel model) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        search.setForRecordNumber(model.getRecordNumber());
        search.setForTypeAnnonce(RafamTypeAnnonce._68C_ANNULATION.getCode());
        search.setForIdAnnonce(model.getIdAnnonce());
        search.setWhereKey("doublon68c");
        if (ALServiceLocator.getAnnonceRafamModelService().count(search) > 0) {
            JadeThread.logError(AnnonceRafam68cModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.typeAnnonce.businessIntegrity.existing68c");
        }

    }

    /**
     * Vérification des codesSystems
     * 
     * @param model
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected static void checkCodesystemIntegrity(AnnonceRafamModel model) throws JadeApplicationException,
            JadePersistenceException {
        // DO NOTHING
    }

    /**
     * vérification de l'intégrité des données
     * 
     * @param model
     *            Modèle à valider
     */
    protected static void checkDatabaseIntegrity(AnnonceRafamModel model) {
        // DO NOTHING
    }

    /**
     * vérification des données requises
     * 
     * @param model
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected static void checkMandatory(AnnonceRafamModel model) throws JadeApplicationException,
            JadePersistenceException {

        if (JadeStringUtil.isBlank(model.getOfficeIdentifier())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.officeIdentifier.mandatory");
        }

        if (JadeStringUtil.isBlank(model.getLegalOffice())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.legalOffice.mandatory");
        }
    }
}
