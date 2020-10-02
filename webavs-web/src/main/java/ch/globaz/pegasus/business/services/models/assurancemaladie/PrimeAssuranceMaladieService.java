package ch.globaz.pegasus.business.services.models.assurancemaladie;

import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.AssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.PrimeAssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.FraisGardeException;
import ch.globaz.pegasus.business.models.assurancemaladie.AssuranceMaladieSearch;
import ch.globaz.pegasus.business.models.assurancemaladie.PrimeAssuranceMaladie;
import ch.globaz.pegasus.business.models.assurancemaladie.PrimeAssuranceMaladieSearch;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

public interface PrimeAssuranceMaladieService extends JadeApplicationService, AbstractDonneeFinanciereService {


    /**
     * Permet de rechercher des Prime d'assurance maladie selon le modèle de critères
     *
     * @param searchModel
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     * @throws AssuranceMaladieException
     */
    public PrimeAssuranceMaladieSearch search(PrimeAssuranceMaladieSearch searchModel) throws PrimeAssuranceMaladieException,
            JadePersistenceException;

    /**
     *
     * @param primeAssuranceMaladie
     * @return
     * @throws FraisGardeException
     * @throws JadePersistenceException
     * @throws AssuranceMaladieException
     * @throws DonneeFinanciereException
     */
    public PrimeAssuranceMaladie create(PrimeAssuranceMaladie primeAssuranceMaladie)
            throws JadePersistenceException, PrimeAssuranceMaladieException, DonneeFinanciereException;

    /**
     *
     * @param idPrimeAssuranceMaladie
     * @return
     * @throws JadePersistenceException
     * @throws AssuranceMaladieException
     */
    public PrimeAssuranceMaladie read(String idPrimeAssuranceMaladie) throws JadePersistenceException, PrimeAssuranceMaladieException;


    /**
     *
     * @param autresRevenus
     * @return
     * @throws AssuranceMaladieException
     * @throws JadePersistenceException
     */
    public PrimeAssuranceMaladie delete(PrimeAssuranceMaladie autresRevenus) throws PrimeAssuranceMaladieException, JadePersistenceException;

    /**
     *
     * @param primeAssuranceMaladie
     * @return
     * @throws FraisGardeException
     * @throws JadePersistenceException
     * @throws AssuranceMaladieException
     * @throws DonneeFinanciereException
     */
    public PrimeAssuranceMaladie update(PrimeAssuranceMaladie primeAssuranceMaladie)
            throws PrimeAssuranceMaladieException, JadePersistenceException, AssuranceMaladieException, DonneeFinanciereException;

    /**
     *
     * @param search
     * @return
     * @throws PrimeAssuranceMaladieException
     * @throws JadePersistenceException
     */
    public int count(PrimeAssuranceMaladieSearch search) throws PrimeAssuranceMaladieException, JadePersistenceException;

    /**
     *
     * @param idDonneeFinanciereHeader
     * @return
     * @throws PrimeAssuranceMaladieException
     * @throws JadePersistenceException
     */
    public PrimeAssuranceMaladie readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws PrimeAssuranceMaladieException, JadePersistenceException;
}

