package globaz.corvus.helpers.decisions;

import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.corvus.TestUnitaireAvecGenerateurIDUnique;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.corvus.domaine.DemandeRenteVieillesse;
import ch.globaz.corvus.domaine.RenteAccordee;
import ch.globaz.corvus.domaine.constantes.EtatDemandeRente;
import ch.globaz.prestation.domaine.CodePrestation;
import ch.globaz.pyxis.domaine.PersonneAVS;

public class REPreparerDecisionSpecifiqueHelperTest extends TestUnitaireAvecGenerateurIDUnique {

    @Test
    public void testDateDeDiminutionDeLaRente() throws Exception {

        PersonneAVS beneficiaire = new PersonneAVS();
        beneficiaire.setId(genererUnIdUnique());

        RenteAccordee ancienneRente = new RenteAccordee();
        ancienneRente.setId(genererUnIdUnique());
        ancienneRente.setBeneficiaire(beneficiaire);
        ancienneRente.setCodePrestation(CodePrestation.CODE_10);
        ancienneRente.setMoisDebut("01.2010");

        DemandeRente nouvelleDemande = new DemandeRenteVieillesse();
        nouvelleDemande.setId(genererUnIdUnique());
        nouvelleDemande.setEtat(EtatDemandeRente.CALCULE);

        RenteAccordee nouvelleRente1 = new RenteAccordee();
        nouvelleRente1.setId(genererUnIdUnique());
        nouvelleRente1.setBeneficiaire(beneficiaire);
        nouvelleRente1.setCodePrestation(CodePrestation.CODE_10);
        nouvelleRente1.setMoisDebut("01.2012");

        nouvelleDemande.setRentesAccordees(Arrays.asList(nouvelleRente1));

        /*
         * Test sans trou dans le droit, doit simplement donner le mois de début de la nouvelle rente moins un mois
         */
        Assert.assertEquals("12.2011",
                REPreparerDecisionSpecifiqueHelper.dateDeDiminutionDeLaRente(ancienneRente, nouvelleDemande));

        RenteAccordee nouvelleRente2 = new RenteAccordee();
        nouvelleRente2.setId(genererUnIdUnique());
        nouvelleRente2.setBeneficiaire(beneficiaire);
        nouvelleRente2.setCodePrestation(CodePrestation.CODE_10);
        nouvelleRente2.setMoisDebut("01.2011");
        nouvelleRente2.setMoisFin("12.2011");

        nouvelleDemande.setRentesAccordees(Arrays.asList(nouvelleRente1, nouvelleRente2));

        /*
         * Test sans trou, mais avec deux rentes dans le nouveau droit. Doit retourner le mois de début de la rente la
         * plus ancienne moins un mois
         */
        Assert.assertEquals("12.2010",
                REPreparerDecisionSpecifiqueHelper.dateDeDiminutionDeLaRente(ancienneRente, nouvelleDemande));

        nouvelleRente1.setMoisDebut("02.2012");
        /*
         * Avec un trou dans le nouveau droit. Doit retourner le mois de début de la rente se trouvant après le trou
         * moins un mois
         */
        Assert.assertEquals("01.2012",
                REPreparerDecisionSpecifiqueHelper.dateDeDiminutionDeLaRente(ancienneRente, nouvelleDemande));

        RenteAccordee nouvelleRente3 = new RenteAccordee();
        nouvelleRente3.setId(genererUnIdUnique());
        nouvelleRente3.setBeneficiaire(beneficiaire);
        nouvelleRente3.setCodePrestation(CodePrestation.CODE_10);
        nouvelleRente3.setMoisDebut("06.2010");
        nouvelleRente3.setMoisFin("11.2010");

        nouvelleDemande.setRentesAccordees(Arrays.asList(nouvelleRente1, nouvelleRente2, nouvelleRente3));
        /*
         * Avec deux trous dans le nouveau droit. Doit retourner le mois de début de la rente se trouvant après le trou
         * le plus récent moins un mois
         */
        Assert.assertEquals("01.2012",
                REPreparerDecisionSpecifiqueHelper.dateDeDiminutionDeLaRente(ancienneRente, nouvelleDemande));

    }
}
