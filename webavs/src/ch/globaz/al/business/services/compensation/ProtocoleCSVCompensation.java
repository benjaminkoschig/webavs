package ch.globaz.al.business.services.compensation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.Collection;
import java.util.HashMap;
import ch.globaz.al.business.compensation.CompensationBusinessModel;

public interface ProtocoleCSVCompensation extends JadeApplicationService {
    /**
     * G�n�re le contenu du protocole CSV des compensations.
     * 
     * @param listePrestations
     *            s�par�es par type activit�
     * @return Contenu du protocole CSV
     * @throws JadeApplicationException
     */
    public String getCSV(HashMap<String, Collection<CompensationBusinessModel>> recapsByActivites,
            HashMap<String, String> params) throws JadeApplicationException;

}
