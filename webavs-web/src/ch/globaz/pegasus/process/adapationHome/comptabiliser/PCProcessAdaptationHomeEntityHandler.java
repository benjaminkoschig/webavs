package ch.globaz.pegasus.process.adapationHome.comptabiliser;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Map;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityPropertySavable;
import ch.globaz.pegasus.process.adaptation.PCProcessAdapationEnum;
import ch.globaz.pegasus.process.util.AbstractEntity;

public class PCProcessAdaptationHomeEntityHandler extends AbstractEntity implements JadeProcessEntityInterface,
        JadeProcessEntityPropertySavable<PCProcessAdapationEnum>, JadeProcessEntityNeedProperties {

    @Override
    public Map<PCProcessAdapationEnum, String> getValueToSave() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setProperties(Map<Enum<?>, String> arg0) {
        // TODO Auto-generated method stub

    }

}
