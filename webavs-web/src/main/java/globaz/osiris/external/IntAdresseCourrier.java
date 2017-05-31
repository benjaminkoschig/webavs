package globaz.osiris.external;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Insérez la description du type ici. Date de création : (27.11.2001 14:32:39)
 * 
 * @author: Administrator
 */
public interface IntAdresseCourrier extends BIEntity {
    public static String AUTRE = "4";
    public static String CORRESPONDANCE = "2";
    public static String POURSUITE = "3";
    public static String PRINCIPALE = "1";

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:33:29)
     * 
     * @return java.lang.String[]
     */
    String[] getAdresse();

    /**
     * Cette méthode permet de récuper la ligne "à l'attention de" de l'adresse
     * 
     * @return String "à l'attention de"
     */
    String getAttention();

    /**
     * Retourne le nom du tiers.
     * 
     * @return java.lang.String
     */
    String getAutreNom();

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:34:28)
     * 
     * @return java.lang.String
     */
    String getCasePostale();

    /**
     * Insérez la description de la méthode ici. Date de création : (14.06.2002 09:59:49)
     * 
     * @return java.lang.String
     */
    String getCivilite();

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:32:25)
     * 
     * @return java.lang.String
     */
    String getIdAdresseCourrier();

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:34:04)
     * 
     * @return java.lang.String
     */
    String getIdCanton();

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:35:16)
     * 
     * @return java.lang.String
     */
    String getLocalite();

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:34:04)
     * 
     * @return java.lang.String
     */
    String getNumCommuneOfs();

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:32:46)
     * 
     * @return java.lang.String
     */
    String getNumeroRue();

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:34:58)
     * 
     * @return java.lang.String
     */
    String getNumPostal();

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:35:39)
     * 
     * @return java.lang.String
     */
    String getPays();

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:36:10)
     * 
     * @return java.lang.String
     */
    String getPaysISO();

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:34:04)
     * 
     * @return java.lang.String
     */
    String getRue();

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:32:46)
     * 
     * @return java.lang.String
     */
    String getRueSansNumero();

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:32:46)
     * 
     * @return java.lang.String
     */
    String getTitre();

    /**
     * Retourne le titre en fonction de la langue.
     * 
     * @param language
     * @return
     */
    String getTitre(String language);

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:36:39)
     * 
     * @return int
     */
    String getTypeAdresse();

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:31:30)
     */
    void retrieve(BITransaction transaction, String idAdresseCourrier) throws java.lang.Exception;

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:31:30)
     */
    void retrieve(String idAdresseCourrier) throws java.lang.Exception;
}
