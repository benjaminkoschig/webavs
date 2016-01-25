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
import ch.globaz.al.business.paiement.CompabilisationPrestationContainer;
import ch.globaz.al.business.services.documents.DocumentDataContainer;
import ch.globaz.al.businessimpl.processus.BusinessTraitement;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * 
 * Exécute le paiement direct. Les prestations préparées par le traitement {@link PreparationPaiementDirectTraitement}
 * sont comptabilisées et inscrites dans la compta
 * 
 * @author jts
 * 
 */
public class PaiementDirectTraitement extends BusinessTraitement {

    /**
     * Logger contenant les éventuelles erreurs survenues pendant le traitement
     */
    private ProtocoleLogger logger = null;
    /**
     * Conteneur pour les protocoles
     */
    private JadePrintDocumentContainer[] protocoles = new JadePrintDocumentContainer[1];
    /**
     * Conteneur pour les protocoles CSV de la simulation
     */
    private ArrayList<String> protocolesCSV = new ArrayList<String>();

    /**
     * Constructeur du traitement génération
     */
    public PaiementDirectTraitement() {
        super();
    }

    @Override
    public void execute() throws JadeApplicationException, JadePersistenceException {
        String periode = getProcessusConteneur().getDataCriterias().periodeCriteria;
        String date = getProcessusConteneur().getDataCriterias().fullDateCriteria;
        String numTraitement = getTraitementPeriodiqueModel().getId();

        CompabilisationPrestationContainer comptaContainer = null;
        // dans le cas d'un partiel, on verse les prestations lié au num du processus
        if (getProcessusConteneur().isPartiel()) {
            comptaContainer = ALImplServiceLocator.getPaiementDirectService().verserPrestationsByNumProcessus(
                    getProcessusConteneur().getProcessusPeriodiqueModel().getId(), date, new ProtocoleLogger());
        } else {
            comptaContainer = ALImplServiceLocator.getPaiementDirectService().verserPrestations(periode, date,
                    new ProtocoleLogger());
        }
        DocumentDataContainer container = ALImplServiceLocator.getPaiementDirectProtocolesService()
                .genererProtocolesDefinitif(comptaContainer.getIdJournal(), date, periode, numTraitement,
                        getProcessusConteneur().getCSProcessus(), getCSTraitement(), comptaContainer.getLogger());

        protocoles[0] = container.getContainer();
        logger = container.getProtocoleLogger();

        for (int i = 0; i < container.getDocumentCSV().size(); i++) {

            if (!JadeStringUtil.isBlank(container.getDocumentCSV().get(i))) {
                protocolesCSV.add(container.getDocumentCSV().get(i));
            }

            // if (!JadeStringUtil.isBlank(container.getDocumentCSV())) {
            // this.protocolesCSV.add(container.getDocumentCSV());
            // }
        }
    }

    @Override
    protected void executeBack() {
        // DO NOTHING
    }

    @Override
    public String getCSTraitement() {
        return ALCSProcessus.NAME_TRAITEMENT_VERSEMENT_DIRECTS;
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

    // TODO à voir si rajouter
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
        return false;
    }
}
