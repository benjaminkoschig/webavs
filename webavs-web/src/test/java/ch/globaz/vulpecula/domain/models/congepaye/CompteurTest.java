package ch.globaz.vulpecula.domain.models.congepaye;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

public class CompteurTest {
    private Compteur compteur;
    private CongePaye congePaye = mock(CongePaye.class);

    @Before
    public void setUp() {
        compteur = new Compteur();
    }

    @Test
    public void getAnneeAsValue_GivenEmptyCompteur_ShouldBe0() {
        assertThat(compteur.getAnneeAsValue(), is(0));
    }

    @Test
    public void getAnneeAsValue_GivenAnnee2000_ShouldBe2000() {
        compteur.setAnnee(new Annee(2000));
        assertThat(compteur.getAnneeAsValue(), is(2000));
    }

    @Test
    public void getIdPosteTravail_GivenEmptyCompteur_ShouldBeNull() {
        assertThat(compteur.getIdPosteTravail(), is(nullValue()));
    }

    @Test
    public void getIdPosteTravail_GivenPosteTravailWithIdOf10_ShouldBe10() {
        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setId("10");
        compteur.setPosteTravail(posteTravail);

        assertThat(compteur.getIdPosteTravail(), is("10"));
    }

    public void isFull_GivenEmptyCompteur_ShouldBeTrue() {
        assertThat(compteur.isFull(), is(true));
    }

    @Test
    public void isFull_GivenCompteurWithCumulMontant2000AndMontantRestantOf0_ShouldBeFalse() {
        init(2000, 0);

        assertThat(compteur.isFull(), is(true));
    }

    @Test
    public void isFull_GivenCompteurWithCumulMontant2000AndMontantRestantOf2100_ShouldBeFalse() {
        init(2000, 500);

        assertThat(compteur.isFull(), is(false));
    }

    public void absorbe_GivenEmptyCompteur_ShouldNotAbsorbeAnything() {
        Montant montantAAbsorber = compteur.absorbe(congePaye, new Montant(1000));

        assertThat(montantAAbsorber, is(new Montant(1000)));
        assertThat(compteur.getCumulCotisation(), is(new Montant(0)));
        assertThat(compteur.getMontantRestant(), is(new Montant(0)));
        assertThat(compteur.getMontantVerse(), is(new Montant(0)));
    }

    @Test
    public void absorbe500_GivenCompteurWithCumulMontant2000AndMontantRestantOf1000_ShouldReturn0() {
        init(2000, 1000);

        Montant montantAAbsorber = compteur.absorbe(congePaye, new Montant(500));

        assertThat(montantAAbsorber, is(Montant.ZERO));
        assertThat(compteur.getCumulCotisation(), is(new Montant(2000)));
        assertThat(compteur.getMontantRestant(), is(new Montant(500)));
        assertThat(compteur.getMontantVerse(), is(new Montant(500)));
    }

    @Test
    public void absorbe1000_GivenCompteurWithCumulCotisation2000AndMontantRestantOf500_ShouldReturn500() {
        init(2000, 500);

        Montant montantAAbsorber = compteur.absorbe(congePaye, new Montant(1000));

        assertThat(montantAAbsorber, is(new Montant(500)));
        assertThat(compteur.getCumulCotisation(), is(new Montant(2000)));
        assertThat(compteur.getMontantRestant(), is(new Montant(0)));
        assertThat(compteur.getMontantVerse(), is(new Montant(500)));
    }

    @Test
    public void absorbe1000_GivenEmptyCompteur_ShouldBeOk() {
        compteur.forceAbsorbe(congePaye, new Montant(1000));

        assertThat(compteur.getCumulCotisation(), is(new Montant(0)));
        assertThat(compteur.getMontantRestant(), is(new Montant(-1000)));
        assertThat(compteur.getMontantVerse(), is(new Montant(1000)));
    }

    @Test
    public void absorbe1000_givenCompteurWithCumulCotisation2000AndMontantRestantOf500_ShouldBeOk() {
        init(2000, 500);

        compteur.forceAbsorbe(congePaye, new Montant(1000));

        assertThat(compteur.getCumulCotisation(), is(new Montant(2000)));
        assertThat(compteur.getMontantRestant(), is(new Montant(-500)));
        assertThat(compteur.getMontantVerse(), is(new Montant(1000)));
    }

    @Test
    public void getMontantVerse_givenEmptyDecompte_ShouldBe0() {
        assertThat(compteur.getMontantVerse(), is(new Montant(0)));
    }

    @Test
    public void addLigne_givenEmptyDecompte_ShouldBeOk() {
        CongePaye congePaye = mock(CongePaye.class);
        compteur.addLigne(congePaye, new Montant(1000));

        assertThat(compteur.getMontantVerse(), is(new Montant(1000)));
    }

    private void init(int cumulCotisation, int montantRestant) {
        compteur.setCumulCotisation(new Montant(cumulCotisation));
        compteur.setMontantRestant(new Montant(montantRestant));
    }
}
