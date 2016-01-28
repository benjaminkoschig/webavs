package globaz.osiris.db.ordres;

import globaz.framework.util.FWMemoryLog;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrement;
import globaz.osiris.db.comptes.CAOperationOrdreVersement;

/**
 * Interface à implémenter pour le formattage d'un ordre de versement ou recouvrement selon la norme DTA, LSV, etc. Date
 * de création : (08.02.2002 13:33:33)
 * 
 * @author: Administrator
 */
public interface IntOrdreFormateur {
    public StringBuffer format(CAOperationOrdreRecouvrement or);

    public StringBuffer format(CAOperationOrdreVersement ov);

    public StringBuffer formatEOF(CAOrdreGroupe og);

    public StringBuffer formatHeader(CAOrdreGroupe og);

    public boolean getEchoToConsole();

    public boolean getInsertNewLine();

    public FWMemoryLog getMemoryLog();

    public java.io.PrintWriter getPrintWriter();

    /**
     * Insérez la description de la méthode ici. Date de création : (11.11.2002 10:38:06)
     * 
     * @return globaz.globall.db.BSession
     */
    globaz.globall.db.BSession getSession();

    public void setEchoToConsole(boolean isEchoToConsole);

    public void setInsertNewLine(boolean newLine);

    public void setMemoryLog(FWMemoryLog newLog);

    public void setPrintWriter(java.io.PrintWriter newPrintWriter);

    /**
     * Insérez la description de la méthode ici. Date de création : (11.11.2002 10:37:16)
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    void setSession(globaz.globall.db.BSession session);
}
