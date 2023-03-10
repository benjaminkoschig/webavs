package globaz.osiris.parser;

import java.io.BufferedReader;

/**
 * Interface compl?tant l'interface des besoin m?tier (IntBVRPojo) et assurant la continuit? du fonctionnement du
 * parseur de fichier plat (remplac? a terme parISO20022)
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

    boolean parseNextElement();

    public void setEchoToConsole(boolean newEchoToConsole);

    public void setInputReader(BufferedReader newInputReader);

    public String getNombreTransactions();
}
