package ch.globaz.al.utils;

import java.util.Date;
import java.util.List;
import java.util.Vector;
import ch.globaz.al.exception.ALGestionnaireException;
import globaz.fx.user.business.bean.FXModuleUser;
import globaz.fx.user.business.enums.TypeDroit;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;

public class ALGestionnaireUtils {

    /**
     * Convertis une liste d'utilisateurs sous forme d'une liste de tableau de String pour la taglib FWListSelectTag
     *
     * @param alUsers
     * @return
     */
    public static Vector<String[]> convertForFWSelect(List<FXModuleUser> alUsers) {
        Vector<String[]> results = new Vector<String[]>();
        if (alUsers != null) {
            for (FXModuleUser jadeUser : alUsers) {
                results.add(new String[] { jadeUser.getVisa(), jadeUser.getVisa() });
            }
        }
        return results;
    }

    // --------------------------------------------------------
    /**
     * Retourne tous les utilisateurs ayant des droit de type {TypeDroit} au niveau du module AL pour la date du
     * jour</br>
     *
     * @see TypeDroit
     * @param session La session à utiliser, doit être connectée au module AL
     * @param right le type de droit recherché
     * @return tous les utilisateurs ayant des droit de type (TypeDroit) au niveau du module AL
     * @throws ALGestionnaireException si la session n'est pas connectée au bon module (AL en l'occurence) ou si des
     *             paramètres sont invalides (paramètres null, session sans transaction ouverte, etc)
     */
    public static List<FXModuleUser> getGestionnaires(BSession session, TypeDroit right)
            throws ALGestionnaireException {
        try {
            return JadeAdminServiceLocatorProvider.getLocator().getUserService().findUsers(session, right, new Date());
        } catch (Exception e) {
            throw new ALGestionnaireException("Exception occured on AL users retrieve" + e.toString(), e);
        }
    }

}
