package globaz.lynx.process;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.process.consolidation.utils.CGImprimerConsolidationUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.lynx.db.impression.LXImpressionBalance;
import globaz.lynx.db.impression.LXImpressionBalanceManager;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitrice;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitriceManager;
import globaz.lynx.utils.LXConstants;
import globaz.lynx.utils.LXImpressionsUtils;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class LXImpressionBalanceProcess extends LXImpressionProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.lynx.process.LXImpressionProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();

        if (!JadeStringUtil.isEmpty(getIdSociete())) {
            createSheetForBalance(wb, getSociete());
        } else {

            // Recherche de toutes les sociétées
            LXSocieteDebitriceManager manager = new LXSocieteDebitriceManager();
            manager.setSession(getSession());
            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            for (int i = 0; i < manager.size(); i++) {
                LXSocieteDebitrice societeDeb = (LXSocieteDebitrice) manager.getEntity(i);
                createSheetForBalance(wb, societeDeb);
            }

        }
        String outputFileName = Jade.getInstance().getHomeDir() + LXConstants.OUTPUT_FILE_WORK_DIR + "/"
                + LXConstants.OUTPUT_FILE_NAME_BALANCE + "_" + LXConstants.NUMERO_REFERENCE_INFOROM_BALANCE + ".xls";
        CGImprimerConsolidationUtils.createFile(wb, outputFileName);
        this.registerAttachedDocument(outputFileName);

        return true;
    }

    /**
     * Crée le contenu du fichier excel
     * 
     * @param wb
     * @param sheet
     * @throws Exception
     */
    private void addContentToBalance(HSSFWorkbook wb, HSSFSheet sheet, LXSocieteDebitrice societeDeb) throws Exception {

        int countLine = 0;

        // -----------------------------
        // Informations générales
        // -----------------------------
        LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_SOCIETEDEBITRICE")
                + " : ", societeDeb.getIdExterne() + " - " + societeDeb.getNom());

        if (!JadeStringUtil.isIntegerEmpty(getDateDebut())) {
            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_DATE_DEPUIS")
                    + " : ", getDateDebut());
        }
        if (!JadeStringUtil.isIntegerEmpty(getDateFin())) {
            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_DATE_FIN")
                    + " : ", getDateFin());
        }
        if (!JadeStringUtil.isIntegerEmpty(getMontantMini())) {
            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_MONTANT_MIN")
                    + " : ", getMontantMini());
        }
        if (!JadeStringUtil.isIntegerEmpty(getMontantMaxi())) {
            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_MONTANT_MAX")
                    + " : ", getMontantMaxi());
        }
        if (!JadeStringUtil.isEmpty(getCsCategorie())) {
            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_CATEGORIE")
                    + " : ", getSession().getCodeLibelle(getCsCategorie()));
        }
        if (getEstBloque().booleanValue()) {
            LXImpressionsUtils
                    .addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_EST_BLOQUE"), "");

            if (!JadeStringUtil.isEmpty(getCsMotifBlocage())) {
                LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++,
                        getSession().getLabel("IMP_MOTIF_BLOCAGE") + " : ",
                        getSession().getCodeLibelle(getCsMotifBlocage()));
            }
        }
        if (!JadeStringUtil.isEmpty(getSelection())) {
            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_SELECTION")
                    + " : ", getSession().getLabel("IMP_" + getSelection()));
        }
        LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_ETAT") + " : ",
                getSession().getLabel("IMP_" + getEtat()));

        // Création lignes vides
        LXImpressionsUtils.addRowVoid(sheet, countLine++);
        LXImpressionsUtils.addRowVoid(sheet, countLine++);

        // Création du contenu du tableau
        LXImpressionBalanceManager manager = new LXImpressionBalanceManager();
        manager.setSession(getSession());
        manager.setForIdSociete(societeDeb.getIdSociete());
        manager.setForDateDebut(getDateDebut());
        manager.setForDateFin(getDateFin());
        manager.setForMontantMaxi(getMontantMaxi());
        manager.setForMontantMini(getMontantMini());
        manager.setForSelection(getSelection());
        manager.setEstBloque(getEstBloque());
        manager.setForCsMotifBlocage(getCsMotifBlocage());
        manager.setForCsCategorie(getCsCategorie());

        ArrayList<String> listEtat = new ArrayList<String>();
        listEtat.add(LXOperation.CS_ETAT_COMPTABILISE);
        listEtat.add(LXOperation.CS_ETAT_PREPARE);
        listEtat.add(LXOperation.CS_ETAT_SOLDE);

        if (!JadeStringUtil.isEmpty(getEtat()) && LXConstants.ETAT_PROVISOIRE.equals(getEtat())) {
            listEtat.add(LXOperation.CS_ETAT_OUVERT);
            listEtat.add(LXOperation.CS_ETAT_TRAITEMENT);
        }
        manager.setForCsEtatIn(listEtat);

        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        if (manager.size() > 0) {
            LXImpressionsUtils.addRowContentLibelleColumns(wb, sheet, countLine++,
                    getSession().getLabel("IMP_FOURNISSEUR"), getSession().getLabel("IMP_DEBIT"), getSession()
                            .getLabel("IMP_CREDIT"), getSession().getLabel("IMP_SOLDE"));

            boolean couleurLigne = false;
            FWCurrency debitSomme = new FWCurrency();
            FWCurrency creditSomme = new FWCurrency();
            FWCurrency soldeSomme = new FWCurrency();

            for (int i = 0; i < manager.size(); i++) {
                LXImpressionBalance imp = (LXImpressionBalance) manager.getEntity(i);

                FWCurrency negateDebit = new FWCurrency();
                negateDebit.add(imp.getDebit());
                negateDebit.negate();

                debitSomme.add(negateDebit);
                soldeSomme.add(imp.getSolde());
                creditSomme.add(imp.getCredit());

                LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, imp.getIdExterne(),
                        imp.getNomComplet(),
                        JadeStringUtil.parseDouble(JANumberFormatter.deQuote(negateDebit.toString()), 0),
                        JadeStringUtil.parseDouble(imp.getCredit(), 0), JadeStringUtil.parseDouble(imp.getSolde(), 0),
                        couleurLigne);
                couleurLigne = couleurLigne ? false : true;
            }

            LXImpressionsUtils.addRowContentLibelleColumns(wb, sheet, countLine++, getSession().getLabel("IMP_TOTAL"),
                    JANumberFormatter.fmt(JANumberFormatter.deQuote(debitSomme.toString()), true, true, false, 2),
                    JANumberFormatter.fmt(JANumberFormatter.deQuote(creditSomme.toString()), true, true, false, 2),
                    JANumberFormatter.fmt(JANumberFormatter.deQuote(soldeSomme.toString()), true, true, false, 2));
        }
    }

    /**
     * Créer la feuille excel contenant la balance.
     * 
     * @param wb
     * @throws Exception
     */
    private void createSheetForBalance(HSSFWorkbook wb, LXSocieteDebitrice societeDeb) throws Exception {

        String libelleSheet = getSession().getLabel("IMP_BALANCE") + " - " + societeDeb.getIdExterne() + " - "
                + societeDeb.getNom();
        HSSFSheet sheet = wb.createSheet(libelleSheet.length() > 31 ? libelleSheet.substring(0, 30) : libelleSheet);

        LXImpressionsUtils.addHeaderFooter(
                sheet,
                getSession(),
                "",
                getSession().getLabel("IMP_BALANCE"),
                "",
                LXConstants.NUMERO_REFERENCE_INFOROM_BALANCE + " - LXImpressionBalanceProcess - "
                        + JACalendar.todayJJsMMsAAAA() + " - " + getSession().getUserFullName(), "");

        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_LIBELLE, 10);
        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_SOLDE_A, 40);
        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_SOLDE_B,
                LXConstants.INDEX_COLUMN_SOLDE_F, "1'000'000'000");

        addContentToBalance(wb, sheet, societeDeb);
    }

    /**
     * Renvois le sujet de l'email.
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
            return getSession().getLabel("IMP_GENERATION_BALANCE_ERROR");
        } else {
            return getSession().getLabel("IMP_GENERATION_BALANCE_OK");
        }
    }
}
