package ch.globaz.pegasus.businessimpl.services.lot;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;

public abstract class AbstractLotBuilder {

    protected BSession getSession() {
        return BSessionUtil.getSessionFromThreadContext();
    }
}
