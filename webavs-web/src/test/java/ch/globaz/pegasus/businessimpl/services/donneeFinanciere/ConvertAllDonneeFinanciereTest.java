package ch.globaz.pegasus.businessimpl.services.donneeFinanciere;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.common.domaine.Taux;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresContainer;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.allocationFamilliale.AllocationFamilliale;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.ApiDegre;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.autreApi.AutreApi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.autreApi.AutreApiType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.avsAi.ApiAvsAi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.avsAi.ApiType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.assuranceRenteViagere.AssuranceRenteViagere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.assuranceVie.AssuranceVie;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreDetteProuvee.AutreDetteProuvee;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreFortuneMobiliere.AutreFortuneMobiliere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreFortuneMobiliere.AutreFortuneMobiliereTypeDeFortune;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente.AutreRente;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente.AutreRenteGenre;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente.AutreRenteType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRevenue.AutreRevenu;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.betail.Betail;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.BienImmobilierHabitableType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonHabitable.BienImmobilierNonHabitable;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonHabitable.BienImmobilierNonHabitableType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonPrincipale.BienImmobilierNonPrincipale;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierServantHbitationPrincipale.BienImmobilierServantHabitationPrincipale;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.capitalLpp.CapitalLpp;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.compteBancairePostal.CompteBancairePostal;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.contratEntretienViager.ContratEntretienViager;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.cotisationPsal.CotisationPsal;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.dessaisissementFortune.DessaisissementFortune;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.dessaisissementRevenu.DessaisissementRevenu;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.iJAi.IjAi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.indeminteJournaliereApg.IndemniteJournaliereApg;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.indeminteJournaliereApg.IndemniteJournaliereApgGenre;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.loyer1.Loyer;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.loyer1.LoyerType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.marchandiseStock.MarchandiseStock;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.numeraire.Numeraire;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.pensionAlimentaire.PensionAlimentaire;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.pensionAlimentaire.PensionAlimentaireLienParente;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.pensionAlimentaire.PensionAlimentaireType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.pretEnversTiers.PretEnversTiers;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.renteAvsAi.RenteAvsAi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.renteAvsAi.RenteAvsAiType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.renteAvsAi.TypeSansRente;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.revenuActiviteLucrativeIndependante.RevenuActiviteLucrativeIndependante;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.revenuActiviteLucrativeIndependante.RevenuActiviteLucrativeIndependanteGenreRevenu;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.revenueActiviteLucrativeDependante.RevenuActiviteLucrativeDependante;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.revenueHypothtique.RevenuHypothtique;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.revenueHypothtique.RevenuHypothtiqueMotif;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.taxeJournalierHome.TaxeJournaliereHome;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.titre.Titre;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.vehicule.Vehicule;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaieEtrangereType;
import ch.globaz.pegasus.business.models.revisionquadriennale.DonneeFinanciereComplexModel;

public class ConvertAllDonneeFinanciereTest {
    private static ConvertAllDonneeFinanciere converter = new ConvertAllDonneeFinanciere();
    private static ProprieteType CO_PROPRIETAIRE = ProprieteType.CO_PROPRIETAIRE;
    private static Part PART_1_2 = new Part(1, 2);
    private Montant M_1500 = new Montant(1500);
    private Montant M_2000 = new Montant(2000);
    private Montant M_300000 = new Montant(300000);
    private Montant M_45 = new Montant(45);
    private Montant M_20 = new Montant(20);
    private Montant M_15 = new Montant(15);
    private Montant M_8 = new Montant(8);

