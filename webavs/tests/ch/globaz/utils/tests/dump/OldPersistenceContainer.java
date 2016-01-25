package ch.globaz.utils.tests.dump;

import globaz.globall.db.BEntity;
import java.util.List;

public class OldPersistenceContainer extends DataContainer {

    private List<BEntity> entitees = null;

    public OldPersistenceContainer(List<BEntity> entitees) {
        this.entitees = entitees;
    }

    @Override
    public void createJson() {
        prepareJson();
        out.write(gson.toJson(getEntitees()));
        closeJson();
    }

    public List<BEntity> getEntitees() {
        return entitees;
    }
}
