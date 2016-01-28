package globaz.helios.process.consolidation.print;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.helios.application.CGApplication;
import globaz.helios.db.comptes.CGBilanListViewBean;
import globaz.helios.db.comptes.CGExerciceComptableViewBean;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPeriodeComptableManager;
import globaz.helios.db.comptes.CGPlanComptableManager;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.helios.db.comptes.CGSolde;
import globaz.helios.db.comptes.CGSoldeManager;
import globaz.helios.process.consolidation.utils.CGImprimerConsolidationUtils;
import globaz.helios.process.consolidation.utils.CGProcessConsolidationUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CGProcessImprimerConsolidationParMois extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String NUMERO_REFERENCE_INFOROM = "0058GCF";

    private static final String OUTPUT_FILE_NAME = "consolidation_mois";
    private static final String OUTPUT_FILE_WORK_DIR = "work";
    private String idExerciceComptable;

    /**
     * Constructor for CGProcessImprimerConsolidationParMois.
     */
    public CGProcessImprimerConsolidationParMois() throws Exception {
        this(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }

    /**
     * Constructor for CGProcessImprimerConsolidationParMois.
     * 
     * @param parent
     */
    public CGProcessImprimerConsolidationParMois(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CGProcessImprimerConsolidationParMois.
     * 
     * @param session
     */
    public CGProcessImprimerConsolidationParMois(BSession session) throws Exception {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
        // Do nothing.
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        CGExerciceComptableViewBean exerciceComptable = CGProcessConsolidationUtil.getExerciceComptable(getSession(),
                getTransaction(), getIdExerciceComptable());

        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet sheet = wb.createSheet(JadeStringUtil.substring(exerciceComptable.getMandat().getLibelle(), 0, 30)
                .trim());

        HSSFPrintSetup psa = sheet.getPrintSetup();
        psa.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);

        CGImprimerConsolidationUtils.addHeaderFooter(getSession(), sheet, exerciceComptable,
                "CONSOLIDATION_COMPTE_PAR_MOIS_POUR", CGProcessImprimerConsolidationParMois.NUMERO_REFERENCE_INFOROM);

        CGImprimerConsolidationUtils.addParamsInfos(getSession(), wb, sheet, exerciceComptable);

        ArrayList<String> periodeList = addAndGetPeriodeList(wb, sheet);

        addContent(wb, sheet, periodeList);

        String outputFileName = Jade.getInstance().getHomeDir()
                + CGProcessImprimerConsolidationParMois.OUTPUT_FILE_WORK_DIR + "/"
                + CGProcessImprimerConsolidationParMois.OUTPUT_FILE_NAME + System.currentTimeMillis() + ".xls";
        CGImprimerConsolidationUtils.createFile(wb, outputFileName);

        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(CGProcessImprimerConsolidationParMois.NUMERO_REFERENCE_INFOROM);

        this.registerAttachedDocument(docInfo, outputFileName);

        return true;
    }

    /**
     * Ajout des titre des périodes.
     * 
     * @param wb
     * @param sheet
     * @return Les périodes trouvées dans le système pour l'exercice comptable en cours.
     * @throws Exception
     */
    private ArrayList<String> addAndGetPeriodeList(HSSFWorkbook wb, HSSFSheet sheet) throws Exception {
        ArrayList periodeList = new ArrayList();

        HSSFCellStyle styleColumn = wb.createCellStyle();
        styleColumn.setBorderTop(HSSFCellStyle.BORDER_DASHED);
        styleColumn.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM_DASHED);
        styleColumn.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

        HSSFRow rowTitleColumn = sheet.createRow((short) 3);
        HSSFCell cellColumn = rowTitleColumn.createCell((short) 0);
        cellColumn.setCellValue("");
        cellColumn.setCellStyle(styleColumn);

        CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
        manager.setSession(getSession());

        manager.setForIdExerciceComptable(getIdExerciceComptable());
        manager.setOrderBy(CGPeriodeComptableManager.TRI_DATE_FIN_ASC_AND_TYPE_ASC);

        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        int i = 0;
        for (i = 0; i < manager.size(); i++) {
            CGPeriodeComptable periode = (CGPeriodeComptable) manager.get(i);

            if (!periode.getIdTypePeriode().equals(CGPeriodeComptable.CS_CODE_CLOTURE)) {
                periodeList.add(periode.getIdPeriodeComptable());

                cellColumn = rowTitleColumn.createCell((short) (i + 1));
                cellColumn.setCellValue(periode.getMonthLibelle());
                cellColumn.setCellStyle(styleColumn);
            }
        }
        periodeList.add(CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE);

        HSSFCellStyle styleColumnTotal = wb.createCellStyle();
        styleColumnTotal.setBorderTop(HSSFCellStyle.BORDER_DASHED);
        styleColumnTotal.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM_DASHED);
        styleColumnTotal.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        styleColumnTotal.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM_DASHED);

        cellColumn = rowTitleColumn.createCell((short) (i + 1));
        cellColumn.setCellValue(getSession().getLabel("PARSER_TOTAUX"));
        cellColumn.setCellStyle(styleColumnTotal);

        return periodeList;
    }

    /**
     * Ajout du contenu du tableau. Un compte doit contenir au minimum un solde de période pour être ajouté au tableau.
     * 
     * @param wb
     * @param sheet
     * @param periodeList
     * @throws Exception
     * @throws NumberFormatException
     */
    private void addContent(HSSFWorkbook wb, HSSFSheet sheet, ArrayList periodeList) throws Exception,
            NumberFormatException {
        CGPlanComptableManager planManager = new CGPlanComptableManager();
        planManager.setSession(getSession());

        planManager.setForIdExerciceComptable(getIdExerciceComptable());
        planManager.setOrderBy(CGBilanListViewBean.CG_BILAN_ORDER_BY);

        int countRow = 4;

        HSSFCellStyle cellContentStyle = wb.createCellStyle();
        cellContentStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));

        HSSFCellStyle styleColumnTotal = wb.createCellStyle();
        styleColumnTotal.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM_DASHED);
        styleColumnTotal.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));

        HSSFCellStyle styleControl = wb.createCellStyle();
        styleControl.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        HSSFFont font = wb.createFont();
        font.setColor(HSSFFont.COLOR_RED);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        styleControl.setFont(font);

        planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        CGImprimerConsolidationUtils.setContentColumnWidth(sheet, 1, planManager.size());

        for (int i = 0; i < planManager.size(); i++) {
            CGPlanComptableViewBean compte = (CGPlanComptableViewBean) planManager.get(i);

            HSSFRow row = null;

            int j = 0;
            for (j = 0; j < periodeList.size(); j++) {
                String idPeriode = (String) periodeList.get(j);

                CGSoldeManager soldeManager = new CGSoldeManager();
                soldeManager.setSession(getSession());

                soldeManager.setForIdPeriodeComptable(idPeriode);
                soldeManager.setForIdExerComptable(getIdExerciceComptable());

                soldeManager.setForEstPeriode(new Boolean(!(idPeriode
                        .equals(CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE))));
                soldeManager.setForIdCompte(compte.getIdCompte());

                soldeManager.find(getTransaction());

                if (!soldeManager.isEmpty()) {
                    CGSolde solde = (CGSolde) soldeManager.getFirstEntity();

                    if (row == null) {
                        row = sheet.createRow((short) countRow);

                        HSSFCell cell = row.createCell((short) 0);
                        cell.setCellValue(compte.getIdExterne());
                        countRow++;
                    }

                    HSSFCell cell = row.createCell((short) (j + 1));
                    cell.setCellValue(Double.parseDouble(solde.getSolde()));

                    if (idPeriode.equals(CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE)) {
                        cell.setCellStyle(styleColumnTotal);
                    } else {
                        cell.setCellStyle(cellContentStyle);
                    }
                }
            }

            if (row != null) {
                HSSFCell cell = row.createCell((short) (j + 1));
                char columnTotal = (char) (66 + (j - 1));
                char columnEnd = (char) (66 + (j - 2));
                cell.setCellFormula("IF(" + columnTotal + countRow + "=SUM(B" + countRow + ":" + columnEnd + countRow
                        + "),\"\",\"" + getSession().getLabel("DIFFERENCE") + " !\")");
                cell.setCellStyle(styleControl);
            }
        }
    }

    /**
     * Renvois le sujet de l'email.
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
            return getSession().getLabel("CONSOLIDATION_IMPR_PAR_MOIS_ERROR");
        } else {
            return getSession().getLabel("CONSOLIDATION_IMPR_PAR_MOIS_OK");
        }
    }

    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setIdExerciceComptable(String idExerciceComptable) {
        this.idExerciceComptable = idExerciceComptable;
    }

}
