package globaz.phenix.translation;

import globaz.framework.controller.FWController;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.jade.client.util.JadeStringUtil;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (29.05.2002 18:29:26)
 * 
 * @author: Administrator
 */
public class CodeSystem {
    // Catalogue de textes
    public final static String CS_DOMAINE_CP = "616001";
    public final static String CS_TYPE_ACOMPTE_IND = "615004";
    public final static String CS_TYPE_ACOMPTE_NAC = "615009";
    public final static String CS_TYPE_DECISION = "615005";
    public final static String CS_TYPE_DECISION_IND = "615006";
    public final static String CS_TYPE_DECISION_NAC = "615008";
    public final static String CS_TYPE_LETTRE_COUPLE_MINIMUM = "615002";
    public final static String CS_TYPE_LETTRE_IMPUTATION = "615003";
    public final static String CS_TYPE_LETTRE_REMISE = "615010";
    public final static String CS_TYPE_LETTRE_SALARIE_DISPENSE = "615001";
    public final static String CS_TYPE_TABLEAU_COTISATION = "615007";
    public final static String CS_VERSO_ACO_IND = "99000050000";
    public final static String CS_VERSO_ACO_NAC = "99000040000";
    public final static String CS_VERSO_DEF_IND = "99000030000";
    public final static String CS_VERSO_DEF_NAC = "99000020000";
    public final static String CS_VERSO_GEN = "99000110000";
    public final static String CS_VERSO_GEN_ACO = "99000140000";
    public final static String CS_VERSO_GEN_DEF = "99000130000";
    public final static String CS_VERSO_GEN_IND = "99000090000";
    public final static String CS_VERSO_GEN_NAC = "99000080000";
    public final static String CS_VERSO_GEN_OPP = "99000100000";
    public final static String CS_VERSO_GEN_PRO = "99000120000";
    public final static String CS_VERSO_OPP_IND = "99000070000";
    public final static String CS_VERSO_OPP_NAC = "99000060000";
    public final static String CS_VERSO_PRO_IND = "99000010000";
    // Verso
    public final static String CS_VERSO_PRO_NAC = "99000000000";

    private final static String SESSION_CONTROLLER_NAME = "objController";

    /**
     * Insérez la description de la méthode ici. Date de création : (08.08.2002 11:25:41)
     * 
     * @param code
     *            java.lang.String
     */
    public static String getCode(BSession session, String code) throws Exception {
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession(session);
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getCodeUtilisateur();
    }

    /*
     * Retourne le code + le libellé d'un code système
     */
    public static String getCodeLibelle(HttpSession session, String code) throws Exception {

        BISession bSession = CodeSystem.getSession(session);
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession((BSession) bSession);
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getCodeUtilisateur() + "-" + cs.getCurrentCodeUtilisateur().getLibelle();

    }

    /**
     * Renvoie la liste des codes utilisateurs attachés au code.
     * 
     * @return la liste des codes utilisateurs
     */
    public static String getCodeSysteme(BSession session, BTransaction transaction, String famille,
            String codeUtilisateur, String langue) throws Exception {
        if (!JadeStringUtil.isEmpty(famille) && !JadeStringUtil.isEmpty(codeUtilisateur)) {
            if (JadeStringUtil.isEmpty(langue)) {
                langue = "F";
            }
            FWParametersUserCode userCode = new FWParametersUserCode();
            userCode.setAlternateKey(FWParametersUserCode.ALT_KEY_TYPE_USER);
            userCode.setCodeUtilisateur(codeUtilisateur);
            userCode.setIdLangue(langue);
            userCode.setTypeCode(famille);
            userCode.setSession(session);
            userCode.retrieve(transaction);
            if (!userCode.isNew()) {
                return userCode.getIdCodeSysteme();
            }
        }
        transaction.addErrors("Impossible de retrouver le code système pour " + famille + ", " + codeUtilisateur + ".");
        return "";
    }

    /*
     * Retourne le code utilisateur d'un code système
     */
    public static String getCodeUtilisateur(BSession session, String code) throws Exception {
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession(session);
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getCodeUtilisateur();

    }

    /*
     * Retourne le Code Utilisateur d'un code système
     */
    public static String getCodeUtilisateur(HttpSession session, String code) throws Exception {
        BISession bSession = CodeSystem.getSession(session);
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession((BSession) bSession);
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getCodeUtilisateur();

    }

    /*
     * Retourne le libellé d'un code système
     */
    public static String getLibelle(BSession session, String code) throws Exception {
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession(session);
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getLibelle();

    }

    /*
     * Retourne le libellé d'un code système suivant la langue ISO ex: FR, IT, DE
     */
    public static String getLibelle(BSession session, String code, String idLangue) throws Exception {
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession(session);
        cs.getCode(code);
        // Le code utilisateur n'utilise que la première lettre de l'idLangue
        // ISO
        return cs.getCodeUtilisateur(idLangue.substring(0, 1)).getLibelle();

    }

    /*
     * Retourne le libellé d'un code système
     */
    public static String getLibelle(HttpSession session, String code) throws Exception {

        BISession bSession = CodeSystem.getSession(session);
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession((BSession) bSession);
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getLibelle();

    }

    /*
     * Retourne le libellé court d'un code système
     */
    public static String getLibelleCourt(BSession session, String code) throws Exception {
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession(session);
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getCodeUtilisateur();

    }

    public static BSession getSession(HttpSession httpSession) throws Exception {

        BSession bSession = null;
        FWController controller = (FWController) httpSession.getAttribute(CodeSystem.SESSION_CONTROLLER_NAME);
        if (controller != null) {
            bSession = (BSession) controller.getSession();
        } else {

            // si on a une erreur, et que l'on arrive pas a obtenir la session,
            // j'en cree une par defaut se qui evite une erreur.
            // (mais on aura pas forcement la bonne langue...) - oca
            bSession = new BSession();
        }
        return bSession;
    }

    /**
     * Commentaire relatif au constructeur CodeSystem.
     */
    public CodeSystem(HttpSession httpSession) throws Exception {
        super();
    }
}
