package globaz.osiris.print.itext.list;

import globaz.framework.printing.itext.api.FWIImporterInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APIOperation;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperationManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Insérez la description du type ici. Date de création : (03.06.2003 15:46:19)
 * 
 * @author: Administrator
 */
public class CAIListJournalEcritures_Doc extends CAIListFactory {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String NUMERO_REFERENCE_INFOROM = "0014GCA";
    private List etats;
    private String fromIdExterneRole = "";
    private java.lang.String idJournal = new String();
    private CAJournal journal;
    private BProcess process;
    private int showDetail = 0;
    private String toIdExterneRole = "";

    /**
     * Commentaire relatif au constructeur CAIListJournalEcritures_Doc.
     */
    public CAIListJournalEcritures_Doc() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Constructor CAIListJournalEcritures_Doc.
     * 
     * @param context
     */
    public CAIListJournalEcritures_Doc(BProcess parent) throws FWIException {
        super(parent, CAApplication.DEFAULT_OSIRIS_ROOT, "CAIListJournalEcritures");
        super.setDocumentTitle(getSession().getLabel("TITLE_JOURNAL_ECRITURE_DOC"));
    }

    /**
     * Commentaire relatif au constructeur CAIListJournalEcritures_Doc.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CAIListJournalEcritures_Doc(BSession session) throws FWIException {
        super(session, CAApplication.DEFAULT_OSIRIS_ROOT, "CAIListJournalEcritures");
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
            super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("6002"));
            // Numéro du journal
            super.setParametres(CAIListJournalEcrituresParam.LABEL_NUMERO, getSession().getLabel("NOJOURNAL"));
            super.setParametres(CAIListJournalEcrituresParam.PARAM_NUMERO, journal.getIdJournal());
            // Libellé
            super.setParametres(CAIListJournalEcrituresParam.LABEL_LIBELLE, getSession().getLabel("LIBELLE"));
            super.setParametres(CAIListJournalEcrituresParam.PARAM_LIBELLE, journal.getLibelle());
            // Date de valeur
            super.setParametres(CAIListJournalEcrituresParam.LABEL_DATEVAL, getSession().getLabel("DATEVALEUR"));
            super.setParametres(CAIListJournalEcrituresParam.PARAM_DATEVAL, journal.getDateValeurCG());
            // Etat
            super.setParametres(CAIListJournalEcrituresParam.LABEL_ETAT, getSession().getLabel("ETAT"));
            super.setParametres(CAIListJournalEcrituresParam.PARAM_ETAT, journal.getUcEtat().getLibelle());

            // Affichage de la sélection des comptes annexes
            String selectionCompteAnnexe = "";
            if (!JadeStringUtil.isBlank(getFromIdExterneRole()) || !JadeStringUtil.isBlank(getToIdExterneRole())) {
                selectionCompteAnnexe = " : " + getFromIdExterneRole() + " - " + getToIdExterneRole();
            }
            // Type de liste
            super.setParametres(CAIListJournalEcrituresParam.LABEL_LISTDET, getSession().getLabel("LISTE"));

            if (getShowDetail() == 0) {
                super.setParametres(CAIListJournalEcrituresParam.PARAM_LISTDET,
                        getSession().getLabel("RECAP_RECAPITULATIF"));
            } else if (getShowDetail() == 1) {

                super.setParametres(CAIListJournalEcrituresParam.PARAM_LISTDET, getSession()
                        .getLabel("RECAP_DETAILLEE") + selectionCompteAnnexe);
            } else if (getShowDetail() == 2) {
                super.setParametres(CAIListJournalEcrituresParam.PARAM_LISTDET, getSession().getLabel("RECAP_PAR_CA")
                        + selectionCompteAnnexe);
            }

        } catch (Exception e) {
            JadeLogger.fatal(this, e);
            return;
        }
        return;
    }

    /**
     * Methode pour insérer les constantes qui s'affiche dans la dernière page Utiliser super.setParametres(Key, Value)
     */
    protected void _summaryText() {
        // si Liste détaillée ou regroupement par compte annexe
        if (getShowDetail() != 0) {
            super.setParametres(CAIListSoldeCompteAnnexeParam.LABEL_TOTAUX, getSession().getLabel("TOTAL"));
        }
    }

