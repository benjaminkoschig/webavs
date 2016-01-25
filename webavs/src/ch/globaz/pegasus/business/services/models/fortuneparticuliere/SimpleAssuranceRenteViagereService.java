/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.AssuranceRenteViagereException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleAssuranceRenteViagere;

/**
 * @author BSC
 * 
 */
public interface SimpleAssuranceRenteViagereService extends JadeApplicationService {

    /**
     * Permet la création d'une entité simpleAssuranceRenteViagere
     * 
     * @param simpleAssuranceRenteViagere
     *            L'entité simpleAssuranceRenteViagere à créer
     * @return L'entité simpleAssuranceRenteViagere créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceRenteViagereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAssuranceRenteViagere create(SimpleAssuranceRenteViagere simpleAssuranceRenteViagere)
            throws JadePersistenceException, AssuranceRenteViagereException;

    /**
     * Permet la suppression d'une entité simpleAssuranceRenteViagere
     * 
     * @param simpleAssuranceRenteViagere
     *            L'entité simpleAssuranceRenteViagere à supprimer
     * @return L'entité simpleAssuranceRenteViagere supprimé
     * @throws AssuranceRenteViagereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleAssuranceRenteViagere delete(SimpleAssuranceRenteViagere simpleAssuranceRenteViagere)
            throws AssuranceRenteViagereException, JadePersistenceException;

    /**
     * Permet l'effacement de simpleAssuranceRenteViagere en donnant une liste des id de leurs headerDonneeFinanciere
     * 
     * @param listeIDString
     *            la liste des id des headerDonneeFinanciere pour lesquels il faut effacer les
     *            simpleAssuranceREnteViagere
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité simpleAssuranceRenteViagere
     * 
     * @param idAssuranceRenteViagere
     *            L'identifiant de l'entité simpleAssuranceRenteViagere à charger en mémoire
     * @return L'entité simpleAssuranceRenteViagere chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceRenteViagereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAssuranceRenteViagere read(String idAssuranceRenteViagere) throws JadePersistenceException,
            AssuranceRenteViagereException;

    /**
     * 
     * Permet la mise à jour d'une entité simpleAssuranceRenteViagere
     * 
     * @param simpleAssuranceRenteViagere
     *            L'entité simpleAssuranceRenteViagere à mettre à jour
     * @return L'entité simpleAssuranceRenteViagere mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceRenteViagereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAssuranceRenteViagere update(SimpleAssuranceRenteViagere simpleAssuranceRenteViagere)
            throws JadePersistenceException, AssuranceRenteViagereException;
}
