package ch.globaz.vulpecula.domain.comparators;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;

/**
 * @author Arnaud Geiser (AGE) | Créé le 6 janv. 2014
 * 
 */
public class PosteTravailsAlphabetiqueComparatorTest {
    private PosteTravail p1;
    private PosteTravail p2;

    private final PosteTravailsAlphabetiqueComparator comparator = new PosteTravailsAlphabetiqueComparator();
    private final Travailleur travailleur1 = new Travailleur();
    private final Travailleur travailleur2 = new Travailleur();

    @Before
    public void setUp() {
        p1 = new PosteTravail();
        p1.setTravailleur(travailleur1);
        p2 = new PosteTravail();
        p2.setTravailleur(travailleur2);
    }

    @Test
    public void givenNullWhenCompareThen0() {
        p1 = null;
        p2 = null;
        assertEquals(0, comparator.compare(p1, p2));
    }

    @Test
    public void givenEmptyWhenCompareThen0() {
        assertEquals(0, comparator.compare(p1, p2));
    }

    @Test
    public void givenPosteWithDesignationAndEmptyWhenCompareThen1() {
        // p1.getTravailleur().setDesignation1("Geiser");
        // assertEquals(1, comparator.compare(p1, p2));
    }
}
