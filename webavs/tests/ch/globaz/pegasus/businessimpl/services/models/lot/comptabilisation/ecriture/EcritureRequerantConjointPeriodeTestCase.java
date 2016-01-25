package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import org.junit.Assert;
import org.junit.Test;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;

public class EcritureRequerantConjointPeriodeTestCase {

    @Test
    public void testAddEcritureConjointBeneficiaire() throws ComptabiliserLotException {
        EcritureRequerantConjointPeriode periode = new EcritureRequerantConjointPeriode();
        Ecriture e = EcritureFactory.generateEcrituresBeneficiaire(150, IPCDroits.CS_ROLE_FAMILLE_CONJOINT, "10");
        periode.addEcriture(e);
        Assert.assertEquals(periode.getConjoint().getBeneficiaire(), e);
    }

    @Test
    public void testAddEcritureConjointRestitution() throws ComptabiliserLotException {
        EcritureRequerantConjointPeriode periode = new EcritureRequerantConjointPeriode();
        Ecriture e = EcritureFactory.generateEcrituresResitution(150, IPCDroits.CS_ROLE_FAMILLE_CONJOINT, "10");
        periode.addEcriture(e);
        Assert.assertEquals(periode.getConjoint().getRestitution(), e);
    }

    @Test
    public void testAddEcritureRequerantBeneficiaire() throws ComptabiliserLotException {

        EcritureRequerantConjointPeriode periode = new EcritureRequerantConjointPeriode();
        Ecriture e = EcritureFactory.generateEcrituresBeneficiaire(150, IPCDroits.CS_ROLE_FAMILLE_REQUERANT, "10");
        periode.addEcriture(e);

        Assert.assertEquals(periode.getRequerant().getBeneficiaire(), e);
    }

    @Test
    public void testAddEcritureRequerantRestitution() throws ComptabiliserLotException {
        EcritureRequerantConjointPeriode periode = new EcritureRequerantConjointPeriode();
        Ecriture e = EcritureFactory.generateEcrituresResitution(150, IPCDroits.CS_ROLE_FAMILLE_REQUERANT, "10");
        periode.addEcriture(e);
        Assert.assertEquals(periode.getRequerant().getRestitution(), e);
    }

    @Test
    public void testHasBeneficiaireConjointFalse() throws ComptabiliserLotException {
        EcritureRequerantConjointPeriode periode = new EcritureRequerantConjointPeriode();
        Ecriture e = EcritureFactory.generateEcrituresResitution(150, IPCDroits.CS_ROLE_FAMILLE_REQUERANT, "10");
        periode.addEcriture(e);
        Assert.assertFalse(periode.hasBeneficiaireConjoint());
    }

    @Test
    public void testHasBeneficiaireRequerantTrue() throws ComptabiliserLotException {
        EcritureRequerantConjointPeriode periode = new EcritureRequerantConjointPeriode();
        Ecriture e = EcritureFactory.generateEcrituresResitution(150, IPCDroits.CS_ROLE_FAMILLE_REQUERANT, "10");
        periode.addEcriture(e);
        Assert.assertFalse(periode.hasBeneficiaireConjoint());
    }

    @Test
    public void testIsBeneficiaireDom2Rtrue() throws ComptabiliserLotException {
        EcritureRequerantConjointPeriode periode = new EcritureRequerantConjointPeriode();
        Ecriture e = EcritureFactory.generateEcrituresResitution(150, IPCDroits.CS_ROLE_FAMILLE_REQUERANT, "10");
        periode.addEcriture(e);
        Assert.assertFalse(periode.hasBeneficiaireConjoint());
    }

}
