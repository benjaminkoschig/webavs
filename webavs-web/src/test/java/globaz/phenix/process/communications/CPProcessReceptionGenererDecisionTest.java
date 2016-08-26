package globaz.phenix.process.communications;

import static org.fest.assertions.api.Assertions.*;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.phenix.db.communications.CPSedexDonneesCommerciales;
import globaz.phenix.db.principale.CPDecisionViewBean;
import org.junit.Test;

public class CPProcessReceptionGenererDecisionTest {

    private AFAffiliation chargeDataAffiliation(String dateDebut, String dateFin) {

        AFAffiliation affiliation = new AFAffiliation();
        affiliation.setDateDebut(dateDebut);
        affiliation.setDateFin(dateFin);
        affiliation.populateSpy("20160825133251");

        return affiliation;
    }

    private CPSedexDonneesCommerciales chargeDataDonneeCommerciale(String dateDebut, String dateFin) {

        CPSedexDonneesCommerciales doco = new CPSedexDonneesCommerciales();
        doco.setCommencementOfSelfEmployment(dateDebut);
        doco.setEndOfSelfEmployment(dateFin);
        doco.populateSpy("20160825133251");

        return doco;
    }

    @Test
    public void testDefineDateExerciceCPDecisionViewBeanStringCPSedexDonneesCommercialesAFAffiliation_AvecDoco()
            throws Exception {

        String dateAffDebut = "20140101";
        String dateAffFin = "";

        String dateDocoDebut = "2015-01-01";
        String dateDocoFin = "2015-12-31";

        CPProcessReceptionGenererDecision process = new CPProcessReceptionGenererDecision();
        CPDecisionViewBean newDecision = new CPDecisionViewBean();

        process.defineDateExercice1(newDecision, "2015", chargeDataDonneeCommerciale(dateDocoDebut, dateDocoFin),
                chargeDataAffiliation(dateAffDebut, dateAffFin));

        assertThat(newDecision.getDebutExercice1()).contains("01.01.2015");
        assertThat(newDecision.getFinExercice1()).contains("31.12.2015");
    }

    @Test
    public void testDefineDateExerciceCPDecisionViewBeanStringCPSedexDonneesCommercialesAFAffiliation_SansDoco()
            throws Exception {

        String dateAffDebut = "20140101";
        String dateAffFin = "";

        String dateDocoDebut = "";
        String dateDocoFin = "";

        CPProcessReceptionGenererDecision process = new CPProcessReceptionGenererDecision();
        CPDecisionViewBean newDecision = new CPDecisionViewBean();

        process.defineDateExercice1(newDecision, "2015", chargeDataDonneeCommerciale(dateDocoDebut, dateDocoFin),
                chargeDataAffiliation(dateAffDebut, dateAffFin));

        assertThat(newDecision.getDebutExercice1()).contains("01.01.2015");
        assertThat(newDecision.getFinExercice1()).contains("31.12.2015");
    }

    @Test
    public void testDefineDateExerciceCPDecisionViewBeanStringCPSedexDonneesCommercialesAFAffiliation_SansDocoAffDebut()
            throws Exception {

        String dateAffDebut = "20150401";
        String dateAffFin = "";

        String dateDocoDebut = "";
        String dateDocoFin = "";

        CPProcessReceptionGenererDecision process = new CPProcessReceptionGenererDecision();
        CPDecisionViewBean newDecision = new CPDecisionViewBean();

        process.defineDateExercice1(newDecision, "2015", chargeDataDonneeCommerciale(dateDocoDebut, dateDocoFin),
                chargeDataAffiliation(dateAffDebut, dateAffFin));

        assertThat(newDecision.getDebutExercice1()).contains("01.04.2015");
        assertThat(newDecision.getFinExercice1()).contains("31.12.2015");
    }

    @Test
    public void testDefineDateExerciceCPDecisionViewBeanStringCPSedexDonneesCommercialesAFAffiliation_SansDocoAffFin()
            throws Exception {

        String dateAffDebut = "20140401";
        String dateAffFin = "20150531";

        String dateDocoDebut = "";
        String dateDocoFin = "";

        CPProcessReceptionGenererDecision process = new CPProcessReceptionGenererDecision();
        CPDecisionViewBean newDecision = new CPDecisionViewBean();

        process.defineDateExercice1(newDecision, "2015", chargeDataDonneeCommerciale(dateDocoDebut, dateDocoFin),
                chargeDataAffiliation(dateAffDebut, dateAffFin));

        assertThat(newDecision.getDebutExercice1()).contains("01.01.2015");
        assertThat(newDecision.getFinExercice1()).contains("31.05.2015");
    }
}
