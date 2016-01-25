package globaz.prestation.tools;

import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocator;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.gsc.service.JadeRoleService;

/**
 * @author BSC
 */
public class PRUserUtils {

    /**
     * retourne vrai si l'utilisateur actuellement connecté possède le rôle donné.
     * 
     * @param session
     *            une session utilisateur valide, servant à définir l'ID de l'utilisateur
     * @param idRole
     *            un identifiant du rôle (de FX) dont on aimerait savoir si l'utilisateur fait parti
     * 
     * @return <code>true</code> si l'utilisateur fait parti du rôle, sinon <code>false</code>
     */
    public static final boolean isUtilisateurARole(BSession session, String idRole) throws Exception {

        JadeAdminServiceLocator serviceLocator = JadeAdminServiceLocatorProvider.getLocator();
        JadeRoleService roleService = serviceLocator.getRoleService();

        String[] idRoleDeCetteUtilisateur = roleService.findAllIdRoleForIdUser(session.getUserId());

        if (idRoleDeCetteUtilisateur != null) {
            for (String unIdRole : idRoleDeCetteUtilisateur) {
                if ((unIdRole != null) && unIdRole.equals(idRole)) {
                    return true;
                }
            }
        }

        return false;
    }
}
