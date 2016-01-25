package globaz.phenix.reprise;

import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuelUtil;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliation;
import globaz.phenix.db.principale.CPDecisionAffiliationManager;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIHistoriqueAvsManager;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Insérez la description du type ici. Date de création : (17.06.2003 14:38:38)
 * 
 * @author: jpa
 */
public class CPListeCotPersCiRadie {
    private String fromAnneeDecision = "";
    private String fromNumAff = "";
    private String idPassage = "";
    private CPDecisionAffiliationManager manager = null;
    private Vector messages = new Vector();
    private boolean miseAjourCI = false;
    private BProcess processAppelant = null;
    private BSession session = null;
    private HSSFSheet sheet;
    private String tillNumAff = "";
    private String toAnneeDecision = "";

    private HSSFWorkbook wb;

    /**
     * Commentaire relatif au constructeur TentDocument.
     */
    public CPListeCotPersCiRadie() {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet("COMP1");
    }

    /**
     * Commentaire relatif au constructeur TentDocument.
     */
    public CPListeCotPersCiRadie(BSession session, String fromAnnee, String toAnnee, String fromNumAff,
            String tillNumAff) {
        this.session = session;
        wb = new HSSFWorkbook();
        setFromAnneeDecision(fromAnnee);
        setToAnneeDecision(toAnnee);
        setFromNumAff(fromNumAff);
        setTillNumAff(tillNumAff);
        sheet = wb.createSheet(fromAnnee + " -" + toAnnee);
        sheet = setTitleRow(wb, sheet);
    }

    /**
     * Returns the fromAnneeDecision.
     * 
     * @return String
     */
    public String getFromAnneeDecision() {
        return fromAnneeDecision;
    }

    public String getFromNumAff() {
        return fromNumAff;
    }

    public String getIdPassage() {
        return idPassage;
    }

    public CPDecisionAffiliationManager getManager() {
        return manager;
    }

    public String getOutputFile() {
        try {
            File f = File.createTempFile(session.getLabel("LISTE_TITRE") + JACalendar.today().toStrAMJ() + "_",
                    ".xls");
            // f.deleteOnExit();
            FileOutputStream out = new FileOutputStream(f);
            wb.write(out);
            out.close();
            return f.getAbsolutePath();
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return "";
        }
    }

    /**
     * Returns the processAppelant.
     * 
     * @return BProcess
     */
    public BProcess getProcessAppelant() {
        return processAppelant;
    }

    /**
     * Returns the session.
     * 
     * @return BSession
     */
    public BSession getSession() {
        return session;
    }

    public String getTillNumAff() {
        return tillNumAff;
    }

    /**
     * Returns the toAnneeDecision.
     * 
     * @return String
     */
    public String getToAnneeDecision() {
        return toAnneeDecision;
    }

    public boolean isMiseAjourCI() {
        return miseAjourCI;
    }

    public HSSFSheet populateSheet(CPDecisionAffiliationManager manager, BTransaction transaction) throws Exception {

        // Création journal CI si mise à jour activée
        BSession sessionPavo = null;
        CIJournal journalCi = null;
        CICompteIndividuelUtil ci = null;
        if (isMiseAjourCI()) {
            sessionPavo = (BSession) GlobazSystem.getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).newSession(
                    getSession());
            journalCi = new CIJournal();
            journalCi.setSession(sessionPavo);
            journalCi.setIdTypeInscription(CIJournal.CS_DECISION_COT_PERS);
            journalCi.setRefExterneFacturation(getIdPassage());
            journalCi.setLibelle("Correction CI pour cot.pers. radié");
            journalCi.add(transaction);

            ci = new CICompteIndividuelUtil();
            ci.setSession(sessionPavo);
            ci.setIdJournal(journalCi.getIdJournal());
        }
        BStatement statement = null;
        CPDecisionAffiliation decision = null;
        CPDecisionAffiliation decisionPrecedente = null;
        BigDecimal montantEnCI = null;
        BigDecimal montantEnCIImputation = null;
        BigDecimal montantCPBD = new BigDecimal("0");
        BigDecimal montantCP = new BigDecimal("0");
        String periodeAffiliation = "";
        // decision.setSession(transaction.getSession());
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style2.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
        processAppelant.setProgressScaleValue(manager.getCount(transaction));

