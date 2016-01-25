package globaz.webavs.common;

import globaz.globall.db.BEntity;
import java.util.List;

public interface BIGenericManager<T extends BEntity> {

    public List<T> getContainerAsList();
}
