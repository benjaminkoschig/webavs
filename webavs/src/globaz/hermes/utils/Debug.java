package globaz.hermes.utils;

/**
 * Insérez la description du type ici. Date de création : (15.10.2002 08:48:47)
 * 
 * @author: Administrator
 */
public interface Debug {

    public static boolean DEBUG = true;

    // message types

    public static final int ERROR = 0;
    public static final int INFO = 2;
    public static final int WARNING = 1;

    public void debug(String methodName, String message, int type);
}
