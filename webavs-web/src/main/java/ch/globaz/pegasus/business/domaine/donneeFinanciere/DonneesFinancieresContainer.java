package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.allocationFamilliale.AllocationFamilliale;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.autreApi.AutreApi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.avsAi.ApiAvsAi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.assuranceRenteViagere.AssuranceRenteViagere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.assuranceRenteViagere.AssurancesRenteViagere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.assuranceVie.AssuranceVie;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.assurancemaladie.PrimeAssuranceMaladie;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.assurancemaladie.SubsideAssuranceMaladie;
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
import ch.globaz.pegasus.business.domaine.donneeFinanciere.fraisdegarde.FraisDeGarde;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.iJAi.IjAi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.iJAi.IjsAi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.indeminteJournaliereApg.IndemniteJournaliereApg;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.indeminteJournaliereApg.IndemnitesJournalieresApg;
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
import ch.globaz.pegasus.business.domaine.donneeFinanciere.sejourmoispartiel.SejourMoisPartiel;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.taxeJournalierHome.TaxeJournaliereHome;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.taxeJournalierHome.TaxesJournalieresHome;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.titre.Titre;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.vehicule.Vehicule;
import ch.globaz.pegasus.business.domaine.membreFamille.MembreFamille;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.businessimpl.services.revisionquadriennale.Regimes;

import java.util.ArrayList;
import java.util.List;

public class DonneesFinancieresContainer {
    private DonneesFinancieresListBase<AllocationFamilliale> allocationsFamilliale = new DonneesFinancieresListBase<AllocationFamilliale>();
    private DonneesFinancieresListBase<ApiAvsAi> apisAvsAi = new DonneesFinancieresListBase<ApiAvsAi>();
    private AssurancesRenteViagere assurancesRentesViageres = new AssurancesRenteViagere();
    private DonneesFinancieresListBase<AssuranceVie> assurancesVie = new DonneesFinancieresListBase<AssuranceVie>();
    private DonneesFinancieresListBase<PrimeAssuranceMaladie> primeAssuranceMaladie = new DonneesFinancieresListBase<>();
    private DonneesFinancieresListBase<SubsideAssuranceMaladie> subsideAssuranceMaladie = new DonneesFinancieresListBase<>();
    private DonneesFinancieresListBase<AutreApi> autresApi = new DonneesFinancieresListBase<AutreApi>();
    private DonneesFinancieresListBase<AutreDetteProuvee> autresDettesProuvees = new DonneesFinancieresListBase<AutreDetteProuvee>();
    private DonneesFinancieresListBase<AutreFortuneMobiliere> autresFortunesMobilieres = new DonneesFinancieresListBase<AutreFortuneMobiliere>();
    private AutresRentes autresRentes = new AutresRentes();
    private DonneesFinancieresListBase<AutreRevenu> autresRevenus = new DonneesFinancieresListBase<AutreRevenu>();
    private DonneesFinancieresListBase<Betail> betails = new DonneesFinancieresListBase<Betail>();
    private BiensImmobiliersNonHabitable biensImmobiliersNonHabitable = new BiensImmobiliersNonHabitable();
    private BiensImmobiliersNonPrincipale biensImmobiliersNonPrincipale = new BiensImmobiliersNonPrincipale();
    private BiensImmobiliersServantHabitationPrincipale biensImmobiliersServantHabitationPrincipale = new BiensImmobiliersServantHabitationPrincipale();
    private DonneesFinancieresListBase<CapitalLpp> capitalsLpp = new DonneesFinancieresListBase<CapitalLpp>();
    private DonneesFinancieresListBase<CompteBancairePostal> comptesBancairePostal = new DonneesFinancieresListBase<CompteBancairePostal>();
    private DonneesFinancieresListBase<ContratEntretienViager> contratsEntretienViager = new DonneesFinancieresListBase<ContratEntretienViager>();
    private DonneesFinancieresListBase<CotisationPsal> cotisationsPsal = new DonneesFinancieresListBase<CotisationPsal>();
    private DonneesFinancieresListBase<DessaisissementRevenu> dessaisissementsRevenu = new DonneesFinancieresListBase<DessaisissementRevenu>();
    private DonneesFinancieresListBase<DessaisissementFortune> dessaississementsFortune = new DonneesFinancieresListBase<DessaisissementFortune>();
    private IjsAi ijAis = new IjsAi();
    private IndemnitesJournalieresApg indemintesJournaliereApg = new IndemnitesJournalieresApg();
    private Loyers loyers = new Loyers();
    private TaxesJournalieresHome taxesJournaliereHome = new TaxesJournalieresHome();
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
    private DonneesFinancieresListBase<FraisDeGarde> fraisDeGarde = new DonneesFinancieresListBase<>();
    private DonneesFinancieresListBase<SejourMoisPartiel> sejourMoisPartiel = new DonneesFinancieresListBase<>();

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

