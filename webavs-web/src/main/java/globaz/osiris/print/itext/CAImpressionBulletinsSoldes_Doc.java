package globaz.osiris.print.itext;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.api.ICOEtape;
import globaz.aquila.api.ICOGestionContentieuxExterne;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAModulePassage;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.external.ServicesFacturation;
import globaz.musca.itext.FAImpressionFacture_Param;
import globaz.musca.process.FAImpressionFactureProcess;
import globaz.musca.util.FAUtil;
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
import globaz.osiris.db.historique.CAHistoriqueBulletinSolde;
import globaz.osiris.db.historique.CAHistoriqueBulletinSoldeManager;
import globaz.osiris.db.utils.CAReferenceBVR;
import globaz.osiris.external.IntAdressePaiement;
import globaz.osiris.external.IntRole;
import globaz.osiris.external.IntTiers;
import globaz.osiris.print.itext.list.CADocumentManager;
import globaz.osiris.utils.CAContentieux;
import globaz.osiris.utils.CADateUtil;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.constantes.IConstantes;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class CAImpressionBulletinsSoldes_Doc extends CADocumentManager {

    private static final long serialVersionUID = -5283345749972078174L;
    public final static int DAYS_ADDED = 12;
    private static final int NB_JOURS_DELAI_FACTURE_ECHUE = 5;

    public final static String NUM_REF_INFOROM_BVR_SOLDE = "0100CFA";

    public final static String TEMPLATE_FILENAME = "MUSCA_BVR4BULLSOLD";
    public final static String TYPE_FACTURE_ETUDIANT = "22";

    private String adressePrincipale;
    private FAAfact afact;
    private CAReferenceBVR bvr = null;
    private Boolean callEcran = new Boolean(false);
    private String centimes;
    private CACompteAnnexe compteAnnexe;
    private Iterator<FAAfact> entityList = null;
    private Boolean envoyerGed = new Boolean(false);
    private boolean factureAvecMontantMinime = false;
    private int factureImpressionNo = 0;
    private boolean factureMontantReport = false;
    private Boolean forcerBV = false;
    private String idModule = "";
    private String idSection = "";
    private boolean isMuscaSource = false;
    FAModulePassage module = null;
    private String montantMinimeMax = null;
    private String montantMinimeNeg = null;
    private String montantMinimePos = null;
    private String montantSansCentime;

    private FAPassage passage;

    private CASection section = null;

    private CAImpressionBulletinsSoldes_DS sectionCourante;
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
        for (Iterator it = mgr.iterator(); it.hasNext();) {
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
     * Via le tiers du compte annexe.
     * 
     * @return l'adresse du domaine Facturation de type Courrier
     * @throws Exception
     */
    public String _getAdresseCourrier() throws Exception {
        IntTiers tiers = compteAnnexe.getTiers();
        if (tiers == null) {
            return "";
        } else {
            String domaine = section.getDomaine();
            if (JadeStringUtil.isBlankOrZero(domaine)) {
                domaine = compteAnnexe._getDefaultDomainFromRole();
            }
            return tiers.getAdresseAsString(getDocumentInfo(), section.getTypeAdresse(), domaine,
                    compteAnnexe.getIdExterneRole(), JACalendar.today().toStr("."));
        }
    }

    /**
     * Via le tiers du compte annexe.
     * 
     * @return l'adresse du domaine Standard de type Domicile
     * @throws Exception
     */
    public String _getAdresseDomicile() throws Exception {
        IntTiers tiers = compteAnnexe.getTiers();
        if (tiers == null) {
            return "";
        } else {
            return tiers.getAdresseAsString(getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    IConstantes.CS_APPLICATION_DEFAUT, compteAnnexe.getIdExterneRole(), JACalendar.today().toStr("."));
        }
    }

    /**
     * L'adresse de paiement est l'adresse de courrier
     * 
     * @return l'adresse courrier du domaine facturation sinon l'adresse de domicile du domaine standard
     * @throws Exception
     */
    public String _getAdressePrincipale() throws Exception {
        String result = _getAdresseCourrier();
        if (!JadeStringUtil.isBlank(result)) {
            return result;
        } else {
            return _getAdresseDomicile();
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
     * Le montant de la facture après les compensations.
     * 
     * @return le montant de la section moins le montant de la facture.
     */
    public String _getMontantApresCompensation() {
        FWCurrency montant = new FWCurrency(sectionCourante.getSection().getSolde());
        return JANumberFormatter.fmt(JANumberFormatter.deQuote(montant.toStringFormat()), false, true, false, 2);
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
     * Dans le cas de musca, il y a plusieurs bulletins de soldes à itérer.
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
                    // Si le passage est comptabilisé, on ne tient pas compte du
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
                        // contrôler si le solde Osiris est supérieur à la
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
                            // le solde Osiris - compensation est inférieur ou
                            // égal à 0
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
     * Ajoute l'envois du bulletin de soldes à l'historique de la section.
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
    }

    /**
     * Récupère les informations du décompte avant impression.
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

    /**
     * La facture est-elle une facture avec montant minime ?<br/>
     * Sauvegarde l'information dans une variable de class.<br/>
     * 
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
                if (isMuscaSource && JadeStringUtil.isEmpty(getIdSection())) {
                    setIdSection(sectionCourante.getSection().getIdSection());
                    loadSection();
                }

                List data = _buildLignes();

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

                decalerEcheanceContencieux();

                tableHeader();
                fillBVR();

                addToHistory();
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
                throw new FWIException(e);
            }
        }
    }

    /**
     * Décale l'échéance du contentieux si l'étape de sommation n'est pas encore atteinte.<br/>
     * Si la section n'est pas au contentieux, cette dernière y sera préablement ajoutée.
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
     * Recherche le compte annexe pour le rôle et l'idexterne.
     * 
     * @return
     */
    private CACompteAnnexeManager findCompteAnnexe() throws Exception {
        CACompteAnnexeManager manager = new CACompteAnnexeManager();
        manager.setSession(getSessionOsiris());

        // On contrôle si le idRoleDebiteurCompensation de l'afact n'est pas
        // vide on le prend sinon on prend celui de l'entête
        if (!JadeStringUtil.isBlankOrZero(afact.getIdRoleDebiteurCompensation())) {
            manager.setForIdRole(afact.getIdRoleDebiteurCompensation());
        } else {
            manager.setForIdRole(afact.getEnteteFacture().getIdRole());
        }
        // On contrôle si le idRoleDebiteurCompensation de l'afact n'est pas
        // vide on le prend sinon on prend celui de l'entête
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
     * Renvoie la référence BVR.
     * 
     * @return la référence BVR.
     */
    public CAReferenceBVR getBvr() {
        if (bvr == null) {
            bvr = new CAReferenceBVR();
        }
        return bvr;
    }

    public Boolean getCallEcran() {
        return callEcran;
    }

    /**
     * Calcule le delai de paiement Base = max(dateFacturation + 12j, date d'échéance de la section) <br>
     * 
     * @author: sel Créé le : 10 nov. 06
     * @return Date de délai de paiement
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
            // le process s'est mal déroulé
            if (isMuscaSource) {
                return FWMessageFormat.format(getSession().getLabel("SOLDE_EMAIL_PASS_PASOK"), passage.getIdPassage());
            } else {
                return FWMessageFormat.format(getSession().getLabel("SOLDE_EMAIL_SEC_PASOK"), section.getIdExterne());
            }
        } else {
            // le process s'est bien déroulé
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
     * En mode ancien contentieux, est-on à l'étape rappel ?
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
     * La facture est-elle échue ?<br/>
     * 
     * @return True : Si la date du jour (osiris) ou la date de facturation du passage (musca) + 5 jours OUVRABLES est
     *         plus grand que la date d'échéance de la section.
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
     * La facture est-elle une facture étudiant ?
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
     * @return la section chargée
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
     * Charge le compte annexe liée à la section.
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
     * @param factureAvecMontantMinime
     *            The factureAvecMontantMinime to set
     */
    public void setFactureAvecMontantMinime(boolean factureAvecMontantMinime) {
        this.factureAvecMontantMinime = factureAvecMontantMinime;
    }

    /**
     * Sets the factureImpressionNo.
     * 
     * @param factureImpressionNo
     *            The factureImpressionNo to set
     */
    public void setFactureImpressionNo(int factureImpressionNo) {
        this.factureImpressionNo = factureImpressionNo;
    }

    /**
     * @param factureMontantReport
     *            the factureMontantReport to set
     */
    public void setFactureMontantReport(boolean factureMontantReport) {
        this.factureMontantReport = factureMontantReport;
    }

    /**
     * @param forcerBV
     *            the forcerBV to set
     */
    public void setForcerBV(Boolean forcerBV) {
        this.forcerBV = forcerBV;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    /**
     * Sets the isMuscaSource.
     * 
     * @param isMuscaSource
     *            The isMuscaSource to set
     */
    public void setIsMuscaSource(boolean isMuscaSource) {
        this.isMuscaSource = isMuscaSource;
    }

    /**
     * @param montantMinimeMax
     *            the montantMinimeMax to set
     */
    public void setMontantMinimeMax(String montantMinimeMax) {
        this.montantMinimeMax = montantMinimeMax;
    }

    /**
     * Sets the montantMinimeNeg.
     * 
     * @param montantMinimeNeg
     *            The montantMinimeNeg to set
     */
    public void setMontantMinimeNeg(String montantMinimeNeg) {
        this.montantMinimeNeg = montantMinimeNeg;
    }

    /**
     * Sets the montantMinimePos.
     * 
     * @param montantMinimePos
     *            The montantMinimePos to set
     */
    public void setMontantMinimePos(String montantMinimePos) {
        this.montantMinimePos = montantMinimePos;
    }

    /**
     * Sets the passage.
     * 
     * @param passage
     *            The passage to set
     */
    public void setPassage(FAPassage passage) {
        this.passage = passage;
    }

    /**
     * Sets the journalLibelle.
     * 
     * @param journalLibelle
     *            The journalLibelle to set
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
                // Si on ne dispose pas d'adresse de paiement pour le compte, on affiche un message différent quant à la
                // méthode dont le montant sera remboursé
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
}
