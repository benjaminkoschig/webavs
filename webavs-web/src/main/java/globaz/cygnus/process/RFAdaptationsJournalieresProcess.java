package globaz.cygnus.process;

import globaz.cygnus.application.RFApplication;
import globaz.cygnus.exceptions.RFXmlmlException;
import globaz.cygnus.mappingXmlml.IRFAdaptationsJournalieresListeColumns;
import globaz.cygnus.mappingXmlml.RFXmlmlAdaptationJournaliereBean;
import globaz.cygnus.mappingXmlml.RFXmlmlMappingLogAdaptationJournaliere;
import globaz.cygnus.services.adaptationJournaliere.RFAdaptationJournaliereService;
import globaz.cygnus.utils.RFExcelmlUtils;
import globaz.cygnus.utils.RFXmlmlContainer;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeError;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.properties.CommonProperties;

/**
 * Crée et met à jour les Qds impactées par les décisions PC récemment validées
 * 
 * @author JJE
 */
public class RFAdaptationsJournalieresProcess extends BProcess {

    private static final long serialVersionUID = 1L;
    private String idGestionnaire = "";
    private boolean ajouterCommunePolitique = false;

    public RFAdaptationsJournalieresProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    public boolean _executeProcess() {

        try {

            setAjouterCommunePolitique(CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue());

            // Traitement des nouvelles adaptations
            RFAdaptationJournaliereService rfAdaJouSer = new RFAdaptationJournaliereService(getIdGestionnaire(),
                    getSession(), getTransaction());

            rfAdaJouSer.setIdGestionnaire(idGestionnaire);

            rfAdaJouSer.rechercherAdaptations();

            if (!islogEnErreur(rfAdaJouSer.getLogsList())) {
                rfAdaJouSer.executerAdaptations();
            }

            List<String[]> logList = ajouterCommunePolitique(rfAdaJouSer.getLogsList());

            createDocument(logList, rfAdaJouSer.getDates());

            return true;

        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
            e.printStackTrace();
            getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR,
                    "RFAdaptationsJournalieresProcess._executeProcess()");
            return false;
        } finally {
            if (getTransaction() != null) {
                try {
                    getTransaction().closeTransaction();
                } catch (Exception e) {
                    throw new JadeError(e);
                }
            }
        }
    }

    private List<RFXmlmlAdaptationJournaliereBean> convertAsListBean(List<String[]> logsList) {
        List<RFXmlmlAdaptationJournaliereBean> log = new ArrayList<RFXmlmlAdaptationJournaliereBean>();

        for (String[] logCourant : logsList) {
            log.add(new RFXmlmlAdaptationJournaliereBean(logCourant[0], logCourant[1], logCourant[2], logCourant[3],
                    logCourant[4], logCourant[5], logCourant[6]));
        }

        return log;
    }

    private List<String[]> convertAsListTabString(List<RFXmlmlAdaptationJournaliereBean> logsList) {
        List<String[]> log = new ArrayList<String[]>();

        for (RFXmlmlAdaptationJournaliereBean logCourant : logsList) {
            log.add(new String[] { logCourant.getTypeDeMessage(), logCourant.getIdAdaptationJournaliere(),
                    logCourant.getIdTiersBeneficiaire(), logCourant.getNss(), logCourant.getMsgErreur(),
                    logCourant.getIdDecisionPc(), logCourant.getNumDecisionPc(), logCourant.getCommunePolitique() });
        }

        return log;
    }

    private List<String[]> ajouterCommunePolitique(List<String[]> logList) {

        if (!isAjouterCommunePolitique()) {
            return logList;
        }

        List<RFXmlmlAdaptationJournaliereBean> log = convertAsListBean(logList);

        BITransaction transaction;
        try {
            transaction = getSession().newTransaction();

            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            Set<String> setIdTiers = new HashSet<String>();

            for (RFXmlmlAdaptationJournaliereBean logCourant : log) {
                if (!JadeStringUtil.isEmpty(logCourant.getIdTiersBeneficiaire())) {
                    setIdTiers.add(logCourant.getIdTiersBeneficiaire());
                }
            }

            if (setIdTiers.size() > 0) {
                Map<String, String> mapCommuneParIdTiers = PRTiersHelper.getCommunePolitique(setIdTiers, new Date(),
                        getSession());

                for (RFXmlmlAdaptationJournaliereBean logCourant : log) {
                    if (!JadeStringUtil.isEmpty(logCourant.getIdTiersBeneficiaire())) {
                        logCourant.setCommunePolitique(mapCommuneParIdTiers.get(logCourant.getIdTiersBeneficiaire()));
                    }
                }

                Collections.sort(log);
            }
        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
            getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR,
                    "RFAdaptationsJournalieresProcess.ajouterCommunePolitique()");

            return logList;
        } finally {
            if (getTransaction() != null) {
                try {
                    getTransaction().closeTransaction();
                } catch (Exception e) {
                    throw new JadeError(e);
                }
            }
        }

        return convertAsListTabString(log);
    }

    private boolean createDocument(List<String[]> logsList, List<String> dates) throws RFXmlmlException, Exception {

        setProgressScaleValue(logsList.size());
        RFXmlmlContainer container = RFXmlmlMappingLogAdaptationJournaliere.loadResults(logsList, dates, this);

        if (isAborted()) {
            return false;
        }

        String nomDoc = getSession().getLabel("PROCESS_ADAPTATION_JOURNALIERE");
        String docPath = "";
        if (isAjouterCommunePolitique()) {
            docPath = RFExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                    + IRFAdaptationsJournalieresListeColumns.MODEL_COMMUNE_POLITIQUE_NAME, nomDoc, container);
        } else {
            docPath = RFExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                    + IRFAdaptationsJournalieresListeColumns.MODEL_NAME, nomDoc, container);
        }

        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(RFApplication.DEFAULT_APPLICATION_CYGNUS);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        this.registerAttachedDocument(docInfo, docPath);

        return true;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("PROCESS_ADAPTATION_JOURNALIERE");
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    private boolean islogEnErreur(List<String[]> logsList) {

        for (String[] logCourant : logsList) {

            if (logCourant != null) {
                if (logCourant[0].equals(FWViewBeanInterface.ERROR)) {
                    return true;
                }
            } else {
                return false;
            }

        }

        return false;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public boolean isAjouterCommunePolitique() {
        return ajouterCommunePolitique;
    }

    public void setAjouterCommunePolitique(boolean ajouterCommunePolitique) {
        this.ajouterCommunePolitique = ajouterCommunePolitique;
    }

}
