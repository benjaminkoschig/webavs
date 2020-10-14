/**
 *
 */
package ch.globaz.pegasus.businessimpl.utils.plancalcul;

import java.util.ArrayList;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.PegasusCalculUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import org.apache.commons.lang.StringUtils;

/**
 * @author SCE
 *
 *         10 nov. 2010
 */
public class PCGroupeRevenusHandler extends PCGroupeAbstractHandler {

    private static final String SOUSTRACTION = "-";
    Boolean isCoupleSepareParMaladie = false;

    /************************ CONSTANTES PAR CATEGORIE ***********************************************/
    String[] REV_ALLOCATION_IMPOTENT = {IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_AVS_AI, IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_LAA,
            IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_LAM, IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_AUTRE_API};
    String[] REV_AUTRES_RENTES = {IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_LAA, IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_LPP,
            IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_ETRANGERE,
            IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_ASSURANCE_PRIVEE,
            IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_3EME_PILLIER,
            IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_LAM, IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_AUTRE_RENTE,
            IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_ASSURANCE_RENTE_VIAGERE};

    String[] REV_AUTRES_REVENUS = {IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJAI, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_CHOMAGE,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LAA, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LAM,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LAMAL, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_APG,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_AUTRE_IJ, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LCA,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_ALLOCATIONS_FAMILLIALES,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_PENSION_ALIM_RECUE,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_CONTRAT_ENTRETIEN_VIAGER,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_AUTRE_REVENUS};
    String[] REV_DESSAISISSEMENT_REVENUS = {IPCValeursPlanCalcul.CLE_REVEN_DESS_REV_TOTAL};
    // Header categorie
    // String[] REV_HEADER_CAT = { IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_TOTAL,
    // IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_TOTAL, IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_TOTAL,
    // IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL, IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_TOTAL,
    // IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_TOTAL, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_TOTAL };
    // Header minimale
    // String[] REV_HEADER_MINIMAL = { IPCValeursPlanCalcul.CLE_REVEN_IMP_FORT_TOTAL,
    // IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_TOTAL, IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_TOTAL,
    // IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL, IPCValeursPlanCalcul.CLE_REVEN_RENAVSAI_TOTAL,
    // IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_TOTAL, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_TOTAL,
    // // IPCValeursPlanCalcul.CLE_REVENU_TOTAL_DESSAISI
    // IPCValeursPlanCalcul.CLE_REVEN_REV_DETE_TOTAL };
    // Constante PAR CATEGORIE
    String[] REV_IMPUTATION_FORTUNE_NETTE = {IPCValeursPlanCalcul.CLE_REVEN_IMP_FORT_TOTAL};

    String[] REV_SUBSIDE_ASSURANCE_MALADIE = {IPCValeursPlanCalcul.CLE_REVEN_SUBSIDE_ASSURANCE_MALADIE_TOTAL};

    String[] REV_INTERET_FORTUNE_DESSAISI = {IPCValeursPlanCalcul.CLE_REVEN_INTDESFO_TOTAL};
    String[] REV_INTERET_FORTUNE_MOBILIERE = {IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_INTERETS_NUMERAIRES,
            IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_INTERETS_COMPTES_BANCAIRE,
            IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_RENDEMENT_TITRES,
            IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_INTERETS_PRETS,
            IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_INTERETS_CAPITAL_LPP};

    String[] REV_LOYER = {IPCValeursPlanCalcul.CLE_REVEN_LOYER_SOUS_LOCATION_NET};
    String[] REV_RENTES_AVS_AI = {IPCValeursPlanCalcul.CLE_REVEN_RENAVSAI_TOTAL};
    String[] REV_REVENU_ACTIVITE_LUCRATIVE = {
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_AGRICOLE,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_HYPOTHETIQUE,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_SOCIALES,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_LPP,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_FRAIS_OBTENTION_REVENU,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_FORFAITAIRE_REVENU,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIS_EN_COMPTE};

    String[] REV_REVENU_ACTIVITE_LUCRATIVE_CONJOINT = {
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL_CONJOINT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_CONJOINT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_AGRICOLE_CONJOINT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE_CONJOINT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_HYPOTHETIQUE_CONJOINT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_SOCIALES_CONJOINT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_LPP_CONJOINT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_FRAIS_OBTENTION_REVENU_CONJOINT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE_CONJOINT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIS_EN_COMPTE_CONJOINT};
    String[] DERNIERE_LIGNE_REVENU_ACTIVITE_LUCRATIVE_CONJOINT = {
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_CONJOINT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_AGRICOLE_CONJOINT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE_CONJOINT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_HYPOTHETIQUE_CONJOINT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_SOCIALES_CONJOINT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_LPP_CONJOINT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_FRAIS_OBTENTION_REVENU_CONJOINT};

