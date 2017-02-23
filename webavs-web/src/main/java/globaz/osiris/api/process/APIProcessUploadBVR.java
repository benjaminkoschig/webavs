package globaz.osiris.api.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;

public interface APIProcessUploadBVR extends FWViewBeanInterface {

    public String getDateValeur();

    public boolean getEchoToConsole();

    public String getEMailAddress();

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
}
