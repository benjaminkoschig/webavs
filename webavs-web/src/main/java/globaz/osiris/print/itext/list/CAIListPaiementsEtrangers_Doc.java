package globaz.osiris.print.itext.list;

import globaz.framework.printing.itext.api.FWIImporterInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CAJournal;
import java.util.Map;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (03.06.2003 15:46:19)
 * 
 * @author: Administrator
 */
public class CAIListPaiementsEtrangers_Doc extends CAIListFactory {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String NUMERO_REFERENCE_INFOROM = "0015GCA";
    private java.lang.String idJournal = new String();
    private CAJournal journal;
    private BProcess process;
    private int showDetail = 0;

    /**
     * Commentaire relatif au constructeur CAIListPaiementsEtrangers_Doc.
     */
    public CAIListPaiementsEtrangers_Doc() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Constructor CAIListPaiementsEtrangers_Doc.
     * 
     * @param context
     */
    public CAIListPaiementsEtrangers_Doc(BProcess parent) throws FWIException {
        super(parent, CAApplication.DEFAULT_OSIRIS_ROOT, "CAIListPaiementsEtrangers");
        super.setDocumentTitle(getSession().getLabel("TITLE_JOURNAL_PAIEMENT_ETRANGER"));
    }

    /**
     * Commentaire relatif au constructeur CAIListPaiementsEtrangers_Doc.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CAIListPaiementsEtrangers_Doc(BSession session) throws FWIException {
        super(session, CAApplication.DEFAULT_OSIRIS_ROOT, "CAIListPaiementsEtrangers");
        super.setDocumentTitle(session.getLabel("TITLE_JOURNAL_ECRITURE_DOC"));
    }

    /**
     * Methode pour ins�rer les constantes qui s'affiche dans la premi�re page Utiliser super.setParametres(Key, Value)
     */
    @Override
    protected void _headerText() {
        try {
            // V�rifier l'identifiant du journal
            if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
                JadeLogger.fatal(this, "5507 : " + getIdJournal());
                return;
            }
            // Instancier le journal
            journal = new CAJournal();
            journal.setSession(getSession());
            journal.setIdJournal(getIdJournal());
            journal.retrieve(getTransaction());
            if (journal.isNew()) {
                JadeLogger.fatal(this, "5508" + ":" + getTransaction().getErrors().toString());
                return;
            }
            // Societe
            super._headerText();
            // Exercice (date)
            super.setParametres(FWIImportParametre.PARAM_EXERCICE, globaz.globall.util.JACalendar.todayJJsMMsAAAA());
            // Titre du document
            super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("PE_TITRE"));
            // Num�ro du journal
            super.setParametres(CAIListPaiementsEtrangersParam.LABEL_NUMERO, getSession().getLabel("NOJOURNAL"));
            super.setParametres(CAIListPaiementsEtrangersParam.PARAM_NUMERO, journal.getIdJournal());
            // Libell�
            super.setParametres(CAIListPaiementsEtrangersParam.LABEL_LIBELLE, getSession().getLabel("LIBELLE"));
            super.setParametres(CAIListPaiementsEtrangersParam.PARAM_LIBELLE, journal.getLibelle());
            // Date de valeur
            super.setParametres(CAIListPaiementsEtrangersParam.LABEL_DATEVAL, getSession().getLabel("DATEVALEUR"));
            super.setParametres(CAIListPaiementsEtrangersParam.PARAM_DATEVAL, journal.getDateValeurCG());
            // Etat
            super.setParametres(CAIListPaiementsEtrangersParam.LABEL_ETAT, getSession().getLabel("ETAT"));
            super.setParametres(CAIListPaiementsEtrangersParam.PARAM_ETAT, journal.getUcEtat().getLibelle());

            // Type de liste
            super.setParametres(CAIListPaiementsEtrangersParam.LABEL_LISTDET, getSession().getLabel("LISTE"));

