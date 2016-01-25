package ch.globaz.al.businessimpl.services.paiement;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.constantes.ALConstProtocoles;
import ch.globaz.al.business.exceptions.copies.ALCopieBusinessException;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.periodeAF.PeriodeAFModel;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel;
import ch.globaz.al.business.paiement.PaiementBusinessModel;
import ch.globaz.al.business.paiement.PaiementContainer;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.documents.DocumentDataContainer;
import ch.globaz.al.business.services.paiement.PaiementDirectProtocolesService;
import ch.globaz.al.business.services.paiement.PaiementDirectService;
import ch.globaz.al.business.services.paiement.ProtocoleDetaillePaiementDirectService;
import ch.globaz.al.business.services.paiement.ProtocoleErreursPaiementDirectService;
import ch.globaz.al.business.services.paiement.ProtocoleRecapitulatifPaiementDirectService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.service.ParamServiceLocator;

/**
 * Implémentation du service permettant l'impression des protocoles liés aux traitement des paiements direct
 * 
 * @author jts
 * 
 */
public class PaiementDirectProtocolesServiceImpl extends ALAbstractBusinessServiceImpl implements
        PaiementDirectProtocolesService {

    private JadePrintDocumentContainer AddDocumentData(java.util.Collection<PaiementBusinessModel> listePaiement,
            HashMap<String, String> params, ProtocoleDetaillePaiementDirectService sProtDet,
            JadePrintDocumentContainer container) throws JadePersistenceException, JadeApplicationException {

        // contrôle des paramètres
        if (listePaiement == null) {
            throw new ALProtocoleException(
                    "ProtocolDetaillePaiementDirectServiceImpl#getDocumentData: listePaiement is null");
        }
        if (params == null) {
            throw new ALProtocoleException("ProtocolDetaillePaiementDirectServiceImpl#getDocumentData: params is null");
        }

        int compteurLigneListePaiement = 0;
        Iterator it = listePaiement.iterator();

        ArrayList<PaiementBusinessModel> listePaiementTemporaire = new ArrayList<PaiementBusinessModel>();
        int compteurPrestation = 0;
        while (it.hasNext()) {

            PaiementBusinessModel paiement = (PaiementBusinessModel) it.next();
            compteurLigneListePaiement++;
            compteurPrestation++;

            listePaiementTemporaire.add(paiement);

            if ((compteurLigneListePaiement == 2000) || (listePaiement.size() == compteurPrestation)) {

                JadePublishDocumentInfo pubInfo2 = new JadePublishDocumentInfo();
                pubInfo2.setDocumentTitle(JadeThread
                        .getMessage("al.protocole.paiementDirect.publication.simulation.titreListeAllocataires"));
                pubInfo2.setDocumentSubject(JadeThread
                        .getMessage("al.protocole.paiementDirect.publication.simulation.titreListeAllocataires"));
                pubInfo2.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
                pubInfo2.setOwnerEmail(JadeThread.currentUserEmail());
                pubInfo2.setOwnerId(JadeThread.currentUserId());
                // ajoute le document au container
                container.addDocument(sProtDet.getDocumentData(listePaiementTemporaire, params), pubInfo2);
                // remet le compteur à 0 seuelement si le nombre de prestation n'eat pas atteinte
                if (listePaiement.size() > compteurPrestation) {
                    compteurLigneListePaiement = 0;
                }
                // vidage de la liste temporaire
                listePaiementTemporaire.clear();

            }

        }

        return container;
    }

    private DocumentDataContainer createProtocoleSimulation(HashMap<String, String> params,
            PaiementContainer paiementContainer) throws JadePersistenceException, JadeApplicationException {

        ProtocoleErreursPaiementDirectService sProtErr = ALImplServiceLocator
                .getProtocoleErreursPaiementDirectService();
        ProtocoleDetaillePaiementDirectService sProtDet = ALImplServiceLocator
                .getProtocoleDetaillePaiementDirectService();
        ProtocoleRecapitulatifPaiementDirectService sProtRecap = ALImplServiceLocator
                .getProtocoleRecapitulatifPaiementDirectService();

        // initialisation conteneur documents
        JadePrintDocumentContainer container = new JadePrintDocumentContainer();

        // Liste récapitulative
        JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();
        pubInfo.setDocumentTitle(JadeThread
                .getMessage("al.protocole.paiementDirect.publication.simulation.titreListeRubrique"));
        pubInfo.setDocumentSubject(JadeThread
                .getMessage("al.protocole.paiementDirect.publication.simulation.titreListeRubrique"));
        pubInfo.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
        pubInfo.setOwnerEmail(JadeThread.currentUserEmail());
        pubInfo.setOwnerId(JadeThread.currentUserId());
        container.addDocument(
                sProtRecap.getProtocoleDocumentData(paiementContainer.getPaiementRecapitulatifBusinessList(), params),
                pubInfo);

        // protocole détaillé
        JadePublishDocumentInfo pubInfo2 = new JadePublishDocumentInfo();
        pubInfo2.setDocumentTitle(JadeThread
                .getMessage("al.protocole.paiementDirect.publication.simulation.titreListeAllocataires"));
        pubInfo2.setDocumentSubject(JadeThread
                .getMessage("al.protocole.paiementDirect.publication.simulation.titreListeAllocataires"));
        pubInfo2.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
        pubInfo2.setOwnerEmail(JadeThread.currentUserEmail());
        pubInfo2.setOwnerId(JadeThread.currentUserId());
        Collection<PaiementBusinessModel> prest = paiementContainer.getPaiementBusinessList();

        String protocoleListeAllocCSV = null;

        // pdf ou csv
        ParameterModel param = new ParameterModel();
        try {

            param = ParamServiceLocator.getParameterModelService().getParameterByName(ALConstParametres.APPNAME,
                    ALConstParametres.PROTOCOLE_CSV_SIMU_PAIEMENT_DIRECT_DETAIL_ALLOC,
                    JadeDateUtil.getGlobazFormattedDate(new Date()));

        } catch (Exception e) {
            return null;
        }

        if (param.getValeurAlphaParametre().equals("true")) {
            // protocole CSV liste allocataires

            protocoleListeAllocCSV = getProtocoleSimulationCSVListesAlloc(prest, params);
        } else {

            // protocole pdf
            container.addDocument(sProtDet.getDocumentData(prest, params), pubInfo2);
        }

        // protocoles csv pour paiement supérieure à une limite
        String protocoleCSV = null;

        param = ParamServiceLocator.getParameterModelService().getParameterByName(ALConstParametres.APPNAME,
                ALConstParametres.PROTOCOLE_CSV_SIMU_PAIEMENT_DIRECT, JadeDateUtil.getGlobazFormattedDate(new Date()));

        if (param.getValeurAlphaParametre().equals("true")) {
            protocoleCSV = getProtocoleSimulationCSV(prest, params, param);
        }

        // protocole d'erreurs
        JadePublishDocumentInfo pubInfo3 = new JadePublishDocumentInfo();
        pubInfo3.setDocumentTitle(JadeThread
                .getMessage("al.protocole.paiementDirect.publication.simulation.titreProtocole"));
        pubInfo3.setDocumentSubject(JadeThread
                .getMessage("al.protocole.paiementDirect.publication.simulation.titreProtocole"));
        pubInfo3.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
        pubInfo3.setOwnerEmail(JadeThread.currentUserEmail());
        pubInfo3.setOwnerId(JadeThread.currentUserId());
        ProtocoleLogger logger = new ProtocoleLogger();

        DocumentDataContainer ddc = sProtErr.getDocumentData(ALImplServiceLocator.getPaiementDirectService()
                .checkPrestations(prest, JadeDateUtil.getGlobazFormattedDate(new Date()), logger), params);

        container.addDocument(ddc.getDocument(), pubInfo3);

        ddc.setContainer(container);
        ddc.setDocument(null);
        ArrayList<String> listCsv = new ArrayList<String>();
        if (!JadeStringUtil.isBlank(protocoleCSV)) {
            listCsv.add(protocoleCSV);
        }
        if (!JadeStringUtil.isBlank(protocoleListeAllocCSV)) {
            listCsv.add(protocoleListeAllocCSV);
        }

        ddc.setDocumentCSV(listCsv);
        return ddc;
    }

    @Override
    public DocumentDataContainer genererProtocolesDefinitif(String idJournal, String dateVersement, String periode,
            String numTraitement, String infoProcessus, String infoTraitement, ProtocoleLogger logger)
            throws JadeApplicationException, JadePersistenceException {

        // initialisation services
        PaiementDirectService sPaiement = ALImplServiceLocator.getPaiementDirectService();
        ProtocoleErreursPaiementDirectService sProtErr = ALImplServiceLocator
                .getProtocoleErreursPaiementDirectService();
        ProtocoleDetaillePaiementDirectService sProtDet = ALImplServiceLocator
                .getProtocoleDetaillePaiementDirectService();
        ProtocoleRecapitulatifPaiementDirectService sProtRecap = ALImplServiceLocator
                .getProtocoleRecapitulatifPaiementDirectService();

        // initialisation conteneur documents
        JadePrintDocumentContainer container = new JadePrintDocumentContainer();

        // paramètres
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ALConstProtocoles.INFO_PASSAGE, numTraitement);
        params.put(ALConstProtocoles.INFO_PROCESSUS, JadeCodesSystemsUtil.getCodeLibelle(infoProcessus));
        params.put(ALConstProtocoles.INFO_TRAITEMENT, JadeCodesSystemsUtil.getCodeLibelle(infoTraitement));
        params.put(ALConstProtocoles.INFO_PERIODE, periode);

        // chargement des prestations
        PaiementContainer pc = sPaiement.loadPrestationsComptabilisees(idJournal);

        // Liste récapitulative
        JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();
        pubInfo.setDocumentTitle(JadeThread.getMessage("al.protocole.paiementDirect.publication.titreListeRubrique"));
        pubInfo.setDocumentSubject(JadeThread.getMessage("al.protocole.paiementDirect.publication.titreListeRubrique"));
        pubInfo.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
        pubInfo.setOwnerEmail(JadeThread.currentUserEmail());
        pubInfo.setOwnerId(JadeThread.currentUserId());
        container.addDocument(sProtRecap.getProtocoleDocumentData(pc.getPaiementRecapitulatifBusinessList(), params),
                pubInfo);

        // protocole détaillé
        JadePublishDocumentInfo pubInfo2 = new JadePublishDocumentInfo();
        pubInfo2.setDocumentTitle(JadeThread
                .getMessage("al.protocole.paiementDirect.publication.titreListeAllocataires"));
        pubInfo2.setDocumentSubject(JadeThread
                .getMessage("al.protocole.paiementDirect.publication.titreListeAllocataires"));
        pubInfo2.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
        pubInfo2.setOwnerEmail(JadeThread.currentUserEmail());
        pubInfo2.setOwnerId(JadeThread.currentUserId());
        Collection<PaiementBusinessModel> prest = pc.getPaiementBusinessList();

        String protocoleListeAllocCSV = null;

        // pdf ou csv
        ParameterModel param = new ParameterModel();
        try {

            param = ParamServiceLocator.getParameterModelService().getParameterByName(ALConstParametres.APPNAME,
                    ALConstParametres.PROTOCOLE_CSV_SIMU_PAIEMENT_DIRECT_DETAIL_ALLOC,
                    JadeDateUtil.getGlobazFormattedDate(new Date()));

        } catch (Exception e) {
            return null;
        }

        if (param.getValeurAlphaParametre().equals("true")) {
            // protocole CSV

            protocoleListeAllocCSV = getProtocoleSimulationCSVListesAlloc(prest, params);
        } else {

            // protocole pdf
            container.addDocument(sProtDet.getDocumentData(prest, params), pubInfo2);
        }

        // protocole d'erreurs
        JadePublishDocumentInfo pubInfo3 = new JadePublishDocumentInfo();
        pubInfo3.setDocumentTitle(JadeThread.getMessage("al.protocole.paiementDirect.publication.titreProtocole"));
        pubInfo3.setDocumentSubject(JadeThread.getMessage("al.protocole.paiementDirect.publication.titreProtocole"));
        pubInfo3.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
        pubInfo3.setOwnerEmail(JadeThread.currentUserEmail());
        pubInfo3.setOwnerId(JadeThread.currentUserId());
        DocumentDataContainer ddc = sProtErr.getDocumentData(
                sPaiement.checkPrestations(prest, JadeDateUtil.getGlobazFormattedDate(new Date()), logger), params);

        container.addDocument(ddc.getDocument(), pubInfo3);
        ddc.setContainer(container);
        ddc.setDocument(null);

        ArrayList<String> listCsv = new ArrayList<String>();

        if (!JadeStringUtil.isBlank(protocoleListeAllocCSV)) {
            listCsv.add(protocoleListeAllocCSV);
        }

        ddc.setDocumentCSV(listCsv);

        return ddc;
    }

    @Override
    public DocumentDataContainer genererProtocolesSimulation(String periode, String numTraitement,
            String infoProcessus, String infoTraitement) throws JadePersistenceException, JadeApplicationException {

        // initialisation services
        PaiementDirectService sPaiement = ALImplServiceLocator.getPaiementDirectService();

        // paramètres
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ALConstProtocoles.INFO_PASSAGE, numTraitement);
        params.put(ALConstProtocoles.INFO_PROCESSUS, JadeCodesSystemsUtil.getCodeLibelle(infoProcessus));
        params.put(ALConstProtocoles.INFO_TRAITEMENT, JadeCodesSystemsUtil.getCodeLibelle(infoTraitement));
        params.put(ALConstProtocoles.INFO_PERIODE, periode);

        // chargement des prestations
        PaiementContainer pc = sPaiement.loadPrestationsSimulation(periode);

        return createProtocoleSimulation(params, pc);
    }

    @Override
    public DocumentDataContainer genererProtocolesSimulationByNumProcessus(String idProcessus, String numTraitement,
            String infoProcessus, String infoTraitement) throws JadePersistenceException, JadeApplicationException {

        // initialisation services
        PaiementDirectService sPaiement = ALImplServiceLocator.getPaiementDirectService();

        ProcessusPeriodiqueModel processus = ALServiceLocator.getProcessusPeriodiqueModelService().read(idProcessus);
        PeriodeAFModel periode = ALServiceLocator.getPeriodeAFModelService().read(processus.getIdPeriode());

        // paramètres
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ALConstProtocoles.INFO_PASSAGE, numTraitement);
        params.put(ALConstProtocoles.INFO_PROCESSUS, JadeCodesSystemsUtil.getCodeLibelle(infoProcessus));
        params.put(ALConstProtocoles.INFO_TRAITEMENT, JadeCodesSystemsUtil.getCodeLibelle(infoTraitement));
        params.put(ALConstProtocoles.INFO_PERIODE, periode.getDatePeriode());

        // chargement des prestations
        PaiementContainer pc = sPaiement.loadPrestationsSimulationByNumProcessus(idProcessus);

        return createProtocoleSimulation(params, pc);

    }

    protected String getProtocoleSimulationCSV(Collection<PaiementBusinessModel> prest, HashMap<String, String> params,
            ParameterModel param) throws JadeApplicationException, JadeApplicationServiceNotAvailableException {

        try {

            return ALImplServiceLocator.getProtocoleCSVPaiementDirect().getCSVMontantSuperieurALimite(prest,
                    param.getValeurNumParametre(), params);

        } catch (JadeApplicationException e) {
            throw new ALCopieBusinessException(
                    "PaiementDirectProtocolesServiceImpl#getProtocoleSimulationCSV : Impossible de récupérer le type de copie à utiliser",
                    e);
        }

    }

    protected String getProtocoleSimulationCSVListesAlloc(Collection<PaiementBusinessModel> prest,
            HashMap<String, String> params) throws JadeApplicationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        try {
            return ALImplServiceLocator.getProtocoleCSVPaiementDirect().getCSVListeAllocataire(prest, params);

        } catch (JadeApplicationException e) {
            throw new ALCopieBusinessException(
                    "PaiementDirectProtocolesServiceImpl#getProtocoleSimulationCSVListesAlloc : Impossible de récupérer le type de copie à utiliser",
                    e);
        }
    }
}
