package ch.globaz.al.businessimpl.generation.prestations.managers;

import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadContext;

/**
 * Classe permettant la gestion de context pour la génération
 * 
 * @author jts
 * 
 */
public abstract class ContextProvider {

    /**
     * Retourne un clone du contexte passé en paramètre
     * 
     * @param context
     *            le contexte à cloner
     * @return le clone du context
     * @throws Exception
     *             Exception levée si les rôles de l'utilisateur n'ont pas pu être récupérés
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
