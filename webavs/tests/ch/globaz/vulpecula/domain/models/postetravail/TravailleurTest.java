package ch.globaz.vulpecula.domain.models.postetravail;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;

public class TravailleurTest {

    private Travailleur travailleur;

    private Date dateForTest = new Date("01.04.2013");

    @Before
    public void setUp() {
        travailleur = new Travailleur();
    }

    @Test
    public void givenTravailleurWithoutPosteTravailWhenGetPostesActifsThenReturnEmptyList() {
        Assert.assertTrue(getPostesTravailActifsForDate01042013().isEmpty());
    }

    @Test
    public void givenTravailleurWithPosteTravailActifWhenGetPostesActifsThenReturnOneElement() {
        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setPeriodeActivite(new Periode("01.01.2013", "01.01.2014"));

        addPosteTravail(posteTravail);

        Assert.assertEquals(getPostesTravailActifsForDate01042013().size(), 1);
        Assert.assertThat(getPostesTravailActifsForDate01042013(), Matchers.hasItems(posteTravail));
    }

    @Test
    public void givenTravailleurWithoutPosteTravailInactifWhenGetPostesActifsThenReturnZeroElement() {
        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setPeriodeActivite(new Periode("01.01.2013", "01.03.2013"));

        addPosteTravail(posteTravail);

        Assert.assertTrue(getPostesTravailActifsForDate01042013().isEmpty());
    }

    @Test
    public void getAge_GivenDateNaissanceTravailleurIn1983AndDateReferenceIn2014_ShouldReturn31() {
        travailleur.setDateNaissance("03.06.1983");
        int ageDuCapitaine = travailleur.getAge(new Date("01.10.2014"));

        assertThat(ageDuCapitaine, is(31));
    }

    @Test
    public void hasMoreThan18Ans_GivenDateNaissanceOf01012000AndDateReferenceOf01012014_ShouldBeFalse() {
        travailleur.setDateNaissance("01.01.2000");
        boolean hasMoreThan18Ans = travailleur.hasMoreThan18Ans(new Date("01.01.2014"));

        assertFalse(hasMoreThan18Ans);
    }

    @Test
    public void hasMoreThan18Ans_GivenDateNaissanceOf01011990AndDateReferenceOf01012014_ShouldBeFalse() {
        travailleur.setDateNaissance("01.01.1990");
        boolean hasMoreThan18Ans = travailleur.hasMoreThan18Ans(new Date("01.01.2014"));

        assertTrue(hasMoreThan18Ans);
    }

    private void addPosteTravail(PosteTravail posteTravail) {
        travailleur.addPosteTravail(posteTravail);
    }

    private List<PosteTravail> getPostesTravailActifsForDate01042013() {
        return travailleur.getPostesTravailActifs(dateForTest);
    }
}
