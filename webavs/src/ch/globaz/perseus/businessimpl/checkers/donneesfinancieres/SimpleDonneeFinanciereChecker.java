package ch.globaz.perseus.businessimpl.checkers.donneesfinancieres;

import ch.globaz.perseus.business.models.donneesfinancieres.SimpleDonneeFinanciere;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

public class SimpleDonneeFinanciereChecker extends PerseusAbstractChecker {
    /**
     * @param donneeFinanciere
     */
    public static void checkForCreate(SimpleDonneeFinanciere donneeFinanciere) {
        SimpleDonneeFinanciereChecker.checkMandatory(donneeFinanciere);
    }

    /**
     * @param donneeFinanciere
     */
    public static void checkForDelete(SimpleDonneeFinanciere donneeFinanciere) {

    }

    /**
     * @param donneeFinanciere
     */
    public static void checkForUpdate(SimpleDonneeFinanciere donneeFinanciere) {
        SimpleDonneeFinanciereChecker.checkMandatory(donneeFinanciere);
    }

    /**
     * @param donneeFinanciere
     */
    private static void checkMandatory(SimpleDonneeFinanciere donneeFinanciere) {

    }

}
