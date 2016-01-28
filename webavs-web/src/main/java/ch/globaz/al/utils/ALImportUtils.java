package ch.globaz.al.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Outils utiles pour l'importation. Ne doit être utilisé que par le projet d'importation de données
 * 
 * @author jts
 * 
 */
public class ALImportUtils {
    /**
     * Nom de la caisse
     */
    public static String caisse = null;

    /**
     * date de début de l'importation au format spy yyyyMMddHHmmss
     */
    public static String dateDebutImport = null;
    /**
     * Indique s'il faut supprimer les anciens tarif lors de l'importation de nouveaux tarifs
     */
    public static boolean deleteOldTarifs;
    /**
     * Répertoire devant contenir les fichiers qui ont été importés avec succès
     */
    public static final String FILES_OK_DIR = "/../files_ok/";
    public static ArrayList<String> ignoreList = new ArrayList<String>();
    /**
     * Indique si l'importation est faite depuis des données ALFA-Gest
     */
    public static boolean importFromAlfaGest = false;
    /**
     * Chemin du répertoire contenant les dossiers à importer
     */
    public static String importPath = null;
    public static ArrayList<String> includeList = new ArrayList<String>();
    /**
     * Permet d'indiquer qu'une importation est en cours
     */
    public static boolean isImport = false;
    /**
     * Nom du répertoire devant contenir les log d'importation
     */
    public static final String LOG_DIR = "/../import_logs/";
    /**
     * Noms des catégories de type caisse devant être importés
     */
    public static List<String> nomsCategoriesTarifsCaisse;
    /**
     * mot de passe
     */
    public static String pwd = null;
    /**
     * Constante représentant un allocataire
     */
    public static final String SYMB_ALLOC = "Alloc";
    /**
     * Constante représentant un enfant
     */
    public static final String SYMB_ENFANT = "Enfant";
    /**
     * Utilisateur
     */
    public static String userId = null;
    /**
     * Indique si les fichiers XML doivent être validés à l'aide du fichier XSD
     */
    public static boolean validateXML = true;

    /**
     * Nom du fichier xsd à utiliser pour la validation
     */
    public static String xsdFile = null;
}