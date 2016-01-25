package globaz.osiris.api;

/**
 * Insérez la description du type ici. Date de création : (11.03.2002 10:40:49)
 * 
 * @author: Administrator
 */
public interface APISectionDescriptor extends java.io.Serializable {
    /**
     * Insérez la description de la méthode ici. Date de création : (10.05.2003 10:46:54)
     * 
     * @return java.lang.String
     */
    String getDateDebutPeriode();

    /**
     * Retourne la plus grande date entre la date de facturation + 12j et la date d'échéance légale.
     * 
     * @author: sel Créé le : 10 nov. 06
     * @return max(date facturation + 12, date échéance)
     */
    public String getDateEcheanceAdaptee();

    /**
     * Récupère la date d'échéance pour la facturation Si le délai de base (date de facturation + 12 jours) est
     * inférieur à l’échéance légale, le délai est fixé à l’échéance. Sinon, c'est le délai de base qui figure sur la
     * facture.
     */
    public String getDateEcheanceFacturation();

    /**
     * Récupère la date d'échéance par défaut
     */
    public String getDateEcheanceLegale();

    /**
     * Récupère la date d'échéance pour le LSV (recouvrement) même comportement que pour l'échance facturation
     */
    public String getDateEcheanceLSV();

    /**
     * Insérez la description de la méthode ici. Date de création : (10.05.2003 10:47:02)
     * 
     * @return java.lang.String
     */
    String getDateFinPeriode();

    /**
     * Insérez la description de la méthode ici. Date de création : (16.12.2002 18:14:15)
     * 
     * @return java.lang.String
     */
    String getDescription();

    /**
     * Insérez la description de la méthode ici. Date de création : (16.12.2002 18:21:26)
     * 
     * @return java.lang.String
     * @param codeISOLangue
     *            java.lang.String
     */
    String getDescription(String codeISOLangue);

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 13:19:41)
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
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 13:24:32)
     * 
     * @return java.lang.String
     */
    String getIdTypeSection();

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 13:18:51)
     * 
     * @return boolean
     */
    boolean hasErrors();

    /**
     * Insérez la description de la méthode ici. Date de création : (16.12.2002 18:35:18)
     * 
     * @param session
     *            globaz.globall.api.BISession
     */
    void setISession(globaz.globall.api.BISession session);

    /**
     * Insérez la description de la méthode ici. Date de création : (16.12.2002 18:21:50)
     * 
     * @param section
     *            globaz.osiris.api.APISection
     */
    void setSection(APISection section) throws Exception;

    /**
     * Insérez la description de la méthode ici. Date de création : (18.12.2002 17:19:39)
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
