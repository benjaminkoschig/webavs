package ch.globaz.pegasus.process.util;

import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;

public abstract class AbstractEntity implements JadeProcessEntityInterface {
    protected JadeProcessEntity entity;

    public JadeProcessEntity getEntity() {
        return entity;
    }

    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        this.entity = entity;
    }
}
