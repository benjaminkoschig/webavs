package ch.globaz.corvus.domaine;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.corvus.TestUnitaireAvecGenerateurIDUnique;
import ch.globaz.corvus.domaine.constantes.TypeSoldePourRestitution;

public class SoldePourRestitutionTest extends TestUnitaireAvecGenerateurIDUnique {

    @Test
    public void testSetMontantRetenueMensuelle() throws Exception {
        SoldePourRestitution soldePourRestitution = new SoldePourRestitution();
        soldePourRestitution.setId(genererUnIdUnique());
        soldePourRestitution.setType(TypeSoldePourRestitution.RESTITUTION);

        try {
            soldePourRestitution.setMontantRetenueMensuelle(BigDecimal.ONE);
            Assert.fail("il ne doit pas �tre permis de d�finir un montant mensuel si le type n'est pas \"retenue mensuelle\"");
        } catch (IllegalArgumentException ex) {
            // ok
        }

        soldePourRestitution.setType(TypeSoldePourRestitution.RETENUES);
        try {
            soldePourRestitution.setMontantRetenueMensuelle(BigDecimal.ONE);
        } catch (IllegalArgumentException ex) {
            Assert.fail("Il doit �tre autoris� de changer le montant de la retenue mensuel quand le type est \"retenue mensuelle\"");
        }
    }

    @Test
    public void testSetType() throws Exception {
        SoldePourRestitution soldePourRestitution = new SoldePourRestitution();
        soldePourRestitution.setId(genererUnIdUnique());
        soldePourRestitution.setType(TypeSoldePourRestitution.RETENUES);

        soldePourRestitution.setMontantRetenueMensuelle(BigDecimal.ONE);

        soldePourRestitution.setType(TypeSoldePourRestitution.RESTITUTION);

        Assert.assertEquals(
                "Le montant mensuelle doit �tre remis � z�ro si le type restition n'est pas \"retenue mensuelle\"",
                BigDecimal.ZERO, soldePourRestitution.getMontantRetenueMensuelle());
    }

}
