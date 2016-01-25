/**
 *
 */
package ch.globaz.vulpecula.process.decompte.step2;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityDataFind;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.utils.BasicSerializer;
import ch.globaz.vulpecula.process.decompte.PTProcessDecompteProperty;

/**
 * @author sel
 * 
 */
public class PTProcessDecompteEntityHandlerPublier implements JadeProcessEntityInterface,
        JadeProcessEntityDataFind<PTProcessDecompteProperty> {

    private List<String> decompteToPrints;

    protected Map<PTProcessDecompteProperty, String> properties;

    public PTProcessDecompteEntityHandlerPublier(final List<String> decompteToPrints) {
        this.decompteToPrints = decompteToPrints;
    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        String id = properties.get(PTProcessDecompteProperty.ID_DECOMPTE);
        List<String> idsDecomptes = BasicSerializer.deserialize(id);
        for (String idDecompte : idsDecomptes) {
            decompteToPrints.add(idDecompte);
        }
    }

    @Override
    public void setCurrentEntity(final JadeProcessEntity entity) {
    }

    @Override
    public void setData(final Map<PTProcessDecompteProperty, String> hashMap) {
        properties = hashMap;
    }
}
