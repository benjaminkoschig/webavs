package globaz.phenix.process;

import static org.fest.assertions.api.Assertions.*;
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
}
