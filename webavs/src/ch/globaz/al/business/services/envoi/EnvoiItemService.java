/**
 * 
 */
package ch.globaz.al.business.services.envoi;

import globaz.globall.parameters.FWParametersCode;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.HashMap;
import ch.globaz.al.business.models.envoi.EnvoiTemplateComplexModel;

/**
 * Service destiné à la gestion des envois pour les lettres au poste de travail
 * 
 * @author dhi
 * 
 */
public interface EnvoiItemService extends JadeApplicationService {

    public void changeEnvoiStatus(String idEnvoiItem);

    public HashMap<String, Object> generateWordFile(String idDossier, String idFormule);

    public ArrayList<String> getAllClassList(String basePackage);

    public ArrayList<String> getAllMethodList(String classFullName);

    public ArrayList<FWParametersCode> getAvailableDocumentsCSList(String idDossier);

    public ArrayList<EnvoiTemplateComplexModel> getAvailableDocumentsList(String idDossier)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException, JadePersistenceException;

}
