package ch.globaz.pegasus.excel;

import ch.globaz.pegasus.excel.model.PCListeComparaisonCommuneModel;
import ch.globaz.pegasus.excel.model.PCListeFratrie2LoyerModel;
import ch.globaz.pegasus.utils.PCAbstractListExcel;
import globaz.globall.db.BSession;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractModel;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PCListeComparaisonCommuneExcel  extends PCAbstractListExcel {
    private List<PCListeComparaisonCommuneModel> listSheetNormal = new ArrayList<>();
    private List<PCListeComparaisonCommuneModel> listSheetCourrier = new ArrayList<>();
    private List<PCListeComparaisonCommuneModel> listSheetNotFound = new ArrayList<>();
    private List<PCListeFratrie2LoyerModel> listeFratrie2Loyer = new ArrayList<>();
    private HSSFCellStyle styleHeader;
    private static String SHEET_LABEL_1 = "DIFF";
    private static String SHEET_LABEL_2 = "COURRIER";
    private static String SHEET_LABEL_3 = "EMPTY";
    private static String SHEET_LABEL_4 = "FRATRIE2LOYERS";
    private static final String HEADER_COL_1 = "NSS";
    private static final String HEADER_COL_2 = "Nom";
    private static final String HEADER_COL_3 = "NSS Requérant";
    private static final String HEADER_COL_4 = "Nom Requérant";
    private static final String HEADER_COL_5 = "Localité PC";
    private static final String HEADER_COL_6 = "Localité Tiers";
    private static final String HEADER_COL_7 = "Est Requérant";
    private static final String HEADER_COL_8 = "Info";

    private static final String HEADER_NB_LOYERS = "Nbre loyers";
    public PCListeComparaisonCommuneExcel(BSession session) {
        super(session, "Liste de contrôle communales", session.getLabel(""));
        JadeThreadContext threadContext = this.initThreadContext(session);

        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            this.styleHeader = this.getWorkbook().createCellStyle();
            this.styleHeader.setAlignment((short)1);
            this.styleHeader.setFont(this.getFontBold());
        } catch (SQLException var7) {
            JadeLogger.error(PCListeComparaisonCommuneExcel.class, var7.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }
    public void creerDocument() {
        this.createControle();
    }

    private void createControle() {
        this.createSheet(SHEET_LABEL_1);
        this.initPage(true);
        this.initColumnWidthSheetListePrestation();
        this.createHeaderRows();
        if (!this.listSheetNormal.isEmpty()) {
            this.createDataRows(this.listSheetNormal);
        }
        for(int i = 0; i < 8; ++i) {
            this.currentSheet.autoSizeColumn((short)i);
        }
        this.createSheet(SHEET_LABEL_2);
        this.initPage(true);
        this.initColumnWidthSheetListePrestation();
        this.createHeaderRows();
        if (!this.listSheetCourrier.isEmpty()) {
            this.createDataRows(this.listSheetCourrier);
        }
        for(int i = 0; i < 8; ++i) {
            this.currentSheet.autoSizeColumn((short)i);
        }
        this.createSheet(SHEET_LABEL_3);
        this.initPage(true);
        this.initColumnWidthSheetListePrestation();
        this.createHeaderRows();
        if (!this.listSheetNotFound.isEmpty()) {
            this.createDataRows(this.listSheetNotFound);
        }
        for(int i = 0; i < 8; ++i) {
            this.currentSheet.autoSizeColumn((short)i);
        }
        this.createSheet(SHEET_LABEL_4);
        this.initPage(true);
        this.initColumnWidthSheetListePrestation();
        this.createHeaderRowsFratrie2Loyer();
        if (!this.listeFratrie2Loyer.isEmpty()) {
            this.createDataRowsFratrie2Loyer(this.listeFratrie2Loyer);
        }
        for(int i = 0; i < 8; ++i) {
            this.currentSheet.autoSizeColumn((short)i);
        }
    }



    private void createDataRows(List<PCListeComparaisonCommuneModel> listSheet) {
        Iterator<PCListeComparaisonCommuneModel> it = listSheet.iterator();
        while(it.hasNext()) {
            PCListeComparaisonCommuneModel model =  it.next();
            this.createRow();
            this.createCell(model.getNss());
            this.createCell(model.getNom());
            this.createCell(model.getNssRequerant());
            this.createCell(model.getNomRequerant());
            this.createCell(model.getNomLocaliteFromPC());
            this.createCell(model.getNomLocaliteFromTiers());
            this.createCell(model.isRequerant());
            this.createCell(model.getRemarque());
        }
    }
    private void createDataRowsFratrie2Loyer(List<PCListeFratrie2LoyerModel> listeFratrie2Loyer) {
        Iterator<PCListeFratrie2LoyerModel> it = listeFratrie2Loyer.iterator();
        while(it.hasNext()) {
            PCListeFratrie2LoyerModel model = it.next();
            this.createRow();
            this.createCell(model.getNssRequerant());
            this.createCell(model.getNomRequerant());
            this.createCell(model.getNbLoyer());
        }
    }

    private void createHeaderRows() {
        this.createRow();
        this.createCell(HEADER_COL_1, this.styleHeader);
        this.createCell(HEADER_COL_2, this.styleHeader);
        this.createCell(HEADER_COL_3, this.styleHeader);
        this.createCell(HEADER_COL_4, this.styleHeader);
        this.createCell(HEADER_COL_5, this.styleHeader);
        this.createCell(HEADER_COL_6, this.styleHeader);
        this.createCell(HEADER_COL_7, this.styleHeader);
        this.createCell(HEADER_COL_8, this.styleHeader);
    }
    private void createHeaderRowsFratrie2Loyer() {
        this.createRow();
        this.createCell(HEADER_COL_3, this.styleHeader);
        this.createCell(HEADER_COL_4, this.styleHeader);
        this.createCell(HEADER_NB_LOYERS,this.styleHeader);
    }

    private void initColumnWidthSheetListePrestation() {
        short numCol = 0;
        this.currentSheet.setColumnWidth(numCol, (short)7000);
        this.currentSheet.setColumnWidth(numCol++, (short)7000);
        this.currentSheet.setColumnWidth(numCol++, (short)7000);
        this.currentSheet.setColumnWidth(numCol++, (short)7000);
        this.currentSheet.setColumnWidth(numCol++, (short)12000);
        this.currentSheet.setColumnWidth(numCol++, (short)7000);
    }

    public List<PCListeComparaisonCommuneModel> getListSheetNormal() {
        return listSheetNormal;
    }

    public void setListSheetNormal(List<PCListeComparaisonCommuneModel> listSheetNormal) {
        this.listSheetNormal = listSheetNormal;
    }

    public List<PCListeComparaisonCommuneModel> getListSheetCourrier() {
        return listSheetCourrier;
    }

    public void setListSheetCourrier(List<PCListeComparaisonCommuneModel> listSheetCourrier) {
        this.listSheetCourrier = listSheetCourrier;
    }

    public List<PCListeComparaisonCommuneModel> getListSheetNotFound() {
        return listSheetNotFound;
    }

    public void setListSheetNotFound(List<PCListeComparaisonCommuneModel> listSheetNotFound) {
        this.listSheetNotFound = listSheetNotFound;
    }
    public List<PCListeFratrie2LoyerModel> getListeFratrie2Loyer() {
        return listeFratrie2Loyer;
    }

    public void setListeFratrie2Loyer(List<PCListeFratrie2LoyerModel> listeFratrie2Loyer) {
        this.listeFratrie2Loyer = listeFratrie2Loyer;
    }

}
