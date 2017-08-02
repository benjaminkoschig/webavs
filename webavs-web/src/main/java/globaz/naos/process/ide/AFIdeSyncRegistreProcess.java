package globaz.naos.process.ide;

import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.ide.AFIdeAffiliationManager;
import globaz.naos.properties.AFProperties;
import globaz.naos.util.AFIDEUtil;
import globaz.naos.util.IDEDataBean;
import globaz.naos.util.IDEServiceCallUtil;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.WebServiceException;
import org.apache.commons.io.IOUtils;
import ch.globaz.naos.exception.NaosException;

public class AFIdeSyncRegistreProcess extends BProcess {

    private static final long serialVersionUID = 3679015353882576640L;

    public static final String NAME_CSV_FILE_RECAP_SYNCHRO_REGISTRE = "recapitulatifSynchroRegistre.csv";
    public static final String CODE_NOGA_INCONNU = "990099";
    private boolean modeForceAllStatus;

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        List<String> listCsvLine = new ArrayList<String>();
        timeMillis = System.currentTimeMillis();
        try {
            // identifier les Affiliation à traiter
            // boucler sur les affiliations concernées
            for (AFAffiliation aff : getListAffiliationAvecIDE()) {
                final StringBuilder errmsg = new StringBuilder();
                try {
                    // . interroger le webService
                    printTime("start");
                    IDEDataBean data = initInfoIDEPourRechercheTrouvee(getSession(), aff.getNumeroIDE());
                    printTime("interroger le webService");
                    // . traiter le retour
                    updateAffiliationAfterFind(data, aff);
                } catch (MalformedURLException mue) {
                    throw mue;
                } catch (com.sun.xml.internal.ws.client.ClientTransportException cte) {
                    errmsg.append(cte.getMessage());
                } catch (WebServiceException wse) {
                    throw wse;
                } catch (NaosException e) {
                    errmsg.append(e.getMessage());
                } catch (Exception e) {
                    errmsg.append(e.getMessage());
                } finally {
                    // . persister info
                    // . persister numero IDE complet non ponctué
                    errmsg.append(handleTransaction(aff.getAffiliationId()));
                    // . logger/remplir CSV
                    printTime("persister transac");
                    listCsvLine.add(createCsvBodyLines(aff, errmsg.toString()));
                    incProgressCounter();
                }
                printTime("fin iter");
                if (isAborted()) {
                    throw new Exception("Process Aborded");
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    getSession().getLabel("NAOS_PROCESS_IDE_SYNCHRO_REGISTRE_PROBLEME_FATAL") + " " + e.toString(),
                    FWMessage.FATAL, this.getClass().getName());
        } finally {
            // envoyer le csv de rapport
            genererCsvRecapSynchroRegistre(listCsvLine);
        }
        return !(isAborted() || isOnError() || getSession().hasErrors());

    }

    /**
     * Recherche du numéro IDE sur le registre
     * 
     * @return l'ideDataBean si n° trouvé unique
     * @throws Exception si pas ou plus d'un numéro correspond a la recherche
     */
    private IDEDataBean initInfoIDEPourRechercheTrouvee(BSession session, String numeroIDE)
            throws MalformedURLException, Exception {
        final List<IDEDataBean> rechercheIDE = IDEServiceCallUtil.searchForNumeroIDE(numeroIDE, session);
        if (rechercheIDE.size() == 1) {
            return rechercheIDE.get(0);
        } else {
            throw new Exception(session.getLabel("NAOS_PROCESS_IDE_SYNCHRO_REGISTRE_PROBLEME_IDE_INCONNU"));
        }
    }

    /**
     * commit de la transaction de manière atomique
     * 
     * @param idAffiliation just pour le logger
     * @return message d'erreur sur la persistence de l'aff
     * @throws Exception erreur db
     */
    private String handleTransaction(String idAffiliation) throws Exception {
        String error = "";
        if (isOnError() || isAborted() || getTransaction().hasErrors() || getTransaction().hasWarnings()
                || getSession().hasErrors() || getSession().hasWarnings()) {
            logTechnicalErrorMessage(idAffiliation);
            getTransaction().rollback();
            error = getTransaction().getErrors().toString();
        } else {
            getTransaction().commit();
        }
        viderErreurProcess();
        return error;
    }

