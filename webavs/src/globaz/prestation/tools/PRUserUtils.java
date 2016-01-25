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
     * retourne vrai si l'utilisateur actuellement connect� poss�de le r�le donn�.
     * 
     * @param session
     *            une session utilisateur valide, servant � d�finir l'ID de l'utilisateur
     * @param idRole
     *            un identifiant du r�le (de FX) dont on aimerait savoir si l'utilisateur fait parti
     * 
     * @return <code>true</code> si l'utilisateur fait parti du r�le, sinon <code>false</code>
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
