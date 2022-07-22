package globaz.osiris.print.itext;

import ch.globaz.common.properties.CommonProperties;
import globaz.aquila.api.ICOApplication;
import globaz.aquila.api.ICOEtape;
import globaz.aquila.api.ICOGestionContentieuxExterne;
import globaz.aquila.print.CODecisionFPV;
import globaz.aquila.print.COParameter;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.CADocumentInfoHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.musca.api.musca.PaireIdExterneEBill;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.*;
import globaz.musca.external.ServicesFacturation;
import globaz.musca.itext.FAImpressionFacture_Param;
import globaz.osiris.process.ebill.EBillFichier;
import globaz.musca.process.FAImpressionFactureProcess;
import globaz.musca.util.FAUtil;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.api.APIEtape;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.db.comptes.extrait.CAExtraitCompteManager;
import globaz.osiris.db.contentieux.CAExtraitCompteListViewBean;
import globaz.osiris.db.contentieux.CALigneExtraitCompte;
import globaz.osiris.db.ebill.enums.CATraitementEtatEBillEnum;
import globaz.osiris.db.historique.CAHistoriqueBulletinSolde;
import globaz.osiris.db.historique.CAHistoriqueBulletinSoldeManager;
import ch.globaz.common.document.reference.ReferenceBVR;
import ch.globaz.common.document.reference.ReferenceQR;
import globaz.osiris.external.IntAdressePaiement;
import globaz.osiris.external.IntRole;
import globaz.osiris.print.itext.list.CADocumentManager;
import globaz.osiris.process.ebill.EBillSftpProcessor;
import globaz.osiris.utils.CAContentieux;
import globaz.osiris.utils.CADateUtil;
import globaz.pyxis.application.TIApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class CAImpressionBulletinsSoldes_Doc extends CADocumentManager {

    private static final long serialVersionUID = -5283345749972078174L;
    public final static int DAYS_ADDED = 12;
    private static final int NB_JOURS_DELAI_FACTURE_ECHUE = 5;

    public final static String NUM_REF_INFOROM_BVR_SOLDE = "0100CFA";

    public final static String TEMPLATE_FILENAME = "MUSCA_BVR4BULLSOLD_QR";
    public final static String TYPE_FACTURE_ETUDIANT = "22";

    private String adressePrincipale;
    private FAAfact afact;
    private ReferenceBVR bvr = null;
    private Boolean callEcran = new Boolean(false);
    private String centimes;
    private Iterator<FAAfact> entityList = null;
    private Boolean envoyerGed = new Boolean(false);
    private boolean factureAvecMontantMinime = false;
    private int factureImpressionNo = 0;
    private boolean factureMontantReport = false;
    private Boolean forcerBV = false;
    private Boolean eBillPrintable = false;
    private String idModule = "";
    private String idSection = "";
    private boolean isMuscaSource = false;
    FAModulePassage module = null;
    private String montantMinimeMax = null;
    private String montantMinimeNeg = null;
    private String montantMinimePos = null;
    private String montantSansCentime;
    public Map<PaireIdExterneEBill, List<Map>> lignesSolde = new LinkedHashMap();
    public Map<PaireIdExterneEBill, String> referencesSolde = new LinkedHashMap();
    private static final Logger LOGGER = LoggerFactory.getLogger(CAImpressionBulletinsSoldes_Doc.class);
    private int factureEBill = 0;

    private FAPassage passage;

    private String sectionLibelle = "";

    private BSession sessionAquila = null;
    private BSession sessionMusca = null;

    private BSession sessionOsiris = null;

    public CAImpressionBulletinsSoldes_Doc() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    public CAImpressionBulletinsSoldes_Doc(BProcess parent, String rootApplication, String fileName)
            throws FWIException {
        super(parent, rootApplication, fileName);
    }

    public CAImpressionBulletinsSoldes_Doc(BSession session) throws FWIException {
        super(session, session.getLabel("CACFILENAME"));
    }

    public CAImpressionBulletinsSoldes_Doc(BSession session, FAImpressionFactureProcess parent)
            throws java.lang.Exception {
        super(session, session.getLabel("CACFILENAME"));
        setEnvoyerGed(parent.getEnvoyerGed());
        setCallEcran(parent.getCallEcran());
    }

    public CAImpressionBulletinsSoldes_Doc(BSession session, String rootApplication, String fileName)
            throws FWIException {
        super(session, rootApplication, fileName);
    }

    /**
     * @param list
     * @param dateValeur
     * @param dateComptable
     * @param libelleCourant
     * @param valeurCourante
     */
    private void _addLine(List list, String dateValeur, String dateComptable, String libelleCourant,
                          Double valeurCourante) {
        if (valeurCourante.doubleValue() != 0) {
            Map m = new HashMap();
            m.put("COL_1", dateComptable);
            m.put("COL_2", dateValeur);
            m.put("COL_3", libelleCourant);
            m.put("COL_4", valeurCourante);
            m.put("COL_ID", new Integer(list.size() + 1));
            list.add(m);
        }
    }

    private List _buildLignes() throws Exception {
        List data = new ArrayList();
        BSession session = getSession();
        // String isoLangueTiers = compteAnnexe.getTiers().getLangueISO();
        CAExtraitCompteListViewBean mgr = new CAExtraitCompteListViewBean();

        mgr.setIdCompteAnnexe(sectionCourante.getSection().getIdCompteAnnexe());
        mgr.setSession(session);
        mgr.setForSelectionTri(CAExtraitCompteManager.ORDER_BY_DATE_COMPTABLE);
        mgr.setForIdSection(sectionCourante.getSection().getIdSection());
        mgr.setPrintLanguage(compteAnnexe.getTiers().getLangueISO());
        mgr.find();
        // System.out.println("Date Comptable | Date Valeur | Descrition | montant");
        for (Iterator it = mgr.iterator(); it.hasNext(); ) {
            CALigneExtraitCompte e = (CALigneExtraitCompte) it.next();
            // System.out.println(e.getDateJournal()+" | "+e.getDate()+" | "+e.getDescription()+" | "+e.getTotal()+" | "+e.getHorsCompteAnnexe());

            Double tot = null;
            if (!JadeStringUtil.isEmpty(e.getHorsCompteAnnexe())) {
                tot = new FWCurrency(e.getHorsCompteAnnexe()).doubleObject();
                // ignore ????
            } else {
                tot = new Double(e.getTotal());
                _addLine(data, e.getDateJournal(), e.getDate(), e.getDescription(), tot);
            }
        }
        return data;
    }

    private void _fillDocInfo() {
        String numAff = compteAnnexe.getIdExterneRole();
        String idTiers = compteAnnexe.getIdTiers();

        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", numAff);
        try {
            IFormatData affilieFormater = ((TIApplication) GlobazServer.getCurrentSystem().getApplication(
                    TIApplication.DEFAULT_APPLICATION_PYXIS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", affilieFormater.unformat(numAff));
            TIDocumentInfoHelper.fill(getDocumentInfo(), idTiers, getSession(), compteAnnexe.getIdRole(), numAff,
                    affilieFormater.unformat(numAff));
            getDocumentInfo().setDocumentProperty("annee", getAnneeFromSection(section));

            if (getPassage() != null) {
                getDocumentInfo().setDocumentDate(getPassage().getDateFacturation());
            } else {
                getDocumentInfo().setDocumentDate(JACalendar.today().toStr("."));
            }

            // GED
            if (isMuscaSource()) {
                if (getEnvoyerGed().booleanValue()) {
                    getDocumentInfo().setPublishDocument(false);
                    getDocumentInfo().setArchiveDocument(true);
                } else if (getSessionMusca().getApplication().getProperty("mettreGed").equals("true")
                        && !getCallEcran().booleanValue()) {
                    getDocumentInfo().setPublishDocument(false);
                    getDocumentInfo().setArchiveDocument(true);
                } else {
                    getDocumentInfo().setPublishDocument(true);
                    getDocumentInfo().setArchiveDocument(false);
                }
            } else {
                getDocumentInfo().setPublishDocument(true);
                getDocumentInfo().setArchiveDocument(true);
            }

            getDocumentInfo().setDocumentType(CAImpressionBulletinsSoldes_Doc.NUM_REF_INFOROM_BVR_SOLDE);

            getDocumentInfo().setDocumentProperty("osiris.section.idExterne", afact.getIdExterneFactureCompensation());

            if ((afact != null) && !JadeStringUtil.isBlankOrZero(afact.getIdEnteteFacture())) {
                FAEnteteFacture entete = new FAEnteteFacture();
                entete.setSession(getSession());
                entete.setIdEntete(afact.getIdEnteteFacture());
                entete.retrieve();
                if (!entete.equals(null) && !entete.isNew()) {
                    if (entete.isNonImprimable().booleanValue()) {
                        getDocumentInfo().setSeparateDocument(true);
                    }
                }
            }
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", numAff);
        }
    }

    /**
     * @return
     * @throws Exception
     */
    public String _getEtatModuleComptabilisation() throws Exception {
        if ((module == null) || module.isNew()) {
            idModule = ServicesFacturation.getIdModFacturationByTypeAndPassage(getSessionMusca(), getTransaction(),
                    FAModuleFacturation.CS_MODULE_COMPTABILISATION, getPassage().getIdPassage());
            if (!JadeStringUtil.isBlankOrZero(idModule)) {
                module = new FAModulePassage();
                module.setSession(getSessionMusca());
                module.setIdModuleFacturation(idModule);
                module.setIdPassage(getPassage().getIdPassage());
                module.retrieve();
            } else {
                throw new Exception(getSessionMusca().getLabel("ERREUR_TYPE_MODULE_COMPTA"));
            }
        }
        return module.getIdAction();

    }

    /**
     * Return l'historique des bulletins de soldes pour la section en cours.
     *
     * @return
     * @throws Exception
     */
    private CAHistoriqueBulletinSoldeManager _getHistoriqueBulletinSoldes() throws Exception {
        CAHistoriqueBulletinSoldeManager bsManager = new CAHistoriqueBulletinSoldeManager();
        bsManager.setSession(getSessionOsiris());

        bsManager.setForIdSection(sectionCourante.getSection().getIdSection());

        bsManager.find(getTransaction());

        return bsManager;
    }

    /**
     * Returns the sectionIdExterne
     *
     * @return String
     */
    public String _getSectionIdExterne() {
        if (loadSection() != null) {
            return section.getIdExterne();
        } else {
            return "";
        }
    }

    /**
     * Returns the journalLibelle.
     *
     * @return String
     */
    public String _getSectionLibelle() {
        if (section != null) {
            return section.getDescription(getSession().getIdLangueISO());
        } else {
            return sectionLibelle;
        }
    }

    /**
     * Dans le cas de musca, il y a plusieurs bulletins de soldes � it�rer.
     *
     * @return
     * @throws FWIException
     */
    private boolean _nextElement() throws Exception {
        boolean hasNext;
        if (hasNext = getEntityList().hasNext()) {

            afact = (FAAfact) getEntityList().next();
            setFactureImpressionNo(factureImpressionNo);

            CACompteAnnexeManager manager = findCompteAnnexe();

            if (!manager.hasErrors() && (manager.size() == 1)) {
                compteAnnexe = (CACompteAnnexe) manager.getFirstEntity();

                CASectionManager sectionManager = findSection();

                if (!manager.hasErrors() && (sectionManager.size() == 1)) {
                    // Si le passage est comptabilis�, on ne tient pas compte du
                    // montant de facturation
                    if ((_getEtatModuleComptabilisation().equals(FAModulePassage.CS_ACTION_COMPTABILISE) || getPassage()
                            .getStatus().equals(FAPassage.CS_ETAT_COMPTABILISE))
                            && ((CASection) sectionManager.getFirstEntity()).getSoldeToCurrency().isPositive()
                            && !((CASection) sectionManager.getFirstEntity()).getSoldeToCurrency().isZero()) {
                        sectionCourante = new CAImpressionBulletinsSoldes_DS(
                                (CASection) sectionManager.getFirstEntity(), getSessionOsiris(), compteAnnexe
                                .getTiers().getLangueISO());
                        if (APISection.ID_CATEGORIE_SECTION_REPARATION_DOMMAGES.equals(sectionCourante.getSection()
                                .getCategorieSection())) {
                            if (getEntityList().hasNext()) {
                                return next();
                            } else {
                                return false;
                            }
                        }
                    } else {
                        // contr�ler si le solde Osiris est sup�rieur � la
                        // compensation
                        if (((Double.parseDouble(((CASection) sectionManager.getFirstEntity()).getSolde()) - Double
                                .parseDouble(afact.getMontantFactureToCurrency().toString())) > 0)
                                || isTypeFactureEtudiant()) {
                            factureImpressionNo++;
                            sectionCourante = new CAImpressionBulletinsSoldes_DS(
                                    (CASection) sectionManager.getFirstEntity(), getSessionOsiris(), compteAnnexe
                                    .getTiers().getLangueISO());
                            // sectionCourante.setMontantCompensation(Double.parseDouble(afact.getMontantFactureToCurrency().toString()));
                            super.setDocumentTitle(afact.getIdExterneDebiteurCompensation() + " - "
                                    + afact.getNomTiers());
                        } else {
                            // le solde Osiris - compensation est inf�rieur ou
                            // �gal � 0
                            // on prend on compte le prochain donc....
                            return next();
                        }
                    }
                } else {
                    throw new Exception(
                            (getSession().getLabel("BULLETINS_SOLDES_COMPTE_ANNEXE_NON_TROUVE_COMPTE") + " : "
                                    + compteAnnexe.getIdCompteAnnexe()
                                    + getSession().getLabel("BULLETINS_SOLDES_COMPTE_ANNEXE_NON_TROUVE_ET_FACTURE")
                                    + " : " + afact.getIdExterneFactureCompensation()));
                }
            } else {
                throw new Exception(getSession().getLabel("BULLETINS_SOLDES_COMPTE_ANNEXE_NON_TROUVE")
                        + afact.getIdTiersDebiteurCompensation());
            }
        }
        return hasNext;
    }

    /**
     * Ajoute l'envois du bulletin de soldes � l'historique de la section.
     *
     * @throws Exception
     */
    private void addToHistory() throws Exception {
        CAHistoriqueBulletinSolde historique = new CAHistoriqueBulletinSolde();
        historique.setSession(getSessionOsiris());

        historique.setIdSection(sectionCourante.getSection().getIdSection());
        historique.setDateHistorique(JACalendar.todayJJsMMsAAAA());
        historique.setSolde(sectionCourante.getSection().getSolde());

        historique.add(getTransaction());

        if (historique.hasErrors() || historique.isNew()) {
            throw new Exception(getSession().getLabel("BULLETINS_SOLDES_HISTORIQUES"));
        }
    }

    /**
     * @see FWIDocumentManager#beforeBuildReport()
     */
    @Override
    public void beforeBuildReport() {
        super.setSendCompletionMail(true);
        super.setSendMailOnError(true);
        if (getPassage() != null) {
            getDocumentInfo().setDocumentProperty("numeroPassage", getPassage().getIdPassage());
            getDocumentInfo().setDocumentProperty("numeroPlanFacturation", getPassage().getIdPlanFacturation());
        }
        computeTotalPage();
    }

    /**
     * R�cup�re les informations du d�compte avant impression.
     */
    @Override
    public final void beforeExecuteReport() {
        super.setTemplateFile(CAImpressionBulletinsSoldes_Doc.TEMPLATE_FILENAME);
        super.setImpressionParLot(true);
        super.setTailleLot(1);
        super.setNumeroReferenceInforom(CAImpressionBulletinsSoldes_Doc.NUM_REF_INFOROM_BVR_SOLDE);

        loadSection();
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforePrintDocument()
     */
    @Override
    public boolean beforePrintDocument() {
        if (isMuscaSource()) {
            try {
                getExporter().setExportApplicationRoot(FAApplication.APPLICATION_MUSCA_REP);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return super.beforePrintDocument();
    }

    @Override
    public void afterExecuteReport() {
        if (!isMuscaSource && compteAnnexe != null && sectionCourante.getSection() != null) {

            boolean eBillOsirisActif = CAApplication.getApplicationOsiris().getCAParametres().isEBillOsirisActifEtDansListeCaisses(getSession());

            // On imprime les factures eBill si :
            //  - eBillOsiris est actif
            //  - eBillPrintable est s�lection� sur l'�cran d'impression
            if (eBillOsirisActif && getEBillPrintable()) {
                 if (compteAnnexe != null && !JadeStringUtil.isBlankOrZero(compteAnnexe.getEBillAccountID())) {
                    try {
                        EBillSftpProcessor.getInstance();
                        traiterBulletinDeSoldesEBillOsiris();
                        getMemoryLog().logMessage(getSession().getLabel("OBJEMAIL_EBILL_FAELEC") + factureEBill, FWMessage.INFORMATION, this.getClass().getName());
                    } catch (Exception exception) {
                        LOGGER.error("Impossible de cr�er les fichiers eBill : " + exception.getMessage(), exception);
                        getMemoryLog().logMessage(getSession().getLabel("BODEMAIL_EBILL_FAILED") + exception.getCause().getMessage(), FWMessage.ERREUR, this.getClass().getName());
                    } finally {
                        EBillSftpProcessor.closeServiceFtp();
                    }
                }
            }
        }
    }

    /**
     * La facture est-elle une facture avec montant minime ?<br/>
     * Sauvegarde l'information dans une variable de class.<br/>
     */
    private void checkMontantMinime() {
        if (!getForcerBV()) {
            if (montantMinimeMax == null) {
                try {
                    montantMinimeMax = getSessionMusca().getApplication().getProperty(FAApplication.MONTANT_MINIMEMAX);
                } catch (Exception e) {
                    getMemoryLog().logMessage(
                            "Property (" + FAApplication.MONTANT_MINIMEMAX + ") not defined : " + e.toString(),
                            FWMessage.INFORMATION, this.getClass().getName());
                    montantMinimeMax = "20";
                }
            }
            if (montantMinimePos == null) {
                try {
                    montantMinimePos = getSessionMusca().getApplication().getProperty(FAApplication.MONTANT_MINIMEPOS);
                } catch (Exception e) {
                    getMemoryLog().logMessage(
                            "Property (" + FAApplication.MONTANT_MINIMEPOS + ") not defined : " + e.toString(),
                            FWMessage.INFORMATION, this.getClass().getName());
                    montantMinimePos = "+2";
                }
            }
            if (montantMinimeNeg == null) {
                try {
                    montantMinimeNeg = getSessionMusca().getApplication().getProperty(FAApplication.MONTANT_MINIMENEG);
                } catch (Exception e) {
                    getMemoryLog().logMessage(
                            "Property (" + FAApplication.MONTANT_MINIMENEG + ") not defined : " + e.toString(),
                            FWMessage.INFORMATION, this.getClass().getName());
                    montantMinimeNeg = "-2";
                }
            }

            FWCurrency montantFacCur = new FWCurrency(sectionCourante.getSolde());
            if ((montantFacCur.compareTo(new FWCurrency(montantMinimePos)) < 0)
                    && (montantFacCur.compareTo(new FWCurrency(montantMinimeNeg)) > 0)) {
                setFactureAvecMontantMinime(true);
            } else {
                setFactureAvecMontantMinime(false);
            }

            try {
                if (new Boolean(getSessionMusca().getApplication().getProperty(FAApplication.REPORTER_MONTANT_MIN))) {
                    if ((!montantFacCur.isZero()) && (montantFacCur.compareTo(new FWCurrency(montantMinimePos)) > 0)
                            && (montantFacCur.compareTo(new FWCurrency(montantMinimeMax)) < 0)) {
                        setFactureMontantReport(true);
                        sectionCourante.getSection().setIdModeCompensation(APISection.MODE_REPORT);
                        sectionCourante.getSection().update(getTransaction());
                    } else {
                        setFactureMontantReport(false);
                    }
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(
                        "Property (" + FAApplication.REPORTER_MONTANT_MIN + ") not defined : " + e.toString(),
                        FWMessage.INFORMATION, this.getClass().getName());
            }
        }
    }

    @Override
    public void createDataSource() throws FWIException {
        super.setParametres(FAImpressionFacture_Param.P_HEADER_EACH_PAGE, new Boolean(wantHeaderOnEachPage()));

        FAUtil.fillDocInfoWithPassageInfo(getDocumentInfo(), getPassage());

        if (((afact != null) && (compteAnnexe != null) && (sectionCourante.getSection() != null)) || !isMuscaSource) {
            try {
                //Extrait les lignes dans une liste
                List data = _buildLignes(); // lignes Bulletins de soldes

                // Pour les Bulletins de soldes imprim�s depuis la facturation
                if (isMuscaSource) {

                    // Prepare la map des lignes de bulletins de soldes eBill si propri�t� eBillMusca est active et si compte annexe de la facture inscrit � eBill
                    boolean eBillMuscaActif = CAApplication.getApplicationOsiris().getCAParametres().isEBillMuscaActifEtDansListeCaisses(getSession());
                    if (eBillMuscaActif) {
                        if (compteAnnexe != null && !JadeStringUtil.isBlankOrZero(compteAnnexe.getEBillAccountID())) {
                            // Met les lignes trouv�es dans une hashMap identifi� de mani�re unique par une pair d'idE
                            lignesSolde.put(new PaireIdExterneEBill(afact.getIdExterneRole(), afact.getIdExterneFactureCompensation(), _getMontantApresCompensation()), data);
                        }
                    }
                    if (JadeStringUtil.isEmpty(getIdSection())) {
                        setIdSection(sectionCourante.getSection().getIdSection());
                        loadSection();
                    }
                // Pour les Bulletins de soldes imprim�s depuis la compta auxiliaire
                } else if (sectionCourante.getSection() != null && !isMuscaSource) {

                    // Prepare la map des lignes de bulletins de soldes eBill si propri�t� eBillOsiris est active et si compte annexe de la facture inscrit � eBill et si eBillPrintable est s�lection� sur l'�cran d'impression
                    boolean eBillOsirisActif = CAApplication.getApplicationOsiris().getCAParametres().isEBillOsirisActifEtDansListeCaisses(getSession());
                    if (eBillOsirisActif && getEBillPrintable()) {
                        if (compteAnnexe != null && !JadeStringUtil.isBlankOrZero(compteAnnexe.getEBillAccountID())) {
                            // Met les lignes trouv�es dans une hashMap identifi� de mani�re unique par une pair d'id
                            lignesSolde.put(new PaireIdExterneEBill(sectionCourante.getSection().getCompteAnnexe().getIdExterneRole(), sectionCourante.getSection().getIdExterne(), _getMontantApresCompensation()), data);
                        }
                    }
                }

                super.setParametres(FAImpressionFacture_Param.P_TOTAL_ROW, new Integer(data.size()));
                super.setDataSource(data.toArray(new Object[data.size()]));

                checkMontantMinime();
                try {
                    adressePrincipale = _getAdressePrincipale();
                } catch (Exception e) {
                    // Do nothing.
                }

                initMontant();

                _fillDocInfo();

                ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                        getDocumentInfo(), getSession().getApplication(), compteAnnexe.getTiers().getLangueISO());
                CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
                headerText(headerBean);
                caisseReportHelper.addHeaderParameters(this, headerBean);
                if (!computePageActive) {
                    decalerEcheanceContencieux();
                    tableHeader();
                }

                if (CommonProperties.QR_FACTURE.getBooleanValue()) {
                    // -- QR
                    qrFacture = new ReferenceQR();
                    qrFacture.setSession(getSession());

                    qrFacture.setQrNeutre(APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(sectionCourante.getSection().getIdTypeSection()));
                    qrFacture.setMontantMinimeOuMontantReporter(this.isFactureAvecMontantMinime() || this.isFactureMontantReport());

                    // Initialisation des variables du document
                    initVariableQR(compteAnnexe.getTiers().getLangueISO(), new FWCurrency(_getMontantApresCompensation()), sectionCourante.getSection().getCompteAnnexe().getIdTiers());

                    // G�n�ration du document QR
                    qrFacture.initQR(this, qrFactures);
                    if (isMuscaSource) {
                        referencesSolde.put(new PaireIdExterneEBill(afact.getIdExterneRole(), afact.getIdExterneFactureCompensation(), _getMontantApresCompensation()), qrFacture.getReference());
                    } else {
                        referencesSolde.put(new PaireIdExterneEBill(sectionCourante.getSection().getCompteAnnexe().getIdExterneRole(), sectionCourante.getSection().getIdExterne(), _getMontantApresCompensation()), qrFacture.getReference());
                    }
                } else {
                    fillBVR();
                    if (isMuscaSource) {
                        referencesSolde.put(new PaireIdExterneEBill(afact.getIdExterneRole(), afact.getIdExterneFactureCompensation(), _getMontantApresCompensation()), getBvr().getRefNoSpace());
                    } else {
                        referencesSolde.put(new PaireIdExterneEBill(sectionCourante.getSection().getCompteAnnexe().getIdExterneRole(), sectionCourante.getSection().getIdExterne(), _getMontantApresCompensation()), getBvr().getRefNoSpace());
                    }
                }
                if (!computePageActive) {
                    addToHistory();
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
                throw new FWIException(e);
            }
        }
    }

    /**
     * M�thode permettant de traiter les bulletins de soldes eBill
     * en attente d'�tre envoy� dans le processus actuel.
     */
    public void traiterBulletinDeSoldesEBillOsiris() throws Exception {

        for (Map.Entry<PaireIdExterneEBill, List<Map>> lignes : lignesSolde.entrySet()) {
            if (sectionCourante.getSection().getCompteAnnexe().getIdExterneRole().equals(lignes.getKey().getIdExterneRole())
                    && sectionCourante.getSection().getIdExterne().equals(lignes.getKey().getIdExterneFactureCompensation())) {

                FAEnteteFacture entete = generateEnteteFacture();
                FAEnteteFacture enteteReference = getEnteteFactureReference(lignes.getKey());

                String reference = referencesSolde.get(lignes.getKey());
                JadePublishDocument attachedDocument = removeAndReturnAttachedDocument(entete, getAttachedDocuments());
                creerFichierEBillOsiris(compteAnnexe, entete, enteteReference, lignes.getKey().getMontant(), lignes.getValue(), reference, attachedDocument, getDateFacturationFromSection(sectionCourante.getSection()), sectionCourante.getSection());
                factureEBill++;
            }
        }
    }

    private FAEnteteFacture generateEnteteFacture() {
        FAEnteteFacture entete = new FAEnteteFacture();
        entete.setSession(getSession());
        entete.setIdModeRecouvrement(CodeSystem.MODE_RECOUV_AUTOMATIQUE);
        entete.setIdTiers(sectionCourante.getSection().getCompteAnnexe().getIdTiers());
        entete.setIdTypeCourrier(sectionCourante.getSection().getTypeAdresse());
        entete.setIdDomaineCourrier(sectionCourante.getSection().getDomaine());
        entete.setIdExterneRole(sectionCourante.getSection().getCompteAnnexe().getIdExterneRole());
        entete.setIdExterneFacture(sectionCourante.getSection().getIdExterne());
        return entete;
    }

    /**
     * M�thode permetant de rechercher une ent�te eBill par le biais d'une paire d'idExterneRole et d'idExterneFactureCompensation
     * cette m�thode est utilis� dans le processus d'impression des bulletins de soldes eBill.
     *
     * @param paireIdExterneEBill : pair d'idExterneRole et d'idExterneFactureCompensation
     * @return l'ent�te ou null
     */
    private FAEnteteFacture getEnteteFactureReference(PaireIdExterneEBill paireIdExterneEBill) throws Exception {
        FAEnteteFactureManager manager = new FAEnteteFactureManager();
        manager.setSession(getSession());
        manager.setForIdExterneRole(paireIdExterneEBill.getIdExterneRole());
        manager.setForIdExterneFacture(paireIdExterneEBill.getIdExterneFactureCompensation());
        manager.find(BManager.SIZE_NOLIMIT);
        return (FAEnteteFacture) manager.getFirstEntity();
    }

    /**
     * M�thode permettant de rechercher le fichier g�n�r� durant l'impression
     * de le retourner pour �tre ajouter � la facture eBill et de le supprimer
     * de la listes de fichiers � merger dans l'impression actuelle
     *
     * @param entete : l'entete qui permet d'identifier le fichier � retourner
     * @param attachedDocuments : les fichiers g�n�r� durant l'impression
     * @return le fichier g�n�r� durant l'impression
     */
    public JadePublishDocument removeAndReturnAttachedDocument(FAEnteteFacture entete, List<JadePublishDocument> attachedDocuments) {
        JadePublishDocument attachedDocument = null;
        Iterator<JadePublishDocument> jadePublishDocumentIterator = attachedDocuments.iterator();
        while (jadePublishDocumentIterator.hasNext()) {
            final JadePublishDocument jadePublishDocument = jadePublishDocumentIterator.next();
            if (entete.getIdExterneFacture().equals(jadePublishDocument.getPublishJobDefinition().getDocumentInfo().getDocumentProperties().get(CADocumentInfoHelper.SECTION_ID_EXTERNE))
                    && entete.getIdExterneRole().equals(jadePublishDocument.getPublishJobDefinition().getDocumentInfo().getDocumentProperties().get("numero.role.formatte"))
                    && jadePublishDocument.getPublishJobDefinition().getDocumentInfo().getDocumentType().equals(CAImpressionBulletinsSoldes_Doc.NUM_REF_INFOROM_BVR_SOLDE)) {
                attachedDocument = jadePublishDocument;
                jadePublishDocumentIterator.remove();
                break;
            }
        }
        return attachedDocument;
    }

    private String getDateFacturationFromSection(CASection section) throws JAException {
        JACalendarGregorian calendar = new JACalendarGregorian();
        JADate dateFacturation = JACalendar.today();
        JADate dateEcheanceSection = new JADate(section.getDateEcheance());
        if (calendar.compare(dateFacturation, dateEcheanceSection) == JACalendar.COMPARE_FIRSTUPPER) {
             return dateFacturation.toStr(".");
        } else {
             return dateEcheanceSection.toStr(".");
        }
    }

    /**
     * M�thode permettant de cr�er la bulletin de soldes eBill,
     * de g�n�rer et remplir le fichier puis de l'envoyer sur le ftp.
     *
     * @param compteAnnexe            : le compte annexe
     * @param entete                  : l'ent�te de la facture
     * @param enteteReference         : l'ent�te de r�f�rence pour les bulletin de soldes (vide dans le cas d'une facture bvr)
     * @param montantBulletinSoldes   : contient le montant total de la factures (seulement rempli dans le case d'un bulletin de soldes)
     * @param lignes                  : contient les lignes de bulletins de soldes
     * @param reference               : la r�f�rence BVR ou QR.
     * @param attachedDocument        : le fichier cr�e par l'impression classique � joindre en base64 dans le fichier eBill
     * @param dateFacturation         : la date de facturation
     * @param section                 : la section
     * @throws Exception
     */
    private void creerFichierEBillOsiris(CACompteAnnexe compteAnnexe, FAEnteteFacture entete, FAEnteteFacture enteteReference, String montantBulletinSoldes, List<Map> lignes, String reference, JadePublishDocument attachedDocument, String dateFacturation, CASection section) throws Exception {

        String billerId = CAApplication.getApplicationOsiris().getCAParametres().getEBillBillerId();

        // G�n�re et ajoute un eBillTransactionId dans l'ent�te de facture eBill
        entete.addEBillTransactionID(getTransaction());

        // Met � jour le flag eBillPrinted dans l'ent�te de facture eBill
        entete.setEBillPrinted(true);

        // met � jour le status eBill de la section
        updateSectionEtatEtTransactionID(section, entete.getEBillTransactionID());

        String dateEcheance = dateFacturation;
        EBillFichier.creerFichierEBill(compteAnnexe, entete, enteteReference, montantBulletinSoldes, null, lignes, reference, attachedDocument, dateFacturation, dateEcheance, billerId, getSession());
    }

    /**
     * Mise � jour du statut eBill de la section et de son transactionID eBill
     * passe l'�tat � pending.
     *
     * @param section  la section � mettre � jour.
     * @param transactionId l'id de transaction li� au traitement.
     */
    public void updateSectionEtatEtTransactionID(final CASection section, final String transactionId) {
        try {
            section.setEBillEtat(CATraitementEtatEBillEnum.NUMERO_ETAT_REJECTED_OR_PENDING);
            section.setEBillErreur("");
            section.setEBillTransactionID(transactionId);
            section.update();
        } catch (Exception e) {
            getMemoryLog().logMessage("Impossible de mettre � jour la section avec l'id : " + section.getIdSection() + " : " + e.getMessage(), FWViewBeanInterface.WARNING, this.getClass().getName());
        }
    }

    /**
     * D�cale l'�ch�ance du contentieux si l'�tape de sommation n'est pas encore atteinte.<br/>
     * Si la section n'est pas au contentieux, cette derni�re y sera pr�ablement ajout�e.
     *
     * @throws Exception
     */
    private void decalerEcheanceContencieux() throws Exception {
        String idRole = sectionCourante.getSection().getCompteAnnexe().getIdRole();
        if (!idRole.equals(IntRole.ROLE_ADMINISTRATEUR)
                && sectionCourante.getSection().getCategorieSection()
                .equals(APISection.ID_CATEGORIE_SECTION_REPARATION_DOMMAGES)) {
            return;
        }

        if (new FWCurrency(sectionCourante.getSolde()).isPositive()) {
            if (isFactureEchue() && (!sectionCourante.getSection().isSectionAuContentieux() || sommationNotReached())) {
                if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
                    JACalendarGregorian calendar = new JACalendarGregorian();
                    if (!sectionCourante.getSection().isSectionAuContentieux()
                            && JadeStringUtil.isBlank(sectionCourante.getSection()
                            .getFirstIdEvContentieuxNonExecuteIgnore())
                            && JadeStringUtil.isBlank(sectionCourante.getSection().getFirstIdEvContentieuxNonExecute())) {
                        CAContentieux.creerPremiereEtapeAncienContentieux(getSessionOsiris(), getTransaction(),
                                sectionCourante.getSection(), calendar.addDays(sectionCourante.getSection()
                                        .getDateEcheance(), CAImpressionBulletinsSoldes_Doc.DAYS_ADDED));
                    } else if (isAncienContentieuxEtapeRappel()
                            && !JadeStringUtil.isBlank(sectionCourante.getSection().getLastEvenementContentieux()
                            .getDateExecution())
                            && JadeStringUtil.isBlank(sectionCourante.getSection().getFirstIdEvContentieuxNonExecute())) {
                        CAContentieux.insererSommationAncienContentieux(getSessionOsiris(), getTransaction(),
                                sectionCourante.getSection(), calendar.addDays(sectionCourante.getSection()
                                        .getDateEcheance(), CAImpressionBulletinsSoldes_Doc.DAYS_ADDED));
                    } else {
                        CAContentieux.decalerEcheance(getSessionOsiris(), getTransaction(),
                                sectionCourante.getSection(), CAImpressionBulletinsSoldes_Doc.DAYS_ADDED);
                    }
                } else {
                    ICOGestionContentieuxExterne gestionContentieux = (ICOGestionContentieuxExterne) getSessionAquila()
                            .getAPIFor(ICOGestionContentieuxExterne.class);
                    if (!sectionCourante.getSection().isSectionAuContentieux()) {
                        gestionContentieux.creerContentieux(getSessionAquila(), getTransaction(),
                                sectionCourante.getSection());
                    }

                    gestionContentieux.decalerEcheance(getSessionAquila(), getTransaction(),
                            sectionCourante.getSection(), CAImpressionBulletinsSoldes_Doc.DAYS_ADDED);
                }
            }
        }
    }

    /**
     * Rempli le BVR.
     */
    private void fillBVR() {
        try {
            getBvr().setSession(getSession());
            getBvr().setForcerBV(getForcerBV());
            getBvr().setBVR(sectionCourante.getSection(), _getMontantApresCompensation(), isFactureMontantReport());

            // Modification suite � QR-Facture. Choix du footer
            super.setParametres(COParameter.P_SUBREPORT_QR, getImporter().getImportPath() + "BVR_TEMPLATE.jasper");
            super.setParametres(COParameter.P_SUBREPORT_QR_CURRENT_PAGE, getImporter().getImportPath() + "BVR_TEMPLATE_CURRENT_PAGE.jasper");

            super.setParametres(FWIImportParametre.PARAM_REFERENCE + "_X", CODecisionFPV.REFERENCE_NON_FACTURABLE_DEFAUT);
            super.setParametres(COParameter.P_OCR + "_X", CODecisionFPV.OCRB_DEFAUT);
            super.setParametres(COParameter.P_FRANC + "_X", CODecisionFPV.MONTANT_DEFAUT);
            super.setParametres(COParameter.P_CENTIME + "_X", CODecisionFPV.CENT_DEFAUT);


            super.setParametres(CAImpressionBulletinsSoldes_Param.P_ADRESSE, getBvr().getAdresse());
            super.setParametres(CAImpressionBulletinsSoldes_Param.P_ADRESSECOPY, getBvr().getAdresse());
            super.setParametres(CAImpressionBulletinsSoldes_Param.P_COMPTE, getBvr().getNumeroCC());

            super.setParametres(CAImpressionBulletinsSoldes_Param.P_VERSE, getBvr().getLigneReference() + "\n"
                    + adressePrincipale);
            super.setParametres(CAImpressionBulletinsSoldes_Param.P_PAR, adressePrincipale);
            super.setParametres(FWIImportParametre.PARAM_REFERENCE, getBvr().getLigneReference());
            super.setParametres(CAImpressionBulletinsSoldes_Param.P_OCR, getBvr().getOcrb());
        } catch (Exception e1) {
            getMemoryLog().logMessage(
                    getSession().getLabel("BULLETINS_SOLDES_ERREUR_RECHERCHE_TEXTE") + " : " + e1.getMessage(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }

        FWCurrency tmpCurrency = new FWCurrency(_getMontantApresCompensation());
        if (!tmpCurrency.isNegative() && !tmpCurrency.isZero() && !isFactureAvecMontantMinime()
                && !isFactureMontantReport()) {
            super.setParametres(CAImpressionBulletinsSoldes_Param.P_FRANC, montantSansCentime);
            super.setParametres(CAImpressionBulletinsSoldes_Param.P_CENTIME, centimes);
        } else {
            super.setParametres(CAImpressionBulletinsSoldes_Param.P_FRANC, "XXXXXXXXXXXXXXX");
            super.setParametres(CAImpressionBulletinsSoldes_Param.P_CENTIME, "XX");
        }
    }

    /**
     * Recherche le compte annexe pour le r�le et l'idexterne.
     *
     * @return
     */
    private CACompteAnnexeManager findCompteAnnexe() throws Exception {
        CACompteAnnexeManager manager = new CACompteAnnexeManager();
        manager.setSession(getSessionOsiris());

        // On contr�le si le idRoleDebiteurCompensation de l'afact n'est pas
        // vide on le prend sinon on prend celui de l'ent�te
        if (!JadeStringUtil.isBlankOrZero(afact.getIdRoleDebiteurCompensation())) {
            manager.setForIdRole(afact.getIdRoleDebiteurCompensation());
        } else {
            manager.setForIdRole(afact.getEnteteFacture().getIdRole());
        }
        // On contr�le si le idRoleDebiteurCompensation de l'afact n'est pas
        // vide on le prend sinon on prend celui de l'ent�te
        if (!JadeStringUtil.isBlankOrZero(afact.getIdExterneDebiteurCompensation())) {
            manager.setForIdExterneRole(afact.getIdExterneDebiteurCompensation());
        } else {
            manager.setForIdExterneRole(afact.getEnteteFacture().getIdExterneRole());
        }

        manager.find(getTransaction());

        return manager;
    }

    /**
     * Recherche la section correspondante du compte annexe (idexterne facture et type de facture).
     *
     * @return
     */
    private CASectionManager findSection() throws Exception {
        CASectionManager sectionManager = new CASectionManager();
        sectionManager.setSession(getSessionOsiris());
        sectionManager.setForIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
        sectionManager.setForIdExterne(afact.getIdExterneFactureCompensation());
        sectionManager.setForIdTypeSection(afact.getIdTypeFactureCompensation());

        sectionManager.find(getTransaction());

        return sectionManager;
    }

    /**
     * Renvoie la r�f�rence BVR.
     *
     * @return la r�f�rence BVR.
     */
    public ReferenceBVR getBvr() {
        if (bvr == null) {
            bvr = new ReferenceBVR();
        }
        return bvr;
    }

    /**
     * Renvoie la r�f�rence QR.
     *
     * @return la r�f�rence QR.
     */
    public ReferenceQR getQrFacture() {
        return qrFacture;
    }

    public Boolean getCallEcran() {
        return callEcran;
    }

    /**
     * Calcule le delai de paiement Base = max(dateFacturation + 12j, date d'�ch�ance de la section) <br>
     *
     * @return Date de d�lai de paiement
     * @author: sel Cr�� le : 10 nov. 06
     */
    private String getDelaiPaiement() {
        String result = null;

        try {
            JACalendarGregorian calendar = new JACalendarGregorian();
            JADate dateFacturation;
            JADate dateEcheanceSection = new JADate(sectionCourante.getSection().getDateEcheance());

            if (getPassage() != null) {
                dateFacturation = new JADate(getPassage().getDateFacturation());
            } else {
                dateFacturation = JACalendar.today();
            }

            dateFacturation = calendar.addDays(dateFacturation, CAImpressionBulletinsSoldes_Doc.DAYS_ADDED);
            dateFacturation = CADateUtil.getDateOuvrable(dateFacturation);

            if (calendar.compare(dateFacturation, dateEcheanceSection) == JACalendar.COMPARE_FIRSTUPPER) {
                result = dateFacturation.toStr(".");
            } else {
                result = dateEcheanceSection.toStr(".");
            }
        } catch (Exception e) {
            result = "";
        }

        return result;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            // le process s'est mal d�roul�
            if (isMuscaSource) {
                return FWMessageFormat.format(getSession().getLabel("SOLDE_EMAIL_PASS_PASOK"), passage.getIdPassage());
            } else {
                return FWMessageFormat.format(getSession().getLabel("SOLDE_EMAIL_SEC_PASOK"), section.getIdExterne());
            }
        } else {
            // le process s'est bien d�roul�
            if (isMuscaSource) {
                return FWMessageFormat.format(getSession().getLabel("SOLDE_EMAIL_PASS_OK"), passage.getIdPassage());
            } else {
                return FWMessageFormat.format(getSession().getLabel("SOLDE_EMAIL_SEC_OK"), section.getIdExterne());
            }
        }
    }

    /**
     * Returns the entityList.
     *
     * @return Iterator
     */
    protected Iterator getEntityList() {
        return entityList;
    }

    public Boolean getEnvoyerGed() {
        return envoyerGed;
    }

    /**
     * Returns the factureImpressionNo.
     *
     * @return int
     */
    public int getFactureImpressionNo() {
        return factureImpressionNo;
    }

    /**
     * @return the forcerBV
     */
    public Boolean getForcerBV() {
        return forcerBV;
    }

    /**
     * @return the eBillPrintable
     */
    public Boolean getEBillPrintable() {
        return eBillPrintable;
    }

    /**
     * Returns the journalId.
     *
     * @return String
     */
    public String getIdSection() {
        return idSection;
    }

    /**
     * @return the montantMinimeMax
     */
    public String getMontantMinimeMax() {
        return montantMinimeMax;
    }

    /**
     * Returns the montantMinimeNeg.
     *
     * @return String
     */
    public String getMontantMinimeNeg() {
        return montantMinimeNeg;
    }

    /**
     * Returns the montantMinimePos.
     *
     * @return String
     */
    public String getMontantMinimePos() {
        return montantMinimePos;
    }

    /**
     * Returns the passage.
     *
     * @return FAPassage
     */
    public FAPassage getPassage() {
        return passage;
    }

    public BSession getSessionAquila() {
        if (sessionAquila == null) {
            try {
                sessionAquila = (BSession) GlobazSystem.getApplication(ICOApplication.DEFAULT_APPLICATION_AQUILA)
                        .newSession(getSession());
            } catch (Exception ex) {
                sessionAquila = getSession();
            }
        }

        return sessionAquila;
    }

    public BSession getSessionMusca() {
        if (sessionMusca == null) {
            try {
                sessionMusca = (BSession) GlobazSystem.getApplication(FAApplication.DEFAULT_APPLICATION_MUSCA)
                        .newSession(getSession());
            } catch (Exception ex) {
                sessionMusca = getSession();
            }
        }

        return sessionMusca;
    }

    public BSession getSessionOsiris() {
        if (sessionOsiris == null) {
            try {
                sessionOsiris = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS)
                        .newSession(getSession());
            } catch (Exception ex) {
                sessionOsiris = getSession();
            }
        }

        return sessionOsiris;
    }

    public final String getTextWithoutDelimiter(String textString, String delim) {
        StringTokenizer st = new java.util.StringTokenizer(textString, delim);
        StringBuffer sb = new StringBuffer();
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
        }
        return sb.toString();
    }

    /**
     * @param headerBean
     * @throws Exception
     */
    private void headerText(CaisseHeaderReportBean headerBean) throws Exception {
        if (CAApplication.getApplicationOsiris().getCAParametres().isConfidentiel()) {
            headerBean.setConfidentiel(true);
        }

        headerBean.setAdresse(adressePrincipale);
        headerBean.setNoAffilie(compteAnnexe.getIdExterneRole());

        super.setParametres(CAImpressionBulletinsSoldes_Param.getParamP(6),
                getSession().getApplication().getLabel("CACAVS", compteAnnexe.getTiers().getLangueISO()) + ": ");
        super.setParametres(CAImpressionBulletinsSoldes_Param.getParamP(8), sectionCourante.getSection()
                .getFullDescription(compteAnnexe.getTiers().getLangueISO()));
        super.setParametres(CAImpressionBulletinsSoldes_Param.P_ADR_CAISSE,
                getSession().getApplication().getLabel("ADR_CAISSE", compteAnnexe.getTiers().getLangueISO()));

        JadeUserService service = JadeAdminServiceLocatorProvider.getLocator().getUserService();
        if ((passage != null) && !JadeStringUtil.isBlank(passage.getPersonneRef())) {
            JadeUser user = service.loadForVisa(passage.getPersonneRef());

            headerBean.setNomCollaborateur(user.getFirstname() + " " + user.getLastname());
            headerBean.setTelCollaborateur(user.getPhone());
            headerBean.setUser(user);
        } else {
            headerBean.setUser(getSession().getUserInfo());
            headerBean.setNomCollaborateur(getSession().getUserInfo().getFirstname() + " "
                    + getSession().getUserInfo().getLastname());
            headerBean.setTelCollaborateur(getSession().getUserInfo().getPhone());
            headerBean.setEmailCollaborateur(getSession().getUserInfo().getEmail());
        }

        String numImpression = getSession().getApplication()
                .getLabel("CACPAGE", compteAnnexe.getTiers().getLangueISO()) + " ";
        String dateImpression = null;
        if (getPassage() != null) {
            dateImpression = getPassage().getDateFacturation();
            numImpression += getPassage().getIdPassage() + "-" + getFactureImpressionNo();
        } else {
            dateImpression = JACalendar.today().toStr(".");
            numImpression = "";
        }

        headerBean.setDate(JACalendar.format(dateImpression, compteAnnexe.getTiers().getLangueISO()));
        super.setParametres(CAImpressionBulletinsSoldes_Param.P_FACTURE_IMPNO, numImpression);

    }

    /**
     * Initialise les montants (avec centimes, sans centimes, centimes)
     */
    public void initMontant() {
        String montantFacture = _getMontantApresCompensation();
        montantFacture = JANumberFormatter.deQuote(montantFacture);

        montantSansCentime = JAUtil.createBigDecimal(montantFacture).toBigInteger().toString();
        BigDecimal montantSansCentimeBig = JAUtil.createBigDecimal(montantSansCentime);

        BigDecimal montantAvecCentimeBig = JAUtil.createBigDecimal(montantFacture);

        try {
            centimes = montantAvecCentimeBig.subtract(montantSansCentimeBig).toString().substring(2, 4);
        } catch (Exception e) {
            centimes = "";
        }
    }

    /**
     * En mode ancien contentieux, est-on � l'�tape rappel ?
     *
     * @return
     * @throws Exception
     */
    private boolean isAncienContentieuxEtapeRappel() throws Exception {
        if (sectionCourante.getSection().getLastEvenementContentieux() != null) {
            String typeEtape = sectionCourante.getSection().getLastEvenementContentieux().getParametreEtape()
                    .getEtape().getTypeEtape();
            return APIEtape.RAPPEL.equals(typeEtape);
        } else {
            return false;
        }
    }

    /**
     * Returns the factureAvecMontantMinime.
     *
     * @return boolean
     */
    public boolean isFactureAvecMontantMinime() {
        return factureAvecMontantMinime;
    }

    /**
     * La facture est-elle �chue ?<br/>
     *
     * @return True : Si la date du jour (osiris) ou la date de facturation du passage (musca) + 5 jours OUVRABLES est
     * plus grand que la date d'�ch�ance de la section.
     * @throws Exception
     */
    private boolean isFactureEchue() throws Exception {
        JACalendarGregorian calendar = new JACalendarGregorian();
        JADate dateEcheanceSection = new JADate(sectionCourante.getSection().getDateEcheance());

        JADate dateMax;
        if (getPassage() != null) {
            dateMax = new JADate(getPassage().getDateFacturation());
        } else {
            dateMax = JACalendar.today();
        }

        dateMax = CADateUtil.getDateOuvrable(calendar.addDays(dateMax,
                CAImpressionBulletinsSoldes_Doc.NB_JOURS_DELAI_FACTURE_ECHUE));

        return new JACalendarGregorian().compare(dateMax, dateEcheanceSection) == JACalendar.COMPARE_FIRSTUPPER;
    }

    /**
     * @return the factureMontantReport
     */
    public boolean isFactureMontantReport() {
        return factureMontantReport;
    }

    private boolean isMontantZero() {
        return new FWCurrency(sectionCourante.getSolde()).isZero();
    }

    /**
     * Returns the isMuscaSource.
     *
     * @return boolean
     */
    public boolean isMuscaSource() {
        return isMuscaSource;
    }

    /**
     * La facture est-elle une facture �tudiant ?
     *
     * @return
     */
    private boolean isTypeFactureEtudiant() {
        String typeFacture = "";
        if ((afact != null) && (afact.getIdExterneFactureCompensation().length() > 6)) {
            typeFacture = afact.getIdExterneFactureCompensation().substring(4, 6);
        }

        return CAImpressionBulletinsSoldes_Doc.TYPE_FACTURE_ETUDIANT.equals(typeFacture);
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Charge la section
     *
     * @return la section charg�e
     */
    public CASection loadSection() {
        section = new CASection();
        section.setSession(getSession());
        section.setIdSection(idSection);

        try {
            section.retrieve(getTransaction());
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
        }

        return section;
    }

    /**
     * Charge le compte annexe li�e et la section.
     *
     * @throws Exception
     */
    public CACompteAnnexe loadSectionAndCompteAnnexe() throws Exception {
        if (section == null) {
            loadSection();
        }
        if (compteAnnexe == null) {
            loadSectionCompteAnnexe();
        }

        return compteAnnexe;
    }

    /**
     * Charge le compte annexe li�e � la section.
     *
     * @throws Exception
     */
    public CACompteAnnexe loadSectionCompteAnnexe() throws Exception {
        compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(getSession());
        compteAnnexe.setIdCompteAnnexe(section.getIdCompteAnnexe());
        compteAnnexe.retrieve(getTransaction());

        return compteAnnexe;
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        try {
            if (isMuscaSource()) {
                return _nextElement();
            } else {
                if (factureImpressionNo == 0) {
                    if (section != null) {
                        loadSectionCompteAnnexe();
                        sectionCourante = new CAImpressionBulletinsSoldes_DS(section, getSession(), compteAnnexe
                                .getTiers().getLangueISO());

                        String idRole = sectionCourante.getSection().getCompteAnnexe().getIdRole();
                        if (!idRole.equals(IntRole.ROLE_ADMINISTRATEUR)
                                && sectionCourante.getSection().getCategorieSection()
                                .equals(APISection.ID_CATEGORIE_SECTION_REPARATION_DOMMAGES)) {
                            return false;
                        }

                        super.setDocumentTitle(compteAnnexe.getIdExterneRole() + " - "
                                + compteAnnexe.getTiers().getNom());
                    }
                    factureImpressionNo++;
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
            throw new FWIException(e);
        }
    }

    @Override
    public String getJasperTemplate() {
        return TEMPLATE_FILENAME;
    }

    public void setCallEcran(Boolean callEcran) {
        this.callEcran = callEcran;
    }

    public void setEntityList(ArrayList newEntityList) {
        entityList = newEntityList.iterator();
    }

    public void setEnvoyerGed(Boolean envoyerGed) {
        this.envoyerGed = envoyerGed;
    }

    /**
     * Sets the factureAvecMontantMinime.
     *
     * @param factureAvecMontantMinime The factureAvecMontantMinime to set
     */
    public void setFactureAvecMontantMinime(boolean factureAvecMontantMinime) {
        this.factureAvecMontantMinime = factureAvecMontantMinime;
    }

    /**
     * Sets the factureImpressionNo.
     *
     * @param factureImpressionNo The factureImpressionNo to set
     */
    public void setFactureImpressionNo(int factureImpressionNo) {
        this.factureImpressionNo = factureImpressionNo;
    }

    /**
     * @param factureMontantReport the factureMontantReport to set
     */
    public void setFactureMontantReport(boolean factureMontantReport) {
        this.factureMontantReport = factureMontantReport;
    }

    /**
     * @param forcerBV the forcerBV to set
     */
    public void setForcerBV(Boolean forcerBV) {
        this.forcerBV = forcerBV;
    }

    /**
     * @param eBillPrintable the eBillPrintable to set
     */
    public void setEBillPrintable(Boolean eBillPrintable) {
        this.eBillPrintable = eBillPrintable;
    }

    /**
     * @param idSection the idSection to set
     */
    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    /**
     * Sets the isMuscaSource.
     *
     * @param isMuscaSource The isMuscaSource to set
     */
    public void setIsMuscaSource(boolean isMuscaSource) {
        this.isMuscaSource = isMuscaSource;
    }

    /**
     * @param montantMinimeMax the montantMinimeMax to set
     */
    public void setMontantMinimeMax(String montantMinimeMax) {
        this.montantMinimeMax = montantMinimeMax;
    }

    /**
     * Sets the montantMinimeNeg.
     *
     * @param montantMinimeNeg The montantMinimeNeg to set
     */
    public void setMontantMinimeNeg(String montantMinimeNeg) {
        this.montantMinimeNeg = montantMinimeNeg;
    }

    /**
     * Sets the montantMinimePos.
     *
     * @param montantMinimePos The montantMinimePos to set
     */
    public void setMontantMinimePos(String montantMinimePos) {
        this.montantMinimePos = montantMinimePos;
    }

    /**
     * Sets the passage.
     *
     * @param passage The passage to set
     */
    public void setPassage(FAPassage passage) {
        this.passage = passage;
    }

    /**
     * Sets the journalLibelle.
     *
     * @param sectionLibelle The journalLibelle to set
     */
    public void setSectionLibelle(String sectionLibelle) {
        this.sectionLibelle = sectionLibelle;
    }

    /**
     * La sommation est-elle atteinte ?
     *
     * @return
     * @throws Exception
     */
    private boolean sommationNotReached() throws Exception {
        if (!sectionCourante.getSection().isSectionAuContentieux()) {
            return false;
        }

        if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            String typeEtape = sectionCourante.getSection().getLastEvenementContentieux().getParametreEtape()
                    .getEtape().getTypeEtape();
            return isAncienContentieuxEtapeRappel()
                    || (APIEtape.SOMMATION.equals(typeEtape) && JadeStringUtil.isBlank(sectionCourante.getSection()
                    .getLastEvenementContentieux().getDateExecution()));
        } else {
            return ICOEtape.CS_AUCUNE.equals(sectionCourante.getSection().getIdLastEtatAquila())
                    || ICOEtape.CS_PREMIER_RAPPEL_ENVOYE.equals(sectionCourante.getSection().getIdLastEtatAquila())
                    || ICOEtape.CS_DEUXIEME_RAPPEL_ENVOYE.equals(sectionCourante.getSection().getIdLastEtatAquila());
        }
    }

    private void tableHeader() throws Exception {
        setColumnHeader(
                1,
                getSession().getApplication()
                        .getLabel("IMPR_EXTR_CPT_ANNEXE_COL_8", compteAnnexe.getTiers().getLangueISO()).toUpperCase()); // date
        // comptable
        setColumnHeader(
                2,
                getSession().getApplication()
                        .getLabel("IMPR_EXTR_CPT_ANNEXE_COL_1", compteAnnexe.getTiers().getLangueISO()).toUpperCase()); // date
        // valeur
        setColumnHeader(3, getSession().getApplication().getLabel("CACLIBELLE", compteAnnexe.getTiers().getLangueISO()));
        setColumnHeader(4, getSession().getApplication().getLabel("CACMONTANT", compteAnnexe.getTiers().getLangueISO()));

        String textMontant = "";

        if (new FWCurrency(sectionCourante.getSolde()).isNegative()) {
            if (isFactureAvecMontantMinime()) {
                textMontant = getSession().getApplication().getLabel("CACTEXT_MINIMENEG",
                        compteAnnexe.getTiers().getLangueISO());
            } else {
                // Si on ne dispose pas d'adresse de paiement pour le compte, on affiche un message diff�rent quant � la
                // m�thode dont le montant sera rembours�
                IntAdressePaiement[] listeAdressesPaiement = compteAnnexe.getTiers().getListeAdressesPaiement();
                if ((listeAdressesPaiement == null) || (listeAdressesPaiement.length == 0)) {
                    textMontant = getSession().getApplication().getLabel("CACTEXT_NCR3",
                            compteAnnexe.getTiers().getLangueISO());
                } else {
                    textMontant = getSession().getApplication().getLabel("CACTEXT_NCR",
                            compteAnnexe.getTiers().getLangueISO())
                            + "\n"
                            + getSession().getApplication().getLabel("CACTEXT_NCR2",
                            compteAnnexe.getTiers().getLangueISO());
                }
            }
        } else {
            CAHistoriqueBulletinSoldeManager bsManager = _getHistoriqueBulletinSoldes();

            if (!bsManager.hasErrors() && !bsManager.isEmpty()) {
                textMontant += FWMessageFormat.format(
                        getSession().getApplication().getLabel("CACREPORT", compteAnnexe.getTiers().getLangueISO()),
                        ((CAHistoriqueBulletinSolde) bsManager.getFirstEntity()).getDateHistorique()) + '\n';
            } else {
                textMontant += FWMessageFormat.format(
                        getSession().getApplication().getLabel("CACREPORT", compteAnnexe.getTiers().getLangueISO()),
                        sectionCourante.getSection().getDateSection()) + '\n';
            }

            if (isFactureEchue()) {
                textMontant += getSession().getApplication().getLabel("BULLETINS_SOLDES_MONTANT_ECHU",
                        compteAnnexe.getTiers().getLangueISO())
                        + " ";
            } else {
                textMontant += getSession().getApplication().getLabel("CACTEXT_1",
                        compteAnnexe.getTiers().getLangueISO())
                        + " " + getDelaiPaiement();
            }

        }

        if (isFactureAvecMontantMinime()) {
            textMontant = getSession().getApplication().getLabel("CACTEXT_MINIMEPOS",
                    compteAnnexe.getTiers().getLangueISO());
        }

        if (isFactureMontantReport()) {
            try {
                textMontant = getSession().getApplication().getLabel("MINIMEPOS_REPORTE",
                        compteAnnexe.getTiers().getLangueISO());
            } catch (Exception e1) {
                getMemoryLog().logMessage(e1.getMessage(), FWMessage.ERREUR, this.getClass().getName());
            }
        }

        if (isMontantZero()) {
            textMontant = "";
        }

        FAApplication muscaApp = (FAApplication) GlobazServer.getCurrentSystem().getApplication(
                FAApplication.DEFAULT_APPLICATION_MUSCA);
        super.setParametres(CAImpressionBulletinsSoldes_Param.P_TEXT, textMontant);
        super.setParametres(FAImpressionFacture_Param.P_TEXT1,
                muscaApp.getLabel("FACREPORTMONT", compteAnnexe.getTiers().getLangueISO()));
        super.setParametres(FAImpressionFacture_Param.P_TEXT2,
                muscaApp.getLabel("FACREPORT", compteAnnexe.getTiers().getLangueISO()));
    }

    public boolean wantHeaderOnEachPage() throws FWIException {
        boolean headerOnEachPage = false;
        String headerOnEachPageTxt;
        try {
            headerOnEachPageTxt = getSession().getApplication().getProperty("headerOnEachPage");
        } catch (Exception e) {
            throw new FWIException(e);
        }
        if ("true".equals(headerOnEachPageTxt)) {
            headerOnEachPage = true;
        }
        return headerOnEachPage;
    }

    public Map<PaireIdExterneEBill, List<Map>> getLignesSolde() {
        return lignesSolde;
    }

    public void setLignesSolde(Map<PaireIdExterneEBill, List<Map>> lignesSolde) {
        this.lignesSolde = lignesSolde;
    }

    public Map<PaireIdExterneEBill, String> getReferencesSolde() {
        return referencesSolde;
    }

    public void setReferencesSolde(Map<PaireIdExterneEBill, String> referencesSolde) {
        this.referencesSolde = referencesSolde;
    }
}
