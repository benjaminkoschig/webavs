package ch.globaz.pegasus.businessimpl.services.models.droit;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneesPersonnellesException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.DonneesPersonnelles;
import ch.globaz.pegasus.business.models.droit.DonneesPersonnellesSearch;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.SimpleDonneesPersonnelles;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.droit.DonneesPersonnellesService;
import ch.globaz.pegasus.businessimpl.checkers.droit.DonneesPersonnellesChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.droit.DonneeFinanciereUtils;

/**
 * @author BSC
 */
public class DonneesPersonnellesServiceImpl extends PegasusAbstractServiceImpl implements DonneesPersonnellesService {

    @Override
    public void copyAllDonneesPersonelleByDroit(Droit newDroit, Droit oldDroit,
            DroitMembreFamilleSearch newdroitMembreFamilleSearch) throws DonneesPersonnellesException,
            JadePersistenceException {
        DonneesPersonnellesSearch search = new DonneesPersonnellesSearch();
        search.setForIdDroit(oldDroit.getSimpleDroit().getIdDroit());
        search.setWhereKey(DonneesPersonnellesSearch.FOR_DROIT);
        search = search(search);
        DroitMembreFamille droitMembreFamille = null;
        SimpleDonneesPersonnelles simpleDonneesPersonnelles = null;
        for (JadeAbstractModel model : search.getSearchResults()) {
            DonneesPersonnelles dpPersonnelles = (DonneesPersonnelles) model;

            droitMembreFamille = DonneeFinanciereUtils.findTheDroitMembreFamilleInTheSarchModle(
                    newdroitMembreFamilleSearch, dpPersonnelles.getDroitMbrFam().getIdMembreFamilleSF());

            try {
                if (droitMembreFamille != null) {
                    simpleDonneesPersonnelles = (SimpleDonneesPersonnelles) JadePersistenceUtil.clone(dpPersonnelles
                            .getSimpleDonneesPersonnelles());

                    dpPersonnelles.setSimpleDonneesPersonnelles(PegasusImplServiceLocator
                            .getSimpleDonneesPersonnellesService().create(simpleDonneesPersonnelles));

                    droitMembreFamille.getSimpleDroitMembreFamille().setIdDonneesPersonnelles(
                            dpPersonnelles.getSimpleDonneesPersonnelles().getIdDonneesPersonnelles());

                    PegasusImplServiceLocator.getSimpleDroitMembreFamilleService().update(
                            droitMembreFamille.getSimpleDroitMembreFamille());
                }
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new DonneesPersonnellesException("Service not available - " + e.getMessage());
            } catch (JadeCloneModelException e) {
                throw new DonneesPersonnellesException("Unabale to copy the donneesPersonnel,pb with the clone", e);
            } catch (DroitException e) {
                throw new DonneesPersonnellesException("Unabale to copy the donneesPersonnel", e);
            }
            droitMembreFamille = null;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.droit.DonneesPersonnellesService
     * #create(ch.globaz.pegasus.business.models.droit.DonneesPersonnelles)
     */
    @Override
    public DonneesPersonnelles create(DonneesPersonnelles donneesPersonnelles) throws JadePersistenceException,
            DonneesPersonnellesException {

        if (donneesPersonnelles == null) {
            throw new DonneesPersonnellesException("Unable to create donneesPersonnelles, the given model is null!");
        }

        try {
            DonneesPersonnellesChecker.checkForCreate(donneesPersonnelles);

            // creation du simpleDonneesPersonnelles
            PegasusImplServiceLocator.getSimpleDonneesPersonnellesService().create(
                    donneesPersonnelles.getSimpleDonneesPersonnelles());

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneesPersonnellesException("Service not available - " + e.getMessage());
        }

        return donneesPersonnelles;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.droit.DonneesPersonnellesService
     * #delete(ch.globaz.pegasus.business.models.droit.DonneesPersonnelles)
     */
    @Override
    public DonneesPersonnelles delete(DonneesPersonnelles donneesPersonnelles) throws DonneesPersonnellesException,
            JadePersistenceException {
        if (donneesPersonnelles == null) {
            throw new DonneesPersonnellesException("Unable to delete donneesPersonnelles, the given model is null!");
        }
        try {

            DonneesPersonnellesChecker.checkForDelete(donneesPersonnelles);

            // on effece uniquement le simpleDonneesPersonnelles
            donneesPersonnelles.setSimpleDonneesPersonnelles(PegasusImplServiceLocator
                    .getSimpleDonneesPersonnellesService().delete(donneesPersonnelles.getSimpleDonneesPersonnelles()));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneesPersonnellesException("Service not available - " + e.getMessage());
        }

        return donneesPersonnelles;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.droit.DonneesPersonnellesService #read(java.lang.String)
     */
    @Override
    public DonneesPersonnelles read(String idDonneesPersonnelles) throws JadePersistenceException,
            DonneesPersonnellesException {
        if (JadeStringUtil.isEmpty(idDonneesPersonnelles)) {
            throw new DonneesPersonnellesException("Unable to read donneesPersonnelles, the id passed is null!");
        }
        DonneesPersonnelles donneesPersonnelles = new DonneesPersonnelles();
        donneesPersonnelles.setId(idDonneesPersonnelles);
        return (DonneesPersonnelles) JadePersistenceManager.read(donneesPersonnelles);
    }

    @Override
    public DonneesPersonnellesSearch search(DonneesPersonnellesSearch search) throws DonneesPersonnellesException,
            JadePersistenceException {
        if (search == null) {
            throw new DonneesPersonnellesException("Unable to search search, the model passed is null!");
        }
        return (DonneesPersonnellesSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.droit.DonneesPersonnellesService
     * #update(ch.globaz.pegasus.business.models.droit.DonneesPersonnelles)
     */
    @Override
    public DonneesPersonnelles update(DonneesPersonnelles donneesPersonnelles) throws JadePersistenceException,
            DonneesPersonnellesException {

        if (donneesPersonnelles == null) {
            throw new DonneesPersonnellesException("Unable to update donneesPersonnelles, the given model is null!");
        }

        try {
            DonneesPersonnellesChecker.checkForUpdate(donneesPersonnelles);
            Droit droit;
            try {
                droit = PegasusServiceLocator.getDroitService().getCurrentVersionDroit(
                        donneesPersonnelles.getDroitMbrFam().getIdDroit());
            } catch (DroitException e) {
                throw new DonneesPersonnellesException("Unable to find the curent version for the droit", e);
            }
            try {
                PegasusServiceLocator.getDroitService().processOnUpdateDonneFinanciere(droit);
            } catch (JadeApplicationException e) {
                throw new DonneesPersonnellesException("Unable to deleteByIdVersionDroit", e);
            }

            // la mise a jour ne se fait que sur le simpleDonneesPersonnelles
            PegasusImplServiceLocator.getSimpleDonneesPersonnellesService().update(
                    donneesPersonnelles.getSimpleDonneesPersonnelles());

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneesPersonnellesException("Service not available - " + e.getMessage());
        }

        return donneesPersonnelles;
    }
}
