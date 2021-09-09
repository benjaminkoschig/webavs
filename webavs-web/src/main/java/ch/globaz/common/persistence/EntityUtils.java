package ch.globaz.common.persistence;

import ch.globaz.common.exceptions.CommonTechnicalException;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;

public final class EntityUtils {

    public static <T extends BEntity> T addEntity(T entity, BSession session) {
        entity.setSession(session);
        try {
            entity.add();
            return entity;
        } catch (Exception e) {
            throw new CommonTechnicalException("Impossible de sauver l'entité de type " + entity.getClass() + " avec l'entité " + entity + " dans la base", e);
        }
    }

    public static <T extends BEntity> T entityLoader(final Class<T> entityClass, final String id, final BSession session) {
        try {
            T entity = entityClass.newInstance();
            entity.setSession(session);
            entity.setId(id);
            entity.retrieve();
            return entity;
        } catch (Exception e) {
            throw new CommonTechnicalException("Impossible to load the " + entityClass.getSimpleName() + " with this id:" + id, e);
        }
    }
}
