/*
 * Créé le 17 mai 05
 * 
 * Description :
 */
package globaz.prestation.interfaces.fx;

import globaz.framework.security.FWSecurityLoginException;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocator;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.gsc.bean.JadeRoleUser;
import globaz.jade.admin.gsc.service.JadeRoleUserService;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserGroupService;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeStringUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author scr
 * @update sce, 03.02.2011
 * @titi
 */
public class PRGestionnaireHelper {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final JadeAdminServiceLocator LOCATOR = JadeAdminServiceLocatorProvider.getLocator();
    private static final JadeRoleUserService ROLE_USER_SERVICE = PRGestionnaireHelper.LOCATOR.getRoleUserService();
    private static final JadeUserGroupService USER_GROUP_SERVICE = PRGestionnaireHelper.LOCATOR.getUserGroupService();
    private static final JadeUserService USER_SERVICE = PRGestionnaireHelper.LOCATOR.getUserService();

    public static String getDetailGestionnaire(BSession session, String idGestionnaire)
            throws FWSecurityLoginException, Exception {

        if (JadeStringUtil.isEmpty(idGestionnaire)) {
            return "";
        } else {
            JadeUser userName = session.getApplication()._getSecurityManager().getUserForVisa(session, idGestionnaire);
            return userName.getIdUser() + " - " + userName.getFirstname() + " " + userName.getLastname();
        }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Retourne un gestionnaire selon son nom d'utilisateur.
     * 
     * @param gestionnaireId
     *            le nom d'utilisateur du gestionnaire à rechercher.
     * 
     * @return la valeur courante de l'attribut gestionnaire
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final JadeUser getGestionnaire(String gestionnaireId) throws Exception {
        return PRGestionnaireHelper.USER_SERVICE.load(PRGestionnaireHelper.USER_SERVICE
                .findIdUserForVisa(gestionnaireId));
    }

    /**
     * getter pour l'attribut gestionnaires id
     * 
     * @param groupGestionaire
     *            DOCUMENT ME!
     * 
     * @return Tableau contenant les id des gestionnaires du group.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final String[] getGestionnairesId(String groupGestionaire) throws Exception {
        return PRGestionnaireHelper.USER_GROUP_SERVICE.findAllIdUserForIdGroup(groupGestionaire);
    }

    /**
     * Recherche tous les id et les noms complets des utilisateurs du groupe 'groupGestionnaire' et les retournent sous
     * forme de Vector de String[2]{userId, userFullName}.
     * 
     * @param groupGestionaire
     *            le groupe de gestionnaires dont il faut rechercher les membres.
     * 
     * @return un vecteur (jamais null)
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final Vector getIdsEtNomsGestionnaires(String groupGestionaire) throws Exception {
        String[] users = PRGestionnaireHelper.getGestionnairesId(groupGestionaire);
        String[] userNames = PRGestionnaireHelper.getNomsGestionnaires(users);
        Vector retValue = new Vector(Math.min(users.length, userNames.length));

        for (int idUser = 0; idUser < retValue.capacity(); ++idUser) {
            retValue.add(new String[] { users[idUser], userNames[idUser] });
        }

        return retValue;
    }

    /**
     * Retourne les id et nom des gestionnaire d'un groupe dans une map (les clés sont les ID des gestionnaires)
     * 
     * @param groupeGestionnaire
     * @return
     * @throws Exception
     */
    public static final Map<String, String> getIdsEtNomsGestionnairesInMap(String groupeGestionnaire) throws Exception {
        Map<String, String> idEtCsGestionnaire = new HashMap<String, String>();
        for (String idGestionnaire : PRGestionnaireHelper.getGestionnairesId(groupeGestionnaire)) {
            idEtCsGestionnaire.put(idGestionnaire, PRGestionnaireHelper.getNomGestionnaire(idGestionnaire));
        }
        return idEtCsGestionnaire;
    }

    /**
     * getter pour l'attribut nom gestionnaire
     * 
     * @param gestionnaireId
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut nom gestionnaire
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final String getNomGestionnaire(String gestionnaireId) throws Exception {
        JadeUser user = PRGestionnaireHelper.getGestionnaire(gestionnaireId);

        return user.getFirstname() + " " + user.getLastname();
    }

    /**
     * getter pour l'attribut noms gestionnaires
     * 
     * @param gestionnairesIds
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut noms gestionnaires
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final String[] getNomsGestionnaires(String[] gestionnairesIds) throws Exception {
        String[] retValue = new String[gestionnairesIds.length];

        for (int idGestionnaire = gestionnairesIds.length; --idGestionnaire >= 0;) {
            retValue[idGestionnaire] = gestionnairesIds[idGestionnaire] + " - "
                    + PRGestionnaireHelper.getNomGestionnaire(gestionnairesIds[idGestionnaire]);
        }

        return retValue;
    }

    /**
     * Retourne vrai si l'utilisateur ayant comme identifiant 'idUtilisateur' possède le rôle 'nomRole'
     * 
     * @param idUtilisateur
     *            DOCUMENT ME!
     * @param nomRole
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final boolean utilisateurARole(String idUtilisateur, String nomRole) throws Exception {
        HashMap criteres = new HashMap();

        criteres.put(JadeRoleUserService.CR_FOR_IDUSER, idUtilisateur);
        criteres.put(JadeRoleUserService.CR_FOR_IDROLE, nomRole);

        JadeRoleUser[] result = PRGestionnaireHelper.ROLE_USER_SERVICE.findForCriteres(criteres);

        return ((result != null) && (result.length > 0));
    }

    private PRGestionnaireHelper() {
        // ne peut être instanciée
    }
}
