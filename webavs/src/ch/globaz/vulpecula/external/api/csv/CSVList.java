package ch.globaz.vulpecula.external.api.csv;

import globaz.globall.db.BSession;
import globaz.jade.common.Jade;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public abstract class CSVList {
    private static final String DEFAULT_SEPARATOR = ";";
    private static final String DEFAULT_EXTENSION = "csv";
    private static final String EXTENSION_SEPARATOR = ".";

    private final PrintWriter printWriter;
    private final String separator;
    private final BSession session;
    private final String outputFile;
    private boolean firstCell;

    public CSVList(BSession session, String fileName) throws FileNotFoundException {
        this(session, fileName, DEFAULT_SEPARATOR);
    }

    public abstract void createContent();

    public CSVList(BSession session, String fileName, String separator) throws FileNotFoundException {
        String filePath = Jade.getInstance().getPersistenceDir();
        outputFile = filePath + fileName + EXTENSION_SEPARATOR + DEFAULT_EXTENSION;
        printWriter = new PrintWriter(new File(outputFile));
        this.session = session;
        this.separator = separator;
        firstCell = true;
    }

    public void createCell(String element) {
        if (!firstCell) {
            printWriter.write(separator);
        } else {
            firstCell = false;
        }
        printWriter.write(element);
    }

    public void createCellFormat(String element, int maxLength) {
        createCell(formatEntry(element, maxLength));
    }

    public void createRow() {
        firstCell = true;
        printWriter.write("\n");
    }

    public String getOutputFile() {
        createContent();
        printWriter.close();
        return outputFile;
    }

    public String getLabel(String id) {
        return session.getLabel(id);
    }

    public String getCodeLibelle(String cs) {
        return session.getCodeLibelle(cs);
    }

    public String getCode(String cs) {
        return session.getCode(cs);
    }

    public String formatEntry(String data, int maxLength) {
        if (data == null || data.length() == 0) {
            return "";
        }
        if (data.length() <= maxLength) {
            return data;
        }
        return data.substring(0, maxLength);
    }

    public String unformat(String dataFormated) {
        if (dataFormated == null || dataFormated.length() == 0) {
            return "";
        }
        return dataFormated.replace(".", "");
    }
}
