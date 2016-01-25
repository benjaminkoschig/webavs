package ch.globaz.pegasus.business.services.models.blocage;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeCrudService;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.EPCTypeDeblocage;
import ch.globaz.pegasus.business.exceptions.models.blocage.BlocageException;
import ch.globaz.pegasus.business.models.blocage.SimpleLigneDeblocage;
import ch.globaz.pegasus.business.models.blocage.SimpleLigneDeblocageSearch;

public interface SimpleLigneDeblocageService extends JadeCrudService<SimpleLigneDeblocage, SimpleLigneDeblocageSearch> {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws BlocageException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    @Override
    public int count(SimpleLigneDeblocageSearch search) throws BlocageException, JadePersistenceException;

    /**
     * Permet la création d'une entité SimpleDeblocage
     * 
     * @param SimpleLigneDeblocage
     *            La simpleDeblocage à créer
     * @return simpleDeblocage créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationException
     */
    @Override
    public SimpleLigneDeblocage create(SimpleLigneDeblocage simpleDeblocage) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Permet la suppression d'une entité simpleDeblocage
     * 
     * @param SimpleLigneDeblocage
     *            La simpleDeblocage à supprimer
     * @return supprimé
     * @throws BlocageException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    @Override
    public SimpleLigneDeblocage delete(SimpleLigneDeblocage simpleDeblocage) throws BlocageException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire une simpleDeblocage PC
     * 
     * @param idsimpleDeblocage
     *            L'identifiant de simpleDeblocage à charger en mémoire
     * @return simpleDeblocage chargée en mémoire
     * @throws BlocageException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    @Override
    public SimpleLigneDeblocage read(String idSimpleDeblocage) throws BlocageException, JadePersistenceException;

    /**
     * Permet de chercher des SimpleDeblocage selon un modèle de critères.
     * 
     * @param simpleDeblocageSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws BlocageException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    @Override
    public SimpleLigneDeblocageSearch search(SimpleLigneDeblocageSearch simpleDeblocageSearch) throws BlocageException,
            JadePersistenceException;

    /**
     * Execute une recherche et regroupe les déblocages par leur type
     * 
     * @param simpleDeblocageSearch
     * @return
     * @throws BlocageException
     * @throws JadePersistenceException
     */
    public Map<EPCTypeDeblocage, List<SimpleLigneDeblocage>> searchAndGroupByCsTypeDeblocage(
            SimpleLigneDeblocageSearch simpleDeblocageSearch) throws BlocageException, JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleDeblocage
     * 
     * @param SimpleLigneDeblocage
     *            Le modele à mettre à jour
     * @return simpleDeblocage mis à jour
     * 
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationException
     */
    @Override
    public SimpleLigneDeblocage update(SimpleLigneDeblocage simpleDeblocage) throws JadePersistenceException,
            JadeApplicationException;

}
