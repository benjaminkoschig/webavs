package globaz.libra.vb.dossiers;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.security.FWSecurityLoginException;
import globaz.libra.db.domaines.LIDomaines;
import globaz.libra.db.dossiers.LIDossiers;
import globaz.libra.db.groupes.LIGroupes;
import globaz.libra.db.groupes.LIGroupesManager;
import globaz.libra.db.utilisateurs.LIUtilisateurs;
import globaz.libra.db.utilisateurs.LIUtilisateursManager;
import globaz.libra.utils.LIEcransUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LIDossiersViewBean extends LIDossiers implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    boolean isModifEcheances = false;

    Map sacUsers = new HashMap();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getDetailGestionnaire() throws FWSecurityLoginException, Exception {
        return LIEcransUtil.getDetailGestionnaire(getSession(), getIdGestionnaire());
    }

    public String getDetailTiersLigne() throws Exception {
        return LIEcransUtil.getDetailTiersLigne(getSession(), getIdTiers());
    }

    public String[] getGroupesList(String idDomaine) throws Exception {

        LIGroupesManager grpMgr = new LIGroupesManager();
        grpMgr.setSession(getSession());
        grpMgr.setForIdDomaine(idDomaine);
        grpMgr.find();

        String[] groupesList = new String[grpMgr.size() * 2];
        int i = 0;

        for (Iterator iterator = grpMgr.iterator(); iterator.hasNext();) {
            LIGroupes groupe = (LIGroupes) iterator.next();

            groupesList[i] = groupe.getIdGroupe();
            groupesList[i + 1] = groupe.getLibelleGroupe();

            i = i + 2;
        }

        return groupesList;
    }

    public String getLibelleDomaine() throws Exception {

        LIDomaines domaine = new LIDomaines();
        domaine.setSession(getSession());
        domaine.setIdDomaine(getIdDomaine());
        domaine.retrieve();

        return getSession().getCodeLibelle(domaine.getCsDomaine());

    }

    public Map getSacUsers() {
        return sacUsers;
    }

    public String[] getUsersList(String idGroupe) throws Exception {

        LIUtilisateursManager usrMgr = new LIUtilisateursManager();
        usrMgr.setSession(getSession());
        usrMgr.setForIdGroupe(idGroupe);
        usrMgr.find();

        String[] usersList = new String[usrMgr.size() * 2];
        int i = 0;

        for (Iterator iterator = usrMgr.iterator(); iterator.hasNext();) {
            LIUtilisateurs user = (LIUtilisateurs) iterator.next();

            usersList[i] = user.getIdUtilisateur();
            usersList[i + 1] = user.getIdUtilisateurExterne();

            i = i + 2;
        }

        return usersList;

    }

    public boolean isModifEcheances() {
        return isModifEcheances;
    }

    public void loadUsers() throws Exception {

        LIGroupesManager grpMgr = new LIGroupesManager();
        grpMgr.setSession(getSession());
        grpMgr.find();

        for (Iterator iterator = grpMgr.iterator(); iterator.hasNext();) {
            LIGroupes groupe = (LIGroupes) iterator.next();

            LIUtilisateursManager usrMgr = new LIUtilisateursManager();
            usrMgr.setSession(getSession());
            usrMgr.setForIdGroupe(groupe.getIdGroupe());
            usrMgr.find();

            List listUsers = new LinkedList();

            for (Iterator iterator2 = usrMgr.iterator(); iterator2.hasNext();) {
                LIUtilisateurs user = (LIUtilisateurs) iterator2.next();

                listUsers.add(user.getIdUtilisateur());
                listUsers.add(user.getIdUtilisateurExterne());

            }

            sacUsers.put(groupe.getIdGroupe(), listUsers);

        }

    }

    public void setIsModifEcheances(boolean isModifEcheances) {
        this.isModifEcheances = isModifEcheances;
    }

    public void setSacUsers(Map sacUsers) {
        this.sacUsers = sacUsers;
    }

}
