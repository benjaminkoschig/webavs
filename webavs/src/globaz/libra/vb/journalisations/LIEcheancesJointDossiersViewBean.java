package globaz.libra.vb.journalisations;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.libra.db.domaines.LIDomaines;
import globaz.libra.db.domaines.LIDomainesManager;
import globaz.libra.db.groupes.LIGroupes;
import globaz.libra.db.groupes.LIGroupesManager;
import globaz.libra.db.journalisations.LIJournalisationsJointDossiers;
import globaz.libra.db.utilisateurs.LIUtilisateurs;
import globaz.libra.db.utilisateurs.LIUtilisateursManager;
import globaz.libra.interfaces.ILIConstantes;
import globaz.libra.vb.dossiers.LIDossiersViewBean;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class LIEcheancesJointDossiersViewBean extends LIJournalisationsJointDossiers implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public static final String getLibelleCourtSexe(String csSexe, BSession session) {

        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return session.getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return session.getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public static final String getLibellePays(String csNationalite, BSession session) {

        if ("999".equals(session.getCode(session.getSystemCode("CIPAYORI", csNationalite)))) {
            return "";
        } else {
            return session.getCodeLibelle(session.getSystemCode("CIPAYORI", csNationalite));
        }

    }

    private LIDossiersViewBean dossier = new LIDossiersViewBean();
    private transient Vector orderBy = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    Map sacGroupes = new HashMap();

    Map sacUsers = new HashMap();

    /**
     * Méthode qui retourne le détail du tiers formaté
     * 
     * @return le détail du tiers formaté
     * @throws Exception
     */
    public String getDetailTiers() throws Exception {

        return PRNSSUtil.formatDetailRequerantListe(getNoAVS(), getNom() + " " + getPrenom(), getDateNaissance(),
                getLibelleCourtSexe(getCsSexe(), getSession()), getLibellePays(getCsNationalite(), getSession()));

    }

    /**
     * Return les domaines par rapports aux droits de l'utilisateur courant
     */
    public String[] getDomainesForUser(boolean wantBlank) throws Exception {

        LIDomainesManager domMgr = new LIDomainesManager();
        domMgr.setSession(getSession());
        domMgr.find();

        List listeDomaines = new ArrayList();

        for (Iterator iterator = domMgr.iterator(); iterator.hasNext();) {
            LIDomaines domaine = (LIDomaines) iterator.next();

            if (getSession().hasRight(domaine.getNomApplication(), globaz.framework.secure.FWSecureConstants.READ)) {
                listeDomaines.add(domaine);
            }

        }

        int i = 0;
        String[] domainesTab;

        if (wantBlank) {
            domainesTab = new String[(listeDomaines.size() * 2) + 2];
            StringBuffer allDomains = new StringBuffer();

            allDomains.append("IN (");

            if (listeDomaines.size() > 0) {
                for (Iterator iterator = listeDomaines.iterator(); iterator.hasNext();) {
                    LIDomaines domaine = (LIDomaines) iterator.next();

                    allDomains.append(domaine.getIdDomaine());
                    if (iterator.hasNext()) {
                        allDomains.append(",");
                    }

                }

                allDomains.append(")");

                domainesTab[i] = allDomains.toString();
                domainesTab[i + 1] = "";
                i += 2;
            } else {
                return new String[0];
            }

        } else {
            domainesTab = new String[listeDomaines.size() * 2];
        }

        for (Iterator iterator = listeDomaines.iterator(); iterator.hasNext();) {
            LIDomaines domaine = (LIDomaines) iterator.next();
            domainesTab[i] = domaine.getIdDomaine();
            domainesTab[i + 1] = getSession().getCodeLibelle(domaine.getCsDomaine());
            i += 2;
        }

        return domainesTab;
    }

    public LIDossiersViewBean getDossier() throws Exception {
        return dossier;
    }

    public Vector getOrderByData() {
        orderBy = new Vector(4);
        orderBy.add(new String[] { ILIConstantes.ECHEANCES_TRI_DATE, "Date" });
        orderBy.add(new String[] { ILIConstantes.ECHEANCES_TRI_DOMAINE, "Domaine" });
        orderBy.add(new String[] { ILIConstantes.ECHEANCES_TRI_TYPE, "Type" });
        orderBy.add(new String[] { ILIConstantes.ECHEANCES_TRI_UTILISATEUR, "Utilisateur" });
        return orderBy;
    }

    public Map getSacGroupes() {
        return sacGroupes;
    }

    public Map getSacUsers() {
        return sacUsers;
    }

    public void loadDossier(String idDossier) throws Exception {
        dossier.setSession(getSession());
        dossier.setIdDossier(idDossier);
        dossier.retrieve();
    }

    public void loadGroupes() throws Exception {

        String[] domaines = getDomainesForUser(false);

        for (int i = 0; i < domaines.length; i = i + 2) {
            String idDomaine = domaines[i];

            LIGroupesManager grpMgr = new LIGroupesManager();
            grpMgr.setSession(getSession());
            grpMgr.setForIdDomaine(idDomaine);
            grpMgr.find();

            List listGroupes = new LinkedList();

            for (Iterator iterator = grpMgr.iterator(); iterator.hasNext();) {
                LIGroupes groupe = (LIGroupes) iterator.next();

                if (groupe.isUserInGroup(getSession().getUserId())) {
                    listGroupes.add(groupe.getIdGroupe());
                    listGroupes.add(groupe.getLibelleGroupe());
                }

            }

            sacGroupes.put(idDomaine, listGroupes);

        }

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

    public void setSacGroupes(Map sacGroupes) {
        this.sacGroupes = sacGroupes;
    }

    public void setSacUsers(Map sacUsers) {
        this.sacUsers = sacUsers;
    }

}
