package globaz.osiris.client.cfc.contentieux;

import ch.globaz.common.document.reference.ReferenceQR;
import ch.globaz.common.properties.CommonProperties;
import globaz.aquila.print.CODecisionFPV;
import globaz.aquila.print.COParameter;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.framework.printing.itext.FWIScriptDocument;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.bean.JadeUserDetail;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Objects;
import java.util.Vector;

/**
 * Cette Classe permet d'imprimer le document pour le premier rappel de la Caisse F�d�rale
 * 
 * @author s�bastien Chappatte
 */
public class CFPremierRappel_Doc extends FWIScriptDocument {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String USERDETAIL_PHONE = "Phone";
    private CFPremierRappel rappel = null;
    private Iterator rappelIt = null;
    private String responsable = null;

    /**
     * Commentaire relatif au constructeur CAProcessImprJournalContentieuxCSC.
     */
    public CFPremierRappel_Doc() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Commentaire relatif au constructeur CAProcessImprJournalContentieuxCSC.
     * 
     * @param parent
     *            globaz.globall.db.BProcess
     */
    public CFPremierRappel_Doc(BProcess parent) throws FWIException {
        super(parent, CAApplication.DEFAULT_OSIRIS_ROOT, "JournalContentieux");
        super.setDocumentTitle("Journal du contentieux");
    }

    /**
     * Commentaire relatif au constructeur CAProcessImprJournalContentieuxCSC.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CFPremierRappel_Doc(BSession session) throws FWIException {
        super(session, CAApplication.DEFAULT_OSIRIS_ROOT, "JournalContentieux");
        super.setDocumentTitle("Journal du contentieux");
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.07.2003 07:32:07)
     * 
     * @return java.lang.String
     */
    private String _getAdresseCourrier() {
        return rappel.getLigneAdresse1() + "\n" + rappel.getLigneAdresse2() + "\n" + rappel.getLigneAdresse3() + "\n"
                + rappel.getLigneAdresse4() + "\n" + rappel.getLigneAdresse5() + "\n" + rappel.getLigneAdresse6()
                + "\n" + rappel.getLigneAdresse7();
    }

