package globaz.caisse.report.helper;

import globaz.framework.printing.itext.api.FWIDocumentInterface;
import globaz.framework.printing.itext.api.FWIImporterInterface;
import globaz.globall.db.BApplication;

/**
 * Inteface Description : Helper Interface, d�finitions des m�thodes pour la mise � jour des ent�tes et pieds de pages
 * des documents, sp�cifiques aux diff�rentes caisses. Date de cr�ation: 10 ao�t 04
 * 
 * @author scr
 */
public interface ICaisseReportHelper {

    // Liste des param�tres des ent�tes et pieds de pages des documents

    public static String PARAM_BODY_CCP = "P_BODY_CCP";

    // R�pertoire par d�faut des models jasper
    static String PARAM_DEFAULT_MODEL_PATH = "P_DEFAULT_MODEL_PATH";

    // adresse du tier
    static String PARAM_HEADER_ADRESSE = "P_HEADER_ADRESSE";
    // adresse de la caisse
    static String PARAM_HEADER_ADRESSE_CAISSE = "P_HEADER_ADRESSE_CAISSE";

    // libelle de l'adresse de la caisse (utiliser pour la CS)
    public static String PARAM_HEADER_COMPTE_CAISSE_LIBELLE = "P_HEADER_COMPTE_CAISSE_LIBELLE";

    // compte de la caisse
    public static String PARAM_HEADER_COMTPE_CAISSE = "P_HEADER_COMPTE_CAISSE";

    public static String PARAM_HEADER_CONFIDENTIEL = "P_HEADER_CONFIDENTIEL";

    // Date de g�n�ration du document
    static String PARAM_HEADER_DATE = "P_HEADER_DATE";

    static String PARAM_HEADER_DONNEES = "P_HEADER_DONNEES";

    // adresse email de la caisse
    static String PARAM_HEADER_EMAIL_CAISSE = "P_HEADER_EMAIL_CAISSE";

    // adresse email du collaborateur de la caisse
    static String PARAM_HEADER_EMAIL_COLLABORATEUR = "P_HEADER_EMAIL_COLLABORATEUR";

    // n� de fax de la caisse
    static String PARAM_HEADER_FAX_CAISSE = "P_HEADER_FAX_CAISSE";
    // Tous les libell�s : N� Affili� ; N� AVS ; ...
    static String PARAM_HEADER_LIBELLES = "P_HEADER_LIBELLES";
    // nom du collaborateur de la caisse
    static String PARAM_HEADER_NOM_COLLABORATEUR = "P_HEADER_NOM_COLLABORATEUR";
    // num�ro de r�f�rence de l'assur�
    public static String PARAM_HEADER_NUMERO_REFERENCE = "P_HEADER_NUMERO_REFERENCE";
    // pr�fixe compte postal abr�g�
    static String PARAM_HEADER_PREFIXE_CP_ABREGE = "P_HEADER_PREFIXE_CP_ABREGE";
    // pr�fixe t�l�phone abr�g�
    static String PARAM_HEADER_PREFIXE_FAX_ABREGE = "P_HEADER_PREFIXE_FAX_ABREGE";
    // pr�fixe t�l�phone abr�g�
    static String PARAM_HEADER_PREFIXE_TEL_ABREGE = "P_HEADER_PREFIXE_TEL_ABREGE";

    public static String PARAM_HEADER_RECOMMANDEE = "P_HEADER_RECOMMANDEE";

    // service du collaborateur de la caisse
    static String PARAM_HEADER_SERVICE_COLLABORATEUR = "P_HEADER_SERVICE_COLLABORATEUR";

    // site web de la caisse
    static String PARAM_HEADER_SITE_CAISSE = "P_HEADER_SITE_CAISSE";
    // n� de t�l�phone de la caisse
    static String PARAM_HEADER_TEL_CAISSE = "P_HEADER_TEL_CAISSE";

    // no de t�l�phone du collaborateur de la caisse
    static String PARAM_HEADER_TEL_COLLABORATEUR = "P_HEADER_TEL_COLLABORATEUR";

    public static String PARAM_HEADER_TYPECOURRIER = "P_HEADER_TYPECOURRIER";
    static String PARAM_SIGNATURE_2SIGNATAIRES = "P_SIGNATURE_2SIGNATAIRES";

    // nom de la caisse
    static String PARAM_SIGNATURE_CAISSE = "P_SIGNATURE_CAISSE";

    // extra data
    static String PARAM_SIGNATURE_DATA = "P_SIGNATURE_DATA";

    // // Constantes pour le visa
    // static String PARAM_SUBREPORT_NOM_COLLABORATEUR_VISA =
    // "P_SUBREPORT_NOM_COLLABORATEUR_VISA";
    // static String PARAM_SUBREPORT_NUM_TEL_VISA = "P_SUBREPORT_NUM_TEL_VISA";

