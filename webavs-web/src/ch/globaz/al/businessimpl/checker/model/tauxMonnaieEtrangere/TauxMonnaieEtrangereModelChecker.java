package ch.globaz.al.businessimpl.checker.model.tauxMonnaieEtrangere;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.exceptions.tauxMonnaieEtrangere.ALTauxMonnaieEtrangereException;
import ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereModel;
import ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;

/**
 * Classe de validation des donn�es du mod�le
 * {@link ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereModel}
 * 
 * @author PTA
 * 
 */

public abstract class TauxMonnaieEtrangereModelChecker extends ALAbstractChecker {

    /**
     * V�rifie l'int�grit� m�tier des donn�es d'un taux de monnaie �tang�re
     * 
     * @param tauxMonnaieModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkBusinessIntegrity(TauxMonnaieEtrangereModel tauxMonnaieModel)
            throws JadePersistenceException, JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }
        // int finTrimestre = Integer.parseInt(tauxMonnaieModel.getFinTaux()
        // .substring(0, 2));
        // int debutTrimestre = Integer.parseInt(tauxMonnaieModel.getDebutTaux()
        // .substring(0, 2));

        // la date de fin de taux
        // post�rieure � la date du d�but du taux
        if (!JadeDateUtil.isDateMonthYearAfter(tauxMonnaieModel.getFinTaux(), tauxMonnaieModel.getDebutTaux())) {
            JadeThread.logError(TauxMonnaieEtrangereModelChecker.class.getName(),
                    "al.tauxMonnaieEtrangere.tauxMonnaieEtrangereModel.dateFin.businessIntegrity");
        }
        // La date de d�but doit correspondre au d�but d'un trimestre
        if ((JadeStringUtil.parseInt(tauxMonnaieModel.getDebutTaux().substring(0, 2), 0) != 1)
                && (JadeStringUtil.parseInt(tauxMonnaieModel.getDebutTaux().substring(0, 2), 0) != 4)
                && (JadeStringUtil.parseInt(tauxMonnaieModel.getDebutTaux().substring(0, 2), 0) != 7)
                && (JadeStringUtil.parseInt(tauxMonnaieModel.getDebutTaux().substring(0, 2), 0) != 10)

        ) {
            JadeThread.logError(TauxMonnaieEtrangereModelChecker.class.getName(),
                    "al.tauxMonnaieEtrangere.tauxMonnaieEtrangereModel.dateDebutTrimestre.businessIntegrity");

        }

        // la date de fin doit correspondre � la fin
        // du
        // trimestre correspondant au d�but du trimestre
        //
        if (!JadeStringUtil
                .equals((ALServiceLocator.getPeriodeAFBusinessService().getPeriodeFinTrimestre(tauxMonnaieModel
                        .getDebutTaux())), tauxMonnaieModel.getFinTaux(), false)) {
            JadeThread.logError(TauxMonnaieEtrangereModelChecker.class.getName(),
                    "al.tauxMonnaieEtrangere.tauxMonnaieEtrangereModel.dateFinTrimestre.businessIntegrity");

        }

        // contr�le qu'un taux pour une p�riode et pour une monnaie n'existe pas
        // encore

        TauxMonnaieEtrangereSearchModel tauxChercher = new TauxMonnaieEtrangereSearchModel();
        tauxChercher.setForTypeMonnaie(tauxMonnaieModel.getTypeMonnaie());
        tauxChercher.setForDebutValiditeTaux(tauxMonnaieModel.getDebutTaux());

        if ((1 == ALServiceLocator.getTauxMonnaieEtrangereModelService().count(tauxChercher))
                && tauxMonnaieModel.isNew()) {
            JadeThread.logError(TauxMonnaieEtrangereModelChecker.class.getName(),
                    "al.tauxMonnaieEtrangere.tauxMonnaieEtrangereModel.existingId.businessIntegrity");

        }

    }

    /**
     * @param tauxMonnaieModel
     *            mod�le � v�rifier
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkCodesystemIntegrity(TauxMonnaieEtrangereModel tauxMonnaieModel)
            throws JadeApplicationException, JadePersistenceException {
        if (ALAbstractChecker.hasError()) {
            return;
        }
        try {

            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSTiers.GROUP_MONNAIE, tauxMonnaieModel.getTypeMonnaie())) {
                JadeThread.logError(TauxMonnaieEtrangereModelChecker.class.getName(),
                        "al.tauxMonnaieEtrangere.tauxMonnaieEtrangereModel.typeTaux.codesystemIntegrity");
            }

            // if (!JadeStringUtil.equals(ALCSTiers.MONNAIE_EURO,
            // tauxMonnaieModel
            // .getTypeMonnaie(), false)
            // && !JadeStringUtil.equals(ALCSTiers.MONNAIE_GB,
            // tauxMonnaieModel.getTypeMonnaie(), false)
            // && !JadeStringUtil.equals(ALCSTiers.MONNAIE_DANOISE,
            // tauxMonnaieModel.getTypeMonnaie(), false)
            // && !JadeStringUtil.equals(ALCSTiers.MONNAIE_NORVE,
            // tauxMonnaieModel.getTypeMonnaie(), false)
            // && !JadeStringUtil.equals(ALCSTiers.MONNAIE_SUEDE,
            // tauxMonnaieModel.getTypeMonnaie(), false)
            // && !JadeStringUtil.equals(ALCSTiers.MONNAIE_TCHEQUE,
            // tauxMonnaieModel.getTypeMonnaie(), false)) {
            // JadeThread
            // .logError(
            // TauxMonnaieEtrangereModelChecker.class
            // .getName(),
            // "al.tauxMonnaieEtrangere.tauxMonnaieEtrangereModel.typeTaux.codesystemIntegrity");
            // }
        } catch (Exception e) {
            throw new ALTauxMonnaieEtrangereException(
                    "TauxMonnaieEtrangereModelChecker problem during checking codes system integrity", e);

        }

    }

    /**
     * V�rification de l'int�grit� des donn�es
     * 
     * @param tauxMonnaieModel
     *            Mod�le � valider
     */

