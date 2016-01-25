package globaz.pavo.translation;

import globaz.framework.translation.FWTranslation;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JAUtil;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (18.10.2002 08:59:14)
 * 
 * @author: Administrator
 */
public class CodeSystem {
    private static FWParametersSystemCodeManager lcsBrancheEconomique = null;
    private static FWParametersSystemCodeManager lcsCode = null;

    private static FWParametersSystemCodeManager lcsCodeSpecial = null;

    private static FWParametersSystemCodeManager lcsExtourne = null;

    private static FWParametersSystemCodeManager lcsGenreEcriture = null;

    private static FWParametersSystemCodeManager lcsIdEtatDossier = null;

    private static FWParametersSystemCodeManager lcsIdEtatJournal = null;

    private static FWParametersSystemCodeManager lcsIdEtatMandat = null;

    private static FWParametersSystemCodeManager lcsIdGenreSplitting = null;

    private static FWParametersSystemCodeManager lcsIdMotifSplitting = null;

    private static FWParametersSystemCodeManager lcsIdTypeCompte = null;

    private static FWParametersSystemCodeManager lcsIdTypeInscription = null;

    private static FWParametersSystemCodeManager lcsPaysOrigineId = null;

    private static FWParametersSystemCodeManager lcsRegistre = null;

    private final static String SESSION_CONTROLLER_NAME = "objController";

    /**
     * Insérez la description de la méthode ici. Date de création : (08.08.2002 11:25:41)
     * 
     * @param code
     *            java.lang.String
     */
    public static String getCodeUtilisateur(String code, BSession session) {
        if (JAUtil.isIntegerEmpty(code)) {
            return "";
        }
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession(session);
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getCodeUtilisateur();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.08.2002 11:25:41)
     * 
     * @param code
     *            java.lang.String
     */
    public static String getCodeUtilisateur(String code, HttpSession httpSession) {
        String codeUtilisateur;
        try {
            codeUtilisateur = getCodeUtilisateur(code, (BSession) getSession(httpSession));
        } catch (Exception e) {
            // valeur par défaut
            codeUtilisateur = "";
        }
        return codeUtilisateur;
    }

    // Méthode spécial retournant 0 si le code système est nul
    // (la méthode utilitaire de récupération de CU retourne "" si aucun code
    // n'est trouvé)
    public static String getCodeUtilisateurWithZero(String code, HttpSession session) {
        if (JAUtil.isIntegerEmpty(code)) {
            return "0";
        } else {
            return getCodeUtilisateur(code, session);
        }
    }

    public static FWParametersSystemCodeManager getLcsBrancheEconomique(HttpSession httpSession) throws Exception {
        BISession bSession = getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("CIBRAECO", bSession, lcsBrancheEconomique);
    }

    public static FWParametersSystemCodeManager getLcsBrancheEconomiqueWithoutBlank(HttpSession httpSession)
            throws Exception {
        BISession bSession = getSession(httpSession);
        return FWTranslation.getSystemCodeList("CIBRAECO", bSession, lcsBrancheEconomique);
    }

    public static FWParametersSystemCodeManager getLcsCode(HttpSession httpSession) throws Exception {
        BISession bSession = getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("CICODAMO", bSession, lcsCode);
    }

    public static FWParametersSystemCodeManager getLcsCodeSpecial(HttpSession httpSession) throws Exception {
        BISession bSession = getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("CICODSPE", bSession, lcsCodeSpecial);
    }

    public static FWParametersSystemCodeManager getLcsCodeSpecialWithoutBlank(HttpSession httpSession) throws Exception {
        BISession bSession = getSession(httpSession);
        return FWTranslation.getSystemCodeList("CICODSPE", bSession, lcsCodeSpecial);
    }

    public static FWParametersSystemCodeManager getLcsCodeWithoutBlank(HttpSession httpSession) throws Exception {
        BISession bSession = getSession(httpSession);
        return FWTranslation.getSystemCodeList("CICODAMO", bSession, lcsCode);
    }

    public static FWParametersSystemCodeManager getLcsExtourne(HttpSession httpSession) throws Exception {
        BISession bSession = getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("CICODEXT", bSession, lcsExtourne);
    }

    public static FWParametersSystemCodeManager getLcsExtourneWithoutBlank(HttpSession httpSession) throws Exception {
        BISession bSession = getSession(httpSession);
        return FWTranslation.getSystemCodeList("CICODEXT", bSession, lcsExtourne);
    }

    public static FWParametersSystemCodeManager getLcsGenreEcriture(HttpSession httpSession) throws Exception {
        BISession bSession = getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("CICODGEN", bSession, lcsGenreEcriture);
    }

    public static FWParametersSystemCodeManager getLcsGenreEcritureWithoutBlank(HttpSession httpSession)
            throws Exception {
        BISession bSession = getSession(httpSession);
        return FWTranslation.getSystemCodeList("CICODGEN", bSession, lcsGenreEcriture);
    }

    public static FWParametersSystemCodeManager getLcsIdEtatDossier(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("CIETADOS", session, lcsIdEtatDossier);
    }

