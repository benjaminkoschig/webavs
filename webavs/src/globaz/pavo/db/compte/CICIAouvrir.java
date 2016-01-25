/*
 * Créé le 8 févr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.pavo.db.compte;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BSession;

public class CICIAouvrir {
    public static boolean existeCI(String nss, BSession sessionMgr) throws Exception {
        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
        ciMgr.setSession(sessionMgr);
        ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        ciMgr.setForNumeroAvs(NSUtil.unFormatAVS(nss).trim());
        if (ciMgr.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
