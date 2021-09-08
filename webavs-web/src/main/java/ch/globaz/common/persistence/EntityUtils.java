package ch.globaz.common.persistence;

import ch.globaz.common.exceptions.CommonTechnicalException;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;

public final class EntityUtils {

    public static void addEntity(BEntity entity, BSession session) {
        entity.setSession(session);
        try {
            entity.add();
        } catch (Exception e) {
            throw new CommonTechnicalException("Impossible de sauver l'entit� de type " + entity.getClass() + " avec l'entit� " + entity + " dans la base", e);
        }
    }
}
