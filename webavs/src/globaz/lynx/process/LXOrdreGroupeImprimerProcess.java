package globaz.lynx.process;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.util.JACalendar;
import globaz.helios.process.consolidation.utils.CGImprimerConsolidationUtils;
import globaz.jade.common.Jade;
import globaz.lynx.db.escompte.LXEscompte;
import globaz.lynx.db.escompte.LXEscompteManager;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.paiement.LXPaiement;
import globaz.lynx.db.paiement.LXPaiementManager;
import globaz.lynx.utils.LXConstants;
import globaz.lynx.utils.LXImpressionsUtils;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class LXOrdreGroupeImprimerProcess extends LXOrdreGroupeProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.lynx.process.LXOrdreGroupeProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();

        createSheetForOrdreGroupe(wb);

        String outputFileName = Jade.getInstance().getHomeDir() + LXConstants.OUTPUT_FILE_WORK_DIR + "/"
                + LXConstants.OUTPUT_FILE_NAME_RESUME_ORDREGROUPE + "_"
                + LXConstants.NUMERO_REFERENCE_INFOROM_RESUME_ORDREGROUPE + ".xls";
        CGImprimerConsolidationUtils.createFile(wb, outputFileName);
        registerAttachedDocument(outputFileName);

        return true;
    }

    private void addContentToOrdreGroupe(HSSFWorkbook wb, HSSFSheet sheet) throws Exception {
        int countLine = 0;

        // -----------------------------
        // Informations générales
        // -----------------------------
        LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_ORDREGROUPE")
                + " : ", getOrdreGroupe().getIdOrdreGroupe() + " - " + getOrdreGroupe().getLibelle());
        LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_SOCIETEDEBITRICE")
                + " : ", getSociete().getIdExterne() + " - " + getSociete().getNom());
        LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_DATE_ECHEANCE")
                + " : ", getOrdreGroupe().getDateEcheance());

        // Création lignes vides
        LXImpressionsUtils.addRowVoid(sheet, countLine++);
        LXImpressionsUtils.addRowVoid(sheet, countLine++);

        // -----------------------------
        // Récupération des factures
        // -----------------------------
        LXPaiementManager manager = new LXPaiementManager();
        manager.setSession(getSession());
        manager.setForIdOrdreGroupe(getIdOrdreGroupe());

        ArrayList listEtat = new ArrayList();
        listEtat.add(LXOperation.CS_ETAT_COMPTABILISE);
        listEtat.add(LXOperation.CS_ETAT_OUVERT);
        listEtat.add(LXOperation.CS_ETAT_PREPARE);
        listEtat.add(LXOperation.CS_ETAT_SOLDE);
        listEtat.add(LXOperation.CS_ETAT_TRAITEMENT);
        manager.setForCsEtatIn(listEtat);

        manager.find(BManager.SIZE_NOLIMIT);

        if (manager.size() > 0) {
            LXImpressionsUtils.addRowContentLibelleColumns(wb, sheet, countLine++, getSession()
                    .getLabel("IMP_PAIEMENT"), getSession().getLabel("IMP_DATE"), null,
                    getSession().getLabel("IMP_FOURNISSEUR"), getSession().getLabel("IMP_NO_FAC_INTERNE"), getSession()
                            .getLabel("IMP_NO_FAC_EXTERNE"), getSession().getLabel("IMP_SOLDE"));
        }

        boolean couleurLigne = false;
        FWCurrency currency = new FWCurrency();

        for (int i = 0; i < manager.size(); i++) {
            LXPaiement ope = (LXPaiement) manager.get(i);

            currency.add(ope.getMontant());

            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, ope.getLibelle(), ope.getDateFacture(),
                    null, ope.getNomFournisseur(), ope.getIdExterne(), ope.getReferenceExterne(),
                    ope.getMontantFormatted(), couleurLigne);
            couleurLigne = couleurLigne ? false : true;
        }

        if (manager.size() > 0) {
            LXImpressionsUtils.addRowContentLibelleColumns(wb, sheet, countLine++, "", "", null, "", "", getSession()
                    .getLabel("IMP_TOTAL") + " :", currency.toStringFormat());

            // Création lignes vides
            LXImpressionsUtils.addRowVoid(sheet, countLine++);
            LXImpressionsUtils.addRowVoid(sheet, countLine++);
            LXImpressionsUtils.addRowVoid(sheet, countLine++);
        }

        // -----------------------------
        // Récupération des NDC de base
        // -----------------------------

        LXEscompteManager escompteManager = new LXEscompteManager();
        escompteManager.setSession(getSession());
        escompteManager.setForIdOrdreGroupe(getIdOrdreGroupe());
        escompteManager.setForCsEtatIn(listEtat);
        escompteManager.find(BManager.SIZE_NOLIMIT);

        if (escompteManager.size() > 0) {
            LXImpressionsUtils.addRowContentLibelleColumns(wb, sheet, countLine++, getSession()
                    .getLabel("IMP_ESCOMPTE"), getSession().getLabel("IMP_DATE"), null,
                    getSession().getLabel("IMP_FOURNISSEUR"), getSession().getLabel("IMP_NO_FAC_INTERNE"), getSession()
                            .getLabel("IMP_NO_FAC_EXTERNE"), getSession().getLabel("IMP_SOLDE"));
        }

        currency = new FWCurrency();
        couleurLigne = false;

        for (int i = 0; i < escompteManager.size(); i++) {
            LXEscompte ope = (LXEscompte) escompteManager.get(i);

            currency.add(ope.getMontant());

            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, ope.getLibelle(), ope.getDateFacture(),
                    null, ope.getNomFournisseur(), ope.getIdExterne(), ope.getReferenceExterne(),
                    ope.getMontantFormatted(), couleurLigne);
            couleurLigne = couleurLigne ? false : true;
        }
        if (escompteManager.size() > 0) {
            LXImpressionsUtils.addRowContentLibelleColumns(wb, sheet, countLine++, "", "", null, "", "", getSession()
                    .getLabel("IMP_TOTAL") + " :", currency.toStringFormat());
        }
    }

    /**
     * Créer la feuille excel contenant les comptes du bilan.
     * 
     * @param wb
     * @param exerciceComptable
     * @throws Exception
     */
    private void createSheetForOrdreGroupe(HSSFWorkbook wb) throws Exception {
        HSSFSheet sheet = wb.createSheet(getSession().getLabel("IMP_RESUME_ORDREGROUPE"));
        LXImpressionsUtils.initPage(sheet, true);

        LXImpressionsUtils.addHeaderFooter(sheet, getSession(), "", getSession().getLabel("IMP_RESUME_ORDREGROUPE"),
                "", LXConstants.NUMERO_REFERENCE_INFOROM_RESUME_ORDREGROUPE + " - LXOrdreGroupeImprimerProcess - "
                        + JACalendar.todayJJsMMsAAAA() + " - " + getSession().getUserFullName(), "");

        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_LIBELLE, 30);
        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_SOLDE_A,
                LXConstants.INDEX_COLUMN_SOLDE_F, "1'000'000'000");
        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_SOLDE_B, 30);
        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_SOLDE_C, 15);
        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_SOLDE_D, 15);

        addContentToOrdreGroupe(wb, sheet);
    }

    /**
     * Renvois le sujet de l'email.
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
            return getSession().getLabel("IMP_GENERATION_RESUME_ORDREGROUPE_ERROR");
        } else {
            return getSession().getLabel("IMP_GENERATION_RESUME_ORDREGROUPE_OK");
        }
    }

}
