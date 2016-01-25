package globaz.osiris.impl.print.itext.list;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.osiris.application.CAApplication;
import globaz.osiris.external.IntTiers;

/**
 * @author: Sylvain Crelier Insérez la description du type ici. Date de création : (03.06.2003 15:46:19)
 */
public class CAProcessImprPremierRappelCSC extends CAProcessImprRappelCSC {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String PREMIER_RAPPEL = "PremierRappel";
    private static String TEMPLATE_DOC = "CAPremierRappelCSC";

    /**
     * Commentaire relatif au constructeur CAProcessImprJournalContentieuxCSC.
     */
    public CAProcessImprPremierRappelCSC() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Commentaire relatif au constructeur CAProcessImprJournalContentieuxCSC.
     * 
     * @param session
     *            BProcess
     */
    public CAProcessImprPremierRappelCSC(BProcess parent) throws FWIException {
        super(parent, CAApplication.DEFAULT_OSIRIS_ROOT, CAProcessImprPremierRappelCSC.PREMIER_RAPPEL);
    }

    /**
     * Commentaire relatif au constructeur CAProcessImprJournalContentieuxCSC.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CAProcessImprPremierRappelCSC(BSession session) throws FWIException {
        super(session, CAApplication.DEFAULT_OSIRIS_ROOT, CAProcessImprPremierRappelCSC.PREMIER_RAPPEL);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.FWIDocumentManager#afterBuildReport()
     */
    @Override
    public void afterBuildReport() {
        if (extraitCompteDoc != null) {
            addDocument(extraitCompteDoc);
        }
    }

    /**
     * Dernière méthode lancé avant la création du document par JasperReport Dernier minute pour fournir le nom du
     * rapport à utiliser avec la méthode setTemplateFile(String) et si nécessaire le type de document à sortir avec la
     * méthode setFileType(String [PDF|CSV|HTML|XSL]) par défaut PDF Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        // Attribution du nom du document
        setTemplateFile(CAProcessImprPremierRappelCSC.TEMPLATE_DOC);
        getDocumentInfo().setTemplateName(CAProcessImprPremierRappelCSC.TEMPLATE_DOC);
    }

    /**
     * Première méthode appelé (sauf _validate()) avant le chargement des données par le processus On initialise le
     * manager principal définit dans le constructeur ou si on fournit un JRDataSource on le fournit aussi ici avec la
     * méthode setSource et setSubSource (setSubReport(true) si on a un sousRapport avec des valeurs non paramètres)
     */
    @Override
    public void beforeExecuteReport() {
    }

