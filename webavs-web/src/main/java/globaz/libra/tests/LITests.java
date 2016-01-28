package globaz.libra.tests;

import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.libra.application.LIApplication;
import ch.globaz.libra.business.services.LibraServiceLocator;
import ch.globaz.libra.constantes.ILIConstantesExternes;

/**
 * 
 * @author HPE
 * 
 */
public class LITests {

    // Création d'un dossier de base (Test Interface 1 dossier)
    public static void main(String[] args) {

        BISession session;
        try {

            session = GlobazSystem.getApplication(LIApplication.DEFAULT_APPLICATION_LIBRA).newSession("hpe", "hpe");

            // Test création de rappel with test dossier
            LibraServiceLocator.getEcheanceService().createRappelWithTestDossier("31.12.2010", "120", "TEST INTERFACE",
                    "108912", ILIConstantesExternes.CS_DOMAINE_AF, true);

            // // Test Création Dossier : OK
            // transaction = LIServiceLocator.getDossierService().createDossier(
            // (BTransaction) transaction, "112001",
            // ILIConstantes.CS_DOMAINE_PC, "1");

            // // Test Clôture Dossier : OK
            // transaction =
            // LIServiceLocator.getDossierService().clotureDossier((BTransaction)transaction,
            // "1");

            // // Test Réactivation Dossier : OK
            // transaction =
            // LIServiceLocator.getDossierService().reactivationDossier((BTransaction)transaction,
            // "1");

            // // Test création échéance : OK
            // transaction =
            // LIServiceLocator.getEcheanceService().createRappelOnDossierTiers((BTransaction)transaction,
            // JACalendar.todayJJsMMsAAAA(), "1", "Test création échéance",
            // true);

            // // Test journalisation sans remarque : OK
            // transaction =
            // LIServiceLocator.getJournalisationService().createJournalisationSimple((BTransaction)transaction,
            // "1", "Journalisation sans remarque", true);

            // // Test journalisation avec remarque : OK
            // transaction =
            // LIServiceLocator.getJournalisationService().createJournalisationDossierAvecRemarque((BTransaction)transaction,
            // "1", "Journalisation sans remarque", "REMARQUE", true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(-1);
        }
    }

}
