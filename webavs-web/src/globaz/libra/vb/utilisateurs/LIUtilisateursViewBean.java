package globaz.libra.vb.utilisateurs;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.libra.db.domaines.LIDomaines;
import globaz.libra.db.domaines.LIDomainesManager;
import globaz.libra.db.groupes.LIGroupes;
import globaz.libra.db.groupes.LIGroupesManager;
import globaz.libra.db.utilisateurs.LIUtilisateurs;
import globaz.libra.utils.LIJadeUserService;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class LIUtilisateursViewBean extends LIUtilisateurs implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Object[] METHODES_SEL_USER = new Object[] { new String[] { "idUserFX", "id" } };

    private String idUserFX = new String();

    boolean isRetourDepuisPyxis = false;

    Map sacGroupes = new HashMap();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getDetailUserExterne() throws Exception {

        if (JadeStringUtil.isBlankOrZero(getIdUtilisateurExterne())) {
            return "";
        } else {
            JadeUser userFX = LIJadeUserService.getInstance().loadByUserId(getIdUtilisateurExterne());
            return userFX.getFirstname() + " " + userFX.getLastname();
        }

    }

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

    public String[] getGroupesList(String idDomaine) throws Exception {

        LIGroupesManager grpMgr = new LIGroupesManager();
        grpMgr.setSession(getSession());
        grpMgr.setForIdDomaine(idDomaine);
        grpMgr.find();

        String[] listGroupes = new String[(grpMgr.size() * 2)];
        int i = 0;

        for (Iterator iterator = grpMgr.iterator(); iterator.hasNext();) {
            LIGroupes groupe = (LIGroupes) iterator.next();

            listGroupes[i] = groupe.getIdGroupe();
            listGroupes[i + 1] = groupe.getLibelleGroupe();

            i = i + 2;
        }

        return listGroupes;

    }

    public String getIdDomaine(String idGroupe) throws Exception {

        LIGroupes groupe = new LIGroupes();
        groupe.setSession(getSession());
        groupe.setIdGroupe(idGroupe);
        groupe.retrieve();

        return groupe.getIdDomaine();

    }

    public String getIdUserFX() {
        return idUserFX;
    }

    public String getLibelleDomaine(String idGroupe) throws Exception {

        LIGroupes groupe = new LIGroupes();
        groupe.setSession(getSession());
        groupe.setIdGroupe(idGroupe);
        groupe.retrieve();

        LIDomaines domaine = new LIDomaines();
        domaine.setSession(getSession());
        domaine.setIdDomaine(groupe.getIdDomaine());
        domaine.retrieve();

        return getSession().getCodeLibelle(domaine.getCsDomaine());

    }

    public String getLibelleGroupe(String idGroupe) throws Exception {

        LIGroupes groupe = new LIGroupes();
        groupe.setSession(getSession());
        groupe.setIdGroupe(idGroupe);
        groupe.retrieve();

        return groupe.getLibelleGroupe();

    }

    /**
     * getter pour l'attribut methodes selection user
     * 
     * @return la valeur courante de l'attribut methodes selection user
     */
    public Object[] getMethodesSelectionUser() {
        return METHODES_SEL_USER;
    }

    public Map getSacGroupes() {
        return sacGroupes;
    }

    public boolean isRetourDepuisPyxis() {
        return isRetourDepuisPyxis;
    }

    public Map loadGroupes() throws Exception {

        String[] domainesList = getDomainesList(false);

        for (int i = 0; i < domainesList.length; i = i + 2) {
            String domaine = domainesList[i];

            java.util.List listGroupes = new LinkedList();
            String[] groupesList = getGroupesList(domaine);

            for (int j = 0; j < groupesList.length; j = j + 2) {
                String groupe = groupesList[j];
                listGroupes.add(groupe);
            }

            sacGroupes.put(domaine, listGroupes);

        }

        return sacGroupes;
    }

    public void setIdUserFX(String idUserFX) {
        setIdUtilisateurExterne(idUserFX);
        setRetourDepuisPyxis(true);
        this.idUserFX = idUserFX;
    }

    public void setRetourDepuisPyxis(boolean isRetourDepuisPyxis) {
        this.isRetourDepuisPyxis = isRetourDepuisPyxis;
    }

    public void setSacGroupes(Map sacGroupes) {
        this.sacGroupes = sacGroupes;
    }

}