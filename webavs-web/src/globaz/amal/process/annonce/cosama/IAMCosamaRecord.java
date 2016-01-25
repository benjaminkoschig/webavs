/**
 * 
 */
package globaz.amal.process.annonce.cosama;

/**
 * Interface de d�finition des fonctions standards attendues pour les objets de type AMCosamaRecord
 * 
 * Utiliser pour la g�n�ration et parsing de fichier cosama
 * 
 * @author dhi
 * 
 */
public interface IAMCosamaRecord {

    /**
     * Information canton jura
     */
    public static String _CantonJura = "JU";
    /**
     * S�parateur de centime pour les montants, si activ�
     */
    public static String _CurrencySeparator = ",";
    /**
     * S�parateur de champ, si activ�
     */
    public static String _FieldSeparator = ";";

    /**
     * Type d'enregistrement d�tail
     */
    public static String _TypeEnregistrementDetail = "2";
    /**
     * Type d'enregistrement en-t�te
     */
    public static String _TypeEnregistrementEnTete = "1";
    /**
     * Type d'enregistrement Total
     */
    public static String _TypeEnregistrementTotal = "3";

    /**
     * Formattage des champs en fonction des longueurs COSAMA
     */
    public void formatFields();

    /**
     * R�cup�ration du type d'enregistrement
     * 
     * @return
     */
    public String getTypeEnregistrement();

    /**
     * Parse la ligne pass�e en param�tre
     * 
     * @param currentLigne
     */
    public void parseLigne(String currentLigne);

    /**
     * R�cup�ration d'une ligne repr�sentant un enregistrement cosama
     * 
     * @return Une ligne formatt�e cosama
     */
    public String writeLigne();

    /**
     * R�cup�ration d'une ligne repr�sentant un enregistrement cosama Les champs peuvent �tre s�par� par un s�parateur,
     * si bWithSeparator � true
     * 
     * @param bWithSeparator
     *            S�paration des champs avec un s�parateur
     * @return Une ligne formatt� cosama, avec s�parateurs
     */
    public String writeLigne(boolean bWithSeparator);

}
