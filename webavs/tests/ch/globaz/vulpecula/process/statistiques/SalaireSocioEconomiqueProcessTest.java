package ch.globaz.vulpecula.process.statistiques;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

public class SalaireSocioEconomiqueProcessTest {
    private SalaireSocioEconomiqueProcess process = new SalaireSocioEconomiqueProcess();

    /*
     * @Test
     * public void findAndGroupLignesDecomptes_GivenEmptyCollection_ShouldReturnEmptyMap() {
     * Map<PosteTravail, Collection<DecompteSalaire>> map = process.findAndGroupLignesDecomptes(
     * new ArrayList<PosteTravail>(), new Annee(2000), new Annee(2001));
     * assertEquals(map.size(), 0);
     * }
     * 
     * @Test
     * public void findAndGroupLignesDecomptes_GivenOnePT_ShouldReturn() {
     * PosteTravail pt = new PosteTravail();
     * Map<PosteTravail, Collection<DecompteSalaire>> map = process.findAndGroupLignesDecomptes(Arrays.asList(pt),
     * new Annee(2000), new Annee(2001));
     * 
     * assertEquals(map.size(), 1);
     * }
     */

    @Test
    public void groupLignesDecomptesByPosteTravail_GivenEmptyList_ShouldReturnEmptyMap() {
        Map<PosteTravail, Collection<DecompteSalaire>> map = process
                .groupLignesDecomptesByPosteTravail(new ArrayList<DecompteSalaire>());

        assertEquals(map.size(), 0);
    }

    @Test
    public void groupLignesDecomptesByPosteTravail_GivenOneDS_ShouldReturnSomething() {
        DecompteSalaire ds = mock(DecompteSalaire.class);
        when(ds.getPosteTravail()).thenReturn(new PosteTravail());
        ArrayList<DecompteSalaire> alDS = new ArrayList<DecompteSalaire>();
        alDS.add(ds);

        Map<PosteTravail, Collection<DecompteSalaire>> map = process.groupLignesDecomptesByPosteTravail(alDS);

        assertEquals(map.size(), 1);
    }

    @Test(expected = NullPointerException.class)
    public void groupLigneDecompteByPosteTravail_GivenNull_ThrowException() {
        Map<PosteTravail, Collection<DecompteSalaire>> map = process.groupLignesDecomptesByPosteTravail(null);
        assertEquals(map, null);
    }

    @Test
    public void findAndGroupByPosteTravail_GivenTwoPTWithThreeDS_ShouldReturnMapWithTwoResult() {
        SalaireSocioEconomiqueProcess spyProcess = spy(process);

        Employeur employeur = new Employeur();
        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setId("1");
        PosteTravail posteTravail2 = new PosteTravail();
        posteTravail.setId("2");
        employeur.setPostesTravail(Arrays.asList(posteTravail, posteTravail2));

        DecompteSalaire ds1 = new DecompteSalaire();
        DecompteSalaire ds2 = new DecompteSalaire();
        DecompteSalaire ds3 = new DecompteSalaire();
        ds1.setPosteTravail(posteTravail);
        ds2.setPosteTravail(posteTravail2);
        ds3.setPosteTravail(posteTravail2);

        List<DecompteSalaire> lDS = new ArrayList<DecompteSalaire>();
        lDS.add(ds1);
        lDS.add(ds2);
        lDS.add(ds3);

        doReturn(lDS).when(spyProcess).findListeDecomptes(anyList(), any(Date.class), any(Date.class));

        Map<PosteTravail, Collection<DecompteSalaire>> dsGroupByPoste = spyProcess.findAndGroupByPosteTravail(
                employeur, new Date("01.01.2015"), new Date("31.01.2015"));
        assertEquals(1, dsGroupByPoste.get(posteTravail).size());
        assertEquals(2, dsGroupByPoste.get(posteTravail2).size());
    }
}
