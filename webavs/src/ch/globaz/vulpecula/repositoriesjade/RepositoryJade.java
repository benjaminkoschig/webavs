package ch.globaz.vulpecula.repositoriesjade;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.model.JadeComplexModel;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import globaz.jade.persistence.model.JadeSearchSimpleModel;
import globaz.jade.persistence.model.JadeSimpleModel;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.repositories.Repository;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

/**
 * Implémentation abstraite d'un <code>Repository</code> reposant sur la nouvelle persistance de Jade.
 * 
 * @param <T> le type d'entité que le Repository permettra de manipuler
 * @param <C> le <code>JadeComplexModel</code> associé au type d'entité
 * @param <S> le <code>JadeSimpleModel</code> associé au type d'entité
 */
public abstract class RepositoryJade<T extends DomainEntity, C extends JadeComplexModel, S extends JadeSimpleModel>
        implements Repository<T> {

    public interface SearchLotExecutor<T> {
        List<T> execute(List<String> ids);
    }

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static final int NBMAX_ELEMENTS_PER_LOT = 1000;

    public static <T> List<T> searchByLot(final Collection<String> ids, final SearchLotExecutor<T> executor) {
        return RepositoryJade.searchByLot(ids, executor, RepositoryJade.NBMAX_ELEMENTS_PER_LOT);
    }

    public static <T> List<T> searchByLot(final Collection<String> ids, final SearchLotExecutor<T> executor,
            final int limitSize) {
        List<String> idsTemp = new ArrayList<String>();
        List<T> listReturn = new ArrayList<T>();
        int i = 1;
        for (String id : ids) {
            idsTemp.add(id);
            if (((i % limitSize == 0) || (i == ids.size())) && (idsTemp.size() > 0)) {
                listReturn.addAll(executor.execute(idsTemp));
                idsTemp = new ArrayList<String>();
            }
            i++;
        }
        return listReturn;
    }

    public abstract DomaineConverterJade<T, C, S> getConverter();

    @Override
    public T create(final T entity) {
        JadeSimpleModel model = converToPersistence(entity);

        try {
            model = JadePersistenceManager.add(model);
            entity.setId(model.getId());
            entity.setSpy(model.getSpy());
        } catch (JadePersistenceException e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
        return entity;
    }

    @Override
    public T findById(final String id) {
        JadeAbstractSearchModel search = getSearchModelAndSetId(id);
        search = this.search(search);
        if (search.getSearchResults().length > 0) {
            @SuppressWarnings("unchecked")
            S model = (S) search.getSearchResults()[0];
            return this.getConverter().convertToDomain(model);
        }

        return null;
    }

    @Override
    public T update(final T entity) {
        JadeSimpleModel model = converToPersistence(entity);

        try {
            model = JadePersistenceManager.update(model);
            entity.setId(model.getId());
            entity.setSpy(model.getSpy());
        } catch (JadePersistenceException e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
        return entity;
    }

    @Override
    public void delete(final T entity) {
        deleteById(entity.getId());
    }

    /**
     * Supprime un élément en fonction de son identifiant.
     * 
     * @param id l'identifiant de l'élément à supprimer
     * @throws GlobazTechnicalException Quand une erreur survient dans la couche de persistence
     */
    @Override
    public void deleteById(final String id) {
        JadeAbstractSearchModel search = getSearchModelAndSetId(id);
        try {
            JadePersistenceManager.delete((JadeSearchSimpleModel) search);
        } catch (JadePersistenceException e) {
            logger.error("Could not delete entity {} with id {} : {}", this.getClass().getCanonicalName(), id,
                    e.getLocalizedMessage());
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

    /**
     * Cherche l'entité correspondant au SearchModel et la retourne.
     * 
     * @param search le SearchModel
     * @return l'entité trouvée, ou <code>null</code> si plus d'une entité est trouvée
     */
    @SuppressWarnings("unchecked")
    public T readAndFetch(final JadeAbstractSearchModel search) {
        search(search);
        if (search.getSearchResults().length > 0) {
            C model = (C) search.getSearchResults()[0];
            return this.getConverter().convertToDomain(model);
        }

        return null;
    }

    protected List<T> fetchSearchModel(final JadeSearchComplexModel search) {
        List<T> list = new ArrayList<T>();
        for (JadeAbstractModel abstractModel : search.getSearchResults()) {
            @SuppressWarnings("unchecked")
            C model = (C) abstractModel;
            T entity = this.getConverter().convertToDomain(model);
            list.add(entity);
        }
        return list;
    }

    protected List<T> fetchSearchModel(final JadeSearchSimpleModel search) {
        List<T> list = new ArrayList<T>();
        for (JadeAbstractModel abstractModel : search.getSearchResults()) {
            @SuppressWarnings("unchecked")
            S model = (S) abstractModel;
            T entity = this.getConverter().convertToDomain(model);
            list.add(entity);
        }
        return list;
    }

    protected JadeAbstractSearchModel search(final JadeAbstractSearchModel searchModel) {
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
            return returnSearchModel;
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

    protected T searchAndFetchFirst(final JadeSearchComplexModel searchModel) {
        JadeSearchComplexModel search = (JadeSearchComplexModel) this.search(searchModel);
        for (JadeAbstractModel abstractModel : search.getSearchResults()) {
            @SuppressWarnings("unchecked")
            C model = (C) abstractModel;
            T entity = this.getConverter().convertToDomain(model);
            return entity;
        }
        return null;
    }

    protected T searchAndFetchFirst(final JadeSearchSimpleModel searchModel) {
        JadeSearchSimpleModel search = (JadeSearchSimpleModel) this.search(searchModel);
        for (JadeAbstractModel abstractModel : search.getSearchResults()) {
            @SuppressWarnings("unchecked")
            S model = (S) abstractModel;
            T entity = this.getConverter().convertToDomain(model);
            return entity;
        }
        return null;
    }

    public static void searchFor(final JadeSearchComplexModel searchModel) {
        try {
            searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            JadePersistenceManager.search(searchModel);
        } catch (JadePersistenceException e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_INCONNUE, e);
        }
    }

    public static void searchFor(final JadeSearchSimpleModel searchModel) {
        try {
            searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            JadePersistenceManager.search(searchModel);
        } catch (JadePersistenceException e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_INCONNUE, e);
        }
    }

    public static <T extends JadeAbstractModel> List<T> searchForAndFetch(final JadeSearchSimpleModel searchModel) {
        searchFor(searchModel);
        return createListe(searchModel);
    }

    public static <T extends JadeAbstractModel> List<T> searchForAndFetch(final JadeSearchComplexModel searchModel) {
        searchFor(searchModel);
        return createListe(searchModel);
    }

    @SuppressWarnings("unchecked")
    private static <T extends JadeAbstractModel> List<T> createListe(JadeAbstractSearchModel searchModel) {
        List<T> liste = new ArrayList<T>();
        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            liste.add((T) model);
        }
        return liste;
    }

    protected List<T> searchAndFetch(final JadeSearchComplexModel searchModel) {
        JadeSearchComplexModel search = (JadeSearchComplexModel) this.search(searchModel);
        return this.fetchSearchModel(search);
    }

    protected List<T> searchAndFetch(final JadeSearchSimpleModel searchModel) {
        JadeSearchSimpleModel search = (JadeSearchSimpleModel) this.search(searchModel);
        return this.fetchSearchModel(search);
    }

    private JadeAbstractSearchModel getSearchModelAndSetId(final String id) {
        JadeAbstractSearchModel search = this.getConverter().getSearchSimpleModel();
        try {
            Method method = search.getClass().getMethod("setForId", new Class[] { String.class });
            method.invoke(search, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return search;
    }

    private JadeSimpleModel converToPersistence(final T entity) {
        return this.getConverter().convertToPersistence(entity);
    }

    /**
     * Permet la gestion de la persistance de liste.
     * En passant en paramètre la liste des éléments actuels (état que l'on souhaite créer), la liste des éléments
     * existants (état précédent) et un repository, cette méthode gérera automatiquement les ajouts, les modifications
     * et les suppressions à effectuer.
     * 
     * @param liste Liste des éléments actuels (état que l'on souhaite créer)
     * @param listeExistants Liste des éléments existants (état précédent)
     * @param repository Repository utilisé pour la persistance.
     */
    public static <T extends DomainEntity> void persistList(List<T> liste, List<T> listeExistants,
            Repository<T> repository) {
        List<T> listeASupprimer = new ArrayList<T>(listeExistants);
        List<T> listeAAjouter = new ArrayList<T>(liste);
        List<T> listeAModifier = new ArrayList<T>(liste);

        listeASupprimer.removeAll(liste);
        listeAAjouter.removeAll(listeExistants);
        listeAModifier.removeAll(listeAAjouter);

        for (T entity : listeASupprimer) {
            repository.delete(entity);
        }

        for (T entity : listeAAjouter) {
            repository.create(entity);
        }

        for (T entity : listeAModifier) {
            repository.update(entity);
        }
    }
}
