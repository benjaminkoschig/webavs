package ch.globaz.vulpecula.domain.specifications.decompte;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.external.models.hercule.InteretsMoratoires;

public class DecompteCTSPInteretsRequisTest {
    private DecompteCTSPInteretsRequis spec;

    @Before
    public void setUp() {
        spec = new DecompteCTSPInteretsRequis();
    }

    @Test
    public void isSatisfiedBy_GivenDecompteComplementaire_ShouldBeTrue() throws UnsatisfiedSpecificationException {
        Decompte decompte = new Decompte();
        decompte.setType(TypeDecompte.COMPLEMENTAIRE);

        assertTrue(spec.isSatisfiedBy(decompte));
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void isSatisfiedBy_GivenDecompteControleEmployeur_ShouldBeFalse() throws UnsatisfiedSpecificationException {
        Decompte decompte = new Decompte();
        decompte.setType(TypeDecompte.CONTROLE_EMPLOYEUR);
        spec.isSatisfiedBy(decompte);
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void isSatisfiedBy_GivenDecompteSpecial_ShouldBeFalse() throws UnsatisfiedSpecificationException {
        Decompte decompte = new Decompte();
        decompte.setType(TypeDecompte.SPECIAL);
        spec.isSatisfiedBy(decompte);
    }

    @Test
    public void isSatisfiedBy_GivenDecompteSpecialWithInterets_ShouldBeTrue() throws UnsatisfiedSpecificationException {
        Decompte decompte = new Decompte();
        decompte.setType(TypeDecompte.SPECIAL);
        decompte.setInteretsMoratoires(InteretsMoratoires.AUTOMATIQUE);
        assertTrue(spec.isSatisfiedBy(decompte));
    }
}