        BTransaction transactionLecture = (BTransaction) getSession().newTransaction();

        sheet.setColumnWidth((short) 0, (short) 3500);
        sheet.setColumnWidth((short) 1, (short) 5000);
        sheet.setColumnWidth((short) 2, (short) 3000);
        sheet.setColumnWidth((short) 3, (short) 2000);
        sheet.setColumnWidth((short) 4, (short) 5500);
        sheet.setColumnWidth((short) 5, (short) 3500);
        sheet.setColumnWidth((short) 6, (short) 3500);
        sheet.setColumnWidth((short) 7, (short) 3500);
        sheet.setColumnWidth((short) 8, (short) 3500);
        int nbRow = 11;
        try {
            transactionLecture.openTransaction();
            statement = manager.cursorOpen(transactionLecture);

            while (((decision = (CPDecisionAffiliation) manager.cursorReadNext(statement)) != null)
                    && (!decision.isNew())) {
                try {
                    // Test s'il n'y a pas une autre décision active sous une
                    // autre affiliation
                    CPDecisionAffiliationManager mng1 = new CPDecisionAffiliationManager();
                    mng1.setForIdTiers(decision.getIdTiers());
                    mng1.setSession(getSession());
                    mng1.setIsActive(Boolean.TRUE);
                    mng1.setForAnneeDecision(decision.getAnneeDecision());
                    mng1.setForGenreAffilie(decision.getGenreAffilie());
                    mng1.find();
                    if (mng1.size() == 0) {
                        // Si tiers ou année différente => recherche du montant
                        // CI
                        if ((decisionPrecedente == null)
                                || !decision.getIdTiers().equalsIgnoreCase(decisionPrecedente.getIdTiers())
                                || !decision.getAnneeDecision().equalsIgnoreCase(decisionPrecedente.getAnneeDecision())) {
                            // Recherche du n° avs correspondant à l'année de
                            // décision
                            String numAvs = "";
                            String numAvsBk = "";
                            TIHistoriqueAvsManager histManager = new TIHistoriqueAvsManager();
                            histManager.setSession(getSession());
                            histManager.setForIdTiers(decision.getIdTiers());
                            histManager.orderByNumAvs();
                            histManager.find();
                            montantEnCI = new BigDecimal("0");
                            montantEnCIImputation = new BigDecimal("0");
                            Boolean nonActif = Boolean.FALSE;
                            if (decision.getTypeAffiliation().equalsIgnoreCase(
                                    globaz.naos.translation.CodeSystem.TYPE_AFFILI_NON_ACTIF)
                                    || decision.getTypeAffiliation().equalsIgnoreCase(
                                            globaz.naos.translation.CodeSystem.TYPE_AFFILI_SELON_ART_1A)) {
                                nonActif = Boolean.TRUE;
                            }
                            for (int i = 0; i < histManager.size(); i++) {
                                numAvs = ((TIHistoriqueAvs) histManager.getEntity(i)).getNumAvs();
                                if (!numAvs.equalsIgnoreCase(numAvsBk)) {
                                    numAvsBk = numAvs;
                                    CICompteIndividuelUtil compteIndividuel = new CICompteIndividuelUtil();
                                    compteIndividuel.setSession(getSession());
                                    montantEnCI = montantEnCI.add(compteIndividuel.getSommeParAnneeGenre(numAvs,
                                            decision.getAnneeDecision(), false, "", nonActif));
                                    // Inscription directe de la bonification
                                    montantEnCIImputation = montantEnCIImputation.add(ci
                                            .getSommeParAnneeCodeAmortissement(numAvs, "", CIEcriture.CS_CIGENRE_4,
                                                    decision.getAnneeDecision(), CIEcriture.CS_CODE_MIS_EN_COMTE));
                                    montantEnCIImputation = montantEnCIImputation.add(ci
                                            .getSommeParAnneeCodeAmortissement(numAvs, "", CIEcriture.CS_CIGENRE_7,
                                                    decision.getAnneeDecision(), CIEcriture.CS_CODE_MIS_EN_COMTE));
                                }
                            }
                            // Impression si montant CI et cot. pers sont
                            // différent
                            if ((montantCPBD.compareTo(montantEnCI) != 0)
                                    || (montantCPBD.compareTo(montantEnCIImputation) != 0)) {
                                float diff = montantCPBD.subtract(montantEnCI).floatValue();
                                if ((diff >= 10) || (diff <= -10)) {

                                    if (BSessionUtil.compareDateFirstLower(getSession(),
                                            decision.getFinDecision(), "01.01.2009")) {
                                        TIHistoriqueAvs hist = new TIHistoriqueAvs();
                                        hist.setSession(getSession());
                                        try {
                                            numAvs = hist.findPrevKnownNumAvs(decision.getIdTiers(),
                                                    decision.getDebutDecision());
                                            if (JadeStringUtil.isEmpty(numAvs)) {
                                                numAvs = hist.findNextKnownNumAvs(decision.getIdTiers(),
                                                        decision.getFinDecision());
                                            }
                                        } catch (Exception e) {
                                            numAvs = "";
                                        }
                                    } else {
                                        numAvs = decision.getTiers().getNumAvsActuel();
                                    }
                                    HSSFRow row = sheet.createRow(nbRow);
                                    nbRow++;
                                    int colNum = 0;
                                    // numéro affilié
                                    HSSFCell cell = row.createCell((short) colNum++);
                                    cell.setCellStyle(style);
                                    cell.setCellValue(new HSSFRichTextString(decision.getNumAffilie()));
                                    // numéro avs
                                    cell = row.createCell((short) colNum++);
                                    cell.setCellStyle(style);
                                    cell.setCellValue(new HSSFRichTextString(numAvs));
                                    // Genre
                                    cell = row.createCell((short) colNum++);
                                    cell.setCellStyle(style);
                                    cell.setCellValue(new HSSFRichTextString(globaz.phenix.translation.CodeSystem
                                            .getLibelleCourt(getSession(), decision.getGenreAffilie())));
                                    // Année
                                    cell = row.createCell((short) colNum++);
                                    cell.setCellStyle(style);
                                    cell.setCellValue(new HSSFRichTextString(decision.getAnneeDecision()));
                                    // periode affiliation
                                    if ((!JadeStringUtil.isIntegerEmpty(decision.getFinAffiliation()) && BSessionUtil
                                            .compareDateFirstGreaterOrEqual(getSession(),
                                                    decision.getDebutDecision(), decision.getFinAffiliation()))
                                            || (BSessionUtil.compareDateFirstLowerOrEqual(getSession(),
                                                    decision.getFinDecision(), decision.getDebutAffiliation()))) {
                                        periodeAffiliation = decision.getDebutAffiliation() + " - "
                                                + decision.getFinAffiliation();
                                    } else {
                                        periodeAffiliation = "";
                                        if (decision.getDebutAffiliation().length() > 0) {
                                            periodeAffiliation = decision.getDebutAffiliation();
                                        }
                                        if (decision.getFinAffiliation().length() > 0) {
                                            periodeAffiliation += " - " + decision.getFinAffiliation();
                                        }
                                    }
                                    cell = row.createCell((short) colNum++);
                                    cell.setCellStyle(style);
                                    cell.setCellValue(new HSSFRichTextString(periodeAffiliation));
                                    // Montant CP
                                    cell = row.createCell((short) colNum++);
                                    cell.setCellStyle(style2);
                                    cell.setCellValue(new HSSFRichTextString("0"));
                                    // Montant CI
                                    cell = row.createCell((short) colNum++);
                                    cell.setCellStyle(style2);
                                    montantEnCI.add(montantEnCIImputation);
                                    cell.setCellValue(new HSSFRichTextString(montantEnCI.toString()));
                                    // Différence
                                    cell = row.createCell((short) colNum++);
                                    cell.setCellStyle(style2);
                                    cell.setCellValue(new HSSFRichTextString((montantCPBD.subtract(montantEnCI))
                                            .toString()));
                                    // id Affiliation
                                    cell = row.createCell((short) colNum++);
                                    cell.setCellStyle(style2);
                                    cell.setCellValue(new HSSFRichTextString(decision.getIdAffiliation()));
                                    // //id Ecriture Ci
                                    // String idEcriture="";
                                    // if(JadeStringUtil.isBlankOrZero(montantEnCI.toString())){
                                    // CIEcritureManager ciMng = new
                                    // CIEcritureManager();
                                    // ciMng.setForIdTiers(decision.getIdAffiliation());
                                    // ciMng.setForAnnee(decision.getAnneeDecision());
                                    // ciMng.setForMontant(montantCPBD.toString());
                                    // ciMng.setSession(getSession());
                                    // ciMng.find();
                                    // if(ciMng.size()==1)
                                    // idEcriture=
                                    // ((CIEcriture)ciMng.getFirstEntity()).getEcritureId();
                                    // }
                                    // cell = row.createCell((short)colNum++);
                                    // cell.setCellStyle(style2);
                                    // cell.setCellValue(idEcriture);
                                    // Mise à jour du CI
                                    if ((journalCi != null) && !journalCi.isNew()) {
                                        CPDecision deci = new CPDecision();
                                        deci.setSession(getSession());
                                        deci.setIdDecision(decision.getIdDecision());
                                        deci.retrieve();
                                        if (!deci.isNew()) {
                                            // Si aucun n° trouvé dans
                                            // historique ou NNSS => prendre
                                            // l'actuel n° avs
                                            if (JadeStringUtil.isEmpty(numAvs)) {
                                                numAvs = decision.getTiers().getNumAvsActuel();
                                            }
                                            // Récupération période CI
                                            String moisDebut = Integer.toString(JACalendar.getMonth(decision
                                                    .getDebutDecision()));
                                            String moisFin = Integer.toString(JACalendar.getMonth(decision
                                                    .getFinDecision()));
                                            // Détermination du genre
                                            String genre = "";
                                            if (deci.isNonActif()) {
                                                if (decision.getTiers().isRentier(
                                                        Integer.parseInt(decision.getAnneeDecision()))) {
                                                    genre = CIEcriture.CS_CIGENRE_7;
                                                } else {
                                                    genre = CIEcriture.CS_CIGENRE_4;
                                                }
                                            } else if (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(decision
                                                    .getGenreAffilie())) {
                                                genre = CIEcriture.CS_CIGENRE_3;
                                            } else if (CPDecision.CS_RENTIER.equalsIgnoreCase(decision
                                                    .getGenreAffilie())) {
                                                genre = CIEcriture.CS_CIGENRE_7;
                                            } else if (CPDecision.CS_AGRICULTEUR.equalsIgnoreCase(decision
                                                    .getGenreAffilie())) {
                                                genre = CIEcriture.CS_CIGENRE_9;
                                            } else if (CPDecision.CS_TSE.equalsIgnoreCase(decision.getGenreAffilie())) {
                                                genre = CIEcriture.CS_CIGENRE_2;
                                            }
                                            if (!JadeStringUtil.isEmpty(numAvs)) {
                                                // Mettre le montant de la
                                                // décision que si il est
                                                // infèrieure à celui qui est au
                                                // CI pour l'année concernée
                                                // sinon mettre le montant du CI
                                                // Recherche du montant CI de
                                                // base
                                                String code = null;
                                                try {
                                                    if (montantCPBD.compareTo(montantEnCIImputation) != 0) {
                                                        ci.verifieCI(transaction, decision.getIdAffiliation(), numAvs,
                                                                moisDebut, moisFin, decision.getAnneeDecision(),
                                                                (montantCPBD.subtract(montantEnCIImputation))
                                                                        .toString(), genre,
                                                                CICompteIndividuelUtil.MODE_DIRECT,
                                                                CIEcriture.CS_CODE_MIS_EN_COMTE, CPToolBox
                                                                        ._returnCodeSpecial(deci), "");
                                                    }
                                                    if (montantCPBD.compareTo(montantEnCI) != 0) {
                                                        ci.verifieCI(transaction, decision.getIdAffiliation(), numAvs,
                                                                moisDebut, moisFin, decision.getAnneeDecision(),
                                                                (montantCPBD.subtract(montantEnCI)).toString(), genre,
                                                                CICompteIndividuelUtil.MODE_DIRECT, code,
                                                                CPToolBox._returnCodeSpecial(deci), "");
                                                    }
                                                    if (getSession().hasErrors()) {
                                                        processAppelant.getMemoryLog().logMessage(
                                                                transaction.getErrors().toString(), FWMessage.ERREUR,
                                                                this.getClass().getName());
                                                        System.out.println(decision.getNumAffilie() + " - "
                                                                + decision.getAnneeDecision());
                                                        transaction.rollback();
                                                        transaction.clearErrorBuffer();
                                                    }
                                                    if (transaction.hasErrors()) {
                                                        System.out.println(decision.getNumAffilie() + " - "
                                                                + decision.getAnneeDecision());
                                                        processAppelant.getMemoryLog().logMessage(
                                                                transaction.getErrors().toString(), FWMessage.ERREUR,
                                                                this.getClass().getName());
                                                        transaction.rollback();
                                                        transaction.clearErrorBuffer();
                                                    } else {
                                                        transaction.commit();
                                                    }
                                                } catch (Exception e) {
                                                    processAppelant.getMemoryLog().logMessage(
                                                            decision.getNumAffilie() + " - "
                                                                    + decision.getAnneeDecision() + " - "
                                                                    + decision.getIdDecision(), FWMessage.INFORMATION,
                                                            this.getClass().getName());
                                                    processAppelant.getTransaction().rollback();
                                                } finally {
                                                    for (Iterator iter = processAppelant.getMemoryLog()
                                                            .getMessagesToVector().iterator(); iter.hasNext();) {
                                                        messages.add(iter.next());
                                                    }
                                                    processAppelant.getMemoryLog().clear();
                                                }

                                            }
                                        }

                                    }
                                }
                            }
                            montantCPBD = new BigDecimal("0");
                        }
                        montantCPBD = montantCPBD.add(montantCP);
                    }
                    decisionPrecedente = decision;
                    processAppelant.incProgressCounter();
                } catch (Exception e) {
                    processAppelant.getMemoryLog().logMessage(
                            decision.getNumAffilie() + " - " + decision.getAnneeDecision() + " - "
                                    + decision.getIdDecision(), FWMessage.INFORMATION, this.getClass().getName());
                    processAppelant.getTransaction().rollback();
                }
            }
        } catch (Exception e) {
            processAppelant.getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        } finally {
            try {
                try {
                    manager.cursorClose(statement);
                } finally {
                    if (transactionLecture != null) {
                        transactionLecture.closeTransaction();
                    }
                    statement = null;
                }
            } catch (Exception e) {
                processAppelant.getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL,
                        this.getClass().getName());
            }
        }
        if ((journalCi != null) && !journalCi.isNew()) {
            journalCi.updateInscription(transaction);
        }
        // Remettre les erreurs des process dans le log
        for (Iterator iter = messages.iterator(); iter.hasNext();) {
            processAppelant.getMemoryLog().getMessagesToVector().add(iter.next());
        }
        manager.cursorClose(statement);
        statement = null;
        HSSFFooter footer = sheet.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0120CCP");
        return sheet;
    }

    /**
     * Sets the fromAnneeDecision.
     * 
     * @param fromAnneeDecision
     *            The fromAnneeDecision to set
     */
    public void setFromAnneeDecision(String fromAnneeDecision) {
        this.fromAnneeDecision = fromAnneeDecision;
    }

    public void setFromNumAff(String fromNumAff) {
        this.fromNumAff = fromNumAff;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    public void setManager(CPDecisionAffiliationManager manager) {
        this.manager = manager;
    }

    public void setMiseAjourCI(boolean miseAjourCI) {
        this.miseAjourCI = miseAjourCI;
    }

    /**
     * Sets the processAppelant.
     * 
     * @param processAppelant
     *            The processAppelant to set
     */
    public void setProcessAppelant(BProcess processAppelant) {
        this.processAppelant = processAppelant;
    }

    /**
     * Sets the session.
     * 
     * @param session
     *            The session to set
     */
    public void setSession(BSession session) {
        this.session = session;
    }

    public void setTillNumAff(String tillNumAff) {
        this.tillNumAff = tillNumAff;
    }

    /**
     * @param wb
     * @param sheet
     * @param row
     * @return
     */
    private HSSFSheet setTitleRow(HSSFWorkbook wb, HSSFSheet sheet) {

        final String[] COL_TITLES = { session.getLabel("NUM_AFFILIE"), session.getLabel("NUM_AVS"),
                session.getLabel("GENRE_DECISION"), session.getLabel("ANNEE"),
                session.getLabel("PERIODE_AFFILIATION"), session.getLabel("LISTE_MONTANT_CP"),
                session.getLabel("LISTE_MONTANT_COMPTA"), session.getLabel("LISTE_DIFFERENCE"),
                "idAffiliation", "idEcritureCi" };

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
            c.setCellValue(new HSSFRichTextString(session.getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase())));
        } catch (Exception e) {
            c.setCellValue(new HSSFRichTextString(""));
        }
        // row = sheet.createRow(1);
        // row = sheet.createRow(2);
        c = row.createCell((short) 5);
        c.setCellValue(new HSSFRichTextString(JACalendar.todayJJsMMsAAAA()));
        c.setCellStyle(style2);
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        c = row.createCell((short) 1);
        c.setCellValue(new HSSFRichTextString(session.getLabel("LISTE_TITRE1")));
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
        int i = 4;
        // création de l'entête
        try {
            // Année de début
            if (getFromAnneeDecision().length() > 0) {
                row = sheet.createRow(i++);
                c = row.createCell((short) 0);
                c.setCellValue(new HSSFRichTextString(""));
                c.setCellStyle(style3);
                c = row.createCell((short) 1);
                c.setCellValue(new HSSFRichTextString(session.getLabel("LISTE_ANNEE_DEB")));
                c.setCellStyle(style3);
                c = row.createCell((short) 3);
                c.setCellValue(new HSSFRichTextString(getFromAnneeDecision()));
                c.setCellStyle(style3);
            }
            // Année de fin
            if (getToAnneeDecision().length() > 0) {
                row = sheet.createRow(i++);
                c = row.createCell((short) 0);
                c.setCellValue(new HSSFRichTextString(""));
                c.setCellStyle(style3);
                c = row.createCell((short) 1);
                c.setCellValue(new HSSFRichTextString(session.getLabel("LISTE_ANNEE_FIN")));
                c.setCellStyle(style3);
                c = row.createCell((short) 3);
                c.setCellValue(new HSSFRichTextString(getToAnneeDecision()));
                c.setCellStyle(style3);
            }
            // Num Affilié de début
            if (getFromNumAff().length() > 0) {
                row = sheet.createRow(i++);
                c = row.createCell((short) 0);
                c.setCellValue(new HSSFRichTextString(""));
                c.setCellStyle(style3);
                c = row.createCell((short) 1);
                c.setCellValue(new HSSFRichTextString(session.getLabel("LISTE_AFF_DEB")));
                c.setCellStyle(style3);
                c = row.createCell((short) 3);
                c.setCellValue(new HSSFRichTextString(getFromNumAff()));
                c.setCellStyle(style3);
            }
            // Num Affilié de fin
            if (getTillNumAff().length() > 0) {
                row = sheet.createRow(i++);
                c = row.createCell((short) 0);
                c.setCellValue(new HSSFRichTextString(""));
                c.setCellStyle(style3);
                c = row.createCell((short) 1);
                c.setCellValue(new HSSFRichTextString(session.getLabel("LISTE_AFF_FIN")));
                c.setCellStyle(style3);
                c = row.createCell((short) 3);
                c.setCellValue(new HSSFRichTextString(getTillNumAff()));
                c.setCellStyle(style3);
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
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
        row = sheet.createRow(10);
        for (int j = 0; j < COL_TITLES.length; j++) {
            c = row.createCell((short) j);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[j]));
            c.setCellStyle(style);
        }
        return sheet;
    }

    /**
     * Sets the toAnneeDecision.
     * 
     * @param toAnneeDecision
     *            The toAnneeDecision to set
     */
    public void setToAnneeDecision(String toAnneeDecision) {
        this.toAnneeDecision = toAnneeDecision;
    }
}
