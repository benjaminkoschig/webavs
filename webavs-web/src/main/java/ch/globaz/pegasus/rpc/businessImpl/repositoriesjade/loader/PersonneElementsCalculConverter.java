package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCIJAI;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresContainer;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresListBase;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Filtre;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.avsAi.ApiAvsAi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.assuranceRenteViagere.AssuranceRenteViagere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente.AutreRente;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente.AutreRenteGenre;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente.AutresRentes;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.BienImmobilier;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.contratEntretienViager.ContratEntretienViager;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.iJAi.IjsAi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.pensionAlimentaire.PensionAlimentaireType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.renteAvsAi.RenteAvsAi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.taxeJournalierHome.TaxeJournaliereHome;
import ch.globaz.pegasus.business.domaine.membreFamille.MembreFamille;
import ch.globaz.pegasus.business.domaine.membreFamille.MembreFamilleWithDonneesFinanciere;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.domaine.parametre.Parameters;
import ch.globaz.pegasus.business.domaine.parametre.forfaitPrime.ForfaitsPrimeAssuranceMaladie;
import ch.globaz.pegasus.business.domaine.parametre.home.HomeCategoriArgentPoche;
import ch.globaz.pegasus.business.domaine.parametre.home.HomeCategorie;
import ch.globaz.pegasus.business.domaine.parametre.home.ServiceEtat;
import ch.globaz.pegasus.business.domaine.parametre.home.TypeChambrePrix;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaieEtrangereType;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariableMetier;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariableMetierType;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.ProxyCalculDates;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.rpc.businessImpl.RpcTechnicalException;
import ch.globaz.pegasus.rpc.businessImpl.converter.RpcBusinessException;
import ch.globaz.pegasus.rpc.domaine.PersonElementsCalcul;
import ch.globaz.pegasus.rpc.domaine.RpcAddress;

public class PersonneElementsCalculConverter {
    private static final Logger LOG = LoggerFactory.getLogger(PersonneElementsCalculConverter.class);
    protected static final float tauxChangePrecision = 100000;

