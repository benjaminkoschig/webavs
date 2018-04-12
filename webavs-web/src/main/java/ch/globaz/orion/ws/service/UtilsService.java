package ch.globaz.orion.ws.service;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionInfo;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazServer;
import globaz.jade.log.JadeLogger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.orion.business.constantes.EBProperties;

/**
 * Utilitaire pour les WebServices
 * 
 * @author sco
 * 
 */
public final class UtilsService {
    static final String DATE_FORMAT = "dd.MM.yyyy";

    private UtilsService() {
        throw new UnsupportedOperationException();
    }

    /**
     * Initialise une session anonyme
     * 
     * @return
     */
    public static BSession initSession() {
        BSession session = null;

        try {
            session = (BSession) GlobazServer.getCurrentSystem().getApplication("FRAMEWORK").newSession();
            session._connectAnonymous();
        } catch (Exception e) {
            JadeLogger.error("An error happened while getting a new session!", e);
        }

        return session;
    }

    public static BSession getSessionUserGeneric(BSession sessionWebAvs) {
        BSession sessionUserGeneric = null;
        BSessionInfo sessionInfo = new BSessionInfo();

        try {
            sessionInfo.setApplication(sessionWebAvs.getApplicationId());
            sessionInfo.setLanguageId(sessionWebAvs.getIdLangue());
            sessionInfo.setLanguageISO(sessionWebAvs.getIdLangueISO());
            sessionInfo.setUserId(UtilsService.getUserGeneric());
            sessionUserGeneric = BSessionUtil.createSession(sessionInfo);

            sessionUserGeneric.connectSession(sessionUserGeneric);
        } catch (Exception e) {
            JadeLogger.error("An error happened while getting a new session with generic user!", e);
        }

        return sessionUserGeneric;
    }

    private static String getUserGeneric() throws PropertiesException {
        return EBProperties.GENERIC_USER.getValue();
    }

    /**
     * Test une date au format dd.MM.aaaa
     * 
     * @param date
     * @return
     */
    public static boolean checkDate(String date) {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * REtourne une date au format dd.MM.yyyy sous le format yyyyMMdd
     * 
     * @param date
     * @return
     */
    public static String parseDate(String date) {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            Date d = df.parse(date);

            df = new SimpleDateFormat("yyyyMMdd");
            return df.format(d);
        } catch (ParseException e) {
            return null;
        }
    }
}
