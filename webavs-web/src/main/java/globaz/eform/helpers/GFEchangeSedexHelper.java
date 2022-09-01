package globaz.eform.helpers;

import globaz.eform.translation.CodeSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;

import java.util.Vector;
import java.util.stream.IntStream;

public class GFEchangeSedexHelper {
    public static Vector getCaisseData(BSession objSession) throws Exception {
        TIAdministrationManager admAdrMgr = new TIAdministrationManager();
        admAdrMgr.setSession(objSession);
        admAdrMgr.setForGenreAdministration(CodeSystem.GENRE_ADMIN_CAISSE_COMP);
        admAdrMgr.orderByCodeAdministration();
        admAdrMgr.find(BManager.SIZE_NOLIMIT);

        Vector listCaisse = new Vector();

        IntStream.range(0, admAdrMgr.size()).forEach(i -> {
            TIAdministrationViewBean admnVb = (TIAdministrationViewBean) admAdrMgr.get(i);

            listCaisse.add(new String[] { admnVb.getCodeAdministration(), admnVb.getNom() });
        });

        return listCaisse;
    }
}
