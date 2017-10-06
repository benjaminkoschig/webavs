/**
 * 
 */
package ch.globaz.corvus.businessimpl.services.models.ventilation;

import globaz.corvus.db.ventilation.REVentilation;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.corvus.business.models.lots.SimpleLotSearch;
import ch.globaz.corvus.business.models.ventilation.SimpleVentilation;
import ch.globaz.corvus.business.models.ventilation.SimpleVentilationSearch;
import ch.globaz.corvus.business.models.ventilation.VentilationException;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.corvus.business.services.models.ventilation.SimpleVentilationService;
import ch.globaz.pegasus.business.exceptions.models.process.AdaptationException;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordeeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pyxis.common.Messages;

/**
 * @author est
 * 
 */
public class SimpleVentilationServiceImpl implements SimpleVentilationService {
    private enum OPERATION {
        ADD,
        DELETE,
        UPDATE
    };

    private REVentilation _adapt(SimpleVentilation model) throws VentilationException {
        REVentilation bean = new REVentilation(); // destination

        // Preconditions
        if (model == null) {
            // Préventif, ne devrait jamais arriver...
            throw new VentilationException(Messages.MAPPING_ERROR + " - " + this.getClass().getName() + "._adapt(...)");
        }

        // Mapping des données
        bean.setId(model.getId());
        bean.setIdPrestationAccordee(model.getIdPrestationAccordee());
        bean.setMontantVentile(model.getMontantVentile());
        bean.setCsTypeVentilation(model.getCsTypeVentilation());

        bean.populateSpy(model.getSpy());
        return bean;
    }

    private SimpleVentilation _adapt(REVentilation bean) throws VentilationException {
        SimpleVentilation model = new SimpleVentilation(); // destination

        // Preconditions
        if (bean == null) {
            // Préventif, ne devrait jamais arriver...
            throw new VentilationException(Messages.MAPPING_ERROR + " - " + this.getClass().getName() + "._adapt(...)");
        }

        // Mapping des données
        model.setId(bean.getId());
        model.setIdPrestationAccordee(bean.getIdPrestationAccordee());
        model.setMontantVentile(bean.getMontantVentile());
        model.setCsTypeVentilation(bean.getCsTypeVentilation());

        model.setSpy(bean.getSpy().getFullData());
        return model;
    }

    private SimpleVentilation _perform(OPERATION operation, SimpleVentilation model) throws VentilationException {
        if (model == null) {
            throw new VentilationException(Messages.MODEL_IS_NULL + " - SimpleVentilationServiceImpl." + operation
                    + "(...)");
        }
        // Execution de l'opération d'ajout ou de mise à jour
        REVentilation bean = null;
        try {
            bean = _adapt(model);

            switch (operation) {
                case ADD:
                    bean.add();
                    model = _adapt(bean);

                    break;
                case UPDATE:
                    bean.update();
                    model = _adapt(bean);

                    break;
                case DELETE:
                    bean.delete();

                    break;

            }

        } catch (Exception e) {
            throw new VentilationException(Messages.TECHNICAL, e);
        }

        return model;
    }

    @Override
    public SimpleVentilation create(SimpleVentilation model) throws JadePersistenceException, VentilationException {
        if (model == null) {
            throw new VentilationException("Unable to create SimpleVentilation, the model passed is null!");
        }

        return _perform(OPERATION.ADD, model);
    }

    @Override
    public SimpleVentilation delete(SimpleVentilation simpleVentilation) throws JadePersistenceException {
        return null;
    }

    @Override
    public SimpleVentilation read(String idSimpleLot) throws JadePersistenceException {
        return null;
    }

    @Override
    public SimpleVentilationSearch search(SimpleLotSearch search) throws JadePersistenceException {
        return null;
    }

    @Override
    public SimpleVentilation update(SimpleVentilation simpleVentilation) throws JadePersistenceException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * #search(ch.globaz.corvus.business.models.ventilation.SimpleVentilationSearch)
     */
    @Override
    public SimpleVentilationSearch search(SimpleVentilationSearch search) throws JadePersistenceException,
            AdaptationException {
        if (search == null) {
            throw new AdaptationException("Unable to search simpleVentilationSearch, the model passed is null!");
        }
        return (SimpleVentilationSearch) JadePersistenceManager.search(search);
    }

    @Override
    public SimpleVentilation getMontantVentileFromIdPca(String idPca) throws JadeApplicationException,
            JadePersistenceException {
        SimplePCAccordeeSearch pcasearch = new SimplePCAccordeeSearch();
        pcasearch.setForIdPCAccordee(idPca);
        PegasusServiceLocator.getSimplePcaccordeeService().search(pcasearch);
        if (pcasearch.getSearchResults().length != 0) {
            SimplePCAccordee pca = (SimplePCAccordee) pcasearch.getSearchResults()[0];
            SimpleVentilationSearch ventilationSearch = new SimpleVentilationSearch();
            ventilationSearch.setForIdPrestationAccordee(pca.getIdPrestationAccordee());
            ventilationSearch = CorvusServiceLocator.getSimpleVentilationService().search(ventilationSearch);
            if (ventilationSearch.getSearchResults().length != 0) {
                return ((SimpleVentilation) ventilationSearch.getSearchResults()[0]);
            }

        }
        return null;
    }

}