    /**
     * Retourne l'alias du responable Date de cr�ation : (02.05.2003 11:59:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String _getResponsable() {
        // Si responsable est vide, on charge le responsable
        if (JadeStringUtil.isBlank(responsable)) {
            responsable = "";
            try {
                JadeUser user = JadeAdminServiceLocatorProvider.getLocator().getUserService()
                        .load(getSession().getUserId());
                if (user != null) {
                    responsable = user.getFirstname() + " " + user.getLastname() + "\n";
                    JadeUserDetail detail = null;
                    try {
                        detail = JadeAdminServiceLocatorProvider.getLocator().getUserDetailService()
                                .load(user.getIdUser(), FWSecureConstants.USER_DETAIL_PHONE);
                    } catch (Exception e) {
                        detail = null;
                    }
                    if (detail != null) {
                        responsable += detail.getValue();
                    }
                }
            } catch (Exception e) {
                // si on ne trouve pas de responsable, ce n'est pas grave...
            }
        }
        return responsable;
    }

    /**
     * Methode pour ins�rer les constantes qui s'affiche dans la premi�re page Utiliser super.setParametres(Key, Value)
     */
    protected void _headerText() {
        try {
            super.setParametres(FWIImportParametre.PARAM_COMPANYNAME,
                    getSession().getApplication().getLabel("ADR_CAISSE", rappel.getTiers().getLangueISO()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Derni�re m�thode lanc� avant la cr�ation du document par JasperReport Dernier minute pour fournir le nom du
     * rapport � utiliser avec la m�thode setTemplateFile(String) et si n�cessaire le type de document � sortir avec la
     * m�thode setFileType(String [PDF|CSV|HTML|XSL]) par d�faut PDF Date de cr�ation : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() throws FWIException {
    }

    /**
     * Premi�re m�thode appel� (sauf _validate()) avant le chargement des donn�es par le processus On initialise le
     * manager principal d�finit dans le constructeur ou si on fournit un JRDataSource on le fournit aussi ici avec la
     * m�thode setSource et setSubSource (setSubReport(true) si on a un sousRapport avec des valeurs non param�tres)
     */
    @Override
    public void beforeExecuteReport() {
        setTemplateFile("CARappel1_BVR_QR");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforePrintDocument ()
     */
    @Override
    public boolean beforePrintDocument() {
        return ((super.size() > 0) && !super.isAborted());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIDocument#bindData(String)
     */
    @Override
    public void bindData(String arg0) throws Exception {
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (15.05.2003 07:17:28)
     * 
     * @param data
     *            java.lang.Object
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    public void bindObject(Object data) throws java.lang.Exception {
        rappelIt = ((Vector) data).iterator();
    }

    /**
     * Methode appel� pour la cr�ation des valeurs pour le document 1) addRow (si n�cessaire) 2) App�le des m�thodes
     * pour la cr�ation des param�tres
     */
    @Override
    public void createDataSource() throws Exception {

        try {
            rappel = (CFPremierRappel) rappelIt.next();

            // Header
            ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(null,
                    getSession().getApplication(), rappel.getTiers().getLangueISO());
            CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
            headerBean.setAdresse(_getAdresseCourrier());
            headerBean.setDate(rappel.getDate());
            headerBean.setNomCollaborateur(_getResponsable());

            caisseReportHelper.addHeaderParameters(this, headerBean);

            // Labels
            super.setParametres(CFRappelParam.LABEL_NOCONTROLE,
                    getSession().getApplication().getLabel("CONTENTIEUX_NOCONTROLE", rappel.getTiers().getLangueISO()));
            super.setParametres(CFRappelParam.LABEL_NOFACTURE,
                    getSession().getApplication().getLabel("CONTENTIEUX_NOFACTURE", rappel.getTiers().getLangueISO()));
            super.setParametres(
                    CFRappelParam.LABEL_PAYABLE_JUSQUAU,
                    getSession().getApplication().getLabel("CONTENTIEUX_PAYABLE_JUSQUAU",
                            rappel.getTiers().getLangueISO()));
            super.setParametres(
                    CFRappelParam.LABEL_PRISE_EN_CONSIDERATION,
                    getSession().getApplication().getLabel("CONTENTIEUX_PRISE_EN_CONSIDERATION",
                            rappel.getTiers().getLangueISO()));
            super.setParametres(CFRappelParam.LABEL_TEXT_1,
                    getSession().getApplication().getLabel("CONTENTIEUX_TEXT_1", rappel.getTiers().getLangueISO())
                            + " " + rappel.getDateSection());
            super.setParametres(CFRappelParam.LABEL_TEXT_2,
                    getSession().getApplication().getLabel("CONTENTIEUX_TEXT_2", rappel.getTiers().getLangueISO()));
            super.setParametres(CFRappelParam.LABEL_TEXT_3,
                    getSession().getApplication().getLabel("CONTENTIEUX_TEXT_3", rappel.getTiers().getLangueISO()));

            // Param�tres
            super.setParametres(CFRappelParam.PARAM_LIEU_DATE,
                    getSession().getApplication().getLabel("RAPPELDATE", rappel.getTiers().getLangueISO()) + " "
                            + rappel.getDate());
            super.setParametres(CFRappelParam.PARAM_NOCONTROLE, rappel.getNoCompte());
            super.setParametres(CFRappelParam.PARAM_NOFACTURE, rappel.getNoFacture());
            super.setParametres(CFRappelParam.PARAM_ADRESSECOURRIER, _getAdresseCourrier());
            super.setParametres("P_6",
                    getSession().getApplication().getLabel("CONTENTIEUX_SOMMATION", rappel.getTiers().getLangueISO()));
            super.setParametres(CFRappelParam.PARAM_PAYABLE_JUSQUAU, rappel.getDateDelaiPaiement());
            super.setParametres(CFRappelParam.PARAM_PRISE_EN_CONSIDERATION, rappel.getDate());
            super.setParametres(CFRappelParam.PARAM_REMARQUE,
                    getSession().getApplication().getLabel("REMARQUE_FIXE", rappel.getTiers().getLangueISO()));
            super.setParametres(CFRappelParam.PARAM_REMARQUE2_GRAS,
                    getSession().getApplication().getLabel("REMARQUE_FIXE2_GRAS", rappel.getTiers().getLangueISO()));
            super.setParametres(CFRappelParam.PARAM_TAXE, new BigDecimal(JANumberFormatter.deQuote(rappel.getTaxe())));
            super.setParametres(CFRappelParam.PARAM_MONTANT,
                    new BigDecimal(JANumberFormatter.deQuote(rappel.getMontant())));

            if (CommonProperties.QR_FACTURE.getBooleanValue()) {
                // -- QR
                rappel.setQrFacture(new ReferenceQR());
                rappel.getQrFacture().setSession(getSession());
                // Initialisation des variables du document
                rappel.initVariableQR(rappel.getTiers().getLangueISO(), new FWCurrency(rappel.getMontant()), rappel.getTiers().getIdTiers());
                // G�n�ration du document QR
                rappel.getQrFacture().initQR(this, rappel.getQrFactures());
            } else {
                initBVR();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        _headerText();
    }

    /**
     * BVR
     */
    private void initBVR() {

        try {
            // Modification suite � QR-Facture. Choix du footer
            super.setParametres(COParameter.P_SUBREPORT_QR, getImporter().getImportPath() + "BVR_TEMPLATE.jasper");

            super.setParametres(CFRappelParam.PARAM_MONTANT_FACTURE,
                    new BigDecimal(JANumberFormatter.deQuote(rappel.getMontantFacture())));
            super.setParametres(CFRappelParam.PARAM_ADRESSE,
                    getSession().getApplication().getLabel("ADR_CAISSE_BVR", rappel.getTiers().getLangueISO()));
            super.setParametres(CFRappelParam.PARAM_ADRESSECOPY,
                    getSession().getApplication().getLabel("ADR_CAISSE_BVR", rappel.getTiers().getLangueISO()));
            super.setParametres(CFRappelParam.PARAM_REFERENCE, rappel.getReference());
            super.setParametres(CFRappelParam.PARAM_COMPTE,
                    getSession().getApplication().getLabel("NUMERO_COMPTE", rappel.getTiers().getLangueISO()));


            super.setParametres(CFRappelParam.PARAM_FRANC, JANumberFormatter.deQuote(rappel.getMontantSansCentime()));
            super.setParametres(CFRappelParam.PARAM_CENTIME, rappel.getCentimes());
            super.setParametres(CFRappelParam.PARAM_VERSE, _getAdresseCourrier());
            super.setParametres(CFRappelParam.PARAM_PAR, _getAdresseCourrier());
            super.setParametres(CFRappelParam.PARAM_OCR, rappel.getOcrb());

        } catch (Exception e1) {
            getMemoryLog().logMessage(
                    "Erreur lors de recherche du texte sur le bvr de la D�cision : " + e1.getMessage(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }
    }

    /**
     * Method jobQueue. Cette m�thode d�finit la nature du traitement s'il s'agit d'un processus qui doit-�tre lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        return rappelIt.hasNext();
    }

    /**
     * M�thode appel� pour lancer l'exportation du document Par d�faut ne pas utiliser car d�j� impl�ment� par la
     * superClass Utile si on ne veut pas exporter en fichier temporaire Date de cr�ation : (17.02.2003 14:44:15)
     */
    @Override
    public void returnDocument() throws FWIException {
        super.imprimerListDocument();
    }

}
