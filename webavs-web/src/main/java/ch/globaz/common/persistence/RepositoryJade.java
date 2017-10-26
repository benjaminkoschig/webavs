package ch.globaz.common.persistence;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.JadePersistencePKProvider;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import globaz.jade.persistence.model.JadeSearchSimpleModel;
import globaz.jade.persistence.model.JadeSimpleModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.domaine.repository.DomainEntity;
import ch.globaz.common.domaine.repository.Repository;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.pegasus.business.models.decision.DecisionRefus;
import ch.globaz.pegasus.business.models.decision.DecisionRefusSearch;
import com.google.common.collect.Lists;

/**
 * Implémentation abstraite d'un <code>Repository</code> reposant sur la nouvelle persistance de Jade.
 * 
 * @param <T> le type d'entité que le Repository permettra de manipuler
 * @param <S> le <code>JadeSimpleModel</code> associé au type d'entité
 */
public abstract class RepositoryJade<T extends DomainEntity, S extends JadeSimpleModel> implements Repository<T> {
    private static final int NBMAX_ELEMENTS_PER_LOT = 1000;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public abstract DomaineConverterJade<T, S> getConverter();

    protected S converToPersistence(final T entity) {
        return this.getConverter().convertToPersistence(entity);
    }

    public interface SearchLotExecutor<T> {
        List<T> execute(List<String> ids);
    }

