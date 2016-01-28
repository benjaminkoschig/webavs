/**
 * 
 */
package ch.globaz.al.businessimpl.services.models.droit;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSPays;
import ch.globaz.al.business.exceptions.model.droit.ALEnfantModelException;
import ch.globaz.al.business.models.droit.EnfantModel;
import ch.globaz.al.business.models.droit.EnfantSearchModel;
import ch.globaz.al.business.services.models.droit.EnfantModelService;
import ch.globaz.al.businessimpl.checker.model.droit.EnfantModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Classe d'implémentation des services de EnfantModel
 * 
 * @author PTA
 * 
 */
public class EnfantModelServiceImpl extends ALAbstractBusinessServiceImpl implements EnfantModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.EnfantModelService#count(
     * ch.globaz.al.business.models.droit.EnfantSearch)
     */
    @Override
    public int count(EnfantSearchModel enfantSearch) throws JadePersistenceException, JadeApplicationException {

        if (enfantSearch == null) {
            throw new ALEnfantModelException("EnfantModelServiceImpl#count : enfantSearch is null");
        }

        return JadePersistenceManager.count(enfantSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.droit.EnfantModelService#create(ch.globaz
     * .al.business.model.droit.EnfantModel)
     */
    @Override
    public EnfantModel create(EnfantModel enfantModel) throws JadeApplicationException, JadePersistenceException {
        if (enfantModel == null) {
            throw new ALEnfantModelException("Unable to create the EnfantModel-the id passed is null");

        }
        // contôle la validité des données
        EnfantModelChecker.validate(enfantModel);
        // ajoute l'enfant dans la persistance et le retourne
        return (EnfantModel) JadePersistenceManager.add(enfantModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.droit.EnfantModelService#delete(ch.globaz
     * .al.business.model.droit.EnfantModel)
     */
    @Override
    public EnfantModel delete(EnfantModel enfantModel) throws JadeApplicationException, JadePersistenceException {
        if (enfantModel == null) {
            throw new ALEnfantModelException("unable to delete the enfantModel-the model passed is null");
        }
        if (enfantModel.isNew()) {
            throw new ALEnfantModelException("unable to delete th enfantModel-th model passed is null");
        }

        // vérification
        EnfantModelChecker.validateForDelete(enfantModel);

        // suppression
        return (EnfantModel) JadePersistenceManager.delete(enfantModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.EnfantModelService#initModel
     * (ch.globaz.al.business.models.droit.EnfantModel)
     */
    @Override
    public EnfantModel initModel(EnfantModel enfantModel) throws JadeApplicationException, JadePersistenceException {
        if (enfantModel == null) {
            throw new ALEnfantModelException("Unable to init the enfantModel-the model passed is null");
        }
        enfantModel.setCapableExercer(true);
        enfantModel.setTypeAllocationNaissance(ALCSDroit.NAISSANCE_TYPE_AUCUNE);
        enfantModel.setAllocationNaissanceVersee(Boolean.FALSE);
        enfantModel.setIdPaysResidence(ALCSPays.PAYS_SUISSE);
        enfantModel.setMontantAllocationNaissanceFixe("");

        return enfantModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.droit.EnfantModelService#read(java.lang .String)
     */
    @Override
    public EnfantModel read(String idEnfantModel) throws JadeApplicationException, JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idEnfantModel)) {
            throw new ALEnfantModelException("Unable to read EnfantModel-the idEnfant passed is empty");
        }
        EnfantModel enfantModel = new EnfantModel();
        enfantModel.setId(idEnfantModel);

        return (EnfantModel) JadePersistenceManager.read(enfantModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.droit.EnfantModelService#update(ch.globaz
     * .al.business.model.droit.EnfantModel)
     */
    @Override
    public EnfantModel update(EnfantModel enfantModel) throws JadeApplicationException, JadePersistenceException {
        if (enfantModel == null) {
            throw new ALEnfantModelException("Unable to update the enfantModel-the model passed is null");
        }
        if (enfantModel.isNew()) {
            throw new ALEnfantModelException("Unable to update the enfantModel-the model passed is null");
        }
        // contrôle la validité des données
        EnfantModelChecker.validate(enfantModel);
        // ajoute l'enfantModel à modifier dans la persistance et le retourne
        return (EnfantModel) JadePersistenceManager.update(enfantModel);
    }

}
