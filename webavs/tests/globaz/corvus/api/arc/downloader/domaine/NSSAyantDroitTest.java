package globaz.corvus.api.arc.downloader.domaine;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import globaz.corvus.api.arc.downloader.REAnnoncesHermesMap;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSession;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import com.google.common.collect.ImmutableMap;

public class NSSAyantDroitTest {

    private static final String CHAMP_ENREGISTREMENT_CI_NORMAL = "111111111111111111111111111111111111NNNNNNNNNNN11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
    private static final String CHAMP_ENREGISTREMENT_CI_AD = "1111111111111111111111111111111AAAAAAAAAAA1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";

    @Test
    public void simpleGroupByIdTiersTest() {

        ImmutableMap<String, Collection<PRTiersWrapper>> listeGroupes;

        listeGroupes = NSSAyantDroit.groupTiersListByIdTiers(getTiersWrapperList().getTiersWrappers());

        assertTrue("Test taille regroupemement :7", listeGroupes.keySet().size() == 7);
        assertFalse("Test taille pas taille initiale de la liste",
                listeGroupes.keySet().size() == getTiersWrapperList().getTiersWrappers().size());

    }

    @Test(expected = NullPointerException.class)
    public void nullEnEntree() {

        ImmutableMap<String, Collection<PRTiersWrapper>> listeGroupes;

        listeGroupes = NSSAyantDroit.groupTiersListByIdTiers(null);

        assertTrue("Test taille regroupemement :7", listeGroupes.keySet().size() == 7);
        assertFalse("Test taille pas taille initiale de la liste",
                listeGroupes.keySet().size() == getTiersWrapperList().getTiersWrappers().size());

    }

    @Test
    public void listeVideEnEntree() {

        ImmutableMap<String, Collection<PRTiersWrapper>> listeGroupes;

        listeGroupes = NSSAyantDroit.groupTiersListByIdTiers(new ArrayList<PRTiersWrapper>());

        assertTrue("Test taille regroupemement :0", listeGroupes.keySet().size() == 0);
        assertTrue("Test taille pas taille initiale de la liste",
                listeGroupes.keySet().size() == new ArrayList<PRTiersWrapper>().size());

    }

    @Ignore
    @Test
    public void standardTiersComparaisonTest() throws Exception {

        NSSAyantDroit test = Mockito.mock(NSSAyantDroit.class);
        // Mockito.when(test.findTiersWrapperById(any(String.class))).thenReturn(value)

        boolean tiersDiff = test.areTiersInCollectionIdentical(getTiersWrapperList().getIdTiersAsStringList());
        assertFalse(tiersDiff);

        boolean tiersIdentical = test.areTiersInCollectionIdentical(getIdenticalTiersWrapperList()
                .getIdTiersAsStringList());
        assertTrue(tiersIdentical);

    }

    @Test
    public void ensureConstructorFunctionnality() {
        NSSAyantDroit nss;

        try {
            nss = new NSSAyantDroit(null, null, null);
        } catch (NullPointerException ex) {
            assertTrue(true);
            assertTrue(ex.getMessage().equals("The annonce can't be null"));
            assertFalse(ex.getMessage().equals("aThe annonce can't be null"));
        }

    }

    @Test
    public void findNssInAnnonceStandardTest() throws Exception {

        // Annonce
        REAnnoncesHermesMap annonce = mock(REAnnoncesHermesMap.class);
        when(annonce.getAnnonce()).thenReturn(mock(IHEOutputAnnonce.class));

        when(annonce.getAnnonce().getChampEnregistrement()).thenReturn(CHAMP_ENREGISTREMENT_CI_NORMAL);

        NSSAyantDroit ayantDroit = new NSSAyantDroit(annonce, new BSession(), new FWMemoryLog());
        String nssFind = ayantDroit.findNssNavsInAnnonce(TypeCI.CI_NORMAL);

        assertTrue("Recup champs enregistrement CI Normal, :" + nssFind, nssFind.equals("NNNNNNNNNNN"));

        annonce = mock(REAnnoncesHermesMap.class);
        when(annonce.getAnnonce()).thenReturn(mock(IHEOutputAnnonce.class));
        when(annonce.getAnnonce().getChampEnregistrement()).thenReturn(CHAMP_ENREGISTREMENT_CI_AD);
        ayantDroit = new NSSAyantDroit(annonce, new BSession(), new FWMemoryLog());
        nssFind = ayantDroit.findNssNavsInAnnonce(TypeCI.CI_ADDITIONNEL);
        assertTrue("Recup champs enregistrement CI Ad, :" + nssFind, nssFind.equals("AAAAAAAAAAA"));

    }

    private PRTiersWrapper getTiersWrapper(int id) {
        String randomIdTiers = String.valueOf(id);
        PRTiersWrapper wrapper = mock(PRTiersWrapper.class);
        Mockito.when(wrapper.getIdTiers()).thenReturn(randomIdTiers);

        return wrapper;
    }

    private TiersTestData getTiersWrapperList() {

        TiersTestData tiersData = new TiersTestData();
        List<PRTiersWrapper> listTiers = new ArrayList<PRTiersWrapper>();
        List<String> listIdTiers = new ArrayList<String>();

        // Creation d'une liste de tiers wrapper
        listTiers.add(getTiersWrapper(1));
        listIdTiers.add("1");
        listTiers.add(getTiersWrapper(11));
        listIdTiers.add("11");
        listTiers.add(getTiersWrapper(2));
        listIdTiers.add("2");
        listTiers.add(getTiersWrapper(1));
        listIdTiers.add("1");
        listTiers.add(getTiersWrapper(3));
        listIdTiers.add("3");
        listTiers.add(getTiersWrapper(2));
        listIdTiers.add("2");
        listTiers.add(getTiersWrapper(1));
        listIdTiers.add("1");
        listTiers.add(getTiersWrapper(2));
        listIdTiers.add("2");
        listTiers.add(getTiersWrapper(5));
        listIdTiers.add("5");
        listTiers.add(getTiersWrapper(3));
        listIdTiers.add("3");
        listTiers.add(getTiersWrapper(6));
        listIdTiers.add("6");
        listTiers.add(getTiersWrapper(2));
        listIdTiers.add("2");
        listTiers.add(getTiersWrapper(9));
        listIdTiers.add("9");

        tiersData.setIdTiersAsStringList(listIdTiers);
        tiersData.setTiersWrappers(listTiers);

        return tiersData;
    }

    private TiersTestData getIdenticalTiersWrapperList() {

        TiersTestData tiersData = new TiersTestData();

        List<PRTiersWrapper> listTiers = new ArrayList<PRTiersWrapper>();
        List<String> listIdTiers = new ArrayList<String>();

        for (int cpt = 0; cpt < 12; cpt++) {
            listTiers.add(getTiersWrapper(11));
            listIdTiers.add("11");
        }

        tiersData.setTiersWrappers(listTiers);
        tiersData.setIdTiersAsStringList(listIdTiers);

        return tiersData;
    }

    class TiersTestData {
        private List<PRTiersWrapper> tiersWrappers;
        private List<String> idTiersAsStringList;

        public List<PRTiersWrapper> getTiersWrappers() {
            return tiersWrappers;
        }

        public void setTiersWrappers(List<PRTiersWrapper> tiersWrappers) {
            this.tiersWrappers = tiersWrappers;
        }

        public List<String> getIdTiersAsStringList() {
            return idTiersAsStringList;
        }

        public void setIdTiersAsStringList(List<String> idTiersAsStringList) {
            this.idTiersAsStringList = idTiersAsStringList;
        }

    }

}
