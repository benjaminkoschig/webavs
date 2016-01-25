package ch.globaz.pegasus.businessimpl.services.models.droit;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneesPersonnellesException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.SimpleDonneesPersonnelles;
import ch.globaz.pegasus.business.models.droit.SimpleDroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.SimpleDroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.VersionDroitMembreFamilleSearch;
import ch.globaz.pegasus.business.services.models.droit.DroitMembreFamilleService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * 
 * @author BSC
 * 
 */
public class DroitMembreFamilleServiceImpl extends PegasusAbstractServiceImpl implements DroitMembreFamilleService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.droit.DroitMembreFamilleService
     * #count(ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch)
     */
    @Override
    public int count(DroitMembreFamilleSearch search) throws DroitException, JadePersistenceException {
        if (search == null) {
            throw new DroitException("Unable to count droitMembreFamille, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public void delete(DroitMembreFamille droitMembreFamille) throws DroitException, JadePersistenceException {
        try {
            PegasusImplServiceLocator.getSimpleDroitMembreFamilleService().delete(
                    droitMembreFamille.getSimpleDroitMembreFamille());
            SimpleDonneesPersonnelles simpleDonneesPersonnelles;
            try {
                simpleDonneesPersonnelles = PegasusImplServiceLocator.getSimpleDonneesPersonnellesService().read(
                        droitMembreFamille.getSimpleDroitMembreFamille().getIdDonneesPersonnelles());
                PegasusImplServiceLocator.getSimpleDonneesPersonnellesService().delete(simpleDonneesPersonnelles);
            } catch (DonneesPersonnellesException e) {
                throw new DroitException("Unable to delete the membre famille", e);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Unable to delete, the serivice is down");
        }

    }

    @Override
    public void deleteByIdDroit(String idDroit) throws JadePersistenceException, DroitException,
            JadeApplicationServiceNotAvailableException {

        SimpleDroitMembreFamilleSearch search = new SimpleDroitMembreFamilleSearch();
        search.setForIdDroit(idDroit);
        search = PegasusImplServiceLocator.getSimpleDroitMembreFamilleService().search(search);
        for (JadeAbstractModel dmf : search.getSearchResults()) {
            SimpleDroitMembreFamille simpleDmf = (SimpleDroitMembreFamille) dmf;
            try {
                SimpleDonneesPersonnelles donneesPersonnelles = PegasusImplServiceLocator
                        .getSimpleDonneesPersonnellesService().read(simpleDmf.getIdDonneesPersonnelles());

                PegasusImplServiceLocator.getSimpleDonneesPersonnellesService().delete(donneesPersonnelles);

            } catch (DonneesPersonnellesException e) {
                throw new DroitException("Unable to delete donneesPersonnel", e);
            }
            PegasusImplServiceLocator.getSimpleDroitMembreFamilleService().delete(simpleDmf);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.droit.DroitMembreFamilleService #read(java.lang.String)
     */
    @Override
    public DroitMembreFamille read(String idDroitMembreFamille) throws JadePersistenceException, DroitException {
        if (JadeStringUtil.isEmpty(idDroitMembreFamille)) {
            throw new DroitException("Unable to read droitMembreFamille, the id passed is null!");
        }
        DroitMembreFamille droitMembreFamille = new DroitMembreFamille();
        droitMembreFamille.setId(idDroitMembreFamille);
        return (DroitMembreFamille) JadePersistenceManager.read(droitMembreFamille);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.droit.DroitMembreFamilleService
     * #search(ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch)
     */
    @Override
    public DroitMembreFamilleSearch search(DroitMembreFamilleSearch search) throws JadePersistenceException,
            DroitException {
        if (search == null) {
            throw new DroitException("Unable to search droitMembreFamille, the search model passed is null!");
        }
        return (DroitMembreFamilleSearch) JadePersistenceManager.search(search);
    }

    @Override
    public VersionDroitMembreFamilleSearch search(VersionDroitMembreFamilleSearch search)
            throws JadePersistenceException, DroitException {
        if (search == null) {
            throw new DroitException("Unable to search vserionDroitMembreFamille, the search model passed is null!");
        }
        return (VersionDroitMembreFamilleSearch) JadePersistenceManager.search(search);
    }

}
