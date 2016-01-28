package ch.globaz.al.businessimpl.checker.model.adi;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.adi.AdiSaisieModel;
import ch.globaz.al.business.models.adi.AdiSaisieSearchModel;
import ch.globaz.al.business.models.droit.EnfantSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.checker.model.allocataire.AllocataireModelChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Classe de validation du modèle AdiSaisieModel
 * 
 * @author GMO
 * 
 */
public class AdiSaisieModelChecker extends ALAbstractChecker {
    /**
     * vérification des règles métier
     * 
     * @param adiSaisieModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkBusinessIntegrity(AdiSaisieModel adiSaisieModel) throws JadePersistenceException,
            JadeApplicationException {

        // intégrité de la date du début (antérieur) et de la date de
        // fin(postérieur)

        if (JadeDateUtil.isDateMonthYearAfter(adiSaisieModel.getPeriodeDe(), adiSaisieModel.getPeriodeA())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.adiSaisieModel.dateDebutDateFin.businessIntegrity");
        }

        // vérification de l'enfant lié à la saisie
        if (!JadeNumericUtil.isEmptyOrZero(adiSaisieModel.getIdEnfant())) {
            EnfantSearchModel searchModel = new EnfantSearchModel();
            searchModel.setForIdEnfant(adiSaisieModel.getIdEnfant());
            if (ALImplServiceLocator.getEnfantModelService().count(searchModel) == 0) {
                JadeThread.logError(AdiSaisieModelChecker.class.getName(),
                        "al.adi.adiSaisieModel.idEnfant.businessIntegrity.existingId");
            }
        }
        // Vérification si saisie déjà existante
        AdiSaisieSearchModel searchSaisie = new AdiSaisieSearchModel();
        searchSaisie.setForIdDecompteAdi(adiSaisieModel.getIdDecompteAdi());
        searchSaisie.setForIdEnfant(adiSaisieModel.getIdEnfant());
        searchSaisie.setWhereKey("saisieExistante");
        searchSaisie.setForPeriodeMin(adiSaisieModel.getPeriodeDe());
        searchSaisie.setForPeriodeMax(adiSaisieModel.getPeriodeA());

        searchSaisie = ALServiceLocator.getAdiSaisieModelService().search(searchSaisie);
        if (searchSaisie.getSize() > 0) {
            JadeThread.logError(AdiSaisieModelChecker.class.getName(),
                    "al.adi.adiSaisieModel.warning.businessIntegrity.saisieExistante");
        }
    }

    /**
     * vérification de l'intégrité des données relatives à la base
     * 
     * @param adiSaisieModel
     *            Modèle à valider
     */
    private static void checkDatabaseIntegrity(AdiSaisieModel adiSaisieModel) {

        // validité période début
        if (!JadeDateUtil.isGlobazDateMonthYear(adiSaisieModel.getPeriodeDe())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.adiSaisieModel.periodeDebut.databaseIntegrity");
        }

        // validité période fin
        if (!JadeDateUtil.isGlobazDateMonthYear(adiSaisieModel.getPeriodeA())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.adiSaisieModel.AnneeDecompte.databaseIntegrity");
        }

        // id du décompte adi
        if (!JadeNumericUtil.isIntegerPositif(adiSaisieModel.getIdDecompteAdi())) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.adi.adiSaisieModel.idDecompteAdi.databaseIntegrity");
        }

        // id du décompte adi
        if (!JadeNumericUtil.isEmptyOrZero(adiSaisieModel.getIdEnfant())
                && !JadeNumericUtil.isIntegerPositif(adiSaisieModel.getIdEnfant())) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.adi.adiSaisieModel.idEnfant.databaseIntegrity");
        }

        // montant saisi
        if (!JadeNumericUtil.isNumeric(adiSaisieModel.getMontantSaisi())) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.adi.adiSaisieModel.montantSaisie.databaseIntegrity");
        }

    }

    /**
     * vérification des paramètres requis
     * 
     * @param adiSaisieModel
     *            Modèle à valider
     */
    private static void checkMandatory(AdiSaisieModel adiSaisieModel) {

        // période du début du décompte obligatoire
        if (JadeStringUtil.isEmpty(adiSaisieModel.getPeriodeDe())) {
            JadeThread
                    .logError(DecompteAdiModelChecker.class.getName(), "al.adi.adiSaisieModel.PeriodeDebut.mandatory");
        }
        // période de fin du décompte obligatoire
        if (JadeStringUtil.isEmpty(adiSaisieModel.getPeriodeA())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(), "al.adi.adiSaisieModel.PeriodeFin.mandatory");
        }

        if (JadeStringUtil.isEmpty(adiSaisieModel.getIdDecompteAdi())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.adiSaisieModel.idDecompteAdi.mandatory");
        }

    }

    /**
     * validation de l'intégrité des données et des règles métier, de l'obligation des données
     * 
     * @param saisieModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validate(AdiSaisieModel saisieModel) throws JadePersistenceException, JadeApplicationException {
        AdiSaisieModelChecker.checkMandatory(saisieModel);
        AdiSaisieModelChecker.checkDatabaseIntegrity(saisieModel);
        AdiSaisieModelChecker.checkBusinessIntegrity(saisieModel);
    }

}
