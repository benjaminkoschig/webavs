package globaz.lynx.process;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.process.consolidation.utils.CGImprimerConsolidationUtils;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.lynx.db.impression.LXImpressionBalanceAgee;
import globaz.lynx.db.impression.LXImpressionBalanceAgeeManager;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.operation.LXOperationManager;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitrice;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitriceManager;
import globaz.lynx.utils.LXConstants;
import globaz.lynx.utils.LXImpressionsUtils;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class LXImpressionBalanceAgeeProcess extends LXImpressionProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void addRowContentColumns(HSSFWorkbook wb, HSSFSheet sheet, int countLine, String estBloque,
            String referenceExterne, String libelle, String value1, String value2, String value3, String value4,
            String value5, String value6, String value7, String value8, boolean alterneCouleur) {
        HSSFRow row = sheet.createRow((short) countLine);

        HSSFCellStyle styleColumnAlignRight = wb.createCellStyle();
        styleColumnAlignRight.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        styleColumnAlignRight.setDataFormat((short) 4);
        if (alterneCouleur && LXConstants.USE_COLOR) {
            styleColumnAlignRight.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumnAlignRight.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
        }

        HSSFCellStyle styleColumnAlignLeft = wb.createCellStyle();
        styleColumnAlignLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        if (alterneCouleur && LXConstants.USE_COLOR) {
            styleColumnAlignLeft.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumnAlignLeft.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
        }

        HSSFCellStyle styleColumnAlignCenter = wb.createCellStyle();
        styleColumnAlignCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        if (alterneCouleur && LXConstants.USE_COLOR) {
            styleColumnAlignCenter.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumnAlignCenter.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
        }

        int i = 0;

        HSSFCell cell = row.createCell((short) i++);
        cell.setCellValue(referenceExterne);
        cell.setCellStyle(styleColumnAlignCenter);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelle);
        cell.setCellStyle(styleColumnAlignLeft);

        cell = row.createCell((short) i++);
        cell.setCellValue(value1);
        cell.setCellStyle(styleColumnAlignCenter);

        cell = row.createCell((short) i++);
        cell.setCellValue(value2);
        cell.setCellStyle(styleColumnAlignCenter);

        cell = row.createCell((short) i++);
        if (JadeStringUtil.isBlankOrZero(value3)) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(JadeStringUtil.parseDouble(JANumberFormatter.deQuote(value3), 0));
        }
        cell.setCellStyle(styleColumnAlignRight);

        cell = row.createCell((short) i++);
        if (JadeStringUtil.isBlankOrZero(value4)) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(JadeStringUtil.parseDouble(JANumberFormatter.deQuote(value4), 0));
        }
        cell.setCellStyle(styleColumnAlignRight);

        cell = row.createCell((short) i++);
        if (JadeStringUtil.isBlankOrZero(value5)) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(JadeStringUtil.parseDouble(JANumberFormatter.deQuote(value5), 0));
        }
        cell.setCellStyle(styleColumnAlignRight);

        cell = row.createCell((short) i++);
        if (JadeStringUtil.isBlankOrZero(value6)) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(JadeStringUtil.parseDouble(JANumberFormatter.deQuote(value6), 0));
        }
        cell.setCellStyle(styleColumnAlignRight);

        cell = row.createCell((short) i++);
        if (JadeStringUtil.isBlankOrZero(value7)) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(JadeStringUtil.parseDouble(JANumberFormatter.deQuote(value7), 0));
        }
        cell.setCellStyle(styleColumnAlignRight);

        cell = row.createCell((short) i++);
        if (JadeStringUtil.isBlankOrZero(value8)) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(JadeStringUtil.parseDouble(JANumberFormatter.deQuote(value8), 0));
        }
        cell.setCellStyle(styleColumnAlignRight);
    }

    public static void addRowContentFooterLibelleColumns(HSSFWorkbook wb, HSSFSheet sheet, int countLine,
            String libelle, String libelleCol1, String libelleCol2, String libelleCol3, String libelleCol4,
            String libelleCol5, String libelleCol6, String libelleCol7, String libelleCol8, String libelleCol9,
            short colorBackground, short colorFont) {
        HSSFRow row = sheet.createRow((short) countLine);

        HSSFFont font = wb.createFont();
        font.setColor(colorFont);

        HSSFCellStyle styleColumnLeft = wb.createCellStyle();
        styleColumnLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

        if (LXConstants.USE_COLOR) {
            styleColumnLeft.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumnLeft.setFillForegroundColor(colorBackground);
            styleColumnLeft.setFont(font);
        }

        HSSFCellStyle styleColumn = wb.createCellStyle();
        styleColumn.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumn.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumn.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleColumn.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleColumn.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        if (LXConstants.USE_COLOR) {
            styleColumn.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumn.setFillForegroundColor(colorBackground);
            styleColumn.setFont(font);
        }

        HSSFCellStyle styleColumnRight = wb.createCellStyle();
        styleColumnRight.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnRight.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleColumnRight.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleColumnRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        styleColumnRight.setDataFormat((short) 4);

        if (LXConstants.USE_COLOR) {
            styleColumnRight.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumnRight.setFillForegroundColor(colorBackground);
            styleColumnRight.setFont(font);
        }

        int i = 0;

        HSSFCell cell = row.createCell((short) i++);
        cell.setCellValue(libelle);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol1);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol2);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol3);
        cell.setCellStyle(styleColumnLeft);

        cell = row.createCell((short) i++);
        cell.setCellValue(JadeStringUtil.parseDouble(JANumberFormatter.deQuote(libelleCol4), 0));
        cell.setCellStyle(styleColumnRight);

        cell = row.createCell((short) i++);
        cell.setCellValue(JadeStringUtil.parseDouble(JANumberFormatter.deQuote(libelleCol5), 0));
        cell.setCellStyle(styleColumnRight);

        cell = row.createCell((short) i++);
        cell.setCellValue(JadeStringUtil.parseDouble(JANumberFormatter.deQuote(libelleCol6), 0));
        cell.setCellStyle(styleColumnRight);

        cell = row.createCell((short) i++);
        cell.setCellValue(JadeStringUtil.parseDouble(JANumberFormatter.deQuote(libelleCol7), 0));
        cell.setCellStyle(styleColumnRight);

        cell = row.createCell((short) i++);
        cell.setCellValue(JadeStringUtil.parseDouble(JANumberFormatter.deQuote(libelleCol8), 0));
        cell.setCellStyle(styleColumnRight);

        cell = row.createCell((short) i++);
        cell.setCellValue(JadeStringUtil.parseDouble(JANumberFormatter.deQuote(libelleCol9), 0));
        cell.setCellStyle(styleColumnRight);

    }

    public static void addRowContentLibelleColumns(HSSFWorkbook wb, HSSFSheet sheet, int countLine, String libelle,
            String libelleCol1, String libelleCol2, String libelleCol3, String libelleCol4, String libelleCol5,
            String libelleCol6, String libelleCol7, String libelleCol8, String libelleCol9) {
        HSSFRow row = sheet.createRow((short) countLine);

        HSSFFont font = wb.createFont();
        font.setColor(new HSSFColor.WHITE().getIndex());

        HSSFCellStyle styleColumnLeft = wb.createCellStyle();
        styleColumnLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);

        if (LXConstants.USE_COLOR) {
            styleColumnLeft.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumnLeft.setFillForegroundColor(new HSSFColor.LIGHT_BLUE().getIndex());
            styleColumnLeft.setFont(font);
        }

        HSSFCellStyle styleColumn = wb.createCellStyle();
        styleColumn.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumn.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumn.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleColumn.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleColumn.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        if (LXConstants.USE_COLOR) {
            styleColumn.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumn.setFillForegroundColor(new HSSFColor.LIGHT_BLUE().getIndex());
            styleColumn.setFont(font);
        }

        HSSFCellStyle styleColumnRight = wb.createCellStyle();
        styleColumnRight.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnRight.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleColumnRight.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleColumnRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

        if (LXConstants.USE_COLOR) {
            styleColumnRight.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumnRight.setFillForegroundColor(new HSSFColor.LIGHT_BLUE().getIndex());
            styleColumnRight.setFont(font);
        }

        int i = 0;

        HSSFCell cell = row.createCell((short) i++);
        cell.setCellValue(libelle);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol1);
        cell.setCellStyle(styleColumnLeft);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol2);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol3);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol4);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol5);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol6);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol7);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol8);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol9);
        cell.setCellStyle(styleColumn);

    }

    private int countLine = 0;
    private String dateValeurPaiement;

    private String echeanceDeux;
    private String echeanceQuatre;
    private String echeanceTrois;

    private String echeanceUne;

    private String libelle6 = "";
    private String libelle7 = "";
    private String libelle8 = "";
    private String libelle9 = "";

    private ArrayList listEscomptePaiement = new ArrayList();

    private ArrayList listEtat = new ArrayList();

    private ArrayList listNdcLiee = new ArrayList();

    /**
     * @see globaz.lynx.process.LXImpressionProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {

        initialise();
        HSSFWorkbook wb = new HSSFWorkbook();

        if (!JadeStringUtil.isEmpty(getIdSociete())) {
            createSheetForBalanceAgee(wb, getSociete());
        } else {

            // Recherche de toutes les sociétées
            LXSocieteDebitriceManager manager = new LXSocieteDebitriceManager();
            manager.setSession(getSession());
            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            for (int i = 0; i < manager.size(); i++) {
                LXSocieteDebitrice societeDeb = (LXSocieteDebitrice) manager.getEntity(i);
                createSheetForBalanceAgee(wb, societeDeb);
            }

        }

        String outputFileName = Jade.getInstance().getHomeDir() + LXConstants.OUTPUT_FILE_WORK_DIR + "/"
                + LXConstants.OUTPUT_FILE_NAME_BALANCE_AGEE + "_" + LXConstants.NUMERO_REFERENCE_INFOROM_BALANCE_AGEE
                + ".xls";
        CGImprimerConsolidationUtils.createFile(wb, outputFileName);
        this.registerAttachedDocument(outputFileName);

        return false;
    }

    /**
     * @see BProcess#_validate() throws Exception
     */
    @Override
    protected void _validate() throws Exception {

        if (JadeStringUtil.isBlank(getDateValeurPaiement())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_IMP_DATE_VALEUR_PAIEMENT"));
            return;
        }

        if (JadeStringUtil.isIntegerEmpty(getEcheanceDeux()) || !JadeStringUtil.isDigit(getEcheanceDeux())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_IMP_ECHEANCE"));
            return;
        }

        if (JadeStringUtil.isIntegerEmpty(getEcheanceTrois()) || !JadeStringUtil.isDigit(getEcheanceTrois())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_IMP_ECHEANCE"));
            return;
        }

        if (JadeStringUtil.isIntegerEmpty(getEcheanceQuatre()) || !JadeStringUtil.isDigit(getEcheanceQuatre())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_IMP_ECHEANCE"));
            return;
        }

        if ((Integer.parseInt(getEcheanceUne()) > Integer.parseInt(getEcheanceDeux()))
                || (Integer.parseInt(getEcheanceDeux()) > Integer.parseInt(getEcheanceTrois()))
                || (Integer.parseInt(getEcheanceTrois()) > Integer.parseInt(getEcheanceQuatre()))) {
            this._addError(getTransaction(), getSession().getLabel("VAL_IMP_ECHEANCE_CROISSANT"));
            return;
        }

    }

    private void addContentToBalanceAgee(HSSFWorkbook wb, HSSFSheet sheet, LXSocieteDebitrice societeDeb)
            throws Exception {

        String idFournisseur = "";
        boolean couleurLigne = false;
        JADate dateEcheanceDeux = null;
        JADate dateEcheanceTrois = null;
        JADate dateEcheanceQuatre = null;

        FWCurrency borne0 = new FWCurrency();
        FWCurrency borne1 = new FWCurrency();
        FWCurrency borne2 = new FWCurrency();
        FWCurrency borne3 = new FWCurrency();
        FWCurrency borne4 = new FWCurrency();
        FWCurrency borneTotal = new FWCurrency();

        FWCurrency borne0Total = new FWCurrency();
        FWCurrency borne1Total = new FWCurrency();
        FWCurrency borne2Total = new FWCurrency();
        FWCurrency borne3Total = new FWCurrency();
        FWCurrency borne4Total = new FWCurrency();
        FWCurrency borneTotalTotal = new FWCurrency();

        LXImpressionBalanceAgeeManager manager = new LXImpressionBalanceAgeeManager();
        manager.setSession(getSession());
        manager.setForIdSociete(societeDeb.getIdSociete());
        manager.setForFournisseurNameBorneInf(getFournisseurBorneInf());
        manager.setForFournisseurNameBorneSup(getFournisseurBorneSup());
        manager.setForFournisseurIdBorneInf(getFournisseurIdBorneInf());
        manager.setForFournisseurIdBorneSup(getFournisseurIdBorneSup());

        manager.setForCsEtatIn(listEtat);

        manager.find(getTransaction());

        for (int i = 0; i < manager.size(); i++) {
            LXImpressionBalanceAgee imp = (LXImpressionBalanceAgee) manager.getEntity(i);

            // Si comptabilisé, on recherche paiement, escompte et note de
            // crédit lié pour soustraire au montant
            if (!LXOperation.CS_ETAT_OUVERT.equals(imp.getCsEtat())) {
                imp.setMontant(rechercheOperationSrcOuLie(imp.getMontant(), imp.getIdOperation(), imp.getIdSection()));
            }

            if (!JadeStringUtil.isIntegerEmpty(imp.getMontant())) {
                if (!idFournisseur.equals(imp.getIdFournisseur())) {

                    if (!JadeStringUtil.isEmpty(idFournisseur)) {
                        LXImpressionBalanceAgeeProcess.addRowContentFooterLibelleColumns(wb, sheet, countLine++, "",
                                "", "", getSession().getLabel("IMP_TOTAL") + " :", borne0.toStringFormat(),
                                borne1.toStringFormat(), borne2.toStringFormat(), borne3.toStringFormat(),
                                borne4.toStringFormat(), borneTotal.toStringFormat(),
                                new HSSFColor.LIGHT_BLUE().getIndex(), new HSSFColor.WHITE().getIndex());

                        borne0Total.add(borne0);
                        borne1Total.add(borne1);
                        borne2Total.add(borne2);
                        borne3Total.add(borne3);
                        borne4Total.add(borne4);
                        borneTotalTotal.add(borneTotal);

                        borne0 = new FWCurrency();
                        borne1 = new FWCurrency();
                        borne2 = new FWCurrency();
                        borne3 = new FWCurrency();
                        borne4 = new FWCurrency();
                        borneTotal = new FWCurrency();
                    } else {
                        idFournisseur = imp.getIdFournisseur();
                    }

                    idFournisseur = imp.getIdFournisseur();
                    couleurLigne = false;
                    // Création lignes vides
                    LXImpressionsUtils.addRowVoid(sheet, countLine++);
                    LXImpressionsUtils.addRowVoid(sheet, countLine++);

                    LXImpressionBalanceAgeeProcess.addRowContentLibelleColumns(wb, sheet, countLine++, imp
                            .getIdExterneFournisseur(), imp.getNomCompletFournisseur(),
                            getSession().getLabel("IMP_DATE"), getSession().getLabel("IMP_ECHEANCE"), " < "
                                    + getDateValeurPaiement(), libelle6, libelle7, libelle8, libelle9, getSession()
                                    .getLabel("IMP_TOTAL"));

                }

                JACalendarGregorian date = new JACalendarGregorian();
                dateEcheanceDeux = date.addDays(new JADate(getDateValeurPaiement()),
                        Integer.parseInt(getEcheanceDeux()));
                dateEcheanceTrois = date.addDays(new JADate(getDateValeurPaiement()),
                        Integer.parseInt(getEcheanceTrois()));
                dateEcheanceQuatre = date.addDays(new JADate(getDateValeurPaiement()),
                        Integer.parseInt(getEcheanceQuatre()));

                String estBloque = imp.getEstBloque().booleanValue() ? "X" : "";

                borneTotal.add(imp.getMontant());
                if (JadeDateUtil.isDateBefore(imp.getDateEcheance(), getDateValeurPaiement())) {

                    borne0.add(imp.getMontant());
                    LXImpressionBalanceAgeeProcess.addRowContentColumns(wb, sheet, countLine++, estBloque,
                            imp.getReferenceExterne(), imp.getLibelle(), imp.getDateOperation(), imp.getDateEcheance(),
                            imp.getMontant(), "", "", "", "", borneTotal.toString(), couleurLigne);
                } else if (JadeDateUtil.isDateBefore(imp.getDateEcheance(), dateEcheanceDeux.toStr("."))) {
                    borne1.add(imp.getMontant());
                    LXImpressionBalanceAgeeProcess.addRowContentColumns(wb, sheet, countLine++, estBloque,
                            imp.getReferenceExterne(), imp.getLibelle(), imp.getDateOperation(), imp.getDateEcheance(),
                            "", imp.getMontant(), "", "", "", borneTotal.toString(), couleurLigne);
                } else if (JadeDateUtil.isDateBefore(imp.getDateEcheance(), dateEcheanceTrois.toStr("."))) {
                    borne2.add(imp.getMontant());
                    LXImpressionBalanceAgeeProcess.addRowContentColumns(wb, sheet, countLine++, estBloque,
                            imp.getReferenceExterne(), imp.getLibelle(), imp.getDateOperation(), imp.getDateEcheance(),
                            "", "", imp.getMontant(), "", "", borneTotal.toString(), couleurLigne);
                } else if (JadeDateUtil.isDateBefore(imp.getDateEcheance(), dateEcheanceQuatre.toStr("."))) {
                    borne3.add(imp.getMontant());
                    LXImpressionBalanceAgeeProcess.addRowContentColumns(wb, sheet, countLine++, estBloque,
                            imp.getReferenceExterne(), imp.getLibelle(), imp.getDateOperation(), imp.getDateEcheance(),
                            "", "", "", imp.getMontant(), "", borneTotal.toString(), couleurLigne);
                } else {
                    borne4.add(imp.getMontant());
                    LXImpressionBalanceAgeeProcess.addRowContentColumns(wb, sheet, countLine++, estBloque,
                            imp.getReferenceExterne(), imp.getLibelle(), imp.getDateOperation(), imp.getDateEcheance(),
                            "", "", "", "", imp.getMontant(), borneTotal.toString(), couleurLigne);
                }

                couleurLigne = couleurLigne ? false : true;
            }
        }

        // On termine le dernier tableu
        if ((manager.size() > 0) && !JadeStringUtil.isBlank(idFournisseur)) {
            LXImpressionBalanceAgeeProcess.addRowContentFooterLibelleColumns(wb, sheet, countLine++, "", "", "",
                    getSession().getLabel("IMP_TOTAL") + " :", borne0.toString(), borne1.toString(), borne2.toString(),
                    borne3.toString(), borne4.toString(), borneTotal.toString(), new HSSFColor.LIGHT_BLUE().getIndex(),
                    new HSSFColor.WHITE().getIndex());

            LXImpressionsUtils.addRowVoid(sheet, countLine++);
            LXImpressionsUtils.addRowVoid(sheet, countLine++);

            // Affichage des totaux
            LXImpressionBalanceAgeeProcess.addRowContentLibelleColumns(wb, sheet, countLine++, "", "", "", "", " < "
                    + getDateValeurPaiement(), libelle6, libelle7, libelle8, libelle9,
                    getSession().getLabel("IMP_TOTAL"));
            borne0Total.add(borne0);
            borne1Total.add(borne1);
            borne2Total.add(borne2);
            borne3Total.add(borne3);
            borne4Total.add(borne4);
            borneTotalTotal.add(borneTotal);
            LXImpressionBalanceAgeeProcess.addRowContentFooterLibelleColumns(wb, sheet, countLine++, "", "", "",
                    getSession().getLabel("IMP_TOTAL") + " :", borne0Total.toString(), borne1Total.toString(),
                    borne2Total.toString(), borne3Total.toString(), borne4Total.toString(), borneTotalTotal.toString(),
                    new HSSFColor.BLACK().getIndex(), new HSSFColor.WHITE().getIndex());
        }

    }

    private void addRecapitulatifRecherche(HSSFWorkbook wb, HSSFSheet sheet, LXSocieteDebitrice societeDeb) {
        // ------------------------------------
        // Infos générales
        // ------------------------------------
        LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_SOCIETEDEBITRICE")
                + " : ", societeDeb.getIdExterne() + " - " + societeDeb.getNom());

        if (!JadeStringUtil.isEmpty(getCsCategorie())) {
            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_CATEGORIE")
                    + " : ", getSession().getCodeLibelle(getCsCategorie()));
        }
        if (!JadeStringUtil.isEmpty(getFournisseurBorneInf())) {
            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_FOUR_BORNE_INF")
                    + " : ", getFournisseurBorneInf());
        }
        if (!JadeStringUtil.isEmpty(getFournisseurBorneSup())) {
            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_FOUR_BORNE_SUP")
                    + " : ", getFournisseurBorneSup());
        }
        if (!JadeStringUtil.isEmpty(getFournisseurIdBorneInf())) {
            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++,
                    getSession().getLabel("IMP_FOUR_ID_BORNE_INF") + " : ", getFournisseurIdBorneInf());
        }
        if (!JadeStringUtil.isEmpty(getFournisseurIdBorneSup())) {
            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++,
                    getSession().getLabel("IMP_FOUR_ID_BORNE_SUP") + " : ", getFournisseurIdBorneSup());
        }
        LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_ETAT") + " : ",
                getSession().getLabel("IMP_" + getEtat()));
        // ------------------------------------
    }

    /**
     * Créer la feuille excel contenant la balance.
     * 
     * @param wb
     * @throws Exception
     */
    private void createSheetForBalanceAgee(HSSFWorkbook wb, LXSocieteDebitrice societeDeb) throws Exception {
        countLine = 0;
        String libelleSheet = getSession().getLabel("IMP_BALANCE_AGEE") + " - " + societeDeb.getIdExterne() + " - "
                + societeDeb.getNom();
        HSSFSheet sheet = wb.createSheet(libelleSheet.length() > 31 ? libelleSheet.substring(0, 30) : libelleSheet);
        LXImpressionsUtils.initPage(sheet, true);

        LXImpressionsUtils.addHeaderFooter(
                sheet,
                getSession(),
                "",
                getSession().getLabel("IMP_BALANCE_AGEE"),
                "",
                LXConstants.NUMERO_REFERENCE_INFOROM_BALANCE_AGEE + " - LXImpressionBalanceAgeeProcess - "
                        + JACalendar.todayJJsMMsAAAA() + " - " + getSession().getUserFullName(), "");

        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_LIBELLE, 10);
        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_SOLDE_B,
                LXConstants.INDEX_COLUMN_SOLDE_K, "1'000'000'000");
        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_SOLDE_A, 20);

        addRecapitulatifRecherche(wb, sheet, societeDeb);
        addContentToBalanceAgee(wb, sheet, societeDeb);
    }

    public String getDateValeurPaiement() {
        return dateValeurPaiement;
    }

    public String getEcheanceDeux() {
        return echeanceDeux;
    }

    public String getEcheanceQuatre() {
        return echeanceQuatre;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getEcheanceTrois() {
        return echeanceTrois;
    }

    public String getEcheanceUne() {
        return echeanceUne;
    }

    /**
     * Renvois le sujet de l'email.
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
            return getSession().getLabel("IMP_GENERATION_BALANCE_AGEE_ERROR");
        } else {
            return getSession().getLabel("IMP_GENERATION_BALANCE_AGEE_OK");
        }
    }

    /**
     * Initialisation de certaines variables
     */
    private void initialise() {

        // Création de la liste des etats
        listEtat = new ArrayList();
        listEtat.add(LXOperation.CS_ETAT_COMPTABILISE);
        listEtat.add(LXOperation.CS_ETAT_PREPARE);

        if (!JadeStringUtil.isEmpty(getEtat()) && LXConstants.ETAT_PROVISOIRE.equals(getEtat())) {
            listEtat.add(LXOperation.CS_ETAT_OUVERT);
            listEtat.add(LXOperation.CS_ETAT_TRAITEMENT);
        }

        // Creation de la liste des types escompte + paiement
        listEscomptePaiement = new ArrayList();
        listEscomptePaiement.add(LXOperation.CS_TYPE_ESCOMPTE);
        listEscomptePaiement.add(LXOperation.CS_TYPE_PAIEMENT_BVR_ORANGE);
        listEscomptePaiement.add(LXOperation.CS_TYPE_PAIEMENT_BVR_ROUGE);
        listEscomptePaiement.add(LXOperation.CS_TYPE_PAIEMENT_CAISSE);
        listEscomptePaiement.add(LXOperation.CS_TYPE_PAIEMENT_LSV);
        listEscomptePaiement.add(LXOperation.CS_TYPE_PAIEMENT_VIREMENT);

        // Création de la liste des type ndc liées
        listNdcLiee = new ArrayList();
        listNdcLiee.add(LXOperation.CS_TYPE_NOTEDECREDIT_LIEE);

        // Création des libellés
        libelle6 = " 0 - " + getEcheanceDeux() + " " + getSession().getLabel("IMP_JOURS");
        libelle7 = (Integer.parseInt(getEcheanceDeux()) + 1) + " - " + getEcheanceTrois() + " "
                + getSession().getLabel("IMP_JOURS");
        libelle8 = (Integer.parseInt(getEcheanceTrois()) + 1) + " - " + getEcheanceQuatre() + " "
                + getSession().getLabel("IMP_JOURS");
        libelle9 = "> " + getEcheanceQuatre() + " " + getSession().getLabel("IMP_JOURS");

    }

    /**
     * Réduit le montant de la facture en cherchant les notes de crédit liées, paiement et escompte.
     * 
     * @param montant
     * @param idOperation
     * @param idSection
     * @return
     * @throws Exception
     */
    protected String rechercheOperationSrcOuLie(String montant, String idOperation, String idSection) throws Exception {

        FWCurrency curMontant = new FWCurrency();
        curMontant.add(montant);

        // Recherche des paiement-escompte pour cette facture
        LXOperationManager opeManager = new LXOperationManager();
        opeManager.setSession(getSession());
        opeManager.setForIdOperationSrc(idOperation);
        opeManager.setForIdSection(idSection);
        opeManager.setForCsEtatIn(listEtat);
        opeManager.setForIdTypeOperationIn(listEscomptePaiement);
        opeManager.find(getTransaction());

        for (int j = 0; j < opeManager.size(); j++) {
            LXOperation ope = (LXOperation) opeManager.getEntity(j);

            curMontant.add(ope.getMontant());

        }

        // Recherche des ndc liée pour cette facture
        opeManager = new LXOperationManager();
        opeManager.setSession(getSession());
        opeManager.setForIdOperationLiee(idOperation);
        opeManager.setForIdSection(idSection);
        opeManager.setForCsEtatIn(listEtat);
        opeManager.setForIdTypeOperationIn(listNdcLiee);
        opeManager.find(getTransaction());

        for (int j = 0; j < opeManager.size(); j++) {
            LXOperation ope = (LXOperation) opeManager.getEntity(j);

            curMontant.add(ope.getMontant());

        }

        return curMontant.toString();
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setDateValeurPaiement(String dateValeurPaiement) {
        this.dateValeurPaiement = dateValeurPaiement;
    }

    public void setEcheanceDeux(String echeanceDeux) {
        this.echeanceDeux = echeanceDeux;
    }

    public void setEcheanceQuatre(String echeanceQuatre) {
        this.echeanceQuatre = echeanceQuatre;
    }

    public void setEcheanceTrois(String echeanceTrois) {
        this.echeanceTrois = echeanceTrois;
    }

    public void setEcheanceUne(String echeanceUne) {
        this.echeanceUne = echeanceUne;
    }

}
