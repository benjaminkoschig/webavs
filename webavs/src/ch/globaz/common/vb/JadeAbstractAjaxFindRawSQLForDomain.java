package ch.globaz.common.vb;

import globaz.framework.bean.JadeAbstractAjaxFindViewBean;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.repositories.Repository;

public abstract class JadeAbstractAjaxFindRawSQLForDomain<T extends DomainEntity> extends JadeAbstractAjaxFindViewBean
        implements DomainViewBean<T> {

    private JadeSearchSimpleModel search;
    private List<T> list;

    public JadeAbstractAjaxFindRawSQLForDomain() {
        this.initList();
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
        };
    }

    @Override
    public void find() throws Exception {
        this.list = findBySQL();
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
        return this.list;
    }

    @Override
    public final JadeAbstractModel getCurrentEntity() {

        if (getEntity() == null) {
            throw new IllegalArgumentException("The entity is null, please set it");
        }
        return new JadeAbstractModel() {

            @Override
            public String getId() {
                return String.valueOf(JadeAbstractAjaxFindRawSQLForDomain.this.getEntity().getId());
            }

            @Override
            public String getSpy() {
                return JadeAbstractAjaxFindRawSQLForDomain.this.getEntity().getSpy().toString();
            }

            @Override
            public void setId(String id) {
                JadeAbstractAjaxFindRawSQLForDomain.this.getEntity().setId(id);
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
        this.getRepository().update(getEntity());
    }

    @Override
    public void add() throws Exception {
        this.getRepository().create(getEntity());
    }

    @Override
    public void delete() throws Exception {
        this.getRepository().delete(getEntity());
    }

    @Override
    public void retrieve() throws Exception {
        this.getRepository().findById(getEntity().getId());
    }

    @Override
    public void initList() {
        @SuppressWarnings("unchecked")
        final Class<JadeAbstractModel> clazz = (Class<JadeAbstractModel>) this.getCurrentEntity().getClass();

        this.search = new JadeSearchSimpleModel() {
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