    String[] REV_REVENU_ACTIVITE_LUCRATIVE_ENFANT = {
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL_ENFANT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_ENFANT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_AGRICOLE_ENFANT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE_ENFANT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_HYPOTHETIQUE_ENFANT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_SOCIALES_ENFANT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_LPP_ENFANT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_FRAIS_OBTENTION_REVENU_ENFANT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE_ENFANT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIS_EN_COMPTE_ENFANT};
    String[] DERNIERE_LIGNE_REVENU_ACTIVITE_LUCRATIVE_ENFANT = {
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_ENFANT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_AGRICOLE_ENFANT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE_ENFANT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_HYPOTHETIQUE_ENFANT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_SOCIALES_ENFANT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_LPP_ENFANT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_FRAIS_OBTENTION_REVENU_ENFANT};

    String[] REV_REVENUS_FORTUNE_IMMOBILIERE = {IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_VALEUR_LOCATIVE,
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_DROIT_HABITATION,
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_REVENUS_LOCATIONS,
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_REVENUS_SOUS_LOCATIONS,
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_BIENS_IMMO_NON_HABITABLES};
    String[] REV_TOTAL_PERSONNE_PROPRE = {IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_VALEUR_LOCATIVE,
            IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_AVS_AI, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LCA};// ,
    // IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_CONTRAT_ENTRETIEN_VIAGER };

    /************************ FIN CONSTANTES CATEGORIE ***********************************************/

    String[] REV_TOTAL_REVENUS_DETERMINANTS = {IPCValeursPlanCalcul.CLE_REVEN_REV_DETE_TOTAL};

    /**
     * Constructeur avec tupleroot en parametre
     *
     * @throws CalculException
     */
    public PCGroupeRevenusHandler(TupleDonneeRapport tupleRoot) throws CalculException {
        super(tupleRoot);
        isCoupleSepareParMaladie = PegasusCalculUtil.isCoupleSepareParMaladie(tupleRoot);
    }

