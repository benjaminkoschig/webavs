package ch.globaz.al.businessimpl.services.compensation;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import ch.globaz.al.business.compensation.CompensationBusinessModel;
import ch.globaz.al.business.compensation.CompensationRecapitulatifBusinessModel;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.constantes.ALConstProtocoles;
import ch.globaz.al.business.exceptions.prestations.ALCompensationPrestationException;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.periodeAF.PeriodeAFModel;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.compensation.CompensationFactureProtocolesService;
import ch.globaz.al.business.services.compensation.CompensationFactureService;
import ch.globaz.al.business.services.compensation.ProtocoleDetailleCompensationService;
import ch.globaz.al.business.services.compensation.ProtocoleErreursCompensationService;
import ch.globaz.al.business.services.compensation.ProtocoleRecapitulatifCompensationService;
import ch.globaz.al.business.services.documents.DocumentDataContainer;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.param.business.exceptions.ParamException;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.service.ParamServiceLocator;

/**
 * Implémentation des services permettant l'impression des protocoles liés à la compensation sur facture
 * 
 * @author jts
 * 
 */
public class CompensationFactureProtocolesServiceImpl extends ALAbstractBusinessServiceImpl implements
        CompensationFactureProtocolesService {

    @Override
    public DocumentDataContainer genererProtocolesDefinitif(String idPassage, String dateFacturation, String periode,
            String typeCoti, String email, ProtocoleLogger logger) throws JadeApplicationException,
            JadePersistenceException {

        if (!JadeNumericUtil.isNumericPositif(idPassage)) {
            throw new ALCompensationPrestationException(
                    "CompensationFactureProtocolesServiceImpl#genererProtocolesDefinitif : idPassage is null, zero or empty");
        }

        if (!JadeDateUtil.isGlobazDate(dateFacturation)) {
            throw new ALCompensationPrestationException(
                    "CompensationFactureProtocolesServiceImpl#genererProtocolesDefinitif : dateFacturation is not a valid date");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(periode)) {
            throw new ALCompensationPrestationException(
                    "CompensationFactureProtocolesServiceImpl#genererProtocolesDefinitif : dateFacturation is not a valid date");
        }

        if (!ALConstPrestations.TYPE_INDIRECT_GROUPE.equals(typeCoti)
                && !ALConstPrestations.TYPE_COT_PAR.equals(typeCoti)
                && !ALConstPrestations.TYPE_COT_PERS.equals(typeCoti)) {
            throw new ALCompensationPrestationException(
                    "CompensationFactureProtocolesServiceImpl#genererProtocolesDefinitif : " + typeCoti
                            + " is not valid");
        }

        if (JadeStringUtil.isBlank(email)) {
            throw new ALCompensationPrestationException(
                    "CompensationFactureProtocolesServiceImpl#genererProtocolesDefinitif"
                            + JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                                    "al.checkParam.common.param.undefined", new String[] { "email" }));
        }

        if (logger == null) {
            throw new ALCompensationPrestationException(
                    "CompensationFactureProtocolesServiceImpl#genererProtocolesDefinitif : logger is null");
        }

        // initialisation services
        CompensationFactureService sCompFacture = ALImplServiceLocator.getCompensationFactureService();
        ProtocoleErreursCompensationService sProtErrComp = ALImplServiceLocator
                .getProtocoleErreursCompensationService();
        ProtocoleDetailleCompensationService sProtDet = ALImplServiceLocator.getProtocoleDetailleCompensationService();
        ProtocoleRecapitulatifCompensationService sProtRecap = ALImplServiceLocator
                .getProtocolesRecapitulatifCompensationService();

        // types d'activités à traiter
        HashSet<String> activites = ALImplServiceLocator.getDossierBusinessService().getActivitesToProcess(typeCoti);

        // initialisation conteneur documents
        JadePrintDocumentContainer container = new JadePrintDocumentContainer();
        JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();
        pubInfo.setOwnerEmail(email);
        pubInfo.setOwnerId(JadeThread.currentUserId());
        StringBuffer sbTitreMail = new StringBuffer();

        // paramètres
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ALConstProtocoles.INFO_PASSAGE, idPassage);
        if (ALConstPrestations.TYPE_COT_PAR.equals(typeCoti)) {
            params.put(ALConstProtocoles.INFO_PROCESSUS,
                    JadeCodesSystemsUtil.getCodeLibelle(ALCSProcessus.NAME_PROCESSUS_COMPENSATION_PAR));
            sbTitreMail.append(JadeCodesSystemsUtil.getCodeLibelle(ALCSProcessus.NAME_PROCESSUS_COMPENSATION_PAR))
                    .append(", ");
        } else if (ALConstPrestations.TYPE_COT_PAR.equals(typeCoti)) {
            params.put(ALConstProtocoles.INFO_PROCESSUS,
                    JadeCodesSystemsUtil.getCodeLibelle(ALCSProcessus.NAME_PROCESSUS_COMPENSATION_PERS));
            sbTitreMail.append(JadeCodesSystemsUtil.getCodeLibelle(ALCSProcessus.NAME_PROCESSUS_COMPENSATION_PERS))
                    .append(", ");
        } else {
            params.put(ALConstProtocoles.INFO_PROCESSUS,
                    JadeCodesSystemsUtil.getCodeLibelle(ALCSProcessus.NAME_PROCESSUS_COMPENSATION));
            sbTitreMail.append(JadeCodesSystemsUtil.getCodeLibelle(ALCSProcessus.NAME_PROCESSUS_COMPENSATION)).append(
                    ", ");
        }

        sbTitreMail.append(JadeCodesSystemsUtil.getCodeLibelle(ALCSProcessus.NAME_TRAITEMENT_COMPENSATION)).append(" ");
        params.put(ALConstProtocoles.INFO_TRAITEMENT,
                JadeCodesSystemsUtil.getCodeLibelle(ALCSProcessus.NAME_TRAITEMENT_COMPENSATION));
        params.put(ALConstProtocoles.INFO_PERIODE, periode);

        /*
         * Liste récapitulative
         */
        pubInfo.setDocumentTitle(sbTitreMail.toString()
                + JadeThread.getMessage("al.protocole.compensation.publication.titreListeRubrique"));
        pubInfo.setDocumentSubject(sbTitreMail.toString()
                + JadeThread.getMessage("al.protocole.compensation.publication.titreListeRubrique"));
        pubInfo.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
        Collection<CompensationRecapitulatifBusinessModel> rubriques = sCompFacture.loadRubriquesDefinitif(idPassage);
        container.addDocument(sProtRecap.getProtocoleDocumentData(rubriques, params), pubInfo);

        /*
         * Liste(s) détaillée(s)
         */
        JadePublishDocumentInfo pubInfo2 = pubInfo.createCopy();
        pubInfo2.setDocumentTitle(sbTitreMail.toString()
                + JadeThread.getMessage("al.protocole.compensation.publication.titreListeAllocataires"));
        pubInfo2.setDocumentSubject(sbTitreMail.toString()
                + JadeThread.getMessage("al.protocole.compensation.publication.titreListeAllocataires"));
        pubInfo2.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));

        /*
         * protocole d'erreurs
         */
        JadePublishDocumentInfo pubInfo3 = pubInfo.createCopy();
        pubInfo3.setDocumentTitle(sbTitreMail.toString()
                + JadeThread.getMessage("al.protocole.compensation.publication.titreProtocole"));
        pubInfo3.setDocumentSubject(sbTitreMail.toString()
                + JadeThread.getMessage("al.protocole.compensation.publication.titreProtocole"));
        pubInfo3.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));

        HashMap<String, Collection<CompensationBusinessModel>> recapsByActivites = new HashMap<String, Collection<CompensationBusinessModel>>();

        for (Iterator<String> it = activites.iterator(); it.hasNext();) {
            String activite = it.next();
            HashSet<String> set = new HashSet<String>();
            set.add(activite);
            recapsByActivites.put(activite, sCompFacture.loadRecapsDefinitif(idPassage, set, true));

        }

        if ("1".equals((ParamServiceLocator.getParameterModelService().getParameterByName(ALConstParametres.APPNAME,
                ALConstParametres.IMPRIMER_ACTIVITES_SEP, "01." + periode)).getValeurAlphaParametre())) {

            for (String recapActivite : recapsByActivites.keySet()) {

                if (!recapsByActivites.get(recapActivite).isEmpty()) {
                    // protocole détaillé
                    container.addDocument(sProtDet.getDocumentData(recapsByActivites.get(recapActivite), params),
                            pubInfo2);
                    // protocole erreur
                    container.addDocument(sProtErrComp.getDocumentData(
                            sCompFacture.checkRecaps(recapsByActivites.get(recapActivite), new ProtocoleLogger()),
                            params), pubInfo3);
                }
            }

        } else {

            Collection<CompensationBusinessModel> recaps = new ArrayList<CompensationBusinessModel>();
            for (String recapActivite : recapsByActivites.keySet()) {
                if (!recapsByActivites.get(recapActivite).isEmpty()) {
                    recaps.addAll(recapsByActivites.get(recapActivite));
                }

            }

            // protocole détaillé
            ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName(
                    ALConstParametres.APPNAME, ALConstParametres.PROTOCOLE_CSV_COMPENSATION_LISTE_AFFILIE,
                    JadeDateUtil.getGlobazFormattedDate(new Date()));

            if (!ALConstProtocoles.IMPRIMER_PROTOCOLE_CSV.equals(param.getValeurAlphaParametre())) {
                container.addDocument(sProtDet.getDocumentData(recaps, params), pubInfo2);
            }

            // protocole erreur
            container.addDocument(
                    sProtErrComp.getDocumentData(sCompFacture.checkRecaps(recaps, logger), params),
                    pubInfo3);

        }

        DocumentDataContainer datacontainer = new DocumentDataContainer();
        datacontainer.setContainer(container);
        ArrayList<String> docCsv = new ArrayList<String>();

        docCsv.add(getProtocoleCSVListeAffilie(recapsByActivites, params));

        datacontainer.setDocumentCSV(docCsv);

        return datacontainer;

    }

    @Override
    public DocumentDataContainer genererProtocolesSimulation(String periode, String typeCoti, String numTraitement,
            String infoProcessus, String infoTraitement) throws JadePersistenceException, JadeApplicationException {
        // initialisation services
        CompensationFactureService sCompFacture = ALImplServiceLocator.getCompensationFactureService();
        ProtocoleErreursCompensationService sProtErrComp = ALImplServiceLocator
                .getProtocoleErreursCompensationService();
        ProtocoleDetailleCompensationService sProtDet = ALImplServiceLocator.getProtocoleDetailleCompensationService();
        ProtocoleRecapitulatifCompensationService sProtRecap = ALImplServiceLocator
                .getProtocolesRecapitulatifCompensationService();

        // types d'activités à traiter
        HashSet<String> activites = ALImplServiceLocator.getDossierBusinessService().getActivitesToProcess(typeCoti);

        // initialisation conteneur documents
        JadePrintDocumentContainer container = new JadePrintDocumentContainer();
        JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();
        pubInfo.setOwnerEmail(JadeThread.currentUserEmail());
        pubInfo.setOwnerId(JadeThread.currentUserId());
        // paramètres
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ALConstProtocoles.INFO_PASSAGE, numTraitement);
        params.put(ALConstProtocoles.INFO_PROCESSUS, JadeCodesSystemsUtil.getCodeLibelle(infoProcessus));
        params.put(ALConstProtocoles.INFO_TRAITEMENT, JadeCodesSystemsUtil.getCodeLibelle(infoTraitement));
        params.put(ALConstProtocoles.INFO_PERIODE, periode);

        StringBuffer sbTitreMail = new StringBuffer(JadeCodesSystemsUtil.getCodeLibelle(infoProcessus)).append(", ")
                .append(JadeCodesSystemsUtil.getCodeLibelle(infoTraitement)).append(" ");
        /*
         * Liste récapitulative
         */
        pubInfo.setDocumentTitle(sbTitreMail.toString()
                + JadeThread.getMessage("al.protocole.compensation.publication.simulation.titreListeRubrique"));
        pubInfo.setDocumentSubject(sbTitreMail.toString()
                + JadeThread.getMessage("al.protocole.compensation.publication.simulation.titreListeRubrique"));

        Collection<CompensationRecapitulatifBusinessModel> rubriques = sCompFacture.loadRubriquesSimulation(periode,
                typeCoti);
        container.addDocument(sProtRecap.getProtocoleDocumentData(rubriques, params), pubInfo);

        /*
         * Liste(s) détaillée(s)
         */
        JadePublishDocumentInfo pubInfo2 = pubInfo.createCopy();
        pubInfo2.setDocumentTitle(sbTitreMail.toString()
                + JadeThread.getMessage("al.protocole.compensation.publication.simulation.titreListeAffilies"));
        pubInfo2.setDocumentSubject(sbTitreMail.toString()
                + JadeThread.getMessage("al.protocole.compensation.publication.simulation.titreListeAffilies"));

        /*
         * protocole d'erreurs
         */
        JadePublishDocumentInfo pubInfo3 = pubInfo.createCopy();
        pubInfo3.setDocumentTitle(sbTitreMail.toString()
                + JadeThread.getMessage("al.protocole.compensation.publication.simulation.titreProtocole"));
        pubInfo3.setDocumentSubject(sbTitreMail.toString()
                + JadeThread.getMessage("al.protocole.compensation.publication.simulation.titreProtocole"));

        HashMap<String, Collection<CompensationBusinessModel>> recapsByActivites = new HashMap<String, Collection<CompensationBusinessModel>>();

        for (Iterator<String> it = activites.iterator(); it.hasNext();) {
            String activite = it.next();
            HashSet<String> set = new HashSet<String>();
            set.add(activite);
            recapsByActivites.put(activite, sCompFacture.loadRecapsSimulation(periode, set, true));

        }

        if ("1".equals((ParamServiceLocator.getParameterModelService().getParameterByName(ALConstParametres.APPNAME,
                ALConstParametres.IMPRIMER_ACTIVITES_SEP, "01." + periode)).getValeurAlphaParametre())) {

            for (String recapActivite : recapsByActivites.keySet()) {

                if (!recapsByActivites.get(recapActivite).isEmpty()) {

                    container.addDocument(sProtDet.getDocumentData(recapsByActivites.get(recapActivite), params),
                            pubInfo2);

                    container.addDocument(sProtErrComp.getDocumentData(
                            sCompFacture.checkRecaps(recapsByActivites.get(recapActivite), new ProtocoleLogger()),
                            params), pubInfo3);
                }
            }

        } else {

            Collection<CompensationBusinessModel> recaps = new ArrayList<CompensationBusinessModel>();
            for (String recapActivite : recapsByActivites.keySet()) {
                if (!recapsByActivites.get(recapActivite).isEmpty()) {
                    recaps.addAll(recapsByActivites.get(recapActivite));
                }
            }

            /*
             * protocole détaillé
             */
            ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName(
                    ALConstParametres.APPNAME, ALConstParametres.PROTOCOLE_CSV_COMPENSATION_LISTE_AFFILIE,
                    JadeDateUtil.getGlobazFormattedDate(new Date()));

            if (!ALConstProtocoles.IMPRIMER_PROTOCOLE_CSV.equals(param.getValeurAlphaParametre())) {
                container.addDocument(sProtDet.getDocumentData(recaps, params), pubInfo2);
            }

            container.addDocument(
                    sProtErrComp.getDocumentData(sCompFacture.checkRecaps(recaps, new ProtocoleLogger()), params),
                    pubInfo3);
        }

        DocumentDataContainer datacontainer = new DocumentDataContainer();
        datacontainer.setContainer(container);

        ArrayList<String> docCsv = new ArrayList<String>();

        docCsv.add(getProtocoleCSVListeAffilie(recapsByActivites, params));

        datacontainer.setDocumentCSV(docCsv);

        // datacontainer.setDocumentCSV(this.getProtocoleCSVListeAffilie(recapsByActivites, params));

        return datacontainer;
    }

    @Override
    public DocumentDataContainer genererProtocolesSimulationByNumProcessus(String idProcessus, String typeCoti,
            String numTraitement, String infoProcessus, String infoTraitement) throws JadePersistenceException,
            JadeApplicationException {

        // initialisation services
        CompensationFactureService sCompFacture = ALImplServiceLocator.getCompensationFactureService();
        ProtocoleErreursCompensationService sProtErrComp = ALImplServiceLocator
                .getProtocoleErreursCompensationService();
        ProtocoleDetailleCompensationService sProtDet = ALImplServiceLocator.getProtocoleDetailleCompensationService();
        ProtocoleRecapitulatifCompensationService sProtRecap = ALImplServiceLocator
                .getProtocolesRecapitulatifCompensationService();

        // types d'activités à traiter
        HashSet<String> activites = ALImplServiceLocator.getDossierBusinessService().getActivitesToProcess(typeCoti);

        // initialisation conteneur documents
        JadePrintDocumentContainer container = new JadePrintDocumentContainer();
        JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();
        pubInfo.setOwnerEmail(JadeThread.currentUserEmail());
        pubInfo.setOwnerId(JadeThread.currentUserId());
        ProcessusPeriodiqueModel processus = ALServiceLocator.getProcessusPeriodiqueModelService().read(idProcessus);
        PeriodeAFModel periode = ALServiceLocator.getPeriodeAFModelService().read(processus.getIdPeriode());

        // paramètres
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ALConstProtocoles.INFO_PASSAGE, numTraitement);
        params.put(ALConstProtocoles.INFO_PROCESSUS, JadeCodesSystemsUtil.getCodeLibelle(infoProcessus));
        params.put(ALConstProtocoles.INFO_TRAITEMENT, JadeCodesSystemsUtil.getCodeLibelle(infoTraitement));
        params.put(ALConstProtocoles.INFO_PERIODE, periode.getDatePeriode());

        /*
         * Liste récapitulative
         */
        pubInfo.setDocumentTitle(JadeThread
                .getMessage("al.protocole.compensation.publication.simulation.titreListeRubrique"));
        pubInfo.setDocumentSubject(JadeThread
                .getMessage("al.protocole.compensation.publication.simulation.titreListeRubrique"));

        Collection<CompensationRecapitulatifBusinessModel> rubriques = sCompFacture
                .loadRubriquesSimulationByNumProcessus(idProcessus);
        container.addDocument(sProtRecap.getProtocoleDocumentData(rubriques, params), pubInfo);

        /*
         * Liste(s) détaillée(s)
         */
        JadePublishDocumentInfo pubInfo2 = pubInfo.createCopy();
        pubInfo2.setDocumentTitle(JadeThread
                .getMessage("al.protocole.compensation.publication.simulation.titreListeAffilies"));
        pubInfo2.setDocumentSubject(JadeThread
                .getMessage("al.protocole.compensation.publication.simulation.titreListeAffilies"));

        // TODO (lot 2) ne génère pas le protocole d'erreur en cas
        // d'impression séparés
        /*
         * protocole d'erreurs
         */
        JadePublishDocumentInfo pubInfo3 = pubInfo.createCopy();
        pubInfo3.setDocumentTitle(JadeThread
                .getMessage("al.protocole.compensation.publication.simulation.titreProtocole"));
        pubInfo3.setDocumentSubject(JadeThread
                .getMessage("al.protocole.compensation.publication.simulation.titreProtocole"));

        HashMap<String, Collection<CompensationBusinessModel>> recapsByActivites = new HashMap<String, Collection<CompensationBusinessModel>>();

        for (Iterator<String> it = activites.iterator(); it.hasNext();) {
            String activite = it.next();
            HashSet<String> set = new HashSet<String>();
            set.add(activite);
            recapsByActivites.put(activite, sCompFacture.loadRecapsSimulationByNumProcessus(idProcessus, set, true));

        }

        if ("1".equals((ParamServiceLocator.getParameterModelService().getParameterByName(ALConstParametres.APPNAME,
                ALConstParametres.IMPRIMER_ACTIVITES_SEP, "01." + periode.getDatePeriode())).getValeurAlphaParametre())) {

            for (String recapActivite : recapsByActivites.keySet()) {

                // recapsByActivites.get(recapActivite);
                if (!recapsByActivites.get(recapActivite).isEmpty()) {

                    container.addDocument(sProtDet.getDocumentData(recapsByActivites.get(recapActivite), params),
                            pubInfo2);

                    container.addDocument(sProtErrComp.getDocumentData(
                            sCompFacture.checkRecaps(recapsByActivites.get(recapActivite), new ProtocoleLogger()),
                            params), pubInfo3);
                }
            }

        } else {

            Collection<CompensationBusinessModel> recaps = new ArrayList<CompensationBusinessModel>();
            for (String recapActivite : recapsByActivites.keySet()) {
                if (!recapsByActivites.get(recapActivite).isEmpty()) {
                    recaps.addAll(recapsByActivites.get(recapActivite));
                }

            }

            /*
             * protocole détaillé
             */
            ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName(
                    ALConstParametres.APPNAME, ALConstParametres.PROTOCOLE_CSV_COMPENSATION_LISTE_AFFILIE,
                    JadeDateUtil.getGlobazFormattedDate(new Date()));

            if (!ALConstProtocoles.IMPRIMER_PROTOCOLE_CSV.equals(param.getValeurAlphaParametre())) {
                container.addDocument(sProtDet.getDocumentData(recaps, params), pubInfo2);
            }

            container.addDocument(
                    sProtErrComp.getDocumentData(sCompFacture.checkRecaps(recaps, new ProtocoleLogger()), params),
                    pubInfo3);
        }

        DocumentDataContainer datacontainer = new DocumentDataContainer();
        datacontainer.setContainer(container);

        ArrayList<String> docCsv = new ArrayList<String>();
        docCsv.add(getProtocoleCSVListeAffilie(recapsByActivites, params));
        datacontainer.setDocumentCSV(docCsv);

        return datacontainer;

    }

    @Override
    public String getProtocoleCSVListeAffilie(HashMap<String, Collection<CompensationBusinessModel>> recapsByActivites,
            HashMap<String, String> params) throws JadeApplicationException, JadePersistenceException {

        try {
            ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName(
                    ALConstParametres.APPNAME, ALConstParametres.PROTOCOLE_CSV_COMPENSATION_LISTE_AFFILIE,
                    JadeDateUtil.getGlobazFormattedDate(new Date()));
            if (ALConstProtocoles.IMPRIMER_PROTOCOLE_CSV_PDF.equals(param.getValeurAlphaParametre())
                    || ALConstProtocoles.IMPRIMER_PROTOCOLE_CSV.equals(param.getValeurAlphaParametre())) {
                return ALImplServiceLocator.getProtocoleCSVCompensation().getCSV(recapsByActivites, params);
            } else {
                return null;
            }
        } catch (ParamException e) {
            // si le paramètre n'existe pas c'est qu'on ne doit pas générer ce protocole
            return null;
        } catch (JadeApplicationException e) {
            throw new ALProtocoleException(
                    "CompensationFactureProtocolesServiceImpl#getProtocoleCSVListeAffilie : Impossible de générer le fichier CSV",
                    e);
        }
    }

}
