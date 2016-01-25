/**
 * 
 */
package globaz.perseus.vb.donneesfinancieres;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.perseus.utils.PFUserHelper;
import java.util.ArrayList;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnue;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnueSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnueType;
import ch.globaz.perseus.business.models.donneesfinancieres.Dette;
import ch.globaz.perseus.business.models.donneesfinancieres.DetteSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.DetteType;
import ch.globaz.perseus.business.models.donneesfinancieres.DonneeFinanciere;
import ch.globaz.perseus.business.models.donneesfinancieres.Fortune;
import ch.globaz.perseus.business.models.donneesfinancieres.FortuneSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.FortuneType;
import ch.globaz.perseus.business.models.donneesfinancieres.Revenu;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuType;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFDonneefinanciereViewBean extends PFAbstractDonneesFinancieresViewBean {

    private String adresseAssure = null;
    private Revenu aideFormation = null;
    private Revenu aidesLogement = null;
    private Revenu allocationCantonaleMaternite = null;
    private Revenu allocationsAMINH = null;
    private Revenu allocationsFamiliales = null;
    private Fortune autreBien = null;
    private Revenu autresCreances = null;
    private Dette autresDettes = null;
    private Fortune autresImmeubles = null;
    private Revenu autresRentes = null;
    private String avec13emeSalaire = null;
    private Fortune biensEtrangers = null;
    private Revenu brapa = null;
    private Fortune cession = null;
    private DepenseReconnue chargesAnnuelles = null;
    private Revenu contratEntretiensViager = null;
    private DepenseReconnue cotisationNonActif = null;
    private Dette dettesHypothecaires = null;
    private Revenu droitHabitation = null;
    private DepenseReconnue fraisEntretiensImmeuble = null;
    private DepenseReconnue fraisRepas = null;
    private DepenseReconnue fraisTransport = null;
    private DepenseReconnue fraisVetements = null;
    private Fortune hoirie = null;
    private String idConjoint = null;
    private String idRequerant = null;
    private Fortune immeubleHabite = null;
    private Revenu indemnitesJournalieresAccidents = null;
    private Revenu indemnitesJournalieresAI = null;
    private Revenu indemnitesJournalieresAPG = null;
    private Revenu indemnitesJournalieresChomage = null;
    private Revenu indemnitesJournalieresMaladie = null;
    private Revenu indemnitesJournalieresMilitaire = null;
    private Revenu interetFortune = null;
    private DepenseReconnue interetsHypothecaires = null;
    private Fortune liquidite = null;
    private DepenseReconnue loyerAnnuel = null;
    private Revenu loyersEtFermages = null;
    private Revenu pensionAlimentaire = null;
    private DepenseReconnue pensionAlimentaireVersee = null;
    private String penurieLogement = null;
    private String plusieursEmployeurs = null;
    private Fortune rachatAssuranceVie = null;
    private Revenu rentes3P = null;
    private Revenu rentesAI = null;
    private Revenu rentesAssurancesPrivees = null;
    private Revenu rentesAVS = null;
    private Revenu rentesEtrangeres = null;
    private Revenu rentesLAA = null;
    private Revenu rentesLPP = null;
    private Revenu rentesMilitaire = null;
    private Revenu renteVeufOuOrphelin = null;
    private Revenu revenuBrutImpotSource = null;
    private Revenu revenuHypothetiqueCasRigueur = null;
    private Revenu revenuIndependant = null;
    private Revenu salaireNature = null;
    private Revenu salaireNet = null;
    private Revenu sousLocation = null;
    private Revenu successionNonPartagee = null;
    private DepenseReconnue tailleUniteAssistance = null;
    private Revenu tauxOccupation = null;
    private Revenu totalRentes = null;
    private Revenu valeurLocativePropreImmeuble = null;
    private Revenu valeurUsufruit = null;

    public PFDonneefinanciereViewBean() {
        super();
        revenus = new ArrayList<Revenu>();
        dettes = new ArrayList<Dette>();
        fortunes = new ArrayList<Fortune>();
        depensesReconnues = new ArrayList<DepenseReconnue>();

        // Revenus
        valeurUsufruit = new Revenu(RevenuType.VALEUR_USUFRUIT);
        revenus.add(valeurUsufruit);
        droitHabitation = new Revenu(RevenuType.DROIT_HABITATION);
        revenus.add(droitHabitation);
        aidesLogement = new Revenu(RevenuType.AIDES_LOGEMENT);
        revenus.add(aidesLogement);
        autresCreances = new Revenu(RevenuType.AUTRES_CREANCES);
        revenus.add(autresCreances);
        successionNonPartagee = new Revenu(RevenuType.SUCCESSION_NON_PARTAGEE);
        revenus.add(successionNonPartagee);
        rentesEtrangeres = new Revenu(RevenuType.RENTES_ETRANGERES);
        revenus.add(rentesEtrangeres);
        rentesLAA = new Revenu(RevenuType.RENTES_LAA);
        revenus.add(rentesLAA);
        renteVeufOuOrphelin = new Revenu(RevenuType.RENTE_VEUF_OU_ORPHELIN);
        revenus.add(renteVeufOuOrphelin);
        rentes3P = new Revenu(RevenuType.RENTES_3P);
        revenus.add(rentes3P);
        allocationsAMINH = new Revenu(RevenuType.ALLOCATIONS_AMINH);
        revenus.add(allocationsAMINH);
        rentesAssurancesPrivees = new Revenu(RevenuType.RENTES_ASSURANCES_PRIVEES);
        revenus.add(rentesAssurancesPrivees);
        rentesMilitaire = new Revenu(RevenuType.RENTES_MILITAIRE);
        revenus.add(rentesMilitaire);
        revenuBrutImpotSource = new Revenu(RevenuType.REVENU_BRUT_IMPOT_SOURCE);
        revenus.add(revenuBrutImpotSource);
        rentesAI = new Revenu(RevenuType.RENTES_AI);
        revenus.add(rentesAI);
        rentesAVS = new Revenu(RevenuType.RENTES_AVS);
        revenus.add(rentesAVS);
        rentesLPP = new Revenu(RevenuType.RENTES_LPP);
        revenus.add(rentesLPP);
        pensionAlimentaire = new Revenu(RevenuType.PENSION_ALIMENTAIRE);
        revenus.add(pensionAlimentaire);
        brapa = new Revenu(RevenuType.BRAPA);
        revenus.add(brapa);
        contratEntretiensViager = new Revenu(RevenuType.CONTRAT_ENTRETIENS_VIAGER);
        revenus.add(contratEntretiensViager);
        autresRentes = new Revenu(RevenuType.AUTRES_RENTES);
        revenus.add(autresRentes);
        indemnitesJournalieresMaladie = new Revenu(RevenuType.INDEMNITES_JOURNALIERES_MALADIE);
        revenus.add(indemnitesJournalieresMaladie);
        indemnitesJournalieresAccidents = new Revenu(RevenuType.INDEMNITES_JOURNALIERES_ACCIDENTS);
        revenus.add(indemnitesJournalieresAccidents);
        indemnitesJournalieresChomage = new Revenu(RevenuType.INDEMNITES_JOURNALIERES_CHOMAGE);
        revenus.add(indemnitesJournalieresChomage);
        indemnitesJournalieresAPG = new Revenu(RevenuType.INDEMNITES_JOURNALIERES_APG);
        revenus.add(indemnitesJournalieresAPG);
        indemnitesJournalieresAI = new Revenu(RevenuType.INDEMNITES_JOURNALIERES_AI);
        revenus.add(indemnitesJournalieresAI);
        indemnitesJournalieresMilitaire = new Revenu(RevenuType.INDEMNITES_JOURNALIERES_MILITAIRE);
        revenus.add(indemnitesJournalieresMilitaire);
        allocationsFamiliales = new Revenu(RevenuType.ALLOCATIONS_FAMILIALES);
        revenus.add(allocationsFamiliales);
        aideFormation = new Revenu(RevenuType.AIDE_FORMATION);
        revenus.add(aideFormation);
        interetFortune = new Revenu(RevenuType.INTERET_FORTUNE);
        revenus.add(interetFortune);
        valeurLocativePropreImmeuble = new Revenu(RevenuType.VALEUR_LOCATIVE_PROPRE_IMMEUBLE);
        revenus.add(valeurLocativePropreImmeuble);
        sousLocation = new Revenu(RevenuType.SOUS_LOCATION);
        revenus.add(sousLocation);
        loyersEtFermages = new Revenu(RevenuType.LOYERS_ET_FERMAGES);
        revenus.add(loyersEtFermages);
        allocationCantonaleMaternite = new Revenu(RevenuType.ALLOCATION_CANTONALE_MATERNITE);
        revenus.add(allocationCantonaleMaternite);
        revenuIndependant = new Revenu(RevenuType.REVENU_INDEPENDANT);
        revenus.add(revenuIndependant);
        salaireNet = new Revenu(RevenuType.SALAIRE_NET);
        revenus.add(salaireNet);
        salaireNature = new Revenu(RevenuType.SALAIRE_NATURE);
        revenus.add(salaireNature);
        tauxOccupation = new Revenu(RevenuType.TAUX_OCCUPATION);
        revenus.add(tauxOccupation);
        totalRentes = new Revenu(RevenuType.TOTAL_RENTES);
        revenus.add(totalRentes);
        revenuHypothetiqueCasRigueur = new Revenu(RevenuType.REVENU_HYPOTHETIQUE_CAS_RIGUEUR);
        revenus.add(revenuHypothetiqueCasRigueur);

        // Dettes
        dettesHypothecaires = new Dette(DetteType.DETTES_HYPOTHECAIRES);
        dettes.add(dettesHypothecaires);
        autresDettes = new Dette(DetteType.AUTRES_DETTES);
        dettes.add(autresDettes);

        // Fortune
        immeubleHabite = new Fortune(FortuneType.IMMEUBLE_HABITE);
        fortunes.add(immeubleHabite);
        autresImmeubles = new Fortune(FortuneType.AUTRES_IMMEUBLES);
        fortunes.add(autresImmeubles);
        biensEtrangers = new Fortune(FortuneType.BIENS_ETRANGERS);
        fortunes.add(biensEtrangers);
        liquidite = new Fortune(FortuneType.LIQUIDITE);
        fortunes.add(liquidite);
        rachatAssuranceVie = new Fortune(FortuneType.RACHAT_ASSURANCE_VIE);
        fortunes.add(rachatAssuranceVie);
        hoirie = new Fortune(FortuneType.HOIRIE);
        fortunes.add(hoirie);
        cession = new Fortune(FortuneType.CESSION);
        fortunes.add(cession);
        autreBien = new Fortune(FortuneType.AUTRE_BIEN);
        fortunes.add(autreBien);

        // Depenses reconnues
        loyerAnnuel = new DepenseReconnue(DepenseReconnueType.LOYER_ANNUEL);
        depensesReconnues.add(loyerAnnuel);
        chargesAnnuelles = new DepenseReconnue(DepenseReconnueType.CHARGES_ANNUELLES);
        depensesReconnues.add(chargesAnnuelles);
        cotisationNonActif = new DepenseReconnue(DepenseReconnueType.COTISATION_NON_ACTIF);
        depensesReconnues.add(cotisationNonActif);
        fraisEntretiensImmeuble = new DepenseReconnue(DepenseReconnueType.FRAIS_ENTRETIENS_IMMEUBLE);
        depensesReconnues.add(fraisEntretiensImmeuble);
        interetsHypothecaires = new DepenseReconnue(DepenseReconnueType.INTERETS_HYPOTHECAIRES);
        depensesReconnues.add(interetsHypothecaires);
        pensionAlimentaireVersee = new DepenseReconnue(DepenseReconnueType.PENSION_ALIMENTAIRE_VERSEE);
        depensesReconnues.add(pensionAlimentaireVersee);
        tailleUniteAssistance = new DepenseReconnue(DepenseReconnueType.TAILLE_UNITE_ASSISTANCE);
        depensesReconnues.add(tailleUniteAssistance);
        fraisRepas = new DepenseReconnue(DepenseReconnueType.FRAIS_REPAS);
        depensesReconnues.add(fraisRepas);
        fraisTransport = new DepenseReconnue(DepenseReconnueType.FRAIS_TRANSPORT);
        depensesReconnues.add(fraisTransport);
        fraisVetements = new DepenseReconnue(DepenseReconnueType.FRAIS_VETEMENTS);
        depensesReconnues.add(fraisVetements);

    }

    /**
     * @return the adresseAssure
     */
    public String getAdresseAssure() {
        return adresseAssure;
    }

    /**
     * @return the aideFormation
     */
    public Revenu getAideFormation() {
        return aideFormation;
    }

    /**
     * @return the aidesLogement
     */
    public Revenu getAidesLogement() {
        return aidesLogement;
    }

    /**
     * @return the allocationCantonaleMaternite
     */
    public Revenu getAllocationCantonaleMaternite() {
        return allocationCantonaleMaternite;
    }

    /**
     * @return the rentesViageres
     */
    public Revenu getAllocationsAMINH() {
        return allocationsAMINH;
    }

    /**
     * @return the allocationsFamiliales
     */
    public Revenu getAllocationsFamiliales() {
        return allocationsFamiliales;
    }

    /**
     * @return the autreBien
     */
    public Fortune getAutreBien() {
        return autreBien;
    }

    /**
     * @return the autresCreances
     */
    public Revenu getAutresCreances() {
        return autresCreances;
    }

    /**
     * @return the autresDettes
     */
    public Dette getAutresDettes() {
        return autresDettes;
    }

    /**
     * @return the autresImmeubles
     */
    public Fortune getAutresImmeubles() {
        return autresImmeubles;
    }

    /**
     * @return the autresRentes
     */
    public Revenu getAutresRentes() {
        return autresRentes;
    }

    /**
     * @return the avec13emeSalaire
     */
    public String getAvec13emeSalaire() {
        return avec13emeSalaire;
    }

    /**
     * @return the biensEtrangers
     */
    public Fortune getBiensEtrangers() {
        return biensEtrangers;
    }

    public Revenu getBrapa() {
        return brapa;
    }

    /**
     * @return the cession
     */
    public Fortune getCession() {
        return cession;
    }

    /**
     * @return the chargesAnnuelles
     */
    public DepenseReconnue getChargesAnnuelles() {
        return chargesAnnuelles;
    }

    /**
     * @return the contratEntretiensViager
     */
    public Revenu getContratEntretiensViager() {
        return contratEntretiensViager;
    }

    /**
     * @return the cotisationNonActif
     */
    public DepenseReconnue getCotisationNonActif() {
        return cotisationNonActif;
    }

    /**
     * @return the dettesHypothecaires
     */
    public Dette getDettesHypothecaires() {
        return dettesHypothecaires;
    }

    /**
     * @return the droitHabitation
     */
    public Revenu getDroitHabitation() {
        return droitHabitation;
    }

    /**
     * @return the fraisEntretiensImmeuble
     */
    public DepenseReconnue getFraisEntretiensImmeuble() {
        return fraisEntretiensImmeuble;
    }

    /**
     * @return the fraisRepas
     */
    public DepenseReconnue getFraisRepas() {
        return fraisRepas;
    }

    /**
     * @return the fraisTransport
     */
    public DepenseReconnue getFraisTransport() {
        return fraisTransport;
    }

    /**
     * @return the fraisVetements
     */
    public DepenseReconnue getFraisVetements() {
        return fraisVetements;
    }

    /**
     * @return the hoirie
     */
    public Fortune getHoirie() {
        return hoirie;
    }

    /**
     * @return the idConjoint
     */
    public String getIdConjoint() {
        return idConjoint;
    }

    /**
     * @return the idRequerant
     */
    public String getIdRequerant() {
        return idRequerant;
    }

    /**
     * @return the immeubleHabite
     */
    public Fortune getImmeubleHabite() {
        return immeubleHabite;
    }

    /**
     * @return the indemnitesJournalieresAccidents
     */
    public Revenu getIndemnitesJournalieresAccidents() {
        return indemnitesJournalieresAccidents;
    }

    /**
     * @return the indemnitesJournalieresAI
     */
    public Revenu getIndemnitesJournalieresAI() {
        return indemnitesJournalieresAI;
    }

    /**
     * @return the indemnitesJournalieresAPG
     */
    public Revenu getIndemnitesJournalieresAPG() {
        return indemnitesJournalieresAPG;
    }

    /**
     * @return the indemnitesJournalieresChomage
     */
    public Revenu getIndemnitesJournalieresChomage() {
        return indemnitesJournalieresChomage;
    }

    /**
     * @return the indemnitesJournalieresMaladie
     */
    public Revenu getIndemnitesJournalieresMaladie() {
        return indemnitesJournalieresMaladie;
    }

    /**
     * @return the indemnitesJournalieresMilitaire
     */
    public Revenu getIndemnitesJournalieresMilitaire() {
        return indemnitesJournalieresMilitaire;
    }

    /**
     * @return the interetFortune
     */
    public Revenu getInteretFortune() {
        return interetFortune;
    }

    /**
     * @return the interetsHypothecaires
     */
    public DepenseReconnue getInteretsHypothecaires() {
        return interetsHypothecaires;
    }

    /**
     * @return the liquidite
     */
    public Fortune getLiquidite() {
        return liquidite;
    }

    /**
     * @return the loyerAnnuel
     */
    public DepenseReconnue getLoyerAnnuel() {
        return loyerAnnuel;
    }

    /**
     * @return the loyersEtFermages
     */
    public Revenu getLoyersEtFermages() {
        return loyersEtFermages;
    }

    /**
     * @return the pensionAlimentaire
     */
    public Revenu getPensionAlimentaire() {
        return pensionAlimentaire;
    }

    /**
     * @return the pensionAlimentaireVersee
     */
    public DepenseReconnue getPensionAlimentaireVersee() {
        return pensionAlimentaireVersee;
    }

    /**
     * @return the penurieLogement
     */
    public String getPenurieLogement() {
        return penurieLogement;
    }

    /**
     * @return the plusieursEmployeurs
     */
    public String getPlusieursEmployeurs() {
        return plusieursEmployeurs;
    }

    /**
     * @return the rachatAssuranceVie
     */
    public Fortune getRachatAssuranceVie() {
        return rachatAssuranceVie;
    }

    /**
     * @return the rentes3P
     */
    public Revenu getRentes3P() {
        return rentes3P;
    }

    /**
     * @return the rentesAI
     */
    public Revenu getRentesAI() {
        return rentesAI;
    }

    /**
     * @return the rentesAssurancesPrivees
     */
    public Revenu getRentesAssurancesPrivees() {
        return rentesAssurancesPrivees;
    }

    /**
     * @return the rentesAVS
     */
    public Revenu getRentesAVS() {
        return rentesAVS;
    }

    /**
     * @return the rentesEtrangeres
     */
    public Revenu getRentesEtrangeres() {
        return rentesEtrangeres;
    }

    /**
     * @return the rentesLAA
     */
    public Revenu getRentesLAA() {
        return rentesLAA;
    }

    /**
     * @return the rentesLPP
     */
    public Revenu getRentesLPP() {
        return rentesLPP;
    }

    /**
     * @return the rentesMilitaire
     */
    public Revenu getRentesMilitaire() {
        return rentesMilitaire;
    }

    public Revenu getRenteVeufOuOrphelin() {
        return renteVeufOuOrphelin;
    }

    /**
     * @return the revenuBrutImpotSource
     */
    public Revenu getRevenuBrutImpotSource() {
        return revenuBrutImpotSource;
    }

    /**
     * @return the revenuHypothetiqueCasRigueur
     */
    public Revenu getRevenuHypothetiqueCasRigueur() {
        return revenuHypothetiqueCasRigueur;
    }

    /**
     * @return the revenuIndependant
     */
    public Revenu getRevenuIndependant() {
        return revenuIndependant;
    }

    /**
     * @return the salaireNature
     */
    public Revenu getSalaireNature() {
        return salaireNature;
    }

    /**
     * @return the salaireNet
     */
    public Revenu getSalaireNet() {
        return salaireNet;
    }

    /**
     * @return the sousLocation
     */
    public Revenu getSousLocation() {
        return sousLocation;
    }

    /**
     * @return the successionNonPartagee
     */
    public Revenu getSuccessionNonPartagee() {
        return successionNonPartagee;
    }

    /**
     * @return the tailleUniteAssistance
     */
    public DepenseReconnue getTailleUniteAssistance() {
        return tailleUniteAssistance;
    }

    /**
     * @return the tauxOccupation
     */
    public Revenu getTauxOccupation() {
        return tauxOccupation;
    }

    /**
     * @return the totalRentes
     */
    public Revenu getTotalRentes() {
        return totalRentes;
    }

    /**
     * @return the valeurLocativePropreImmeuble
     */
    public Revenu getValeurLocativePropreImmeuble() {
        return valeurLocativePropreImmeuble;
    }

    /**
     * @return the valeurUsufruit
     */
    public Revenu getValeurUsufruit() {
        return valeurUsufruit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    public void init() throws Exception {
        // TODO : Faire les init ici plutôt que dans PFDonneeFinanciereAction
    }

    @Override
    public void retrieve() throws Exception {
        // Récupération de la localité du tiers
        if (!JadeStringUtil.isEmpty(getIdRequerant())) {
            adresseAssure = PFUserHelper.getLocaliteAssure(membreFamille.getPersonneEtendue(), demande
                    .getSimpleDemande().getDateDebut());
        } else {
            adresseAssure = "";
        }

        // Retrieve des revenus

        RevenuSearchModel searchModel = new RevenuSearchModel();
        searchModel.setForIdDemande(demande.getId());
        searchModel.setForIdMembreFamille(membreFamille.getId());
        searchModel = PerseusServiceLocator.getRevenuService().search(searchModel);

        for (JadeAbstractModel monRevenu : searchModel.getSearchResults()) {
            Revenu revenu = (Revenu) monRevenu;
            switch (revenu.getTypeAsEnum()) {
                case SALAIRE_NET:
                    salaireNet.copy(revenu);
                    break;
                case SALAIRE_NATURE:
                    salaireNature.copy(revenu);
                    break;
                case INDEMNITES_JOURNALIERES_MALADIE:
                    indemnitesJournalieresMaladie.copy(revenu);
                    break;
                case REVENU_INDEPENDANT:
                    revenuIndependant.copy(revenu);
                    break;
                case TAUX_OCCUPATION:
                    tauxOccupation.copy(revenu);
                    break;
                case VALEUR_USUFRUIT:
                    valeurUsufruit.copy(revenu);
                    break;
                case DROIT_HABITATION:
                    droitHabitation.copy(revenu);
                    break;
                case AIDES_LOGEMENT:
                    aidesLogement.copy(revenu);
                    break;
                case AUTRES_CREANCES:
                    autresCreances.copy(revenu);
                    break;
                case SUCCESSION_NON_PARTAGEE:
                    successionNonPartagee.copy(revenu);
                    break;
                case RENTES_ETRANGERES:
                    rentesEtrangeres.copy(revenu);
                    break;
                case RENTES_LAA:
                    rentesLAA.copy(revenu);
                    break;
                case RENTE_VEUF_OU_ORPHELIN:
                    renteVeufOuOrphelin.copy(revenu);
                    break;
                case RENTES_3P:
                    rentes3P.copy(revenu);
                    break;
                case ALLOCATIONS_AMINH:
                    allocationsAMINH.copy(revenu);
                    break;
                case RENTES_ASSURANCES_PRIVEES:
                    rentesAssurancesPrivees.copy(revenu);
                    break;
                case RENTES_MILITAIRE:
                    rentesMilitaire.copy(revenu);
                    break;
                case REVENU_BRUT_IMPOT_SOURCE:
                    revenuBrutImpotSource.copy(revenu);
                    break;
                case RENTES_AI:
                    rentesAI.copy(revenu);
                    break;
                case RENTES_AVS:
                    rentesAVS.copy(revenu);
                    break;
                case RENTES_LPP:
                    rentesLPP.copy(revenu);
                    break;
                case PENSION_ALIMENTAIRE:
                    pensionAlimentaire.copy(revenu);
                    break;
                case BRAPA:
                    brapa.copy(revenu);
                    break;
                case CONTRAT_ENTRETIENS_VIAGER:
                    contratEntretiensViager.copy(revenu);
                    break;
                case AUTRES_RENTES:
                    autresRentes.copy(revenu);
                    break;
                case INDEMNITES_JOURNALIERES_ACCIDENTS:
                    indemnitesJournalieresAccidents.copy(revenu);
                    break;
                case INDEMNITES_JOURNALIERES_CHOMAGE:
                    indemnitesJournalieresChomage.copy(revenu);
                    break;
                case INDEMNITES_JOURNALIERES_APG:
                    indemnitesJournalieresAPG.copy(revenu);
                    break;
                case INDEMNITES_JOURNALIERES_AI:
                    indemnitesJournalieresAI.copy(revenu);
                    break;
                case INDEMNITES_JOURNALIERES_MILITAIRE:
                    indemnitesJournalieresMilitaire.copy(revenu);
                    break;
                case ALLOCATIONS_FAMILIALES:
                    allocationsFamiliales.copy(revenu);
                    break;
                case AIDE_FORMATION:
                    aideFormation.copy(revenu);
                    break;
                case INTERET_FORTUNE:
                    interetFortune.copy(revenu);
                    break;
                case VALEUR_LOCATIVE_PROPRE_IMMEUBLE:
                    valeurLocativePropreImmeuble.copy(revenu);
                    break;
                case LOYERS_ET_FERMAGES:
                    loyersEtFermages.copy(revenu);
                    break;
                case ALLOCATION_CANTONALE_MATERNITE:
                    allocationCantonaleMaternite.copy(revenu);
                    break;
                case TOTAL_RENTES:
                    totalRentes.copy(revenu);
                    break;
                case REVENU_HYPOTHETIQUE_CAS_RIGUEUR:
                    revenuHypothetiqueCasRigueur.copy(revenu);
                    break;
                case SOUS_LOCATION:
                    sousLocation.copy(revenu);
                    break;
            }
        }

        // Retrieve des dettes

        DetteSearchModel detteSearchModel = new DetteSearchModel();
        detteSearchModel.setForIdDemande(demande.getId());
        detteSearchModel.setForIdMembreFamille(membreFamille.getId());
        detteSearchModel = PerseusServiceLocator.getDetteService().search(detteSearchModel);

        for (JadeAbstractModel maDette : detteSearchModel.getSearchResults()) {
            Dette dette = (Dette) maDette;
            switch (dette.getTypeAsEnum()) {
                case DETTES_HYPOTHECAIRES:
                    dettesHypothecaires.copy(dette);
                    break;
                case AUTRES_DETTES:
                    autresDettes.copy(dette);
                    break;
            }
        }

        // Retrieve de la fortune

        FortuneSearchModel fortuneSearchModel = new FortuneSearchModel();
        fortuneSearchModel.setForIdDemande(demande.getId());
        fortuneSearchModel.setForIdMembreFamille(membreFamille.getId());
        fortuneSearchModel = PerseusServiceLocator.getFortuneService().search(fortuneSearchModel);

        for (JadeAbstractModel maFortune : fortuneSearchModel.getSearchResults()) {
            Fortune fortune = (Fortune) maFortune;
            switch (fortune.getTypeAsEnum()) {
                case IMMEUBLE_HABITE:
                    immeubleHabite.copy(fortune);
                    break;
                case AUTRES_IMMEUBLES:
                    autresImmeubles.copy(fortune);
                    break;
                case BIENS_ETRANGERS:
                    biensEtrangers.copy(fortune);
                    break;
                case LIQUIDITE:
                    liquidite.copy(fortune);
                    break;
                case RACHAT_ASSURANCE_VIE:
                    rachatAssuranceVie.copy(fortune);
                    break;
                case HOIRIE:
                    hoirie.copy(fortune);
                    break;
                case CESSION:
                    cession.copy(fortune);
                    break;
                case AUTRE_BIEN:
                    autreBien.copy(fortune);
                    break;
            }
        }

        // Retrieve des dépenses reconnues

        DepenseReconnueSearchModel depenseReconnueSearchModel = new DepenseReconnueSearchModel();
        depenseReconnueSearchModel.setForIdDemande(demande.getId());
        depenseReconnueSearchModel.setForIdMembreFamille(membreFamille.getId());
        depenseReconnueSearchModel = PerseusServiceLocator.getDepenseReconnueService().search(
                depenseReconnueSearchModel);

        for (JadeAbstractModel maDepenseReconnue : depenseReconnueSearchModel.getSearchResults()) {
            DepenseReconnue depenseReconnue = (DepenseReconnue) maDepenseReconnue;
            switch (depenseReconnue.getTypeAsEnum()) {
                case LOYER_ANNUEL:
                    loyerAnnuel.copy(depenseReconnue);
                    break;
                case CHARGES_ANNUELLES:
                    chargesAnnuelles.copy(depenseReconnue);
                    break;
                case COTISATION_NON_ACTIF:
                    cotisationNonActif.copy(depenseReconnue);
                    break;
                case FRAIS_ENTRETIENS_IMMEUBLE:
                    fraisEntretiensImmeuble.copy(depenseReconnue);
                    break;
                case INTERETS_HYPOTHECAIRES:
                    interetsHypothecaires.copy(depenseReconnue);
                    break;
                case PENSION_ALIMENTAIRE_VERSEE:
                    pensionAlimentaireVersee.copy(depenseReconnue);
                    break;
                case TAILLE_UNITE_ASSISTANCE:
                    tailleUniteAssistance.copy(depenseReconnue);
                    break;
                case FRAIS_REPAS:
                    fraisRepas.copy(depenseReconnue);
                    break;
                case FRAIS_TRANSPORT:
                    fraisTransport.copy(depenseReconnue);
                    break;
                case FRAIS_VETEMENTS:
                    fraisVetements.copy(depenseReconnue);
                    break;
            }
        }

    }

    /**
     * @param adresseAssure
     *            the adresseAssure to set
     */
    public void setAdresseAssure(String adresseAssure) {
        this.adresseAssure = adresseAssure;
    }

    /**
     * @param aideFormation
     *            the aideFormation to set
     */
    public void setAideFormation(Revenu aideFormation) {
        this.aideFormation = aideFormation;
    }

    /**
     * @param aidesLogement
     *            the aidesLogement to set
     */
    public void setAidesLogement(Revenu aidesLogement) {
        this.aidesLogement = aidesLogement;
    }

    /**
     * @param allocationCantonaleMaternite
     *            the allocationCantonaleMaternite to set
     */
    public void setAllocationCantonaleMaternite(Revenu allocationCantonaleMaternite) {
        this.allocationCantonaleMaternite = allocationCantonaleMaternite;
    }

    /**
     * @param rentesViageres
     *            the rentesViageres to set
     */
    public void setAllocationsAMINH(Revenu allocationsAMINH) {
        this.allocationsAMINH = allocationsAMINH;
    }

    /**
     * @param allocationsFamiliales
     *            the allocationsFamiliales to set
     */
    public void setAllocationsFamiliales(Revenu allocationsFamiliales) {
        this.allocationsFamiliales = allocationsFamiliales;
    }

    /**
     * @param autreBien
     *            the autreBien to set
     */
    public void setAutreBien(Fortune autreBien) {
        this.autreBien = autreBien;
    }

    /**
     * @param autresCreances
     *            the autresCreances to set
     */
    public void setAutresCreances(Revenu autresCreances) {
        this.autresCreances = autresCreances;
    }

    /**
     * @param autresDettes
     *            the autresDettes to set
     */
    public void setAutresDettes(Dette autresDettes) {
        this.autresDettes = autresDettes;
    }

    /**
     * @param autresImmeubles
     *            the autresImmeubles to set
     */
    public void setAutresImmeubles(Fortune autresImmeubles) {
        this.autresImmeubles = autresImmeubles;
    }

    /**
     * @param autresRentes
     *            the autresRentes to set
     */
    public void setAutresRentes(Revenu autresRentes) {
        this.autresRentes = autresRentes;
    }

    /**
     * @param avec13emeSalaire
     *            the avec13emeSalaire to set
     */
    public void setAvec13emeSalaire(String avec13emeSalaire) {
        this.avec13emeSalaire = avec13emeSalaire;
    }

    /**
     * @param biensEtrangers
     *            the biensEtrangers to set
     */
    public void setBiensEtrangers(Fortune biensEtrangers) {
        this.biensEtrangers = biensEtrangers;
    }

    public void setBrapa(Revenu brapa) {
        this.brapa = brapa;
    }

    /**
     * @param cession
     *            the cession to set
     */
    public void setCession(Fortune cession) {
        this.cession = cession;
    }

    /**
     * @param chargesAnnuelles
     *            the chargesAnnuelles to set
     */
    public void setChargesAnnuelles(DepenseReconnue chargesAnnuelles) {
        this.chargesAnnuelles = chargesAnnuelles;
    }

    /**
     * @param contratEntretiensViager
     *            the contratEntretiensViager to set
     */
    public void setContratEntretiensViager(Revenu contratEntretiensViager) {
        this.contratEntretiensViager = contratEntretiensViager;
    }

    /**
     * @param cotisationNonActif
     *            the cotisationNonActif to set
     */
    public void setCotisationNonActif(DepenseReconnue cotisationNonActif) {
        this.cotisationNonActif = cotisationNonActif;
    }

    /**
     * @param dettesHypothecaires
     *            the dettesHypothecaires to set
     */
    public void setDettesHypothecaires(Dette dettesHypothecaires) {
        this.dettesHypothecaires = dettesHypothecaires;
    }

    /**
     * @param droitHabitation
     *            the droitHabitation to set
     */
    public void setDroitHabitation(Revenu droitHabitation) {
        this.droitHabitation = droitHabitation;
    }

    /**
     * @param fraisEntretiensImmeuble
     *            the fraisEntretiensImmeuble to set
     */
    public void setFraisEntretiensImmeuble(DepenseReconnue fraisEntretiensImmeuble) {
        this.fraisEntretiensImmeuble = fraisEntretiensImmeuble;
    }

    /**
     * @param fraisRepas
     *            the fraisRepas to set
     */
    public void setFraisRepas(DepenseReconnue fraisRepas) {
        this.fraisRepas = fraisRepas;
    }

    /**
     * @param fraisTransport
     *            the fraisTransport to set
     */
    public void setFraisTransport(DepenseReconnue fraisTransport) {
        this.fraisTransport = fraisTransport;
    }

    /**
     * @param fraisVetements
     *            the fraisVetements to set
     */
    public void setFraisVetements(DepenseReconnue fraisVetements) {
        this.fraisVetements = fraisVetements;
    }

    /**
     * @param hoirie
     *            the hoirie to set
     */
    public void setHoirie(Fortune hoirie) {
        this.hoirie = hoirie;
    }

    /**
     * @param idConjoint
     *            the idConjoint to set
     */
    public void setIdConjoint(String idConjoint) {
        this.idConjoint = idConjoint;
    }

    /**
     * @param idRequerant
     *            the idRequerant to set
     */
    public void setIdRequerant(String idRequerant) {
        this.idRequerant = idRequerant;
    }

    private void setIdsDonneesFinancieres(DonneeFinanciere df) {
        df.setDemande(demande);
        df.setMembreFamille(membreFamille);
    }

    /**
     * @param immeubleHabite
     *            the immeubleHabite to set
     */
    public void setImmeubleHabite(Fortune immeubleHabite) {
        this.immeubleHabite = immeubleHabite;
    }

    /**
     * @param indemnitesJournalieresAccidents
     *            the indemnitesJournalieresAccidents to set
     */
    public void setIndemnitesJournalieresAccidents(Revenu indemnitesJournalieresAccidents) {
        this.indemnitesJournalieresAccidents = indemnitesJournalieresAccidents;
    }

    /**
     * @param indemnitesJournalieresAI
     *            the indemnitesJournalieresAI to set
     */
    public void setIndemnitesJournalieresAI(Revenu indemnitesJournalieresAI) {
        this.indemnitesJournalieresAI = indemnitesJournalieresAI;
    }

    /**
     * @param indemnitesJournalieresAPG
     *            the indemnitesJournalieresAPG to set
     */
    public void setIndemnitesJournalieresAPG(Revenu indemnitesJournalieresAPG) {
        this.indemnitesJournalieresAPG = indemnitesJournalieresAPG;
    }

    /**
     * @param indemnitesJournalieresChomage
     *            the indemnitesJournalieresChomage to set
     */
    public void setIndemnitesJournalieresChomage(Revenu indemnitesJournalieresChomage) {
        this.indemnitesJournalieresChomage = indemnitesJournalieresChomage;
    }

    /**
     * @param indemnitesJournalieresMaladie
     *            the indemnitesJournalieresMaladie to set
     */
    public void setIndemnitesJournalieresMaladie(Revenu indemnitesJournalieresMaladie) {
        this.indemnitesJournalieresMaladie = indemnitesJournalieresMaladie;
    }

    /**
     * @param indemnitesJournalieresMilitaire
     *            the indemnitesJournalieresMilitaire to set
     */
    public void setIndemnitesJournalieresMilitaire(Revenu indemnitesJournalieresMilitaire) {
        this.indemnitesJournalieresMilitaire = indemnitesJournalieresMilitaire;
    }

    /**
     * @param interetFortune
     *            the interetFortune to set
     */
    public void setInteretFortune(Revenu interetFortune) {
        this.interetFortune = interetFortune;
    }

    /**
     * @param interetsHypothecaires
     *            the interetsHypothecaires to set
     */
    public void setInteretsHypothecaires(DepenseReconnue interetsHypothecaires) {
        this.interetsHypothecaires = interetsHypothecaires;
    }

    /**
     * @param liquidite
     *            the liquidite to set
     */
    public void setLiquidite(Fortune liquidite) {
        this.liquidite = liquidite;
    }

    /**
     * @param loyerAnnuel
     *            the loyerAnnuel to set
     */
    public void setLoyerAnnuel(DepenseReconnue loyerAnnuel) {
        this.loyerAnnuel = loyerAnnuel;
    }

    /**
     * @param loyersEtFermages
     *            the loyersEtFermages to set
     */
    public void setLoyersEtFermages(Revenu loyersEtFermages) {
        this.loyersEtFermages = loyersEtFermages;
    }

    /**
     * @param pensionAlimentaire
     *            the pensionAlimentaire to set
     */
    public void setPensionAlimentaire(Revenu pensionAlimentaire) {
        this.pensionAlimentaire = pensionAlimentaire;
    }

    /**
     * @param pensionAlimentaireVersee
     *            the pensionAlimentaireVersee to set
     */
    public void setPensionAlimentaireVersee(DepenseReconnue pensionAlimentaireVersee) {
        this.pensionAlimentaireVersee = pensionAlimentaireVersee;
    }

    /**
     * @param penurieLogement
     *            the penurieLogement to set
     */
    public void setPenurieLogement(String penurieLogement) {
        this.penurieLogement = penurieLogement;
    }

    /**
     * @param plusieursEmployeurs
     *            the plusieursEmployeurs to set
     */
    public void setPlusieursEmployeurs(String plusieursEmployeurs) {
        this.plusieursEmployeurs = plusieursEmployeurs;
    }

    /**
     * @param rachatAssuranceVie
     *            the rachatAssuranceVie to set
     */
    public void setRachatAssuranceVie(Fortune rachatAssuranceVie) {
        this.rachatAssuranceVie = rachatAssuranceVie;
    }

    /**
     * @param rentes3p
     *            the rentes3P to set
     */
    public void setRentes3P(Revenu rentes3p) {
        rentes3P = rentes3p;
    }

    /**
     * @param rentesAI
     *            the rentesAI to set
     */
    public void setRentesAI(Revenu rentesAI) {
        this.rentesAI = rentesAI;
    }

    /**
     * @param rentesAssurancesPrivees
     *            the rentesAssurancesPrivees to set
     */
    public void setRentesAssurancesPrivees(Revenu rentesAssurancesPrivees) {
        this.rentesAssurancesPrivees = rentesAssurancesPrivees;
    }

    /**
     * @param rentesAVS
     *            the rentesAVS to set
     */
    public void setRentesAVS(Revenu rentesAVS) {
        this.rentesAVS = rentesAVS;
    }

    /**
     * @param rentesEtrangeres
     *            the rentesEtrangeres to set
     */
    public void setRentesEtrangeres(Revenu rentesEtrangeres) {
        this.rentesEtrangeres = rentesEtrangeres;
    }

    /**
     * @param rentesLAA
     *            the rentesLAA to set
     */
    public void setRentesLAA(Revenu rentesLAA) {
        this.rentesLAA = rentesLAA;
    }

    /**
     * @param rentesLPP
     *            the rentesLPP to set
     */
    public void setRentesLPP(Revenu rentesLPP) {
        this.rentesLPP = rentesLPP;
    }

    /**
     * @param rentesMilitaire
     *            the rentesMilitaire to set
     */
    public void setRentesMilitaire(Revenu rentesMilitaire) {
        this.rentesMilitaire = rentesMilitaire;
    }

    public void setRenteVeufOuOrphelin(Revenu renteVeufOuOrphelin) {
        this.renteVeufOuOrphelin = renteVeufOuOrphelin;
    }

    /**
     * @param revenuBrutImpotSource
     *            the revenuBrutImpotSource to set
     */
    public void setRevenuBrutImpotSource(Revenu revenuBrutImpotSource) {
        this.revenuBrutImpotSource = revenuBrutImpotSource;
    }

    /**
     * @param revenuHypothetiqueCasRigueur
     *            the revenuHypothetiqueCasRigueur to set
     */
    public void setRevenuHypothetiqueCasRigueur(Revenu revenuHypothetiqueCasRigueur) {
        this.revenuHypothetiqueCasRigueur = revenuHypothetiqueCasRigueur;
    }

    /**
     * @param revenuIndependant
     *            the revenuIndependant to set
     */
    public void setRevenuIndependant(Revenu revenuIndependant) {
        this.revenuIndependant = revenuIndependant;
    }

    /**
     * @param salaireNature
     *            the salaireNature to set
     */
    public void setSalaireNature(Revenu salaireNature) {
        this.salaireNature = salaireNature;
    }

    /**
     * @param salaireNet
     *            the salaireNet to set
     */
    public void setSalaireNet(Revenu salaireNet) {
        this.salaireNet = salaireNet;
    }

    /**
     * @param sousLocation
     *            the sousLocation to set
     */
    public void setSousLocation(Revenu sousLocation) {
        this.sousLocation = sousLocation;
    }

    /**
     * @param successionNonPartagee
     *            the successionNonPartagee to set
     */
    public void setSuccessionNonPartagee(Revenu successionNonPartagee) {
        this.successionNonPartagee = successionNonPartagee;
    }

    /**
     * @param tailleUniteAssistance
     *            the tailleUniteAssistance to set
     */
    public void setTailleUniteAssistance(DepenseReconnue tailleUniteAssistance) {
        this.tailleUniteAssistance = tailleUniteAssistance;
    }

    /**
     * @param tauxOccupation
     *            the tauxOccupation to set
     */
    public void setTauxOccupation(Revenu tauxOccupation) {
        this.tauxOccupation = tauxOccupation;
    }

    /**
     * @param totalRentes
     *            the totalRentes to set
     */
    public void setTotalRentes(Revenu totalRentes) {
        this.totalRentes = totalRentes;
    }

    /**
     * @param valeurLocativePropreImmeuble
     *            the valeurLocativePropreImmeuble to set
     */
    public void setValeurLocativePropreImmeuble(Revenu valeurLocativePropreImmeuble) {
        this.valeurLocativePropreImmeuble = valeurLocativePropreImmeuble;
    }

    /**
     * @param valeurUsufruit
     *            the valeurUsufruit to set
     */
    public void setValeurUsufruit(Revenu valeurUsufruit) {
        this.valeurUsufruit = valeurUsufruit;
    }

    @Override
    public void update() throws Exception {
        // Si c'est le requérant, contrôler que le nombre de personnes a bien été remplit
        // if (!JadeStringUtil.isEmpty(this.idRequerant)
        // && JadeStringUtil.isEmpty(this.loyerAnnuel.getNbPersonnesLogement())) {
        // JadeThread.logError(PFDonneefinanciereViewBean.class.getName(),
        // "perseus.donneesfinancieres.depenseReconnue.nbPersonnesLogement.mandatory");
        // }
        // if (!JadeStringUtil.isEmpty(this.idRequerant)
        // && JadeStringUtil.isEmpty(this.loyerAnnuel.getSimpleDonneeFinanciere().getValeur())) {
        // JadeThread.logError(PFDonneefinanciereViewBean.class.getName(),
        // "perseus.donneesfinancieres.depenseReconnue.loyerannuel.mandatory");
        // }
        // // Si les frais de transport sont saisis, contrôler que les frais de transport acceptés sont remplis
        // if (!JadeStringUtil.isEmpty(this.fraisTransport.getSimpleDonneeFinanciere().getValeur())
        // && JadeStringUtil.isEmpty(this.fraisTransport.getSimpleDonneeFinanciere().getValeurModifieeTaxateur())) {
        // JadeThread.logError(PFDonneefinanciereViewBean.class.getName(),
        // "perseus.donneesfinancieres.depenseReconnue.fraisTransport.check");
        // }
        loyerAnnuel.setPenurieLogement("on".equals(penurieLogement));
        salaireNet.setAvec13eme("on".equals(avec13emeSalaire));
        salaireNet.setPlusieursEmployeurs("on".equals(plusieursEmployeurs));
        super.update();
    }

}
