package globaz.caisse.report.helper;

import globaz.globall.api.BIApplication;
import globaz.globall.db.BApplication;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import ch.globaz.topaz.datajuicer.DocumentData;

public class ACaisseReportHelperOO implements ICaisseReportHelperOO {

    public final static String HEADER_ADRESSE_CAISSE = "header.adresse.caisse.";
    public final static String HEADER_COMTPE_CAISSE = "header.compte.caisse.";
    public final static String HEADER_COMTPE_CAISSE_LIBELLE = "header.compte.caisse.libelle.";
    public final static String HEADER_CONFIDENTIEL = "header.confidentiel.";
    public final static String HEADER_EMAIL_COLLABORATEUR = "header.mail.collaborateur.";

    public final static String HEADER_NOM_COLLABORATEUR = "header.nom.collaborateur.";
    public final static String HEADER_NUMERO_REFERENCE = "header.numero.reference.";
    public final static String HEADER_PREFIXE_DATE = "header.prefixe.date.";
    public final static String HEADER_PREFIXE_EMAIL_COLLABORATEUR = "header.prefixe.email.collaborateur.";

    public final static String HEADER_PREFIXE_NO_AFFILIE = "header.prefixe.no.aff.";
    public final static String HEADER_PREFIXE_NO_AVS = "header.prefixe.no.avs.";
    public final static String HEADER_PREFIXE_NO_SECTION = "header.prefixe.no.section.";
    public final static String HEADER_PREFIXE_NOM_COLLABORATEUR = "header.prefixe.nom.collaborateur.";
    public final static String HEADER_PREFIXE_TEL_COLLABORATEUR = "header.prefixe.tel.collaborateur.";
    public final static String HEADER_RECOMMANDEE = "header.recommandee.";
    public final static String HEADER_SERVICE_COLLABORATEUR = "header.service.collaborateur.";

    public final static String HEADER_TEL_COLLABORATEUR = "header.tel.collaborateur.";
    public final static String HEADER_TYPECOURRIER = "header.typeCourrier.";
    public final static String JASPER_PROPERTIES_FILENAME = "globazJasper.properties";

    public final static String SIGN_NOM_CAISSE = "signature.nom.caisse.";
    public final static String SIGN_NOM_SERVICE = "signature.nom.service.";
    public final static String SIGN_SIGNATAIRE = "signature.signataire.";
    private BApplication application = null;

    CaisseHeaderReportBean bean = null;

    private String codeIsoLangue = "FR";
    private Properties jasperProperties = new Properties();
    private Map properties = new HashMap();

    private String templateName = "";

    /**
     * Constructor for ACaisseReportHelperOO.
     */
    public ACaisseReportHelperOO() {
        super();
    }

    /**
     * Renvoie la liste des propriétés par défaut
     * 
     * @return la liste des propriétés par défaut
     */
    protected final Properties _getJasperProperties() {
        return jasperProperties;
    }

    /**
     * Charge les propriétés par défaut (dans le fichier 'globazJasper.properties')
     */
    protected final void _loadJasperProperties() {
        InputStream inputStream = null;
        try {
            inputStream = getClass().getResourceAsStream('/' + JASPER_PROPERTIES_FILENAME);
            if (inputStream == null) {
                try {
                    inputStream = new FileInputStream(JASPER_PROPERTIES_FILENAME);
                } catch (Exception e) {
                    throw new IOException("File '" + JASPER_PROPERTIES_FILENAME + "' not found");
                }
            }
        } catch (IOException ioe) {
            inputStream = null;
        }
        if (inputStream != null) {
            try {
                _getJasperProperties().load(inputStream);
            } catch (Exception e) {
                JadeLogger.warn(this, "Can't load properties file for '" + templateName + "'");
            }
        }
    }

    @Override
    public DocumentData addFooterParameters(DocumentData docData, CaisseHeaderReportBean bean) throws Exception {
        return docData;
    }

