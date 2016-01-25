package ch.globaz.al.businessimpl.generation.prestations.managers;

import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadContext;

/**
 * Classe permettant la gestion de context pour la g�n�ration
 * 
 * @author jts
 * 
 */
public abstract class ContextProvider {

    /**
     * Retourne un clone du contexte pass� en param�tre
     * 
     * @param context
     *            le contexte � cloner
     * @return le clone du context
     * @throws Exception
     *             Exception lev�e si les r�les de l'utilisateur n'ont pas pu �tre r�cup�r�s
     */
    public static JadeThreadContext getNewJadeThreadContext(JadeContextImplementation context) throws Exception {
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(context.getApplicationId());
        ctxtImpl.setLanguage(context.getLanguage());
        ctxtImpl.setUserEmail(context.getUserEmail());
        ctxtImpl.setUserId(context.getUserId());
        ctxtImpl.setUserName(context.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(context.getUserId());

        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }

        return new JadeThreadContext(ctxtImpl);
    }
}
