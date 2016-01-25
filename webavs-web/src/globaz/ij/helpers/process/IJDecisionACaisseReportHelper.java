package globaz.ij.helpers.process;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.framework.printing.itext.api.FWIImporterInterface;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.util.AFIDEUtil;

public class IJDecisionACaisseReportHelper extends ACaisseReportHelper {

    private String codeIsoLangue = "";

    public IJDecisionACaisseReportHelper(JadePublishDocumentInfo docInfo) {
        super(docInfo);

    }

    /**
     * @param doc
     * @param bean
     * @param adresseCaisse
     * @throws Exception
     */
    public void addHeaderParameters(FWIImporterInterface doc, CaisseHeaderReportBean bean, String adresseCaisse,
            String codeILangue, String paramHeaderDate) throws Exception {

        codeIsoLangue = codeILangue;

        doc.setParametre(ICaisseReportHelper.PARAM_DEFAULT_MODEL_PATH, getDefaultModelPath());
        doc.setParametre(ICaisseReportHelper.PARAM_SUBREPORT_HEADER, getDefaultModelPath() + "/"
                + getDefaultHeaderPath(doc));

        doc.setParametre(ICaisseReportHelper.PARAM_HEADER_ADRESSE_CAISSE, adresseCaisse);

        // Utiliser pour le document de la caisse suisse
        if (!JadeStringUtil.isEmpty(doc.getTemplateProperty(docInfo, JASP_PROP_HEADER_COMTPE_CAISSE + codeIsoLangue))) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_COMTPE_CAISSE,
                    doc.getTemplateProperty(docInfo, JASP_PROP_HEADER_COMTPE_CAISSE + codeIsoLangue));
        }

        // Utiliser pour le document de la caisse suisse
        if (!JadeStringUtil.isEmpty(doc.getTemplateProperty(docInfo, JASP_PROP_HEADER_COMTPE_CAISSE_LIBELLE
                + codeIsoLangue))) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_COMPTE_CAISSE_LIBELLE,
                    doc.getTemplateProperty(docInfo, JASP_PROP_HEADER_COMTPE_CAISSE_LIBELLE + codeIsoLangue));
        }

        // Utiliser pour le document de la caisse suisse
        if (!JadeStringUtil
                .isEmpty(doc.getTemplateProperty(docInfo, JASP_PROP_HEADER_NUMERO_REFERENCE + codeIsoLangue))) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_NUMERO_REFERENCE,
                    doc.getTemplateProperty(docInfo, JASP_PROP_HEADER_NUMERO_REFERENCE + codeIsoLangue));
        }

        doc.setParametre(ICaisseReportHelper.PARAM_HEADER_ADRESSE, bean.getAdresse());

        if (!JadeStringUtil.isEmpty(bean.getEmailCollaborateur() == null ? bean.getEmailCollaborateur() : bean
                .getEmailCollaborateur().trim())) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_EMAIL_COLLABORATEUR,
                    doc.getTemplateProperty(docInfo, JASP_PROP_HEADER_PREFIXE_EMAIL_COLLABORATEUR + codeIsoLangue)
                            + bean.getEmailCollaborateur());
        } else if (!JadeStringUtil.isNull(bean.getEmailCollaborateur())) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_EMAIL_COLLABORATEUR, "");
        }

        if (!JadeStringUtil.isEmpty(bean.getNomCollaborateur() == null ? bean.getNomCollaborateur() : bean
                .getNomCollaborateur().trim())) {
            doc.setParametre(
                    ICaisseReportHelper.PARAM_HEADER_NOM_COLLABORATEUR,
                    doc.getTemplateProperty(docInfo, JASP_PROP_HEADER_PREFIXE_NOM_COLLABORATEUR + codeIsoLangue)
                            + bean.getNomCollaborateur());
        } else if (!JadeStringUtil.isNull(bean.getNomCollaborateur())) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_NOM_COLLABORATEUR, "");
        }

        if (!JadeStringUtil.isEmpty(bean.getTelCollaborateur() == null ? bean.getTelCollaborateur() : bean
                .getTelCollaborateur().trim())) {
            doc.setParametre(
                    ICaisseReportHelper.PARAM_HEADER_TEL_COLLABORATEUR,
                    doc.getTemplateProperty(docInfo, JASP_PROP_HEADER_PREFIXE_TEL_COLLABORATEUR + codeIsoLangue)
                            + bean.getTelCollaborateur());
        } else if (!JadeStringUtil.isNull(bean.getTelCollaborateur())) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_TEL_COLLABORATEUR, "");
        }

        // On place le numéro AVS ; d'Affilié et de section dans le même champ
        // pour eviter un trou s'il n'y a pas de numéro AVS par exemple.
        doc.setParametre(ICaisseReportHelper.PARAM_HEADER_DONNEES, getDonnees(bean));
        doc.setParametre(ICaisseReportHelper.PARAM_HEADER_LIBELLES, getLibelles(doc, bean));

        /*
         * if (!JadeStringUtil.isEmpty(bean.getDate() == null ? bean.getDate() : bean.getDate().trim())) {
         * doc.setParametre(ICaisseReportHelper.PARAM_HEADER_DATE, PRStringUtils.replaceString(paramHeaderDate,
         * CDT_DATE, bean.getDate())); } else if (!JadeStringUtil.isNull(bean.getDate())) {
         * doc.setParametre(ICaisseReportHelper.PARAM_HEADER_DATE, ""); }
         */
        doc.setParametre(ICaisseReportHelper.PARAM_HEADER_DATE, "");

        if (bean.isRecommandee()) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_RECOMMANDEE,
                    doc.getTemplateProperty(docInfo, JASP_PROP_HEADER_RECOMMANDEE + codeIsoLangue));
        }

        if (bean.isConfidentiel()) {
            doc.setParametre(ICaisseReportHelper.PARAM_HEADER_CONFIDENTIEL,
                    doc.getTemplateProperty(docInfo, JASP_PROP_HEADER_CONFIDENTIEL + codeIsoLangue));
        }

    }

    public void addSignatureParameters(FWIImporterInterface doc, String param, String signature) throws Exception {

        doc.setParametre(ICaisseReportHelper.PARAM_DEFAULT_MODEL_PATH, getDefaultModelPath());

        if (doc.getTemplateProperty(docInfo, JASP_PROP_SIGN_IMAGE) != null) {
            doc.setParametre(ICaisseReportHelper.PARAM_SIGNATURE_IMG,
                    doc.getTemplateProperty(docInfo, JASP_PROP_SIGN_IMAGE));
        }

        doc.setParametre(ICaisseReportHelper.PARAM_SUBREPORT_SIGNATURE,
                getDefaultModelPath() + "/" + doc.getTemplateProperty(docInfo, FWIImportProperties.SIGNATURE_FILENAME));

        if (null != signature) {
            doc.setParametre(ICaisseReportHelper.PARAM_SIGNATURE_CAISSE, signature);
        } else {

            if (doc.getTemplateProperty(docInfo, JASP_PROP_SIGN_NOM_CAISSE + codeIsoLangue) != null) {
                doc.setParametre(ICaisseReportHelper.PARAM_SIGNATURE_CAISSE,
                        doc.getTemplateProperty(docInfo, JASP_PROP_SIGN_NOM_CAISSE + codeIsoLangue));
            }

            if (doc.getTemplateProperty(docInfo, JASP_PROP_SIGN_NOM_SERVICE + codeIsoLangue) != null) {
                doc.setParametre(ICaisseReportHelper.PARAM_SIGNATURE_SERVICE,
                        doc.getTemplateProperty(docInfo, JASP_PROP_SIGN_NOM_SERVICE + codeIsoLangue));
            }
        }

        if (doc.getTemplateProperty(docInfo, JASP_PROP_SIGN_SIGNATAIRE + codeIsoLangue) != null) {
            doc.setParametre(ICaisseReportHelper.PARAM_SIGNATURE_SIGNATAIRE,
                    doc.getTemplateProperty(docInfo, JASP_PROP_SIGN_SIGNATAIRE + codeIsoLangue));
        }

        String data = doc.getTemplateProperty(docInfo, JASP_PROP_SIGN_DATA + codeIsoLangue);
        if (param != null) {
            data = globaz.framework.util.FWMessageFormat.format(data, param);
        }

        if (data != null) {
            doc.setParametre(ICaisseReportHelper.PARAM_SIGNATURE_DATA, data);
        }
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
            libelles.append(doc.getTemplateProperty(docInfo, JASP_PROP_HEADER_PREFIXE_NO_AFFILIE + codeIsoLangue));

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
            libelles.append(doc.getTemplateProperty(docInfo, JASP_PROP_HEADER_PREFIXE_NO_AVS + codeIsoLangue));
        }
        if (!JadeStringUtil.isEmpty(bean.getNoSection() == null ? bean.getNoSection() : bean.getNoSection().trim())) {
            if (!JadeStringUtil.isEmpty(libelles.toString())) {
                libelles.append("\n");
            }
            libelles.append(doc.getTemplateProperty(docInfo, JASP_PROP_HEADER_PREFIXE_NO_SECTION + codeIsoLangue));
        }

        return libelles.toString();
    }
}
