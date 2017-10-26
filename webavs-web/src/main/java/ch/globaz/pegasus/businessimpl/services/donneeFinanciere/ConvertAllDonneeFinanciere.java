package ch.globaz.pegasus.businessimpl.services.donneeFinanciere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.common.domaine.Taux;
import ch.globaz.hera.business.exceptions.models.MembreFamilleException;
import ch.globaz.hera.business.models.famille.MembreFamille;
import ch.globaz.hera.business.services.HeraServiceLocator;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
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
import ch.globaz.pegasus.businessimpl.services.adresse.TechnicalExceptionWithTiers;

class ConvertAllDonneeFinanciere {

    Integer toInteger(String value) {
        Integer i = 0;
        if (value != null && value.trim().length() > 0) {
            i = Integer.valueOf(value);
        }
        return i;
    }

    Montant toMontant(String value) {
        if (value == null) {
            return Montant.ZERO;
        } else {
            if (value.trim().length() == 0) {
                return Montant.ZERO;
            } else {
                return new Montant(value);
            }
        }
    }

    public DonneesFinancieresContainer convertToDomain(List<DonneeFinanciereComplexModel> donnesFinanciere) {
        DonneesFinancieresContainer list = new DonneesFinancieresContainer();
        DonneeFinanciereConverter donneeFinanciereConverter = new DonneeFinanciereConverter();
        for (DonneeFinanciereComplexModel dr : donnesFinanciere) {
            try {
                convert(list, donneeFinanciereConverter, dr);
            } catch (Exception e1) {
                MembreFamille membre = new MembreFamille();
                try {
                    membre = HeraServiceLocator.getMembreFamilleService().read(dr.getIdMembreFamilleSF());
                } catch (MembreFamilleException e) {
                    throw new RuntimeException(e.toString(), e);
                } catch (JadeApplicationServiceNotAvailableException e) {
                    throw new RuntimeException(e.toString(), e);
                } catch (JadePersistenceException e) {
                    throw new RuntimeException(e.toString(), e);
                }
                throw new TechnicalExceptionWithTiers("Impossible de convertire la donnée financier suivante: " + dr,
                        membre.getPersonneEtendue(), e1);
            }
        }
        return list;
    }

