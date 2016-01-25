/**
 * 
 */
package ch.globaz.amal.business.services.models.deductionsfiscalesenfants;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.deductionsFiscalesEnfants.DeductionsFiscalesEnfantsException;
import ch.globaz.amal.business.exceptions.models.parametreannuel.ParametreAnnuelException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfants;
import ch.globaz.amal.business.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfantsSearch;

/**
 * @author CBU
 * 
 */
public interface SimpleDeductionsFiscalesEnfantsService extends JadeApplicationService {

    /**
     * Copie une ann�e de param�tre pour en cr�er une nouvelle
     * 
     * @param yearToCopy
     * @param newYear
     * @throws ParametreAnnuelException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public void copyParams(String yearToCopy, String newYear) throws DeductionsFiscalesEnfantsException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleDeductionsFiscalesEnfantsSearch search) throws DeductionsFiscalesEnfantsException,
            JadePersistenceException;

    /**
     * Creation d'une entit� en base de donn�e
     * 
     * @param simpleParametreAnnuel
     * @return l'entit� cr�e
     * @throws ParametreAnnuelException
     * @throws JadePersistenceException
     */
    public SimpleDeductionsFiscalesEnfants create(SimpleDeductionsFiscalesEnfants simpleDeductionsFiscalesEnfants)
            throws DeductionsFiscalesEnfantsException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit�
     * 
     * @param simpleParametreAnnuel
     * @return l'entit� supprim�
     * @throws JadePersistenceException
     * @throws ParametreAnnuelException
     */
    public SimpleDeductionsFiscalesEnfants delete(SimpleDeductionsFiscalesEnfants simpleDeductionsFiscalesEnfants)
            throws JadePersistenceException, DeductionsFiscalesEnfantsException;

    /**
     * Permet le chargement d'une entit�
     * 
     * @param idParametreAnnuel
     * @return l'entit� charg�
     * @throws JadePersistenceException
     * @throws ParametreAnnuelException
     */
    public SimpleDeductionsFiscalesEnfants read(String idDeductionFiscaleEnfant) throws JadePersistenceException,
            DeductionsFiscalesEnfantsException;

    /**
     * Permet la recherche d'entit�s
     * 
     * @param parametreAnnuelSearch
     * @return le resultat de la recherche
     * @throws JadePersistenceException
     * @throws ParametreAnnuelException
     */
    public SimpleDeductionsFiscalesEnfantsSearch search(
            SimpleDeductionsFiscalesEnfantsSearch simpleDeductionsFiscalesEnfantsSearch)
            throws JadePersistenceException, DeductionsFiscalesEnfantsException;

    /**
     * Update d'une entit� en base de donn�e
     * 
     * @param simpleParametreAnnuel
     * @return l'entit� cr�e
     * @throws ParametreAnnuelException
     * @throws JadePersistenceException
     */
    public SimpleDeductionsFiscalesEnfants update(SimpleDeductionsFiscalesEnfants simpleDeductionsFiscalesEnfants)
            throws DeductionsFiscalesEnfantsException, JadePersistenceException;

}
