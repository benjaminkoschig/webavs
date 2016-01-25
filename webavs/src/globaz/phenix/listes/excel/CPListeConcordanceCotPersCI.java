package globaz.phenix.listes.excel;

import globaz.campus.db.annonces.GEAnnonces;
import globaz.campus.db.annonces.GEAnnoncesManager;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;
import globaz.pavo.db.compte.CICompteIndividuelUtil;
import globaz.phenix.db.principale.CPCotisation;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliationCalcul;
import globaz.phenix.db.principale.CPDecisionAffiliationManager;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.db.principale.CPSortie;
import globaz.phenix.db.principale.CPSortieManager;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIHistoriqueAvsManager;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Insérez la description du type ici. Date de création : (17.06.2003 14:38:38)
 * 
 * @author: jpa
 */
public class CPListeConcordanceCotPersCI {
    private String forIdPassage = "";
    private String fromAnneeDecision = "";
    private String fromDiffAdmise = "";
    private String fromNumAff = "";
    private String fromPeriodeFactu = "";
    private int i = 4;
    private CPDecisionAffiliationManager manager = null;
    private BProcess processAppelant = null;
    private BSession session = null;
    private HSSFSheet sheet;
    private String tillNumAff = "";
    private String tillPeriodeFactu = "";
    private String toAnneeDecision = "";
    private String toDiffAdmise = "";

    private HSSFWorkbook wb;

    /**
     * Commentaire relatif au constructeur TentDocument.
     */
    public CPListeConcordanceCotPersCI() {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet("TENT");
    }

    /**
     * Commentaire relatif au constructeur TentDocument.
     */
    public CPListeConcordanceCotPersCI(BSession session, String fromAnnee, String toAnnee, String fromNumAff,
            String tillNumAff, String fromPeriodeFactu, String tillPeriodeFactu, String forIdPassage) {
        this.session = session;
        wb = new HSSFWorkbook();
        setFromAnneeDecision(fromAnnee);
        setToAnneeDecision(toAnnee);
        setFromNumAff(fromNumAff);
        setTillNumAff(tillNumAff);
        setFromPeriodeFactu(fromPeriodeFactu);
        setTillPeriodeFactu(tillPeriodeFactu);
        setForIdPassage(forIdPassage);
        sheet = wb.createSheet(fromAnnee + " -" + toAnnee);
        HSSFPrintSetup ps = sheet.getPrintSetup();
        // format
        ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        ps.setLandscape(true);
        sheet = setTitleRow(wb, sheet);
    }

    public String getForIdPassage() {
        return forIdPassage;
    }

    /**
     * Returns the fromAnneeDecision.
     * 
     * @return String
     */
    public String getFromAnneeDecision() {
        return fromAnneeDecision;
    }

    public String getFromDiffAdmise() {
        return fromDiffAdmise;
    }

    public String getFromNumAff() {
        return fromNumAff;
    }

    public String getFromPeriodeFactu() {
        return fromPeriodeFactu;
    }

    public CPDecisionAffiliationManager getManager() {
        return manager;
    }

