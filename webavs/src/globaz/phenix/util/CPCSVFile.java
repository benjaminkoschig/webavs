package globaz.phenix.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Vector;

/**
 * @author JPA
 */
public class CPCSVFile {

    private final static char CELL_SEPARATOR = ';';
    private int m_colsCount;
    private Vector<Vector<String>> m_fileContent;
    private int m_rowsCount;

    /**
     * Method CSVFile.
     * 
     * @param reader
     *            un reader dans lequel on lit le fichier CSV.
     */
    public CPCSVFile(Reader reader) {
        m_fileContent = new Vector<Vector<String>>();
        readFromFile(reader);
        fitVectorsToSize();
    }

    /**
     * Method CSVFile.
     * 
     * @param path
     *            le chemin du fichier � parser.
     * @throws FileNotFoundException
     *             si le fichier sp�cifi� n'existe pas.
     */
    public CPCSVFile(String path) throws FileNotFoundException {
        m_fileContent = new Vector<Vector<String>>();
        FileReader fileReader = new FileReader(path);
        readFromFile(fileReader);
        fitVectorsToSize();
    }

    private void fitVectorsToSize() {
        m_fileContent.setSize(getRowsCount());
        int fileSize = getRowsCount();
        int colCount = getColsCount();
        for (int i = 0; i < fileSize; i++) {
            Vector<?> aRow = m_fileContent.get(i);
            if (aRow == null) {
                m_fileContent.set(i, new Vector<String>());
                aRow = m_fileContent.get(i);
            }
            aRow.setSize(colCount);
        }
    }

    /**
     * Returns the colsCount.
     * 
     * @return int
     */
    public int getColsCount() {
        return m_colsCount;
    }

    /**
     * Method getData.
     * 
     * @param row
     *            la ligne voulue
     * @param col
     *            la colonne voulue
     * @return String la valeur � l'enplacement sp�cifi�. Null si outOfBound.
     */
    public String getData(int row, int col) {
        if ((row < 0) || (col < 0) || (row > (getRowsCount() - 1)) || (col > (getColsCount() - 1))) {
            return null;
        }
        try {
            Vector<?> theRow = m_fileContent.get(row);
            String result = (String) theRow.get(col);
            return (result == null ? "" : result);
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    /**
     * Returns the rowsCount.
     * 
     * @return int
     */
    public int getRowsCount() {
        return m_rowsCount;
    }

    /**
     * Method readFromFile.
     * 
     * @param path
     */
    private void readFromFile(Reader reader) {
        BufferedReader buffReader = new BufferedReader(reader);
        if (buffReader != null) {
            try {
                String tempLine;
                tempLine = buffReader.readLine();
                while (tempLine != null) {
                    readFromLine(tempLine);
                    tempLine = buffReader.readLine();
                }
            } catch (IOException e) {
                System.err.println("Error reading CSV file: " + e.toString());
            } finally {
                try {
                    buffReader.close();
                } catch (IOException e) {
                    System.err.println("Erreur closing CSV file: " + e.toString());
                }
            }
        }
        System.runFinalization();
        System.gc();
    }

    /**
     * Method readFromLine.
     * 
     * @param tempLine
     */
    private void readFromLine(String tempLine) {
        if (tempLine == null) {
            return;
        }
        Vector<String> currentLine = new Vector<String>();
        m_fileContent.add(currentLine);
        m_rowsCount++;
        // setRowsCount(getRowsCount() + 1);
        if (tempLine.trim().length() == 0) {
            return;
        }
        int colCount = 0;
        int cursorBegin = 0;
        int cursorEnd = tempLine.indexOf(CPCSVFile.CELL_SEPARATOR);
        while (cursorBegin > -1) {
            if (cursorEnd == -1) {
                currentLine.add(tempLine.substring(cursorBegin));
                cursorBegin = cursorEnd;
            } else {
                currentLine.add(tempLine.substring(cursorBegin, cursorEnd));
                cursorBegin = cursorEnd + 1;
            }
            cursorEnd = tempLine.indexOf(CPCSVFile.CELL_SEPARATOR, cursorBegin);
            colCount++;
        }
        if (colCount > getColsCount()) {
            setColsCount(Math.max(getColsCount(), colCount));
        }
    }

    /**
     * Sets the colsCount.
     * 
     * @param colsCount
     *            The colsCount to set
     */
    public void setColsCount(int colsCount) {
        m_colsCount = colsCount;
        fitVectorsToSize();
    }

    /**
     * Method setData.
     * 
     * @param row
     *            le num�ro de ligne (commence � 0).
     * @param col
     *            le num�ro de colonne (commence � 0).
     * @param data
     *            les donn�es � ins�rer.
     */
    public void setData(int row, int col, String data) {
        if ((row < 0) || (col < 0) || (row > (getRowsCount() - 1)) || (col > (getColsCount() - 1))) {
            throw new IndexOutOfBoundsException();
        }
        Vector<String> theRow = m_fileContent.get(row);
        theRow.setElementAt(data, col);
    }

    /**
     * Sets the rowsCount.
     * 
     * @param rowsCount
     *            The rowsCount to set
     */
    public void setRowsCount(int rowsCount) {
        m_rowsCount = rowsCount;
        fitVectorsToSize();
    }

    /**
     * Method write.
     * 
     * @param filePath
     *            le fichier dans lequel sauver les donn�es.
     * @throws IOException
     *             si une erreur survient.
     */
    public void write(String filePath) throws IOException {
        FileWriter fileWriter = new FileWriter(filePath);
        this.write(fileWriter);
    }

    /**
     * Method write.
     * 
     * @param aWriter
     *            le writer dans lequel on veut �crire les donn�es.
     * @throws IOException
     *             si une erreur survient.
     */
    public void write(Writer aWriter) throws IOException {
        BufferedWriter writer;
        writer = new BufferedWriter(aWriter);
        int fileSize = getRowsCount();
        int colCount = getColsCount();
        for (int i = 0; i < fileSize; i++) {
            for (int j = 0; j < colCount; j++) {
                writer.write(getData(i, j));
                if (j + 1 < colCount) {
                    writer.write(CPCSVFile.CELL_SEPARATOR);
                }
            }
            if (i + 1 < fileSize) {
                writer.write("\n");
            }
        }
        writer.flush();
        writer.close();
    }
}