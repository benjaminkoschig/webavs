/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.plancalcul;

import globaz.globall.db.BSessionUtil;
import java.util.ArrayList;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

/**
 * @author SCE
 * 
 *         15 nov. 2010
 */
abstract class PCGroupeAbstractHandler {

    /* valeur string normale */
    public final String ADDITION = "";
    /* classe css gras */
    public final String CSS_GRAS = "gras";
    /* classe incrément activite lucrative deduction */
    public final String CSS_INCREMENT_DEDUCTION = "increment";
    /* classe de soulignement */
    public final String CSS_SOULIGNE = "souligne";
    /* Liste contenant les ligne du groupe */
    ArrayList<PCLignePlanCalculHandler> groupList = new ArrayList<PCLignePlanCalculHandler>(0);
    /* aucune classe chaine vide */
    public final String NO_CSS_CLASS = "";
    public final String SOUSTRACTION = "-";
    /* tuple root bd */
    public TupleDonneeRapport tupleRoot = null;
    /* valeur vide par defaut */
    public final PCValeurPlanCalculHandler VALEUR_VIDE = new PCValeurPlanCalculHandler("", "", "", "");

    /**
     * Constructeur avec tupleRoot en parametre
     * 
     * @param tupleRoot
     */
    public PCGroupeAbstractHandler(TupleDonneeRapport tupleRoot) {
        this.tupleRoot = tupleRoot;
    }

    /**
     * Creation d'une ligne a ajouter à la liste retournée pour la jsp
     * 
     * @param cs
     *            , le code système de la valeur pour le libelle
     * @param val
     *            , la valeur
     * @param col
     *            , la colone dans laquelle afficher la valeur
     * @return ligne, instance de PCLignePlanCalculHelper
     */
    public PCLignePlanCalculHandler createLigneForGroupeList(String cs, String legende, Float val, int col) {

        PCValeurPlanCalculHandler valeur = new PCValeurPlanCalculHandler(cs, val.toString(), ADDITION, NO_CSS_CLASS);
        PCLignePlanCalculHandler ligne = null;

        switch (col) {
            case 2:
                ligne = new PCLignePlanCalculHandler(cs, legende, VALEUR_VIDE, valeur, VALEUR_VIDE);
                break;
            case 1:
                ligne = new PCLignePlanCalculHandler(cs, legende, valeur, VALEUR_VIDE, VALEUR_VIDE);
                break;
            case 3:
                ligne = new PCLignePlanCalculHandler(cs, legende, VALEUR_VIDE, VALEUR_VIDE, valeur);
        }
        return ligne;
    }

    /**
     * Création d'une ligne de plan de calcul. Avec style css
     * 
     * @param cs
     * @param legende
     * @param col1
     * @param col2
     * @param col3
     * @return instance PCLignePlanCalculHelper
     */
    public PCLignePlanCalculHandler createLignePlancalcul(String cs, String legende, String cssClass,
            PCValeurPlanCalculHandler col1, PCValeurPlanCalculHandler col2, PCValeurPlanCalculHandler col3) {
        return new PCLignePlanCalculHandler(cs, legende, cssClass, col1, col2, col3);
    }

    /**
     * Création d'une ligne de plan de calcul. Sans style css
     * 
     * @param cs
     * @param legende
     * @param col1
     * @param col2
     * @param col3
     * @return instance PCLignePlanCalculHelper
     */
    public PCLignePlanCalculHandler createLignePlanCalcul(String cs, String legende, PCValeurPlanCalculHandler col1,
            PCValeurPlanCalculHandler col2, PCValeurPlanCalculHandler col3) {
        return new PCLignePlanCalculHandler(cs, legende, col1, col2, col3);
    }

    /**
     * Création d'un objet PCValeurPlanCalculHelper
     * 
     * @param codeSystem
     * @param valeur
     * @param signe
     * @param css
     * @return instance PCValeurPlanCalculHelper
     */
    public PCValeurPlanCalculHandler createValeurPlanCalcul(String codeSystem, String valeur, String signe, String css) {
        return new PCValeurPlanCalculHandler(codeSystem, valeur, signe, css);
    }

    /**
     * Retourne un objet OCVAleurPlanCalculHelper d'un enfant
     * 
     * @param cs
     *            le code systeme de l'enfant
     * @param cat
     *            , la categorie dans laquelle se trouve l'enfant
     * @return
     */
    public PCValeurPlanCalculHandler getByCS(String cs, PCValeurPlanCalculHandler cat) {
        for (PCValeurPlanCalculHandler membres : cat.getMembresCategorie()) {
            if (membres.getCodeSysteme().equals(cs)) {
                return membres;
            }
        }
        return null;
    }

    /**
     * Retourne le libelle, pour le groupe total
     * 
     * @param idLibelle
     *            , l'id du libelle
     * @return chaine de carcatère
     */
    public String getLabel(String idLibelle) {
        return BSessionUtil.getSessionFromThreadContext().getLabel(idLibelle);
    }

    /**
     * Retourne la légende d'un enfant
     * 
     * @param csKey
     *            , le code systeme de l'enfant
     * @return legende, la legende
     */
    public String getLegende(String csKey) {
        String legende = tupleRoot.getLegendeEnfant(csKey);

        if (legende == null) {
            return "";
        } else {
            return legende;
        }
    }

    /**
     * @return the tupleRoot
     */
    public TupleDonneeRapport getTupleRoot() {
        return tupleRoot;
    }

    /**
     * Retourne la valeur d'un enfant du tuppleRoot
     * 
     * @param cs
     *            , le code système de l'enfant
     * @return valeur de l'enfant
     */
    public Float getValeur(String cs) {
        return tupleRoot.getValeurEnfant(cs);
    }

    /**
     * Retourne une ligne vide afin de gérer les espaces dans l'affichage du plan de calcul
     * 
     * @return instance de PCLignePlanCalculHelper, mais vide
     */
    public PCLignePlanCalculHandler setEmptyLigne() {
        PCLignePlanCalculHandler emptyLigne = new PCLignePlanCalculHandler("", "", VALEUR_VIDE, VALEUR_VIDE,
                VALEUR_VIDE);
        return emptyLigne;
    }

    /**
     * Set la valeur d'une ligne sur la colone numero 3
     * 
     * @param valeur
     *            a fficher dans la colone 3
     * @return ligne du plan de calcul
     */
    public PCLignePlanCalculHandler setValToCol3(PCValeurPlanCalculHandler valeur) {
        String cs = valeur.getCodeSysteme();

        if ((cs.equals(IPCValeursPlanCalcul.CLE_REVEN_REV_DETE_TOTAL))
                || (valeur.getCodeSysteme().equals(IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL) || (valeur
                        .getCodeSysteme().equals(IPCValeursPlanCalcul.CLE_DEPEN_DEP_RECO_TOTAL)))) {
            valeur.setCssClass("total");
        }

        if (cs.equals(IPCValeursPlanCalcul.CLE_FORTU_DED_LEGA_TOTAL)) {
            valeur.setCssClass("souligne");
        }
        return createLignePlanCalcul(cs, getLegende(cs), null, null, valeur);
    }
}
