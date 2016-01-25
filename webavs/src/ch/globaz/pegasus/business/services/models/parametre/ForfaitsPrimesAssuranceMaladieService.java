package ch.globaz.pegasus.business.services.models.parametre;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.ForfaitsPrimesAssuranceMaladie;
import ch.globaz.pegasus.business.models.parametre.ForfaitsPrimesAssuranceMaladieSearch;

public interface ForfaitsPrimesAssuranceMaladieService extends JadeApplicationService {
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
    public int count(ForfaitsPrimesAssuranceMaladieSearch search) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité ForfaitsPrimesAssuranceMaladie
     * 
     * @param ForfaitsPrimesAssuranceMaladie
     *            Lr forfaitsPrimesAssuranceMaladie à créer
     * @return forfaitsPrimesAssuranceMaladie créé
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public ForfaitsPrimesAssuranceMaladie create(ForfaitsPrimesAssuranceMaladie forfaitsPrimesAssuranceMaladie)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entité forfaitsPrimesAssuranceMaladie
     * 
     * @param ForfaitsPrimesAssuranceMaladie
     *            La forfaitsPrimesAssuranceMaladie métier à supprimer
     * @return supprimé
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public ForfaitsPrimesAssuranceMaladie delete(ForfaitsPrimesAssuranceMaladie forfaitsPrimesAssuranceMaladie)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en mémoire une forfaitsPrimesAssuranceMaladie PC
     * 
     * @param idforfaitsPrimesAssuranceMaladie
     *            L'identifiant de forfaitsPrimesAssuranceMaladie à charger en mémoire
     * @return forfaitsPrimesAssuranceMaladie chargée en mémoire
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public ForfaitsPrimesAssuranceMaladie read(String idForfaitsPrimesAssuranceMaladie)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException;

    /**
     * Permet de chercher des ForfaitsPrimesAssuranceMaladie selon un modèle de critères.
     * 
     * @param forfaitsPrimesAssuranceMaladieSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public ForfaitsPrimesAssuranceMaladieSearch search(
            ForfaitsPrimesAssuranceMaladieSearch forfaitsPrimesAssuranceMaladieSearch)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité ForfaitsPrimesAssuranceMaladie
     * 
     * @param ForfaitsPrimesAssuranceMaladie
     *            Le modele à mettre à jour
     * @return forfaitsPrimesAssuranceMaladie mis à jour
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public ForfaitsPrimesAssuranceMaladie update(ForfaitsPrimesAssuranceMaladie forfaitsPrimesAssuranceMaladie)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

}