    @Override
    public DocumentData addHeaderParameters(DocumentData docData, CaisseHeaderReportBean bean, Boolean isCopie)
            throws Exception {
        this.bean = bean;

        loadTemplateProperties(getTemplateName());
        _loadJasperProperties();

        docData.addData(ICaisseReportHelperOO.PARAM_HEADER_ADRESSE, bean.getAdresse());
        docData.addData(ICaisseReportHelperOO.PARAM_HEADER_DONNEES, getDonnees(bean));
        docData.addData(ICaisseReportHelperOO.PARAM_HEADER_LIBELLES, getLibelles(bean));

        if (bean.isConfidentiel()) {
            docData.addData(ICaisseReportHelperOO.PARAM_HEADER_CONFIDENTIEL, getProperty(HEADER_CONFIDENTIEL
                    + codeIsoLangue));
        }

        if (JadeStringUtil.isBlankOrZero(bean.getDate())) {
            docData.addData(ICaisseReportHelperOO.PARAM_HEADER_DATE, "");
        } else {
            docData.addData(ICaisseReportHelperOO.PARAM_HEADER_DATE, getProperty(HEADER_PREFIXE_DATE + codeIsoLangue)
                    + bean.getDate());
        }

        if (!JadeStringUtil.isEmpty(bean.getEmailCollaborateur())) {
            docData.addData(ICaisseReportHelperOO.PARAM_HEADER_EMAIL_COLLABORATEUR, bean.getEmailCollaborateur());
        }

        if (!JadeStringUtil.isEmpty(bean.getNomCollaborateur())) {
            docData.addData(ICaisseReportHelperOO.PARAM_HEADER_NOM_COLLABORATEUR, bean.getNomCollaborateur());
        }

        if (!JadeStringUtil.isEmpty(bean.getTelCollaborateur())) {
            docData.addData(ICaisseReportHelperOO.PARAM_HEADER_TEL_COLLABORATEUR, bean.getTelCollaborateur());
        }

        if (bean.isRecommandee()) {
            docData.addData(ICaisseReportHelperOO.PARAM_HEADER_RECOMMANDEE, getProperty(HEADER_RECOMMANDEE
                    + codeIsoLangue));
        }
        try {
            docData.addData(ICaisseReportHelperOO.PARAM_HEADER_TYPECOURRIER, getProperty(HEADER_TYPECOURRIER
                    + codeIsoLangue));
        } catch (Exception e) {
            docData.addData(ICaisseReportHelperOO.PARAM_HEADER_TYPECOURRIER, "");
        }
        if (bean.getServiceCollaborateur().equals(bean.getNomCollaborateur())) {
            docData.addData(ICaisseReportHelperOO.PARAM_HEADER_SERVICE_COLLABORATEUR, "");
        } else {
            docData.addData(ICaisseReportHelperOO.PARAM_HEADER_SERVICE_COLLABORATEUR, bean.getServiceCollaborateur());
        }

        if (isCopie.booleanValue()) {
            if (codeIsoLangue.equals("fr") || codeIsoLangue.equals("FR")) {
                docData.addData("IS_COPIE", "COPIE");
            } else if (codeIsoLangue.equals("de") || codeIsoLangue.equals("DE")) {
                docData.addData("IS_COPIE", "KOPIE");
            } else {
                docData.addData("IS_COPIE", "COPIA");
            }
        } else {
            docData.addData("IS_COPIE", "");
        }

        return docData;
    }

    @Override
    public DocumentData addSignatureParameters(DocumentData docData, CaisseHeaderReportBean bean) throws Exception {
        this.bean = bean;

        loadTemplateProperties(getTemplateName());
        _loadJasperProperties();

        docData.addData(ICaisseReportHelperOO.PARAM_SIGNATURE_CAISSE, getProperty(SIGN_NOM_CAISSE + codeIsoLangue));
        docData.addData(ICaisseReportHelperOO.PARAM_SIGNATURE_SERVICE, getProperty(SIGN_NOM_SERVICE + codeIsoLangue));
        docData.addData(ICaisseReportHelperOO.PARAM_SIGNATURE_SIGNATAIRE, getProperty(SIGN_SIGNATAIRE + codeIsoLangue));

        return docData;
    }

