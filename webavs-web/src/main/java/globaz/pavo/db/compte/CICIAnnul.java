package globaz.pavo.db.compte;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BSession;

public class CICIAnnul {
    public static boolean existeCI(String nss, BSession sessionMgr) throws Exception {
        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
        ciMgr.setSession(sessionMgr);
        ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        ciMgr.setForNumeroAvs(NSUtil.unFormatAVS(nss).trim());
        ciMgr.find();
        if (ciMgr.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
