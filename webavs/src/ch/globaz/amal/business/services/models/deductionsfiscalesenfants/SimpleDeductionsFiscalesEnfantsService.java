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
     * Copie une année de paramètre pour en créer une nouvelle
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
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleDeductionsFiscalesEnfantsSearch search) throws DeductionsFiscalesEnfantsException,
            JadePersistenceException;

    /**
     * Creation d'une entité en base de donnée
     * 
     * @param simpleParametreAnnuel
     * @return l'entité crée
     * @throws ParametreAnnuelException
     * @throws JadePersistenceException
     */
    public SimpleDeductionsFiscalesEnfants create(SimpleDeductionsFiscalesEnfants simpleDeductionsFiscalesEnfants)
            throws DeductionsFiscalesEnfantsException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité
     * 
     * @param simpleParametreAnnuel
     * @return l'entité supprimé
     * @throws JadePersistenceException
     * @throws ParametreAnnuelException
     */
    public SimpleDeductionsFiscalesEnfants delete(SimpleDeductionsFiscalesEnfants simpleDeductionsFiscalesEnfants)
            throws JadePersistenceException, DeductionsFiscalesEnfantsException;

    /**
     * Permet le chargement d'une entité
     * 
     * @param idParametreAnnuel
     * @return l'entité chargé
     * @throws JadePersistenceException
     * @throws ParametreAnnuelException
     */
    public SimpleDeductionsFiscalesEnfants read(String idDeductionFiscaleEnfant) throws JadePersistenceException,
            DeductionsFiscalesEnfantsException;

    /**
     * Permet la recherche d'entités
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
     * Update d'une entité en base de donnée
     * 
     * @param simpleParametreAnnuel
     * @return l'entité crée
     * @throws ParametreAnnuelException
     * @throws JadePersistenceException
     */
    public SimpleDeductionsFiscalesEnfants update(SimpleDeductionsFiscalesEnfants simpleDeductionsFiscalesEnfants)
            throws DeductionsFiscalesEnfantsException, JadePersistenceException;

}
