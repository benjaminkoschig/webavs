package ch.globaz.amal.business.services.models.formule;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.envoi.business.exceptions.models.parametrageEnvoi.SignetException;
import ch.globaz.envoi.business.models.parametrageEnvoi.SimpleSignetModelSearch;

public interface FormuleSignetsService extends JadeApplicationService {
    /**
     * Service qui va retourner la liste complète des signets afin de générer un &lt;SELECT&gt; sur une page AJAX
     * 
     * @param param
     * @return
     * @throws SignetException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public SimpleSignetModelSearch getListeSignets(String param) throws SignetException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;
}
