package globaz.prestation.helpers;

import globaz.framework.ajax.utils.UtilException;
import globaz.framework.bean.FWAJAXFindInterface;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.framework.controller.IFWHelper;
import globaz.framework.servlets.widget.FWJadeWidget;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BIPersistentObjectList;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletException;
import ch.globaz.common.domaine.Checkers;

/**
 * <p>
 * Helper assurant la compatibilité dans la gestion de la transaction entre la nouvelle et l'ancienne persistance.
 * </p>
 * <p>
 * Vu que la méthode {@link #invoke(FWViewBeanInterface, FWAction, BISession)} est final dans {@link FWHelper}, cette
 * classe est principalement un copier/coller de FWHelper afin de pouvoir modifier le comportement de invoke.
 * </p>
 */
public class PRHybridHelper implements IFWHelper {

    private static final Class<?>[] PARAMS = new Class[] { FWViewBeanInterface.class, FWAction.class, BSession.class };

    protected void _add(final FWViewBeanInterface viewBean, final FWAction action,
            final globaz.globall.api.BISession session) throws Exception {
        ((BIPersistentObject) viewBean).add();
    }

    protected void _chercher(final FWViewBeanInterface viewBean, final FWAction action,
            final globaz.globall.api.BISession session) throws Exception {
    }

    protected void _delete(final FWViewBeanInterface viewBean, final FWAction action,
            final globaz.globall.api.BISession session) throws Exception {
        ((BIPersistentObject) viewBean).delete();
    }

    protected void _find(final BIPersistentObjectList persistentList, final FWAction action,
            final globaz.globall.api.BISession session) throws Exception {
        persistentList.find();
    }

    protected void _find(final Object viewBean, final FWAction action, final globaz.globall.api.BISession session)
            throws Exception {
        ((BIPersistentObject) viewBean).retrieve();// DMA modif à voir // VYJ VU
    }

    protected void _findAjax(final Object viewBean, final FWAction action, final globaz.globall.api.BISession session)
            throws Exception {
        if (FWAJAXFindInterface.class.isAssignableFrom(viewBean.getClass())) {
            ((FWAJAXFindInterface) viewBean).find();
        }
    }

    protected void _findNext(final BIPersistentObjectList persistentList, final FWAction action,
            final globaz.globall.api.BISession session) throws Exception {
        persistentList.findNext();
    }

    protected void _findPrevious(final BIPersistentObjectList persistentList, final FWAction action,
            final globaz.globall.api.BISession session) throws Exception {
        persistentList.findPrev();
    }

    protected void _init(final FWViewBeanInterface viewBean, final FWAction action,
            final globaz.globall.api.BISession session) throws Exception {
    }

    protected void _retrieve(final FWViewBeanInterface viewBean, final FWAction action,
            final globaz.globall.api.BISession session) throws Exception {
        ((BIPersistentObject) viewBean).retrieve();

    }

