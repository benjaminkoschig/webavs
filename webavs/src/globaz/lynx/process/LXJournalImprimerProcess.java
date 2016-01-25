package globaz.lynx.process;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.util.JACalendar;
import globaz.helios.process.consolidation.utils.CGImprimerConsolidationUtils;
import globaz.jade.common.Jade;
import globaz.lynx.db.escompte.LXEscompteManager;
import globaz.lynx.db.extourne.LXExtourne;
import globaz.lynx.db.extourne.LXExtourneManager;
import globaz.lynx.db.facture.LXFacture;
import globaz.lynx.db.facture.LXFactureManager;
import globaz.lynx.db.notedecredit.LXNoteDeCredit;
import globaz.lynx.db.notedecredit.LXNoteDeCreditManager;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.paiement.LXPaiement;
import globaz.lynx.db.paiement.LXPaiementManager;
import globaz.lynx.utils.LXConstants;
import globaz.lynx.utils.LXImpressionsUtils;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class LXJournalImprimerProcess extends LXJournalProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int countLine = 0;
    private ArrayList listEtat = null;

    /**
     * @see globaz.lynx.process.LXJournalProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();

        listEtat = new ArrayList();
        listEtat.add(LXOperation.CS_ETAT_COMPTABILISE);
        listEtat.add(LXOperation.CS_ETAT_OUVERT);
        listEtat.add(LXOperation.CS_ETAT_PREPARE);
        listEtat.add(LXOperation.CS_ETAT_SOLDE);
        listEtat.add(LXOperation.CS_ETAT_TRAITEMENT);

        createSheetForJournal(wb);

        String outputFileName = Jade.getInstance().getHomeDir() + LXConstants.OUTPUT_FILE_WORK_DIR + "/"
                + LXConstants.OUTPUT_FILE_NAME_RESUME_JOURNAL + "_"
                + LXConstants.NUMERO_REFERENCE_INFOROM_RESUME_JOURNAL + ".xls";
        CGImprimerConsolidationUtils.createFile(wb, outputFileName);
        registerAttachedDocument(outputFileName);

        return true;
    }

    /**
     * Crée le contenu du fichier excel
     * 
     * @param wb
     * @param sheet
     * @throws Exception
     */
    private void addContentToJournal(HSSFWorkbook wb, HSSFSheet sheet) throws Exception {

        // -----------------------------
        // Informations générales
        // -----------------------------
        LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_JOURNAL") + " : ",
                getJournal().getIdJournal() + " - " + getJournal().getLibelle());
        LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_SOCIETEDEBITRICE")
                + " : ", getSociete().getIdExterne() + " - " + getSociete().getNom());
        LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_DATE_COMPTABLE")
                + " : ", getJournal().getDateValeurCG());

        // Création lignes vides
        LXImpressionsUtils.addRowVoid(sheet, countLine++);
        LXImpressionsUtils.addRowVoid(sheet, countLine++);

        // -----------------------------
        // Récupération des factures
        // -----------------------------
        createTableFactures(wb, sheet);

        // -----------------------------
        // Récupération des Paiements
        // -----------------------------
        createTablePaiements(wb, sheet);

        // -----------------------------
        // Récupération des Escomptes
        // -----------------------------
        createTableEscompte(wb, sheet);

        // -----------------------------
        // Récupération des NDC de base
        // -----------------------------
        createTableNdc(wb, sheet);

        // -----------------------------
        // Récupération des Extournes
        // -----------------------------
        createTableExtourne(wb, sheet);

    }

    /**
     * Créer la feuille excel contenant les comptes du bilan.
     * 
     * @param wb
     * @param exerciceComptable
     * @throws Exception
     */
    private void createSheetForJournal(HSSFWorkbook wb) throws Exception {
        HSSFSheet sheet = wb.createSheet(getSession().getLabel("IMP_RESUME_JOURNAL"));
        LXImpressionsUtils.initPage(sheet, true);

        LXImpressionsUtils.addHeaderFooter(
                sheet,
                getSession(),
                "",
                getSession().getLabel("IMP_RESUME_JOURNAL"),
                "",
                LXConstants.NUMERO_REFERENCE_INFOROM_RESUME_JOURNAL + " - LXJournalImprimerProcess - "
                        + JACalendar.todayJJsMMsAAAA() + " - " + getSession().getUserFullName(), "");

        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_LIBELLE, 30);
        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_SOLDE_A,
                LXConstants.INDEX_COLUMN_SOLDE_F, "1'000'000'000");
        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_SOLDE_C, 30);
        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_SOLDE_D, 15);
        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_SOLDE_E, 15);

        addContentToJournal(wb, sheet);
    }

    /**
     * Permet la récupération et la création du tableau des escomptes
     * 
     * @param wb
     * @param sheet
     * @throws Exception
     */
    protected void createTableEscompte(HSSFWorkbook wb, HSSFSheet sheet) throws Exception {
        LXEscompteManager escompteManager = new LXEscompteManager();
        escompteManager.setSession(getSession());
        escompteManager.setForIdJournal(getIdJournal());
        escompteManager.setForCsEtatIn(listEtat);
        escompteManager.find(BManager.SIZE_NOLIMIT);

        if (escompteManager.size() > 0) {
            LXImpressionsUtils.addRowContentLibelleColumns(wb, sheet, countLine++, getSession()
                    .getLabel("IMP_ESCOMPTE"), getSession().getLabel("IMP_DATE"),
                    getSession().getLabel("IMP_ECHEANCE"), getSession().getLabel("IMP_FOURNISSEUR"), getSession()
                            .getLabel("IMP_NO_FAC_INTERNE"), getSession().getLabel("IMP_NO_FAC_EXTERNE"), getSession()
                            .getLabel("IMP_SOLDE"));
        }

        FWCurrency currency = new FWCurrency();
        boolean couleurLigne = false;

        for (int i = 0; i < escompteManager.size(); i++) {
            LXPaiement ope = (LXPaiement) escompteManager.get(i);

            currency.add(ope.getMontant());

            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, ope.getLibelle(), ope.getDateFacture(),
                    ope.getDateEcheance(), ope.getNomFournisseur(), ope.getIdExterne(), ope.getReferenceExterne(),
                    ope.getMontantFormatted(), couleurLigne);
            couleurLigne = couleurLigne ? false : true;
        }
        if (escompteManager.size() > 0) {
            LXImpressionsUtils.addRowContentLibelleColumns(wb, sheet, countLine++, "", "", "", "", "", getSession()
                    .getLabel("IMP_TOTAL") + " :", currency.toStringFormat());

            // Création lignes vides
            LXImpressionsUtils.addRowVoid(sheet, countLine++);
            LXImpressionsUtils.addRowVoid(sheet, countLine++);
        }
    }

    /**
     * Permet la récupération et la création du tableau des extournes
     * 
     * @param wb
     * @param sheet
     * @throws Exception
     */
    protected void createTableExtourne(HSSFWorkbook wb, HSSFSheet sheet) throws Exception {
        LXExtourneManager extourneManager = new LXExtourneManager();
        extourneManager.setSession(getSession());
        extourneManager.setForIdJournal(getIdJournal());
        extourneManager.setForCsEtatIn(listEtat);
        extourneManager.find(BManager.SIZE_NOLIMIT);

        if (extourneManager.size() > 0) {
            LXImpressionsUtils.addRowContentLibelleColumns(wb, sheet, countLine++, getSession()
                    .getLabel("IMP_EXTOURNE"), getSession().getLabel("IMP_DATE"),
                    getSession().getLabel("IMP_ECHEANCE"), getSession().getLabel("IMP_FOURNISSEUR"), getSession()
                            .getLabel("IMP_NO_FAC_INTERNE"), getSession().getLabel("IMP_NO_FAC_EXTERNE"), getSession()
                            .getLabel("IMP_SOLDE"));
        }

        FWCurrency currency = new FWCurrency();
        boolean couleurLigne = false;

        for (int i = 0; i < extourneManager.size(); i++) {
            LXExtourne ope = (LXExtourne) extourneManager.get(i);

            currency.add(ope.getMontant());

            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, ope.getLibelle(), ope.getDateFacture(),
                    ope.getDateEcheance(), ope.getNomFournisseur(), ope.getIdExterne(), ope.getReferenceExterne(),
                    ope.getMontantFormatted(), couleurLigne);
            couleurLigne = couleurLigne ? false : true;
        }
        if (extourneManager.size() > 0) {
            LXImpressionsUtils.addRowContentLibelleColumns(wb, sheet, countLine++, "", "", "", "", "", getSession()
                    .getLabel("IMP_TOTAL") + " :", currency.toStringFormat());

            // Création lignes vides
            LXImpressionsUtils.addRowVoid(sheet, countLine++);
            LXImpressionsUtils.addRowVoid(sheet, countLine++);
        }
    }

    /**
     * Permet la récupération et la création du tableau des factures
     * 
     * @param wb
     * @param sheet
     * @throws Exception
     */
    protected void createTableFactures(HSSFWorkbook wb, HSSFSheet sheet) throws Exception {

        LXFactureManager manager = new LXFactureManager();
        manager.setSession(getSession());
        manager.setForIdJournal(getIdJournal());
        manager.setForCsEtatIn(listEtat);

        manager.find(BManager.SIZE_NOLIMIT);

        if (manager.size() > 0) {
            LXImpressionsUtils.addRowContentLibelleColumns(wb, sheet, countLine++,
                    getSession().getLabel("IMP_FACTURE"), getSession().getLabel("IMP_DATE"),
                    getSession().getLabel("IMP_ECHEANCE"), getSession().getLabel("IMP_FOURNISSEUR"), getSession()
                            .getLabel("IMP_NO_FAC_INTERNE"), getSession().getLabel("IMP_NO_FAC_EXTERNE"), getSession()
                            .getLabel("IMP_SOLDE"));
        }

        boolean couleurLigne = false;
        FWCurrency currency = new FWCurrency();

        for (int i = 0; i < manager.size(); i++) {
            LXFacture ope = (LXFacture) manager.get(i);

            currency.add(ope.getMontant());

            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, ope.getLibelle(), ope.getDateFacture(),
                    ope.getDateEcheance(), ope.getNomFournisseur(), ope.getIdExterne(), ope.getReferenceExterne(),
                    ope.getMontantFormatted(), couleurLigne);
            couleurLigne = couleurLigne ? false : true;
        }

        if (manager.size() > 0) {
            LXImpressionsUtils.addRowContentLibelleColumns(wb, sheet, countLine++, "", "", "", "", "", getSession()
                    .getLabel("IMP_TOTAL") + " :", currency.toStringFormat());

            // Création lignes vides
            LXImpressionsUtils.addRowVoid(sheet, countLine++);
            LXImpressionsUtils.addRowVoid(sheet, countLine++);
        }
    }

    /**
     * Permet la récupération et la création du tableau des notes de crédit
     * 
     * @param wb
     * @param sheet
     * @throws Exception
     */
    protected void createTableNdc(HSSFWorkbook wb, HSSFSheet sheet) throws Exception {
        LXNoteDeCreditManager ndcManager = new LXNoteDeCreditManager();
        ndcManager.setSession(getSession());
        ndcManager.setForIdJournal(getIdJournal());
        ndcManager.setForCsEtatIn(listEtat);

        ArrayList forIdTypeOperationIn = new ArrayList();
        forIdTypeOperationIn.add(LXOperation.CS_TYPE_NOTEDECREDIT_DEBASE);
        ndcManager.setForCsTypeOperationIn(forIdTypeOperationIn);

        ndcManager.find(BManager.SIZE_NOLIMIT);

        if (ndcManager.size() > 0) {
            LXImpressionsUtils.addRowContentLibelleColumns(wb, sheet, countLine++, getSession().getLabel("IMP_NDC"),
                    getSession().getLabel("IMP_DATE"), getSession().getLabel("IMP_ECHEANCE"),
                    getSession().getLabel("IMP_FOURNISSEUR"), getSession().getLabel("IMP_NO_FAC_INTERNE"), getSession()
                            .getLabel("IMP_NO_FAC_EXTERNE"), getSession().getLabel("IMP_SOLDE"));
        }

        FWCurrency currency = new FWCurrency();
        boolean couleurLigne = false;

        for (int i = 0; i < ndcManager.size(); i++) {
            LXNoteDeCredit ope = (LXNoteDeCredit) ndcManager.get(i);

            currency.add(ope.getMontant());

            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, ope.getLibelle(), ope.getDateFacture(),
                    ope.getDateEcheance(), ope.getNomFournisseur(), ope.getIdExterne(), ope.getReferenceExterne(),
                    ope.getMontantFormatted(), couleurLigne);
            couleurLigne = couleurLigne ? false : true;
        }
        if (ndcManager.size() > 0) {
            LXImpressionsUtils.addRowContentLibelleColumns(wb, sheet, countLine++, "", "", "", "", "", getSession()
                    .getLabel("IMP_TOTAL") + " :", currency.toStringFormat());

            // Création lignes vides
            LXImpressionsUtils.addRowVoid(sheet, countLine++);
            LXImpressionsUtils.addRowVoid(sheet, countLine++);
        }
    }

    /**
     * Permet la récupération et la création du tableau des paiements
     * 
     * @param wb
     * @param sheet
     * @throws Exception
     */
    protected void createTablePaiements(HSSFWorkbook wb, HSSFSheet sheet) throws Exception {
        LXPaiementManager paiementManager = new LXPaiementManager();
        paiementManager.setSession(getSession());
        paiementManager.setForIdJournal(getIdJournal());
        paiementManager.setForCsEtatIn(listEtat);
        paiementManager.find(BManager.SIZE_NOLIMIT);

        if (paiementManager.size() > 0) {
            LXImpressionsUtils.addRowContentLibelleColumns(wb, sheet, countLine++, getSession()
                    .getLabel("IMP_PAIEMENT"), getSession().getLabel("IMP_DATE"),
                    getSession().getLabel("IMP_ECHEANCE"), getSession().getLabel("IMP_FOURNISSEUR"), getSession()
                            .getLabel("IMP_NO_FAC_INTERNE"), getSession().getLabel("IMP_NO_FAC_EXTERNE"), getSession()
                            .getLabel("IMP_SOLDE"));
        }

        FWCurrency currency = new FWCurrency();
        boolean couleurLigne = false;

        for (int i = 0; i < paiementManager.size(); i++) {
            LXPaiement ope = (LXPaiement) paiementManager.get(i);

            currency.add(ope.getMontant());

            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, ope.getLibelle(), ope.getDateFacture(),
                    ope.getDateEcheance(), ope.getNomFournisseur(), ope.getIdExterne(), ope.getReferenceExterne(),
                    ope.getMontantFormatted(), couleurLigne);
            couleurLigne = couleurLigne ? false : true;
        }
        if (paiementManager.size() > 0) {
            LXImpressionsUtils.addRowContentLibelleColumns(wb, sheet, countLine++, "", "", "", "", "", getSession()
                    .getLabel("IMP_TOTAL") + " :", currency.toStringFormat());

            // Création lignes vides
            LXImpressionsUtils.addRowVoid(sheet, countLine++);
            LXImpressionsUtils.addRowVoid(sheet, countLine++);
        }
    }

    /**
     * Renvois le sujet de l'email.
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
            return getSession().getLabel("IMP_GENERATION_RESUME_JOURNAL_ERROR");
        } else {
            return getSession().getLabel("IMP_GENERATION_RESUME_JOURNAL_OK");
        }
    }
}
