/*
 * Créé le 20 janvier 2011
 */
package globaz.cygnus.process;

import globaz.cygnus.application.RFApplication;
import globaz.cygnus.exceptions.RFXmlmlException;
import globaz.cygnus.services.statistiques.RFStatistiquesParMontantsSashService;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.common.JadeError;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.List;

/**
 * 
 * @author MBO
 */
public class RFStatistiquesParMontantsSashProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresseMail = null;
    private String dateDebutStat = null;
    private String dateFinStat = null;
    private String gestionnaire = "";

    /**
     * Crée une nouvelle instance de la classe RFStatistiquesMontantsProcess.
     */
    public RFStatistiquesParMontantsSashProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean _executeProcess() {

        try {

            // Traitement des nouvelles adaptations
            RFStatistiquesParMontantsSashService rfStatMontSer = new RFStatistiquesParMontantsSashService(
                    getGestionnaire(), getSession(), getTransaction(), this);

            rfStatMontSer.setGestionnaire(gestionnaire);
            rfStatMontSer.setDateDebutStat(dateDebutStat);
            rfStatMontSer.setDateFinStat(dateFinStat);
            rfStatMontSer.setAdresseMail(adresseMail);
            rfStatMontSer.setSession(getSession());
            rfStatMontSer.executerStatistiquesParMontantSash();

            return true;

        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
            e.printStackTrace();
            getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR,
                    "RFStatistiquesMontantsProcess._executeProcess()");
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

    private boolean createDocument(List<String[]> logsList, List<String> dates) throws RFXmlmlException, Exception {

        setProgressScaleValue(logsList.size());

        if (isAborted()) {
            return false;
        }

        String nomDoc = getSession().getLabel("PROCESS_ECHEC_STATISTIQUES_PAR_MONTANTS_SASH");

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

    public String getDateDebutStat() {
        return dateDebutStat;
    }

    public String getDateFinStat() {
        return dateFinStat;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("PROCESS_ECHEC_STATISTIQUES_PAR_MONTANTS_SASH");
    }

    public String getGestionnaire() {
        return gestionnaire;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    public void setDateDebutStat(String dateDebutStat) {
        this.dateDebutStat = dateDebutStat;
    }

    public void setDateFinStat(String dateFinStat) {
        this.dateFinStat = dateFinStat;
    }

    public void setGestionnaire(String gestionnaire) {
        this.gestionnaire = gestionnaire;
    }

}
