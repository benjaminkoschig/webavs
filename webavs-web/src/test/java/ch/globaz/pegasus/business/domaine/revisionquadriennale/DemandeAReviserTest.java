package ch.globaz.pegasus.business.domaine.revisionquadriennale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.common.domaine.Adresse;
import ch.globaz.common.domaine.Canton;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.common.domaine.Taux;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresContainer;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.allocationFamilliale.AllocationFamilliale;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.ApiDegre;
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
import ch.globaz.pegasus.business.domaine.donneeFinanciere.regime.Regime;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.renteAvsAi.RenteAvsAi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.renteAvsAi.RenteAvsAiType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.renteAvsAi.TypeSansRente;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.revenuActiviteLucrativeIndependante.RevenuActiviteLucrativeIndependante;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.revenuActiviteLucrativeIndependante.RevenuActiviteLucrativeIndependanteGenreRevenu;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.revenueActiviteLucrativeDependante.RevenuActiviteLucrativeDependante;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.revenueHypothtique.RevenuHypothtique;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.revenueHypothtique.RevenuHypothtiqueMotif;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.titre.Titre;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.vehicule.Vehicule;
import ch.globaz.pegasus.business.domaine.membreFamille.MembresFamilles;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.domaine.parametre.Parameters;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaieEtrangereType;
import ch.globaz.pegasus.business.domaine.pca.PcaRequerantConjoint;
import ch.globaz.pegasus.business.models.revisionquadriennale.ListrevisionWithPcaRequerantConjoint;
import ch.globaz.pyxis.domaine.Pays;

public class DemandeAReviserTest {

    private static ProprieteType PROPRIETAIRE = ProprieteType.PROPRIETAIRE;
    private static ProprieteType USUFRUITIER = ProprieteType.USUFRUITIER;
    private static ProprieteType DROIT_HABITATION = ProprieteType.DROIT_HABITATION;
    private static Part PART_1_2 = new Part(1, 2);
    private static Part PART_1_1 = new Part(1, 1);

    private DonneeFinanciere DF_REQ = BuilderDf.createDF(RoleMembreFamille.REQUERANT);
    private DonneeFinanciere DF_CONJ = BuilderDf.createDF(RoleMembreFamille.CONJOINT);
    private DonneeFinanciere DF_ENF = BuilderDf.createDF(RoleMembreFamille.ENFANT);

    private Montant M_1000 = new Montant(1000);
    private Montant M_100 = new Montant(100);
    private Montant M_2000 = new Montant(2000);
    private Montant M_300000 = new Montant(300000);
    private Montant M_100000 = new Montant(100000);
    private Montant M_INTERET = new Montant(40);
    private Montant M_20 = new Montant(20);
    private Montant M_15 = new Montant(15);
    private Montant M_10 = new Montant(10);
    private Montant M_5 = new Montant(5);
    private Montant M_SOUS_LOCATION = new Montant(5);
    private Montant M_DETTE = new Montant(10000);

    private String L_AUTRE_RENTE_ETRANGERE = "Une rente étrangère";

    private DonneesFinancieresContainer container = new DonneesFinancieresContainer();
    private DemandeAReviser demandeAReviser = new DemandeAReviser(container,
            new ListrevisionWithPcaRequerantConjoint(), new Adresse(), new Adresse(), new Pays(),
            new PcaRequerantConjoint(), new MembresFamilles(), new Parameters());

