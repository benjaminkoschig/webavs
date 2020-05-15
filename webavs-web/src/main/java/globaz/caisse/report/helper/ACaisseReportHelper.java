package globaz.caisse.report.helper;

import globaz.apg.enums.APTypeDePrestation;
import globaz.framework.printing.itext.api.FWIDocumentInterface;
import globaz.framework.printing.itext.api.FWIImporterInterface;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.globall.api.BIApplication;
import globaz.globall.db.BApplication;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.bean.JadeUserDetail;
import globaz.jade.admin.user.service.JadeUserDetailService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.util.AFIDEUtil;
import globaz.phenix.listes.itext.CPIListeDecisionParam;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe : type_conteneur Description : Classe abstraite , pour la mise à jour des entêtes et pieds de pages des
 * documents Date de création: 11 août 04
 * 
 * @author scr
 */
public abstract class ACaisseReportHelper implements ICaisseReportHelper {

    private final static String DEFAULT_MODEL_DIRECTORY = "defaultModel";
    public static String JASP_PROP_BODY_CCP = "body.no.ccp.caisse.";
    public final static String JASP_PROP_HEADER_ADRESSE_CAISSE = "header.adresse.caisse.";

    public final static String JASP_PROP_HEADER_COMTPE_CAISSE = "header.compte.caisse.";

    public final static String JASP_PROP_HEADER_COMTPE_CAISSE_LIBELLE = "header.compte.caisse.libelle.";
    public final static String JASP_PROP_HEADER_CONFIDENTIEL = "header.confidentiel.";
    // adresse email de la caisse
    public final static String JASP_PROP_HEADER_EMAIL_CAISSE = "header.email.caisse.";
    public final static String JASP_PROP_HEADER_EMAIL_COLLABORATEUR = "header.mail.collaborateur.";

    // n° de fax de la caisse
    public final static String JASP_PROP_HEADER_FAX_CAISSE = "header.fax.caisse.";
    public final static String JASP_PROP_HEADER_NOM_COLLABORATEUR = "header.nom.collaborateur.";
    public final static String JASP_PROP_HEADER_NUMERO_REFERENCE = "header.numero.reference.";
    // préfixe compte postal abrégé
    public final static String JASP_PROP_HEADER_PREFIXE_CP_ABREGE = "header.prefixe.cp.abrege.";
    public final static String JASP_PROP_HEADER_PREFIXE_DATE = "header.prefixe.date.";
    public final static String JASP_PROP_HEADER_PREFIXE_EMAIL_COLLABORATEUR = "header.prefixe.email.collaborateur.";
    // préfixe téléphone abrégé
    public final static String JASP_PROP_HEADER_PREFIXE_FAX_ABREGE = "header.prefixe.fax.abrege.";
    public final static String JASP_PROP_HEADER_PREFIXE_NO_AFFILIE = "header.prefixe.no.aff.";
    public final static String JASP_PROP_HEADER_PREFIXE_NO_IDE = "header.prefixe.no.ide.";
    public final static String JASP_PROP_HEADER_PREFIXE_NO_AVS = "header.prefixe.no.avs.";
    public final static String JASP_PROP_HEADER_PREFIXE_NO_SECTION = "header.prefixe.no.section.";
    public final static String JASP_PROP_HEADER_PREFIXE_NOM_COLLABORATEUR = "header.prefixe.nom.collaborateur.";
    // préfixe téléphone abrégé
    public final static String JASP_PROP_HEADER_PREFIXE_TEL_ABREGE = "header.prefixe.tel.abrege.";
    public final static String JASP_PROP_HEADER_PREFIXE_TEL_COLLABORATEUR = "header.prefixe.tel.collaborateur.";
    public final static String JASP_PROP_HEADER_RECOMMANDEE = "header.recommandee.";

    public final static String JASP_PROP_HEADER_SERVICE_COLLABORATEUR = "header.service.collaborateur.";
    // site web de la caisse
    public final static String JASP_PROP_HEADER_SITE_CAISSE = "header.site.caisse.";
    // n° de téléphone de la caisse
    public final static String JASP_PROP_HEADER_TEL_CAISSE = "header.tel.caisse.";

    public final static String JASP_PROP_HEADER_TEL_COLLABORATEUR = "header.tel.collaborateur.";
    public final static String JASP_PROP_HEADER_TYPECOURRIER = "header.typeCourrier.";
    public final static String JASP_PROP_NOM_CAISSE = "nom.caisse.";
    public final static String JASP_PROP_SIGN_DATA = "signature.data.";

    public final static String JASP_PROP_SIGN_FILE_NAME = "signature.filename";
    public final static String JASP_PROP_SIGN_IMAGE = "signature.image";
    public final static String JASP_PROP_SIGN_NOM_CAISSE = "signature.nom.caisse.";
    public final static String JASP_PROP_SIGN_NOM_SERVICE = "signature.nom.service.";
    public final static String JASP_PROP_SIGN_SIGNATAIRE = "signature.signataire.";
    public final static String JASP_PROP_SIGN_NOM_GESTIONNAIRE = "signature.nom.gestionnaire.";

    /*
     * OCA - remplace des variable dans une chaine de car par la/les valeurs des user details : exemple : abc
     * {user.Service} def {user.Phone} -> abc service1 def 078/767.66.42
     */
    public static String _replaceVars(String str, String idUser, Map map) throws Exception {
        String strFinal = "";

        if (str != null) {
            if (str.indexOf("{user.") == -1) {
                return str; // no varaiables... just return the original string
            }

            if (map == null) {
                map = new HashMap();
                if (!JadeStringUtil.isEmpty(idUser)) {
                    JadeUserDetailService uds = JadeAdminServiceLocatorProvider.getLocator().getUserDetailService();
                    JadeUserDetail[] details = uds.findForIdUser(idUser);
                    for (int i = 0; i < details.length; i++) {
                        String key = details[i].getKey();
                        String value = details[i].getValue();
                        map.put(key, value);
                    }
                }
            }

            boolean inVar = false;
            boolean startName = false;
            String var = "";
            for (int i = 0; i < str.length(); i++) {
                switch (str.charAt(i)) {
                    case '{':
                        inVar = true;
                        break;
                    case '.':
                        if (inVar) {
                            startName = true;
                        }
                        break;
                    case '}':
                        // look for cooresponding value

                        Object value = map.get(var);
                        if (value != null) {
                            strFinal += value.toString();
                        }
                        var = ""; // reset for next var
                        inVar = false;
                        startName = false;
                        break;
                    default:
                        if (startName) {
                            var += str.charAt(i);
                        } else if (!inVar) {
                            strFinal += str.charAt(i);
                        }
                }
            }
        }
        return strFinal;

    }

