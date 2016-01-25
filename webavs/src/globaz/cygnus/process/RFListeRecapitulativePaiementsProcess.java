/*
 * Créé le 09 mai 2012
 */
package globaz.cygnus.process;

import globaz.cygnus.application.RFApplication;
import globaz.cygnus.exceptions.RFXmlmlException;
import globaz.cygnus.services.RFListeRecapitulativePaiementsService;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.common.JadeError;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.prestation.process.PRAbstractProcess;
import java.util.List;

/**
 * @author MBO
 */
public class RFListeRecapitulativePaiementsProcess extends PRAbstractProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresseMail = null;
    private String datePeriode = null;

    public RFListeRecapitulativePaiementsProcess() {
        super();

        setThreadContextInitialized(true);
    }

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    private boolean createDocument(List<String[]> logsList, List<String> dates) throws RFXmlmlException, Exception {

        setProgressScaleValue(logsList.size());

        if (isAborted()) {
            return false;
        }

        String nomDoc = getSession().getLabel("PROCESS_LISTE_RECAP_PAIEMENTS");

        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(RFApplication.DEFAULT_APPLICATION_CYGNUS);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);

        return true;
    }

    public String getAdresseMail() {
        return adresseMail;
    }

    public String getDatePeriode() {
        return datePeriode;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("PROCESS_LISTE_RECAP_PAIEMENTS");
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    @Override
    public boolean runProcess() {

        try {

            RFListeRecapitulativePaiementsService rfListeRecapPaiSer = new RFListeRecapitulativePaiementsService(
                    adresseMail, getSession(), getTransaction(), this);

            rfListeRecapPaiSer.setDatePeriode(datePeriode);
            rfListeRecapPaiSer.setAdresseMail(adresseMail);
            rfListeRecapPaiSer.setSession(getSession());
            rfListeRecapPaiSer.executerListeRecapitulativePaiements();

            return true;

        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
            e.printStackTrace();
            getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR,
                    "RFListeRecapitulativePaiementsProcess._executeProcess()");
            return false;
        } finally {
            if (getTransaction() != null) {
                try {
                    getTransaction().closeTransaction();
                } catch (Exception e) {
                    throw new JadeError(e);
                }
            }

            if (getMemoryLog().hasErrors()) {
                try {
                    JadeSmtpClient.getInstance().sendMail(adresseMail,
                            getSession().getLabel("PROCESS_SUBJECT_ERREUR_AUCUNE_RECAP_POUR_CE_MOIS"),
                            getMemoryLog().getMessagesInString(), null);
                } catch (Exception e) {
                    throw new JadeError(e);
                }
            }
        }
    }

    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    public void setDatePeriode(String datePeriode) {
        this.datePeriode = datePeriode;
    }
}
