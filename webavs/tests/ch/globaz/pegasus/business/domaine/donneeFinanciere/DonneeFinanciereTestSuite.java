package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import ch.globaz.common.domaine.MontantTest;
import ch.globaz.common.domaine.MontantTypePeriodeTest;
import ch.globaz.common.domaine.PartTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.allocationFamilliale.AllocationFamillialeTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.autreApi.AutreApiTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.avsAi.ApiAvsAiTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.assuranceRenteViagere.AssuranceRenteViagereTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.assuranceVie.AssuranceVieTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreDetteProuvee.AutreDetteProuveeTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreFortuneMobiliere.AutreFortuneMobiliereTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente.AutreRenteGenreTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente.AutreRenteTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente.AutresRentesTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente.ConversionDeviseTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRevenue.AutreRevenuTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.betail.BetailTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.BienImmobilierTestSuite;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.capitalLpp.CapitalLppTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.compteBancairePostal.CompteBancairePostalTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.contratEntretienViager.ContratEntretienViagerTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.cotisationPsal.CotisationPsalTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.dessaisissementFortune.DessaisissementFortuneTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.dessaisissementRevenu.DessaisissementRevenuTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.iJAi.IjAiTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.indeminteJournaliereApg.IndemniteJournaliereApgGenreTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.indeminteJournaliereApg.IndemniteJournaliereApgTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.loyer1.LoyerTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.loyer1.LoyerTypeTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.marchandiseStock.MarchandiseStockTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.numeraire.NumeraireTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.pensionAlimentaire.PensionAlimentaireTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.pensionAlimentaire.PensionAlimentaireTypeTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.pensionAlimentaire.PensionsAlimentairesTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.pretEnversTiers.PretEnversTiersTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.regime.RegimeTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.renteAvsAi.RenteAvsAiTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.renteAvsAi.TypeSansRenteTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.revenuActiviteLucrativeIndependante.RevenuActiviteLucrativeIndependanteTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.revenueActiviteLucrativeDependante.RevenuActiviteLucrativeDependanteTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.revenueHypothtique.RevenuHypothtiqueTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.taxeJournalierHome.TaxeJournaliereHomeTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.titre.TitreTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.vehicule.VehiculeTest;

//@formatter:off
    @RunWith(Suite.class)
    @SuiteClasses({DonneeFinanciereTypeTest.class,
                    MontantTest.class,
                    MontantTypePeriodeTest.class,
                    PartTest.class,
                    ProprieteTypeTest.class,
                    CalculRevenuTest.class,
                    
                    BienImmobilierTestSuite.class,
                    TypeSansRenteTest.class,
                    DonneesFinancieresContainerTest.class,
                    IndemniteJournaliereApgGenreTest.class,
                    AutreRenteGenreTest.class,
                    PensionAlimentaireTypeTest.class,
                    PensionsAlimentairesTest.class,
                    LoyerTypeTest.class,
                    AutresRentesTest.class,
                    AllocationFamillialeTest.class,
                    ApiAvsAiTest.class,
                    AssuranceRenteViagereTest.class,
                    AssuranceVieTest.class,
                    AutreApiTest.class,
                    AutreDetteProuveeTest.class,
                    AutreFortuneMobiliereTest.class,
                    AutreRenteTest.class,
                    AutreRevenuTest.class,
                    BetailTest.class,
                    CapitalLppTest.class,
                    CompteBancairePostalTest.class,
                    ContratEntretienViagerTest.class,
                    CotisationPsalTest.class,
                    DessaisissementRevenuTest.class,
                    DessaisissementFortuneTest.class,
                    IjAiTest.class,
                    IndemniteJournaliereApgTest.class,
                    LoyerTest.class,
                    MarchandiseStockTest.class,
                    NumeraireTest.class,
                    PensionAlimentaireTest.class,
                    PretEnversTiersTest.class,
                    RenteAvsAiTest.class,
                    RevenuActiviteLucrativeIndependanteTest.class,
                    RevenuActiviteLucrativeDependanteTest.class,
                    RevenuHypothtiqueTest.class,
                    TaxeJournaliereHomeTest.class,
                    TitreTest.class,
                    VehiculeTest.class,
                    RegimeTest.class,
                    
                    ConversionDeviseTest.class
                })

    public class DonneeFinanciereTestSuite {
        
    }