    protected static JadePersistencePKProvider buildKeyProviender(Class<? extends JadeSimpleModel> clazz,
            long reservedRange) {
        try {
            return JadePersistenceManager.getPrimaryKeyProvider(clazz, reservedRange);
        } catch (JadePersistenceException e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

    public static <T> List<T> searchByLot(final Collection<String> ids, final SearchLotExecutor<T> executor) {
        return RepositoryJade.searchByLot(ids, executor, RepositoryJade.NBMAX_ELEMENTS_PER_LOT);
    }

    public static <T> List<T> searchByLot(final Collection<String> ids, final SearchLotExecutor<T> executor,
            final int limitSize) {
        List<List<String>> idsSplited = Lists.partition(new ArrayList<String>(ids), limitSize);

        List<T> listReturn = new ArrayList<T>();

        for (List<String> idsList : idsSplited) {
            listReturn.addAll(executor.execute(idsList));
        }

        return listReturn;
    }

    @Override
    public T create(final T entity) {
        JadeSimpleModel model = converToPersistence(entity);
        return RepositoryJade.create(entity, model);
    }

    public T create(final T entity, JadePersistencePKProvider jadePersistencePKProvider) {
        JadeSimpleModel model = converToPersistence(entity);
        try {
            model.setId(jadePersistencePKProvider.getNextPrimaryKey());
        } catch (JadePersistenceException e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
        return create(entity, model);
    }

    public static <T extends JadeSimpleModel> T create(final T model,
            JadePersistencePKProvider jadePersistencePKProvider) {
        try {
            model.setId(jadePersistencePKProvider.getNextPrimaryKey());
        } catch (JadePersistenceException e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
        return create(model);
    }

    @SuppressWarnings("unchecked")
    public static <T extends JadeSimpleModel> T create(final T model) {
        try {
            return (T) JadePersistenceManager.add(model);
        } catch (JadePersistenceException e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

    public static <T extends DomainEntity> T create(final T entity, JadeSimpleModel model) {
        JadeSimpleModel modelNew = create(model);
        entity.setId(modelNew.getId());
        entity.setSpy(modelNew.getSpy());
        return entity;
    }

    @Override
    public T update(final T entity) {
        JadeSimpleModel model = converToPersistence(entity);
        model.setId(entity.getId());
        model.setSpy(entity.getSpy());
        update(entity, model);
        return entity;
    }

    protected void update(final T entity, JadeSimpleModel model) {
        try {
            JadeSimpleModel modelNew = JadePersistenceManager.update(model);
            entity.setId(modelNew.getId());
            entity.setSpy(modelNew.getSpy());
        } catch (JadePersistenceException e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

    @Override
    public void delete(final T entity) {
        deleteById(entity.getId());
    }

    /**
     * Supprime un élément en fonction de son identifiant.
     * 
     * @param id l'identifiant de l'élément à supprimer
     * @throws GlobazTechnicalException Quand une erreur survient dans la couche de; persistence
     */
    @Override
    public void deleteById(final String id) {
        DomaineJadeAbstractSearchModel search = this.getConverter().getSearchModel();
        search.setForId(id);
        try {
            JadePersistenceManager.delete(search);
        } catch (JadePersistenceException e) {
            logger.error("Could not delete entity {} with id {} : {}", this.getClass().getCanonicalName(), id,
                    e.getLocalizedMessage());
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

    @Override
    public T findById(final String id) {
        S model = findModelById(id);
        if (model != null) {
            return this.getConverter().convertToDomain(model);
        }
        return null;
    }

    @Override
    public List<T> findAll() {
        DomaineConverterJade<T, S> converter = this.getConverter();
        JadeAbstractSearchModel search = this.search(converter.getSearchModel());

        List<T> entities = new ArrayList<T>();
        for (JadeAbstractModel model : search.getSearchResults()) {
            entities.add(converter.convertToDomain((S) model));
        }
        return entities;
    }

    @Override
    @SuppressWarnings("unchecked")
    public S findModelById(final String id) {
        DomaineJadeAbstractSearchModel sm = this.getConverter().getSearchModel();
        sm.setForId(id);

        try {
            JadeSimpleModel model = ((JadeSimpleModel) sm.whichModelClass().newInstance());
            model.setId(id);
            return (S) JadePersistenceManager.read(model);
        } catch (Exception e) {
            logger.error("Error reade a JadeModel : {}", e.getLocalizedMessage());
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

    @Override
    public List<T> findByIds(final Collection<String> ids) {
        final DomaineJadeAbstractSearchModel sm = this.getConverter().getSearchModel();
        sm.setForIds(ids);
        return this.findByIds(sm);
    }

    @SuppressWarnings("unchecked")
    public List<T> findByIds(final DomaineJadeAbstractSearchModel domaineJadeAbstractSearchModel) {
        List<JadeAbstractModel> list = searchByLot(domaineJadeAbstractSearchModel.getForIds(),
                new SearchLotExecutor<JadeAbstractModel>() {
                    @Override
                    public List<JadeAbstractModel> execute(List<String> ids) {
                        JadeAbstractSearchModel search = RepositoryJade.this.search(domaineJadeAbstractSearchModel);
                        if (search.getSearchResults().length > 0) {
                            return Arrays.asList(search.getSearchResults());
                        }
                        return new ArrayList<JadeAbstractModel>();
                    }
                });

        List<T> entities = new ArrayList<T>();
        DomaineConverterJade<T, S> converter = this.getConverter();
        for (JadeAbstractModel model : list) {
            entities.add(converter.convertToDomain((S) model));
        }
        return entities;
    }

    protected List<T> fetchSearchModel(final JadeSearchSimpleModel search) {
        List<T> list = new ArrayList<T>();
        DomaineConverterJade<T, S> converter = this.getConverter();
        for (JadeAbstractModel abstractModel : search.getSearchResults()) {
            @SuppressWarnings("unchecked")
            S model = (S) abstractModel;
            T entity = converter.convertToDomain(model);
            list.add(entity);
        }
        return list;
    }

    protected <SM extends JadeAbstractSearchModel> SM search(final SM searchModel) {
        try {
            PaginationContainer jadePaginator = StorePaginator.getPaginator();

            // On ne stocke que le paginator si celui-ci n'est pas encore
            // présent, dans le but d'éviter la surcharge de la pagination.
            if (jadePaginator != null && !jadePaginator.hasNbOfResultMatchingQuery()) {
                searchModel.setDefinedSearchSize(jadePaginator.getDefinedSearchSize());
                searchModel.setOffset(jadePaginator.getOffset());
            } else {
                searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            }

            if (jadePaginator != null && JadeStringUtil.isEmpty(searchModel.getOrderKey())) {
                searchModel.setOrderKey(jadePaginator.getOrderKey());
            }

            JadeAbstractSearchModel returnSearchModel = JadePersistenceManager.search(searchModel);

            if (jadePaginator != null && !jadePaginator.hasNbOfResultMatchingQuery()) {
                jadePaginator.setNbOfResultMatchingQuery(returnSearchModel.getNbOfResultMatchingQuery());
                jadePaginator.setHasMoreElements(returnSearchModel.isHasMoreElements());
            }
            return (SM) returnSearchModel;
        } catch (JadePersistenceException e) {
            logger.error("Error searching a JadeModel : {}", e.getLocalizedMessage());
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

    protected int count(final JadeAbstractSearchModel searchModel) {
        try {
            return JadePersistenceManager.count(searchModel);
        } catch (JadePersistenceException e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

    protected T searchAndFetchFirst(final JadeSearchSimpleModel searchModel) {
        JadeSearchSimpleModel search = this.search(searchModel);
        for (JadeAbstractModel abstractModel : search.getSearchResults()) {
            @SuppressWarnings("unchecked")
            S model = (S) abstractModel;
            return this.getConverter().convertToDomain(model);
        }
        return null;
    }

    private static void searchFor(final JadeSearchComplexModel searchModel) {
        try {
            JadePersistenceManager.search(searchModel);
        } catch (JadePersistenceException e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_INCONNUE, e);
        }
    }

    private static void searchFor(final JadeSearchSimpleModel searchModel) {
        try {
            JadePersistenceManager.search(searchModel);
        } catch (JadePersistenceException e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_INCONNUE, e);
        }
    }

    public static <T extends JadeAbstractModel> List<T> searchForAndFetch(final JadeSearchSimpleModel searchModel) {
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        searchFor(searchModel);
        return createListe(searchModel);
    }

    public static <T extends JadeAbstractModel> List<T> searchForAndFetch(final JadeSearchSimpleModel searchModel,
            int pageInateSize) {
        searchModel.setDefinedSearchSize(pageInateSize);
        searchFor(searchModel);
        return createListe(searchModel);
    }

    public static <T extends JadeAbstractModel> List<T> searchForAndFetch(final JadeSearchComplexModel searchModel) {
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        searchFor(searchModel);
        return createListe(searchModel);
    }

    public static <T extends JadeAbstractModel> List<T> searchForAndFetch(final JadeSearchComplexModel searchModel,
            int pageInateSize) {
        searchModel.setDefinedSearchSize(pageInateSize);
        searchFor(searchModel);
        return createListe(searchModel);
    }

    @SuppressWarnings("unchecked")
    private static <T extends JadeAbstractModel> List<T> createListe(JadeAbstractSearchModel searchModel) {
        List<T> liste = new ArrayList<T>(searchModel.getSize());
        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            liste.add((T) model);
        }
        return liste;
    }

    protected List<T> searchAndFetch(final JadeSearchSimpleModel searchModel) {
        JadeSearchSimpleModel search = this.search(searchModel);
        return this.fetchSearchModel(search);
    }

    public static List<DecisionRefus> searchForAndFetchOne(DecisionRefusSearch search) {
        // TODO Auto-generated method stub
        return null;
    }
}