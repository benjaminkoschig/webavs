package globaz.osiris.impl.print.itext.list;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.osiris.application.CAApplication;

/**
 * @author: Sylvain Crelier Ins�rez la description du type ici. Date de cr�ation : (03.06.2003 15:46:19)
 */
public class CAProcessImprSecondRappelCSC extends CAProcessImprRappelCSC {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String SECOND_RAPPEL = "SecondRappel";
    private static String TEMPLATE_DOC = "CASecondRappelCSC";

    /**
     * Commentaire relatif au constructeur CAProcessImprJournalContentieuxCSC.
     */
    public CAProcessImprSecondRappelCSC() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Commentaire relatif au constructeur CAProcessImprJournalContentieuxCSC.
     * 
     * @param session
     *            BProcess
     */
    public CAProcessImprSecondRappelCSC(BProcess parent) throws FWIException {
        super(parent, CAApplication.DEFAULT_OSIRIS_ROOT, CAProcessImprSecondRappelCSC.SECOND_RAPPEL);
    }

    /**
     * Commentaire relatif au constructeur CAProcessImprJournalContentieuxCSC.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CAProcessImprSecondRappelCSC(BSession session) throws FWIException {
        super(session, CAApplication.DEFAULT_OSIRIS_ROOT, CAProcessImprSecondRappelCSC.SECOND_RAPPEL);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.FWIDocumentManager#afterBuildReport()
     */
    @Override
    public void afterBuildReport() {
        if (dispositionsLegales != null) {
            addDocument(dispositionsLegales);
        }

        if (extraitCompteDoc != null) {
            addDocument(extraitCompteDoc);
        }
    }

    /**
     * Derni�re m�thode lanc� avant la cr�ation du document par JasperReport Dernier minute pour fournir le nom du
     * rapport � utiliser avec la m�thode setTemplateFile(String) et si n�cessaire le type de document � sortir avec la
     * m�thode setFileType(String [PDF|CSV|HTML|XSL]) par d�faut PDF Date de cr�ation : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        // Attribution du nom du document
        setTemplateFile(CAProcessImprSecondRappelCSC.TEMPLATE_DOC);
        getDocumentInfo().setTemplateName(CAProcessImprSecondRappelCSC.TEMPLATE_DOC);
    }

    /**
     * Premi�re m�thode appel� (sauf _validate()) avant le chargement des donn�es par le processus On initialise le
     * manager principal d�finit dans le constructeur ou si on fournit un JRDataSource on le fournit aussi ici avec la
     * m�thode setSource et setSubSource (setSubReport(true) si on a un sousRapport avec des valeurs non param�tres)
     */
    @Override
    public void beforeExecuteReport() {
    }

    /**
     * Methode appel� pour la cr�ation des valeurs pour le document 1) addRow (si n�cessaire) 2) App�le des m�thodes
     * pour la cr�ation des param�tres
     */
    @Override
    public void createDataSource() throws Exception {

        rappel = (CAImprSecondRappelCSC) rappelIt.next();
        // Labels

        extraitCompteDoc = rappel.getExtraitCompteDoc();

        super.setDocumentTitle(rappel.getIdExterneRole());

        String isoLangue = "FR";
        String titre = "UKNOWN";
        String name = "UKNOWN";
        if (rappel.getTiers() != null) {
            isoLangue = rappel.getTiers().getLangueISO();

            titre = rappel.getTiers().getTitre();
            name = rappel.getTiers().getDesignation1();
        }

        dispositionsLegales = rappel.getDispositionsLegales(getApplication(), isoLangue, rappel.getTiers()
                .getPrenomNom());

        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), isoLangue);
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        headerBean.setAdresse(_getAdresseCourrier());
        headerBean.setRecommandee(true);
        headerBean.setDate(rappel.getDateSurDocument());

        headerBean.setNoAffilie(getNumAffilieAndSatellite(rappel));

        setTemplateFile(CAProcessImprSecondRappelCSC.TEMPLATE_DOC);
        caisseReportHelper.addHeaderParameters(this, headerBean);
        caisseReportHelper.addFooterParameters(this);

        super.setParametres(CAImprPremierRappelCSCParam.PARAM_3,
                getApplication().getLabel("IMPR_2EME_RAPPEL_CSC_TITLE", isoLangue));
        super.setParametres(CAImprPremierRappelCSCParam.PARAM_4,
                getApplication().getLabel("IMPR_2EME_RAPPEL_CSC_S_TITLE", isoLangue));

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

        StringBuffer sb = new StringBuffer();
        sb.append(FWMessageFormat.format(getApplication().getLabel("IMPR_2EME_RAPPEL_CSC_TXT_1", isoLangue),
                rappel.getDateRappel()));
        sb.append("\n\n");
        sb.append(getApplication().getLabel("IMPR_2EME_RAPPEL_CSC_TXT_2", isoLangue));
        sb.append("\n\n");
        sb.append(getApplication().getLabel("IMPR_2EME_RAPPEL_CSC_TXT_3", isoLangue));
        sb.append("\n\n");
        sb.append(getApplication().getLabel("IMPR_2EME_RAPPEL_CSC_TXT_4", isoLangue));
        sb.append("\n\n");
        sb.append(getApplication().getLabel("IMPR_2EME_RAPPEL_CSC_TXT_5", isoLangue));
        sb.append("\n\n");
        sb.append(getApplication().getLabel("IMPR_2EME_RAPPEL_CSC_TXT_6", isoLangue));
        sb.append("\n\n");

        String localiteSatellite = getSatelliteLocalite(rappel, isoLangue);

        sb.append(FWMessageFormat.format(getApplication().getLabel("IMPR_2EME_RAPPEL_CSC_TXT_7", isoLangue),
                localiteSatellite));

        sb.append("\n\n");
        if ("FR".equalsIgnoreCase(isoLangue)) {
            sb.append(FWMessageFormat.format(getApplication().getLabel("IMPR_2EME_RAPPEL_CSC_TXT_8", isoLangue), titre));
        } else {
            sb.append(getApplication().getLabel("IMPR_2EME_RAPPEL_CSC_TXT_8", isoLangue));
        }

        super.setParametres(CAImprPremierRappelCSCParam.PARAM_TXT_1, sb.toString());
        super.setParametres(CAImprPremierRappelCSCParam.PARAM_TIMBRE,
                getApplication().getLabel("IMPR_2EME_RAPPEL_CSC_TIMBRE", isoLangue));

        super.setParametres(CAImprPremierRappelCSCParam.PARAM_5_TITLE,
                getApplication().getLabel("IMPR_2EME_RAPPEL_CSC_COPIE_TITLE", isoLangue));
        super.setParametres(CAImprPremierRappelCSCParam.PARAM_5_CONTENT, FWMessageFormat.format(getApplication()
                .getLabel("IMPR_2EME_RAPPEL_CSC_COPIE_CONTENT", isoLangue), localiteSatellite));

        super.setParametres(CAImprPremierRappelCSCParam.PARAM_7_TITLE,
                getApplication().getLabel("IMPR_2EME_RAPPEL_ANNEXES_TITLE", isoLangue));
        super.setParametres(CAImprPremierRappelCSCParam.PARAM_7_CONTENT,
                getApplication().getLabel("IMPR_2EME_RAPPEL_ANNEXES_CONTENT", isoLangue));

        super.setParametres(CAImprPremierRappelCSCParam.PARAM_8,
                getApplication().getLabel("IMPR_2EME_RAPPEL_SANS_SIGNATURE", isoLangue));

        _headerText();
    }
}
