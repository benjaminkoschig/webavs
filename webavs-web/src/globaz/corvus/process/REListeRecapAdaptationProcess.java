package globaz.corvus.process;

import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.excel.REListeRecapitulationAdaptation;
import globaz.corvus.itext.REListeRecapitulativePaiement;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfo;

/**
 * @author HPE
 */
public class REListeRecapAdaptationProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String moisAnnee;

    public REListeRecapAdaptationProcess() {
        super();

        moisAnnee = "";
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        try {

            JadePublishDocumentInfo info = createDocumentInfo();
            info.setPublishDocument(true);
            info.setArchiveDocument(false);
            info.setDocumentTypeNumber(IRENoDocumentInfoRom.ADAPTATION_LISTE_RECAPITULATIVE_ADAPTATION);

            // Création de la liste récapitulative de l'adaptation
            REListeRecapitulationAdaptation lstRecapAdaptation = new REListeRecapitulationAdaptation(getSession());
            lstRecapAdaptation.setMoisAnnee(getMoisAnnee());
            lstRecapAdaptation.populateSheetListe(getTransaction());

            // attachement du fichier de sortie au mail
            this.registerAttachedDocument(info, lstRecapAdaptation.getOutputFile());

            // Impression de la liste récapitulative des paiements pour le mois d'après
            REListeRecapitulativePaiement listeRecapitulativePaiement = new REListeRecapitulativePaiement();
            listeRecapitulativePaiement.setForMoisAnnee(getMoisAnnee());
            listeRecapitulativePaiement.setTransaction(getTransaction());
            listeRecapitulativePaiement.setParentWithCopy(this);
            listeRecapitulativePaiement.executeProcess();

            info = createDocumentInfo();
            info.setPublishDocument(true);
            info.setArchiveDocument(false);

            this.mergePDF(info, true, 500, false, null);

            return true;

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "REListeRecapAdaptationProcess");
            return false;
        }
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("PROCESS_GENERAL_ADAPTATION_TITRE_MAIL") + " - "
                + getSession().getLabel("PROCESS_LISTE_RECAP_ADA_OBJET_MAIL");
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }
}
