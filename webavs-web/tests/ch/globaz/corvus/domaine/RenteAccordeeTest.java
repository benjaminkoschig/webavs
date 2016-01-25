package ch.globaz.corvus.domaine;

import java.math.BigDecimal;
import junit.framework.Assert;
import org.junit.Test;
import ch.globaz.common.domaine.Periode;
import ch.globaz.corvus.TestUnitaireAvecGenerateurIDUnique;
import ch.globaz.corvus.domaine.constantes.TypePrestationDue;
import ch.globaz.prestation.domaine.CodePrestation;
import ch.globaz.prestation.domaine.EnTeteBlocage;

public class RenteAccordeeTest extends TestUnitaireAvecGenerateurIDUnique {

    @Test
    public void testComporteUnMontantBloque() throws Exception {
        RenteAccordee rente = new RenteAccordee();
        rente.setId(genererUnIdUnique());

        Assert.assertFalse(rente.comporteUnMontantBloque());

        EnTeteBlocage enTeteBlocage = new EnTeteBlocage();
        enTeteBlocage.setId(genererUnIdUnique());
        rente.setEnTeteBlocage(enTeteBlocage);

        Assert.assertFalse(rente.comporteUnMontantBloque());

        enTeteBlocage.ajouterUnBlocagePourLeMois("01.2014", BigDecimal.ONE);

        Assert.assertTrue(rente.comporteUnMontantBloque());
    }

    @Test
    public void testGetMontantRetroactif() throws Exception {
        RenteAccordee renteAccordee = new RenteAccordee();
        renteAccordee.setId(genererUnIdUnique());

        Assert.assertEquals(
                "Si aucune prestation due de type montant rétroactif total n'est présente, le montant rétroactif de la rente accordée est zéro",
                BigDecimal.ZERO, renteAccordee.getMontantRetroactif());

        PrestationDue prestationDue = new PrestationDue();
        prestationDue.setId(genererUnIdUnique());
        prestationDue.setPeriode(new Periode("01.2010", ""));
        prestationDue.setType(TypePrestationDue.PAIEMENT_MENSUEL);
        prestationDue.setMontant(BigDecimal.ONE);

        renteAccordee.ajouterPrestationDue(prestationDue);

        Assert.assertEquals(
                "S'il n'y a que des prestations dues de type \"paiement mensuel\", aucun montant rétroactif ne doit être retourné",
                BigDecimal.ZERO, renteAccordee.getMontantRetroactif());

        PrestationDue prestationDue2 = new PrestationDue();
        prestationDue2.setId(genererUnIdUnique());
        prestationDue2.setPeriode(new Periode("01.2010", "01.2012"));
        prestationDue2.setType(TypePrestationDue.MONTANT_RETROACTIF_TOTAL);
        prestationDue2.setMontant(BigDecimal.TEN);

        renteAccordee.ajouterPrestationDue(prestationDue2);

        Assert.assertEquals(
                "Le montant rétroactif doit correspondre au montant de la prestation due de type \"montant total rétroactif\"",
                BigDecimal.TEN, renteAccordee.getMontantRetroactif());
    }

    @Test
    public void testEstDeLaMemeFamilleDePrestationQue() throws Exception {

        /*
         * On vérifie ici que :
         * - (1) les deux rentes sont du même groupe de prestation : API ou non-API
         * - (2) dans le cas des non-API et que c'est une rente complémentaire pour enfant : que les deux rentes soient
         * issues du même donneur de droit (père ou mère)
         */

        RenteAccordee rente55 = new RenteAccordee();
        rente55.setCodePrestation(CodePrestation.CODE_55);

        RenteAccordee rente81 = new RenteAccordee();
        rente81.setCodePrestation(CodePrestation.CODE_81);

        Assert.assertFalse("Une rente AI et API ne doivent pas être considérées de la même famille",
                rente55.estDeLaMemeFamilleDePrestationQue(rente81));

        RenteAccordee rente82 = new RenteAccordee();
        rente82.setCodePrestation(CodePrestation.CODE_82);

        Assert.assertTrue("Deux rentes API doivent être considérées de la même famille",
                rente81.estDeLaMemeFamilleDePrestationQue(rente82));

        RenteAccordee rente10 = new RenteAccordee();
        rente10.setCodePrestation(CodePrestation.CODE_10);

        Assert.assertTrue("Une rente AI et une rente AVS doivent être considérées de la même famille",
                rente55.estDeLaMemeFamilleDePrestationQue(rente10));

        RenteAccordee rente54 = new RenteAccordee();
        rente54.setCodePrestation(CodePrestation.CODE_54);

        Assert.assertFalse(
                "Cas particulier : deux rentes complémentaires ne découlant pas du même droit (père ou mère) ne doivent pas être considérées comme de la même famille",
                rente54.estDeLaMemeFamilleDePrestationQue(rente55));
    }
}
