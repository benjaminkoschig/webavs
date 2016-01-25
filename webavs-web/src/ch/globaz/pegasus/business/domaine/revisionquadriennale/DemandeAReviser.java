package ch.globaz.pegasus.business.domaine.revisionquadriennale;

import ch.globaz.common.domaine.Adresse;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Taux;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresContainer;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Each;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente.AutreRenteGenre;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente.AutresRentes;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierServantHbitationPrincipale.BienImmobilierServantHabitationPrincipale;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.loyer1.Loyer;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.pensionAlimentaire.PensionAlimentaireType;
import ch.globaz.pegasus.business.domaine.membreFamille.MembresFamilles;
import ch.globaz.pegasus.business.domaine.parametre.Parameters;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariableMetierType;
import ch.globaz.pegasus.business.domaine.pca.PcaRequerantConjoint;
import ch.globaz.pegasus.business.domaine.pca.PcaSitutation;
import ch.globaz.pegasus.business.models.revisionquadriennale.ListrevisionWithPcaRequerantConjoint;
import ch.globaz.pyxis.domaine.Pays;
import ch.globaz.pyxis.domaine.PersonneAVS;

public class DemandeAReviser implements RevisionCsv {

    private final DonneesFinancieresContainer donneesFinancieres;
    private final ListrevisionWithPcaRequerantConjoint demande;
    private final Adresse adresseCourrier;
    private final Adresse adresseDomicile;
    private final Pays pays;
    private final PcaRequerantConjoint pcas;
    private final Parameters parameters;
    private final MembresFamilles membresFamilles;
    private final PersonneAVS conjoint;

    public DemandeAReviser(DonneesFinancieresContainer donneesFinancieres,
            ListrevisionWithPcaRequerantConjoint demande, Adresse adresseCourrier, Adresse adresseDomicile, Pays pays,
            PcaRequerantConjoint pcas, MembresFamilles membresFamilles, Parameters parameters) {
        this.donneesFinancieres = donneesFinancieres;
        this.demande = demande;
        this.adresseCourrier = adresseCourrier;
        this.adresseDomicile = adresseDomicile;
        this.pays = pays;
        this.pcas = pcas;
        this.membresFamilles = membresFamilles;
        conjoint = membresFamilles.getConjoint().getPersonne();
        this.parameters = parameters;
    }

    @Override
    public String getNss() {
        return demande.getNssRequerant();
    }

    @Override
    public String getNomPrenom() {
        return demande.getNomRequerant() + " " + demande.getPrenomRequerant();
    }

    @Override
    public String getDateDeNaissance() {
        return demande.getDateNaissanceRequerant();
    }

    @Override
    public String getSexe() {
        return demande.getSexeRequerant();
    }

    @Override
    public String getSexeTitre() {
        return demande.getSexeRequerant();
    }

    @Override
    public String getNationalite() {
        return pays.getTraductionParLangue().get(Langues.Francais);
    }

    @Override
    public String getNssConjoint() {
        if (conjoint.estInitialisee()) {
            return conjoint.getNss().toString();
        } else {
            return "";
        }
    }

    @Override
    public String getNomPrenomConjoint() {
        return conjoint.getNom() + " " + conjoint.getPrenom();
    }

    @Override
    public String getDateDeNaissanceConjoint() {
        return conjoint.getDateNaissance();
    }

    @Override
    public String getSexeConjoint() {
        if (conjoint.estInitialisee()) {
            return String.valueOf(conjoint.getSexe().getCodeSysteme());
        } else {
            return "";
        }
    }

    @Override
    public String getSexeTitreConjoint() {
        if (conjoint.estInitialisee()) {
            return String.valueOf(conjoint.getSexe().getCodeSysteme());
        } else {
            return "";
        }
    }

    @Override
    public String getGestionnaire() {
        return demande.getIdGestionnaire();
    }

    @Override
    public String getDateRevision() {
        return demande.getDateProchaineRevision();
    }

    @Override
    public String getAgenceCommunale() {
        return demande.getAgenceDesignation1() + " " + demande.getAgenceDesignation2();
    }

    @Override
    public String getMotifRevision() {
        return demande.getMotifProchaineRevision();
    }

    @Override
    public String getAdresseCourrierRue() {
        return adresseCourrier.getRue();
    }

    @Override
    public String getAdresseCourrierNumero() {
        return adresseCourrier.getRueNumero();
    }

    @Override
    public String getAdresseCourrierNpa() {
        return adresseCourrier.getNpa();
    }

    @Override
    public String getAdresseCourrierLocalite() {
        return adresseCourrier.getLocalite();
    }

