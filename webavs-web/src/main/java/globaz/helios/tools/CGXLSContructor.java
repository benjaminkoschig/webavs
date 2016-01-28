package globaz.helios.tools;

import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.application.CGApplication;
import globaz.helios.itext.list.CGProcessImpressionAnalyseBudgetaire;
import globaz.helios.itext.list.CGProcessImpressionBalanceComptes;
import globaz.helios.itext.list.CGProcessImpressionBilan;
import globaz.helios.itext.list.CGProcessImpressionPertesProfits;
import globaz.helios.itext.list.CGProcessImpressionPlanComptable;
import globaz.helios.itext.list.balance.comptes.CGCompteSoldeBean;
import globaz.helios.itext.list.grand.livre.CGGrandLivre_Bean;
import globaz.helios.itext.list.journal.ecritures.CGJournalEcritures_Bean;
import globaz.helios.itext.list.releveAVS.CGReleveAVS_Bean;
import globaz.helios.itext.list.utils.CGGeneric_Bean;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.op.excelml.model.document.ExcelmlBuilder;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author jsi
 * @revision SEL mai 2013 - multi niveaux de classification
 * 
 */
public class CGXLSContructor {

    private CGHeliosContainer contData = new CGHeliosContainer();
    private String documentFileName = "";
    private String exportPath = "";
    private String importPath = "";
    private List listBeans = new ArrayList();
    private Map<String, Comparable<?>> param = new HashMap<String, Comparable<?>>();
    private BSession session;

    /**
	 * 
	 */
    public CGXLSContructor() {
    }

    /**
     * @param debit
     * @param credit
     */
    private void calculSoldeDebitCredit(Double debit, Double credit) {
        if ((debit != null) && ((credit == null) || (credit == 0))) {
            this.remplirColumn("COL_5_VALUE", debit, "0.00");
            this.remplirColumn("COL_6_VALUE", "", "");
        } else if (((debit == null) || (debit == 0)) && (credit != null)) {
            this.remplirColumn("COL_5_VALUE", "", "");
            this.remplirColumn("COL_6_VALUE", credit, "0.00");
        } else if ((debit != null) && (credit != null)) {
            double result = debit - credit;
            if (result > 0) {
                this.remplirColumn("COL_5_VALUE", result, "0.00");
                this.remplirColumn("COL_6_VALUE", "", "");
            } else {
                this.remplirColumn("COL_5_VALUE", "", "");
                this.remplirColumn("COL_6_VALUE", (result * -1), "0.00");
            }
        } else {
            this.remplirColumn("COL_5_VALUE", "", "");
            this.remplirColumn("COL_6_VALUE", "", "");
        }
    }

    /**
     * @return the documentFileName
     */
    public String getDocumentFileName() {
        return documentFileName;
    }

    /**
     * @return the exportPath
     */
    public String getExportPath() {
        return exportPath;
    }

    /**
     * @return the importPath
     */
    public String getImportPath() {
        return importPath;
    }

    public BSession getSession() {
        return session;
    }

