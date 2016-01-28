package globaz.cygnus.services;

import globaz.cygnus.api.process.IRFEtatProcess;
import globaz.cygnus.db.process.RFEtatProcess;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;

/**
 * 
 * Classe permettant de g�rer l'�tat des processus RFM
 * 
 * @author JJE
 * 
 */
public class RFSetEtatProcessService {

    private static boolean getEtatProcess(String idProcess, BISession session) throws Exception {

        BITransaction transactionEtatProcess = null;
        try {
            transactionEtatProcess = ((BSession) session).newTransaction();
            transactionEtatProcess.openTransaction();

            RFEtatProcess rfEtatProcess = new RFEtatProcess();
            rfEtatProcess.setSession((BSession) session);
            rfEtatProcess.setIdProcess(idProcess);

            rfEtatProcess.retrieve();

            if (!rfEtatProcess.isNew()) {
                // Pour s'assurer que l'�tat n'est pas en cours de modification (verouill�) on fait un update
                rfEtatProcess.setEtatProcess(rfEtatProcess.getEtatProcess());
                rfEtatProcess.update(transactionEtatProcess);

                if (transactionEtatProcess.hasErrors() || transactionEtatProcess.isRollbackOnly()) {
                    transactionEtatProcess.rollback();
                    throw new Exception(
                            "RFSetEtatProcessService.getEtatProcess(): Impossible de mettre � jour l'�tat du process");
                } else {
                    transactionEtatProcess.commit();
                }

                return rfEtatProcess.getEtatProcess();
            } else {
                throw new Exception(
                        "RFSetEtatProcessService.getEtatProcess(): Impossible de retrouver l'�tat du process");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("RFSetEtatProcessService.getEtatProcess(): " + e.getMessage());
        } finally {
            transactionEtatProcess.closeTransaction();
        }

    }

    /**
     * 
     * Renvoie l'�tat du process "Pr�parer d�cision"
     * 
     * @param viewBean
     * @param session
     * @throws Exception
     */
    public static boolean getEtatProcessPreparerDecision(BISession session) throws Exception {
        return RFSetEtatProcessService.getEtatProcess(IRFEtatProcess.idProcessPreparerDecision, session);
    }

    /**
     * 
     * Renvoie l'�tat du process "Valider d�cision"
     * 
     * @param viewBean
     * @param session
     * @throws Exception
     */
    public static boolean getEtatProcessValiderDecision(BISession session) throws Exception {
        return RFSetEtatProcessService.getEtatProcess(IRFEtatProcess.idProcessValiderDecision, session);
    }

    /**
     * 
     * Change l'�tat d'un process (arr�t�, d�marr�) selon les param�tres
     * 
     * @param idProcess
     * @param isDemarre
     * @param session
     * @param msgException
     * @throws Exception
     */
    private static void setEtatProcess(String idProcess, boolean isDemarre, BISession session) throws Exception {

        BITransaction transactionEtatProcess = null;
        try {
            transactionEtatProcess = ((BSession) session).newTransaction();
            transactionEtatProcess.openTransaction();

            RFEtatProcess rfEtatProcess = new RFEtatProcess();
            rfEtatProcess.setSession((BSession) session);
            rfEtatProcess.setIdProcess(idProcess);

            rfEtatProcess.retrieve();

            if (!rfEtatProcess.isNew()) {
                rfEtatProcess.setEtatProcess(isDemarre);
                rfEtatProcess.update(transactionEtatProcess);

                if (transactionEtatProcess.hasErrors() || transactionEtatProcess.isRollbackOnly()) {
                    transactionEtatProcess.rollback();
                    throw new Exception(
                            "RFSetEtatProcessService.setEtatProcess(): Impossible de mettre � jour l'�tat du process");
                } else {
                    transactionEtatProcess.commit();
                }

            } else {
                throw new Exception(
                        "RFSetEtatProcessService.setEtatProcess(): Impossible de mettre � jour l'�tat du process");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("RFSetEtatProcessService.setEtatProcess(): " + e.getMessage());
        } finally {
            transactionEtatProcess.closeTransaction();
        }
    }

    /**
     * Change l'�tat du process "Pr�parer d�cision"
     * 
     * @param isDemarre
     * @param session
     * @param msgException
     * @throws Exception
     */
    public static void setEtatProcessPreparerDecision(boolean isDemarre, BISession session) throws Exception {
        RFSetEtatProcessService.setEtatProcess(IRFEtatProcess.idProcessPreparerDecision, isDemarre, session);
    }

    /**
     * Change l'�tat du process "Valider d�cision"
     * 
     * @param isDemarre
     * @param session
     * @param msgException
     * @throws Exception
     */
    public static void setEtatProcessValiderDecision(boolean isDemarre, BISession session) throws Exception {
        RFSetEtatProcessService.setEtatProcess(IRFEtatProcess.idProcessValiderDecision, isDemarre, session);
    }

}
