package globaz.osiris.print.itext.list;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.api.FWIImporterInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationOrdreManager;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrementManager;
import globaz.osiris.db.comptes.CAOperationOrdreVersementManager;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.utils.CAAdressePaiementFormatter;
import globaz.osiris.utils.CAOsirisContainer;
import globaz.webavs.common.CommonExcelmlUtils;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * @author: Administrator
 */
public class CAIListOrdreGroupe extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String NUMERO_REFERENCE_INFOROM = "0016GCA";
    public static final String XLS_DOC_NAME = "ListOrdreGroupe";
    private int compteur = 0;
    private boolean hasNext = true;
    private String idOrdreGroupe = new String();
    private String idTypeOperation = new String();
    FWIImporterInterface importDoc = null;
    private List li = null;
    private Map m_container = new java.util.Hashtable();
    private Map m_containerRecap = new java.util.Hashtable();
    private CAOrdreGroupe ordreGroupe;
    private String typeImpression = "pdf";
    private CAOsirisContainer xlsContainer = new CAOsirisContainer();

    /**
     * @param parent
     * @throws FWIException
     */
    public CAIListOrdreGroupe(BProcess parent) throws FWIException {
        super(parent, CAApplication.DEFAULT_OSIRIS_ROOT, "ListOrdreGroupe");
        super.setDocumentTitle(getSession().getLabel("TITLE_LIST_ORDRE_GROUPE"));
    }

    /**
     * Constructor for CAIListOrdreGroupe.
     * 
     * @param parent
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public CAIListOrdreGroupe(BProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2002 11:49:57)
     * 
     * @return globaz.osiris.db.ordres.CAOrdreGroupe
     */
    public CAOrdreGroupe _getOrdreGroupe() {
        // Si pas déjà chargé
        if (ordreGroupe == null) {
            try {
                ordreGroupe = new CAOrdreGroupe();
                ordreGroupe.setSession(getSession());
                ordreGroupe.setIdOrdreGroupe(getIdOrdreGroupe());
                ordreGroupe.retrieve(getTransaction());
                if (getTransaction().hasErrors()) {
                    getMemoryLog().logMessage("5147", getIdOrdreGroupe(), FWMessage.FATAL, this.getClass().getName());
                    ordreGroupe = null;
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
                ordreGroupe = null;
            }
        }

        return ordreGroupe;

    }

    // Création du paramètre de référence pour les documents de type liste
    private String _getRefParam() {
        try {
            SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy ' - ' HH:mm");
            StringBuffer refBuffer = new StringBuffer(getSession().getLabel("REFERENCE") + " ");
            refBuffer.append(this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.') + 1));
            refBuffer.append(" (");
            refBuffer.append(formater.format(new Date()));
            refBuffer.append(")");
            refBuffer.append(" - ");
            refBuffer.append(getSession().getUserId());
            return refBuffer.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Methode pour insérer les constantes qui s'affiche dans la première page Utiliser super.setParametres(Key, Value)
     */
    protected void _header() {
        try {
            this.setParametres(
                    FWIImportParametre.PARAM_COMPANYNAME,
                    FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                            ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));

            // Exercice (date)
            super.setParametres(FWIImportParametre.PARAM_EXERCICE, globaz.globall.util.JACalendar.todayJJsMMsAAAA());
            // Titre du document
            if (_getOrdreGroupe().getTypeOrdreGroupe().equals(CAOrdreGroupe.VERSEMENT)) {
                super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("6000"));
            } else {
                super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("6001"));
            }
            // Numéro de l'ordre groupé
            super.setParametres(CAIListOrdreGroupeParam.LABEL_NUMERO_ORDRE, getSession().getLabel("NUMERO_ORDRE"));
            super.setParametres(CAIListOrdreGroupeParam.PARAM_NUMERO_ORDRE, _getOrdreGroupe().getIdOrdreGroupe());

            // Motif
            super.setParametres(CAIListOrdreGroupeParam.LABEL_MOTIF, getSession().getLabel("MOTIF"));
            super.setParametres(CAIListOrdreGroupeParam.PARAM_MOTIF, _getOrdreGroupe().getMotif());
            // Date d'échéance
            super.setParametres(CAIListOrdreGroupeParam.LABEL_DATE_ECHEANCE, getSession().getLabel("DATEECHEANCE"));
            super.setParametres(CAIListOrdreGroupeParam.PARAM_DATE_ECHEANCE, _getOrdreGroupe().getDateEcheance());
            // Organe d'exécution
            super.setParametres(CAIListOrdreGroupeParam.LABEL_ORGANE_EXECUTION,
                    getSession().getLabel("ORGANE_EXECUTION"));
            super.setParametres(CAIListOrdreGroupeParam.PARAM_ORGANE_EXECUTION, _getOrdreGroupe().getOrganeExecution()
                    .getNom());
            // Numéro d'ordre groupé
            super.setParametres(CAIListOrdreGroupeParam.LABEL_NUMERO_OG, getSession().getLabel("NUMERO_OG"));
            super.setParametres(CAIListOrdreGroupeParam.PARAM_NUMERO_OG, _getOrdreGroupe().getNumeroOG());
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return;
        }

        return;
    }

    /**
     * Methode pour insérer les constantes qui s'affiche dans la dernière page Utiliser super.setParametres(Key, Value)
     */
    protected void _summary() {
        // Texte total en bas de liste
        super.setParametres(CAIListSoldeSectionParam.LABEL_TOTAUX, getSession().getLabel("TOTAL"));
    }

    /**
     * Methode pour insérer les constantes qui s'affiche le nom des colonnes Utiliser super.setParametres(Key, Value)
     */
    protected void _tableHeader() {
        // Entêtes de colonnes
        this.setParametres(FWIImportParametre.getCol(1), getSession().getLabel("BENEFICIAIRE"));
        this.setParametres(FWIImportParametre.getCol(2), getSession().getLabel("COMPTEANNEXE"));
        this.setParametres(FWIImportParametre.getCol(3), getSession().getLabel("ADRESSE_VERSEMENT"));
        this.setParametres(FWIImportParametre.getCol(4), getSession().getLabel("NATURE_VERSEMENT"));
        this.setParametres(FWIImportParametre.getCol(5), getSession().getLabel("MONTANT"));
        this.setParametres(FWIImportParametre.getCol(6), getSession().getLabel("NUMERO_TRANSACTION"));
        this.setParametres(FWIImportParametre.getCol(10), getSession().getLabel("RECAP_PAR_GENRE_VIREMENT"));
        this.setParametres(FWIImportParametre.getCol(11), getSession().getLabel("NOMBRE"));
        this.setParametres(FWIImportParametre.getCol(12), getSession().getLabel("MONTANT"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#afterBuildReport ()
     */
    @Override
    public void afterBuildReport() {

        if ("xls".equals(getTypeImpression())) {
            try {
                printXlsDocument();
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }

            return;
        }

        Map globalMap = importDoc.getParametre();
        String currTemplate = importDoc.getDocumentTemplate();
        importDoc.setDocumentTemplate("CAIListOrdreGroupe_sub");
        int numeroPage = importDoc.getDocument().getPages().size();
        // Premiere recap
        // Definir subsource 1 --> détail

        try {
            JRBeanCollectionDataSource source = new JRBeanCollectionDataSource((new TreeMap(m_containerRecap)).values());
            importDoc.setParametre(CAIListOrdreGroupeParam.PARAM_PAGENUMERO, new Integer(numeroPage));
            importDoc.setDataSource(source);
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
        importDoc = super.getImporter();
        getDocumentInfo().setTemplateName(importDoc.getDocumentTemplate());

        // Ajout des références en bas de liste
        super.setParametres(FWIImportParametre.PARAM_REFERENCE, _getRefParam());
        getDocumentInfo().setDocumentTypeNumber(CAIListOrdreGroupe.NUMERO_REFERENCE_INFOROM);
        _header();
        _tableHeader();
        _summary();
    }

    /**
     * Première méthode appelé (sauf _validate()) avant le chargement des données par le processus On initialise le
     * manager principal définit dans le constructeur ou si on fournit un JRDataSource on le fournit aussi ici avec la
     * méthode setSource et setSubSource (setSubReport(true) si on a un sousRapport avec des valeurs non paramètres)
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.setTemplateFile("CAIListOrdreGroupe");
    }

    @Override
    public boolean beforePrintDocument() {
        if ("xls".equals(getTypeImpression())) {
            return false;
        }

        return super.beforePrintDocument();
    }

    /**
     * @param dataSource
     * @throws java.lang.Exception
     */
    public void bindData(net.sf.jasperreports.engine.JRDataSource dataSource) throws java.lang.Exception {
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
        setIdOrdreGroupe(id);
    }

    /**
     * Methode appelé pour la création des valeurs pour le document 1) addRow (si nécessaire) 2) Appèle des méthodes
     * pour la création des paramètres
     */
    @Override
    public void createDataSource() {
        li = new ArrayList((new TreeMap(m_container)).values());
        super.setDataSource(li);
    }

    private void extractValues(CAOperation entity) throws Exception {
        try {
            // Données
            CAIListOrdreGroupeBean bean = new CAIListOrdreGroupeBean(entity);
            compteur++;

            m_container.put(new Integer(compteur), bean);

            CAIListOrdreGroupeRecapBean recapBean;
            String sIdTypeAdresse;

            if (entity.getAdressePaiement() != null) {
                // Formatter l'adresse de paiement
                CAAdressePaiementFormatter fmtAdPmt = new CAAdressePaiementFormatter(entity.getAdressePaiement());

                // Récapitulation
                sIdTypeAdresse = "21400" + fmtAdPmt.getTypeAdresse();
            } else {
                sIdTypeAdresse = "";
            }
            recapBean = (CAIListOrdreGroupeRecapBean) m_containerRecap.get(sIdTypeAdresse);

            if (recapBean == null) {
                recapBean = new CAIListOrdreGroupeRecapBean(entity);
            } else {
                recapBean.add(entity);
            }
            m_containerRecap.put(sIdTypeAdresse, recapBean);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

    }

    /**
     * Returns the idOrdreGroupe.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdOrdreGroupe() {
        return idOrdreGroupe;
    }

    /**
     * @return
     */
    public String getIdTypeOperation() {
        return idTypeOperation;
    }

    public String getTypeImpression() {
        return typeImpression;
    }

    /**
     * Returns the hasNext.
     * 
     * @return boolean
     */
    public boolean isHasNext() {
        return hasNext;
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

    private void labelsXls() {
        // Entetes du fichier excel
        this.remplirColumn("NUMERO_INFOROM", CAIListOrdreGroupe.NUMERO_REFERENCE_INFOROM, "");
        this.remplirColumn("DATE_DOC", JACalendar.todayJJsMMsAAAA(), "");
        this.remplirColumn("COL_1_LABEL", getSession().getLabel("BENEFICIAIRE"), "");
        this.remplirColumn("COL_2_LABEL", getSession().getLabel("COMPTEANNEXE"), "");
        this.remplirColumn("COL_3_LABEL", getSession().getLabel("ADRESSE_VERSEMENT"), "");
        this.remplirColumn("COL_4_LABEL", getSession().getLabel("NATURE_VERSEMENT"), "");
        this.remplirColumn("COL_5_LABEL", getSession().getLabel("MONTANT"), "");
        this.remplirColumn("COL_6_LABEL", getSession().getLabel("NUMERO_TRANSACTION"), "");

        this.remplirColumn(FWIImportParametre.PARAM_COMPANYNAME,
                (String) getImporter().getParametre().get(FWIImportParametre.PARAM_COMPANYNAME), "");
        this.remplirColumn(FWIImportParametre.PARAM_TITLE,
                (String) getImporter().getParametre().get(FWIImportParametre.PARAM_TITLE), "");
        this.remplirColumn(CAIListOrdreGroupeParam.LABEL_NUMERO_ORDRE,
                (String) getImporter().getParametre().get(CAIListOrdreGroupeParam.LABEL_NUMERO_ORDRE), "");
        this.remplirColumn(CAIListOrdreGroupeParam.PARAM_NUMERO_ORDRE,
                (String) getImporter().getParametre().get(CAIListOrdreGroupeParam.PARAM_NUMERO_ORDRE), "");
        this.remplirColumn(CAIListOrdreGroupeParam.LABEL_MOTIF,
                (String) getImporter().getParametre().get(CAIListOrdreGroupeParam.LABEL_MOTIF), "");
        this.remplirColumn(CAIListOrdreGroupeParam.PARAM_MOTIF,
                (String) getImporter().getParametre().get(CAIListOrdreGroupeParam.PARAM_MOTIF), "");
        this.remplirColumn(CAIListOrdreGroupeParam.LABEL_DATE_ECHEANCE,
                (String) getImporter().getParametre().get(CAIListOrdreGroupeParam.LABEL_DATE_ECHEANCE), "");
        this.remplirColumn(CAIListOrdreGroupeParam.PARAM_DATE_ECHEANCE,
                (String) getImporter().getParametre().get(CAIListOrdreGroupeParam.PARAM_DATE_ECHEANCE), "");
        this.remplirColumn(CAIListOrdreGroupeParam.LABEL_ORGANE_EXECUTION,
                (String) getImporter().getParametre().get(CAIListOrdreGroupeParam.LABEL_ORGANE_EXECUTION), "");
        this.remplirColumn(CAIListOrdreGroupeParam.PARAM_ORGANE_EXECUTION,
                (String) getImporter().getParametre().get(CAIListOrdreGroupeParam.PARAM_ORGANE_EXECUTION), "");
        this.remplirColumn(CAIListOrdreGroupeParam.LABEL_NUMERO_OG,
                (String) getImporter().getParametre().get(CAIListOrdreGroupeParam.LABEL_NUMERO_OG), "");
        this.remplirColumn(CAIListOrdreGroupeParam.PARAM_NUMERO_OG,
                (String) getImporter().getParametre().get(CAIListOrdreGroupeParam.PARAM_NUMERO_OG), "");
        this.remplirColumn(CAIListSoldeSectionParam.LABEL_TOTAUX, getSession().getLabel("TOTAL"), "");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        if (!isHasNext()) {
            return false;
        }

        CAOperationOrdreManager manager;

        if (getIdTypeOperation().equals(CAOrdreGroupe.RECOUVREMENT)) {
            manager = new CAOperationOrdreRecouvrementManager();
        } else {
            manager = new CAOperationOrdreVersementManager();
        }

        try {
            manager.setSession(super.getSession());
            manager.setForIdOrdreGroupe(getIdOrdreGroupe());
            manager.setOrderBy(CAOperationOrdreManager.ORDER_IDORDREGROUPE_NOMCACHE);

            manager.changeManagerSize(BManager.SIZE_NOLIMIT);

            manager.find(getTransaction());

            if (getParent().isAborted()) {
                return false;
            }

            getParent().setProgressScaleValue(manager.size());

            // lit le nouveau entity

            for (int i = 0; i < manager.getSize(); i++) {
                if (getParent().isAborted()) {
                    return false;
                }
                CAOperation entity = (CAOperation) manager.getEntity(i);
                extractValues(entity);
                getParent().incProgressCounter();
            }

            return true;
            // fin de la transaction on ferme le curseur
        } catch (Exception e) {
            e.printStackTrace();
            JadeLogger.error(this, e);
            return false;
        } finally {
            // Un seul document
            setHasNext(false);
        }
    }

    /**
     * Préparation des données pour le document excel
     */
    private void prepareDataForXLSDoc() {

        BigDecimal total = new BigDecimal(0);

        // labels du doc
        labelsXls();

        // liste
        int size = li.size();
        for (int i = 0; i < size; i++) {

            CAIListOrdreGroupeBean bean = (CAIListOrdreGroupeBean) li.get(new Integer(i));

            this.remplirColumn("COL_1", bean.getCOL_1(), "");
            this.remplirColumn("COL_2", bean.getCOL_2(), "");
            this.remplirColumn("COL_3", bean.getCOL_3(), "");
            this.remplirColumn("COL_4", bean.getCOL_4(), "0.00");
            this.remplirColumn("COL_5", bean.getCOL_5(), "");
            this.remplirColumn("COL_6", bean.getCOL_6(), "");

            if (bean.getCOL_4() != null) {
                total = total.add(bean.getCOL_5());
            }
        }

        this.remplirColumn("TOTAL_MONTANT", total, "0.00");
        this.remplirColumn("TOTAL_TRANS", "" + size, "0");
    }

    public void printXlsDocument() throws Exception {

        try {

            String xmlModelPath = Jade.getInstance().getExternalModelDir() + CAApplication.DEFAULT_OSIRIS_ROOT
                    + "/model/excelml/" + getSession().getIdLangueISO().toUpperCase() + "/"
                    + CAIListOrdreGroupe.XLS_DOC_NAME + "Modele.xml";

            String xlsDocPath = Jade.getInstance().getPersistenceDir()
                    + JadeFilenameUtil.addOrReplaceFilenameSuffixUID(CAIListOrdreGroupe.XLS_DOC_NAME + ".xml");

            prepareDataForXLSDoc();

            xlsDocPath = CommonExcelmlUtils.createDocumentExcel(xmlModelPath, xlsDocPath, xlsContainer);

            JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
            docInfoExcel.setApplicationDomain(CAApplication.DEFAULT_APPLICATION_OSIRIS);
            docInfoExcel.setDocumentTitle(CAIListOrdreGroupe.XLS_DOC_NAME);
            docInfoExcel.setPublishDocument(true);
            docInfoExcel.setArchiveDocument(false);
            docInfoExcel.setDocumentTypeNumber(CAIListOrdreGroupe.NUMERO_REFERENCE_INFOROM);
            this.registerAttachedDocument(docInfoExcel, xlsDocPath);

        } catch (Exception e) {
            throw new Exception("Error generating excel file", e);
        }
    }

    public void remplirColumn(String column, BigDecimal value, String defaultValue) {
        if (value != null) {
            xlsContainer.addValue(column, value.toString());
        } else {
            xlsContainer.addValue(column, defaultValue);
        }
    }

    public void remplirColumn(String column, String value, String defaultValue) {
        if (!JadeStringUtil.isEmpty(value)) {
            xlsContainer.addValue(column, value);
        } else {
            xlsContainer.addValue(column, defaultValue);
        }
    }

    /**
     * Sets the hasNext.
     * 
     * @param hasNext
     *            The hasNext to set
     */
    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    /**
     * Sets the idOrdreGroupe.
     * 
     * @param idOrdreGroupe
     *            The idOrdreGroupe to set
     */
    public void setIdOrdreGroupe(java.lang.String idOrdreGroupe) {
        this.idOrdreGroupe = idOrdreGroupe;
    }

    /**
     * @param string
     */
    public void setIdTypeOperation(String string) {
        idTypeOperation = string;
    }

    /**
     * Commentaire relatif à la méthode setSession.
     */
    public void setSession(globaz.globall.api.BISession newSession) {
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }
}
