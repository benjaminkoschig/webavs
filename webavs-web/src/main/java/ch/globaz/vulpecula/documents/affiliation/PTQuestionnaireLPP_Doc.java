package ch.globaz.vulpecula.documents.affiliation;

import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.naos.application.AFApplication;
import globaz.naos.itext.suiviLPP.AFQuestionnaireLPP_Doc;
import ch.globaz.vulpecula.application.ApplicationConstants;
import ch.globaz.vulpecula.web.application.PTApplication;

/**
 * AGE (Disparu le 10.02.2017)
 * Cette classe est très mauvaise idée en terme de réalisation.
 * Nous héritons de AFQuestionnaireLPP dont le constructeur va démarrer une session NAOS que nous overriderons avec une
 * session VULPECULA.
 * 
 * @author age
 * 
 */
public class PTQuestionnaireLPP_Doc extends AFQuestionnaireLPP_Doc {
    private static final long serialVersionUID = -7486022557102659488L;

    private String modelDe = "NAOS_QUESTIONNAIRE_LPP_VERSO_DE";
    private String modelFr = "NAOS_QUESTIONNAIRE_LPP_VERSO_FR";

    private String modelPath = "vulpeculaRoot/model/static/";

    public PTQuestionnaireLPP_Doc() throws Exception {
        super();
        setDocumentRoot(ApplicationConstants.APPLICATION_VULPECULA_REP);
    }

    public PTQuestionnaireLPP_Doc(BSession session) throws Exception {
        super(session);
        setDocumentRoot(AFApplication.DEFAULT_APPLICATION_NAOS_REP);
        setSession((BSession) GlobazSystem.getApplication(PTApplication.DEFAULT_APPLICATION_VULPECULA).newSession(
                session));
    }

    @Override
    public void afterBuildReport() {
        FWIImportManager m = new FWIImportManager();
        try {
            if ("DE".equals(getIsoLangueDestinataire())) {
                addDocument(m.importReport(modelDe, Jade.getInstance().getExternalModelDir() + modelPath));
            } else {
                addDocument(m.importReport(modelFr, Jade.getInstance().getExternalModelDir() + modelPath));
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }
}
