/**
 * 
 */
package ch.globaz.amal.process.repriseFinAnnee;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * @author dhi
 * 
 */
public class AMProcessRepriseFinAnneePopulationCsvFileHelper extends AMProcessRepriseFinAnneePopulationFileHelper {

    /**
     * Default constructor
     */
    protected AMProcessRepriseFinAnneePopulationCsvFileHelper() {
    }

    /**
     * Récupération du nom du fichier complet
     * 
     * @return
     */
    @Override
    public String getFullFileName() {
        return super.getFullFileName() + AMProcessRepriseFinAnneePopulationFileHelper.FILE_TYPE_CSV_EXTENSION;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.amal.process.annonce.AnnonceCMProcessFileHelper#writeFile(java.util.List)
     */
    @Override
    public void writeFile(List<String> records) {
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
            lineHeader += "NSS" + AMProcessRepriseFinAnneePopulationFileHelper.CSV_SEPARATOR;
            lineHeader += "Rôle_Amal" + AMProcessRepriseFinAnneePopulationFileHelper.CSV_SEPARATOR;
            lineHeader += "Nom_prénom_Amal" + AMProcessRepriseFinAnneePopulationFileHelper.CSV_SEPARATOR;
            lineHeader += "Date_naissance_Amal" + AMProcessRepriseFinAnneePopulationFileHelper.CSV_SEPARATOR;
            lineHeader += "Date_décès" + AMProcessRepriseFinAnneePopulationFileHelper.CSV_SEPARATOR;
            lineHeader += "Recherche_PC" + AMProcessRepriseFinAnneePopulationFileHelper.CSV_SEPARATOR;
            lineHeader += "Début_PC" + AMProcessRepriseFinAnneePopulationFileHelper.CSV_SEPARATOR;
            lineHeader += "Fin_PC" + AMProcessRepriseFinAnneePopulationFileHelper.CSV_SEPARATOR;
            lineHeader += "Etat_PC" + AMProcessRepriseFinAnneePopulationFileHelper.CSV_SEPARATOR;
            out.write(lineHeader);
            out.newLine();

            for (String currentRecord : records) {
                out.write(currentRecord);
                out.newLine();
            }

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
