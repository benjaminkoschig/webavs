/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.plancalcul;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import java.util.ArrayList;
import ch.globaz.common.business.language.LanguageResolver;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

/**
 * @author SCE
 * 
 *         29 nov. 2010
 */
public class PCGroupeTotalHandler extends PCGroupeAbstractHandler {

    private static final String ASSURANCE_MALADIE = IPCValeursPlanCalcul.CLE_TOTAL_PRIMEMAL_TOTAL;
    // Constantes
    private static final String DEPENSES_RECONNUES = IPCValeursPlanCalcul.CLE_DEPEN_DEP_RECO_TOTAL;
    private static final String REVENUS_DETERMINANTS = IPCValeursPlanCalcul.CLE_REVEN_REV_DETE_TOTAL;
    private static final String SOUSTRACTION = "-";
    private static final String TOTAL_CC = IPCValeursPlanCalcul.CLE_TOTAL_CC;
    private static final String TOTAL_CC_DEDUIT = IPCValeursPlanCalcul.CLE_TOTAL_CC_DEDUIT;
    private static final String TOTAL_CC_DEDUIT_MENS = IPCValeursPlanCalcul.CLE_TOTAL_CC_DEDUIT_MENSUEL;
    ArrayList<PCLignePlanCalculHandler> groupList = new ArrayList<PCLignePlanCalculHandler>(0);

    /**
     * @param tupleRoot
     */
    public PCGroupeTotalHandler(TupleDonneeRapport tupleRoot) {
        super(tupleRoot);
    }

    private PCLignePlanCalculHandler generateAssuranceMaladieLigne(String langue, BSession session) {
        // Valeur CCTotal
        String csAssMal = PCGroupeTotalHandler.ASSURANCE_MALADIE;
        Float valAssMall = getValeur(csAssMal);
        PCValeurPlanCalculHandler valeurAssMal = createValeurPlanCalcul(csAssMal, valAssMall.toString(), ADDITION,
                NO_CSS_CLASS);
        String libellePCAmal = LanguageResolver
                .resolveLibelleFromLabel(langue, "JSP_PC_PLANCALCUL_D_PC_PAMAL", session);

        return (createLignePlanCalcul(libellePCAmal, "", VALEUR_VIDE, VALEUR_VIDE, valeurAssMal));
    }

    /**
     * Création de la derniere ligne du bloc resume
     * 
     * @return
     */
    private PCLignePlanCalculHandler generateLastLigne(String langue, BSession session) {
        PCLignePlanCalculHandler ligne = null;
        // test de la valeur de la cle total pc mensuelle
        String cs = PCGroupeTotalHandler.TOTAL_CC;// recup code systeme
        // String legCs = this.getLegende(cs);
        Float val = getValeur(cs);// recup valeu
        // si valeur total negative, excedent de recette
        if ((val < 0.00f)) {
            // test valeur finale
            Float valMontantPc = getValeur(PCGroupeTotalHandler.TOTAL_CC_DEDUIT);
            String csMontantPc = PCGroupeTotalHandler.TOTAL_CC_DEDUIT;
            // Si plus petit que 0, excedent de recette REFUS
            if ((valMontantPc < 0.00f) || (valMontantPc == 0.00f)) {
                String csExcedentNet = LanguageResolver.resolveLibelleFromLabel(langue,
                        "JSP_PC_PLANCALCUL_D_EXCEDENT_NET", session);
                // Valeur pour total a 0
                PCValeurPlanCalculHandler valeurCCZero = createValeurPlanCalcul(csExcedentNet, getValeur(csMontantPc)
                        .toString(), ADDITION, "");

                // Ligne
                ligne = createLignePlancalcul(csExcedentNet, "", CSS_GRAS, VALEUR_VIDE, VALEUR_VIDE, valeurCCZero);
            }
            // Si val plus grand que 0, droit partiel
            if (valMontantPc > 0) {
                String csExcedent = LanguageResolver.resolveLibelleFromLabel(langue, "JSP_PC_PLANCALCUL_D_PC_PARTIEL",
                        session);
                // Valeur
                PCValeurPlanCalculHandler valeurCCDeduit = createValeurPlanCalcul(csExcedent, "0.00", ADDITION, "");
                // Ligne
                ligne = createLignePlancalcul(csExcedent, "", CSS_GRAS, VALEUR_VIDE, VALEUR_VIDE, valeurCCDeduit);
            }
        }// Si valeur total plus grand que 0
        else if (val > 0f) {
            String csMontPcMens = PCGroupeTotalHandler.TOTAL_CC_DEDUIT_MENS;
            // Valeur
            PCValeurPlanCalculHandler valeurCCMensuel = createValeurPlanCalcul(csMontPcMens,
                    getValeur(PCGroupeTotalHandler.TOTAL_CC_DEDUIT_MENS).toString(), ADDITION, "");
            // Ligne
            String labelMontPCMens = LanguageResolver.resolveLibelleFromLabel(langue, "JSP_PC_PLANCALCUL_D_PC_MENSUEL",
                    session);
            ligne = createLignePlancalcul(labelMontPCMens, "", CSS_GRAS, VALEUR_VIDE, VALEUR_VIDE, valeurCCMensuel);
        } else// Si valeur == 0
        {
            String csMontPcMens = "";// IPCValeursPlanCalcul.CLE_TOTAL_MONTANT_MENSUEL;
            // Valeur
            PCValeurPlanCalculHandler valeurCCMensuel = createValeurPlanCalcul(csMontPcMens,
                    getValeur(PCGroupeTotalHandler.TOTAL_CC_DEDUIT_MENS).toString(), ADDITION, "");
            // Ligne
            ligne = createLignePlancalcul(csMontPcMens, "", CSS_GRAS, VALEUR_VIDE, VALEUR_VIDE, valeurCCMensuel);
        }
        return ligne;
    }

