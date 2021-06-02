package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonHabitable.BienImmobilierNonHabitable;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonHabitable.BienImmobilierNonHabitableType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonPrincipale.BienImmobilierNonPrincipale;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierServantHbitationPrincipale.BienImmobilierServantHabitationPrincipale;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariablesMetier;

public class BiensImmobiliersTest {

    @Test
    public void testSumFraisEntretientBrut() throws Exception {
        FraisEntretiensImmeuble fraisEntretiensImmeuble = spy(new FraisEntretiensImmeuble(mock(VariablesMetier.class)));
        doReturn(Montant.newAnnuel(10)).when(fraisEntretiensImmeuble).computeBrut(any(BienImmobilier.class));

        BiensImmobiliersListBase biensImmobiliers = new BiensImmobiliersListBase();

        biensImmobiliers.add(new BienImmobilierNonHabitable(new Montant(10), new Montant(6000), new Montant(20),
                new Montant(1000), BienImmobilierNonHabitableType.AISANCE, new Part(1, 2), ProprieteType.PROPRIETAIRE,
                BuilderDf.createDF()));

        biensImmobiliers.add(new BienImmobilierNonPrincipale(new Montant(5000), new Montant(20), new Montant(30),
                new Montant(70), new Montant(100), new Montant(1000), BienImmobilierHabitableType.APPARTEMENT,
                new Part(1, 2), ProprieteType.PROPRIETAIRE, BuilderDf.createDF()));

        biensImmobiliers.add(new BienImmobilierServantHabitationPrincipale(new Montant(300000), new Montant(45),
                new Montant(20), new Montant(1500), new Montant(15), new Montant(100000), 2,
                BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.PROPRIETAIRE, BuilderDf
                        .createDF()));

        assertEquals(Montant.newAnnuel(20), biensImmobiliers.sumFraisEntretientBrut(fraisEntretiensImmeuble));

    }
}
