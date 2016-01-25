package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierServantHbitationPrincipale.BienImmobilierServantHabitationPrincipale;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariableMetier;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariableMetierType;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariableMetierTypeDeDonnee;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariablesMetier;

public class FraisEntretiensImmeubleTest {

    private BienImmobilierServantHabitationPrincipale bienImmobilier = new BienImmobilierServantHabitationPrincipale(
            new Montant(500000), new Montant(50), new Montant(20), new Montant(50), new Montant(100), new Montant(
                    100000), 2, BienImmobilierHabitableType.APPARTEMENT, new Part(1, 2), ProprieteType.PROPRIETAIRE,
            BuilderDf.createDF());

    @Test
    public void testCompute() throws Exception {
        FraisEntretiensImmeuble fraisEntretiensImmeuble = buildFraisEntretient();
        Montant frais = fraisEntretiensImmeuble.compute(Montant.newAnnuel(50),
                buildVaraible(new Date("01.2015"), new Part(1, 5)));
        assertEquals(Montant.newAnnuel(10), frais);
    }

    @Test
    public void testComputeBrutBienImmobilierDate() throws Exception {
        FraisEntretiensImmeuble fraisEntretiensImmeuble = buildFraisEntretient();
        Montant frais = fraisEntretiensImmeuble.computeBrut(bienImmobilier, new Date("01.2014"));
        assertEquals(Montant.newAnnuel(200), frais);
    }

    @Test
    public void testComputeBrutBienImmobilier() throws Exception {
        FraisEntretiensImmeuble fraisEntretiensImmeuble = buildFraisEntretient();
        Montant frais = fraisEntretiensImmeuble.computeBrut(bienImmobilier);
        assertEquals(Montant.newAnnuel(40), frais);
    }

    private FraisEntretiensImmeuble buildFraisEntretient() {
        VariablesMetier variablesMetier = new VariablesMetier();
        variablesMetier.add(buildVaraible(new Date("01.2015"), new Part(1, 5)));
        variablesMetier.add(buildVaraible(new Date("01.2014"), new Part(1, 1)));
        FraisEntretiensImmeuble fraisEntretiensImmeuble = new FraisEntretiensImmeuble(variablesMetier);
        return fraisEntretiensImmeuble;
    }

    private VariableMetier buildVaraible(Date date, Part part) {
        VariableMetier variableMetier = new VariableMetier();
        variableMetier.setDateDebut(date);
        variableMetier.setId("2");
        variableMetier.setPart(part);
        variableMetier.setType(VariableMetierType.FRAIS_ENTRETIEN_IMMEUBLE);
        variableMetier.setTypeDeDonnee(VariableMetierTypeDeDonnee.FRACTION);
        return variableMetier;
    }
}
