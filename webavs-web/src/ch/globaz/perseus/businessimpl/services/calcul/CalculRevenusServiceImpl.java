/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.calcul;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.perseus.business.calcul.InputCalcul;
import ch.globaz.perseus.business.calcul.OutputCalcul;
import ch.globaz.perseus.business.calcul.OutputData;
import ch.globaz.perseus.business.constantes.CSTypeConjoint;
import ch.globaz.perseus.business.constantes.CSTypeGarde;
import ch.globaz.perseus.business.constantes.CSVariableMetier;
import ch.globaz.perseus.business.constantes.IPFConstantes;
import ch.globaz.perseus.business.exceptions.calcul.CalculException;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuType;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.business.services.calcul.CalculRevenusService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * @author DDE
 * 
 */
public class CalculRevenusServiceImpl extends PerseusAbstractServiceImpl implements CalculRevenusService {

    private OutputCalcul calculerAutresRevenus(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {

        // Récupération des données pour le calcul
        Float aidesLogement = inputCalcul.getDonneesFinancieresRegroupees().getElementRevenu(RevenuType.AIDES_LOGEMENT)
                .getValeur();
        aidesLogement = this.roundFloat(aidesLogement);
        Float sousLocation = inputCalcul.getDonneesFinancieresRegroupees().getElementRevenu(RevenuType.SOUS_LOCATION)
                .getValeur();
        sousLocation = this.roundFloat(sousLocation);
        Float valeurUsufruit = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.VALEUR_USUFRUIT).getValeur();
        valeurUsufruit = this.roundFloat(valeurUsufruit);
        Float droitHabitation = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.DROIT_HABITATION).getValeur();
        droitHabitation = this.roundFloat(droitHabitation);
        Float autresCreances = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.AUTRES_CREANCES).getValeur();
        autresCreances = this.roundFloat(autresCreances);
        Float successionNonPartagee = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.SUCCESSION_NON_PARTAGEE).getValeur();
        successionNonPartagee = this.roundFloat(successionNonPartagee);

        // Déclaration des données à calculer
        Float autresRevenus = new Float(0);

        // Calcul des données à calculer
        autresRevenus += aidesLogement;
        autresRevenus += valeurUsufruit;
        autresRevenus += droitHabitation;
        autresRevenus += autresCreances;
        autresRevenus += successionNonPartagee;
        autresRevenus += sousLocation;

        // Enregistrement des données
        outputCalcul.addDonnee(OutputData.REVENUS_AIDES_LOGEMENT, aidesLogement);
        outputCalcul.addDonnee(OutputData.REVENUS_SOUS_LOCATION, sousLocation);
        outputCalcul.addDonnee(OutputData.REVENUS_VALEUR_USUFRUIT, valeurUsufruit);
        outputCalcul.addDonnee(OutputData.REVENUS_DROIT_HABITATION, droitHabitation);
        outputCalcul.addDonnee(OutputData.REVENUS_AUTRES_CREANCES, autresCreances);
        outputCalcul.addDonnee(OutputData.REVENUS_SUCCESSION_NON_PARTAGEE, successionNonPartagee);
        outputCalcul.addDonnee(OutputData.REVENUS_AUTRES_REVENUS, autresRevenus);

        return outputCalcul;
    }

    private OutputCalcul calculerDeductionFranchise(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {
        // Récupération des données pour le calcul
        Float revenuActivite = outputCalcul.getDonnee(OutputData.REVENUS_ACTIVITE);
        Float tauxFranchiseRevenu = inputCalcul.getVariableMetier(CSVariableMetier.TAUX_FRANCHISE_REVENU).getTaux();
        Float montantMaxFranchiseRI = inputCalcul.getVariableMetier(CSVariableMetier.FRANCHISE_MAX_RI).getMontant();
        Float revenuHypothetique = outputCalcul.getDonnee(OutputData.REVENUS_HYPOTHETIQUE);

        // Données à calculer
        Float deductionFranchise = new Float(0);
        Float partDuRevenuDepassant = new Float(0);
        Float revenuActiviteModif = new Float(0);
        Float deductionFranchiseRI = new Float(0);

        // Calcul de la franchise sur la part dépassant le revenu hypothétique (seulement si il y'a un revenu
        // d'activité)
        if (revenuActivite > 0) {
            partDuRevenuDepassant = revenuActivite - revenuHypothetique;
            if (partDuRevenuDepassant < 0) {
                partDuRevenuDepassant = new Float(0);
            }
            deductionFranchise = partDuRevenuDepassant * tauxFranchiseRevenu;
            deductionFranchise = this.roundFloat(deductionFranchise);

            // Calcul de la franchise RI
            if (montantMaxFranchiseRI != 0) {
                deductionFranchiseRI = partDuRevenuDepassant / 2;

                if (deductionFranchiseRI > montantMaxFranchiseRI) {
                    deductionFranchiseRI = montantMaxFranchiseRI;
                }
            }

        }
        if (deductionFranchiseRI > deductionFranchise) {
            deductionFranchise = deductionFranchiseRI;
        }

        revenuActiviteModif = revenuActivite - deductionFranchise;
        revenuActiviteModif = this.roundFloat(revenuActiviteModif);

        // Enregistrement des données
        outputCalcul.addDonnee(OutputData.REVENUS_DEDUCTION_FRANCHISE, deductionFranchise);
        outputCalcul.addDonnee(OutputData.REVENUS_ACTIVITE_MODIF, revenuActiviteModif);

        return outputCalcul;
    }

    private OutputCalcul calculerImputationFortuneNette(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {

        // Récupération des données pour le calcul
        Float fortuneNette = outputCalcul.getDonnee(OutputData.FORTUNE_NETTE);
        Float tauxFortune = inputCalcul.getVariableMetier(CSVariableMetier.TAUX_FORTUNE).getTaux();

        // Déclaration des données à calculer
        Float fortuneNetteModif = new Float(0);

        // Calcul des données à calculer
        fortuneNetteModif = fortuneNette * tauxFortune;
        fortuneNetteModif = this.roundFloat(fortuneNetteModif);

        // Enregistrement des données
        outputCalcul.addDonnee(OutputData.REVENUS_FORTUNE_NETTE_MODIF, fortuneNetteModif);
        outputCalcul.addDonnee(OutputData.REVENUS_TAUX_FORTUNE, tauxFortune);

        return outputCalcul;
    }

    private OutputCalcul calculerIndemnitesJournalieres(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {

        // Récupération des données pour le calcul
        Float indemniteJournaliereMaladie = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_MALADIE).getValeur();
        indemniteJournaliereMaladie = this.roundFloat(indemniteJournaliereMaladie);
        Float indemniteJournaliereAccidents = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_ACCIDENTS).getValeur();
        indemniteJournaliereAccidents = this.roundFloat(indemniteJournaliereAccidents);
        Float indemniteJournaliereChomage = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_CHOMAGE).getValeur();
        indemniteJournaliereChomage = this.roundFloat(indemniteJournaliereChomage);
        Float indemniteJournaliereAPG = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_APG).getValeur();
        indemniteJournaliereAPG = this.roundFloat(indemniteJournaliereAPG);
        Float indemniteJournaliereAI = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_AI).getValeur();
        indemniteJournaliereAI = this.roundFloat(indemniteJournaliereAI);
        Float indemniteJournaliereMilitaire = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_MILITAIRE).getValeur();
        indemniteJournaliereMilitaire = this.roundFloat(indemniteJournaliereMilitaire);

        // Déclaration des données à calculer
        Float indemnitesJournalieres = new Float(0);

        // Calcul des données à calculer
        indemnitesJournalieres += indemniteJournaliereMaladie;
        indemnitesJournalieres += indemniteJournaliereAccidents;
        indemnitesJournalieres += indemniteJournaliereChomage;
        indemnitesJournalieres += indemniteJournaliereAPG;
        indemnitesJournalieres += indemniteJournaliereAI;
        indemnitesJournalieres += indemniteJournaliereMilitaire;

        // Enregistrement des données
        outputCalcul.addDonnee(OutputData.REVENUS_INDEMNITE_JOURNALIERE_MALADIE, indemniteJournaliereMaladie);
        outputCalcul.addDonnee(OutputData.REVENUS_INDEMNITE_JOURNALIERE_ACCIDENTS, indemniteJournaliereAccidents);
        outputCalcul.addDonnee(OutputData.REVENUS_INDEMNITE_JOURNALIERE_CHOMAGE, indemniteJournaliereChomage);
        outputCalcul.addDonnee(OutputData.REVENUS_INDEMNITE_JOURNALIERE_APG, indemniteJournaliereAPG);
        outputCalcul.addDonnee(OutputData.REVENUS_INDEMNITE_JOURNALIERE_AI, indemniteJournaliereAI);
        outputCalcul.addDonnee(OutputData.REVENUS_INDEMNITE_JOURNALIERE_MILITAIRE, indemniteJournaliereMilitaire);
        outputCalcul.addDonnee(OutputData.REVENUS_INDEMNITES_JOURNALIERES, indemnitesJournalieres);

        return outputCalcul;
    }

    private OutputCalcul calculerPrestationsRecuees(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {
        // Récupération des données pour le calcul
        Float pensionAlimentaire = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.PENSION_ALIMENTAIRE).getValeur();
        pensionAlimentaire = this.roundFloat(pensionAlimentaire);
        Float brapa = inputCalcul.getDonneesFinancieresRegroupees().getElementRevenu(RevenuType.BRAPA).getValeur();
        brapa = this.roundFloat(brapa);
        Float allocationsFamiliales = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.ALLOCATIONS_FAMILIALES).getValeur();
        allocationsFamiliales = this.roundFloat(allocationsFamiliales);
        Float allocationCantonaleMaternite = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.ALLOCATION_CANTONALE_MATERNITE).getValeur();
        allocationCantonaleMaternite = this.roundFloat(allocationCantonaleMaternite);
        Float allocationsAMINH = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.ALLOCATIONS_AMINH).getValeur();
        allocationsAMINH = this.roundFloat(allocationsAMINH);
        Float aideFormation = inputCalcul.getDonneesFinancieresRegroupees().getElementRevenu(RevenuType.AIDE_FORMATION)
                .getValeur();
        aideFormation = this.roundFloat(aideFormation);
        Float contratEntretiensViager = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.CONTRAT_ENTRETIENS_VIAGER).getValeur();
        contratEntretiensViager = this.roundFloat(contratEntretiensViager);
        Float totalRentes = inputCalcul.getDonneesFinancieresRegroupees().getElementRevenu(RevenuType.TOTAL_RENTES)
                .getValeur();
        totalRentes = this.roundFloat(totalRentes);
        Float autresRentes = inputCalcul.getDonneesFinancieresRegroupees().getElementRevenu(RevenuType.AUTRES_RENTES)
                .getValeur();
        autresRentes = this.roundFloat(autresRentes);

        // Addition du montant de la données financière BRAPA avec pension alimentaire
        pensionAlimentaire += brapa;
        this.roundFloat(pensionAlimentaire);

        // Déclaration des données à calculer
        Float prestationsRecues = new Float(0);

        // Calcul des données à calculer
        prestationsRecues += pensionAlimentaire;
        prestationsRecues += allocationsFamiliales;
        prestationsRecues += allocationCantonaleMaternite;
        prestationsRecues += allocationsAMINH;
        prestationsRecues += aideFormation;
        prestationsRecues += contratEntretiensViager;
        prestationsRecues += totalRentes;
        prestationsRecues += autresRentes;

        // Enregistrement des données
        outputCalcul.addDonnee(OutputData.REVENUS_PENSION_ALIMENTAIRE, pensionAlimentaire);
        outputCalcul.addDonnee(OutputData.REVENUS_ALLOCATIONS_FAMILIALES, allocationsFamiliales);
        outputCalcul.addDonnee(OutputData.REVENUS_ALLOCATION_CANTONALE_MATERNITE, allocationCantonaleMaternite);
        outputCalcul.addDonnee(OutputData.REVENUS_ALLOCATIONS_AMINH, allocationsAMINH);
        outputCalcul.addDonnee(OutputData.REVENUS_AIDE_FORMATION, aideFormation);
        outputCalcul.addDonnee(OutputData.REVENUS_CONTRAT_ENTRETIENS_VIAGER, contratEntretiensViager);
        outputCalcul.addDonnee(OutputData.REVENUS_TOTAL_RENTES, totalRentes);
        outputCalcul.addDonnee(OutputData.REVENUS_AUTRES_RENTES, autresRentes);
        outputCalcul.addDonnee(OutputData.REVENUS_PRESTATIONS_RECUEES, prestationsRecues);

        return outputCalcul;
    }

    private OutputCalcul calculerRendementFortuneImmobiliere(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {

        // Récupération des données pour le calcul
        Float loyersEtFermages = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.LOYERS_ET_FERMAGES).getValeur();
        loyersEtFermages = this.roundFloat(loyersEtFermages);
        Float valeurLocativePropresImmeuble = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.VALEUR_LOCATIVE_PROPRE_IMMEUBLE).getValeur();
        valeurLocativePropresImmeuble = this.roundFloat(valeurLocativePropresImmeuble);

        // Déclaration des données à calculer
        Float rendementFortuneImmobiliere = new Float(0);

        // Calcul des données à calculer
        rendementFortuneImmobiliere += loyersEtFermages;
        rendementFortuneImmobiliere += valeurLocativePropresImmeuble;

        // Enregistrement des données
        outputCalcul.addDonnee(OutputData.REVENUS_LOYERS_ET_FERMAGES, loyersEtFermages);
        outputCalcul.addDonnee(OutputData.REVENUS_VALEUR_LOCATIVE_PROPRE_IMMEUBLE, valeurLocativePropresImmeuble);
        outputCalcul.addDonnee(OutputData.REVENUS_RENDEMENT_FORTUNE_IMMOBILIERE, rendementFortuneImmobiliere);

        return outputCalcul;
    }

    private OutputCalcul calculerRendementFortuneMobiliere(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {

        // Récupération des données pour le calcul
        Float interetFortune = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.INTERET_FORTUNE).getValeur();
        interetFortune = this.roundFloat(interetFortune);
        Float fortuneDessaisie = outputCalcul.getDonnee(OutputData.FORTUNE_CESSION_MODIF_REQUERANT)
                + outputCalcul.getDonnee(OutputData.FORTUNE_CESSION_MODIF_CONJOINT);
        Float tauxInteretFortuneDessaisie = inputCalcul.getVariableMetier(
                CSVariableMetier.TAUX_INTERET_FORTUNE_DESSAISIE).getTaux();

        // Déclaration des données à calculer
        Float rendementFortuneMobiliere = new Float(0);
        Float interetFortuneDessaisie = new Float(0);

        // Calcul de la fortune dessaisie
        interetFortuneDessaisie = fortuneDessaisie * tauxInteretFortuneDessaisie;
        interetFortuneDessaisie = this.roundFloat(interetFortuneDessaisie);

        // Calcul des données à calculer
        rendementFortuneMobiliere += interetFortune;
        rendementFortuneMobiliere += interetFortuneDessaisie;

        // Enregistrement des données
        outputCalcul.addDonnee(OutputData.REVENUS_INTERET_FORTUNE, interetFortune);
        outputCalcul.addDonnee(OutputData.REVENUS_INTERET_FORTUNE_DESSAISIE, interetFortuneDessaisie);
        outputCalcul.addDonnee(OutputData.REVENUS_RENDEMENT_FORTUNE_MOBILIERE, rendementFortuneMobiliere);

        return outputCalcul;
    }

    private OutputCalcul calculerRevenuActiviteLucrative(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {

        // Récupération des données pour le calcul
        Float salaireNetRequerant = inputCalcul.getDonneesFinancieresRequerant()
                .getElementRevenu(RevenuType.SALAIRE_NET).getValeur();
        salaireNetRequerant = this.roundFloat(salaireNetRequerant);
        Float salaireNatureRequerant = inputCalcul.getDonneesFinancieresRequerant()
                .getElementRevenu(RevenuType.SALAIRE_NATURE).getValeur();
        salaireNatureRequerant = this.roundFloat(salaireNatureRequerant);
        Float revenuIndependantRequerant = inputCalcul.getDonneesFinancieresRequerant()
                .getElementRevenu(RevenuType.REVENU_INDEPENDANT).getValeur();
        revenuIndependantRequerant = this.roundFloat(revenuIndependantRequerant);
        Float salaireNetConjoint = inputCalcul.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.SALAIRE_NET)
                .getValeur();
        salaireNetConjoint = this.roundFloat(salaireNetConjoint);
        Float salaireNatureConjoint = inputCalcul.getDonneesFinancieresConjoint()
                .getElementRevenu(RevenuType.SALAIRE_NATURE).getValeur();
        salaireNatureConjoint = this.roundFloat(salaireNatureConjoint);
        Float revenuIndependantConjoint = inputCalcul.getDonneesFinancieresConjoint()
                .getElementRevenu(RevenuType.REVENU_INDEPENDANT).getValeur();
        revenuIndependantConjoint = this.roundFloat(revenuIndependantConjoint);

        // Déclaration des données à calculer
        Float revenuActivite = new Float(0);

        // Calcul des données à calculer
        revenuActivite = salaireNetRequerant + salaireNatureRequerant + revenuIndependantRequerant;
        revenuActivite += salaireNetConjoint + salaireNatureConjoint + revenuIndependantConjoint;

        // Enregistrement des données
        outputCalcul.addDonnee(OutputData.REVENUS_SALAIRE_NET_REQUERANT, salaireNetRequerant);
        outputCalcul.addDonnee(OutputData.REVENUS_SALAIRE_NATURE_REQUERANT, salaireNatureRequerant);
        outputCalcul.addDonnee(OutputData.REVENUS_INDEPENDANT_REQUERANT, revenuIndependantRequerant);
        outputCalcul.addDonnee(OutputData.REVENUS_SALAIRE_NET_CONJOINT, salaireNetConjoint);
        outputCalcul.addDonnee(OutputData.REVENUS_SALAIRE_NATURE_CONJOINT, salaireNatureConjoint);
        outputCalcul.addDonnee(OutputData.REVENUS_INDEPENDANT_CONJOINT, revenuIndependantConjoint);
        outputCalcul.addDonnee(OutputData.REVENUS_ACTIVITE, revenuActivite);

        return outputCalcul;
    }

    private OutputCalcul calculerRevenuHypothetique(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {

        // Récupération des données pour le calcul
        Float revenuActivie = outputCalcul.getDonnee(OutputData.REVENUS_ACTIVITE);
        Float indemnitesJournalieres = outputCalcul.getDonnee(OutputData.REVENUS_INDEMNITES_JOURNALIERES);
        Float allocationsCantonalesMaternites = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.ALLOCATION_CANTONALE_MATERNITE).getValeur();
        Float revenuHypothetique;
        if (JadeStringUtil.isEmpty(inputCalcul.getDemande().getSituationFamiliale().getConjoint().getId())) {
            revenuHypothetique = inputCalcul.getVariableMetier(CSVariableMetier.REVENU_HYPOTHETIQUE_PERSONNE_SEULE)
                    .getMontant();
        } else {
            revenuHypothetique = inputCalcul.getVariableMetier(CSVariableMetier.REVENU_HYPOTHETIQUE_COUPLE)
                    .getMontant();
        }
        // Dans le cas d'un cas de rigueur, prendre le revenu hypothétique si celui ci est défini
        if (inputCalcul.getDemande().getSimpleDemande().getCalculParticulier()) {
            revenuHypothetique = inputCalcul.getDonneesFinancieresRequerant()
                    .getElementRevenu(RevenuType.REVENU_HYPOTHETIQUE_CAS_RIGUEUR).getValeur();
        }

        // Déclaration des données à calculer
        Float revenuHypothetiqueModif = new Float(0);

        revenuHypothetiqueModif = revenuHypothetique;
        revenuHypothetiqueModif -= revenuActivie;
        revenuHypothetiqueModif -= allocationsCantonalesMaternites;
        revenuHypothetiqueModif -= indemnitesJournalieres;

        if (revenuHypothetiqueModif < 0) {
            revenuHypothetiqueModif = new Float(0);
        }

        // Enregistrement des données
        outputCalcul.addDonnee(OutputData.REVENUS_HYPOTHETIQUE, revenuHypothetique);
        outputCalcul.addDonnee(OutputData.REVENUS_HYPOTHETIQUE_MODIF, revenuHypothetiqueModif);

        return outputCalcul;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.calcul.CalculRevenusService#calculerRevenusBrutImpotSource(ch.globaz.perseus
     * .business.calcul.InputCalcul, ch.globaz.perseus.business.calcul.OutputCalcul)
     */
    @Override
    public OutputCalcul calculerRevenusBrutImpotSource(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {
        // Données à calculer
        Float revenuBrutImpotSource = new Float(0);

        Boolean prendreEnCompteConjoint = false;
        String csTypeConjoint = inputCalcul.getDemande().getSituationFamiliale().getSimpleSituationFamiliale()
                .getCsTypeConjoint();
        if (CSTypeConjoint.CONJOINT.getCodeSystem().equals(csTypeConjoint)
                || CSTypeConjoint.PARTENAIRE_ENREGISTRE.getCodeSystem().equals(csTypeConjoint)) {
            prendreEnCompteConjoint = true;
        }

        // On prend le champ du requérant
        revenuBrutImpotSource += inputCalcul.getDonneesFinancieresRequerant()
                .getElementRevenu(RevenuType.REVENU_BRUT_IMPOT_SOURCE).getValeur();
        revenuBrutImpotSource += inputCalcul.getDonneesFinancieresRequerant()
                .getElementRevenu(RevenuType.ALLOCATION_CANTONALE_MATERNITE).getValeur();
        revenuBrutImpotSource += inputCalcul.getDonneesFinancieresRequerant()
                .getElementRevenu(RevenuType.ALLOCATIONS_FAMILIALES).getValeur();
        revenuBrutImpotSource += inputCalcul.getDonneesFinancieresRequerant()
                .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_MALADIE).getValeur();
        revenuBrutImpotSource += inputCalcul.getDonneesFinancieresRequerant()
                .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_ACCIDENTS).getValeur();
        revenuBrutImpotSource += inputCalcul.getDonneesFinancieresRequerant()
                .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_AI).getValeur();
        revenuBrutImpotSource += inputCalcul.getDonneesFinancieresRequerant()
                .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_MILITAIRE).getValeur();
        revenuBrutImpotSource += inputCalcul.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.TOTAL_RENTES)
                .getValeur();

        // Et ceux du conjoint si il est pas en concubinage
        if (prendreEnCompteConjoint) {
            revenuBrutImpotSource += inputCalcul.getDonneesFinancieresConjoint()
                    .getElementRevenu(RevenuType.REVENU_BRUT_IMPOT_SOURCE).getValeur();
            revenuBrutImpotSource += inputCalcul.getDonneesFinancieresConjoint()
                    .getElementRevenu(RevenuType.ALLOCATION_CANTONALE_MATERNITE).getValeur();
            revenuBrutImpotSource += inputCalcul.getDonneesFinancieresConjoint()
                    .getElementRevenu(RevenuType.ALLOCATIONS_FAMILIALES).getValeur();
            revenuBrutImpotSource += inputCalcul.getDonneesFinancieresConjoint()
                    .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_MALADIE).getValeur();
            revenuBrutImpotSource += inputCalcul.getDonneesFinancieresConjoint()
                    .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_ACCIDENTS).getValeur();
            revenuBrutImpotSource += inputCalcul.getDonneesFinancieresConjoint()
                    .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_AI).getValeur();
            revenuBrutImpotSource += inputCalcul.getDonneesFinancieresConjoint()
                    .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_MILITAIRE).getValeur();
            revenuBrutImpotSource += inputCalcul.getDonneesFinancieresConjoint()
                    .getElementRevenu(RevenuType.TOTAL_RENTES).getValeur();
        }

        // On cherche les revenus d'apprentissage des enfants
        for (EnfantFamille ef : inputCalcul.getListeEnfants()) {
            // Prendre si l'enfant a plus de 18 ans
            int ageEnfant = JadeDateUtil.getNbYearsBetween(ef.getEnfant().getMembreFamille().getPersonneEtendue()
                    .getPersonne().getDateNaissance(), inputCalcul.getDemande().getSimpleDemande().getDateDebut());
            if (ageEnfant > IPFConstantes.AGE_18ANS) {
                // Calcul du revenu modifié par la franchise
                revenuBrutImpotSource += inputCalcul
                        .getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                        .getElementRevenu(RevenuType.REVENUS_ACTIVITE_ENFANT).getValeur();
                revenuBrutImpotSource += inputCalcul
                        .getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                        .getElementRevenu(RevenuType.RENTE_ENFANT).getValeur();
                revenuBrutImpotSource += inputCalcul
                        .getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                        .getElementRevenu(RevenuType.AUTRES_REVENUS_ENFANT).getValeur();

            }
        }

        // Enregistrement des données
        outputCalcul.addDonnee(OutputData.REVENUS_BRUT_IMPOT_SOURCE, revenuBrutImpotSource);

        return outputCalcul;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.calcul.CalculRevenusService#calculerRevenusDeterminants(ch.globaz.perseus
     * .business.calcul.InputCalcul, ch.globaz.perseus.business.calcul.OutputCalcul)
     */
    @Override
    public OutputCalcul calculerRevenusDeterminants(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {

        outputCalcul = calculerImputationFortuneNette(inputCalcul, outputCalcul);
        outputCalcul = calculerRevenuActiviteLucrative(inputCalcul, outputCalcul);
        outputCalcul = calculerIndemnitesJournalieres(inputCalcul, outputCalcul);
        outputCalcul = calculerRevenuHypothetique(inputCalcul, outputCalcul);
        outputCalcul = calculerDeductionFranchise(inputCalcul, outputCalcul);
        outputCalcul = calculerRevenusEnfants(inputCalcul, outputCalcul);
        outputCalcul = calculerPrestationsRecuees(inputCalcul, outputCalcul);
        outputCalcul = calculerRendementFortuneMobiliere(inputCalcul, outputCalcul);
        outputCalcul = calculerRendementFortuneImmobiliere(inputCalcul, outputCalcul);
        outputCalcul = calculerAutresRevenus(inputCalcul, outputCalcul);

        Float revenuDterminant = new Float(0);
        revenuDterminant += outputCalcul.getDonnee(OutputData.REVENUS_FORTUNE_NETTE_MODIF);
        revenuDterminant += outputCalcul.getDonnee(OutputData.REVENUS_ACTIVITE_MODIF);
        revenuDterminant += outputCalcul.getDonnee(OutputData.REVENUS_INDEMNITES_JOURNALIERES);
        revenuDterminant += outputCalcul.getDonnee(OutputData.REVENUS_HYPOTHETIQUE_MODIF);
        revenuDterminant += outputCalcul.getDonnee(OutputData.REVENUS_ENFANTS);
        revenuDterminant += outputCalcul.getDonnee(OutputData.REVENUS_PRESTATIONS_RECUEES);
        revenuDterminant += outputCalcul.getDonnee(OutputData.REVENUS_RENDEMENT_FORTUNE_MOBILIERE);
        revenuDterminant += outputCalcul.getDonnee(OutputData.REVENUS_RENDEMENT_FORTUNE_IMMOBILIERE);
        revenuDterminant += outputCalcul.getDonnee(OutputData.REVENUS_AUTRES_REVENUS);

        outputCalcul.addDonnee(OutputData.REVENUS_DETERMINANT, revenuDterminant);

        return outputCalcul;
    }

    private OutputCalcul calculerRevenusEnfants(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {

        // Données à calculer
        Float revenusEnfants = new Float(0);
        // Mémoriser le détail du calcul dans un tableau (bricolage puisque pas de stockage dynamique prévu au début du
        // projet)
        String detailCalculEnfantsXml = "<table width='100%'>";
        String celluleAmount = "<td class='libelleMonnaie '>CHF</td><td class='montant' data-g-amountformatter='blankAsZero:false'>";
        String celluleAmountSoulignee = "<td class='libelleMonnaie souligne'>CHF</td><td class='montant souligne' data-g-amountformatter='blankAsZero:false'>";

        // Récupération de la franchise à déduire
        Float franchiseRevenuEnfant = inputCalcul.getVariableMetier(CSVariableMetier.REVENU_NET_FORMATION).getMontant();

        // Pour chacun des enfants, ajouter la valeur dépassant la franchise et les autres revenus sans franchise
        for (EnfantFamille ef : inputCalcul.getListeEnfants()) {
            // Calcul du revenu modifié par la franchise
            Float revenuActivite = inputCalcul
                    .getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                    .getElementRevenu(RevenuType.REVENUS_ACTIVITE_ENFANT).getValeur();
            Float revenuActiviteModif = revenuActivite - franchiseRevenuEnfant;
            if (revenuActiviteModif < 0) {
                revenuActiviteModif = new Float(0);
            }
            // Récupération des autres revenus
            Float autresRevenus = inputCalcul
                    .getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                    .getElementRevenu(RevenuType.AUTRES_REVENUS_ENFANT).getValeur();
            // Récupération de l'aide aux études
            Float aidesFormation = inputCalcul
                    .getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                    .getElementRevenu(RevenuType.AIDE_FORMATION).getValeur();
            // Récupération de la pension alimentaire de l'enfant
            Float pensionAlimentaire = inputCalcul
                    .getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                    .getElementRevenu(RevenuType.PENSION_ALIMENTAIRE_ENFANT).getValeur();
            // Récupération de la donnée finaicère BRAPA_Enfant
            Float brapaEnfant = inputCalcul
                    .getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                    .getElementRevenu(RevenuType.BRAPA_ENFANT).getValeur();
            // Récupération de la rente de l'enfant
            Float rente = inputCalcul.getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                    .getElementRevenu(RevenuType.RENTE_ENFANT).getValeur();

            Float revenusCetEnfant = new Float(0);
            revenusCetEnfant += revenuActiviteModif;
            revenusCetEnfant += autresRevenus;
            revenusCetEnfant += aidesFormation;

            revenusCetEnfant += rente;

            if (CSTypeGarde.GARDE_PARTAGEE.getCodeSystem().equals(ef.getSimpleEnfantFamille().getCsGarde())) {
                revenusCetEnfant = revenusCetEnfant / 2;
            }
            // BZ 7737, la pension alimentaire de l'enfant n'est pas à diviser par 2
            // S120914_002 Ajout d'une donnée financière BRAPA Enfant qui doit être regroupé dans la pension alimentaire
            pensionAlimentaire += brapaEnfant;
            revenusCetEnfant += pensionAlimentaire;
            // Ajout du revenu modifié et des autres revenus aux revenus total des enfants
            revenusEnfants += revenusCetEnfant;

            // Mise dans le tableau xml des infos
            detailCalculEnfantsXml += "<tr><td colspan='7'>"
                    + ef.getEnfant().getMembreFamille().getPersonneEtendue().getTiers().getDesignation1()
                    + " "
                    + ef.getEnfant().getMembreFamille().getPersonneEtendue().getTiers().getDesignation2()
                    + (CSTypeGarde.GARDE_PARTAGEE.getCodeSystem().equals(ef.getSimpleEnfantFamille().getCsGarde()) ? " (1/2)"
                            : "") + "</td></tr>";
            detailCalculEnfantsXml += "<tr><td>&nbsp;-&nbsp;Revenu d'apprentissage</td>" + celluleAmount
                    + revenuActivite + "</td><td colspan='4'>&nbsp;</td></tr>";
            detailCalculEnfantsXml += "<tr><td>&nbsp;&nbsp;&nbsp;-&nbsp;Déduction franchise</td>"
                    + celluleAmountSoulignee + "-" + franchiseRevenuEnfant + "</td>" + celluleAmount
                    + revenuActiviteModif + "</td><td colspan='2'>&nbsp;</td></tr>";
            detailCalculEnfantsXml += "<tr><td>&nbsp;-&nbsp;Autres revenus</td><td colspan='2'>&nbsp;</td>"
                    + celluleAmount + autresRevenus + "</td><td colspan='2'>&nbsp;</td></tr>";
            detailCalculEnfantsXml += "<tr><td>&nbsp;-&nbsp;Aides à la formation</td><td colspan='2'>&nbsp;</td>"
                    + celluleAmount + aidesFormation + "</td><td colspan='2'>&nbsp;</td></tr>";
            detailCalculEnfantsXml += "<tr><td>&nbsp;-&nbsp;Rente</td><td colspan='2'>&nbsp;</td>" + celluleAmount
                    + rente + "</td><td colspan='2'>&nbsp;</td></tr>";
            detailCalculEnfantsXml += "<tr><td>&nbsp;-&nbsp;Pension alimentaire</td><td colspan='2'>&nbsp;</td>"
                    + celluleAmountSoulignee + pensionAlimentaire + "</td>" + celluleAmount + revenusCetEnfant
                    + "</td></tr>";
        }

        revenusEnfants = this.roundFloat(revenusEnfants);
        // Ligne de total
        detailCalculEnfantsXml += "<tr><td colspan='7'>&nbsp;</td></tr>";
        detailCalculEnfantsXml += "<tr><td colspan='5'>Total</td>" + celluleAmount + revenusEnfants + "</td></tr>";

        detailCalculEnfantsXml += "</table>";

        // Enregistrement des données
        outputCalcul.addDonnee(OutputData.REVENUS_ENFANTS_DETAIL_XML, detailCalculEnfantsXml);
        outputCalcul.addDonnee(OutputData.REVENUS_ENFANTS, revenusEnfants);

        return outputCalcul;
    }

}
