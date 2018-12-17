package ch.globaz.al.businessimpl.checker.model.rafam;

import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

/**
 * classe de validation des données de {@link ch.globaz.al.business.models.rafam.AnnonceRafamModel}
 *
 * @author jts
 *
 */
public abstract class AnnonceRafam68aModelChecker extends ALAbstractChecker {

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
    protected static void checkBusinessIntegrity(AnnonceRafamModel model)
            throws JadePersistenceException, JadeApplicationException {
        // DO NOTHING
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
    protected static void checkCodesystemIntegrity(AnnonceRafamModel model)
            throws JadeApplicationException, JadePersistenceException {
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
    protected static void checkMandatory(AnnonceRafamModel model)
            throws JadeApplicationException, JadePersistenceException {

        // base légale
        if (JadeStringUtil.isEmpty(model.getBaseLegale())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.baseLegale.mandatory");
        }

        // NssAllocataire
        if (JadeStringUtil.isEmpty(model.getNssAllocataire())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.nssAllocataire.mandatory");
        }

        // statut familial
        if (JadeStringUtil.isEmpty(model.getCodeStatutFamilial())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.statutFamilial.mandatory");
        }

        // activité de l'allocataire
        if (JadeStringUtil.isEmpty(model.getCodeTypeActivite())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.typeActivite.mandatory");
        }

        // code etat de l'annonce
        if (JadeStringUtil.isEmpty(model.getEtat())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.codeEtat.mandatory");
        }

        if (model.getDelegated() == null) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.delegated.mandatory");
        }

        if (JadeStringUtil.isBlank(model.getOfficeIdentifier())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.officeIdentifier.mandatory");
        }

        if (JadeStringUtil.isBlank(model.getLegalOffice())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.legalOffice.mandatory");
        }

//        if (JadeStringUtil.isBlank(model.getCodeCentralePaysEnfant())) {
//            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
//                    "al.rafam.annonceRafamModel.codeCentralePaysEnfant.mandatory");
//        }
    }
}
