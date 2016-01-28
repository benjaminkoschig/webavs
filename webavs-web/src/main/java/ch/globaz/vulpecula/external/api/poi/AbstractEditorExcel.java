package ch.globaz.vulpecula.external.api.poi;

import globaz.globall.db.BApplication;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.Region;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import ch.globaz.vulpecula.util.CodeSystemUtil;
import ch.globaz.vulpecula.util.I18NUtil;

public abstract class AbstractEditorExcel {
    protected BSession session;

    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private HSSFCell cell;

    private int rowNum;

    private String outputPath;

    private static final String WORK_DIRECTORY = "work/";
    private static final String MODEL_DIRECTORY = "model/excel/";

    public int getRowNum() {
        return rowNum;
    }

    public int getColNum() {
        return cell.getCellNum();
    }

    public AbstractEditorExcel(BSession session) {
        this.session = session;
    }

    public void executeProcess() throws IOException {
        String sourceFilePath = getModelApplicationPath() + getSourceFilePath();

        InputStream inp = new FileInputStream(sourceFilePath);
        workbook = new HSSFWorkbook(inp);
        sheet = workbook.getSheetAt(0);
        edit();
        String fileName = getOutputFileName() + new java.util.Date().getTime() + ".xls";
        File outputFile = new File(getWorkApplicationPath() + "/" + fileName);
        outputPath = outputFile.getCanonicalPath();
        workbook.write(new FileOutputStream(outputFile));
        inp.close();
    }

    public String getRootApplicationPath() {
        return JadeStringUtil.change(Jade.getInstance().getHomeDir() + session.getApplicationId().toLowerCase()
                + "Root/", "\\", "/");
    }

    public String getWorkApplicationPath() {
        return getRootApplicationPath() + WORK_DIRECTORY;
    }

    public String getModelApplicationPath() {
        return getRootApplicationPath() + MODEL_DIRECTORY;
    }

    protected void nextRows(int number) {
        for (int i = 0; i < number; i++) {
            nextRow();
        }
    }

    protected void nextRow() {
        rowNum = rowNum + 1;
        cell = sheet.getRow(rowNum).getCell((short) (cell.getCellNum() - 1));
    }

    protected void moveToRow(int increment) {
        for (int i = 0; i < increment; i++) {
            moveToRow();
        }
    }

    protected void moveToRow() {
        rowNum = rowNum + 1;
        cell = sheet.getRow(rowNum).getCell((short) 0);
    }

    protected void createRow() {
        rowNum = rowNum + 1;
        cell = sheet.createRow(rowNum).getCell((short) 0);
    }

    protected void createRows(int number) {
        for (int i = 0; i < number; i++) {
            createRow();
        }
    }

    protected void createEmptyCells(int number) {
        for (int i = 0; i < number; i++) {
            createEmptyCell();
        }
    }

    protected void createEmptyCell() {
        createCell("");
    }

    protected void createCell(double value) {
        cell.setCellValue(value);
        nextCell();
    }

    protected void createCell(String value) {
        if (value == null) {
            createEmptyCell();
        } else {
            cell.setCellValue(value);
        }
        nextCell();
    }

    protected void firstCell() {
        cell = sheet.getRow(rowNum).getCell(((short) 0));
    }

    protected void nextCell() {
        cell = sheet.getRow(rowNum).getCell(((short) (cell.getCellNum() + 1)));
    }

    protected void nextCells(int number) {
        for (int i = 0; i < number; i++) {
            nextCell();
        }
    }

    protected void createCell(Montant montant) {
        createCell(montant.doubleValue());
    }

    public String getLabel(String label) {
        BApplication application;
        try {
            application = session.getApplication();
            String langue = I18NUtil.getLanguesOf(getCodeLangue()).getCodeIso();
            return application.getLabel(label, langue);
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE);
        }
    }

    public String getCodeLibelle(String cs) {
        return CodeSystemUtil.getCodeSysteme(cs, I18NUtil.getLanguesOf(getCodeLangue())).getLibelle();
    }

    public String getCode(String cs) {
        return CodeSystemUtil.getCodeSysteme(cs, I18NUtil.getLanguesOf(getCodeLangue())).getCode();
    }

    protected void setCell(String reference) {
        CellReference cr = new CellReference(reference);
        cell = sheet.getRow(cr.getRow()).getCell(cr.getCol());
        rowNum = cr.getRow();
    }

    protected void copyCurrentRow() {
        copyRow(rowNum, rowNum + 1);
    }

    protected void copyRow(int sourceRowNum, int destinationRowNum) {
        copyRow(workbook, sheet, (short) sourceRowNum, (short) destinationRowNum);
    }

    protected void createPNGImage(String path, HSSFClientAnchor anchor) {
        try {
            InputStream inp = new FileInputStream(getModelApplicationPath() + path);
            byte[] image = IOUtils.toByteArray(inp);
            int picture_id = workbook.addPicture(image, HSSFWorkbook.PICTURE_TYPE_PNG);
            HSSFPatriarch drawing = sheet.createDrawingPatriarch();
            drawing.createPicture(anchor, picture_id);
        } catch (Exception ex) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, ex);
        }
    }

    protected void copyRow(HSSFWorkbook workbook, HSSFSheet worksheet, short sourceRowNum, short destinationRowNum) {
        // Get the source / new row
        HSSFRow newRow = worksheet.getRow(destinationRowNum);
        HSSFRow sourceRow = worksheet.getRow(sourceRowNum);

        // If the row exist in destination, push down all rows by 1 else create a new row
        if (newRow != null) {
            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
        } else {
            newRow = worksheet.createRow(destinationRowNum);
        }

        // Loop through source columns to add to new row
        for (short i = 0; i <= sourceRow.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
            HSSFCell oldCell = sourceRow.getCell(i);
            HSSFCell newCell = newRow.createCell(i);

            // If the old cell is null jump to next cell
            if (oldCell == null) {
                newCell = null;
                continue;
            }

            // Copy style from old cell and apply to new cell
            newCell.setCellStyle(oldCell.getCellStyle());

            // If there is a cell comment, copy
            if (newCell.getCellComment() != null) {
                newCell.setCellComment(oldCell.getCellComment());
            }

            // Set the cell data type
            newCell.setCellType(oldCell.getCellType());

            // Set the cell data value
            switch (oldCell.getCellType()) {
                case HSSFCell.CELL_TYPE_BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case HSSFCell.CELL_TYPE_ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case HSSFCell.CELL_TYPE_FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case HSSFCell.CELL_TYPE_STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
            }
        }

        // If there are are any merged regions in the source row, copy to new row
        for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
            Region region = worksheet.getMergedRegionAt(i);
            if (region.getRowFrom() == sourceRow.getRowNum()) {
                Region newRegion = new Region(newRow.getRowNum(), region.getColumnFrom(),
                        (short) (newRow.getRowNum() + (region.getRowTo() - region.getRowFrom())), region.getColumnTo());
                worksheet.addMergedRegion(newRegion);
            }
        }
    }

    public String getOutputPath() {
        return outputPath;
    }

    protected abstract String getSourceFilePath();

    protected abstract String getOutputFileName();

    protected abstract CodeLangue getCodeLangue();

    protected abstract void edit();
}
