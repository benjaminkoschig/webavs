/**
 *
 */
package ch.globaz.vulpecula.process.decompte.step1;

import globaz.jade.exception.JadePersistenceException;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.vulpecula.businessimpl.services.decompte.GenererDecompteProcessor;

/**
 * Première étape de la génération des décomptes.
 * 
 * @author sel
 * 
 */
public class PTProcessDecompteGenererVides implements JadeProcessStepInterface {
    private GenererDecompteProcessor genererDecompteProcess;

    public PTProcessDecompteGenererVides() throws JadePersistenceException {
        genererDecompteProcess = new GenererDecompteProcessor(true);

    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        PTProcessDecompteEntityHandlerGenererVides entityHandler = new PTProcessDecompteEntityHandlerGenererVides();
        entityHandler.setGenererDecompteProcess(genererDecompteProcess);
        return entityHandler;
    }
}
