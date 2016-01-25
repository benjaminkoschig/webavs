package ch.globaz.common;

import globaz.jade.context.JadeThread;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.businessimpl.services.adresse.TechnicalExceptionWithTiers;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class ExceptionsHandler {
    List<Exception> exceptions = new ArrayList<Exception>();

    private boolean add(Exception e) {
        return exceptions.add(e);
    }

    public static boolean add(String message, Exception e) {
        TechnicalExceptionWithTiers technicalExceptionWithTiers = new TechnicalExceptionWithTiers(message, e);
        return getInstance().add(technicalExceptionWithTiers);
    }

    public static boolean add(String message, PersonneEtendueComplexModel personne, Exception e) {
        Exception exception = new TechnicalExceptionWithTiers(message, personne, e);
        return getInstance().add(exception);
    }

    public static boolean add(String message, String idTiers, Exception e) {
        Exception exception = new TechnicalExceptionWithTiers(message, idTiers, e);
        return getInstance().add(exception);
    }

    private static synchronized ExceptionsHandler getInstance() {
        Object object = JadeThread.getTemporaryObject(ExceptionsHandler.class.getName());
        ExceptionsHandler exceptionsHandler;
        if (object != null) {
            exceptionsHandler = (ExceptionsHandler) object;
        } else {
            exceptionsHandler = new ExceptionsHandler();
            JadeThread.storeTemporaryObject(ExceptionsHandler.class.getName(), exceptionsHandler);
        }
        return exceptionsHandler;
    }

    public static void print() {
        for (Exception e : getInstance().exceptions) {
            e.printStackTrace();
        }
    }
}
