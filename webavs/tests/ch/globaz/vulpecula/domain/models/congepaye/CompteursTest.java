package ch.globaz.vulpecula.domain.models.congepaye;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;

public class CompteursTest {
    private Compteurs compteurs;
    private CongePaye congePaye;

    @Before
    public void setUp() {
        congePaye = mock(CongePaye.class);
        compteurs = new Compteurs(new ArrayList<Compteur>());
    }

    @Test
    public void add_CongePayeOf0_ShouldPass() {
        when(congePaye.getTotalSalaire()).thenReturn(new Montant(0));
        compteurs.add(congePaye);

        for (Compteur compteur : compteurs.getCompteurs()) {
            assertThat(compteur.getMontantVerse(), is(new Montant(0)));
        }
    }

    @Test
    public void add_CongePayeOf1500_ShouldBeOk() {
        initCompteurs(new int[] { 2000, 2000 }, new int[] { 2000, 2000 });
        when(congePaye.getTotalSalaire()).thenReturn(new Montant(1500));
        compteurs.add(congePaye);

        assertThat(getCompteur(0).getMontantVerse(), is(new Montant(1500)));
        assertThat(getCompteur(1).getMontantVerse(), is(new Montant(0)));
    }

    @Test
    public void add_CongePayeOf1500_OnTwoDifferentsCompteurs_ShouldBeOk() {
        initCompteurs(new int[] { 2000, 1000 }, new int[] { 2000, 2000 });

        when(congePaye.getTotalSalaire()).thenReturn(new Montant(1500));
        compteurs.add(congePaye);

        assertThat(getCompteur(0).getMontantVerse(), is(new Montant(1000)));
        assertThat(getCompteur(1).getMontantVerse(), is(new Montant(500)));
    }

    @Test
    public void add_CongePayeOf1500_OnOneFullCompteur_ShouldBeOk() {
        initCompteurs(new int[] { 2000, 0 });

        when(congePaye.getTotalSalaire()).thenReturn(new Montant(1500));
        compteurs.add(congePaye);

        assertThat(getCompteur(0).getMontantVerse(), is(new Montant(1500)));
    }

    @Test
    public void add_CongePayeOf200_OnCompteurOf1000_ShouldCreateOneRowAndMontant200() {
        initCompteurs(new int[] { 1000, 1000 });

        when(congePaye.getTotalSalaire()).thenReturn(new Montant(200));
        compteurs.add(congePaye);
        assertEquals(1, getCompteur(0).getLignes().size());
        assertThat(getCompteur(0).getMontantVerse(), is(new Montant(200)));
        assertThat(getCompteur(0).getMontantRestant(), is(new Montant(800)));
    }

    @Test
    public void add_CongePayeOf200_OnCompteurOf0_ShouldReduceCompteurToMinus2000() {
        initCompteurs(new int[] { 0, 0 });

        when(congePaye.getTotalSalaire()).thenReturn(new Montant(2000));
        compteurs.add(congePaye);

        assertEquals(1, getCompteur(0).getLignes().size());
        assertThat(getCompteur(0).getMontantVerse(), is(new Montant(2000)));
        assertThat(getCompteur(0).getMontantRestant(), is(new Montant(-2000)));
    }

    @Test
    public void hasCompteursActifs_GivenEmptyCompteurs_ShouldBeFalse() {
        assertThat(compteurs.hasCompteursActifs(), is(false));
    }

    @Test
    public void hasCompteursActifs_GivenOneCompteurActif_ShouldBeTrue() {
        compteurs.add(createCompteur(2000, 1000));
        assertThat(compteurs.hasCompteursActifs(), is(true));
    }

    @Test
    public void hasCompteursActifs_GivenOnFullCompteur_ShouldBeFalse() {
        compteurs.add(createCompteur(2000, 0));
        assertThat(compteurs.hasCompteursActifs(), is(false));
    }

    @Test
    public void hasCompteursActifs_GivenOneFullAndOneActifCompteurs_ShouldBeTrue() {
        compteurs.add(createCompteur(2000, 0));
        compteurs.add(createCompteur(2000, 1000));

        assertThat(compteurs.hasCompteursActifs(), is(true));
    }

    @Test
    public void hasLignePourPeriodeSaisie_GivenEmptyCompteurs_ShouldBeFalse() {
        Annee anneeDebut = new Annee(2014);
        Annee anneeFin = new Annee(2014);

        assertThat(compteurs.hasLignePourPeriodeSaisie(anneeDebut, anneeFin), is(false));
    }

    @Test
    public void hasLignePourPeriodeSaisie_GivenCompteursWithLigneFor2014_ShouldBeTrue() {
        Annee anneeDebut = new Annee(2014);
        Annee anneeFin = new Annee(2014);

        createCompteur(2014, true);

        assertThat(compteurs.hasLignePourPeriodeSaisie(anneeDebut, anneeFin), is(true));
    }

    @Test
    public void hasLignePourPeriodeSaisie_GivenCompteursWithLigneFor2013_ShouldBeFalse() {
        Annee anneeDebut = new Annee(2014);
        Annee anneeFin = new Annee(2014);

        createCompteur(2013, true);

        assertThat(compteurs.hasLignePourPeriodeSaisie(anneeDebut, anneeFin), is(false));
    }

    @Test
    public void hasLignePourPeriodeSaisie_GivenCompteursWithLigneFor2013And2014_ShouldBeTrue() {
        Annee anneeDebut = new Annee(2014);
        Annee anneeFin = new Annee(2014);

        createCompteur(2013, true);
        createCompteur(2014, true);

        assertThat(compteurs.hasLignePourPeriodeSaisie(anneeDebut, anneeFin), is(true));
    }

    @Test
    public void hasLignePourPeriodeSaisieOf2014_2016_GivenCompteursWithLigneFor2013And2014_ShouldBeTrue() {
        Annee anneeDebut = new Annee(2014);
        Annee anneeFin = new Annee(2016);

        createCompteur(2013, true);
        createCompteur(2015, true);

        assertThat(compteurs.hasLignePourPeriodeSaisie(anneeDebut, anneeFin), is(true));
    }

    private void createCompteur(int annee, boolean hasLignes) {
        Compteur compteur = mock(Compteur.class);
        when(compteur.getAnnee()).thenReturn(new Annee(annee));
        when(compteur.hasLignes()).thenReturn(hasLignes);
        compteurs.add(compteur);
    }

    private Compteur createCompteur(int cumulCotisation, int montantRestant) {
        Compteur compteur = new Compteur();
        compteur.setCumulCotisation(new Montant(cumulCotisation));
        compteur.setMontantRestant(new Montant(montantRestant));
        return compteur;
    }

    private Compteur getCompteur(int i) {
        return compteurs.getCompteurs().get(i);
    }

    /**
     * Initialise les compteurs.
     * La première valeur du tableau correspond au cumul.
     * La seconde valeur du tableau correspond au montant restant.
     * 
     * On peut initialiser autant de compteurs que l'on souhaite.
     * 
     * @param values Tableau de int représentants les valeurs des compteurs
     */
    private void initCompteurs(int[]... values) {
        for (int i = 0; i < values.length; i++) {
            compteurs.add(createCompteur(values[i][0], values[i][1]));
        }
    }
}
