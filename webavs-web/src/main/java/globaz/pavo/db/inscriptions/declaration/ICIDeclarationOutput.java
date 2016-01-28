package globaz.pavo.db.inscriptions.declaration;

import globaz.globall.db.BSession;

/**
 * @author oca
 * 
 *         Processus Datentr�ger Fa�ade permettant d'avoir plusieur format de sortie pour les listes des d�clarations de
 *         salaire A d�finir ...
 */
public interface ICIDeclarationOutput {

    public String getOutputFile();

    public BSession getSession();

    public boolean isSimulation();

    public void setData(Object obj);

    public void setSession(BSession session);

    public void setSimulation(boolean simulation);

}
