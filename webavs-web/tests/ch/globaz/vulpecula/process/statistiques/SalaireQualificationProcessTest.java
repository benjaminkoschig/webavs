package ch.globaz.vulpecula.process.statistiques;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.utils.Pair;
import ch.globaz.vulpecula.domain.models.common.DetailGroupeLocalite;
import ch.globaz.vulpecula.domain.models.common.DetailGroupeLocalites;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;

public class SalaireQualificationProcessTest {
    private SalaireQualificationProcess process;

    @Before
    public void setUp() {
        process = spy(new SalaireQualificationProcess());
    }

    @Test(expected = NullPointerException.class)
    public void findPairRegionQualif_GivenNull_ShouldReturnNull() {
        DetailGroupeLocalite dgl = mock(DetailGroupeLocalite.class);

        ArrayList<DetailGroupeLocalite> alDGL = new ArrayList<DetailGroupeLocalite>();
        alDGL.add(dgl);

        doReturn(new DetailGroupeLocalites(alDGL)).when(process).findDetailGroupeLocalites(any(PosteTravail.class));

        Pair<String, Qualification> pair = process.findPairRegionQualif(null);

        assertEquals(pair.getLeft(), "Autre");
        assertEquals(pair.getRight(), null);
    }

    @Test
    public void findPairRegionQualif_GivenOnePT_ShouldReturnSomething() {
        PosteTravail pt = mock(PosteTravail.class);
        DetailGroupeLocalite dgl = mock(DetailGroupeLocalite.class);
        when(dgl.getTypeGroupeDetailLocalite()).thenReturn(DetailGroupeLocalites.CS_REGION);
        when(dgl.getNomGroupeFR()).thenReturn("Sierre");

        ArrayList<DetailGroupeLocalite> alDGL = new ArrayList<DetailGroupeLocalite>();
        alDGL.add(dgl);
        DetailGroupeLocalites dgls = new DetailGroupeLocalites(alDGL);

        doReturn(dgls).when(process).findDetailGroupeLocalites(any(PosteTravail.class));

        Pair<String, Qualification> pair = process.findPairRegionQualif(pt);

        assertEquals(pair.getLeft(), "Sierre");
        assertEquals(pair.getRight(), null);
    }

    @Test(expected = NullPointerException.class)
    public void mapToQualifications_GivenNull_ThrowsException() {
        List<Qualification> liste = process.mapToQualifications(null);
        assertEquals(liste, null);
    }

    @Test
    public void mapToQualifications_GivenEmptyList_ShouldReturnEmptyList() {
        List<Qualification> liste = process.mapToQualifications(new ArrayList<String>());
        assertEquals(liste.size(), 0);
    }

    @Test
    public void mapToQualifications_GivenOneQ_ShouldReturnSomething() {
        List<Qualification> liste = process.mapToQualifications(new ArrayList<String>());
        assertEquals(liste.size(), 0);
    }

    @Test
    public void groupeByRegionAndQualification_GivenEmptyList_ShouldReturnEmptyMap() {
        Map<Pair<String, Qualification>, Collection<PosteTravail>> map = process
                .groupeByRegionAndQualification(new ArrayList<PosteTravail>());

        assertEquals(map.size(), 0);
    }

    @Test(expected = NullPointerException.class)
    public void groupeByRegionAndQualification_GivenNull_ThrowException() {
        Map<Pair<String, Qualification>, Collection<PosteTravail>> map = process.groupeByRegionAndQualification(null);
        assertEquals(map, null);
    }

    @Test
    public void groupeByRegionAndQualification_GivenOnePT_ShouldReturnSomething() {
        PosteTravail pt = mock(PosteTravail.class);

        ArrayList<PosteTravail> alPT = new ArrayList<PosteTravail>();
        alPT.add(pt);

        DetailGroupeLocalite dgl = mock(DetailGroupeLocalite.class);

        ArrayList<DetailGroupeLocalite> alDGL = new ArrayList<DetailGroupeLocalite>();
        alDGL.add(dgl);

        doReturn(new DetailGroupeLocalites(alDGL)).when(process).findDetailGroupeLocalites(pt);

        Map<Pair<String, Qualification>, Collection<PosteTravail>> map = process.groupeByRegionAndQualification(alPT);

        assertEquals(map.size(), 1);
    }

