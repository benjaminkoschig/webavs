package globaz.libra.vb.formules;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.libra.db.domaines.LIDomaines;
import globaz.libra.db.domaines.LIDomainesManager;
import globaz.libra.db.formules.LIFormuleJoint;
import java.util.ArrayList;
import java.util.Iterator;

public class LIFormulesViewBean extends LIFormuleJoint implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Return les domaines existants
     */
    public String[] getDomaines() throws Exception {

        LIDomainesManager domMgr = new LIDomainesManager();
        domMgr.setSession(getSession());
        domMgr.find();

        boolean isAllRight = true;

        for (Iterator iterator = domMgr.iterator(); iterator.hasNext();) {
            LIDomaines domaine = (LIDomaines) iterator.next();

            if (!getSession().hasRight(domaine.getNomApplication(), globaz.framework.secure.FWSecureConstants.READ)) {
                isAllRight = false;
            }

        }

        ArrayList domaines = new ArrayList();

        if (isAllRight) {
            domaines.add("");
            domaines.add("");
        }

        for (Iterator iterator = domMgr.iterator(); iterator.hasNext();) {
            LIDomaines domaine = (LIDomaines) iterator.next();

            if (getSession().hasRight(domaine.getNomApplication(), globaz.framework.secure.FWSecureConstants.READ)) {
                domaines.add(domaine.getIdDomaine());
                domaines.add(getSession().getCodeLibelle(domaine.getCsDomaine()));
            }
        }

        String[] domainesTab = new String[domaines.size()];

        int i = 0;

        for (Iterator iterator = domaines.iterator(); iterator.hasNext();) {
            String object = (String) iterator.next();

            domainesTab[i] = object;
            i += 1;

        }

        return domainesTab;
    }

}