    @Override
    public String getAdresseCourrierTitre() {
        return adresseCourrier.getCsTitre();
    }

    @Override
    public String getAdresseCourrierCasePostal() {
        return adresseCourrier.getCasePostale();
    }

    @Override
    public String getAdresseCourrierNom() {
        return adresseCourrier.getDesignation1();
    }

    @Override
    public String getAdresseCourrierPrenom() {
        return adresseCourrier.getDesignation2();
    }

    @Override
    public String getAdresseDomicileRue() {
        return adresseDomicile.getRue();
    }

    @Override
    public String getAdresseDomicileNumero() {
        return adresseDomicile.getRueNumero();
    }

    @Override
    public String getAdresseDomicileNpa() {
        return adresseDomicile.getNpa();
    }

    @Override
    public String getAdresseDomicileLocalite() {
        return adresseDomicile.getLocalite();
    }

    @Override
    public String getAdresseDomicileTitre() {
        return adresseDomicile.getCsTitre();
    }

    @Override
    public String getAdresseDomicileNom() {
        return adresseDomicile.getDesignation1();
    }

    @Override
    public String getAdresseDomicilePrenom() {
        return adresseDomicile.getDesignation2();
    }

    @Override
    public String getAdresseDomicileCasePostal() {
        return adresseDomicile.getCasePostale();
    }

    @Override
    public Integer getNbAdulte() {
        return membresFamilles.countRequerantConjoint();
    }

    @Override
    public Integer getNbEnfant() {
        return membresFamilles.countNbEnfant();
    }

    @Override
    public PcaSitutation getCasPca() {
        return pcas.resolveCasPca();
    }

    @Override
    public Montant getFortuneArgentRequerant() {
        Montant compteBanquaireCCp = donneesFinancieres.getComptesBancairePostal().getDonneesForRequerant()
                .sumFortunePartPropriete();
        Montant pretTier = donneesFinancieres.getPretsEnversTiers().getDonneesForRequerant().sumFortunePartPropriete();
        Montant titre = donneesFinancieres.getTitres().getDonneesForRequerant().sumFortunePartPropriete();
        Montant numeraire = donneesFinancieres.getNumeraires().getDonneesForRequerant().sumFortunePartPropriete();

        return compteBanquaireCCp.add(pretTier).add(titre).add(numeraire);
    }

    @Override
    public Montant getFortuneArgentCojnEnf() {

        Montant compteBanquaireCCp = donneesFinancieres.getComptesBancairePostal().getDonneesForConjointEnfant()
                .sumFortunePartPropriete();
        Montant pretTier = donneesFinancieres.getPretsEnversTiers().getDonneesForRequerant().sumFortunePartPropriete();
        Montant titre = donneesFinancieres.getTitres().getDonneesForConjointEnfant().sumFortunePartPropriete();
        Montant numeraire = donneesFinancieres.getNumeraires().getDonneesForConjointEnfant().sumFortunePartPropriete();

        return compteBanquaireCCp.add(pretTier).add(titre).add(numeraire);
    }

    @Override
    public Montant getFortuneValeurRachatAssuranceVieRequerant() {
        Montant assurancesRentesViager = donneesFinancieres.getAssurancesRentesViageres().getDonneesForRequerant()
                .sumFortuneBrut();
        Montant assuranceVie = donneesFinancieres.getAssurancesVie().getDonneesForRequerant().sumFortuneBrut();
        return assurancesRentesViager.add(assuranceVie);
    }

    @Override
    public Montant getFortuneValeurRachatAssuranceVieConjEnf() {
        Montant assurancesRentesViager = donneesFinancieres.getAssurancesRentesViageres().getDonneesForConjointEnfant()
                .sumFortuneBrut();
        Montant assuranceVie = donneesFinancieres.getAssurancesVie().getDonneesForConjointEnfant().sumFortuneBrut();
        return assurancesRentesViager.add(assuranceVie);
    }

    @Override
    public Montant getFortuneBienDessaisiRequerant() {
        Montant fortune = donneesFinancieres.getDessaississementsFortune().getDonneesForRequerant().sumFortune();
        Montant revenu = donneesFinancieres.getDessaisissementsRevenu().getDonneesForRequerant().sumRevenuAnnuel();
        return fortune.add(revenu);
    }

    @Override
    public Montant getFortuneBienDessaisiConjEnfant() {
        Montant fortune = donneesFinancieres.getDessaississementsFortune().getDonneesForConjointEnfant().sumFortune();
        Montant revenu = donneesFinancieres.getDessaisissementsRevenu().getDonneesForConjointEnfant().sumRevenuAnnuel();
        return fortune.add(revenu);
    }

