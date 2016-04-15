package globaz.al.process.decision;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.common.Jade;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.client.JadePublishServerFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.message.JadePublishDocumentMessage;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;

public class ALDecisionsMasseListeProcess extends ALDecisionsMasseAbstractProcess {
    private static final Logger LOG = LoggerFactory.getLogger(ALDecisionsMasseListeProcess.class);
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void process() {

        try {
            serviceDecision = ALServiceLocator.getDecisionBuilderService();
            serviceDossier = ALServiceLocator.getDossierComplexModelService();

            printListCSV();

        } catch (Exception e) {
            getLogSession().error(this.getClass().getName(),
                    JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                            "globaz.al.process.generic.err_process") + e.getMessage());
            LOG.error(this.getClass().getName(), JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                    "globaz.al.process.generic.err_process") + e.getMessage());
        } finally {
            sendMail();
        }
    }

    @Override
    public String getName() {
        return JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                "globaz.al.process.decision.ALDecisionMasseListeProcess.name");
    }

    @Override
    public String getDescription() {
        // TODO labelisation
        return JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                "globaz.al.process.ALDecisionsMasseListeProcess.description");
    }

    /**
     * Lance la génération et l'envoi par email d'une liste csv des dossiers concernés par les critères sélectionnés
     *
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     * @throws JadeClassCastException
     */
    private void printListCSV() throws JadePersistenceException, JadeApplicationException, JadeServiceLocatorException,
            JadeServiceActivatorException, JadeClassCastException {

        JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();
        pubInfo.setOwnerId(JadeThread.currentUserId());
        pubInfo.setOwnerEmail(getEmail());
        String date = JadeDateUtil.getGlobazFormattedDate(new Date());
        pubInfo.setDocumentTitle(JadeThread.getMessage("al.protocoles.listeDecisionsMasse.mail.title") + date);
        pubInfo.setDocumentSubject(JadeThread.getMessage("al.protocoles.listeDecisionsMasse.mail.title") + date);
        pubInfo.setDocumentDate(date);

        String fileName = JadeFilenameUtil.addOrReplaceFilenameSuffixUID("listeCSV.csv");
        String filePath = getModuleWorkDir() + File.separatorChar + fileName;

        try {
            List<DossierComplexModel> listDossiers = getDossierOrderedFiltered();
            byte[] data = serviceDecision.getListeDossiersCSV(listDossiers, getParam()).getBytes();

            File file = new File(filePath);
            FileUtils.writeByteArrayToFile(file, data);

            getProgressHelper().setCurrent(getProgressHelper().getCurrent() + listDossiers.size());

            JadePublishDocument docInfoCSV = new JadePublishDocument(fileName, pubInfo);
            docInfoCSV.setDocumentLocation(file.getAbsolutePath());

            JadePublishServerFacade.publishDocument(new JadePublishDocumentMessage(docInfoCSV));
        } catch (IOException e) {
            LOG.error("printListCSV writing csv to " + fileName, e);
        }
    }

    /**
     * Retourne le chemin vers le dossier 'work' de ce module pour la génération de fichier. Le 'slash/anti-slash' de
     * fin n'est pas inséré
     *
     * @return le chemin vers le dossier 'work' de ce module pour la génération de fichier. Le 'slash/anti-slash' de fin
     *         n'est pas inséré
     */
    private String getModuleWorkDir() {
        String outPath = "work";
        outPath = getSession().getApplicationId().toLowerCase() + "Root" + File.separatorChar + outPath;
        return Jade.getInstance().getHomeDir() + outPath;
    }

}
