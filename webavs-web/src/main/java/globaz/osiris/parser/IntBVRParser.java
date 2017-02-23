package globaz.osiris.parser;

import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSession;
import java.io.BufferedReader;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (14.02.2002 16:23:08)
 * 
 * @author: Administrator
 */
@Deprecated
public interface IntBVRParser {
    String FOOTER = "F";
    String GENRE_CREDIT = "D";
    String GENRE_DEBIT = "C";
    String HEADER = "H";
    String TRANSACTION = "T";

    public String getCodeRejet();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.02.2002 13:43:11)
     * 
     * @return String
     */
    String getCurrentBuffer();

    public String getDateDepot();

    public String getDateInscription();

    public String getDateTraitement();

    public boolean getEchoToConsole();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.02.2002 09:27:13)
     * 
     * @return String
     */
    String getGenreEcriture();

    public String getGenreTransaction();

    public BufferedReader getInputReader();

    public FWMemoryLog getMemoryLog();

    public String getMontant();

    public String getNombreTransactions();

    public String getNumeroAdherent();

    public String getNumeroReference();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.02.2002 12:48:13)
     * 
     * @return String
     */
    String getNumeroTransaction();

    public String getReferenceInterne();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (11.11.2002 10:38:06)
     * 
     * @return BSession
     */
    BSession getSession();

    public String getTaxeTraitement();

    public String getTaxeVersement();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.02.2002 12:42:56)
     * 
     * @return String
     */
    String getTypeTransaction();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.02.2002 16:31:51)
     * 
     * @return boolean
     */
    boolean parseNextElement();

    public void setEchoToConsole(boolean newEchoToConsole);

    public void setInputReader(BufferedReader newInputReader);

    public void setMemoryLog(FWMemoryLog newMemoryLog);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (11.11.2002 10:37:16)
     * 
     * @param session
     *            BSession
     */
    void setSession(BSession session);
}
