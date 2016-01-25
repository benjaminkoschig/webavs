package ch.globaz.perseus.businessimpl.checkers.variablemetier;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.perseus.business.models.variablemetier.SimpleVariableMetier;
import ch.globaz.perseus.business.models.variablemetier.SimpleVariableMetierSearch;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class SimpleVariableMetierChecker extends PerseusAbstractChecker {

    private static void checkAllNumericValueIsNotEmpty(SimpleVariableMetier simpleVariableMetier) {
        if (JadeStringUtil.isEmpty(simpleVariableMetier.getTaux())
                && JadeStringUtil.isEmpty(simpleVariableMetier.getMontant())
                && SimpleVariableMetierChecker.fractionIsEmpty(simpleVariableMetier)) {
            JadeThread.logError(simpleVariableMetier.getClass().getName(),
                    "perseus.simpleVariableMetier.vleurnumerique.mandatory");
        }
    }

    public static void checkForCreate(SimpleVariableMetier simpleVariableMetier) throws VariableMetierException,
            JadePersistenceException {
        SimpleVariableMetierChecker.checkMandatory(simpleVariableMetier);
        SimpleVariableMetierChecker.checkIntegrity(simpleVariableMetier);
    }

    public static void checkForDelete(SimpleVariableMetier simpleVariableMetier) throws VariableMetierException,
            JadePersistenceException {
        // TODO Auto-generated method stub

    }

    public static void checkForUpdate(SimpleVariableMetier simpleVariableMetier) throws VariableMetierException,
            JadePersistenceException {
        SimpleVariableMetierChecker.checkMandatory(simpleVariableMetier);
        SimpleVariableMetierChecker.checkIntegrity(simpleVariableMetier);
    }

    private static void checkFraction(SimpleVariableMetier simpleVariableMetier) {
        if ("0".equals(simpleVariableMetier.getFractionDenominateur())) {
            JadeThread.logError(simpleVariableMetier.getClass().getName(),
                    "perseus.simpleVariableMetier.vleurnumerique.div0");
        }
    }

    private static void checkIntegrity(SimpleVariableMetier simpleVariableMetier) throws VariableMetierException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        if (JadeDateUtil.isDateMonthYearAfter(simpleVariableMetier.getDateDebut(), simpleVariableMetier.getDateFin())) {
            JadeThread.logError(simpleVariableMetier.getClass().getName(),
                    "perseus.simpleVariableMetier.dateDebutPlusPetite.integrity");
        }
        simpleVariableMetier = SimpleVariableMetierChecker.checkNumericValueAndSetNull(simpleVariableMetier);

        try {
            if (!PerseusAbstractChecker.threadOnError()) {
                SimpleVariableMetierChecker.checkPeriodeSuperieur(simpleVariableMetier);
                SimpleVariableMetierChecker.checkSuperPositionPeriode(simpleVariableMetier);
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new VariableMetierException("Unable to check variableMetierPeriode", e);
        }
    }

    private static void checkMandatory(SimpleVariableMetier simpleVariableMetier) {

        // Test le champ date de début
        if (JadeStringUtil.isEmpty(simpleVariableMetier.getDateDebut())) {
            JadeThread.logError(simpleVariableMetier.getClass().getName(),
                    "perseus.simpleVariableMetier.datedebut.mandatory");
        }

        // Test le champ Variable métier
        if (JadeStringUtil.isEmpty(simpleVariableMetier.getCsTypeVariableMetier())) {
            JadeThread.logError(simpleVariableMetier.getClass().getName(),
                    "perseus.simpleVariableMetier.csTypeVariableMetier.mandatory");
        }

        SimpleVariableMetierChecker.checkAllNumericValueIsNotEmpty(simpleVariableMetier);

    }

    private static SimpleVariableMetier checkNumericValueAndSetNull(SimpleVariableMetier variableMetier) {

        if (SimpleVariableMetierChecker.tauxIsValid(variableMetier)) {
            variableMetier.setMontant(null);
            variableMetier.setFractionDenominateur(null);
            variableMetier.setFractionNumerateur(null);
        } else if (SimpleVariableMetierChecker.montantIsValid(variableMetier)) {
            variableMetier.setTaux(null);
            variableMetier.setFractionDenominateur(null);
            variableMetier.setFractionNumerateur(null);

        } else if (SimpleVariableMetierChecker.fractionIsValid(variableMetier)) {
            variableMetier.setTaux(null);
            variableMetier.setMontant(null);
        } else if (SimpleVariableMetierChecker.hasManyNumericValue(variableMetier)) {

            JadeThread.logError(variableMetier.getClass().getName(),
                    "perseus.simpleVariableMetier.vleurnumeriquePlusieurs.integrity");
        }
        return variableMetier;
    }

    private static void checkPeriodeSuperieur(SimpleVariableMetier simpleVariableMetier)
            throws VariableMetierException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeNoBusinessLogSessionError {
        SimpleVariableMetierSearch variableMetierSearch = SimpleVariableMetierChecker
                .convertToSearchModel(simpleVariableMetier);
        if (JadeStringUtil.isEmpty(simpleVariableMetier.getDateFin())) {
            variableMetierSearch.setWhereKey("checkDateDebutValabe");
            if (PerseusImplServiceLocator.getSimpleVariableMetierService().count(variableMetierSearch) > 0) {
                JadeThread.logError(simpleVariableMetier.getClass().getName(),
                        "perseus.simpleVariableMetier.periodeOuverte.integrity");

            }
        }
    }

    private static void checkSuperPositionPeriode(SimpleVariableMetier simpleVariableMetier)
            throws VariableMetierException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeNoBusinessLogSessionError {
        SimpleVariableMetierSearch variableMetierSearch = SimpleVariableMetierChecker
                .convertToSearchModel(simpleVariableMetier);
        variableMetierSearch.setWhereKey("checkSuperpositionDates");
        if (PerseusImplServiceLocator.getSimpleVariableMetierService().count(variableMetierSearch) > 0) {
            JadeThread.logError(simpleVariableMetier.getClass().getName(),
                    "perseus.simpleVariableMetier.superpositionDates.integrity");
        }
    }

    private static SimpleVariableMetierSearch convertToSearchModel(SimpleVariableMetier simpleVariableMetier) {
        SimpleVariableMetierSearch variableMetierSearch = new SimpleVariableMetierSearch();
        variableMetierSearch.setForDateDebutCheckPeriode(simpleVariableMetier.getDateDebut());
        variableMetierSearch.setForDateFinCheckPeriode(simpleVariableMetier.getDateFin());
        variableMetierSearch.setForIdVariableMetier(simpleVariableMetier.getIdVariableMetier());
        variableMetierSearch.setForforCsTypeVariableMetier(simpleVariableMetier.getCsTypeVariableMetier());
        return variableMetierSearch;
    }

    private static boolean fractionIsEmpty(SimpleVariableMetier simpleVariableMetier) {
        boolean retour = false;
        if (JadeStringUtil.isEmpty(simpleVariableMetier.getFractionDenominateur())
                && JadeStringUtil.isEmpty(simpleVariableMetier.getFractionNumerateur())) {
            retour = true;
        }
        return retour;
    }

    private static boolean fractionIsValid(SimpleVariableMetier simpleVariableMetier) {
        boolean retour = false;
        boolean denominateur = false;
        boolean numerateur = false;
        if (JadeStringUtil.isEmpty(simpleVariableMetier.getTaux())
                && JadeStringUtil.isEmpty(simpleVariableMetier.getMontant())
                && !SimpleVariableMetierChecker.fractionIsEmpty(simpleVariableMetier)) {
            SimpleVariableMetierChecker.checkFraction(simpleVariableMetier);
            retour = true;
        }
        if (retour) {
            if (!JadeStringUtil.isEmpty(simpleVariableMetier.getFractionDenominateur())) {
                denominateur = true;
            }
            if (!JadeStringUtil.isEmpty(simpleVariableMetier.getFractionNumerateur())) {
                numerateur = true;
            }
            if ((!denominateur && numerateur) || (denominateur && !numerateur)) {
                JadeThread.logError(simpleVariableMetier.getClass().getName(),
                        "perseus.simpleVariableMetier.fractionNonValable.integrity");
            }
        }
        return retour;
    }

    private static boolean hasManyNumericValue(SimpleVariableMetier variableMetier) {
        int count = 0;
        if (!JadeStringUtil.isEmpty(variableMetier.getFractionDenominateur())
                || !JadeStringUtil.isEmpty(variableMetier.getFractionNumerateur())) {
            count++;

        }
        if (!JadeStringUtil.isEmpty(variableMetier.getTaux())) {
            count++;
        }
        if (!JadeStringUtil.isEmpty(variableMetier.getMontant())) {
            count++;

        }
        return count > 1;
    }

    private static boolean montantIsValid(SimpleVariableMetier simpleVariableMetier) {
        boolean retour = false;
        if (JadeStringUtil.isEmpty(simpleVariableMetier.getTaux())
                && SimpleVariableMetierChecker.fractionIsEmpty(simpleVariableMetier)
                && !JadeStringUtil.isEmpty(simpleVariableMetier.getMontant())) {
            retour = true;
        }
        return retour;
    }

    private static boolean tauxIsValid(SimpleVariableMetier simpleVariableMetier) {
        boolean retour = false;
        if (JadeStringUtil.isEmpty(simpleVariableMetier.getMontant())
                && SimpleVariableMetierChecker.fractionIsEmpty(simpleVariableMetier)
                && !JadeStringUtil.isEmpty(simpleVariableMetier.getTaux())) {
            retour = true;
        }
        return retour;
    }
}
