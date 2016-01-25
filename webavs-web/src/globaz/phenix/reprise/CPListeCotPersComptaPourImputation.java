package globaz.phenix.reprise;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWIncrementation;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.ServicesFacturation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliationCotisation;
import globaz.phenix.db.principale.CPDecisionAffiliationCotisationManager;
import globaz.phenix.db.principale.CPDecisionAffiliationManager;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.db.principale.CPSortie;
import globaz.phenix.db.principale.CPSortieManager;
import globaz.phenix.db.principale.CPSortieMontant;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
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
public class CPListeCotPersComptaPourImputation {
    private String fromAnneeDecision = "";
    private String fromNumAff = "";
    private String idPassage = "";
    private CPDecisionAffiliationManager manager = null;
    private BProcess processAppelant = null;
    private BSession session = null;
    private HSSFSheet sheet;
    private String tillNumAff = "";
    private String toAnneeDecision = "";
    private HSSFWorkbook wb;

    /**
     * Commentaire relatif au constructeur TentDocument.
     */
    public CPListeCotPersComptaPourImputation() {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet("COMP1");
    }

    /**
     * Commentaire relatif au constructeur TentDocument.
     */
    public CPListeCotPersComptaPourImputation(BSession session, String fromAnnee, String toAnnee, String fromNumAff,
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
            File f = File.createTempFile(session.getLabel("LISTE_TITRE1") + JACalendar.today().toStrAMJ() + "_",
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

    /**
     * Retourne le nombre de mois à facturer pour une cotisation Date de création : (30.04.2003 14:14:07)
     * 
     * @return int
     * @param session
     *            globaz.globall.db.BSession
     * @param transaction
     *            globaz.globall.db.BTransaction
     * @param cotisation
     *            globaz.phenix.db.principale.CPCotisation
     * @param affiliation
     *            globaz.naos.db.affiliation.AFAffiliation
     * @param passage
     *            globaz.musca.api.IFAPassage
     */
    public int nbMoisFacture(BSession session, CPDecisionAffiliationCotisation cotisation, IFAPassage passage) {
        // Si l'année de la décision concerne l'année en cours, il faut calculer
        // le nombre
        // de mois selon la dernière facturation périodique (afin de ne pas
        // facturer à double)
        try {
            int anneeDecision = JACalendar.getYear(cotisation.getDebutCotisation());
            int moisDebut = 0;
            int moisFin = 0;
            boolean calculProrata = false;
            String dateDebut = cotisation.getDebutCotisation();
            String dateFin = cotisation.getFinCotisation();
            // Si l'affilié est radié pendant la période de décision, les dates
            // de début
            // et de fin doivent être prise par rapport à la cotisation de
            // l'affiliation
            AFCotisation cotiAf = new AFCotisation();
            cotiAf.setSession(session);
            cotiAf.setCotisationId(cotisation.getIdCotiAffiliation());
            cotiAf.retrieve();
            int anneeDebutCotisAf = JACalendar.getYear(cotiAf.getDateDebut());
            int anneeFinAcotiAf = JACalendar.getYear(cotiAf.getDateFin());
            if ((anneeDebutCotisAf == anneeDecision)
                    && (BSessionUtil.compareDateFirstGreater(session, cotiAf.getDateDebut(),
                            cotisation.getDebutCotisation()))) {
                moisDebut = JACalendar.getMonth(cotiAf.getDateDebut());
                if (anneeDecision != JACalendar.getYear(JACalendar.today().toString())) {
                    dateDebut = cotiAf.getDateDebut();
                    calculProrata = true;
                }
            } else {
                moisDebut = JACalendar.getMonth(cotisation.getDebutCotisation());
            }
            if ((anneeFinAcotiAf == anneeDecision)
                    && (BSessionUtil.compareDateFirstLower(session, cotiAf.getDateFin(), cotisation.getFinCotisation()))) {
                moisFin = JACalendar.getMonth(cotiAf.getDateFin());
                if (anneeDecision != JACalendar.getYear(JACalendar.today().toString())) {
                    dateFin = cotiAf.getDateFin();
                    calculProrata = true;
                }
            } else {
                moisFin = JACalendar.getMonth(cotisation.getFinCotisation());
            }

            int ecart = 0;
            // Cas sortie non repris
            if (calculProrata) {
                cotisation.setMontantAnnuel(CPToolBox.getMontantProRata(dateDebut, dateFin,
                        JANumberFormatter.deQuote(cotisation.getMontantAnnuel())));
                return 88;
            }
            if (passage != null) {
                // Si l'anné de la dernière facturation périodique est plus
                // grande que la date de fin de la décision => tout est rétro
                int anneeCotisation = JACalendar.getYear(cotisation.getFinCotisation());
                int anneePassage = JACalendar.getYear(passage.getDatePeriode());
                if (anneeCotisation < anneePassage) {
                    return 99; // => Il sera pris le montant de la cotisation
                    // annuelle
                }
                // Si la date de début de décision > date de dernière
                // facturation
                // => ne rien facturer, le cas sera pris lors de la périodique
                else if (BSessionUtil.compareDateFirstGreater(session, cotisation.getDebutCotisation(),
                        "01." + passage.getDatePeriode())) {
                    return 0;
                }
                // Détermination de la période minimum que le passage doit avoir
                // pour que la cotisation soit entièrement retro active.
                // La détermination de cette date doit tenir compte de la
                // périodicité.
                // Ex: Si dernierPassage= 02.2007 => si coti = 1.01.07 au
                // 31.01.07 => rétro total pour cas mensuel mais 0 si
                // trimestriel
                int moisMaxPossible = 12;
                int moisMinPossible = 0;
                int moisPassage = JACalendar.getMonth(passage.getDatePeriode());
                if (CodeSystem.PERIODICITE_MENSUELLE.equalsIgnoreCase(cotiAf.getPeriodicite())) {
                    moisMaxPossible = moisPassage;
                } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equalsIgnoreCase(cotiAf.getPeriodicite())) {
                    if (moisPassage < 3) {
                        moisMaxPossible = 0;
                    } else if (moisPassage < 6) {
                        moisMaxPossible = 3;
                    } else if (moisPassage < 9) {
                        moisMaxPossible = 6;
                    } else if (moisPassage < 12) {
                        moisMaxPossible = 9;
                    } else {
                        moisMaxPossible = 12;
                    }

                } else if (CodeSystem.PERIODICITE_ANNUELLE.equalsIgnoreCase(cotiAf.getPeriodicite())) {
                    if (!JadeStringUtil.isIntegerEmpty(cotiAf.getTraitementMoisAnnee())) {
                        moisMinPossible = Integer
                                .parseInt(JadeStringUtil.substring(cotiAf.getTraitementMoisAnnee(), 4)) + 1;
                    } else {
                        moisMinPossible = 13;
                    }
                }
                if ((moisFin <= moisMaxPossible) && (moisPassage > moisMinPossible)) {
                    return 99; // => Il sera pris le montant de la cotisation
                } else {
                    // sinon il faut prendre jusqu'à la dernière facturation
                    // afin
                    // de ne pas facturer à double avec la facturation
                    // périodique.
                    int anneeFacturation = JACalendar.getYear(passage.getDatePeriode());
                    moisFin = JACalendar.getMonth(passage.getDatePeriode());
                    // Test si la dernière facturation concerne la périodicité
                    // de l'affiliation
                    // si C'est pas le cas redéterminer le mois de fin.
                    if (cotiAf.getPeriodicite().equalsIgnoreCase(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
                        moisFin = (moisFin / 3) * 3;
                    } else if (cotiAf.getPeriodicite().equalsIgnoreCase("802003")) {
                        moisFin = (moisFin / 6) * 6;
                    } else if (cotiAf.getPeriodicite().equalsIgnoreCase(CodeSystem.PERIODICITE_ANNUELLE)) {
                        moisFin = (moisFin / 12) * 12;
                    }
                    if (anneeFacturation < anneeDecision) {
                        moisDebut = moisDebut + 12;
                        ecart = moisDebut - moisFin;
                        return ecart;
                    } else {
                        ecart = (moisFin - moisDebut) + 1;
                        return ecart;
                    }
                }
            } else {
                // Aucune facturation périodique de faite
                return 0;
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            e.printStackTrace();
            return 0;
        }
    }

    public HSSFSheet populateSheet(CPDecisionAffiliationCotisationManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = null;
        CPDecisionAffiliationCotisation cotisation = null;
        CPDecisionAffiliationCotisation cotisationPrecedente = null;
        BigDecimal montantEnCompta = null;
        BigDecimal montantCotiBD = new BigDecimal("0");
        BigDecimal montantCoti = new BigDecimal("0");
        String periodeAffiliation = "";
        String montantCotisation = "";
        CPDonneesBase base = null;
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
        processAppelant.setProgressScaleValue(manager.getCount(transaction));
        statement = manager.cursorOpen(transaction);
        sheet.setColumnWidth((short) 0, (short) 3500);
        sheet.setColumnWidth((short) 1, (short) 5500);
        sheet.setColumnWidth((short) 2, (short) 3000);
        sheet.setColumnWidth((short) 3, (short) 6500);
        sheet.setColumnWidth((short) 4, (short) 4000);
        sheet.setColumnWidth((short) 5, (short) 4000);
        sheet.setColumnWidth((short) 6, (short) 4000);
        sheet.setColumnWidth((short) 7, (short) 4000);
        sheet.setColumnWidth((short) 8, (short) 4000);
        // Recherche du dernier passage périodique comptabilisé
        IFAPassage myPassage = ServicesFacturation.getDernierPassageFacturation(getSession(), null,
                globaz.musca.db.facturation.FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS);
        int nbRow = 11;
        // Recherche compteur décision (CPDECIP)
        int comtpeur = 0;
        FWIncrementation inc = new FWIncrementation();
        inc.setIdIncrement("CPDECIP");
        inc.setIdCodeSysteme("0");
        inc.setSession(getSession());
        inc.setAnneeIncrement("0");
        inc.retrieve();
        if (!inc.isNew()) {
            comtpeur = Integer.parseInt(inc.getValeurIncrement());
        }
        while (((cotisation = (CPDecisionAffiliationCotisation) manager.cursorReadNext(statement)) != null)
                && (!cotisation.isNew())) {
            try {
                if (JadeStringUtil.isEmpty(cotisation.getMontantAnnuel())) {
                    montantCotisation = "0";
                } else {
                    // détermination du montant selon le dernier passage
                    // périodique comptabilisé
                    // pour ne pas prendre par ex toute l'année en cours si l'on
                    // est qu'au mois d'avril
                    int nbMois = nbMoisFacture(getSession(), cotisation, myPassage);
                    if (nbMois == 99) {
                        montantCotisation = cotisation.getMontantAnnuel();
                    } else if (nbMois == 88) {
                        montantCotisation = cotisation.getMontantAnnuel();
                    } else {
                        FWCurrency montantInt = new FWCurrency(Float.parseFloat(JANumberFormatter.deQuote(cotisation
                                .getMontantMensuel())) * nbMois);
                        montantCotisation = montantInt.toString();
                    }
                }
                // Si la décision n'est plus comprise dans l'affiliation =>
                // montant extourné
                // donc montant de décision = 0 et on affiche la période
                // d'affiliation
                // Si décison <
                if (CPDecision.CS_SORTIE.equalsIgnoreCase(cotisation.getEtat())
                        || CPDecision.CS_REMISE.equalsIgnoreCase(cotisation.getTypeDecision())) {
                    montantCoti = new BigDecimal("0");
                } else if ((!JadeStringUtil.isIntegerEmpty(cotisation.getFinAffiliation()) && BSessionUtil
                        .compareDateFirstGreaterOrEqual(getSession(), cotisation.getDebutDecision(),
                                cotisation.getFinAffiliation()))
                        || (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), cotisation.getFinDecision(),
                                cotisation.getDebutAffiliation()))) {
                    montantCoti = new BigDecimal("0");
                } else if (CPDecision.CS_IMPUTATION.equalsIgnoreCase(cotisation.getTypeDecision())) {
                    base = new CPDonneesBase();
                    base.setSession(getSession());
                    base.setIdDecision(cotisation.getIdDecision());
                    base.retrieve();
                    if (!base.isNew() && !JadeStringUtil.isIntegerEmpty(base.getCotisation1())) {
                        montantCoti = new BigDecimal(JANumberFormatter.deQuote("-" + base.getCotisation1()));
                    }
                } else {
                    // Tester si décision avec cotisation sur salaire
                    base = new CPDonneesBase();
                    base.setSession(getSession());
                    base.setIdDecision(cotisation.getIdDecision());
                    base.retrieve();
                    if (!base.isNew() && !JadeStringUtil.isIntegerEmpty(base.getCotisationSalarie())) {
                        float montantImputation = 0;
                        // recherche coti payé en tant que salarié
                        float cotiEncode = Float.parseFloat(JANumberFormatter.deQuote(base.getCotisationSalarie()));
                        // Calcul du montant qui doit être imputer selon le
                        // montant de cotisation payé en tant que salarié
                        // CPCotisation coti =
                        // CPCotisation._returnCotisation(getSession(),
                        // cotisation.getIdDecision(),
                        // CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
                        // if (coti != null) {
                        // Calcul du montant imputé
                        // montantImputation =
                        // Float.parseFloat(JANumberFormatter.deQuote(coti.getMontantAnnuel()))
                        // - cotiEncode;
                        // }
                        // montantImputation =
                        // JANumberFormatter.round(montantImputation, 1, 2,
                        // JANumberFormatter.NEAR);
                        montantImputation = JANumberFormatter.round(cotiEncode, 1, 2, JANumberFormatter.NEAR);
                        FWCurrency montantInt = new FWCurrency(Float.parseFloat(JANumberFormatter
                                .deQuote(montantCotisation)) - montantImputation);
                        montantCoti = montantInt.getBigDecimalValue();
                    } else {
                        montantCoti = new BigDecimal(JANumberFormatter.deQuote(montantCotisation));
                    }
                }
                // Si la décision n'est plus comprise dans l'affiliation =>
                // montant extourné
                // donc montant de décision = 0 et on affiche la période
                // d'affiliation
                if ((!JadeStringUtil.isIntegerEmpty(cotisation.getFinAffiliation()) && BSessionUtil
                        .compareDateFirstGreaterOrEqual(getSession(), cotisation.getDebutDecision(),
                                cotisation.getFinAffiliation()))
                        || (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), cotisation.getFinDecision(),
                                cotisation.getDebutAffiliation()))
                        || CPDecision.CS_SALARIE_DISPENSE.equalsIgnoreCase(cotisation.getSpecification())) {
                    montantCoti = new BigDecimal("0");
                }
                // Si la décision est en cours de sortie => prendre les montants
                // des sorties
                CPSortieManager sortieManager = new CPSortieManager();
                sortieManager.setSession(getSession());
                sortieManager.setForIdTiers(cotisation.getIdTiers());
                sortieManager.setForIdDecision(cotisation.getIdDecision());
                sortieManager.setForAnnee(cotisation.getAnneeDecision());
                sortieManager.find();
                if (sortieManager.size() > 0) {
                    montantCoti = new BigDecimal("0");
                }
                for (int i = 0; i < sortieManager.size(); i++) {
                    CPSortieMontant sortieMontant = new CPSortieMontant();
                    sortieMontant.setSession(getSession());
                    sortieMontant.setAlternateKey(1);
                    String idSortie = ((CPSortie) sortieManager.getEntity(i)).getIdSortie();
                    sortieMontant.setIdSortie(idSortie);
                    ;
                    sortieMontant.setAssurance(cotisation.getIdCotiAffiliation());
                    sortieMontant.retrieve();
                    if (!sortieMontant.isNew()) {
                        montantCoti = montantCoti.subtract(new BigDecimal(JANumberFormatter.deQuote(sortieMontant
                                .getMontant())));
                    }
                }
                // Si tiers ou année différente => recherche du montant compta
                if (!premierEnreg
                        && (!cotisation.getNumAffilie().equalsIgnoreCase(cotisationPrecedente.getNumAffilie()) || !cotisation
                                .getAnneeDecision().equalsIgnoreCase(cotisationPrecedente.getAnneeDecision()))) {
                    // Recherche du compte annexe
                    CACompteAnnexe compte = new CACompteAnnexe();
                    compte.setSession(getSession());
                    compte.setAlternateKey(1);
                    compte.setIdRole(CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(
                            getSession().getApplication()));
                    compte.setIdExterneRole(cotisationPrecedente.getNumAffilie());
                    compte.wantCallMethodBefore(false);
                    compte.retrieve();
                    // Recherche du compteur pour l'assurance
                    String montantCompteur = CPToolBox.rechMontantFacture(getSession(), null,
                            compte.getIdCompteAnnexe(),
                            AFCotisation._getRubrique(cotisationPrecedente.getIdCotiAffiliation(), getSession()),
                            cotisationPrecedente.getAnneeDecision());
                    montantEnCompta = new BigDecimal(JANumberFormatter.deQuote(montantCompteur));
                    // Impression si montant CI et cot. pers sont différent
                    if (montantCotiBD.compareTo(montantEnCompta) != 0) {
                        // Verifier si le calcul au prorata de la coti ne
                        // correspond pas au montant en compta
                        montantCotiBD = new BigDecimal(JANumberFormatter.deQuote(CPToolBox.getMontantProRata(
                                cotisationPrecedente.getDebutDecision(), cotisationPrecedente.getFinDecision(),
                                JANumberFormatter.deQuote(montantCotiBD.toString()))));
                    }
                    if (montantCotiBD.compareTo(montantEnCompta) != 0) {
                        CPDecision decPrecedente = new CPDecision();
                        decPrecedente.setSession(getSession());
                        decPrecedente.setIdDecision(cotisationPrecedente.getIdDecision());
                        decPrecedente.retrieve();
                        // Détermination date
                        int anneeDebut = 0;
                        int anneeFin = 0;
                        int anneeDecision = Integer.parseInt(decPrecedente.getAnneeDecision());
                        String debRef = decPrecedente.loadAffiliation().getDateDebut();
                        ;
                        String finRef = decPrecedente.loadAffiliation().getDateFin();
                        String debutImputation = decPrecedente.getDebutDecision();
                        String finImputation = decPrecedente.getFinDecision();
                        // Tests si décision comprise dans les dates de
                        // référence (affiliation ou cotisation AVS)
                        if (!JadeStringUtil.isBlank(debRef)) {
                            anneeDebut = globaz.globall.util.JACalendar.getYear(debRef);
                            // Si année d'activité = année de décision => debut
                            // période décision = début d'activité
                            if (anneeDebut == anneeDecision) {
                                debutImputation = debRef;
                            }
                        } // Si fin d'activité
                        if (!JadeStringUtil.isBlank(finRef)) {
                            anneeFin = globaz.globall.util.JACalendar.getYear(finRef);
                            // année de décision = année de fin d'activité > fin
                            // période décision = fin d'activité
                            if (anneeFin == anneeDecision) {
                                finImputation = finRef;
                            }
                        }
                        // --------- Recherche des données du tiers
                        // --------------------------------------------------------
                        TITiersViewBean persAvs = new TITiersViewBean();
                        persAvs.setSession(getSession());
                        persAvs.setIdTiers(decPrecedente.getIdTiers());
                        persAvs.retrieve();
                        String dateAgeAvs = persAvs.getDateAvs();
                        int anneeAvs = JACalendar.getYear(dateAgeAvs);
                        // Si non actif
                        if (decPrecedente.isNonActif()) {
                            // Cas âge AVS
                            int anneeDec = JACalendar.getYear(debutImputation);
                            if ((anneeAvs == anneeDec)
                                    && BSessionUtil.compareDateFirstGreater(getSession(), finImputation,
                                            dateAgeAvs)) {
                                finImputation = dateAgeAvs;
                            }
                        }
                        HSSFRow row = sheet.createRow(nbRow);
                        nbRow++;
                        int colNum = 0;
                        // numéro affilié
                        HSSFCell cell = row.createCell((short) colNum++);
                        cell.setCellStyle(style);
                        cell.setCellValue(new HSSFRichTextString(cotisationPrecedente.getNumAffilie()));
                        // Genre
                        cell = row.createCell((short) colNum++);
                        cell.setCellStyle(style);
                        cell.setCellValue(new HSSFRichTextString(globaz.phenix.translation.CodeSystem.getLibelle(
                                getSession(), cotisationPrecedente.getGenreAffilie())));
                        // Année
                        cell = row.createCell((short) colNum++);
                        cell.setCellStyle(style);
                        cell.setCellValue(new HSSFRichTextString(cotisationPrecedente.getAnneeDecision()));
                        // periode affiliation
                        if ((!JadeStringUtil.isIntegerEmpty(cotisationPrecedente.getFinAffiliation()) && BSessionUtil
                                .compareDateFirstGreaterOrEqual(getSession(),
                                        cotisationPrecedente.getDebutDecision(),
                                        cotisationPrecedente.getFinAffiliation()))
                                || (BSessionUtil.compareDateFirstLowerOrEqual(getSession(),
                                        cotisationPrecedente.getFinDecision(),
                                        cotisationPrecedente.getDebutAffiliation()))) {
                            periodeAffiliation = cotisationPrecedente.getDebutAffiliation() + " - "
                                    + cotisationPrecedente.getFinAffiliation();
                        } else {
                            periodeAffiliation = "";
                            if (cotisationPrecedente.getDebutAffiliation().length() > 0) {
                                periodeAffiliation = cotisationPrecedente.getDebutAffiliation();
                            }
                            if (cotisationPrecedente.getFinAffiliation().length() > 0) {
                                periodeAffiliation += " - " + cotisationPrecedente.getFinAffiliation();
                            }
                        }
                        cell = row.createCell((short) colNum++);
                        cell.setCellStyle(style);
                        cell.setCellValue(new HSSFRichTextString(periodeAffiliation));
                        // Montant Cotisation CP
                        cell = row.createCell((short) colNum++);
                        cell.setCellStyle(style2);
                        cell.setCellValue(new HSSFRichTextString(montantCotiBD.toString()));
                        // Montant Compta
                        cell = row.createCell((short) colNum++);
                        cell.setCellStyle(style2);
                        cell.setCellValue(new HSSFRichTextString(montantEnCompta.toString()));
                        // Différence
                        cell = row.createCell((short) colNum++);
                        cell.setCellStyle(style2);
                        cell.setCellValue(new HSSFRichTextString((montantCotiBD.subtract(montantEnCompta)).toString()));
                        // idTiers
                        cell = row.createCell((short) colNum++);
                        cell.setCellStyle(style2);
                        cell.setCellValue(new HSSFRichTextString(cotisationPrecedente.getIdTiers()));
                        // idAffiliation
                        cell = row.createCell((short) colNum++);
                        cell.setCellStyle(style2);
                        cell.setCellValue(new HSSFRichTextString(cotisationPrecedente.getIdAffiliation()));
                        // création script
                        comtpeur++;
                        cell = row.createCell((short) colNum++);
                        cell.setCellStyle(style2);
                        cell.setCellValue(new HSSFRichTextString(
                                "INSERT INTO CCJUWEB.CPDECIP (EBIPAS,HTICJT,HTITIE,IAACTI,IAANNE,IAAPRI,IAASSU,IABCMP,IABCOM,IABDAC,IABIMP,IABPAS,IABPRO,IABREC,IABSIG,IACBLO,IACINT,IADDEB,IADFAC,IADFIN,IADINF,IAFACT,IAICOM,IAIDEC,IAISES,IANTOT,IAOPPO,IARESP,IATAXA,IATETA,IATGAF,IATSPE,IATTDE,IBIDCF,ICIIFD,ICIIFP,MAIAFF,PSPY) "
                                        + "VALUES ("
                                        + getIdPassage()
                                        + ","
                                        + decPrecedente.getIdConjoint()
                                        + ","
                                        + decPrecedente.getIdTiers()
                                        + ",'2',"
                                        + decPrecedente.getAnneeDecision()
                                        + ",'1','2','2','2','2','1','2','','2','2','2',229001,"
                                        + new JADate(debutImputation).toStrAMJ()
                                        + ",0,"
                                        + new JADate(finImputation).toStrAMJ()
                                        + ","
                                        + new JADate(JACalendar.today().toString()).toStrAMJ()
                                        + ",'1',0,"
                                        + comtpeur
                                        + ",0,0,'2','','N',604001,602002,0,605005,0,"
                                        + decPrecedente.getIdIfdDefinitif()
                                        + ","
                                        + decPrecedente.getIdIfdProvisoire()
                                        + ","
                                        + decPrecedente.getIdAffiliation()
                                        + ",'20081120163200ccjuglo');\n"
                                        + "INSERT INTO CCJUWEB.CPDOENP (IAIDEC,IDBCI0,IDCAPI,IDCOT1,IDCOT2,IDDDE1,IDDDE2,IDDFI1,IDDFI2,IDDFOR,IDFORT,IDMAU1,IDMAU2,IDMEX1,IDMEX2,IDMSAL,IDRAU1,IDRAU2,IDRCIF,IDREV1,IDREV2,IDTSRC,PSPY) "
                                        + "VALUES ("
                                        + comtpeur
                                        + ",'2',0,"
                                        + (montantCotiBD.subtract(montantEnCompta))
                                        + ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'20081120163312ccjuglo');\n"));
                    }
                    montantCotiBD = new BigDecimal("0");
                }
                montantCotiBD = montantCotiBD.add(montantCoti);
                cotisationPrecedente = cotisation;
                premierEnreg = false;
                processAppelant.incProgressCounter();
            } catch (Exception e) {
                processAppelant.getMemoryLog().logMessage(e.getMessage(), FWMessage.AVERTISSEMENT,
                        cotisation.getNumAffilie() + " - " + cotisation.getAnneeDecision());
            }
        }
        // Recherche du compte annexe
        CACompteAnnexe compte = new CACompteAnnexe();
        compte.setSession(getSession());
        compte.setAlternateKey(1);
        compte.setIdRole(CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(
                getSession().getApplication()));
        compte.setIdExterneRole(cotisationPrecedente.getNumAffilie());
        compte.wantCallMethodBefore(false);
        compte.retrieve();
        // Recherche du compteur pour l'assurance
        String montantCompteur = CPToolBox.rechMontantFacture(getSession(), null, compte.getIdCompteAnnexe(),
                AFCotisation._getRubrique(cotisationPrecedente.getIdCotiAffiliation(), getSession()),
                cotisationPrecedente.getAnneeDecision());
        montantEnCompta = new BigDecimal(JANumberFormatter.deQuote(montantCompteur));
        // Impression du dernier cas
        // Impression si montant CI et cot. pers sont différent
        if (montantCotiBD.compareTo(montantEnCompta) != 0) {
            // Verifier si le calcul au prorata de la coti ne correspond pas au
            // montant en compta
            montantCotiBD = new BigDecimal(JANumberFormatter.deQuote(CPToolBox.getMontantProRata(
                    cotisationPrecedente.getDebutDecision(), cotisationPrecedente.getFinDecision(),
                    JANumberFormatter.deQuote(montantCotiBD.toString()))));
        }
        if (montantCotiBD.compareTo(montantEnCompta) != 0) {
            CPDecision decPrecedente = new CPDecision();
            decPrecedente.setSession(getSession());
            decPrecedente.setIdDecision(cotisationPrecedente.getIdDecision());
            decPrecedente.retrieve();
            // Détermination date
            int anneeDebut = 0;
            int anneeFin = 0;
            int anneeDecision = Integer.parseInt(decPrecedente.getAnneeDecision());
            String debRef = decPrecedente.loadAffiliation().getDateDebut();
            ;
            String finRef = decPrecedente.loadAffiliation().getDateFin();
            String debutImputation = decPrecedente.getDebutDecision();
            String finImputation = decPrecedente.getFinDecision();
            // Tests si décision comprise dans les dates de référence
            // (affiliation ou cotisation AVS)
            if (!JadeStringUtil.isBlank(debRef)) {
                anneeDebut = globaz.globall.util.JACalendar.getYear(debRef);
                // Si année d'activité = année de décision => debut période
                // décision = début d'activité
                if (anneeDebut == anneeDecision) {
                    debutImputation = debRef;
                }
            } // Si fin d'activité
            if (!JadeStringUtil.isBlank(finRef)) {
                anneeFin = globaz.globall.util.JACalendar.getYear(finRef);
                // année de décision = année de fin d'activité > fin période
                // décision = fin d'activité
                if (anneeFin == anneeDecision) {
                    finImputation = finRef;
                }
            }
            // --------- Recherche des données du tiers
            // --------------------------------------------------------
            TITiersViewBean persAvs = new TITiersViewBean();
            persAvs.setSession(getSession());
            persAvs.setIdTiers(decPrecedente.getIdTiers());
            persAvs.retrieve();
            String dateAgeAvs = persAvs.getDateAvs();
            int anneeAvs = JACalendar.getYear(dateAgeAvs);
            // Si non actif
            if (decPrecedente.isNonActif()) {
                // Cas âge AVS
                int anneeDec = JACalendar.getYear(debutImputation);
                if ((anneeAvs == anneeDec)
                        && BSessionUtil.compareDateFirstGreater(getSession(), finImputation, dateAgeAvs)) {
                    finImputation = dateAgeAvs;
                }
            }
            HSSFRow row = sheet.createRow(nbRow);
            int colNum = 0;
            // numéro affilié
            HSSFCell cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(cotisationPrecedente.getNumAffilie()));
            // Genre
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(globaz.phenix.translation.CodeSystem.getLibelle(getSession(),
                    cotisationPrecedente.getGenreAffilie())));
            // Année
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(cotisationPrecedente.getAnneeDecision()));
            // periode affiliation
            if ((!JadeStringUtil.isIntegerEmpty(cotisationPrecedente.getFinAffiliation()) && BSessionUtil
                    .compareDateFirstGreaterOrEqual(getSession(), cotisationPrecedente.getDebutDecision(),
                            cotisationPrecedente.getFinAffiliation()))
                    || (BSessionUtil.compareDateFirstLowerOrEqual(getSession(),
                            cotisationPrecedente.getFinDecision(), cotisationPrecedente.getDebutAffiliation()))) {
                periodeAffiliation = cotisationPrecedente.getDebutAffiliation() + " - "
                        + cotisationPrecedente.getFinAffiliation();
            } else {
                periodeAffiliation = "";
                if (cotisationPrecedente.getDebutAffiliation().length() > 0) {
                    periodeAffiliation = cotisationPrecedente.getDebutAffiliation();
                }
                if (cotisationPrecedente.getFinAffiliation().length() > 0) {
                    periodeAffiliation += " - " + cotisationPrecedente.getFinAffiliation();
                }
            }
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(periodeAffiliation));
            // Montant CP
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(montantCoti.toString()));
            // Montant CI
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(montantEnCompta.toString()));
            // Différence
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString((montantCotiBD.subtract(montantEnCompta)).toString()));
            // idTiers
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(cotisationPrecedente.getIdTiers()));
            // idAffiliation
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(cotisationPrecedente.getIdAffiliation()));
            // création script
            comtpeur++;
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(
                    "INSERT INTO CCJUWEB.CPDECIP (EBIPAS,HTICJT,HTITIE,IAACTI,IAANNE,IAAPRI,IAASSU,IABCMP,IABCOM,IABDAC,IABIMP,IABPAS,IABPRO,IABREC,IABSIG,IACBLO,IACINT,IADDEB,IADFAC,IADFIN,IADINF,IAFACT,IAICOM,IAIDEC,IAISES,IANTOT,IAOPPO,IARESP,IATAXA,IATETA,IATGAF,IATSPE,IATTDE,IBIDCF,ICIIFD,ICIIFP,MAIAFF,PSPY) "
                            + "VALUES (0,"
                            + decPrecedente.getIdConjoint()
                            + ","
                            + decPrecedente.getIdTiers()
                            + ",'2',"
                            + decPrecedente.getAnneeDecision()
                            + ",'1','2','2','2','2','1','2','','2','2','2',229001,"
                            + new JADate(debutImputation).toStrAMJ()
                            + ",0,"
                            + new JADate(finImputation).toStrAMJ()
                            + ","
                            + new JADate(JACalendar.today().toString()).toStrAMJ()
                            + ",'1',0,"
                            + comtpeur
                            + ",0,0,'2','','N',604001,602002,0,605005,0,"
                            + decPrecedente.getIdIfdDefinitif()
                            + ","
                            + decPrecedente.getIdIfdProvisoire()
                            + ","
                            + decPrecedente.getIdAffiliation()
                            + ",'20081120163200ccjuglo');\n"
                            + "INSERT INTO CCJUWEB.CPDOENP (IAIDEC,IDBCI0,IDCAPI,IDCOT1,IDCOT2,IDDDE1,IDDDE2,IDDFI1,IDDFI2,IDDFOR,IDFORT,IDMAU1,IDMAU2,IDMEX1,IDMEX2,IDMSAL,IDRAU1,IDRAU2,IDRCIF,IDREV1,IDREV2,IDTSRC,PSPY) "
                            + "VALUES ("
                            + comtpeur
                            + ",'2',0,"
                            + (montantCotiBD.subtract(montantEnCompta))
                            + ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'20081120163312ccjuglo');\n"));
        }
        inc.setValeurIncrement(Integer.toString(comtpeur));
        inc.update();
        manager.cursorClose(statement);
        statement = null;
        HSSFFooter footer = sheet.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0121CCP");
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

        final String[] COL_TITLES = { session.getLabel("NUM_AFFILIE"), session.getLabel("GENRE_DECISION"),
                session.getLabel("ANNEE"), session.getLabel("PERIODE_AFFILIATION"),
                session.getLabel("LISTE_MONTANT_CP"), session.getLabel("LISTE_MONTANT_COMPTA"),
                session.getLabel("LISTE_DIFFERENCE"), "idTiers", "idAffiliation" };

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
