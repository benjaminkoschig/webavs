package globaz.osiris.api;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (11.03.2002 10:40:49)
 * 
 * @author: Administrator
 */
public interface APISectionDescriptor extends java.io.Serializable {
    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.05.2003 10:46:54)
     * 
     * @return java.lang.String
     */
    String getDateDebutPeriode();

    /**
     * Retourne la plus grande date entre la date de facturation + 12j et la date d'�ch�ance l�gale.
     * 
     * @author: sel Cr�� le : 10 nov. 06
     * @return max(date facturation + 12, date �ch�ance)
     */
    public String getDateEcheanceAdaptee();

    /**
     * R�cup�re la date d'�ch�ance pour la facturation Si le d�lai de base (date de facturation + 12 jours) est
     * inf�rieur � l��ch�ance l�gale, le d�lai est fix� � l��ch�ance. Sinon, c'est le d�lai de base qui figure sur la
     * facture.
     */
    public String getDateEcheanceFacturation();

    /**
     * R�cup�re la date d'�ch�ance par d�faut
     */
    public String getDateEcheanceLegale();

    /**
     * R�cup�re la date d'�ch�ance pour le LSV (recouvrement) m�me comportement que pour l'�chance facturation
     */
    public String getDateEcheanceLSV();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.05.2003 10:47:02)
     * 
     * @return java.lang.String
     */
    String getDateFinPeriode();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.12.2002 18:14:15)
     * 
     * @return java.lang.String
     */
    String getDescription();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.12.2002 18:21:26)
     * 
     * @return java.lang.String
     * @param codeISOLangue
     *            java.lang.String
     */
    String getDescription(String codeISOLangue);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (01.03.2003 13:19:41)
     * 
     * @return java.lang.String
     */
    String getErrors();

    String getIdCategorie();

    /**
     * @return idSection
     */
    public String getIdSection();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (01.03.2003 13:24:32)
     * 
     * @return java.lang.String
     */
    String getIdTypeSection();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (01.03.2003 13:18:51)
     * 
     * @return boolean
     */
    boolean hasErrors();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.12.2002 18:35:18)
     * 
     * @param session
     *            globaz.globall.api.BISession
     */
    void setISession(globaz.globall.api.BISession session);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.12.2002 18:21:50)
     * 
     * @param section
     *            globaz.osiris.api.APISection
     */
    void setSection(APISection section) throws Exception;

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.12.2002 17:19:39)
     * 
     * @param newIdExterne
     *            java.lang.String
     * @param newIdSousType
     *            java.lang.String
     * @param newDate
     *            java.lang.String
     * @param newDateDebut
     *            java.lang.String
     * @param newDateFin
     *            java.lang.String
     */
    void setSection(String newIdExterne, String newIdTypeSection, String newIdSousType, String newDate,
            String newDateDebut, String newDateFin) throws Exception;
}
