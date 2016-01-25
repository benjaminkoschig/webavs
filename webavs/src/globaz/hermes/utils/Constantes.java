package globaz.hermes.utils;

/**
 * Class containing only constant variables.<br>
 * These variables are prefixed by language (EN,FR,IT,GE)<br>
 * Then an underscore ( _ )<br>
 * Then by type (E for Error, S for Screen, and OK for good messages) Then an underscore ( _ ) <br>
 * Then an subjectively attributed code Date de cr�ation : (22.10.2002 13:28:34)
 * 
 * @author: Administrator
 */
public class Constantes {
    // French Messages
    // Exception messages are prefixed with E
    public static final String FR_E_00001 = "ERREUR";
    public static final String FR_E_00002 = "Tentative de modification de num�ro ill�gale : il existe une relation entre ce motif et un code application";
    public static final String FR_E_00003 = "Tentative de suppression ill�gale : il existe une relation entre ce motif et un code application";
    public static final String FR_E_00004 = "Aucune valeur saisie ou valeur incorrecte ";
    public static final String FR_E_00005 = "L'enregistrement fin doit �tre sup�rieur ou �gal � l'enregistrement de d�but";
    // OK Messages are prefixed with OK
    public static final String FR_OK_00001 = "OK";
    // Screen messages are prefixed with S
    public static final String FR_S_00001 = "S";
}
