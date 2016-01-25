package ch.globaz.al.businessimpl.services.models.attribut;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.Date;
import ch.globaz.al.business.exceptions.model.attribut.ALAttributEntiteModelException;
import ch.globaz.al.business.models.attribut.AttributEntiteModel;
import ch.globaz.al.business.models.attribut.AttributEntiteSearchModel;
import ch.globaz.al.business.services.models.attribut.AttributEntiteModelService;
import ch.globaz.al.businessimpl.checker.model.attribut.AttributEntiteModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.naos.business.model.AffiliationSearchSimpleModel;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.service.ParamServiceLocator;

/**
 * Classe d'implémentation des services de AttributEntiteModel
 * 
 * @author GMO
 * 
 */
public class AttributEntiteModelServiceImpl extends ALAbstractBusinessServiceImpl implements AttributEntiteModelService {
    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.attribut.AttributEntiteModelService
     * #create(ch.globaz.al.business.models.attribut.AttributEntiteModel)
     */
    @Override
    public AttributEntiteModel create(AttributEntiteModel attributEntiteModel) throws JadeApplicationException,
            JadePersistenceException {
        if (attributEntiteModel == null) {
            throw new ALAttributEntiteModelException("Unable to create attributEntite-the model passed is empty");
        }
        AttributEntiteModelChecker.validate(attributEntiteModel);
        return (AttributEntiteModel) JadePersistenceManager.add(attributEntiteModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.attribut.AttributEntiteModelService
     * #delete(ch.globaz.al.business.models.attribut.AttributEntiteModel)
     */
    @Override
    public AttributEntiteModel delete(AttributEntiteModel attributEntiteModel) throws JadeApplicationException,
            JadePersistenceException {

        if (attributEntiteModel == null) {
            throw new ALAttributEntiteModelException("Unable to delete attribut-the model passed is null");
        }

        if (attributEntiteModel.isNew()) {
            throw new ALAttributEntiteModelException("unable to delete attribut-the model passed is new");
        }

        return (AttributEntiteModel) JadePersistenceManager.delete(attributEntiteModel);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.attribut.AttributEntiteModelService
     * #getAttributAffilie(java.lang.String, java.lang.String)
     */
    @Override
    public AttributEntiteModel getAttributAffilie(String nomAttr, String idAffiliation)
            throws JadeApplicationException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(nomAttr)) {
            throw new ALAttributEntiteModelException(
                    "AttributEntiteModelServiceImpl#getAttributAffilie : nomAttr is null or empty");
        }
        if (JadeStringUtil.isEmpty(idAffiliation)) {
            throw new ALAttributEntiteModelException(
                    "AttributEntiteModelServiceImpl#getAttributAffilie : idAffiliation is null or empty");
        }

        AttributEntiteSearchModel searchModel = new AttributEntiteSearchModel();
        searchModel.setForCleEntite(idAffiliation);
        searchModel.setForNomAttribut(nomAttr);
        searchModel.setForTypeEntite(AffiliationSimpleModel.class.getName().toString());

        searchModel = search(searchModel);
        if (searchModel.getSize() > 0) {
            return (AttributEntiteModel) (search(searchModel)).getSearchResults()[0];
        } else {
            ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName("WEBAF", nomAttr,
                    JadeDateUtil.getGlobazFormattedDate(new Date()));

            AttributEntiteModel attributDefault = new AttributEntiteModel();
            // attributDefault.setCleEntite(param.get);
            attributDefault.setNomAttribut(param.getIdCleDiffere());
            attributDefault.setTypeEntite(AffiliationSimpleModel.class.getName());
            attributDefault.setValeurAlpha(param.getValeurAlphaParametre());
            attributDefault.setValeurNum(param.getValeurNumParametre());

            return attributDefault;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.attribut.AttributEntiteModelService
     * #getAttributAffilieByNumAffilie(java.lang.String, java.lang.String)
     */
    @Override
    public AttributEntiteModel getAttributAffilieByNumAffilie(String nomAttr, String numeroAffilie)
            throws JadeApplicationException, JadePersistenceException {

        AffiliationSearchSimpleModel searchModel = new AffiliationSearchSimpleModel();

        searchModel.setForNumeroAffilie(numeroAffilie);
        searchModel.setForDateValidite(JadeDateUtil.getGlobazFormattedDate(new Date()));
        searchModel.setWhereKey("withDateValidite");
        searchModel = AFBusinessServiceLocator.getAffiliationService().find(searchModel);

        if ((searchModel.getSize() > 1)) {
            throw new ALAttributEntiteModelException(
                    "AttributEntiteModelServiceImpl#getAttributAffilieByNumAffilie : impossible de déterminer l'id affiliation");
        }
        // Si on ne trouve pas d'affiliation active, on prend même une inactive
        if ((searchModel.getSize() == 0)) {
            searchModel.setForDateValidite(null);
            searchModel.setWhereKey("default");
            searchModel = AFBusinessServiceLocator.getAffiliationService().find(searchModel);
            if (searchModel.getSize() == 0) {
                throw new ALAttributEntiteModelException(
                        "AttributEntiteModelServiceImpl#getAttributAffilieByNumAffilie : impossible de déterminer l'id affiliation");
            }

        }
        return getAttributAffilie(nomAttr,
                ((AffiliationSimpleModel) searchModel.getSearchResults()[0]).getAffiliationId());

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.attribut.AttributEntiteModelService
     * #search(ch.globaz.al.business.models.attribut.AttributEntiteSearchModel)
     */
    @Override
    public AttributEntiteSearchModel search(AttributEntiteSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new ALAttributEntiteModelException(
                    "AttributEntiteModelServiceImpl#search : searchModel is null or empty");

        }
        return (AttributEntiteSearchModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.attribut.AttributEntiteModelService
     * #update(ch.globaz.al.business.models.attribut.AttributEntiteModel)
     */
    @Override
    public AttributEntiteModel update(AttributEntiteModel attributEntiteModel) throws JadeApplicationException,
            JadePersistenceException {
        if (attributEntiteModel == null) {
            throw new ALAttributEntiteModelException("Unable to update attribut-the model passed is null");
        }
        if (attributEntiteModel.isNew()) {
            throw new ALAttributEntiteModelException("Unable to update attribut-the model passed is new");
        }

        AttributEntiteModelChecker.validate(attributEntiteModel);

        return (AttributEntiteModel) JadePersistenceManager.update(attributEntiteModel);
    }

}