    private BApplication application = null;

    private CaisseHeaderReportBean bean = null;

    private String codeIsoLangue = "FR";
    protected JadePublishDocumentInfo docInfo = null;

    private String headerName = "";

    /**
     * Constructor for ACaisseReportHelper.
     */
    public ACaisseReportHelper(JadePublishDocumentInfo docInfo) {
        super();
        this.docInfo = docInfo;
    }

    /**
     * @see globaz.caisse.report.helper.ICaisseReportHelper#addFooterParameters(FWIDocumentInterface,
     *      CaisseHeaderReportBean)
     */
    @Override
    public void addFooterParameters(FWIDocumentInterface doc) throws Exception {
        doc.setParametres(ICaisseReportHelper.PARAM_SUBREPORT_FOOTER,
                getDefaultModelPath() + "/" + doc.getTemplateProperty(docInfo, FWIImportProperties.FOOTER_FILENAME));
    }

    /**
     * @see globaz.caisse.report.helper.ICaisseReportHelper#addFooterParameter(FWIImporterInterface,
     *      CaisseHeaderReportBean)
     */
    @Override
    public void addFooterParameters(FWIImporterInterface doc) throws Exception {
        doc.setParametre(ICaisseReportHelper.PARAM_SUBREPORT_FOOTER,
                getDefaultModelPath() + "/" + doc.getTemplateProperty(docInfo, FWIImportProperties.FOOTER_FILENAME));
    }

