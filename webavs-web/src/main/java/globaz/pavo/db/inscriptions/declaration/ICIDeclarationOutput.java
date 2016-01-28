package globaz.pavo.db.inscriptions.declaration;

import globaz.globall.db.BSession;

/**
 * @author oca
 * 
 *         Processus Datenträger Façade permettant d'avoir plusieur format de sortie pour les listes des déclarations de
 *         salaire A définir ...
 */
public interface ICIDeclarationOutput {

    public String getOutputFile();

    public BSession getSession();

    public boolean isSimulation();

    public void setData(Object obj);

    public void setSession(BSession session);

    public void setSimulation(boolean simulation);

}
