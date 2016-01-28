package globaz.osiris.db.irrecouvrable;

import org.junit.Assert;
import org.junit.Test;

public class CARecouvrementKeyPosteContainerTest {

    /**
     * Comparaison des deux cl�s. Les deux sont identiques
     */
    @Test
    public void compareToAreKeysEqualTest() {
        CARecouvrementKeyPosteContainer firstKeyPosteContainer = new CARecouvrementKeyPosteContainer(2010,
                "2110.4010.0000", "1");
        CARecouvrementKeyPosteContainer secondKeyPosteContainer = new CARecouvrementKeyPosteContainer(2010,
                "2110.4010.0000", "1");

        Assert.assertTrue(firstKeyPosteContainer.compareTo(secondKeyPosteContainer) == 0);
    }

    /**
     * Comparaison des deux cl�s. Les priorit�s sont identiques les num�ros de rubriques sont identiques et la premi�re
     * ann�e est plus grande
     */
    @Test
    public void compareToArePrioriteEqualsAndNumeroRubriqueEqualsAndFirstAnneeIsGreaterTest() {
        CARecouvrementKeyPosteContainer firstKeyPosteContainer = new CARecouvrementKeyPosteContainer(2011,
                "2111.4010.0000", "1");
        CARecouvrementKeyPosteContainer secondKeyPosteContainer = new CARecouvrementKeyPosteContainer(2010,
                "2110.4010.0000", "1");

        Assert.assertTrue(firstKeyPosteContainer.compareTo(secondKeyPosteContainer) == 1);
    }

    /**
     * Comparaison des deux cl�s. La premi�re priorit� est plus grande, les num�ros de rubrique sont identiques et les
     * ann�es sont identiques
     */
    @Test
    public void compareToIsFirstPrioriteGreaterAndFirstNumeroRubriqueEqualsAndAnneEqualsTest() {
        CARecouvrementKeyPosteContainer firstKeyPosteContainer = new CARecouvrementKeyPosteContainer(2010,
                "2111.4010.0000", "2");
        CARecouvrementKeyPosteContainer secondKeyPosteContainer = new CARecouvrementKeyPosteContainer(2010,
                "2110.4010.0000", "1");

        Assert.assertTrue(firstKeyPosteContainer.compareTo(secondKeyPosteContainer) == 1);
    }

    /**
     * Comparaison des deux cl�s. La premi�re priorit� est plus grande, le premier num�ro de rubrique est plus petit
     */
    @Test
    public void compareToIsFirstPrioriteGreaterAndFirstNumeroRubriqueSmallerAndAnneeEqualsTest() {
        CARecouvrementKeyPosteContainer firstKeyPosteContainer = new CARecouvrementKeyPosteContainer(2011,
                "2110.4010.0000", "2");
        CARecouvrementKeyPosteContainer secondKeyPosteContainer = new CARecouvrementKeyPosteContainer(2010,
                "2111.4010.0000", "1");

        Assert.assertTrue(firstKeyPosteContainer.compareTo(secondKeyPosteContainer) == 1);
    }

    /**
     * Comparaison des deux cl�s. La premi�re priorit� est plus grande, les num�ros de rubrique sont identiques et la
     * premi�re ann�e est plus petite ann�es sont identiques
     */
    @Test
    public void compareToIsFirstPrioriteGreaterAndNumeroRubriqueEqualsAndFirstAnneSmallerTest() {
        CARecouvrementKeyPosteContainer firstKeyPosteContainer = new CARecouvrementKeyPosteContainer(2010,
                "2111.4010.0000", "2");
        CARecouvrementKeyPosteContainer secondKeyPosteContainer = new CARecouvrementKeyPosteContainer(2011,
                "2110.4010.0000", "1");

        Assert.assertTrue(firstKeyPosteContainer.compareTo(secondKeyPosteContainer) == 1);
    }

    /**
     * Comparaison des deux cl�s. La premi�re priorit� est plus grande, les num�ros de rubriques sont identiques
     */
    @Test
    public void compareToIsFirstPrioriteGreaterAndNumeroRubriqueEqualsTest() {
        CARecouvrementKeyPosteContainer firstKeyPosteContainer = new CARecouvrementKeyPosteContainer(2010,
                "2110.4010.0000", "2");
        CARecouvrementKeyPosteContainer secondKeyPosteContainer = new CARecouvrementKeyPosteContainer(2011,
                "2110.4010.0000", "1");

        Assert.assertTrue(firstKeyPosteContainer.compareTo(secondKeyPosteContainer) == 1);
    }

