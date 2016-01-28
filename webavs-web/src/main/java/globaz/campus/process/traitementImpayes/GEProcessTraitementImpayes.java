package globaz.campus.process.traitementImpayes;

import globaz.campus.db.annonces.GEAnnonces;
import globaz.campus.db.annonces.GEAnnoncesManager;
import globaz.campus.util.GEUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.db.tauxAssurance.AFTauxAssuranceManager;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.process.CAProcessExtournerSection;
import globaz.phenix.db.divers.CPTableNonActif;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class GEProcessTraitementImpayes extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final static String COMMENT_EXTOURNE_SECTION = "étudiant : cotisation impayée";
    private String annee = "";
    private String montant = "";
    private int nbAnnoncesTraites = 0;
    private BSession sessionPavo = null;
    private BSession sessionPhenix = null;
    private BSession sessionPyxis = null;
    private HSSFSheet sheet1;
    private HSSFWorkbook wb;

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        boolean result = true;
        GEAnnoncesManager annoncesMng = null;
        BStatement statement = null;
        BTransaction transactionCurseur = null;
        try {
            if (anneeCorecte()) {
                // Initialisation du document excel
                final String titre = getSession().getLabel("TRAIT_IMPAYES_TITRE_DOC");
                wb = new HSSFWorkbook();
                sheet1 = wb.createSheet(titre);
                HSSFCellStyle style = wb.createCellStyle();
                style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                style.setBorderRight(HSSFCellStyle.BORDER_THIN);
                style.setBorderTop(HSSFCellStyle.BORDER_THIN);
                style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                // Création des colonnes
                sheet1 = setTitleRowMiseCompte(wb, sheet1);
                // Création des sessions
                creationSession();
                // On va rechercher les sections
                CASectionManager sectionManager = new CASectionManager();
                sectionManager.setSession(getSession());
                // Pour les sections étudiants
                sectionManager.setForCategorieSection(APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS_ETUDIANT);
                // Pour le solde égal à la coti minimum
                if (!JadeStringUtil.isEmpty(getMontant())) {
                    sectionManager.setForSolde(getMontant());
                } else {
                    sectionManager.setForSolde(getMontantDefaut());
                }
                sectionManager.setLikeIdExterne(annee);
                // Dont le contentieux n'est pas suspendu
                // voir avec SPA :
                // sectionManager.setForContentieuxNonSuspendu(true);
                sectionManager.changeManagerSize(BManager.SIZE_NOLIMIT);
                sectionManager.find();
                // On set la progress bar
                setProgressScaleValue(sectionManager.size());
                for (int i = 0; i < sectionManager.size(); i++) {
                    creationSession();
                    transactionCurseur = _getTransactionCurseur();
                    incProgressCounter();
                    CASection section = (CASection) sectionManager.getEntity(i);
                    // Pas de contentieux
                    // voir avec SPA : if (section.getIdLastEtapeCtx() != "0") {
                    // voir avec SPA : if
                    // (!JadeStringUtil.isEmpty(section.getIdLastEtapeCtx())) {
                    // On est dans le cas d'impayé
                    // On extourne la section
                    CAProcessExtournerSection processExtourne = new CAProcessExtournerSection();
                    processExtourne.setSession(sessionPyxis);
                    processExtourne.setIdSection(section.getIdSection());
                    processExtourne.setComment(GEProcessTraitementImpayes.COMMENT_EXTOURNE_SECTION);
                    processExtourne.setTransaction(transactionCurseur);
                    processExtourne.executeProcess();
                    // On recherche l'affiliation
                    // IAFAffiliation affiliation = (IAFAffiliation)
                    // section.getCompteAnnexe().getRole().getAffiliation();
                    AFAffiliationManager affiliationManager = new AFAffiliationManager();
                    affiliationManager.setSession(getSession());
                    String numAffilie = section.getCompteAnnexe().getIdExterneRole();
                    affiliationManager.setForAffilieNumero(numAffilie);
                    affiliationManager.setForDateDebut("01.01." + getAnnee());
                    affiliationManager.setForDateFin("31.12." + getAnnee());
                    affiliationManager.setForTypesAffPersonelles();
                    affiliationManager.find();
                    AFAffiliation affiliation = (AFAffiliation) affiliationManager.getFirstEntity();
                    if (affiliation != null) {
                        // On recherche l'annonce
                        CPDecisionManager decisionManager = new CPDecisionManager();
                        decisionManager.setSession(getSession());
                        decisionManager.setForAnneeDecision(getAnnee());
                        decisionManager.setForIdAffiliation(affiliation.getAffiliationId());
                        decisionManager.find();
                        CPDecision decision = (CPDecision) decisionManager.getFirstEntity();
                        if (decision != null) {
                            GEAnnoncesManager annonceManager = new GEAnnoncesManager();
                            annonceManager.setSession(getSession());
                            // annonceManager.setForCsEtatAnnonce(GEAnnonces.CS_ETAT_VALIDE);
                            annonceManager.setForIdDecision(decision.getIdDecision());
                            annonceManager.find();
                            if (annonceManager.size() > 0) {
                                // On va mettre à jour l'état de l'annonce
                                for (int j = 0; j < annonceManager.size(); j++) {
                                    GEAnnonces annonce = (GEAnnonces) annonceManager.getEntity(j);
                                    annonce.setCsEtatAnnonce(GEAnnonces.CS_ETAT_IMPAYEE);
                                    annonce.update(transactionCurseur);
                                }
                            } else {
                                getMemoryLog().logMessage(
                                        getSession().getLabel("TRAIT_IMPAYES_ANN_NON_TROUVEE") + " : "
                                                + section.getCompteAnnexe().getIdExterneRole() + " : "
                                                + getTransaction().getErrors().toString(), FWViewBeanInterface.OK,
                                        "ProcessTraitementImpayes");
                            }

                            // On recherche le tiers
                            TITiersViewBean tiers = new TITiersViewBean();
                            tiers.setSession(getSession());
                            tiers.setIdTiers(affiliation.getIdTiers());
                            tiers.retrieve();
                            if (tiers != null) {
                                creationLigneExcel(numAffilie, tiers.getNomPrenom(), section.getSolde());
                                nbAnnoncesTraites++;
                            } else {
                                transactionCurseur.addErrors(getSession().getLabel("TRAIT_IMPAYES_TIERS_NON_TROUVEE")
                                        + " : " + section.getCompteAnnexe().getIdExterneRole() + " : "
                                        + getTransaction().getErrors().toString());
                                // getMemoryLog().logMessage(getSession().getLabel("TRAIT_IMPAYES_TIERS_NON_TROUVEE")+" : "+
                                // section.getCompteAnnexe().getIdExterneRole()
                                // + " : " +
                                // getTransaction().getErrors().toString(),
                                // BProcess.OK, "ProcessTraitementImpayes");
                            }
                        } else {
                            transactionCurseur.addErrors(getSession().getLabel("TRAIT_IMPAYES_DEC_NON_TROUVEE") + " : "
                                    + section.getCompteAnnexe().getIdExterneRole());
                            // getMemoryLog().logMessage(getSession().getLabel("TRAIT_IMPAYES_DEC_NON_TROUVEE")+" : "+
                            // section.getCompteAnnexe().getIdExterneRole() +
                            // " : " + getTransaction().getErrors().toString(),
                            // BProcess.OK, "ProcessTraitementImpayes");
                        }
                    } else {
                        transactionCurseur.addErrors(getSession().getLabel("TRAIT_IMPAYES_AFF_NON_TROUVEE") + " : "
                                + section.getCompteAnnexe().getIdExterneRole());
                        // getMemoryLog().logMessage(getSession().getLabel("TRAIT_IMPAYES_AFF_NON_TROUVEE")+" : "+
                        // section.getCompteAnnexe().getIdExterneRole() + " : "
                        // + getTransaction().getErrors().toString(),
                        // BProcess.OK, "ProcessTraitementImpayes");
                    }
                    if (transactionCurseur.hasErrors()) {
                        getMemoryLog().logMessage(
                                getSession().getLabel("TRAIT_IMPAYES_ERR_MAJ_ANNONCE") + " : "
                                        + section.getCompteAnnexe().getIdExterneRole() + " : "
                                        + transactionCurseur.getErrors().toString(), FWViewBeanInterface.OK,
                                "ProcessTraitementImpayes");
                        transactionCurseur.rollback();
                        if ((transactionCurseur != null) && transactionCurseur.isOpened()) {
                            transactionCurseur.closeTransaction();
                        }
                    } else {
                        transactionCurseur.commit();
                        if ((transactionCurseur != null) && transactionCurseur.isOpened()) {
                            transactionCurseur.closeTransaction();
                        }
                    }
                }
                if (nbAnnoncesTraites > 0) {
                    // Création du fichier excel
                    File f = File.createTempFile(titre + " - " + JACalendar.todayJJsMMsAAAA() + "  ", ".xls");
                    f.deleteOnExit();
                    FileOutputStream out = new FileOutputStream(f);
                    wb.write(out);
                    out.close();
                    // Ajout du document
                    JadePublishDocumentInfo docInfo = createDocumentInfo();
                    docInfo.setDocumentType("");
                    docInfo.setDocumentTypeNumber("");
                    this.registerAttachedDocument(docInfo, f.getAbsolutePath());
                } else {
                    result = false;
                    if (!JadeStringUtil.isEmpty(getMontant())) {
                        getMemoryLog().logMessage(
                                getSession().getLabel("TRAIT_IMPAYES_AUCUN") + " " + getMontant() + "(" + getAnnee()
                                        + ")", FWViewBeanInterface.WARNING, "ProcessTraitementImpayes");
                    } else {
                        getMemoryLog().logMessage(
                                getSession().getLabel("TRAIT_IMPAYES_AUCUN") + " " + getMontantDefaut() + "("
                                        + getAnnee() + ")", FWViewBeanInterface.WARNING, "ProcessTraitementImpayes");
                    }
                }
            } else {
                result = false;
                getMemoryLog().logMessage(
                        getSession().getLabel("TRAIT_IMPAYES_ANNEE_MANQUANTE") + " " + getMontantDefaut() + "("
                                + getAnnee() + ")", FWViewBeanInterface.WARNING, "ProcessTraitementImpayes");
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage().toString(), FWViewBeanInterface.WARNING,
                    "ProcessTraitementImpayes");
            result = false;
        } finally {
            try {
                if (annoncesMng != null) {
                    annoncesMng.cursorClose(statement);
                }
            } finally {
                if ((transactionCurseur != null) && transactionCurseur.isOpened()) {
                    transactionCurseur.closeTransaction();
                }
            }
        }
        return result;
    }

    private BTransaction _getTransactionCurseur() throws Exception {
        try {
            BTransaction transactionCurseur = new BTransaction(getSession());
            transactionCurseur.getSession().newTransaction();
            transactionCurseur.openTransaction();
            return transactionCurseur;
        } catch (Exception e) {
            throw new Exception("_getTransactionCurseur: " + e.getMessage());
        }
    }

    @Override
    protected void _validate() throws Exception {
        // L'adresse email est obligatoire
        if (JadeStringUtil.isBlank(getEMailAddress())) {
            this._addError(getSession().getLabel("EMAIL_OBLIGATOIRE"));
        }

        // divers :
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        // Les erreurs sont ajoutées à la session,
        // abort permet l'arrêt du process
        if (getSession().hasErrors()) {
            abort();
        }
    }

    private boolean anneeCorecte() {
        if (JadeStringUtil.isEmpty(getAnnee())) {
            return false;
        }
        try {
            JADate date = new JADate("01.01." + getAnnee());
            if (date.getYear() == 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private void creationLigneExcel(String numAffilie, String nomPrenom, String soldeSection) {
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows());
        int colNum = 0;
        // numAffilié
        HSSFCell cell = row.createCell((short) colNum++);
        cell.setCellValue(numAffilie);
        cell.setCellStyle(style);
        // nomPrenom
        cell = row.createCell((short) colNum++);
        cell.setCellValue(nomPrenom);
        cell.setCellStyle(style);
        // solde
        cell = row.createCell((short) colNum++);
        cell.setCellValue(soldeSection);
        cell.setCellStyle(style);
        // annee
        cell = row.createCell((short) colNum++);
        cell.setCellValue(getAnnee());
        cell.setCellStyle(style);
    }

    // Création des sessions
    private void creationSession() throws Exception {
        // Création de la session pavo
        sessionPavo = GEUtil.creationSessionPavo(getSession());
        // Création de la session phenix
        sessionPhenix = GEUtil.creationSessionPhenix(getSession());
        // Création de la session pyxis
        sessionPyxis = GEUtil.creationSessionPyxis(getSession());
        ;
    }

    /*
     * protected boolean _executeProcess() throws Exception { boolean result = true; GEAnnoncesManager annoncesMng =
     * null; GEAnnonces annonce = null; BStatement statement = null; BTransaction transactionCurseur = null; BigDecimal
     * solde = null; boolean estNonPayeur = false; try { // Création des sessions creationSession(); // Recherche des
     * annonces à l'état validé et qui n'ont pas le code postgrade ou doctorant annoncesMng = new GEAnnoncesManager();
     * annoncesMng.setSession(getSession()); annoncesMng.setForCsEtatAnnonce(GEAnnonces.CS_ETAT_VALIDE); // traiter les
     * annonces avant les imputations annoncesMng.orderByAnnoncesImputations(); // count sur les annonces nbAnnonces =
     * annoncesMng.getCount(getTransaction()); // Ouverture de la transaction du curseur transactionCurseur =
     * _getTransactionCurseur(); // ouverture du curseur statement = annoncesMng.cursorOpen(transactionCurseur); //
     * Progression du traitement setProgressScaleValue(nbAnnonces); while ((annonce = (GEAnnonces)
     * annoncesMng.cursorReadNext(statement)) != null && (!annonce.isNew())) { try { // Recherche de la décision //
     * CPDecision decision = _getDecision(annonce.getIdDecision()); // Recherche de l'affiliation correspondant à la
     * décision AFAffiliation affiliation = _getAffiliation(annonce.getIdAffiliation(getSession())); // Recherche du
     * Tiers // TITiersViewBean tiers = _getTiers(affiliation.getIdTiers()); // Recherche du compte annexe
     * CACompteAnnexe compteAnnexe = _getCompteAnnexe(affiliation.getAffilieNumero()); // Stokage du solde du compte
     * annexe solde = new BigDecimal(JANumberFormatter.deQuote(compteAnnexe.getSolde())); // On va rechercher le montant
     * pour l'année concernée _getMontantComptabilise(compteAnnexe.getIdCompteAnnexe(), annee + ""); // insciption au CI
     * if (estNonPayeur) { // Mettre l'annonce à l'état impayé majAnnonces(annonce, GEAnnonces.CS_ETAT_COMPTABILISE);
     * nbAnnoncesTraites++; } } catch (Exception e) { getTransaction().clearErrorBuffer(); if (annonce != null) {
     * getMemoryLog().logMessage("(" + annonce.getNumImmatriculationTransmis() + ", " + annonce.getNom() + ", " +
     * annonce.getPrenom() + ")" + e.getMessage().toString(), BProcess.WARNING, "ProcessComptabilisationCI"); } else {
     * getTransaction().rollback(); } } incProgressCounter(); getTransaction().commit(); estNonPayeur = false; } //
     * Information dans l'email pour le nombre d'annonces / imputations impayés if (nbAnnoncesTraites == 0) {
     * getMemoryLog().logMessage("Aucune annonce est impayé", BProcess.OK, "ProcessTraitementImpayes"); } else {
     * getMemoryLog().logMessage("Nombre d'annonces impayés" + nbAnnoncesTraites, BProcess.OK,
     * "ProcessTraitementImpayes"); } } catch (Exception e) { getMemoryLog().logMessage(e.getMessage().toString(),
     * BProcess.WARNING, "ProcessTraitementImpayes"); result = false; } finally { if (annoncesMng != null) {
     * annoncesMng.cursorClose(statement); } if (transactionCurseur != null && transactionCurseur.isOpened()) {
     * transactionCurseur.closeTransaction(); } } return result; }
     */

    /*
     * private BTransaction _getTransactionCurseur() throws Exception { try { BTransaction transactionCurseur = new
     * BTransaction(getSession()); transactionCurseur.getSession().newTransaction();
     * transactionCurseur.openTransaction(); return transactionCurseur; } catch (Exception e) { throw new
     * Exception("_getTransactionCurseur: " + e.getMessage()); } }
     */

    public String getAnnee() {
        return annee;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || isAborted() || getMemoryLog().getErrorLevel().equals(FWMessage.ERREUR)) {
            return getSession().getLabel("RESULTAT_PROCESS_IMPAYES_KO");
        } else {
            return getSession().getLabel("RESULTAT_PROCESS_IMPAYES_OK");
        }
    }

    public String getMontant() {
        return montant;
    }

    public String getMontantDefaut() {
        String anneeCoti = getAnnee();
        try {
            if (!JadeStringUtil.isEmpty(anneeCoti)) {
                float cotiMinimum = CPTableNonActif.getCotisationMin(getSession(), anneeCoti);
                AFAssuranceManager man = new AFAssuranceManager();
                man.setSession(getSession());
                man.setForGenreAssurance(globaz.naos.translation.CodeSystem.GENRE_ASS_PERSONNEL);
                man.setForTypeAssurance(globaz.naos.translation.CodeSystem.TYPE_ASS_FRAIS_ADMIN);
                man.find();
                AFAssurance ass = (AFAssurance) man.getFirstEntity();
                AFTauxAssuranceManager tauxManager = new AFTauxAssuranceManager();
                tauxManager.setSession(getSession());
                tauxManager.setForIdAssurance(ass.getAssuranceId());
                tauxManager.setForDate("31.12." + anneeCoti);
                tauxManager.find();
                AFTauxAssurance taux = (AFTauxAssurance) tauxManager.getFirstEntity();
                float fpa = (cotiMinimum * Float.valueOf(String.valueOf(taux.getTauxDouble())).floatValue());
                fpa = fpa / 12;
                fpa = JANumberFormatter.round(Float.parseFloat(Float.toString(fpa)), 0.05, 2, JANumberFormatter.NEAR);
                fpa = fpa * 12;
                return String.valueOf(fpa + cotiMinimum);
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }

    }

    public BSession getSessionPavo() {
        return sessionPavo;
    }

    public BSession getSessionPhenix() {
        return sessionPhenix;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setSessionPavo(BSession sessionPavo) {
        this.sessionPavo = sessionPavo;
    }

    public void setSessionPhenix(BSession sessionPhenix) {
        this.sessionPhenix = sessionPhenix;
    }

    private HSSFSheet setTitleRowMiseCompte(HSSFWorkbook wb, HSSFSheet sheet) {
        // définition de la taille des cell
        sheet1.setColumnWidth((short) 0, (short) 3000);
        sheet1.setColumnWidth((short) 1, (short) 7000);
        sheet1.setColumnWidth((short) 2, (short) 3000);
        sheet1.setColumnWidth((short) 3, (short) 2000);
        // nom des cell
        final String numAffilie = getSession().getLabel("TRAIT_IMPAYES_NUM_AFFILIE");
        final String nomPrenom = getSession().getLabel("TRAIT_IMPAYES_NOM_PRENOM");
        final String soldeSection = getSession().getLabel("TRAIT_IMPAYES_SOLDE");
        // final String dateSection =
        // getSession().getLabel("TRAIT_IMPAYES_DATE");
        final String anneeConcernee = getSession().getLabel("TRAIT_IMPAYES_ANNEE_CONCERNEE");

        final String titre = getSession().getLabel("TRAIT_IMPAYES_TITRE");

        final String[] COL_TITLES = { numAffilie, nomPrenom, soldeSection, anneeConcernee };
        HSSFRow row = null;
        HSSFCell c;
        // création du style pour le titre de la page
        HSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font.setFontHeight((short) 500);
        font.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle style = wb.createCellStyle();

        HSSFCellStyle style2 = wb.createCellStyle();
        HSSFFont font2 = wb.createFont();
        font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font2.setFontHeight((short) 200);
        style2.setFont(font2);
        style.setFont(font);

        row = sheet.createRow(0);
        c = row.createCell((short) 0);
        try {
            c.setCellValue(getSession().getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase()));
        } catch (Exception e) {
            c.setCellValue("");
        }
        c.setCellStyle(style2);
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        c = row.createCell((short) 1);
        c.setCellValue(titre);
        c.setCellStyle(style2);
        row = sheet.createRow(3);
        row = sheet.createRow(4);
        // let's use a nifty font for the title
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(COL_TITLES[i]);
            c.setCellStyle(style);
        }
        return sheet;
    }
}
