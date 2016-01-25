package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.allocationFamilliale.AllocationFamilliale;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.autreApi.AutreApi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.avsAi.ApiAvsAi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.assuranceRenteViagere.AssuranceRenteViagere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.assuranceVie.AssuranceVie;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreDetteProuvee.AutreDetteProuvee;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreFortuneMobiliere.AutreFortuneMobiliere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente.AutreRente;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente.AutreRenteGenre;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente.AutresRentes;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRevenue.AutreRevenu;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.betail.Betail;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.BienImmobilier;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.BiensImmobiliersListBase;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonHabitable.BienImmobilierNonHabitable;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonHabitable.BiensImmobiliersNonHabitable;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonPrincipale.BienImmobilierNonPrincipale;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonPrincipale.BiensImmobiliersNonPrincipale;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierServantHbitationPrincipale.BienImmobilierServantHabitationPrincipale;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierServantHbitationPrincipale.BiensImmobiliersServantHabitationPrincipale;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.capitalLpp.CapitalLpp;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.compteBancairePostal.CompteBancairePostal;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.contratEntretienViager.ContratEntretienViager;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.cotisationPsal.CotisationPsal;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.dessaisissementFortune.DessaisissementFortune;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.dessaisissementRevenu.DessaisissementRevenu;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.iJAi.IjAi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.indeminteJournaliereApg.IndemniteJournaliereApg;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.loyer1.Loyer;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.loyer1.Loyers;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.marchandiseStock.MarchandiseStock;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.numeraire.Numeraire;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.pensionAlimentaire.PensionAlimentaire;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.pensionAlimentaire.PensionAlimentaireType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.pensionAlimentaire.PensionsAlimentaires;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.pretEnversTiers.PretEnversTiers;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.regime.Regime;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.renteAvsAi.RenteAvsAi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.revenuActiviteLucrativeIndependante.RevenuActiviteLucrativeIndependante;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.revenueActiviteLucrativeDependante.RevenuActiviteLucrativeDependante;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.revenueHypothtique.RevenuHypothtique;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.taxeJournalierHome.TaxeJournaliereHome;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.titre.Titre;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.vehicule.Vehicule;
import ch.globaz.pegasus.businessimpl.services.revisionquadriennale.Regimes;

public class DonneesFinancieresContainer {
    private DonneesFinancieresListBase<AllocationFamilliale> allocationsFamilliale = new DonneesFinancieresListBase<AllocationFamilliale>();
    private DonneesFinancieresListBase<ApiAvsAi> apisAvsAi = new DonneesFinancieresListBase<ApiAvsAi>();
    private DonneesFinancieresListBase<AssuranceRenteViagere> assurancesRentesViageres = new DonneesFinancieresListBase<AssuranceRenteViagere>();
    private DonneesFinancieresListBase<AssuranceVie> assurancesVie = new DonneesFinancieresListBase<AssuranceVie>();
    private DonneesFinancieresListBase<AutreApi> autresApi = new DonneesFinancieresListBase<AutreApi>();
    private DonneesFinancieresListBase<AutreDetteProuvee> autresDettesProuvees = new DonneesFinancieresListBase<AutreDetteProuvee>();
    private DonneesFinancieresListBase<AutreFortuneMobiliere> autresFortunesMobilieres = new DonneesFinancieresListBase<AutreFortuneMobiliere>();
    private AutresRentes autresRentes = new AutresRentes();
    private DonneesFinancieresListBase<AutreRevenu> autresRevenus = new DonneesFinancieresListBase<AutreRevenu>();
    private DonneesFinancieresListBase<Betail> betails = new DonneesFinancieresListBase<Betail>();
    private BiensImmobiliersNonHabitable biensImmobiliersNonHabitable = new BiensImmobiliersNonHabitable();
    private BiensImmobiliersNonPrincipale biensImmobiliersNonPrincipale = new BiensImmobiliersNonPrincipale();
    private BiensImmobiliersServantHabitationPrincipale biensImmobiliersServantHbitationPrincipale = new BiensImmobiliersServantHabitationPrincipale();
    private DonneesFinancieresListBase<CapitalLpp> capitalsLpp = new DonneesFinancieresListBase<CapitalLpp>();
    private DonneesFinancieresListBase<CompteBancairePostal> comptesBancairePostal = new DonneesFinancieresListBase<CompteBancairePostal>();
    private DonneesFinancieresListBase<ContratEntretienViager> contratsEntretienViager = new DonneesFinancieresListBase<ContratEntretienViager>();
    private DonneesFinancieresListBase<CotisationPsal> cotisationsPsal = new DonneesFinancieresListBase<CotisationPsal>();
    private DonneesFinancieresListBase<DessaisissementRevenu> dessaisissementsRevenu = new DonneesFinancieresListBase<DessaisissementRevenu>();
    private DonneesFinancieresListBase<DessaisissementFortune> dessaississementsFortune = new DonneesFinancieresListBase<DessaisissementFortune>();
    private DonneesFinancieresListBase<IjAi> ijAis = new DonneesFinancieresListBase<IjAi>();
    private DonneesFinancieresListBase<IndemniteJournaliereApg> indemintesJournaliereApg = new DonneesFinancieresListBase<IndemniteJournaliereApg>();
    private Loyers loyers = new Loyers();
    private DonneesFinancieresListBase<TaxeJournaliereHome> taxesJournaliereHome = new DonneesFinancieresListBase<TaxeJournaliereHome>();
    private DonneesFinancieresListBase<MarchandiseStock> marchandisesStocks = new DonneesFinancieresListBase<MarchandiseStock>();
    private DonneesFinancieresListBase<Numeraire> numeraires = new DonneesFinancieresListBase<Numeraire>();
    private DonneesFinancieresListBase<PensionAlimentaire> pensionsAlimentaire = new DonneesFinancieresListBase<PensionAlimentaire>();
    private DonneesFinancieresListBase<PretEnversTiers> pretsEnversTiers = new DonneesFinancieresListBase<PretEnversTiers>();
    private DonneesFinancieresListBase<RenteAvsAi> rentesAvsAi = new DonneesFinancieresListBase<RenteAvsAi>();
    private DonneesFinancieresListBase<RevenuActiviteLucrativeIndependante> revenusActiviteLucrativeIndependante = new DonneesFinancieresListBase<RevenuActiviteLucrativeIndependante>();
    private DonneesFinancieresListBase<RevenuActiviteLucrativeDependante> revenusActiviteLucrativeDependante = new DonneesFinancieresListBase<RevenuActiviteLucrativeDependante>();
    private DonneesFinancieresListBase<RevenuHypothtique> revenusHypothtique = new DonneesFinancieresListBase<RevenuHypothtique>();
    private DonneesFinancieresListBase<Titre> titres = new DonneesFinancieresListBase<Titre>();
    private DonneesFinancieresListBase<Vehicule> vehicules = new DonneesFinancieresListBase<Vehicule>();
    private DonneesFinancieresListBase<Regime> regimes = new DonneesFinancieresListBase<Regime>();

