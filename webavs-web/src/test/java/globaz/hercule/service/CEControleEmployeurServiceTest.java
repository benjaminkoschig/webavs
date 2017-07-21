package globaz.hercule.service;

import static org.junit.Assert.*;
import globaz.hercule.db.ICEControleEmployeur;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class CEControleEmployeurServiceTest {

    @Test
    public void testCalculPeriodeCouverture() throws Exception {

        Map<String, String> params = new HashMap<String, String>();
        params.put(ICEControleEmployeur.PERIODE1, "5");
        params.put(ICEControleEmployeur.PERIODE2, "2");
        params.put(ICEControleEmployeur.PERIODE3, "2");

        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(0, "4", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(1, "4", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(2, "4", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(3, "4", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(4, "4", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(5, "4", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(6, "4", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(7, "4", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(8, "4", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(9, "4", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(10, "4", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(11, "4", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(12, "4", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(13, "4", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(14, "4", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(15, "4", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(16, "4", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(17, "4", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(18, "4", params, 0));

        assertEquals("Nombre années : ", 9, CEControleEmployeurService.calculPeriodeCouverture(0, "3", params, 0));
        assertEquals("Nombre années : ", 9, CEControleEmployeurService.calculPeriodeCouverture(1, "3", params, 0));
        assertEquals("Nombre années : ", 9, CEControleEmployeurService.calculPeriodeCouverture(2, "3", params, 0));
        assertEquals("Nombre années : ", 7, CEControleEmployeurService.calculPeriodeCouverture(3, "3", params, 0));
        assertEquals("Nombre années : ", 7, CEControleEmployeurService.calculPeriodeCouverture(4, "3", params, 0));
        assertEquals("Nombre années : ", 7, CEControleEmployeurService.calculPeriodeCouverture(5, "3", params, 0));
        assertEquals("Nombre années : ", 7, CEControleEmployeurService.calculPeriodeCouverture(6, "3", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(7, "3", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(8, "3", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(9, "3", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(10, "3", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(11, "3", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(12, "3", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(13, "3", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(14, "3", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(15, "3", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(16, "3", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(17, "3", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(18, "3", params, 0));

        assertEquals("Nombre années : ", 9, CEControleEmployeurService.calculPeriodeCouverture(0, "2", params, 0));
        assertEquals("Nombre années : ", 9, CEControleEmployeurService.calculPeriodeCouverture(1, "2", params, 0));
        assertEquals("Nombre années : ", 9, CEControleEmployeurService.calculPeriodeCouverture(2, "2", params, 0));
        assertEquals("Nombre années : ", 9, CEControleEmployeurService.calculPeriodeCouverture(3, "2", params, 0));
        assertEquals("Nombre années : ", 9, CEControleEmployeurService.calculPeriodeCouverture(4, "2", params, 0));
        assertEquals("Nombre années : ", 9, CEControleEmployeurService.calculPeriodeCouverture(5, "2", params, 0));
        assertEquals("Nombre années : ", 7, CEControleEmployeurService.calculPeriodeCouverture(6, "2", params, 0));
        assertEquals("Nombre années : ", 7, CEControleEmployeurService.calculPeriodeCouverture(7, "2", params, 0));
        assertEquals("Nombre années : ", 7, CEControleEmployeurService.calculPeriodeCouverture(8, "2", params, 0));
        assertEquals("Nombre années : ", 7, CEControleEmployeurService.calculPeriodeCouverture(9, "2", params, 0));
        assertEquals("Nombre années : ", 7, CEControleEmployeurService.calculPeriodeCouverture(10, "2", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(11, "2", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(12, "2", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(13, "2", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(14, "2", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(15, "2", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(16, "2", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(17, "2", params, 0));
        assertEquals("Nombre années : ", 5, CEControleEmployeurService.calculPeriodeCouverture(18, "2", params, 0));
    }

    @Test
    public void testFindCategorieDouble() throws Exception {
        assertEquals("Catégorie salariale : ", ICEControleEmployeur.CATEGORIE_MASSE_0,
                CEControleEmployeurService.findCategorie(new Double("0")));

        assertEquals("Catégorie salariale : ", ICEControleEmployeur.CATEGORIE_MASSE_1,
                CEControleEmployeurService.findCategorie(new Double("10000.00")));
        assertEquals("Catégorie salariale : ", ICEControleEmployeur.CATEGORIE_MASSE_1,
                CEControleEmployeurService.findCategorie(new Double("99999.99")));
        assertEquals("Catégorie salariale : ", ICEControleEmployeur.CATEGORIE_MASSE_1,
                CEControleEmployeurService.findCategorie(new Double("100000")));
        assertEquals("Catégorie salariale : ", ICEControleEmployeur.CATEGORIE_MASSE_1,
                CEControleEmployeurService.findCategorie(new Double("149999.999999")));
        assertEquals("Catégorie salariale : ", ICEControleEmployeur.CATEGORIE_MASSE_1,
                CEControleEmployeurService.findCategorie(new Double("149999")));

        assertEquals("Catégorie salariale : ", ICEControleEmployeur.CATEGORIE_MASSE_2,
                CEControleEmployeurService.findCategorie(new Double("150000")));
        assertEquals("Catégorie salariale : ", ICEControleEmployeur.CATEGORIE_MASSE_2,
                CEControleEmployeurService.findCategorie(new Double("205345.25")));
        assertEquals("Catégorie salariale : ", ICEControleEmployeur.CATEGORIE_MASSE_2,
                CEControleEmployeurService.findCategorie(new Double("499999.999")));

        assertEquals("Catégorie salariale : ", ICEControleEmployeur.CATEGORIE_MASSE_3,
                CEControleEmployeurService.findCategorie(new Double("500000")));
        assertEquals("Catégorie salariale : ", ICEControleEmployeur.CATEGORIE_MASSE_3,
                CEControleEmployeurService.findCategorie(new Double("550689.546832")));
        assertEquals("Catégorie salariale : ", ICEControleEmployeur.CATEGORIE_MASSE_3,
                CEControleEmployeurService.findCategorie(new Double("4999999.99999")));

        assertEquals("Catégorie salariale : ", ICEControleEmployeur.CATEGORIE_MASSE_4,
                CEControleEmployeurService.findCategorie(new Double("5000000.000")));
        assertEquals("Catégorie salariale : ", ICEControleEmployeur.CATEGORIE_MASSE_4,
                CEControleEmployeurService.findCategorie(new Double("12354831210")));
        assertEquals("Catégorie salariale : ", ICEControleEmployeur.CATEGORIE_MASSE_4,
                CEControleEmployeurService.findCategorie(new Double("321331226354.455")));

    }

}
