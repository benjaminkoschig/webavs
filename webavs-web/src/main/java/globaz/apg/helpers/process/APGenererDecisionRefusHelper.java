package globaz.apg.helpers.process;

import globaz.apg.vb.process.APGenererDecisionRefusViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author JJE
 */
public class APGenererDecisionRefusHelper extends FWHelper {

    private static final Class[] PARAMS = new Class[] { FWViewBeanInterface.class, FWAction.class, BSession.class };

    public static final String SET_SESSION = "setSession";

    public void allerVersChoixAnnexesEtCopies(FWViewBeanInterface viewBean, FWAction action, BSession session) {

        // Validation des ?l?ments
        APGenererDecisionRefusViewBean vb = (APGenererDecisionRefusViewBean) viewBean;

        // adresse email
        if (JadeStringUtil.isEmpty(vb.getEMailAddress())) {
            vb.setMsgType(FWViewBeanInterface.ERROR);
            vb.setMessage(session.getLabel("EMAIL_REQUIS"));
            session.addError(session.getLabel("EMAIL_REQUIS"));
        }

        // date sur document
        if (JadeStringUtil.isEmpty(vb.getDateSurDocument())) {
            vb.setMsgType(FWViewBeanInterface.ERROR);
            vb.setMessage(session.getLabel("JSP_ERREUR_DATE_SUR_DOCUMENT"));
            session.addError(session.getLabel("JSP_ERREUR_DATE_SUR_DOCUMENT"));
        }

    }

    /**
     * recherche une m?thode PUBLIQUE de cette classe qui porte le nom de la partie action de l'instance 'action' et
     * prend les m?mes param?tres que cette m?thode sauf pour BSession a la place de BISession et l'invoke.
     * 
     * <p>
     * le tout est fait dans un bloc try...catch, il est donc possible de lancer une exception dans ces m?thodes. Cette
     * m?thode retourne ensuite le viewBean retourn? par ladite m?thode ou celui transis en argument si la m?thode n'a
     * pas de return value.
     * </p>
     * 
     * <p>
     * si la partie action contient la chaine SET_ACTION, la session est simplement settee dans le viewBean et celui-ci
     * est retourn?.
     * </p>
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws ClassCastException
     *             si session n'est pas une instance de BSession
     * 
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    protected FWViewBeanInterface deleguerExecute(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws ClassCastException {
        if (SET_SESSION.equals(action.getActionPart())) {
            viewBean.setISession(session);

            return viewBean;
        } else {
            try {
                Method method = getClass().getMethod(action.getActionPart(), PARAMS);
                Object retValue = method.invoke(this, new Object[] { viewBean, action, (BSession) session });

                if ((retValue != null) && (retValue instanceof FWViewBeanInterface)) {
                    return (FWViewBeanInterface) retValue;
                }
            } catch (InvocationTargetException ie) {
                if (ie.getTargetException() != null) {
                    ((BSession) session).addError(ie.getTargetException().getMessage());
                } else {
                    JadeLogger.error(ie, ie.getMessage());
                    ((BSession) session).addError(ie.getMessage());
                }
            } catch (Exception e) {
                JadeLogger.error(e, e.getMessage());
                ((BSession) session).addError(e.getMessage());
            }

            return viewBean;
        }
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

}
