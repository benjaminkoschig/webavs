package ch.globaz.pegasus.business.services.models.assurancemaladie;

import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.AssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.PrimeAssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.SubsideAssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.assurancemaladie.PrimeAssuranceMaladie;
import ch.globaz.pegasus.business.models.assurancemaladie.PrimeAssuranceMaladieSearch;
import ch.globaz.pegasus.business.models.assurancemaladie.SubsideAssuranceMaladie;
import ch.globaz.pegasus.business.models.assurancemaladie.SubsideAssuranceMaladieSearch;
import ch.globaz.pegasus.business.models.droit.*;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.util.List;

public interface AssuranceMaladieService extends JadeApplicationService {

    public void copyAssuranceMaladie(Droit newDroit, Droit oldDroit, DroitMembreFamilleSearch droitMembreFamilleSearch)
            throws DonneeFinanciereException;

    public PrimeAssuranceMaladie createPrimeAssuranceMaladie(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille instanceDroitMembreFamille, PrimeAssuranceMaladie primeAssuranceMaladie)
            throws PrimeAssuranceMaladieException, JadePersistenceException, DonneeFinanciereException;

    public PrimeAssuranceMaladie createPrimeAssuranceMaladie(PrimeAssuranceMaladie primeAssuranceMaladie)
            throws PrimeAssuranceMaladieException, JadePersistenceException, DonneeFinanciereException;

    public void deleteParListeIdDoFinH(List<String> idsDonneFinanciere, String typeDonneFinianciere)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException;

    public PrimeAssuranceMaladie readPrimeAssuranceMaladie(String id) throws JadePersistenceException, AssuranceMaladieException;

    public PrimeAssuranceMaladieSearch searchPrimeAssuranceMaladie (PrimeAssuranceMaladieSearch searchModel) throws JadePersistenceException, AssuranceMaladieException;


    public SubsideAssuranceMaladie createSubsideAssuranceMaladie(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille instanceDroitMembreFamille, SubsideAssuranceMaladie subsideAssuranceMaladie)
            throws SubsideAssuranceMaladieException, JadePersistenceException, DonneeFinanciereException;

    public SubsideAssuranceMaladie createSubsideAssuranceMaladie(SubsideAssuranceMaladie subsideAssuranceMaladie)
            throws SubsideAssuranceMaladieException, JadePersistenceException, DonneeFinanciereException;

    public SubsideAssuranceMaladie readSubsideAssuranceMaladie(String id) throws JadePersistenceException, AssuranceMaladieException;

    public SubsideAssuranceMaladieSearch searchSubsideAssuranceMaladie (SubsideAssuranceMaladieSearch searchModel) throws JadePersistenceException, AssuranceMaladieException;

}