    public String getOutputFile() {
        try {
            File f = File.createTempFile(session.getLabel("LISTE_TITRE") + JACalendar.today().toStrAMJ() + "_", ".xls");
            f.deleteOnExit();
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

    public String getTillPeriodeFactu() {
        return tillPeriodeFactu;
    }

    /**
     * Returns the toAnneeDecision.
     * 
     * @return String
     */
    public String getToAnneeDecision() {
        return toAnneeDecision;
    }

    public String getToDiffAdmise() {
        return toDiffAdmise;
    }

    public HSSFSheet populateSheet(CPDecisionAffiliationManager manager, BTransaction transaction) throws Exception {
        BStatement statement = null;
        CPDecisionAffiliationCalcul decision = null;
        CPDecisionAffiliationCalcul decisionPrecedente = null;
        BigDecimal montantEnCI = null;
        BigDecimal montantCPBD = new BigDecimal("0");
        BigDecimal montantCP = new BigDecimal("0");
        String periodeAffiliation = "";
        String montantCiDecision = "";
        boolean premierEnreg = true;
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
        int nb = manager.getCount(transaction);
        if (nb > 0) {
            processAppelant.setProgressScaleValue(nb);
            statement = manager.cursorOpen(transaction);
            sheet.setColumnWidth((short) 0, (short) 3500);
            sheet.setColumnWidth((short) 1, (short) 5000);
            sheet.setColumnWidth((short) 2, (short) 3000);
            sheet.setColumnWidth((short) 3, (short) 2000);
            sheet.setColumnWidth((short) 4, (short) 5500);
            sheet.setColumnWidth((short) 5, (short) 3500);
            sheet.setColumnWidth((short) 6, (short) 3500);
            sheet.setColumnWidth((short) 7, (short) 3500);
            int nbRow = i++;
            // init limite
            if (JadeStringUtil.isBlank(getFromDiffAdmise())) {
                setFromDiffAdmise("0");
            }
            if (JadeStringUtil.isBlank(getToDiffAdmise())) {
                setToDiffAdmise("0");
            }
            BigDecimal limInf = new BigDecimal(getFromDiffAdmise());
            BigDecimal limSup = new BigDecimal(getToDiffAdmise());
            while (((decision = (CPDecisionAffiliationCalcul) manager.cursorReadNext(statement)) != null)
                    && (!decision.isNew()) && !processAppelant.isAborted()) {
                try {
                    if (JadeStringUtil.isEmpty(decision.getMontant()) || Boolean.FALSE.equals(decision.getActive())) {
                        montantCiDecision = "0";
                    } else {
                        montantCiDecision = decision.getMontant();
                    }
                    // Calculer et imputer le CI selon le montant payé comme salarié
                    if (CPDecision.CS_SALARIE_DISPENSE.equalsIgnoreCase(decision.getSpecification())) {
                        montantCP = new BigDecimal("0");
                    } else {
                        CPDonneesBase base = new CPDonneesBase();
                        base.setSession(getSession());
                        base.setIdDecision(decision.getIdDecision());
                        base.retrieve();
                        if (!base.isNew() && !JadeStringUtil.isIntegerEmpty(base.getCotisationSalarie())) {
                            float revenuCiImputation = 0;
                            // recherche coti payé en tant que salarié
                            float cotiEncode = Float.parseFloat(JANumberFormatter.deQuote(base.getCotisationSalarie()));
                            // Calcul du Ci qui doit être imputer selon le montant
                            // de cotisation payé en tant que salarié
                            CPCotisation coti = CPCotisation._returnCotisation(getSession(), decision.getIdDecision(),
                                    CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
                            if (coti != null) {
                                // Calcul du CI
                                revenuCiImputation = (Float.parseFloat(JANumberFormatter.deQuote(coti
                                        .getMontantAnnuel())) - cotiEncode) * (float) 9.9;
                                revenuCiImputation = JANumberFormatter.round(revenuCiImputation, 1, 2,
                                        JANumberFormatter.NEAR);
                                revenuCiImputation = Float.parseFloat(JANumberFormatter.deQuote(montantCiDecision))
                                        - revenuCiImputation;
                            } else {
                                revenuCiImputation = cotiEncode * (float) 9.9;
                                revenuCiImputation = JANumberFormatter.round(revenuCiImputation, 1, 2,
                                        JANumberFormatter.NEAR);
                            }
                            montantCP = new BigDecimal(Float.parseFloat(JANumberFormatter.deQuote(montantCiDecision))
                                    - revenuCiImputation);
                        } else {
                            montantCP = new BigDecimal(JANumberFormatter.deQuote(montantCiDecision));
                        }
                    }
                    // Si la décision n'est plus comprise dans l'affiliation =>
                    // montant extourné
                    // donc montant de décision = 0 et on affiche la période
                    // d'affiliation
                    if (CPDecision.CS_SORTIE.equalsIgnoreCase(decision.getEtat())) {
                        montantCP = new BigDecimal("0");
                    } else if ((!JadeStringUtil.isIntegerEmpty(decision.getFinAffiliation()) && BSessionUtil
                            .compareDateFirstGreaterOrEqual(getSession(), decision.getDebutDecision(),
                                    decision.getFinAffiliation()))
                            || (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), decision.getFinDecision(),
                                    decision.getDebutAffiliation()))) {
                        montantCP = new BigDecimal("0");
                    } else if (decision.getDebutAffiliation().equalsIgnoreCase(decision.getFinAffiliation())) {
                        montantCP = new BigDecimal("0");
                    } else if (CPDecision.CS_IMPUTATION.equalsIgnoreCase(decision.getTypeDecision())) {
                        montantCP = new BigDecimal(JANumberFormatter.deQuote("-" + montantCiDecision));
                    }
                    // Si la décision est en cours de sortie => prendre les montants
                    // des sorties
                    CPSortieManager sortieManager = new CPSortieManager();
                    sortieManager.setSession(getSession());
                    sortieManager.setForIdTiers(decision.getIdTiers());
                    sortieManager.setForIdDecision(decision.getIdDecision());
                    sortieManager.setForAnnee(decision.getAnneeDecision());
                    sortieManager.setForChecked(Boolean.FALSE);
                    sortieManager.find();
                    if (sortieManager.size() > 0) {
                        montantCP = new BigDecimal("0");
                    }
                    for (int i = 0; i < sortieManager.size(); i++) {
                        String montantSortie = ((CPSortie) sortieManager.getEntity(i)).getMontantCI();
                        if (JadeStringUtil.isEmpty(montantSortie)) {
                            montantSortie = "0";
                        }
                        montantCP = montantCP.subtract(new BigDecimal(JANumberFormatter.deQuote(montantSortie)));
                    }
                    // SI Etudiant => regarder si le cas a été inscrit au CI dans
                    // Campus
                    if (CPDecision.CS_ETUDIANT.equalsIgnoreCase(decision.getGenreAffilie())) {
                        // Regarder si l'état dans Campus est "Inscrit au CI"
                        GEAnnoncesManager annMan = new GEAnnoncesManager();
                        annMan.setSession(getSession());
                        annMan.setForIdDecision(decision.getIdDecision());
                        annMan.setForCsEtatAnnonce(GEAnnonces.CS_ETAT_COMPTABILISE);
                        annMan.find();
                        if (annMan.getSize() == 0) {
                            montantCP = new BigDecimal("0");
                        }
                    }
                    // Si tiers ou année différente => recherche du montant CI
                    if (!premierEnreg
                            && (!decision.getIdTiers().equalsIgnoreCase(decisionPrecedente.getIdTiers()) || !decision
                                    .getAnneeDecision().equalsIgnoreCase(decisionPrecedente.getAnneeDecision()))) {
                        // Recherche du n° avs correspondant à l'année de décision
                        String numAvs = "";
                        String numAvsBk = "";
                        TIHistoriqueAvsManager histManager = new TIHistoriqueAvsManager();
                        histManager.setSession(getSession());
                        histManager.setForIdTiers(decisionPrecedente.getIdTiers());
                        histManager.orderByNumAvs();
                        histManager.find();
                        // try {
                        // numAvs=hist.findPrevKnownNumAvs(decisionPrecedente.getIdTiers(),"31.12."+decisionPrecedente.getAnneeDecision());
                        // if(JadeStringUtil.isEmpty(numAvs)){
                        // numAvs=hist.findNextKnownNumAvs(decisionPrecedente.getIdTiers(),"01.01."+decisionPrecedente.getAnneeDecision());
                        // }
                        // } catch (Exception e) {
                        // numAvs="";
                        // }
                        montantEnCI = new BigDecimal("0");
                        // boolean autreNumAffilie = this.rechercheAutreAffilie(decisionPrecedente);
                        // String genre = "";
                        // if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(decisionPrecedente.getGenreAffilie())
                        // || CPDecision.CS_ETUDIANT.equalsIgnoreCase(decisionPrecedente.getGenreAffilie())) {
                        // if (decisionPrecedente.getTiers().isRentier(
                        // Integer.parseInt(decisionPrecedente.getAnneeDecision()))) {
                        // genre = CIEcriture.CS_CIGENRE_7;
                        // } else {
                        // genre = CIEcriture.CS_CIGENRE_4;
                        // }
                        // } else if (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(decisionPrecedente.getGenreAffilie()))
                        // {
                        // genre = CIEcriture.CS_CIGENRE_3;
                        // } else if (CPDecision.CS_RENTIER.equalsIgnoreCase(decisionPrecedente.getGenreAffilie())) {
                        // genre = CIEcriture.CS_CIGENRE_7;
                        // } else if (CPDecision.CS_AGRICULTEUR.equalsIgnoreCase(decisionPrecedente.getGenreAffilie()))
                        // {
                        // genre = CIEcriture.CS_CIGENRE_9;
                        // } else if (CPDecision.CS_TSE.equalsIgnoreCase(decisionPrecedente.getGenreAffilie())) {
                        // genre = CIEcriture.CS_CIGENRE_2;
                        // }
                        for (int i = 0; i < histManager.size(); i++) {
                            TIHistoriqueAvs histo = ((TIHistoriqueAvs) histManager.getEntity(i));
                            numAvs = histo.getNumAvs();
                            if (!numAvs.equalsIgnoreCase(numAvsBk)
                                    && !"506006".equalsIgnoreCase(histo.getMotifHistorique())) {
                                numAvsBk = numAvs;
                                CICompteIndividuelUtil compteIndividuel = new CICompteIndividuelUtil();
                                compteIndividuel.setSession(getSession());
                                montantEnCI = montantEnCI.add(compteIndividuel.getSommeParAnneeCotPers(numAvs,
                                        decisionPrecedente.getAnneeDecision(), Boolean.FALSE));
                                // if (autreNumAffilie) {
                                // montantEnCI = montantEnCI.add(compteIndividuel.getSommeParAnneeNoAffilie(numAvs,
                                // decisionPrecedente.getNumAffilie(), genre,
                                // decisionPrecedente.getAnneeDecision(), CIEcriture.CS_CODE_AMORTISSEMENT));
                                // if (!CIEcriture.CS_CIGENRE_7.equalsIgnoreCase(genre)) {
                                // montantEnCI = montantEnCI.add(compteIndividuel.getSommeParAnneeNoAffilie(numAvs,
                                // decisionPrecedente.getNumAffilie(), CIEcriture.CS_CIGENRE_7,
                                // decisionPrecedente.getAnneeDecision(), CIEcriture.CS_CODE_AMORTISSEMENT));
                                // }
                                // } else {
                                // montantEnCI = montantEnCI.add(compteIndividuel.getSommeParAnnee(numAvs, "", genre,
                                // decisionPrecedente.getAnneeDecision(), CIEcriture.CS_CODE_AMORTISSEMENT));
                                // if (!CIEcriture.CS_CIGENRE_7.equalsIgnoreCase(genre)) {
                                // montantEnCI = montantEnCI.add(compteIndividuel.getSommeParAnnee(numAvs, "",
                                // CIEcriture.CS_CIGENRE_7, decisionPrecedente.getAnneeDecision(),
                                // CIEcriture.CS_CODE_AMORTISSEMENT));
                                // }
                                // }
                            }
                        }
                        // Impression si montant CI et cot. pers sont différent
                        BigDecimal diff = montantCPBD.subtract(montantEnCI);
                        if ((diff.compareTo(limInf) == -1) || (diff.compareTo(limSup) == 1)) {
                            HSSFRow row = sheet.createRow(nbRow);
                            nbRow++;
                            int colNum = 0;
                            // numéro affilié
                            HSSFCell cell = row.createCell((short) colNum++);
                            cell.setCellStyle(style);
                            cell.setCellValue(new HSSFRichTextString(decisionPrecedente.getNumAffilie()));
                            // numéro avs
                            cell = row.createCell((short) colNum++);
                            cell.setCellStyle(style);
                            cell.setCellValue(new HSSFRichTextString(numAvsBk));
                            // Genre
                            cell = row.createCell((short) colNum++);
                            cell.setCellStyle(style);
                            cell.setCellValue(new HSSFRichTextString(globaz.phenix.translation.CodeSystem
                                    .getLibelleCourt(getSession(), decisionPrecedente.getGenreAffilie())));
                            // Année
                            cell = row.createCell((short) colNum++);
                            cell.setCellStyle(style);
                            cell.setCellValue(new HSSFRichTextString(decisionPrecedente.getAnneeDecision()));
                            // periode affiliation
                            if ((!JadeStringUtil.isIntegerEmpty(decisionPrecedente.getFinAffiliation()) && BSessionUtil
                                    .compareDateFirstGreaterOrEqual(getSession(),
                                            decisionPrecedente.getDebutDecision(),
                                            decisionPrecedente.getFinAffiliation()))
                                    || (BSessionUtil.compareDateFirstLowerOrEqual(getSession(),
                                            decisionPrecedente.getFinDecision(),
                                            decisionPrecedente.getDebutAffiliation()))) {
                                periodeAffiliation = decisionPrecedente.getDebutAffiliation() + " - "
                                        + decisionPrecedente.getFinAffiliation();
                            } else {
                                periodeAffiliation = "";
                                if (decisionPrecedente.getDebutAffiliation().length() > 0) {
                                    periodeAffiliation = decisionPrecedente.getDebutAffiliation();
                                }
                                if (decisionPrecedente.getFinAffiliation().length() > 0) {
                                    periodeAffiliation += " - " + decisionPrecedente.getFinAffiliation();
                                }
                            }
                            cell = row.createCell((short) colNum++);
                            cell.setCellStyle(style);
                            cell.setCellValue(new HSSFRichTextString(periodeAffiliation));
                            // Montant CP
                            cell = row.createCell((short) colNum++);
                            cell.setCellStyle(style2);
                            cell.setCellValue(new HSSFRichTextString(montantCPBD.toString()));
                            // Montant CI
                            cell = row.createCell((short) colNum++);
                            cell.setCellStyle(style2);
                            cell.setCellValue(new HSSFRichTextString(montantEnCI.toString()));
                            // Différence
                            cell = row.createCell((short) colNum++);
                            cell.setCellStyle(style2);
                            cell.setCellValue(new HSSFRichTextString(diff.toString()));
                        }
                        montantCPBD = new BigDecimal("0");
                    }
                    montantCPBD = montantCPBD.add(montantCP);
                    decisionPrecedente = decision;
                    premierEnreg = false;
                    processAppelant.incProgressCounter();
                } catch (Exception e) {
                    processAppelant.getMemoryLog().logMessage(
                            decision.getNumAffilie() + " - " + decision.getAnneeDecision() + " - "
                                    + decision.getIdDecision(), FWMessage.INFORMATION, this.getClass().getName());
                }
            }
            // Recherche du n° avs correspondant à l'année de décision
            String numAvs = "";
            String numAvsBk = "";
            // TIHistoriqueAvs hist = new TIHistoriqueAvs();
            // hist.setSession(getSession());
            // try {
            // numAvs=hist.findPrevKnownNumAvs(decisionPrecedente.getIdTiers(),"31.12."+decisionPrecedente.getAnneeDecision());
            // if(JadeStringUtil.isEmpty(numAvs)){
            // numAvs=hist.findNextKnownNumAvs(decisionPrecedente.getIdTiers(),"01.01."+decisionPrecedente.getAnneeDecision());
            // }
            // } catch (Exception e) {
            // numAvs="";
            // }
            // CICompteIndividuelUtil compteIndividuel = new
            // CICompteIndividuelUtil();
            // compteIndividuel.setSession(getSession());
            // montantEnCI =
            // compteIndividuel.getSommeParAnneeCotPers(numAvs,decisionPrecedente.getAnneeDecision());
            //
            TIHistoriqueAvsManager histManager = new TIHistoriqueAvsManager();
            histManager.setSession(getSession());
            histManager.setForIdTiers(decisionPrecedente.getIdTiers());
            histManager.orderByNumAvs();
            histManager.find();
            // try {
            // numAvs=hist.findPrevKnownNumAvs(decisionPrecedente.getIdTiers(),"31.12."+decisionPrecedente.getAnneeDecision());
            // if(JadeStringUtil.isEmpty(numAvs)){
            // numAvs=hist.findNextKnownNumAvs(decisionPrecedente.getIdTiers(),"01.01."+decisionPrecedente.getAnneeDecision());
            // }
            // } catch (Exception e) {
            // numAvs="";
            // }
            montantEnCI = new BigDecimal("0");
            // boolean autreNumAffilie = this.rechercheAutreAffilie(decisionPrecedente);
            // String genre = "";
            // if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(decisionPrecedente.getGenreAffilie())
            // || CPDecision.CS_ETUDIANT.equalsIgnoreCase(decisionPrecedente.getGenreAffilie())) {
            // if (decisionPrecedente.getTiers().isRentier(Integer.parseInt(decisionPrecedente.getAnneeDecision()))) {
            // genre = CIEcriture.CS_CIGENRE_7;
            // } else {
            // genre = CIEcriture.CS_CIGENRE_4;
            // }
            // } else if (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(decisionPrecedente.getGenreAffilie())) {
            // genre = CIEcriture.CS_CIGENRE_3;
            // } else if (CPDecision.CS_RENTIER.equalsIgnoreCase(decisionPrecedente.getGenreAffilie())) {
            // genre = CIEcriture.CS_CIGENRE_7;
            // } else if (CPDecision.CS_AGRICULTEUR.equalsIgnoreCase(decisionPrecedente.getGenreAffilie())) {
            // genre = CIEcriture.CS_CIGENRE_9;
            // } else if (CPDecision.CS_TSE.equalsIgnoreCase(decisionPrecedente.getGenreAffilie())) {
            // genre = CIEcriture.CS_CIGENRE_2;
            // }
            for (int i = 0; i < histManager.size(); i++) {
                TIHistoriqueAvs histo = ((TIHistoriqueAvs) histManager.getEntity(i));
                numAvs = histo.getNumAvs();
                if (!numAvs.equalsIgnoreCase(numAvsBk) && !"506006".equalsIgnoreCase(histo.getMotifHistorique())) {
                    numAvsBk = numAvs;
                    CICompteIndividuelUtil compteIndividuel = new CICompteIndividuelUtil();
                    compteIndividuel.setSession(getSession());
                    montantEnCI = montantEnCI.add(compteIndividuel.getSommeParAnneeCotPers(numAvs,
                            decisionPrecedente.getAnneeDecision(), Boolean.FALSE));
                    // if (autreNumAffilie) {
                    // montantEnCI = montantEnCI.add(compteIndividuel.getSommeParAnneeNoAffilie(numAvs,
                    // decisionPrecedente.getNumAffilie(), genre, decisionPrecedente.getAnneeDecision(),
                    // CIEcriture.CS_CODE_AMORTISSEMENT));
                    // if (!CIEcriture.CS_CIGENRE_7.equalsIgnoreCase(genre)) {
                    // montantEnCI = montantEnCI.add(compteIndividuel.getSommeParAnneeNoAffilie(numAvs,
                    // decisionPrecedente.getNumAffilie(), CIEcriture.CS_CIGENRE_7,
                    // decisionPrecedente.getAnneeDecision(), CIEcriture.CS_CODE_AMORTISSEMENT));
                    // }
                    // } else {
                    // montantEnCI = montantEnCI.add(compteIndividuel.getSommeParAnnee(numAvs, "", genre,
                    // decisionPrecedente.getAnneeDecision(), CIEcriture.CS_CODE_AMORTISSEMENT));
                    // if (!CIEcriture.CS_CIGENRE_7.equalsIgnoreCase(genre)) {
                    // montantEnCI = montantEnCI.add(compteIndividuel.getSommeParAnnee(numAvs, "",
                    // CIEcriture.CS_CIGENRE_7, decisionPrecedente.getAnneeDecision(),
                    // CIEcriture.CS_CODE_AMORTISSEMENT));
                    // }
                    // }
                }
            }
            // Impression du dernier cas
            BigDecimal diff = montantCPBD.subtract(montantEnCI);
            if ((diff.compareTo(limInf) == -1) || (diff.compareTo(limSup) == 1)) {
                HSSFRow row = sheet.createRow(nbRow);
                int colNum = 0;
                // numéro affilié
                HSSFCell cell = row.createCell((short) colNum++);
                cell.setCellStyle(style);
                cell.setCellValue(new HSSFRichTextString(decisionPrecedente.getNumAffilie()));
                // numéro avs
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style);
                cell.setCellValue(new HSSFRichTextString(numAvsBk));
                // Genre
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style);
                cell.setCellValue(new HSSFRichTextString(globaz.phenix.translation.CodeSystem.getLibelleCourt(
                        getSession(), decisionPrecedente.getGenreAffilie())));
                // Année
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style);
                cell.setCellValue(new HSSFRichTextString(decisionPrecedente.getAnneeDecision()));
                // periode affiliation
                if ((!JadeStringUtil.isIntegerEmpty(decisionPrecedente.getFinAffiliation()) && BSessionUtil
                        .compareDateFirstGreaterOrEqual(getSession(), decisionPrecedente.getDebutDecision(),
                                decisionPrecedente.getFinAffiliation()))
                        || (BSessionUtil.compareDateFirstLowerOrEqual(getSession(),
                                decisionPrecedente.getFinDecision(), decisionPrecedente.getDebutAffiliation()))) {
                    periodeAffiliation = decisionPrecedente.getDebutAffiliation() + " - "
                            + decisionPrecedente.getFinAffiliation();
                } else {
                    periodeAffiliation = "";
                    if (decisionPrecedente.getDebutAffiliation().length() > 0) {
                        periodeAffiliation = decisionPrecedente.getDebutAffiliation();
                    }
                    if (decisionPrecedente.getFinAffiliation().length() > 0) {
                        periodeAffiliation += " - " + decisionPrecedente.getFinAffiliation();
                    }
                }
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style);
                cell.setCellValue(new HSSFRichTextString(periodeAffiliation));
                // Montant CP
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style2);
                cell.setCellValue(new HSSFRichTextString(montantCPBD.toString()));
                // Montant CI
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style2);
                cell.setCellValue(new HSSFRichTextString(montantEnCI.toString()));
                // Différence
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style2);
                cell.setCellValue(new HSSFRichTextString((montantCPBD.subtract(montantEnCI)).toString()));
            }
            manager.cursorClose(statement);
            statement = null;
        }
        HSSFFooter footer = sheet.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0120CCP");
        return sheet;
    }

    public void setForIdPassage(String forIdPassage) {
        this.forIdPassage = forIdPassage;
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

    public void setFromDiffAdmise(String fromDiffAdmise) {
        this.fromDiffAdmise = fromDiffAdmise;
    }

    public void setFromNumAff(String fromNumAff) {
        this.fromNumAff = fromNumAff;
    }

    public void setFromPeriodeFactu(String fromPeriodeFactu) {
        this.fromPeriodeFactu = fromPeriodeFactu;
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

    public void setTillNumAff(String tillNumAff) {
        this.tillNumAff = tillNumAff;
    }

    public void setTillPeriodeFactu(String tillPeriodeFactu) {
        this.tillPeriodeFactu = tillPeriodeFactu;
    }

    /**
     * @param wb
     * @param sheet
     * @param row
     * @return
     */
    private HSSFSheet setTitleRow(HSSFWorkbook wb, HSSFSheet sheet) {

        final String[] COL_TITLES = { session.getLabel("NUM_AFFILIE"), session.getLabel("NUM_AVS"),
                session.getLabel("GENRE_DECISION"), session.getLabel("ANNEE"), session.getLabel("PERIODE_AFFILIATION"),
                session.getLabel("LISTE_MONTANT_CP"), session.getLabel("LISTE_MONTANT_CI"),
                session.getLabel("LISTE_DIFFERENCE") };

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
        c.setCellValue(new HSSFRichTextString(session.getLabel("LISTE_TITRE")));
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
        // int i=4;
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
            // Période facturation début
            if (getFromPeriodeFactu().length() > 0) {
                row = sheet.createRow(i++);
                c = row.createCell((short) 0);
                c.setCellValue(new HSSFRichTextString(""));
                c.setCellStyle(style3);
                c = row.createCell((short) 1);
                c.setCellValue(new HSSFRichTextString(session.getLabel("LISTE_PERIODE_FACTU_DEB")));
                c.setCellStyle(style3);
                c = row.createCell((short) 3);
                c.setCellValue(new HSSFRichTextString(getFromPeriodeFactu()));
                c.setCellStyle(style3);
            }
            // Période facturation fin
            if (getTillPeriodeFactu().length() > 0) {
                row = sheet.createRow(i++);
                c = row.createCell((short) 0);
                c.setCellValue(new HSSFRichTextString(""));
                c.setCellStyle(style3);
                c = row.createCell((short) 1);
                c.setCellValue(new HSSFRichTextString(session.getLabel("LISTE_PERIODE_FACTU_FIN")));
                c.setCellStyle(style3);
                c = row.createCell((short) 3);
                c.setCellValue(new HSSFRichTextString(getTillPeriodeFactu()));
                c.setCellStyle(style3);
            }
            // Numéro de passage en factu
            if (getForIdPassage().length() > 0) {
                row = sheet.createRow(i++);
                c = row.createCell((short) 0);
                c.setCellValue(new HSSFRichTextString(""));
                c.setCellStyle(style3);
                c = row.createCell((short) 1);
                c.setCellValue(new HSSFRichTextString(session.getLabel("LISTE_NUMERO_PASSAGE")));
                c.setCellStyle(style3);
                c = row.createCell((short) 3);
                c.setCellValue(new HSSFRichTextString(getForIdPassage()));
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
        row = sheet.createRow(i++);
        row = sheet.createRow(i++);
        row = sheet.createRow(i++);
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

    public void setToDiffAdmise(String toDiffAdmise) {
        this.toDiffAdmise = toDiffAdmise;
    }

}