    @Override
    public Montant getFortuneAutreBienRequerant() {
        Montant marchandiseStock = donneesFinancieres.getMarchandisesStocks().getDonneesForRequerant()
                .sumFortunePartPropriete();
        Montant vehicule = donneesFinancieres.getVehicules().getDonneesForRequerant().sumFortunePartPropriete();
        Montant betail = donneesFinancieres.getBetails().getDonneesForRequerant().sumFortunePartPropriete();
        Montant lpp = donneesFinancieres.getCapitalsLpp().getDonneesForRequerant().sumFortunePartPropriete();
        Montant autreFortuneMobiliere = donneesFinancieres.getAutresFortunesMobilieres().getDonneesForRequerant()
                .sumFortunePartPropriete();
        return marchandiseStock.add(vehicule).add(betail).add(autreFortuneMobiliere).add(lpp);
    }

    @Override
    public Montant getFortuneAutreBienConjEnfant() {
        Montant marchandiseStock = donneesFinancieres.getMarchandisesStocks().getDonneesForConjointEnfant()
                .sumFortunePartPropriete();
        Montant vehicule = donneesFinancieres.getVehicules().getDonneesForConjointEnfant().sumFortunePartPropriete();
        Montant betial = donneesFinancieres.getBetails().getDonneesForConjointEnfant().sumFortunePartPropriete();
        Montant lpp = donneesFinancieres.getCapitalsLpp().getDonneesForConjointEnfant().sumFortunePartPropriete();
        Montant autreFortuneMobiliere = donneesFinancieres.getAutresFortunesMobilieres().getDonneesForConjointEnfant()
                .sumFortunePartPropriete();

        return marchandiseStock.add(vehicule).add(betial).add(autreFortuneMobiliere).add(lpp);
    }

    @Override
    public Montant getFortuneImmeublePrincipaleNonAgricoleRequerant() {
        return donneesFinancieres.getBiensImmobiliersServantHbitationPrincipale().getDonneesForRequerant().sumFortune();
    }

    @Override
    public Montant getFortuneImmeublePrincipaleNonAgricoleConEnf() {
        return donneesFinancieres.getBiensImmobiliersServantHbitationPrincipale().getDonneesForConjointEnfant()
                .sumFortune();
    }

    @Override
    public Montant getFortuneImmeubleNonPrincipalNonAgricoleRequerant() {
        return donneesFinancieres.getBiensImmobiliersNonPrincipale().getDonneesForRequerant().sumFortune();
    }

    @Override
    public Montant getFortuneImmeubleNonPrincipalNonAgricoleConjEnf() {
        return donneesFinancieres.getBiensImmobiliersNonPrincipale().getDonneesForConjointEnfant().sumFortune();
    }

    @Override
    public Montant getFortuneImmeubleAgricoleRequerant() {
        return donneesFinancieres.getBiensImmobiliersNonHabitable().getDonneesForRequerant().sumFortune();
    }

    @Override
    public Montant getFortuneImmeubleAgricoleConjEnf() {
        return donneesFinancieres.getBiensImmobiliersNonHabitable().getDonneesForConjointEnfant().sumFortune();
    }

    @Override
    public Montant getFortuneDetteHypothecaireRequerant() {
        return donneesFinancieres.getAllBiensImmobilier().getDonneesForRequerant().sumDette();
    }

    @Override
    public Montant getFortuneDetteHypothecaireConjEnf() {
        return donneesFinancieres.getAllBiensImmobilier().getDonneesForConjointEnfant().sumDette();
    }

    @Override
    public Montant getFortuneAutreDetteRequerant() {
        return donneesFinancieres.getAutresDettesProuvees().getDonneesForRequerant().sumDetteBrut();
    }

    @Override
    public Montant getFortuneAutreDetteConjEnf() {
        return donneesFinancieres.getAutresDettesProuvees().getDonneesForConjointEnfant().sumDetteBrut();
    }

    @Override
    public Montant getRevenuActiviteLucrativeDependanteRequerant() {
        return donneesFinancieres.getRevenusActiviteLucrativeDependante().getDonneesForRequerant()
                .sumRevenuAnnuelBrut();
    }

    @Override
    public Montant getRevenuActiviteLucrativeDependanteConEnf() {
        return donneesFinancieres.getRevenusActiviteLucrativeDependante().getDonneesForConjointEnfant()
                .sumRevenuAnnuelBrut();
    }

    @Override
    public Montant getRevenuHypothetiqueRequerant() {
        return donneesFinancieres.getRevenusHypothtique().getDonneesForRequerant().sumRevenuAnnuelBrut();
    }

