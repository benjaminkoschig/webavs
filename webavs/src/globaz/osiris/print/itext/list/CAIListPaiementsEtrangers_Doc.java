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
 * Insérez la description du type ici. Date de création : (03.06.2003 15:46:19)
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
     * Methode pour insérer les constantes qui s'affiche dans la première page Utiliser super.setParametres(Key, Value)
     */
    @Override
    protected void _headerText() {
        try {
            // Vérifier l'identifiant du journal
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
            // Numéro du journal
            super.setParametres(CAIListPaiementsEtrangersParam.LABEL_NUMERO, getSession().getLabel("NOJOURNAL"));
            super.setParametres(CAIListPaiementsEtrangersParam.PARAM_NUMERO, journal.getIdJournal());
            // Libellé
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
     * Methode pour insérer les constantes qui s'affiche dans la dernière page Utiliser super.setParametres(Key, Value)
     */
    protected void _summaryText() {
        // si Liste détaillée
        if (getShowDetail() != 0) {
            super.setParametres(CAIListSoldeCompteAnnexeParam.LABEL_TOTAUX, getSession().getLabel("TOTAL"));
        }
    }

    /**
     * Methode pour insérer les constantes qui s'affiche le nom des colonnes Utiliser super.setParametres(Key, Value)
     */
    protected void _tableHeader() {
        // si Liste détaillée
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
     * ADL, utilise pour envoiyer un email quand le process est terminé
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
        // Definir subsource 1 --> récapitulation par rubrique
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
        // Definir subsource 2 --> Récapitulation par compte courant
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
     * Dernière méthode lancé avant la création du document par JasperReport Dernier minute pour fournir le nom du
     * rapport à utiliser avec la méthode setTemplateFile(String) et si nécessaire le type de document à sortir avec la
     * méthode setFileType(String [PDF|CSV|HTML|XSL]) par défaut PDF Date de création : (25.02.2003 10:18:15)
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
     * Première méthode appelé (sauf _validate()) avant le chargement des données par le processus On initialise le
     * manager principal définit dans le constructeur ou si on fournit un JRDataSource on le fournit aussi ici avec la
     * méthode setSource et setSubSource (setSubReport(true) si on a un sousRapport avec des valeurs non paramètres)
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.setTemplateFile("CAIListPaiementsEtrangers");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 13:59:41)
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
     * Methode appelé pour la création des valeurs pour le document 1) addRow (si nécessaire) 2) Appèle des méthodes
     * pour la création des paramètres
     */
    @Override
    public void createDataSource() throws FWIException {
        CAIListPaiementsEtrangers_DS ds = new CAIListPaiementsEtrangers_DS();
        ds.setSession(super.getSession());
        ds.setForIdJournal(getIdJournal());
        ds.setVueOperationCpteAnnexe("true");
        ds.setContext(process);

        // si Liste détaillée ou Regroupement par compte annexe
        if (getShowDetail() != 0) {
            super.getImporter().setDataSource(ds);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.06.2003 09:54:29)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdJournal() {
        return idJournal;
    }

    /**
     * Cette méthode permet de connaître le type de liste : - Récapitulatif - Liste détaillée Date de création :
     * (02.06.2003 07:35:08)
     * 
     * @return int
     */
    public int getShowDetail() {
        return showDetail;
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
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
     * Insérez la description de la méthode ici. Date de création : (04.06.2003 09:54:29)
     * 
     * @param newIdJournal
     *            java.lang.String
     */
    public void setIdJournal(java.lang.String newIdJournal) {
        idJournal = newIdJournal;
    }

    /**
     * Cette méthode permet de définir si on veut le détail des écritures ou non.
     * 
     * @param newShowDetail
     *            boolean
     */
    public void setShowDetail(int newShowDetail) {
        showDetail = newShowDetail;
    }

}
