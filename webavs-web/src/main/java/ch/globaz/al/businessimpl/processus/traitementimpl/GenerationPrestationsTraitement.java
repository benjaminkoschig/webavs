package ch.globaz.al.businessimpl.processus.traitementimpl;

import globaz.globall.db.BSessionUtil;
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
import java.util.GregorianCalendar;
import java.util.HashMap;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.constantes.ALConstProtocoles;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.businessimpl.processus.BusinessTraitement;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.orion.business.constantes.EBProperties;
import ch.globaz.orion.businessimpl.services.af.AfServiceImpl;

/**
 * 
 * Traitement implémentant la génération des prestations
 * 
 * @author jts
 * 
 */
public class GenerationPrestationsTraitement extends BusinessTraitement {

    /**
     * Logger contenant les erreurs survenues pendant le traitement
     */
    private ProtocoleLogger protocoleLogger = null;
    /**
     * Conteneur pour les protocoles de la génération
     */
    private JadePrintDocumentContainer protocoles[] = new JadePrintDocumentContainer[1];

    /**
     * Conteneur pour les protocoles CSV de la simulation
     */
    private ArrayList<String> protocolesCSV = new ArrayList<String>();
    /**
     * PubInfo pour l'envoi des protocoles
     */
    private JadePublishDocumentInfo pubInfo;

    /**
     * Constructeur du traitement génération
     */
    public GenerationPrestationsTraitement() {
        super();
    }

    @Override
    public void execute() throws JadeApplicationException, JadePersistenceException {
        boolean isEbusinessConnected = CommonProperties.EBUSINESS_CONNECTED.getBooleanValue();
        boolean isManagedRecapAfInEbusiness = EBProperties.RECAPAF_MANAGE_RECAP_IN_EBUSINESS.getBooleanValue();

        String periode = getProcessusConteneur().getDataCriterias().periodeCriteria;
        String type = getProcessusConteneur().getDataCriterias().cotisationCriteria;
        String numGeneration = getTraitementPeriodiqueModel().getId();
        String processus = JadeCodesSystemsUtil.getCodeLibelle(getProcessusConteneur().getCSProcessus());
        String traitement = JadeCodesSystemsUtil.getCodeLibelle(getCSTraitement());

        // clôture des récaps AF côté Ebusiness (si les récaps sont remontées dans EBusiness)
        if (isEbusinessConnected && isManagedRecapAfInEbusiness) {
            XMLGregorianCalendar anneeMoisRecap = computeAnneeMoisRecapXmlGregorian(periode);
            AfServiceImpl.cloturerRecapAf(BSessionUtil.getSessionFromThreadContext(), anneeMoisRecap);
        }

        // génération
        ProtocoleLogger logger = ALImplServiceLocator.getGenerationService().generationGlobale(periode, type,
                numGeneration, new ProtocoleLogger());

        // préparation du protocole
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ALConstProtocoles.INFO_PASSAGE, numGeneration);
        params.put(ALConstProtocoles.INFO_PROCESSUS, processus);
        params.put(ALConstProtocoles.INFO_TRAITEMENT, traitement);
        params.put(ALConstProtocoles.INFO_PERIODE, periode);

        JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();
        pubInfo.setOwnerEmail(JadeThread.currentUserEmail());
        pubInfo.setOwnerId(JadeThread.currentUserId());
        pubInfo.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
        pubInfo.setDocumentTitle(processus + " - " + traitement);
        pubInfo.setDocumentSubject(processus + " - " + traitement);
        JadePrintDocumentContainer container = new JadePrintDocumentContainer();
        container.addDocument(
                ALImplServiceLocator.getProtocoleErreursGenerationService().getDocumentData(logger, params), pubInfo);
        protocoles[0] = container;
        protocoleLogger = logger;
        String protocoleCSV = logger.toCSV();
        if (!JadeStringUtil.isBlank(protocoleCSV)) {
            protocolesCSV.add(protocoleCSV);
        }
        this.pubInfo = pubInfo;
    }

    private XMLGregorianCalendar computeAnneeMoisRecapXmlGregorian(String periode) {
        GregorianCalendar anneeMoiDateCal = new GregorianCalendar();

        String anneeMoisStr = JadeDateUtil.getFirstDateOfMonth(periode);
        Date anneeMoisDate = JadeDateUtil.getGlobazDate(anneeMoisStr);

        anneeMoiDateCal.setTime(anneeMoisDate);

        XMLGregorianCalendar anneeMoisRecap = null;

        try {
            anneeMoisRecap = DatatypeFactory.newInstance().newXMLGregorianCalendar(anneeMoiDateCal);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e.toString(), e);
        }

        return anneeMoisRecap;
    }

    @Override
    protected void executeBack() {
        // DO NOTHING
    }

    @Override
    public String getCSTraitement() {
        return ALCSProcessus.NAME_TRAITEMENT_GENERATION_GLOBALE;
    }

    @Override
    public JadePrintDocumentContainer[] getProtocole() {
        return protocoles;
    }

    @Override
    public ProtocoleLogger getProtocoleLogger() {
        return protocoleLogger;
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
        return false;
    }
}