    public boolean add(PrimeAssuranceMaladie e) {
        return primeAssuranceMaladie.add(e);
    }

    public boolean add(SubsideAssuranceMaladie e) {
        return subsideAssuranceMaladie.add(e);
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
        return biensImmobiliersServantHabitationPrincipale.add(e);
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

    public boolean add(FraisDeGarde e) {
        return fraisDeGarde.add(e);
    }

    public boolean add(SejourMoisPartiel e) {
        return sejourMoisPartiel.add(e);
    }

    public DonneesFinancieresListBase<AllocationFamilliale> getAllocationsFamilliale() {
        return allocationsFamilliale;
    }

    public DonneesFinancieresListBase<ApiAvsAi> getApisAvsAi() {
        return apisAvsAi;
    }

    public AssurancesRenteViagere getAssurancesRentesViageres() {
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
        return biensImmobiliersServantHabitationPrincipale;
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

    public IjsAi getIjAis() {
        return ijAis;
    }

    public IndemnitesJournalieresApg getIndemintesJournaliereApg() {
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

    public TaxesJournalieresHome getTaxesJournaliereHome() {
        return taxesJournaliereHome;
    }

    public DonneesFinancieresListBase<Titre> getTitres() {
        return titres;
    }

    public DonneesFinancieresListBase<Vehicule> getVehicules() {
        return vehicules;
    }

    public DonneesFinancieresListBase<FraisDeGarde> getFraisDeGarde() {
        return fraisDeGarde;
    }

    public DonneesFinancieresListBase<SejourMoisPartiel> getSejourMoisPartiel() {
        return sejourMoisPartiel;
    }

    public DonneesFinancieresListBase<PrimeAssuranceMaladie> getPrimeAssuranceMaladie() {
        return primeAssuranceMaladie;
    }

    public DonneesFinancieresListBase<SubsideAssuranceMaladie> getSubsideAssuranceMaladie() {
        return subsideAssuranceMaladie;
    }

    public void setSubsideAssuranceMaladie(DonneesFinancieresListBase<SubsideAssuranceMaladie> subsideAssuranceMaladie) {
        this.subsideAssuranceMaladie = subsideAssuranceMaladie;
    }

    public BiensImmobiliersListBase getAllBiensImmobilier() {
        List<BienImmobilier> list = new ArrayList<BienImmobilier>();
        list.addAll(biensImmobiliersNonHabitable.getList());
        list.addAll(biensImmobiliersNonPrincipale.getList());
        list.addAll(biensImmobiliersServantHabitationPrincipale.getList());
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

    /**
     * Filtre toutes les données financière qui sont liées à une personne
     * 
     * @param membreFamille Le membre de famille à sur qui on veux filtrer les données financièers
     * @return Une nouvelle instance du container avec les données financières comprises dans la période données.
     */
    public DonneesFinancieresContainer filtreForMembreFamille(MembreFamille membreFamille) {
        DonneesFinancieresContainer container = new DonneesFinancieresContainer();

        container.allocationsFamilliale = allocationsFamilliale.filtreForMembreFamille(membreFamille);
        container.apisAvsAi = apisAvsAi.filtreForMembreFamille(membreFamille);
        container.assurancesRentesViageres = assurancesRentesViageres.filtreForMembreFamille(membreFamille);
        container.assurancesVie = assurancesVie.filtreForMembreFamille(membreFamille);
        container.autresApi = autresApi.filtreForMembreFamille(membreFamille);
        container.autresDettesProuvees = autresDettesProuvees.filtreForMembreFamille(membreFamille);
        container.autresFortunesMobilieres = autresFortunesMobilieres.filtreForMembreFamille(membreFamille);
        container.autresRentes = autresRentes.filtreForMembreFamille(membreFamille);
        container.autresRevenus = autresRevenus.filtreForMembreFamille(membreFamille);
        container.betails = betails.filtreForMembreFamille(membreFamille);
        container.biensImmobiliersNonHabitable = biensImmobiliersNonHabitable.filtreForMembreFamille(membreFamille);
        container.biensImmobiliersNonPrincipale = biensImmobiliersNonPrincipale.filtreForMembreFamille(membreFamille);
        container.biensImmobiliersServantHabitationPrincipale = biensImmobiliersServantHabitationPrincipale
                .filtreForMembreFamille(membreFamille);
        container.capitalsLpp = capitalsLpp.filtreForMembreFamille(membreFamille);
        container.comptesBancairePostal = comptesBancairePostal.filtreForMembreFamille(membreFamille);
        container.contratsEntretienViager = contratsEntretienViager.filtreForMembreFamille(membreFamille);
        container.cotisationsPsal = cotisationsPsal.filtreForMembreFamille(membreFamille);
        container.dessaisissementsRevenu = dessaisissementsRevenu.filtreForMembreFamille(membreFamille);
        container.dessaississementsFortune = dessaississementsFortune.filtreForMembreFamille(membreFamille);
        container.ijAis = ijAis.filtreForMembreFamille(membreFamille);
        container.indemintesJournaliereApg = indemintesJournaliereApg.filtreForMembreFamille(membreFamille);
        container.loyers = loyers.filtreForMembreFamille(membreFamille);
        container.taxesJournaliereHome = taxesJournaliereHome.filtreForMembreFamille(membreFamille);
        container.marchandisesStocks = marchandisesStocks.filtreForMembreFamille(membreFamille);
        container.numeraires = numeraires.filtreForMembreFamille(membreFamille);
        container.pensionsAlimentaire = pensionsAlimentaire.filtreForMembreFamille(membreFamille);
        container.pretsEnversTiers = pretsEnversTiers.filtreForMembreFamille(membreFamille);
        container.rentesAvsAi = rentesAvsAi.filtreForMembreFamille(membreFamille);
        container.revenusActiviteLucrativeIndependante = revenusActiviteLucrativeIndependante
                .filtreForMembreFamille(membreFamille);
        container.revenusActiviteLucrativeDependante = revenusActiviteLucrativeDependante
                .filtreForMembreFamille(membreFamille);
        container.revenusHypothtique = revenusHypothtique.filtreForMembreFamille(membreFamille);
        container.fraisDeGarde = fraisDeGarde.filtreForMembreFamille(membreFamille);
        container.titres = titres.filtreForMembreFamille(membreFamille);
        container.vehicules = vehicules.filtreForMembreFamille(membreFamille);
        container.regimes = regimes.filtreForMembreFamille(membreFamille);
        container.sejourMoisPartiel = sejourMoisPartiel.filtreForMembreFamille(membreFamille);
        container.primeAssuranceMaladie = primeAssuranceMaladie.filtreForMembreFamille(membreFamille);
        container.subsideAssuranceMaladie = subsideAssuranceMaladie.filtreForMembreFamille(membreFamille);

        return container;
    }

    /**
     * Filtre toutes les données financière qui sont liées à une personne
     * 
     * @param membreFamille Le membre de famille à sur qui on veux filtrer les données financièers
     * @return Une nouvelle instance du container avec les données financières comprises dans la période données.
     */
    public DonneesFinancieresContainer filtreForRole(RoleMembreFamille roleMembreFamille) {
        DonneesFinancieresContainer container = new DonneesFinancieresContainer();

        container.allocationsFamilliale = allocationsFamilliale.filtreForRole(roleMembreFamille);
        container.apisAvsAi = apisAvsAi.filtreForRole(roleMembreFamille);
        container.assurancesRentesViageres = assurancesRentesViageres.filtreForRole(roleMembreFamille);
        container.assurancesVie = assurancesVie.filtreForRole(roleMembreFamille);
        container.autresApi = autresApi.filtreForRole(roleMembreFamille);
        container.autresDettesProuvees = autresDettesProuvees.filtreForRole(roleMembreFamille);
        container.autresFortunesMobilieres = autresFortunesMobilieres.filtreForRole(roleMembreFamille);
        container.autresRentes = autresRentes.filtreForRole(roleMembreFamille);
        container.autresRevenus = autresRevenus.filtreForRole(roleMembreFamille);
        container.betails = betails.filtreForRole(roleMembreFamille);
        container.biensImmobiliersNonHabitable = biensImmobiliersNonHabitable.filtreForRole(roleMembreFamille);
        container.biensImmobiliersNonPrincipale = biensImmobiliersNonPrincipale.filtreForRole(roleMembreFamille);
        container.biensImmobiliersServantHabitationPrincipale = biensImmobiliersServantHabitationPrincipale
                .filtreForRole(roleMembreFamille);
        container.capitalsLpp = capitalsLpp.filtreForRole(roleMembreFamille);
        container.comptesBancairePostal = comptesBancairePostal.filtreForRole(roleMembreFamille);
        container.contratsEntretienViager = contratsEntretienViager.filtreForRole(roleMembreFamille);
        container.cotisationsPsal = cotisationsPsal.filtreForRole(roleMembreFamille);
        container.dessaisissementsRevenu = dessaisissementsRevenu.filtreForRole(roleMembreFamille);
        container.dessaississementsFortune = dessaississementsFortune.filtreForRole(roleMembreFamille);
        container.ijAis = ijAis.filtreForRole(roleMembreFamille);
        container.indemintesJournaliereApg = indemintesJournaliereApg.filtreForRole(roleMembreFamille);
        container.loyers = loyers.filtreForRole(roleMembreFamille);
        container.taxesJournaliereHome = taxesJournaliereHome.filtreForRole(roleMembreFamille);
        container.marchandisesStocks = marchandisesStocks.filtreForRole(roleMembreFamille);
        container.numeraires = numeraires.filtreForRole(roleMembreFamille);
        container.pensionsAlimentaire = pensionsAlimentaire.filtreForRole(roleMembreFamille);
        container.pretsEnversTiers = pretsEnversTiers.filtreForRole(roleMembreFamille);
        container.rentesAvsAi = rentesAvsAi.filtreForRole(roleMembreFamille);
        container.revenusActiviteLucrativeIndependante = revenusActiviteLucrativeIndependante
                .filtreForRole(roleMembreFamille);
        container.revenusActiviteLucrativeDependante = revenusActiviteLucrativeDependante
                .filtreForRole(roleMembreFamille);
        container.revenusHypothtique = revenusHypothtique.filtreForRole(roleMembreFamille);
        container.fraisDeGarde = fraisDeGarde.filtreForRole(roleMembreFamille);
        container.titres = titres.filtreForRole(roleMembreFamille);
        container.vehicules = vehicules.filtreForRole(roleMembreFamille);
        container.regimes = regimes.filtreForRole(roleMembreFamille);
        container.sejourMoisPartiel = sejourMoisPartiel.filtreForRole(roleMembreFamille);
        container.primeAssuranceMaladie = primeAssuranceMaladie.filtreForRole(roleMembreFamille);
        container.subsideAssuranceMaladie = subsideAssuranceMaladie.filtreForRole(roleMembreFamille);

        return container;
    }

    /**
     * Filtre toutes les données financière qui sont comprises dans la période données.
     * 
     * @param debut Date de début de la période.
     * @param fin Date de fin de la période.
     * @return Une nouvelle instance du container avec les données financières comprises dans la période données.
     */
    public DonneesFinancieresContainer filtreForPeriode(Date debut, Date fin) {
        DonneesFinancieresContainer container = new DonneesFinancieresContainer();

        container.allocationsFamilliale = allocationsFamilliale.filtreForPeriode(debut, fin);
        container.apisAvsAi = apisAvsAi.filtreForPeriode(debut, fin);
        container.assurancesRentesViageres = assurancesRentesViageres.filtreForPeriode(debut, fin);
        container.assurancesVie = assurancesVie.filtreForPeriode(debut, fin);
        container.autresApi = autresApi.filtreForPeriode(debut, fin);
        container.autresDettesProuvees = autresDettesProuvees.filtreForPeriode(debut, fin);
        container.autresFortunesMobilieres = autresFortunesMobilieres.filtreForPeriode(debut, fin);
        container.autresRentes = autresRentes.filtreForPeriode(debut, fin);
        container.autresRevenus = autresRevenus.filtreForPeriode(debut, fin);
        container.betails = betails.filtreForPeriode(debut, fin);
        container.biensImmobiliersNonHabitable = biensImmobiliersNonHabitable.filtreForPeriode(debut, fin);
        container.biensImmobiliersNonPrincipale = biensImmobiliersNonPrincipale.filtreForPeriode(debut, fin);
        container.biensImmobiliersServantHabitationPrincipale = biensImmobiliersServantHabitationPrincipale
                .filtreForPeriode(debut, fin);
        container.capitalsLpp = capitalsLpp.filtreForPeriode(debut, fin);
        container.comptesBancairePostal = comptesBancairePostal.filtreForPeriode(debut, fin);
        container.contratsEntretienViager = contratsEntretienViager.filtreForPeriode(debut, fin);
        container.cotisationsPsal = cotisationsPsal.filtreForPeriode(debut, fin);
        container.dessaisissementsRevenu = dessaisissementsRevenu.filtreForPeriode(debut, fin);
        container.dessaississementsFortune = dessaississementsFortune.filtreForPeriode(debut, fin);
        container.ijAis = ijAis.filtreForPeriode(debut, fin);
        container.indemintesJournaliereApg = indemintesJournaliereApg.filtreForPeriode(debut, fin);
        container.loyers = loyers.filtreForPeriode(debut, fin);
        container.taxesJournaliereHome = taxesJournaliereHome.filtreForPeriode(debut, fin);
        container.marchandisesStocks = marchandisesStocks.filtreForPeriode(debut, fin);
        container.numeraires = numeraires.filtreForPeriode(debut, fin);
        container.pensionsAlimentaire = pensionsAlimentaire.filtreForPeriode(debut, fin);
        container.pretsEnversTiers = pretsEnversTiers.filtreForPeriode(debut, fin);
        container.rentesAvsAi = rentesAvsAi.filtreForPeriode(debut, fin);
        container.revenusActiviteLucrativeIndependante = revenusActiviteLucrativeIndependante.filtreForPeriode(debut,
                fin);
        container.revenusActiviteLucrativeDependante = revenusActiviteLucrativeDependante.filtreForPeriode(debut, fin);
        container.revenusHypothtique = revenusHypothtique.filtreForPeriode(debut, fin);
        container.fraisDeGarde = fraisDeGarde.filtreForPeriode(debut, fin);
        container.titres = titres.filtreForPeriode(debut, fin);
        container.vehicules = vehicules.filtreForPeriode(debut, fin);
        container.regimes = regimes.filtreForPeriode(debut, fin);
        container.sejourMoisPartiel = sejourMoisPartiel.filtreForPeriode(debut, fin);
        container.primeAssuranceMaladie = primeAssuranceMaladie.filtreForPeriode(debut, fin);
        container.subsideAssuranceMaladie = subsideAssuranceMaladie.filtreForPeriode(debut, fin);

        return container;
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
                + biensImmobiliersServantHabitationPrincipale.size() + ", capitalsLpp=" + capitalsLpp.size()
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
                + vehicules.size()  + ", fraisDeGarde="
                + fraisDeGarde.size() + ", primeAssuranceMaladie="
                + primeAssuranceMaladie.size() + ", subsideAssuranceMaladie="
                + subsideAssuranceMaladie.size() + ", sejourMoisPartiel="
                + sejourMoisPartiel.size()+ "]";
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
