package globaz.caisse.report.helper;

import globaz.globall.db.BApplication;
import ch.globaz.topaz.datajuicer.DocumentData;

public interface ICaisseReportHelperOO {

    // Liste des param�tres des ent�tes et pieds de pages des documents

    static String PARAM_HEADER_ADRESSE = "ADRESSE";
    static String PARAM_HEADER_ADRESSE_CAISSE = "HEADER_ADRESSE_CAISSE";
    static String PARAM_HEADER_CONFIDENTIEL = "HEADER_CONFIDENTIEL";
    static String PARAM_HEADER_DATE = "DATE_ET_LIEU";
    static String PARAM_HEADER_DONNEES = "VALEUR";
    static String PARAM_HEADER_EMAIL_COLLABORATEUR = "EMAIL_COLLABORATEUR";
    static String PARAM_HEADER_LIBELLES = "LIBELLE";
    static String PARAM_HEADER_NOM_COLLABORATEUR = "NOM_COLLABORATEUR";
    static String PARAM_HEADER_RECOMMANDEE = "HEADER_RECOMMANDE";
    static String PARAM_HEADER_SERVICE_COLLABORATEUR = "SERVICE_COLLABORATEUR";
    static String PARAM_HEADER_TEL_COLLABORATEUR = "TEL_COLLABORATEUR";

    static String PARAM_HEADER_TYPECOURRIER = "HEADER_TYPECOURRIER";

    static String PARAM_SIGNATURE_CAISSE = "SIGNATURE_NOM_CAISSE";
    static String PARAM_SIGNATURE_SERVICE = "SIGNATURE_NOM_SERVICE";
    static String PARAM_SIGNATURE_SIGNATAIRE = "SIGNATAIRE";

    /**
     * Method addFooterParameters. Ajoute au document les diff�rents param�tres du pied de page
     * 
     * @param bean
     * @throws Exception
     */
    public DocumentData addFooterParameters(DocumentData docData, CaisseHeaderReportBean bean) throws Exception;

    /**
     * Method addHeaderParameters. Ajoute au document les diff�rents param�tres de l'ent�te
     * 
     * @param bean
     * @throws Exception
     */
    public DocumentData addHeaderParameters(DocumentData docData, CaisseHeaderReportBean bean, Boolean isCopie)
            throws Exception;

    /**
     * Method addSignatureParameters. Ajoute au document les diff�rents param�tres de la signature
     * 
     * @param bean
     * @throws Exception
     */
    public DocumentData addSignatureParameters(DocumentData docData, CaisseHeaderReportBean bean) throws Exception;

    /**
     * Method init. Initialistion du composants
     * 
     * @param application
     */
    void init(BApplication application, String codeIsoLangue);

    public void setTemplateName(String templateName) throws Exception;

}
