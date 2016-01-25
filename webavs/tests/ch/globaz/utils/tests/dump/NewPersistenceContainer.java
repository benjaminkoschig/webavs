package ch.globaz.utils.tests.dump;

import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.List;

public class NewPersistenceContainer extends DataContainer {

    private List<JadeAbstractModel> entitees = null;

    public NewPersistenceContainer(List<JadeAbstractModel> entitees) {
        this.entitees = entitees;
    }

    @Override
    public void createJson() {
        prepareJson();
        out.write(gson.toJson(getEntitees()));
        closeJson();
    }

    public List<JadeAbstractModel> getEntitees() {
        return entitees;
    }

}
