package globaz.osiris.external;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (27.11.2001 14:32:39)
 * 
 * @author: Administrator
 */
public interface IntAdresseCourrier extends BIEntity {
    public static String AUTRE = "4";
    public static String CORRESPONDANCE = "2";
    public static String POURSUITE = "3";
    public static String PRINCIPALE = "1";

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.11.2001 07:33:29)
     * 
     * @return java.lang.String[]
     */
    String[] getAdresse();

    /**
     * Cette m�thode permet de r�cuper la ligne "� l'attention de" de l'adresse
     * 
     * @return String "� l'attention de"
     */
    String getAttention();

    /**
     * Retourne le nom du tiers.
     * 
     * @return java.lang.String
     */
    String getAutreNom();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.11.2001 07:34:28)
     * 
     * @return java.lang.String
     */
    String getCasePostale();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.06.2002 09:59:49)
     * 
     * @return java.lang.String
     */
    String getCivilite();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.11.2001 07:32:25)
     * 
     * @return java.lang.String
     */
    String getIdAdresseCourrier();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.11.2001 07:34:04)
     * 
     * @return java.lang.String
     */
    String getIdCanton();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.11.2001 07:35:16)
     * 
     * @return java.lang.String
     */
    String getLocalite();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.11.2001 07:34:04)
     * 
     * @return java.lang.String
     */
    String getNumCommuneOfs();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.11.2001 07:32:46)
     * 
     * @return java.lang.String
     */
    String getNumeroRue();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.11.2001 07:34:58)
     * 
     * @return java.lang.String
     */
    String getNumPostal();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.11.2001 07:35:39)
     * 
     * @return java.lang.String
     */
    String getPays();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.11.2001 07:36:10)
     * 
     * @return java.lang.String
     */
    String getPaysISO();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.11.2001 07:34:04)
     * 
     * @return java.lang.String
     */
    String getRue();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.11.2001 07:32:46)
     * 
     * @return java.lang.String
     */
    String getRueSansNumero();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.11.2001 07:32:46)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.11.2001 07:36:39)
     * 
     * @return int
     */
    String getTypeAdresse();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.11.2001 07:31:30)
     */
    void retrieve(BITransaction transaction, String idAdresseCourrier) throws java.lang.Exception;

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.11.2001 07:31:30)
     */
    void retrieve(String idAdresseCourrier) throws java.lang.Exception;
}
