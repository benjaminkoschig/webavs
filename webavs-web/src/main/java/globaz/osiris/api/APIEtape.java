package globaz.osiris.api;

import globaz.globall.api.BIEntity;

/**
 * Insérez la description du type ici. Date de création : (25.09.2002 16:42:57)
 * 
 * @author: Administrator
 */
public interface APIEtape extends BIEntity {
    public final static String CONTINUER = "216004";
    public final static String ETAPE_POURSUITE_FORMAT = APIEtape.POURSUITE + ", " + APIEtape.CONTINUER + ", "
            + APIEtape.VENTE;
    public final static String POURSUITE = "216003";
    public final static String RAPPEL = "216001";
    public final static String SOMMATION = "216002";
    public final static String SOMMATION_AQUILA = "5200034";
    public final static String TAXATION = "216006";

    public final static String VENTE = "216005";

    public String getDescription();

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDescription(String codeIsoLangue);

    public String getIdentificationSource();

    /**
     * Getter
     */
    public java.lang.String getIdEtape();

    public java.lang.String getIdTraduction();

    public java.lang.String getTypeEtape();

    public void setDescription(String newDescription) throws Exception;

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @param newDescription
     *            java.lang.String
     */
    public void setDescription(String newDescription, String codeISOLangue);

    /*
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02) @param newDescription
     * java.lang.String @param codeISOLangue java.lang.String
     */
    public void setDescriptionDe(String newDescription) throws Exception;

    /*
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02) @param newDescription
     * java.lang.String @param codeISOLangue java.lang.String
     */
    public void setDescriptionFr(String newDescription) throws Exception;

    /*
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02) @param newDescription
     * java.lang.String @param codeISOLangue java.lang.String
     */
    public void setDescriptionIt(String newDescription) throws Exception;

    /**
     * Setter
     */
    public void setIdEtape(java.lang.String newIdEtape);

    public void setIdTraduction(java.lang.String newIdTraduction);

    public void setTypeEtape(java.lang.String newTypeEtape);
}
