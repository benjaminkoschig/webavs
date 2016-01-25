package ch.globaz.al.businessimpl.services.calcul;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSCantons;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.services.calcul.CalculService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALTestCaseJU4;

public class CalculServiceImplTest extends ALTestCaseJU4 {

    @Ignore
    @Test
    public void testGetAgeForCalcul() {

        try {
            CalculService s = ALImplServiceLocator.getCalculService();

            String nais = "08.07.1998";
            Assert.assertEquals(1, s.getAgeForCalcul(nais, "01.07.1998"));
            Assert.assertEquals(1, s.getAgeForCalcul(nais, "31.07.1998"));
            Assert.assertEquals(1, s.getAgeForCalcul(nais, "01.08.1998"));
            Assert.assertEquals(1, s.getAgeForCalcul(nais, "31.08.1998"));

            Assert.assertEquals(1, s.getAgeForCalcul(nais, "01.07.1999"));
            Assert.assertEquals(1, s.getAgeForCalcul(nais, "31.07.1999"));
            Assert.assertEquals(2, s.getAgeForCalcul(nais, "01.08.1999"));
            Assert.assertEquals(2, s.getAgeForCalcul(nais, "31.08.1999"));

            nais = "01.07.1998";
            Assert.assertEquals(1, s.getAgeForCalcul(nais, "01.07.1998"));
            Assert.assertEquals(1, s.getAgeForCalcul(nais, "31.07.1998"));
            Assert.assertEquals(1, s.getAgeForCalcul(nais, "01.08.1998"));
            Assert.assertEquals(1, s.getAgeForCalcul(nais, "31.08.1998"));

            Assert.assertEquals(1, s.getAgeForCalcul(nais, "01.07.1999"));
            Assert.assertEquals(1, s.getAgeForCalcul(nais, "31.07.1999"));
            Assert.assertEquals(2, s.getAgeForCalcul(nais, "01.08.1999"));
            Assert.assertEquals(2, s.getAgeForCalcul(nais, "31.08.1999"));

            nais = "08.07.1998";
            Assert.assertEquals(12, s.getAgeForCalcul(nais, "30.06.2010"));
            Assert.assertEquals(12, s.getAgeForCalcul(nais, "01.07.2010"));
            Assert.assertEquals(12, s.getAgeForCalcul(nais, "31.07.2010"));
            Assert.assertEquals(13, s.getAgeForCalcul(nais, "01.08.2010"));
            Assert.assertEquals(13, s.getAgeForCalcul(nais, "31.08.2010"));
            Assert.assertEquals(13, s.getAgeForCalcul(nais, "07.09.2010"));

            nais = "01.07.1998";
            Assert.assertEquals(12, s.getAgeForCalcul(nais, "30.06.2010"));
            Assert.assertEquals(12, s.getAgeForCalcul(nais, "01.07.2010"));
            Assert.assertEquals(12, s.getAgeForCalcul(nais, "31.07.2010"));
            Assert.assertEquals(13, s.getAgeForCalcul(nais, "01.08.2010"));
            Assert.assertEquals(13, s.getAgeForCalcul(nais, "31.08.2010"));
            Assert.assertEquals(13, s.getAgeForCalcul(nais, "07.09.2010"));

            nais = "08.07.1998";
            Assert.assertEquals(16, s.getAgeForCalcul(nais, "30.06.2014"));
            Assert.assertEquals(16, s.getAgeForCalcul(nais, "01.07.2014"));
            Assert.assertEquals(16, s.getAgeForCalcul(nais, "31.07.2014"));
            Assert.assertEquals(17, s.getAgeForCalcul(nais, "01.08.2014"));
            Assert.assertEquals(17, s.getAgeForCalcul(nais, "31.08.2014"));
            Assert.assertEquals(17, s.getAgeForCalcul(nais, "07.09.2014"));

            nais = "01.07.1998";
            Assert.assertEquals(16, s.getAgeForCalcul(nais, "30.06.2014"));
            Assert.assertEquals(16, s.getAgeForCalcul(nais, "01.07.2014"));
            Assert.assertEquals(16, s.getAgeForCalcul(nais, "31.07.2014"));
            Assert.assertEquals(17, s.getAgeForCalcul(nais, "01.08.2014"));
            Assert.assertEquals(17, s.getAgeForCalcul(nais, "31.08.2014"));
            Assert.assertEquals(17, s.getAgeForCalcul(nais, "07.09.2014"));

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            doFinally();
        }
    }

