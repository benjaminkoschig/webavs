package ch.globaz.pegasus.businessimpl.services.models.annonce.annoncelaprams;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.annonce.SequenceException;
import ch.globaz.pegasus.business.models.annonce.SimpleSequence;
import ch.globaz.pegasus.business.models.annonce.SimpleSequenceSearch;
import ch.globaz.pegasus.business.services.models.annonce.annoncelaprams.SimpleSequenceService;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class ResolveSequence {

    // private SimpleSequenceService service

    public int resolveSequence(String domaine, String businessKey) throws SequenceException, JadePersistenceException {
        try {
            return this.resolveSequence(domaine, businessKey, PegasusImplServiceLocator.getSimpleSequenceService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SequenceException("Service not available", e);
        }
    }

    int resolveSequence(String domaine, String businessKey, SimpleSequenceService service) throws SequenceException,
            JadePersistenceException {
        SimpleSequenceSearch search = new SimpleSequenceSearch();
        SimpleSequence sequence = new SimpleSequence();
        String key = null;
        int sequ = 0;

        List<SimpleSequence> sequences = service.find(search);
        if (sequences.size() == 1) {
            sequence = sequences.get(0);
            sequ = Integer.valueOf(sequence.getSequence());
        } else if (sequences.size() > 1) {
            throw new SequenceException("Too many sequances was found");
        }

        if (!businessKey.equals(sequence.getBusinessKey())) {
            sequ = sequ + 1;

            SimpleSequence model = new SimpleSequence();
            model.setBusinessKey(key);
            model.setSequence(String.valueOf(sequ));
            model.setDomaine(domaine);
            if (sequences.size() > 0) {
                service.delete(sequence);
            }
            service.create(model);
        }
        return sequ;
    }

}
