package ch.globaz.pegasus.business.services.models.mutation;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.MutationException;
import ch.globaz.pegasus.business.vo.lot.MutationPCA;

public interface MutationService extends JadeApplicationService {

    public List<MutationPCA> findMutationMontantPCA(String date) throws JadePersistenceException, MutationException;

    public List<MutationPCA> findMutationMontantPCAnew(String date) throws JadePersistenceException, MutationException;

    public List<MutationPCA> findOldAugmentationFutur(String dateMonth) throws MutationException,
            JadePersistenceException;
}
