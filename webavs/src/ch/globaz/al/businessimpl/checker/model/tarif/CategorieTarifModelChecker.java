package ch.globaz.al.businessimpl.checker.model.tarif;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.model.tarif.ALCategorieTarifModelException;
import ch.globaz.al.business.models.tarif.CategorieTarifModel;
import ch.globaz.al.business.models.tarif.CategorieTarifSearchModel;
import ch.globaz.al.business.models.tarif.LegislationTarifSearchModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * classe de validit� des donn�es de CategrotieTarifModel
 * 
 * @author PTA
 * 
 */
public abstract class CategorieTarifModelChecker extends ALAbstractChecker {

    /**
     * v�rifier l�int�grit� des donn�es au niveau m�tier AF categorietarif
     * 
     * @param categorieTarifModel
     *            Mod�le � valider
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    private static void checkBusinessIntegrity(CategorieTarifModel categorieTarifModel)
            throws JadeApplicationException, JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // v�rifie que l'idLegislation fourni existe bien dans la table
        // Legislation Tarif
        LegislationTarifSearchModel sl = new LegislationTarifSearchModel();
        sl.setForIdLegislationTarif(categorieTarifModel.getIdLegislation());
        if (ALImplServiceLocator.getLegislationTarifModelService().count(sl) == 0) {
            JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                    "al.tarif.categorieTarifModel.idLegislation.businessIntegrity.existingId");
        }

        // deux cat�gories de m�me type et de m�me l�gislation ne peuvent
        // exister
        CategorieTarifSearchModel cts = new CategorieTarifSearchModel();
        cts.setForCategorieTarif(categorieTarifModel.getCategorieTarif());
        cts.setForIdCategorieTarif(categorieTarifModel.getIdCategorieTarif());
        if (ALImplServiceLocator.getCategorieTarifModelService().count(cts) > 0) {
            JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                    "al.tarif.categorieTarifModel.categorieTarif.businessIntegrity.alreadyExisting");
        }
    }

    /**
     * V�rification de l'int�grit� des codes syst�me
     * 
     * @param categorieTarifModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkCodeSystemIntegrity(CategorieTarifModel categorieTarifModel)
            throws JadePersistenceException, JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        try {

            // contr�le de cat�gorie de tarif
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSTarif.GROUP_CATEGORIE,
                    categorieTarifModel.getCategorieTarif())) {
                JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                        "al.tarif.categorieTarifModel.categorieTarif.codesystemIntegrity");
            }
        } catch (Exception e) {
            throw new ALCategorieTarifModelException(
                    "CategorieTarifModel problem during checking codes system integrity", e);
        }
    }

    /**
     * v�rifie l'int�grit� des donn�es de TarifModel, si non respect�e lance un message sur l'int�grit� de ces donn�es
     * 
     * @param categorieTarifModel
     *            Mod�le � valider
     */
    private static void checkDatabaseIntegrity(CategorieTarifModel categorieTarifModel) {
        if (ALAbstractChecker.hasError()) {
            return;
        }
        // contr�le type cat�gorie du tarif
        if (!JadeNumericUtil.isIntegerPositif(categorieTarifModel.getCategorieTarif())) {
            JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                    "al.tarif.categorieTarifModel.categorieTarif.databaseIntegrity.type");

        }
        // contr�le type l'identifiant de la prestation du tarif
        if (!JadeNumericUtil.isIntegerPositif(categorieTarifModel.getIdLegislation())) {
            JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                    "al.tarif.categorieTarifModel.idLegislation.databaseIntegrity.type");
        }

    }

    /**
     * * v�rifie l'obligation des donn�es de TarifModel, si non respect�e lance un message sur l'int�grit� de ces
     * donn�es
     * 
     * @param categorieTarifModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkMandatory(CategorieTarifModel categorieTarifModel) throws JadeApplicationException {

        // obligation de cat�gorie de tarif
        if (JadeStringUtil.isEmpty(categorieTarifModel.getCategorieTarif())) {
            JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                    "al.tarif.categorieTarifModel.categorieTarif.mandatory.");
        }
        // obligation de l'identifiant de la legislation du tarif
        if (JadeStringUtil.isEmpty(categorieTarifModel.getIdLegislation())) {
            JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                    "al.tarif.categorieTarifModel.idLegislation.mandatory");
        }

    }

    /**
     * valide les donn�es de TarifModel
     * 
     * @param categorieTarifModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validate(CategorieTarifModel categorieTarifModel) throws JadePersistenceException,
            JadeApplicationException {
        CategorieTarifModelChecker.checkMandatory(categorieTarifModel);
        CategorieTarifModelChecker.checkDatabaseIntegrity(categorieTarifModel);
        CategorieTarifModelChecker.checkCodeSystemIntegrity(categorieTarifModel);
        CategorieTarifModelChecker.checkBusinessIntegrity(categorieTarifModel);
    }
}