    @Override
    public Montant getRevenuHypothetiqueConjEnfant() {
        return donneesFinancieres.getRevenusHypothtique().getDonneesForConjointEnfant().sumRevenuAnnuelBrut();
    }

    @Override
    public Montant getRevenuLucrativeIndependanteRequerant() {
        return donneesFinancieres.getRevenusActiviteLucrativeIndependante().getDonneesForRequerant()
                .sumRevenuAnnuelBrut();
    }

    @Override
    public Montant getRevenuLucrativeIndependanteConjEnf() {
        return donneesFinancieres.getRevenusActiviteLucrativeIndependante().getDonneesForConjointEnfant()
                .sumRevenuAnnuelBrut();
    }

    @Override
    public Montant getRevenuSalaireNatureRequerant() {
        return donneesFinancieres.getAutresRevenus().getDonneesForRequerant().sumRevenuAnnuelBrut();
    }

    @Override
    public Montant getRevenuSalaireNatureConjoint() {
        return donneesFinancieres.getAutresRevenus().getDonneesForConjointEnfant().sumRevenuAnnuelBrut();
    }

    @Override
    public Montant getRevenuAllocationPourPerteDeGainRequerant() {
        return donneesFinancieres.getIndemintesJournaliereApg().getDonneesForRequerant().sumRevenuAnnuel();
    }

    @Override
    public Montant getRevenuAllocationPourPerteDeGainConjEnf() {
        return donneesFinancieres.getIndemintesJournaliereApg().getDonneesForConjointEnfant().sumRevenuAnnuel();
    }

    @Override
    public Montant getRevenuAllocationFamillialeRequant() {
        return donneesFinancieres.getAllocationsFamilliale().getDonneesForRequerant().sumRevenuAnnuelBrut();
    }

    @Override
    public Montant getRevenuAllocationFamillialeConjEnf() {
        return donneesFinancieres.getAllocationsFamilliale().getDonneesForConjointEnfant().sumRevenuAnnuelBrut();
    }

    @Override
    public Montant getRevenuRenteAnnuelleAvsAiRequant() {
        return donneesFinancieres.getRentesAvsAi().getDonneesForRequerant().sumRevenuAnnuelBrut();
    }

    @Override
    public Montant getRevenuRenteAnnuelleAvsAiConjEnf() {
        return donneesFinancieres.getRentesAvsAi().getDonneesForConjointEnfant().sumRevenuAnnuelBrut();
    }

    @Override
    public Montant getRevenuIndemniteJournaliereAiRequant() {
        return donneesFinancieres.getIjAis().getDonneesForRequerant().sumRevenuAnnuel();
    }

    @Override
    public Montant getRevenuIndemniteJournaliereAiConjEnf() {
        return donneesFinancieres.getIjAis().getDonneesForConjointEnfant().sumRevenuAnnuel();
    }

    @Override
    public Montant getRevenuAllocationPourImpotantRequant() {
        return donneesFinancieres.getApisAvsAi().getDonneesForRequerant().sumRevenuAnnuelBrut();
    }

    @Override
    public Montant getRevenuAllocationPourImpotantConjEnft() {
        return donneesFinancieres.getApisAvsAi().getDonneesForConjointEnfant().sumRevenuAnnuelBrut();
    }

    @Override
    public Montant getRevenuRenteEtrangereRequerant() {
        AutresRentes autresRentes = donneesFinancieres.getAutresRentes().getAutresRentesByGenre(
                AutreRenteGenre.RENTE_ETRANGERE);
        return autresRentes.getDonneesForRequerant().sumAndComputeDevise(parameters.getMonnaiesEtrangere());
    }

    @Override
    public Montant getRevenuRenteEtrangereConjEnf() {

        AutresRentes autresRentes = donneesFinancieres.getAutresRentes(AutreRenteGenre.RENTE_ETRANGERE);
        return autresRentes.getDonneesForConjointEnfant().sumAndComputeDevise(parameters.getMonnaiesEtrangere());

    }

    @Override
    public Montant getRevenuRenteLaaRequerant() {
        return donneesFinancieres.getAutresRentes(AutreRenteGenre.LAA).getDonneesForRequerant().sumRevenuAnnuel();
    }

    @Override
    public Montant getRevenuRenteLaaConjEnf() {
        return donneesFinancieres.getAutresRentes(AutreRenteGenre.LAA).getDonneesForConjointEnfant().sumRevenuAnnuel();
    }

    @Override
    public Montant getRevenuRenteAssurenceMilitaireRequerant() {
        return donneesFinancieres.getAutresRentes(AutreRenteGenre.LAM).getDonneesForRequerant().sumRevenuAnnuel();
    }

