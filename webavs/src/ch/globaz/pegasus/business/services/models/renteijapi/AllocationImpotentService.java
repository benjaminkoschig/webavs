package ch.globaz.pegasus.business.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AllocationImpotentException;
import ch.globaz.pegasus.business.models.renteijapi.AllocationImpotent;
import ch.globaz.pegasus.business.models.renteijapi.AllocationImpotentSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

/**
 * Interface pour le service complexes des allocations impotents AI 6.2010
 * 
 * @author SCE
 * 
 */
public interface AllocationImpotentService extends JadeApplicationService, AbstractDonneeFinanciereService {

    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     * @return
     * @throws AllocationImpotentException
     * @throws JadePersistenceException
     */
    public int count(AllocationImpotentSearch search) throws AllocationImpotentException, JadePersistenceException;

    /**
     * Permet la création d'une entité allocation impotent.
     * 
     * @param allocationImpotent
     * @return
     * @throws AllocationImpotentException
     * @throws DonneeFinanciereException
     * @throws JadePersistenceException
     */
    public AllocationImpotent create(AllocationImpotent allocationImpotent) throws AllocationImpotentException,
            DonneeFinanciereException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité allocation impotent
     * 
     * @param allocationImpotent
     * @return
     * @throws AllocationImpotentException
     * @throws JadePersistenceException
     */
    public AllocationImpotent delete(AllocationImpotent allocationImpotent) throws AllocationImpotentException,
            JadePersistenceException;

    /**
     * PErmet de charger une entité allocation impotent en mémoire
     * 
     * @param idAllocationImpotent
     * @return
     * @throws AllocationImpotentException
     * @throws JadePersistenceException
     */
    public AllocationImpotent read(String idAllocationImpotent) throws AllocationImpotentException,
            JadePersistenceException;

    /**
     * Chargement d'une allocation impotenet via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AllocationImpotentException
     * @throws JadePersistenceException
     */
    public AllocationImpotent readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws AllocationImpotentException, JadePersistenceException;

    /**
     * Permet la recherche d'après les paramètres de recherche
     * 
     * @param allocationImpotenetSearch
     * @return La recherche effectué
     * @throws AllocationImpotentException
     * @throws DonneeFinanciereException
     * @throws JadePersistenceException
     */
    public AllocationImpotentSearch search(AllocationImpotentSearch allocationImpotentSearch)
            throws AllocationImpotentException, DonneeFinanciereException, JadePersistenceException;

    /**
     * Permet de mettre à jour une entité allocation impotent
     * 
     * @param allocationImpotent
     * @return
     * @throws AllocationImpotentException
     * @throws DonneeFinanciereException
     * @throws JadePersistenceException
     */
    public AllocationImpotent update(AllocationImpotent allocationImpotent) throws AllocationImpotentException,
            DonneeFinanciereException, JadePersistenceException;

}