    public boolean add(AllocationFamilliale e) {
        return allocationsFamilliale.add(e);
    }

    public boolean add(ApiAvsAi e) {
        return apisAvsAi.add(e);
    }

    public boolean add(AssuranceRenteViagere e) {
        return assurancesRentesViageres.add(e);
    }

    public boolean add(AssuranceVie e) {
        return assurancesVie.add(e);
    }

    public boolean add(AutreApi e) {
        return autresApi.add(e);
    }

    public boolean add(AutreDetteProuvee e) {
        return autresDettesProuvees.add(e);
    }

    public boolean add(AutreFortuneMobiliere e) {
        return autresFortunesMobilieres.add(e);
    }

    public boolean add(AutreRente e) {
        return autresRentes.add(e);
    }

    public boolean add(AutreRevenu e) {
        return autresRevenus.add(e);
    }

    public boolean add(Betail e) {
        return betails.add(e);
    }

    public boolean add(BienImmobilierNonHabitable e) {
        return biensImmobiliersNonHabitable.add(e);
    }

    public boolean add(BienImmobilierNonPrincipale e) {
        return biensImmobiliersNonPrincipale.add(e);
    }

    public boolean add(BienImmobilierServantHabitationPrincipale e) {
        return biensImmobiliersServantHbitationPrincipale.add(e);
    }

    public boolean add(CapitalLpp e) {
        return capitalsLpp.add(e);
    }

    public boolean add(CompteBancairePostal e) {
        return comptesBancairePostal.add(e);
    }

    public boolean add(ContratEntretienViager e) {
        return contratsEntretienViager.add(e);
    }

    public boolean add(CotisationPsal e) {
        return cotisationsPsal.add(e);
    }

    public boolean add(DessaisissementRevenu e) {
        return dessaisissementsRevenu.add(e);
    }

    public boolean add(DessaisissementFortune e) {
        return dessaississementsFortune.add(e);
    }

    public boolean add(IjAi e) {
        return ijAis.add(e);
    }

    public boolean add(IndemniteJournaliereApg e) {
        return indemintesJournaliereApg.add(e);
    }

    public void add(Loyer e) {
        loyers.add(e);
    }

    public boolean add(MarchandiseStock e) {
        return marchandisesStocks.add(e);
    }

