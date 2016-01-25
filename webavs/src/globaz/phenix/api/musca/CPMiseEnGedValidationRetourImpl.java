package globaz.phenix.api.musca;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.phenix.documentsItext.CPImpressionCommunicationRetour_Doc;
import java.util.Iterator;
import java.util.List;

/**
 * Insérez la description du type ici. Date de création : (24.04.2003 12:51:01)
 * 
 * @author: btc
 */
public class CPMiseEnGedValidationRetourImpl extends CPFacturationGenericImpl implements IntModuleFacturation {
    /**
     * Commentaire relatif au constructeur FAListGenericImpl.
     */
    public CPMiseEnGedValidationRetourImpl() {
        super();
    }

    @Override
    public boolean avantRegenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return true;
    }

    @Override
    public boolean avantRepriseErrGen(IFAPassage passage, BProcess context, String idModuleFacturation)
            throws Exception {
        return true;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.04.2003 08:52:52)
     */
    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        // copier le process parent
        BSession sessionPhenix = new globaz.globall.db.BSession(
                globaz.phenix.application.CPApplication.DEFAULT_APPLICATION_PHENIX);
        context.getSession().connectSession(sessionPhenix);

        CPImpressionCommunicationRetour_Doc decision = new CPImpressionCommunicationRetour_Doc();
        decision.setSession(sessionPhenix);
        decision.setSendCompletionMail(false);
        decision.setSendMailOnError(false);
        decision.setDeleteOnExit(true);
        decision.setForIdPassage(passage.getIdPassage());
        decision.setWantMiseEnGEd(Boolean.TRUE);
        decision.executeProcess();
        if ((context != null) && decision.hasAttachedDocuments()) {
            List<?> e = decision.getAttachedDocuments();
            for (Iterator<?> iter = e.iterator(); iter.hasNext();) {
                JadePublishDocument doc = (JadePublishDocument) iter.next();
                context.registerAttachedDocument(doc.getPublishJobDefinition().getDocumentInfo(),
                        doc.getDocumentLocation());
            }
        }
        context.getMemoryLog().logMessage(decision.getMemoryLog());
        // contrôler si le process a fonctionné
        if (!decision.isAborted() && !context.getTransaction().hasErrors()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        // copier le process parent
        BSession sessionPhenix = new globaz.globall.db.BSession(
                globaz.phenix.application.CPApplication.DEFAULT_APPLICATION_PHENIX);
        context.getSession().connectSession(sessionPhenix);

        CPImpressionCommunicationRetour_Doc decision = new CPImpressionCommunicationRetour_Doc();
        decision.setSession(sessionPhenix);
        decision.setSendCompletionMail(false);
        decision.setSendMailOnError(false);
        decision.setDeleteOnExit(true);
        decision.setForIdPassage(passage.getIdPassage());
        decision.setWantMiseEnGEd(Boolean.TRUE);
        decision.executeProcess();
        if ((context != null) && decision.hasAttachedDocuments()) {
            List<?> e = decision.getAttachedDocuments();
            for (Iterator<?> iter = e.iterator(); iter.hasNext();) {
                JadePublishDocument doc = (JadePublishDocument) iter.next();
                context.registerAttachedDocument(doc.getPublishJobDefinition().getDocumentInfo(),
                        doc.getDocumentLocation());
            }
        }
        context.getMemoryLog().logMessage(decision.getMemoryLog());
        // contrôler si le process a fonctionné
        if (!decision.isAborted() && !context.getTransaction().hasErrors()) {
            return true;
        } else {
            return false;
        }
    }

    protected Boolean getGed() throws Exception {
        BSession sessionMusca = new globaz.globall.db.BSession(
                globaz.musca.application.FAApplication.DEFAULT_APPLICATION_MUSCA);
        if ("true".equals((sessionMusca.getApplication()).getProperty("mettreGed"))) {
            return (Boolean.TRUE);
        } else {
            return (Boolean.FALSE);
        }
    }

    protected String getOrder() {
        return null;
    }

    @Override
    public boolean regenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return true;
    }

    @Override
    public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception {
        return comptabiliser(passage, context);
    }

}
