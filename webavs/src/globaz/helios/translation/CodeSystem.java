package globaz.helios.translation;

import globaz.framework.controller.FWController;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersSystemCode;
import javax.servlet.http.HttpSession;

/**
 * Classe contenant les methodes utilisée pour obtenir une liste de codesystem principalement utilisée dans les jsp
 * depuis par customTag FWSystemCodeSelectTag.
 * 
 * Date de création : (29.05.2002 18:29:26)
 * 
 * @author: Administrator
 */
public class CodeSystem {

    public final static String CS_AFF_COURS = "721004";

    // CodeSytem generic

    public final static String CS_AFF_DATE_VALEUR = "721003";
    public final static String CS_AFF_LIVRE = "721002";
    // Affichage écriture
    public final static String CS_AFF_PIECE_COMPTABLE = "721001";
    public final static String CS_ASCII_DELIMITE = "728002";

    public final static String CS_CENTRE_CHARGE = "726003";
    public final static String CS_CREDIT = "724002";
    // Debit credit
    public final static String CS_DEBIT = "724001";
    public final static String CS_DEFINITIF = "725002";

    public final static String CS_EXTOURNE_CREDIT = "724004";
    public final static String CS_EXTOURNE_DEBIT = "724003";
    // Ecriture vue
    public final static String CS_GENERAL = "726001";
    // Livre
    public final static String CS_LIVRE_1 = "730001";
    public final static String CS_LIVRE_2 = "730002";

    public final static String CS_MONNAIE_ETR = "726002";
    // Exportation
    public final static String CS_PAS_D_EXEPORTATION = "728001";

    // provisoire définitif
    public final static String CS_PROVISOIRE = "725001";
    public final static String CS_SHOW_MONNAIE_ETR = "722002";
    public final static String CS_SHOW_MONNAIE_SUISSE = "722001";
    public final static String CS_TRI_COMPTE_DATE = "729002";

    // Imprimer ecriture tri
    public final static String CS_TRI_DATE_COMPTE = "729001";
    public final static String CS_TRI_LIBELLE = "727002";

    // Soldes des Compte Tri
    public final static String CS_TRI_NUMERO_COMPTE = "727001";
    public final static String CS_TRI_PIECE_COMPTE_DATE = "729004";

    public final static String CS_TRI_PIECE_DATE_COMPTE = "729003";
    public final static String CS_VUE_CONSOLIDEE = "726004";
    private final static String SESSION_CONTROLLER_NAME = "objController";

    /**
     * Insérez la description de la méthode ici. Date de création : (08.08.2002 11:25:41)
     * 
     * @param code
     *            java.lang.String
     */
    public static String getCode(String code) {
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getCodeUtilisateur();
    }

    public static String getCodeLibelle(String code) {
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getCodeUtilisateur() + "-" + cs.getCurrentCodeUtilisateur().getLibelle();

    }

    public static String getLibelle(BSession session, String code) throws Exception {

        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession(session);
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getLibelle();

    }

    public static String getLibelle(HttpSession session, String code) throws Exception {

        BISession bSession = CodeSystem.getSession(session);
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession((BSession) bSession);
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getLibelle();

    }

    public static BISession getSession(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = null;
        FWController controller = (FWController) httpSession.getAttribute(CodeSystem.SESSION_CONTROLLER_NAME);
        if (controller != null) {
            session = controller.getSession();
        } else {
            throw new Exception("Aucune session connectée");
        }
        return session;
    }
}