    @Test
    public void groupeByRegionAndQualification_GivenFourPT_ShouldReturnThreeSizeMap() {
        PosteTravail arnaud = mock(PosteTravail.class);
        PosteTravail nmo = mock(PosteTravail.class);
        PosteTravail jpa = mock(PosteTravail.class);
        PosteTravail cbu = mock(PosteTravail.class);

        when(arnaud.getQualification()).thenReturn(Qualification.APPRENTI);
        when(nmo.getQualification()).thenReturn(Qualification.APPRENTI);
        when(jpa.getQualification()).thenReturn(Qualification.EBENISTE_QUALIFIE);
        when(cbu.getQualification()).thenReturn(Qualification.APPRENTI);

        when(arnaud.getIdLocaliteEmployeur()).thenReturn("17");
        when(nmo.getIdLocaliteEmployeur()).thenReturn("21");
        when(jpa.getIdLocaliteEmployeur()).thenReturn("17");
        when(cbu.getIdLocaliteEmployeur()).thenReturn("17");

        DetailGroupeLocalite dglArnaud = mock(DetailGroupeLocalite.class);
        when(dglArnaud.getTypeGroupeDetailLocalite()).thenReturn(DetailGroupeLocalites.CS_REGION);
        when(dglArnaud.getNomGroupeFR()).thenReturn("Sierre");

        DetailGroupeLocalite dglNMO = mock(DetailGroupeLocalite.class);
        when(dglNMO.getTypeGroupeDetailLocalite()).thenReturn(DetailGroupeLocalites.CS_REGION);
        when(dglNMO.getNomGroupeFR()).thenReturn("Conches");

        DetailGroupeLocalite dglJPA = mock(DetailGroupeLocalite.class);
        when(dglJPA.getTypeGroupeDetailLocalite()).thenReturn(DetailGroupeLocalites.CS_REGION);
        when(dglJPA.getNomGroupeFR()).thenReturn("Sierre");

        DetailGroupeLocalite dglCBU = mock(DetailGroupeLocalite.class);
        when(dglCBU.getNomGroupeFR()).thenReturn("Sierre");
        when(dglCBU.getTypeGroupeDetailLocalite()).thenReturn(DetailGroupeLocalites.CS_REGION);

        ArrayList<DetailGroupeLocalite> alDGL = new ArrayList<DetailGroupeLocalite>();
        alDGL.add(dglArnaud);
        DetailGroupeLocalites dglsArnaud = new DetailGroupeLocalites(alDGL);
        alDGL = new ArrayList<DetailGroupeLocalite>();
        alDGL.add(dglNMO);
        DetailGroupeLocalites dglsNMO = new DetailGroupeLocalites(alDGL);
        alDGL = new ArrayList<DetailGroupeLocalite>();
        alDGL.add(dglJPA);
        DetailGroupeLocalites dglsJPA = new DetailGroupeLocalites(alDGL);
        alDGL = new ArrayList<DetailGroupeLocalite>();
        alDGL.add(dglCBU);
        DetailGroupeLocalites dglsCBU = new DetailGroupeLocalites(alDGL);

        doReturn(dglsArnaud).when(process).findDetailGroupeLocalites(arnaud);
        doReturn(dglsNMO).when(process).findDetailGroupeLocalites(nmo);
        doReturn(dglsJPA).when(process).findDetailGroupeLocalites(jpa);
        doReturn(dglsCBU).when(process).findDetailGroupeLocalites(cbu);

        List<PosteTravail> postes = new ArrayList<PosteTravail>();
        postes.add(arnaud);
        postes.add(nmo);
        postes.add(jpa);
        postes.add(cbu);

        Map<Pair<String, Qualification>, Collection<PosteTravail>> map = process.groupeByRegionAndQualification(postes);

        for (Map.Entry<Pair<String, Qualification>, Collection<PosteTravail>> entree : map.entrySet()) {
            System.out.println(entree.getKey() + " " + entree.getValue());
        }

        assertEquals(map.size(), 3);
    }
}
