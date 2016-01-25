package globaz.musca.translation;

import globaz.framework.translation.FWTranslation;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAModuleFacturationManager;
import java.util.Vector;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (29.05.2002 18:29:26)
 * 
 * @author: Administrator
 */
public class CodeSystem {
    private static FWParametersSystemCodeManager lcsAction = null;
    private static FWParametersSystemCodeManager lcsCatNewFacture = null;
    private static FWParametersSystemCodeManager lcsCritereDecompte = null;
    private static FWParametersSystemCodeManager lcsEtat = null;

    private static FWParametersSystemCodeManager lcsModeImpression = null;

    private static FWParametersSystemCodeManager lcsModeRecouvrement = null;

    private static FWParametersSystemCodeManager lcsMotifIm = null;

    private static FWParametersSystemCodeManager lcsTriDecompte = null;

    private static FWParametersSystemCodeManager lcsTriDecomptePassage = null;

    private static FWParametersSystemCodeManager lcsTriPassage = null;

    private static FWParametersSystemCodeManager lcsTypeAfact = null;

    private static FWParametersSystemCodeManager lcsTypeFacturation = null;

    private static FWParametersSystemCodeManager lcsTypeFacture = null;

    private static FWParametersSystemCodeManager lcsTypeModule = null;

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

    /**
     * Insérez la description de la méthode ici. Date de création : (08.08.2002 11:25:41)
     * 
     * @param code
     *            java.lang.String
     */
    public static String getCodeLibelle(String code) {
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getCodeUtilisateur() + "-" + cs.getCurrentCodeUtilisateur().getLibelle();
    }

    public static FWParametersSystemCodeManager getLcsAction(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("MUACTION", session, CodeSystem.lcsAction);
    }

    public static FWParametersSystemCodeManager getLcsActionWithoutBlank(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeList("MUACTION", session, CodeSystem.lcsAction);
    }

    public static FWParametersSystemCodeManager getLcsCatNewFacture(HttpSession httpSession) throws Exception {
        BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("MUGRPAFA", session, CodeSystem.lcsCatNewFacture);
    }

    public static FWParametersSystemCodeManager getLcsCritereDecompte(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("MUCRIDECOM", session, CodeSystem.lcsCritereDecompte);
    }

