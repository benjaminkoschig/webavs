package ch.globaz.pegasus.business.services.models.calcul;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.calculmoissuivant.CalculMoisSuivantBuilder;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;

public interface CalculMoisSuivantService extends JadeApplicationService {

    public void updateDonneeFinancieres(List<String> idsDfForVersion, String noVersion) throws CalculException,
            DonneeFinanciereException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            PmtMensuelException, SecurityException, NoSuchMethodException, DroitException;

    public CalculMoisSuivantBuilder getCalculMoisSuivantBuilder();

}