    // nom de l'image (gif ou jpg) r�pr�sentant la signature
    static String PARAM_SIGNATURE_IMG = "P_SIGNATURE_IMG";

    // service
    static String PARAM_SIGNATURE_SERVICE = "P_SIGNATURE_SERVICE";
    static String PARAM_SIGNATURE_SERVICE2 = "P_SIGNATURE_SERVICE2";

    // nom du signataire, collaborateur de la caisse
    static String PARAM_SIGNATURE_SIGNATAIRE = "P_SIGNATURE_SIGNATAIRE";

    static String PARAM_SIGNATURE_SIGNATAIRE2 = "P_SIGNATURE_SIGNATAIRE2";
    // nom du pied de page du sous-rapport
    static String PARAM_SUBREPORT_FOOTER = "P_SUBREPORT_FOOTER";

    // nom de l'ent�te du sous-rapport
    static String PARAM_SUBREPORT_HEADER = "P_SUBREPORT_HEADER";

    // nom de la signature du sous-rapport
    static String PARAM_SUBREPORT_SIGNATURE = "P_SUBREPORT_SIGNATURE";

    /**
     * Method addFooterParameters. Ajoute au document les diff�rents param�tres du pied de page
     * 
     * @param doc
     * @param bean
     * @throws Exception
     */
    public void addFooterParameters(FWIDocumentInterface doc) throws Exception;

    /**
     * Method addFooterParameters. Ajoute au document les diff�rents param�tres du pied de page
     * 
     * @param doc
     * @param bean
     * @throws Exception
     */
    public void addFooterParameters(FWIImporterInterface doc) throws Exception;

    /**
     * Method addHeaderParameters. Ajoute au document les diff�rents param�tres de l'ent�te
     * 
     * @param doc
     * @param bean
     * @throws Exception
     */
    public void addHeaderParameters(FWIDocumentInterface doc, CaisseHeaderReportBean bean) throws Exception;

    /**
     * Method addHeaderParameters. Ajoute au document les diff�rents param�tres de l'ent�te
     * 
     * @param doc
     * @param bean
     * @throws Exception
     */
    public void addHeaderParameters(FWIImporterInterface doc, CaisseHeaderReportBean bean) throws Exception;

    /**
     * Method addHeaderParameters. Ajoute au document les diff�rents param�tres de l'ent�te
     * 
     * @param doc
     * @param bean
     * @throws Exception
     */
    public void addHeaderParameters(FWIImporterInterface doc, CaisseHeaderReportBean bean, String param)
            throws Exception;

    /**
     * Method addSignatureParameters. Ajoute au document les diff�rents param�tres de la signature
     * 
     * @param doc
     * @throws Exception
     */
    public void addSignatureParameters(FWIDocumentInterface doc) throws Exception;

    // /**
    // * Method addHeaderParameters. Ajoute au document les diff�rents
    // param�tres de l'ent�te
    // * @param doc
    // * @param JadeUser
    // * @throws Exception
    // */
    // public void addFooterParameters(FWIDocumentInterface doc, JadeUser user)
    // throws Exception;

    /**
     * Method addSignatureParameters. Ajoute au document les diff�rents param�tres de la signature
     * 
     * @param doc
     * @param bean
     * @throws Exception
     */
    public void addSignatureParameters(FWIDocumentInterface doc, CaisseSignatureReportBean bean) throws Exception;

    /**
     * Method addSignatureParameters. Ajoute au document les diff�rents param�tres de la signature
     * 
     * @param doc
     * @param param
     *            Le champ 'data' de la signature peut contenir un param�tre variable, il prendra la valeur de 'param'.
     * @throws Exception
     */
    public void addSignatureParameters(FWIDocumentInterface doc, String param) throws Exception;

    /**
     * Method addSignatureParameters. Ajoute au document les diff�rents param�tres de la signature
     * 
     * @param doc
     * @throws Exception
     */
    public void addSignatureParameters(FWIImporterInterface doc) throws Exception;

    /**
     * Method addSignatureParameters. Ajoute au document les diff�rents param�tres de la signature
     * 
     * @param doc
     * @param param
     *            Le champ data de la signature peut contenir un param�tre variable, il prendra la valeur de 'param'.
     * @throws Exception
     */
    public void addSignatureParameters(FWIImporterInterface doc, String param) throws Exception;

    /**
     * Method init. Initialistion du composants
     * 
     * @param application
     */
    void init(BApplication application, String codeIsoLangue);

    /**
     * Method setHeaderName. Permet de saisir un en-t�te pour le document (si rien n'est saisi, �a prendra le document
     * par d�faut du fichier properties
     * 
     * @param String
     */
    public void setHeaderName(String headerName);
}
