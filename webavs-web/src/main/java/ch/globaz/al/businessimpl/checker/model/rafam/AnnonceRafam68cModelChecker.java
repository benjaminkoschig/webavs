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
 * classe de validation des donn�es de {@link ch.globaz.al.business.models.rafam.AnnonceRafamModel}
 * 
 * @author jts
 * 
 */
public abstract class AnnonceRafam68cModelChecker extends ALAbstractChecker {

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
    protected static void checkCodesystemIntegrity(AnnonceRafamModel model) throws JadeApplicationException,
            JadePersistenceException {
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
