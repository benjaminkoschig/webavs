package ch.globaz.perseus.businessimpl.checkers.donneesfinancieres;

import ch.globaz.perseus.business.models.donneesfinancieres.SimpleDonneeFinanciereSpecialisation;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

public class SimpleDonneeFinanciereSpecialisationChecker extends PerseusAbstractChecker {
    /**
     * @param donneeFinanciere
     */
    public static void checkForCreate(SimpleDonneeFinanciereSpecialisation donneeFinanciereSpecialisation) {
        SimpleDonneeFinanciereSpecialisationChecker.checkMandatory(donneeFinanciereSpecialisation);
    }

    /**
     * @param donneeFinanciere
     */
    public static void checkForDelete(SimpleDonneeFinanciereSpecialisation donneeFinanciereSpecialisation) {

    }

    /**
     * @param donneeFinanciere
     */
    public static void checkForUpdate(SimpleDonneeFinanciereSpecialisation donneeFinanciereSpecialisation) {
        SimpleDonneeFinanciereSpecialisationChecker.checkMandatory(donneeFinanciereSpecialisation);
    }

    /**
     * @param donneeFinanciere
     */
    private static void checkMandatory(SimpleDonneeFinanciereSpecialisation donneeFinanciereSpecialisation) {

    }

}
