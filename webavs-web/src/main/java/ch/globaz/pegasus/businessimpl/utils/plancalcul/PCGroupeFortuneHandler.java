/**
 *
 */
package ch.globaz.pegasus.businessimpl.utils.plancalcul;

import java.util.ArrayList;

import ch.globaz.eavs.utils.StringUtils;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

/**
 * @author SCE
 *
 *         8 nov. 2010
 */
public class PCGroupeFortuneHandler extends PCGroupeAbstractHandler {
    private static final String SOUSTRACTION = "-";
    /************************ CONSTANTES PAR CATEGORIE ***********************************************/
    /* Groupe Fortune (FOR), categorie autres dettees prouvées */
    String[] FOR_AUTRES_DETTES_PROUVEES = {IPCValeursPlanCalcul.CLE_FORTU_AUT_DETT_TOTAL};
    /* Groupe Fortune (FOR), categorie Deductions legales */
    String[] FOR_DEDUCTION_LEGALES = {IPCValeursPlanCalcul.CLE_FORTU_DED_LEGA_TOTAL};
    /* Groupe Fortune (FOR), categorie Dettes hypothécaire */
    String[] FOR_DETTES_HYPOTHECAIRE = {IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_TOTAL};
    /* Groupe Fortune (FOR), categorie fortune dessaisie */
    String[] FOR_FORTUNE_DESSAISIE = {IPCValeursPlanCalcul.CLE_FORTU_FOR_DESS_TOTAL};
    /* Groupe Fortune (FOR), categorie fortune imobiliere */
    String[] FOR_FORTUNE_IMOBILIERE = {IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_TOTAL,
            IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_IMMO_HABIT_PRINCIPALE,
            IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABIT_PRINCIPALE,
            IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABITABLES,
            IPCValeursPlanCalcul.CLE_FORTU_SOUS_TOTAL};
    /* Groupe Fortune (FOR), categorie fortune mobiliere */
    String[] FOR_FORTUNE_MOBILIERE = {IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_TOTAL,
            IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_NUMERAIRES, IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_TABLEAUX,
            IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_BIJOUX, IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_METAUX_PRECIEUX,
            IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_TIMBRES, IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_AUTRE,
            IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_COMPTE_BANCAIRE, IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_TITRES,
            IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_PRET_ENVERS_TIERS,
            IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_CAPITAL_LPP, IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_ASSURANCE_VIE,
            IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_ASSURANCE_RENTE_VIAGERE,
            IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_MARCHANDISES_STOCK, IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_BETAIL,
            IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_VEHICULES};
    /* Groupe Fortune (FOR), categorie fortune nettes */
    String[] FOR_FORTUNE_NETTE = {IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL, IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL_PART};

    /************************ FIN CONSTANTES CATEGORIE ***********************************************/

    /**
     * Constructeur. Initilaise le champ tupleRoot, avec l'instance passé en paramètre
     *
     * @param tupleRoot
     *            , objet TupleDonneeRapport
     */
    public PCGroupeFortuneHandler(TupleDonneeRapport tupleRoot) {
        super(tupleRoot);
    }

