/**
 * 
 */
package ch.globaz.al.businessimpl.checker.model.tarif;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.model.tarif.ALCategorieTarifModelException;
import ch.globaz.al.business.models.tarif.CritereTarifModel;
import ch.globaz.al.business.models.tarif.CritereTarifSearchModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * classe de validation des r�gles d'int�grrit� de CritereTarifModel
 * 
 * @author PTA
 * 
 */
public class CritereTarifModelChecker extends ALAbstractChecker {

    /**
     * V�rifie l'int�grit� m�tier des donn�es
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    private static void checkBusinessIntegrity(CritereTarifModel model) throws JadeApplicationException,
            JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // deux crit�res identiques ne peuvent exister
        CritereTarifSearchModel cts = new CritereTarifSearchModel();
        cts.setForCritereTarif(model.getCritereTarif());
        cts.setForDebut(model.getDebutCritere());
        cts.setForFin(model.getFinCritere());

        if (ALImplServiceLocator.getCritereTarifModelService().count(cts) > 0) {
            JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                    "al.tarif.categorieTarifModel.categorieTarif.businessIntegrity.alreadyExisting");
        }
    }

    /**
     * v�rification des Codes Syst�mes
     * 
     * @param critereTarifModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkCodeSystemIntegrity(CritereTarifModel critereTarifModel) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        try {
            // code syst�me de crit�re du tarif
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSTarif.GROUP_CRITERE, critereTarifModel.getCritereTarif())) {
                JadeThread.logError(CritereTarifModelChecker.class.getName(),
                        "al.tarif.critereTarifModel.critereTarif.codeSystemIntegrity");
            }
        } catch (Exception e) {
            throw new ALCategorieTarifModelException(
                    "CritereTarifModel problem during checking codes system integrity", e);
        }
    }

    /**
     * v�rification de la validit� des donn�es
     * 
     * @param critereTarifModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */

    private static void checkDatabaseIntegrity(CritereTarifModel critereTarifModel) throws JadePersistenceException,
            JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        // type de donn�es du crit�re du tarif
        if (!JadeNumericUtil.isIntegerPositif(critereTarifModel.getCritereTarif())) {
            JadeThread.logError(CritereTarifModelChecker.class.getName(),
                    "al.tarif.critereTarifModel.critereTarif.databaseIntegrity.type");
        }
        // type de donn�es du d�but du crit�re
        if (!JadeNumericUtil.isInteger(critereTarifModel.getDebutCritere())) {
            JadeThread.logError(CritereTarifModelChecker.class.getName(),
                    "al.tarif.critereTarifModel.debutCritere.type");
        }

        // type de donn�es de fin du crit�re
        if (!JadeNumericUtil.isInteger(critereTarifModel.getFinCritere())) {
            JadeThread.logError(CritereTarifModelChecker.class.getName(), "al.tarif.critereTarifModel.finCritere.type");
        }

    }

    /**
     * v�rification des donn�es obligatoires
     * 
     * @param critereTarifModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */

    private static void checkMandatory(CritereTarifModel critereTarifModel) throws JadePersistenceException,
            JadeApplicationException {

        // obligation du crit�re du tarif
        if (JadeStringUtil.isEmpty(critereTarifModel.getCritereTarif())) {
            JadeThread.logError(CritereTarifModelChecker.class.getName(),
                    "al.tarif.critereTarifModel.critereTarif.mandatory");
        }
        // obligation du d�but du crit�re
        if (JadeStringUtil.isEmpty(critereTarifModel.getDebutCritere())) {
            JadeThread.logError(CritereTarifModelChecker.class.getName(),
                    "al.tarif.critereTarifModel.debutCritere.mandatory");
        }
        // obligation du crit�re de fin
        if (JadeStringUtil.isEmpty(critereTarifModel.getFinCritere())) {
            JadeThread.logError(CritereTarifModelChecker.class.getName(),
                    "al.tarif.critereTarifModel.finCritere.mandatory");
        }
    }

    /**
     * validation des crit�res tarif selon le mod�le pass� en param�tre
     * 
     * @param critereTarifModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */

    public static void validate(CritereTarifModel critereTarifModel) throws JadePersistenceException,
            JadeApplicationException {
        CritereTarifModelChecker.checkMandatory(critereTarifModel);
        CritereTarifModelChecker.checkDatabaseIntegrity(critereTarifModel);
        CritereTarifModelChecker.checkCodeSystemIntegrity(critereTarifModel);
        CritereTarifModelChecker.checkBusinessIntegrity(critereTarifModel);
    }

}
