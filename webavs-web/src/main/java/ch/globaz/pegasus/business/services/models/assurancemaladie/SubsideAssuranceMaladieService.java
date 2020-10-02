package ch.globaz.pegasus.business.services.models.assurancemaladie;

import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.AssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.SubsideAssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.FraisGardeException;
import ch.globaz.pegasus.business.models.assurancemaladie.SubsideAssuranceMaladie;
import ch.globaz.pegasus.business.models.assurancemaladie.SubsideAssuranceMaladieSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

public interface SubsideAssuranceMaladieService extends JadeApplicationService, AbstractDonneeFinanciereService {


    /**
     * Permet de rechercher des Subside d'assurance maladie selon le modèle de critères
     *
     * @param searchModel
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     * @throws AssuranceMaladieException
     */
    public SubsideAssuranceMaladieSearch search(SubsideAssuranceMaladieSearch searchModel) throws SubsideAssuranceMaladieException,
            JadePersistenceException;

    /**
     *
     * @param subsideAssuranceMaladie
     * @return
     * @throws
     * @throws JadePersistenceException
     * @throws AssuranceMaladieException
     * @throws DonneeFinanciereException
     */
    public SubsideAssuranceMaladie create(SubsideAssuranceMaladie subsideAssuranceMaladie)
            throws JadePersistenceException, SubsideAssuranceMaladieException, DonneeFinanciereException;

    /**
     *
     * @param idSubsideAssuranceMaladie
     * @return
     * @throws JadePersistenceException
     * @throws AssuranceMaladieException
     */
    public SubsideAssuranceMaladie read(String idSubsideAssuranceMaladie) throws JadePersistenceException, SubsideAssuranceMaladieException;


    /**
     *
     * @param autresRevenus
     * @return
     * @throws AssuranceMaladieException
     * @throws JadePersistenceException
     */
    public SubsideAssuranceMaladie delete(SubsideAssuranceMaladie autresRevenus) throws SubsideAssuranceMaladieException, JadePersistenceException;

    /**
     *
     * @param subsideAssuranceMaladie
     * @return
     * @throws FraisGardeException
     * @throws JadePersistenceException
     * @throws AssuranceMaladieException
     * @throws DonneeFinanciereException
     */
    public SubsideAssuranceMaladie update(SubsideAssuranceMaladie subsideAssuranceMaladie)
            throws SubsideAssuranceMaladieException, JadePersistenceException, AssuranceMaladieException, DonneeFinanciereException;

    /**
     *
     * @param search
     * @return
     * @throws SubsideAssuranceMaladieException
     * @throws JadePersistenceException
     */
    public int count(SubsideAssuranceMaladieSearch search) throws SubsideAssuranceMaladieException, JadePersistenceException;

    /**
     *
     * @param idDonneeFinanciereHeader
     * @return
     * @throws SubsideAssuranceMaladieException
     * @throws JadePersistenceException
     */
    public SubsideAssuranceMaladie readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws SubsideAssuranceMaladieException, JadePersistenceException;
}

