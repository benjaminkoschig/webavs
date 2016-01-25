package ch.globaz.pegasus.businessimpl.services.models.annonce.annoncelaprams;

import ch.globaz.pegasus.business.exceptions.models.annonce.SequenceException;
import ch.globaz.pegasus.business.models.annonce.SimpleSequence;
import ch.globaz.pegasus.business.models.annonce.SimpleSequenceSearch;
import ch.globaz.pegasus.business.services.models.annonce.annoncelaprams.SimpleSequenceService;
import ch.globaz.pegasus.businessimpl.services.SimpleCrudService;

public class SimpleSequenceServiceImpl extends
        SimpleCrudService<SimpleSequenceSearch, SimpleSequence, SequenceException> implements SimpleSequenceService {

    @Override
    protected SimpleSequenceSearch newInstanceSearchModel() {
        return new SimpleSequenceSearch();
    }

}
