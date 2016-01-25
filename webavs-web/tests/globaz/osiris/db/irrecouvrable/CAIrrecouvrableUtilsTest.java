package globaz.osiris.db.irrecouvrable;

import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APIVPDetailMontant;
import globaz.osiris.db.ventilation.CAVPTypeDeProcedureOrdre;
import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

public class CAIrrecouvrableUtilsTest {
    @Test
    public void checkParametreSalarieEmployeurTest() {
        CAVPTypeDeProcedureOrdre typeDeProcedureEmployeur = new CAVPTypeDeProcedureOrdre();
        typeDeProcedureEmployeur.setTypeOrdre(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR);
        CAVPTypeDeProcedureOrdre typeDeProcedureSalarie = new CAVPTypeDeProcedureOrdre();
        typeDeProcedureSalarie.setTypeOrdre(APIVPDetailMontant.CS_VP_MONTANT_SALARIE);

        Assert.assertTrue(CAIrrecouvrableUtils.checkParametreSalarieEmployeur(typeDeProcedureEmployeur,
                typeDeProcedureSalarie));
        Assert.assertTrue(CAIrrecouvrableUtils.checkParametreSalarieEmployeur(typeDeProcedureSalarie,
                typeDeProcedureEmployeur));
        Assert.assertFalse(CAIrrecouvrableUtils.checkParametreSalarieEmployeur(typeDeProcedureEmployeur,
                typeDeProcedureEmployeur));
    }

    @Test
    public void isCompensationTest() {
        Assert.assertFalse(CAIrrecouvrableUtils.isCompensation("0"));
        Assert.assertFalse(CAIrrecouvrableUtils.isCompensation(APIRubrique.COMPTE_COURANT_CREANCIER));
        Assert.assertTrue(CAIrrecouvrableUtils.isCompensation(APIRubrique.COMPTE_COMPENSATION));
    }

    @Test
    public void isDateBeforOrEqualTest() {
        Assert.assertTrue(CAIrrecouvrableUtils.isDateBeforOrEqual("01.03.2013", "04.05.2013"));
        Assert.assertTrue(CAIrrecouvrableUtils.isDateBeforOrEqual("04.05.2013", "04.05.2013"));
        Assert.assertFalse(CAIrrecouvrableUtils.isDateBeforOrEqual("04.05.2013", "08.02.2013"));
        Assert.assertTrue(CAIrrecouvrableUtils.isDateBeforOrEqual("04.05.2012", "08.02.2013"));
    }

    @Test
    public void isIdSectionValideTest() {
        Assert.assertTrue(CAIrrecouvrableUtils.isIdSectionValide("1"));
        Assert.assertFalse(CAIrrecouvrableUtils.isIdSectionValide("-1"));
        Assert.assertFalse(CAIrrecouvrableUtils.isIdSectionValide(""));
        Assert.assertFalse(CAIrrecouvrableUtils.isIdSectionValide(null));
    }

    @Test
    public void isMontantNegatifTest() {
        Assert.assertFalse(CAIrrecouvrableUtils.isMontantNegatif(new BigDecimal(1)));
        Assert.assertFalse(CAIrrecouvrableUtils.isMontantNegatif(new BigDecimal(0)));
        Assert.assertTrue(CAIrrecouvrableUtils.isMontantNegatif(new BigDecimal(-1)));
    }

    @Test
    public void isPaiementTest() {
        Assert.assertFalse(CAIrrecouvrableUtils.isPaiement(APIRubrique.COMPTE_COMPENSATION));
        Assert.assertTrue(CAIrrecouvrableUtils.isPaiement(APIRubrique.COMPTE_COURANT_DEBITEUR));
        Assert.assertTrue(CAIrrecouvrableUtils.isPaiement(APIRubrique.COMPTE_COURANT_CREANCIER));
        Assert.assertTrue(CAIrrecouvrableUtils.isPaiement(APIRubrique.COMPTE_FINANCIER));
    }

    @Test
    public void isRubriqueCotPersTest() {
        Assert.assertTrue(CAIrrecouvrableUtils.isRubriqueCotPers("2110.3300.0000"));
        Assert.assertTrue(CAIrrecouvrableUtils.isRubriqueCotPers("2116.3300.0000"));
        Assert.assertFalse(CAIrrecouvrableUtils.isRubriqueCotPers("3111.3300.0000"));
        Assert.assertFalse(CAIrrecouvrableUtils.isRubriqueCotPers("2110.1111.0000"));
        Assert.assertFalse(CAIrrecouvrableUtils.isRubriqueCotPers("1abc"));
    }
}
