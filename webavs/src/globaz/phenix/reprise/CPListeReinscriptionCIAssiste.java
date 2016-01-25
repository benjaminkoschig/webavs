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
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuelUtil;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.util.CICorrigeCCVDUtil;
import globaz.phenix.db.principale.CPCotisation;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliation;
import globaz.phenix.db.principale.CPDecisionAffiliationManager;
import globaz.phenix.db.principale.CPDecisionManager;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.db.principale.CPDonneesCalcul;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIHistoriqueAvsManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Insérez la description du type ici. Date de création : (17.06.2003 14:38:38)
 * 
 * @author: jpa
 */
public class CPListeReinscriptionCIAssiste {
    private String fromAnneeDecision = "";
    private String fromNumAffilie = "";
    private CPDecisionAffiliationManager manager = null;
    private Vector<Object> messages = new Vector<Object>();
    private boolean miseAjourCI = false;
    private BProcess processAppelant = null;
    private BSession session = null;
    private HSSFSheet sheet;
    private String tillAnneeDecision = "";
    private String tillNumAffilie = "";
    private HSSFWorkbook wb;

    /**
     * Commentaire relatif au constructeur TentDocument.
     */
    public CPListeReinscriptionCIAssiste() {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet("COMP1");
    }

    /**
     * Commentaire relatif au constructeur TentDocument.
     */
    public CPListeReinscriptionCIAssiste(BSession session) {
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
            File f = File.createTempFile(session.getLabel("ListeComplementaire") + JACalendar.today().toStrAMJ()
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

    public boolean isMiseAjourCI() {
        return miseAjourCI;
    }

    private void majCiCotiSalarie(BTransaction transaction, CICompteIndividuelUtil ci, CPDecision deci, String numAvs,
            String montantCI, CPDonneesBase base, String moisDebut, String moisFin, String genre) throws Exception {
        float revenuCiImputation = 0;
        float varMontCI = 0;
        // recherche coti payé en tant que salarié
        float cotiEncode = Float.parseFloat(JANumberFormatter.deQuote(base.getCotisationSalarie()));
        // Calcul du Ci qui doit être imputer selon le montant de cotisation
        // payé en tant que salarié
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
            varMontCI = Math.abs(Float.parseFloat(JANumberFormatter.deQuote(montantCI)));
        }
        if (varMontCI != 0) {
            ci.verifieCI(transaction, deci.getIdAffiliation(), numAvs, moisDebut, moisFin, deci.getAnneeDecision(), "-"
                    + montantCI, genre, CICompteIndividuelUtil.MODE_DIRECT, CIEcriture.CS_CODE_MIS_EN_COMTE,
                    CPToolBox._returnCodeSpecial(deci), "");
        }
    }

    private void majCIImputation(BTransaction transaction, CPDecision deci, String numAvs, String montantCI,
            String moisDebut, String moisFin, CICompteIndividuelUtil ci, String genre, boolean autreNumAffilie)
            throws Exception {
        // Si annule et remplace
        if (Boolean.TRUE.equals(deci.getComplementaire())) {
            // Extourne du total
            java.math.BigDecimal montantEnCI = new BigDecimal(0);
            if (autreNumAffilie) {
                montantEnCI = ci.getSommeParAnneeCodeAmortissement(numAvs, deci.getAffiliation().getAffilieNumero(),
                        genre, deci.getAnneeDecision(), CIEcriture.CS_CODE_MIS_EN_COMTE);
            } else {
                montantEnCI = ci.getSommeParAnneeCodeAmortissement(numAvs, "", genre, deci.getAnneeDecision(),
                        CIEcriture.CS_CODE_MIS_EN_COMTE);
            }
            if (montantEnCI.floatValue() != 0) {
                montantEnCI = montantEnCI.negate();
                ci.verifieCI(transaction, deci.getIdAffiliation(), numAvs, moisDebut, moisFin, deci.getAnneeDecision(),
                        montantEnCI.toString(), genre, CICompteIndividuelUtil.MODE_DIRECT,
                        CIEcriture.CS_CODE_MIS_EN_COMTE, CPToolBox._returnCodeSpecial(deci), "");
            }
            if (!JadeStringUtil.isEmpty(montantCI) && !montantCI.equalsIgnoreCase("0.00")
                    && !montantCI.equalsIgnoreCase("-0.00") && !montantCI.equalsIgnoreCase("0")
                    && !montantCI.equalsIgnoreCase("-0")) {
                // Inscription du nouveau montant
                ci.verifieCI(transaction, deci.getIdAffiliation(), numAvs, moisDebut, moisFin, deci.getAnneeDecision(),
                        "-" + montantCI, genre, CICompteIndividuelUtil.MODE_DIRECT, CIEcriture.CS_CODE_MIS_EN_COMTE,
                        CPToolBox._returnCodeSpecial(deci), "");
            }
        } else {
            // Inscription directe de la bonification
            java.math.BigDecimal montantEnCI = ci.getSommeParAnnee(numAvs, "", genre, deci.getAnneeDecision(),
                    CIEcriture.CS_CODE_AMORTISSEMENT);
            if (!CIEcriture.CS_CIGENRE_7.equalsIgnoreCase(genre)) {
                montantEnCI = montantEnCI.add(ci.getSommeParAnnee(numAvs, "", CIEcriture.CS_CIGENRE_7,
                        deci.getAnneeDecision(), CIEcriture.CS_CODE_AMORTISSEMENT));
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
                        CPToolBox._returnCodeSpecial(deci), "");
            }
        }
    }

