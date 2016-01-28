package globaz.osiris.parser;

import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSession;
import java.io.BufferedReader;

/**
 * Insérez la description du type ici. Date de création : (18.11.2002 10:05:47)
 * 
 * @author: Administrator
 */
public interface IntLSVParser {
    String FOOTER = "F";
    java.lang.String GENRE_CREDIT = "D";
    java.lang.String GENRE_DEBIT = "C";
    java.lang.String HEADER = "H";
    java.lang.String TRANSACTION = "T";

    public String getCodeRefus();

    public String getCommunication1();

    public String getCommunication2();

    public String getCommunication3();

    public String getCommunication4();

    public String getCommunication5();

    /**
     * Insérez la description de la méthode ici. Date de création : (21.02.2002 13:43:11)
     * 
     * @return String
     */
    String getCurrentBuffer();

    public String getDateEcheance();

    public String getDesignationSuppDebiteur();

    public String getDesignationSuppDonneurOrdre();

    public boolean getEchoToConsole();

    public String getElementEcritureCreditCode();

    public String getElementEcritureCreditMontantTotal();

    public String getElementEcritureCreditNbreTransaction();

    public String getElementEcritureRefusCode();

    public String getElementEcritureRefusMontantTotal();

    public String getElementEcritureRefusNbreTransaction();

    public String getGenreTransaction();

    public String getIdentificateurFichier();

    public BufferedReader getInputReader();

    public String getLieuDebiteur();

    public String getLieuDonneurOrdre();

    public FWMemoryLog getMemoryLog();

    public String getMontant();

    public String getMontantTotalPrix();

    public String getNombreTransactions();

    public String getNomDebiteur();

    public String getNomDonneurOrdre();

    public String getNPADebiteur();

    public String getNPADonneurOrdre();

    public String getNumeroAdherent();

    public String getNumeroComptePostalDebiteur();

    public String getNumeroOrdre();

    public String getNumeroReference();

    String getNumeroTransaction();

    public String getPrix();

    public String getRueDebiteur();

    public String getRueDonneurOrdre();

    /**
     * Insérez la description de la méthode ici. Date de création : (11.11.2002 10:38:06)
     * 
     * @return BSession
     */
    BSession getSession();

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 12:42:56)
     * 
     * @return String
     */
    String getTypeTransaction();

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 16:31:51)
     * 
     * @return boolean
     */
    boolean parseNextElement();

    public void setEchoToConsole(boolean newEchoToConsole);

    public void setInputReader(BufferedReader newInputReader);

    public void setMemoryLog(FWMemoryLog newMemoryLog);

    /**
     * Insérez la description de la méthode ici. Date de création : (11.11.2002 10:37:16)
     * 
     * @param session
     *            BSession
     */
    void setSession(BSession session);
}