    /**
     * Traitement de la categorie allocation impotent
     */
    private void dealCategorieAllocationsImpotents() {
        String[] tabCategorie = REV_ALLOCATION_IMPOTENT;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        String legende = getLegende(cs);
        // SI valeur diff de 0
        if (getValeur(cs) != 0f) {
            // on traite les membres si il y en a
            // iteration sur les membres
            for (int cpt = 1; cpt < tabCategorie.length; cpt++) {
                String csMembre = tabCategorie[cpt];
                Float valMembre = getValeur(csMembre);

                if (!isCoupleSepareParMaladie && (valMembre != 0f)) {
                    groupList.add(createLigneForGroupeList(csMembre, legende, valMembre, 2));

                } else {// Sinon si couple séparé, et clé autre que propre et revenu diff de 0
                    if (!csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_AVS_AI) && (valMembre != 0f)) {
                        groupList.add(createLigneForGroupeList(csMembre, legende, valMembre, 2));
                    }
                }
                // si valeur != 0, on traite
                // if (valMembre != 0f) {
                // this.groupList.add(this.createLigneForGroupeList(csMembre, legende, valMembre, 2));
                // }
            }
        }
    }

    /**
     * Traitement de la categorie autre rentes, obligatoire
     */
    private void dealCategorieAutresRentes() {
        String[] tabCategorie = REV_AUTRES_RENTES;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        // Gestion autre rente texte libre
        String legende = getLegende(cs);
        // .values();
        // TupleDonneeRapport g = (TupleDonneeRapport) listeEnfants.get(0);
        // String test = g.getLegende();
        Float val = getValeur(cs);// recup valeur
        // SI valeur diff de 0
        if (getValeur(cs) != 0f) {
            // on traite les membres si il y en a

            // iteration sur les membres
            for (int cpt = 1; cpt < tabCategorie.length; cpt++) {

                String csMembre = tabCategorie[cpt];
                String legCsMembre = getLegende(csMembre);
                Float valMembre = getValeur(csMembre);

                // Gestion libelle autre texte libre, cas des autres rentes
                /*
                 * if (csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_AUTRE_RENTE) && (valMembre != 0f)) { Map
                 * listeEnfants = this.tupleRoot.getOrCreateEnfant(csMembre).getEnfants();// .values(); Collection
                 * values = listeEnfants.values(); TupleDonneeRapport sousTuple = (TupleDonneeRapport)
                 * values.toArray()[0]; legCsMembre = PRStringUtils.replaceString(legCsMembre,
                 * PCGroupeRevenusHandler.AUTRE_RENTE_STRING_TO_REPLACE, sousTuple.getLegende()); }
                 */

                // String test = (String) values.toArray()[0];
                // String legCsMembre = this.getLegende(csMembre);

                // si valeur != 0, on traite
                if (valMembre != 0f) {
                    groupList.add(createLigneForGroupeList(csMembre, legCsMembre, valMembre, 2));
                }
            }

        } else// Si valeur 0, on ajoute
        {
            groupList.add(createLigneForGroupeList(cs, legende, val, 2));
        }
    }

    /**
     * Traitement de la categorie autre revenus, obligatoire
     */
    private void dealCategorieAutresRevenus() {
        String[] tabCategorie = REV_AUTRES_REVENUS;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        String legende = getLegende(cs);
        Float val = getValeur(cs);// recup valeur
        // SI valeur diff de 0
        if (getValeur(cs) != 0f) {
            // on traite les membres si il y en a

            // iteration sur les membres
            for (int cpt = 1; cpt < tabCategorie.length; cpt++) {
                String csMembre = tabCategorie[cpt];
                String legendeMembre = getLegende(csMembre);
                Float valMembre = getValeur(csMembre);

                // Si couple pas séparée par la maldie et valuer diff de 0 --> normal
                if (!isCoupleSepareParMaladie && (valMembre != 0f)) {
                    groupList.add(createLigneForGroupeList(csMembre, legendeMembre, valMembre, 2));

                } else {// Sinon si couple séparé, et clé autre que propre et revenu diff de 0
                    if (!(csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LCA))
                            // && !(csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_CONTRAT_ENTRETIEN_VIAGER))
                            && (valMembre != 0f)) {
                        groupList.add(createLigneForGroupeList(csMembre, legendeMembre, valMembre, 2));
                    }
                }
                // si valeur != 0, on traite
                // if (valMembre != 0f) {
                // this.groupList.add(this.createLigneForGroupeList(csMembre, legendeMembre, valMembre, 2));
                // }
            }
            // Ajout souslignement et sous total à la derniere valeur

        } else// Si valeur 0, on ajoute
        {
            // Valeur
            PCValeurPlanCalculHandler valeurAutreRevenus = createValeurPlanCalcul(cs, val.toString(), ADDITION,
                    NO_CSS_CLASS);
            // Sous total général
            String csSousTotal = IPCValeursPlanCalcul.CLE_REVEN_REV_SOUS_TOTAL_RECONNU;
            // PCValeurPlanCalculHandler valeurSousTotal = this.createValeurPlanCalcul(
            // IPCValeursPlanCalcul.CLE_REVENU_TOTAL_DETERMINANT, this.getValeur(csSousTotal).toString(),
            // this.ADDITION, this.NO_CSS_CLASS);
            // Ligne
            PCLignePlanCalculHandler ligne = createLignePlanCalcul(cs, legende, VALEUR_VIDE, valeurAutreRevenus,
                    VALEUR_VIDE);
            groupList.add(ligne);
        }
    }

    /**
     * Traitement categorie imputation fortune nette, obligatoire Legende
     */
    private void dealCategorieImputationFortuneNette() {
        String[] tabCategorie = REV_IMPUTATION_FORTUNE_NETTE;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        String legende = getLegende(cs);
        Float val = getValeur(cs);// recup valeur
        // valeur imputation
        PCValeurPlanCalculHandler valeurImputation = createValeurPlanCalcul(cs, val.toString(), ADDITION, NO_CSS_CLASS);
        // valeur fortune
        String csF = IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL;
        val = getValeur(csF);
        PCValeurPlanCalculHandler valeurFortuneNette = createValeurPlanCalcul(csF, val.toString(), ADDITION,
                NO_CSS_CLASS);

        String cs2 = IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL_PART;
        String legende2 = getLegende(cs2);

        if (StringUtils.isBlank(legende2)) {
            groupList.add(createLignePlanCalcul(cs, legende, valeurFortuneNette, valeurImputation, VALEUR_VIDE));
        } else {
            groupList.add(createLignePlanCalcul(cs, legende, valeurFortuneNette, VALEUR_VIDE, valeurImputation));
        }
    }

    /**
     * Traitement de la categorie interet dessaisissement fortune, non obligatoire, 1 valeur
     */
    private void dealCategorieInteretDessaisissementFortune() {

        String[] tabCategorie = REV_INTERET_FORTUNE_DESSAISI;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        String legende = getLegende(cs);
        Float val = getValeur(cs);// recup valeur
        // Ajout de la ligne
        groupList.add(createLigneForGroupeList(cs, legende, val, 2));
    }

    /**
     * Traitement de la categorie interet fortune mobiliere, obligatoire
     */
    private void dealCategorieInteretFortuneMobiliere() {
        String[] tabCategorie = REV_INTERET_FORTUNE_MOBILIERE;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        String legende = getLegende(cs);
        Float val = getValeur(cs);// recup valeur
        // SI valeur diff de 0
        if (getValeur(cs) != 0f) {
            // on traite les membres si il y en a

            // iteration sur les membres
            for (int cpt = 1; cpt < tabCategorie.length; cpt++) {
                String csMembre = tabCategorie[cpt];
                String legendeMembre = getLegende(csMembre);
                Float valMembre = getValeur(csMembre);
                // si valeur != 0, on traite
                if (valMembre != 0f) {
                    groupList.add(createLigneForGroupeList(csMembre, legendeMembre, valMembre, 2));
                }
            }

        } else// Si valeur 0, on ajoute
        {
            groupList.add(createLigneForGroupeList(cs, legende, val, 2));
        }
    }

    /**
     * Traitement du groupe loyer, sous locations des loyers
     */
    private void dealCategorieLoyer() {

        // Cle si loyer calculé mais plaffoné a 0
        String csTestSiCleCalcul = IPCValeursPlanCalcul.CLE_INTER_LOYER_SOUS_LOCATIONS_CALCULE;
        float valCsTestSiCleCalcul = getValeur(csTestSiCleCalcul);

        String[] tabCategorie = REV_LOYER;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        // SI valeur diff de 0
        if ((getValeur(cs) != 0f) || (valCsTestSiCleCalcul != 0.0f)) {
            String legende = getLegende(cs);

            Float val = getValeur(cs);// recup valeur

            // Ajout de la ligne
            groupList.add(createLigneForGroupeList(cs, legende, val, 2));

        }

    }

    /**
     * Traitement de la categorie rentes AVSAI, obligatoire, 1 valeur
     */
    private void dealCategorieRentesAvsAi() {
        String[] tabCategorie = REV_RENTES_AVS_AI;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        String legende = getLegende(cs);
        Float val = getValeur(cs);// recup valeur
        // Ajout de la ligne
        groupList.add(createLigneForGroupeList(cs, legende, val, 2));
    }

    /**
     * Traitement de la categorie revenu activite lucrative du requérant, obligatoire
     */
    private void dealCategorieRevenuActiviteLucrative() {
        String[] tabCategorie = REV_REVENU_ACTIVITE_LUCRATIVE;
        String keyRevenuNonPlaffone = IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL_NON_PLAFFONNE;

        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        String legende = getLegende(cs);
        Float val = getValeur(cs);// recup valeur
        // gestion plaffonenemtn à zéro

        // SI valeur diff de 0
        if (((getValeur(keyRevenuNonPlaffone) <= 0f) || (getValeur(cs) != 0f))) {
            // iteration sur les membres¨
            boolean afficherForfaitActiviteLucr = false;
            boolean hasRevenuPriviligie = false;
            Float valeurForfait = 0f;
            for (int cpt = 1; cpt < tabCategorie.length; cpt++) {
                String csMembre = tabCategorie[cpt];
                String legendeMembre = getLegende(csMembre);
                Float valMembre = getValeur(csMembre);

                // Si on a une activité lucrative de quelque sorte != 0, on affiche le forfait même si il est égal à 0
                if ((csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE)
                        || csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE) || csMembre
                        .equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_AGRICOLE))
                        && valMembre != 0f) {
                    afficherForfaitActiviteLucr = true;
                }

                if (csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE) && valMembre != 0f) {
                    hasRevenuPriviligie = true;
                }

                // si valeur != 0, on traite si = 0 mais activité lucrative on affiche quand même le forfait à 0
                // (pour la ligne9
                if (valMembre != 0f
                        || (IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_FORFAITAIRE_REVENU.equals(csMembre) && afficherForfaitActiviteLucr)) {
                    // traitement deduction
                    if (csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_SOCIALES)
                            || csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_LPP)
                            || csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_FRAIS_OBTENTION_REVENU)
                            || csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_FORFAITAIRE_REVENU)) {

                        // traitement de la valeur
                        PCValeurPlanCalculHandler valeur;
                        if (csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_FORFAITAIRE_REVENU)) {
                            valeur = createValeurPlanCalcul(csMembre, valMembre.toString(),
                                    PCGroupeRevenusHandler.SOUSTRACTION, CSS_SOULIGNE);
                        } else {
                            valeur = createValeurPlanCalcul(csMembre, valMembre.toString(),
                                    PCGroupeRevenusHandler.SOUSTRACTION, NO_CSS_CLASS);
                        }
                        valeurForfait = valMembre;
                        PCLignePlanCalculHandler ligne = createLignePlanCalcul(csMembre, legendeMembre, valeur,
                                VALEUR_VIDE, VALEUR_VIDE);
                        ligne.setCssClass(CSS_INCREMENT_DEDUCTION);
                        groupList.add(ligne);

                    } else {
                        if (csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE)
                                || (csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIS_EN_COMPTE) && !hasRevenuPriviligie)) {
                            // valeur revenu privilegie
                            createLigneTotalGroupe(csMembre, legendeMembre, valMembre);

                        } else {
                            // Si on a un revenu pris en compte ici ç aveut dire qu'on a un revenu pris au 2/3 et on
                            // doit pas afficher le revenu complet
                            if (!csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIS_EN_COMPTE)) {
                                groupList.add(createLigneForGroupeList(csMembre, legendeMembre, valMembre, 1));
                            }
                        }
                    }
                } else if (isReforme()) {
                    // dans le cas de la réforme, on affiche systématiquement la ligne de revenu pris en compte.
                    if (csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE) && valeurForfait != 0) {
                        createLigneTotalGroupe(csMembre, legendeMembre, valMembre);
                    }
                }
            }
        } else// Si valeur 0, on ajoute
        {
            groupList.add(createLigneForGroupeList(cs, legende, val, 2));
        }
    }

    /**
     * Méthode de création de la dernière ligne d'un bloc.
     * @param csMembre
     * @param legendeMembre
     * @param valMembre
     */
    private void createLigneTotalGroupe(String csMembre, String legendeMembre, Float valMembre) {
        // valeur revenu privilegie
        PCValeurPlanCalculHandler valeurRevPriv = createValeurPlanCalcul(csMembre, valMembre.toString(), ADDITION, NO_CSS_CLASS);
        // Valeur revenu net
        String csRevNet = IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL;
        Float valRevNet = getValeur(csRevNet);
        PCValeurPlanCalculHandler valeurRevNet = createValeurPlanCalcul(csRevNet, valRevNet.toString(), ADDITION, NO_CSS_CLASS);
        groupList.add(createLignePlanCalcul(csMembre, legendeMembre, valeurRevNet, valeurRevPriv, VALEUR_VIDE));
    }

    /**
     * Traitement de la categorie revenu activite lucrative pour le conjoint
     */
    private void dealCategorieRevenuActiviteLucrativeConjoint() {
        String[] tabCategorie = REV_REVENU_ACTIVITE_LUCRATIVE_CONJOINT;

        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        Float val = getValeur(cs);// recup valeur
        // gestion plaffonenemtn à zéro

        String derniereLigne = getDerniereLigne(DERNIERE_LIGNE_REVENU_ACTIVITE_LUCRATIVE_CONJOINT);

        // SI valeur diff de 0 ou dernière ligne diff de 0 on affiche le revenu
        if (val != 0f || getValeur(derniereLigne) != 0) {
            // iteration sur les membres¨
            boolean hasRevenuPriviligie = false;
            for (int cpt = 1; cpt < tabCategorie.length; cpt++) {
                String csMembre = tabCategorie[cpt];
                String legendeMembre = getLegende(csMembre);
                Float valMembre = getValeur(csMembre);

                if (csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE_CONJOINT) && valMembre != 0f) {
                    hasRevenuPriviligie = true;
                }

                // si valeur != 0, on traite si = 0 mais activité lucrative on affiche quand même le forfait à 0
                // (pour la ligne9
                if (valMembre != 0f) {
                    // traitement deduction
                    if (csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_SOCIALES_CONJOINT)
                            || csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_LPP_CONJOINT)
                            || csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_FRAIS_OBTENTION_REVENU_CONJOINT)) {
                        // traitement de la valeur
                        PCValeurPlanCalculHandler valeur;
                        if (StringUtils.equals(derniereLigne, csMembre)) {
                            valeur = createValeurPlanCalcul(csMembre, valMembre.toString(),
                                    PCGroupeRevenusHandler.SOUSTRACTION, CSS_SOULIGNE);
                        } else {
                            valeur = createValeurPlanCalcul(csMembre, valMembre.toString(),
                                    PCGroupeRevenusHandler.SOUSTRACTION, NO_CSS_CLASS);
                        }

                        PCLignePlanCalculHandler ligne = createLignePlanCalcul(csMembre, legendeMembre, valeur,
                                VALEUR_VIDE, VALEUR_VIDE);
                        ligne.setCssClass(CSS_INCREMENT_DEDUCTION);
                        groupList.add(ligne);

                    } else {
                        if (csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE_CONJOINT)
                                || (csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIS_EN_COMPTE_CONJOINT) && !hasRevenuPriviligie)) {
                            // valeur revenu privilegie
                            PCValeurPlanCalculHandler valeurRevPriv = createValeurPlanCalcul(csMembre,
                                    valMembre.toString(), ADDITION, NO_CSS_CLASS);
                            // Valeur revenu net
                            String csRevNet = IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL_CONJOINT;
                            Float valRevNet = getValeur(csRevNet);
                            PCValeurPlanCalculHandler valeurRevNet = createValeurPlanCalcul(csRevNet,
                                    valRevNet.toString(), ADDITION, NO_CSS_CLASS);
                            groupList.add(createLignePlanCalcul(csMembre, legendeMembre, valeurRevNet, valeurRevPriv,
                                    VALEUR_VIDE));

                        } else {
                            if (!csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIS_EN_COMPTE_CONJOINT)) {
                                if (StringUtils.equals(derniereLigne, csMembre)) {
                                    PCValeurPlanCalculHandler valeur = createValeurPlanCalcul(csMembre, valMembre.toString(),
                                            ADDITION, CSS_SOULIGNE);
                                    PCLignePlanCalculHandler ligne = createLignePlanCalcul(csMembre, legendeMembre, valeur,
                                            VALEUR_VIDE, VALEUR_VIDE);
                                    groupList.add(ligne);
                                } else {
                                    groupList.add(createLigneForGroupeList(csMembre, legendeMembre, valMembre, 1));
                                }
                            }
                        }
                    }
                } else {
                    // dans le cas de la réforme, on affiche systématiquement la ligne de revenu pris en compte.
                    if (csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE_CONJOINT) && getValeur(derniereLigne) != 0) {
                        createLigneTotalGroupe(csMembre, legendeMembre, valMembre);
                    }
                }
            }
        }
    }


    /**
     * Traitement de la categorie revenu activite lucrative pour les enfants
     */
    private void dealCategorieRevenuActiviteLucrativeEnfant() {
        String[] tabCategorie = REV_REVENU_ACTIVITE_LUCRATIVE_ENFANT;

        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        Float val = getValeur(cs);// recup valeur
        // gestion plaffonenemtn à zéro

        String derniereLigne = getDerniereLigne(DERNIERE_LIGNE_REVENU_ACTIVITE_LUCRATIVE_ENFANT);

        // SI valeur diff de 0 ou dernière ligne diff de 0 on affiche le revenu
        if (val != 0f || getValeur(derniereLigne) != 0) {
            // iteration sur les membres¨
            boolean hasRevenuPriviligie = false;
            for (int cpt = 1; cpt < tabCategorie.length; cpt++) {
                String csMembre = tabCategorie[cpt];
                String legendeMembre = getLegende(csMembre);
                Float valMembre = getValeur(csMembre);

                if (csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE_ENFANT) && valMembre != 0f) {
                    hasRevenuPriviligie = true;
                }

                // si valeur != 0, on traite si = 0 mais activité lucrative on affiche quand même le forfait à 0
                // (pour la ligne9
                if (valMembre != 0f) {
                    // traitement deduction
                    if (csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_SOCIALES_ENFANT)
                            || csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_LPP_ENFANT)
                            || csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_FRAIS_OBTENTION_REVENU_ENFANT)) {
                        // traitement de la valeur
                        PCValeurPlanCalculHandler valeur;
                        if (StringUtils.equals(derniereLigne, csMembre)) {
                            valeur = createValeurPlanCalcul(csMembre, valMembre.toString(),
                                    PCGroupeRevenusHandler.SOUSTRACTION, CSS_SOULIGNE);
                        } else {
                            valeur = createValeurPlanCalcul(csMembre, valMembre.toString(),
                                    PCGroupeRevenusHandler.SOUSTRACTION, NO_CSS_CLASS);
                        }

                        PCLignePlanCalculHandler ligne = createLignePlanCalcul(csMembre, legendeMembre, valeur,
                                VALEUR_VIDE, VALEUR_VIDE);
                        ligne.setCssClass(CSS_INCREMENT_DEDUCTION);
                        groupList.add(ligne);

                    } else {
                        if (csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE_ENFANT)
                                || (csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIS_EN_COMPTE_ENFANT) && !hasRevenuPriviligie)) {
                            // valeur revenu privilegie
                            PCValeurPlanCalculHandler valeurRevPriv = createValeurPlanCalcul(csMembre,
                                    valMembre.toString(), ADDITION, NO_CSS_CLASS);
                            // Valeur revenu net
                            String csRevNet = IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL_ENFANT;
                            Float valRevNet = getValeur(csRevNet);
                            PCValeurPlanCalculHandler valeurRevNet = createValeurPlanCalcul(csRevNet,
                                    valRevNet.toString(), ADDITION, NO_CSS_CLASS);
                            groupList.add(createLignePlanCalcul(csMembre, legendeMembre, valeurRevNet, valeurRevPriv,
                                    VALEUR_VIDE));

                        } else {
                            if (!csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIS_EN_COMPTE_ENFANT)) {
                                if (StringUtils.equals(derniereLigne, csMembre)) {
                                    PCValeurPlanCalculHandler valeur = createValeurPlanCalcul(csMembre, valMembre.toString(),
                                            ADDITION, CSS_SOULIGNE);
                                    PCLignePlanCalculHandler ligne = createLignePlanCalcul(csMembre, legendeMembre, valeur,
                                            VALEUR_VIDE, VALEUR_VIDE);
                                    groupList.add(ligne);
                                } else {
                                    groupList.add(createLigneForGroupeList(csMembre, legendeMembre, valMembre, 1));
                                }
                            }
                        }
                    }
                } else {
                    // dans le cas de la réforme, on affiche systématiquement la ligne de revenu pris en compte.
                    if (csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE_ENFANT) && getValeur(derniereLigne) != 0) {
                        createLigneTotalGroupe(csMembre, legendeMembre, valMembre);
                    }
                }
            }
        }
    }


    /**
     * Méthode permettant de récupérer la dernière ligne du bloc revenu conjoint.
     * @return le CS de la dernière ligne du bloc.
     */
    private String getDerniereLigne(String[] dernieresLignes) {
        String[] tabCategorie = dernieresLignes;
        String derniereLigne = StringUtils.EMPTY;
        for (int cpt = 0; cpt < tabCategorie.length; cpt++) {
            String csMembre = tabCategorie[cpt];
            Float valMembre = getValeur(csMembre);
            if (valMembre != 0f) {
                derniereLigne = csMembre;
            }
        }
        return derniereLigne;
    }


    /**
     * Traitement de la categorie revenus dessaisis, obligatoire
     */
    private void dealCategorieRevenusDessaisis() {
        String[] tabCategorie = REV_DESSAISISSEMENT_REVENUS;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        String legende = getLegende(cs);
        Float val = getValeur(cs);// recup valeur
        if (val != 0.0f) {
            // Ajout de la ligne
            groupList.add(createLigneForGroupeList(cs, legende, val, 2));
        }
    }

    /**
     * Traitement de la categorie total
     */
    private void dealCategorieTotal() {
        String[] tabCategorie = REV_TOTAL_REVENUS_DETERMINANTS;
        String cs = tabCategorie[0];
        String legende = getLegende(cs);
        Float val = getValeur(cs);
        PCValeurPlanCalculHandler valeur = createValeurPlanCalcul(cs, val.toString(), ADDITION, NO_CSS_CLASS);
        PCLignePlanCalculHandler ligneTotal = createLignePlanCalcul(cs, legende, VALEUR_VIDE, VALEUR_VIDE, valeur);
        ligneTotal.setCssClass("total");
        groupList.add(ligneTotal);
    }

    private void dealSousTotalCommunSeparation() {
        String[] tabCategorie = REV_TOTAL_PERSONNE_PROPRE;

        PCValeurPlanCalculHandler valeurTotal = createValeurPlanCalcul(IPCValeursPlanCalcul.CLE_TOTAL_REVENUS_COMMUN,
                getValeur(IPCValeursPlanCalcul.CLE_TOTAL_REVENUS_COMMUN).toString(), ADDITION, NO_CSS_CLASS);
        PCValeurPlanCalculHandler valeurMoitie = createValeurPlanCalcul(
                IPCValeursPlanCalcul.CLE_TOTAL_REVENUS_COMMUN_POUR_MOITIE,
                getValeur(IPCValeursPlanCalcul.CLE_TOTAL_REVENUS_COMMUN_POUR_MOITIE).toString(), ADDITION, NO_CSS_CLASS);
        groupList.add(createLignePlancalcul(IPCValeursPlanCalcul.CLE_TOTAL_REVENUS_COMMUN,
                getLegende(IPCValeursPlanCalcul.CLE_TOTAL_REVENUS_COMMUN), NO_CSS_CLASS, VALEUR_VIDE, valeurTotal,
                valeurMoitie));

        for (int cpt = 0; cpt < tabCategorie.length; cpt++) {
            String csMembre = tabCategorie[cpt];
            String legendeMembre = getLegende(csMembre);
            Float valMembre = getValeur(csMembre);

            if (valMembre != 0.0f) {
                PCValeurPlanCalculHandler valeur = createValeurPlanCalcul(csMembre, valMembre.toString(), ADDITION,
                        NO_CSS_CLASS);

                groupList.add(createLignePlanCalcul(csMembre, legendeMembre, VALEUR_VIDE, VALEUR_VIDE, valeur));
            }
        }
        groupList.get(groupList.size() - 1).getValCol3().setCssClass(CSS_SOULIGNE);

    }

    /**
     * Traitement de la categorie revenu fortune immobiliere, obligatoire
     */
    private void delaCategorieRevenuFortuneImmobiliere() {
        String[] tabCategorie = REV_REVENUS_FORTUNE_IMMOBILIERE;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        String legende = getLegende(cs);
        Float val = getValeur(cs);// recup valeur
        // SI valeur diff de 0
        if (getValeur(cs) != 0f) {
            // on traite les membres si il y en a
            if (tabCategorie.length > 1) {
                // iteration sur les membres
                for (int cpt = 1; cpt < tabCategorie.length; cpt++) {
                    String csMembre = tabCategorie[cpt];
                    String legendeMembre = getLegende(csMembre);
                    Float valMembre = getValeur(csMembre);

                    // Si couple pas séparée par la maldie et valuer diff de 0 --> normal
                    if (!isCoupleSepareParMaladie && (valMembre != 0f)) {
                        groupList.add(createLigneForGroupeList(csMembre, legendeMembre, valMembre, 2));

                    } else {// Sinon si couple séparé, et clé autre que propre et revenu diff de 0
                        if (!csMembre.equals(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_VALEUR_LOCATIVE)
                                && (valMembre != 0f)) {
                            groupList.add(createLigneForGroupeList(csMembre, legendeMembre, valMembre, 2));
                        }
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
     * Traitement du groupe Subside Assurance maladie
     */
    private void dealCategorieSubsideAssuranceMaladie() {

        // Cle si loyer calculé mais plaffoné a 0
        String csTestSiCleCalcul = IPCValeursPlanCalcul.CLE_REVEN_SUBSIDE_ASSURANCE_MALADIE_TOTAL;
        float valCsTestSiCleCalcul = getValeur(csTestSiCleCalcul);

        String[] tabCategorie = REV_SUBSIDE_ASSURANCE_MALADIE;
        // Test du totale de groupe
        String cs = tabCategorie[0];// recup code systeme
        // SI valeur diff de 0
        if ((getValeur(cs) != 0f) || (valCsTestSiCleCalcul != 0.0f)) {
            String legende = getLegende(cs);

            Float val = getValeur(cs);// recup valeur

            // Ajout de la ligne
            groupList.add(createLigneForGroupeList(cs, legende, val, 2));

        }

        // Gestion soulignement et sous total revenus déterminants
        if (!isCoupleSepareParMaladie) {
            groupList.get(groupList.size() - 1).getValCol2().setCssClass(CSS_SOULIGNE);
            String csSousTotal = IPCValeursPlanCalcul.CLE_REVEN_REV_DETE_TOTAL;
            PCValeurPlanCalculHandler valeurSousTotal = createValeurPlanCalcul(
                    IPCValeursPlanCalcul.CLE_REVEN_REV_DETE_TOTAL, getValeur(csSousTotal).toString(), ADDITION,
                    CSS_SOULIGNE);
            groupList.get(groupList.size() - 1).setValCol3(valeurSousTotal);
        } else {
            groupList.get(groupList.size() - 1).getValCol2().setCssClass(CSS_SOULIGNE);
            // String csSousTotal = IPCValeursPlanCalcul.CLE_REVEN_REV_DETE_TOTAL;
            // PCValeurPlanCalculHandler valeurSousTotal = this.createValeurPlanCalcul(
            // IPCValeursPlanCalcul.CLE_REVEN_REV_DETE_TOTAL, this.getValeur(csSousTotal).toString(),
            // this.ADDITION, this.CSS_SOULIGNE);
            // this.groupList.get(this.groupList.size() - 1).setValCol3(valeurSousTotal);
        }

    }

    /**
     * Génération des lignes à afficher dans la page jsp
     *
     * @throws CalculException
     */
    private void generateLigneProcess() {
        dealCategorieImputationFortuneNette();
        dealCategorieInteretDessaisissementFortune();
        dealCategorieInteretFortuneMobiliere();
        delaCategorieRevenuFortuneImmobiliere();
        dealCategorieRevenuActiviteLucrative();
        dealCategorieRevenuActiviteLucrativeConjoint();
        dealCategorieRevenuActiviteLucrativeEnfant();
        dealCategorieRentesAvsAi();
        dealCategorieAutresRentes();
        dealCategorieAllocationsImpotents();
        dealCategorieLoyer();
        dealCategorieAutresRevenus();
        dealCategorieRevenusDessaisis();
        dealCategorieSubsideAssuranceMaladie();

        if (isCoupleSepareParMaladie) {
            dealSousTotalCommunSeparation();
        }
        groupList.add(setEmptyLigne());
        dealCategorieTotal();
    }


    /**
     * @return the groupList
     * @throws CalculException
     */
    public ArrayList<PCLignePlanCalculHandler> getGroupList() throws CalculException {
        generateLigneProcess();
        return groupList;
    }


}
