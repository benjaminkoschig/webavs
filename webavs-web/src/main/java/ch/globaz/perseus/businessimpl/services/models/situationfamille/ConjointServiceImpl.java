package ch.globaz.perseus.businessimpl.services.models.situationfamille;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.Conjoint;
import ch.globaz.perseus.business.models.situationfamille.ConjointSearchModel;
import ch.globaz.perseus.business.models.situationfamille.SimpleConjoint;
import ch.globaz.perseus.business.models.situationfamille.SimpleMembreFamille;
import ch.globaz.perseus.business.services.models.situationfamille.ConjointService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * @author vyj
 */
public class ConjointServiceImpl extends PerseusAbstractServiceImpl implements ConjointService {

    @Override
    public int count(ConjointSearchModel search) throws SituationFamilleException, JadePersistenceException {
        if (search == null) {
            throw new SituationFamilleException("Unable to count conjoints, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.ConjointService#create(ch.globaz.perseus.business.models.conjoint
     * .Conjoint )
     */
    @Override
    public Conjoint create(Conjoint conjoint) throws JadePersistenceException, SituationFamilleException {
        if (conjoint == null) {
            throw new SituationFamilleException("Unable to create conjoint, the given model is null!");
        }

        try {
            SimpleMembreFamille simpleMembreFamille = conjoint.getMembreFamille().getSimpleMembreFamille();
            simpleMembreFamille = PerseusImplServiceLocator.getSimpleMembreFamilleService().createOrRead(
                    simpleMembreFamille);
            conjoint.getMembreFamille().setSimpleMembreFamille(simpleMembreFamille);

            SimpleConjoint simpleConjoint = conjoint.getSimpleConjoint();
            simpleConjoint.setIdMembreFamille(simpleMembreFamille.getId());
            simpleConjoint = PerseusImplServiceLocator.getSimpleConjointService().createOrRead(simpleConjoint);
            conjoint.setSimpleConjoint(simpleConjoint);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SituationFamilleException("Service not available - " + e.getMessage());
        } catch (SituationFamilleException e) {
            throw new SituationFamilleException("SituationFamilleException during creating : " + e.getMessage(), e);
        }

        return conjoint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.ConjointService#delete(ch.globaz.perseus.business.models.conjoint
     * .Conjoint )
     */
    @Override
    public Conjoint delete(Conjoint conjoint) throws JadePersistenceException, SituationFamilleException {
        if (conjoint == null) {
            throw new SituationFamilleException("Unable to delete conjoint, the given model is null!");
        }
        try {
            conjoint.setSimpleConjoint(PerseusImplServiceLocator.getSimpleConjointService().delete(
                    conjoint.getSimpleConjoint()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SituationFamilleException("Service not available - " + e.getMessage());
        }

        return conjoint;
    }

    @Override
    public String getIdConjoint(String idTiers) throws JadePersistenceException, SituationFamilleException {
        if (idTiers == null) {
            throw new SituationFamilleException("Unable to read a conjoint, the id passed is null!");
        }
        ConjointSearchModel searchModel = new ConjointSearchModel();
        searchModel.setForIdTiers(idTiers);
        searchModel = search(searchModel);
        if ((searchModel.getSize() > 1) || (searchModel.getSize() == 0)) {
            return "0";
        } else {
            Conjoint conjoint = (Conjoint) searchModel.getSearchResults()[0];
            return conjoint.getSimpleConjoint().getIdConjoint();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.ConjointService#read(java.lang.String)
     */
    @Override
    public Conjoint read(String idConjoint) throws JadePersistenceException, SituationFamilleException {
        if (JadeStringUtil.isEmpty(idConjoint)) {
            throw new SituationFamilleException("Unable to read a conjoint, the id passed is null!");
        }
        Conjoint conjoint = new Conjoint();
        conjoint.setId(idConjoint);
        return (Conjoint) JadePersistenceManager.read(conjoint);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.perseus.business.services.models.ConjointService#search(ch.globaz.perseus.business.models.conjoint
     * . SearchConjointModel)
     */
    @Override
    public ConjointSearchModel search(ConjointSearchModel searchModel) throws JadePersistenceException,
            SituationFamilleException {
        if (searchModel == null) {
            throw new SituationFamilleException("Unable to search a conjoint, the search model passed is null!");
        }
        return (ConjointSearchModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.ConjointService#update(ch.globaz.perseus.business.models.conjoint
     * .Conjoint )
     */
    @Override
    public Conjoint update(Conjoint conjoint) throws JadePersistenceException, SituationFamilleException {
        throw new SituationFamilleException("No update possible on a conjoint, membreFamille cannot be updated");
    }

}