    protected void _start(final FWViewBeanInterface viewBean, final FWAction action,
            final globaz.globall.api.BISession session) {
        try {
            BProcessLauncher.start((BProcess) viewBean);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }

    protected void _update(final FWViewBeanInterface viewBean, final FWAction action,
            final globaz.globall.api.BISession session) throws Exception {
        ((BIPersistentObject) viewBean).update();
    }

    @Override
    public void afterExecute(final FWViewBeanInterface viewBean, final FWAction action,
            final globaz.globall.api.BISession session) throws Exception {
        if ((viewBean != null)) {

            if ((viewBean.getISession() != null) && viewBean.getISession().hasErrors()) {
                if (!(FWJadeWidget.class.isAssignableFrom(viewBean.getClass()) || FWAJAXViewBeanInterface.class
                        .isAssignableFrom(viewBean.getClass()))) {
                    viewBean.setMessage(viewBean.getISession().getErrors().toString());
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                } else {
                    viewBean.getISession().getErrors();
                }
            }
        }
    }

    @Override
    public void beforeExecute(final FWViewBeanInterface viewBean, final FWAction action,
            final globaz.globall.api.BISession session) throws Exception {
        if (session == null) {
            throw new Exception("invalid session");
        }
        if (action == null) {
            throw new Exception("invalid action");
        }
        viewBean.setISession(session);
        viewBean.setMessage("");
        viewBean.setMsgType(FWViewBeanInterface.OK);
    }

    /**
     * recherche une méthode PUBLIQUE de cette classe qui porte le nom de la partie action de l'instance 'action' et
     * prend les mêmes paramètres que cette méthode sauf pour BSession a la place de BISession et l'invoke.
     * 
     * <p>
     * le tout est fait dans un bloc try...catch, il est donc possible de lancer une exception dans ces méthodes. Cette
     * méthode retourne ensuite le viewBean retourné par ladite méthode ou celui transis en argument si la méthode n'a
     * pas de return value.
     * </p>
     * 
     * <p>
     * si la partie action contient la chaine SET_ACTION, la session est simplement settee dans le viewBean et celui-ci
     * est retourné.
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
    protected FWViewBeanInterface deleguerExecute(final FWViewBeanInterface viewBean, final FWAction action,
            final BISession session) throws ClassCastException {
        if ("setSession".equals(action.getActionPart())) {
            viewBean.setISession(session);

            return viewBean;
        } else {
            try {
                Method method = this.getClass().getMethod(action.getActionPart(), PRHybridHelper.PARAMS);
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

    protected FWViewBeanInterface execute(final FWViewBeanInterface viewBean, final FWAction action,
            final BISession session) {
        return viewBean;
    }

    private Set<String> extraireLesMessageErreurs(final JadeBusinessMessage[] logMessages) {
        Set<String> messagesErreur = new HashSet<String>();

        if (logMessages != null) {
            for (JadeBusinessMessage uneErreur : logMessages) {
                messagesErreur.add(uneErreur.getMessageId());
            }
        }

        return messagesErreur;
    }

    @Override
    public final FWViewBeanInterface invoke(FWViewBeanInterface viewBean, final FWAction action,
            final globaz.globall.api.BISession session) {

        BTransaction transactionFromThreadContext = null;
        try {

            String actionPart = action.getActionPart();
            if (actionPart.equals(FWAction.ACTION_AFFICHER) || actionPart.equals(FWAction.ACTION_DISPLAY)) {
                _retrieve(viewBean, action, session);
            } else if (actionPart.equals(FWAction.ACTION_NOUVEAU) || actionPart.equals(FWAction.ACTION_NEW)) {
                _init(viewBean, action, session);
            } else if (actionPart.equals(FWAction.ACTION_MODIFIER) || actionPart.equals(FWAction.ACTION_MODIFY)) {
                _update(viewBean, action, session);
            } else if (actionPart.equals(FWAction.ACTION_AJOUTER) || actionPart.equals(FWAction.ACTION_ADD)) {
                _add(viewBean, action, session);
            } else if (actionPart.equals(FWAction.ACTION_SUPPRIMER) || actionPart.equals(FWAction.ACTION_REMOVE)) {
                _delete(viewBean, action, session);
            } else if (actionPart.equals(FWAction.ACTION_EXECUTER) || actionPart.equals(FWAction.ACTION_EXECUTE)) {
                _start(viewBean, action, session);
            } else if (actionPart.equals(FWAction.ACTION_CHERCHER) || actionPart.equals(FWAction.ACTION_SEARCH)) {
                _chercher(viewBean, action, session);
            } else if (actionPart.equals(FWAction.ACTION_SUIVANT) || actionPart.equals(FWAction.ACTION_NEXT)) {
                _findNext((BIPersistentObjectList) viewBean, action, session);
            } else if (actionPart.equals(FWAction.ACTION_PRECEDANT) || actionPart.equals(FWAction.ACTION_PREVIOUS)) {
                _findPrevious((BIPersistentObjectList) viewBean, action, session);
            } else if (actionPart.equals(FWAction.ACTION_LISTER) || actionPart.equals(FWAction.ACTION_LIST)) {
                if (viewBean instanceof BIPersistentObjectList) {
                    this._find((BIPersistentObjectList) viewBean, action, session);
                } else {
                    this._find(viewBean, action, session);
                }
            } else if (actionPart.equals(FWAction.ACTION_AFFICHER_AJAX)) {
                _retrieve(viewBean, action, session);
            } else if (actionPart.equals(FWAction.ACTION_MODIFIER_AJAX)) {
                _update(viewBean, action, session);
            } else if (actionPart.equals(FWAction.ACTION_SUPPRIMER_AJAX)) {
                _delete(viewBean, action, session);
            } else if (actionPart.equals(FWAction.ACTION_AJOUTER_AJAX)) {
                _add(viewBean, action, session);
            } else if (actionPart.equals(FWAction.ACTION_LISTER_AJAX)) {
                _findAjax(viewBean, action, session);
            } else if (actionPart.equals(FWAction.ACTION_EXECUTER_AJAX)) {
                _start(viewBean, action, session);
            } else {
                viewBean = execute(viewBean, action, session);
            }

        } catch (Exception e) {

            String err;

            if (FWJadeWidget.class.isAssignableFrom(viewBean.getClass())
                    || FWAJAXViewBeanInterface.class.isAssignableFrom(viewBean.getClass())) {

                // pour laisser la gestion de la transaction au fw
                JadeThread.logError(this.getClass().getName(), "ERROR_AJAX");
                err = UtilException.renderJsonException(e);

            } else {
                Throwable currentException = e;
                StringBuilder errors = new StringBuilder();
                if (JadeThread.currentContext() != null) {
                    JadeThread.logError(this.getClass().getName(), e.toString());
                    while (currentException.getCause() != null) {
                        currentException = currentException.getCause();
                        JadeThread.logError(this.getClass().getName(), currentException.toString());
                    }
                }
                currentException = e;
                errors.append("<br />").append(currentException.toString());
                while (currentException.getCause() != null) {
                    currentException = currentException.getCause();
                    errors.append("<br />").append(currentException.toString());
                }
                err = errors.toString();
            }

            viewBean.setMessage(err);
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        return viewBean;
    }

    private static JadeContext buildContext(BSession session) throws Exception {

        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }

        return ctxtImpl;
    }

    public static void initContext(BSession session, Object caller) throws ServletException {
        Checkers.checkNotNull(caller, "caller");

        if (JadeThread.currentContext() == null) {
            try {
                JadeContext threadContext = buildContext(session);

                JadeThreadActivator.startUsingJdbcContext(caller, threadContext);
                JadeThread.storeTemporaryObject("bsession", session);
            } catch (Exception ex) {

                StringBuilder message = new StringBuilder();
                message.append(caller.getClass().toString());
                message.append(" : failed to init thread context (").append(ex.toString()).append(")");

                JadeLogger.error(caller, message.toString());

                throw new ServletException(message.toString());
            }
        }
    }

    public static void stopUsingContext(Object caller) {
        Checkers.checkNotNull(caller, "caller");
        JadeThreadActivator.stopUsingContext(caller);
    }
}
