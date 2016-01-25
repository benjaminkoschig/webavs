package ch.globaz.vulpecula.external.models.affiliation;

import static org.junit.Assert.*;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;

public class CotisationTest {
    private static final String ALLEMAND = "Allemand";
    private static final String ITALIEN = "Italien";
    private static final String FRANCAIS = "Français";

    private Cotisation cotisation;

    @Before
    public void setUp() {
        cotisation = new Cotisation();
    }

    @Test
    public void givenFrenchLocaleWhenGetAssuranceLibelleShouldReturnFrancais() {
        Locale locale = new Locale("fr");
        initDefaultAssurance();

        String actual = cotisation.getAssuranceLibelle(locale);

        assertEquals(FRANCAIS, actual);
    }

    @Test
    public void givenNullAsLocaleWhenGetAssuranceLibelleShouldReturnFrancais() {
        initDefaultAssurance();

        String actual = cotisation.getAssuranceLibelle(null);

        assertEquals(FRANCAIS, actual);
    }

    @Test
    public void givenGermanLocaleWhenGetAssuranceLibelleShouldReturnAllemand() {
        Locale locale = new Locale("de");
        initDefaultAssurance();

        String actual = cotisation.getAssuranceLibelle(locale);

        assertEquals(ALLEMAND, actual);
    }

    @Test
    public void givenItalienLocaleWhenGetAssuranceLibelleShouldReturnItalien() {
        Locale locale = new Locale("it");
        initDefaultAssurance();

        String actual = cotisation.getAssuranceLibelle(locale);

        assertEquals(ITALIEN, actual);
    }

    public void initDefaultAssurance() {
        Assurance assurance = new Assurance();
        assurance.setLibelleFr(FRANCAIS);
        assurance.setLibelleAl(ALLEMAND);
        assurance.setLibelleIt(ITALIEN);

        cotisation.setAssurance(assurance);
    }
}
