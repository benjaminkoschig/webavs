package globaz.aquila.servlet;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COContentieuxFactory;
import globaz.aquila.service.COServiceLocator;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Classe abstraite parente de toutes les actions de servlet du projet aquila.
 * 
 * @author Pascal Lovy, 11-nov-2004
 * @see globaz.framework.controller.FWDefaultServletAction
 */
public class CODefaultServletAction extends FWDefaultServletAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Initialise l'action.
     * 
     * @param servlet
     *            Le servlet concerné par cette action
     */
    public CODefaultServletAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * si les parametres 'libSequence' et 'selectedId' se trouvent dans la requete et son différent de ceux du
     * contentieux en session, charge le contentieux correspondant, sinon retourne le contentieux se trouvant dans la
     * session http.
     * 
     * @param request
     * @param session
     * @param appSession
     * @return un viewBean de contentieux
     */
    protected COContentieux chargerContentieux(HttpServletRequest request, HttpSession session, BSession appSession) {
        COContentieux contentieux = (COContentieux) session.getAttribute("contentieuxViewBean");
        String selectedId = request.getParameter("selectedId");

        if (shouldReloadContentieux(request, contentieux)) {
            /*
             * on remplace le contentieux en session qui peut ne pas être le bon.
             */
            try {
                contentieux = COContentieuxFactory.loadContentieuxViewBean(appSession, selectedId);
                COServiceLocator.getTaxeService().initMontantsTaxes(appSession, contentieux);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // on remplace la valeur dans la session http
            session.removeAttribute("contentieuxViewbean");
            session.setAttribute("contentieuxViewBean", contentieux);
        }

        // renseigner la session si ce n'est pas le cas
        if (contentieux.getSession() == null) {
            contentieux.setSession(appSession);
        }

        return contentieux;
    }

    /**
     * Nettoie le viewBean des valeurs booléennes par défaut. Si le viewBean contient des propriétés de type Boolean,
     * elles ont été mises par défaut à false, même si elles n'existaient pas dans la requête. Ceci pose problème pour
     * obtenir toutes les valeurs lors d'une recherche. Afin de palier à ce problème on remet à null les propriétés de
     * type Boolean qui n'existent pas dans la requête.
     * 
     * @param request
     *            La requête http
     * @param viewBean
     *            Le viewBean à nettoyer
     * @return Le viewBean
     * @TODO Demander une modif de la méthode JSPUtils.setBeanProperties() dans le framework
     */
    protected FWViewBeanInterface cleanBooleanDefaultValues(HttpServletRequest request, FWViewBeanInterface viewBean) {
        if (viewBean != null) {
            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(viewBean.getClass());
                PropertyDescriptor[] descriptorList = beanInfo.getPropertyDescriptors();

                for (int i = 0; i < descriptorList.length; i++) {
                    Method writeMethod = descriptorList[i].getWriteMethod();

                    // Si on peut modifier la propriété, qu'elle est de type
                    // Boolean et qu'elle n'existe pas dans la
                    // requête
                    if ((writeMethod != null)
                            && writeMethod.getParameterTypes()[0].getName().equals(Boolean.class.getName())
                            && JadeStringUtil.isEmpty(request.getParameter(descriptorList[i].getName()))) {
                        // On affecte null à la propriété
                        writeMethod.invoke(viewBean, new Object[] { null });
                    }
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }

        return viewBean;
    }

    /**
     * Retourne vrai si le contentieux en session devrait être rechargé en fonction de ce qui se trouve dans la requête.
     * 
     * @param request
     * @param contentieux
     * @return
     */
    protected boolean shouldReloadContentieux(HttpServletRequest request, COContentieux contentieux) {
        String selectedId = request.getParameter("selectedId");
        String libSequence = request.getParameter("libSequence");
        Boolean refresh = new Boolean(request.getParameter("refresh"));

        if ((contentieux == null) || refresh.booleanValue()
                || !JadeStringUtil.equals(selectedId, contentieux.getIdContentieux(), false)
                || !JadeStringUtil.equals(libSequence, contentieux.getLibSequence(), false)) {
            return true;
        }

        return false;
    }
}
