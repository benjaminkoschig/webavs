package globaz.eform.itext;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.prestation.interfaces.util.nss.PRUtil;
import lombok.Getter;
import lombok.Setter;

public class GEDocumentDossier extends FWIDocumentManager {

    private static final long serialVersionUID = 1L;

    private final static String MODEL_NAME = "GE_DOCUMENT_DOSSIER";
    protected final static String LABEL_FORMULE_POLITESSE = "DOCUMENT_FORMULE_POLITESSE";
    protected final static String LABEL_SALUTATION = "DOCUMENT_SALUTATION";
    protected final static String LABEL_ASSURE = "DOCUMENT_ASSURE";
    protected final static String LABEL_NSS = "DOCUMENT_NSS";
    protected final static String LABEL_NOM = "DOCUMENT_NOM";
    protected final static String LABEL_PRENOM = "DOCUMENT_PRENOM";
    protected final static String LABEL_DATE_NAISSANCE = "DOCUMENT_DATE_NAISSANCE";

    protected final static String PARAM_LIEU_DATE = "P_LIEU_DATE";
    protected final static String PARAM_FORMULE_POLITESSE = "P_FORMULE_POLITESSE";
    protected final static String PARAM_ASSURE_LABEL = "P_ASSURE_LABEL";
    protected final static String PARAM_ASSURE = "P_ASSURE";
    protected final static String PARAM_NSS_LABEL = "P_NSS_LABEL";
    protected final static String PARAM_NSS = "P_NSS";
    protected final static String PARAM_NOM_LABEL = "P_NOM_LABEL";
    protected final static String PARAM_NOM = "P_NOM";
    protected final static String PARAM_PRENOM_LABEL = "P_PRENOM_LABEL";
    protected final static String PARAM_PRENOM = "P_PRENOM";
    protected final static String PARAM_DATE_NAISSANCE_LABEL = "P_DATE_NAISSANCE_LABEL";
    protected final static String PARAM_DATE_NAISSANCE = "P_DATE_NAISSANCE";
    protected final static String PARAM_TITRE = "P_TITRE";
    protected final static String PARAM_PARAGRAPHE = "P_PARAGRAPHE";
    protected final static String PARAM_SALUTATION = "P_SALUTATION";

    @Getter
    @Setter
    private GEDocumentPojo documentPojo;

    private boolean isFirst = true;

    @Override
    public void beforeBuildReport() throws FWIException {
        try {
            // remplissage de l'entête
            final CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
            headerBean.setNoAffilie(null);
            headerBean.setNoAvs("");
            headerBean.setAdresse("");
            headerBean.setDate(JACalendar.todayJJsMMsAAAA());
            headerBean.setRecommandee(false);
            headerBean.setConfidentiel(false);

            ICaisseReportHelper caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(getDocumentInfo(),
                    getSession().getApplication(), PRUtil.getISOLangueTiers("FR"));
            caisseHelper.addHeaderParameters(this, headerBean);

            // signature
            caisseHelper.addSignatureParameters(getImporter());
        } catch (Exception e) {
            throw new FWIException(e);
        }
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        // Nothing todo
    }

    @Override
    public void createDataSource() throws Exception {
        // set du template
        setTemplateFile(MODEL_NAME);
        // remplissage du docInfo et du titre du doc
        fillDocInfo();

        // envoi des paramètres dans le template
        this.setParametres(PARAM_LIEU_DATE, getLieuDate());
        this.setParametres(PARAM_FORMULE_POLITESSE, getSession().getLabel(LABEL_FORMULE_POLITESSE));
        this.setParametres(PARAM_ASSURE_LABEL, getSession().getLabel(LABEL_ASSURE));
//        this.setParametres(PARAM_ASSURE, documentPojo.);
        this.setParametres(PARAM_NSS_LABEL, getSession().getLabel(LABEL_NSS));
        this.setParametres(PARAM_NSS, documentPojo.getNss());
        this.setParametres(PARAM_NOM_LABEL, getSession().getLabel(LABEL_NOM));
        this.setParametres(PARAM_NOM, documentPojo.getNom());
        this.setParametres(PARAM_PRENOM_LABEL, getSession().getLabel(LABEL_PRENOM));
        this.setParametres(PARAM_PRENOM, documentPojo.getPrenom());
        this.setParametres(PARAM_DATE_NAISSANCE_LABEL, getSession().getLabel(LABEL_DATE_NAISSANCE));
        this.setParametres(PARAM_DATE_NAISSANCE, documentPojo.getDateNaissance());

    }

    private void fillDocInfo() throws JAException {
        getDocumentInfo().setArchiveDocument(false);
        getDocumentInfo().setPublishDocument(false);
        getDocumentInfo().setDocumentProperty("annee",
                Integer.toString(JACalendar.getYear(JACalendar.todayJJsMMsAAAA())));

    }

    private String getLieuDate() {
        return JACalendar.todayJJsMMsAAAA();
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    public boolean next() throws FWIException {

        if (isFirst) {
            isFirst = false;
            return true;
        }

        return false;
    }


}
