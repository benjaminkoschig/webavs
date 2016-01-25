package ch.globaz.perseus.businessimpl.services.models.situationfamille;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.Enfant;
import ch.globaz.perseus.business.models.situationfamille.EnfantSearchModel;
import ch.globaz.perseus.business.models.situationfamille.SimpleEnfant;
import ch.globaz.perseus.business.models.situationfamille.SimpleMembreFamille;
import ch.globaz.perseus.business.services.models.situationfamille.EnfantService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * @author vyj
 */
public class EnfantServiceImpl extends PerseusAbstractServiceImpl implements EnfantService {

    @Override
    public int count(EnfantSearchModel search) throws SituationFamilleException, JadePersistenceException {
        if (search == null) {
            throw new SituationFamilleException("Unable to count enfants, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.EnfantService#create(ch.globaz.perseus.business.models.enfant
     * .Enfant )
     */
    @Override
    public Enfant create(Enfant enfant) throws JadePersistenceException, SituationFamilleException {
        if (enfant == null) {
            throw new SituationFamilleException("Unable to create enfant, the given model is null!");
        }

        try {
            SimpleMembreFamille simpleMembreFamille = enfant.getMembreFamille().getSimpleMembreFamille();
            simpleMembreFamille = PerseusImplServiceLocator.getSimpleMembreFamilleService().createOrRead(
                    simpleMembreFamille);
            enfant.getMembreFamille().setSimpleMembreFamille(simpleMembreFamille);

            SimpleEnfant simpleEnfant = enfant.getSimpleEnfant();
            simpleEnfant.setIdMembreFamille(simpleMembreFamille.getId());
            simpleEnfant = PerseusImplServiceLocator.getSimpleEnfantService().createOrRead(simpleEnfant);
            enfant.setSimpleEnfant(simpleEnfant);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SituationFamilleException("Service not available - " + e.getMessage());
        } catch (SituationFamilleException e) {
            throw new SituationFamilleException("SituationFamilleException during creating : " + e.getMessage(), e);
        }

        return enfant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.EnfantService#delete(ch.globaz.perseus.business.models.enfant
     * .Enfant )
     */
    @Override
    public Enfant delete(Enfant enfant) throws JadePersistenceException, SituationFamilleException {
        if (enfant == null) {
            throw new SituationFamilleException("Unable to delete enfant, the given model is null!");
        }
        try {
            enfant.setSimpleEnfant(PerseusImplServiceLocator.getSimpleEnfantService().delete(enfant.getSimpleEnfant()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SituationFamilleException("Service not available - " + e.getMessage());
        }

        return enfant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.EnfantService#read(java.lang.String)
     */
    @Override
    public Enfant read(String idEnfant) throws JadePersistenceException, SituationFamilleException {
        if (JadeStringUtil.isEmpty(idEnfant)) {
            throw new SituationFamilleException("Unable to read a enfant, the id passed is null!");
        }
        Enfant enfant = new Enfant();
        enfant.setId(idEnfant);
        return (Enfant) JadePersistenceManager.read(enfant);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.perseus.business.services.models.EnfantService#search(ch.globaz.perseus.business.models.enfant .
     * SearchEnfantModel)
     */
    @Override
    public EnfantSearchModel search(EnfantSearchModel searchModel) throws JadePersistenceException,
            SituationFamilleException {
        if (searchModel == null) {
            throw new SituationFamilleException("Unable to search a enfant, the search model passed is null!");
        }
        return (EnfantSearchModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.EnfantService#update(ch.globaz.perseus.business.models.enfant
     * .Enfant )
     */
    @Override
    public Enfant update(Enfant enfant) throws JadePersistenceException, SituationFamilleException {
        throw new SituationFamilleException("No update possible on a enfant, membreFamille cannot be updated");
    }

}
