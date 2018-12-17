package ch.globaz.al.businessimpl.checker.model.rafam;

import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

/**
 * classe de validation des donn�es de {@link ch.globaz.al.business.models.rafam.AnnonceRafamModel}
 *
 * @author jts
 *
 */
public abstract class AnnonceRafam68aModelChecker extends ALAbstractChecker {

    /**
     * V�rifie l'int�grit�e "business" des donn�es
     *
     * @param model
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected static void checkBusinessIntegrity(AnnonceRafamModel model)
            throws JadePersistenceException, JadeApplicationException {
        // DO NOTHING
    }

    /**
     * V�rification des codesSystems
     *
     * @param model
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected static void checkCodesystemIntegrity(AnnonceRafamModel model)
            throws JadeApplicationException, JadePersistenceException {
        // DO NOTHING
    }

    /**
     * v�rification de l'int�grit� des donn�es
     *
     * @param model
     *            Mod�le � valider
     */
    protected static void checkDatabaseIntegrity(AnnonceRafamModel model) {
        // DO NOTHING
    }

    /**
     * v�rification des donn�es requises
     *
     * @param model
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected static void checkMandatory(AnnonceRafamModel model)
            throws JadeApplicationException, JadePersistenceException {

        // base l�gale
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

        // activit� de l'allocataire
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