    public PersonElementsCalcul convert(MembreFamilleWithDonneesFinanciere donneesFinanciere, Parameters parameters,
            Date dateDebut, boolean isCantonValais, boolean isLvpc, RpcAddress legalAddress, RpcAddress livingAddress,
            RoleMembreFamille roleMembreFamille, boolean isCoupleSepare, TupleDonneeRapport tauxChangeRentesEtrangere) {
        PersonElementsCalcul perElCal = new PersonElementsCalcul();

        perElCal.setMembreFamille(donneesFinanciere.getFamille());
        DonneesFinancieresContainer df = donneesFinanciere.getDonneesFinancieres();

        perElCal.setRenteAvsAi(df.getRentesAvsAi().sumRevenuAnnuelBrut());

        //
        // if (df.getIjAis().hasMoreThanOneElement()) {
        // LOG.warn("Many renteIj: {}", personne);
        // }
        // if (df.getRentesAvsAi().hasMoreThanOneElement()) {
        // LOG.warn("Many renteAvsAi: {}", personne);
        // }
        // if (df.getApisAvsAi().hasMoreThanOneElement()) {
        // LOG.warn("Many renteApi: {}", personne);
        // }
        int nbDayInYear = dateDebut.getNbDaysInYear();

        perElCal.setRenteIj(df.getIjAis().sumRevenuAnnuel(nbDayInYear)
                .add(df.getIndemintesJournaliereApg().sumRevenuAnnuel(nbDayInYear)));

        if (df.getRentesAvsAi().hasMoreThanOneElement()) {
            boolean sansRente = true;
            for (RenteAvsAi rente : df.getRentesAvsAi().getList()) {
                if (sansRente) {
                    sansRente = rente.isSansRente();
                }
            }
            perElCal.setRenteIsSansRente(sansRente);
        } else {
            perElCal.setRenteIsSansRente(false);
        }

        perElCal.setRevenuBruteActiviteLucrative(computActiviteLucrative(
                parameters.getVariablesMetier().getForfaitTenueMenage().resolveMostRecent(), df));

        perElCal.setLpp(df.getAutresRentes().getAutresRentesByGenre(AutreRenteGenre.LPP).sumRevenuAnnuel());

        perElCal.setRenteEtrangere(resolveRenteEtrangere(parameters, dateDebut, tauxChangeRentesEtrangere, df));

        Montant montantArv = df.getAssurancesRentesViageres().filtre(new Filtre<AssuranceRenteViagere>() {
            @Override
            public boolean condition(AssuranceRenteViagere arv) {
                return !arv.hasDessaisissementRevenu();
            }
        }).sumWithStrategie();

        perElCal.setTotalRentes(df.getAutresRentes()
                .filtreByGenre(AutreRenteGenre.LAA, AutreRenteGenre.ASSURANCE_PRIVEE, AutreRenteGenre.TROISIEME_PILIER,
                        AutreRenteGenre.LAM, AutreRenteGenre.AUTRES)
                .sumRevenuAnnuel().add(montantArv).add(perElCal.getLpp()).add(perElCal.getRenteEtrangere()));

        perElCal.setRetraitCapitalLpp(Montant.ZERO); // Nous ne disposons pas de cette information.

        perElCal.setPrimeLamal(resolvePrimeLamal(parameters.getForfaitsPrimesAssuranceMaladie(),
                donneesFinanciere.getFamille(), dateDebut));

        // TODO fauteuil roulant

        DonneesFinancieresContainer dfFiltre = df;
        if (roleMembreFamille != null && isCoupleSepare) {
            dfFiltre = df.filtreForRole(roleMembreFamille);
        }

        perElCal.setRevenuBrutHypothetique(df.getRevenusHypothtique().sumRevenuAnnuel());

        perElCal.setAutresDepenses(dfFiltre.getCotisationsPsal().sumDepense()
                .add(dfFiltre.getPensionsAlimentaireByType(PensionAlimentaireType.VERSEE).sumDepense()));
        TaxeJournaliereHome taxeJournaliereHome = dfFiltre.getTaxesJournaliereHome().resolveCurrentTaxejournaliere();
        Montant entretienViager = Montant.ZERO;

        if (taxeJournaliereHome != null) {

            perElCal.setHomeContributionLca(
                    taxeJournaliereHome.computMontantContributionLcaAnnuel(dateDebut.getNbDaysInYear()));

            TypeChambrePrix typeChambrePrix = resolveTypeChambre(parameters, dateDebut, taxeJournaliereHome);
            Montant homeTaxeHomeTotal = typeChambrePrix.getPrix().annualise(dateDebut);

            perElCal.setHomeTaxeHomeTotal(homeTaxeHomeTotal);
            Montant plafond = isCantonValais
                    ? resolvePlafondHome(parameters.getVariablesMetier(), typeChambrePrix.getServiceEtat())
                            .annualise(dateDebut)
                    : Montant.ZERO_ANNUEL;
            perElCal.setHomeTaxeHomePrisEnCompte(
                    homeTaxeHomeTotal.greater(plafond) && !plafond.isZero() ? plafond : homeTaxeHomeTotal);
            if (isCantonValais) {
                perElCal.setHomeTaxeHomePrisEnCompte(perElCal.getHomeTaxeHomePrisEnCompte()
                        .add(taxeJournaliereHome.getFraisLongueDuree().annualise(dateDebut)));
                perElCal.setHomeTaxeHomeTotal(perElCal.getHomeTaxeHomeTotal()
                        .add(taxeJournaliereHome.getFraisLongueDuree().annualise(dateDebut)));
            }
            perElCal.setHomeDepensesPersonnelles(resolveArgentDepoche(typeChambrePrix.getCategorieArgentPoche(),
                    typeChambrePrix.getCategorie(), parameters.getVariablesMetier(), dateDebut, isLvpc));
            // TODO
            perElCal.setHomeParticipationAuxCoutDesPatients(Montant.ZERO_ANNUEL);
            perElCal.setHomeIsApiFacturee(typeChambrePrix.isApiFacturee());

        } else {               
            perElCal.setHomeIsApiFacturee(false);
            perElCal.setHomeContributionLca(Montant.ZERO_ANNUEL);
            perElCal.setHomeTaxeHomeTotal(Montant.ZERO_ANNUEL);
            perElCal.setHomeTaxeHomePrisEnCompte(Montant.ZERO_ANNUEL);
            perElCal.setHomeDepensesPersonnelles(Montant.ZERO_ANNUEL);
            perElCal.setHomeParticipationAuxCoutDesPatients(Montant.ZERO_ANNUEL);
        }
        
        // calcul pour le contrat entretien viager, repris du Plan de calcul (cf StrategieContratRenteViager)
        boolean isHomeViager = false;
        if (taxeJournaliereHome != null && 
                perElCal.getHomeIsApiFacturee()) {
            DonneesFinancieresListBase<ContratEntretienViager> contratEntretiens = df.getContratsEntretienViager().filtreForMembreFamille(donneesFinanciere.getFamille());
                    
            if(!contratEntretiens.isEmpty()) {
                //ContratEntretienViager contrat = (ContratEntretienViager) contratEntretien.get(0);
                String dateDebutViager = contratEntretiens.get(0).getDebut().getSwissMonthValue();
                String dateDebutHome = taxeJournaliereHome.getDebut().getSwissMonthValue();
                isHomeViager = dateDebutViager.equals(dateDebutHome);
            }
        }
        
        if(!isHomeViager) {
            if (isCoupleSepare) {
                entretienViager = df.getContratsEntretienViager().filtreForMembreFamille(donneesFinanciere.getFamille())
                        .sumRevenuAnnuel();
            } else {
                entretienViager = df.getContratsEntretienViager().sumRevenuAnnuel();
            }
        }

        // TODO Dessaisissement automatique et Dessaisissement fortune ????

        perElCal.setAutresRevenus(df.getAutresRevenus().sumRevenuAnnuelBrut()
                .add(df.getPensionsAlimentaireByType(PensionAlimentaireType.DUE).sumRevenuAnnuel())
                .add(df.getDessaisissementsRevenu().sumRevenuAnnuel())
                .add(df.getBiensImmobiliersServantHbitationPrincipale().sumSousLocationPartPorietaire())
                // .add(df.getComptesBancairePostal().sumRevenuAnnuel())
                .add(entretienViager).add(df.getAllocationsFamilliale().sumRevenuAnnuelBrut()));

        perElCal.setLegalAddress(legalAddress);
        perElCal.setLivingAddress(livingAddress);
        perElCal.setTypeRenteCS(resolveMaxType(dfFiltre, df.getIjAis()));

        Montant propIncome = Montant.ZERO;
        Montant usuIncome = Montant.ZERO;
        
        propIncome = df.getBiensImmobiliersServantHbitationPrincipale()
                .filtreByProprieteType(ProprieteType.PROPRIETAIRE, ProprieteType.CO_PROPRIETAIRE)
                .sumMontantValeurLocativePartProprieteEtCoPropiete();
        
        propIncome = propIncome.add(df.getBiensImmobiliersNonPrincipale()
                .filtreByProprieteType(ProprieteType.PROPRIETAIRE, ProprieteType.CO_PROPRIETAIRE)
                .sumMontantValeurLocativePartProprieteEtCoPropiete());
        
        if (taxeJournaliereHome != null) {
            usuIncome = df.getBiensImmobiliersServantHbitationPrincipale()
                    .filtreByProprieteType(ProprieteType.USUFRUITIER)
                    .sumMontantValeurLocativeDH_RPC();
            
            usuIncome = usuIncome.add(df.getBiensImmobiliersNonPrincipale()
                    .filtreByProprieteType(ProprieteType.USUFRUITIER)
                    .sumMontantValeurLocativeDH_RPC());
        } else {
            usuIncome = df.getBiensImmobiliersServantHbitationPrincipale()
                    .filtreByProprieteType(ProprieteType.USUFRUITIER, ProprieteType.DROIT_HABITATION)
                    .sumMontantValeurLocativeDH_RPC();
            
            usuIncome = usuIncome.add(df.getBiensImmobiliersNonPrincipale()
                    .filtreByProprieteType(ProprieteType.USUFRUITIER, ProprieteType.DROIT_HABITATION)
                    .sumMontantValeurLocativeDH_RPC());
        }
    
        perElCal.setUsufructIncome(usuIncome);
        
        perElCal.setValeurLocativeProprietaire(propIncome);

        // Si les APi sont prisent en compte dans le calcul
        if (perElCal.getHomeTaxeHomeTotal().isPositive()) {// !perElCal.getHomeIsApiFacturee() &&
            perElCal.setRenteApi(df.getApisAvsAi().sumRevenuAnnuel());
        } else {
            perElCal.setRenteApi(Montant.ZERO_ANNUEL);
        }

        return perElCal;
    }