    /**
     * Traitement de biens immobiiers servant habitation principal avec ligne deduction forfaitaire
     */
    private PCLignePlanCalculHandler[] createLignesForBienImmoPrincipalReforme() {
        // Tableau de retour
        PCLignePlanCalculHandler[] tabBiensImmo = new PCLignePlanCalculHandler[3];
        // Récupération des montants (Valeur,Déduction Forfaitaire et dette hypothècaire)
        String csValeurImmo = IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_IMMO_HABIT_PRINCIPALE;
        String csValeurDeduFor = IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_DEDUCTION_FOFAITAIRE;
        String csValeurHypo = IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_REAL_PROPERTY;
        String csValeurHypoForLegende = IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_TOTAL;
        String csValeurTotal = IPCValeursPlanCalcul.CLE_INTER_FORTUNE_IMMOBILIER_TOTAL_PRINCIPAL;
        Float valeurImmobilier = getValeur(csValeurImmo);
        Float valeurDeduction = getValeur(csValeurDeduFor);
        Float valeurDetteHypo = getValeur(csValeurHypo);
        Float valeurTotal = getValeur(csValeurTotal);

        // Préparation des containers pour les lignes
        PCValeurPlanCalculHandler valeurForBienImmoMobil = null;
        PCValeurPlanCalculHandler valeurPlanDeductionForfaitaire = null;
        PCValeurPlanCalculHandler valeurPlanDetteHypo = null;
        PCValeurPlanCalculHandler valeurPlanTotal = null;

        valeurForBienImmoMobil = createValeurPlanCalcul(csValeurImmo, valeurImmobilier.toString(), ADDITION,
                NO_CSS_CLASS);
        if (valeurDetteHypo != 0.f) {
            valeurPlanDeductionForfaitaire = createValeurPlanCalcul(csValeurDeduFor, valeurDeduction.toString(), SOUSTRACTION,
                    NO_CSS_CLASS);
            valeurPlanDetteHypo = createValeurPlanCalcul(csValeurHypoForLegende, valeurDetteHypo.toString(), SOUSTRACTION,
                    CSS_SOULIGNE);
        } else {
            valeurPlanDeductionForfaitaire = createValeurPlanCalcul(csValeurDeduFor, valeurDeduction.toString(), SOUSTRACTION,
                    CSS_SOULIGNE);
        }
        valeurPlanTotal = createValeurPlanCalcul(csValeurTotal, valeurTotal.toString(), ADDITION,
                CSS_SOULIGNE);

        //Créations des lignes dans le plan de calcul pour le bien immobilier
        PCLignePlanCalculHandler ligneForBienImmo = null;
        PCLignePlanCalculHandler ligneForBienImmoDeduit = null;
        PCLignePlanCalculHandler ligneForBienImmoHypo = null;
        ligneForBienImmo = createLignePlanCalcul(csValeurImmo, getLegende(csValeurImmo), valeurForBienImmoMobil,
                VALEUR_VIDE, VALEUR_VIDE);
        if (valeurPlanDetteHypo != null) {
            ligneForBienImmoDeduit = createLignePlanCalcul(csValeurDeduFor, getLegende(csValeurDeduFor), valeurPlanDeductionForfaitaire,
                    VALEUR_VIDE, VALEUR_VIDE);
            ligneForBienImmoHypo = createLignePlanCalcul(csValeurHypoForLegende, getLegende(csValeurHypo), valeurPlanDetteHypo,
                    valeurPlanTotal, VALEUR_VIDE);
        } else {
            ligneForBienImmoDeduit = createLignePlanCalcul(csValeurDeduFor, getLegende(csValeurDeduFor), valeurPlanDeductionForfaitaire,
                    valeurPlanTotal, VALEUR_VIDE);
        }
        if (ligneForBienImmoDeduit == null && ligneForBienImmoHypo == null) {
            tabBiensImmo = new PCLignePlanCalculHandler[1];
            tabBiensImmo[0] = ligneForBienImmo;
        } else if (ligneForBienImmoHypo == null) {
            tabBiensImmo = new PCLignePlanCalculHandler[2];
            tabBiensImmo[0] = ligneForBienImmo;
            tabBiensImmo[1] = ligneForBienImmoDeduit;
        } else {
            tabBiensImmo = new PCLignePlanCalculHandler[3];
            tabBiensImmo[0] = ligneForBienImmo;
            tabBiensImmo[1] = ligneForBienImmoDeduit;
            tabBiensImmo[2] = ligneForBienImmoHypo;
        }

        return tabBiensImmo;
    }