    @Override
    public Montant getRevenuRenteAssurenceMilitaireConjEnf() {
        return donneesFinancieres.getAutresRentes(AutreRenteGenre.LAM).getDonneesForConjointEnfant().sumRevenuAnnuel();
    }

    @Override
    public Montant getRevenuRenteLppRequerant() {
        return donneesFinancieres.getAutresRentes(AutreRenteGenre.LPP).getDonneesForRequerant().sumRevenuAnnuel();
    }

    @Override
    public Montant getRevenuRenteLppConjEnf() {
        return donneesFinancieres.getAutresRentes(AutreRenteGenre.LPP).getDonneesForConjointEnfant().sumRevenuAnnuel();
    }

    @Override
    public Montant getRevenuRenteAssuranceVolontaireRequerant() {
        return donneesFinancieres.getAutresRentes(AutreRenteGenre.TROISIEME_PILIER).getDonneesForRequerant()
                .sumRevenuAnnuel();
    }

    @Override
    public Montant getRevenuRenteAssuranceVolontaireConjEnf() {
        return donneesFinancieres.getAutresRentes(AutreRenteGenre.TROISIEME_PILIER).getDonneesForConjointEnfant()
                .sumRevenuAnnuel();
    }

    @Override
    public Montant getRevenuContratEnteretienViagerRequerant() {
        return donneesFinancieres.getContratsEntretienViager().getDonneesForRequerant().sumRevenuAnnuel();
    }

    @Override
    public Montant getRevenuContratEnteretienViagerConjEnf() {
        return donneesFinancieres.getContratsEntretienViager().getDonneesForConjointEnfant().sumRevenuAnnuel();
    }

    @Override
    public Montant getRevenuAutreRenteRequerant() {
        Montant assurancePrivee = donneesFinancieres.getAutresRentes(AutreRenteGenre.ASSURANCE_PRIVEE)
                .getDonneesForRequerant().sumRevenuAnnuel();
        Montant autres = donneesFinancieres.getAutresRentes(AutreRenteGenre.AUTRES).getDonneesForRequerant()
                .sumRevenuAnnuel();

        return assurancePrivee.add(autres);
    }

    @Override
    public Montant getRevenuAutreRenteConjEnf() {
        Montant assurancePrivee = donneesFinancieres.getAutresRentes(AutreRenteGenre.ASSURANCE_PRIVEE)
                .getDonneesForConjointEnfant().sumRevenuAnnuel();
        Montant autres = donneesFinancieres.getAutresRentes(AutreRenteGenre.AUTRES).getDonneesForConjointEnfant()
                .sumRevenuAnnuel();

        return assurancePrivee.add(autres);
    }

    @Override
    public Montant getRevenuRendementLoyerRequerant() {
        Montant biensImmoPrincipale = donneesFinancieres.getBiensImmobiliersServantHbitationPrincipale()
                .getDonneesForRequerant().sumMontantLoyerEncaisse();

        Montant biensImmoNonPrincipale = donneesFinancieres.getBiensImmobiliersNonPrincipale().getDonneesForRequerant()
                .sumMontantLoyerEncaisse();

        Montant biensImmoNonHabitable = donneesFinancieres.getBiensImmobiliersNonHabitable().getDonneesForRequerant()
                .sumMontantRendementBrut();

        return biensImmoPrincipale.add(biensImmoNonPrincipale).add(biensImmoNonHabitable);
    }

    @Override
    public Montant getRevenuRendementLoyerConjEnf() {
        Montant biensImmoPrincipale = donneesFinancieres.getBiensImmobiliersServantHbitationPrincipale()
                .getDonneesForConjointEnfant().sumMontantLoyerEncaisse();

        Montant biensImmoNonPrincipale = donneesFinancieres.getBiensImmobiliersNonPrincipale()
                .getDonneesForConjointEnfant().sumMontantLoyerEncaisse();

        Montant biensImmoNonHabitable = donneesFinancieres.getBiensImmobiliersNonHabitable()
                .getDonneesForConjointEnfant().sumMontantRendementBrut();

        return biensImmoPrincipale.add(biensImmoNonPrincipale).add(biensImmoNonHabitable);
    }

    @Override
    public Montant getRevenuRendementValeurLocativeRequerant() {
        Montant biensImmoPrincipale = donneesFinancieres.getBiensImmobiliersServantHbitationPrincipale()
                .getDonneesForRequerant().sumMontantValeurLocative();

        Montant biensImmoNonPrincipale = donneesFinancieres.getBiensImmobiliersNonPrincipale().getDonneesForRequerant()
                .sumMontantValeurLocative();

        return biensImmoPrincipale.add(biensImmoNonPrincipale);
    }

