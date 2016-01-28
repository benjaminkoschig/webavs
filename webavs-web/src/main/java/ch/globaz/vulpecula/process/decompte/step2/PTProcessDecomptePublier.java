package ch.globaz.vulpecula.process.decompte.step2;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepAfterable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.vulpecula.businessimpl.services.decompte.ImprimerDecomptesProcess;

/**
 * Process permettant l'impression des décomptes vides
 * 
 * @since Web@BMS 0.00.01
 */
public class PTProcessDecomptePublier implements JadeProcessStepInterface, JadeProcessStepAfterable {

    private List<String> decomptesToPrint;

    public PTProcessDecomptePublier() {
        decomptesToPrint = Collections.synchronizedList(new ArrayList<String>());
    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new PTProcessDecompteEntityHandlerPublier(decomptesToPrint);
    }

    @Override
    public void after(final JadeProcessStep step, final Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        try {
            ImprimerDecomptesProcess process = ImprimerDecomptesProcess.createWithIds(decomptesToPrint);
            process.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