    private PCLignePlanCalculHandler[] createLignesForBienImmoSecondaireReforme() {
        // Tableau de retour
        PCLignePlanCalculHandler[] tabBiensImmo = null;
        // Récupération des montants (Valeur,Déduction Forfaitaire et dette hypothècaire)
        String csValeurImmo = IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABIT_PRINCIPALE;
        String csValeurHypo = IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_SELF_INHABITED;
        String csValeurHypoForLegende = IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_TOTAL;
        String csValeurTotal = IPCValeursPlanCalcul.CLE_INTER_FORTUNE_IMMOBILIER_TOTAL_SECONDAIRE;
        Float valeurImmobilier = getValeur(csValeurImmo);
        Float valeurDetteHypo = getValeur(csValeurHypo);
        Float valeurTotal = getValeur(csValeurTotal);

        // Préparation des containers pour les lignes
        PCValeurPlanCalculHandler valeurForBienImmoMobil = null;
        PCValeurPlanCalculHandler valeurPlanDetteHypo = null;
        PCValeurPlanCalculHandler valeurPlanTotal = null;
        if (valeurDetteHypo != 0.f) {
            valeurForBienImmoMobil = createValeurPlanCalcul(csValeurImmo, valeurImmobilier.toString(), ADDITION,
                    NO_CSS_CLASS);
            valeurPlanDetteHypo = createValeurPlanCalcul(csValeurHypoForLegende, valeurDetteHypo.toString(), SOUSTRACTION,
                    CSS_SOULIGNE);
        } else {
            valeurForBienImmoMobil = createValeurPlanCalcul(csValeurImmo, valeurImmobilier.toString(), ADDITION,
                    CSS_SOULIGNE);
        }
        valeurPlanTotal = createValeurPlanCalcul(csValeurTotal, valeurTotal.toString(), ADDITION,
                CSS_SOULIGNE);
        //Créations des lignes dans le plan de calcul pour le bien immobilier
        PCLignePlanCalculHandler ligneForBienImmo = null;
        PCLignePlanCalculHandler ligneForBienImmoHypo = null;

        if (valeurPlanDetteHypo != null) {
            ligneForBienImmo = createLignePlanCalcul(csValeurImmo, getLegende(csValeurImmo), valeurForBienImmoMobil,
                    VALEUR_VIDE, VALEUR_VIDE);
            ligneForBienImmoHypo = createLignePlanCalcul(csValeurHypoForLegende, getLegende(csValeurHypo), valeurPlanDetteHypo,
                    valeurPlanTotal, VALEUR_VIDE);
        } else {
            ligneForBienImmo = createLignePlanCalcul(csValeurImmo, getLegende(csValeurImmo), valeurForBienImmoMobil,
                    valeurPlanTotal, VALEUR_VIDE);
        }

        if (ligneForBienImmoHypo != null) {
            tabBiensImmo = new PCLignePlanCalculHandler[2];
            tabBiensImmo[0] = ligneForBienImmo;
            tabBiensImmo[1] = ligneForBienImmoHypo;
        } else {
            tabBiensImmo = new PCLignePlanCalculHandler[1];
            tabBiensImmo[0] = ligneForBienImmo;
        }
        return tabBiensImmo;
    }

