package ch.globaz.common.persistence;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import lombok.Value;

@Value(staticConstructor = "of")
public class EntityService {

    private final BSession session;

    public <T extends BEntity> T add(final T entity) {
        return EntityUtils.addEntity(entity, session);
    }

    public <T extends BEntity> T load(final Class<T> entityClass, final String id) {
        return EntityUtils.entityLoader(entityClass, id, session);
    }

}
