package globaz.phenix.process.communications;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JAException;
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

    @Test
    public void testGetFranchiseRealConditionFranchise() throws JAException {
        float montant = franchise(12, 1400f, "12.06.2013", "01.08.2013", "31.12.2013");

        assertTrue(montant == 8400f);
    }

    @Test
    public void testGetFranchiseOtherConditionFranchise() throws JAException {
        float montant = franchise(8, 1400f, "12.06.2013", "01.08.2013", "31.12.2013");

        assertTrue(montant == 8400f);
    }

    @Test
    public void testGetFranchiseOtherConditionFranchise2() throws JAException {
        float montant = franchise(12, 1400f, "12.10.2013", "01.08.2013", "31.12.2013");

        assertTrue(montant == 2800f);
    }

    /**
     * NE TOUCHER QUE SI CODE CHANGER DANS CPProcessReceptionGenererDecision.getFranchise()
     * 
     * @param nombreMoisTotalDecision
     * @param pFranchise
     * @param pDateAvs
     * @param pDebutDecision
     * @param pFinDecision
     * @return
     * @throws JAException
     */
    private float franchise(int nombreMoisTotalDecision, float pFranchise, String pDateAvs, String pDebutDecision,
            String pFinDecision) throws JAException {
        float franchise = pFranchise;
        int nombre = nombreMoisTotalDecision;
        final String dateAvs = pDateAvs;
        final int anneeAvs = JACalendar.getYear(dateAvs);
        final String debutDecision = pDebutDecision;
        final String finDecision = pFinDecision;
        final String anneeDecision = String.valueOf(JACalendar.getYear(debutDecision));

        int moisDebut = JACalendar.getMonth(debutDecision);
        int moisFin = JACalendar.getMonth(finDecision);

        int varNum = nombre;
        if (varNum != 0) {
            // Recaler la date de début et de fin par rapport à la
            // période totale
            int vNum = (moisDebut + varNum) - 1;
            if (vNum <= 12) { // décalage du mois de fin
                moisFin = vNum;
            } else { // Décalage du mois de début
                vNum = (moisFin - varNum) + 1;
                if (vNum >= 1) {
                    moisDebut = vNum;
                } else { // Période ne tenant pas dans la décision
                    moisDebut = 1;
                    moisFin = varNum;
                }
            }
            // Nouveau code pour corriger la problématique des rentiers (K160704_001)
            if (anneeDecision.equalsIgnoreCase(Integer.toString(anneeAvs))
                    && moisDebut < (JACalendar.getMonth(dateAvs) + 1)) {
                moisDebut = JACalendar.getMonth(dateAvs) + 1;
            }
        }

        if (anneeDecision.equalsIgnoreCase(Integer.toString(anneeAvs))
                && new JACalendarGregorian().compare(debutDecision, dateAvs) == JACalendar.COMPARE_FIRSTLOWER) {
            moisDebut = JACalendar.getMonth(dateAvs) + 1;
        }

        return franchise * ((moisFin - moisDebut) + 1);
    }
}