    private PCLignePlanCalculHandler[] createLignesForBienImmoNonHabitableReforme() {
        // Tableau de retour
        PCLignePlanCalculHandler[] tabBiensImmo;
        // Récupération des montants (Valeur,Déduction Forfaitaire et dette hypothècaire)
        String csValeurImmo = IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABITABLES;
        String csValeurHypo = IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_NOT_HABITED;
        String csValeurHypoForLegende = IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_TOTAL;
        String csValeurTotal = IPCValeursPlanCalcul.CLE_INTER_FORTUNE_IMMOBILIER_TOTAL_NON_HABITABLE;
        Float valeurImmobilier = getValeur(csValeurImmo);
        Float valeurDetteHypo = getValeur(csValeurHypo);
        Float valeurTotal = getValeur(csValeurTotal);

        // Préparation des containers pour les lignes
        PCValeurPlanCalculHandler valeurForBienImmoMobil = null;
        PCValeurPlanCalculHandler valeurPlanDetteHypo = null;
        PCValeurPlanCalculHandler valeurPlanTotal = null;
        if (valeurDetteHypo != 0.f) {
            valeurForBienImmoMobil = createValeurPlanCalcul(csValeurImmo, valeurImmobilier.toString(), ADDITION,
                    NO_CSS_CLASS);
            valeurPlanDetteHypo = createValeurPlanCalcul(csValeurHypoForLegende, valeurDetteHypo.toString(), SOUSTRACTION,
                    CSS_SOULIGNE);
        } else {
            valeurForBienImmoMobil = createValeurPlanCalcul(csValeurImmo, valeurImmobilier.toString(), ADDITION,
                    CSS_SOULIGNE);
        }
        valeurPlanTotal = createValeurPlanCalcul(csValeurTotal, valeurTotal.toString(), ADDITION,
                NO_CSS_CLASS);
        //Créations des lignes dans le plan de calcul pour le bien immobilier
        PCLignePlanCalculHandler ligneForBienImmo = null;
        PCLignePlanCalculHandler ligneForBienImmoHypo = null;

        if (valeurPlanDetteHypo != null) {
            ligneForBienImmo = createLignePlanCalcul(csValeurImmo, getLegende(csValeurImmo), valeurForBienImmoMobil,
                    VALEUR_VIDE, VALEUR_VIDE);
            ligneForBienImmoHypo = createLignePlanCalcul(csValeurHypoForLegende, getLegende(csValeurHypoForLegende), valeurPlanDetteHypo,
                    valeurPlanTotal, VALEUR_VIDE);
        } else {
            ligneForBienImmo = createLignePlanCalcul(csValeurImmo, getLegende(csValeurImmo), valeurForBienImmoMobil,
                    valeurPlanTotal, VALEUR_VIDE);
        }

        if (ligneForBienImmoHypo != null) {
            tabBiensImmo = new PCLignePlanCalculHandler[2];
            tabBiensImmo[0] = ligneForBienImmo;
            tabBiensImmo[1] = ligneForBienImmoHypo;
        } else {
            tabBiensImmo = new PCLignePlanCalculHandler[1];
            tabBiensImmo[0] = ligneForBienImmo;
        }
        return tabBiensImmo;
    }

    /**
     * Traitement de la categorie des autres dettes prouvées Non obligatoire
     */
    private void dealCategorieAutresDettesProuvees() {
        String[] tabCategorie = FOR_AUTRES_DETTES_PROUVEES;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        String legende = getLegende(cs);
        Float val = getValeur(cs);// recup valeur

        if (val != 0f) {
            // Ajout de la ligne
            PCValeurPlanCalculHandler valeur = createValeurPlanCalcul(cs, val.toString(),
                    PCGroupeFortuneHandler.SOUSTRACTION, NO_CSS_CLASS);
            groupList.add(createLignePlanCalcul(cs, legende, VALEUR_VIDE, VALEUR_VIDE, valeur));
        }
    }

    /**
     * Traitement de la catgorie des dette hypothecaires Non obligatoire
     */
    private void dealCategorieDettesHypothecaire() {
        String[] tabCategorie = FOR_DETTES_HYPOTHECAIRE;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        String legende = getLegende(cs);
        Float val = getValeur(cs);// recup valeur
        if (val != 0f) {
            // Ajout de la ligne
            PCValeurPlanCalculHandler valeur = createValeurPlanCalcul(cs, val.toString(),
                    PCGroupeFortuneHandler.SOUSTRACTION, NO_CSS_CLASS);
            groupList.add(createLignePlanCalcul(cs, legende, VALEUR_VIDE, VALEUR_VIDE, valeur));

        }
    }

    /**
     * Traitement de la categorie des deductions legales Non obligatoire
     */
    private void dealCategorieForDeductionLegales() {

        String[] tabCategorie = FOR_DEDUCTION_LEGALES;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        String legende = getLegende(cs);
        Float val = getValeur(cs);// recup valeur
        if (val != 0f) {
            // Ajout de la ligne
            PCValeurPlanCalculHandler valeur = createValeurPlanCalcul(cs, val.toString(),
                    PCGroupeFortuneHandler.SOUSTRACTION, CSS_SOULIGNE);
            groupList.add(createLignePlanCalcul(cs, legende, VALEUR_VIDE, VALEUR_VIDE, valeur));
        }
    }

