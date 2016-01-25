package ch.globaz.vulpecula.externalservices;

import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.mercato.notification.Notification;
import ch.globaz.vulpecula.application.ApplicationConstants;
import ch.globaz.vulpecula.business.models.notification.NotificationSimpleModel;

public class RequestFactory {
    public static final String URL = "http://localhost:9000/notifications";
    public static final String URL_SALAIRES_THEORIQUES_ANNUEL = "http://localhost:9000/salaires/theoriquesannuel";
    public static final String URL_SALAIRES_THEORIQUES_MENSUEL = "http://localhost:9000/salaires/theoriquesmensuel";
    public static final String URL_COTISATIONS = "http://localhost:9000/salaires/cotisations";

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestFactory.class);

    private JadeContext context;

    /**
     * Permet de persister depuis un tracker ancienne persistance.
     * 
     * @param notification
     * @param session
     */
    public void persistFromAnciennePersistance(Notification notification, BSession session) {
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), getContext(session));
            persistFromNouvellePersistance(notification);
        } catch (SQLException e) {
            LOGGER.error("Error while persisting notification.");
        } catch (Exception e) {
            LOGGER.error("Error while persisting notification.");
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }

    /**
     * Permet de persister depuis un tracker nouvelle persistance.
     * 
     * @param notification
     */
    public void persistFromNouvellePersistance(Notification notification) {
        try {
            NotificationSimpleModel notificationSimpleModel = new NotificationSimpleModel();
            notificationSimpleModel.setInfoType(notification.getInfoType().toString());
            notificationSimpleModel.setIdCible(notification.getTargetId());
            notificationSimpleModel.setExtra(notification.getExtra());
            JadePersistenceManager.add(notificationSimpleModel);
        } catch (JadePersistenceException e) {
            LOGGER.error("Error while persisting notification.");
        }

    }

    public JadeContext getContext(BSession session) throws Exception {
        if (context == null) {
            context = initContext(session).getContext();
        }
        return context;
    }

    private JadeThreadContext initContext(BSession session) throws Exception {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(ApplicationConstants.DEFAULT_APPLICATION_VULPECULA);
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);
        return context;
    }
}