    @Test
    public void testConvertRenteAvs() throws Exception {
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(DonneeFinanciereType.RENTE_AVS_AI);
        dr.setRenteAVSAICsType(RenteAvsAiType.RENTE_10.getValue());
        dr.setRenteAVSAICsTypeSansRente(null);
        dr.setRenteAVSAIMontant("1566");

        RenteAvsAi result = convert(dr).getRentesAvsAi().get(0);
        RenteAvsAi expected = new RenteAvsAi(new Montant("1566"), RenteAvsAiType.RENTE_10, TypeSansRente.INDEFINIT,
                BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertIjAi() throws Exception {
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(DonneeFinanciereType.IJAI);
        dr.setIJAIJours("25");
        dr.setIJAIMontant("32");
        IjAi result = convert(dr).getIjAis().get(0);
        IjAi expected = new IjAi(new Montant("32"), 25, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertApiAvsAi() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.API_AVS_AI;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setAPIAVSAIMontant("32");
        dr.setAPIAVSCsType(ApiType.API_81.getValue());
        dr.setAPIAVSCsDegre(ApiDegre.FAIBLE.getValue());
        ApiAvsAi result = convert(dr).getApisAvsAi().get(0);
        ApiAvsAi expected = new ApiAvsAi(new Montant("32"), ApiType.API_81, ApiDegre.FAIBLE, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertAutreRente() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.AUTRE_RENTE;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setAutresRentesAutreGenre("libelle");
        dr.setAutresRentesCsGenre(AutreRenteGenre.AUTRES.getValue());
        dr.setAutresRentesCsType(AutreRenteType.INVALIDITE.getValue());
        dr.setAutresRentesMontant("32");
        dr.setAutreRentesEtrangeresCSTypeDevise(null);
        AutreRente result = convert(dr).getAutresRentes().get(0);
        AutreRente expected = new AutreRente(new Montant("32"), AutreRenteType.INVALIDITE, AutreRenteGenre.AUTRES,
                "libelle", MonnaieEtrangereType.INDEFINIT, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertAutreRenteEtrangere() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.AUTRE_RENTE;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setAutresRentesAutreGenre("libelle");
        dr.setAutresRentesCsGenre(AutreRenteGenre.RENTE_ETRANGERE.getValue());
        dr.setAutresRentesCsType(AutreRenteType.INVALIDITE.getValue());
        dr.setAutresRentesMontant("32");
        dr.setAutreRentesEtrangeresCSTypeDevise(MonnaieEtrangereType.EURO.getValue());
        AutreRente result = convert(dr).getAutresRentes().get(0);
        AutreRente expected = new AutreRente(new Montant("32"), AutreRenteType.INVALIDITE,
                AutreRenteGenre.RENTE_ETRANGERE, "libelle", MonnaieEtrangereType.EURO, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertIndeminteJournaliereApg() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.INDEMNITE_JOURNLIERE_APG;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setIJAPGAutreGenre("libelle");
        dr.setIJAPGcotisationLPPMens("150");
        dr.setIJAPGgainIntAnnuel("200");
        dr.setIJAPGGenre(IndemniteJournaliereApgGenre.APG.getValue());
        dr.setIJAPGMontant("100");
        dr.setIJAPGMontantChomage("124");
        dr.setIJAPGnbJours("2");
        dr.setIJAPGtauxAA("1.1");
        dr.setIJAPGtauxAVS("6.5");

        IndemniteJournaliereApg result = convert(dr).getIndemintesJournaliereApg().get(0);
        IndemniteJournaliereApg expected = new IndemniteJournaliereApg(new Montant(100), new Montant(124), new Montant(
                150), new Montant(200), IndemniteJournaliereApgGenre.APG, 2, new Taux(1.1), new Taux(6.5), "libelle",
                BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertAutreApi() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.AUTRE_API;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setAutresApiAutre("libelle");
        dr.setAutresAPICsTypeMontant(AutreApiType.API_ACCIDENT.getValue());
        dr.setAutresAPIMontant("25");
        dr.setAutresApiCsDegre(ApiDegre.FAIBLE.getValue());
        AutreApi result = convert(dr).getAutresApi().get(0);
        AutreApi expected = new AutreApi(new Montant(25), AutreApiType.API_ACCIDENT, ApiDegre.FAIBLE, "libelle",
                BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertLoyer() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.LOYER;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setLoyerCsTypeLoyer(LoyerType.BRUT_CHARGES_COMPRISES.getValue());
        dr.setLoyerIsFauteuilRoulant(false);
        dr.setLoyerIsTenueMenage(false);
        dr.setLoyerMontant("1200");
        dr.setLoyerMontantCharges("250");
        dr.setLoyerMontantSousLocations("50");
        dr.setLoyerNbPersonnes("2");
        dr.setLoyerTaxeJournalierePensionNonReconnue("100");
        Loyer result = convert(dr).getLoyers().get(0);
        Loyer expected = new Loyer(new Montant(1200), new Montant(250), new Montant(50), new Montant(100),
                LoyerType.BRUT_CHARGES_COMPRISES, 2, false, false, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertTaxeJournaliereHome() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.TAXE_JOURNALIERE_HOME;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setTaxeJournaliereDateEntreeHome("21.01.2015");
        dr.setTaxeJournaliereIdTypeChambre("3");
        dr.setTaxeJournaliereIsParticipationLCA(true);
        dr.setTaxeJournaliereMontantJournalierLCA("11");
        dr.setTaxeJournalierePrimeAPayer("123");
        TaxeJournaliereHome result = convert(dr).getTaxesJournaliereHome().get(0);
        TaxeJournaliereHome expected = new TaxeJournaliereHome(new Montant(11), new Montant(123), true, new Date(
                "21.01.2015"), "1", BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertCompteBancairePostal() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.COMPTE_BANCAIRE_POSTAL;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setCompteBancaireCPPCsTypePropriete(CO_PROPRIETAIRE.getValue());
        dr.setCompteBancaireCPPFractionDenominateur("2");
        dr.setCompteBancaireCPPFractionNumerateur("1");
        dr.setCompteBancaireCPPMontant("1500");
        dr.setCompteBancaireCPPMontantFrais("120");
        dr.setCompteBancaireCPPMontantInterets("15");
        dr.setCompteBancaireIsSansInteret(false);
        CompteBancairePostal result = convert(dr).getComptesBancairePostal().get(0);
        CompteBancairePostal expected = new CompteBancairePostal(new Montant(1500), new Montant(120), new Montant(15),
                PART_1_2, CO_PROPRIETAIRE, false, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertTitre() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.TITRE;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setTitreCsTypePropriete(CO_PROPRIETAIRE.getValue());
        dr.setTitreDroitGarde("100");
        dr.setTitreFractionDenominateur("2");
        dr.setTitreFractionNumerateur("1");
        dr.setTitreIsSansRendement(true);
        dr.setTitreMontant("1500");
        dr.setTitreRendement("25");

        Titre result = convert(dr).getTitres().get(0);
        Titre expected = new Titre(new Montant(1500), new Montant("100"), new Montant(25), CO_PROPRIETAIRE, PART_1_2,
                true, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertAssuranceVie() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.ASSURANCE_VIE;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setAssuranceVieMontantValeurRachat("150");

        AssuranceVie result = convert(dr).getAssurancesVie().get(0);
        AssuranceVie expected = new AssuranceVie(new Montant(150), BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertCapitalLpp() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.CAPITAL_LPP;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setCapitalLPPCsTypePropriete(CO_PROPRIETAIRE.getValue());
        dr.setCapitalLPPFractionDenominateur("2");
        dr.setCapitalLPPFractionNumerateur("1");
        dr.setCapitalLPPIsSansInteret(true);
        dr.setCapitalLPPMontant("150");
        dr.setCapitalLPPMontantFrais("12");
        dr.setCapitalLPPMontantInterets("2");

        CapitalLpp result = convert(dr).getCapitalsLpp().get(0);
        CapitalLpp expected = new CapitalLpp(new Montant(150), new Montant(12), new Montant(2), PART_1_2,
                CO_PROPRIETAIRE, true, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertAutreDetteProuvee() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.AUTRE_DETTE_PROUVEE;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setAutresDettesProuveesMontant("1500");

        AutreDetteProuvee result = convert(dr).getAutresDettesProuvees().get(0);
        AutreDetteProuvee expected = new AutreDetteProuvee(new Montant(1500), BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertPretEnversTiers() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.PRET_ENVERS_TIERS;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setPretEnversTiersCsTypePropriete(CO_PROPRIETAIRE.getValue());
        dr.setPretEnversTiersIsSansInteret(true);
        dr.setPretEnversTiersMontant("150");
        dr.setPretEnversTiersMontantInterets("15");
        dr.setPretEnversTiersPartProprieteDenominateur("2");
        dr.setPretEnversTiersPartProprieteNumerateur("1");

        PretEnversTiers result = convert(dr).getPretsEnversTiers().get(0);
        PretEnversTiers expected = new PretEnversTiers(new Montant(150), new Montant(15), PART_1_2,
                ProprieteType.CO_PROPRIETAIRE, true, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertAssuranceRenteViagere() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.ASSURANCE_RENTE_VIAGERE;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setAssuranceRenteViagereExcedant("5");
        dr.setAssuranceRenteViagereMontant("150");
        dr.setAssuranceRenteViagereMontantValeurRachat("3000");

        AssuranceRenteViagere result = convert(dr).getAssurancesRentesViageres().get(0);
        AssuranceRenteViagere expected = new AssuranceRenteViagere(new Montant(150), new Montant(5), new Montant(3000),
                false, false, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertNumeraire() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.NUMERAIRE;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setNumeraireCsTypePropriete(CO_PROPRIETAIRE.getValue());
        dr.setNumeraireFractionDenominateur("2");
        dr.setNumeraireFractionNumerateur("1");
        dr.setNumeraireIsSansInteret(true);
        dr.setNumeraireMontant("1500");
        dr.setNumeraireMontantInterets("15");

        Numeraire result = convert(dr).getNumeraires().get(0);
        Numeraire expected = new Numeraire(M_1500, M_15, PART_1_2, CO_PROPRIETAIRE, true, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertMarchandiseStock() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.MARCHANDISE_STOCK;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setMarchandiseStockCsTypePropriete(CO_PROPRIETAIRE.getValue());
        dr.setMarchandiseStockFractionDenominateur("2");
        dr.setMarchandiseStockFractionNumerateur("1");
        dr.setMarchandiseStockMontant("1500");

        MarchandiseStock result = convert(dr).getMarchandisesStocks().get(0);
        MarchandiseStock expected = new MarchandiseStock(M_1500, CO_PROPRIETAIRE, PART_1_2, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertVehicule() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.VEHICULE;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setVehiculeCsTypePropriete(CO_PROPRIETAIRE.getValue());
        dr.setVehiculeFractionDenominateur("2");
        dr.setVehiculeFractionNumerateur("1");
        dr.setVehiculeMontant("1500");

        Vehicule result = convert(dr).getVehicules().get(0);
        Vehicule expected = new Vehicule(M_1500, CO_PROPRIETAIRE, PART_1_2, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertBetail() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.BETAIL;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setBetailCsTypePropriete(CO_PROPRIETAIRE.getValue());
        dr.setBetailFractionDenominateur("2");
        dr.setBetailFractionNumerateur("1");
        dr.setBetailMontant("1500");
        Betail result = convert(dr).getBetails().get(0);
        Betail expected = new Betail(M_1500, CO_PROPRIETAIRE, PART_1_2, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertAutreFortuneMobiliere() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.AUTRE_FORTUNE_MOBILIERE;

        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setAutreFortuneMobiliereCsTypeFortune(AutreFortuneMobiliereTypeDeFortune.AUTRES.getValue());
        dr.setAutreFortuneMobiliereCsTypePropriete(CO_PROPRIETAIRE.getValue());
        dr.setAutreFortuneMobiliereFractionDenominateur("2");
        dr.setAutreFortuneMobiliereFractionNumerateur("1");
        dr.setAutreFortuneMobiliereMontant("1500");
        AutreFortuneMobiliere result = convert(dr).getAutresFortunesMobilieres().get(0);
        AutreFortuneMobiliere expected = new AutreFortuneMobiliere(M_1500, CO_PROPRIETAIRE, PART_1_2,
                AutreFortuneMobiliereTypeDeFortune.AUTRES, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertRevenuActiviteLucrativeIndependante() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.REVENU_ACTIVITE_LUCRATIVE_INDEPENDANTE;

        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setRevenuActiviteLucrativeIndependanteCSGenreRevenu(RevenuActiviteLucrativeIndependanteGenreRevenu.GENRE_REVENU_ACT_LUCR_AGRICOLE_FORESTIER
                .getValue());
        dr.setRevenuActiviteLucrativeIndependanteMontant("1500");
        RevenuActiviteLucrativeIndependante result = convert(dr).getRevenusActiviteLucrativeIndependante().get(0);

        RevenuActiviteLucrativeIndependante expected = new RevenuActiviteLucrativeIndependante(M_1500,
                RevenuActiviteLucrativeIndependanteGenreRevenu.GENRE_REVENU_ACT_LUCR_AGRICOLE_FORESTIER,
                BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertRevenueHypothtique() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.REVENU_HYPOTHETIQUE;

        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setRevenuHypothetiqueMontantDeductionsLPP("8");
        dr.setRevenuHypothetiqueMontantDeductionsSociales("20");
        dr.setRevenuHypothetiqueMontantFraisGarde("15");
        dr.setRevenuHypothetiqueMontantRevenuBrut("2000");
        dr.setRevenuHypothetiqueMontantRevenuNet("1500");
        dr.setRevenuHypothetiqueMotif(RevenuHypothtiqueMotif.MOTIF_REVENU_HYPO_14A_OPC.getValue());

        RevenuHypothtique result = convert(dr).getRevenusHypothtique().get(0);

        RevenuHypothtique expected = new RevenuHypothtique(M_1500, M_2000, M_15, M_20, M_8,
                RevenuHypothtiqueMotif.MOTIF_REVENU_HYPO_14A_OPC, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertAllocationFamilliale() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.ALLOCATION_FAMILIALLE;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setAllocationFamilialeMontantMensuel("1500");
        AllocationFamilliale result = convert(dr).getAllocationsFamilliale().get(0);
        AllocationFamilliale expected = new AllocationFamilliale(M_1500, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertContratEntretienViager() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.CONTRAT_ENTRETIEN_VIAGER;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setContratEntretienViagerMontant("1500");
        ContratEntretienViager result = convert(dr).getContratsEntretienViager().get(0);
        ContratEntretienViager expected = new ContratEntretienViager(M_1500, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertAutreRevenue() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.AUTRE_REVENU;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setAutresRevenusMontant("1500");
        dr.setAutresRevenusLibelle("libelle");
        AutreRevenu result = convert(dr).getAutresRevenus().get(0);
        AutreRevenu expected = new AutreRevenu(M_1500, "libelle", BuilderDf.createDF());

        assertEquals(expected, result);
        assertTrue(expected.getMontant().isAnnuel());
    }

    @Test
    public void testConvertCotisationPsal() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.COTISATION_PSAL;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setCotisationPSALMontantAnnuel("1500");

        CotisationPsal result = convert(dr).getCotisationsPsal().get(0);
        CotisationPsal expected = new CotisationPsal(M_1500, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertPensionAlimentaire() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.PENSION_ALIMENTAIRE;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setPensionAlimentaireCsTypePension(PensionAlimentaireType.DUE.getValue());
        dr.setPensionAlimentaireIsDeductionsRenteEnfant(true);
        dr.setPensionAlimentaireLienParente(PensionAlimentaireLienParente.AUTRES.getValue());
        dr.setPensionAlimentaireMontant("1500");
        dr.setPensionAlimentaireMontantRenteEnfant("15");

        PensionAlimentaire result = convert(dr).getPensionsAlimentaire().get(0);
        PensionAlimentaire expected = new PensionAlimentaire(M_1500, M_15, PensionAlimentaireType.DUE,
                PensionAlimentaireLienParente.AUTRES, true, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertDessaississementFortune() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.DESSAISISSEMENT_FORTUNE;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setDessaisissementFortuneDeductions("15");
        dr.setDessaisissementFortuneMontant("1500");

        DessaisissementFortune result = convert(dr).getDessaississementsFortune().get(0);
        DessaisissementFortune expected = new DessaisissementFortune(M_1500, M_15, BuilderDf.createDF());
        assertEquals(expected, result);
    }

    @Test
    public void testConvertDessaisissementRevenu() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.DESSAISISSEMENT_REVENU;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setDessaisissementRevenuDeductions("15");
        dr.setDessaisissementRevenuMontant("1500");

        DessaisissementRevenu result = convert(dr).getDessaisissementsRevenu().get(0);
        DessaisissementRevenu expected = new DessaisissementRevenu(M_1500, M_15, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertRevenueActiviteLucrativeDependante() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.REVENU_ACTIVITE_LUCRATIVE_DEPENDANTE;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setRevenuActiviteLucrativeDependanteDeductionsLPP("15");
        dr.setRevenuActiviteLucrativeDependanteDeductionsSociales("8");
        dr.setRevenuActiviteLucrativeDependanteMontant("1500");
        dr.setRevenuActiviteLucrativeDependanteMontantFraisEffectifs("20");

        RevenuActiviteLucrativeDependante result = convert(dr).getRevenusActiviteLucrativeDependante().get(0);
        RevenuActiviteLucrativeDependante expected = new RevenuActiviteLucrativeDependante(M_1500, M_15, M_8, M_20,
                BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertBienImmobilierServantHbitationPrincipale() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setBienImmoPrincipalCSPropriete(CO_PROPRIETAIRE.getValue());
        dr.setBienImmoPrincipalMontantDetteHypothecaire("2000");
        dr.setBienImmoPrincipalMontantInteretHypothecaire("20");
        dr.setBienImmoPrincipalMontantLoyersEncaisses("1500");
        dr.setBienImmoPrincipalMontantSousLocation("15");
        dr.setBienImmoPrincipalMontantValeurFiscale("300000");
        dr.setBienImmoPrincipalMontantValeurLocative("45");
        dr.setBienImmoPrincipalNombrePersonnes("2");
        dr.setBienImmoPrincipalPartDenominateur("2");
        dr.setBienImmoPrincipalPartNumerateur("1");
        dr.setBienImmoPrincipalCsTypeDeBien(BienImmobilierHabitableType.APPARTEMENT.getValue());

        BienImmobilierServantHabitationPrincipale result = convert(dr).getBiensImmobiliersServantHbitationPrincipale()
                .get(0);
        BienImmobilierServantHabitationPrincipale expected = new BienImmobilierServantHabitationPrincipale(M_300000,
                M_45, M_20, M_1500, M_15, M_2000, 2, BienImmobilierHabitableType.APPARTEMENT, PART_1_2,
                CO_PROPRIETAIRE, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertBienImmobilierNonHabitable() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.BIEN_IMMOBILIER_NON_HABITABLE;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setBienImmoNonHabitableCsTypePropriete(CO_PROPRIETAIRE.getValue());
        dr.setBienImmoNonHabitableMontantDetteHypothecaire("2000");
        dr.setBienImmoNonHabitableMontantInteretHypothecaire("15");
        dr.setBienImmoNonHabitableMontantRendement("20");
        dr.setBienImmoNonHabitableMontantValeurVenale("300000");
        dr.setBienImmoNonHabitablePartDenominateur("2");
        dr.setBienImmoNonHabitablePartNumerateur("1");
        dr.setBienImmoNonHabitableCsTypeDeBien(BienImmobilierNonHabitableType.AISANCE.getValue());

        BienImmobilierNonHabitable result = convert(dr).getBiensImmobiliersNonHabitable().get(0);
        BienImmobilierNonHabitable expected = new BienImmobilierNonHabitable(M_20, M_300000, M_15, M_2000,
                BienImmobilierNonHabitableType.AISANCE, PART_1_2, CO_PROPRIETAIRE, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    @Test
    public void testConvertBienImmobilierNonPrincipale() throws Exception {
        DonneeFinanciereType type = DonneeFinanciereType.BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE;
        DonneeFinanciereComplexModel dr = createBaseDonneFinanciere(type);
        dr.setBienImmoAnnexeCsTypePropriete(CO_PROPRIETAIRE.getValue());
        dr.setBienImmoAnnexeMontantDetteHypothecaire("2000");
        dr.setBienImmoAnnexeMontantInteretHypothecaire("20");
        dr.setBienImmoAnnexeMontantLoyersEncaisses("1500");
        dr.setBienImmoAnnexeMontantSousLocation("15");
        dr.setBienImmoAnnexeMontantValeurLocative("8");
        dr.setBienImmoAnnexeMontantValeurVenale("300000");
        dr.setBienImmoAnnexePartDenominateur("2");
        dr.setBienImmoAnnexePartNumerateur("1");
        dr.setBienImmoAnnexeCsTypeDeBien(BienImmobilierHabitableType.APPARTEMENT.getValue());

        BienImmobilierNonPrincipale result = convert(dr).getBiensImmobiliersNonPrincipale().get(0);
        BienImmobilierNonPrincipale expected = new BienImmobilierNonPrincipale(M_300000, M_20, M_1500, M_15, M_8,
                M_2000, BienImmobilierHabitableType.APPARTEMENT, PART_1_2, CO_PROPRIETAIRE, BuilderDf.createDF());

        assertEquals(expected, result);
    }

    private DonneesFinancieresContainer convert(DonneeFinanciereComplexModel dr) {
        List<DonneeFinanciereComplexModel> list = new ArrayList<DonneeFinanciereComplexModel>();
        list.add(dr);
        DonneesFinancieresContainer dfs = converter.convertToDomain(list);
        return dfs;
    }

    private DonneeFinanciereComplexModel createBaseDonneFinanciere(DonneeFinanciereType donneeFinanciereType) {
        DonneeFinanciereComplexModel dr = new DonneeFinanciereComplexModel();
        dr.setCsTypeDonneeFinanciere(donneeFinanciereType.getValue());
        dr.setCsRoleFamille(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
        dr.setDateDebutDonneeFinanciere("01.2014");
        dr.setDateFinDonneeFinanciere(null);
        dr.setIdDonneeFinanciereHeader("20");
        dr.setIdDroitMembreFamille("1");
        return dr;
    }

    @Test
    public void testToInteger() throws Exception {
        assertEquals(new Integer(0), converter.toInteger(null));
        assertEquals(new Integer(0), converter.toInteger(""));
        assertEquals(new Integer(0), converter.toInteger("   "));
        assertEquals(new Integer(0), converter.toInteger("0"));
        assertEquals(new Integer(10), converter.toInteger("10"));
    }

    @Test
    public void testToMontant() throws Exception {
        assertEquals(new Montant(0), converter.toMontant(null));
        assertEquals(new Montant(0), converter.toMontant(""));
        assertEquals(new Montant(0), converter.toMontant("   "));
        assertEquals(new Montant(0), converter.toMontant("0"));
        assertEquals(new Montant(10), converter.toMontant("10"));
    }

}