    @Test
    @Ignore
    public void testGetCalcul() {
        Assert.fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testGetCantonForTarif() {
        try {
            CalculService s = ALImplServiceLocator.getCalculService();

            Assert.assertEquals(ALCSCantons.AG, s.getCantonForTarif(ALCSTarif.CATEGORIE_AG));
            Assert.assertEquals(ALCSCantons.AI, s.getCantonForTarif(ALCSTarif.CATEGORIE_AI));
            Assert.assertEquals(ALCSCantons.AR, s.getCantonForTarif(ALCSTarif.CATEGORIE_AR));
            Assert.assertEquals(ALCSCantons.BE, s.getCantonForTarif(ALCSTarif.CATEGORIE_BE));
            Assert.assertEquals(ALCSCantons.BL, s.getCantonForTarif(ALCSTarif.CATEGORIE_BL));
            Assert.assertEquals(ALCSCantons.BS, s.getCantonForTarif(ALCSTarif.CATEGORIE_BS));
            Assert.assertEquals(ALCSCantons.GE, s.getCantonForTarif(ALCSTarif.CATEGORIE_GE));
            Assert.assertEquals(ALCSCantons.GL, s.getCantonForTarif(ALCSTarif.CATEGORIE_GL));
            Assert.assertEquals(ALCSCantons.GR, s.getCantonForTarif(ALCSTarif.CATEGORIE_GR));
            Assert.assertEquals(ALCSCantons.JU, s.getCantonForTarif(ALCSTarif.CATEGORIE_JU));
            Assert.assertEquals(ALCSCantons.LU, s.getCantonForTarif(ALCSTarif.CATEGORIE_LU));
            Assert.assertEquals(ALCSCantons.NE, s.getCantonForTarif(ALCSTarif.CATEGORIE_NE));
            Assert.assertEquals(ALCSCantons.NW, s.getCantonForTarif(ALCSTarif.CATEGORIE_NW));
            Assert.assertEquals(ALCSCantons.OW, s.getCantonForTarif(ALCSTarif.CATEGORIE_OW));
            Assert.assertEquals(ALCSCantons.SG, s.getCantonForTarif(ALCSTarif.CATEGORIE_SG));
            Assert.assertEquals(ALCSCantons.SH, s.getCantonForTarif(ALCSTarif.CATEGORIE_SH));
            Assert.assertEquals(ALCSCantons.SO, s.getCantonForTarif(ALCSTarif.CATEGORIE_SO));
            Assert.assertEquals(ALCSCantons.SZ, s.getCantonForTarif(ALCSTarif.CATEGORIE_SZ));
            Assert.assertEquals(ALCSCantons.TG, s.getCantonForTarif(ALCSTarif.CATEGORIE_TG));
            Assert.assertEquals(ALCSCantons.TI, s.getCantonForTarif(ALCSTarif.CATEGORIE_TI));
            Assert.assertEquals(ALCSCantons.UR, s.getCantonForTarif(ALCSTarif.CATEGORIE_UR));
            Assert.assertEquals(ALCSCantons.VD, s.getCantonForTarif(ALCSTarif.CATEGORIE_VD));
            Assert.assertEquals(ALCSCantons.VS, s.getCantonForTarif(ALCSTarif.CATEGORIE_VS));
            Assert.assertEquals(ALCSCantons.ZG, s.getCantonForTarif(ALCSTarif.CATEGORIE_ZG));
            Assert.assertEquals(ALCSCantons.ZH, s.getCantonForTarif(ALCSTarif.CATEGORIE_ZH));
            Assert.assertEquals(ALCSCantons.VD, s.getCantonForTarif(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS));

            try {
                s.getCantonForTarif(ALCSTarif.CATEGORIE_CATMP);
                Assert.fail("L'exception de type ALCalculException n'a pas été levée");
            } catch (ALCalculException e) {
                e.getMessage();
            }
            try {
                s.getCantonForTarif(ALCSTarif.CATEGORIE_CATMP);
                Assert.fail("L'exception de type ALCalculException n'a pas été levée");
            } catch (ALCalculException e) {
                e.getMessage();
            }
            try {
                s.getCantonForTarif(ALCSTarif.CATEGORIE_FPV_BANQUES);
                Assert.fail("L'exception de type ALCalculException n'a pas été levée");
            } catch (ALCalculException e) {
                e.getMessage();
            }
            try {
                s.getCantonForTarif(ALCSTarif.CATEGORIE_LFM);
                Assert.fail("L'exception de type ALCalculException n'a pas été levée");
            } catch (ALCalculException e) {
                e.getMessage();
            }
            try {
                s.getCantonForTarif(ALCSTarif.CATEGORIE_H51X);
                Assert.fail("L'exception de type ALCalculException n'a pas été levée");
            } catch (ALCalculException e) {
                e.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            doFinally();
        }
    }

    @Test
    @Ignore
    public void testGetTarifForCanton() {
        Assert.fail("Not yet implemented");
    }

    @Test
    @Ignore
    public void testIsDateLAFam() {
        Assert.fail("Not yet implemented");
    }
}
