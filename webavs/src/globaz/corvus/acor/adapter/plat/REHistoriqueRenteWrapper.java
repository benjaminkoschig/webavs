/*
 * Créé le 12 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.corvus.acor.adapter.plat;

import globaz.globall.shared.GlobazValueObject;
import java.util.HashMap;
import java.util.Map;

/**
 * DOCUMENT ME!
 * 
 * @deprecated, plus utilisé avec nouvel historique des rentes.
 * @author scr
 */
public class REHistoriqueRenteWrapper {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String EMPTY_STRING = "";
    private static final HashMap FIELDNAMES = new HashMap();

    static final String HIST_BASE_CALCUL = "baseCalcul";
    static final String HIST_RENTE_ACCORDES = "renteAccordee";
    private static final String PREFIX_BASE_CALCUL = "bc";
    private static final String PREFIX_RENTE_ACCORDE = "ra";
    public static final String PROPERTY_ANNEE_ANTICIPATION = "ra.NBR_ANNEE_ANTICIPATION";
    public static final String PROPERTY_ANNEE_MONTANT_RAM = "ra.ANNEE_MONTANT_RAM";
    public static final String PROPERTY_ANNEE_NIVEAU = "bc.ANNEE_NIVEAU";
    public static final String PROPERTY_CLE_INFIRM_ATTEINTE_FCT = "bc.CLE_INFIRM_ATTEINT_FCT";
    public static final String PROPERTY_CODE_CAS_SPECIAL1 = "ra.CODE_CAS_SPEC1";

    public static final String PROPERTY_CODE_CAS_SPECIAL2 = "ra.CODE_CAS_SPEC2";
    public static final String PROPERTY_CODE_CAS_SPECIAL3 = "ra.CODE_CAS_SPEC3";
    public static final String PROPERTY_CODE_CAS_SPECIAL4 = "ra.CODE_CAS_SPEC4";
    public static final String PROPERTY_CODE_CAS_SPECIAL5 = "ra.CODE_CAS_SPEC5";
    public static final String PROPERTY_CODE_REV_SPLITTE = "bc.REV_SPLITTE";
    public static final String PROPERTY_CODE_REVENU = "bc.CODE_REVENU";
    public static final String PROPERTY_CODE_SURVIVANT_INVALIDE = "ra.SURVIVANT_INVALIDE";
    public static final String PROPERTY_DATE_DEBUT_ANTICIP = "ra.DD_ANTICIP";
    public static final String PROPERTY_DATE_DEBUT_DROIT = "ra.DD_DROIT";
    public static final String PROPERTY_DATE_FIN_DROIT = "ra.DF_DROIT";

    public static final String PROPERTY_DATE_REVOC_AJOURNEMENT = "ra.REVOC_AJOURN";
    public static final String PROPERTY_DEGRE_INVALIDITE = "bc.DEGRE_INVALID";
    // BC
    public static final String PROPERTY_DROIT_APPLIQUE = "bc.DROIT_APPLIQ";
    public static final String PROPERTY_DUREE_AJOURNEMENT = "ra.DUREE_AJOUR";
    public static final String PROPERTY_DUREE_COTI_AP_73 = "bc.DURE_COT_AP73";
    public static final String PROPERTY_DUREE_COTI_AV_73 = "bc.DURE_COT_AV73";
    public static final String PROPERTY_DUREE_COTI_CLASSE_AGE = "bc.DUREE_COT_CLS_AGE";
    public static final String PROPERTY_DUREE_COTI_ETRANGERE_AP_73 = "bc.DUR_COT_ETR_AP73";
    public static final String PROPERTY_DUREE_COTI_ETRANGERE_AV_73 = "bc.DUR_COT_ETR_AV73";
    public static final String PROPERTY_DUREE_COTI_RAM = "bc.DURE_COTI_RAM";
    public static final String PROPERTY_ECHELLE = "bc.ECHELLE";
    public static final String PROPERTY_FRACTION_RENTE = "ra.FRACT_RENTE";
    public static final String PROPERTY_GENRE_PREST = "ra.GENRE_PREST";
    // RA
    public static final String PROPERTY_ID_TIERS_BENEFICIAIRE = "ra.ID_TIERS_BENEFICIAIRE";
    public static final String PROPERTY_INVALID_PRECOCE = "bc.INV_PRECOCE";
    public static final String PROPERTY_MOIS_APP_AP_73 = "bc.MOIS_APP_AP73";
    public static final String PROPERTY_MOIS_APP_AV_73 = "bc.MOIS_APP_AV73";
    public static final String PROPERTY_MONTANT_BONUS_EDUCATIF = "bc.BONUS_EDU";
    public static final String PROPERTY_MONTANT_PRESTATION = "ra.MNT_PREST";
    public static final String PROPERTY_MONTANT_REDUCT_ANTICIP = "ra.MNT_REDUC_ANTICIP";
    public static final String PROPERTY_NBR_ANNEE_BONIF_TRANSITOIRE = "bc.ANNEE_BTR";
    public static final String PROPERTY_NBR_ANNEE_BTA = "bc.ANNEE_BTA";
    public static final String PROPERTY_NBR_ANNEE_BTE = "bc.ANNEE_BTE";
    public static final String PROPERTY_OAI = "bc.OAI";

    // Autres paramètres

    public static final String PROPERTY_RAM = "bc.RAM";

    public static final String PROPERTY_SUPP_CARRIERE = "bc.SUPP_CARR";
    public static final String PROPERTY_SUPPLEM_AJOURNEMENT = "ra.SUPP_AJOUR";

