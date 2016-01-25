package globaz.corvus.vb.demandes;

import globaz.corvus.vb.demandes.REDemandeRenteJointPrestationAccordeeViewBeanComparator.TypeComparaison;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;

public class REDemandeRenteJointPrestationAccordeeViewBeanComparatorTest {

    private REDemandeRenteJointPrestationAccordeeViewBeanComparator testInstance;
    @Mock
    private REDemandeRenteJointPrestationAccordeeViewBean vb1, vb2;

    @Before
    public void setUp() throws Exception {
        testInstance = new REDemandeRenteJointPrestationAccordeeViewBeanComparator(TypeComparaison.Alphabetique);

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCompare() {
        testInstance = Mockito.spy(testInstance);

        String messageAppelEtat = "Appel au tri par etat";
        String messageAppelAlphabetique = "Appel au tri alphabétique";
        String messageAppelDateReception = "Appel au tri par date réception";

        Mockito.doThrow(new MockitoException(messageAppelEtat))
                .when(testInstance)
                .compareEtat(Matchers.any(REDemandeRenteJointPrestationAccordeeViewBean.class),
                        Matchers.any(REDemandeRenteJointPrestationAccordeeViewBean.class));
        Mockito.doThrow(new MockitoException(messageAppelAlphabetique))
                .when(testInstance)
                .compareAlphabetique(Matchers.any(REDemandeRenteJointPrestationAccordeeViewBean.class),
                        Matchers.any(REDemandeRenteJointPrestationAccordeeViewBean.class));
        Mockito.doThrow(new MockitoException(messageAppelDateReception))
                .when(testInstance)
                .compareDateReception(Matchers.any(REDemandeRenteJointPrestationAccordeeViewBean.class),
                        Matchers.any(REDemandeRenteJointPrestationAccordeeViewBean.class));

        try {
            testInstance.setTypeComparaison(TypeComparaison.Alphabetique);
            testInstance.compare(vb1, vb2);
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof MockitoException);
            Assert.assertEquals(messageAppelAlphabetique, ex.getMessage());
        }

        try {
            testInstance.setTypeComparaison(TypeComparaison.Etat);
            testInstance.compare(vb1, vb2);
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof MockitoException);
            Assert.assertEquals(messageAppelEtat, ex.getMessage());
        }