    /**
     * @see globaz.caisse.report.helper.ICaisseReportHelper#addHeaderParameters(FWIDocumentInterface,
     *      CaisseHeaderReportBean) OCA
     */
    @Override
    public void addHeaderParameters(FWIDocumentInterface doc, CaisseHeaderReportBean bean) throws Exception {
        this.bean = bean;
        String varTemp = "";
        doc.setParametres(ICaisseReportHelper.PARAM_DEFAULT_MODEL_PATH, getDefaultModelPath());
        doc.setParametres(ICaisseReportHelper.PARAM_SUBREPORT_HEADER,
                getDefaultModelPath() + "/" + this.getDefaultHeaderPath(doc));
        doc.setParametres(ICaisseReportHelper.PARAM_HEADER_ADRESSE_CAISSE,
                doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_ADRESSE_CAISSE + codeIsoLangue));
        doc.setParametres(ICaisseReportHelper.PARAM_HEADER_ADRESSE, bean.getAdresse());

        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_COMTPE_CAISSE_LIBELLE
                + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametres(ICaisseReportHelper.PARAM_HEADER_COMPTE_CAISSE_LIBELLE, varTemp);
        }
        // Utiliser pour le document de la caisse suisse
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_COMTPE_CAISSE + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametres(ICaisseReportHelper.PARAM_HEADER_COMTPE_CAISSE, varTemp);
        }
        // Utiliser pour le document de la caisse suisse
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_NUMERO_REFERENCE
                + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametres(ICaisseReportHelper.PARAM_HEADER_NUMERO_REFERENCE, varTemp);
        }
        // N° CCP
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_BODY_CCP + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametres(ICaisseReportHelper.PARAM_BODY_CCP, varTemp);
        }
        // prefixe tél abrégé
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_TEL_ABREGE
                + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametres(ICaisseReportHelper.PARAM_HEADER_PREFIXE_TEL_ABREGE, varTemp);
        }
        // prefixe fax abrégé
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_FAX_ABREGE
                + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametres(ICaisseReportHelper.PARAM_HEADER_PREFIXE_FAX_ABREGE, varTemp);
        }
        // prefixe compte postal abrégé
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_CP_ABREGE
                + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametres(ICaisseReportHelper.PARAM_HEADER_PREFIXE_CP_ABREGE, varTemp);
        }
        // Numéro de téléphone caisse
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_TEL_CAISSE + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametres(ICaisseReportHelper.PARAM_HEADER_TEL_CAISSE, varTemp);
        }
        // Numéro de fax de la caisse
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_FAX_CAISSE + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametres(ICaisseReportHelper.PARAM_HEADER_FAX_CAISSE, varTemp);
        }
        // Site internet de la caisse
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_SITE_CAISSE + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametres(ICaisseReportHelper.PARAM_HEADER_SITE_CAISSE, varTemp);
        }
        // Email de la caisse
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_EMAIL_CAISSE + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametres(ICaisseReportHelper.PARAM_HEADER_EMAIL_CAISSE, varTemp);
        }

        /*
         * TODO modif :
         */

        /* EMAIL */
        String email = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_EMAIL_COLLABORATEUR
                + codeIsoLangue);
        String emailCol = bean.getEmailCollaborateur();
        if ((email == null) || email.equalsIgnoreCase("$emailUser")) {
            email = emailCol == null ? emailCol : emailCol.trim();
            if (JadeStringUtil.isEmpty(email)) {
                email = "";
            }
        }
        if (!JadeStringUtil.isEmpty(email)) {
            doc.setParametres(
                    ICaisseReportHelper.PARAM_HEADER_EMAIL_COLLABORATEUR,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_EMAIL_COLLABORATEUR
                            + codeIsoLangue)
                            + email);
        } else {
            doc.setParametres(ICaisseReportHelper.PARAM_HEADER_EMAIL_COLLABORATEUR, "");
        }

        /* NOM */
        String nom = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_NOM_COLLABORATEUR
                + codeIsoLangue);
        String nomCol = bean.getNomCollaborateur();
        if ((nom == null) || nom.equalsIgnoreCase("$nomUser")) {
            nom = nomCol == null ? nomCol : nomCol.trim();
            if (JadeStringUtil.isEmpty(nom)) {
                nom = "";
            }
        }
        if (!JadeStringUtil.isEmpty(nom)) {
            doc.setParametres(
                    ICaisseReportHelper.PARAM_HEADER_NOM_COLLABORATEUR,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_NOM_COLLABORATEUR
                            + codeIsoLangue)
                            + nom);
        } else {
            doc.setParametres(ICaisseReportHelper.PARAM_HEADER_NOM_COLLABORATEUR, "");
        }

        /* SERVICE - */
        String service = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_SERVICE_COLLABORATEUR
                + codeIsoLangue);
        String serCol = bean.getServiceCollaborateur();
        if ((service == null) || service.equalsIgnoreCase("$serviceUser")) {
            service = serCol == null ? serCol : serCol.trim();
            if (JadeStringUtil.isEmpty(service)) {
                service = "";
            }
        }
        if (!JadeStringUtil.isEmpty(service)) {
            doc.setParametres(ICaisseReportHelper.PARAM_HEADER_SERVICE_COLLABORATEUR, service);
        } else {
            doc.setParametres(ICaisseReportHelper.PARAM_HEADER_SERVICE_COLLABORATEUR, "");
        }

        /* TEL */
        String tel = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_TEL_COLLABORATEUR
                + codeIsoLangue);
        if (tel != null) {
            String userId = "";
            if ((bean != null) && (bean.getUser() != null)) {
                JadeUser user = bean.getUser();
                if (user != null) {
                    userId = user.getIdUser();
                }
            }
            tel = ACaisseReportHelper._replaceVars(tel, userId, null);
        }
        String telCol = bean.getTelCollaborateur();
        if ((tel == null) || tel.equalsIgnoreCase("$telUser")) {
            tel = telCol == null ? telCol : telCol.trim();
            if (JadeStringUtil.isEmpty(tel)) {
                tel = "";
            }
        }
        if (!JadeStringUtil.isEmpty(tel)) {
            doc.setParametres(
                    ICaisseReportHelper.PARAM_HEADER_TEL_COLLABORATEUR,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_TEL_COLLABORATEUR
                            + codeIsoLangue)
                            + tel);
        } else {
            doc.setParametres(ICaisseReportHelper.PARAM_HEADER_TEL_COLLABORATEUR, "");
        }

        /*
         * fin modif
         */

        // On place le numéro AVS ; d'Affilié et de section dans le même champ
        // pour eviter un trou s'il n'y a pas de numéro AVS par exemple.
        doc.setParametres(ICaisseReportHelper.PARAM_HEADER_DONNEES, getDonnees(bean));
        doc.setParametres(ICaisseReportHelper.PARAM_HEADER_LIBELLES, this.getLibelles(doc, bean));

        if (!JadeStringUtil.isEmpty(bean.getDate() == null ? bean.getDate() : bean.getDate().trim())) {
            doc.setParametres(ICaisseReportHelper.PARAM_HEADER_DATE,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_DATE + codeIsoLangue)
                            + bean.getDate());
        } else if (!JadeStringUtil.isNull(bean.getDate())) {
            doc.setParametres(ICaisseReportHelper.PARAM_HEADER_DATE, "");
        }

        if (bean.isRecommandee()) {
            doc.setParametres(ICaisseReportHelper.PARAM_HEADER_RECOMMANDEE,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_RECOMMANDEE + codeIsoLangue));
        }
        try {
            doc.setParametres(ICaisseReportHelper.PARAM_HEADER_TYPECOURRIER,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_TYPECOURRIER + codeIsoLangue));
        } catch (Exception e) {
            doc.setParametres(ICaisseReportHelper.PARAM_HEADER_TYPECOURRIER, "");
        }
        if (bean.isConfidentiel()) {
            doc.setParametres(ICaisseReportHelper.PARAM_HEADER_CONFIDENTIEL,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_CONFIDENTIEL + codeIsoLangue));
        }
    }

    /**
     * @see globaz.caisse.report.helper.ICaisseReportHelper#addHeaderParameter(FWIImporterInterface,
     *      CaisseHeaderReportBean)
     */
    @Override
    public void addHeaderParameters(FWIImporterInterface doc, CaisseHeaderReportBean bean) throws Exception {
        this.bean = bean;
        String varTemp = "";
        doc.setParametre(ICaisseReportHelper.PARAM_DEFAULT_MODEL_PATH, getDefaultModelPath());
        doc.setParametre(ICaisseReportHelper.PARAM_SUBREPORT_HEADER,
                getDefaultModelPath() + "/" + this.getDefaultHeaderPath(doc));

        doc.setParametre(ICaisseReportHelper.PARAM_HEADER_ADRESSE_CAISSE,
                doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_ADRESSE_CAISSE + codeIsoLangue));

        // Utiliser pour le document de la caisse suisse
        if (!JadeStringUtil.isEmpty(doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_COMTPE_CAISSE
                + codeIsoLangue))) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_COMTPE_CAISSE, doc.getTemplateProperty(docInfo,
                    ACaisseReportHelper.JASP_PROP_HEADER_COMTPE_CAISSE + codeIsoLangue));
        }
        // Utiliser pour le document de la caisse suisse
        if (!JadeStringUtil.isEmpty(doc.getTemplateProperty(docInfo,
                ACaisseReportHelper.JASP_PROP_HEADER_COMTPE_CAISSE_LIBELLE + codeIsoLangue))) {
            doc.setParametre(
                    ICaisseReportHelper.PARAM_HEADER_COMPTE_CAISSE_LIBELLE,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_COMTPE_CAISSE_LIBELLE
                            + codeIsoLangue));
        }
        // Utiliser pour le document de la caisse suisse
        if (!JadeStringUtil.isEmpty(doc.getTemplateProperty(docInfo,
                ACaisseReportHelper.JASP_PROP_HEADER_NUMERO_REFERENCE + codeIsoLangue))) {
            doc.setParametre(
                    ICaisseReportHelper.PARAM_HEADER_NUMERO_REFERENCE,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_NUMERO_REFERENCE
                            + codeIsoLangue));
        }

        doc.setParametre(ICaisseReportHelper.PARAM_HEADER_ADRESSE, bean.getAdresse());

        // N° CCP
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_BODY_CCP + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametre(ICaisseReportHelper.PARAM_BODY_CCP, varTemp);
        }
        // prefixe tél abrégé
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_TEL_ABREGE
                + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_PREFIXE_TEL_ABREGE, varTemp);
        }

        // prefixe fax abrégé
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_FAX_ABREGE
                + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_PREFIXE_FAX_ABREGE, varTemp);
        }
        // prefixe compte postal abrégé
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_CP_ABREGE
                + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_PREFIXE_CP_ABREGE, varTemp);
        }
        // Numéro de téléphone caisse
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_TEL_CAISSE + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_TEL_CAISSE, varTemp);
        }
        // Numéro de fax de la caisse
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_FAX_CAISSE + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_FAX_CAISSE, varTemp);
        }
        // Site internet de la caisse
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_SITE_CAISSE + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_SITE_CAISSE, varTemp);
        }
        // Email de la caisse
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_EMAIL_CAISSE + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_EMAIL_CAISSE, varTemp);
        }

        /*
         * TODO Modif
         */

        /* EMAIL */
        String email = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_EMAIL_COLLABORATEUR
                + codeIsoLangue);
        String emailCol = bean.getEmailCollaborateur();
        if (JadeStringUtil.isEmpty(email) || email.equalsIgnoreCase("$emailUser")) {
            email = emailCol == null ? emailCol : emailCol.trim();
            if (JadeStringUtil.isEmpty(email)) {
                email = "";
            }
        }
        if (!JadeStringUtil.isEmpty(email)) {
            doc.setParametre(
                    ICaisseReportHelper.PARAM_HEADER_EMAIL_COLLABORATEUR,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_EMAIL_COLLABORATEUR
                            + codeIsoLangue)
                            + email);
        } else {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_EMAIL_COLLABORATEUR, "");
        }

        /* NOM */
        String nom = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_NOM_COLLABORATEUR
                + codeIsoLangue);
        String nomCol = bean.getNomCollaborateur();
        if (JadeStringUtil.isEmpty(nom) || nom.equalsIgnoreCase("$nomUser")) {
            nom = nomCol == null ? nomCol : nomCol.trim();
            if (JadeStringUtil.isEmpty(nom)) {
                nom = "";
            }
        }
        if (!JadeStringUtil.isEmpty(nom)) {
            doc.setParametre(
                    ICaisseReportHelper.PARAM_HEADER_NOM_COLLABORATEUR,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_NOM_COLLABORATEUR
                            + codeIsoLangue)
                            + nom);
        } else {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_NOM_COLLABORATEUR, "");
        }

        /* SERVICE */
        String service = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_SERVICE_COLLABORATEUR
                + codeIsoLangue);
        String serCol = bean.getServiceCollaborateur();
        if (JadeStringUtil.isEmpty(service) || service.equalsIgnoreCase("$serviceUser")) {
            service = serCol == null ? serCol : serCol.trim();
            if (JadeStringUtil.isEmpty(service)) {
                service = "";
            }
        }
        if (!JadeStringUtil.isEmpty(service)) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_SERVICE_COLLABORATEUR, service);
        } else {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_SERVICE_COLLABORATEUR, "");
        }

        /* TEL */
        String tel = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_TEL_COLLABORATEUR
                + codeIsoLangue);
        if (tel != null) {
            String userId = "";
            if ((bean != null) && (bean.getUser() != null)) {
                JadeUser user = bean.getUser();
                if (user != null) {
                    userId = user.getIdUser();
                }
            }
            tel = ACaisseReportHelper._replaceVars(tel, userId, null);
        }
        String telCol = bean.getTelCollaborateur();
        if (JadeStringUtil.isEmpty(tel) || tel.equalsIgnoreCase("$telUser")) {
            tel = telCol == null ? telCol : telCol.trim();
            if (JadeStringUtil.isEmpty(tel)) {
                tel = "";
            }
        }
        if (!JadeStringUtil.isEmpty(tel)) {
            doc.setParametre(
                    ICaisseReportHelper.PARAM_HEADER_TEL_COLLABORATEUR,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_TEL_COLLABORATEUR
                            + codeIsoLangue)
                            + tel);
        } else {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_TEL_COLLABORATEUR, "");
        }

        /*
         * fin modif
         */

        // On place le numéro AVS ; d'Affilié et de section dans le même champ
        // pour eviter un trou s'il n'y a pas de numéro AVS par exemple.
        doc.setParametre(ICaisseReportHelper.PARAM_HEADER_DONNEES, getDonnees(bean));
        doc.setParametre(ICaisseReportHelper.PARAM_HEADER_LIBELLES, this.getLibelles(doc, bean));

        if (!JadeStringUtil.isEmpty(bean.getDate() == null ? bean.getDate() : bean.getDate().trim())) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_DATE,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_DATE + codeIsoLangue)
                            + bean.getDate());
        } else if (!JadeStringUtil.isNull(bean.getDate())) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_DATE, "");
        }

        if (bean.isRecommandee()) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_RECOMMANDEE,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_RECOMMANDEE + codeIsoLangue));
        }
        try {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_TYPECOURRIER,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_TYPECOURRIER + codeIsoLangue));
        } catch (Exception e) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_TYPECOURRIER, "");
        }
        if (bean.isConfidentiel()) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_CONFIDENTIEL,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_CONFIDENTIEL + codeIsoLangue));
        }
    }

    /**
     * 
     * A supprimer et implementer dans CaissseHeaderReportBean
     * 
     * @param doc
     * @param bean
     * @param adresseCaisse
     * @throws Exception
     */
    @Override
    public void addHeaderParameters(FWIImporterInterface doc, CaisseHeaderReportBean bean, String adresseCaisse)
            throws Exception {
        this.bean = bean;
        String varTemp = "";
        ;
        doc.setParametre(ICaisseReportHelper.PARAM_DEFAULT_MODEL_PATH, getDefaultModelPath());
        doc.setParametre(ICaisseReportHelper.PARAM_SUBREPORT_HEADER,
                getDefaultModelPath() + "/" + this.getDefaultHeaderPath(doc));

        doc.setParametre(ICaisseReportHelper.PARAM_HEADER_ADRESSE_CAISSE, adresseCaisse);

        // Utiliser pour le document de la caisse suisse
        if (!JadeStringUtil.isEmpty(doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_COMTPE_CAISSE
                + codeIsoLangue))) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_COMTPE_CAISSE, doc.getTemplateProperty(docInfo,
                    ACaisseReportHelper.JASP_PROP_HEADER_COMTPE_CAISSE + codeIsoLangue));
        }
        // Utiliser pour le document de la caisse suisse
        if (!JadeStringUtil.isEmpty(doc.getTemplateProperty(docInfo,
                ACaisseReportHelper.JASP_PROP_HEADER_COMTPE_CAISSE_LIBELLE + codeIsoLangue))) {
            doc.setParametre(
                    ICaisseReportHelper.PARAM_HEADER_COMPTE_CAISSE_LIBELLE,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_COMTPE_CAISSE_LIBELLE
                            + codeIsoLangue));
        }
        // Utiliser pour le document de la caisse suisse
        if (!JadeStringUtil.isEmpty(doc.getTemplateProperty(docInfo,
                ACaisseReportHelper.JASP_PROP_HEADER_NUMERO_REFERENCE + codeIsoLangue))) {
            doc.setParametre(
                    ICaisseReportHelper.PARAM_HEADER_NUMERO_REFERENCE,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_NUMERO_REFERENCE
                            + codeIsoLangue));
        }
        // N° CCP
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_BODY_CCP + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametre(ICaisseReportHelper.PARAM_BODY_CCP, varTemp);
        }
        // prefixe tél abrégé
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_TEL_ABREGE
                + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_PREFIXE_TEL_ABREGE, varTemp);
        }
        // prefixe fax abrégé
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_FAX_ABREGE
                + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_PREFIXE_FAX_ABREGE, varTemp);
        }
        // prefixe compte postal abrégé
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_CP_ABREGE
                + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_PREFIXE_CP_ABREGE, varTemp);
        }
        // Numéro de téléphone caisse
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_TEL_CAISSE + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_TEL_CAISSE, varTemp);
        }
        // Numéro de fax de la caisse
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_FAX_CAISSE + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_FAX_CAISSE, varTemp);
        }
        // Site internet de la caisse
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_SITE_CAISSE + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_SITE_CAISSE, varTemp);
        }
        // Email de la caisse
        varTemp = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_EMAIL_CAISSE + codeIsoLangue);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_EMAIL_CAISSE, varTemp);
        }
        doc.setParametre(ICaisseReportHelper.PARAM_HEADER_ADRESSE, bean.getAdresse());

        /*
         * TODO modif
         */

        /* EMAIL */
        String email = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_EMAIL_COLLABORATEUR
                + codeIsoLangue);
        String emailCol = bean.getEmailCollaborateur();
        if ((email == null) || email.equalsIgnoreCase("$emailUser")) {
            email = emailCol == null ? emailCol : emailCol.trim();
            if (JadeStringUtil.isEmpty(email)) {
                email = "";
            }
        }
        if (!JadeStringUtil.isEmpty(email)) {
            doc.setParametre(
                    ICaisseReportHelper.PARAM_HEADER_EMAIL_COLLABORATEUR,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_EMAIL_COLLABORATEUR
                            + codeIsoLangue)
                            + email);
        } else {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_EMAIL_COLLABORATEUR, "");
        }

        /* NOM */
        String nom = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_NOM_COLLABORATEUR
                + codeIsoLangue);
        String nomCol = bean.getNomCollaborateur();
        if ((nom == null) || nom.equalsIgnoreCase("$nomUser")) {
            nom = nomCol == null ? nomCol : nomCol.trim();
            if (JadeStringUtil.isEmpty(nom)) {
                nom = "";
            }
        }
        if (!JadeStringUtil.isEmpty(nom)) {
            doc.setParametre(
                    ICaisseReportHelper.PARAM_HEADER_NOM_COLLABORATEUR,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_NOM_COLLABORATEUR
                            + codeIsoLangue)
                            + nom);
        } else {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_NOM_COLLABORATEUR, "");
        }

        /* SERVICE */
        String service = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_SERVICE_COLLABORATEUR
                + codeIsoLangue);
        String serCol = bean.getServiceCollaborateur();
        if ((service == null) || service.equalsIgnoreCase("$serviceUser")) {
            service = serCol == null ? serCol : serCol.trim();
            if (JadeStringUtil.isEmpty(service)) {
                service = "";
            }
        }
        if (!JadeStringUtil.isEmpty(service)) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_SERVICE_COLLABORATEUR, service);
        } else {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_SERVICE_COLLABORATEUR, "");
        }

        /* TEL */
        String tel = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_TEL_COLLABORATEUR
                + codeIsoLangue);
        if (tel != null) {
            String userId = "";
            if ((bean != null) && (bean.getUser() != null)) {
                JadeUser user = bean.getUser();
                if (user != null) {
                    userId = user.getIdUser();
                }
            }
            tel = ACaisseReportHelper._replaceVars(tel, userId, null);
        }
        String telCol = bean.getTelCollaborateur();
        if ((tel == null) || JadeStringUtil.isEmpty(tel) || tel.equalsIgnoreCase("$telUser")) {
            tel = telCol == null ? telCol : telCol.trim();
            if (JadeStringUtil.isEmpty(tel)) {
                tel = "";
            }
        }
        if (!JadeStringUtil.isEmpty(tel)) {
            doc.setParametre(
                    ICaisseReportHelper.PARAM_HEADER_TEL_COLLABORATEUR,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_TEL_COLLABORATEUR
                            + codeIsoLangue)
                            + tel);
        } else {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_TEL_COLLABORATEUR, "");
        }

        /*
         * Fin modif
         */

        // On place le numéro AVS ; d'Affilié et de section dans le même champ
        // pour eviter un trou s'il n'y a pas de numéro AVS par exemple.
        doc.setParametre(ICaisseReportHelper.PARAM_HEADER_DONNEES, getDonnees(bean));
        doc.setParametre(ICaisseReportHelper.PARAM_HEADER_LIBELLES, this.getLibelles(doc, bean));

        if (!JadeStringUtil.isEmpty(bean.getDate() == null ? bean.getDate() : bean.getDate().trim())) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_DATE,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_DATE + codeIsoLangue)
                            + bean.getDate());
        } else if (!JadeStringUtil.isNull(bean.getDate())) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_DATE, "");
        }

        if (bean.isRecommandee()) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_RECOMMANDEE,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_RECOMMANDEE + codeIsoLangue));
        }
        try {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_TYPECOURRIER,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_TYPECOURRIER + codeIsoLangue));
        } catch (Exception e) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_TYPECOURRIER, "");
        }
        if (bean.isConfidentiel()) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_CONFIDENTIEL,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_CONFIDENTIEL + codeIsoLangue));
        }

    }

    /**
     * @see globaz.caisse.report.helper.ICaisseReportHelper#addSignatureParameters(FWIDocumentInterface)
     */
    @Override
    public void addSignatureParameters(FWIDocumentInterface doc) throws Exception {
        this.addSignatureParameters(doc, (String) null);
    }

    /**
     * @see globaz.caisse.report.helper.ICaisseReportHelper#addHeaderParameter(FWIImporterInterface,
     *      CaisseHeaderReportBean)
     */
    @Override
    public void addSignatureParameters(FWIDocumentInterface doc, CaisseSignatureReportBean bean) throws Exception {

        doc.setParametres(ICaisseReportHelper.PARAM_SUBREPORT_SIGNATURE,
                getDefaultModelPath() + "/" + doc.getTemplateProperty(docInfo, FWIImportProperties.SIGNATURE_FILENAME));

        /*
         * TODO modif
         */
        if (!JadeStringUtil.isEmpty(bean.getService() == null ? bean.getService() : bean.getService().trim())) {

            String service = ACaisseReportHelper._replaceVars(bean.getService(), null, null);
            doc.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_SERVICE, service);
        } else if (!JadeStringUtil.isNull(bean.getService())) {
            doc.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_SERVICE, "");
        }

        if (!JadeStringUtil.isEmpty(bean.getService2() == null ? bean.getService2() : bean.getService2().trim())) {
            doc.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_SERVICE2, bean.getService2());
        } else if (!JadeStringUtil.isNull(bean.getService2())) {
            doc.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_SERVICE2, "");
        }

        if (!JadeStringUtil.isEmpty(bean.getSignataire() == null ? bean.getSignataire() : bean.getSignataire().trim())) {
            doc.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_SIGNATAIRE, bean.getSignataire());

        } else if (!JadeStringUtil.isNull(bean.getSignataire())) {
            doc.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_SIGNATAIRE, "");
        }

        if (!JadeStringUtil.isEmpty(bean.getSignataire2() == null ? bean.getSignataire2() : bean.getSignataire2()
                .trim())) {
            doc.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_SIGNATAIRE2, bean.getSignataire2());
        } else if (!JadeStringUtil.isNull(bean.getSignataire2())) {
            doc.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_SIGNATAIRE2, "");
        }

        /*
         * end modif
         */

        if (!JadeStringUtil.isEmpty(bean.getSignatureCaisse() == null ? bean.getSignatureCaisse() : bean
                .getSignatureCaisse().trim())) {
            doc.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_CAISSE, bean.getSignatureCaisse());
        } else if (!JadeStringUtil.isNull(bean.getSignataire2())) {
            doc.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_CAISSE, "");
        }
    }

    /**
     * @see globaz.caisse.report.helper.ICaisseReportHelper#addSignatureParameters(FWIDocumentInterface, String)
     */

    @Override
    public void addSignatureParameters(FWIDocumentInterface doc, String param) throws Exception {

        doc.setParametres(ICaisseReportHelper.PARAM_DEFAULT_MODEL_PATH, getDefaultModelPath());

        if (doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_SIGN_IMAGE) != null) {
            doc.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_IMG,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_SIGN_IMAGE));
        }

        doc.setParametres(ICaisseReportHelper.PARAM_SUBREPORT_SIGNATURE,
                getDefaultModelPath() + "/" + doc.getTemplateProperty(docInfo, FWIImportProperties.SIGNATURE_FILENAME));

        if (doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_SIGN_NOM_CAISSE + codeIsoLangue) != null) {
            doc.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_CAISSE,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_SIGN_NOM_CAISSE + codeIsoLangue));
        }

        /*
         * TODO Modif
         */
        if (doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_SIGN_NOM_SERVICE + codeIsoLangue) != null) {
            String nomService = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_SIGN_NOM_SERVICE
                    + codeIsoLangue);
            if ((bean != null) && (bean.getUser() != null)) {
                nomService = JadeStringUtil.change(nomService, "{user}", bean.getNomCollaborateur());
                nomService = ACaisseReportHelper._replaceVars(nomService, bean.getUser().getIdUser(), null);
            }
            doc.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_SERVICE, nomService);
        }

        if (doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_SIGN_NOM_GESTIONNAIRE + codeIsoLangue) != null
                && (bean != null && APTypeDePrestation.PANDEMIE.equals(bean.getTypePrestation()))) {
            String nomGestionnaire = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_SIGN_NOM_GESTIONNAIRE
                    + codeIsoLangue);
            if ((bean != null) && (bean.getUser() != null)) {
                nomGestionnaire = JadeStringUtil.change(nomGestionnaire, "{user}", bean.getNomCollaborateur());
                nomGestionnaire = ACaisseReportHelper._replaceVars(nomGestionnaire, bean.getUser().getIdUser(), null);
            }
            doc.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_GESTIONNAIRE, nomGestionnaire);
        }

        if (doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_SIGN_SIGNATAIRE + codeIsoLangue) != null) {
            String signataire = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_SIGN_SIGNATAIRE
                    + codeIsoLangue);
            if ((bean != null) && (bean.getUser() != null)) {
                signataire = JadeStringUtil.change(signataire, "{user}", bean.getNomCollaborateur());
                signataire = ACaisseReportHelper._replaceVars(signataire, bean.getUser().getIdUser(), null);
            }
            doc.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_SIGNATAIRE, signataire);
        }

        /*
         * Fin modif
         */

        String data = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_SIGN_DATA + codeIsoLangue);
        if (param != null) {
            data = globaz.framework.util.FWMessageFormat.format(data, param);
        }

        if (data != null) {
            doc.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_DATA, data);
        }

    }

    /**
     * @see globaz.caisse.report.helper.ICaisseReportHelper#addSignatureParameters(FWIImporterInterface)
     */
    @Override
    public void addSignatureParameters(FWIImporterInterface doc) throws Exception {
        this.addSignatureParameters(doc, (String) null);
    }

    /**
     * @see globaz.caisse.report.helper.ICaisseReportHelper#addSignatureParameters(FWIImporterInterface, String)
     */
    @Override
    public void addSignatureParameters(FWIImporterInterface doc, String param) throws Exception {
        doc.setParametre(ICaisseReportHelper.PARAM_DEFAULT_MODEL_PATH, getDefaultModelPath());

        if (doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_SIGN_IMAGE) != null) {
            doc.setParametre(ICaisseReportHelper.PARAM_SIGNATURE_IMG,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_SIGN_IMAGE));
        }

        doc.setParametre(ICaisseReportHelper.PARAM_SUBREPORT_SIGNATURE,
                getDefaultModelPath() + "/" + doc.getTemplateProperty(docInfo, FWIImportProperties.SIGNATURE_FILENAME));

        if (doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_SIGN_NOM_CAISSE + codeIsoLangue) != null) {
            doc.setParametre(ICaisseReportHelper.PARAM_SIGNATURE_CAISSE,
                    doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_SIGN_NOM_CAISSE + codeIsoLangue));
        }

        if (doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_SIGN_NOM_SERVICE + codeIsoLangue) != null) {
            String nomService = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_SIGN_NOM_SERVICE
                    + codeIsoLangue);
            if ((bean != null) && (bean.getUser() != null)) {
                nomService = JadeStringUtil.change(nomService, "{user}", bean.getNomCollaborateur());
                nomService = ACaisseReportHelper._replaceVars(nomService, bean.getUser().getIdUser(), null);
            }
            doc.setParametre(ICaisseReportHelper.PARAM_SIGNATURE_SERVICE, nomService);
        }

        if ((doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_SIGN_NOM_GESTIONNAIRE + codeIsoLangue) != null)
                && (bean != null && APTypeDePrestation.PANDEMIE.equals(bean.getTypePrestation()))) {
            String nomGestionnaire = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_SIGN_NOM_GESTIONNAIRE
                    + codeIsoLangue);
            if ((bean != null) && (bean.getUser() != null)) {
                nomGestionnaire = JadeStringUtil.change(nomGestionnaire, "{user}", bean.getNomCollaborateur());
                nomGestionnaire = ACaisseReportHelper._replaceVars(nomGestionnaire, bean.getUser().getIdUser(), null);
            }
            doc.setParametre(ICaisseReportHelper.PARAM_SIGNATURE_GESTIONNAIRE, nomGestionnaire);
        }

        /*
         * TODO modif
         */
        String signataire = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_SIGN_SIGNATAIRE
                + codeIsoLangue);
        if ((bean != null) && (bean.getUser() != null)) {
            signataire = JadeStringUtil.change(signataire, "{user}", bean.getNomCollaborateur());
            signataire = ACaisseReportHelper._replaceVars(signataire, bean.getUser().getIdUser(), null);
        }

        if (!JadeStringUtil.isBlank(signataire)) {
            doc.setParametre(ICaisseReportHelper.PARAM_SIGNATURE_SIGNATAIRE, signataire);
        }

        /*
         * end modif
         */

        String data = doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_SIGN_DATA + codeIsoLangue);
        if (param != null) {
            data = globaz.framework.util.FWMessageFormat.format(data, param);
        }

        if (data != null) {
            doc.setParametre(ICaisseReportHelper.PARAM_SIGNATURE_DATA, data);
        }

    }

    public String getDefaultHeaderPath(FWIDocumentInterface doc) {
        if (JadeStringUtil.isEmpty(getHeaderName())) {
            return doc.getTemplateProperty(docInfo, FWIImportProperties.HEADER_FILENAME);
        } else {
            return getHeaderName();
        }

    }

    public String getDefaultHeaderPath(FWIImporterInterface doc) {
        if (JadeStringUtil.isEmpty(getHeaderName())) {
            return doc.getTemplateProperty(docInfo, FWIImportProperties.HEADER_FILENAME);
        } else {
            return getHeaderName();
        }

    }

    /**
     * @see globaz.caisse.report.helper.ICaisseReportHelper#getDefaultModelPath()
     */
    public String getDefaultModelPath() {
        return JadeStringUtil.change(application.getExternalModelPath() + ACaisseReportHelper.DEFAULT_MODEL_DIRECTORY,
                '\\', '/');
    }

    /**
     * @param bean
     * @return
     */
    private String getDonnees(CaisseHeaderReportBean bean) {
        // On place le numéro AVS ; d'Affilié et de section dans le même champ
        // pour eviter un trou s'il n'y a pas de numéro AVS par exemple.
        StringBuffer donnees = new StringBuffer();
        if (!JadeStringUtil.isEmpty(bean.getNoAffilie() == null ? bean.getNoAffilie() : bean.getNoAffilie().trim())) {
            if (!JadeStringUtil.isEmpty(donnees.toString())) {
                donnees.append("\n");
            }
            donnees.append(bean.getNoAffilie());
            // ajout du l'IDE
            if (!JadeStringUtil.isEmpty(bean.getNumeroIDE() == null ? bean.getNumeroIDE() : bean.getNumeroIDE().trim())) {
                donnees.append("/" + bean.getNumeroIDE());
            }
        }

        if (!JadeStringUtil.isEmpty(bean.getNoAvs() == null ? bean.getNoAvs() : bean.getNoAvs().trim())) {
            if (!JadeStringUtil.isEmpty(donnees.toString())) {
                donnees.append("\n");
            }
            donnees.append(bean.getNoAvs());
        }

        if (!JadeStringUtil.isEmpty(bean.getNoSection() == null ? bean.getNoSection() : bean.getNoSection().trim())) {
            if (!JadeStringUtil.isEmpty(donnees.toString())) {
                donnees.append("\n");
            }
            donnees.append(bean.getNoSection());
        }

        return donnees.toString();
    }

    public String getHeaderName() {
        return headerName;
    }

    /**
     * @param doc
     * @param bean
     * @return
     */
    private String getLibelles(FWIDocumentInterface doc, CaisseHeaderReportBean bean) {
        // On place le numéro AVS ; d'Affilié et de section dans le même champ
        // pour eviter un trou s'il n'y a pas de numéro AVS par exemple.
        StringBuffer libelles = new StringBuffer("");
        if (!JadeStringUtil.isEmpty(bean.getNoAffilie() == null ? bean.getNoAffilie() : bean.getNoAffilie().trim())) {
            if (!JadeStringUtil.isEmpty(libelles.toString())) {
                libelles.append("\n");
            }
            libelles.append(doc.getTemplateProperty(docInfo,
                    ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_NO_AFFILIE + codeIsoLangue).trim());

            // ajout du label pour l'IDE
            if (!JadeStringUtil.isEmpty(bean.getNumeroIDE() == null ? bean.getNumeroIDE() : bean.getNumeroIDE().trim())) {
                libelles = AFIDEUtil.removeEndingSpacesAndDoublePoint(libelles);
                libelles.append("/"
                        + doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_NO_IDE
                                + codeIsoLangue));
            }
        }
        if (!JadeStringUtil.isEmpty(bean.getNoAvs() == null ? bean.getNoAvs() : bean.getNoAvs().trim())) {
            if (!JadeStringUtil.isEmpty(libelles.toString())) {
                libelles.append("\n");
            }
            libelles.append(doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_NO_AVS
                    + codeIsoLangue));
        }
        if (!JadeStringUtil.isEmpty(bean.getNoSection() == null ? bean.getNoSection() : bean.getNoSection().trim())) {
            if (!JadeStringUtil.isEmpty(libelles.toString())) {
                libelles.append("\n");
            }
            libelles.append(doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_NO_SECTION
                    + codeIsoLangue));
        }

        return libelles.toString();
    }

    /**
     * @param doc
     * @param bean
     * @return
     */
    private String getLibelles(FWIImporterInterface doc, CaisseHeaderReportBean bean) {
        // On place le numéro AVS ; d'Affilié et de section dans le même champ
        // pour eviter un trou s'il n'y a pas de numéro AVS par exemple.
        StringBuffer libelles = new StringBuffer("");
        if (!JadeStringUtil.isEmpty(bean.getNoAffilie() == null ? bean.getNoAffilie() : bean.getNoAffilie().trim())) {
            if (!JadeStringUtil.isEmpty(libelles.toString())) {
                libelles.append("\n");
            }
            libelles.append(doc.getTemplateProperty(docInfo,
                    ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_NO_AFFILIE + codeIsoLangue).trim());

            // ajout du label pour l'IDE
            if (!JadeStringUtil.isEmpty(bean.getNumeroIDE() == null ? bean.getNumeroIDE() : bean.getNumeroIDE().trim())) {
                libelles = AFIDEUtil.removeEndingSpacesAndDoublePoint(libelles);
                libelles.append("/"
                        + doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_NO_IDE
                                + codeIsoLangue));
            }
        }
        if (!JadeStringUtil.isEmpty(bean.getNoAvs() == null ? bean.getNoAvs() : bean.getNoAvs().trim())) {
            if (!JadeStringUtil.isEmpty(libelles.toString())) {
                libelles.append("\n");
            }
            libelles.append(doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_NO_AVS
                    + codeIsoLangue));
        }
        if (!JadeStringUtil.isEmpty(bean.getNoSection() == null ? bean.getNoSection() : bean.getNoSection().trim())) {
            if (!JadeStringUtil.isEmpty(libelles.toString())) {
                libelles.append("\n");
            }
            libelles.append(doc.getTemplateProperty(docInfo, ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_NO_SECTION
                    + codeIsoLangue));
        }

        return libelles.toString();
    }

    /**
     * @see globaz.caisse.report.helper.ICaisseReportHelper#init(BIApplication)
     */
    @Override
    public void init(BApplication application, String codeIsoLangue) {
        this.application = application;
        this.codeIsoLangue = codeIsoLangue.toUpperCase();
        if (!codeIsoLangue.equals("FR") && !codeIsoLangue.equals("DE") && !codeIsoLangue.equals("IT")) {
            codeIsoLangue = "FR";
        }
    }

    @Override
    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

}
