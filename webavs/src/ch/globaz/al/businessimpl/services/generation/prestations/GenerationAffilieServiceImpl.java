package ch.globaz.al.businessimpl.services.generation.prestations;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.Date;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALConstProtocoles;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.services.generation.prestations.GenerationAffilieService;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextTucana;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Implémentation du service de génération de prestations permettant de générer les prestations des dossiers d'un
 * affilié
 * 
 * @author jts
 * 
 */
public class GenerationAffilieServiceImpl extends ALAbstractBusinessServiceImpl implements GenerationAffilieService {

    @Override
    public JadePrintDocumentContainer generationAffilie(String numAffilie, String periode)
            throws JadeApplicationException, JadePersistenceException {

        ContextTucana.initContext("01." + periode);

        ProtocoleLogger logger = ALImplServiceLocator.getGenerationService().generationAffilie(numAffilie, periode,
                "0", new ProtocoleLogger());

        // préparation du protocole
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ALConstProtocoles.INFO_PASSAGE, "-");
        params.put(ALConstProtocoles.INFO_PROCESSUS, "al.generation.manuelle");
        params.put(ALConstProtocoles.INFO_TRAITEMENT, "al.generation.manuelle.affilie");
        params.put(ALConstProtocoles.INFO_PERIODE, periode);
        params.put(ALConstProtocoles.INFO_AFFILIE, numAffilie);

        JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();
        pubInfo.setOwnerEmail(JadeThread.currentUserEmail());
        pubInfo.setOwnerId(JadeThread.currentUserId());

        String title = JadeThread.getMessage("al.generation.manuelle") + " - "
                + JadeThread.getMessage("al.generation.manuelle.affilie") + " " + numAffilie;
        pubInfo.setDocumentTitle(title);
        pubInfo.setDocumentSubject(title);
        pubInfo.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
        JadePrintDocumentContainer container = new JadePrintDocumentContainer();
        container.addDocument(
                ALImplServiceLocator.getProtocoleErreursGenerationService().getDocumentData(logger, params), pubInfo);

        return container;

    }
}