    /**
     * Comparaison des deux cl�s. La premi�re priorit� est plus petite, le premier num�ro de rubrique est plus petit
     */
    @Test
    public void compareToIsFirstPrioriteSmallerAndFirstNumeroRubriqueGreaterTest() {
        CARecouvrementKeyPosteContainer firstKeyPosteContainer = new CARecouvrementKeyPosteContainer(2010,
                "2111.4010.0000", "1");
        CARecouvrementKeyPosteContainer secondKeyPosteContainer = new CARecouvrementKeyPosteContainer(2011,
                "2110.4010.0000", "2");

        Assert.assertTrue(firstKeyPosteContainer.compareTo(secondKeyPosteContainer) == -1);
    }

    /**
     * Comparaison des deux cl�s. La premi�re priorit� est plus petite, le premier num�ro de rubrique est plus petit et
     * les ann�es sont identiques
     */
    @Test
    public void compareToIsFirstPrioriteSmallerAndFirstNumeroRubriqueSmallerAndAnneeEqualsTest() {
        CARecouvrementKeyPosteContainer firstKeyPosteContainer = new CARecouvrementKeyPosteContainer(2010,
                "2110.4010.0000", "1");
        CARecouvrementKeyPosteContainer secondKeyPosteContainer = new CARecouvrementKeyPosteContainer(2010,
                "2111.4010.0000", "2");

        Assert.assertTrue(firstKeyPosteContainer.compareTo(secondKeyPosteContainer) == -1);
    }

    /**
     * Comparaison des deux cl�s. La premi�re priorit� est plus petite, les num�ro de rubrique sont identiques, les
     * ann�es sont identiques
     */
    @Test
    public void compareToIsFirstPrioriteSmallerAndNumeroRubriqueEqualsTest() {
        CARecouvrementKeyPosteContainer firstKeyPosteContainer = new CARecouvrementKeyPosteContainer(2010,
                "2110.4010.0000", "1");
        CARecouvrementKeyPosteContainer secondKeyPosteContainer = new CARecouvrementKeyPosteContainer(2010,
                "2110.4010.0000", "2");

        Assert.assertTrue(firstKeyPosteContainer.compareTo(secondKeyPosteContainer) == -1);
    }

    /**
     * Comparaison des deux cl�s. Les priorit�s sont identiques, le premier num�ro de rubrique est plus grand et les
     * ann�es sont identiques
     */
    @Test
    public void compareToIsPrioriteEqualsAndFirstNumeroRubriqueGreaterAndAnneeEqualsTest() {
        CARecouvrementKeyPosteContainer firstKeyPosteContainer = new CARecouvrementKeyPosteContainer(2010,
                "2111.4010.0000", "1");
        CARecouvrementKeyPosteContainer secondKeyPosteContainer = new CARecouvrementKeyPosteContainer(2010,
                "2110.4010.0000", "1");

        Assert.assertTrue(firstKeyPosteContainer.compareTo(secondKeyPosteContainer) == 1);
    }

    /**
     * Comparaison des deux cl�s. Les priorit�s sont identiques, le premier num�ro de rubrique est plus petit et les
     * ann�es sont identiques
     */
    @Test
    public void compareToIsPrioriteEqualsAndFirstNumeroRubriqueSmallerAndAnneeEqualsTest() {
        CARecouvrementKeyPosteContainer firstKeyPosteContainer = new CARecouvrementKeyPosteContainer(2010,
                "2110.4010.0000", "1");
        CARecouvrementKeyPosteContainer secondKeyPosteContainer = new CARecouvrementKeyPosteContainer(2010,
                "2111.4010.0000", "1");

        Assert.assertTrue(firstKeyPosteContainer.compareTo(secondKeyPosteContainer) == -1);
    }

    /**
     * Comparaison des deux cl�s. Les deux cl�s ne sont identiques
     */
    @Test
    public void EqualsFirstKeyAndSecondKeyAreDifferentTest() {
        CARecouvrementKeyPosteContainer firstKeyPosteContainer = new CARecouvrementKeyPosteContainer(2011,
                "2110.4010.0000", "");
        CARecouvrementKeyPosteContainer secondKeyPosteContainer = new CARecouvrementKeyPosteContainer(2010,
                "2110.4010.0000", "1");

        Assert.assertFalse(firstKeyPosteContainer.equals(secondKeyPosteContainer));
    }

    /**
     * Comparaison des deux cl�s. Les deux cl�s sont identiques
     */
    @Test
    public void EqualsFirstKeyAndSecondKeyAreEqualsTest() {
        CARecouvrementKeyPosteContainer firstKeyPosteContainer = new CARecouvrementKeyPosteContainer(2010,
                "2110.4010.0000", "1");
        CARecouvrementKeyPosteContainer secondKeyPosteContainer = new CARecouvrementKeyPosteContainer(2010,
                "2110.4010.0000", "1");

        Assert.assertTrue(firstKeyPosteContainer.equals(secondKeyPosteContainer));
    }

}
