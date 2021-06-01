package globaz.phenix.process;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JAException;
import org.junit.Test;

public class CPProcessCalculCotisationTest {

    @Test
    public void testDetermineRevenuMaxSuivantNombreDeMois() throws Exception {
        CPProcessCalculCotisation process = new CPProcessCalculCotisation();

        // Mois renseigné
        assertThat(process.determineRevenuMaxSuivantNombreDeMois("126'000", 45000, "12")).isEqualTo(126000);
        assertThat(process.determineRevenuMaxSuivantNombreDeMois("126'000", 45000, "6")).isEqualTo(63000);
        assertThat(process.determineRevenuMaxSuivantNombreDeMois("126000", 45000, "3")).isEqualTo(31500);

        // Pas de mois renseigné
        assertThat(process.determineRevenuMaxSuivantNombreDeMois("126'000", 45000, "")).isEqualTo(45000);
        assertThat(process.determineRevenuMaxSuivantNombreDeMois("126'000", 45000, "0")).isEqualTo(45000);
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
     * NE TOUCHER QUE SI CODE CHANGER DANS CPProcessCalculCotisation.getFranchise()
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
