package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.globall.db.BSession;

/**
 * Le r�le de cette interface est de pouvoir d�coupler l'acc�s � la base de donn�es des impl�mentation des Rule. La
 * classe m�re 'Rule' dont toute les Rules h�ritent, poss�de une impl�mentation de cette interface qui va permettre
 * l'acc�s aux donn�es Il est possible de red�finir cette instance via le setter appropri� dans pour effectuer des test
 * unitqire sur l'es r�gles RAPG.
 * 
 * @author lga
 */
public interface APRuleDBDataProvider {

    /**
     * Lance une requ�te pour voir si le time stamp fournis en param�tres est unique
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