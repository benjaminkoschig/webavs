package globaz.osiris.parser;

import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSession;
import java.io.BufferedReader;

/**
 * Interface complétant l'interface des besoin métier (IntBVRPojo) et assurant la continuité du fonctionnement du
 * parseur de fichier plat (remplacé a terme parISO20022)
 * 
 * [ce dernier reprend le contrat complet de l'interface IntBVRParser]
 * 
 * @author cel
 * 
 */
public interface IntBVRFlatFileParser extends IntBVRPojo {
    String FOOTER = "F";
    String HEADER = "H";
    String TRANSACTION = "T";

    String getTypeTransaction();

    String getNumeroTransaction();

    String getCurrentBuffer();

    public boolean getEchoToConsole();

    public BufferedReader getInputReader();

    public FWMemoryLog getMemoryLog();

    BSession getSession();

    boolean parseNextElement();

    public void setEchoToConsole(boolean newEchoToConsole);

    public void setInputReader(BufferedReader newInputReader);

    public void setMemoryLog(FWMemoryLog newMemoryLog);

    void setSession(BSession session);

    public String getNombreTransactions();
}