    /**
     * Methode appelé pour la création des valeurs pour le document 1) addRow (si nécessaire) 2) Appèle des méthodes
     * pour la création des paramètres
     */
    @Override
    public void createDataSource() throws Exception {

        rappel = (CAImprPremierRappelCSC) rappelIt.next();
        // Labels

        extraitCompteDoc = rappel.getExtraitCompteDoc();

        super.setDocumentTitle(rappel.getIdExterneRole());
        IntTiers tier = rappel.getTiers();
        String isoLangue = "FR";
        String titre = "UKNOWN";
        String name = "UKNOWN";

        if (tier != null) {
            isoLangue = tier.getLangueISO();

            titre = tier.getTitre();
            name = tier.getDesignation1();
        }

        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), isoLangue);
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        headerBean.setAdresse(_getAdresseCourrier());
        headerBean.setDate(rappel.getDateSurDocument());
        headerBean.setNoAffilie(getNumAffilieAndSatellite(rappel));

        setTemplateFile(CAProcessImprPremierRappelCSC.TEMPLATE_DOC);
        caisseReportHelper.addHeaderParameters(this, headerBean);
        caisseReportHelper.addFooterParameters(this);

        // Assignation des paramètres
        if ("FR".equalsIgnoreCase(isoLangue)) {
            super.setParametres(CAImprPremierRappelCSCParam.PARAM_GENDER,
                    FWMessageFormat.format(getApplication().getLabel("IMPR_RAPPEL_CSC_GENDER", isoLangue), titre));
        } else if ("DE".equalsIgnoreCase(isoLangue)) {
            if (titre.toLowerCase().indexOf("frau") > -1) {
                super.setParametres(CAImprPremierRappelCSCParam.PARAM_GENDER, FWMessageFormat.format(getApplication()
                        .getLabel("IMPR_RAPPEL_CSC_GENDER_FEMALE", isoLangue), titre, name));
            } else if (titre.toLowerCase().indexOf("herr") > -1) {
                super.setParametres(CAImprPremierRappelCSCParam.PARAM_GENDER, FWMessageFormat.format(getApplication()
                        .getLabel("IMPR_RAPPEL_CSC_GENDER_MALE", isoLangue), titre, name));
            } else {
                super.setParametres(CAImprPremierRappelCSCParam.PARAM_GENDER, FWMessageFormat.format(getApplication()
                        .getLabel("IMPR_RAPPEL_CSC_GENDER", isoLangue), titre, name));
            }
        } else if ("IT".equalsIgnoreCase(isoLangue)) {
            if (titre.toLowerCase().indexOf("signora") > -1) {
                super.setParametres(CAImprPremierRappelCSCParam.PARAM_GENDER, FWMessageFormat.format(getApplication()
                        .getLabel("IMPR_RAPPEL_CSC_GENDER_FEMALE", isoLangue), titre));
            } else if (titre.toLowerCase().indexOf("signor") > -1) {
                super.setParametres(CAImprPremierRappelCSCParam.PARAM_GENDER, FWMessageFormat.format(getApplication()
                        .getLabel("IMPR_RAPPEL_CSC_GENDER_MALE", isoLangue), titre));
            } else {
                super.setParametres(CAImprPremierRappelCSCParam.PARAM_GENDER,
                        getApplication().getLabel("IMPR_RAPPEL_CSC_GENDER", isoLangue));
            }
        } else {
            super.setParametres(CAImprPremierRappelCSCParam.PARAM_GENDER,
                    getApplication().getLabel("IMPR_RAPPEL_CSC_GENDER", isoLangue));
        }

        super.setParametres(CAImprPremierRappelCSCParam.PARAM_4,
                getApplication().getLabel("IMPR_1ER_RAPPEL_CSC_S_TITLE", isoLangue));
        super.setParametres(
                CAImprPremierRappelCSCParam.PARAM_TXT_1,
                FWMessageFormat.format(getApplication().getLabel("IMPR_1ER_RAPPEL_CSC_TXT_1", isoLangue),
                        rappel.getDateRappel()));
        super.setParametres(CAImprPremierRappelCSCParam.PARAM_MONTANT, FWMessageFormat.format(getApplication()
                .getLabel("IMPR_1ER_RAPPEL_CSC_MONTANT", isoLangue), JANumberFormatter.fmt(rappel.getMontant(), true,
                true, false, 2)));
        super.setParametres(CAImprPremierRappelCSCParam.PARAM_TXT_2,
                getApplication().getLabel("IMPR_1ER_RAPPEL_CSC_TXT_2", isoLangue));

        StringBuffer sb = new StringBuffer();
        sb.append(getApplication().getLabel("IMPR_1ER_RAPPEL_CSC_TXT_3", isoLangue));
        sb.append("\n\n");
        sb.append(getApplication().getLabel("IMPR_1ER_RAPPEL_CSC_TXT_4", isoLangue));
        sb.append("\n\n");
        sb.append(getApplication().getLabel("IMPR_1ER_RAPPEL_CSC_TXT_5", isoLangue));
        sb.append("\n\n");

        String localiteSatellite = getSatelliteLocalite(rappel, isoLangue);
        sb.append(FWMessageFormat.format(getApplication().getLabel("IMPR_1ER_RAPPEL_CSC_TXT_6", isoLangue),
                localiteSatellite));

        sb.append("\n\n");
        if ("FR".equalsIgnoreCase(isoLangue)) {
            sb.append(FWMessageFormat.format(getApplication().getLabel("IMPR_1ER_RAPPEL_CSC_TXT_7", isoLangue), titre));
        } else {
            sb.append(getApplication().getLabel("IMPR_1ER_RAPPEL_CSC_TXT_7", isoLangue));
        }

        super.setParametres(CAImprPremierRappelCSCParam.PARAM_TXT_3, sb.toString());

        super.setParametres(CAImprPremierRappelCSCParam.PARAM_TIMBRE,
                getApplication().getLabel("IMPR_1ER_RAPPEL_CSC_TIMBRE", isoLangue));

        super.setParametres(CAImprPremierRappelCSCParam.PARAM_5_TITLE,
                getApplication().getLabel("IMPR_1ER_RAPPEL_CSC_COPIE_TITLE", isoLangue));
        super.setParametres(CAImprPremierRappelCSCParam.PARAM_5_CONTENT, FWMessageFormat.format(getApplication()
                .getLabel("IMPR_1ER_RAPPEL_CSC_COPIE_CONTENT", isoLangue), localiteSatellite));

        super.setParametres(CAImprPremierRappelCSCParam.PARAM_7_TITLE,
                getApplication().getLabel("IMPR_1ER_RAPPEL_CSC_ANNEXES_TITLE", isoLangue));
        super.setParametres(CAImprPremierRappelCSCParam.PARAM_7_CONTENT,
                getApplication().getLabel("IMPR_1ER_RAPPEL_CSC_ANNEXES_CONTENT", isoLangue));

        super.setParametres(CAImprPremierRappelCSCParam.PARAM_8,
                getApplication().getLabel("IMPR_1ER_RAPPEL_SANS_SIGNATURE", isoLangue));

        _headerText();
    }

}