    private boolean canAddImmobilierNonHabitable(DonneesFinancieresContainer df) {
        boolean canAdd = true;
        for (BienImmobilier bienImmo : df.getAllBiensImmobilier().getList()) {
            if (bienImmo.getProprieteType() == ProprieteType.PROPRIETAIRE) {
                canAdd = false;
                break;
            } else if (!(bienImmo.getDette().isZero() && bienImmo.getInteretHypothecaire().isZero())) {
                canAdd = false;
                break;
            } else if(DonneeFinanciereType.BIEN_IMMOBILIER_NON_HABITABLE.equals(bienImmo.getTypeDonneeFinanciere())
                    && ProprieteType.USUFRUITIER.equals(bienImmo.getProprieteType())) {
                canAdd = false;
                break;
            }
        }
        return canAdd;
    }

    private Montant resolveRenteEtrangere(Parameters parameters, Date dateDebut,
            TupleDonneeRapport tauxChangeRentesEtrangere, DonneesFinancieresContainer df) {
        AutresRentes rentesEtrangere = df.getAutresRentes().getAutresRentesByGenre(AutreRenteGenre.RENTE_ETRANGERE);
        Montant montantRentesEtranger = Montant.ZERO;
        boolean hasAllDevises = true;
        for (AutreRente renteEtrangere : rentesEtrangere.getList()) {
            MonnaieEtrangereType deviseCode = renteEtrangere.getMonnaieType();
            Float deviseValue = tauxChangeRentesEtrangere.getValeurEnfant(deviseCode.getValue());
            if (!deviseValue.equals(new Float(0))) {
                deviseValue = deviseValue / tauxChangePrecision;
                montantRentesEtranger = montantRentesEtranger.add(renteEtrangere.getMontant().multiply(deviseValue));
            } else {
                hasAllDevises = false;
                break;
            }
        }

        if (!hasAllDevises) {
            montantRentesEtranger = df.getAutresRentes().getAutresRentesByGenre(AutreRenteGenre.RENTE_ETRANGERE)
                    .sumAndComputeDevise(parameters.getMonnaiesEtrangere(), dateDebut);
        }
        return montantRentesEtranger;
    }