    @Override
    public Montant getRevenuRendementValeurLocativeConjEnf() {
        Montant biensImmoPrincipale = donneesFinancieres.getBiensImmobiliersServantHbitationPrincipale()
                .getDonneesForConjointEnfant().sumMontantValeurLocative();

        Montant biensImmoNonPrincipale = donneesFinancieres.getBiensImmobiliersNonPrincipale()
                .getDonneesForConjointEnfant().sumMontantValeurLocative();

        return biensImmoPrincipale.add(biensImmoNonPrincipale);
    }

    @Override
    public Montant getRevenuRendementInteretsRequerant() {
        return donneesFinancieres.getAllDonneesWithInteret().getDonneesForRequerant().sumInteret(getTauxOfas());
    }

    @Override
    public Montant getRevenuRendementInteretsConjEnf() {
        return donneesFinancieres.getAllDonneesWithInteret().getDonneesForConjointEnfant().sumInteret(getTauxOfas());
    }

    Taux getTauxOfas() {
        return parameters.getParameters(VariableMetierType.TAUX_OFAS).resolveMostRecent().getTaux();
    }

    @Override
    public Montant getRevenuAutresRevenusValeurUsufruitReq() {
        Montant biensImmoHabitationPrincipale = donneesFinancieres.getBiensImmobiliersServantHbitationPrincipale()
                .getDonneesForRequerant().sumMontantValeurLocativePartPropriete(ProprieteType.USUFRUITIER);

        Montant biensImmoNonPrincipale = donneesFinancieres.getBiensImmobiliersNonPrincipale().getDonneesForRequerant()
                .sumMontantValeurLocativePartPropriete(ProprieteType.USUFRUITIER);

        Montant biensImmoNonHabitable = donneesFinancieres.getBiensImmobiliersNonHabitable().getDonneesForRequerant()
                .sumMontantRendementPartPropriete(ProprieteType.USUFRUITIER);

        return biensImmoHabitationPrincipale.add(biensImmoNonPrincipale).add(biensImmoNonHabitable);
    }

    @Override
    public Montant getRevenuAutresRevenusValeurUsufruitConjEnf() {
        Montant biensImmoHabitationPrincipale = donneesFinancieres.getBiensImmobiliersServantHbitationPrincipale()
                .getDonneesForConjointEnfant().sumMontantValeurLocativePartPropriete(ProprieteType.USUFRUITIER);

        Montant biensImmoNonPrincipale = donneesFinancieres.getBiensImmobiliersNonPrincipale()
                .getDonneesForConjointEnfant().sumMontantValeurLocativePartPropriete(ProprieteType.USUFRUITIER);

        Montant biensImmoNonHabitable = donneesFinancieres.getBiensImmobiliersNonHabitable()
                .getDonneesForConjointEnfant().sumMontantRendementPartPropriete(ProprieteType.USUFRUITIER);

        return biensImmoHabitationPrincipale.add(biensImmoNonPrincipale).add(biensImmoNonHabitable);
    }

    @Override
    public Montant getRevenuAutresRevenusDroitHabitationReq() {
        Montant biensImmoHabitationPrincipale = donneesFinancieres.getBiensImmobiliersServantHbitationPrincipale()
                .getDonneesForRequerant().sumMontantValeurLocative(ProprieteType.DROIT_HABITATION);

        Montant biensImmoNonPrincipale = donneesFinancieres.getBiensImmobiliersNonPrincipale().getDonneesForRequerant()
                .sumMontantValeurLocative(ProprieteType.DROIT_HABITATION);

        return biensImmoHabitationPrincipale.add(biensImmoNonPrincipale);
    }

    @Override
    public Montant getRevenuAutresRevenusDroitHabitationConjEnf() {
        Montant biensImmoHabitationPrincipale = donneesFinancieres.getBiensImmobiliersServantHbitationPrincipale()
                .getDonneesForConjointEnfant().sumMontantValeurLocative(ProprieteType.DROIT_HABITATION);

        Montant biensImmoNonPrincipale = donneesFinancieres.getBiensImmobiliersNonPrincipale()
                .getDonneesForConjointEnfant().sumMontantValeurLocative(ProprieteType.DROIT_HABITATION);

        return biensImmoHabitationPrincipale.add(biensImmoNonPrincipale);
    }

    @Override
    public Montant getRevenuAutresRevenusSousLocationsSansPensionReq() {
        Montant biensImmoHabitationPrincipale = donneesFinancieres.getBiensImmobiliersServantHbitationPrincipale()
                .getDonneesForRequerant().sumSousLocation();

        Montant biensImmoNonPrincipale = donneesFinancieres.getBiensImmobiliersNonPrincipale().getDonneesForRequerant()
                .sumSousLocation();

        Montant loyers = donneesFinancieres.getLoyers().getDonneesForRequerant().sumSousLocation();

        return biensImmoHabitationPrincipale.add(biensImmoNonPrincipale).add(loyers);
    }

