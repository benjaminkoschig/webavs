package ch.globaz.common.util;

/**
 * Utilitaire pour les m�thodes li�s aux string
 */
public class Strings {

    public static String toStringOrNull(Object toReturn){
        if(toReturn == null){
            return null;
        }
        return String.valueOf(toReturn);
    }
}
