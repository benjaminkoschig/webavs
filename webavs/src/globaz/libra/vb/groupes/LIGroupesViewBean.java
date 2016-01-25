package globaz.libra.vb.groupes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.libra.db.domaines.LIDomaines;
import globaz.libra.db.domaines.LIDomainesManager;
import globaz.libra.db.groupes.LIGroupes;
import globaz.libra.db.utilisateurs.LIUtilisateursManager;
import java.util.Iterator;

/**
 * 
 * @author HPE
 * 
 */
public class LIGroupesViewBean extends LIGroupes implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String[] getDomainesList(boolean wantBlank) throws Exception {

        LIDomainesManager domMgr = new LIDomainesManager();
        domMgr.setSession(getSession());
        domMgr.find();

        String[] listDomaines;
        int i = 0;

        if (wantBlank) {
            listDomaines = new String[(domMgr.size() * 2) + 2];
            listDomaines[i] = "";
            listDomaines[i + 1] = "";

            i = i + 2;
        } else {
            listDomaines = new String[domMgr.size() * 2];
        }

        for (Iterator iterator = domMgr.iterator(); iterator.hasNext();) {
            LIDomaines domaine = (LIDomaines) iterator.next();

            listDomaines[i] = domaine.getIdDomaine();
            listDomaines[i + 1] = getSession().getCodeLibelle(domaine.getCsDomaine());

            i = i + 2;
        }

        return listDomaines;

    }

    public String getLibelleDomaine(String idDomaine) throws Exception {

        LIDomaines domaine = new LIDomaines();
        domaine.setSession(getSession());
        domaine.setIdDomaine(idDomaine);
        domaine.retrieve();

        return getSession().getCodeLibelle(domaine.getCsDomaine());

    }

    public String getNbUsersGroupe() throws Exception {

        LIUtilisateursManager usrMgr = new LIUtilisateursManager();
        usrMgr.setSession(getSession());
        usrMgr.setForIdGroupe(getIdGroupe());
        usrMgr.find();

        return String.valueOf(usrMgr.size());

    }

}
