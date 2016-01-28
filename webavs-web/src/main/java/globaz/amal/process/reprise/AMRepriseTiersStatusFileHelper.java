/**
 * 
 */
package globaz.amal.process.reprise;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dhi
 * 
 */
public class AMRepriseTiersStatusFileHelper {

    private static Map<String, AMRepriseTiersStatusFileHelper> instances = new HashMap<String, AMRepriseTiersStatusFileHelper>();
    public static final String STATUS_FILE_ERROR_ADRESSES = "errorAdresses";
    public static final String STATUS_FILE_ERROR_TIERS = "errorTiers";

    public static final String STATUS_FILE_NEW_ADRESSES = "newAdresses";
    public static final String STATUS_FILE_NEW_TIERS = "newTiers";
    public static final String STATUS_FILE_UPDATED_ADRESSES = "updatedAdresses";
    public static final String STATUS_FILE_UPDATED_TIERS = "updatedTiers";

    /**
     * Get an instance of AMRepriseTiersStatusFileHelper for a specific file type
     * 
     * @param statusFileType
     * @return
     */
    public static AMRepriseTiersStatusFileHelper getInstance(String statusFileType) {
        if (AMRepriseTiersStatusFileHelper.instances.get(statusFileType) == null) {
            AMRepriseTiersStatusFileHelper.instances.put(statusFileType, new AMRepriseTiersStatusFileHelper(
                    statusFileType));
        }
        return AMRepriseTiersStatusFileHelper.instances.get(statusFileType);
    }

    private PrintWriter statusFileOut = null;

    // Variables de la classes
    private String statusFileType = null;

    /**
     * Default constructor, called by the getInstance Method
     */
    private AMRepriseTiersStatusFileHelper(String statusFileType) {
        this.statusFileType = statusFileType;
        if ((this.statusFileType == null) || (this.statusFileType.length() <= 0)) {
            throw new NullPointerException("Status File Type cannot be blank");
        }
    }

    /**
     * Clôture du fichier
     * 
     */
    public void closeFile() {
        if (statusFileOut != null) {
            statusFileOut.close();
            statusFileOut = null;
        }
    }

    /**
     * Write the file
     * 
     * @param statusFileType
     * @param message
     */
    public void writeStatus(String statusFileType, String message) {

        if (statusFileOut == null) {
            // Create the file
            try {
                String pathToWrite = "D:\\Temp";
                String fileName = "Lamal_Reprise_" + statusFileType + ".csv";
                String filePath = pathToWrite + "/" + fileName;
                statusFileOut = new PrintWriter(new FileWriter(filePath));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            // Create the header
            // StringBuffer csvHeader = new StringBuffer();
            // if (statusFileType.equals(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_ADRESSES)) {
            // csvHeader.append("Adresses Error Messages").append(";");
            // } else if (statusFileType.equals(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_TIERS)) {
            // csvHeader.append("Tiers Error Messages").append(";");
            // } else if (statusFileType.equals(AMRepriseTiersStatusFileHelper.STATUS_FILE_NEW_ADRESSES)) {
            // csvHeader.append("New Adresses Messages").append(";");
            // } else if (statusFileType.equals(AMRepriseTiersStatusFileHelper.STATUS_FILE_NEW_TIERS)) {
            // csvHeader.append("New Tiers Messages").append(";");
            // } else if (statusFileType.equals(AMRepriseTiersStatusFileHelper.STATUS_FILE_UPDATED_ADRESSES)) {
            // csvHeader.append("Updated Adresses Messages").append(";");
            // } else if (statusFileType.equals(AMRepriseTiersStatusFileHelper.STATUS_FILE_UPDATED_TIERS)) {
            // csvHeader.append("Updated Tiers Messages").append(";");
            // }
            // Write it
            // this.statusFileOut.println(csvHeader.toString());
            statusFileOut.flush();
        }
        // update the file
        StringBuffer csvLine = new StringBuffer();
        csvLine.append(message);
        statusFileOut.println(csvLine.toString());
        statusFileOut.flush();
    }

}
