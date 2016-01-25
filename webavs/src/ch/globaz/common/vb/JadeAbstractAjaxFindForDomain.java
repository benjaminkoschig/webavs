package ch.globaz.common.vb;

import globaz.framework.bean.JadeAbstractAjaxFindViewBean;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.repositories.Repository;
import ch.globaz.vulpecula.repositoriesjade.PaginationContainer;
import ch.globaz.vulpecula.repositoriesjade.StorePaginator;

public abstract class JadeAbstractAjaxFindForDomain<T extends DomainEntity> extends JadeAbstractAjaxFindViewBean
        implements DomainViewBean<T> {

    private JadeSearchSimpleModel search;
    private List<T> list;

    public JadeAbstractAjaxFindForDomain() {
        this.initList();
    }

    public abstract Repository<T> getRepository();

    @Override
    public void find() throws Exception {
        PaginationContainer container = new PaginationContainer(search.getDefinedSearchSize(), true,
                search.getNbOfResultMatchingQuery(), search.getOffset(), search.getOrderKey());
        StorePaginator.storePaginator(container);
        this.search.setDefinedSearchSize(container.getDefinedSearchSize());
        this.list = findByRepository();
        this.search.setHasMoreElements(container.isHasMoreElements());
        this.search.setNbOfResultMatchingQuery(container.getNbOfResultMatchingQuery());
    }

    public List<T> getList() {
        return this.list;
    }

    public abstract List<T> findByRepository();

    @Override
    public final JadeAbstractModel getCurrentEntity() {

        if (getEntity() == null) {
            throw new IllegalArgumentException("The entity is null, please set it");
        }
        return new JadeAbstractModel() {

            @Override
            public String getId() {
                return String.valueOf(JadeAbstractAjaxFindForDomain.this.getEntity().getId());
            }

            @Override
            public String getSpy() {
                return JadeAbstractAjaxFindForDomain.this.getEntity().getSpy().toString();
            }

            @Override
            public void setId(String id) {
                JadeAbstractAjaxFindForDomain.this.getEntity().setId(id);
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

}