    @Override
    public Montant getRevenuAutresRevenusSousLocationsSansPensionConjEnf() {
        Montant biensImmoHabitationPrincipale = donneesFinancieres.getBiensImmobiliersServantHbitationPrincipale()
                .getDonneesForConjointEnfant().sumSousLocation();

        Montant biensImmoNonPrincipale = donneesFinancieres.getBiensImmobiliersNonPrincipale()
                .getDonneesForConjointEnfant().sumSousLocation();

        Montant loyers = donneesFinancieres.getLoyers().getDonneesForConjointEnfant().sumSousLocation();

        return biensImmoHabitationPrincipale.add(biensImmoNonPrincipale).add(loyers);
    }

    @Override
    public Montant getRevenuAutresRevenusPensionAlimentaireReqDivorceeReq() {
        return donneesFinancieres.getPensionsAlimentaireByType(PensionAlimentaireType.DUE).getDonneesForRequerant()
                .sumRevenuAnnuel();
    }

    @Override
    public Montant getRevenuAutresRevenusPensionAlimentaireReqDivorceeConjEnf() {
        return donneesFinancieres.getPensionsAlimentaireByType(PensionAlimentaireType.DUE)
                .getDonneesForConjointEnfant().sumRevenuAnnuel();
    }

    @Override
    public Montant getRevenuAutresRevenusRessourcesOuInteretsFortuneDessaisiReq() {
        Montant fortune = donneesFinancieres.getDessaississementsFortune().getDonneesForRequerant()
                .sumInteret(getTauxOfas());
        // Montant revenu = donneesFinancieres.getDessaisissementsRevenu().getDonneesForRequerant().sumRevenuAnnuel();

        return fortune; // fortune.add(revenu);
    }

    @Override
    public Montant getRevenuAutresRevenusRessourcesOuInteretsFortuneDessaisiConjEnf() {
        Montant fortune = donneesFinancieres.getDessaississementsFortune().getDonneesForConjointEnfant()
                .sumInteret(getTauxOfas());
        // Montant revenu =
        // donneesFinancieres.getDessaisissementsRevenu().getDonneesForConjointEnfant().sumRevenuAnnuel();

        return fortune; // fortune.add(revenu);
    }

    @Override
    public Montant getRevenuAutresRevenusAutresCreancesVersTiersReq() {
        return donneesFinancieres.getAutresRevenus().getDonneesForRequerant().sumRevenuAnnuel();
    }

    @Override
    public Montant getRevenuAutresRevenusAutresCreancesVersTiersConjEnf() {
        return donneesFinancieres.getAutresRevenus().getDonneesForConjointEnfant().sumRevenuAnnuel();
    }

    @Override
    public Montant getDeductionLoyerAnnuellesRequerant() {
        Montant loyer = donneesFinancieres.getLoyers().getDonneesForRequerant().sumDepense();

        Montant valeurLocative = donneesFinancieres.getBiensImmobiliersServantHbitationPrincipale()
                .getDonneesForRequerant().sumDepense();

        return loyer.add(valeurLocative);
    }

    @Override
    public Montant getDeductionLoyerAnnuellesConjEnf() {
        Montant loyer = donneesFinancieres.getLoyers().getDonneesForRequerant().sumDepense();

        Montant valeurLocative = donneesFinancieres.getBiensImmobiliersServantHbitationPrincipale()
                .getDonneesForConjointEnfant().sumDepense();

        return loyer.add(valeurLocative);
    }

    @Override
    public Montant getDeductionChargesAnnuelesRequerant() {
        return donneesFinancieres.getLoyers().getDonneesForRequerant().sum(new Each<Loyer>() {
            @Override
            public Montant getMontant(Loyer loyer) {
                return loyer.computeCharge();
            }
        });
    }

    @Override
    public Montant getDeductionChargesAnnuelesConjEnf() {
        return donneesFinancieres.getLoyers().getDonneesForConjointEnfant().sum(new Each<Loyer>() {
            @Override
            public Montant getMontant(Loyer loyer) {
                return loyer.computeCharge();
            }
        });
    }

