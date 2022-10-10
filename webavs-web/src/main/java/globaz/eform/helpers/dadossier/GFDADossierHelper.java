package globaz.eform.helpers.dadossier;

import ch.globaz.eform.constant.GFTypeEForm;
import ch.globaz.eform.web.application.GFApplication;
import ch.globaz.eform.web.servlet.GFFormulaireServletAction;
import globaz.eform.vb.formulaire.GFFormulaireViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocator;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserGroupService;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

public class GFDADossierHelper extends FWHelper {
    private static final String ERREUR_GESTIONNAIRES_INTROUVABLE = "GESTIONNAIRES_INTROUVABLE";

    public static final String PROPERTY_GROUPE_GESTIONNAIRE = "groupe.dadossier.gestionnaire";

    private static Map<String, String> gestionnaires = null;

    /**
     * Retourne une map {userId, userFullName} des gestionnaires
     *
     * <p>
     * La map n'est créé qu'une seule fois pour chaque instance de ce view bean.
     * </p>
     *
     * @param session
     *            DOCUMENT ME!
     *
     * @return la valeur courante de l'attribut responsable data
     */
    public static Map<String, String> getGestionnairesData(BSession session) {
        if (Objects.isNull(gestionnaires)) {
            JadeAdminServiceLocator locator = JadeAdminServiceLocatorProvider.getLocator();

            JadeUserGroupService userGroupService = locator.getUserGroupService();
            JadeUserService userService = locator.getUserService();

            try {
                gestionnaires = Arrays.stream(userGroupService
                                .findForIdGroup(GlobazSystem
                                        .getApplication(GFApplication.APPLICATION_ID)
                                        .getProperty(PROPERTY_GROUPE_GESTIONNAIRE)))
                        .map(jadeUserGroup -> {
                            try {
                                return userService.loadForVisa(jadeUserGroup.getIdUser());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors
                                .toMap(
                                        JadeUser::getIdUser,
                                        jadeUser -> jadeUser.getFirstname() + " - " + jadeUser.getLastname()));
            } catch (Exception e) {
                session.addError(session.getLabel(ERREUR_GESTIONNAIRES_INTROUVABLE));
            }
        }

        return gestionnaires;
    }
}
