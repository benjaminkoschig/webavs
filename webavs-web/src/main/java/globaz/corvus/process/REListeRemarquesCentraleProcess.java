package globaz.corvus.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.common.Jade;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.prestation.tools.PRDateFormater;

/**
 * Process pour la recherche des remarques de la centrale
 *
 * @author ESVE | Créé le 26 août 2020
 *
 */
public class REListeRemarquesCentraleProcess extends BProcess {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    private static final long serialVersionUID = 1L;

    // ~ Instance fields
    // --------------------------------------------------------------------------------------------------
    private String moisAnnee = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    public REListeRemarquesCentraleProcess() {
        super();
    }

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {

            // Appel a un jar externe pour générer les fichiers de remarques de la centrale
            String[] arguments = {JadePersistenceUtil.getDbSchema(), PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(getMoisAnnee()), Jade.getInstance().getHomeDir() + "work/"};
            /*MainRentes.main(getSession().getCurrentThreadTransaction().getConnection(), arguments);

            if (MainRentes.isDocumentAdaptationManuelleGenerated) {
                JadePublishDocumentInfo info1 = createDocumentInfo();
                info1.setPublishDocument(false);
                this.registerAttachedDocument(info1, MainRentes.EMPLACEMENT_DOCUMENT_ADAPTATION_MANUELLE);

                JadePublishDocumentInfo info2 = createDocumentInfo();
                info2.setPublishDocument(false);
                this.registerAttachedDocument(info2, MainRentes.EMPLACEMENT_DOCUMENT_CHANGEMENT_NSS);

                // Envoi email avec pdf ListeChangementNSS et ListeAdaptationManuelle en fichier joint
                JadeSmtpClient.getInstance().sendMail(getSession().getUserEMail(), getEMailObject(), this.getMemoryLog().getMessagesInHtml(), new String[]{MainRentes.EMPLACEMENT_DOCUMENT_ADAPTATION_MANUELLE, MainRentes.EMPLACEMENT_DOCUMENT_CHANGEMENT_NSS});

            } else if (MainRentes.isDocumentChangementNSSGenerated) {

                JadePublishDocumentInfo info2 = createDocumentInfo();
                info2.setPublishDocument(false);
                this.registerAttachedDocument(info2, MainRentes.EMPLACEMENT_DOCUMENT_CHANGEMENT_NSS);

                // Envoi email avec pdf ListeChangementNSS en fichier joint
                JadeSmtpClient.getInstance().sendMail(getSession().getUserEMail(), getEMailObject(), this.getMemoryLog().getMessagesInHtml(), new String[]{MainRentes.EMPLACEMENT_DOCUMENT_CHANGEMENT_NSS});
            }*/

            return true;

        } catch (Exception ex) {
            getAttachedDocuments().clear();
            getMemoryLog().logMessage("Erreur dans le traitement des remarques de la centrale : " + ex.toString(), FWMessage.ERREUR, this.getClass().getName());
            return false;
        }
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("PROCESS_GENERAL_ADAPTATION_TITRE_MAIL") + " - "
                + getSession().getLabel("PROCESS_LISTE_REM_CEN_OBJET_MAIL");
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

}
