package ch.globaz.pegasus.business.services.models.parametre;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.SimpleForfaitPrimesAssuranceMaladie;
import ch.globaz.pegasus.business.models.parametre.SimpleForfaitPrimesAssuranceMaladieSearch;

public interface SimpleForfaitPrimesAssuranceMaladieService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleForfaitPrimesAssuranceMaladieSearch search) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité simpleForfaitAssMal
     * 
     * @param SimplePrimeFofaiteAssurenceMaladie
     *            La simpleForfaitAssMal métier à créer
     * @return simpleForfaitAssMal créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleForfaitPrimesAssuranceMaladie create(SimpleForfaitPrimesAssuranceMaladie simpleForfaitAssMal)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entité simpleForfaitAssMal
     * 
     * @param SimplePrimeFofaiteAssurenceMaladie
     *            le simpleForfaitPrimesAssuranceMaladie à supprimer
     * @return supprimé
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleForfaitPrimesAssuranceMaladie delete(SimpleForfaitPrimesAssuranceMaladie simpleForfaitAssMal)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException;

    /**
     * Permet de charger en mémoire une simpleForfaitAssMal PC
     * 
     * @param idsimpleForfaitAssMal
     *            L'identifiant simpleForfaitPrimesAssuranceMaladie à charger en mémoire
     * @return SimpleForfaitPrimesAssuranceMaladie chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleForfaitPrimesAssuranceMaladie read(String idSimplePrimeFofaiteAssurenceMaladie)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException;

    /**
     * Permet de chercher des simpleForfaitAssMal selon un modèle de critères.
     * 
     * @param variableMetierSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleForfaitPrimesAssuranceMaladieSearch search(
            SimpleForfaitPrimesAssuranceMaladieSearch simpleForfaitAssMalSearch)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité simpleForfaitAssMal
     * 
     * @param SimpleForfaitPrimesAssuranceMaladie
     *            SimpleForfaitPrimesAssuranceMaladie à mettre à jour
     * @return SimpleForfaitPrimesAssuranceMaladie mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleForfaitPrimesAssuranceMaladie update(SimpleForfaitPrimesAssuranceMaladie simpleForfaitAssMal)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

}