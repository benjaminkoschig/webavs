package ch.globaz.pegasus.businessimpl.services.models.creancier;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordee;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordeeSearch;
import ch.globaz.pegasus.business.models.creancier.SimpleCreanceAccordeeSearch;
import ch.globaz.pegasus.business.services.models.creancier.CreanceAccordeeService;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class CreanceAccordeeServiceImpl implements CreanceAccordeeService {

    private BigDecimal computeCreanceVerseForCreancier(String idDemande, String idVersionDroit, String idCreancier)
            throws CreancierException, JadePersistenceException {

        if ((idDemande == null) || (idCreancier == null)) {
            throw new CreancierException(
                    "Unable to find totalCreanceVerser, the idDemande, or idcrenacier passed is/was null!");
        }

        CreanceAccordeeSearch creanceAccordeeSearch = new CreanceAccordeeSearch();
        creanceAccordeeSearch.setForIdCreancier(idCreancier);
        creanceAccordeeSearch.setForIdDemande(idDemande);
        creanceAccordeeSearch.setForIdVersionDroit(idVersionDroit);
        creanceAccordeeSearch = (CreanceAccordeeSearch) JadePersistenceManager.search(creanceAccordeeSearch);

        BigDecimal montantRembourse = new BigDecimal(0);

        for (JadeAbstractModel creance : creanceAccordeeSearch.getSearchResults()) {
            montantRembourse = montantRembourse.add(new BigDecimal(((CreanceAccordee) creance)
                    .getSimpleCreanceAccordee().getMontant()));// Float.parseFloat(((CreanceAccordee)
                                                               // creance).getSimpleCreanceAccordee().getMontant());
        }
        return montantRembourse;
    }

    @Override
    public int count(CreanceAccordeeSearch search) throws CreancierException, JadePersistenceException {
        if (search == null) {
            throw new CreancierException("Unable to count search, the model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public CreanceAccordee create(CreanceAccordee creanceAccordee) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        if (creanceAccordee == null) {
            throw new CreancierException("Unable to create creanceAccordee, the model passed is null!");
        }
        creanceAccordee.setSimpleCreanceAccordee(PegasusImplServiceLocator.getSimpleCreanceAccordeeService().create(
                creanceAccordee.getSimpleCreanceAccordee()));
        return creanceAccordee;
    }

    @Override
    public CreanceAccordee delete(CreanceAccordee creanceAccordee) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        if (creanceAccordee == null) {
            throw new CreancierException("Unable to delete creanceAccordee, the model passed is null!");
        }
        creanceAccordee.setSimpleCreanceAccordee(PegasusImplServiceLocator.getSimpleCreanceAccordeeService().delete(
                creanceAccordee.getSimpleCreanceAccordee()));
        return creanceAccordee;
    }

    @Override
    public int deleteWithSearchModele(SimpleCreanceAccordeeSearch simpleCreanceAccordeeSearch)
            throws JadePersistenceException, CreancierException {
        if (simpleCreanceAccordeeSearch == null) {
            throw new CreancierException("Unable to delete creanceAccordee, the model passed is null!");
        }

        return JadePersistenceManager.delete(simpleCreanceAccordeeSearch);
    }

    @Override
    public BigDecimal findTotalCreanceVerseByDemandeForCreancier(String idDemande, String idCreancier)
            throws JadePersistenceException, CreancierException {
        return computeCreanceVerseForCreancier(idDemande, null, idCreancier);

    }

    @Override
    public BigDecimal findTotalCreanceVerseByVersionDroitForCreancier(String idDemande, String idVersionDroit,
            String idCreancier) throws JadePersistenceException, CreancierException {
        if (idVersionDroit == null) {
            throw new CreancierException(
                    "Unable to find totalCreanceVerser for vesrion Droit, the idVersionDroit,  is null!");
        }
        return computeCreanceVerseForCreancier(idDemande, idVersionDroit, idCreancier);

    }

    @Override
    public CreanceAccordee read(String idCreanceAccordee) throws CreancierException, JadePersistenceException {
        if (idCreanceAccordee == null) {
            throw new CreancierException("Unable to read idCreanceAccordee, the model passed is null!");
        }
        CreanceAccordee creanceAccordee = new CreanceAccordee();
        creanceAccordee.setId(idCreanceAccordee);
        return (CreanceAccordee) JadePersistenceManager.read(creanceAccordee);

    }

    @Override
    public CreanceAccordeeSearch search(CreanceAccordeeSearch creanceAccordeeSearch) throws CreancierException,
            JadePersistenceException {
        if (creanceAccordeeSearch == null) {
            throw new CreancierException("Unable to search creanceAccordeeSearch, the model passed is null!");
        }
        return (CreanceAccordeeSearch) JadePersistenceManager.search(creanceAccordeeSearch);
    }

    @Override
    public CreanceAccordee update(CreanceAccordee creanceAccordee) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        if (creanceAccordee == null) {
            throw new CreancierException("Unable to update creanceAccordee, the model passed is null!");
        }
        creanceAccordee.setSimpleCreanceAccordee(PegasusImplServiceLocator.getSimpleCreanceAccordeeService().update(
                creanceAccordee.getSimpleCreanceAccordee()));
        return creanceAccordee;
    }

}
