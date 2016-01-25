/**
 * 
 */
package globaz.apg.services;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazServer;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.log.JadeLogger;
import globaz.jade.sedex.annotation.OnReceive;
import globaz.jade.sedex.annotation.Setup;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.smtp.JadeSmtpClient;
import java.util.Properties;

/**
 * @author dde ATTENTION : CETTE CLASS EST REFERENCE DANS LE FICHIER DE CONFIGURATION JadeSedexService.xml
 */
public class AnnoncesRapgRetourHandler {

    String[] emails;
    BSession session;
    Object token = new Object();

    @Override
    protected void finalize() throws Throwable {
        JadeThreadActivator.stopUsingContext(token);

        super.finalize();
    }

    private JadeThreadContext initContext(BSession session) throws Exception {
        JadeThreadContext context;
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
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);
        return context;
    }

    @OnReceive
    public void onReceive(SimpleSedexMessage message) throws Exception {
        try {
            String[] attachements = new String[message.attachments.size()];
            int i = 0;
            for (String o : message.attachments.keySet()) {
                attachements[i] = o.toString();
                i++;
            }
            JadeSmtpClient.getInstance().sendMail(emails, "RAPG - Message", "Nouveau message du RAPG", attachements);
        } catch (Exception e) {
            JadeLogger.error(this, e);
            throw e;
        }

    }

    @Setup
    public void setup(Properties properties) throws Exception {
        try {
            session = BSessionUtil.createSession("APG", properties.getProperty("userSedex"));
            JadeThreadActivator.startUsingJdbcContext(token, initContext(session).getContext());
            String mails = GlobazServer.getCurrentSystem().getApplication("APG")
                    .getProperty("rapg.sedexRecipientGroup");
            emails = mails.split(",");
        } catch (Exception e) {
            JadeLogger.error(this, e);
            throw e;
        }
    }

}