    public DemandeAReviserTest() {
        // testGetFortuneArgent
        // container.add(new CapitalLpp(M_2000, M_15, M_20, PART_1_2, PROPRIETAIRE, false, DF_REQ));
        container.add(new PretEnversTiers(M_2000, M_20, PART_1_2, PROPRIETAIRE, false, DF_REQ));
        container.add(new CompteBancairePostal(M_2000, M_10, M_20, PART_1_2, PROPRIETAIRE, false, DF_REQ));
        container.add(new Titre(M_2000, M_10, M_20, PROPRIETAIRE, PART_1_2, false, DF_REQ));
        container.add(new Numeraire(M_2000, M_20, PART_1_2, PROPRIETAIRE, false, DF_REQ));
        container.add(new PretEnversTiers(M_1000, M_15, PART_1_2, PROPRIETAIRE, false, DF_CONJ));
        container.add(new CompteBancairePostal(M_1000, M_5, M_20, PART_1_2, PROPRIETAIRE, false, DF_ENF));
        container.add(new Titre(M_1000, M_5, M_20, PROPRIETAIRE, PART_1_2, false, DF_CONJ));
        container.add(new Numeraire(M_1000, M_15, PART_1_2, PROPRIETAIRE, false, DF_CONJ));
        // testGetFortuneValeurRachatAssuranceV
        container.add(new AssuranceVie(M_1000, DF_REQ));
        container.add(new AssuranceVie(M_2000, DF_CONJ));
        container.add(new AssuranceVie(M_2000, DF_ENF));
        container.add(new AssuranceRenteViagere(M_1000, M_20, M_1000, false, false, DF_REQ));
        container.add(new AssuranceRenteViagere(M_2000, M_20, M_2000, false, false, DF_CONJ));
        container.add(new AssuranceRenteViagere(M_2000, M_20, M_2000, false, false, DF_ENF));

        container.add(new DessaisissementFortune(M_1000, M_100, DF_REQ));
        container.add(new DessaisissementRevenu(M_1000, M_100, DF_REQ));
        container.add(new DessaisissementFortune(M_2000, M_100, DF_ENF));
        container.add(new DessaisissementRevenu(M_2000, M_100, DF_CONJ));
        container.add(new DessaisissementFortune(M_2000, M_100, DF_ENF));
        container.add(new DessaisissementRevenu(M_2000, M_100, DF_CONJ));

        // testGetFortuneAutreBien

        container.add(new MarchandiseStock(M_2000, PROPRIETAIRE, PART_1_2, DF_REQ));
        container.add(new Vehicule(M_2000, PROPRIETAIRE, PART_1_2, DF_REQ));
        container.add(new Betail(M_2000, PROPRIETAIRE, PART_1_2, DF_REQ));
        container.add(new AutreFortuneMobiliere(M_2000, PROPRIETAIRE, PART_1_2,
                AutreFortuneMobiliereTypeDeFortune.AUTRES, DF_REQ));
        container.add(new CapitalLpp(M_2000, M_15, M_20, PART_1_2, PROPRIETAIRE, false, DF_REQ));

        container.add(new MarchandiseStock(M_1000, PROPRIETAIRE, PART_1_2, DF_CONJ));
        container.add(new Vehicule(M_1000, PROPRIETAIRE, PART_1_2, DF_ENF));
        container.add(new Betail(M_1000, PROPRIETAIRE, PART_1_2, DF_ENF));
        container.add(new AutreFortuneMobiliere(M_1000, PROPRIETAIRE, PART_1_2,
                AutreFortuneMobiliereTypeDeFortune.AUTRES, DF_CONJ));
        container.add(new CapitalLpp(M_1000, M_15, M_20, PART_1_2, PROPRIETAIRE, false, DF_CONJ));

        container.add(new BienImmobilierServantHabitationPrincipale(M_300000, M_1000, M_INTERET, M_20, M_SOUS_LOCATION,
                M_DETTE, 1, BienImmobilierHabitableType.APPARTEMENT, PART_1_2, PROPRIETAIRE, DF_REQ));
        container.add(new BienImmobilierServantHabitationPrincipale(M_100000, M_1000, M_INTERET, M_20, M_SOUS_LOCATION,
                M_DETTE, 1, BienImmobilierHabitableType.APPARTEMENT, PART_1_2, PROPRIETAIRE, DF_ENF));
        container.add(new BienImmobilierServantHabitationPrincipale(M_100000, M_1000, M_INTERET, M_20, M_SOUS_LOCATION,
                M_DETTE, 1, BienImmobilierHabitableType.APPARTEMENT, PART_1_2, PROPRIETAIRE, DF_CONJ));

        container.add(new BienImmobilierNonPrincipale(M_300000, M_INTERET, M_1000, M_SOUS_LOCATION, M_15, M_DETTE,
                BienImmobilierHabitableType.APPARTEMENT, PART_1_2, PROPRIETAIRE, DF_REQ));
        container.add(new BienImmobilierNonPrincipale(M_100000, M_INTERET, M_1000, M_SOUS_LOCATION, M_15, M_DETTE,
                BienImmobilierHabitableType.APPARTEMENT, PART_1_2, PROPRIETAIRE, DF_ENF));
        container.add(new BienImmobilierNonPrincipale(M_100000, M_INTERET, M_1000, M_SOUS_LOCATION, M_15, M_DETTE,
                BienImmobilierHabitableType.APPARTEMENT, PART_1_2, PROPRIETAIRE, DF_CONJ));

        container.add(new BienImmobilierNonHabitable(M_1000, M_300000, M_INTERET, M_DETTE,
                BienImmobilierNonHabitableType.FORET, PART_1_2, PROPRIETAIRE, DF_REQ));
        container.add(new BienImmobilierNonHabitable(M_1000, M_100000, M_INTERET, M_DETTE,
                BienImmobilierNonHabitableType.FORET, PART_1_2, PROPRIETAIRE, DF_CONJ));
        container.add(new BienImmobilierNonHabitable(M_1000, M_100000, M_INTERET, M_DETTE,
                BienImmobilierNonHabitableType.FORET, PART_1_2, PROPRIETAIRE, DF_ENF));

        container.add(new AutreDetteProuvee(M_2000, DF_REQ));
        container.add(new AutreDetteProuvee(M_2000, DF_CONJ));
        container.add(new AutreDetteProuvee(M_2000, DF_ENF));

        container.add(new RevenuActiviteLucrativeDependante(M_2000, M_20, M_15, M_5, DF_REQ));
        container.add(new RevenuActiviteLucrativeDependante(M_2000, M_20, M_15, M_5, DF_CONJ));
        container.add(new RevenuActiviteLucrativeDependante(M_2000, M_20, M_15, M_5, DF_ENF));

        container.add(new RevenuHypothtique(M_2000, Montant.ZERO_ANNUEL, M_20, M_15, M_5,
                RevenuHypothtiqueMotif.MOTIF_REVENU_HYPO_14A_OPC, DF_REQ));
        container.add(new RevenuHypothtique(M_2000, Montant.ZERO_ANNUEL, M_20, M_15, M_5,
                RevenuHypothtiqueMotif.MOTIF_REVENU_HYPO_14A_OPC, DF_CONJ));
        container.add(new RevenuHypothtique(M_2000, Montant.ZERO_ANNUEL, M_20, M_15, M_5,
                RevenuHypothtiqueMotif.MOTIF_REVENU_HYPO_14A_OPC, DF_ENF));

        container.add(new RevenuActiviteLucrativeIndependante(M_1000,
                RevenuActiviteLucrativeIndependanteGenreRevenu.GENRE_REVENU_ACT_LUCR_AGRICOLE_FORESTIER, DF_REQ));
        container.add(new RevenuActiviteLucrativeIndependante(M_1000,
                RevenuActiviteLucrativeIndependanteGenreRevenu.GENRE_REVENU_ACT_LUCR_AGRICOLE_FORESTIER, DF_ENF));
        container.add(new RevenuActiviteLucrativeIndependante(M_1000,
                RevenuActiviteLucrativeIndependanteGenreRevenu.GENRE_REVENU_ACT_LUCR_AGRICOLE_FORESTIER, DF_CONJ));

        container.add(new AutreRevenu(M_2000, "Test", DF_REQ));
        container.add(new AutreRevenu(M_2000, "Test", DF_CONJ));
        container.add(new AutreRevenu(M_2000, "Test", DF_ENF));

        container.add(new IndemniteJournaliereApg(new Montant(1), new Montant(0), new Montant(0), new Montant(0),
                IndemniteJournaliereApgGenre.APG, 8, new Taux(0), new Taux(0), "libelle", DF_REQ));
        container.add(new IndemniteJournaliereApg(new Montant(1), new Montant(0), new Montant(0), new Montant(0),
                IndemniteJournaliereApgGenre.APG, 5, new Taux(0), new Taux(0), "libelle", DF_CONJ));
        container.add(new IndemniteJournaliereApg(new Montant(1), new Montant(0), new Montant(0), new Montant(0),
                IndemniteJournaliereApgGenre.APG, 5, new Taux(0), new Taux(0), "libelle", DF_ENF));

        container.add(new AllocationFamilliale(M_20, DF_REQ));
        container.add(new AllocationFamilliale(M_20, DF_ENF));
        container.add(new AllocationFamilliale(M_20, DF_CONJ));

        container.add(new RenteAvsAi(M_1000, RenteAvsAiType.RENTE_10, TypeSansRente.INDEFINIT, DF_REQ));
        container.add(new RenteAvsAi(M_2000, RenteAvsAiType.RENTE_10, TypeSansRente.INDEFINIT, DF_CONJ));
        container.add(new RenteAvsAi(M_2000, RenteAvsAiType.RENTE_10, TypeSansRente.INDEFINIT, DF_ENF));

        container.add(new IjAi(M_20, 10, DF_REQ));
        container.add(new IjAi(M_15, 10, DF_CONJ));
        container.add(new IjAi(M_15, 10, DF_ENF));

        container.add(new ApiAvsAi(M_1000, ApiType.API_81, ApiDegre.FAIBLE, DF_REQ));
        container.add(new ApiAvsAi(M_2000, ApiType.API_81, ApiDegre.FAIBLE, DF_CONJ));
        container.add(new ApiAvsAi(M_2000, ApiType.API_81, ApiDegre.FAIBLE, DF_ENF));

        container.add(new AutreRente(M_1000, AutreRenteType.INVALIDITE, AutreRenteGenre.RENTE_ETRANGERE,
                L_AUTRE_RENTE_ETRANGERE, MonnaieEtrangereType.FRANC_SUISSE, DF_REQ));
        container.add(new AutreRente(M_1000, AutreRenteType.INVALIDITE, AutreRenteGenre.RENTE_ETRANGERE,
                L_AUTRE_RENTE_ETRANGERE, MonnaieEtrangereType.FRANC_SUISSE, DF_CONJ));
        container.add(new AutreRente(M_1000, AutreRenteType.INVALIDITE, AutreRenteGenre.RENTE_ETRANGERE,
                L_AUTRE_RENTE_ETRANGERE, MonnaieEtrangereType.FRANC_SUISSE, DF_ENF));

        container.add(new AutreRente(M_1000, AutreRenteType.INVALIDITE, AutreRenteGenre.LAA, "libelle",
                MonnaieEtrangereType.INDEFINIT, DF_REQ));
        container.add(new AutreRente(M_1000, AutreRenteType.INVALIDITE, AutreRenteGenre.LAA, "libelle",
                MonnaieEtrangereType.INDEFINIT, DF_CONJ));
        container.add(new AutreRente(M_1000, AutreRenteType.INVALIDITE, AutreRenteGenre.LAA, "libelle",
                MonnaieEtrangereType.INDEFINIT, DF_ENF));
        container.add(new AutreRente(M_1000, AutreRenteType.INVALIDITE, AutreRenteGenre.LAM, "libelle",
                MonnaieEtrangereType.INDEFINIT, DF_REQ));

        container.add(new AutreRente(M_1000, AutreRenteType.INVALIDITE, AutreRenteGenre.LAM, "libelle",
                MonnaieEtrangereType.INDEFINIT, DF_CONJ));
        container.add(new AutreRente(M_1000, AutreRenteType.INVALIDITE, AutreRenteGenre.LAM, "libelle",
                MonnaieEtrangereType.INDEFINIT, DF_ENF));
        container.add(new AutreRente(M_1000, AutreRenteType.INVALIDITE, AutreRenteGenre.LPP, "libelle",
                MonnaieEtrangereType.INDEFINIT, DF_REQ));
        container.add(new AutreRente(M_1000, AutreRenteType.INVALIDITE, AutreRenteGenre.LPP, "libelle",
                MonnaieEtrangereType.INDEFINIT, DF_CONJ));
        container.add(new AutreRente(M_1000, AutreRenteType.INVALIDITE, AutreRenteGenre.LPP, "libelle",
                MonnaieEtrangereType.INDEFINIT, DF_ENF));

        container.add(new AutreRente(M_1000, AutreRenteType.INVALIDITE, AutreRenteGenre.TROISIEME_PILIER, "libelle",
                MonnaieEtrangereType.INDEFINIT, DF_REQ));
        container.add(new AutreRente(M_1000, AutreRenteType.INVALIDITE, AutreRenteGenre.TROISIEME_PILIER, "libelle",
                MonnaieEtrangereType.INDEFINIT, DF_CONJ));
        container.add(new AutreRente(M_1000, AutreRenteType.INVALIDITE, AutreRenteGenre.TROISIEME_PILIER, "libelle",
                MonnaieEtrangereType.INDEFINIT, DF_ENF));
        container.add(new AutreRente(M_1000, AutreRenteType.INVALIDITE, AutreRenteGenre.ASSURANCE_PRIVEE, "libelle",
                MonnaieEtrangereType.INDEFINIT, DF_REQ));
        container.add(new AutreRente(M_1000, AutreRenteType.INVALIDITE, AutreRenteGenre.AUTRES, "libelle",
                MonnaieEtrangereType.INDEFINIT, DF_REQ));
        container.add(new AutreRente(M_2000, AutreRenteType.INVALIDITE, AutreRenteGenre.ASSURANCE_PRIVEE, "libelle",
                MonnaieEtrangereType.INDEFINIT, DF_CONJ));
        container.add(new AutreRente(M_2000, AutreRenteType.INVALIDITE, AutreRenteGenre.AUTRES, "libelle",
                MonnaieEtrangereType.INDEFINIT, DF_ENF));

        container.add(new ContratEntretienViager(M_1000, DF_REQ));
        container.add(new ContratEntretienViager(M_1000, DF_CONJ));
        container.add(new ContratEntretienViager(M_1000, DF_ENF));

        container.add(new PensionAlimentaire(M_1000, Montant.ZERO, PensionAlimentaireType.VERSEE,
                PensionAlimentaireLienParente.AUTRES, false, DF_REQ));
        container.add(new PensionAlimentaire(M_1000, Montant.ZERO, PensionAlimentaireType.VERSEE,
                PensionAlimentaireLienParente.AUTRES, false, DF_CONJ));
        container.add(new PensionAlimentaire(M_1000, Montant.ZERO, PensionAlimentaireType.VERSEE,
                PensionAlimentaireLienParente.AUTRES, false, DF_ENF));

        container.add(new Loyer(M_2000, M_20, M_100, M_15, LoyerType.NET_AVEC_CHARGE, 2, false, false, DF_REQ));
        container.add(new Loyer(M_2000, M_5, M_15, M_15, LoyerType.NET_AVEC_CHARGE, 3, false, false, DF_CONJ));
        container.add(new Loyer(M_2000, M_5, M_15, M_15, LoyerType.NET_AVEC_CHARGE, 3, false, false, DF_ENF));

        container.add(new PensionAlimentaire(M_1000, Montant.ZERO, PensionAlimentaireType.DUE,
                PensionAlimentaireLienParente.AUTRES, false, DF_REQ));
        container.add(new PensionAlimentaire(M_1000, Montant.ZERO, PensionAlimentaireType.DUE,
                PensionAlimentaireLienParente.AUTRES, false, DF_CONJ));
        container.add(new PensionAlimentaire(M_1000, Montant.ZERO, PensionAlimentaireType.DUE,
                PensionAlimentaireLienParente.AUTRES, false, DF_ENF));

        container.add(new Regime(M_1000, "", DF_REQ));
    }

