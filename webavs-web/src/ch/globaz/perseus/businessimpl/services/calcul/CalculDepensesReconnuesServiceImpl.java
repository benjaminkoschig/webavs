/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.calcul;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.perseus.utils.PFUserHelper;
import ch.globaz.perseus.business.calcul.InputCalcul;
import ch.globaz.perseus.business.calcul.OutputCalcul;
import ch.globaz.perseus.business.calcul.OutputData;
import ch.globaz.perseus.business.constantes.CSTypeGarde;
import ch.globaz.perseus.business.constantes.CSVariableMetier;
import ch.globaz.perseus.business.exceptions.calcul.CalculException;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnueType;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuType;
import ch.globaz.perseus.business.models.parametres.Loyer;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.calcul.CalculDepensesReconnuesService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;

/**
 * @author DDE
 * 
 */
public class CalculDepensesReconnuesServiceImpl extends PerseusAbstractServiceImpl implements
        CalculDepensesReconnuesService {

    private OutputCalcul calculerBesoinsVitaux(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {
        // Donn�es � calculer
        Float besoinsVitaux = new Float(0);

        // Compter les enfants � garde partag�e, et le nombre d'enfants total
        Integer nbEnfantsGardeExclusive = 0;
        Integer nbEnfantsGardePartagee = 0;
        for (EnfantFamille ef : inputCalcul.getListeEnfants()) {
            if (CSTypeGarde.GARDE_PARTAGEE.getCodeSystem().equals(ef.getSimpleEnfantFamille().getCsGarde())) {
                nbEnfantsGardePartagee++;
            } else {
                nbEnfantsGardeExclusive++;
            }
        }

        // Prise en compte des besoins vitaux selon le nombre d'enfant et si c'est un parent seul ou un couple
        if (JadeStringUtil.isEmpty(inputCalcul.getDemande().getSituationFamiliale().getConjoint().getId())) {
            // Pour les parents seuls
            switch (nbEnfantsGardeExclusive) {
                case 0:
                case 1:
                    besoinsVitaux += inputCalcul
                            .getVariableMetier(CSVariableMetier.BESOINS_VITAUX_PARENT_SEUL_1_ENFANT).getMontant();
                    break;
                case 2:
                    besoinsVitaux += inputCalcul.getVariableMetier(
                            CSVariableMetier.BESOINS_VITAUX_PARENT_SEUL_2_ENFANTS).getMontant();
                    break;
                case 3:
                    besoinsVitaux += inputCalcul.getVariableMetier(
                            CSVariableMetier.BESOINS_VITAUX_PARENT_SEUL_3_ENFANTS).getMontant();
                    break;
                case 4:
                    besoinsVitaux += inputCalcul.getVariableMetier(
                            CSVariableMetier.BESOINS_VITAUX_PARENT_SEUL_4_ENFANTS).getMontant();
                    break;
                case 5:
                    besoinsVitaux += inputCalcul.getVariableMetier(
                            CSVariableMetier.BESOINS_VITAUX_PARENT_SEUL_5_ENFANTS).getMontant();
                    break;
                case 6:
                    besoinsVitaux += inputCalcul.getVariableMetier(
                            CSVariableMetier.BESOINS_VITAUX_PARENT_SEUL_6_ENFANTS).getMontant();
                    break;
            }

            // Pour chaque enfant suppl�mentaire ajouter le montant pr�vu
            Integer nbEnfantsSupplementaires = nbEnfantsGardeExclusive - 6;
            if (nbEnfantsSupplementaires > 0) {
                besoinsVitaux += inputCalcul.getVariableMetier(CSVariableMetier.BESOINS_VITAUX_PARENT_SEUL_6_ENFANTS)
                        .getMontant();
                besoinsVitaux += inputCalcul.getVariableMetier(
                        CSVariableMetier.BESOINS_VITAUX_PARENT_SEUL_ENFANTS_SUPPLEMENTAIRE).getMontant()
                        * nbEnfantsSupplementaires;
            }

            // Enlever la moiti� des besoins vitaux d'un enfant pour chaque enfant � garde partag�e
            if (nbEnfantsGardePartagee > 0) {
                Float montantParEnfant = inputCalcul.getVariableMetier(
                        CSVariableMetier.PLAFOND_PRESTATION_PARENT_SEUL_1_ENFANT).getMontant() / 2;
                if (nbEnfantsGardeExclusive == 0) {
                    besoinsVitaux -= montantParEnfant * 2;
                }
                besoinsVitaux += montantParEnfant * nbEnfantsGardePartagee;
            }

        } else {
            // Pour les couples
            switch (nbEnfantsGardeExclusive) {
                case 0:
                case 1:
                    besoinsVitaux += inputCalcul.getVariableMetier(CSVariableMetier.BESOINS_VITAUX_COUPLE_1_ENFANT)
                            .getMontant();
                    break;
                case 2:
                    besoinsVitaux += inputCalcul.getVariableMetier(CSVariableMetier.BESOINS_VITAUX_COUPLE_2_ENFANTS)
                            .getMontant();
                    break;
                case 3:
                    besoinsVitaux += inputCalcul.getVariableMetier(CSVariableMetier.BESOINS_VITAUX_COUPLE_3_ENFANTS)
                            .getMontant();
                    break;
                case 4:
                    besoinsVitaux += inputCalcul.getVariableMetier(CSVariableMetier.BESOINS_VITAUX_COUPLE_4_ENFANTS)
                            .getMontant();
                    break;
                case 5:
                    besoinsVitaux += inputCalcul.getVariableMetier(CSVariableMetier.BESOINS_VITAUX_COUPLE_5_ENFANTS)
                            .getMontant();
                    break;
                case 6:
                    besoinsVitaux += inputCalcul.getVariableMetier(CSVariableMetier.BESOINS_VITAUX_COUPLE_6_ENFANTS)
                            .getMontant();
                    break;
            }

            // Pour chaque enfant suppl�mentaire ajouter le montant pr�vu
            Integer nbEnfantsSupplementaires = nbEnfantsGardeExclusive - 6;
            if (nbEnfantsSupplementaires > 0) {
                besoinsVitaux += inputCalcul.getVariableMetier(CSVariableMetier.BESOINS_VITAUX_COUPLE_6_ENFANTS)
                        .getMontant();
                besoinsVitaux += inputCalcul.getVariableMetier(
                        CSVariableMetier.BESOINS_VITAUX_COUPLE_ENFANT_SUPPLEMENTAIRE).getMontant()
                        * nbEnfantsSupplementaires;
            }

            // Enlever la moiti� des besoins vitaux d'un enfant pour chaque enfant � garde partag�e
            // Il s'agit ici du m�me calcul que pour un parent seul, je laisse le bout de code diff�rent dans le if
            // parceque cela change souvent.
            if (nbEnfantsGardePartagee > 0) {
                Float montantParEnfant = inputCalcul.getVariableMetier(
                        CSVariableMetier.PLAFOND_PRESTATION_COUPLE_1_ENFANT).getMontant() / 2;
                if (nbEnfantsGardeExclusive == 0) {
                    besoinsVitaux -= montantParEnfant * 2;
                }
                besoinsVitaux += montantParEnfant * nbEnfantsGardePartagee;
            }

        }

        besoinsVitaux = this.roundFloat(besoinsVitaux);
        // Enregistrement des donn�es
        outputCalcul.addDonnee(OutputData.DEPENSES_BESOINS_VITAUX, besoinsVitaux);

        return outputCalcul;
    }

    private OutputCalcul calculerCotisationsNonActif(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {
        // R�cup�rer les donn�es
        Float cotisationNonActif = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementDepenseReconnue(DepenseReconnueType.COTISATION_NON_ACTIF).getValeur();
        cotisationNonActif = this.roundFloat(cotisationNonActif);

        outputCalcul.addDonnee(OutputData.DEPENSES_COTISATION_NON_ACTIF, cotisationNonActif);

        return outputCalcul;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.calcul.CalculDepensesReconnuesService#calculerDepensesReconnues(ch.globaz
     * .perseus.business.calcul.InputCalcul, ch.globaz.perseus.business.calcul.OutputCalcul)
     */
    @Override
    public OutputCalcul calculerDepensesReconnues(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {

        outputCalcul = calculerBesoinsVitaux(inputCalcul, outputCalcul);
        outputCalcul = calculerLoyerEtCharges(inputCalcul, outputCalcul);
        outputCalcul = calculerCotisationsNonActif(inputCalcul, outputCalcul);
        outputCalcul = calculerFraisImmeubles(inputCalcul, outputCalcul);
        outputCalcul = calculerPensionsAlimentairesPayees(inputCalcul, outputCalcul);
        outputCalcul = calculerFraisObtentionRevenu(inputCalcul, outputCalcul);

        Float depensesReconnues = new Float(0);
        depensesReconnues += outputCalcul.getDonnee(OutputData.DEPENSES_BESOINS_VITAUX);
        depensesReconnues += outputCalcul.getDonnee(OutputData.DEPENSES_LOYER_ANNUEL_MODIF);
        depensesReconnues += outputCalcul.getDonnee(OutputData.DEPENSES_CHARGES_ANNUELLES_MODIF);
        depensesReconnues += outputCalcul.getDonnee(OutputData.DEPENSES_COTISATION_NON_ACTIF);
        depensesReconnues += outputCalcul.getDonnee(OutputData.DEPENSES_FRAIS_IMMEUBLE_MODIF);
        depensesReconnues += outputCalcul.getDonnee(OutputData.DEPENSES_PENSION_ALIMENTAIRE_VERSEE);
        depensesReconnues += outputCalcul.getDonnee(OutputData.DEPENSES_FRAIS_OBTENTION_REVENU_REQUERANT_MODIF);
        depensesReconnues += outputCalcul.getDonnee(OutputData.DEPENSES_FRAIS_OBTENTION_REVENU_CONJOINT_MODIF);
        depensesReconnues += outputCalcul.getDonnee(OutputData.DEPENSES_FRAIS_OBTENTION_REVENU_ENFANTS);

        outputCalcul.addDonnee(OutputData.DEPENSES_RECONNUES, depensesReconnues);

        return outputCalcul;
    }

    private OutputCalcul calculerFraisImmeubles(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {

        // R�cup�ration des donn�es
        Float interetsHypothecaires = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementDepenseReconnue(DepenseReconnueType.INTERETS_HYPOTHECAIRES).getValeur();
        interetsHypothecaires = this.roundFloat(interetsHypothecaires);
        Float fraisEntretiensImmeuble = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementDepenseReconnue(DepenseReconnueType.FRAIS_ENTRETIENS_IMMEUBLE).getValeur();
        fraisEntretiensImmeuble = this.roundFloat(fraisEntretiensImmeuble);
        Float tauxFraisImmeuble = inputCalcul.getVariableMetier(CSVariableMetier.TAUX_FRAIS_IMMEUBLES).getTaux();
        Float rendementFortuneImmobiliere = outputCalcul.getDonnee(OutputData.REVENUS_RENDEMENT_FORTUNE_IMMOBILIERE);
        rendementFortuneImmobiliere = this.roundFloat(rendementFortuneImmobiliere);

        // A calculer
        Float fraisEntretiensImmeubleModif = new Float(0);
        Float fraisImmeuble = new Float(0);
        Float fraisImmeubleModif = new Float(0);

        Float fraisEntretiensImmeublePlafond = rendementFortuneImmobiliere * tauxFraisImmeuble;
        fraisEntretiensImmeublePlafond = this.roundFloat(fraisEntretiensImmeublePlafond);

        // Plafonnement des frais d'entretiens d'immeuble
        if (fraisEntretiensImmeuble > fraisEntretiensImmeublePlafond) {
            fraisEntretiensImmeubleModif = fraisEntretiensImmeublePlafond;
        } else {
            fraisEntretiensImmeubleModif = fraisEntretiensImmeuble;
        }

        fraisImmeuble = fraisEntretiensImmeubleModif + interetsHypothecaires;

        // Plafonnement des frais d'immeuble au rendement de la fortune immobili�re
        if (fraisImmeuble > rendementFortuneImmobiliere) {
            fraisImmeubleModif = rendementFortuneImmobiliere;
        } else {
            fraisImmeubleModif = fraisImmeuble;
        }

        outputCalcul.addDonnee(OutputData.DEPENSES_INTERETS_HYPOTHECAIRES, interetsHypothecaires);
        outputCalcul.addDonnee(OutputData.DEPENSES_FRAIS_ENTRETIENS_IMMEUBLE, fraisEntretiensImmeuble);
        outputCalcul.addDonnee(OutputData.DEPENSES_FRAIS_ENTRETIENS_IMMEUBLE_MODIF, fraisEntretiensImmeubleModif);
        outputCalcul.addDonnee(OutputData.DEPENSES_FRAIS_IMMEUBLE, fraisImmeuble);
        outputCalcul.addDonnee(OutputData.DEPENSES_FRAIS_IMMEUBLE_MODIF, fraisImmeubleModif);

        return outputCalcul;
    }

    private OutputCalcul calculerFraisObtentionRevenu(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {
        // R�cup�ration des donn�es
        Float fraisRepasRequerant = inputCalcul.getDonneesFinancieresRequerant()
                .getElementDepenseReconnue(DepenseReconnueType.FRAIS_REPAS).getValeur();
        fraisRepasRequerant = this.roundFloat(fraisRepasRequerant);
        Float fraisTransportRequerant = inputCalcul.getDonneesFinancieresRequerant()
                .getElementDepenseReconnue(DepenseReconnueType.FRAIS_TRANSPORT).getValeur();
        fraisTransportRequerant = this.roundFloat(fraisTransportRequerant);
        Float fraisTransportRequerantModifTaxateur = inputCalcul.getDonneesFinancieresRequerant()
                .getElementDepenseReconnue(DepenseReconnueType.FRAIS_TRANSPORT).getValeurModifieeTaxateur();
        fraisTransportRequerantModifTaxateur = this.roundFloat(fraisTransportRequerantModifTaxateur);
        Float fraisVetementsRequerant = inputCalcul.getDonneesFinancieresRequerant()
                .getElementDepenseReconnue(DepenseReconnueType.FRAIS_VETEMENTS).getValeur();
        fraisVetementsRequerant = this.roundFloat(fraisVetementsRequerant);
        Float fraisRepasConjoint = inputCalcul.getDonneesFinancieresConjoint()
                .getElementDepenseReconnue(DepenseReconnueType.FRAIS_REPAS).getValeur();
        fraisRepasConjoint = this.roundFloat(fraisRepasConjoint);
        Float fraisTransportConjoint = inputCalcul.getDonneesFinancieresConjoint()
                .getElementDepenseReconnue(DepenseReconnueType.FRAIS_TRANSPORT).getValeur();
        fraisTransportConjoint = this.roundFloat(fraisTransportConjoint);
        Float fraisTransportConjointModifTaxateur = inputCalcul.getDonneesFinancieresConjoint()
                .getElementDepenseReconnue(DepenseReconnueType.FRAIS_TRANSPORT).getValeurModifieeTaxateur();
        fraisTransportConjointModifTaxateur = this.roundFloat(fraisTransportConjointModifTaxateur);
        Float fraisVetementsConjoint = inputCalcul.getDonneesFinancieresConjoint()
                .getElementDepenseReconnue(DepenseReconnueType.FRAIS_VETEMENTS).getValeur();
        fraisVetementsConjoint = this.roundFloat(fraisVetementsConjoint);
        Float plafondFraisRepas = inputCalcul.getVariableMetier(CSVariableMetier.PLAFOND_FRAIS_REPAS).getMontant();

        // Donn�es r�cup�r�es et calcul�es directement
        Float revenuRequerant = inputCalcul.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.SALAIRE_NET)
                .getValeur();
        revenuRequerant += inputCalcul.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.SALAIRE_NATURE)
                .getValeur();
        revenuRequerant += inputCalcul.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.REVENU_INDEPENDANT)
                .getValeur();
        Float revenuConjoint = inputCalcul.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.SALAIRE_NET)
                .getValeur();
        revenuConjoint += inputCalcul.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.SALAIRE_NATURE)
                .getValeur();
        revenuConjoint += inputCalcul.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.REVENU_INDEPENDANT)
                .getValeur();

        // Plafonnement des donn�es r�cup�r�es
        Float fraisRepasRequerantModif;
        if (fraisRepasRequerant > plafondFraisRepas) {
            fraisRepasRequerantModif = plafondFraisRepas;
        } else {
            fraisRepasRequerantModif = fraisRepasRequerant;
        }

        Float fraisRepasConjointModif;
        if (fraisRepasConjoint > plafondFraisRepas) {
            fraisRepasConjointModif = plafondFraisRepas;
        } else {
            fraisRepasConjointModif = fraisRepasConjoint;
        }

        // Donn�es � calculer
        Float fraisObtentionRevenuRequerant = new Float(0);
        fraisObtentionRevenuRequerant += fraisRepasRequerantModif;
        fraisObtentionRevenuRequerant += fraisTransportRequerantModifTaxateur;
        fraisObtentionRevenuRequerant += fraisVetementsRequerant;

        Float fraisObtentionRevenuConjoint = new Float(0);
        fraisObtentionRevenuConjoint += fraisRepasConjointModif;
        fraisObtentionRevenuConjoint += fraisTransportConjointModifTaxateur;
        fraisObtentionRevenuConjoint += fraisVetementsConjoint;

        // Plafonnement des r�sultats
        Float fraisObtentionRevenuRequerantModif;
        if (fraisObtentionRevenuRequerant > revenuRequerant) {
            fraisObtentionRevenuRequerantModif = revenuRequerant;
        } else {
            fraisObtentionRevenuRequerantModif = fraisObtentionRevenuRequerant;
        }
        Float fraisObtentionRevenuConjointModif;
        if (fraisObtentionRevenuConjoint > revenuConjoint) {
            fraisObtentionRevenuConjointModif = revenuConjoint;
        } else {
            fraisObtentionRevenuConjointModif = fraisObtentionRevenuConjoint;
        }

        // Calcul des frais d'obtention du revenu des enfants
        Float fraisObtentionRevenuEnfants = new Float(0);
        // M�moriser le d�tail du calcul dans un tableau (bricolage puisque pas de stockage dynamique pr�vu au d�but du
        // projet)
        String fraisObtentionRevenuEnfantsDetailXml = "<table width='100%'>";
        String celluleAmount = "<td class='libelleMonnaie '>CHF</td><td class='montant' data-g-amountformatter='blankAsZero:false'>";
        String celluleAmountSoulignee = "<td class='libelleMonnaie souligne'>CHF</td><td class='montant souligne' data-g-amountformatter='blankAsZero:false'>";

        // R�cup�ration de la franchise � d�duire
        Float franchiseRevenuEnfant = inputCalcul.getVariableMetier(CSVariableMetier.REVENU_NET_FORMATION).getMontant();

        // Pour chaque enfant faire le calcul
        for (EnfantFamille ef : inputCalcul.getListeEnfants()) {
            Float fraisTransport = inputCalcul
                    .getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                    .getElementDepenseReconnue(DepenseReconnueType.FRAIS_TRANSPORT).getValeurModifieeTaxateur();
            Float fraisVetements = inputCalcul
                    .getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                    .getElementDepenseReconnue(DepenseReconnueType.FRAIS_VETEMENTS).getValeur();
            Float fraisRepas = inputCalcul
                    .getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                    .getElementDepenseReconnue(DepenseReconnueType.FRAIS_REPAS).getValeur();

            // Plafonnement des frais de repas
            if (fraisRepas > plafondFraisRepas) {
                fraisRepas = plafondFraisRepas;
            }

            // Addition des frais d'obtention du revenu de l'enfant
            Float fraisCetEnfant = new Float(0);
            fraisCetEnfant += fraisTransport;
            fraisCetEnfant += fraisVetements;
            fraisCetEnfant += fraisRepas;

            // Plafonnement des frais de l'enfant � concurrence du revenu de son activit�
            Float revenuEnfant = inputCalcul
                    .getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                    .getElementRevenu(RevenuType.REVENUS_ACTIVITE_ENFANT).getValeur();
            Float revenuEnfantModif = revenuEnfant - franchiseRevenuEnfant;
            if (revenuEnfantModif < 0) {
                revenuEnfantModif = new Float(0);
            }
            Float fraisCetEnfantModif = fraisCetEnfant;
            if (fraisCetEnfant > revenuEnfantModif) {
                fraisCetEnfantModif = revenuEnfantModif;
            }

            // Ajout des frais de l'enfant dans les frais de revenus de tous les enfants
            if (CSTypeGarde.GARDE_PARTAGEE.getCodeSystem().equals(ef.getSimpleEnfantFamille().getCsGarde())) {
                fraisCetEnfantModif = fraisCetEnfantModif / 2;
            }
            fraisObtentionRevenuEnfants += fraisCetEnfantModif;

            // Mise dans le tableau xml des infos
            fraisObtentionRevenuEnfantsDetailXml += "<tr><td colspan='7'>"
                    + ef.getEnfant().getMembreFamille().getPersonneEtendue().getTiers().getDesignation1()
                    + " "
                    + ef.getEnfant().getMembreFamille().getPersonneEtendue().getTiers().getDesignation2()
                    + (CSTypeGarde.GARDE_PARTAGEE.getCodeSystem().equals(ef.getSimpleEnfantFamille().getCsGarde()) ? " (1/2)"
                            : "") + "</td></tr>";
            fraisObtentionRevenuEnfantsDetailXml += "<tr><td>&nbsp;-&nbsp;Frais de repas</td>" + celluleAmount
                    + fraisRepas + "</td><td colspan='4'>&nbsp;</td></tr>";
            fraisObtentionRevenuEnfantsDetailXml += "<tr><td>&nbsp;-&nbsp;Frais de transport</td>" + celluleAmount
                    + fraisTransport + "</td><td colspan='4'>&nbsp;</td></tr>";
            fraisObtentionRevenuEnfantsDetailXml += "<tr><td>&nbsp;-&nbsp;Achats de v�tements</td>"
                    + celluleAmountSoulignee + fraisVetements + "</td>" + celluleAmount + fraisCetEnfant + "</td>"
                    + celluleAmount + fraisCetEnfantModif + "</td></tr>";
        }
        fraisObtentionRevenuEnfants = this.roundFloat(fraisObtentionRevenuEnfants);

        // Ligne de total
        fraisObtentionRevenuEnfantsDetailXml += "<tr><td colspan='7'>&nbsp;</td></tr>";
        fraisObtentionRevenuEnfantsDetailXml += "<tr><td colspan='5'>Total</td>" + celluleAmount
                + fraisObtentionRevenuEnfants + "</td></tr>";
        fraisObtentionRevenuEnfantsDetailXml += "</table>";

        // Enregistrement des r�sultats
        outputCalcul.addDonnee(OutputData.DEPENSES_FRAIS_TRANSPORT_CONJOINT, fraisTransportConjoint);
        outputCalcul.addDonnee(OutputData.DEPENSES_FRAIS_TRANSPORT_REQUERANT, fraisTransportRequerant);
        outputCalcul.addDonnee(OutputData.DEPENSES_FRAIS_TRANSPORT_CONJOINT_MODIF_TAXATEUR,
                fraisTransportConjointModifTaxateur);
        outputCalcul.addDonnee(OutputData.DEPENSES_FRAIS_TRANSPORT_REQUERANT_MODIF_TAXATEUR,
                fraisTransportRequerantModifTaxateur);
        outputCalcul.addDonnee(OutputData.DEPENSES_FRAIS_REPAS_CONJOINT, fraisRepasConjoint);
        outputCalcul.addDonnee(OutputData.DEPENSES_FRAIS_REPAS_CONJOINT_MODIF, fraisRepasConjointModif);
        outputCalcul.addDonnee(OutputData.DEPENSES_FRAIS_REPAS_REQUERANT, fraisRepasRequerant);
        outputCalcul.addDonnee(OutputData.DEPENSES_FRAIS_REPAS_REQUERANT_MODIF, fraisRepasRequerantModif);
        outputCalcul.addDonnee(OutputData.DEPENSES_FRAIS_VETEMENTS_CONJOINT, fraisVetementsConjoint);
        outputCalcul.addDonnee(OutputData.DEPENSES_FRAIS_VETEMENTS_REQUERANT, fraisVetementsRequerant);

        outputCalcul.addDonnee(OutputData.DEPENSES_FRAIS_OBTENTION_REVENU_REQUERANT, fraisObtentionRevenuRequerant);
        outputCalcul.addDonnee(OutputData.DEPENSES_FRAIS_OBTENTION_REVENU_CONJOINT, fraisObtentionRevenuConjoint);
        outputCalcul.addDonnee(OutputData.DEPENSES_FRAIS_OBTENTION_REVENU_REQUERANT_MODIF,
                fraisObtentionRevenuRequerantModif);
        outputCalcul.addDonnee(OutputData.DEPENSES_FRAIS_OBTENTION_REVENU_CONJOINT_MODIF,
                fraisObtentionRevenuConjointModif);
        outputCalcul.addDonnee(OutputData.DEPENSES_FRAIS_OBTENTION_REVENU_ENFANTS, fraisObtentionRevenuEnfants);
        outputCalcul.addDonnee(OutputData.DEPENSES_FRAIS_OBTENTION_REVENU_ENFANTS_DETAIL_XML,
                fraisObtentionRevenuEnfantsDetailXml);

        return outputCalcul;
    }

    private OutputCalcul calculerLoyerEtCharges(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {
        // R�cup�ration des donn�es pour le calcul
        Float loyerAnnuel = inputCalcul.getDonneesFinancieresRequerant()
                .getElementDepenseReconnue(DepenseReconnueType.LOYER_ANNUEL).getValeur();
        loyerAnnuel = this.roundFloat(loyerAnnuel);
        Boolean penurieLogement = inputCalcul.getDonneesFinancieresRequerant()
                .getElementDepenseReconnue(DepenseReconnueType.LOYER_ANNUEL).getPenurieLogement();
        Float chargesAnnuelles = inputCalcul.getDonneesFinancieresRequerant()
                .getElementDepenseReconnue(DepenseReconnueType.CHARGES_ANNUELLES).getValeur();
        chargesAnnuelles = this.roundFloat(chargesAnnuelles);
        Float pourcentageLoyerCharges = inputCalcul.getVariableMetier(CSVariableMetier.POURCENTAGE_LOYER_CHARGES)
                .getTaux();
        Float pourcentageMajorationLoyerPenurie = inputCalcul.getVariableMetier(
                CSVariableMetier.MAJORATION_LOYER_DEPASSANT_PENURIE).getTaux();
        Integer nbPersonnesLogement = 1;
        if (inputCalcul.getDonneesFinancieresRequerant().getElementDepenseReconnue(DepenseReconnueType.LOYER_ANNUEL)
                .getNbPersonnesLogement() == null) {
            JadeThread.logError(CalculDepensesReconnuesServiceImpl.class.getName(),
                    "perseus.calcul.donneefinanciere.nbPersonnesLoyer.mandatory");
        }
        nbPersonnesLogement = Integer.parseInt(inputCalcul.getDonneesFinancieresRequerant()
                .getElementDepenseReconnue(DepenseReconnueType.LOYER_ANNUEL).getNbPersonnesLogement());
        Integer nbPersonnesFamille = 0;
        if (JadeStringUtil.isEmpty(inputCalcul.getDemande().getSituationFamiliale().getConjoint().getId())) {
            nbPersonnesFamille = 1;
        } else {
            nbPersonnesFamille = 2;
        }
        nbPersonnesFamille += inputCalcul.getListeEnfants().size();

        // Donn�es � plafonner
        Float loyerAnnuelModif = new Float(0);
        Float chargesAnnuellesModif = new Float(0);
        String infosLocalite = "";

        // Plafonnement du loyer
        Float loyerMax = new Float(0);
        try {
            // Retrouver la localit� du tiers
            AdresseTiersDetail adresseTiersDetail = PFUserHelper.getAdresseAssure(inputCalcul.getDemande()
                    .getSituationFamiliale().getRequerant().getMembreFamille().getSimpleMembreFamille().getIdTiers(),
                    inputCalcul.getDemande().getSimpleDemande().getDateDebut());

            // TODO:Contr�ler quand la version d�finitive de pyxis sera mise en place + Utiliser PFUserHelper
            String idLocalite = adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE_ID);
            // Ajout de la localit� prise en compte dans les infos du requ�rant
            infosLocalite = adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_RUE) + " "
                    + adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NUMERO) + ", ";
            infosLocalite += adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA) + " "
                    + adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE);
            // Ajouter le nombre de personnes dans le logement
            infosLocalite += " (" + nbPersonnesLogement + " personnes dans le logement)";

            // Traitement du cas sp�cial d'un parent seul avec enfants (ajouter 1 personne dans le logement pour le
            // plafonnement)

            // BZ 8052 : Utiliser le nombre de personnes dans la sf a la place
            // du nombre de personnes dans le logement pour d�terminer le loyer max
            Integer nbPersonnesCasSpecial = nbPersonnesFamille;
            if (JadeStringUtil.isEmpty(inputCalcul.getDemande().getSituationFamiliale().getConjoint().getId())) {
                nbPersonnesCasSpecial++;
            }

            Loyer loyer = PerseusServiceLocator.getLoyerService().searchForLocalite(idLocalite, nbPersonnesCasSpecial,
                    inputCalcul.getDemande().getSimpleDemande().getDateDebut());
            loyerMax = loyer.getMontant() * 12;
        } catch (Exception e) {
            throw new CalculException("Erreur lors du plafonnement du loyer au "
                    + inputCalcul.getDemande().getSimpleDemande().getDateDebut()
                    + " NSS "
                    + inputCalcul.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel() + " : " + e.getMessage());
        }
        // Si il y'a p�nurie de logement augment� de X %
        if (penurieLogement) {
            loyerMax = loyerMax * (1 + pourcentageMajorationLoyerPenurie);
        }
        loyerMax = this.roundFloat(loyerMax);

        // Prise en compte du loyer selon le nombre de personnes
        if (nbPersonnesLogement > 0) {
            if (nbPersonnesFamille < nbPersonnesLogement) {
                loyerAnnuelModif = (loyerAnnuel / nbPersonnesLogement) * nbPersonnesFamille;
            } else {
                loyerAnnuelModif = loyerAnnuel;
            }
        } else {
            JadeThread.logError(CalculDepensesReconnuesServiceImpl.class.getName(),
                    "perseus.calcul.donneefinanciere.nbPersonnesLoyer.mandatory");
        }
        loyerAnnuelModif = this.roundFloat(loyerAnnuelModif);

        // Prise en compte des charges selon le nombre de personnes
        chargesAnnuellesModif = (chargesAnnuelles / nbPersonnesLogement) * nbPersonnesFamille;
        chargesAnnuellesModif = this.roundFloat(chargesAnnuellesModif);

        // Plafonnement du loyer annuel si c'est pas un cas de rigueur
        if ((loyerAnnuelModif > loyerMax) & !inputCalcul.getDemande().getSimpleDemande().getCalculParticulier()) {
            loyerAnnuelModif = loyerMax;
        }

        // Plafonnement des charges admises
        Float montantMaxChargesAdmises = loyerAnnuelModif * pourcentageLoyerCharges;
        if (chargesAnnuellesModif > montantMaxChargesAdmises) {
            // BZ 7111 probl�me d'arrondi
            chargesAnnuellesModif = this.roundFloat(montantMaxChargesAdmises);
        }

        // Enregistrement des donn�es
        outputCalcul.addDonnee(OutputData.DEPENSES_LOYER_ANNUEL, loyerAnnuel);
        outputCalcul.addDonnee(OutputData.DEPENSES_LOYER_ANNUEL_MODIF, loyerAnnuelModif);
        outputCalcul.addDonnee(OutputData.DEPENSES_CHARGES_ANNUELLES, chargesAnnuelles);
        outputCalcul.addDonnee(OutputData.DEPENSES_CHARGES_ANNUELLES_MODIF, chargesAnnuellesModif);
        outputCalcul.addDonnee(OutputData.ENTETE_LOCALITE, infosLocalite);

        return outputCalcul;
    }

    private OutputCalcul calculerPensionsAlimentairesPayees(InputCalcul inputCalcul, OutputCalcul outputCalcul)
            throws CalculException {
        // R�cup�rer les donn�es
        Float pensionAlimentaireVersee = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementDepenseReconnue(DepenseReconnueType.PENSION_ALIMENTAIRE_VERSEE).getValeur();
        pensionAlimentaireVersee = this.roundFloat(pensionAlimentaireVersee);

        outputCalcul.addDonnee(OutputData.DEPENSES_PENSION_ALIMENTAIRE_VERSEE, pensionAlimentaireVersee);

        return outputCalcul;
    }

}
