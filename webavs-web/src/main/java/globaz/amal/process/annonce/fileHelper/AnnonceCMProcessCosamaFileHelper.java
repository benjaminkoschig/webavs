/**
 * 
 */
package globaz.amal.process.annonce.fileHelper;

import globaz.amal.process.annonce.cosama.AMCosamaRecord;
import globaz.amal.process.annonce.cosama.AMCosamaRecordDetail;
import globaz.amal.process.annonce.cosama.AMCosamaRecordEnTete;
import globaz.amal.process.annonce.cosama.AMCosamaRecordTotal;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;

/**
 * @author dhi
 * 
 */
public class AnnonceCMProcessCosamaFileHelper extends AnnonceCMProcessFileHelper {

    /**
     * Default constructor
     */
    protected AnnonceCMProcessCosamaFileHelper() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.amal.process.annonce.AnnonceCMProcessFileHelper#writeFile(java.util.List)
     */
    @Override
    public void writeFile(List<AMCosamaRecord> records) {
        System.setProperty("line.separator", "\r\n");
        try {
            // Create file
            FileWriter fstream = new FileWriter(new File(getFullFileName()));
            BufferedWriter out = new BufferedWriter(fstream);
            Iterator<AMCosamaRecord> recordsIterator = records.iterator();
            // En-Tête
            while (recordsIterator.hasNext()) {
                AMCosamaRecord currentRecord = recordsIterator.next();
                if (currentRecord instanceof AMCosamaRecordEnTete) {
                    currentRecord.formatFields();
                    String currentLine = currentRecord.writeLigne();
                    // currentLine += System.getProperty("line.separator");
                    out.write(currentLine);
                    out.newLine();
                }
            }
            // Detail
            recordsIterator = records.iterator();
            while (recordsIterator.hasNext()) {
                AMCosamaRecord currentRecord = recordsIterator.next();
                if (currentRecord instanceof AMCosamaRecordDetail) {
                    currentRecord.formatFields();
                    String currentLine = currentRecord.writeLigne();
                    // currentLine += System.getProperty("line.separator");
                    out.write(currentLine);
                    out.newLine();
                }
            }
            // Total
            recordsIterator = records.iterator();
            while (recordsIterator.hasNext()) {
                AMCosamaRecord currentRecord = recordsIterator.next();
                if (currentRecord instanceof AMCosamaRecordTotal) {
                    currentRecord.formatFields();
                    String currentLine = currentRecord.writeLigne();
                    // currentLine += System.getProperty("line.separator");
                    out.write(currentLine);
                    out.newLine();
                }
            }
            // Close the output stream
            out.flush();
            out.close();
        } catch (Exception e) {// Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

}
