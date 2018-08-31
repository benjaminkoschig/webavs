package ch.globaz.vulpecula.ws.utils;

import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.jade.log.JadeLogger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utilitaire pour les WebServices
 * 
 */
public class UtilsService {

    final static String DATE_FORMAT = "dd.MM.yyyy";

    /**
     * Permet d'initialiser une session anonyme
     * 
     * @return
     */
    public static BSession initSession() {
    	return initSession("VULPECULA");
    }
    
    public static BSession initSession(String application) {

        BSession session = null;

        try {
            session = (BSession) GlobazServer.getCurrentSystem().getApplication(application).newSession();
            session.connect("ebu", "ebusiness");
        } catch (Exception e) {
            JadeLogger.error("An error happened while getting a new session!", e);
        }

        return session;
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
