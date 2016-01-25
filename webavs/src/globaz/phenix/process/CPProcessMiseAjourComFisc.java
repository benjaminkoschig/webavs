package globaz.phenix.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPCommunicationFiscale;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichage;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichageManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.divers.CPParametreCanton;
import globaz.phenix.db.divers.CPPeriodeFiscale;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliation;
import globaz.phenix.db.principale.CPDecisionAffiliationManager;
import globaz.phenix.db.principale.CPDecisionManager;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.api.ITIAdministration;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Insérez la description du type ici. Date de création : (18.11.2004 14:00:00)
 * 
 * @author: acr
 */
public class CPProcessMiseAjourComFisc extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public class DocumentExcel {
        private String date_impression = "";
        private String nameFile = "";
        private BProcess process = null;
        private HSSFSheet sheet;
        private String titre = "";
        private String user = "";
        private HSSFWorkbook wb;

        /**
         * Commentaire relatif au constructeur TentDocument.
         */
        public DocumentExcel() {
            wb = new HSSFWorkbook();
            sheet = wb.createSheet("TENT");
        }

        /**
         * Commentaire relatif au constructeur TentDocument.
         */
        public DocumentExcel(String sheetTitle) {
            wb = new HSSFWorkbook();
            titre = sheetTitle;
            sheet = wb.createSheet(sheetTitle);
            setDate_impression(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
            setUser(getSession().getUserFullName());
            HSSFPrintSetup ps = sheet.getPrintSetup();
            // format
            ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
            // orientation
            ps.setLandscape(true);
            // marges en-tête/pied de page
            ps.setHeaderMargin(0);
            ps.setFooterMargin(0);
            sheet = setTitleRow(wb, sheet);
        }

        /**
         * @return
         */
        public String getDate_impression() {
            return date_impression;
        }

        /**
         * @return
         */
        public String getNameFile() {
            return nameFile;
        }

        public String getOutputFile() {
            try {
                File f = File.createTempFile(getNameFile(), ".xls");
                f.deleteOnExit();
                FileOutputStream out = new FileOutputStream(f);
                wb.write(out);
                out.close();
                return f.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace(System.err);
                return "";
            }
        }

        /**
         * @return
         */
        public BProcess getProcess() {
            return process;
        }

        /**
         * @return
         */
        public String getTitre() {
            return titre;
        }

        /**
         * @return
         */
        public String getUser() {
            return user;
        }

        HSSFSheet populateSheet(ArrayList<CPDecision> data) {
            HSSFCellStyle style = wb.createCellStyle();
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            // definition de la taille des cell
            sheet.setColumnWidth((short) 0, (short) 4000);
            sheet.setColumnWidth((short) 1, (short) 5000);
            sheet.setColumnWidth((short) 2, (short) 3500);
            sheet.setColumnWidth((short) 3, (short) 1500);
            sheet.setColumnWidth((short) 4, (short) 3500);
            sheet.setColumnWidth((short) 5, (short) 3500);
            sheet.setColumnWidth((short) 6, (short) 3500);
            sheet.setColumnWidth((short) 7, (short) 3500);
            sheet.setColumnWidth((short) 8, (short) 3500);
            sheet.setColumnWidth((short) 9, (short) 3500);
            // sheet.setColumnWidth((short)10, (short)2400);

            try {
                CPDecisionAffiliationManager decisionManager = new CPDecisionAffiliationManager();
                decisionManager.setSession(getSession());
                if (process == null) {
                    throw new Exception("populateSheet(ArrayList data):Pas de process!");
                }
                process.setProgressScaleValue(data.size());
                for (int i = 0; i < data.size(); i++) {
                    decisionManager.setForIdDecision((data.get(i)).getIdDecision());
                    decisionManager.find(getTransaction());
                    if (decisionManager.isEmpty()) {
                        throw new Exception("Aucune décision trouvée pour l'id: " + (data.get(i)).getIdDecision());
                    }
                    CPDecisionAffiliation decision = (CPDecisionAffiliation) decisionManager.getFirstEntity();
                    CPPeriodeFiscale perfis = new CPPeriodeFiscale();
                    perfis.setSession(getSession());
                    perfis.setIdIfd(decision.getIdIfdDefinitif());
                    perfis.retrieve(getTransaction());
                    if ((perfis == null) || perfis.isNew()) {
                        throw new Exception("Aucune période fiscale pour l'id ifd: " + decision.getIdIfdDefinitif());
                    }
                    // /////////
                    HSSFRow row = sheet.createRow(sheet.getPhysicalNumberOfRows());
                    int colNum = 0;
                    // numéro affilié
                    AFAffiliation affi = new AFAffiliation();
                    affi.setSession(getSession());
                    affi.setAffiliationId(decision.getIdAffiliation());
                    affi.retrieve();
                    HSSFCell cell = row.createCell((short) colNum++);
                    cell.setCellValue(new HSSFRichTextString(affi.getAffilieNumero()));
                    // nom, prénom
                    cell = row.createCell((short) colNum++);
                    TITiersViewBean tiers = new TITiersViewBean();
                    tiers.setSession(getSession());
                    tiers.setIdTiers(decision.getIdTiers());
                    tiers.retrieve();
                    cell.setCellValue(new HSSFRichTextString(tiers.getNomPrenom()));
                    // genre
                    cell = row.createCell((short) colNum++);
                    cell.setCellValue(new HSSFRichTextString(getSession().getCodeLibelle(decision.getGenreAffilie())));
                    // num ifd
                    cell = row.createCell((short) colNum++);
                    cell.setCellValue(new HSSFRichTextString(perfis.getNumIfd()));
                    // année décision
                    cell = row.createCell((short) colNum++);
                    cell.setCellValue(new HSSFRichTextString(decision.getAnneeDecision()));
                    // type décision
                    cell = row.createCell((short) colNum++);
                    cell.setCellValue(new HSSFRichTextString(getSession().getCodeLibelle(decision.getTypeDecision())));
                    // début décision
                    cell = row.createCell((short) colNum++);
                    cell.setCellValue(new HSSFRichTextString(decision.getDebutDecision()));
                    // fin décision
                    cell = row.createCell((short) colNum++);
                    cell.setCellValue(new HSSFRichTextString(decision.getFinDecision()));
                    // date décision
                    cell = row.createCell((short) colNum++);
                    cell.setCellValue(new HSSFRichTextString(decision.getDateInformation()));
                    // Canton
                    // cell = row.createCell((short)colNum++);
                    // cell.setCellValue(tiers.getCanton());
                    process.incProgressCounter();
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "populateSheet");
            }
            HSSFFooter footer = sheet.getFooter();
            footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8)
                    + "Ref: 0085CCP");
            return sheet;
        }

        /**
         * @param string
         */
        public void setDate_impression(String string) {
            date_impression = string;
        }

        /**
         * @param string
         */
        public void setNameFile(String string) {
            nameFile = string;
        }

        /**
         * @param process
         */
        public void setProcess(BProcess process) {
            this.process = process;
        }

        /**
         * @param wb
         * @param sheet
         * @param row
         * @return
         */
        private HSSFSheet setTitleRow(HSSFWorkbook wb, HSSFSheet sheet) {
            final String[] COL_TITLES = { getSession().getLabel("NUM_AFFILIE"), getSession().getLabel("NOM_PRENOM"),
                    getSession().getLabel("GENRE_AFFILIE"), getSession().getLabel("IFD"),
                    getSession().getLabel("ANNEE"), getSession().getLabel("TYPE_DECISION"),
                    getSession().getLabel("DEBUT_DECISION"), getSession().getLabel("FIN_DECISION"),
                    getSession().getLabel("DATE_INFO")
            // ,getSession().getLabel("CANTON")
            };
            HSSFRow row = null;
            HSSFCell c;
            // création du style pour le titre de la page
            HSSFFont font2 = wb.createFont();
            font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
            font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
            font2.setFontHeight((short) 200);
            font2.setColor(HSSFFont.COLOR_NORMAL);
            HSSFCellStyle style2 = wb.createCellStyle();
            style2.setFont(font2);
            row = sheet.createRow(0);
            c = row.createCell((short) 0);
            try {
                c.setCellValue(new HSSFRichTextString(getProcess().getSession().getApplication()
                        .getProperty("COMPANYNAME_" + getProcess().getSession().getIdLangueISO().toUpperCase())));
            } catch (Exception e) {
                c.setCellValue(new HSSFRichTextString(""));
            }
            c.setCellStyle(style2);
            row = sheet.createRow(1);
            row = sheet.createRow(2);
            c = row.createCell((short) 2);
            c.setCellValue(new HSSFRichTextString(getTitre()));
            c.setCellStyle(style2);
            // critères de sélection
            HSSFCellStyle style3 = wb.createCellStyle();
            HSSFFont font3 = wb.createFont();
            font3.setFontName(HSSFFont.FONT_ARIAL); // arial font
            font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
            font3.setFontHeight((short) 200);
            style3.setFont(font3);
            row = sheet.createRow(3);
            row = sheet.createRow(4);
            // création de l'entête
            try {
                row = sheet.createRow(0);
                c = row.createCell((short) 0);
                try {
                    c.setCellValue(new HSSFRichTextString(getSession().getApplication().getProperty(
                            "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase())));
                } catch (Exception e) {
                    c.setCellValue(new HSSFRichTextString(""));
                }
                c.setCellStyle(style2);
                row = sheet.createRow(1);
                row = sheet.createRow(2);
                c = row.createCell((short) 2);
                c.setCellValue(new HSSFRichTextString(titre));
                c.setCellStyle(style2);
                row = sheet.createRow(5);
                c = row.createCell((short) 0);
                c.setCellValue(new HSSFRichTextString(""));
                c.setCellStyle(style3);
                c = row.createCell((short) 1);
                c.setCellValue(new HSSFRichTextString(getSession().getLabel("DATE_IMPRESSION")));
                c.setCellStyle(style3);
                c = row.createCell((short) 3);
                c.setCellValue(new HSSFRichTextString(getDate_impression()));
                c.setCellStyle(style3);
                row = sheet.createRow(6);
                c = row.createCell((short) 0);
                c.setCellValue(new HSSFRichTextString(""));
                c = row.createCell((short) 1);
                c.setCellValue(new HSSFRichTextString(getSession().getLabel("ANNEE")));
                c.setCellStyle(style3);
                c = row.createCell((short) 3);
                c.setCellValue(new HSSFRichTextString(getAnneeDecision()));
                c.setCellStyle(style3);
                row = sheet.createRow(7);
                c = row.createCell((short) 0);
                c.setCellValue(new HSSFRichTextString(""));
                c.setCellStyle(style3);
                c = row.createCell((short) 1);
                c.setCellValue(new HSSFRichTextString(getSession().getLabel("USER")));
                c.setCellStyle(style3);
                c = row.createCell((short) 3);
                c.setCellValue(new HSSFRichTextString(getUser()));
                c.setCellStyle(style3);
            } catch (Exception e) {
                CPProcessMiseAjourComFisc.this._addError(getTransaction(),
                        "Exception dans la méthode setTitleRow de la class CPProcessMiseAjourComFisc.java");
            }
            // let's use a nifty font for the title
            HSSFCellStyle style = wb.createCellStyle();
            style.setWrapText(true);
            style.setFont(font2);
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
            // ??? style.setFillBackgroundColor((short) 150);
            style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            style.setBorderRight(HSSFCellStyle.BORDER_THICK);
            style.setBorderTop(HSSFCellStyle.BORDER_THICK);
            // create Title Row
            row = sheet.createRow(8);
            row = sheet.createRow(9);
            for (int i = 0; i < COL_TITLES.length; i++) {
                // set cell value
                c = row.createCell((short) i);
                c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
                c.setCellStyle(style);
            }
            return sheet;
        }

        /**
         * @param string
         */
        public void setTitre(String string) {
            titre = string;
        }

        /**
         * @param string
         */
        public void setUser(String string) {
            user = string;
        }
    }

    private AFAffiliation affiliation = null;
    private String anneeDecision = "";
    private java.lang.String forGenreAffilie = "";
    private java.lang.String fromAffilieDebut = "";
    private java.lang.String fromAffilieFin = "";
    public boolean historiqueCommunication = true;
    private ArrayList<String> listIdJournalRetour = new ArrayList<String>();
    private Boolean simulation = new Boolean(true);

    public CPProcessMiseAjourComFisc() {
        super();
    }

    public CPProcessMiseAjourComFisc(BProcess parent) {
        super(parent);
    }

    public CPProcessMiseAjourComFisc(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        boolean resultat = true;
        try {
            historiqueCommunication = ((CPApplication) getSession().getApplication()).isHistoriqueCommunication();
            // Création et suppression des communications fiscales
            // en fonction de la dernière decision d'une année
            creationSuppressionComFis();
            // Mise à jour de l'état du journal
            majJournalRetour();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, "CPProcessMiseAjourComFisc");
            resultat = false;
        }
        return resultat;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        // divers :
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        // L'année de décision doit être saisie;
        if (JadeStringUtil.isIntegerEmpty(getAnneeDecision())) {
            getSession().addError(getSession().getLabel("DECISION_INVALIDE"));
        }
        // Contrôle du mail
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            getSession().addError(getSession().getLabel("CP_MSG_0145"));
        }

        // Les erreurs sont ajoutées à la session,
        // abort permet l'arrêt du process
        if (getSession().hasErrors()) {
            abort();
        }
    }

    private void creationListeControle(String sheetTitle, String fileName, ArrayList<CPDecision> liste)
            throws IOException {
        DocumentExcel excelDocIfd = new DocumentExcel(sheetTitle);
        excelDocIfd.setNameFile(fileName);
        setState(sheetTitle);
        excelDocIfd.setTitre(sheetTitle);
        excelDocIfd.setProcess(this);
        excelDocIfd.populateSheet(liste);
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setDocumentType("0085CCP");
        docInfo.setDocumentTypeNumber("");
        this.registerAttachedDocument(docInfo, excelDocIfd.getOutputFile());
    }

    private void creationSuppressionComFis() throws Exception {
        BStatement statement = null;
        BTransaction transactionCurseur = null;
        CPDecisionAffiliation decAff = null;
        CPDecision dec = null;
        CPDecisionAffiliationManager decMng = null;
        ArrayList<CPDecision> listeComFisCreees = null;
        ArrayList<CPDecision> listeComFisSupprimees = null;
        try {
            listeComFisCreees = new ArrayList<CPDecision>();
            listeComFisSupprimees = new ArrayList<CPDecision>();
            // Rechercher de tous les tiers qui ont une décision comptabilisée
            // ou sortie pour une année donnée regroupé par affiliation et trié
            // par tiers.
            decMng = new CPDecisionAffiliationManager();
            decMng.setSession(getSession());
            decMng.setForExceptTypeDecision(CPDecision.CS_IMPUTATION);
            decMng.setForAnneeDecision(getAnneeDecision());
            // PO 9273
            decMng.setInEtat(CPDecision.CS_FACTURATION + ", " + CPDecision.CS_PB_COMPTABILISATION + ", "
                    + CPDecision.CS_REPRISE + ", " + CPDecision.CS_SORTIE);
            if (!JadeStringUtil.isEmpty(getFromAffilieDebut())) {
                decMng.setFromNoAffilie(getFromAffilieDebut());
            }
            if (!JadeStringUtil.isEmpty(getFromAffilieFin())) {
                decMng.setTillNoAffilie(getFromAffilieFin());
            }
            // SI Indépendant de sélectionné => IND, TSE, AGR, REN donc
            // différtent de NON ACTIF...
            if (!JadeStringUtil.isEmpty(getForGenreAffilie())) {
                if (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(getForGenreAffilie())) {
                    decMng.setNotInGenreAffilie(CPDecision.CS_NON_ACTIF + ", " + CPDecision.CS_ETUDIANT);
                } else {
                    decMng.setInGenreAffilie(CPDecision.CS_NON_ACTIF + ", " + CPDecision.CS_ETUDIANT);
                }
            }
            // decMng.setIsActive(Boolean.TRUE);
            decMng.orderByNoAffilie();
            decMng.orderByIdDecisionDesc();
            setState(getSession().getLabel("MAJ_COMFIS"));
            setProgressScaleValue(decMng.getCount(getTransaction()));
            transactionCurseur = new BTransaction(getSession());
            transactionCurseur.getSession().newTransaction();
            transactionCurseur.openTransaction();
            // ouverture du curseur
            statement = decMng.cursorOpen(transactionCurseur);
            String saveNumAffilie = "";
            String saveAnnee = "";
            while (((decAff = (CPDecisionAffiliation) decMng.cursorReadNext(statement)) != null) && (!decAff.isNew())) {
                if (!saveAnnee.equalsIgnoreCase(decAff.getAnneeDecision())
                        || !saveNumAffilie.equalsIgnoreCase(decAff.getNumAffilie())) {
                    try {
                        // Traité une fois par année, idaffiliation
                        saveAnnee = decAff.getAnneeDecision();
                        saveNumAffilie = decAff.getNumAffilie();

                        dec = new CPDecision();
                        dec.setSession(getSession());
                        dec.setIdDecision(decAff.getIdDecision());
                        dec.retrieve();

                        String idJrn = majCommunicationFiscale(dec, true, listeComFisCreees, listeComFisSupprimees);
                        // Sauvegarde de l'id journal
                        if (!JadeStringUtil.isEmpty(idJrn)) {
                            Iterator<String> iter = listIdJournalRetour.iterator();
                            // Insérer le journal si il n'existe pas
                            boolean ok = true;
                            while (iter.hasNext() && ok) {
                                String element = iter.next();
                                if (element.equals(idJrn)) {
                                    ok = false;
                                }
                            }
                            // Stockage de l'id journal à traiter
                            if (ok) {
                                listIdJournalRetour.add(idJrn);
                            }
                        }

                    } catch (Exception e) {
                        if (dec == null) {
                            getMemoryLog().logMessage("Pas de décision: " + e.getMessage(), FWViewBeanInterface.ERROR,
                                    "creationSuppressionComFis() 1");
                        } else {
                            getMemoryLog().logMessage(dec.toString() + ": " + e.getMessage(),
                                    FWViewBeanInterface.ERROR, "creationSuppressionComFis() 1");
                            getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, "");
                        }
                    }
                    // Commit/rollback
                    if (getTransaction().hasErrors()) {
                        if (dec == null) {
                            getMemoryLog().logMessage("Pas de décision: " + getTransaction().getErrors().toString(),
                                    FWViewBeanInterface.ERROR, "creationSuppressionComFis() 2");
                        } else {
                            getMemoryLog().logMessage(dec.toString() + ": ", FWViewBeanInterface.ERROR,
                                    "creationSuppressionComFis() 2");
                            getMemoryLog().logMessage(getTransaction().getErrors().toString(),
                                    FWViewBeanInterface.ERROR, "");
                        }
                        getTransaction().rollback();
                        getTransaction().clearErrorBuffer();
                    } else {
                        getTransaction().commit();
                    }
                }
                dec = null;
                incProgressCounter();
            }
            // Création du document
            if (listeComFisCreees != null) {
                if (!listeComFisCreees.isEmpty()) {
                    String titre = getSession().getLabel("LIST_COMFIS_CREEES");
                    creationListeControle(titre, titre, listeComFisCreees);
                }
            }
            // Création du document
            if (listeComFisSupprimees != null) {
                if (!listeComFisSupprimees.isEmpty()) {
                    String titre = getSession().getLabel("LIST_COMFIS_SUPPRIMEES");
                    creationListeControle(titre, titre, listeComFisSupprimees);
                }
            }
            // PO 6473
            if (((listeComFisSupprimees != null) || !listeComFisSupprimees.isEmpty())
                    && ((listeComFisSupprimees != null) || !listeComFisSupprimees.isEmpty())) {
                getMemoryLog().logMessage(getSession().getLabel("CP_COMFIS_MAJ"), FWViewBeanInterface.WARNING, "");
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    try {
                        decMng.cursorClose(statement);
                    } catch (Exception e) {
                        JadeLogger.warn(this, "Problem in cursorClose()");
                    } finally {
                        try {
                            statement.closeStatement();
                        } catch (Exception e) {
                            JadeLogger.warn(this, "Problem in closeStatement()");
                        }
                    }
                }
            } finally {
                if (transactionCurseur != null) {
                    try {
                        transactionCurseur.closeTransaction();
                        transactionCurseur = null;
                    } catch (Exception e) {
                        JadeLogger.warn(this, "Problem in cursorClose()");
                    }
                }
            }
            // Mettre les objets inutilsés à null pour libérer la mémoire
            decMng = null;
        }
    }

    /**
     * @return
     */
    public String getAnneeDecision() {
        return anneeDecision;
    }

    @Override
    protected java.lang.String getEMailObject() {
        if (!isAborted() && !isOnError()) {
            return getSession().getLabel("OBJEMAIL_MAJ_COMFISOK") + " " + anneeDecision;
        } else {
            return getSession().getLabel("OBJEMAIL_MAJ_COMFISKO") + " " + anneeDecision;
        }
    }

    public java.lang.String getForGenreAffilie() {
        return forGenreAffilie;
    }

    public java.lang.String getFromAffilieDebut() {
        return fromAffilieDebut;
    }

    public java.lang.String getFromAffilieFin() {
        return fromAffilieFin;
    }

    /**
     * @return
     */
    public Boolean getSimulation() {
        return simulation;
    }

    /**
     * @return
     */
    public boolean isHistoriqueCommunication() {
        return historiqueCommunication;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public CPDecision loadDecisionMari(CPDecision decision) throws Exception {
        CPDecision decisionMari = null;
        CPDecisionManager decisionMariMng = new CPDecisionManager();
        decisionMariMng.setSession(getSession());
        decisionMariMng.setForIdTiers(decision.getIdConjoint());
        decisionMariMng.setForAnneeDecision(decision.getAnneeDecision());
        decisionMariMng.orderByDateDecision();
        decisionMariMng.orderByIdDecision();
        decisionMariMng.find(getTransaction());
        if (decisionMariMng.size() >= 1) {
            decisionMari = (CPDecision) decisionMariMng.getFirstEntity();
        }
        return decisionMari;
    }

    /**
     * Mise à jour des communication fiscales Ajout si c'est une décision de type provisoire et qu'il en existe pas déjà
     * une pour l'affilié Suppression si c'est une décision de type définitive et dans l'état sortie. Date de création :
     * (17.03.2004)
     * 
     * @param process
     *            BProcess le processus d'exécution
     * @return idjournal - Utile pour mettre l'état du journal pour les décisions générées à partir du fichier du fisc.
     */
    public String majCommunicationFiscale(CPDecision decision, boolean demandeCreation,
            ArrayList<CPDecision> listeComFisCreees, ArrayList<CPDecision> listeComFisSupprimees) {
        try {
            // Si la décision est de type provisoire et qu'il y a une
            // communication fiscale reçu, mais non traitée, on ne fait rien.
            boolean isProvisoireCommunicationNonTraite = testerCommunicationEnRetourNonTraitee(decision);
            if (!isProvisoireCommunicationNonTraite) {
                // variable d'aide
                boolean creation = false;
                String idJrn = "";
                // Recherche si particularité d'affiliation= Pas d'envoi
                // communication fiscale
                if (AFParticulariteAffiliation.existeParticularite(getTransaction(), decision.getIdAffiliation(),
                        CodeSystem.PARTIC_AFFILIE_SANS_COMM_FISC, decision.getDebutDecision())
                        || CPDecision.CS_SALARIE_DISPENSE.equals(decision.getSpecification())) {
                    demandeCreation = false;
                }
                // Les demandes sont créées uniquement pour les décisions
                // provisoires qui n'on pas l'état sortie ()
                // (provisoire, estimation, notification, notification
                // complémentaire, compémentaire et reprise annuelle)
                if (decision.isProvisoireMetier() && demandeCreation) {
                    // Par défaut, on met à true si c'est une décision
                    // provisoire
                    creation = true;
                    // Recherche de l'affiliation qui concerne la décision -
                    // Lecture seulement si différente de la précédente
                    if ((affiliation == null)
                            || !decision.getIdAffiliation().equalsIgnoreCase(affiliation.getAffiliationId())) {
                        affiliation = new AFAffiliation();
                        affiliation.setSession(getSession());
                        affiliation.setAffiliationId(decision.getIdAffiliation());
                        affiliation.retrieve(getTransaction());
                        if ((affiliation == null) || affiliation.isNew()) {
                            throw new Exception("La décision n'a pas d'affiliation.");
                        }
                    }
                    // Si l'affiliation a une date de fin inférieure ou égale à la date de début de la décision,
                    // il ne faut pas créer de demande de communication fiscale
                    if (!JAUtil.isDateEmpty(affiliation.getDateFin())
                            && (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), affiliation.getDateFin(),
                                    decision.getDebutDecision()))) {
                        creation = false;
                    }
                    if (creation
                            && BSessionUtil.compareDateFirstGreater(getSession(), affiliation.getDateDebut(),
                                    decision.getFinDecision())) {
                        creation = false;
                    }
                    // Si le tiers est retraité et que la décision commence après la date de retraite,
                    // il ne faut pas créer de demande de comm. fis
                    if (creation
                            && decision.isNonActif()
                            && (BSessionUtil.compareDateFirstGreater(getSession(), decision.getDebutDecision(),
                                    decision.loadTiers().getDateAvs()))) {
                        creation = false;
                    }
                }
                // Création d'une demande
                if (creation) {
                    String codeCanton = rechercheCodeCanton(decision);
                    if (JadeStringUtil.isEmpty(codeCanton)) {
                        throw new Exception(getSession().getLabel("CP_MSG_0206"));
                    }
                    // variable d'aide. Permet de savoir s'il faut créer une com// fis
                    boolean creerCommfisc = true;
                    // Si la décision du tiers a un conjoint et que le tiers est une femme,
                    // on ne crée pas la communication fiscale (sauf dans les cas de radiation de l'affiliation du
                    // mari) mais on va chercher l'idcommunication du mari.

                    // Cette règle n'est pas valable pour les cantons qui n'apploiquent pas les règles de Sedex et qui
                    // traite les conjoints séparément (ex: GE)

                    // Recherche du mode de fonctionnenment du canton
                    String modeEnvoiSedex = CPParametreCanton.findCodeWhitTypeAndCanton(getSession(), codeCanton,
                            CPParametreCanton.CS_MODE_ENVOI_SEDEX, "31.12." + decision.getAnneeDecision());

                    if (JadeStringUtil.isBlankOrZero(modeEnvoiSedex)) {
                        modeEnvoiSedex = CPParametreCanton.CS_SEDEXENVOI_DIRECTIVE;
                    }
                    if (!JadeStringUtil.isIntegerEmpty(decision.getIdConjoint())
                            && !CPParametreCanton.CS_SEDEXENVOI_CJT_SEPARE.equalsIgnoreCase(modeEnvoiSedex)) {
                        if (!decision.isNonActif()
                                && (modeEnvoiSedex.equalsIgnoreCase(CPParametreCanton.CS_SEDEXENVOI_CJT_IND_SEPARE) || modeEnvoiSedex
                                        .equalsIgnoreCase(CPParametreCanton.CS_SEDEXENVOI_MODE_VD))) {
                            creerCommfisc = true;
                        } else if (decision.loadTiers().getSexe().equals(TITiersViewBean.CS_FEMME)) {
                            // Recherche de la dernière décision du mari pas
                            // nécessairement comptabilisée
                            CPDecision decisionMari = loadDecisionMari(decision);
                            // Si le mari a une date de fin d'affiliation inférieure à l'année à traiter,
                            // il faut créer une communication fiscale pour la femme.
                            // On part du principe qu'il n'y a pas d'affiliations qui se chevauchent ce qui signifie
                            // que l'on traite avec la première affiliation que l'on trouve pour l'année
                            AFAffiliationManager affiliationMariMng = new AFAffiliationManager();
                            affiliationMariMng.setSession(getSession());
                            affiliationMariMng.setForIdTiers(decision.getIdConjoint());
                            affiliationMariMng.setForTypesAffPersonelles();
                            if (decisionMari == null) {
                                // Si pas de décision pour le mari, recherche d'une affiliation ouverte
                                // dont la date de fin est supérieure au 01.01 de l'année de la décision de la femme
                                // et dont la date de début est inférieurs ou égale au 31.12 de l'année de la décision
                                // de la femme.
                                affiliationMariMng.setFromDateFin("01.01." + decision.getAnneeDecision());
                                affiliationMariMng.setForTillDateDebut("31.12." + decision.getAnneeDecision());
                            } else {
                                // Sinon, recherche d'une affiliation (en fonction de l'id affiliation du mari)
                                // ouverte dont la date de fin est supérieure au début de la décision du mari
                                affiliationMariMng.setFromDateFin(decisionMari.getDebutDecision());
                                affiliationMariMng.setForTillDateDebut("31.12." + decision.getAnneeDecision());
                                affiliationMariMng.setForAffiliationId(decisionMari.getIdAffiliation());
                            }
                            affiliationMariMng.find(getTransaction());
                            if (affiliationMariMng.size() >= 1) {
                                AFAffiliation affiliationMari = (AFAffiliation) affiliationMariMng.getFirstEntity();
                                // Si la date de début de l'affiliation est égale à la date de fin de l'affiliation
                                // c'est que c'est un affiliation par erreur, donc qu'il ne faut pas créer de demande
                                // pour le mari
                                if (!affiliationMari.getDateDebut().equals(affiliationMari.getDateFin())) {
                                    // Pour les non-actif, contrôler les cas de retraite
                                    if (affiliationMari.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_NON_ACTIF)) {
                                        // Si le mari est retraité, il faut créer une communication fiscale pour
                                        // la femme --> seulement pour les NA
                                        String dateAvs = "";
                                        String dateCritere = "";
                                        if (decisionMari == null) {
                                            // Si le mari n'a pas de décision,
                                            dateAvs = decision.loadTiers().getConjoint().getDateAvs();
                                            // date de fin de l'affiliation
                                            if (!JAUtil.isDateEmpty(affiliationMari.getDateFin())) {
                                                dateCritere = affiliationMari.getDateFin();
                                            } else {
                                                if (JACalendar.getYear(affiliationMari.getDateDebut()) > Integer
                                                        .parseInt(decision.getAnneeDecision())) {
                                                    dateCritere = "31.12.9999";
                                                } else {
                                                    dateCritere = "01.01." + decision.getAnneeDecision();
                                                }
                                            }
                                        } else {
                                            // si le mari a une décision
                                            dateAvs = decisionMari.loadTiers().getDateAvs();
                                            // date de début de la décision du mari.
                                            dateCritere = decisionMari.getDebutDecision();
                                        }
                                        if ((BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dateCritere,
                                                dateAvs))
                                                && (decisionMari != null)
                                                && !decisionMari.isNew()
                                                && decisionMari.isNonActif()) {
                                            creerCommfisc = false;
                                        }
                                    } else {
                                        // 19.05.2010: Pour VS, ne pas créer de comm. fis. pour conjoint femme
                                        // indépendant
                                        // Pour les autres genres, le contrôle de la date de fin de l'affiliation est
                                        // suffisante.
                                        if (modeEnvoiSedex.equalsIgnoreCase(CPParametreCanton.CS_SEDEXENVOI_DIRECTIVE) == true) {
                                            creerCommfisc = false;
                                        } else {
                                            creerCommfisc = true;
                                        }
                                    }
                                    // Si on ne crée pas de demande de com fis pour la femme,
                                    // => rechercher si il n'y a pas une communication à supprimer
                                    if (!creerCommfisc) {
                                        // Suppression de la demande de la femme si elle existe déjà
                                        CPCommunicationFiscaleManager comFisMng = new CPCommunicationFiscaleManager();
                                        comFisMng.setSession(getSession());
                                        comFisMng.setForIdTiers(decision.getIdTiers());
                                        comFisMng.setForIdIfd(decision.getIdIfdDefinitif());
                                        comFisMng.setForGenreAffilie(decision.getGenreAffilie());
                                        comFisMng.setForDateComptabilisationVide(Boolean.TRUE);
                                        comFisMng.find(getTransaction());
                                        for (int i = 0; i < comFisMng.getSize(); i++) {
                                            if (listeComFisSupprimees != null) {
                                                listeComFisSupprimees.add(decision);
                                            }
                                            if (!simulation.booleanValue()) {
                                                // Si pas de date de retour et d'envoi => suppression
                                                // sinon mettre à jour la date de comptabilisation (pas de suppression à
                                                // cause d'un possible rejet)
                                                CPCommunicationFiscale cFis = (CPCommunicationFiscale) comFisMng
                                                        .getEntity(i);
                                                if (isHistoriqueCommunication()
                                                        || !JadeStringUtil.isBlankOrZero(cFis.getDateEnvoi())
                                                        || !JadeStringUtil.isBlankOrZero(cFis.getDateRetour())) {
                                                    cFis.setDateComptabilisation(JACalendar.todayJJsMMsAAAA());
                                                    cFis.setIdAffiliation(decision.getIdAffiliation());
                                                    cFis.update(getTransaction());
                                                } else {
                                                    cFis.delete(getTransaction());
                                                }
                                            }
                                        }
                                        // Recherche de la communication fiscale
                                        // du mari.
                                        CPCommunicationFiscale comFis = new CPCommunicationFiscale();
                                        comFis = new CPCommunicationFiscale();
                                        comFis.setSession(getSession());
                                        comFis.setAlternateKey(CPCommunicationFiscale.AK_IFD_IDTIERS);
                                        comFis.setIdTiers(decision.getIdConjoint());
                                        comFis.setIdIfd(decision.getIdIfdDefinitif());
                                        comFis.retrieve(getTransaction());
                                        if (!comFis.isNew() && (comFis != null)) {
                                            if (JadeStringUtil.isIntegerEmpty(decision.getIdCommunication())
                                                    || !decision.getIdCommunication().equals(
                                                            comFis.getIdCommunication())) {
                                                if (listeComFisCreees != null) {
                                                    listeComFisCreees.add(decision);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // Création de la demande de com fis pour la décision
                    if (creerCommfisc) {
                        CPCommunicationFiscale comFis = new CPCommunicationFiscale();
                        // Recherche de la demande de com. fis. pour ne pas la créer si elle existe déjà
                        CPCommunicationFiscaleAffichageManager comFisMng = new CPCommunicationFiscaleAffichageManager();
                        comFisMng.setSession(getSession());
                        comFisMng.setWithAnneeEnCours(Boolean.TRUE);
                        comFisMng.setForIdTiers(decision.getIdTiers());
                        comFisMng.setForIdIfd(decision.getIdIfdDefinitif());
                        comFisMng.setForNumAffilie(affiliation.getAffilieNumero());
                        comFisMng.setExceptComptabilise(Boolean.TRUE);
                        comFisMng.find(getTransaction());
                        if (comFisMng.getSize() == 0) {
                            if (listeComFisCreees != null) {
                                listeComFisCreees.add(decision);
                            }
                            // Mise à jour des anciennes com .fis - Utile pour new provisoire qui aurait été envoyée(NE)
                            comFisMng.setForCanton(codeCanton);
                            comFisMng.find();
                            for (int i = 0; i < comFisMng.getSize(); i++) {
                                CPCommunicationFiscale oldComFis = (CPCommunicationFiscale) comFisMng.getEntity(i);
                                if (JadeStringUtil.isIntegerEmpty(decision.getDateFacturation())
                                        || BSessionUtil.compareDateFirstGreater(getSession(), oldComFis.getDateEnvoi(),
                                                decision.getDateFacturation())) {
                                    oldComFis.setDateComptabilisation(JACalendar.todayJJsMMsAAAA());
                                } else {
                                    oldComFis.setDateComptabilisation(decision.getDateFacturation());
                                }
                                oldComFis.update(getTransaction());
                            }
                            // La demande n'existe pas, on la crée.
                            if (!simulation.booleanValue()) {
                                ITIAdministration admin = AFAffiliationUtil.getNoCaisseAVS(affiliation,
                                        decision.getAnneeDecision());
                                if (admin != null) {
                                    comFis.setIdCaisse(admin.getIdTiersAdministration());
                                } else {
                                    comFis.setIdCaisse("");
                                }
                                comFis.setGenreAffilie(decision.getGenreAffilie());
                                comFis.setAnneePrise(decision.getAnneePrise());
                                comFis.setCanton(codeCanton);
                                comFis.setIdAffiliation(decision.getIdAffiliation());
                                comFis.setIdIfd(decision.getIdIfdDefinitif());
                                comFis.setIdTiers(decision.getIdTiers());
                                comFis.add(getTransaction());
                            }
                        } else if (!simulation.booleanValue() && (comFisMng.getSize() > 0)) {
                            // Mise à jour de l'id communication créé dans la
                            // décision
                            // Si le canton ou le genre change change :
                            // - Si l'annonce n'a pas été envoyée => mise à jour
                            // - Si l'annonce a été envoyée => mettre demande
                            // annulée et créer une nouvelle
                            // communication pour le nouveau canton avec date
                            // d'envoi à 0
                            // Si nouvelle provisoire et date de retour =>
                            // remettre à zéro la date d'envoi et celle de
                            // retour
                            comFis.setSession(getSession());
                            comFis.setIdCommunication(((CPCommunicationFiscaleAffichage) comFisMng.getFirstEntity())
                                    .getIdCommunication());
                            comFis.retrieve(getTransaction());
                            if (!comFis.getCanton().equalsIgnoreCase(codeCanton)
                                    || CPToolBox.isGenreDifferent(comFis.getGenreAffilie(), decision.getGenreAffilie())) {
                                if (JadeStringUtil.isIntegerEmpty(comFis.getDateEnvoi())) {
                                    comFis.setCanton(codeCanton);
                                    // comFis.setCanton(decision.getTiers().getCantonDomicile());
                                    comFis.setIdAffiliation(decision.getIdAffiliation());
                                    comFis.setGenreAffilie(decision.getGenreAffilie());
                                    comFis.setDateRetour("");
                                    comFis.setDateComptabilisation("");
                                    comFis.update(getTransaction());

                                } else {
                                    // Demande d'annulation si le cas n'a pas
                                    // été retourné ou comptabilisé
                                    if (JadeStringUtil.isIntegerEmpty(comFis.getDateRetour())
                                            && JadeStringUtil.isIntegerEmpty(comFis.getDateComptabilisation())) {
                                        comFis.setDemandeAnnulee(Boolean.TRUE);
                                        comFis.setAlternateKey(0);
                                        comFis.update(getTransaction());
                                    }
                                    // Création
                                    CPCommunicationFiscale newComFis = new CPCommunicationFiscale();
                                    newComFis.setSession(getSession());
                                    newComFis.setAnneePrise(decision.getAnneePrise());
                                    newComFis.setCanton(codeCanton);
                                    newComFis.setGenreAffilie(decision.getGenreAffilie());
                                    newComFis.setIdAffiliation(decision.getIdAffiliation());
                                    newComFis.setIdCaisse(comFis.getIdCaisse());
                                    newComFis.setIdIfd(decision.getIdIfdDefinitif());
                                    newComFis.setIdTiers(decision.getIdTiers());
                                    newComFis.add(getTransaction());
                                }
                            } // PO 8280
                            else if (!CPDecision.CS_FACTURATION.equalsIgnoreCase(decision.getDernierEtat())
                                    && !CPDecision.CS_PB_COMPTABILISATION.equalsIgnoreCase(decision.getDernierEtat())
                                    && !CPDecision.CS_REPRISE.equalsIgnoreCase(decision.getDernierEtat())) {
                                comFis.setIdAffiliation(decision.getIdAffiliation());
                                comFis.setDateEnvoi("");
                                comFis.setDateRetour("");
                                comFis.setDateComptabilisation("");
                                comFis.update(getTransaction());
                            } else if (!comFis.getIdAffiliation().equalsIgnoreCase(decision.getIdAffiliation())) {
                                comFis.setIdAffiliation(decision.getIdAffiliation());
                                comFis.update(getTransaction());
                            }

                        }
                    }
                } else {
                    // Si décision de type définitive: Suppression de la demande ou mettre la date de comptabilisation
                    // (si historique)
                    // La recherche ne se fait surtout pas avec l'idCommunication car si pour la femme la communication
                    // a
                    // été créé au nom du mari, elle ne doit pas être supprimée avant que le mari ait une définitive.
                    // Si on trouve une communication fiscale avec la clé alternée pour la femme,
                    // ça veut dire qu'il n'y en a pas pour le mari et on peut la supprimer
                    CPCommunicationFiscaleAffichageManager comFisMng = new CPCommunicationFiscaleAffichageManager();
                    comFisMng.setSession(getSession());
                    comFisMng.setForIdTiers(decision.getIdTiers());
                    comFisMng.setForIdIfd(decision.getIdIfdDefinitif());
                    if ((affiliation != null) && decision.getIdAffiliation().equals(affiliation.getAffiliationId())) {
                        comFisMng.setForNumAffilie(affiliation.getAffilieNumero());
                    } else {
                        comFisMng.setForNumAffilie(decision.getAffiliation().getAffilieNumero());
                    }
                    comFisMng.setWithAnneeEnCours(Boolean.TRUE);
                    comFisMng.find();
                    for (int i = 0; i < comFisMng.getSize(); i++) {
                        CPCommunicationFiscale comFis = new CPCommunicationFiscale();
                        comFis.setSession(getSession());
                        comFis.setIdCommunication(((CPCommunicationFiscaleAffichage) comFisMng.getEntity(i))
                                .getIdCommunication());
                        comFis.retrieve();
                        if (JadeStringUtil.isIntegerEmpty(comFis.getDateRetour())) { // PO 4932
                            comFis.setDateRetour(decision.getDateFacturation());
                        }
                        if (JadeStringUtil.isIntegerEmpty(comFis.getDateComptabilisation())
                                && comFis.getDemandeAnnulee().equals(Boolean.FALSE)) {
                            // Mise à jour de la date retour
                            if (!simulation.booleanValue()) {
                                if (isHistoriqueCommunication()) {
                                    // Mettre en annulé si demande envoyée, non comptabilisé et si pas de retour
                                    if (!JadeStringUtil.isEmpty(comFis.getDateEnvoi())
                                            && JadeStringUtil.isEmpty(comFis.getDateRetour())) {
                                        comFis.setDemandeAnnulee(Boolean.TRUE);
                                    }
                                    if (JadeStringUtil.isIntegerEmpty(decision.getDateFacturation())
                                            || BSessionUtil.compareDateFirstGreater(getSession(),
                                                    comFis.getDateEnvoi(), decision.getDateFacturation())) {
                                        comFis.setDateComptabilisation(JACalendar.todayJJsMMsAAAA());
                                    } else {
                                        comFis.setDateComptabilisation(decision.getDateFacturation());
                                    }
                                    comFis.update(getTransaction());
                                } else {
                                    comFis.delete(getTransaction());
                                }
                            }
                        }
                    }
                }
                if (!simulation.booleanValue()) {
                    // Mise à jour de l'état de la communication fiscale retour
                    if (!JadeStringUtil.isIntegerEmpty(decision.getIdCommunication())) {
                        CPCommunicationFiscaleRetourViewBean comRetour = new CPCommunicationFiscaleRetourViewBean();
                        comRetour.setIdRetour(decision.getIdCommunication());
                        comRetour.setSession(getSession());
                        comRetour.retrieve(getTransaction());
                        if (!comRetour.isNew()
                                && !CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE.equalsIgnoreCase(comRetour
                                        .getStatus())) {
                            comRetour.setStatus(CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE);
                            comRetour.setIdCommunication("");
                            comRetour.update(getTransaction());
                            idJrn = comRetour.getIdJournalRetour();
                        }
                    }
                }
                return idJrn;
            } else {
                return "";
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(decision.toString() + ": ", FWViewBeanInterface.ERROR, this.getClass().getName());
            getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, e.toString());
            return "";
        }
    }

    /**
     * Mise à jour du status des éventuels journaux si des décisions ont été générées par des données du fisc.
     */
    public void majJournalRetour() throws Exception {
        Iterator<String> iter = listIdJournalRetour.iterator();
        // Insérer le journal si il n'existe pas
        while (iter.hasNext()) {
            // Mise à jour du journal
            CPCommunicationFiscaleRetourViewBean jrn = new CPCommunicationFiscaleRetourViewBean();
            jrn.setSession(getSession());
            jrn.setIdJournalRetour(iter.next());
            jrn.retrieve(getTransaction());
            if (!jrn.isNew()) {
                jrn.setStatus(CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE);
                jrn.update();
            }
        }
    }

    protected String rechercheCodeCanton(CPDecision decision) throws Exception {
        TIAdresseDataSource adresse = decision.loadTiers().getAdresseAsDataSource(
                IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005", "31.12." + decision.getAnneeDecision(), true);
        String codeCanton = "";
        if (adresse != null) {
            Hashtable<?, ?> data = adresse.getData();
            codeCanton = (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_CANTON_CODE_OFAS);
            if ("050".equalsIgnoreCase(codeCanton)) {
                codeCanton = IConstantes.CS_LOCALITE_CANTON_JURA;
                // Si adresse étrangère (code canton=30) =>
                // recherche par l'adresse de courrier
            } else if (codeCanton.equalsIgnoreCase("030") || JadeStringUtil.isEmpty(codeCanton)) { // 30
                // =
                // Etranger - PO 7293
                adresse = decision.loadTiers().getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        affiliation.getAffilieNumero(), "31.12." + decision.getAnneeDecision(), true, null);
                if (adresse != null) {
                    data = adresse.getData();
                    codeCanton = (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_CANTON_CODE_OFAS);
                    // PO 8279 - Si adresse étrangère en fin d'année => rechercher si adresse suisse valide pour l'année
                    if (codeCanton.equalsIgnoreCase("030")) {
                        adresse = decision.loadTiers().getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                                "519005", affiliation.getAffilieNumero(), "01.01." + decision.getAnneeDecision(), true,
                                null);
                        data = adresse.getData();
                        codeCanton = (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_CANTON_CODE_OFAS);
                    }
                    if (adresse != null) {
                        if ("050".equalsIgnoreCase(codeCanton)) {
                            codeCanton = IConstantes.CS_LOCALITE_CANTON_JURA;
                        } else {
                            codeCanton = "505" + codeCanton;
                        }
                    }
                }
            } else {
                codeCanton = "505" + codeCanton;
            }
        }
        if (JadeStringUtil.isEmpty(codeCanton)) {
            codeCanton = decision.loadTiers().getCanton();
        }
        return codeCanton;
    }

    /**
     * @param string
     */
    public void setAnneeDecision(String string) {
        anneeDecision = string;
    }

    public void setForGenreAffilie(java.lang.String forGenreAffilie) {
        this.forGenreAffilie = forGenreAffilie;
    }

    public void setFromAffilieDebut(java.lang.String fromAffilieDebut) {
        this.fromAffilieDebut = fromAffilieDebut;
    }

    public void setFromAffilieFin(java.lang.String fromAffilieFin) {
        this.fromAffilieFin = fromAffilieFin;
    }

    /**
     * @param b
     */
    public void setHistoriqueCommunication(boolean b) {
        historiqueCommunication = b;
    }

    /**
     * @param boolean1
     */
    public void setSimulation(Boolean boolean1) {
        simulation = boolean1;
    }

    public boolean testerCommunicationEnRetourNonTraitee(CPDecision decision) throws Exception {
        boolean isProvisoireCommunicationNonTraite = false;
        if (decision.getTypeDecision().equals(CPDecision.CS_PROVISOIRE)) {
            CPCommunicationFiscaleRetourManager comMng = new CPCommunicationFiscaleRetourManager();
            comMng.setSession(getSession());
            comMng.setForIdTiers(decision.getIdTiers());
            comMng.setForIdIfd(decision.getIdIfdDefinitif());
            comMng.setForIdAffiliation(decision.getIdAffiliation());
            comMng.setNotInStatus(CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE + " , "
                    + CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE);
            if (!JadeStringUtil.isEmpty(decision.getIdCommunication())) {
                comMng.setExceptIdRetour(decision.getIdCommunication());
            }
            comMng.find();
            if (comMng.size() > 0) {
                isProvisoireCommunicationNonTraite = true;
            }
        }
        return isProvisoireCommunicationNonTraite;
    }

}