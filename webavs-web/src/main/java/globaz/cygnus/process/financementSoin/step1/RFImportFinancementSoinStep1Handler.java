package globaz.cygnus.process.financementSoin.step1;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;

public class RFImportFinancementSoinStep1Handler implements JadeProcessEntityInterface, JadeProcessEntityNeedProperties {

    private JadeProcessEntity entity = null;
    private List<JadeProcessEntity> listEntity = new ArrayList<JadeProcessEntity>();
    private Map<Enum<?>, String> properties = null;

    /**
     * Constructeur
     * 
     * @param listEntity
     */
    public RFImportFinancementSoinStep1Handler(List<JadeProcessEntity> listEntity) {
        this.listEntity = listEntity;
    }

    /**
     * @return the properties
     */
    public Map<Enum<?>, String> getProperties() {
        return properties;
    }

    /**
     * Ajout de l'entité dans la liste
     */
    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        listEntity.add(entity);
    }

    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        this.entity = entity;
    }

    @Override
    public void setProperties(Map<Enum<?>, String> map) {
        properties = map;
    }
}