    private String majCIInitGenre(CPDecision decision, TITiersViewBean tiers) throws Exception {
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

    private String majCINumAvs(CPDecision decision, TITiersViewBean tiers) throws Exception {
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
        // Si aucun n° trouvé dans historique ou NNSS => prendre l'actuel n° avs
        if (JadeStringUtil.isEmpty(numAvs)) {
            numAvs = tiers.getNumAvsActuel();
        }
        return numAvs;
    }

    public HSSFSheet populateSheet(CPDecisionAffiliationManager manager, BTransaction transaction) throws Exception {

        BSession sessionPavo = null;
        CIJournal journalCi = null;
        CICompteIndividuelUtil ci = null;
        if (isMiseAjourCI()) {
            sessionPavo = (BSession) GlobazSystem.getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).newSession(
                    getSession());
            journalCi = new CIJournal();
            journalCi.setSession(sessionPavo);
            journalCi.setIdTypeInscription(CIJournal.CS_DECISION_COT_PERS);
            String libelleJrn = "Reinscription CI pour assisté non cloturés";
            String rmqJrn = "";
            if (!JadeStringUtil.isEmpty(getFromAnneeDecision())) {
                rmqJrn = libelleJrn + " de " + getFromAnneeDecision();
            }
            if (!getTillAnneeDecision().equalsIgnoreCase(getFromAnneeDecision())) {
                if (!JadeStringUtil.isEmpty(getFromAnneeDecision())) {
                    rmqJrn = rmqJrn + " à " + getTillAnneeDecision();
                } else {
                    rmqJrn = rmqJrn + " jusqu'à " + getTillAnneeDecision();
                }
            }
            if (!JadeStringUtil.isEmpty(getFromNumAffilie())) {
                rmqJrn = rmqJrn + " pour " + getFromNumAffilie();
            }
            if (!getTillNumAffilie().equalsIgnoreCase(getFromNumAffilie())) {
                if (!JadeStringUtil.isEmpty(getFromNumAffilie())) {
                    rmqJrn = rmqJrn + " à " + getTillNumAffilie();
                } else {
                    rmqJrn = rmqJrn + " jusqu'à " + getTillNumAffilie();
                }
            }
            journalCi.setLibelle(libelleJrn);
            journalCi.setRemTexte(rmqJrn);
            journalCi.add(transaction);

            ci = new CICompteIndividuelUtil();
            ci.setSession(sessionPavo);
            ci.setIdJournal(journalCi.getIdJournal());
        }
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
        int nbRow = 11;
        boolean nePasTraiter = false;
        boolean assiste = false;
        String idAffiliationBk = "";
        String idTiersBk = "";
        String idTiersTraite = "";
        String anneeBk = "";
        String idAffBk = "";
        TITiersViewBean tiers = null;
        CICorrigeCCVDUtil ciCorrige = new CICorrigeCCVDUtil();
        AFAffiliation aff = new AFAffiliation();
        try {
            transactionLecture.openTransaction();
            statement = manager.cursorOpen(transactionLecture);

            while (((decision = (CPDecisionAffiliation) manager.cursorReadNext(statement)) != null)
                    && (!decision.isNew())) {
                try {
                    assiste = false;
                    nePasTraiter = false;
                    // Si même tiers et même année => ne rien faire
                    if (!decision.getIdAffiliation().equalsIgnoreCase(idAffiliationBk)
                            || !decision.getAnneeDecision().equalsIgnoreCase(anneeBk)) {
                        if (!decision.getIdTiers().equalsIgnoreCase(idTiersBk)) {
                            tiers = new TITiersViewBean();
                            tiers.setSession(getSession());
                            tiers.setIdTiers(decision.getIdTiers());
                            tiers.retrieve();
                        }
                        // Test si assisté pour la période de décision
                        idTiersBk = decision.getIdTiers();
                        idAffiliationBk = decision.getIdAffiliation();
                        anneeBk = decision.getAnneeDecision();
                        try {
                            assiste = AFParticulariteAffiliation.existeParticulariteOrganesExternes(transaction,
                                    decision.getIdAffiliation(), decision.getDebutDecision());
                        } catch (Exception e) {
                            assiste = false;
                        }
                    } else {
                        assiste = false;
                    }
                    // Impression du cas à modifier
                    if (assiste) {
                        // Suppression des CI cot. pers. pour l'année et pour ce
                        // tiers
                        // Inscription directe de toutes les décisions actives
                        // du tiers pour l'année de décision
                        // (y compris période non assisté)
                        if (!idTiersTraite.equalsIgnoreCase(decision.getIdTiers())) {
                            CPDecisionManager mng1 = new CPDecisionManager();
                            mng1.setForIdTiers(decision.getIdTiers());
                            mng1.setSession(getSession());
                            mng1.setForIsActive(Boolean.TRUE);
                            mng1.setForAnneeDecision(decision.getAnneeDecision());
                            mng1.find();
                            String nAvs = rechNumAvs(decision, tiers);
                            if (JadeStringUtil.isEmpty(nAvs)
                                    || ciCorrige.existeCloture(nAvs, Integer.parseInt(decision.getAnneeDecision()),
                                            sessionPavo, transaction)) {
                                nePasTraiter = true;
                            }
                            if (!nePasTraiter && isMiseAjourCI()) {
                                // Suppression anciennes écritures
                                // Parcourir tous les navs du tiers car l'entête
                                // peut ne pas exister pour un n° avs
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
                            for (int i = 0; i < mng1.getSize(); i++) {
                                CPDecision deci = (CPDecision) mng1.getEntity(i);
                                // Ne rien inscrire si salarié
                                if (!deci.isNew()
                                        && !CPDecision.CS_SALARIE_DISPENSE.equalsIgnoreCase(deci.getSpecification())) {
                                    HSSFRow row = sheet.createRow(nbRow);
                                    nbRow++;
                                    if (!idAffBk.equalsIgnoreCase(deci.getIdAffiliation())) {
                                        idAffBk = deci.getIdAffiliation();
                                        aff.setAffiliationId(deci.getIdAffiliation());
                                        aff.setSession(getSession());
                                        aff.retrieve();
                                    }
                                    deci.setAffiliation(aff);
                                    int colNum = 0;
                                    // numéro affilié
                                    String numAffilie = deci.getAffiliation().getAffilieNumero();
                                    if (nePasTraiter) {
                                        numAffilie = numAffilie + " * ";
                                    }
                                    HSSFCell cell = row.createCell((short) colNum++);
                                    cell.setCellStyle(style);
                                    cell.setCellValue(numAffilie);
                                    // Année
                                    cell = row.createCell((short) colNum++);
                                    cell.setCellStyle(style);
                                    cell.setCellValue(decision.getAnneeDecision());
                                    // Période décision
                                    cell = row.createCell((short) colNum++);
                                    cell.setCellStyle(style);
                                    cell.setCellValue(deci.getDebutDecision() + " - " + deci.getFinDecision());
                                    // Genre
                                    cell = row.createCell((short) colNum++);
                                    cell.setCellStyle(style);
                                    cell.setCellValue(globaz.phenix.translation.CodeSystem.getLibelleCourt(
                                            getSession(), deci.getGenreAffilie()));
                                    if (isMiseAjourCI()) {
                                        try {
                                            if (!nePasTraiter && isMiseAjourCI()) {
                                                String numAvs = majCINumAvs(deci, tiers);
                                                // Suppression anciennes
                                                // écritures
                                                // Inscription
                                                String montantCI = "";
                                                CPDonneesCalcul donnee = new CPDonneesCalcul();
                                                // Si montant forcé à
                                                // l'encodage, prendre celui-ci
                                                // sinon prendre le montant
                                                // calculé
                                                CPDonneesBase base = new CPDonneesBase();
                                                base.setSession(transaction.getSession());
                                                base.setIdDecision(deci.getIdDecision());
                                                base.retrieve(transaction);
                                                if (!base.isNew()) {
                                                    if (!"".equalsIgnoreCase(base.getRevenuCiForce())) {
                                                        montantCI = base.getRevenuCiForce();
                                                    } else {
                                                        donnee.setSession(getSession());
                                                        montantCI = donnee.getMontant(deci.getIdDecision(),
                                                                CPDonneesCalcul.CS_REV_CI);
                                                    }
                                                } else {
                                                    donnee.setSession(transaction.getSession());
                                                    montantCI = donnee.getMontant(deci.getIdDecision(),
                                                            CPDonneesCalcul.CS_REV_CI);
                                                }
                                                // Récupération période CI
                                                String moisDebut = Integer.toString(JACalendar.getMonth(deci
                                                        .getDebutDecision()));
                                                String moisFin = Integer.toString(JACalendar.getMonth(deci
                                                        .getFinDecision()));
                                                if (!base.isNew()
                                                        && CPDonneesBase.CS_BENEFICE_CAP.equalsIgnoreCase(base
                                                                .getSourceInformation())) {
                                                    moisDebut = "77";
                                                    moisFin = "77";
                                                }
                                                String genre = majCIInitGenre(deci, tiers);
                                                if (!JadeStringUtil.isEmpty(montantCI)
                                                        && !montantCI.equalsIgnoreCase("0.00")
                                                        && !montantCI.equalsIgnoreCase("-0.00")
                                                        && !montantCI.equalsIgnoreCase("0")
                                                        && !montantCI.equalsIgnoreCase("-0")) {
                                                    if (CPDecision.CS_IMPUTATION.equalsIgnoreCase(deci
                                                            .getTypeDecision())) {
                                                        majCIImputation(transaction, deci, numAvs, montantCI,
                                                                moisDebut, moisFin, ci, genre, true);
                                                    } else if (!CPDecision.CS_SALARIE_DISPENSE.equals(deci
                                                            .getSpecification())) {
                                                        if (JadeStringUtil.isEmpty(montantCI)) {
                                                            montantCI = "0";
                                                        }
                                                        ci.verifieCI(transaction, deci.getIdAffiliation(), numAvs,
                                                                moisDebut, moisFin, deci.getAnneeDecision(), montantCI,
                                                                genre, CICompteIndividuelUtil.MODE_DIRECT, null,
                                                                CPToolBox._returnCodeSpecial(deci), "");
                                                        // calculer et mettre en
                                                        // négatif le CI lié au
                                                        // montant payé comme
                                                        // salarié
                                                        if (!JadeStringUtil.isIntegerEmpty(base.getCotisationSalarie())) {
                                                            majCiCotiSalarie(transaction, ci, deci, numAvs,
                                                                    montantCI, base, moisDebut, moisFin, genre);
                                                        }
                                                    }
                                                }
                                            }
                                            if (getSession().hasErrors()) {
                                                System.out.println(deci.getAffiliation().getAffilieNumero());
                                                processAppelant.getMemoryLog().logMessage(
                                                        transaction.getErrors().toString(), FWMessage.ERREUR,
                                                        this.getClass().getName());
                                                transaction.rollback();
                                                transaction.clearErrorBuffer();
                                            }
                                            if (transaction.hasErrors()) {
                                                System.out.println(deci.getAffiliation().getAffilieNumero());
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
                                                    decision.getNumAffilie() + " - " + decision.getAnneeDecision()
                                                            + " - " + decision.getIdDecision(), FWMessage.INFORMATION,
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
        // Si aucun n° trouvé dans historique ou NNSS => prendre l'actuel n° avs
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
                session.getLabel("PERIODE"), session.getLabel("GENRE_DECISION") };

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
            c.setCellValue(session.getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase()));
        } catch (Exception e) {
            c.setCellValue("");
        }
        // row = sheet.createRow(1);
        // row = sheet.createRow(2);
        c = row.createCell((short) 5);
        c.setCellValue(JACalendar.todayJJsMMsAAAA());
        c.setCellStyle(style2);
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        c = row.createCell((short) 1);
        c.setCellValue("Reinscription CI pour assisté non cloturés");
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
            // Sélection
            String critere = getFromAnneeDecision() + " " + getTillAnneeDecision() + " "
                    + getFromNumAffilie() + " " + getTillNumAffilie();
            if (!JadeStringUtil.isEmpty(critere)) {
                row = sheet.createRow(i++);
                c = row.createCell((short) 0);
                c.setCellValue("");
                c.setCellStyle(style3);
                c = row.createCell((short) 1);
                c.setCellValue(session.getLabel("CRITERE"));
                c.setCellStyle(style3);
                c = row.createCell((short) 3);
                c.setCellValue(critere);
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
            c.setCellValue(COL_TITLES[j]);
            c.setCellStyle(style);
        }
        return sheet;
    }
}