    private String resolveMaxType(DonneesFinancieresContainer df, IjsAi ijsAi) {
        List<RenteAvsAi> typesRente = df.getRentesAvsAi().getList();
        List<ApiAvsAi> typesApi = df.getApisAvsAi().getList();
        Montant maxMontantAPI = Montant.ZERO;
        Montant maxMontantRente = Montant.ZERO;
        String typeRenteCS = "";
        String typeRenteCSIJ = "";
        String typeRenteCSAPI = "";
        String typeRenteCSRente = "";
        
        if(ijsAi != null && !ijsAi.isEmpty()) {
            typeRenteCSIJ = IPCIJAI.CS_TYPE_DONNEE_FINANCIERE;
        }
        
        for (ApiAvsAi api : typesApi) {
            if (api.getMontant().greater(maxMontantAPI)) {
                maxMontantAPI = api.getMontant();
                typeRenteCSAPI = api.getTypeApi().getValue();
            }
        }

        for (RenteAvsAi rente : typesRente) {
            if (rente.getMontant().greater(maxMontantRente)) {
                maxMontantRente = rente.getMontant();
                typeRenteCSRente = rente.getTypeRente().getValue();
            }
        }
        
        // S180817_002 : si Rente + API annoncer code Rente, Si API + IJ annoncer API
        if(!typeRenteCSRente.isEmpty()) {
            typeRenteCS = typeRenteCSRente;
        } else if (!typeRenteCSAPI.isEmpty()) {
            typeRenteCS = typeRenteCSAPI;
        } else if (!typeRenteCSIJ.isEmpty()){
            typeRenteCS = typeRenteCSIJ;
        }

        return typeRenteCS;
    }

    Montant resolveArgentDepoche(HomeCategoriArgentPoche homeCategoreiArgentPoche, HomeCategorie homeCategorie,
            VariablesMetier variablesMetier, Date dateDebut, boolean isLvpc) {
        Date dateSwitch = new Date(ProxyCalculDates.DEPENSE_TOTAL_RECONNUES_SWITCH_STRATEGY_DATE.timestamp);
        VariableMetierType variableTypeArgentPoche;
        if (dateDebut.afterOrEquals(dateSwitch) && isLvpc) {
            Map<HomeCategoriArgentPoche, VariableMetierType> mapArgentPoche = new EnumMap<HomeCategoriArgentPoche, VariableMetierType>(
                    HomeCategoriArgentPoche.class);
            mapArgentPoche.put(HomeCategoriArgentPoche.EMS_NON_MEDICALISE_AGE,
                    VariableMetierType.MONTANT_EMS_NON_MEDICALISE_AGE_AVANCEES);
            mapArgentPoche.put(HomeCategoriArgentPoche.EMS_NON_MEDICALISE_PSY,
                    VariableMetierType.MONTANT_EMS_NON_MEDICALISE_PSYHIATRIQUE);
            mapArgentPoche.put(HomeCategoriArgentPoche.EPS, VariableMetierType.MONTANT_EPS_ETABLISSEMENT_MEDSOC);
            mapArgentPoche.put(HomeCategoriArgentPoche.HANDICAP_PHYSIQUE,
                    VariableMetierType.MONTANT_ESE_HANDICAP_PHYSIQUE);
            variableTypeArgentPoche = mapArgentPoche.get(homeCategoreiArgentPoche);
        } else {
            Map<HomeCategorie, VariableMetierType> mapArgentPoche = new EnumMap<HomeCategorie, VariableMetierType>(
                    HomeCategorie.class);
            mapArgentPoche.put(HomeCategorie.NON_MEDICALISE, VariableMetierType.ARGENT_POCHE_MEDICALISE);
            mapArgentPoche.put(HomeCategorie.MEDICALISE, VariableMetierType.ARGENT_POCHE_NON_MEDICALISE);
            variableTypeArgentPoche = mapArgentPoche.get(homeCategorie);
        }

        VariableMetier argentPoche;
        if (variableTypeArgentPoche != null) {
            argentPoche = variablesMetier.getParameters(variableTypeArgentPoche).resolveCourant(dateDebut);
            if (argentPoche == null) {
                throw new RpcBusinessException("pegasus.rpc.argentPoche.notFount");
            }
            return argentPoche.getMontant().addMensuelPeriodicity().annualise();
        }
        throw new RpcBusinessException("pegasus.rpc.argentPoche.notFount");
    }

