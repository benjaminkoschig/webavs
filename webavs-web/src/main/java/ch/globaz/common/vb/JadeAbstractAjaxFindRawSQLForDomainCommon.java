package ch.globaz.common.vb;

import globaz.framework.bean.JadeAbstractAjaxFindViewBean;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.Collection;
import java.util.List;
import ch.globaz.common.domaine.repository.DomainEntity;
import ch.globaz.common.domaine.repository.Repository;
import ch.globaz.common.persistence.PaginationContainer;
import ch.globaz.common.persistence.StorePaginator;

public abstract class JadeAbstractAjaxFindRawSQLForDomainCommon<T extends DomainEntity> extends
        JadeAbstractAjaxFindViewBean implements DomainViewBeanCommon<T> {

    private JadeSearchSimpleModel search;
    private List<T> list;

    public JadeAbstractAjaxFindRawSQLForDomainCommon() {
        initList();
    }

    public final Repository<T> getRepository() {
        return new Repository<T>() {
            @Override
            public T create(T entity) {
                return null;
            }

            @Override
            public T findById(String id) {
                return null;
            }

            @Override
            public T update(T entity) {
                return null;
            }

            @Override
            public void delete(T entity) {
            }

            @Override
            public void deleteById(String idEntity) {
            }

            @Override
            public <S> S findModelById(String id) {
                return null;
            }

            @Override
            public <S> S findAll() {
                return null;
            }

            @Override
            public List<T> findByIds(Collection<String> ids) {
                return null;
            }
        };
    }

    @Override
    public void find() throws Exception {
        PaginationContainer container = new PaginationContainer(search.getDefinedSearchSize(), true,
                search.getNbOfResultMatchingQuery(), search.getOffset(), search.getOrderKey());
        StorePaginator.storePaginator(container);
        this.search.setDefinedSearchSize(container.getDefinedSearchSize());
        list = findBySQL();
        container = StorePaginator.getPaginator();
        search.setNbOfResultMatchingQuery(container.getNbOfResultMatchingQuery());
        int nbOfResultMatchingQuery = nbOfResultMathingQuery();
        search.setNbOfResultMatchingQuery(nbOfResultMatchingQuery);
        if (search.getOffset() + search.getDefinedSearchSize() > nbOfResultMatchingQuery) {
            search.setHasMoreElements(false);
        } else {
            search.setHasMoreElements(true);
        }
    }

    public abstract List<T> findBySQL();

    public abstract int nbOfResultMathingQuery();

    public List<T> getList() {
        return list;
    }

    @Override
    public final JadeAbstractModel getCurrentEntity() {

        if (getEntity() == null) {
            throw new IllegalArgumentException("The entity is null, please set it");
        }
        return new JadeAbstractModel() {

            @Override
            public String getId() {
                return String.valueOf(getEntity().getId());
            }

            @Override
            public String getSpy() {
                return getEntity().getSpy().toString();
            }

            @Override
            public void setId(String id) {
                getEntity().setId(id);
            }

            @Override
            public void setSpy(String spy) {
            }
        };
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return search;
    }

    @Override
    public void update() throws Exception {
        getRepository().update(getEntity());
    }

    @Override
    public void add() throws Exception {
        getRepository().create(getEntity());
    }

    @Override
    public void delete() throws Exception {
        getRepository().delete(getEntity());
    }

    @Override
    public void retrieve() throws Exception {
        getRepository().findById(getEntity().getId());
    }

    @Override
    public void initList() {
        @SuppressWarnings("unchecked")
        final Class<JadeAbstractModel> clazz = (Class<JadeAbstractModel>) getCurrentEntity().getClass();

        search = new JadeSearchSimpleModel() {
            @Override
            public Class<JadeAbstractModel> whichModelClass() {
                return clazz;
            }
        };
    }

    public int getOffset() {
        return getSearchModel().getOffset();
    }

    public int getSize() {
        return getSearchModel().getDefinedSearchSize();
    }
}