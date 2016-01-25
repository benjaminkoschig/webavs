package globaz.prestation.tools;

import globaz.globall.db.BEntity;

public class PRAssert {

    private static final String DEFAULT_ENTITY_ERROR_MSG = " Entity not found.";

    public static void notIsNew(BEntity entity, String errorMsg) throws Exception {
        if (entity.isNew()) {
            throw new Exception(errorMsg == null ? (entity.getClass().getName() + DEFAULT_ENTITY_ERROR_MSG) : (errorMsg
                    + entity.getClass().getName() + " not Found. id=" + entity.getId()));
        }
    }

}
