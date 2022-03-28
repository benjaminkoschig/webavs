package globaz.eform.helpers.formulaire;

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
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

public class GFFormulaireHelper extends FWHelper {
    private static final String ERREUR_GESTIONNAIRES_INTROUVABLE = "GESTIONNAIRES_INTROUVABLE";

    public static final String PROPERTY_GROUPE_GESTIONNAIRE = "groupe.eform.gestionnaire";

    private static Vector gestionnaires = null;
    private static final Map<String, Vector<String[]>> type = new HashMap<>();
    private static final Map<String, Vector<String[]>> sortBy = new HashMap<>();

    public static  Vector<String[]> getTypeData(BSession session) {
        if (!type.containsKey(session.getIdLangueISO())) {
            Vector<String[]> vec = new Vector<>();
            type.put(session.getIdLangueISO(), vec);
            vec.add(new String[] { "", "" });
            
            Arrays.stream(GFTypeEForm.values())
                    .forEach(gfTypeEForm -> vec.add(new String[] { gfTypeEForm.getCodeEForm(), gfTypeEForm.getCodeEForm() + " - " + gfTypeEForm.getDesignation(session) }));

        }

        return type.get(session.getIdLangueISO());
    }

    /**
     * Retourne le vecteur de tableaux a 2 entrées {userId, userFullName} des gestionnaires
     *
     * <p>
     * Le vecteur n'est créé qu'une seule fois pour chaque instance de ce view bean.
     * </p>
     *
     * @param session
     *            DOCUMENT ME!
     *
     * @return la valeur courante de l'attribut responsable data
     */
    public static Vector getGestionnairesData(BSession session) {
        if (Objects.isNull(gestionnaires)) {
            try {
                gestionnaires = PRGestionnaireHelper
                        .getIdsEtNomsGestionnaires(GlobazSystem
                                .getApplication(GFApplication.APPLICATION_ID)
                                .getProperty(PROPERTY_GROUPE_GESTIONNAIRE));
            } catch (Exception e) {
                session.addError(session.getLabel(ERREUR_GESTIONNAIRES_INTROUVABLE));
            }

            // on veut une ligne vide
            if (Objects.isNull(gestionnaires)) {
                gestionnaires = new Vector<>();
            }

            gestionnaires.insertElementAt(new String[] { "", "" }, 0);
        }

        return gestionnaires;
    }

    public static String getGestionnaireDesignation(String visa) throws Exception {
        if (StringUtils.isEmpty(visa)) {
            return "";
        }

        JadeUserService userService = JadeAdminServiceLocatorProvider.getLocator().getUserService();

        JadeUser user = userService.load(userService.findIdUserForVisa(visa));

        return  user.getFirstname() + " " + user.getLastname();
    }

    public static Vector<String[]> getSortByData(BSession session) {
        if (!sortBy.containsKey(session.getIdLangueISO())) {
            Vector<String[]> vec = new Vector<>();
            sortBy.put(session.getIdLangueISO(), vec);

            vec.add(new String[]{"default", session.getLabel("ORDER_BY_DATE")});
            vec.add(new String[]{"orderByType", session.getLabel("ORDER_BY_TYPE")});
            vec.add(new String[]{"orderByNSS", session.getLabel("ORDER_BY_NSS")});
            vec.add(new String[]{"orderByGestionnaire", session.getLabel("ORDER_BY_GESTIONNAIRE")});
        }

        return sortBy.get(session.getIdLangueISO());
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (GFFormulaireServletAction.ACTION_TELECHARGER.equals(action.getActionPart()) && (viewBean instanceof GFFormulaireViewBean)) {
            GFFormulaireViewBean bean = (GFFormulaireViewBean) viewBean;
            try {
                if (bean.getFormulaire().isNew()) {
                    bean.retrieveWithBlob();
                    if(bean.getFormulaire().getAttachement() == null) {
                        viewBean.setMessage("Pas de fichier trouvé pour cet id : "+((GFFormulaireViewBean) viewBean).getId());
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    }
                }
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            return viewBean;
        } else if(GFFormulaireServletAction.ACTION_CHANGE_STATUT.equals(action.getActionPart()) && (viewBean instanceof GFFormulaireViewBean)) {
            GFFormulaireViewBean bean = (GFFormulaireViewBean) viewBean;
            try {
                if (bean.getFormulaire().isNew()) {
                    String statut = bean.getFormulaire().getStatus();
                    bean.retrieveWithBlob();
                    bean.setByStatus(statut);
                    bean.setByGestionnaire(bean.getFormulaire().getUserGestionnaire());
                    bean.update();
                }
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            return viewBean;
        } else {
            return super.execute(viewBean, action, session);
        }
    }

}