    // @Test
    public void testGetFortuneArgentRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(4000), demandeAReviser.getFortuneArgentRequerant());
    }

    // @Test
    public void testGetFortuneArgentCojnEnf() throws Exception {
        assertEquals(Montant.newAnnuel(2000), demandeAReviser.getFortuneArgentCojnEnf());
    }

    @Test
    public void testGetFortuneValeurRachatAssuranceVieRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(2000), demandeAReviser.getFortuneValeurRachatAssuranceVieRequerant());
    }

    @Test
    public void testGetFortuneValeurRachatAssuranceVieConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(8000), demandeAReviser.getFortuneValeurRachatAssuranceVieConjEnf());
    }

    @Test
    public void testGetFortuneBienDessaisiRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(1800), demandeAReviser.getFortuneBienDessaisiRequerant());
    }

    @Test
    public void testGetFortuneBienDessaisiConjEnfant() throws Exception {
        assertEquals(Montant.newAnnuel(7600), demandeAReviser.getFortuneBienDessaisiConjEnfant());
    }

    @Test
    public void testGetFortuneAutreBienRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(5000), demandeAReviser.getFortuneAutreBienRequerant());
    }

    @Test
    public void testGetFortuneAutreBienConjEnfant() throws Exception {
        assertEquals(Montant.newAnnuel(2500), demandeAReviser.getFortuneAutreBienConjEnfant());
    }

    @Test
    public void testGetFortuneImmeublePrincipaleNonAgricoleRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(150000), demandeAReviser.getFortuneImmeublePrincipaleNonAgricoleRequerant());
    }

    @Test
    public void testGetFortuneImmeublePrincipaleNonAgricoleConEnf() throws Exception {
        assertEquals(Montant.newAnnuel(100000), demandeAReviser.getFortuneImmeublePrincipaleNonAgricoleConEnf());
    }

    @Test
    public void testGetFortuneImmeubleNonPrincipalNonAgricoleRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(150000), demandeAReviser.getFortuneImmeubleNonPrincipalNonAgricoleRequerant());
    }

    @Test
    public void testGetFortuneImmeubleNonPrincipalNonAgricoleEnfant() throws Exception {
        assertEquals(Montant.newAnnuel(100000), demandeAReviser.getFortuneImmeubleNonPrincipalNonAgricoleConjEnf());
    }

    @Test
    public void testGetFortuneImmeubleAgricoleRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(150000), demandeAReviser.getFortuneImmeubleAgricoleRequerant());
    }

    @Test
    public void testGetFortuneImmeubleAgricoleConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(100000), demandeAReviser.getFortuneImmeubleAgricoleConjEnf());
    }

    @Test
    public void testGetFortuneDetteHypothecaireRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(15000), demandeAReviser.getFortuneDetteHypothecaireRequerant());
    }

    @Test
    public void testGetFortuneDetteHypothecaireConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(30000), demandeAReviser.getFortuneDetteHypothecaireConjEnf());
    }

    @Test
    public void testGetFortuneAutreDetteRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(2000), demandeAReviser.getFortuneAutreDetteRequerant());
    }

    @Test
    public void testGetFortuneAutreDetteConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(4000), demandeAReviser.getFortuneAutreDetteConjEnf());
    }

    @Test
    public void testGetRevenuActiviteLucrativeDependanteRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(2000), demandeAReviser.getRevenuActiviteLucrativeDependanteRequerant());
    }

    @Test
    public void testGetRevenuActiviteLucrativeDependanteConEnf() throws Exception {
        assertEquals(Montant.newAnnuel(4000), demandeAReviser.getRevenuActiviteLucrativeDependanteConEnf());
    }

    @Test
    public void testGetRevenuHypothetiqueRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(2000), demandeAReviser.getRevenuHypothetiqueRequerant());
    }

    @Test
    public void testGetRevenuHypothetiqueConjEnfant() throws Exception {
        assertEquals(Montant.newAnnuel(4000), demandeAReviser.getRevenuHypothetiqueConjEnfant());
    }

    @Test
    public void testGetRevenuLucrativeIndependanteRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(1000), demandeAReviser.getRevenuLucrativeIndependanteRequerant());
    }

    @Test
    public void testGetRevenuLucrativeIndependanteConjoint() throws Exception {
        assertEquals(Montant.newAnnuel(2000), demandeAReviser.getRevenuLucrativeIndependanteConjEnf());
    }

    @Test
    public void testGetRevenuSalaireNatureRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(2000), demandeAReviser.getRevenuSalaireNatureRequerant());
    }

    @Test
    public void testGetRevenuSalaireNatureConjoint() throws Exception {
        assertEquals(Montant.newAnnuel(4000), demandeAReviser.getRevenuSalaireNatureConjoint());
    }

    @Test
    public void testGetRevenuAllocationPourPerteDeGainRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(8), demandeAReviser.getRevenuAllocationPourPerteDeGainRequerant());
    }

    @Test
    public void testGetRevenuAllocationPourPerteDeGainConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(10), demandeAReviser.getRevenuAllocationPourPerteDeGainConjEnf());
    }

    @Test
    public void testGetRevenuAllocationFamillialeRequant() throws Exception {
        assertEquals(Montant.newAnnuel(240), demandeAReviser.getRevenuAllocationFamillialeRequant());
    }

    @Test
    public void testGetRevenuAllocationFamillialeConjoint() throws Exception {
        assertEquals(Montant.newAnnuel(480), demandeAReviser.getRevenuAllocationFamillialeConjEnf());
    }

    @Test
    public void testGetRevenuRenteAnnuelleAvsAiRequant() throws Exception {
        assertEquals(Montant.newAnnuel(12000), demandeAReviser.getRevenuRenteAnnuelleAvsAiRequant());
    }

    @Test
    public void testGetRevenuRenteAnnuelleAvsAiConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(48000), demandeAReviser.getRevenuRenteAnnuelleAvsAiConjEnf());
    }

    @Test
    public void testGetRevenuIndemniteJournaliereAiRequant() throws Exception {
        assertEquals(Montant.newAnnuel(200), demandeAReviser.getRevenuIndemniteJournaliereAiRequant());
    }

    @Test
    public void testGetRevenuIndemniteJournaliereAiConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(300), demandeAReviser.getRevenuIndemniteJournaliereAiConjEnf());
    }

    @Test
    public void testGetRevenuAllocationPourImpotantRequant() throws Exception {
        assertEquals(Montant.newAnnuel(12000), demandeAReviser.getRevenuAllocationPourImpotantRequant());
    }

    @Test
    public void testGetRevenuAllocationPourImpotantConjEnft() throws Exception {
        assertEquals(Montant.newAnnuel(48000), demandeAReviser.getRevenuAllocationPourImpotantConjEnft());
    }

    @Test
    public void testGetRevenuRenteEtrangereRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(1000), demandeAReviser.getRevenuRenteEtrangereRequerant());
    }

    @Test
    public void testGetRevenuRenteEtrangereConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(2000), demandeAReviser.getRevenuRenteEtrangereConjEnf());
    }

    @Test
    public void testGetRevenuRenteLaaRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(1000), demandeAReviser.getRevenuRenteLaaRequerant());
    }

    @Test
    public void testGetRevenuRenteLaaConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(2000), demandeAReviser.getRevenuRenteLaaConjEnf());
    }

    @Test
    public void testGetRevenuRenteAssurenceMilitaireRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(1000), demandeAReviser.getRevenuRenteAssurenceMilitaireRequerant());
    }

    @Test
    public void testGetRevenuRenteAssurenceMilitaireConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(2000), demandeAReviser.getRevenuRenteAssurenceMilitaireConjEnf());
    }

    @Test
    public void testGetRevenuRenteLppRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(1000), demandeAReviser.getRevenuRenteLppRequerant());
    }

    @Test
    public void testGetRevenuRenteLppConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(2000), demandeAReviser.getRevenuRenteLppConjEnf());
    }

    @Test
    public void testGetRevenuRenteAssuranceVolontaireRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(1000), demandeAReviser.getRevenuRenteAssuranceVolontaireRequerant());
    }

    @Test
    public void testGetRevenuRenteAssuranceVolontaireConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(2000), demandeAReviser.getRevenuRenteAssuranceVolontaireConjEnf());
    }

    @Test
    public void testGetRevenuAutreRenteRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(2000), demandeAReviser.getRevenuAutreRenteRequerant());
    }

    @Test
    public void testGetRevenuAutreRenteConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(4000), demandeAReviser.getRevenuAutreRenteConjEnf());
    }

    @Test
    public void testGetRevenuContratEnteretienViagerRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(1000), demandeAReviser.getRevenuContratEnteretienViagerRequerant());
    }

    @Test
    public void testGetRevenuContratEnteretienViagerConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(2000), demandeAReviser.getRevenuContratEnteretienViagerConjEnf());
    }

    @Test
    public void testGetRevenuRendementLoyerRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(2020), demandeAReviser.getRevenuRendementLoyerRequerant());
    }

    @Test
    public void testGetRevenuRendementLoyerConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(4040), demandeAReviser.getRevenuRendementLoyerConjEnf());
    }

    @Test
    public void testGetRevenuRendementValeurLocativeRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(1015), demandeAReviser.getRevenuRendementValeurLocativeRequerant());
    }

    @Test
    public void testGetRevenuRendementValeurLocativeConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(2030), demandeAReviser.getRevenuRendementValeurLocativeConjEnf());
    }

    @Test
    public void testGetDeductionInteretsHypothecaireRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(60), demandeAReviser.getDeductionInteretsHypothecaireRequerant());
    }

    @Test
    public void testGetDeductionInteretsHypothecaireConjEnfant() throws Exception {
        assertEquals(Montant.newAnnuel(120), demandeAReviser.getDeductionInteretsHypothecaireConjEnfant());
    }

    @Test
    public void testGetDeductionPensionAlimentairesVerseeRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(12000), demandeAReviser.getDeductionPensionAlimentairesVerseeRequerant());
    }

    @Test
    public void testGetDeductionPensionAlimentairesVerseeRequerantSuffix() throws Exception {
        assertEquals(Montant.newAnnuel(24000), demandeAReviser.getDeductionPensionAlimentairesVerseeConjEnf());
    }

    @Test
    public void testGetRevenuRendementInteretsRequerant() throws Exception {

        assertEquals(Montant.newAnnuel(100), demandeAReviser.getRevenuRendementInteretsRequerant());
    }

    @Before
    public void before() {
        demandeAReviser = spy(demandeAReviser);
        doReturn(new Taux(1)).when(demandeAReviser).getTauxOfas();
    }

    @Test
    public void testGetRevenuRendementInteretsConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(90), demandeAReviser.getRevenuRendementInteretsConjEnf());
    }

    @Test
    public void testGetDeductionLoyerAnnuellesRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(24500), demandeAReviser.getDeductionLoyerAnnuellesRequerant());
    }

    @Test
    public void testGetDeductionLoyerAnnuellesConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(25000), demandeAReviser.getDeductionLoyerAnnuellesConjEnf());
    }

    @Test
    public void testGetDeductionChargesAnnuelesRequerant() throws Exception {
        assertEquals(Montant.newAnnuel(240), demandeAReviser.getDeductionChargesAnnuelesRequerant());
    }

    @Test
    public void testGetDeductionChargesAnnuelesConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(120), demandeAReviser.getDeductionChargesAnnuelesConjEnf());
    }

    @Test
    public void testGetDeductionNombrePeronneRequerant() throws Exception {
        assertEquals(new Integer(3), demandeAReviser.getDeductionNombrePeronneRequerant());
    }

    @Test
    public void testGetDeductionNombrePeronneConjEnf() throws Exception {
        assertEquals(new Integer(8), demandeAReviser.getDeductionNombrePeronneConjEnf());
    }

    @Test
    public void testGetDeductionCotisationPsalRequerant() throws Exception {
        container.add(new CotisationPsal(M_100, DF_REQ));
        assertEquals(Montant.newAnnuel(100), demandeAReviser.getDeductionCotisationPsalRequerant());
    }

    @Test
    public void testGetDeductionCotisationPsalPeronneConjEnf() throws Exception {
        container.add(new CotisationPsal(M_20, DF_CONJ));
        container.add(new CotisationPsal(M_20, DF_ENF));
        assertEquals(Montant.newAnnuel(40), demandeAReviser.getDeductionCotisationPsalPeronneConjEnf());
    }

    @Test
    public void testGetRevenuAutresRevenusValeurUsufruitReq() throws Exception {
        container.add(new BienImmobilierServantHabitationPrincipale(M_300000, M_1000, M_INTERET, M_20, M_SOUS_LOCATION,
                M_DETTE, 1, BienImmobilierHabitableType.APPARTEMENT, PART_1_2, USUFRUITIER, DF_REQ));
        container.add(new BienImmobilierNonPrincipale(M_300000, M_INTERET, M_1000, M_SOUS_LOCATION, M_15, M_DETTE,
                BienImmobilierHabitableType.APPARTEMENT, PART_1_2, USUFRUITIER, DF_REQ));
        container.add(new BienImmobilierNonHabitable(M_1000, M_300000, M_INTERET, M_DETTE,
                BienImmobilierNonHabitableType.FORET, PART_1_2, USUFRUITIER, DF_REQ));
        assertEquals(Montant.newAnnuel(1007.5), demandeAReviser.getRevenuAutresRevenusValeurUsufruitReq());
    }

    @Test
    public void testGetRevenuAutresRevenusValeurUsufruitConjEnf() throws Exception {
        container.add(new BienImmobilierServantHabitationPrincipale(M_300000, M_2000, M_INTERET, M_20, M_SOUS_LOCATION,
                M_DETTE, 1, BienImmobilierHabitableType.APPARTEMENT, PART_1_2, USUFRUITIER, DF_CONJ));
        container.add(new BienImmobilierNonPrincipale(M_300000, M_INTERET, M_1000, M_SOUS_LOCATION, M_20, M_DETTE,
                BienImmobilierHabitableType.APPARTEMENT, PART_1_2, USUFRUITIER, DF_ENF));
        container.add(new BienImmobilierNonHabitable(M_2000, M_300000, M_INTERET, M_DETTE,
                BienImmobilierNonHabitableType.FORET, PART_1_2, USUFRUITIER, DF_CONJ));
        assertEquals(Montant.newAnnuel(2010), demandeAReviser.getRevenuAutresRevenusValeurUsufruitConjEnf());
    }

    @Test
    public void testGetRevenuAutresRevenusDroitHabitationReq() throws Exception {
        container.add(new BienImmobilierServantHabitationPrincipale(M_300000, M_1000, M_INTERET, M_20, M_SOUS_LOCATION,
                M_DETTE, 1, BienImmobilierHabitableType.APPARTEMENT, PART_1_2, DROIT_HABITATION, DF_REQ));
        container.add(new BienImmobilierNonPrincipale(M_300000, M_INTERET, M_1000, M_SOUS_LOCATION, M_15, M_DETTE,
                BienImmobilierHabitableType.APPARTEMENT, PART_1_2, DROIT_HABITATION, DF_REQ));
        assertEquals(Montant.newAnnuel(1015), demandeAReviser.getRevenuAutresRevenusDroitHabitationReq());
    }

    @Test
    public void testGetRevenuAutresRevenusDroitHabitationConjEnf() throws Exception {
        container.add(new BienImmobilierServantHabitationPrincipale(M_300000, M_1000, M_INTERET, M_20, M_SOUS_LOCATION,
                M_DETTE, 1, BienImmobilierHabitableType.APPARTEMENT, PART_1_2, DROIT_HABITATION, DF_CONJ));
        container.add(new BienImmobilierNonPrincipale(M_300000, M_INTERET, M_1000, M_SOUS_LOCATION, M_1000, M_DETTE,
                BienImmobilierHabitableType.APPARTEMENT, PART_1_2, DROIT_HABITATION, DF_ENF));
        assertEquals(Montant.newAnnuel(2000), demandeAReviser.getRevenuAutresRevenusDroitHabitationConjEnf());
    }

    @Test
    public void testGetRevenuAutresRevenusPensionAlimentaireReqDivorceeReq() throws Exception {
        assertEquals(Montant.newAnnuel(12000), demandeAReviser.getRevenuAutresRevenusPensionAlimentaireReqDivorceeReq());
    }

    @Test
    public void testGetRevenuAutresRevenusPensionAlimentaireReqDivorceeConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(24000),
                demandeAReviser.getRevenuAutresRevenusPensionAlimentaireReqDivorceeConjEnf());
    }

    @Test
    public void testGetRevenuAutresRevenusAutresCreancesVersTiersReq() throws Exception {
        assertEquals(Montant.newAnnuel(2000), demandeAReviser.getRevenuAutresRevenusAutresCreancesVersTiersReq());
    }

    @Test
    public void testGetRevenuAutresRevenusAutresCreancesVersTiersConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(4000), demandeAReviser.getRevenuAutresRevenusAutresCreancesVersTiersConjEnf());
    }

    @Test
    public void testGetRevenuAutresRevenusRessourcesOuInteretsFortuneDessaisiReq() throws Exception {
        assertEquals(Montant.newAnnuel(9),
                demandeAReviser.getRevenuAutresRevenusRessourcesOuInteretsFortuneDessaisiReq());
    }

    @Test
    public void testGetRevenuAutresRevenusRessourcesOuInteretsFortuneDessaisiConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(38),
                demandeAReviser.getRevenuAutresRevenusRessourcesOuInteretsFortuneDessaisiConjEnf());
    }

    @Test
    public void testGetRevenuAutresRevenusSousLocationsSansPensionReq() throws Exception {
        assertEquals(Montant.newAnnuel(1210), demandeAReviser.getRevenuAutresRevenusSousLocationsSansPensionReq());
    }

    @Test
    public void testGetRevenuAutresRevenusSousLocationsSansPensionConjEnf() throws Exception {
        assertEquals(Montant.newAnnuel(380), demandeAReviser.getRevenuAutresRevenusSousLocationsSansPensionConjEnf());
    }

    @Test
    public void testGetNomAdresseCourrier() throws Exception {
        ListrevisionWithPcaRequerantConjoint demande = new ListrevisionWithPcaRequerantConjoint();
        demande.setNomConjoint("NomConjoint");
        demande.setPrenomConjoint("PrenomConjoint");
        demande.setNomRequerant("nomRequerant");
        demande.setPrenomRequerant("prenomRequerant");
        demande.setNssConjoint("nssConjoint");
        demande.setNssRequerant("nssRequerant");

        Adresse adresse = buildAdresse();
        Adresse adresse2 = buildAdresse();
        DemandeAReviser demandeAReviser = new DemandeAReviser(mock(DonneesFinancieresContainer.class), demande,
                adresse, adresse2, null, null, new MembresFamilles(), null);
        assertEquals("designation1", demandeAReviser.getAdresseCourrierNom());
    }

    @Test
    public void testGetPreNomAdresseCourrier() throws Exception {
        ListrevisionWithPcaRequerantConjoint demande = new ListrevisionWithPcaRequerantConjoint();
        demande.setNomConjoint("NomConjoint");
        demande.setPrenomConjoint("PrenomConjoint");
        demande.setNomRequerant("nomRequerant");
        demande.setPrenomRequerant("prenomRequerant");
        demande.setNssConjoint("nssConjoint");
        demande.setNssRequerant("nssRequerant");

        Adresse adresse = buildAdresse();
        Adresse adresse2 = buildAdresse();
        DemandeAReviser demandeAReviser = new DemandeAReviser(mock(DonneesFinancieresContainer.class), demande,
                adresse, adresse2, null, null, new MembresFamilles(), null);
        assertEquals("designation2", demandeAReviser.getAdresseCourrierPrenom());
    }

    @Test
    public void testGetNomAdresseDomicile() throws Exception {
        ListrevisionWithPcaRequerantConjoint demande = new ListrevisionWithPcaRequerantConjoint();
        demande.setNomConjoint("NomConjoint");
        demande.setPrenomConjoint("PrenomConjoint");
        demande.setNomRequerant("nomRequerant");
        demande.setPrenomRequerant("prenomRequerant");
        demande.setNssConjoint("nssConjoint");
        demande.setNssRequerant("nssRequerant");

        Adresse adresse = buildAdresse();
        Adresse adresse2 = buildAdresse();
        DemandeAReviser demandeAReviser = new DemandeAReviser(mock(DonneesFinancieresContainer.class), demande,
                adresse, adresse2, null, null, new MembresFamilles(), null);
        assertEquals("designation1", demandeAReviser.getAdresseDomicileNom());
    }

    @Test
    public void testGetPreNomAdresseDomicile() throws Exception {
        ListrevisionWithPcaRequerantConjoint demande = new ListrevisionWithPcaRequerantConjoint();
        demande.setNomConjoint("NomConjoint");
        demande.setPrenomConjoint("PrenomConjoint");
        demande.setNomRequerant("nomRequerant");
        demande.setPrenomRequerant("prenomRequerant");
        demande.setNssConjoint("nssConjoint");
        demande.setNssRequerant("nssRequerant");

        Adresse adresse = buildAdresse();
        Adresse adresse2 = buildAdresse();
        DemandeAReviser demandeAReviser = new DemandeAReviser(mock(DonneesFinancieresContainer.class), demande,
                adresse, adresse2, null, null, new MembresFamilles(), null);
        assertEquals("designation2", demandeAReviser.getAdresseDomicilePrenom());
    }

    private Adresse buildAdresse() {
        Adresse adresse = new Adresse("localite", "casePostale", "attention", "npa", "pays", new Canton("NE"),
                "csTitre", "rue", "rueNumero", "designation1", "designation2", "designation3", "designation4",
                "idAdresse", "designationTiers1", "designationTiers2", "designationTiers3", "designationTiers4",
                "csTitreTiers");
        return adresse;
    }

    // @Test
    // public void testGetDeductionRegimeRfmRequerant() throws Exception {
    // throw new RuntimeException("not yet implemented");
    // }
    //
    // @Test
    // public void testGetDeductionRegimeRfmConjEnf() throws Exception {
    // throw new RuntimeException("not yet implemented");
    // }

    // @Test
    // public void testGetDeductionFraisEntretiensImmeubleRequerant() throws Exception {
    // assertEquals(Montant.newAnnuel(403), demandeAReviser.getDeductionFraisEntretiensImmeubleRequerant());
    //
    // }
    //
    // @Test
    // public void testGetDeductionFraisEntretiensImmeubleConjEnf() throws Exception {
    // assertEquals(Montant.newAnnuel(806), demandeAReviser.getDeductionFraisEntretiensImmeubleRequerant());
    // }

}