    /**
     * Traitement categorie fortune dessaisie, Obligatoire, 1 valeur categorie
     */
    private void dealCategorieFortuneDessaisie() {
        String[] tabCategorie = FOR_FORTUNE_DESSAISIE;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        String legende = getLegende(cs);
        Float val = getValeur(cs);// recup valeur
        // Ajout de la ligne
        groupList.add(createLigneForGroupeList(cs, legende, val, 2));
    }

    /**
     * Traitement de la categorie des fortunes immobilieres Obligatoire
     * Si il existe des biens immobiliers, on parcours les données, sinon on affiche uniquement la ligne du bien immobiliers principale.
     */
    private void dealCategorieFortuneImmobiliereReforme() {
        int lastValueInList = 0;
        boolean hasBienNonZero = false;
        String[] tabCategorie = FOR_FORTUNE_IMOBILIERE;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        String legende = getLegende(cs);
        Float val = getValeur(cs);// recup valeur

        // on itere sur les membres
        for (int cpt = 1; cpt < tabCategorie.length; cpt++) {
            String csMembre = tabCategorie[cpt];
            Float valMembre = getValeur(csMembre);
            //Vérifier si il y'a des montants dans un des 3 type de biens immobiliers.
            if (valMembre != 0f) {
                /**
                 * Préparation des lignes dans le plan de calcul selon le type de bien immobilier (principale,secondaire ou non habitable)
                 */
                PCLignePlanCalculHandler[] lignesAajouter = null;
                switch (csMembre) {
                    case IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_IMMO_HABIT_PRINCIPALE:
                        lignesAajouter = createLignesForBienImmoPrincipalReforme();
                        break;
                    case IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABIT_PRINCIPALE:
                        lignesAajouter = createLignesForBienImmoSecondaireReforme();
                        break;
                    case IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABITABLES:
                        lignesAajouter = createLignesForBienImmoNonHabitableReforme();
                        break;
                }
                /**
                 * Ajout dans le tableau.
                 */
                if (lignesAajouter != null) {
                    for (int i = 0; i < lignesAajouter.length; i++) {
                        groupList.add(lignesAajouter[i]);
                    }
                    hasBienNonZero = true;
                }
            }
        }
        //Si il y'a pas de biens, alors on affiche la ligne du bien principal.
        if (!hasBienNonZero) {
            float valeurHabitatPrincipal = getValeur(FOR_FORTUNE_IMOBILIERE[1]);
            // Si présent on le traite en dessousde zéro
            PCLignePlanCalculHandler[] lignesAajouter = null;
            lignesAajouter = createLignesForBienImmoPrincipal();
            if (lignesAajouter != null) {
                for (int i = 0; i < lignesAajouter.length; i++) {
                    groupList.add(lignesAajouter[i]);
                }
            }
        }
        if (lastValueInList == 0) {
            lastValueInList = groupList.size() - 1;
        }
        // Valeur sous total (= Total de la fortune nette)
        cs = IPCValeursPlanCalcul.CLE_FORTU_SOUS_TOTAL;
        val = getValeur(cs);
        PCValeurPlanCalculHandler valeurSousTotal = createValeurPlanCalcul(cs, val.toString(), ADDITION, NO_CSS_CLASS);

        groupList.get(lastValueInList).setValCol3(valeurSousTotal);
        groupList.get(lastValueInList).getValCol2().setCssClass(CSS_SOULIGNE);
    }


