package ch.globaz.corvus.domaine;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.corvus.TestUnitaireAvecGenerateurIDUnique;
import ch.globaz.corvus.domaine.constantes.TypeOrdreVersement;

public class DecisionTest extends TestUnitaireAvecGenerateurIDUnique {

    private Decision decision;

    @Before
    public void preparerCasDeTest() {
        decision = new Decision();
        decision.setId(genererUnIdUnique());

        Prestation prestation = new Prestation();
        prestation.setId(genererUnIdUnique());
        decision.setPrestation(prestation);

        Set<OrdreVersement> ordresVersement = new HashSet<OrdreVersement>();
        prestation.setOrdresVersement(ordresVersement);

        OrdreVersement ovBeneficiairePrincipal = new OrdreVersement();
        ovBeneficiairePrincipal.setId(genererUnIdUnique());
        ovBeneficiairePrincipal.setType(TypeOrdreVersement.BENEFICIAIRE_PRINCIPAL);
        ovBeneficiairePrincipal.setMontantCompense(new BigDecimal("20000.00"));
        ordresVersement.add(ovBeneficiairePrincipal);

        OrdreVersement ovInteretMoratoire1 = new OrdreVersement();
        ovInteretMoratoire1.setId(genererUnIdUnique());
        ovInteretMoratoire1.setType(TypeOrdreVersement.INTERET_MORATOIRE);
        ovInteretMoratoire1.setMontantCompense(new BigDecimal("250.00"));
        ordresVersement.add(ovInteretMoratoire1);

        OrdreVersement ovInteretMoratoire2 = new OrdreVersement();
        ovInteretMoratoire2.setId(genererUnIdUnique());
        ovInteretMoratoire2.setType(TypeOrdreVersement.INTERET_MORATOIRE);
        ovInteretMoratoire2.setMontantCompense(new BigDecimal("200.00"));
        ordresVersement.add(ovInteretMoratoire2);

        // Montant total positif pour la décision : 20'450.00

        OrdreVersement ovDette1 = new OrdreVersement();
        ovDette1.setId(genererUnIdUnique());
        ovDette1.setType(TypeOrdreVersement.DETTE);
        ovDette1.setMontantDette(new BigDecimal("1000.00"));
        ovDette1.setMontantCompense(new BigDecimal("1000.00"));
        ovDette1.setCompense(true);
        ordresVersement.add(ovDette1);

        OrdreVersement ovDette2 = new OrdreVersement();
        ovDette2.setId(genererUnIdUnique());
        ovDette2.setType(TypeOrdreVersement.DETTE);
        ovDette2.setMontantDette(new BigDecimal("1500.00"));
        ovDette2.setMontantCompense(new BigDecimal("500.00"));
        ovDette2.setCompense(true);
        ordresVersement.add(ovDette2);

        OrdreVersement ovDette3 = new OrdreVersement();
        ovDette3.setId(genererUnIdUnique());
        ovDette3.setType(TypeOrdreVersement.DETTE);
        ovDette3.setMontantDette(new BigDecimal("10000.00"));
        ovDette3.setMontantCompense(new BigDecimal("10000.00"));
        ovDette3.setCompense(false);
        ordresVersement.add(ovDette3);

        OrdreVersement ovCreancier = new OrdreVersement();
        ovCreancier.setId(genererUnIdUnique());
        ovCreancier.setType(TypeOrdreVersement.CREANCIER);
        ovCreancier.setMontantCompense(new BigDecimal("3000.00"));
        ordresVersement.add(ovCreancier);

        // Montant total dettes (compensées) : 4'500.00
        // --> solde : 15'950.00
    }

    @Test
    public void testGetMontantTotalDettesEtCreances() throws Exception {
        Assert.assertEquals(new BigDecimal("-4500.00"), decision.getMontantTotalDettesEtCreances());
    }

    @Test
    public void testGetMontantTotalGainsBeneficiairePrincipal() throws Exception {
        Assert.assertEquals(new BigDecimal("20450.00"), decision.getMontantTotalGainsBeneficiairePrincipal());
    }

    @Test
    public void testGetOrdresVersementPourType() throws Exception {
        Assert.assertEquals(1, decision.getOrdresVersementPourType(TypeOrdreVersement.BENEFICIAIRE_PRINCIPAL).size());
        Assert.assertEquals(2, decision.getOrdresVersementPourType(TypeOrdreVersement.INTERET_MORATOIRE).size());
        Assert.assertEquals(3, decision.getOrdresVersementPourType(TypeOrdreVersement.DETTE).size());
        Assert.assertEquals(1, decision.getOrdresVersementPourType(TypeOrdreVersement.CREANCIER).size());

        Assert.assertEquals(
                3,
                decision.getOrdresVersementPourType(TypeOrdreVersement.BENEFICIAIRE_PRINCIPAL,
                        TypeOrdreVersement.INTERET_MORATOIRE).size());

        Assert.assertEquals(
                6,
                decision.getOrdresVersementPourType(TypeOrdreVersement.BENEFICIAIRE_PRINCIPAL,
                        TypeOrdreVersement.INTERET_MORATOIRE, TypeOrdreVersement.DETTE).size());

        Assert.assertEquals(
                7,
                decision.getOrdresVersementPourType(TypeOrdreVersement.BENEFICIAIRE_PRINCIPAL,
                        TypeOrdreVersement.INTERET_MORATOIRE, TypeOrdreVersement.DETTE, TypeOrdreVersement.CREANCIER)
                        .size());
    }

    @Test
    public void testGetSolde() throws Exception {
        Assert.assertEquals(new BigDecimal("15950.00"), decision.getSolde());
    }
}
