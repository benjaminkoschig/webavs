package ch.globaz.orion.businessimpl.services;

import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.jade.log.JadeLogger;
import ch.globaz.ebback.api.BackProvider;
import ch.globaz.orion.EBApplication;
import ch.globaz.xmlns.eb.acl.ACLService;
import ch.globaz.xmlns.eb.dan.DANService;
import ch.globaz.xmlns.eb.mail.MailService;
import ch.globaz.xmlns.eb.pac.PACService;
import ch.globaz.xmlns.eb.partnerweb.PartnerWebService;
import ch.globaz.xmlns.eb.partnerweb.User;
import ch.globaz.xmlns.eb.pucs.PUCSService;

public class ServicesProviders {

    public static ACLService aclServiceProvide(BSession session) {
        User unConnectedUser = new User();
        unConnectedUser.setNomLogin(session.getUserId());
        unConnectedUser.setEmail(session.getUserEMail());
        try {
            EBApplication app = (EBApplication) GlobazServer.getCurrentSystem().getApplication(
                    EBApplication.APPLICATION_ID);
            String owner = app.getProperty("ebusiness.owner");

            return BackProvider.getAclService(unConnectedUser, owner, session.getIdLangueISO());
        } catch (Exception e) {
            // Problem général, le service n'est pas utilisable ...
            JadeLogger.fatal(ServicesProviders.class, e);
            throw new IllegalStateException(e);
        }
    }

    public static DANService danServiceProvide(BSession session) {

        User unConnectedUser = new User();
        String owner = "";
        unConnectedUser.setNomLogin(session.getUserId());
        unConnectedUser.setEmail(session.getUserEMail());
        try {
            EBApplication app = (EBApplication) GlobazServer.getCurrentSystem().getApplication(
                    EBApplication.APPLICATION_ID);
            owner = app.getProperty("ebusiness.owner");
            return BackProvider.getDanService(unConnectedUser, owner, session.getIdLangueISO());
        } catch (Exception e) {
            // Problem général, le service n'est pas utilisable ...
            JadeLogger.fatal(ServicesProviders.class, e);
            throw new IllegalStateException(e);
        }
    }

    public static DANService danServiceProvide(String nomLogin, String userEmail, String langueIso) {
        User unConnectedUser = new User();
        String owner = "";
        unConnectedUser.setNomLogin(nomLogin);
        unConnectedUser.setEmail(userEmail);
        try {
            EBApplication app = (EBApplication) GlobazServer.getCurrentSystem().getApplication(
                    EBApplication.APPLICATION_ID);
            owner = app.getProperty("ebusiness.owner");
            return BackProvider.getDanService(unConnectedUser, owner, langueIso);
        } catch (Exception e) {
            // Problem général, le service n'est pas utilisable ...
            JadeLogger.fatal(ServicesProviders.class, e);
            throw new IllegalStateException(e);
        }
    }

    public static PartnerWebService partnerWebServiceProvide(BSession session) {
        User unConnectedUser = new User();
        unConnectedUser.setNomLogin(session.getUserId());
        unConnectedUser.setEmail(session.getUserEMail());
        try {
            EBApplication app = (EBApplication) GlobazServer.getCurrentSystem().getApplication(
                    EBApplication.APPLICATION_ID);
            String owner = app.getProperty("ebusiness.owner");

            return BackProvider.getPartnerWebService(unConnectedUser, owner, session.getIdLangueISO());
        } catch (Exception e) {
            // Problem général, le service n'est pas utilisable ...
            JadeLogger.fatal(ServicesProviders.class, e);
            throw new IllegalStateException(e);
        }

    }

    public static PartnerWebService partnerWebServiceProvide(String nomLogin, String userEmail, String langueIso) {
        User unConnectedUser = new User();
        unConnectedUser.setNomLogin(nomLogin);
        unConnectedUser.setEmail(userEmail);
        try {
            EBApplication app = (EBApplication) GlobazServer.getCurrentSystem().getApplication(
                    EBApplication.APPLICATION_ID);
            String owner = app.getProperty("ebusiness.owner");

            return BackProvider.getPartnerWebService(unConnectedUser, owner, langueIso);
        } catch (Exception e) {
            // Problem général, le service n'est pas utilisable ...
            JadeLogger.fatal(ServicesProviders.class, e);
            throw new IllegalStateException(e);
        }

    }

    public static PUCSService pucsServiceProvide(BSession session) {
        User unConnectedUser = new User();
        unConnectedUser.setNomLogin(session.getUserId());
        unConnectedUser.setEmail(session.getUserEMail());
        try {
            EBApplication app = (EBApplication) GlobazServer.getCurrentSystem().getApplication(
                    EBApplication.APPLICATION_ID);
            String owner = app.getProperty("ebusiness.owner");

            return BackProvider.getPucsService(unConnectedUser, owner, session.getIdLangueISO());
        } catch (Exception e) {
            // Problem général, le service n'est pas utilisable ...
            JadeLogger.fatal(ServicesProviders.class, e);
            throw new IllegalStateException(e);
        }

    }

    public static PUCSService pucsServiceProvide(String nomLogin, String userEmail, String langueIso) {
        User unConnectedUser = new User();
        unConnectedUser.setNomLogin(nomLogin);
        unConnectedUser.setEmail(userEmail);
        try {
            EBApplication app = (EBApplication) GlobazServer.getCurrentSystem().getApplication(
                    EBApplication.APPLICATION_ID);
            String owner = app.getProperty("ebusiness.owner");

            return BackProvider.getPucsService(unConnectedUser, owner, langueIso);
        } catch (Exception e) {
            // Problem général, le service n'est pas utilisable ...
            JadeLogger.fatal(ServicesProviders.class, e);
            throw new IllegalStateException(e);
        }
    }

    public static PACService pacServiceProvide(BSession session) {
        User unConnectedUser = new User();
        unConnectedUser.setNomLogin(session.getUserId());
        unConnectedUser.setEmail(session.getUserEMail());
        try {
            EBApplication app = (EBApplication) GlobazServer.getCurrentSystem().getApplication(
                    EBApplication.APPLICATION_ID);
            String owner = app.getProperty("ebusiness.owner");

            return BackProvider.getPacService(unConnectedUser, owner, session.getIdLangueISO());
        } catch (Exception e) {
            // Problem général, le service n'est pas utilisable ...
            JadeLogger.fatal(ServicesProviders.class, e);
            throw new IllegalStateException(e);
        }
    }

    public static MailService mailServiceProvide(BSession session) {
        User unConnectedUser = new User();
        unConnectedUser.setNomLogin(session.getUserId());
        unConnectedUser.setEmail(session.getUserEMail());
        try {
            EBApplication app = (EBApplication) GlobazServer.getCurrentSystem().getApplication(
                    EBApplication.APPLICATION_ID);
            String owner = app.getProperty("ebusiness.owner");

            return BackProvider.getMailService(unConnectedUser, owner, session.getIdLangueISO());
        } catch (Exception e) {
            // Problem général, le service n'est pas utilisable ...
            JadeLogger.fatal(ServicesProviders.class, e);
            throw new IllegalStateException(e);
        }
    }
}