    Montant resolvePlafondHome(VariablesMetier variablesMetier, ServiceEtat serviceEtat) {
        VariablesMetier plafonds;

        if (serviceEtat.isEms()) {
            plafonds = variablesMetier.getPlafondAnnuelEms();
        } else if (serviceEtat.isInstitution()) {
            plafonds = variablesMetier.getPlafondAnnuelInstitution();
        } else if (serviceEtat.isListAttente()) {
            plafonds = variablesMetier.getPlafondAnnuelListAttente();
        } else {
            throw new RpcTechnicalException(
                    "The plafond cant be found with the cs periode serivce etat[" + serviceEtat.getValue() + "]");
        }
        return plafonds.resolveMostRecent().getMontant();
    }

    Montant resolvePrimeLamal(ForfaitsPrimeAssuranceMaladie forfaitsPrimeAssuranceMaladie, MembreFamille membreFamille,
            Date dateDebut) {
        Date dateNaissance = new Date(membreFamille.getPersonne().getDateNaissance());

        ForfaitsPrimeAssuranceMaladie forfaits = forfaitsPrimeAssuranceMaladie
                .filtreByIdLocalite(membreFamille.getDonneesPersonnelles().getIdDernierDomicileLegale());
        String idLocalite = membreFamille.getDonneesPersonnelles().getIdDernierDomicileLegale();

        if ("0".equals(idLocalite) || idLocalite == null || idLocalite.isEmpty()) {
            throw new RpcBusinessException("pegasus.rpc.dernierDomicileLegale.mandatory");
        }

        if (forfaits.isEmpty()) {
            throw new RpcBusinessException("pegasus.rpc.primeLamal.notFound", idLocalite);
        }
        return forfaitsPrimeAssuranceMaladie.filtreByIdLocalite(idLocalite).filtreByAge(dateDebut, dateNaissance)
                .resolveCourant(dateDebut).getMontant();
    }

    private TypeChambrePrix resolveTypeChambre(Parameters parameters, Date dateDebut,
            TaxeJournaliereHome taxeJournaliereHome) {
        String idTypeChambre = taxeJournaliereHome.getIdTypeChambre();
        if (idTypeChambre != null) {
            if (parameters.getTypesChambrePrix().isEmpty()) {
                throw new RpcBusinessException("pegasus.rpc.typeChambre.introuvable", idTypeChambre);
            }
            if (!parameters.getTypesChambrePrix().hasParameter(idTypeChambre)) {
                throw new RpcBusinessException("pegasus.rpc.typeChambre.prix.introuvable", idTypeChambre);
            }
            return parameters.getTypesChambrePrix().getParameters(idTypeChambre).filtreByPeriode(dateDebut)
                    .resolveCourant(dateDebut);
        }
        return null;
    }

    private Montant computActiviteLucrative(VariableMetier variableMetier, DonneesFinancieresContainer df) {
        Montant sum = df.getRevenusActiviteLucrativeDependante().sumRevenuAnnuelBrut()
                .add(df.getRevenusActiviteLucrativeIndependante().sumRevenuAnnuelBrut());

        // TODO
        if (df.getLoyers().size() == 1 && df.getLoyers().get(0).isTenueMenage()) {
            sum = sum.add(variableMetier.getMontant());
        }
        return sum;
    }

}
