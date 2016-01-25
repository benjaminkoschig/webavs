package globaz.campus.process.chargementLot;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Vector;

public class GELectureFichier {

    private final static String RW_MODE = "r";
    private RandomAccessFile inputFile;
    private BSession session = null;
    private BTransaction transaction = null;
    private Vector vecEnregistrement;

    /**
	 * 
	 * 
	 * 
	 * 
	 **/
    public GELectureFichier(File fileName, BSession session, BTransaction transaction) throws Exception {
        setSession(session);
        setTransaction(transaction);
        //
        vecEnregistrement = new Vector();
        //
        if (!fileName.exists()) {
            throw new FileNotFoundException(fileName.getAbsolutePath());
        }
        inputFile = new RandomAccessFile(fileName, RW_MODE);
    }

    /**
	 * 
	 * 
	 * 
	 * 
	 **/
    public GELectureFichier(String fileName, BSession session, BTransaction transaction) throws Exception {
        this(new File(fileName), session, transaction);
    }

    boolean formatFichierOk(String firstLine) {
        boolean status = true;
        /*
         * try { if (!firstLine.startsWith(FORMAT_DEBUT_FICHIER)) { status = false; } } catch (Exception e) { status =
         * false; }
         */
        return status;
    }

    public GESerieRecords getEnregistrement(int index) {
        return (GESerieRecords) vecEnregistrement.elementAt(index);
    }

    public int getEnregistrementSize() {
        return vecEnregistrement.size();
    }

    public RandomAccessFile getInputFile() {
        return inputFile;
    }

    public BSession getSession() {
        return session;
    }

    public BTransaction getTransaction() {
        return transaction;
    }

    public Vector getVecEnregistrement() {
        return vecEnregistrement;
    }

    /**
     * Method isBusakRecord.
     * 
     * @return boolean
     */
    private boolean isEtudiantRecord(String line) {
        String chaine = line.substring(5, 6);
        return chaine.equals("1") || chaine.equals("2") || chaine.equals("3");
    }

    /**
     * Method parse.
     */
    public void parse() throws Exception {
        String line = "";
        GESerieRecords serieRecords = new GESerieRecords();

        // variables d'aide
        boolean first = true;
        int nombreLigne = 0;
        while ((line = inputFile.readLine()) != null) {
            // Compteur du nombre de ligne
            nombreLigne++;
            // contrôle avec le première ligne si le fichier est OK
            if (first) {
                if (!formatFichierOk(line)) {
                    // fermeture du fichier
                    inputFile.close();
                    throw new Exception(getSession().getLabel("FORMAT_FICHIER_KO"));
                }
                first = false;
            }
            if (isEtudiantRecord(line)) {
                if (!line.substring(5, 6).equals("1")) {
                    // même série
                    serieRecords.addRecord(line);
                } else {
                    // nouvelle série
                    serieRecords = new GESerieRecords();
                    serieRecords.addRecord(line);
                    vecEnregistrement.add(serieRecords);
                }
            }
        }
        // Efface le contenu des vecteurs
        // vecEnregistrement.clear();
        // fermeture du fichier
        inputFile.close();
    }

    public void setInputFile(RandomAccessFile inputFile) {
        this.inputFile = inputFile;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setTransaction(BTransaction transaction) {
        this.transaction = transaction;
    }

    public void setVecEnregistrement(Vector vecEnregistrement) {
        this.vecEnregistrement = vecEnregistrement;
    }

}
