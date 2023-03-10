package globaz.phenix.reprise;

import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuelUtil;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.util.CICorrigeCCVDUtil;
import globaz.phenix.db.principale.CPCotisation;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliation;
import globaz.phenix.db.principale.CPDecisionAffiliationManager;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.db.principale.CPDonneesCalcul;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIHistoriqueAvsManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Vector;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Ins?rez la description du type ici. Date de cr?ation : (17.06.2003 14:38:38)
 * 
 * @author: jpa
 */
public class CPListeReinscriptionCI {
    /**
     * Retourne le code sp?cial pour les CI selon la d?cision
     * 
     * @param CPDecision
     *            decision
     */
    static public String _returnCodeSpecial(CPDecisionAffiliation decision) throws java.lang.Exception {
        boolean nonActif = false;
        if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(decision.getGenreAffilie())
                || CPDecision.CS_ETUDIANT.equalsIgnoreCase(decision.getGenreAffilie())) {
            nonActif = true;
        }
        // Code sp?cial 01
        if (CPDecision.CS_REMISE.equalsIgnoreCase(decision.getTypeDecision())) {
            return CIEcriture.CS_COTISATION_MINIMALE;
        } else if (nonActif && !JadeStringUtil.isIntegerEmpty(decision.getIdServiceSociale())) {
            return CIEcriture.CS_COTISATION_MINIMALE;
        } else if (CPDecision.CS_RENTIER.equalsIgnoreCase(decision.getTypeDecision()) && !nonActif) {
            return CIEcriture.CS_NONFORMATTEUR_INDEPENDANT;
        }
        return "";
    }

    private String fromAnneeDecision = "";
    private String fromNumAffilie = "";
    private CPDecisionAffiliationManager manager = null;
    private Vector messages = new Vector();
    private BProcess processAppelant = null;
    private BSession session = null;
    private HSSFSheet sheet;
    private String tillAnneeDecision = "";
    private String tillNumAffilie = "";

    private HSSFWorkbook wb;

    /**
     * Commentaire relatif au constructeur TentDocument.
     */
    public CPListeReinscriptionCI() {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet("COMP1");
    }

    /**
     * Commentaire relatif au constructeur TentDocument.
     */
    public CPListeReinscriptionCI(BSession session) {
        this.session = session;
        wb = new HSSFWorkbook();
        sheet = wb.createSheet(getFromAnneeDecision() + " -" + getTillAnneeDecision());
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

    public String getFromNumAffilie() {
        return fromNumAffilie;
    }

    public CPDecisionAffiliationManager getManager() {
        return manager;
    }

    public String getOutputFile() {
        try {
            File f = File.createTempFile(session.getLabel("ListeReinscriptionCI") + JACalendar.today().toStrAMJ()
                    + "_", ".xls");
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

    public String getTillAnneeDecision() {
        return tillAnneeDecision;
    }

    public String getTillNumAffilie() {
        return tillNumAffilie;
    }

    private void majCiCotiSalarie(BTransaction transaction, CICompteIndividuelUtil ci, CPDecisionAffiliation deci,
            String numAvs, String montantCI, CPDonneesBase base, String moisDebut, String moisFin, String genre)
            throws Exception {
        float revenuCiImputation = 0;
        float varMontantCI = 0;
        // recherche coti pay? en tant que salari?
        float cotiEncode = Float.parseFloat(JANumberFormatter.deQuote(base.getCotisationSalarie()));
        // Calcul du Ci qui doit ?tre imputer selon le montant de cotisation
        // pay? en tant que salari?
        CPCotisation coti = CPCotisation._returnCotisation(getSession(), deci.getIdDecision(),
                globaz.naos.translation.CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
        if (coti != null) {
            // Calcul du CI
            revenuCiImputation = (Float.parseFloat(JANumberFormatter.deQuote(coti.getMontantAnnuel())) - cotiEncode)
                    * (float) 9.9;
            revenuCiImputation = JANumberFormatter.round(revenuCiImputation, 1, 2, JANumberFormatter.NEAR);
            revenuCiImputation = Float.parseFloat(JANumberFormatter.deQuote(montantCI)) - revenuCiImputation;
        } else {
            revenuCiImputation = cotiEncode * (float) 9.9;
            revenuCiImputation = JANumberFormatter.round(revenuCiImputation, 1, 2, JANumberFormatter.NEAR);
        }
        montantCI = Float.toString(revenuCiImputation);
        if (!JadeStringUtil.isBlankOrZero(montantCI)) {
            varMontantCI = Math.abs(Float.parseFloat(JANumberFormatter.deQuote(montantCI)));
        }
        if (varMontantCI != 0) {
            majCiImputation(transaction, ci, deci, numAvs, montantCI, moisDebut, moisFin, genre);
        }
    }

    private void majCiImputation(BTransaction transaction, CICompteIndividuelUtil ci, CPDecisionAffiliation deci,
            String numAvs, String montantCI, String moisDebut, String moisFin, String genre) throws Exception {
        // Si annule et remplace
        if (Boolean.TRUE.equals(deci.getComplementaire())) {
            // Extourne du total
            java.math.BigDecimal montantEnCI = ci.getSommeParAnneeCodeAmortissement(numAvs, "", genre,
                    deci.getAnneeDecision(), CIEcriture.CS_CODE_MIS_EN_COMTE);
            if (montantEnCI.floatValue() != 0) {
                montantEnCI = montantEnCI.negate();
                ci.verifieCI(transaction, deci.getIdAffiliation(), numAvs, moisDebut, moisFin, deci.getAnneeDecision(),
                        montantEnCI.toString(), genre, CICompteIndividuelUtil.MODE_DIRECT,
                        CIEcriture.CS_CODE_MIS_EN_COMTE, CPListeReinscriptionCI._returnCodeSpecial(deci), "");
            }
            if (!JadeStringUtil.isEmpty(montantCI) && !montantCI.equalsIgnoreCase("0.00")
                    && !montantCI.equalsIgnoreCase("-0.00") && !montantCI.equalsIgnoreCase("0")
                    && !montantCI.equalsIgnoreCase("-0")) {
                // Inscription du nouveau montant
                ci.verifieCI(transaction, deci.getIdAffiliation(), numAvs, moisDebut, moisFin, deci.getAnneeDecision(),
                        "-" + montantCI, genre, CICompteIndividuelUtil.MODE_DIRECT, CIEcriture.CS_CODE_MIS_EN_COMTE,
                        CPListeReinscriptionCI._returnCodeSpecial(deci), "");
            }
        } else {
            // Inscription directe de la bonification
            java.math.BigDecimal montantEnCI = ci.getSommeParAnnee(numAvs, "", genre, deci.getAnneeDecision(),
                    CIEcriture.CS_CODE_AMORTISSEMENT + ", " + CIEcriture.CS_CODE_MIS_EN_COMTE);
            if (!CIEcriture.CS_CIGENRE_7.equalsIgnoreCase(genre)) {
                montantEnCI = montantEnCI.add(ci.getSommeParAnnee(numAvs, "", CIEcriture.CS_CIGENRE_7,
                        deci.getAnneeDecision(), CIEcriture.CS_CODE_AMORTISSEMENT + ", "
                                + CIEcriture.CS_CODE_MIS_EN_COMTE));
            }
            if (!JadeStringUtil.isEmpty(montantCI)
                    && (montantEnCI.floatValue() < Math.abs(Float.parseFloat(JANumberFormatter.deQuote(montantCI))))) {
                montantCI = "-" + montantEnCI.toString(); // Au max on met le
                // montant au CI
            }
            if (!JadeStringUtil.isEmpty(montantCI) && !montantCI.equalsIgnoreCase("0.00")
                    && !montantCI.equalsIgnoreCase("-0.00") && !montantCI.equalsIgnoreCase("0")
                    && !montantCI.equalsIgnoreCase("-0")) {
                ci.verifieCI(transaction, deci.getIdAffiliation(), numAvs, moisDebut, moisFin, deci.getAnneeDecision(),
                        "-" + montantCI, genre, CICompteIndividuelUtil.MODE_DIRECT, CIEcriture.CS_CODE_MIS_EN_COMTE,
                        CPListeReinscriptionCI._returnCodeSpecial(deci), "");
            }
        }
    }

    private String majCIInitGenre(CPDecisionAffiliation decision, TITiersViewBean tiers) throws Exception {
        String genre = "";
        if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(decision.getGenreAffilie())
                || CPDecision.CS_ETUDIANT.equalsIgnoreCase(decision.getGenreAffilie())) {
            if (tiers.isRentier(Integer.parseInt(decision.getAnneeDecision()))) {
                genre = CIEcriture.CS_CIGENRE_7;
            } else {
                genre = CIEcriture.CS_CIGENRE_4;
            }
        } else if (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(decision.getGenreAffilie())) {
            genre = CIEcriture.CS_CIGENRE_3;
        } else if (CPDecision.CS_RENTIER.equalsIgnoreCase(decision.getGenreAffilie())) {
            genre = CIEcriture.CS_CIGENRE_7;
        } else if (CPDecision.CS_AGRICULTEUR.equalsIgnoreCase(decision.getGenreAffilie())) {
            genre = CIEcriture.CS_CIGENRE_9;
        } else if (CPDecision.CS_TSE.equalsIgnoreCase(decision.getGenreAffilie())) {
            genre = CIEcriture.CS_CIGENRE_2;
        }
        return genre;
    }

    private String majCINumAvs(CPDecisionAffiliation decision, TITiersViewBean tiers) throws Exception {
        String numAvs = "";
        if (BSessionUtil.compareDateFirstLower(getSession(), decision.getFinDecision(), "01.01.2009")) {
            TIHistoriqueAvs hist = new TIHistoriqueAvs();
            hist.setSession(getSession());
            try {
                numAvs = hist.findPrevKnownNumAvs(decision.getIdTiers(), decision.getFinDecision());
                if (JadeStringUtil.isEmpty(numAvs)) {
                    numAvs = hist.findNextKnownNumAvs(decision.getIdTiers(), decision.getDebutDecision());
                }
            } catch (Exception e) {
                numAvs = "";
            }
        }
        // Si aucun n? trouv? dans historique ou NNSS => prendre l'actuel n? avs
        if (JadeStringUtil.isEmpty(numAvs)) {
            numAvs = tiers.getNumAvsActuel();
        }
        return numAvs;
    }

    public HSSFSheet populateSheet(CPDecisionAffiliationManager manager, BTransaction transaction) throws Exception {

        BSession sessionPavo = null;
        CIJournal journalCi = null;
        CICompteIndividuelUtil ci = null;
        sessionPavo = (BSession) GlobazSystem.getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).newSession(
                getSession());
        journalCi = new CIJournal();
        journalCi.setSession(sessionPavo);
        journalCi.setIdTypeInscription(CIJournal.CS_COTISATIONS_PERSONNELLES);
        journalCi.setAnneeCotisation(getFromAnneeDecision());
        String libelleJrn = "Re g?n?ration des CI" + getFromAnneeDecision();
        String rmqJrn = "";
        if (!JadeStringUtil.isEmpty(getFromAnneeDecision())) {
            rmqJrn = libelleJrn + " de " + getFromAnneeDecision();
        }
        if (!getTillAnneeDecision().equalsIgnoreCase(getFromAnneeDecision())) {
            if (!JadeStringUtil.isEmpty(getFromAnneeDecision())) {
                rmqJrn = rmqJrn + " ? " + getTillAnneeDecision();
            } else {
                rmqJrn = rmqJrn + " jusqu'? " + getTillAnneeDecision();
            }
        }
        if (!JadeStringUtil.isEmpty(getFromNumAffilie())) {
            rmqJrn = rmqJrn + " pour " + getFromNumAffilie();
        }
        if (!getTillNumAffilie().equalsIgnoreCase(getFromNumAffilie())) {
            if (!JadeStringUtil.isEmpty(getFromNumAffilie())) {
                rmqJrn = rmqJrn + " ? " + getTillNumAffilie();
            } else {
                rmqJrn = rmqJrn + " jusqu'? " + getTillNumAffilie();
            }
        }
        journalCi.setLibelle(libelleJrn);
        journalCi.setRemTexte(rmqJrn);
        journalCi.add(transaction);

        ci = new CICompteIndividuelUtil();
        ci.setSession(sessionPavo);
        ci.setIdJournal(journalCi.getIdJournal());
        BStatement statement = null;
        CPDecisionAffiliation decision = null;
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
        processAppelant.setProgressScaleValue(manager.getCount(transaction));

        BTransaction transactionLecture = (BTransaction) getSession().newTransaction();

        sheet.setColumnWidth((short) 0, (short) 3500);
        sheet.setColumnWidth((short) 1, (short) 2000);
        sheet.setColumnWidth((short) 2, (short) 5000);
        sheet.setColumnWidth((short) 3, (short) 5000);
        sheet.setColumnWidth((short) 4, (short) 5000);
        int nbRow = 11;
        boolean nePasTraiter = false;
        String idTiersBk = "";
        String anneeBk = "";
        TITiersViewBean tiers = null;
        CICorrigeCCVDUtil ciCorrige = new CICorrigeCCVDUtil();
        String erreur = "";
        try {
            transactionLecture.openTransaction();
            statement = manager.cursorOpen(transactionLecture);

            while (((decision = (CPDecisionAffiliation) manager.cursorReadNext(statement)) != null)
                    && (!decision.isNew())) {
                try {
                    nePasTraiter = false;
                    String montantCI = "";
                    CPDonneesCalcul donnee = new CPDonneesCalcul();
                    // Si montant forc? ? l'encodage, prendre celui-ci sinon
                    // prendre le montant calcul?
                    CPDonneesBase base = new CPDonneesBase();
                    base.setSession(transaction.getSession());
                    base.setIdDecision(decision.getIdDecision());
                    base.retrieve(transaction);
                    if (!base.isNew()) {
                        if (!"".equalsIgnoreCase(base.getRevenuCiForce())) {
                            montantCI = base.getRevenuCiForce();
                        } else {
                            donnee.setSession(getSession());
                            montantCI = donnee.getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_REV_CI);
                        }
                    } else {
                        donnee.setSession(transaction.getSession());
                        montantCI = donnee.getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_REV_CI);
                    }
                    if (decision.isNew()
                            || CPDecision.CS_SALARIE_DISPENSE.equalsIgnoreCase(decision.getSpecification())) {
                        nePasTraiter = true;
                    }
                    if (!JadeStringUtil.isEmpty(montantCI) && !montantCI.equalsIgnoreCase("0.00")
                            && !montantCI.equalsIgnoreCase("-0.00") && !montantCI.equalsIgnoreCase("0")
                            && !montantCI.equalsIgnoreCase("-0")) {
                        // Si m?me tiers et m?me ann?e => ne rien faire
                        if (!decision.getIdTiers().equalsIgnoreCase(idTiersBk)
                                || !decision.getAnneeDecision().equalsIgnoreCase(anneeBk)) {
                            if (!decision.getIdTiers().equalsIgnoreCase(idTiersBk)) {
                                tiers = new TITiersViewBean();
                                tiers.setSession(getSession());
                                tiers.setIdTiers(decision.getIdTiers());
                                tiers.retrieve();
                                // Suppression des CI cot. pers. pour l'ann?e et
                                // pour ce tiers
                                String nAvs = rechNumAvs(decision, tiers);
                                // if(JadeStringUtil.isEmpty(nAvs)
                                // || ciCorrige.existeCloture(nAvs,
                                // Integer.parseInt(decision.getAnneeDecision()),
                                // sessionPavo, transaction)){
                                if (JadeStringUtil.isEmpty(nAvs)) {
                                    System.out.println("CI vide: " + decision.getNumAffilie());
                                    nePasTraiter = true;
                                }
                                if (!nePasTraiter) {
                                    // Suppression anciennes ?critures
                                    // Parcourir tous les navs du tiers car
                                    // l'ent?te peut ne pas exister pour un n?
                                    // avs
                                    TIHistoriqueAvsManager hAvsMng = new TIHistoriqueAvsManager();
                                    hAvsMng.setSession(getSession());
                                    hAvsMng.setForIdTiers(tiers.getIdTiers());
                                    hAvsMng.find();
                                    for (int i = 0; i < hAvsMng.getSize(); i++) {
                                        TIHistoriqueAvs hAvs = (TIHistoriqueAvs) hAvsMng.getEntity(i);
                                        if (!JadeStringUtil.isEmpty(hAvs.getNumAvs())) {
                                            ciCorrige.supprimeInscriptionsTiers(hAvs.getNumAvs(),
                                                    decision.getAnneeDecision(), sessionPavo, transaction);
                                        }
                                    }
                                }
                                // Test si assist? pour la p?riode de d?cision
                                idTiersBk = decision.getIdTiers();
                                anneeBk = decision.getAnneeDecision();
                            }
                        }
                        // else {
                        // nePasTraiter = true;
                        // }
                        if (!nePasTraiter) {
                            try {
                                String numAvs = majCINumAvs(decision, tiers);
                                // Suppression anciennes ?critures
                                // Inscription

                                // R?cup?ration p?riode CI
                                String moisDebut = Integer.toString(JACalendar.getMonth(decision.getDebutDecision()));
                                String moisFin = Integer.toString(JACalendar.getMonth(decision.getFinDecision()));
                                if (!base.isNew()
                                        && CPDonneesBase.CS_BENEFICE_CAP.equalsIgnoreCase(base.getSourceInformation())) {
                                    moisDebut = "77";
                                    moisFin = "77";
                                }
                                String genre = majCIInitGenre(decision, tiers);

                                if (CPDecision.CS_IMPUTATION.equalsIgnoreCase(decision.getTypeDecision())) {
                                    majCiImputation(transaction, ci, decision, numAvs, montantCI, moisDebut,
                                            moisFin, genre);
                                } else {
                                    if (JadeStringUtil.isEmpty(montantCI)) {
                                        montantCI = "0";
                                    }
                                    ci.verifieCI(transaction, decision.getIdAffiliation(), numAvs, moisDebut, moisFin,
                                            decision.getAnneeDecision(), montantCI, genre,
                                            CICompteIndividuelUtil.MODE_DIRECT, null,
                                            CPListeReinscriptionCI._returnCodeSpecial(decision), "");
                                    // calculer et mettre en n?gatif le CI li?
                                    // au montant pay? comme salari?
                                    if (!JadeStringUtil.isIntegerEmpty(base.getCotisationSalarie())) {
                                        majCiCotiSalarie(transaction, ci, decision, numAvs, montantCI, base,
                                                moisDebut, moisFin, genre);
                                    }
                                }
                                if (getSession().hasErrors()) {
                                    System.out.println(decision.getNumAffilie());
                                    processAppelant.getMemoryLog().logMessage(transaction.getErrors().toString(),
                                            FWMessage.ERREUR, this.getClass().getName());
                                    transaction.rollback();
                                    transaction.clearErrorBuffer();
                                    erreur = getSession().getErrors().toString();
                                }
                                if (transaction.hasErrors()) {
                                    erreur = transaction.getErrors().toString();
                                    System.out.println(decision.getNumAffilie());
                                    processAppelant.getMemoryLog().logMessage(transaction.getErrors().toString(),
                                            FWMessage.ERREUR, this.getClass().getName());
                                    transaction.rollback();
                                    transaction.clearErrorBuffer();
                                } else {
                                    transaction.commit();
                                }
                            } catch (Exception e) {
                                processAppelant.getMemoryLog().logMessage(
                                        decision.getNumAffilie() + " - " + decision.getAnneeDecision() + " - "
                                                + decision.getIdDecision(), FWMessage.INFORMATION,
                                        this.getClass().getName());
                                processAppelant.getTransaction().rollback();
                            } finally {
                                for (Iterator iter = processAppelant.getMemoryLog().getMessagesToVector()
                                        .iterator(); iter.hasNext();) {
                                    messages.add(iter.next());
                                }
                                processAppelant.getMemoryLog().clear();
                            }
                            if (!JadeStringUtil.isEmpty(erreur)) {
                                HSSFRow row = sheet.createRow(nbRow);
                                nbRow++;
                                int colNum = 0;
                                // num?ro affili?
                                HSSFCell cell = row.createCell((short) colNum++);
                                cell.setCellStyle(style);
                                cell.setCellValue(new HSSFRichTextString(decision.getNumAffilie()));
                                // Ann?e
                                cell = row.createCell((short) colNum++);
                                cell.setCellStyle(style);
                                cell.setCellValue(new HSSFRichTextString(decision.getAnneeDecision()));
                                // P?riode d?cision
                                cell = row.createCell((short) colNum++);
                                cell.setCellStyle(style);
                                cell.setCellValue(new HSSFRichTextString(decision.getDebutDecision() + " - "
                                        + decision.getFinDecision()));
                                // Genre
                                cell = row.createCell((short) colNum++);
                                cell.setCellStyle(style);
                                cell.setCellValue(new HSSFRichTextString(globaz.phenix.translation.CodeSystem
                                        .getLibelleCourt(getSession(), decision.getGenreAffilie())));
                                // Erreur
                                cell = row.createCell((short) colNum++);
                                cell.setCellStyle(style);
                                cell.setCellValue(new HSSFRichTextString(erreur));
                            }
                        }
                    }
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
        // Remettre les erreurs des process dans le log
        for (Iterator iter = messages.iterator(); iter.hasNext();) {
            processAppelant.getMemoryLog().getMessagesToVector().add(iter.next());
        }
        manager.cursorClose(statement);
        statement = null;
        HSSFFooter footer = sheet.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Re-inscription");
        return sheet;
    }

    private String rechNumAvs(CPDecisionAffiliation decision, TITiersViewBean tiers) throws Exception {
        String numAvs = "";
        if (BSessionUtil.compareDateFirstLower(getSession(), decision.getFinDecision(), "01.01.2009")) {
            TIHistoriqueAvs hist = new TIHistoriqueAvs();
            hist.setSession(getSession());
            try {
                numAvs = hist.findPrevKnownNumAvs(decision.getIdTiers(), decision.getFinDecision());
                if (JadeStringUtil.isEmpty(numAvs)) {
                    numAvs = hist.findNextKnownNumAvs(decision.getIdTiers(), decision.getDebutDecision());
                }
            } catch (Exception e) {
                numAvs = "";
            }
        }
        // Si aucun n? trouv? dans historique ou NNSS => prendre l'actuel n? avs
        if (JadeStringUtil.isEmpty(numAvs)) {
            numAvs = tiers.getNumAvsActuel();
        }
        return numAvs;
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

    public void setFromNumAffilie(String fromNumAffilie) {
        this.fromNumAffilie = fromNumAffilie;
    }

    public void setManager(CPDecisionAffiliationManager manager) {
        this.manager = manager;
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

    public void setTillAnneeDecision(String tillAnneeDecision) {
        this.tillAnneeDecision = tillAnneeDecision;
    }

    public void setTillNumAffilie(String tillNumAffilie) {
        this.tillNumAffilie = tillNumAffilie;
    }

    /**
     * @param wb
     * @param sheet
     * @param row
     * @return
     */
    private HSSFSheet setTitleRow(HSSFWorkbook wb, HSSFSheet sheet) {

        final String[] COL_TITLES = { session.getLabel("NUM_AFFILIE"), session.getLabel("ANNEE"),
                session.getLabel("PERIODE"), session.getLabel("GENRE_DECISION"),
                session.getLabel("ERREUR") };

        HSSFRow row = null;
        HSSFCell c;
        // cr?ation du style pour le titre de la page
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
        c.setCellValue(new HSSFRichTextString("Reinscription CI pour assist? non clotur?s"));
        c.setCellStyle(style2);
        // crit?res de s?lection
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font3.setFontHeight((short) 200);
        style3.setFont(font3);
        row = sheet.createRow(3);
        row = sheet.createRow(4);
        int i = 4;
        // cr?ation de l'ent?te
        try {
            // S?lection
            String critere = getFromAnneeDecision() + " " + getTillAnneeDecision() + " "
                    + getFromNumAffilie() + " " + getTillNumAffilie();
            if (!JadeStringUtil.isEmpty(critere)) {
                row = sheet.createRow(i++);
                c = row.createCell((short) 0);
                c.setCellValue(new HSSFRichTextString(""));
                c.setCellStyle(style3);
                c = row.createCell((short) 1);
                c.setCellValue(new HSSFRichTextString(session.getLabel("CRITERE")));
                c.setCellStyle(style3);
                c = row.createCell((short) 3);
                c.setCellValue(new HSSFRichTextString(critere));
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
}
