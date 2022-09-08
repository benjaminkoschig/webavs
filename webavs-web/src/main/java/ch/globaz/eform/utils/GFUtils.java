package ch.globaz.eform.utils;

import ch.globaz.common.util.NSSUtils;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TITiersViewBean;

public final class GFUtils {
    private GFUtils() {}

    public static String formatAffilier(String nss, BSession session) throws Exception {
        TIPersonneAvsManager mgr = new TIPersonneAvsManager();
        mgr.setISession(session);
        mgr.setForNumAvsActuel(NSSUtils.formatNss(nss));
        mgr.setForIncludeInactif(true);
        mgr.find(BManager.SIZE_NOLIMIT);

        if (mgr.size() == 0) {
            return "";
        } else {
            TITiersViewBean tiers = (TITiersViewBean) mgr.getFirstEntity();
            return tiers.getDesignation1() + "/" + tiers.getDesignation2() + "/" + tiers.getNumAffilieActuel() + "/" + tiers.getSexe() + "/" + tiers.getPays().getCodeIso();
        }
    }

    public static BSpy formatSpy(String spy) {
        return new BSpy(spy);
    }
}
