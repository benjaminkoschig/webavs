package ch.globaz.al.businessimpl.processus.traitementimpl;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.ArrayList;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.documents.DocumentDataContainer;
import ch.globaz.al.businessimpl.processus.BusinessTraitement;

/**
 * 
 * Traitement implémentant la génération des prestations
 * 
 * @author jts
 * 
 */
public class SimulationCompensationTraitement extends BusinessTraitement {
    /**
     * Conteneur pour les protocoles de la simulation
     */
    private JadePrintDocumentContainer[] protocoles = new JadePrintDocumentContainer[1];

    /**
     * Conteneur pour les protocoles CSV de la simulation
     */
    private ArrayList<String> protocolesCSV = new ArrayList<String>();
    private JadePublishDocumentInfo pubInfo;

    /**
     * Constructeur
     */
    public SimulationCompensationTraitement() {
        super();
    }

    @Override
    public void execute() throws JadePersistenceException, JadeApplicationException {

        String periode = getProcessusConteneur().getDataCriterias().periodeCriteria;
        String type = getProcessusConteneur().getDataCriterias().cotisationCriteria;
        String numPassage = getTraitementPeriodiqueModel().getId();
        // on défini les informations réutilisées pour la publication de l'éventuel CSV dans le point d'entrée
        // (ALTraitementLancementProcess)
        pubInfo = new JadePublishDocumentInfo();
        pubInfo.setOwnerEmail(JadeThread.currentUserEmail());
        pubInfo.setOwnerId(JadeThread.currentUserId());
        StringBuffer sbTitreMail = new StringBuffer(JadeCodesSystemsUtil.getCodeLibelle(getProcessusConteneur()
                .getCSProcessus())).append(", ").append(JadeCodesSystemsUtil.getCodeLibelle(getCSTraitement()))
                .append(" ");
        pubInfo.setDocumentTitle(sbTitreMail.toString()
                + JadeThread.getMessage("al.protocole.compensation.publication.simulation.titreListeAffilies"));
        pubInfo.setDocumentSubject(sbTitreMail.toString()
                + JadeThread.getMessage("al.protocole.compensation.publication.simulation.titreListeAffilies"));

        if (getProcessusConteneur().isPartiel()) {
            DocumentDataContainer dataContainer = ALServiceLocator.getCompensationFactureProtocolesService()
                    .genererProtocolesSimulationByNumProcessus(
                            getProcessusConteneur().getProcessusPeriodiqueModel().getId(), type, numPassage,
                            getProcessusConteneur().getCSProcessus(), getCSTraitement());
            protocoles[0] = dataContainer.getContainer();

            // this.protocolesCSV.add(dataContainer.getDocumentCSV());

            for (int i = 0; i < dataContainer.getDocumentCSV().size(); i++) {

                if (!JadeStringUtil.isBlank(dataContainer.getDocumentCSV().get(i))) {
                    protocolesCSV.add(dataContainer.getDocumentCSV().get(i));
                }
            }

        } else {

            DocumentDataContainer dataContainer = ALServiceLocator.getCompensationFactureProtocolesService()
                    .genererProtocolesSimulation(periode, type, numPassage, getProcessusConteneur().getCSProcessus(),
                            getCSTraitement());

            protocoles[0] = dataContainer.getContainer();

            for (int i = 0; i < dataContainer.getDocumentCSV().size(); i++) {

                if (!JadeStringUtil.isBlank(dataContainer.getDocumentCSV().get(i))) {
                    protocolesCSV.add(dataContainer.getDocumentCSV().get(i));
                }
            }

            // this.protocolesCSV.add(dataContainer.getDocumentCSV());

        }
    }

    @Override
    protected void executeBack() {
        // DO NOTHING
    }

    @Override
    public String getCSTraitement() {
        return ALCSProcessus.NAME_TRAITEMENT_SIMULATION_COMPENSATION;
    }

    @Override
    public JadePrintDocumentContainer[] getProtocole() {
        return protocoles;
    }

    @Override
    public ProtocoleLogger getProtocoleLogger() {
        // Pas de logger pour ce traitement
        return null;
    }

    @Override
    public ArrayList<String> getProtocolesCSV() {

        return protocolesCSV;
    }

    @Override
    public JadePublishDocumentInfo getPubInfo() {
        return pubInfo;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }
}
