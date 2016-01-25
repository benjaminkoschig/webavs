package globaz.draco.translation;

import globaz.framework.controller.FWController;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersSystemCode;
import javax.servlet.http.HttpSession;

/**
 * Classe contenant les methodes utilis�e pour obtenir une liste de codesystem principalement utilis�e dans les jsp
 * depuis par customTag FWSystemCodeSelectTag.
 * 
 * @author: S�bastien Chappatte
 */
public class CodeSystem {

    public final static String CS_BOUCLEMENT_ACCOMPTE = "122003";

    // CodeSytem generic

    public final static String CS_COMPLEMENTAIRE = "122002";
    public final static String CS_CONTROLE_EMPLOYEUR = "122004";
    public final static String CS_LTN = "122005";
    public final static String CS_LTN_COMPLEMENT = "122006";
    // Etat declaration
    public final static String CS_OUVERT = "121001";
    public final static String CS_PARTIEL = "121002";
    // Type declaration
    public final static String CS_PRINCIPALE = "122001";
    public final static String CS_SALAIRE_DIFFERES = "122007";
    public final static String CS_TRAITE = "121003";
    private final static String SESSION_CONTROLLER_NAME = "objController";

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.08.2002 11:25:41)
     * 
     * @param code
     *            java.lang.String
     */
    public static String getCode(String code) {
        /*
         * FWParametersSystemCode cs = new FWParametersSystemCode(); cs.getCode(code); return
         * cs.getCurrentCodeUtilisateur().getCodeUtilisateur();
         */

        return code;
    }

    public static String getCodeLibelle(String code) {
        /*
         * FWParametersSystemCode cs = new FWParametersSystemCode(); cs.getCode(code); return
         * cs.getCurrentCodeUtilisateur().getCodeUtilisateur ()+"-"+cs.getCurrentCodeUtilisateur().getLibelle();
         */
        return code;
    }

    /**
     * Cette m�thode permet de r�cup�rer le libell� d'un code syt�me depuis un Bsession
     */
    public static String getLibelle(BSession session, String code) throws Exception {

        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession(session);
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getLibelle();

    }

    /**
     * Cette m�thode permet de r�cup�rer le libell� d'un code syt�me depuis un HttpSession
     */
    public static String getLibelle(HttpSession session, String code) throws Exception {

        BISession bSession = CodeSystem.getSession(session);
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession((BSession) bSession);
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getLibelle();

    }

    /**
     * Cette m�thode permet de r�cup�rer une session de HttpSession
     */
    public static BISession getSession(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = null;
        FWController controller = (FWController) httpSession.getAttribute(CodeSystem.SESSION_CONTROLLER_NAME);
        if (controller != null) {
            session = controller.getSession();
        } else {
            throw new Exception("Aucune session connect�e");
        }
        return session;
    }
}