    private void viderErreurProcess() {
        getMemoryLog().clear();
        getSession().getErrors();
        getSession().getWarnings();
        getTransaction().clearErrorBuffer();
        getTransaction().clearWarningBuffer();
    }

    /**
     * update de l'aff sur les donnée de l'ide sans provoquer d'annonces
     * 
     * @param ideDataBean
     * @param affiliation
     * @throws Exception
     */
    private void updateAffiliationAfterFind(IDEDataBean ideDataBean, AFAffiliation affiliation) throws Exception {
        String idCodeNoga = null;

        affiliation.setNumeroIDE(ideDataBean.getNumeroIDE());
        affiliation.setIdeRaisonSociale(ideDataBean.getRaisonSociale());
        affiliation.setIdeStatut(ideDataBean.getStatut());

        if (!AFProperties.NOGA_SYNCHRO_REGISTRE.getValue().isEmpty()
                && AFProperties.NOGA_SYNCHRO_REGISTRE.getValue() != null
                && AFProperties.NOGA_SYNCHRO_REGISTRE.getBooleanValue()) {
            idCodeNoga = getidCodePourCodeNoga(ideDataBean.getNogaCode());
            affiliation.setCodeNoga(idCodeNoga);
        }

        affiliation.setRulesByPass(true);
        AFAffiliationUtil.disableExtraProcessingForAffiliation(affiliation);
        affiliation.update(getTransaction());

        // On throw une erreur que lorsque le code NOGA n'a pas été trouvé, si = null ça veut dire que la propriété est
        // à false et donc on remonte pas d'erreur
        if ("0".equals(idCodeNoga)) {
            if (CODE_NOGA_INCONNU.equals(ideDataBean.getNogaCode())) {
                throw new NaosException(
                        FWMessageFormat.format(
                                getSession().getApplication().getLabel("NAOS_CODE_NOGA_INCONNU",
                                        getSession().getIdLangueISO()), ideDataBean.getNogaCode()));
            } else {
                throw new NaosException(FWMessageFormat.format(
                        getSession().getApplication()
                                .getLabel("NAOS_CODE_NOGA_INDEFINI", getSession().getIdLangueISO()), ideDataBean
                                .getNogaCode()));
            }
        }
    }

    /***
     * Méthode qui permet de récupérer l'id d'un code noga
     * 
     * @param ideDataBean
     * @throws Exception
     */
    private String getidCodePourCodeNoga(String codeNoga) throws Exception {
        FWParametersSystemCodeManager param = new FWParametersSystemCodeManager();

        param.setSession(getSession());
        param.setForCodeUtilisateur(codeNoga);

        param.find(BManager.SIZE_NOLIMIT);

        if (param.size() == 0) {
            return "0";
        } else {
            return ((FWParametersSystemCode) param.getFirstEntity()).getIdCode();
        }
    }

    /**
     * appel le manager pour ne récupérer que les affiliation ayant un n° IDE
     * 
     * @return list d'AFAffiliation
     * @throws Exception
     */
    private List<AFAffiliation> getListAffiliationAvecIDE() throws Exception {

        Boolean isUpdateCodeNoga = false;
        if (AFProperties.NOGA_SYNCHRO_REGISTRE.getValue() != null
                && !AFProperties.NOGA_SYNCHRO_REGISTRE.getValue().isEmpty()
                && AFProperties.NOGA_SYNCHRO_REGISTRE.getBooleanValue()) {
            isUpdateCodeNoga = true;
        }

        AFIdeAffiliationManager manager = new AFIdeAffiliationManager();
        manager.setSession(getSession());
        manager.setModeForceAllStatus(isModeForceAllStatus());
        manager.setUpdateCodeNoga(isUpdateCodeNoga);
        manager.find(BManager.SIZE_NOLIMIT);
        List<AFAffiliation> listAffiliation = new ArrayList<AFAffiliation>();
        for (int i = 0; i < manager.size(); i++) {
            listAffiliation.add((AFAffiliation) manager.getEntity(i));
        }
        setProgressScaleValue(listAffiliation.size());
        return listAffiliation;
    }