    public static FWParametersSystemCodeManager getLcsCritereDecompteWithoutBlank(HttpSession httpSession)
            throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeList("MUCRIDECOM", session, CodeSystem.lcsCritereDecompte);
    }

    public static FWParametersSystemCodeManager getLcsEtat(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("MUETAT", session, CodeSystem.lcsEtat);
    }

    public static FWParametersSystemCodeManager getLcsEtatWithoutBlank(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeList("MUETAT", session, CodeSystem.lcsEtat);
    }

    public static FWParametersSystemCodeManager getLcsModeImpression(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeList("MUDECMOIMP", session, CodeSystem.lcsModeImpression);
    }

    public static FWParametersSystemCodeManager getLcsModeRecouvrement(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("MUMODRECOU", session, CodeSystem.lcsModeRecouvrement);
    }

    public static FWParametersSystemCodeManager getLcsModeRecouvrementWithoutBlank(HttpSession httpSession)
            throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeList("MUMODRECOU", session, CodeSystem.lcsModeRecouvrement);
    }

    public static FWParametersSystemCodeManager getLcsMotifIm(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeList("OSIIMINMO", session, CodeSystem.lcsMotifIm);
    }

    public static FWParametersSystemCodeManager getLcsTriDecompte(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("MUTRIDECOM", session, CodeSystem.lcsTriDecompte);
    }

    public static FWParametersSystemCodeManager getLcsTriDecomptePassage(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("MUTRIDEPAS", session, CodeSystem.lcsTriDecomptePassage);
    }

    public static FWParametersSystemCodeManager getLcsTriDecomptePassageWithoutBlank(HttpSession httpSession)
            throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeList("MUTRIDEPAS", session, CodeSystem.lcsTriDecomptePassage);
    }

    public static FWParametersSystemCodeManager getLcsTriDecompteWithoutBlank(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeList("MUTRIDECOM", session, CodeSystem.lcsTriDecompte);
    }

    public static FWParametersSystemCodeManager getLcsTriPassageWithoutBlank(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeList("MUTRIPASSA", session, CodeSystem.lcsTriPassage);
    }

    public static FWParametersSystemCodeManager getLcsTypeAfact(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("MUTYPAFACT", session, CodeSystem.lcsTypeAfact);
    }

    public static FWParametersSystemCodeManager getLcsTypeAfactWithoutBlank(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeList("MUTYPAFACT", session, CodeSystem.lcsTypeAfact);
    }

    public static FWParametersSystemCodeManager getLcsTypeFacturation(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("MUTYPEFACT", session, CodeSystem.lcsTypeFacturation);
    }

    public static FWParametersSystemCodeManager getLcsTypeFacturationWithoutBlank(HttpSession httpSession)
            throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeList("MUTYPEFACT", session, CodeSystem.lcsTypeFacturation);
    }

    public static FWParametersSystemCodeManager getLcsTypeFacture(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("OSICATSEC", session, CodeSystem.lcsTypeFacture);
    }

    public static FWParametersSystemCodeManager getLcsTypeFactureWithoutBlank(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeList("OSICATSEC", session, CodeSystem.lcsTypeFacture);
    }

    public static FWParametersSystemCodeManager getLcsTypeModule(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("MUTYPMODUL", session, CodeSystem.lcsTypeModule);
    }

    public static FWParametersSystemCodeManager getLcsTypeModuleWithoutBlank(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = CodeSystem.getSession(httpSession);
        return FWTranslation.getSystemCodeList("MUTYPMODUL", session, CodeSystem.lcsTypeModule);
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
     * Retourne le libellé d'un code système
     */
    public static String getLibelle(HttpSession session, String code) throws Exception {

        BISession bSession = CodeSystem.getSession(session);
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession((BSession) bSession);
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getLibelle();

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 16:20:35)
     * 
     * @return java.util.Vector
     * @param session
     *            globaz.globall.api.BISession
     */
    public static Vector<String[]> getListModulesFacturation(BISession session, boolean wantBlank) {
        // Initialiser le vecteur
        Vector<String[]> v = new Vector<String[]>();
        // Insérer un blank si nécessaire
        if (wantBlank) {
            String[] blank = { "", "" };
            v.add(blank);
        }
        // Récupérer les modules
        FAModuleFacturationManager mgr = new FAModuleFacturationManager();
        try {
            mgr.setISession(session);
            mgr.find();
            for (int i = 0; i < mgr.size(); i++) {
                FAModuleFacturation mod = (FAModuleFacturation) mgr.getEntity(i);
                // Remplir un array avec l'id et le libellé
                String[] data = new String[2];
                data[0] = mod.getIdModuleFacturation();
                data[1] = mod.getLibelle();
                v.add(data);
            }
        } catch (Exception e) {
            JadeLogger.error(mgr, e);
        }
        return v;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 16:20:35)
     * 
     * @return java.util.Vector
     * @param session
     *            globaz.globall.api.BISession
     */
    public static Vector<String[]> getListModulesFacturation(HttpSession session, boolean wantBlank) {
        try {
            return CodeSystem.getListModulesFacturation(CodeSystem.getSession(session), wantBlank);
        } catch (Exception e) {
            return null;
        }
    }

    public static globaz.globall.api.BISession getSession(HttpSession httpSession) throws Exception {

        globaz.globall.api.BISession session = null;
        globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) httpSession
                .getAttribute(CodeSystem.SESSION_CONTROLLER_NAME);
        if (controller != null) {
            session = controller.getSession();
        } else {
            throw new Exception("Aucune session connectée");
        }
        return session;
    }

    /**
     * Commentaire relatif au constructeur CodeSystem.
     */
    public CodeSystem(HttpSession httpSession) throws Exception {
        super();
    }
}