    /**
     * Methode pour insérer les constantes qui s'affiche le nom des colonnes Utiliser super.setParametres(Key, Value)
     */
    protected void _tableHeader() {
        // si Liste détaillée ou Regroupement par compte annexe
        if (getShowDetail() != 0) {
            this.setParametres(FWIImportParametre.getCol(1), getSession().getLabel("COMPTEANNEXE"));
            this.setParametres(FWIImportParametre.getCol(2), getSession().getLabel("COMPTECOURANT"));
            this.setParametres(FWIImportParametre.getCol(3), getSession().getLabel("DATE"));
            this.setParametres(FWIImportParametre.getCol(4), getSession().getLabel("RUBRIQUE"));
            this.setParametres(FWIImportParametre.getCol(5), getSession().getLabel("LIBELLE"));
            this.setParametres(FWIImportParametre.getCol(6), getSession().getLabel("BASE"));
            this.setParametres(FWIImportParametre.getCol(7), getSession().getLabel("DEBIT"));
            this.setParametres(FWIImportParametre.getCol(8), getSession().getLabel("CREDIT"));
            this.setParametres(FWIImportParametre.getCol(9), getSession().getLabel("SOLDE"));
        }
        this.setParametres(FWIImportParametre.getCol(10), getSession().getLabel("RECAP_PAR_RUBRIQUE"));
        this.setParametres(FWIImportParametre.getCol(11), getSession().getLabel("DATE"));
        this.setParametres(FWIImportParametre.getCol(12), getSession().getLabel("DOIT"));
        this.setParametres(FWIImportParametre.getCol(13), getSession().getLabel("AVOIR"));
        this.setParametres(FWIImportParametre.getCol(14), getSession().getLabel("SOLDE"));
        this.setParametres(FWIImportParametre.getCol(15), getSession().getLabel("TOTAL"));
        this.setParametres(FWIImportParametre.getCol(16), getSession().getLabel("RECAP_PAR_CC"));
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
        importDoc.setDocumentTemplate("CAIListJournalEcritures_sub");
        int numeroPage = importDoc.getDocument().getPages().size();
        // Premiere recap
        // Definir subsource 1 --> récapitulation par rubrique
        try {
            CAIListJournalEcritures_DS ds1 = new CAIListJournalEcritures_DS();
            ds1.setSession(super.getSession());
            ds1.setTypeSource(CAIListJournalEcritures_DS.TYPE_RUBRIQUE);
            ds1.setForIdJournal(getIdJournal());
            ds1.setForSelectionTri(CAOperationManager.ORDER_RUBRIQUE_DATE_SECTION);
            ds1.setVueOperationCaCcSe(new Boolean(true));
            ds1.setForEtatNotIn(getListeEtats());
            ds1.setShowDetail(getShowDetail());
            ds1.setCompteAnnexeBetween(Boolean.TRUE);
            ds1.setFromIdExterneRole(getFromIdExterneRole());
            ds1.setToIdExterneRole(getToIdExterneRole());
            ds1.setContext(process);
            importDoc.setParametre(CAIListJournalEcrituresParam.PARAM_TYPE_SUB, new Boolean(true));
            importDoc.setParametre(CAIListJournalEcrituresParam.PARAM_PAGENUMERO, new Integer(numeroPage));
            importDoc.setDataSource(ds1);
            importDoc.createDocument();
        } catch (FWIException e) {
            JadeLogger.fatal(this, e);
            e.printStackTrace();
        }
        numeroPage += importDoc.getDocument().getPages().size();
        // Deuxieme Recap
        // Definir subsource 2 --> Récapitulation par compte courant

        if (!getJournal().getEtat().equals(CAJournal.OUVERT)) {
            try {
                CAIListJournalEcritures_DS ds2 = new CAIListJournalEcritures_DS();
                ds2.setSession(super.getSession());
                ds2.setTypeSource(CAIListJournalEcritures_DS.TYPE_CC);
                ds2.setForIdJournal(getIdJournal());
                ds2.setForSelectionTri(CAOperationManager.ORDER_COMPTECOURANT_DATE_SECTION);
                ds2.setVueOperationCaCcSe(new Boolean(true));
                ds2.setForEtatNotIn(getListeEtats());
                ds2.setShowDetail(getShowDetail());
                ds2.setCompteAnnexeBetween(Boolean.TRUE);
                ds2.setFromIdExterneRole(getFromIdExterneRole());
                ds2.setToIdExterneRole(getToIdExterneRole());
                ds2.setContext(process);
                importDoc.setParametre(CAIListJournalEcrituresParam.PARAM_TYPE_SUB, new Boolean(false));
                importDoc.setParametre(CAIListJournalEcrituresParam.PARAM_PAGENUMERO, new Integer(numeroPage));
                importDoc.setDataSource(ds2);
                importDoc.createDocument();
            } catch (FWIException e) {
                JadeLogger.fatal(this, e);
                e.printStackTrace();
            }
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
        super.setParametres(CAIListJournalEcrituresParam.PARAM_SHOWDETAIL, new Integer(getShowDetail()));

        getDocumentInfo().setDocumentTypeNumber(CAIListJournalEcritures_Doc.NUMERO_REFERENCE_INFOROM);
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
        super.setTemplateFile("CAIListJournalEcritures");
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
        CAIListJournalEcritures_DS ds = new CAIListJournalEcritures_DS();

        ds.setSession(super.getSession());
        ds.setForIdJournal(getIdJournal());
        ds.setOrderBy(CAOperationManager.ORDER_CA_SE_CC_DATE_RU);
        ds.setVueOperationCaCcSe(new Boolean(true));
        ds.setForEtatNotIn(getListeEtats());
        ds.setContext(process);

        // si Liste détaillée ou Regroupement par compte annexe
        if (getShowDetail() != 0) {
            // TODO sch 15 nov. 2012 : Ajouter les bornes pour le compte annexe
            ds.setCompteAnnexeBetween(Boolean.TRUE);
            ds.setFromIdExterneRole(getFromIdExterneRole());
            ds.setToIdExterneRole(getToIdExterneRole());
            super.getImporter().setDataSource(ds);
        }
    }

    /**
     * @return the fromIdExterneRole
     */
    public String getFromIdExterneRole() {
        return fromIdExterneRole;
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
     * Cette méthode permet de récupérer un journal.
     * 
     * @return globaz.osiris.db.comptes.CAJournal
     */
    public CAJournal getJournal() {
        // Si pas déjà chargé
        if (journal == null) {
            try {
                journal = new CAJournal();
                journal.setSession(getSession());
                journal.setIdJournal(getIdJournal());
                journal.retrieve(getTransaction());
                if (journal.isNew()) {
                    JadeLogger.fatal(this, "5157 : " + getIdJournal());
                    journal = null;
                }
            } catch (Exception e) {
                JadeLogger.fatal(this, e);
                journal = null;
            }
        }
        return journal;
    }

    private List getListeEtats() {
        if (etats == null) {
            etats = new ArrayList();
            etats.add(APIOperation.ETAT_INACTIF);
        }
        return etats;
    }

    /**
     * Cette méthode permet de connaître le type de liste : - Récapitulatif - Liste détaillée - Regroupement par compte
     * annexe Date de création : (02.06.2003 07:35:08)
     * 
     * @return int
     */
    public int getShowDetail() {
        return showDetail;
    }

    /**
     * @return the toIdExterneRole
     */
    public String getToIdExterneRole() {
        return toIdExterneRole;
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * @param fromIdExterneRole
     *            the fromIdExterneRole to set
     */
    public void setFromIdExterneRole(String fromIdExterneRole) {
        this.fromIdExterneRole = fromIdExterneRole;
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

    /**
     * @param toIdExterneRole
     *            the toIdExterneRole to set
     */
    public void setToIdExterneRole(String toIdExterneRole) {
        this.toIdExterneRole = toIdExterneRole;
    }
}
