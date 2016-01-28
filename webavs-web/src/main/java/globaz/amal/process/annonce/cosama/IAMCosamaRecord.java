/**
 * 
 */
package globaz.amal.process.annonce.cosama;

/**
 * Interface de définition des fonctions standards attendues pour les objets de type AMCosamaRecord
 * 
 * Utiliser pour la génération et parsing de fichier cosama
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
     * Séparateur de centime pour les montants, si activé
     */
    public static String _CurrencySeparator = ",";
    /**
     * Séparateur de champ, si activé
     */
    public static String _FieldSeparator = ";";

    /**
     * Type d'enregistrement détail
     */
    public static String _TypeEnregistrementDetail = "2";
    /**
     * Type d'enregistrement en-tête
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
     * Récupération du type d'enregistrement
     * 
     * @return
     */
    public String getTypeEnregistrement();

    /**
     * Parse la ligne passée en paramètre
     * 
     * @param currentLigne
     */
    public void parseLigne(String currentLigne);

    /**
     * Récupération d'une ligne représentant un enregistrement cosama
     * 
     * @return Une ligne formattée cosama
     */
    public String writeLigne();

    /**
     * Récupération d'une ligne représentant un enregistrement cosama Les champs peuvent être séparé par un séparateur,
     * si bWithSeparator à true
     * 
     * @param bWithSeparator
     *            Séparation des champs avec un séparateur
     * @return Une ligne formatté cosama, avec séparateurs
     */
    public String writeLigne(boolean bWithSeparator);

}
