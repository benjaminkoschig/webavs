package globaz.leo.util;

import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.leo.constantes.ILEConstantes;
import java.util.ArrayList;

/**
 * @author ald
 * @since Créé le 29 mars 05
 */
public class LEUtil {

    /**
     * Renvoie le code système asssocié à un code utilisateur et un groupe.
     * 
     * @param Un
     *            object BTransaction.
     * @param Le
     *            code utilisateur.
     * @param Le
     *            groupe.
     * @return Le code système asssocié à un code utilisateur et un groupe
     */
    public static String getCodeSystemeLangue(BSession session) throws Exception {
        FWParametersSystemCodeManager systemCodeMng = new FWParametersSystemCodeManager();
        systemCodeMng.setSession(session);
        systemCodeMng.setForIdLangue(session.getIdLangue());
        systemCodeMng.setForIdGroupe(ILEConstantes.NOM_GROUPE_LANGUE);
        systemCodeMng.find();
        if (!systemCodeMng.hasErrors()) {
            if (systemCodeMng.getSize() > 0) {
                return ((FWParametersSystemCode) systemCodeMng.getEntity(0)).getIdCode();
            } else {
                // Pas de code système pour le code utilisateur
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Méthode qui retourne la langue Système
     * 
     * @param langueIso
     * @return
     */
    public static String getLangueSystemeByIso(String langueIso) {
        if ("fr".equals(langueIso)) {
            return ILEConstantes.CS_FRANCAIS;
        } else if ("de".equals(langueIso)) {
            return ILEConstantes.CS_ALLEMAND;
        } else {
            return ILEConstantes.CS_FRANCAIS;
        }
    }

    /**
     * 
     * @param session
     * @return
     * @throws Exception
     */
    public ArrayList getCsCodeProvListe(BSession session) throws Exception {
        FWParametersSystemCodeManager csCodeProvListe = null;
        csCodeProvListe = new FWParametersSystemCodeManager();
        csCodeProvListe.setForIdTypeCode(ILEConstantes.CS_PARAM_GEN_GROUP);
        csCodeProvListe.setSession(session);
        csCodeProvListe.find();
        ArrayList res = new ArrayList(csCodeProvListe.size());
        for (int i = 0; i < csCodeProvListe.size(); i++) {
            res.add(((FWParametersSystemCode) csCodeProvListe.getEntity(i)).getIdCode());
        }
        return res;
    }
}
