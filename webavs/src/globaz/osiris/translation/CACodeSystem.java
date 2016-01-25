/*
 * Créé le 19 janv. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.translation;

import globaz.framework.controller.FWController;
import globaz.framework.translation.FWTranslation;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import javax.servlet.http.HttpSession;

/**
 * @author dda Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CACodeSystem {

    private static FWParametersSystemCodeManager categories = null;
    private static FWParametersSystemCodeManager codeReference = null;
    public final static String COMPTE_AUXILIAIRE = "235001";

    public final static String CS_ALLEMAND = "503002";
    public final static String CS_ANGLAIS = "503003";
    public static final String CS_BLOQUAGE_ARD = "204005";
    public final static String CS_DECOMPTE_LTN = "204104";
    public final static String CS_DOMAINE_CA = "241001";
    public final static String CS_ESPAGNOL = "503006";

    // Langue
    public final static String CS_FRANCAIS = "503001";

    public final static String CS_IRRECOUVRABLE = "204020";
    public final static String CS_ITALIEN = "503004";
    public final static String CS_PLAN_RECOUVREMENT = "204001";
    public final static String CS_PORTUGAIS = "503007";
    public final static String CS_RENTIER = "204021";
    public final static String CS_ROMANCHE = "503005";
    public final static String CS_SURSIS_PMT_PART_PENALE = "204052";

    public final static String CS_TYPE_RAPPEL = "242001";

    public final static String CS_TYPE_REQUISITION_POURSUITE = "242003";
    public final static String CS_TYPE_REQUISITION_SAISIE = "242004";
    public final static String CS_TYPE_REQUISITION_VENTE = "242005";
    public final static String CS_TYPE_SOMMATION = "242002";
    public final static String CS_TYPE_SURSIS_DECISION = "242006";
    public final static String CS_TYPE_SURSIS_ECHEANCIER = "242009";
    public final static String CS_TYPE_SURSIS_RAPPEL = "242008";
    public final static String CS_TYPE_SURSIS_VOIES_DROIT = "242007";
    public final static String CS_TYPE_TAXATION = "242010";
    private static FWParametersSystemCodeManager genreComptes = null;
    private static FWParametersSystemCodeManager information = null;

    private static FWParametersSystemCodeManager lcsGenreInteret = null;
    private static FWParametersSystemCodeManager lcsLangue = null;
    private static FWParametersSystemCodeManager modeCompensation = null;
    private static FWParametersSystemCodeManager qualiteDebiteur = null;
    private final static String SESSION_CONTROLLER_NAME = "objController";
    private static FWParametersSystemCodeManager triSpecial = null;

    public static FWParametersSystemCodeManager getCategories(BSession session) throws Exception {
        return FWTranslation.getSystemCodeListSup("VETYPEAFFI", session, CACodeSystem.categories);
    }

    public static FWParametersSystemCodeManager getCodeReference(BSession session) throws Exception {
        return FWTranslation.getSystemCodeListSup("OSIREFRUB", session, CACodeSystem.codeReference);
    }

    /**
     * Retourne le code utilisateur d'un code système.
     * 
     * @param session
     * @param code
     * @return
     * @throws Exception
     */
    public static String getCodeUtilisateur(BSession session, String code) throws Exception {
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession(session);
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getCodeUtilisateur();
    }

    public static FWParametersSystemCodeManager getGenreComptes(BSession session) throws Exception {
        return FWTranslation.getSystemCodeListSup("OSIGENRE", session, CACodeSystem.genreComptes);
    }

    public static FWParametersSystemCodeManager getInformation(BSession session) throws Exception {
        return FWTranslation.getSystemCodeListSup("OSIINFOCA", session, CACodeSystem.information);
    }

    public static FWParametersSystemCodeManager getLcsGenreInteret(BSession session) throws Exception {
        return FWTranslation.getSystemCodeListSup("OSIIMGEIN", session, CACodeSystem.lcsGenreInteret);
    }

    public static FWParametersSystemCodeManager getLcsLangue(BSession session) throws Exception {
        return FWTranslation.getSystemCodeListSup("PYLANGUE", session, CACodeSystem.lcsLangue);
    }

    public static FWParametersSystemCodeManager getLcsMotifDecisionInteret(BSession session) throws Exception {
        return FWTranslation.getSystemCodeListSup("OSIIMINMO", session, CACodeSystem.lcsGenreInteret);
    }

    /**
     * Retourne le libellé d'un code système.
     * 
     * @param session
     * @param code
     * @return
     * @throws Exception
     */
    public static String getLibelle(BSession session, String code) throws Exception {
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession(session);
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getLibelle();
    }

    /**
     * Retourne le libellé d'un code système.
     * 
     * @param session
     * @param code
     * @return
     * @throws Exception
     */
    public static String getLibelle(HttpSession session, String code) throws Exception {
        BISession bSession = CACodeSystem.getSession(session);
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession((BSession) bSession);
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getLibelle();
    }

    public static FWParametersSystemCodeManager getModeCompensation(BSession session) throws Exception {
        return FWTranslation.getSystemCodeListSup("OSIMODCOM", session, CACodeSystem.modeCompensation);
    }

    /**
     * Retourne un manager pour la liste des natures de versements.
     * 
     * @param session
     * @return
     */
    public static FWParametersSystemCodeManager getNatureVersementsManager(BSession session) {
        FWParametersSystemCodeManager manager = new FWParametersSystemCodeManager();
        manager.setSession(session);
        manager.getListeCodes("OSIORDLIV", session.getIdLangue());

        return manager;
    }

    public static FWParametersSystemCodeManager getQualiteDebiteur(BSession session) throws Exception {
        return FWTranslation.getSystemCodeListSup("OSIQUADEBI", session, CACodeSystem.qualiteDebiteur);
    }

    public static BSession getSession(HttpSession httpSession) throws Exception {
        BSession bSession = null;
        FWController controller = (FWController) httpSession.getAttribute(CACodeSystem.SESSION_CONTROLLER_NAME);
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

    public static FWParametersSystemCodeManager getTriSpecial(BSession session) throws Exception {
        return FWTranslation.getSystemCodeListSup("TRISPECIAL", session, CACodeSystem.triSpecial);
    }

    /**
     * @param httpSession
     * @throws Exception
     */
    public CACodeSystem(HttpSession httpSession) throws Exception {
        super();
    }

}
