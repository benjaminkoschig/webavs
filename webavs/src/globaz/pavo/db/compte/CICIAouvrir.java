/*
 * Cr�� le 8 f�vr. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
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
