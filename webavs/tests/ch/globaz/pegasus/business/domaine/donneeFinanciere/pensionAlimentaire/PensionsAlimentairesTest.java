package ch.globaz.pegasus.business.domaine.donneeFinanciere.pensionAlimentaire;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresListBase;

public class PensionsAlimentairesTest {

    @Test
    public void testGetPensionAlimentaireByType() throws Exception {
        DonneesFinancieresListBase<PensionAlimentaire> list = new DonneesFinancieresListBase<PensionAlimentaire>();
        list.add(new PensionAlimentaire(new Montant(50), new Montant(10), PensionAlimentaireType.DUE,
                PensionAlimentaireLienParente.AUTRES, false, BuilderDf.createDF()));

        list.add(new PensionAlimentaire(new Montant(50), new Montant(10), PensionAlimentaireType.VERSEE,
                PensionAlimentaireLienParente.AUTRES, false, BuilderDf.createDF()));

        assertEquals(list.get(0), PensionsAlimentaires.getPensionAlimentaireByType(list, PensionAlimentaireType.DUE)
                .get(0));
        assertEquals(list.get(1), PensionsAlimentaires.getPensionAlimentaireByType(list, PensionAlimentaireType.VERSEE)
                .get(0));

    }

}