    /**
     * @param ligne
     * @return
     * @throws Exception
     */
    private String giveLibelle(String codeSystem) {
        try {
            return CodeSystem.getLibelle(getSession(), codeSystem);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Load the source file and prepare it for later use
     * 
     * @return
     * @throws Exception
     */
    private ExcelmlWorkbook load() throws Exception {
        importPath = Jade.getInstance().getExternalModelDir() + CGApplication.APPLICATION_HELIOS_REP
                + "/model/excelml/" + session.getIdLangueISO().toUpperCase() + "/" + getDocumentFileName()
                + "Modele.xml";
        ExcelmlWorkbook wk = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(importPath);
            wk = ExcelmlBuilder.getDocument(fis);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return wk;
    }

    /**
     * Détermine la valeur à utiliser
     * 
     * @param mvt
     * @param mvtTotal
     * @return
     */
    private Double montant(Double mvt, Double mvtTotal) {
        if (mvt != null) {
            return mvt;
        } else if (mvtTotal != null) {
            return mvtTotal;
        } else {
            return null;
        }
    }

    /**
     * Permet de remplir chaque colonne de la liste
     */
    private void prepareDataAnalyseBudgetaire() {
        this.remplirColumn("numeroInforom", CGProcessImpressionAnalyseBudgetaire.NUMERO_REFERENCE_INFOROM, "");

        double soldeTotal = 0;
        double budgetTotal = 0;
        double budgetPCTotal = 0;
        double execTotal = 0;

        for (int i = 0; i < listBeans.size(); i++) {
            CGCompteSoldeBean ligne = (CGCompteSoldeBean) listBeans.get(i);

            this.remplirColumn("COL_1_VALUE", ligne.getNoClasseLibelle(), "");
            this.remplirColumn("COL_3_VALUE", ligne.getNoCompteLibelle(), "");

            // Calcul du solde
            Double solde = ligne.computeSoldeCharge() != null ? ligne.computeSoldeCharge() : ligne
                    .computeSoldeProduit();
            this.remplirColumn("COL_4_VALUE", solde, "");
            this.remplirColumn("COL_5_VALUE", ligne.getBudget(), "");
            this.remplirColumn("COL_6_VALUE", ligne.getBudgetPourcent(), "");
            this.remplirColumn("COL_7_VALUE", ligne.getSoldeExerciceComparaison(), "");

            soldeTotal += solde == null ? 0.0 : solde;
            budgetTotal += ligne.getBudget() == null ? 0.0 : ligne.getBudget();
            budgetPCTotal += ligne.getBudgetPourcent() == null ? 0.0 : ligne.getBudgetPourcent();
            execTotal += ligne.getSoldeExerciceComparaison() == null ? 0.0 : ligne.getSoldeExerciceComparaison();
        }

        this.remplirColumn("TOTAL_SOLDE", soldeTotal, "");
        this.remplirColumn("TOTAL_BUDGET", budgetTotal, "");
        this.remplirColumn("TOTAL_BUDGET_PC", budgetPCTotal, "");
        this.remplirColumn("TOTAL_EXEC", execTotal, "");
    }

    /**
	 * 
	 */
    private void prepareDataBalanceDesComptes() {
        double mvtDebitTotal = 0;
        double mvtCreditTotal = 0;

        for (int i = 0; i < listBeans.size(); i++) {
            CGCompteSoldeBean ligne = (CGCompteSoldeBean) listBeans.get(i);

            if (ligne.getMvtDebit() != null) {
                mvtDebitTotal += ligne.getMvtDebit();
            }
            if (ligne.getMvtCredit() != null) {
                mvtCreditTotal += ligne.getMvtCredit();
            }

            Double debit = montant(ligne.getMvtDebit(), ligne.getMvtDebitTotal());
            Double credit = montant(ligne.getMvtCredit(), ligne.getMvtCreditTotal());
            this.remplirColumn("COL_1_VALUE", ligne.getNoClasseLibelle(), "");
            this.remplirColumn("COL_2_VALUE", ligne.getNoCompteLibelle(), "");
            this.remplirColumn("COL_3_VALUE", debit, "");
            this.remplirColumn("COL_4_VALUE", credit, "");

            // Calcul du solde
            calculSoldeDebitCredit(debit, credit);
        }

        this.remplirColumn("TOTAL_FINAL_MVT_DEBIT", mvtDebitTotal, "0.00");
        this.remplirColumn("TOTAL_FINAL_MVT_CREDIT", mvtCreditTotal, "0.00");

    }

    private void prepareDataBalMvtJournal() {
        int l = listBeans.size();
        CGGeneric_Bean gb = new CGGeneric_Bean();
        String index = "", indexShort = "";
        String content = "";
        double contentDouble = 0;
        contData.addValue("TOTAL_ENTREES", l + "");

        for (int i = 0; i < l; i++) {
            gb = (CGGeneric_Bean) listBeans.get(i);
            if ((gb.getCol(2) != null) && (gb.getCol(2).toString().length() >= 4)) {
                index = gb.getCol(2).toString().substring(0, 4);
                indexShort = index.substring(0, 1);
            }
            String contentme = "";
            int m = gb.getColSize("me");
            for (int j = 0; j < m; j++) {
                if (gb.getColME(j) != null) {
                    contentme = contentme + " " + gb.getColME(j);
                }
            }
            m = gb.getColSize("");
            for (int j = 0; j < m; j++) {
                if (gb.getCol(j) != null) {
                    content = gb.getCol(j).toString();
                    try {
                        contentDouble = new Double(content).doubleValue();
                        if (j > 0) {
                            content = String.valueOf(new Double(content));
                        }
                    } catch (NumberFormatException e) {
                        contentDouble = 0;
                    }
                } else {
                    content = "";
                    contentDouble = 0;
                }

                if (j == 5) {
                    contData.addValue("COL_" + j + "_VALUE", JANumberFormatter.deQuote(content));
                    contData.addValue("COL_" + j + "_VALUE" + index, JANumberFormatter.deQuote(content));
                } else {
                    contData.addValue("COL_" + j + "_VALUE", content);
                    contData.addValue("COL_" + j + "_VALUE" + index, content);
                }

                if (content.toString().length() > 15) {
                    contData.addValue("COL_" + j + "_ACC" + index, content.substring(0, 14));
                    contData.addValue("COL_" + j + "_LIB" + index, content.substring(15) + contentme);
                }
                contData.addValueToTotal("COL_" + j + "_TOTAL" + index, contentDouble);
                contData.addValueToTotal("COL_" + j + "_TOTAL" + indexShort, contentDouble);
                contData.addValueToTotal("COL_" + j + "_TOTAL", contentDouble);
            }
        }
    }

    /**
	 * 
	 */
    private void prepareDataBilan() {

        double passifsTotal = 0;
        double actifsTotal = 0;

        for (int i = 0; i < listBeans.size(); i++) {
            CGCompteSoldeBean ligne = (CGCompteSoldeBean) listBeans.get(i);

            if (ligne.computeSoldeActif() != null) {
                actifsTotal += ligne.computeSoldeActif();
            }
            if (ligne.computeSoldePassif() != null) {
                passifsTotal += ligne.computeSoldePassif();
            }

            Double actif = montant(ligne.computeSoldeActif(), ligne.getTotalActif());
            Double passif = montant(ligne.computeSoldePassif(), ligne.getTotalPassif());

            this.remplirColumn("COL_1_VALUE", ligne.getNoClasseLibelle(), "");
            this.remplirColumn("COL_2_VALUE", ligne.getNoCompteLibelle(), "");
            this.remplirColumn("COL_3_VALUE", actif, "");
            this.remplirColumn("COL_4_VALUE", passif, "");
        }

        this.remplirColumn("TOTAL_FINAL_ACTIFS", actifsTotal, "0.00");
        this.remplirColumn("TOTAL_FINAL_PASSIFS", passifsTotal, "0.00");

        double resultat = passifsTotal - actifsTotal;
        if (resultat > 0) {
            this.remplirColumn("RESULTAT_ACTIF", resultat, "0.00");
            contData.addValue("RESULTAT_PASSIF", "");
        } else {
            contData.addValue("RESULTAT_ACTIF", "");
            this.remplirColumn("RESULTAT_PASSIF", (resultat * -1), "0.00");
        }
    }

    /**
     * 
     * @param listBean
     */
    private void prepareDataGrandLivre() {
        double solde = 0;
        double totalDebit = 0;
        double totalCredit = 0;
        int l = listBeans.size();

        for (int i = 0; i < l; i++) {
            CGGrandLivre_Bean glb = (CGGrandLivre_Bean) listBeans.get(i);

            String numeroCompte = glb.getCOL_13().toString().substring(0, 14);

            if (glb.getCOL_1() != null) {
                contData.addValue("COL_0_VALUE", glb.getCOL_1().toString());
            } else {
                contData.addValue("COL_0_VALUE", "");
            }

            if (glb.getCOL_2() != null) {
                contData.addValue("COL_2_VALUE", glb.getCOL_2().toString());
            } else {
                contData.addValue("COL_2_VALUE", "");
            }

            contData.addValue("COL_3_VALUE", numeroCompte);

            if (glb.getCOL_3() != null) {
                contData.addValue("COL_4_VALUE", glb.getCOL_3().toString());
            } else {
                contData.addValue("COL_4_VALUE", "");
            }
            if (glb.getCOL_4() != null) {
                contData.addValue("COL_5_VALUE", glb.getCOL_4().toString());
            } else {
                contData.addValue("COL_5_VALUE", "");
            }
            if (!JadeStringUtil.isEmpty(glb.getCOL_5())) {
                contData.addValue("COL_6_VALUE", glb.getCOL_5());
            } else {
                contData.addValue("COL_6_VALUE", "");
            }
            if (!JadeStringUtil.isEmpty(glb.getCOL_7())) {
                contData.addValue("COL_7_VALUE", glb.getCOL_7());
            } else {
                contData.addValue("COL_7_VALUE", "");
            }
            if ((glb.getCOL_8() != null) && (glb.getCOL_8A() == null)) {
                contData.addValue("COL_8_VALUE", glb.getCOL_8().toString());
            } else if ((glb.getCOL_8() != null) && (glb.getCOL_8A() != null)) {
                contData.addValue("COL_8_VALUE", glb.getCOL_8().toString() + " " + glb.getCOL_8A());
            } else {
                contData.addValue("COL_8_VALUE", "");
            }
            if (!JadeStringUtil.isEmpty(glb.getCOL_9())) {
                contData.addValue("COL_9_VALUE", glb.getCOL_9());
            } else {
                contData.addValue("COL_9_VALUE", "");
            }
            if (glb.getCOL_10() != null) {
                contData.addValue("COL_10_VALUE", String.valueOf(glb.getCOL_10()));
                contData.addValueToTotal("COL_10_TOTAL", glb.getCOL_10().doubleValue());
                solde += glb.getCOL_10().doubleValue();
                totalDebit += glb.getCOL_10().doubleValue();
            } else {
                if (glb.getCOL_10_ITALIC() != null) {
                    contData.addValue("COL_10_VALUE", String.valueOf(glb.getCOL_10_ITALIC()));
                    contData.addValueToTotal("COL_10_TOTAL", glb.getCOL_10_ITALIC().doubleValue());
                    solde += glb.getCOL_10_ITALIC().doubleValue();
                    totalDebit += glb.getCOL_10_ITALIC().doubleValue();
                } else {
                    contData.addValue("COL_10_VALUE", "");
                }
            }
            if (glb.getCOL_11() != null) {
                contData.addValue("COL_11_VALUE", String.valueOf(glb.getCOL_11()));
                contData.addValueToTotal("COL_11_TOTAL", glb.getCOL_11().doubleValue());
                solde -= glb.getCOL_11().doubleValue();
                totalCredit += glb.getCOL_11().doubleValue();
            } else {
                if (glb.getCOL_11_ITALIC() != null) {
                    contData.addValue("COL_11_VALUE", String.valueOf(glb.getCOL_11_ITALIC()));
                    contData.addValueToTotal("COL_11_TOTAL", glb.getCOL_11_ITALIC().doubleValue());
                    solde -= glb.getCOL_11_ITALIC().doubleValue();
                    totalCredit += glb.getCOL_11_ITALIC().doubleValue();
                } else {
                    contData.addValue("COL_11_VALUE", "");
                }
            }

            contData.addValue("COL_12_VALUE", String.valueOf(new Double(solde)));

            boolean prochainNumeroCompteDifferent = true;
            if ((i + 1) != l) {
                CGGrandLivre_Bean prochainGlb = (CGGrandLivre_Bean) listBeans.get(i + 1);
                String prochainNumeroCompte = prochainGlb.getCOL_13().toString().substring(0, 14);

                prochainNumeroCompteDifferent = !prochainNumeroCompte.equals(numeroCompte);
            }

            if (prochainNumeroCompteDifferent) {
                for (int j = 0; j < 13; j++) {
                    if (j == 3) {
                        contData.addValue("COL_" + j + "_VALUE", numeroCompte);
                    } else if (j == 5) {
                        // TODO Externaliser le libellé ci-dessous
                        contData.addValue("COL_" + j + "_VALUE", " * Total");
                    } else if (j == 10) {
                        contData.addValue("COL_" + j + "_VALUE", String.valueOf(new Double(totalDebit)));
                    } else if (j == 11) {
                        contData.addValue("COL_" + j + "_VALUE", String.valueOf(new Double(totalCredit)));
                    } else if (j == 12) {
                        contData.addValue("COL_" + j + "_VALUE", String.valueOf(new Double(solde)));
                    } else {
                        contData.addValue("COL_" + j + "_VALUE", "");
                    }
                }
                solde = 0;
                totalDebit = 0;
                totalCredit = 0;
            }

        }
    }

    private void prepareDataListJournalEcritures() {
        int l = listBeans.size();
        CGJournalEcritures_Bean glb = new CGJournalEcritures_Bean();
        for (int i = 0; i < l; i++) {
            glb = (CGJournalEcritures_Bean) listBeans.get(i);
            if (glb.getCOL_1() != null) {
                contData.addValue("COL_1_VALUE", glb.getCOL_1().toString());
            } else {
                contData.addValue("COL_1_VALUE", "");
            }
            if (!JadeStringUtil.isEmpty(glb.getCOL_2())) {
                contData.addValue("COL_2_VALUE", glb.getCOL_2());
            } else {
                contData.addValue("COL_2_VALUE", "");
            }
            if (glb.getCOL_3() != null) {
                contData.addValue("COL_3_ACC_VALUE", glb.getCOL_3().substring(0, 14));
                contData.addValue("COL_3_LIB_VALUE", glb.getCOL_3().substring(15));
            } else {
                contData.addValue("COL_3_ACC_VALUE", "");
                contData.addValue("COL_3_LIB_VALUE", "");
            }
            if (glb.getCOL_4() != null) {
                contData.addValue("COL_4_VALUE", glb.getCOL_4().toString());
            } else {
                contData.addValue("COL_4_VALUE", "");
            }
            if (!JadeStringUtil.isEmpty(glb.getCOL_5())) {
                contData.addValue("COL_5_VALUE", glb.getCOL_5());
            } else {
                contData.addValue("COL_5_VALUE", "");
            }
            if (!JadeStringUtil.isEmpty(glb.getCOL_7())) {
                contData.addValue("COL_6_VALUE", glb.getCOL_7());
            } else {
                contData.addValue("COL_6_VALUE", "");
            }
            if (glb.getCOL_8() != null) {
                contData.addValue("COL_7_VALUE", glb.getCOL_8().toString());
            } else {
                contData.addValue("COL_7_VALUE", "");
            }
            if (glb.getCOL_9() != null) {
                contData.addValue("COL_8_VALUE", glb.getCOL_9().toString());
            } else {
                contData.addValue("COL_8_VALUE", "");
            }
            if (glb.getCOL_10() != null) {
                contData.addValue("COL_9_VALUE", JANumberFormatter.deQuote(String.valueOf(glb.getCOL_10())));
                contData.addValueToTotal("COL_9_TOTAL", glb.getCOL_10().doubleValue());
            } else {
                contData.addValue("COL_9_VALUE", "");
            }
            if (glb.getCOL_11() != null) {
                contData.addValue("COL_10_VALUE", JANumberFormatter.deQuote(String.valueOf(glb.getCOL_11())));
                contData.addValueToTotal("COL_10_TOTAL", glb.getCOL_11().doubleValue());
            } else {
                contData.addValue("COL_10_VALUE", "");
            }
        }
    }

    /**
	 * 
	 */
    private void prepareDataPertesProfits() {
        if (param != null) {
            contData.addValue("P_TITRE_DATE_VAL", param.get("P_TITRE") + " " + param.get("P_DATE_VAL"));
        }

        for (int i = 0; i < listBeans.size(); i++) {
            CGCompteSoldeBean ligne = (CGCompteSoldeBean) listBeans.get(i);

            Double charges = montant(ligne.computeSoldeCharge(), ligne.getTotalCharges());
            Double produits = montant(ligne.computeSoldeProduit(), ligne.getTotalProduits());

            this.remplirColumn("COL_1_VALUE", ligne.getNoClasseLibelle(), "");
            this.remplirColumn("COL_2_VALUE", ligne.getNoCompteLibelle(), "");
            this.remplirColumn("COL_3_VALUE", charges, "");
            this.remplirColumn("COL_4_VALUE", produits, "");
        }
    }

    /**
	 * 
	 */
    private void prepareDataPlanComptable() {
        for (int i = 0; i < listBeans.size(); i++) {
            CGCompteSoldeBean ligne = (CGCompteSoldeBean) listBeans.get(i);

            contData.addValue("COL_1_VALUE", ligne.getNoClasseLibelle());
            contData.addValue("COL_2_VALUE", ligne.getNoCompteLibelle());
            contData.addValue("COL_3_VALUE", giveLibelle(ligne.getIdDomaine()));
            contData.addValue("COL_4_VALUE", giveLibelle(ligne.getIdGenre()));
            contData.addValue("COL_5_VALUE", giveLibelle(ligne.getIdNature()));
            contData.addValue("COL_6_VALUE", ligne.getMonnaieCodeIso());
        }
    }

    private void prepareDataReleveAVS() {

        for (int i = 0; i < listBeans.size(); i++) {
            CGReleveAVS_Bean bean = (CGReleveAVS_Bean) listBeans.get(i);

            Double actif = bean.getCOL_2();
            Double passif = bean.getCOL_3();

            if (bean.getCOL_0() != null) {
                contData.addValue("COL_1_VALUE", bean.getCOL_0());
            } else {
                contData.addValue("COL_1_VALUE", "");
            }

            if (bean.getCOL_1() != null) {
                contData.addValue("COL_2_VALUE", bean.getCOL_1());
            } else {
                contData.addValue("COL_2_VALUE", "");
            }

            if (actif != null) {
                contData.addValue("COL_3_VALUE", String.valueOf(actif));
            } else {
                contData.addValue("COL_3_VALUE", "");
            }

            if (passif != null) {
                contData.addValue("COL_4_VALUE", String.valueOf(passif));
            } else {
                contData.addValue("COL_4_VALUE", "");
            }
        }
    }

    private void prepareParameters() {
        contData.addMap(param);
    }

    public void process(List listBeanDoc, Map<String, Comparable<?>> parameters) throws Exception {
        ExcelmlWorkbook wk = load();
        listBeans = listBeanDoc;
        param = parameters;

        if (documentFileName.equals("GrandLivre")) {
            prepareDataGrandLivre();
        } else if (documentFileName.equals("ListJournalEcritures")) {
            prepareDataListJournalEcritures();
        } else if ("BalMvtJournal".equalsIgnoreCase(getDocumentFileName())) {
            prepareDataBalMvtJournal();
        } else if (documentFileName.equals(CGProcessImpressionPertesProfits.XLS_DOC_NAME)) {
            prepareDataPertesProfits();
        } else if (documentFileName.equals("ReleveAVS")) {
            prepareDataReleveAVS();
        } else if (documentFileName.equals(CGProcessImpressionPlanComptable.XLS_DOC_NAME)) {
            prepareDataPlanComptable();
        } else if (documentFileName.equals(CGProcessImpressionAnalyseBudgetaire.XLS_DOC_NAME)) {
            prepareDataAnalyseBudgetaire();
        } else if (documentFileName.equals(CGProcessImpressionBilan.XLS_DOC_NAME)) {
            prepareDataBilan();
        } else if (documentFileName.equals(CGProcessImpressionBalanceComptes.XLS_DOC_NAME)) {
            prepareDataBalanceDesComptes();
        }

        prepareParameters();
        wk.mergeDocument(contData);
        save(wk);
    }

    /**
     * Permet de remplir une colonne par la valeur passée en parametre.
     * 
     * @param container
     * @param column
     * @param value
     */
    private void remplirColumn(String column, Double value, String defaultValue) {
        if (value != null) {
            contData.addValue(column, value.toString());
        } else {
            contData.addValue(column, defaultValue);
        }
    }

    /**
     * Permet de remplir une colonne par la valeur passée en parametre.
     * 
     * @param container
     * @param column
     * @param value
     */
    private void remplirColumn(String column, String value, String defaultValue) {
        if (!JadeStringUtil.isEmpty(value)) {
            contData.addValue(column, value);
        } else {
            contData.addValue(column, defaultValue);
        }
    }

    /**
     * 
     * @param wk
     * @throws Exception
     */
    private void save(ExcelmlWorkbook wk) throws Exception {
        exportPath = Jade.getInstance().getPersistenceDir()
                + JadeFilenameUtil.addOrReplaceFilenameSuffixUID(getDocumentFileName() + ".xml");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(exportPath);
            wk.write(fos);
            fos.flush();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * @param documentFileName
     *            the documentFileName to set
     */
    public void setDocumentFileName(String documentFileName) {
        this.documentFileName = documentFileName;
    }

    /**
     * @param exportPath
     *            the exportPath to set
     */
    public void setExportPath(String exportPath) {
        this.exportPath = exportPath;
    }

    /**
     * @param importPath
     *            the importPath to set
     */
    public void setImportPath(String importPath) {
        this.importPath = importPath;
    }

    public void setSession(BSession session) {
        this.session = session;
    }
}