    @Override
    protected void _validate() throws Exception {

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            getSession().addError(getSession().getLabel("NAOS_PROCESS_IDE_SYNCHRO_REGISTRE_MAIL_MANDATORY"));
            setSendCompletionMail(false);
            setSendMailOnError(false);
        }
    }

    @Override
    protected String getEMailObject() {

        if (isAborted() || isOnError() || getSession().hasErrors()) {
            return getSession().getLabel("NAOS_PROCESS_IDE_SYNCHRO_REGISTRE_ERROR");
        } else {
            return getSession().getLabel("NAOS_PROCESS_IDE_SYNCHRO_REGISTRE_SUCCESS");
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public boolean isModeForceAllStatus() {
        return modeForceAllStatus;
    }

    public void setModeForceAllStatus(Boolean modeForceAllStatus) {
        this.modeForceAllStatus = modeForceAllStatus.booleanValue();
    }

    // ****************************************
    // * CSV *
    // ****************************************
    private String creerCsvFile(String fileName, List<String> listCsvLine) throws JadeServiceLocatorException,
            JadeServiceActivatorException, NullPointerException, ClassCastException, JadeClassCastException {

        String filePath = Jade.getInstance().getPersistenceDir() + JadeFilenameUtil.addFilenameSuffixUID(fileName);

        JadeFsFacade.writeFile(getLignesInByteFormat(listCsvLine), filePath);

        return filePath;
    }

    private List<Byte> getLignesInByteFormat(List<String> lignes) {
        List<Byte> lignesInByteFormat = new ArrayList<Byte>();

        if (lignes != null) {
            for (String aLigne : lignes) {
                for (byte aByte : aLigne.getBytes()) {
                    lignesInByteFormat.add(Byte.valueOf(aByte));
                }
            }
        }

        return lignesInByteFormat;
    }

    private void publierCsvFile(String fileName, String filePath) throws Exception {

        // Publication du document
        JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
        docInfoExcel.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfoExcel.setDocumentTitle(fileName);
        docInfoExcel.setPublishDocument(true);
        docInfoExcel.setArchiveDocument(false);
        this.registerAttachedDocument(docInfoExcel, filePath);

    }

    private void genererCsvRecapSynchroRegistre(List<String> listCsvLine) throws Exception {

        listCsvLine.add(0, createCsvHeaderLine());

        String filePath = creerCsvFile(NAME_CSV_FILE_RECAP_SYNCHRO_REGISTRE, listCsvLine);

        publierCsvFile(NAME_CSV_FILE_RECAP_SYNCHRO_REGISTRE, filePath);

    }

    private String createCsvHeaderLine() {

        StringBuilder csvHeader = new StringBuilder();
        csvHeader.append(getSession().getLabel("NAOS_PROCESS_IDE_SYNCHRO_REGISTRE_CSV_HEADER_NUMERO_AFFILIE"));
        csvHeader.append(";");
        csvHeader.append(getSession().getLabel("NAOS_PROCESS_IDE_SYNCHRO_REGISTRE_CSV_HEADER_NUMERO_IDE"));
        csvHeader.append(";");
        csvHeader.append(getSession().getLabel("NAOS_PROCESS_IDE_SYNCHRO_REGISTRE_CSV_HEADER_STATUT_IDE"));
        csvHeader.append(";");
        csvHeader.append(getSession().getLabel("NAOS_PROCESS_IDE_SYNCHRO_REGISTRE_CSV_HEADER_ETAT_MAJ"));
        csvHeader.append(";");
        csvHeader.append(getSession().getLabel("NAOS_PROCESS_IDE_SYNCHRO_REGISTRE_CSV_HEADER_MESSAGE_ERREUR"));
        csvHeader.append(";");
        csvHeader.append(getSession().getLabel(
                "NAOS_PROCESS_IDE_SYNCHRO_REGISTRE_CSV_HEADER_RAISON_SOCIALE_AFFILIATION"));
        csvHeader.append(";");
        csvHeader.append(getSession().getLabel("NAOS_PROCESS_IDE_SYNCHRO_REGISTRE_CSV_HEADER_RAISON_SOCIALE_IDE"));
        csvHeader.append(";");

        csvHeader.append(IOUtils.LINE_SEPARATOR);

        return csvHeader.toString();

    }

    private String createCsvBodyLines(AFAffiliation affilie, String errmsg) throws Exception {

        StringBuilder aCsvLine = new StringBuilder();

        aCsvLine.append("=" + '"' + affilie.getAffilieNumero() + '"');
        aCsvLine.append(";");
        aCsvLine.append(AFIDEUtil.giveMeNumIdeFormatedWithPrefix(affilie.getNumeroIDE()));
        aCsvLine.append(";");
        aCsvLine.append(getSession().getCodeLibelle(affilie.getIdeStatut()));
        aCsvLine.append(";");
        aCsvLine.append(JadeStringUtil.isBlankOrZero(errmsg) ? (getSession()
                .getLabel("NAOS_PROCESS_IDE_SYNCHRO_REGISTRE_CSV_HEADER_ETAT_MAJ_TRAITE")) : (getSession()
                .getLabel("NAOS_PROCESS_IDE_SYNCHRO_REGISTRE_CSV_HEADER_ETAT_MAJ_ERREUR")));
        aCsvLine.append(";");
        aCsvLine.append(errmsg.replaceAll("\\r|\\n", ""));
        aCsvLine.append(";");
        aCsvLine.append(affilie.getRaisonSocialeCourt());
        aCsvLine.append(";");
        aCsvLine.append(affilie.getIdeRaisonSociale());
        aCsvLine.append(";");

        aCsvLine.append(IOUtils.LINE_SEPARATOR);

        return aCsvLine.toString();

    }

    // ***********
    // * Logging *
    // ***********
    /**
     * time in millis for logging differential time
     */
    private static Long timeMillis;

    /**
     * @param idAffiliation
     * @deprecated because sysout is dummy, use proper logger and then remove deprecated
     * @return
     */
    @Deprecated
    private String logTechnicalErrorMessage(String idAffiliation) {

        StringBuffer technicalError = new StringBuffer("Technical error with affiliation " + idAffiliation + "\n");

        technicalError.append("Transaction errors : " + getTransaction().getErrors() + "\n");
        technicalError.append("Transaction warnings : " + getTransaction().getWarnings() + "\n");
        technicalError.append("Session errors : " + getSession().getErrors() + "\n");
        technicalError.append("Session warnings : " + getSession().getWarnings() + "\n");
        technicalError.append("Memory Log (only fatal errors make rollbak) : " + getMemoryLog().getMessagesInString()
                + "\n");
        if (isAborted()) {
            technicalError.append("Aborted" + "\n");
        }
        // TODO mettre sur un systeme de logging propre (logback) avant de reactiver
        // System.out.println(technicalError.toString());

        return technicalError.toString();

    }

    /**
     * log le temps depuis le dernier call
     * 
     * @param info texte à afficher dans le log
     * @deprecated because sysout is dummy, use proper logger and then remove deprecated
     */
    @Deprecated
    private static void printTime(String info) {
        // TODO mettre sur un systeme de logging propre (logback) avant de réactiver
        // System.out.println(info + " : " + (System.currentTimeMillis() - timeMillis) + "ms ");
        timeMillis = System.currentTimeMillis();
    }

}