    /**
     * Traitement de la categorie fortune mobiliere, Obligatoire
     *
     * @return ArrayList de PCLignePlanCalculHelper
     */
    private void dealCategorieFortuneMobiliere() {
        String[] tabCategorie = FOR_FORTUNE_MOBILIERE;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        String legende = getLegende(cs);
        Float val = getValeur(cs);// recup valeur
        // Si valeur minimale on l'affiche de toute facon

        // SI valeur diff de 0
        if (getValeur(cs) != 0f) {
            // on traite les membres si il y en a
            if (tabCategorie.length > 1) {
                // iteration sur les membres
                for (int cpt = 1; cpt < tabCategorie.length; cpt++) {
                    String csMembre = tabCategorie[cpt];
                    String legendeMembre = getLegende(cs);
                    Float valMembre = getValeur(csMembre);
                    // si valeur != 0, on traite
                    if (valMembre != 0f) {
                        groupList.add(createLigneForGroupeList(csMembre, legendeMembre, valMembre, 2));
                    }
                }
            } else// si pas de membre on ajoute la categorie
            {
                groupList.add(createLigneForGroupeList(cs, legende, val, 2));
            }
        } else// Si valeur 0, on ajoute
        {
            groupList.add(createLigneForGroupeList(cs, legende, val, 2));
        }
    }

    /**
     * Traitement du bloc total, obligatoire
     */
    private void dealCategorieTotal() {
        String[] tabCategorie = FOR_FORTUNE_NETTE;

        String cs = tabCategorie[0];
        String legende = getLegende(cs);
        Float val = getValeur(cs);

        String cs2 = tabCategorie[1];
        String legende2 = getLegende(cs2);

        PCValeurPlanCalculHandler valeur = createValeurPlanCalcul(cs, val.toString(), ADDITION, NO_CSS_CLASS);
        PCLignePlanCalculHandler ligneTotal;
        if (StringUtils.isBlank(legende2)) {
            ligneTotal = createLignePlanCalcul(cs, legende, VALEUR_VIDE, VALEUR_VIDE, valeur);
        } else {
            ligneTotal = createLignePlanCalcul(cs2, legende2, VALEUR_VIDE, VALEUR_VIDE, valeur);
        }
        ligneTotal.setCssClass("total");
        groupList.add(ligneTotal);
    }

    /**
     * Génération des lignes à afficher dans la page jsp
     */
    private void generateLigneProcess() {
        dealCategorieFortuneMobiliere();
        dealCategorieFortuneDessaisie();
        if (isReforme()) {
            dealCategorieFortuneImmobiliereReforme();
        } else {
            dealCategorieFortuneImmobiliere();
        }
        // Ajout ligne vide
        groupList.add(setEmptyLigne());
        if (!isReforme()) {
            dealCategorieDettesHypothecaire();
        }
        dealCategorieAutresDettesProuvees();
        dealCategorieForDeductionLegales();
        // Ajout ligne vide
        groupList.add(setEmptyLigne());
        dealCategorieTotal();
    }

