package ch.globaz.perseus.business.services.models.impotsource;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.impotsource.TauxException;
import ch.globaz.perseus.business.models.impotsource.SimpleBareme;
import ch.globaz.perseus.business.models.impotsource.SimpleBaremeSearchModel;

public interface SimpleBaremeService extends JadeApplicationService {

    /**
     * Permet la création d'une entité simpleBareme
     * 
     * @param simpleBareme
     *            simpleBareme créer
     * @return simpleBareme créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TauxException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleBareme create(SimpleBareme simpleBareme) throws JadePersistenceException, TauxException;

    /**
     * Permet la suppression d'une entité simpleBareme
     * 
     * @param simpleBareme
     *            simpleBareme à supprimer
     * @return simpleBareme supprimé
     * @throws TauxException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleBareme delete(SimpleBareme simpleBareme) throws JadePersistenceException, TauxException;

    /**
     * Permet de charger en mémoire une SimpleBareme
     * 
     * @param idBareme
     *            L'identifiant SimpleBareme à charger en mémoire
     * @return SimpleBareme chargé en mémoire
     * @throws TauxException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleBareme read(String idBareme) throws JadePersistenceException, TauxException;

    /**
     * Permet de chercher des SimpleBareme selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TauxException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleBaremeSearchModel search(SimpleBaremeSearchModel searchModel) throws JadePersistenceException,
            TauxException;

    /**
     * Permet la mise à jour d'une entité SimpleBareme
     * 
     * @param simpleBareme
     *            simpleBareme à mettre à jour
     * @return simpleBareme mis à jour
     * @throws TauxException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleBareme update(SimpleBareme simpleBareme) throws JadePersistenceException, TauxException;

}
