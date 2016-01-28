package ch.globaz.amal.businessimpl.services.models.caissemaladie;

import org.junit.Assert;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladie;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladieGroupeRCListe;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladieGroupeRCListeSearch;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladieSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author cbu
 * 
 */
public class CaisseMaladieServiceImplTest {

    public CaisseMaladieServiceImplTest(String name) {
        // super(name);
    }

    public void testCount() {
        try {
            CaisseMaladieSearch search = new CaisseMaladieSearch();
            search.setForIdTiersCaisse("-1");
            int nbCaisse = AmalServiceLocator.getCaisseMaladieService().count(search);
            Assert.assertTrue(nbCaisse == 0);

            search = new CaisseMaladieSearch();
            search.setForIdTiersCaisse("195516");
            nbCaisse = AmalServiceLocator.getCaisseMaladieService().count(search);
            Assert.assertTrue(nbCaisse == 1);

            search = new CaisseMaladieSearch();
            search.setForNumGroupe("1111");
            nbCaisse = AmalServiceLocator.getCaisseMaladieService().count(search);
            Assert.assertTrue(nbCaisse > 0);

            search = new CaisseMaladieSearch();
            search.setForIdTiersGroupe("373949");
            nbCaisse = AmalServiceLocator.getCaisseMaladieService().count(search);
            Assert.assertTrue(nbCaisse > 0);
        } catch (Exception e) {
            Assert.fail("CaisseMaladieServiceImplTest : Erreur général lors du testCount sur CaisseMaladie ! ==> "
                    + e.toString());
        } finally {
            // doFinally();
        }
    }

    public void testRead() {
        try {
            // Lecture d'une caisse maladie qui n'existe pas
            CaisseMaladie caisseMaladie = new CaisseMaladie();
            caisseMaladie = AmalServiceLocator.getCaisseMaladieService().read("-1");
            Assert.assertTrue(caisseMaladie.isNew());

            // Lecture d'une caisse maladie qui existe (195516 ==> CSS)
            caisseMaladie = new CaisseMaladie();
            caisseMaladie = AmalServiceLocator.getCaisseMaladieService().read("195516");
            Assert.assertNotNull(caisseMaladie.getNomCaisse());
            Assert.assertEquals("195516", caisseMaladie.getId());
            Assert.assertEquals("195516", caisseMaladie.getIdTiersCaisse());
        } catch (Exception e) {
            Assert.fail("CaisseMaladieServiceImplTest : Erreur général lors du TestRead sur CaisseMaladie ! ==> "
                    + e.toString());
        } finally {
            // doFinally();
        }
    }

    public void testSearch() {
        try {
            CaisseMaladieSearch search = new CaisseMaladieSearch();
            search.setForIdTiersCaisse("-1");
            search = AmalServiceLocator.getCaisseMaladieService().search(search);
            Assert.assertTrue(search.getSize() == 0);

            search = new CaisseMaladieSearch();
            search.setForIdTiersCaisse("195516");
            search = AmalServiceLocator.getCaisseMaladieService().search(search);
            Assert.assertTrue(search.getSize() == 1);
            CaisseMaladie cm = (CaisseMaladie) search.getSearchResults()[0];
            Assert.assertEquals("CSS", cm.getNomCaisse());

            search = new CaisseMaladieSearch();
            search.setLikeNomCaisse("ASSURA");
            search = AmalServiceLocator.getCaisseMaladieService().search(search);
            Assert.assertTrue(search.getSize() == 1);
            cm = (CaisseMaladie) search.getSearchResults()[0];
            Assert.assertEquals("195495", cm.getIdTiersCaisse());

            search = new CaisseMaladieSearch();
            search.setForNumGroupe("1111");
            search = AmalServiceLocator.getCaisseMaladieService().search(search);
            Assert.assertTrue(search.getSize() > 0);

            search = new CaisseMaladieSearch();
            search.setForIdTiersGroupe("195505");
            search = AmalServiceLocator.getCaisseMaladieService().search(search);
            Assert.assertTrue(search.getSize() > 0);
        } catch (Exception e) {
            Assert.fail("CaisseMaladieServiceImplTest : Erreur général lors du TestSearch sur CaisseMaladie ! ==> "
                    + e.toString());
        } finally {
            // doFinally();
        }
    }

    public void testSearchGroupe() {
        try {
            CaisseMaladieGroupeRCListeSearch caisseMaladieGroupeRCListeSearch = new CaisseMaladieGroupeRCListeSearch();
            caisseMaladieGroupeRCListeSearch.setForTypeLien("19150084");
            caisseMaladieGroupeRCListeSearch.setForNumGroupe("-1");
            caisseMaladieGroupeRCListeSearch = AmalServiceLocator.getCaisseMaladieService().searchGroupe(
                    caisseMaladieGroupeRCListeSearch);
            Assert.assertTrue(caisseMaladieGroupeRCListeSearch.getSize() == 0);

            caisseMaladieGroupeRCListeSearch = new CaisseMaladieGroupeRCListeSearch();
            caisseMaladieGroupeRCListeSearch.setForTypeLien("19150084");
            caisseMaladieGroupeRCListeSearch.setForNumGroupe("1111");
            caisseMaladieGroupeRCListeSearch = AmalServiceLocator.getCaisseMaladieService().searchGroupe(
                    caisseMaladieGroupeRCListeSearch);
            Assert.assertTrue(caisseMaladieGroupeRCListeSearch.getSize() == 1);
            CaisseMaladieGroupeRCListe cm = (CaisseMaladieGroupeRCListe) caisseMaladieGroupeRCListeSearch
                    .getSearchResults()[0];
            Assert.assertEquals("Groupe Mutuel", cm.getNomGroupe());

            caisseMaladieGroupeRCListeSearch = new CaisseMaladieGroupeRCListeSearch();
            caisseMaladieGroupeRCListeSearch.setForTypeLien("19150084");
            caisseMaladieGroupeRCListeSearch.setForIdTiersGroupe("195505");
            caisseMaladieGroupeRCListeSearch = AmalServiceLocator.getCaisseMaladieService().searchGroupe(
                    caisseMaladieGroupeRCListeSearch);
            Assert.assertTrue(caisseMaladieGroupeRCListeSearch.getSize() > 0);
            cm = (CaisseMaladieGroupeRCListe) caisseMaladieGroupeRCListeSearch.getSearchResults()[0];
            Assert.assertEquals("Groupe Mutuel", cm.getNomGroupe());

            caisseMaladieGroupeRCListeSearch = new CaisseMaladieGroupeRCListeSearch();
            caisseMaladieGroupeRCListeSearch.setForTypeLien("19150084");
            caisseMaladieGroupeRCListeSearch = AmalServiceLocator.getCaisseMaladieService().searchGroupe(
                    caisseMaladieGroupeRCListeSearch);
            Assert.assertTrue(caisseMaladieGroupeRCListeSearch.getSize() > 0);
        } catch (Exception e) {
            Assert.fail("CaisseMaladieServiceImplTest : Erreur général lors du TestSearchGroupe sur CaisseMaladie ! ==> "
                    + e.toString());
        } finally {
            // doFinally();
        }
    }
}
