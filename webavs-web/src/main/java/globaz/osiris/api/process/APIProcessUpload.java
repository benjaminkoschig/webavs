package globaz.osiris.api.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;

public interface APIProcessUpload extends FWViewBeanInterface {

    public String getDateValeur();

    public boolean getEchoToConsole();

    public String getEMailAddress();

    public void setMemoryLogProcess(FWMemoryLog newMemoryLog);

    public FWMemoryLog getMemoryLogProcess();

    public String getFileName();

    public String getIdOrganeExecution();

    public String getIdYellowReportFile();

    public String getLibelle();

    public BSession getSession();

    public Boolean getSimulation();

    public void setDateValeur(String newDateValeur);

    public void setEchoToConsole(boolean newEchoToConsole);

    public void setFileName(String fileName);

    public void setIdOrganeExecution(String idOrgarneExecution);

    public void setIdYellowReportFile(String id);

    public void setLibelle(String newLibelle);

    public void setSession(BSession newSession);

    public void setSimulation(Boolean simulation);

    public void setProgressScaleValueProcess(long value);

    public BTransaction getTransactionProcess();

    public boolean isAbortedProcess();
}
