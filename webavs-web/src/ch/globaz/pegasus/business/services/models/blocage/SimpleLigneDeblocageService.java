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
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws BlocageException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    @Override
    public int count(SimpleLigneDeblocageSearch search) throws BlocageException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� SimpleDeblocage
     * 
     * @param SimpleLigneDeblocage
     *            La simpleDeblocage � cr�er
     * @return simpleDeblocage cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationException
     */
    @Override
    public SimpleLigneDeblocage create(SimpleLigneDeblocage simpleDeblocage) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Permet la suppression d'une entit� simpleDeblocage
     * 
     * @param SimpleLigneDeblocage
     *            La simpleDeblocage � supprimer
     * @return supprim�
     * @throws BlocageException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    @Override
    public SimpleLigneDeblocage delete(SimpleLigneDeblocage simpleDeblocage) throws BlocageException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire une simpleDeblocage PC
     * 
     * @param idsimpleDeblocage
     *            L'identifiant de simpleDeblocage � charger en m�moire
     * @return simpleDeblocage charg�e en m�moire
     * @throws BlocageException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    @Override
    public SimpleLigneDeblocage read(String idSimpleDeblocage) throws BlocageException, JadePersistenceException;

    /**
     * Permet de chercher des SimpleDeblocage selon un mod�le de crit�res.
     * 
     * @param simpleDeblocageSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws BlocageException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    @Override
    public SimpleLigneDeblocageSearch search(SimpleLigneDeblocageSearch simpleDeblocageSearch) throws BlocageException,
            JadePersistenceException;

    /**
     * Execute une recherche et regroupe les d�blocages par leur type
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
     * Permet la mise � jour d'une entit� SimpleDeblocage
     * 
     * @param SimpleLigneDeblocage
     *            Le modele � mettre � jour
     * @return simpleDeblocage mis � jour
     * 
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationException
     */
    @Override
    public SimpleLigneDeblocage update(SimpleLigneDeblocage simpleDeblocage) throws JadePersistenceException,
            JadeApplicationException;

}