            if (getShowDetail() == 0) {
                super.setParametres(CAIListPaiementsEtrangersParam.PARAM_LISTDET,
                        getSession().getLabel("RECAP_RECAPITULATIF"));
            } else {
                super.setParametres(CAIListPaiementsEtrangersParam.PARAM_LISTDET,
                        getSession().getLabel("RECAP_DETAILLEE"));
            }

        } catch (Exception e) {
            JadeLogger.fatal(this, e);

        }

    }

    /**
     * Methode pour ins�rer les constantes qui s'affiche dans la derni�re page Utiliser super.setParametres(Key, Value)
     */
    protected void _summaryText() {
        // si Liste d�taill�e
        if (getShowDetail() != 0) {
            super.setParametres(CAIListSoldeCompteAnnexeParam.LABEL_TOTAUX, getSession().getLabel("TOTAL"));
        }
    }

    /**
     * Methode pour ins�rer les constantes qui s'affiche le nom des colonnes Utiliser super.setParametres(Key, Value)
     */
    protected void _tableHeader() {
        // si Liste d�taill�e
        if (getShowDetail() != 0) {
            this.setParametres(FWIImportParametre.getCol(1), getSession().getLabel("COMPTEANNEXE"));
            this.setParametres(FWIImportParametre.getCol(2), getSession().getLabel("COMPTECOURANT"));
            this.setParametres(FWIImportParametre.getCol(3), getSession().getLabel("DATE"));
            this.setParametres(FWIImportParametre.getCol(4), getSession().getLabel("RUBRIQUE"));
            this.setParametres(FWIImportParametre.getCol(5), getSession().getLabel("LIBELLE"));
            this.setParametres(FWIImportParametre.getCol(6), getSession().getLabel("PE_MONTANTME"));
            this.setParametres(FWIImportParametre.getCol(7), getSession().getLabel("DEBIT"));
            this.setParametres(FWIImportParametre.getCol(8), getSession().getLabel("CREDIT"));
        }
        this.setParametres(FWIImportParametre.getCol(10), getSession().getLabel("RECAP_PAR_RUBRIQUE"));
        this.setParametres(FWIImportParametre.getCol(11), getSession().getLabel("PE_ME"));
        this.setParametres(FWIImportParametre.getCol(12), getSession().getLabel("DOIT"));
        this.setParametres(FWIImportParametre.getCol(13), getSession().getLabel("AVOIR"));
        this.setParametres(FWIImportParametre.getCol(14), getSession().getLabel("SOLDE"));
        this.setParametres(FWIImportParametre.getCol(15), getSession().getLabel("TOTAL"));

        this.setParametres(FWIImportParametre.getCol(16), getSession().getLabel("PE_ISOME"));
        this.setParametres(FWIImportParametre.getCol(17), getSession().getLabel("PE_RECAP_PAR_ME"));
        this.setParametres(FWIImportParametre.getCol(18), getSession().getLabel("PE_TOTAL_ME"));
        this.setParametres(FWIImportParametre.getCol(19), getSession().getLabel("PE_TOTAL_CHF"));
    }

    /**
     * ADL, utilise pour envoiyer un email quand le process est termin�
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        setSendCompletionMail(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#afterBuildReport ()
     */
    @Override
    public void afterBuildReport() {
        FWIImporterInterface importDoc = super.getImporter();
        Map globalMap = importDoc.getParametre();
        String currTemplate = importDoc.getDocumentTemplate();
        importDoc.setDocumentTemplate("CAIListPaiementsEtrangers_sub");
        int numeroPage = importDoc.getDocument().getPages().size();
        // Premiere recap
        // Definir subsource 1 --> r�capitulation par rubrique
        try {
            CAIListPaiementsEtrangers_DS ds1 = new CAIListPaiementsEtrangers_DS();
            ds1.setSession(super.getSession());
            ds1.setTypeSource(CAIListPaiementsEtrangers_DS.TYPE_RUBRIQUE);
            ds1.setForIdJournal(getIdJournal());
            ds1.setVueOperationCpteAnnexe("true");
            ds1.setOrderByRubrique(true);
            ds1.setShowDetail(getShowDetail());
            ds1.setContext(process);
            importDoc.setParametre(CAIListPaiementsEtrangersParam.PARAM_TYPE_SUB, new Boolean(true));
            importDoc.setParametre(CAIListPaiementsEtrangersParam.PARAM_PAGENUMERO, new Integer(numeroPage));
            importDoc.setDataSource(ds1);
            importDoc.createDocument();
        } catch (FWIException e) {
            JadeLogger.fatal(this, e);
            e.printStackTrace();
        }
        numeroPage += importDoc.getDocument().getPages().size();
        // Deuxieme Recap
        // Definir subsource 2 --> R�capitulation par compte courant
        currTemplate = importDoc.getDocumentTemplate();
        importDoc.setDocumentTemplate("CAIListPaiementsEtrangers_sub2");

        try {
            CAIListPaiementsEtrangers_DS ds2 = new CAIListPaiementsEtrangers_DS();
            ds2.setSession(super.getSession());
            ds2.setTypeSource(CAIListPaiementsEtrangers_DS.TYPE_MONNAIE);
            ds2.setForIdJournal(getIdJournal());
            ds2.setVueOperationCpteAnnexe("true");
            ds2.setOrderByISOMe(true);
            ds2.setShowDetail(getShowDetail());
            ds2.setContext(process);
            importDoc.setParametre(CAIListPaiementsEtrangersParam.PARAM_TYPE_SUB, new Boolean(false));
            importDoc.setParametre(CAIListPaiementsEtrangersParam.PARAM_PAGENUMERO, new Integer(numeroPage));
            importDoc.setDataSource(ds2);
            importDoc.createDocument();
        } catch (FWIException e) {
            JadeLogger.fatal(this, e);
            e.printStackTrace();
        }

        // On redonne les infos d'origine
        importDoc.setParametre(globalMap);
        importDoc.setDocumentTemplate(currTemplate);

        // CQFD
    }

    /**
     * Derni�re m�thode lanc� avant la cr�ation du document par JasperReport Dernier minute pour fournir le nom du
     * rapport � utiliser avec la m�thode setTemplateFile(String) et si n�cessaire le type de document � sortir avec la
     * m�thode setFileType(String [PDF|CSV|HTML|XSL]) par d�faut PDF Date de cr�ation : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        super.setParametres(CAIListPaiementsEtrangersParam.PARAM_SHOWDETAIL, new Integer(getShowDetail()));
        getDocumentInfo().setDocumentTypeNumber(CAIListPaiementsEtrangers_Doc.NUMERO_REFERENCE_INFOROM);
        _getRefParam();
        _headerText();
        _tableHeader();
        _summaryText();
    }

    /**
     * Premi�re m�thode appel� (sauf _validate()) avant le chargement des donn�es par le processus On initialise le
     * manager principal d�finit dans le constructeur ou si on fournit un JRDataSource on le fournit aussi ici avec la
     * m�thode setSource et setSubSource (setSubReport(true) si on a un sousRapport avec des valeurs non param�tres)
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.setTemplateFile("CAIListPaiementsEtrangers");
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (25.02.2003 13:59:41)
     * 
     * @param id
     *            java.lang.String
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public void bindData(String id) throws Exception {
        setIdJournal(id);
        // execute();
        // Definir source
        // seb super.execute();
    }

    /**
     * Methode appel� pour la cr�ation des valeurs pour le document 1) addRow (si n�cessaire) 2) App�le des m�thodes
     * pour la cr�ation des param�tres
     */
    @Override
    public void createDataSource() throws FWIException {
        CAIListPaiementsEtrangers_DS ds = new CAIListPaiementsEtrangers_DS();
        ds.setSession(super.getSession());
        ds.setForIdJournal(getIdJournal());
        ds.setVueOperationCpteAnnexe("true");
        ds.setContext(process);

        // si Liste d�taill�e ou Regroupement par compte annexe
        if (getShowDetail() != 0) {
            super.getImporter().setDataSource(ds);
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (04.06.2003 09:54:29)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdJournal() {
        return idJournal;
    }

    /**
     * Cette m�thode permet de conna�tre le type de liste : - R�capitulatif - Liste d�taill�e Date de cr�ation :
     * (02.06.2003 07:35:08)
     * 
     * @return int
     */
    public int getShowDetail() {
        return showDetail;
    }

    /**
     * Method jobQueue. Cette m�thode d�finit la nature du traitement s'il s'agit d'un processus qui doit-�tre lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        // return GlobazJobQueue.READ_LONG;
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (04.06.2003 09:54:29)
     * 
     * @param newIdJournal
     *            java.lang.String
     */
    public void setIdJournal(java.lang.String newIdJournal) {
        idJournal = newIdJournal;
    }

    /**
     * Cette m�thode permet de d�finir si on veut le d�tail des �critures ou non.
     * 
     * @param newShowDetail
     *            boolean
     */
    public void setShowDetail(int newShowDetail) {
        showDetail = newShowDetail;
    }

}
