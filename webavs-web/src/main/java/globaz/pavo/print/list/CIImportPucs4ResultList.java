package globaz.pavo.print.list;

import globaz.aquila.print.list.COAbstractListExcel;
import globaz.globall.db.BSession;
import globaz.pavo.db.inscriptions.declaration.CIImportPucs4DetailResultBean;
import globaz.pavo.db.inscriptions.declaration.CIImportPucs4DetailResultInscriptionBean;
import globaz.pavo.db.inscriptions.declaration.CIImportPucs4ResumeBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.Region;

public class CIImportPucs4ResultList extends COAbstractListExcel {

    private static final String NUMERO_INFOROM = "0319CCI";

    private CIImportPucs4DetailResultBean detailResultBean;
    private HSSFFont fontRed;
    private HSSFCellStyle styleRedAlignLeft;

    public CIImportPucs4ResultList(BSession session, CIImportPucs4DetailResultBean detailResultBean) {
        super(session, session.getLabel("IMPORT_PUCS_4_PROCESS_ROOT_FILE_NAME"), session
                .getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_TITLE"));
        this.detailResultBean = detailResultBean;
    }

    @Override
    public String getNumeroInforom() {
        return CIImportPucs4ResultList.NUMERO_INFOROM;
    }

    public void createResultList() {

        for (Entry<String, List<CIImportPucs4DetailResultInscriptionBean>> entryAnneeListInscriptions : detailResultBean
                .getMapAnneeListInscriptions().entrySet()) {

            String sheetName = getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_SHEET_NAME") + "_"
                    + detailResultBean.getNumeroAffilie() + "_" + entryAnneeListInscriptions.getKey();
            String sheetTitle = getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_SHEET_TITLE") + " : "
                    + detailResultBean.getNumeroAffilie() + " - " + entryAnneeListInscriptions.getKey();
            if (detailResultBean.isAFSeul()) {
                sheetTitle = sheetTitle + " (" + getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_AF_SEUL")
                        + ")";
            }
            createSheetDetail(sheetName, sheetTitle, entryAnneeListInscriptions.getValue());
        }

        for (Entry<String, CIImportPucs4ResumeBean> entryAnneeResume : detailResultBean.getMapAnneeResume().entrySet()) {
            String sheetName = getSession().getLabel("IMPORT_PUCS_4_RESUME_RESULT_LIST_SHEET_NAME") + "_"
                    + detailResultBean.getNumeroAffilie() + "_" + entryAnneeResume.getKey();
            String sheetTitle = getSession().getLabel("IMPORT_PUCS_4_RESUME_RESULT_LIST_SHEET_TITLE") + " : "
                    + detailResultBean.getNumeroAffilie() + " - " + entryAnneeResume.getKey();
            createSheetResume(sheetName, sheetTitle, entryAnneeResume.getValue());
        }

    }

    private void createSheetDetail(String sheetName, String sheetTitle,
            List<CIImportPucs4DetailResultInscriptionBean> listDetailResultInscriptionBean) {

        createSheet(sheetName);
        initPage(false);
        createHeader();
        createFooter(CIImportPucs4ResultList.NUMERO_INFOROM);

        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, (short) 5000);
        currentSheet.setColumnWidth((short) numCol++, (short) 14000);
        currentSheet.setColumnWidth((short) numCol++, (short) 5000);
        currentSheet.setColumnWidth((short) numCol++, (short) 5000);
        currentSheet.setColumnWidth((short) numCol++, (short) 5000);
        currentSheet.setColumnWidth((short) numCol++, (short) 5000);
        currentSheet.setColumnWidth((short) numCol++, (short) 5000);

        createRow();
        createCell(CIImportPucs4ResultList.NUMERO_INFOROM, getStyleListCenter());
        createRow();
        currentSheet.addMergedRegion(new Region(1, (short) 0, 1, (short) 1));
        createCell(sheetTitle, getStyleListTitleLeft());

        ArrayList<String> colTitles = new ArrayList<String>();
        colTitles.add(getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_NSS"));
        colTitles.add(getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_NOM") + " / "
                + getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_MESSAGE"));
        colTitles.add(getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_MM") + "-"
                + getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_MM"));
        colTitles.add(getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_ANNEE"));
        colTitles.add(getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_CC"));
        colTitles.add(getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_REVENU_AVS"));
        colTitles.add(getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_REVENU_CAF"));

        initTitleRow(colTitles);

        createContentRowsDetail(listDetailResultInscriptionBean);

    }

    private HSSFFont getFontRed() {

        if (fontRed == null) {
            fontRed = getWorkbook().createFont();
            fontRed.setColor(HSSFFont.COLOR_RED);
        }

        return fontRed;

    }

    private HSSFCellStyle getStyleRedAlignLeft() {

        if (styleRedAlignLeft == null) {

            styleRedAlignLeft = getWorkbook().createCellStyle();
            styleRedAlignLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            styleRedAlignLeft.setFont(getFontRed());
            styleRedAlignLeft.setWrapText(true); // La largeur de la cellule s'adapte au contenu
        }

        return styleRedAlignLeft;
    }

    private void createSheetResume(String sheetName, String sheetTitle, CIImportPucs4ResumeBean resumeBean) {

        createSheet(sheetName);
        initPage(false);
        createHeader();
        createFooter("TODO");

        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, (short) 14000);
        currentSheet.setColumnWidth((short) numCol++, (short) 5000);
        currentSheet.setColumnWidth((short) numCol++, (short) 5000);

        createRow();
        createRow();
        currentSheet.addMergedRegion(new Region(1, (short) 0, 1, (short) 1));
        createCell(sheetTitle, getStyleListTitleLeft());

        ArrayList<String> colTitles = new ArrayList<String>();
        colTitles.add(getSession().getLabel("IMPORT_PUCS_4_RESUME_RESULT_LIST_LIBELLE"));
        colTitles.add(getSession().getLabel("IMPORT_PUCS_4_RESUME_RESULT_LIST_NOMBRE"));
        colTitles.add(getSession().getLabel("IMPORT_PUCS_4_RESUME_RESULT_LIST_MONTANT"));

        initTitleRow(colTitles);

        createContentRowsResume(resumeBean);

    }

    private void createContentRowsDetail(List<CIImportPucs4DetailResultInscriptionBean> listDetailResultInscriptionBean) {

        for (CIImportPucs4DetailResultInscriptionBean aDetailResultInscriptionBean : listDetailResultInscriptionBean) {
            createRow();
            createCell(aDetailResultInscriptionBean.getNss());
            createCell(aDetailResultInscriptionBean.getNom());
            createCell(aDetailResultInscriptionBean.getMoisDebut() + "-" + aDetailResultInscriptionBean.getMoisFin());
            createCell(aDetailResultInscriptionBean.getAnnee());
            createCell(aDetailResultInscriptionBean.getGenre());
            createCell(aDetailResultInscriptionBean.getRevenuAVS());
            createCell(aDetailResultInscriptionBean.getRevenuCAF());

            for (String aErreur : aDetailResultInscriptionBean.getErrors()) {
                createRow();
                createCell(getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_ERREUR") + " : ",
                        getStyleRedAlignLeft());
                createCell(aErreur, getStyleRedAlignLeft());
            }

            for (String aInfo : aDetailResultInscriptionBean.getInfos()) {
                createRow();
                createCell(getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_AVERTISSEMENT") + " : ");
                createCell(aInfo);
            }
        }

    }

    private void createContentRowsResume(CIImportPucs4ResumeBean resumeBean) {

        createRow();
        createCell(getSession().getLabel("IMPORT_PUCS_4_RESUME_RESULT_LIST_INSCRIPTION_ERREUR"));
        createCell(String.valueOf(resumeBean.getNbrInscriptionsErreur()));
        createCell(String.valueOf(resumeBean.getMontantInscriptionsErreur()));

        createRow();
        createCell(getSession().getLabel("IMPORT_PUCS_4_RESUME_RESULT_LIST_INSCRIPTION_SUSPENS"));
        createCell(String.valueOf(resumeBean.getNbrInscriptionsSuspens()));
        createCell(String.valueOf(resumeBean.getMontantInscriptionsSuspens()));

        createRow();
        createCell(getSession().getLabel("IMPORT_PUCS_4_RESUME_RESULT_LIST_INSCRIPTION_CI"));
        createCell(String.valueOf(resumeBean.getNbrInscriptionsCI()));
        createCell(String.valueOf(resumeBean.getMontantInscriptionsCI()));

        createRow();
        createCell(getSession().getLabel("IMPORT_PUCS_4_RESUME_RESULT_LIST_INSCRIPTION_TRAITE"));
        createCell(String.valueOf(resumeBean.getNbrInscriptionsTraites()));
        createCell(String.valueOf(resumeBean.getMontantInscriptionsTraites()));

        createRow();
        createCell(getSession().getLabel("IMPORT_PUCS_4_RESUME_RESULT_LIST_INSCRIPTION_NEGATIVE"));
        createCell(String.valueOf(resumeBean.getNbrInscriptionsNegatives()));
        createCell(String.valueOf(resumeBean.getMontantInscriptionsNegatives()));

    }

}