    private void convert(DonneesFinancieresContainer list, DonneeFinanciereConverter donneeFinanciereConverter,
            DonneeFinanciereComplexModel dr) {
        DonneeFinanciere df = donneeFinanciereConverter.convertToDomain(dr);
        DonneeFinanciereType dft = DonneeFinanciereType.fromValue(dr.getCsTypeDonneeFinanciere());
        if (dft.isRenteAvsAi()) {
            Montant montant = toMontant(dr.getRenteAVSAIMontant());
            RenteAvsAiType typeDonneeFinanciere = RenteAvsAiType.fromValue(dr.getRenteAVSAICsType());
            TypeSansRente typeSansRente = TypeSansRente.fromValue(dr.getRenteAVSAICsTypeSansRente());
            RenteAvsAi renteAvsAi = new RenteAvsAi(montant, typeDonneeFinanciere, typeSansRente, df);
            list.add(renteAvsAi);
        } else if (dft.isIjAi()) {
            Montant montant = toMontant(dr.getIJAIMontant());
            Integer nbJour = toInteger(dr.getIJAIJours());
            IjAi ijAi = new IjAi(montant, nbJour, df);
            list.add(ijAi);
        } else if (dft.isApiAvsAi()) {
            Montant montant = toMontant(dr.getAPIAVSAIMontant());
            ApiType typeApi = ApiType.fromValue(dr.getAPIAVSCsType());
            ApiDegre degre = ApiDegre.fromValue(dr.getAPIAVSCsDegre());
            ApiAvsAi apiAvsAi = new ApiAvsAi(montant, typeApi, degre, df);

            list.add(apiAvsAi);
        } else if (dft.isAutreRente()) {
            // dr.getAutresRentesAutreGenre();
            AutreRenteType autreRenteType = AutreRenteType.fromValue(dr.getAutresRentesCsType());
            AutreRenteGenre autreRenteGenre = AutreRenteGenre.fromValue(dr.getAutresRentesCsGenre());
            Montant montant = toMontant(dr.getAutresRentesMontant());
            MonnaieEtrangereType monnaieEtrangereType = MonnaieEtrangereType.INDEFINIT;
            if (autreRenteGenre.isRenteEtrangere()) {
                monnaieEtrangereType = MonnaieEtrangereType.fromValue(dr.getAutreRentesEtrangeresCSTypeDevise());
            }
            AutreRente autreRente = new AutreRente(montant, autreRenteType, autreRenteGenre,
                    dr.getAutresRentesAutreGenre(), monnaieEtrangereType, df);
            list.add(autreRente);
        } else if (dft.isIndeminteJournaliereApg()) {
            Montant montant = toMontant(dr.getIJAPGMontant());
            Montant montantChomage = toMontant(dr.getIJAPGMontantChomage());
            Montant cotisationLpp = toMontant(dr.getIJAPGcotisationLPPMens());
            Montant gainIntermediaireAnnuel = toMontant(dr.getIJAPGgainIntAnnuel());
            IndemniteJournaliereApgGenre genre = IndemniteJournaliereApgGenre.fromValue(dr.getIJAPGGenre());
            Integer nbJour = toInteger(dr.getIJAPGnbJours());
            Taux tauxAa = new Taux(dr.getIJAPGtauxAA());
            Taux tauxAvs = new Taux(dr.getIJAPGtauxAVS());
            IndemniteJournaliereApg indeminteJournaliereApg = new IndemniteJournaliereApg(montant, montantChomage,
                    cotisationLpp, gainIntermediaireAnnuel, genre, nbJour, tauxAa, tauxAvs, dr.getIJAPGAutreGenre(), df);
            list.add(indeminteJournaliereApg);
        } else if (dft.isAutreApi()) {
            Montant montant = toMontant(dr.getAutresAPIMontant());
            AutreApiType type = AutreApiType.fromValue(dr.getAutresAPICsTypeMontant());
            ApiDegre degre = ApiDegre.fromValue(dr.getAutresApiCsDegre());
            AutreApi autreApi = new AutreApi(montant, type, degre, dr.getAutresApiAutre(), df);
            list.add(autreApi);
        } else if (dft.isLoyer()) {
            Montant montant = toMontant(dr.getLoyerMontant());
            Montant charge = toMontant(dr.getLoyerMontantCharges());
            Montant sousLocation = toMontant(dr.getLoyerMontantSousLocations());
            Montant taxeJournalierePensionNonReconnue = toMontant(dr.getLoyerTaxeJournalierePensionNonReconnue());
            LoyerType type = LoyerType.fromValue(dr.getLoyerCsTypeLoyer());
            Integer nbPersonnne = toInteger(dr.getLoyerNbPersonnes());
            Loyer loyer = new Loyer(montant, charge, sousLocation, taxeJournalierePensionNonReconnue, type,
                    nbPersonnne, dr.getLoyerIsFauteuilRoulant(), dr.getLoyerIsTenueMenage(), df);
            list.add(loyer);
        } else if (dft.isTaxeJournalierHome()) {
            Date dateEntreeHome = DonneeFinanciereConverter.toDate(dr.getTaxeJournaliereDateEntreeHome());
            Montant montantJournalierLca = toMontant(dr.getTaxeJournaliereMontantJournalierLCA());
            Montant primeAPayer = toMontant(dr.getTaxeJournalierePrimeAPayer());
            TaxeJournaliereHome taxeJournalierHome = new TaxeJournaliereHome(montantJournalierLca, primeAPayer,
                    dr.getTaxeJournaliereIsParticipationLCA(), dateEntreeHome, dr.getTaxeJournaliereIdTypeChambre(), df);
            list.add(taxeJournalierHome);
        } else if (dft.isCompteBancairePostal()) {

            Montant montant = toMontant(dr.getCompteBancaireCPPMontant());
            Montant frais = toMontant(dr.getCompteBancaireCPPMontantFrais());
            Montant interet = toMontant(dr.getCompteBancaireCPPMontantInterets());
            Part part = new Part(dr.getCompteBancaireCPPFractionNumerateur(),
                    dr.getCompteBancaireCPPFractionDenominateur());
            ProprieteType typePropriete = ProprieteType.fromValue(dr.getCompteBancaireCPPCsTypePropriete());

            CompteBancairePostal compteBancairePostal = new CompteBancairePostal(montant, frais, interet, part,
                    typePropriete, dr.getCompteBancaireIsSansInteret(), df);

            list.add(compteBancairePostal);
        } else if (dft.isTitre()) {
            Montant montant = toMontant(dr.getTitreMontant());
            Montant droitGarde = toMontant(dr.getTitreDroitGarde());
            Montant rendement = toMontant(dr.getTitreRendement());
            ProprieteType typePropriete = ProprieteType.fromValue(dr.getTitreCsTypePropriete());
            Part part = new Part(dr.getTitreFractionNumerateur(), dr.getTitreFractionDenominateur());

            Titre titre = new Titre(montant, droitGarde, rendement, typePropriete, part, dr.getTitreIsSansRendement(),
                    df);

            list.add(titre);

        } else if (dft.isAssuranceVie()) {
            Montant montant = toMontant(dr.getAssuranceVieMontantValeurRachat());

            AssuranceVie assuranceVie = new AssuranceVie(montant, df);
            list.add(assuranceVie);

        } else if (dft.isCapitalLpp()) {
            Montant montant = toMontant(dr.getCapitalLPPMontant());
            Montant frais = toMontant(dr.getCapitalLPPMontantFrais());
            Montant interet = toMontant(dr.getCapitalLPPMontantInterets());
            Part part = new Part(dr.getCapitalLPPFractionNumerateur(), dr.getCapitalLPPFractionDenominateur());
            ProprieteType proprieteType = ProprieteType.fromValue(dr.getCapitalLPPCsTypePropriete());

            CapitalLpp capitalLpp = new CapitalLpp(montant, frais, interet, part, proprieteType,
                    dr.getCapitalLPPIsSansInteret(), df);

            list.add(capitalLpp);
        } else if (dft.isAutreDetteProuvee()) {

            Montant montant = toMontant(dr.getAutresDettesProuveesMontant());
            AutreDetteProuvee autreDetteProuvee = new AutreDetteProuvee(montant, df);
            list.add(autreDetteProuvee);

        } else if (dft.isPretEnversTiers()) {
            Montant montant = toMontant(dr.getPretEnversTiersMontant());
            Montant interet = toMontant(dr.getPretEnversTiersMontantInterets());
            Part part = new Part(dr.getPretEnversTiersPartProprieteNumerateur(),
                    dr.getPretEnversTiersPartProprieteDenominateur());
            ProprieteType proprieteType = ProprieteType.fromValue(dr.getPretEnversTiersCsTypePropriete());

            PretEnversTiers pretEnversTiers = new PretEnversTiers(montant, interet, part, proprieteType,
                    dr.getPretEnversTiersIsSansInteret(), df);

            list.add(pretEnversTiers);
        } else if (dft.isAssuranceRenteViagere()) {
            Montant montant = toMontant(dr.getAssuranceRenteViagereMontant());
            Montant excedant = toMontant(dr.getAssuranceRenteViagereExcedant());
            Montant valeurDeRachat = toMontant(dr.getAssuranceRenteViagereMontantValeurRachat());

            AssuranceRenteViagere assuranceRenteViagere = new AssuranceRenteViagere(montant, excedant, valeurDeRachat,
                    dr.getIsDessaisissementFortune(), dr.getIsDessaisissementRevenu(), df);
            list.add(assuranceRenteViagere);

        } else if (dft.isNumeraire()) {
            Montant montant = toMontant(dr.getNumeraireMontant());
            Montant interet = toMontant(dr.getNumeraireMontantInterets());
            Part part = new Part(dr.getNumeraireFractionNumerateur(), dr.getNumeraireFractionDenominateur());
            ProprieteType proprieteType = ProprieteType.fromValue(dr.getNumeraireCsTypePropriete());

            Numeraire numeraire = new Numeraire(montant, interet, part, proprieteType, dr.getNumeraireIsSansInteret(),
                    df);

            list.add(numeraire);
        } else if (dft.isMarchandiseStock()) {

            Montant montant = toMontant(dr.getMarchandiseStockMontant());
            ProprieteType proprieteType = ProprieteType.fromValue(dr.getMarchandiseStockCsTypePropriete());
            Part part = new Part(dr.getMarchandiseStockFractionNumerateur(),
                    dr.getMarchandiseStockFractionDenominateur());

            MarchandiseStock marchandiseStock = new MarchandiseStock(montant, proprieteType, part, df);
            list.add(marchandiseStock);
        } else if (dft.isVehicule()) {
            Montant montant = toMontant(dr.getVehiculeMontant());
            ProprieteType proprieteType = ProprieteType.fromValue(dr.getVehiculeCsTypePropriete());
            Part part = new Part(dr.getVehiculeFractionNumerateur(), dr.getVehiculeFractionDenominateur());

            Vehicule vehicule = new Vehicule(montant, proprieteType, part, df);
            list.add(vehicule);

        } else if (dft.isBetail()) {
            Montant montant = toMontant(dr.getBetailMontant());
            ProprieteType proprieteType = ProprieteType.fromValue(dr.getBetailCsTypePropriete());
            Part part = new Part(dr.getBetailFractionNumerateur(), dr.getBetailFractionDenominateur());

            Betail betail = new Betail(montant, proprieteType, part, df);
            list.add(betail);

        } else if (dft.isAutreFortuneMobiliere()) {
            Montant montant = toMontant(dr.getAutreFortuneMobiliereMontant());
            ProprieteType proprieteType = ProprieteType.fromValue(dr.getAutreFortuneMobiliereCsTypePropriete());
            Part part = new Part(dr.getAutreFortuneMobiliereFractionNumerateur(),
                    dr.getAutreFortuneMobiliereFractionDenominateur());
            AutreFortuneMobiliereTypeDeFortune typeDeFortune = AutreFortuneMobiliereTypeDeFortune.fromValue(dr
                    .getAutreFortuneMobiliereCsTypeFortune());

            AutreFortuneMobiliere autreFortuneMobiliere = new AutreFortuneMobiliere(montant, proprieteType, part,
                    typeDeFortune, df);
            list.add(autreFortuneMobiliere);

        } else if (dft.isRevenuActiviteLucrativeIndependante()) {
            Montant montant = toMontant(dr.getRevenuActiviteLucrativeIndependanteMontant());
            RevenuActiviteLucrativeIndependanteGenreRevenu revenuActiviteLucrativeIndependanteGenreRevenu = RevenuActiviteLucrativeIndependanteGenreRevenu
                    .fromValue(dr.getRevenuActiviteLucrativeIndependanteCSGenreRevenu());

            RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante = new RevenuActiviteLucrativeIndependante(
                    montant, revenuActiviteLucrativeIndependanteGenreRevenu, df);
            list.add(revenuActiviteLucrativeIndependante);

        } else if (dft.isRevenueHypothtique()) {
            Montant revenuNet = toMontant(dr.getRevenuHypothetiqueMontantRevenuNet());
            Montant revenuBrut = toMontant(dr.getRevenuHypothetiqueMontantRevenuBrut());
            Montant fraisFarde = toMontant(dr.getRevenuHypothetiqueMontantFraisGarde());
            Montant deductionSocial = toMontant(dr.getRevenuHypothetiqueMontantDeductionsSociales());
            Montant deductionLpp = toMontant(dr.getRevenuHypothetiqueMontantDeductionsLPP());
            RevenuHypothtiqueMotif motif = RevenuHypothtiqueMotif.fromValue(dr.getRevenuHypothetiqueMotif());

            RevenuHypothtique revenueHypothtique = new RevenuHypothtique(revenuNet, revenuBrut, fraisFarde,
                    deductionSocial, deductionLpp, motif, df);
            list.add(revenueHypothtique);

        } else if (dft.isAllocationFamilliale()) {
            Montant montant = toMontant(dr.getAllocationFamilialeMontantMensuel());

            AllocationFamilliale allocationFamilliale = new AllocationFamilliale(montant, df);
            list.add(allocationFamilliale);

        } else if (dft.isContratEntretienViager()) {
            Montant montant = toMontant(dr.getContratEntretienViagerMontant());

            ContratEntretienViager contratEntretienViager = new ContratEntretienViager(montant, df);
            list.add(contratEntretienViager);

        } else if (dft.isAutreRevenue()) {
            Montant montant = toMontant(dr.getAutresRevenusMontant());

            AutreRevenu autreRevenue = new AutreRevenu(montant, dr.getAutresRevenusLibelle(), df);
            list.add(autreRevenue);

        } else if (dft.isCotisationPsal()) {
            Montant montant = toMontant(dr.getCotisationPSALMontantAnnuel());

            CotisationPsal cotisationPsal = new CotisationPsal(montant, df);
            list.add(cotisationPsal);
        } else if (dft.isPensionAlimentaire()) {
            Montant montant = toMontant(dr.getPensionAlimentaireMontant());
            Montant montantRenteEnfant = toMontant(dr.getPensionAlimentaireMontantRenteEnfant());

            PensionAlimentaireType type = PensionAlimentaireType.fromValue(dr.getPensionAlimentaireCsTypePension());
            PensionAlimentaireLienParente lienParente = PensionAlimentaireLienParente.fromValue(dr
                    .getPensionAlimentaireLienParente());

            PensionAlimentaire pensionAlimentaire = new PensionAlimentaire(montant, montantRenteEnfant, type,
                    lienParente, dr.getPensionAlimentaireIsDeductionsRenteEnfant(), df);
            list.add(pensionAlimentaire);

        } else if (dft.isDessaississementFortune()) {

            Montant montant = toMontant(dr.getDessaisissementFortuneMontant());
            Montant deduction = toMontant(dr.getDessaisissementFortuneDeductions());

            DessaisissementFortune dessaississementFortune = new DessaisissementFortune(montant, deduction, df);
            list.add(dessaississementFortune);

        } else if (dft.isDessaisissementRevenu()) {
            Montant montant = toMontant(dr.getDessaisissementRevenuMontant());
            Montant deduction = toMontant(dr.getDessaisissementRevenuDeductions());

            DessaisissementRevenu dessaisissementRevenu = new DessaisissementRevenu(montant, deduction, df);
            list.add(dessaisissementRevenu);
        } else if (dft.isRevenueActiviteLucrativeDependante()) {

            Montant montant = toMontant(dr.getRevenuActiviteLucrativeDependanteMontant());
            Montant deductionLpp = toMontant(dr.getRevenuActiviteLucrativeDependanteDeductionsLPP());
            Montant deductionSociale = toMontant(dr.getRevenuActiviteLucrativeDependanteDeductionsSociales());
            Montant frais = toMontant(dr.getRevenuActiviteLucrativeDependanteMontantFraisEffectifs());

            RevenuActiviteLucrativeDependante revenueActiviteLucrativeDependante = new RevenuActiviteLucrativeDependante(
                    montant, deductionLpp, deductionSociale, frais, df);

            list.add(revenueActiviteLucrativeDependante);
        } else if (dft.isBienImmobilierServantHbitationPrincipale()) {
            Montant valeurFiscale = toMontant(dr.getBienImmoPrincipalMontantValeurFiscale());
            Montant valeurLocative = toMontant(dr.getBienImmoPrincipalMontantValeurLocative());
            Montant interet = toMontant(dr.getBienImmoPrincipalMontantInteretHypothecaire());
            Montant loyerEncaisse = toMontant(dr.getBienImmoPrincipalMontantLoyersEncaisses());
            Montant sousLocation = toMontant(dr.getBienImmoPrincipalMontantSousLocation());
            Montant dette = toMontant(dr.getBienImmoPrincipalMontantDetteHypothecaire());
            Integer nbPersonne = toInteger(dr.getBienImmoPrincipalNombrePersonnes());
            BienImmobilierHabitableType typeDeBien = BienImmobilierHabitableType.fromValue(dr
                    .getBienImmoPrincipalCsTypeDeBien());
            Part part = new Part(dr.getBienImmoPrincipalPartNumerateur(), dr.getBienImmoPrincipalPartDenominateur());
            ProprieteType proprieteType = ProprieteType.fromValue(dr.getBienImmoPrincipalCSPropriete());

            BienImmobilierServantHabitationPrincipale bienImmobilierServantHbitationPrincipale = new BienImmobilierServantHabitationPrincipale(
                    valeurFiscale, valeurLocative, interet, loyerEncaisse, sousLocation, dette, nbPersonne, typeDeBien,
                    part, proprieteType, df);
            list.add(bienImmobilierServantHbitationPrincipale);

        } else if (dft.isBienImmobilierNonHabitable()) {
            Montant rendement = toMontant(dr.getBienImmoNonHabitableMontantRendement());
            Montant valeurVeanle = toMontant(dr.getBienImmoNonHabitableMontantValeurVenale());
            Montant interetHypothecaire = toMontant(dr.getBienImmoNonHabitableMontantInteretHypothecaire());
            Montant dette = toMontant(dr.getBienImmoNonHabitableMontantDetteHypothecaire());
            Part part = new Part(dr.getBienImmoNonHabitablePartNumerateur(),
                    dr.getBienImmoNonHabitablePartDenominateur());

            BienImmobilierNonHabitableType typeDeBien = BienImmobilierNonHabitableType.fromValue(dr
                    .getBienImmoNonHabitableCsTypeDeBien());

            ProprieteType proprieteType = ProprieteType.fromValue(dr.getBienImmoNonHabitableCsTypePropriete());

            BienImmobilierNonHabitable bienImmobilierNonHabitable = new BienImmobilierNonHabitable(rendement,
                    valeurVeanle, interetHypothecaire, dette, typeDeBien, part, proprieteType, df);
            list.add(bienImmobilierNonHabitable);

        } else if (dft.isBienImmobilierNonPrincipale()) {

            Montant valeurVenale = toMontant(dr.getBienImmoAnnexeMontantValeurVenale());
            Montant interetHypothecaire = toMontant(dr.getBienImmoAnnexeMontantInteretHypothecaire());
            Montant loyerEncaisse = toMontant(dr.getBienImmoAnnexeMontantLoyersEncaisses());
            Montant sousLocation = toMontant(dr.getBienImmoAnnexeMontantSousLocation());
            Montant valeurLocative = toMontant(dr.getBienImmoAnnexeMontantValeurLocative());
            Montant dette = toMontant(dr.getBienImmoAnnexeMontantDetteHypothecaire());
            BienImmobilierHabitableType typeDeBien = BienImmobilierHabitableType.fromValue(dr
                    .getBienImmoAnnexeCsTypeDeBien());

            Part part = new Part(dr.getBienImmoAnnexePartNumerateur(), dr.getBienImmoAnnexePartDenominateur());
            ProprieteType proprieteType = ProprieteType.fromValue(dr.getBienImmoAnnexeCsTypePropriete());

            BienImmobilierNonPrincipale bienImmobilierNonPrincipale = new BienImmobilierNonPrincipale(valeurVenale,
                    interetHypothecaire, loyerEncaisse, sousLocation, valeurLocative, dette, typeDeBien, part,
                    proprieteType, df);

            list.add(bienImmobilierNonPrincipale);

        } else {
            throw new IllegalArgumentException("Imposible de détérminer le type de donnée financière: " + df);
        }
    }
}
