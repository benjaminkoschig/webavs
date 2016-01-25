package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.globall.db.BSession;

/**
 * Le rôle de cette interface est de pouvoir découpler l'accès à la base de données des implémentation des Rule. La
 * classe mère 'Rule' dont toute les Rules héritent, possède une implémentation de cette interface qui va permettre
 * l'accès aux données Il est possible de redéfinir cette instance via le setter approprié dans pour effectuer des test
 * unitqire sur l'es règles RAPG.
 * 
 * @author lga
 */
public interface APRuleDBDataProvider {

    /**
     * Lance une requête pour voir si le time stamp fournis en paramètres est unique
     * 
     * @param timeStamp
     * @throws APRuleExecutionException
     */
    abstract boolean isTimeStampUnique(String timeStamp, BSession session) throws APRuleExecutionException;

    /**
     * @param insurantDomicileCountry
     * @throws APRuleExecutionException
     */
    abstract boolean isCodePaysExistant(String insurantDomicileCountry, BSession session)
            throws APRuleExecutionException;

}