package ch.globaz.al.businessimpl.processus.traitementimpl;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.ArrayList;
import java.util.Date;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.services.documents.DocumentDataContainer;
import ch.globaz.al.businessimpl.processus.BusinessTraitement;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * 
 * Traitement implémentant la génération des prestations
 * 
 * @author jts
 * 
 */
public class SimulationPaiementDirectTraitement extends BusinessTraitement {

    /**
     * Logger contenant les éventuelles erreurs survenues pendant le traitement
     */
    private ProtocoleLogger logger = null;
    /**
     * Conteneur pour les protocoles de la simulation
     */
    private JadePrintDocumentContainer[] protocoles = new JadePrintDocumentContainer[1];

    /**
     * Conteneur pour les protocoles CSV de la simulation
     */
    private ArrayList<String> protocolesCSV = new ArrayList<String>();

    /**
     * Constructeur
     */
    public SimulationPaiementDirectTraitement() {
        super();
    }

    @Override
    public void execute() throws JadePersistenceException, JadeApplicationException {
        DocumentDataContainer container = null;
        if (getProcessusConteneur().isPartiel()) {
            container = ALImplServiceLocator.getPaiementDirectProtocolesService()
                    .genererProtocolesSimulationByNumProcessus(
                            getProcessusConteneur().getProcessusPeriodiqueModel().getId(),
                            getTraitementPeriodiqueModel().getId(), getProcessusConteneur().getCSProcessus(),
                            getCSTraitement());
        } else {
            container = ALImplServiceLocator.getPaiementDirectProtocolesService().genererProtocolesSimulation(
                    getProcessusConteneur().getDataCriterias().periodeCriteria, getTraitementPeriodiqueModel().getId(),
                    getProcessusConteneur().getCSProcessus(), getCSTraitement());
        }

        protocoles[0] = container.getContainer();
        logger = container.getProtocoleLogger();
        for (int i = 0; i < container.getDocumentCSV().size(); i++) {

            if (!JadeStringUtil.isBlank(container.getDocumentCSV().get(i))) {
                protocolesCSV.add(container.getDocumentCSV().get(i));
            }
        }

    }

    @Override
    protected void executeBack() {
        // DO NOTHING
        JadeThread.logInfo(SimulationPaiementDirectTraitement.class.getName(),
                "SimulationPaiementDirectTraitement#executeBack");
    }

    @Override
    public String getCSTraitement() {
        return ALCSProcessus.NAME_TRAITEMENT_SIMULATION_VERSEMENT_DIRECTS;
    }

    @Override
    public JadePrintDocumentContainer[] getProtocole() {
        return protocoles;
    }

    @Override
    public ProtocoleLogger getProtocoleLogger() {
        return logger;
    }

    @Override
    public ArrayList<String> getProtocolesCSV() {
        return protocolesCSV;
    }

    @Override
    public JadePublishDocumentInfo getPubInfo() {
        String processus = JadeCodesSystemsUtil.getCodeLibelle(getProcessusConteneur().getCSProcessus());
        String traitement = JadeCodesSystemsUtil.getCodeLibelle(getCSTraitement());

        JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();
        pubInfo.setOwnerId(JadeThread.currentUserId());
        pubInfo.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
        pubInfo.setOwnerEmail(JadeThread.currentUserEmail());
        pubInfo.setDocumentTitle(processus + " - " + traitement);
        pubInfo.setDocumentSubject(processus + " - " + traitement);

        return pubInfo;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }
}