    private String getDonnees(CaisseHeaderReportBean bean) {
        // On place le numéro AVS ; d'Affilié et de section dans le même champ
        // pour eviter un trou s'il n'y a pas de numéro AVS par exemple.
        StringBuffer donnees = new StringBuffer();
        if (!JadeStringUtil.isEmpty(bean.getNoAffilie() == null ? bean.getNoAffilie() : bean.getNoAffilie().trim())) {
            if (!JadeStringUtil.isEmpty(donnees.toString())) {
                donnees.append("\n");
            }
            donnees.append(bean.getNoAffilie());
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

    private String getLibelles(CaisseHeaderReportBean bean) {
        // On place le numéro AVS ; d'Affilié et de section dans le même champ
        // pour eviter un trou s'il n'y a pas de numéro AVS par exemple.
        StringBuffer libelles = new StringBuffer("");
        if (!JadeStringUtil.isEmpty(bean.getNoAffilie() == null ? bean.getNoAffilie() : bean.getNoAffilie().trim())) {
            if (!JadeStringUtil.isEmpty(libelles.toString())) {
                libelles.append("\n");
            }
            libelles.append(getProperty(HEADER_PREFIXE_NO_AFFILIE + codeIsoLangue));
        }
        if (!JadeStringUtil.isEmpty(bean.getNoAvs() == null ? bean.getNoAvs() : bean.getNoAvs().trim())) {
            if (!JadeStringUtil.isEmpty(libelles.toString())) {
                libelles.append("\n");
            }
            libelles.append(getProperty(HEADER_PREFIXE_NO_AVS + codeIsoLangue));
        }
        if (!JadeStringUtil.isEmpty(bean.getNoSection() == null ? bean.getNoSection() : bean.getNoSection().trim())) {
            if (!JadeStringUtil.isEmpty(libelles.toString())) {
                libelles.append("\n");
            }
            libelles.append(getProperty(HEADER_PREFIXE_NO_SECTION + codeIsoLangue));
        }

        return libelles.toString();
    }

    public Map getProperties() {
        return properties;
    }

    protected String getProperty(String propertyName) {
        String property = (String) getProperties().get(propertyName);

        if (null == property) {
            property = _getJasperProperties().getProperty(propertyName);
            if (null == property) {
                return "";
            } else {
                return property;
            }
        } else {
            return property;
        }

    }

    public String getTemplateName() {
        return templateName;
    }

    /**
     * @see globaz.caisse.report.helper.ICaisseReportHelperOO#init(BIApplication)
     */
    @Override
    public void init(BApplication application, String codeIsoLangue) {
        this.application = application;
        this.codeIsoLangue = codeIsoLangue.toUpperCase();
    }

    protected final Properties loadTemplateProperties(String templateName) {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            String filename = templateName;
            int extensionSeparator = filename.indexOf('.');
            if (extensionSeparator >= 0) {
                filename = filename.substring(0, extensionSeparator);
            }
            filename += ".properties";
            inputStream = getClass().getResourceAsStream('/' + filename);
            if (inputStream == null) {
                try {
                    inputStream = new FileInputStream(filename);
                } catch (Exception e) {
                    throw new IOException("File '" + filename + "' not found");
                }
            }
        } catch (IOException ioe) {
            inputStream = null;
        }
        if (inputStream != null) {
            try {
                properties.load(inputStream);
            } catch (Exception e) {

            }
        }

        Set set = properties.keySet();

        for (Iterator iterator = set.iterator(); iterator.hasNext();) {

            String key = (String) iterator.next();
            String value = (String) properties.get(key);
            getProperties().put(key, value);

        }

        return properties;
    }

    @Override
    public void setTemplateName(String templateName) throws Exception {
        this.templateName = templateName;
    }

}
