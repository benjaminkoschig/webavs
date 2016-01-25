package ch.globaz.al.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Outils utiles pour l'importation. Ne doit �tre utilis� que par le projet d'importation de donn�es
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
     * date de d�but de l'importation au format spy yyyyMMddHHmmss
     */
    public static String dateDebutImport = null;
    /**
     * Indique s'il faut supprimer les anciens tarif lors de l'importation de nouveaux tarifs
     */
    public static boolean deleteOldTarifs;
    /**
     * R�pertoire devant contenir les fichiers qui ont �t� import�s avec succ�s
     */
    public static final String FILES_OK_DIR = "/../files_ok/";
    public static ArrayList<String> ignoreList = new ArrayList<String>();
    /**
     * Indique si l'importation est faite depuis des donn�es ALFA-Gest
     */
    public static boolean importFromAlfaGest = false;
    /**
     * Chemin du r�pertoire contenant les dossiers � importer
     */
    public static String importPath = null;
    public static ArrayList<String> includeList = new ArrayList<String>();
    /**
     * Permet d'indiquer qu'une importation est en cours
     */
    public static boolean isImport = false;
    /**
     * Nom du r�pertoire devant contenir les log d'importation
     */
    public static final String LOG_DIR = "/../import_logs/";
    /**
     * Noms des cat�gories de type caisse devant �tre import�s
     */
    public static List<String> nomsCategoriesTarifsCaisse;
    /**
     * mot de passe
     */
    public static String pwd = null;
    /**
     * Constante repr�sentant un allocataire
     */
    public static final String SYMB_ALLOC = "Alloc";
    /**
     * Constante repr�sentant un enfant
     */
    public static final String SYMB_ENFANT = "Enfant";
    /**
     * Utilisateur
     */
    public static String userId = null;
    /**
     * Indique si les fichiers XML doivent �tre valid�s � l'aide du fichier XSD
     */
    public static boolean validateXML = true;

    /**
     * Nom du fichier xsd � utiliser pour la validation
     */
    public static String xsdFile = null;
}