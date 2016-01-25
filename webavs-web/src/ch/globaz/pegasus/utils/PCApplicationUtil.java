package ch.globaz.pegasus.utils;

import ch.globaz.pegasus.web.application.PCApplication;

public final class PCApplicationUtil {

    private static PCApplicationUtil instance;

    public static PCApplicationUtil getInstance() {
        if (PCApplicationUtil.instance == null) {
            PCApplicationUtil.instance = new PCApplicationUtil();
        }
        return PCApplicationUtil.instance;
    }

    private PCApplicationUtil() {
        super();
    }

    public String getDefaultApplicationName() {
        return PCApplication.DEFAULT_APPLICATION_PEGASUS;
    }

}