    @Override
    public Integer getDeductionNombrePeronneRequerant() {
        Integer sumLoyer = 0;
        for (Loyer loyer : donneesFinancieres.getLoyers().getDonneesForRequerant().getList()) {
            sumLoyer = sumLoyer + loyer.getNbPersonnne();
        }

        Integer sumBienImmo = 0;
        for (BienImmobilierServantHabitationPrincipale bienImmo : donneesFinancieres
                .getBiensImmobiliersServantHbitationPrincipale().getDonneesForRequerant().getList()) {
            sumBienImmo = sumBienImmo + bienImmo.getNbPersonne();
        }

        return sumLoyer + sumBienImmo;
    }

    @Override
    public Integer getDeductionNombrePeronneConjEnf() {
        Integer sumLoyer = 0;
        for (Loyer loyer : donneesFinancieres.getLoyers().getDonneesForConjointEnfant().getList()) {
            sumLoyer = sumLoyer + loyer.getNbPersonnne();
        }

        Integer sumBienImmo = 0;
        for (BienImmobilierServantHabitationPrincipale bienImmo : donneesFinancieres
                .getBiensImmobiliersServantHbitationPrincipale().getDonneesForConjointEnfant().getList()) {
            sumBienImmo = sumBienImmo + bienImmo.getNbPersonne();
        }
        return sumLoyer + sumBienImmo;
    }

    @Override
    public Montant getDeductionCotisationPsalRequerant() {
        return donneesFinancieres.getCotisationsPsal().getDonneesForRequerant().sumDepense();
    }

    @Override
    public Montant getDeductionCotisationPsalPeronneConjEnf() {
        return donneesFinancieres.getCotisationsPsal().getDonneesForConjointEnfant().sumDepense();
    }

    @Override
    public Montant getDeductionInteretsHypothecaireRequerant() {
        Montant principale = donneesFinancieres.getBiensImmobiliersServantHbitationPrincipale()
                .getDonneesForRequerant().sumInteretHypothecaire();

        Montant nonPrincipal = donneesFinancieres.getBiensImmobiliersNonPrincipale().getDonneesForRequerant()
                .sumInteretHypotecaire();

        Montant annexe = donneesFinancieres.getBiensImmobiliersNonHabitable().getDonneesForRequerant()
                .sumInteretHypotecaire();
        return principale.add(nonPrincipal).add(annexe);
    }

    @Override
    public Montant getDeductionInteretsHypothecaireConjEnfant() {
        Montant principale = donneesFinancieres.getBiensImmobiliersServantHbitationPrincipale()
                .getDonneesForConjointEnfant().sumInteretHypothecaire();

        Montant nonPrincipal = donneesFinancieres.getBiensImmobiliersNonPrincipale().getDonneesForConjointEnfant()
                .sumInteretHypotecaire();

        Montant annexe = donneesFinancieres.getBiensImmobiliersNonHabitable().getDonneesForConjointEnfant()
                .sumInteretHypotecaire();
        return principale.add(nonPrincipal).add(annexe);
    }

    @Override
    public Montant getDeductionFraisEntretiensImmeubleRequerant() {
        return donneesFinancieres
                .getAllBiensImmobilier()
                .getBiensImmobiliersByProprieteType(ProprieteType.CO_PROPRIETAIRE, ProprieteType.NU_PROPRIETAIRE,
                        ProprieteType.PROPRIETAIRE, ProprieteType.USUFRUITIER).getDonneesForRequerant()
                .sumFraisEntretientBrut(parameters.getVariablesMetier());
    }

    @Override
    public Montant getDeductionFraisEntretiensImmeubleConjEnf() {
        return donneesFinancieres
                .getAllBiensImmobilier()
                .getBiensImmobiliersByProprieteType(ProprieteType.CO_PROPRIETAIRE, ProprieteType.NU_PROPRIETAIRE,
                        ProprieteType.PROPRIETAIRE, ProprieteType.USUFRUITIER).getDonneesForConjointEnfant()
                .sumFraisEntretientBrut(parameters.getVariablesMetier());
    }

    @Override
    public Montant getDeductionPensionAlimentairesVerseeRequerant() {
        return donneesFinancieres.getPensionsAlimentaireByType(PensionAlimentaireType.VERSEE).getDonneesForRequerant()
                .sumDepense();
    }

    @Override
    public Montant getDeductionPensionAlimentairesVerseeConjEnf() {
        return donneesFinancieres.getPensionsAlimentaireByType(PensionAlimentaireType.VERSEE)
                .getDonneesForConjointEnfant().sumDepense();
    }

    @Override
    public Montant getDeductionRegimeRfmRequerant() {
        return donneesFinancieres.getRegimesRfm().getDonneesForRequerant().sumRevenuAnnuel();
    }

    @Override
    public Montant getDeductionRegimeRfmConjEnf() {
        return donneesFinancieres.getRegimesRfm().getDonneesForConjointEnfant().sumRevenuAnnuel();
    }

}