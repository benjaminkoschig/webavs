package globaz.musca.db.gestionJourFerie;

import globaz.globall.db.BSession;
import globaz.musca.application.FAApplication;

public class Tests {

    public static void main(String[] args) {
        Tests.tests();
    }

    public static void testIsWorkDay() {
        System.out.println("\n\n-----------testIsWorkDay-----------\n\n");
        try {
            System.out.println("Is Work Day : "
                    + FAGestionJourFerieUtil.isWorkDay("15.07.2010", FAGestionJourFerieSystemCode.DOMAINE_AI,
                            new BSession(FAApplication.DEFAULT_APPLICATION_MUSCA)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testNextWorkDay() {

        System.out.println("\n\n-----------testNextWorkDay-----------\n\n");
        try {
            System.out.println("Next Work Day : "
                    + FAGestionJourFerieUtil.giveNextWorkDay("15.07.2010", 2,
                            FAGestionJourFerieSystemCode.DOMAINE_CONTENTIEUX, new BSession(
                                    FAApplication.DEFAULT_APPLICATION_MUSCA)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void tests() {
        Tests.testNextWorkDay();
        Tests.testIsWorkDay();
    }

}
