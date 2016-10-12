/**
 * @author : scr
 * @date : 18.10.2007
 */

package globaz.corvus.module.compta;

import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.process.RETraiterLotDecisionsProcess;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.osiris.api.APIGestionComptabiliteExterne;

/**
 * Description Interface définissant les méthodes communes au module comptables
 * 
 * @author scr
 * 
 */
public interface IREModuleComptable {

    public FWMemoryLog doTraitement(RETraiterLotDecisionsProcess process, APIGestionComptabiliteExterne compta,
            BSession session, BTransaction transaction, REDecisionEntity decision, String dateComptable, String idLot,
            String dateEcheance) throws Exception;

    public FWMemoryLog doTraitement(RETraiterLotDecisionsProcess process, APIGestionComptabiliteExterne compta,
            BSession session, BTransaction transaction, REDecisionEntity decision, String dateComptable, String idLot,
            String dateEcheance, String idOrganeExecution) throws Exception;

    public int getPriority();

    @Override
    public String toString();

}
