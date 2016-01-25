/**
 * 
 */
package ch.globaz.al.businessimpl.services.models.envoi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.envoi.ALEnvoiTemplateException;
import ch.globaz.al.business.models.envoi.EnvoiTemplateComplexModel;
import ch.globaz.al.business.models.envoi.EnvoiTemplateComplexModelSearch;
import ch.globaz.al.business.models.envoi.EnvoiTemplateSimpleModel;
import ch.globaz.al.business.services.models.envoi.EnvoiTemplateComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleList;
import ch.globaz.envoi.business.services.ENServiceLocator;

/**
 * @author dhi
 * 
 */
public class EnvoiTemplateComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        EnvoiTemplateComplexModelService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiTemplateComplexModelService#count(ch.globaz.al.business.models
     * .envoi.EnvoiTemplateComplexModelSearch)
     */
    @Override
    public int count(EnvoiTemplateComplexModelSearch envoiTemplateSearch) throws JadePersistenceException,
            JadeApplicationException {

        if (envoiTemplateSearch == null) {
            throw new ALEnvoiTemplateException(
                    "Unable to count for results, the envoiTemplateSearch passed as parameter is null");
        }

        return JadePersistenceManager.count(envoiTemplateSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiTemplateComplexModelService#create(ch.globaz.al.business.models
     * .envoi.EnvoiTemplateComplexModel)
     */
    @Override
    public EnvoiTemplateComplexModel create(EnvoiTemplateComplexModel envoiTemplate) throws JadeApplicationException,
            JadePersistenceException {

        if (envoiTemplate == null) {
            throw new ALEnvoiTemplateException(
                    "Unable to create an EnvoiTemplateComplexModel, the model passed as parameter is null");
        } else if (envoiTemplate.getFormuleList() == null) {
            throw new ALEnvoiTemplateException(
                    "Unable to create an EnvoiTemplateComplexModel.formuleList, the model passed as parameter is null");
        } else if (envoiTemplate.getEnvoiTemplateSimpleModel() == null) {
            throw new ALEnvoiTemplateException(
                    "Unable to create an EnvoiTemplateComplexModel.envoiTemplateSimpleModel, the model passed as parameter is null");
        }
        // Create the formuleList
        FormuleList newFormuleList = ENServiceLocator.getFormuleListService().create(envoiTemplate.getFormuleList());
        envoiTemplate.getEnvoiTemplateSimpleModel().setIdFormule(newFormuleList.getFormule().getId());
        // Create the EnvoiTemplateSimpleModel
        EnvoiTemplateSimpleModel newEnvoiTemplate = ALImplServiceLocator.getEnvoiTemplateSimpleModelService().create(
                envoiTemplate.getEnvoiTemplateSimpleModel());
        // Affectation des créations
        envoiTemplate.setEnvoiTemplateSimpleModel(newEnvoiTemplate);
        envoiTemplate.setFormuleList(newFormuleList);
        return envoiTemplate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiTemplateComplexModelService#delete(ch.globaz.al.business.models
     * .envoi.EnvoiTemplateComplexModel)
     */
    @Override
    public EnvoiTemplateComplexModel delete(EnvoiTemplateComplexModel envoiTemplate) throws JadeApplicationException,
            JadePersistenceException {

        if (envoiTemplate == null) {
            throw new ALEnvoiTemplateException(
                    "Unable to delete an EnvoiTemplateComplexModel, the model passed as parameter is null");
        } else if (envoiTemplate.getFormuleList() == null) {
            throw new ALEnvoiTemplateException(
                    "Unable to delete an EnvoiTemplateComplexModel.formuleList, the model passed as parameter is null");
        } else if (envoiTemplate.getEnvoiTemplateSimpleModel() == null) {
            throw new ALEnvoiTemplateException(
                    "Unable to delete an EnvoiTemplateComplexModel.envoiTemplateSimpleModel, the model passed as parameter is null");
        }
        // Delete the formuleList
        FormuleList deletedFormuleList = ENServiceLocator.getFormuleListService()
                .delete(envoiTemplate.getFormuleList());
        // Delete the EnvoiTemplateSimpleModel
        EnvoiTemplateSimpleModel deletedEnvoiTemplate = ALImplServiceLocator.getEnvoiTemplateSimpleModelService()
                .delete(envoiTemplate.getEnvoiTemplateSimpleModel());
        // Affectation des suppressions
        envoiTemplate.setEnvoiTemplateSimpleModel(deletedEnvoiTemplate);
        envoiTemplate.setFormuleList(deletedFormuleList);
        return envoiTemplate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.envoi.EnvoiTemplateComplexModelService#read(java.lang.String)
     */
    @Override
    public EnvoiTemplateComplexModel read(String idEnvoiTemplateComplexModel) throws JadeApplicationException,
            JadePersistenceException {

        if (JadeStringUtil.isEmpty(idEnvoiTemplateComplexModel)) {
            throw new ALEnvoiTemplateException(
                    "Unable to read an EnvoiTemplateComplexModel, the id passed as parameter is empty");
        }
        // NEW
        EnvoiTemplateComplexModel envoiTemplate = new EnvoiTemplateComplexModel();
        envoiTemplate.setId(idEnvoiTemplateComplexModel);
        // READ SIMPLE MODEL
        EnvoiTemplateSimpleModel readEnvoiTemplateSimple = ALImplServiceLocator.getEnvoiTemplateSimpleModelService()
                .read(envoiTemplate.getEnvoiTemplateSimpleModel().getId());
        envoiTemplate.setEnvoiTemplateSimpleModel(readEnvoiTemplateSimple);
        // READ FORMULE LIST
        // TODO : ADAPT THE SERVICE ! read doesn't exist !
        return envoiTemplate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiTemplateComplexModelService#search(ch.globaz.al.business.models
     * .envoi.EnvoiTemplateComplexModelSearch)
     */
    @Override
    public EnvoiTemplateComplexModelSearch search(EnvoiTemplateComplexModelSearch envoiTemplateSearch)
            throws JadeApplicationException, JadePersistenceException {

        if (envoiTemplateSearch == null) {
            throw new ALEnvoiTemplateException(
                    "Unable to search for results, the envoiTemplateSearch passed as parameter is null");
        }

        return (EnvoiTemplateComplexModelSearch) JadePersistenceManager.search(envoiTemplateSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiTemplateComplexModelService#update(ch.globaz.al.business.models
     * .envoi.EnvoiTemplateComplexModel)
     */
    @Override
    public EnvoiTemplateComplexModel update(EnvoiTemplateComplexModel envoiTemplate) throws JadeApplicationException,
            JadePersistenceException {

        if (envoiTemplate == null) {
            throw new ALEnvoiTemplateException(
                    "Unable to update an EnvoiTemplateComplexModel, the model passed as parameter is null");
        } else if (envoiTemplate.getFormuleList() == null) {
            throw new ALEnvoiTemplateException(
                    "Unable to update an EnvoiTemplateComplexModel.formuleList, the model passed as parameter is null");
        } else if (envoiTemplate.getEnvoiTemplateSimpleModel() == null) {
            throw new ALEnvoiTemplateException(
                    "Unable to update an EnvoiTemplateComplexModel.envoiTemplateSimpleModel, the model passed as parameter is null");
        }
        // Update the formuleList
        FormuleList newFormuleList = ENServiceLocator.getFormuleListService().update(envoiTemplate.getFormuleList());
        envoiTemplate.getEnvoiTemplateSimpleModel().setIdFormule(newFormuleList.getFormule().getId());
        // Update the EnvoiTemplateSimpleModel
        EnvoiTemplateSimpleModel newEnvoiTemplate = ALImplServiceLocator.getEnvoiTemplateSimpleModelService().update(
                envoiTemplate.getEnvoiTemplateSimpleModel());
        // Affectation des mise à jour
        envoiTemplate.setEnvoiTemplateSimpleModel(newEnvoiTemplate);
        envoiTemplate.setFormuleList(newFormuleList);
        return envoiTemplate;
    }

}
