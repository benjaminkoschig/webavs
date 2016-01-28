/**
 * 
 */
package globaz.amal.process.annonce.fileHelper;

import globaz.amal.process.annonce.cosama.AMCosamaRecord;
import globaz.amal.process.annonce.cosama.AMCosamaRecordDetail;
import globaz.amal.process.annonce.cosama.IAMCosamaRecord;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;

/**
 * @author dhi
 * 
 */
public class AnnonceCMProcessCsvFileHelper extends AnnonceCMProcessFileHelper {

    /**
     * Default constructor
     */
    protected AnnonceCMProcessCsvFileHelper() {
    }

    /**
     * Récupération du nom du fichier complet
     * 
     * @return
     */
    @Override
    public String getFullFileName() {
        return super.getFullFileName() + AnnonceCMProcessFileHelper.FILE_TYPE_CSV_EXTENSION;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.amal.process.annonce.AnnonceCMProcessFileHelper#writeFile(java.util.List)
     */
    @Override
    public void writeFile(List<AMCosamaRecord> records) {
        System.setProperty("line.separator", "\r\n");

        // Create file
        FileWriter fstream;
        BufferedWriter out;
        try {
            fstream = new FileWriter(new File(getFullFileName()));
            out = new BufferedWriter(fstream);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        try {
            // CSV Line Header
            String lineHeader = "";
            lineHeader += "No assuré" + IAMCosamaRecord._FieldSeparator;
            lineHeader += "No NSS" + IAMCosamaRecord._FieldSeparator;
            lineHeader += "No contribuable" + IAMCosamaRecord._FieldSeparator;
            lineHeader += "Nom et prénom" + IAMCosamaRecord._FieldSeparator;
            lineHeader += "Date naissance YYYYMMJJ" + IAMCosamaRecord._FieldSeparator;
            lineHeader += "Montant maximum subside" + IAMCosamaRecord._FieldSeparator;
            lineHeader += "P/A - Non" + IAMCosamaRecord._FieldSeparator;
            lineHeader += "Début droit" + IAMCosamaRecord._FieldSeparator;
            lineHeader += "Fin droit" + IAMCosamaRecord._FieldSeparator;
            // lineHeader += System.getProperty("line.separator");
            out.write(lineHeader);
            out.newLine();

            // en premier - entete
            Iterator<AMCosamaRecord> recordsIterator = records.iterator();
            // while (recordsIterator.hasNext()) {
            // AMCosamaRecord currentRecord = recordsIterator.next();
            // if (currentRecord instanceof AMCosamaRecordEnTete) {
            // String currentLine = currentRecord.writeLigne(true);
            // currentLine += System.getProperty("line.separator");
            // out.write(currentLine);
            // }
            // }
            // au milieu - detail
            recordsIterator = records.iterator();
            while (recordsIterator.hasNext()) {
                AMCosamaRecord currentRecord = recordsIterator.next();
                if (currentRecord instanceof AMCosamaRecordDetail) {
                    String currentLine = "";
                    currentLine += ((AMCosamaRecordDetail) currentRecord).getNoAssure()
                            + IAMCosamaRecord._FieldSeparator;
                    currentLine += ((AMCosamaRecordDetail) currentRecord).getNoAVS() + IAMCosamaRecord._FieldSeparator;
                    currentLine += ((AMCosamaRecordDetail) currentRecord).getNoPersonnelCantonal()
                            + IAMCosamaRecord._FieldSeparator;
                    currentLine += ((AMCosamaRecordDetail) currentRecord).getNomPrenomUsuel()
                            + IAMCosamaRecord._FieldSeparator;
                    if (((AMCosamaRecordDetail) currentRecord).getDateNaissance().length() > 7) {
                        currentLine += ((AMCosamaRecordDetail) currentRecord).getDateNaissance().substring(0, 4);
                        currentLine += "/";
                        currentLine += ((AMCosamaRecordDetail) currentRecord).getDateNaissance().substring(4, 6);
                        currentLine += "/";
                        currentLine += ((AMCosamaRecordDetail) currentRecord).getDateNaissance().substring(6);
                    } else {
                        currentLine += ((AMCosamaRecordDetail) currentRecord).getDateNaissance();
                    }
                    currentLine += IAMCosamaRecord._FieldSeparator;
                    if (((AMCosamaRecordDetail) currentRecord).getMontantEffectifSubside().length() > 2) {
                        currentLine += ((AMCosamaRecordDetail) currentRecord).getMontantEffectifSubside().substring(0,
                                ((AMCosamaRecordDetail) currentRecord).getMontantEffectifSubside().length() - 2);
                        currentLine += ".";
                        currentLine += ((AMCosamaRecordDetail) currentRecord).getMontantEffectifSubside().substring(
                                ((AMCosamaRecordDetail) currentRecord).getMontantEffectifSubside().length() - 2);
                    } else {
                        currentLine += ((AMCosamaRecordDetail) currentRecord).getMontantEffectifSubside();
                    }

                    currentLine += IAMCosamaRecord._FieldSeparator;
                    if (((AMCosamaRecordDetail) currentRecord).getBeneficiaireAssiste().equals("1")) {
                        currentLine += "A" + IAMCosamaRecord._FieldSeparator;
                    } else if (((AMCosamaRecordDetail) currentRecord).getBeneficiairePC().equals("1")) {
                        currentLine += "P" + IAMCosamaRecord._FieldSeparator;
                    } else {
                        currentLine += "NON" + IAMCosamaRecord._FieldSeparator;
                    }
                    if (((AMCosamaRecordDetail) currentRecord).getDateDebutSubside().length() > 5) {
                        currentLine += ((AMCosamaRecordDetail) currentRecord).getDateDebutSubside().substring(4);
                        currentLine += ".";
                        currentLine += ((AMCosamaRecordDetail) currentRecord).getDateDebutSubside().substring(0, 4);
                    } else {
                        currentLine += ((AMCosamaRecordDetail) currentRecord).getDateDebutSubside();
                    }
                    currentLine += IAMCosamaRecord._FieldSeparator;
                    if (((AMCosamaRecordDetail) currentRecord).getDateFinSubside().length() > 5) {
                        currentLine += ((AMCosamaRecordDetail) currentRecord).getDateFinSubside().substring(4);
                        currentLine += ".";
                        currentLine += ((AMCosamaRecordDetail) currentRecord).getDateFinSubside().substring(0, 4);
                    } else {
                        currentLine += ((AMCosamaRecordDetail) currentRecord).getDateFinSubside();
                    }
                    currentLine += IAMCosamaRecord._FieldSeparator;
                    // currentLine += System.getProperty("line.separator");
                    out.write(currentLine);
                    out.newLine();
                }
            }
            // en fin - total
            // recordsIterator = records.iterator();
            // while (recordsIterator.hasNext()) {
            // AMCosamaRecord currentRecord = recordsIterator.next();
            // if (currentRecord instanceof AMCosamaRecordTotal) {
            // String currentLine = currentRecord.writeLigne(true);
            // currentLine += System.getProperty("line.separator");
            // out.write(currentLine);
            // }
            // }
            // Close the output stream
        } catch (Exception e) {// Catch exception if any
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