    public static FWParametersSystemCodeManager getLcsIdEtatDossierWithoutBlank(HttpSession httpSession)
            throws Exception {
        globaz.globall.api.BISession session = getSession(httpSession);
        return FWTranslation.getSystemCodeList("CIETADOS", session, lcsIdEtatDossier);
    }

    public static FWParametersSystemCodeManager getLcsIdEtatJournal(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("CIETAJOU", session, lcsIdEtatJournal);
    }

    public static FWParametersSystemCodeManager getLcsIdEtatJournalWithoutBlank(HttpSession httpSession)
            throws Exception {
        globaz.globall.api.BISession session = getSession(httpSession);
        return FWTranslation.getSystemCodeList("CIETAJOU", session, lcsIdEtatJournal);
    }

    public static FWParametersSystemCodeManager getLcsIdEtatMandat(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("CIETAMAN", session, lcsIdEtatMandat);
    }

    public static FWParametersSystemCodeManager getLcsIdEtatMandatWithoutBlank(HttpSession httpSession)
            throws Exception {
        globaz.globall.api.BISession session = getSession(httpSession);
        return FWTranslation.getSystemCodeList("CIETAMAN", session, lcsIdEtatMandat);
    }

    public static FWParametersSystemCodeManager getLcsIdGenreSplitting(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("CIGENSPL", session, lcsIdGenreSplitting);
    }

    public static FWParametersSystemCodeManager getLcsIdGenreSplittingWithoutBlank(HttpSession httpSession)
            throws Exception {
        globaz.globall.api.BISession session = getSession(httpSession);
        return FWTranslation.getSystemCodeList("CIGENSPL", session, lcsIdGenreSplitting);
    }

    public static FWParametersSystemCodeManager getLcsIdMotifSplitting(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("CIMOTSPL", session, lcsIdMotifSplitting);
    }

    public static FWParametersSystemCodeManager getLcsIdMotifSplittingWithoutBlank(HttpSession httpSession)
            throws Exception {
        globaz.globall.api.BISession session = getSession(httpSession);
        return FWTranslation.getSystemCodeList("CIMOTSPL", session, lcsIdMotifSplitting);
    }

    public static FWParametersSystemCodeManager getLcsIdTypeCompte(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("CITYPCOM", session, lcsIdTypeCompte);
    }

    public static FWParametersSystemCodeManager getLcsIdTypeCompteWithoutBlank(HttpSession httpSession)
            throws Exception {
        globaz.globall.api.BISession session = getSession(httpSession);
        return FWTranslation.getSystemCodeList("CITYPCOM", session, lcsIdTypeCompte);
    }

    public static FWParametersSystemCodeManager getLcsIdTypeInscription(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("CITYPINS", session, lcsIdTypeInscription);
    }

    public static FWParametersSystemCodeManager getLcsIdTypeInscriptionWithoutBlank(HttpSession httpSession)
            throws Exception {
        globaz.globall.api.BISession session = getSession(httpSession);
        return FWTranslation.getSystemCodeList("CITYPINS", session, lcsIdTypeInscription);
    }

    public static FWParametersSystemCodeManager getLcsPaysOrigineId(HttpSession httpSession) throws Exception {
        BISession bSession = getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("CIPAYORI", bSession, lcsPaysOrigineId);
    }

    public static FWParametersSystemCodeManager getLcsPaysOrigineIdWithoutBlank(HttpSession httpSession)
            throws Exception {
        BISession bSession = getSession(httpSession);
        return FWTranslation.getSystemCodeList("CIPAYORI", bSession, lcsPaysOrigineId);
    }

    public static FWParametersSystemCodeManager getLcsRegistre(HttpSession httpSession) throws Exception {
        BISession bSession = getSession(httpSession);
        return FWTranslation.getSystemCodeListSup("CIREGIST", bSession, lcsRegistre);
    }

    public static FWParametersSystemCodeManager getLcsRegistreWithoutBlank(HttpSession httpSession) throws Exception {
        BISession bSession = getSession(httpSession);
        return FWTranslation.getSystemCodeList("CIREGIST", bSession, lcsRegistre);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.08.2002 11:25:41)
     * 
     * @param code
     *            java.lang.String
     */
    public static String getLibelle(String code, BSession session) {
        if (JAUtil.isIntegerEmpty(code)) {
            return "";
        }
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession(session);
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getLibelle();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.08.2002 11:25:41)
     * 
     * @param code
     *            java.lang.String
     */
    public static String getLibelle(String code, HttpSession httpSession) {
        String libelle;
        try {
            libelle = getLibelle(code, (BSession) getSession(httpSession));
        } catch (Exception e) {
            // valeur par défaut
            libelle = "unknow";
        }
        return libelle;
    }

    public static globaz.globall.api.BISession getSession(HttpSession httpSession) throws Exception {

        globaz.globall.api.BISession session = null;
        globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) httpSession
                .getAttribute(SESSION_CONTROLLER_NAME);
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
    public CodeSystem() {
        super();
    }
}