    public static final String PROPERTY_SURVEN_EVEN_ASSURE = "bc.SURV_EVE_ASS";
    public static final String PROPERTY_TRANSFERE_01_2001 = "params.TRANSFERE_01_2001";
    static {

        // noms des champs pour la rente accordée
        HashMap fields = new HashMap();

        fields.put(PROPERTY_ID_TIERS_BENEFICIAIRE, "idTiersBeneficiaire");
        fields.put(PROPERTY_GENRE_PREST, "codePrestation");
        fields.put(PROPERTY_FRACTION_RENTE, "fractionRente");
        fields.put(PROPERTY_MONTANT_PRESTATION, "montantPrestation");
        fields.put(PROPERTY_CODE_CAS_SPECIAL1, "codeCasSpeciaux1");
        fields.put(PROPERTY_CODE_CAS_SPECIAL2, "codeCasSpeciaux2");
        fields.put(PROPERTY_CODE_CAS_SPECIAL3, "codeCasSpeciaux3");
        fields.put(PROPERTY_CODE_CAS_SPECIAL4, "codeCasSpeciaux4");
        fields.put(PROPERTY_CODE_CAS_SPECIAL5, "codeCasSpeciaux5");
        fields.put(PROPERTY_DATE_DEBUT_DROIT, "dateDebutDroit");
        fields.put(PROPERTY_DATE_FIN_DROIT, "dateFinDroit");
        fields.put(PROPERTY_DUREE_AJOURNEMENT, "dureeAjournement");
        fields.put(PROPERTY_SUPPLEM_AJOURNEMENT, "supplementAjournement");
        fields.put(PROPERTY_DATE_REVOC_AJOURNEMENT, "dateRevocationAjournement");
        fields.put(PROPERTY_ANNEE_ANTICIPATION, "anneeAnticipation");
        fields.put(PROPERTY_MONTANT_REDUCT_ANTICIP, "montantReducationAnticipation");
        fields.put(PROPERTY_DATE_DEBUT_ANTICIP, "dateDebutAnticipation");
        fields.put(PROPERTY_CODE_SURVIVANT_INVALIDE, "codeSurvivantInvalide");
        fields.put(PROPERTY_ANNEE_MONTANT_RAM, "anneeMontantRAM");

        FIELDNAMES.put(HIST_RENTE_ACCORDES, fields);

        // noms des champs pour la base de calcul
        fields = new HashMap();

        fields.put(PROPERTY_CODE_REVENU, "revenuPrisEnCompte");
        fields.put(PROPERTY_RAM, "revenuAnnuelMoyen");
        fields.put(PROPERTY_DUREE_COTI_AV_73, "dureeCotiAvant73");
        fields.put(PROPERTY_DUREE_COTI_AP_73, "dureeCotiDes73");
        fields.put(PROPERTY_DUREE_COTI_RAM, "dureeRevenuAnnuelMoyen");
        fields.put(PROPERTY_DUREE_COTI_CLASSE_AGE, "anneeCotiClasseAge");
        fields.put(PROPERTY_ANNEE_NIVEAU, "anneeDeNiveau");
        fields.put(PROPERTY_ECHELLE, "echelleRente");
        fields.put(PROPERTY_OAI, "codeOfficeAi");
        fields.put(PROPERTY_DEGRE_INVALIDITE, "degreInvalidite");
        fields.put(PROPERTY_CLE_INFIRM_ATTEINTE_FCT, "cleInfirmiteAyantDroit");
        fields.put(PROPERTY_SURVEN_EVEN_ASSURE, "survenanceEvtAssAyantDroit");
        fields.put(PROPERTY_INVALID_PRECOCE, "isInvaliditePrecoce");
        fields.put(PROPERTY_DUREE_COTI_ETRANGERE_AV_73, "periodeAssEtrangerAv73");
        fields.put(PROPERTY_CODE_REV_SPLITTE, "isRevenuSplitte");
        fields.put(PROPERTY_NBR_ANNEE_BTE, "anneeBonifTacheEduc");
        fields.put(PROPERTY_NBR_ANNEE_BTA, "anneeBonifTacheAssistance");
        fields.put(PROPERTY_NBR_ANNEE_BONIF_TRANSITOIRE, "anneeBonifTransitoire");
        fields.put(PROPERTY_SUPP_CARRIERE, "supplementCarriere");
        fields.put(PROPERTY_MONTANT_BONUS_EDUCATIF, "bonificationTacheEducative");
        fields.put(PROPERTY_DUREE_COTI_ETRANGERE_AP_73, "periodeAssEtrangerDes73");
        fields.put(PROPERTY_DROIT_APPLIQUE, "droitApplique");

        FIELDNAMES.put(HIST_BASE_CALCUL, fields);

    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Map params;
    private GlobazValueObject voBC;
    private GlobazValueObject voRA;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    REHistoriqueRenteWrapper(GlobazValueObject bc, GlobazValueObject ra) {
        voBC = bc;
        voRA = ra;

    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut property
     * 
     * @param name
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut property
     */
    public String getProperty(String name) {

        Object value = null;
        if (name != null && name.startsWith(PREFIX_BASE_CALCUL)) {
            value = voBC.getProperty((String) ((HashMap) FIELDNAMES.get(HIST_BASE_CALCUL)).get(name));
        } else if (name != null && name.startsWith(PREFIX_RENTE_ACCORDE)) {
            value = voRA.getProperty((String) ((HashMap) FIELDNAMES.get(HIST_RENTE_ACCORDES)).get(name));
        } else if (params != null) {
            value = params.get(name);
        }

        if (value != null) {
            return value.toString();
        } else {
            return EMPTY_STRING;
        }
    }

    /**
     * @param params
     *            the params to set
     */
    public void setParams(Map params) {
        this.params = params;
    }
}