    public boolean add(Numeraire e) {
        return numeraires.add(e);
    }

    public boolean add(PensionAlimentaire e) {
        return pensionsAlimentaire.add(e);
    }

    public boolean add(PretEnversTiers e) {
        return pretsEnversTiers.add(e);
    }

    public boolean add(RenteAvsAi e) {
        return rentesAvsAi.add(e);
    }

    public boolean add(RevenuActiviteLucrativeIndependante e) {
        return revenusActiviteLucrativeIndependante.add(e);
    }

    public boolean add(RevenuActiviteLucrativeDependante e) {
        return revenusActiviteLucrativeDependante.add(e);
    }

    public boolean add(RevenuHypothtique e) {
        return revenusHypothtique.add(e);
    }

    public boolean add(TaxeJournaliereHome e) {
        return taxesJournaliereHome.add(e);
    }

    public boolean add(Titre e) {
        return titres.add(e);
    }

    public boolean add(Vehicule e) {
        return vehicules.add(e);
    }

    public DonneesFinancieresListBase<AllocationFamilliale> getAllocationsFamilliale() {
        return allocationsFamilliale;
    }

    public DonneesFinancieresListBase<ApiAvsAi> getApisAvsAi() {
        return apisAvsAi;
    }

    public DonneesFinancieresListBase<AssuranceRenteViagere> getAssurancesRentesViageres() {
        return assurancesRentesViageres;
    }

    public DonneesFinancieresListBase<AssuranceVie> getAssurancesVie() {
        return assurancesVie;
    }

    public DonneesFinancieresListBase<AutreApi> getAutresApi() {
        return autresApi;
    }

    public DonneesFinancieresListBase<AutreDetteProuvee> getAutresDettesProuvees() {
        return autresDettesProuvees;
    }

    public DonneesFinancieresListBase<AutreFortuneMobiliere> getAutresFortunesMobilieres() {
        return autresFortunesMobilieres;
    }

    public AutresRentes getAutresRentes(AutreRenteGenre genre) {
        return autresRentes.getAutresRentesByGenre(genre);
    }

    public AutresRentes getAutresRentes() {
        return autresRentes;
    }

    public DonneesFinancieresListBase<AutreRevenu> getAutresRevenus() {
        return autresRevenus;
    }

    public DonneesFinancieresListBase<Betail> getBetails() {
        return betails;
    }

    public BiensImmobiliersNonHabitable getBiensImmobiliersNonHabitable() {
        return biensImmobiliersNonHabitable;
    }

    public BiensImmobiliersNonPrincipale getBiensImmobiliersNonPrincipale() {
        return biensImmobiliersNonPrincipale;
    }

    public BiensImmobiliersServantHabitationPrincipale getBiensImmobiliersServantHbitationPrincipale() {
        return biensImmobiliersServantHbitationPrincipale;
    }

    public DonneesFinancieresListBase<CapitalLpp> getCapitalsLpp() {
        return capitalsLpp;
    }

    public DonneesFinancieresListBase<CompteBancairePostal> getComptesBancairePostal() {
        return comptesBancairePostal;
    }

    public DonneesFinancieresListBase<ContratEntretienViager> getContratsEntretienViager() {
        return contratsEntretienViager;
    }

    public DonneesFinancieresListBase<CotisationPsal> getCotisationsPsal() {
        return cotisationsPsal;
    }

    public DonneesFinancieresListBase<DessaisissementRevenu> getDessaisissementsRevenu() {
        return dessaisissementsRevenu;
    }

    public DonneesFinancieresListBase<DessaisissementFortune> getDessaississementsFortune() {
        return dessaississementsFortune;
    }

    public DonneesFinancieresListBase<IjAi> getIjAis() {
        return ijAis;
    }

    public DonneesFinancieresListBase<IndemniteJournaliereApg> getIndemintesJournaliereApg() {
        return indemintesJournaliereApg;
    }

    public Loyers getLoyers() {
        return loyers;
    }

    public DonneesFinancieresListBase<MarchandiseStock> getMarchandisesStocks() {
        return marchandisesStocks;
    }

    public DonneesFinancieresListBase<Numeraire> getNumeraires() {
        return numeraires;
    }

    public DonneesFinancieresListBase<PensionAlimentaire> getPensionsAlimentaire() {
        return pensionsAlimentaire;
    }

    public DonneesFinancieresListBase<PensionAlimentaire> getPensionsAlimentaireByType(PensionAlimentaireType type) {
        return PensionsAlimentaires.getPensionAlimentaireByType(pensionsAlimentaire, type);
    }

    public DonneesFinancieresListBase<PretEnversTiers> getPretsEnversTiers() {
        return pretsEnversTiers;
    }

