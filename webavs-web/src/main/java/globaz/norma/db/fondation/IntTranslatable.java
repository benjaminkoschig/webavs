package globaz.norma.db.fondation;

import globaz.globall.api.BIEntity;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (18.12.2001 17:43:46)
 * 
 * @author: Administrator
 */
public interface IntTranslatable extends BIEntity {

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.01.2002 16:58:47)
     * 
     * @return java.lang.String
     */
    String getDescription();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.01.2002 16:59:13)
     * 
     * @return java.lang.String
     * @param codeISOLangue
     *            java.lang.String
     */
    String getDescription(String codeISOLangue);

    /**
     * R�cup�re l'identification de l'objet source Date de cr�ation : (19.12.2001 11:01:05)
     * 
     * @return java.lang.String
     */
    String getIdentificationSource();

    /**
     * R�cup�re l'id traduction Date de cr�ation : (18.12.2001 17:44:26)
     * 
     * @return java.lang.String
     */
    String getIdTraduction();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.01.2002 17:00:34)
     * 
     * @param newDescription
     *            java.lang.String
     */
    void setDescription(String newDescription) throws Exception;

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.01.2002 17:01:02)
     * 
     * @param newDescription
     *            java.lang.String
     * @param codeISOLangue
     *            java.lang.String
     */
    void setDescription(String newDescription, String codeISOLangue) throws Exception;

    /**
     * Mise � jour de l'id traduction Date de cr�ation : (19.12.2001 10:45:36)
     * 
     * @param newIdTraduction
     *            java.lang.String
     */
    void setIdTraduction(String newIdTraduction);
}
