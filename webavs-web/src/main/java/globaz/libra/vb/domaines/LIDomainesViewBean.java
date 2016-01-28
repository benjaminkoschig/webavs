package globaz.libra.vb.domaines;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.libra.db.domaines.LIDomaines;
import globaz.libra.db.groupes.LIGroupes;
import globaz.libra.db.groupes.LIGroupesManager;
import globaz.libra.db.utilisateurs.LIUtilisateursManager;
import java.util.Iterator;

public class LIDomainesViewBean extends LIDomaines implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getNbGroupeDomaine() throws Exception {

        LIGroupesManager grpMgr = new LIGroupesManager();
        grpMgr.setSession(getSession());
        grpMgr.setForIdDomaine(getIdDomaine());
        grpMgr.find();

        return String.valueOf(grpMgr.size());

    }

    public String getNbUsersDomaine() throws Exception {

        LIGroupesManager grpMgr = new LIGroupesManager();
        grpMgr.setSession(getSession());
        grpMgr.setForIdDomaine(getIdDomaine());
        grpMgr.find();

        int nbUsers = 0;

        for (Iterator iterator = grpMgr.iterator(); iterator.hasNext();) {
            LIGroupes groupe = (LIGroupes) iterator.next();

            LIUtilisateursManager usrMgr = new LIUtilisateursManager();
            usrMgr.setSession(getSession());
            usrMgr.setForIdGroupe(groupe.getIdGroupe());
            usrMgr.find();

            nbUsers += usrMgr.getSize();

        }

        return String.valueOf(nbUsers);

    }

}