    private void dealCategorieFortuneImmobiliere() {
        int lastValueInList = 0;
        String[] tabCategorie = FOR_FORTUNE_IMOBILIERE;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        String legende = getLegende(cs);
        Float val = getValeur(cs);// recup valeur
        // Si cat diff 0, itere sur les membre
        if (val != 0f) {
            // on itere sur les membres
            for (int cpt = 1; cpt < tabCategorie.length; cpt++) {
                String csMembre = tabCategorie[cpt];
                String legendeMembre = getLegende(csMembre);
                Float valMembre = getValeur(csMembre);
                // si valeur != 0, et BISHP on traite les deux lignes avec
                // deduction forfaitaire
                if ((valMembre != 0f)
                        && csMembre.equals(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_IMMO_HABIT_PRINCIPALE)) {
                    // on stocke la ligne, pour la mettre a jour ensuite avec le
                    // sous total et l'ajouter
                    groupList.add(createLignesForBienImmoPrincipal()[0]);
                    groupList.add(createLignesForBienImmoPrincipal()[1]);

                } else {
                    if (csMembre.equals(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABIT_PRINCIPALE)
                            && (getValeur(csMembre) != 0f)) {
                        groupList.add(createLigneForGroupeList(csMembre, legendeMembre, valMembre, 2));
                        lastValueInList = groupList.size() - 1;
                    }
                    if (csMembre.equals(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABITABLES)
                            && (getValeur(csMembre) != 0f)) {
                        groupList.add(createLigneForGroupeList(csMembre, legendeMembre, valMembre, 2));
                        lastValueInList = groupList.size() - 1;
                    }
                }
            }
        } else// Sinon on affiche, cat obligatoire
        {
            float valeurHabitatPrincipal = getValeur(FOR_FORTUNE_IMOBILIERE[1]);
            // Si présent on le traite en dessousde zéro
            if (valeurHabitatPrincipal != 0) {
                groupList.add(createLignesForBienImmoPrincipal()[0]);
                groupList.add(createLignesForBienImmoPrincipal()[1]);
            } else {
                groupList.add(createLigneForGroupeList(cs, legende, val, 2));
                lastValueInList = groupList.size() - 1;
            }

        }
        if (lastValueInList == 0) {
            lastValueInList = groupList.size() - 1;
        }
        // Valeur sous total
        cs = IPCValeursPlanCalcul.CLE_FORTU_SOUS_TOTAL;
        val = getValeur(cs);
        PCValeurPlanCalculHandler valeurSousTotal = createValeurPlanCalcul(cs, val.toString(), ADDITION, NO_CSS_CLASS);

        groupList.get(lastValueInList).setValCol3(valeurSousTotal);
        groupList.get(lastValueInList).getValCol2().setCssClass(CSS_SOULIGNE);
    }

    private PCLignePlanCalculHandler[] createLignesForBienImmoPrincipal() {
        // Tableau de retour
        PCLignePlanCalculHandler[] tabBiensImmo = new PCLignePlanCalculHandler[2];
        // Valeur et ligne bien immo
        String cs = IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_IMMO_HABIT_PRINCIPALE;
        String legende = getLegende(cs);
        Float val = getValeur(cs);
        // Valeur
        PCValeurPlanCalculHandler valeurForBienImmoMobil = createValeurPlanCalcul(cs, val.toString(), ADDITION,
                NO_CSS_CLASS);
        // Ligne
        PCLignePlanCalculHandler ligneForBienImmo = createLignePlanCalcul(cs, legende, valeurForBienImmoMobil,
                VALEUR_VIDE, VALEUR_VIDE);
        // Ajout de la ligne dans le tableau de retour
        tabBiensImmo[0] = ligneForBienImmo;

        // Valeur et ligne deduction fofaitaire
        cs = IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_DEDUCTION_FOFAITAIRE;
        val = getValeur(cs);
        PCValeurPlanCalculHandler valeurForBienImmoDedForf = createValeurPlanCalcul(cs, val.toString(),
                PCGroupeFortuneHandler.SOUSTRACTION, CSS_SOULIGNE);

        // Valeur deduite
        cs = IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_PRINCIPAL_DEDUIT;
        val = getValeur(cs);
        // if(val<0f){
        // val = 0f;
        // }
        PCValeurPlanCalculHandler valeurForBienImmoDeduit = createValeurPlanCalcul(cs, val.toString(), ADDITION,
                NO_CSS_CLASS);

        // Ligne
        PCLignePlanCalculHandler ligneForBienImmoDeduit = createLignePlanCalcul(cs, legende, valeurForBienImmoDedForf,
                valeurForBienImmoDeduit, VALEUR_VIDE);
        // Ajout ligne dans tableu retour
        tabBiensImmo[1] = ligneForBienImmoDeduit;
        return tabBiensImmo;
    }

    /**
     * Retourne la liste des lignes qui seront affichées dans la page JSP
     *
     * @return the groupList
     */
    public ArrayList<PCLignePlanCalculHandler> getGroupList() {
        // Lancement du traitement et remplisage de la liste
        generateLigneProcess();
        return groupList;
    }

}