    private static void checkDatabaseIntegrity(TauxMonnaieEtrangereModel tauxMonnaieModel) {
        if (ALAbstractChecker.hasError()) {
            return;
        }
        // date de d�but de droit
        if (!JadeDateUtil.isGlobazDateMonthYear(tauxMonnaieModel.getDebutTaux())) {
            JadeThread.logError(TauxMonnaieEtrangereModelChecker.class.getName(),
                    "al.tauxMonnaieEtrangere.tauxMonnaieEtrangereModel.dateDebut.dataBaseIntgrity.type");
        }
        // date de fin du taux
        if (!JadeDateUtil.isGlobazDateMonthYear(tauxMonnaieModel.getFinTaux())) {
            JadeThread.logError(TauxMonnaieEtrangereModelChecker.class.getName(),
                    "al.tauxMonnaieEtrangere.tauxMonnaieEtrangereModel.dateFin.dataBaseIntgrity.type");

        }
        // type de taux
        if (!JadeNumericUtil.isIntegerPositif(tauxMonnaieModel.getTypeMonnaie())) {
            JadeThread.logError(TauxMonnaieEtrangereModelChecker.class.getName(),
                    "al.tauxMonnaieEtrangere.tauxMonnaieEtrangereModel.typeTaux.dataBaseIntgrity.type");
        }
        // valeur du taux
        if (!JadeNumericUtil.isNumericPositif(tauxMonnaieModel.getTauxMonnaie())) {
            JadeThread.logError(TauxMonnaieEtrangereModelChecker.class.getName(),
                    "al.tauxMonnaieEtrangere.tauxMonnaieEtrangereModel.tauxMonnaie.dataBaseIntegrity.type");
        }

    }

    /**
     * Contr�le d'int�grit� des donn�es obligatoires
     * 
     * @param tauxMonnaieModel
     *            mod�le � v�rifier
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    private static void checkMandatory(TauxMonnaieEtrangereModel tauxMonnaieModel) throws JadeApplicationException,
            JadePersistenceException {

        // d�but de la validit� du taux
        if (JadeStringUtil.isEmpty(tauxMonnaieModel.getDebutTaux())) {
            JadeThread.logError(TauxMonnaieEtrangereModelChecker.class.getName(),
                    "al.tauxMonnaieEtrangere.tauxMonnaieEtrangereModel.dateDebut.mandatory");

        }
        // fin de validit� du taux
        if (JadeStringUtil.isEmpty(tauxMonnaieModel.getFinTaux())) {
            JadeThread.logError(TauxMonnaieEtrangereModelChecker.class.getName(),
                    "al.tauxMonnaieEtrangere.tauxMonnaieEtrangereModel.dateFin.mandatory");

        }
        // type de monnaie
        if (JadeStringUtil.isEmpty(tauxMonnaieModel.getTypeMonnaie())) {
            JadeThread.logError(TauxMonnaieEtrangereModelChecker.class.getName(),
                    "al.tauxMonnaieEtrangere.tauxMonnaieEtrangereModel.typeTaux.mandatory");
        }
        // taux de la monnaie
        if (JadeStringUtil.isEmpty(tauxMonnaieModel.getTauxMonnaie())) {
            JadeThread.logError(TauxMonnaieEtrangereModelChecker.class.getName(),
                    "al.tauxMonnaieEtrangere.tauxMonnaieEtrangereModel.tauxMonnaie.mandatory");

        }

    }

    /**
     * Validation des donn�es de tauxMonnnaieModel
     * 
     * @param tauxMonnaieModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(TauxMonnaieEtrangereModel tauxMonnaieModel) throws JadeApplicationException,
            JadePersistenceException {
        TauxMonnaieEtrangereModelChecker.checkMandatory(tauxMonnaieModel);
        TauxMonnaieEtrangereModelChecker.checkDatabaseIntegrity(tauxMonnaieModel);
        TauxMonnaieEtrangereModelChecker.checkCodesystemIntegrity(tauxMonnaieModel);
        TauxMonnaieEtrangereModelChecker.checkBusinessIntegrity(tauxMonnaieModel);

    }

    /**
     * Validation pour la suppression
     * 
     * @param tauxMonnaieModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validateForDelete(TauxMonnaieEtrangereModel tauxMonnaieModel) throws JadeApplicationException,
            JadePersistenceException {
    }

}