    private void generateLignesProcess(String langueTiers, BSession session) {
        groupList.add(generateMainLigne(langueTiers, session));

        // TODO a voir normalement pas de code métier ici
        // si cc annuel > 0 on traite les assurance maladies
        if (getValeur(PCGroupeTotalHandler.TOTAL_CC) < 0) {
            groupList.add(generateAssuranceMaladieLigne(langueTiers, session));
        }
        groupList.add(generateLastLigne(langueTiers, session));
    }

    /**
     * Cnostruction de la ligne principale
     */
    private PCLignePlanCalculHandler generateMainLigne(String langue, BSession session) {
        // ligne à créer
        PCLignePlanCalculHandler ligne = null;

        // valeur totale
        String cs = PCGroupeTotalHandler.TOTAL_CC;// recup code systeme
        Float val = getValeur(cs);// recup valeur
        // Valeurs dépenses
        String csDepenses = PCGroupeTotalHandler.DEPENSES_RECONNUES;
        Float valDepenses = getValeur(csDepenses);
        PCValeurPlanCalculHandler valeurDepenses = createValeurPlanCalcul(csDepenses, valDepenses.toString(), ADDITION,
                NO_CSS_CLASS);

        // Valeurs revenus
        String csRevenus = PCGroupeTotalHandler.REVENUS_DETERMINANTS;
        Float valRevenus = getValeur(csRevenus);
        PCValeurPlanCalculHandler valeurRevenus = createValeurPlanCalcul(csRevenus, valRevenus.toString(),
                PCGroupeTotalHandler.SOUSTRACTION, NO_CSS_CLASS);

        // si valeur total negative, excedent de recette
        if (val < 0f) {
            String csExcedent = LanguageResolver.resolveLibelleFromLabel(langue,
                    "JSP_PC_PLANCALCUL_D_EXCEDENT_REVENUS", session);
            // test valeur finale
            Float valMontantPcMens = getValeur(PCGroupeTotalHandler.TOTAL_CC_DEDUIT_MENS);
            // Si 0, excedent de recette avec droit partiel
            if (valMontantPcMens == 0) {
                // Valeur pour total a 0
                PCValeurPlanCalculHandler valeurCCZero = createValeurPlanCalcul(cs, getValeur(cs).toString(), ADDITION,
                        "");

                // Ligne
                ligne = createLignePlancalcul(csExcedent, "", CSS_GRAS, valeurDepenses, valeurRevenus, valeurCCZero);
            }
            // Si val plus petit que 0, Excedent de recette brut
            if (valMontantPcMens < 0) {
                // Valeur
                PCValeurPlanCalculHandler valeurCCDeduit = createValeurPlanCalcul(csExcedent,
                        getValeur(PCGroupeTotalHandler.TOTAL_CC_DEDUIT).toString(), ADDITION, "");
                // Ligne
                ligne = createLignePlancalcul(csExcedent, "", CSS_GRAS, valeurDepenses, valeurRevenus, valeurCCDeduit);
            }
        }// Si valeur total plus grand que 0
        else if (val > 0f) {
            // libelle excedent de depenses pc annuelles
            String csExcedentDepense = LanguageResolver.resolveLibelleFromLabel(langue,
                    "JSP_PC_PLANCALCUL_D_EXCEDENT_DEPENSES", session);
            // Valeur
            PCValeurPlanCalculHandler valeurCCAnnuel = createValeurPlanCalcul(csExcedentDepense,
                    getValeur(PCGroupeTotalHandler.TOTAL_CC).toString(), ADDITION, "");
            // Ligne
            ligne = createLignePlancalcul(csExcedentDepense, "", CSS_GRAS, valeurDepenses, valeurRevenus,
                    valeurCCAnnuel);
        } else// Si valeur == 0
        {
            String montPcAnnuel = "";
            // Valeur
            PCValeurPlanCalculHandler valeurCCAnnuel = createValeurPlanCalcul(montPcAnnuel,
                    getValeur(PCGroupeTotalHandler.TOTAL_CC_DEDUIT).toString(), ADDITION, "");
            // Ligne
            ligne = createLignePlancalcul(montPcAnnuel, "", CSS_GRAS, valeurDepenses, valeurRevenus, valeurCCAnnuel);
        }

        return ligne;
    }

    /**
     * @return the groupList
     */
    public ArrayList<PCLignePlanCalculHandler> getGroupList(String langueTiers) {
        // récupération de la session depuis le ThreadContext
        BSession session = BSessionUtil.getSessionFromThreadContext();
        generateLignesProcess(langueTiers, session);
        return groupList;
    }

}
