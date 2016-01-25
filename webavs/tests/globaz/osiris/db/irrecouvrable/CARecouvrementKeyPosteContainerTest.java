package globaz.osiris.db.irrecouvrable;

import org.junit.Assert;
import org.junit.Test;

public class CARecouvrementKeyPosteContainerTest {

    /**
     * Comparaison des deux clés. Les deux sont identiques
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
     * Comparaison des deux clés. Les priorités sont identiques les numéros de rubriques sont identiques et la première
     * année est plus grande
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
     * Comparaison des deux clés. La première priorité est plus grande, les numéros de rubrique sont identiques et les
     * années sont identiques
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
     * Comparaison des deux clés. La première priorité est plus grande, le premier numéro de rubrique est plus petit
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
     * Comparaison des deux clés. La première priorité est plus grande, les numéros de rubrique sont identiques et la
     * première année est plus petite années sont identiques
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
     * Comparaison des deux clés. La première priorité est plus grande, les numéros de rubriques sont identiques
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
     * Comparaison des deux clés. La première priorité est plus petite, le premier numéro de rubrique est plus petit
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
     * Comparaison des deux clés. La première priorité est plus petite, le premier numéro de rubrique est plus petit et
     * les années sont identiques
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
     * Comparaison des deux clés. La première priorité est plus petite, les numéro de rubrique sont identiques, les
     * années sont identiques
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
     * Comparaison des deux clés. Les priorités sont identiques, le premier numéro de rubrique est plus grand et les
     * années sont identiques
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
     * Comparaison des deux clés. Les priorités sont identiques, le premier numéro de rubrique est plus petit et les
     * années sont identiques
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
     * Comparaison des deux clés. Les deux clés ne sont identiques
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
     * Comparaison des deux clés. Les deux clés sont identiques
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