    public DonneesFinancieresListBase<RenteAvsAi> getRentesAvsAi() {
        return rentesAvsAi;
    }

    public DonneesFinancieresListBase<RevenuActiviteLucrativeDependante> getRevenusActiviteLucrativeDependante() {
        return revenusActiviteLucrativeDependante;
    }

    public DonneesFinancieresListBase<RevenuHypothtique> getRevenusHypothtique() {
        return revenusHypothtique;
    }

    public DonneesFinancieresListBase<RevenuActiviteLucrativeIndependante> getRevenusActiviteLucrativeIndependante() {
        return revenusActiviteLucrativeIndependante;
    }

    public DonneesFinancieresListBase<TaxeJournaliereHome> getTaxesJournaliereHome() {
        return taxesJournaliereHome;
    }

    public DonneesFinancieresListBase<Titre> getTitres() {
        return titres;
    }

    public DonneesFinancieresListBase<Vehicule> getVehicules() {
        return vehicules;
    }

    public BiensImmobiliersListBase getAllBiensImmobilier() {
        List<BienImmobilier> list = new ArrayList<BienImmobilier>();
        list.addAll(biensImmobiliersNonHabitable.getList());
        list.addAll(biensImmobiliersNonPrincipale.getList());
        list.addAll(biensImmobiliersServantHbitationPrincipale.getList());
        BiensImmobiliersListBase dflist = new BiensImmobiliersListBase();
        dflist.addAll(list);

        return dflist;
    }

    public DonneesFinancieresListBase<DonneeFinanciere> getAllDonneesWithInteret() {
        List<DonneeFinanciere> list = new ArrayList<DonneeFinanciere>();
        list.addAll(comptesBancairePostal.getList());
        list.addAll(titres.getList());
        list.addAll(pretsEnversTiers.getList());
        list.addAll(numeraires.getList());
        list.addAll(capitalsLpp.getList());
        DonneesFinancieresListBase<DonneeFinanciere> dflist = new DonneesFinancieresListBase<DonneeFinanciere>();
        dflist.addAll(list);

        return dflist;
    }

    @Override
    public String toString() {
        return "DonneesFinancieresContainer [allocationsFamilliale=" + allocationsFamilliale.size() + ", apisAvsAi="
                + apisAvsAi.size() + ", assurancesRentesViageres=" + assurancesRentesViageres.size()
                + ", assurancesVie=" + assurancesVie.size() + ", autresApi=" + autresApi.size()
                + ", autresDettesProuvees=" + autresDettesProuvees.size() + ", autresFortunesMobilieres="
                + autresFortunesMobilieres.size() + ", autresRentes=" + autresRentes.size() + ", autresRevenues="
                + autresRevenus.size() + ", betails=" + betails.size() + ", biensImmobiliersNonHabitable="
                + biensImmobiliersNonHabitable.size() + ", biensImmobiliersNonPrincipale="
                + biensImmobiliersNonPrincipale.size() + ", biensImmobiliersServantHbitationPrincipale="
                + biensImmobiliersServantHbitationPrincipale.size() + ", capitalsLpp=" + capitalsLpp.size()
                + ", comptesBancairePostal=" + comptesBancairePostal.size() + ", contratsEntretienViager="
                + contratsEntretienViager.size() + ", cotisationsPsal=" + cotisationsPsal.size()
                + ", dessaisissementsRevenu=" + dessaisissementsRevenu.size() + ", dessaississementsFortune="
                + dessaississementsFortune.size() + ", ijAis=" + ijAis.size() + ", indemintesJournaliereApg="
                + indemintesJournaliereApg.size() + ", loyers=" + loyers.size() + ", taxesJournaliereHome="
                + taxesJournaliereHome.size() + ", marchandisesStocks=" + marchandisesStocks.size() + ", numeraires="
                + numeraires.size() + ", pensionsAlimentaire=" + pensionsAlimentaire.size() + ", pretsEnversTiers="
                + pretsEnversTiers.size() + ", rentesAvsAi=" + rentesAvsAi.size()
                + ", revenusActiviteLucrativeIndependante=" + revenusActiviteLucrativeIndependante.size()
                + ", revenuesActiviteLucrativeDependante=" + revenusActiviteLucrativeDependante.size()
                + ", revenuesHypothtique=" + revenusHypothtique.size() + ", titres=" + titres.size() + ", vehicules="
                + vehicules.size() + "]";
    }

    public boolean addAll(Regimes regimesRfm) {
        return regimes.addAll(regimesRfm);

    }

    public boolean add(Regime regimeRfm) {
        return regimes.add(regimeRfm);
    }

    public DonneesFinancieresListBase<Regime> getRegimesRfm() {
        return regimes;
    }
}