        try {
            testInstance.setTypeComparaison(TypeComparaison.DateReception);
            testInstance.compare(vb1, vb2);
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof MockitoException);
            Assert.assertEquals(messageAppelDateReception, ex.getMessage());
        }
    }

    @Test
    public void testCompareAlphabetique() {
        Mockito.when(vb1.getNom()).thenReturn("Aaaaaaaaaa");
        Mockito.when(vb2.getNom()).thenReturn("Baaaaaaaaa");
        Assert.assertEquals(-1, testInstance.compareAlphabetique(vb1, vb2));

        Mockito.when(vb2.getNom()).thenReturn("Aaaaaaaaaab");
        Assert.assertEquals(-1, testInstance.compareAlphabetique(vb1, vb2));

        // si les même nom, teste sur le prénom
        Mockito.when(vb2.getNom()).thenReturn("Aaaaaaaaaa");
        Mockito.when(vb1.getPrenom()).thenReturn("Baaaaaaaaa");
        Mockito.when(vb2.getPrenom()).thenReturn("Aaaaaaaaaa");
        Assert.assertEquals(1, testInstance.compareAlphabetique(vb1, vb2));

        // si le même prénom, teste sur les date de début et de fin de droit (autre méthode)
        Mockito.when(vb1.getPrenom()).thenReturn("Aaaaaaaaaa");
        Mockito.when(vb1.getDateDebut()).thenThrow(new NullPointerException());
        Mockito.when(vb2.getDateDebut()).thenThrow(new NullPointerException());
        Mockito.when(vb1.getDateFin()).thenThrow(new NullPointerException());
        Mockito.when(vb2.getDateFin()).thenThrow(new NullPointerException());
        try {
            testInstance.compareAlphabetique(vb1, vb2);
            Assert.fail("La recherche pas date de début et de fin de droit n'a pas été lancé");
        } catch (Exception ex) {
            // OK
        }
    }

    @Test
    public void testCompareDate() {
        String date1 = "01.01.2000";
        String date2 = "02.01.2000";
        Assert.assertEquals(1, testInstance.compareDate(date1, date2));
        Assert.assertEquals(-1, testInstance.compareDate(date2, date1));

        date2 = "01.01.2000";
        Assert.assertEquals(0, testInstance.compareDate(date1, date2));

        date1 = "";
        Assert.assertEquals(0, testInstance.compareDate(date1, date2));

        date1 = "01.01.2000";
        date2 = "";
        Assert.assertEquals(0, testInstance.compareDate(date1, date2));

        date2 = "abcdefg";
        Assert.assertEquals(0, testInstance.compareDate(date1, date2));
    }

    @Test
    public void testCompareDateDroit() {
        Mockito.when(vb1.getDateFin()).thenReturn("01.04.2001");
        Mockito.when(vb2.getDateFin()).thenReturn("");
        Mockito.when(vb1.getDateDebut()).thenReturn("01.03.2001");
        Mockito.when(vb2.getDateDebut()).thenReturn("01.01.2001");
        Assert.assertEquals(1, testInstance.compareDateDroit(vb1, vb2));

        Mockito.when(vb2.getDateFin()).thenReturn("01.03.2001");
        Assert.assertEquals(-1, testInstance.compareDateDroit(vb1, vb2));

        // cette date va devenir 31.12.2099
        Mockito.when(vb2.getDateFin()).thenReturn("0a.12.2000");
        Assert.assertEquals(1, testInstance.compareDateDroit(vb1, vb2));

        // même date de fin, la date du début devient la valeur testée
        Mockito.when(vb2.getDateFin()).thenReturn("01.04.2001");
        Assert.assertEquals(-1, testInstance.compareDateDroit(vb1, vb2));
    }

    @Test
    public void testCompareDateReception() {
        Mockito.when(vb1.getDateReception()).thenReturn("28.03.2011");
        Mockito.when(vb2.getDateReception()).thenReturn("29.03.2011");
        Assert.assertEquals(1, testInstance.compareDateReception(vb1, vb2));

        Mockito.when(vb1.getDateReception()).thenReturn("30.03.2011");
        Assert.assertEquals(-1, testInstance.compareDateReception(vb1, vb2));

        // si les deux valeurs sont les mêmes, le tri alphabétique sera appelé
        Mockito.when(vb2.getDateReception()).thenReturn("30.03.2011");
        Mockito.when(vb1.getNom()).thenThrow(new NullPointerException());
        Mockito.when(vb2.getNom()).thenThrow(new NullPointerException());
        try {
            testInstance.compareDateReception(vb1, vb2);
            Assert.fail("Le tri alphabétique n'a pas été lancé (Pas d'appel à getNom())");
        } catch (Exception ex) {
            // Ok
        }
    }

    @Test
    public void testCompareEtat() {
        Mockito.when(vb1.getCsEtatDemande()).thenReturn("52804004");
        Mockito.when(vb2.getCsEtatDemande()).thenReturn("52804002");
        Assert.assertEquals(1, testInstance.compareEtat(vb1, vb2));

        Mockito.when(vb2.getCsEtatDemande()).thenReturn("52804005");
        Assert.assertEquals(-1, testInstance.compareEtat(vb1, vb2));

        // si les deux valeurs sont les mêmes, le tri alphabétique sera appelé
        Mockito.when(vb1.getCsEtatDemande()).thenReturn("52804004");
        Mockito.when(vb1.getNom()).thenThrow(new NullPointerException());
        Mockito.when(vb2.getNom()).thenThrow(new NullPointerException());
        try {
            testInstance.compareDateReception(vb1, vb2);
            Assert.fail("Le tri alphabétique n'a pas été lancé");
        } catch (Exception ex) {
            // Ok
        }
    }

    @Test
    public void testFormatDate() {
        String date = "01.2000";
        Assert.assertEquals("01.01.2000", testInstance.formatDate(date));

        date = "4.2000";
        Assert.assertEquals("01.04.2000", testInstance.formatDate(date));

        date = "";
        Assert.assertEquals("31.12.2099", testInstance.formatDate(date));

        date = "03.12.2012";
        Assert.assertEquals(date, testInstance.formatDate(date));

        date = "32.01.2000";
        Assert.assertEquals("31.12.2099", testInstance.formatDate(date));

        date = "01.00";
        Assert.assertEquals("31.12.2099", testInstance.formatDate(date));
    }
}
