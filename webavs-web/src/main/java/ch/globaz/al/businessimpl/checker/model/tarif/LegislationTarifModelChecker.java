package ch.globaz.al.businessimpl.checker.model.tarif;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.model.tarif.ALLegislationTarifModelException;
import ch.globaz.al.business.models.tarif.LegislationTarifModel;
import ch.globaz.al.business.models.tarif.LegislationTarifSearchModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * classe de validation des donn�es de LegislationTarif
 * 
 * @author PTA
 * 
 */
public class LegislationTarifModelChecker extends ALAbstractChecker {

    /**
     * V�rifie l'int�grit� m�tier des donn�es
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkBusinessIntegrity(LegislationTarifModel model) throws JadeApplicationException,
            JadePersistenceException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        // deux l�gislations ne peuvent utiliser le m�me type, on v�rifie si
        // c'est le cas
        LegislationTarifSearchModel lts = new LegislationTarifSearchModel();
        lts.setForTypeLegislation(model.getTypeLegislation());
        if (ALImplServiceLocator.getLegislationTarifModelService().count(lts) > 0) {
            JadeThread.logError(LegislationTarifModelChecker.class.getName(),
                    "al.tarif.legislationTarifModel.typeLegislation.businessIntegrity.alreadyExisting");
        }
    }

    /**
     * V�rification de l'int�grit� des codes syst�me
     * 
     * @param legislationTarifModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkCodeSystemIntegrity(LegislationTarifModel legislationTarifModel)
            throws JadePersistenceException, JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSTarif.GROUP_LEGISLATION,
                    legislationTarifModel.getTypeLegislation())) {
                JadeThread.logError(LegislationTarifModelChecker.class.getName(),
                        "al.tarif.legislationTarifModel.typeLegislation.codeSystemIntegrity");
            }
        } catch (Exception e) {
            throw new ALLegislationTarifModelException(
                    "LegislationTarifModel problem during checking codes system integrity", e);
        }

    }

    /**
     * 
     * @param legislationTarifModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDatabaseIntegrity(LegislationTarifModel legislationTarifModel)
            throws JadePersistenceException, JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }
        // type de donn�es de type de l�gislation du tarif
        if (!JadeNumericUtil.isIntegerPositif(legislationTarifModel.getTypeLegislation())) {
            JadeThread.logError(LegislationTarifModelChecker.class.getName(),
                    "al.tarif.legislationTarifModel.typeLegislation.databaseIntegrity.type");
        }
    }

    /**
     * 
     * @param legislationTarifModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkMandatory(LegislationTarifModel legislationTarifModel) throws JadePersistenceException,
            JadeApplicationException {

        // obligation du type de l�gislation
        if (JadeStringUtil.isEmpty(legislationTarifModel.getTypeLegislation())) {
            JadeThread.logError(LegislationTarifModelChecker.class.getName(),
                    "al.tarif.legislationTarifModel.typeLegislation.mandatory");
        }

    }

    /**
     * V�rification de l'int�grit� des donn�es
     * 
     * @param legislationTarifModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(LegislationTarifModel legislationTarifModel) throws JadePersistenceException,
            JadeApplicationException {
        LegislationTarifModelChecker.checkMandatory(legislationTarifModel);
        LegislationTarifModelChecker.checkDatabaseIntegrity(legislationTarifModel);
        LegislationTarifModelChecker.checkCodeSystemIntegrity(legislationTarifModel);
        LegislationTarifModelChecker.checkBusinessIntegrity(legislationTarifModel);
    }

}
