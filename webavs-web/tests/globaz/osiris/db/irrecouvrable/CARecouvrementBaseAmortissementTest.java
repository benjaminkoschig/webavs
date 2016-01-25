package globaz.osiris.db.irrecouvrable;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

public class CARecouvrementBaseAmortissementTest {

    @Test
    public void addBaseAmortissementTest() {
        CARecouvrementBaseAmortissementContainer baseAmortissementContainer = new CARecouvrementBaseAmortissementContainer();
        baseAmortissementContainer.addRecouvrementBaseAmortissement(new Integer(2010), new BigDecimal("15253.50"));
        Assert.assertFalse("La map est vide", baseAmortissementContainer.getRecouvrementAmortissementMap().isEmpty());
    }

    @Test
    public void displayBaseAmortissementTest() {
        CARecouvrementBaseAmortissementContainer baseAmortissementContainer = new CARecouvrementBaseAmortissementContainer();
        baseAmortissementContainer.addRecouvrementBaseAmortissement(new Integer(2010), new BigDecimal("15253.50"));
        baseAmortissementContainer.addRecouvrementBaseAmortissement(new Integer(2011), new BigDecimal("450.25"));
        String resultExpected = "";
        resultExpected += "\nkey(annee) : 2010\n";
        resultExpected += "           anneeAmortissement      : 2010\n";
        resultExpected += "           cumulCotisationForAnnee : -15253.50\n";
        resultExpected += "###########################################################################";
        resultExpected += "\nkey(annee) : 2011\n";
        resultExpected += "           anneeAmortissement      : 2011\n";
        resultExpected += "           cumulCotisationForAnnee : -450.25\n";
        resultExpected += "###########################################################################";
        // System.out.println(resultExpected);
        // System.out.println(baseAmortissementContainer.toString());
        Assert.assertTrue("L'affichage ne fonctionne pas correctement selon le modèle définit",
                resultExpected.equals(baseAmortissementContainer.toString()));
    }

}
