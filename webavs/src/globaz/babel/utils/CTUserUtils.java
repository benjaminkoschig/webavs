package globaz.babel.utils;

import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocator;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.gsc.service.JadeRoleService;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class CTUserUtils {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final JadeAdminServiceLocator LOCATOR = JadeAdminServiceLocatorProvider.getLocator();
    private static final JadeRoleService ROLE_SERVICE = LOCATOR.getRoleService();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * retourne vrai si l'utilisateur actuellement connecté possède le rôle donné.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param idRole
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut utilisateur ARole
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final boolean isUtilisateurARole(BSession session, String idRole) throws Exception {

        if (idRole == null) {
            return false;
        }

        String[] sRoles = ROLE_SERVICE.findAllIdRoleForIdUser(session.getUserId());
        for (int i = 0; i < sRoles.length; i++) {
            if (idRole.equals(sRoles[i])) {
                return true;
            }
        }
        return false;
    }
}
